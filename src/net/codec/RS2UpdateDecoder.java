package net.codec;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.buffer.ChannelBuffer;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import org.jboss.netty.handler.codec.frame.FrameDecoder;

import boa.update.UpdateServer;


public class RS2UpdateDecoder extends FrameDecoder {

    private LinkedList<Request> requests = new LinkedList<Request>();

    private static final ExecutorService updateService = Executors.newFixedThreadPool(1);

	@Override
	protected Object decode(ChannelHandlerContext ctx, final Channel channel, ChannelBuffer buffer) throws Exception {
		while (buffer.readableBytes() > 3) {
			int type = buffer.readByte() & 0xff;
			final int idx = buffer.readByte() & 0xff;
			final int file = buffer.readShort() & 0xffff;
			switch (type) {
				case 0: // non-urgent
					requests.add(new Request(idx, file));
					break;
				case 1: // urgent
					updateService.execute(new Runnable() {
						public void run() {
							try {
								//System.out.println("requesting "+file+" from "+idx);
								channel.write(UpdateServer.getRequest(idx, file));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					break;
				case 2: // clear requests
				case 3:
					requests.clear();
					break;
				case 4: // client error
					break;
			}
			while (requests.size() > 0) {
				Request request = (Request) requests.removeFirst();
				channel.write(UpdateServer.getRequest(request.getIdx(), request.getFile()));
			}
			return true;
		}
		return null;
	}
	
    private static class Request {

        private final int idx;
        private final int file;

        public Request(int idx, int file) {
            this.idx = idx;
            this.file = file;
        }

        public int getIdx() {
            return idx;
        }

        public int getFile() {
            return file;
        }
    }
}
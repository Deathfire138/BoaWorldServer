package boa.io.rs2client.protocol;

import java.util.logging.Logger;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class RS2ProtocolDecoder extends FrameDecoder {

	private static final Logger logger = Logger.getLogger(RS2ProtocolDecoder.class.getName());
	
	@Override
	@SuppressWarnings("unused")
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() > 1) {
			int protocolId = buffer.readByte() & 0xff;
			int hash = buffer.readByte() & 0xff;
			switch (protocolId) {
				case 15:
					if (buffer.readableBytes() > 2) {
						buffer.readByte();
						int version = buffer.readShort() & 0xffff;
						ByteBuffer out = ByteBuffer.allocateDirect(512);
						if (version == 498) {
							out.put((byte)0);
							channel.write((ByteBuffer)out.flip());
							channel.getPipeline().replace("decoder", "decoder", new RS2UpdateDecoder());
							return null;
						} else {
							out.put((byte)6);
							channel.write((ByteBuffer)out.flip());
							channel.close();
							return null;
						}
					}
					return null;
				case 14:
					ByteBuffer out = ByteBuffer.allocateDirect(9);
					long key = ((long) (java.lang.Math.random() * 99999999D) << 32) + (long) (java.lang.Math.random() * 99999999D);
					out.put((byte)0).putLong(key);
					channel.write((ByteBuffer)out.flip());
					channel.getPipeline().replace("decoder", "decoder", new RS2LoginDecoder());
					return null;
				default:
					return null;
			}
		} else if (buffer.readableBytes() > 0){
			int something = buffer.readByte() & 0xFF;
		} else {
		}
		return null;
	}
}
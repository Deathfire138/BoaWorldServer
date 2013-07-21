package net.codec;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import miscellaneous.Utils;
import net.Login;

import org.jboss.netty.buffer.ChannelBuffer;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class RS2LoginDecoder extends FrameDecoder {

	private static final Logger logger = Logger.getLogger(RS2LoginDecoder.class.getName());

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() > 2) {
			buffer.readByte();
			int size = buffer.readByte() & 0xff;
			if (size == buffer.readableBytes()) {
				int version = buffer.readInt();
				if (version == 498) {
					boolean isLowMemory = (buffer.readByte() & 0xFF) == 1;
					for(int i = 0; i < 24; i++) {
						buffer.readByte();
					}
					for(int i = 0; i < 27; i++) {
						buffer.readInt();
					}
					int reportedSize = buffer.readByte() & 0xff;
					if(reportedSize != 10) {
						buffer.readByte();
					}
					/*
					 * We read the client's session key.
					 */
					long clientKey = buffer.readLong();
					
					/*
					 * And verify it has the correct server session key.
					 */
					//long serverKey = (Long) session.getAttribute("serverKey");
					long reportedServerKey = buffer.readLong();
					//if(reportedServerKey != serverKey) {
						//logger.info("Server key mismatch (expected : " + serverKey + ", reported : " + reportedServerKey + ")");
						//session.close(false);
						//in.rewind();
						//return false;
					//}
					
					/*
					 * The UID, found in random.dat in newer clients and
					 * uid.dat in older clients is a way of identifying a
					 * computer.
					 * 
					 * However, some clients send a hardcoded or random UID,
					 * making it useless in the private server scene.
					 */
					//int uid = in.getInt();
/*					
					
					for (int i = 0; i < 4; i++) {
						buffer.readInt();
					}
					//long clientKey = buffer.readLong();
					int uid = buffer.readInt();
*/					
					long name = buffer.readLong();
					String username = Utils.longToPlayerName(name), password = Utils.getRS2String(buffer);
					logger.info("Username: "+username+", password: "+password);
					Login.login(channel, username, name, password, isLowMemory);
					//TODO Implement RSA/ISAAC
				}
			} else {
				//channel.write((ByteBuffer)ByteBuffer.allocate(1).put((byte) 22).flip());
			}
		}
		return null;
	}
}
package boa.io.central.codec;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import boa.Server;

import packet.Packet;

public class BoaEncoder extends OneToOneEncoder {

	private static final Logger logger = Logger.getLogger(BoaEncoder.class.getName());
	
	@Override
	protected Object encode(ChannelHandlerContext context, Channel channel, Object object) throws Exception {
		if (object instanceof Packet) {
			Packet packet = (Packet) object;
			return ChannelBuffers.copiedBuffer(packet.getBuffer());
		} else if (object instanceof ByteBuffer) {
			return ChannelBuffers.copiedBuffer((ByteBuffer) object);
		} else {
			if (Server.isDebugEnabled) {
				logger.warning("Data format not handled by encoder.");
			}
		}
		return null;
	}

}

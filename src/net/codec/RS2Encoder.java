package net.codec;

import java.nio.ByteBuffer;

import net.PacketBuilder;

import org.jboss.netty.buffer.ChannelBuffers;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class RS2Encoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
		if (message instanceof PacketBuilder) {
			PacketBuilder packet = (PacketBuilder) message;
			if (packet.getOpcode() == -1) {
				return ChannelBuffers.copiedBuffer((ByteBuffer) packet.getBuffer().flip());
			}
			ByteBuffer buffer = ByteBuffer.allocate(packet.getBuffer().position() + 3);
			buffer.put((byte)packet.getOpcode());
			switch (packet.getSize()) {
				case VAR_BYTE:
					buffer.put((byte) packet.getBuffer().position());
					break;
				case VAR_SHORT:
					buffer.putShort((short) packet.getBuffer().position());
					break;
			}
			buffer.put((ByteBuffer) packet.getBuffer().flip());
			return ChannelBuffers.copiedBuffer((ByteBuffer) buffer.flip());
		} else if (message instanceof ByteBuffer) {
			return ChannelBuffers.copiedBuffer((ByteBuffer) message);
		} else {
			System.out.println("Unhanlded packet type for encoder.");
			return null;
		}
	}
}

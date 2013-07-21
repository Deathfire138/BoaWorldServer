package net.codec;

import java.nio.ByteBuffer;

import model.Player;

import org.jboss.netty.buffer.ChannelBuffer;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import org.jboss.netty.handler.codec.frame.FrameDecoder;

import packet.Packet;
import packet.PacketManager;

public class RS2Decoder extends FrameDecoder {
	
	private static final byte[] PACKET_SIZES = new byte[] {
		   //0---1---2---3---4---5---6---7---8---9
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 0
			-3, -3, -3,  8, -3, -3, -3, -3, -3, -3, // 1
			-3, -3, -3, -3, -3, -3, -3, -3,  6, -3, // 2
			-3, -3,  1, -3, -3, -3, -3, -3, -3,  6, // 3
			-3, -3, -3, -3, -3, -3, -1,  8, -3, -3, // 4
			 6, -3, -3, -3, 10, -3,  0, -3, -3, -3, // 5
			-3, -3, -3, -3, -3, -3, -3,  6, -3, -1, // 6
			-3, -3, -3, -3, -1, -3, -3, -3, -3, -3, // 7
			-3, -3, -3, -3, -3, -3, -3, -3,  2, -3, // 8
			-3, -3,  8, -3, -3, -3, -3, -3, -3, -3, // 9
			-3, -3, -3, -3, -1, -3, -3, -3, -3, -3, // 10
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 11
			-3, -3, -3, -3, -3, -3, -3,  6,  6, -3, // 12
			-3, -3, -3, -3, -3, -3, -3,  8, -3, -3, // 13
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -1, // 14
			-3, -3, -3, -3, -3, -3,  6, -3, -3, -3, // 15
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 16
			-3, -3, -3, -3, -3, -3, -3, -1, -3,  8, // 17
			 4, -3, -3,  4, -3, -3, -3,  6, -3, -3, // 18
			-3, -3,  4, -3, -3, -3, -3, -3, -3, -3, // 19
			-3, -3, -3,  6, -3,  6, -3, -3, -3, -3, // 20
			-3,  6, -3, -3, -3, -3, -3, -3, -3, -3, // 21
			-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 22
			 6, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 23
			-3, -3, -3, -3,  8, -3, -3, -3, -3, -3, // 24
			-3, -3, -3, -3, -3, -3 // 25
	};

	
	private final Player player;
	
	public RS2Decoder(Player player) {
		this.player = player;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() > 0) {
			int id = buffer.readByte() & 0xFF;
			int size = PACKET_SIZES[id];
			if(size == -1) {
				if(buffer.readableBytes() > 0) {
	
					size = buffer.readByte();
				}
			}
			if(size < 0) {
				size = buffer.readableBytes();
				System.out.println("for packet " + id + ", guessing size " + size);
			}
			if (buffer.readableBytes() >= size && size != 0) {
				byte[] data = new byte[size];
				buffer.readBytes(data, 0, size);
				PacketManager.handle(player, new Packet(id, ByteBuffer.wrap(data)));
			}
		}
		return null;
	}
}
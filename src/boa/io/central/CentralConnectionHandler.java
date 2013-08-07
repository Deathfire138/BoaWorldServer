package boa.io.central;

import java.util.logging.Logger;

import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

import boa.io.CentralServer;

public final class CentralConnectionHandler extends SimpleChannelHandler {
	
	private static final Logger logger = Logger.getLogger(CentralConnectionHandler.class.getName());

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Connection connected on: " + e.getChannel().toString());
		CentralServer.setChannel(e.getChannel());
		logger.info("Saved channel.");
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		logger.info("Connection closed on: " + e.getChannel().toString());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
	}
}

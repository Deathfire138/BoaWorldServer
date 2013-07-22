package boa.io;

import java.util.logging.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public final class ConnectionManager {
		
	private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());
	
	private static final ExecutorService inservice = Executors.newCachedThreadPool();
	
	private static final ExecutorService outservice = Executors.newCachedThreadPool();

	private static ServerBootstrap bootstrap;

	private static NioServerSocketChannelFactory factory;

	public static void init() throws Throwable {
		factory = new NioServerSocketChannelFactory(inservice, outservice);
		bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ProtocolPipelineMultiplexer());
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		bootstrap.bind(new InetSocketAddress(43594));
		logger.info("Listening on: " + 43594);
	}
}
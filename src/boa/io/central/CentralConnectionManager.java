package boa.io.central;

import java.util.logging.Logger;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;

import boa.io.CentralServer;

public final class CentralConnectionManager {
		
	private static final Logger logger = Logger.getLogger(CentralConnectionManager.class.getName());
	
	private static final ExecutorService inservice = Executors.newCachedThreadPool();
	
	private static final ExecutorService outservice = Executors.newCachedThreadPool();

	private static ClientBootstrap bootstrap;
	
	private static NioClientSocketChannelFactory factory;

	public static void init() throws Throwable {
		factory = new NioClientSocketChannelFactory(inservice, outservice);
		bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new CentralProtocolPipelineMultiplexer());
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		bootstrap.connect(new InetSocketAddress(43595));
		logger.info("Connecting on 43595.");
	}
}
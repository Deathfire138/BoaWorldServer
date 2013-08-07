package boa.io.central;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import boa.io.central.codec.BoaEncoder;

public final class CentralProtocolPipelineMultiplexer implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("encoder", new BoaEncoder());
		//pipeline.addLast("decoder", new RS2ProtocolDecoder());
		pipeline.addLast("logic", new CentralConnectionHandler());
		return pipeline;
	}
}
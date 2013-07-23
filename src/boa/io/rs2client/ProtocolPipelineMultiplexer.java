package boa.io.rs2client;


import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import boa.io.rs2client.protocol.RS2Encoder;
import boa.io.rs2client.protocol.RS2ProtocolDecoder;

public final class ProtocolPipelineMultiplexer implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("encoder", new RS2Encoder());
		pipeline.addLast("decoder", new RS2ProtocolDecoder());
		pipeline.addLast("logic", new ConnectionHandler());
		return pipeline;
	}
}
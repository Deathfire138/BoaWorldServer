package net;

import net.codec.RS2Encoder;
import net.codec.RS2ProtocolDecoder;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public final class ProtocolPipelineMultiplexer implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("encoder", new RS2Encoder());
		pipeline.addLast("decoder", new RS2ProtocolDecoder());
		pipeline.addLast("logic", new ConnectionHandler());
		return pipeline;
	}
}
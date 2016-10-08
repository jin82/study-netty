package ws;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;


/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/07
 * \* Time: 14:45
 * \* Description:
 * \
 */
public class SecureChatServerIntializer extends
ChatServerInitializer{
	private final SslContext context;

	public SecureChatServerIntializer(ChannelGroup group, SslContext context) {
		super(group);
		this.context = context;
	}

	@Override
	protected void initChannel(Channel channel) throws Exception {
		super.initChannel(channel);
		SSLEngine engine = context.newEngine(channel.alloc());
		engine.setUseClientMode(false);
		channel.pipeline().addFirst(new SslHandler(engine));
	}
}

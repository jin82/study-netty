package udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/08
 * \* Time: 19:31
 * \* Description:接受并且处理
 * \
 */
public class EventLogMonitor {

	private final Bootstrap bootstrap;
	private final EventLoopGroup group;

	public EventLogMonitor(InetSocketAddress address) {
		bootstrap = new Bootstrap();
		group = new NioEventLoopGroup();
		bootstrap.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel channel) throws Exception {
						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast(new LogEventDecoder());
						pipeline.addLast(new LogEventHandler());
					}
				})
				.localAddress(address);
	}

	public Channel bind() {
		return bootstrap.bind().syncUninterruptibly().channel();
	}

	public void stop() {
		group.shutdownGracefully();
	}

	public static void main(String[] args) throws Exception{
		EventLogMonitor monitor = new EventLogMonitor(new InetSocketAddress(9999));
		try{
			Channel channel = monitor.bind();
			System.out.println("LogEventMonitor running");
			channel.closeFuture().await();
		}finally {
			monitor.stop();
		}
	}
}

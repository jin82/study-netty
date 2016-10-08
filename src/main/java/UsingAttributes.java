import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;


/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/07
 * \* Time: 11:29
 * \* Description:
 * \
 */
public class UsingAttributes {

	public static void main(String[] args) {
		final AttributeKey<Integer> id = AttributeKey.newInstance("ID");

		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new SimpleChannelInboundHandler<ByteBuf>() {
					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						Integer idValue = ctx.channel().attr(id).get();
						System.out.println(idValue);
					}

					@Override
					protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
						System.out.println("Reveived data");
					}
				});
		bootstrap.option(ChannelOption.SO_KEEPALIVE,true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000);
		bootstrap.attr(id,123456);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.baidu.com",80));
		future.syncUninterruptibly();
		Future<?> closeFuture = group.shutdownGracefully();
		try {
			closeFuture.sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("CLOSEING");
	}

}

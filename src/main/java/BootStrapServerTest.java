import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/07
 * \* Time: 1:39
 * \* Description:
 * \
 */
public class BootStrapServerTest {
	public static void main(String[] args) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group)
				.channel(NioServerSocketChannel.class)
				.childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
					@Override
					protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
						System.out.println("Reveived data");
						System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
						byteBuf.clear();
					}
				});
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
		future.addListener(future1 -> {
			if (future1.isSuccess()) {
				System.out.println("Server bound");
			}else{
				System.err.println("Bound attempt failed");
				future1.cause().printStackTrace();
			}
		});
	}
}

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/06
 * \* Time: 22:04
 * \* Description:
 * \
 */
public class BootStrapClientTest {
	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new SimpleChannelInboundHandler<ByteBuf>() {
					@Override
					protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
						System.out.print("Data coming ");
						System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
					}
				});

		ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.google.com",80));
		future.addListener((future1 -> {
			if (future1.isSuccess()) {
				System.out.println("Connection established");
			}else{
				System.err.println("Connection attempt failed");
				future1.cause().printStackTrace();
			}
		}));

	}
}

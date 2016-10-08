package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/09/29
 * \* Time: 16:04
 * \* Description:
 * \
 */
public class EchoServer {

	private final int port;

	public EchoServer(int port){
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		int port = 7432;
		new EchoServer(port).start();
	}

	public void start() throws Exception {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group)
			        .channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {

						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(new EchoServerHandler());
						}
					});

			ChannelFuture f = b.bind().sync();
			System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
			f.channel().closeFuture().sync();
		}finally {
			group.shutdownGracefully().sync();
		}
	}

}

package ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/07
 * \* Time: 14:53
 * \* Description:
 * \
 */
public class SecureChatServer extends ChatServer {
	private final SslContext context;

	public SecureChatServer(SslContext context) {
		this.context = context;
	}

	@Override
	protected ChannelInitializer<Channel> createInitializer(ChannelGroup group){
		return new SecureChatServerIntializer(group,context);
	}

	public static void main(String[] args) {
		int port = 8080;
		SelfSignedCertificate cert;
		SslContext context = null;

		try {
			cert = new SelfSignedCertificate();
			context = SslContextBuilder.forServer(cert.certificate(),cert.privateKey()).build();
		} catch (CertificateException |SSLException e) {
			e.printStackTrace();
		}

		final SecureChatServer server = new SecureChatServer(context);
		ChannelFuture future = server.start(new InetSocketAddress(port));

		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				server.destroy();
			}
		});
		future.channel().closeFuture().syncUninterruptibly();
	}
}

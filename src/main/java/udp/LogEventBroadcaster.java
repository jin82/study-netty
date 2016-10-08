package udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import udp.encoder.LogEventEncoder;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/08
 * \* Time: 18:58
 * \* Description: 读取并且发送
 * \
 */
public class LogEventBroadcaster {

	private final Bootstrap bootstrap;
	private final File file;
	private final EventLoopGroup group;

	public LogEventBroadcaster(InetSocketAddress address,File file) {
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(new LogEventEncoder(address));
		this.file = file;
	}

	public void run() throws Exception {
		Channel channel = bootstrap.bind(0).syncUninterruptibly().channel();
		System.out.println("LogEventBroadcaster running");
		long pointer = 0;
		for (;;) {
			long len = file.length();
			if (len < pointer) {
				pointer = len;
			} else {
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				raf.seek(pointer);
				String line;
				String utfLine;
				while ((line = raf.readLine())!= null) {
					utfLine =new String(line.getBytes(CharsetUtil.ISO_8859_1),CharsetUtil.UTF_8);
					channel.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(),utfLine ));
					System.out.println("FLUSH "+utfLine);
				}
				pointer = raf.getFilePointer();
				raf.close();
			}
			try {
				Thread.sleep(1000);
			}catch (InterruptedException e){
				Thread.interrupted();
			}
		}
	}

	public void stop() {
		group.shutdownGracefully();
	}


	public static void main(String[] args) throws Exception{
		LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255",9999),new File("E:\\netty_test.log"));
		try{
			broadcaster.run();
		}finally {
			broadcaster.stop();
		}
	}

}

package udp.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import udp.LogEvent;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/08
 * \* Time: 18:51
 * \* Description:
 * \
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

	private final InetSocketAddress remoteAdress;

	public LogEventEncoder(InetSocketAddress remoteAdress) {
		this.remoteAdress = remoteAdress;
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, LogEvent logEvent, List<Object> list) throws Exception {
		byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
		byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
		ByteBuf buf = channelHandlerContext.alloc().buffer(file.length+msg.length+1);
		buf.writeBytes(file);
		buf.writeByte(LogEvent.SEPARATOR);
		buf.writeBytes(msg);
		list.add(new DatagramPacket(buf, remoteAdress));
	}
}

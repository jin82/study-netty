package udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/08
 * \* Time: 19:39
 * \* Description:
 * \
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket>{
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
		ByteBuf buf = datagramPacket.content();
		int index = buf.indexOf(0, buf.readableBytes(), LogEvent.SEPARATOR);
		String fileName = buf.slice(0, index).toString(CharsetUtil.UTF_8);
		String msg = buf.slice(index + 1, buf.readableBytes()).toString(CharsetUtil.UTF_8);

		LogEvent event = new LogEvent(datagramPacket.recipient(), System.currentTimeMillis(), fileName, msg);
		list.add(event);
	}
}

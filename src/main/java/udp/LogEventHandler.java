package udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/08
 * \* Time: 19:45
 * \* Description:
 * \
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent>{
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, LogEvent logEvent) throws Exception {
		StringBuilder builder = new StringBuilder();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(logEvent.getReceived());
		builder.append(simpleDateFormat.format(date))
				.append(" [")
				.append(logEvent.getSource().toString())
				.append("] [")
				.append(logEvent.getLogfile())
				.append("] : ")
				.append(logEvent.getMsg());

		System.out.println(builder.toString());

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

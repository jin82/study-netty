package ws;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/07
 * \* Time: 13:34
 * \* Description:
 * \
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String wsUri;
	private static final File INDEX;

	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			String path = location.toURI() + "index.html";
			path = !path.contains("file:")?path:path.substring(5);
			INDEX = new File(path);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Unbale to locate index.html",e);
		}
	}

	public HttpRequestHandler(String wsUri){
		this.wsUri = wsUri;
	}


	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
		if(wsUri.equalsIgnoreCase(fullHttpRequest.uri())){
			channelHandlerContext.fireChannelRead(fullHttpRequest.retain());
		}else{
			if (HttpUtil.is100ContinueExpected(fullHttpRequest)) {
				send100Continue(channelHandlerContext);
			}

			RandomAccessFile file = new RandomAccessFile(INDEX,"r");

			HttpResponse response = new DefaultHttpResponse(fullHttpRequest.protocolVersion(), HttpResponseStatus.OK);

			response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");

			boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);

			if (keepAlive) {
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}

			channelHandlerContext.write(response);

			if (channelHandlerContext.pipeline().get(SslHandler.class) == null) {
				channelHandlerContext.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
			}else{
				channelHandlerContext.write(new ChunkedNioFile(file.getChannel()));
			}

			ChannelFuture future = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

			if (!keepAlive) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}

	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

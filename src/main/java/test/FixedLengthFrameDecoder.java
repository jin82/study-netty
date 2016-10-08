package test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/07
 * \* Time: 11:53
 * \* Description:
 * \
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder{

	private final int frameLength;

	public FixedLengthFrameDecoder(int frameLength) {
		if (frameLength < 0) {
			throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
		}
		this.frameLength = frameLength;
	}


	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		if (byteBuf.readableBytes() >= frameLength) {
			ByteBuf buf = byteBuf.readBytes(frameLength);
			list.add(buf);
		}
	}
}

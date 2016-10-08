package test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;

/**
 * Created by jin82 on 2016/10/07.
 */
public class FixedLengthFrameDecoderTest {
	@org.junit.Test
	public void decode() throws Exception {
		ByteBuf buf = Unpooled.buffer();
		for (int i = 0; i < 9; i++) {
			buf.writeByte(i);
		}
		ByteBuf input = buf.duplicate();

		EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
		Assert.assertFalse(channel.writeInbound(input.readBytes(2)));
		Assert.assertTrue(channel.writeInbound(input.readBytes(7)));

		Assert.assertTrue(channel.finish());

		ByteBuf read = channel.readInbound();
		Assert.assertEquals(buf.readSlice(3),read);
		System.out.println(read.toString());
		read.release();

		read = channel.readInbound();
		Assert.assertEquals(buf.readSlice(3),read);
		System.out.println(read.toString());
		read.release();

		read = channel.readInbound();
		Assert.assertEquals(buf.readSlice(3),read);
		System.out.println(read.toString());
		read.release();

		Assert.assertNull(channel.readInbound());
		buf.release();
	}

}
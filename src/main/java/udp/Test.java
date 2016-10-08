package udp;

import java.io.RandomAccessFile;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/10/08
 * \* Time: 20:17
 * \* Description: 写入文件
 * \
 */
public class Test {
	public static void main(String[] args) throws Exception{
		RandomAccessFile file = new RandomAccessFile("E:\\netty_test.log","rw");
		long length = file.length();
		file.seek(length);
		String randomInfo;
		while (true) {
			randomInfo = Math.random() +"";
			file.write(randomInfo.getBytes());
			Thread.sleep(800);
		}

	}
}

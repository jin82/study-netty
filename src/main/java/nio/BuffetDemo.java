package nio;

import java.io.FileInputStream;

/**
 * \*
 * \* User: jin82
 * \* Date: 2016/09/30
 * \* Time: 16:31
 * \* Description:
 * \
 */
public class BuffetDemo {

	public static void main(String[] args){
//		RandomAccessFile file = new RandomAccessFile("G:\\err1 (1).txt","rw");
//		FileChannel channel = file.getChannel();
//		ByteBuffer buf = ByteBuffer.allocate(48);
//		while (channel.read(buf) != -1) {
//			buf.flip();
//			while (buf.hasRemaining()) {
//				System.out.print((char)buf.get());
//			}
//			buf.clear();
//		}
//		file.close();


		try(FileInputStream in = new FileInputStream("G:\\err1 (1).txt")){
			byte[] buffer = new byte[1024];
			while(in.read(buffer,0,buffer.length) != -1){
				System.out.print(new String(buffer));
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}

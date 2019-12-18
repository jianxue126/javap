package jx.com.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ByteBufferDemo {
    public static void main(String[] args) {
        String fileName = "src/1.txt";
        try {
            readFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFile(String fileName) throws IOException {
        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        FileChannel channel = file.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        int size = channel.read(byteBuffer);
        System.out.println("size:"+size);

        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        while(size > 0){
            byteBuffer.flip();
            int i=0;
//            while(byteBuffer.remaining() > 0){
//                System.out.println(byteBuffer.getChar(i++));
//            }

            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(byteBuffer.asReadOnlyBuffer());
            System.out.println(charBuffer.toString());
            byteBuffer.clear();
            size = channel.read(byteBuffer);
            System.out.println("size:"+size);

            byteBuffer.get();
        }

        channel.close();;
        file.close();
    }
}

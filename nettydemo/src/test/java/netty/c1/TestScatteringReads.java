package netty.c1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static netty.c1.ByteBufferUtil.debugAll;

/**
 * 类描述
 *
 * @author zcl
 * @Description 分散读取
 * @Date 2022/1/2 14:36
 */
public class TestScatteringReads {
    public static void main(String[] args) {
        try {
            FileChannel channel = new RandomAccessFile("words.txt", "r").getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(3);
            ByteBuffer byteBuffer1 = ByteBuffer.allocate(3);
            ByteBuffer byteBuffer2 = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{byteBuffer,byteBuffer1,byteBuffer2});
            byteBuffer.flip();
            byteBuffer1.flip();
            byteBuffer2.flip();
            debugAll(byteBuffer);
            debugAll(byteBuffer1);
            debugAll(byteBuffer2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package netty.c1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import static netty.c1.ByteBufferUtil.debugAll;

/**
 * 类描述
 *
 * @author zcl
 * @Description 分散读取
 * @Date 2022/1/2 14:36
 */
public class TestScatteringWrites {
    public static void main(String[] args) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("world");
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("你好");
        FileChannel channel = null;
        try {
            channel = new RandomAccessFile("words.txt", "rw").getChannel();
            channel.write(new ByteBuffer[]{buffer,buffer1,buffer2});
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            closeResouece(channel);
            e.printStackTrace();
        } finally {
            closeResouece(channel);
        }
    }

    private static void closeResouece(FileChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

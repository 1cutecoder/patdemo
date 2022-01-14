package netty.c1;


import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zcl
 * @date 2021/12/31 15:58
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        //FilterChannel
        try {
            FileChannel channel = new FileInputStream("src/data.txt").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                int len = channel.read(buffer);
                log.debug("读到的字节数:" + len);
                if (len == -1) {
                    break;
                }
                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    System.out.println((char) b);
                }
                buffer.clear();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

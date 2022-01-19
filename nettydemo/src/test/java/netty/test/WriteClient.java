package netty.test;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author zcl
 * @date 2022/1/4 15:57
 */
@Slf4j
public class WriteClient {
    public static void main(String[] args) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress(8080));
            System.out.println("waiting...");
            int count = 0;
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                int read = sc.read(buffer);
                 count += read;
                System.out.println("count = " + count);
                 buffer.clear();
            }

        } catch (IOException e) {
            closeResource(sc);
            e.printStackTrace();
        } finally {
            closeResource(sc);
        }
    }

    public static void closeResource(SocketChannel sc) {
        if (sc != null) {
            try {
                sc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

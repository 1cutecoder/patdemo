package netty.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author zcl
 * @date 2022/1/4 12:16
 */
public class Client {
    public static void main(String[] args) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress(8080));
            System.out.println("waiting...");
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

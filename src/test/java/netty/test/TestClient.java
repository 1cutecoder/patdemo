package netty.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author zcl
 * @date 2022/1/5 11:11
 */
public class TestClient {
    public static void main(String[] args) {
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("localhost", 8080));
            sc.write(Charset.defaultCharset().encode("123456789@abcdef"));
            System.in.read();
        } catch (IOException e) {
            closeResource(sc);
            e.printStackTrace();
        } finally {
            closeResource(sc);
        }
    }

    public static void closeResource(SocketChannel ssc) {
        if (ssc != null) {
            try {
                ssc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

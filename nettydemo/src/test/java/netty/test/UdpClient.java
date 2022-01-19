package netty.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zcl
 * @date 2022/1/5 14:17
 */
public class UdpClient {
    public static void main(String[] args) {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello");
            InetSocketAddress address = new InetSocketAddress("localhost", 9999);
            channel.send(buffer, address);
        } catch (Exception e) {
            closeResource(channel);
            e.printStackTrace();

        } finally {
            closeResource(channel);
        }
    }

    public static void closeResource(DatagramChannel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

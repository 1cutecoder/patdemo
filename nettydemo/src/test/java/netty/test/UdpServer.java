package netty.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;

import static netty.c1.ByteBufferUtil.debugAll;

/**
 * @author zcl
 * @date 2022/1/5 14:14
 */
public class UdpServer {
    public static void main(String[] args) {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(9999));
            System.out.println("waiting...");
            ByteBuffer buffer = ByteBuffer.allocate(32);
            channel.receive(buffer);
            buffer.flip();
            debugAll(buffer);
        } catch (IOException e) {
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

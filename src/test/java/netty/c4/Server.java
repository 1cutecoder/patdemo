package netty.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static netty.c1.ByteBufferUtil.debugRead;

/**
 * @author zcl
 * @date 2022/1/4 12:02
 */
@Slf4j
public class Server {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8080));
            List<SocketChannel> channels = new ArrayList<>();
            while (true) {
                SocketChannel sc = ssc.accept();
                if (sc != null) {
                    log.debug("connected...{}",sc);
                    sc.configureBlocking(false);
                    channels.add(sc);
                }
                for (SocketChannel channel : channels) {
                    int read = channel.read(buffer);
                    if (read > 0) {
                        log.debug("before read...{}",channel);
                        buffer.flip();
                        debugRead(buffer);
                        buffer.clear();
                        log.debug("after read...{}",channel);
                    }
                }
            }
        } catch (IOException e) {
            closeResource(ssc);
            e.printStackTrace();
        } finally {
            closeResource(ssc);
        }

    }


    public static void closeResource(ServerSocketChannel ssc) {
        if (ssc != null) {
            try {
                ssc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

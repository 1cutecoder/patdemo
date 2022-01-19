package netty.test;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static netty.c1.ByteBufferUtil.debugAll;

/**
 * @author zcl
 * @date 2022/1/4 12:34
 */
@Slf4j
public class Server {
    public static void main(String[] args) {
        ServerSocketChannel ssc = null;
        Selector selector = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(8080));
            selector = Selector.open();
            SelectionKey sscKey = ssc.register(selector, 0, null);
            sscKey.interestOps(SelectionKey.OP_ACCEPT);
            log.debug("register key:{}", sscKey);
            while (true) {
                selector.select();
                //���������з������¼�
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    log.debug("key:{}", key);
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel sc = channel.accept();
                        sc.configureBlocking(false);
                        ByteBuffer buffer = ByteBuffer.allocate(6);
                        SelectionKey scKey = sc.register(selector, 0, buffer);
                        scKey.interestOps(SelectionKey.OP_READ);
                        log.debug("{}", sc);
                        log.debug("scKey{}", scKey);
                    } else if (key.isReadable()) {
                        try {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            int read = channel.read(buffer);
                            if (read == -1) {
                                key.cancel();
                            } else {
                                split(buffer);
                                if (buffer.position() == buffer.limit()) {
                                    ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 3);
                                    buffer.flip();
                                    newBuffer.put(buffer);
                                    key.attach(newBuffer);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            key.cancel();
                        }
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

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int length = i - source.position() + 1;
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        source.compact();
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

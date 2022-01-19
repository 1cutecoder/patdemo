package netty.test;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author zcl
 * @date 2022/1/4 15:47
 */
@Slf4j
public class WriteServer {
    public static void main(String[] args) {
        ServerSocketChannel ssc = null;
        Selector selector = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            ssc.bind(new InetSocketAddress(8080));
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    //刚建立连接
                    if (key.isAcceptable()) {
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        SelectionKey scKey = sc.register(selector, 0, null);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 30_000_000; i++) {
                            sb.append("a");
                        }
                        ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                        int write = sc.write(buffer);
                        System.out.println("write = " + write);
                        if (buffer.hasRemaining()) {
                            //关注可写事件
                            scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);
                            //未写完的数据挂到scKey上
                            scKey.attach(buffer);
                        }
                    } else if (key.isWritable()) {
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        SocketChannel sc = (SocketChannel) key.channel();
                        int write = sc.write(buffer);
                        System.out.println("write = " + write);
                        //清理操作
                        if (!buffer.hasRemaining()) {
                            key.attach(null);
                            key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
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

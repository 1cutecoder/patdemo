package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zcl
 * @date 2021/12/29 17:04
 */
public class SocketNio {
    /**
     * NIO
     * 优势：规避多线程问题 C10k
     * 弊端：假设1w个连接 只有一个连接发送数据，每循环一次，必须向内核发送1w次系统调用，有999次是无意义的，浪费的，消耗时间和资源
     * 用户控件向内核空间的循环遍历，复杂度在系统调用上
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        List<SocketChannel> clients = new LinkedList<>();
        ServerSocketChannel ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(9090));
        ss.configureBlocking(false);
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            SocketChannel client = ss.accept();
            if (client == null) {
                System.out.println("null......");
            } else {
                //非阻塞之后 都会有客户端，区别在于有无数据
                client.configureBlocking(false);
                int port = client.socket().getPort();
                System.out.println("client...port: " + port);
                clients.add(client);
            }
            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
            //传1w个客户端给系统调用，系统调用返回其中有数据的一个客户端，之后只进行一次系统调用 -->多路复用器
            for (SocketChannel c : clients) {
                int num = c.read(buffer);
                //-1 0 1 阻塞
                if (num > 0) {
                    buffer.flip();
                    byte[] aaa = new byte[buffer.limit()];
                    buffer.get(aaa);
                    String b = new String(aaa);
                    System.out.println(c.socket().getPort() + ":" + b);
                    buffer.clear();
                }
            }

        }
    }
}

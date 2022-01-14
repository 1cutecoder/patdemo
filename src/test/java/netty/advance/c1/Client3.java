package netty.advance.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Random;

/**
 * 类描述
 *
 * @author zcl
 * @Description 行解码器解决粘包半包问题
 * @Date 2022/1/9 13:14
 */
@Slf4j
public class Client3 {
    public static void main(String[] args) {
        send();
        System.out.println("finish");
    }

    private static void send() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = null;
        try {
            channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    char c = '0';
                                    Random r = new Random();
                                    for (int i = 0; i < 10; i++) {
                                        StringBuilder sb = makeString(c, r.nextInt(256) +1);
                                        c++;
                                        buf.writeBytes(sb.toString().getBytes());
                                    }
                                    ctx.writeAndFlush(buf);
                                }
                            });
                        }
                    })
                    .connect(new InetSocketAddress(8080))
                    .sync()
                    .channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.closeFuture().addListener(future -> {
            group.shutdownGracefully();
        });
    }

    public static StringBuilder makeString(char c, int len) {
        StringBuilder sb = new StringBuilder(len + 2);
        for (int i = 0; i < len; i++) {
            sb.append(c);
        }
        sb.append("\n");
        return sb;

    }
}

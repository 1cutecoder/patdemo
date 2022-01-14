package netty.advance;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * 类描述
 *
 * @author zcl
 * @Description TODO
 * @Date 2022/1/9 13:14
 */
@Slf4j
public class HellloWorldClient {
    public static void main(String[] args)  {
        for (int i = 0; i < 10; i++) {
            send();
        }
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
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        ByteBuf buf = ctx.alloc().buffer();
                                        buf.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 31, 31, 32, 33, 34, 35, 36});
                                        ctx.writeAndFlush(buf);
                                    ctx.channel().close();
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
}

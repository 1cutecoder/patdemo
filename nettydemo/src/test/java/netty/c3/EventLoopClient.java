package netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.Scanner;

/**
 * @author zcl
 * @date 2022/1/5 16:34
 */
@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080);
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        log.debug("{}",channel);
        channel.writeAndFlush("hello,world");
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();
        /*channelFuture.addListener(new ChannelFutureListener() {
            //连接建立后被调用
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = channelFuture.channel();
                log.debug("{}", channel);
                channel.writeAndFlush("hello,world");
            }
        });*/
        System.out.println("");
        //1、同步处理关闭 2、异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.sync();
        log.debug("处理关闭之后的操作");
        group.shutdownGracefully();
    }
}

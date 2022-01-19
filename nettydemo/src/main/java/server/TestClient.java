package server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import message.*;
import protocol.MessageCodecSharable;
import protocol.ProtocolFrameDecoder;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zcl
 * @date 2022/1/19 14:39
 */
@Slf4j
public class TestClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOOGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    ch.pipeline().addLast(LOOGING_HANDLER);
                    ch.pipeline().addLast(messageCodecSharable);
                }
            });
            Channel channel = bootstrap.connect(new InetSocketAddress("localhost", 8080)).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.debug("client error exception:{}", e.toString());
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}

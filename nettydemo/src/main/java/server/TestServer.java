package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import protocol.MessageCodecSharable;
import protocol.ProtocolFrameDecoder;
import server.handler.*;

/**
 * @author zcl
 * @date 2022/1/19 14:39
 */
@Slf4j
public class TestServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOOGING_HANDLER = new LoggingHandler();
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        LoginRequestMessageHandler requestMessageHandler = new LoginRequestMessageHandler();
        ChatRequestMessageHandler messageHandler = new ChatRequestMessageHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {

                    ch.pipeline().addLast(new ProtocolFrameDecoder());
                    ch.pipeline().addLast(LOOGING_HANDLER);
                    ch.pipeline().addLast(messageCodecSharable);
                    ch.pipeline().addLast(requestMessageHandler);
                    ch.pipeline().addLast(messageHandler);
                }
            });
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.debug("server error exception:{}", e.toString());
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

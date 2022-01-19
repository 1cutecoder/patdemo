package netty.advance;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 类描述
 *
 * @author zcl
 * @Description TODO
 * @Date 2022/1/9 12:43
 */
@Slf4j
public class HelloWorldServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //调整系统的接收缓冲器(滑动窗口)
 //       serverBootstrap.option(ChannelOption.SO_RCVBUF,10); 接受缓冲区设小一些
        //调整netty的接受缓冲区(bytebuf)
        serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR,new AdaptiveRecvByteBufAllocator(16,16,16));
        ChannelFuture future = serverBootstrap
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler());
                    }
                })
                .bind(8080);
        future.channel().closeFuture().sync();
    }
}

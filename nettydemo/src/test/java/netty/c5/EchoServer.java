package netty.c5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

import static netty.c1.ByteBufferUtil.debugRead;

/**
 * 类描述
 *
 * @author zcl
 * @Description TODO
 * @Date 2022/1/9 12:43
 */
@Slf4j
public class EchoServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = ctx.alloc().buffer(20);
                                byteBuf.writeBytes("hello,I'm server.".getBytes());
                                ByteBuf msgbuf = (ByteBuf) msg;
                                log.debug("read from client msg:{}", msgbuf.toString(Charset.defaultCharset()));
                                ctx.writeAndFlush(byteBuf);
                            }
                        });
                    }
                })
                .bind(8080);
    }
}

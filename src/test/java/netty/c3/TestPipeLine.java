package netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zcl
 * @date 2022/1/7 16:02
 */
@Slf4j
public class TestPipeLine {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(final NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //添加处理器 head-->h1->h2-->h3-->h4-->h5-->h6-->tail 双向链表
                        pipeline.addLast("h1",new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("1");
                                super.channelRead(ctx, msg);
                            }
                        });
                        pipeline.addLast("h2",new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("2");
                                super.channelRead(ctx, msg);
                            }
                        });
                        pipeline.addLast("h4",new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("4");
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("h3",new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("3");
                                super.channelRead(ctx, msg);
                                //从tail往head找
                                //ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server..".getBytes()));
                                //添加处理器 head-->h1->h2-->h4-->h3-->h5-->h6-->tail 双向链表 从h3往head找
                                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server..".getBytes()));
                            }
                        });

                        pipeline.addLast("h5",new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("5");
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("h6",new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("6");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(8080);
    }
    static class Student{
        
    }
}

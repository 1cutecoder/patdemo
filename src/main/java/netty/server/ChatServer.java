package netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import netty.protocol.MessageCodecSharable;
import netty.protocol.ProtocolFrameDecoder;
import netty.server.handler.*;

/**
 * @author zcl
 * @date 2022/1/12 11:09
 */
@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOOGING_HANDLER = new LoggingHandler();
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        LoginRequestMessageHandler requestMessageHandler = new LoginRequestMessageHandler();
        ChatRequestMessageHandler messageHandler = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler groupCreateRequestHandler = new GroupCreateRequestMessageHandler();
        GroupChatRequestMessageHandler groupChatRequestHandler = new GroupChatRequestMessageHandler();
        QuitHandler quitHandler = new QuitHandler();
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
                    ch.pipeline().addLast(new IdleStateHandler(60,0,0));
                    //入站和出站处理器
                    ch.pipeline().addLast(new ChannelDuplexHandler(){
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.READER_IDLE) {
                                log.debug("已经60s没有读到数据了");
                            }
                        }
                    });
                    ch.pipeline().addLast(requestMessageHandler);
                    ch.pipeline().addLast(messageHandler);
                    ch.pipeline().addLast(groupCreateRequestHandler);
                    ch.pipeline().addLast(groupChatRequestHandler);
                    ch.pipeline().addLast(quitHandler);
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

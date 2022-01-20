package server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import message.RpcRequestMessage;
import net.sf.cglib.proxy.Proxy;
import protocol.MessageCodecSharable;
import protocol.ProtocolFrameDecoder;
import protocol.SequenceIdGenerator;
import server.handler.RpcResponseMessageHandler;
import server.service.HelloService;

import java.net.InetSocketAddress;

/**
 * @author zcl
 * @date 2022/1/12 14:23
 */
@Slf4j
public class RpcClientManager {
    private static Channel channel = null;

    public static Channel getChannel() {
        if (channel == null) {
            synchronized (RpcClientManager.class) {
                if (channel == null) {
                    synchronized (RpcClientManager.class) {
                        initChannel();
                    }
                }
            }
        }
        return channel;

    }

    public static <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class[] interfaceClasses = new Class[]{serviceClass};
        Object o = Proxy.newProxyInstance(loader, interfaceClasses, (proxy, method, args) -> {
            int sequenceId = SequenceIdGenerator.nexId();
            RpcRequestMessage message = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
            );
            getChannel().writeAndFlush(message);
            /*EventLoop eventLoop = getChannel().eventLoop();
            DefaultPromise<Object> promise = new DefaultPromise<>(eventLoop);
            RpcResponseMessageHandler.PROMISES.put(sequenceId,promise);
            promise.await();
            if (promise.isSuccess()) {
                return promise.getNow();
            }else {
                System.out.println("error"+promise.cause());
                throw new RuntimeException(promise.cause());
            }*/
            return null;
        });
        return (T) o;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class<HelloService> serviceClass = HelloService.class;
        ClassLoader loader = serviceClass.getClassLoader();
        Class[] interfaceClasses = new Class[]{serviceClass};
        HelloService helloService = (HelloService) Proxy.newProxyInstance(loader, interfaceClasses, (proxy, method, arguments) -> {
            int sequenceId = SequenceIdGenerator.nexId();
            RpcRequestMessage message = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    arguments
            );
            getChannel().writeAndFlush(message);
            return null;
        });
        String s = helloService.sayHello("zhangsan");
        System.out.println("s = " + s);   }

    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOOGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        RpcResponseMessageHandler rpcResponseHandler = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolFrameDecoder());
                ch.pipeline().addLast(LOOGING_HANDLER);
                ch.pipeline().addLast(messageCodecSharable);
                ch.pipeline().addLast(rpcResponseHandler);
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.debug("{}", msg);
                        System.out.println("msg = " + msg);
                    }
                });
            }
        });
        try {
            channel = bootstrap.connect(new InetSocketAddress("localhost", 8080)).sync().channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            log.debug("client error exception:{}", e.toString());
            e.printStackTrace();
        }
    }
}

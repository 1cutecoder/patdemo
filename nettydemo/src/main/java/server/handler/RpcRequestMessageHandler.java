package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.RpcRequestMessage;
import message.RpcResponseMessage;
import server.service.HelloService;
import server.service.ServiceFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zcl
 * @date 2022/1/19 9:32
 */
@Slf4j
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) {
        log.debug("RpcRequestMessageHandler channelRead0 start:{}",msg);
        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
        try {
            HelloService service = (HelloService) ServiceFactory.getService(Class.forName(msg.getInterfaceName()));
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValue());
            rpcResponseMessage.setReturnValue(invoke);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            log.debug("error {}",e.toString());
            rpcResponseMessage.setExceptionValue(e);
        }
        log.debug("channelRead0 before  writeAndFlush:{}",rpcResponseMessage);
        ctx.writeAndFlush(rpcResponseMessage);
    }

    public static void main(String[] args) {
        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
        RpcRequestMessage msg = new RpcRequestMessage(
                1,
                "server.service.HelloService",
                "sayHello",
                String.class, new Class[]{String.class},
                new Object[]{"张三"}
        );
        try {
            HelloService service = (HelloService) ServiceFactory.getService(Class.forName(msg.getInterfaceName()));
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValue());
            rpcResponseMessage.setReturnValue(invoke);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            log.debug("error {}",e.toString());
            rpcResponseMessage.setExceptionValue(e);
        }
        System.out.println("rpcResponseMessage = " + rpcResponseMessage);
    }
}

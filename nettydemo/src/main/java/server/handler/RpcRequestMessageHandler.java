package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.RpcRequestMessage;

/**
 * @author zcl
 * @date 2022/1/19 9:32
 */
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
    }

    public static void main(String[] args) {
        RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(
                1,"server.service.HelloService","sayHello",String.class,new Class[]{String.class},new Object[]{"张三"}
        );
    }
}

package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import message.RpcResponseMessage;

/**
 * @author zcl
 * @date 2022/1/19 9:32
 */
@Slf4j
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        log.debug("RpcResponseMessageHandler channelRead0 start:{}",msg);
        log.debug("{}:",msg);
    }
}

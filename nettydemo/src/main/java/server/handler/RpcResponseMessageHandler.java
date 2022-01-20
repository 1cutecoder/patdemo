package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import message.RpcResponseMessage;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zcl
 * @date 2022/1/19 9:32
 */
@Slf4j
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    public static final Map<Integer, Promise<Object>> PROMISES = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        log.debug("{}:", msg);
        Promise<Object> promise = PROMISES.get(msg.getSequenceId());
        if (promise == null) {
            throw new RuntimeException("PROMISES.get(msg.getSequenceId()) is null");
        }
        Object returnValue = msg.getReturnValue();
        Exception exceptionValue = msg.getExceptionValue();
        if (exceptionValue != null) {
            promise.setFailure(exceptionValue);
            return;
        }
        promise.setSuccess(returnValue);
        log.debug("RpcResponseMessageHandler channelRead0 start:{}", msg);
    }
}

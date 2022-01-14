package netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import netty.server.session.SessionFactory;

/**
 * @author zcl
 * @date 2022/1/13 11:25
 */
@ChannelHandler.Sharable
@Slf4j
public class QuitHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String nameByChannel = SessionFactory.getSession().getNameByChannel(channel);
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已经断开", nameByChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        String nameByChannel = SessionFactory.getSession().getNameByChannel(channel);
        SessionFactory.getSession().unbind(channel);
        log.debug("{} 异常断开", nameByChannel);

    }
}

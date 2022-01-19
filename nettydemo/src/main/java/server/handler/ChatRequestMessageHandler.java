package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.ChatRequestMessage;
import message.ChatResponseMessage;
import server.session.SessionFactory;

/**
 * @author zcl
 * @date 2022/1/12 16:32
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null) {
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(),msg.getContent()));
        } else {
            ctx.writeAndFlush(new ChatResponseMessage(false,"对方不存在或者不在线"));
        }
    }
}

package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.GroupChatRequestMessage;
import message.GroupChatResponseMessage;
import server.session.GroupSessionFactory;

import java.util.List;

/**
 * @author zcl
 * @date 2022/1/13 11:12
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String content = msg.getContent();
        String from = msg.getFrom();
        List<Channel> membersChannel = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
        for (Channel channel : membersChannel) {
            channel.writeAndFlush(new GroupChatResponseMessage(from,content));
        }
    }
}

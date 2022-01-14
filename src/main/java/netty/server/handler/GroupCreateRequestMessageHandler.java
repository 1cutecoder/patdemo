package netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.message.GroupCreateRequestMessage;
import netty.message.GroupCreateResponseMessage;
import netty.server.session.Group;
import netty.server.session.GroupSession;
import netty.server.session.GroupSessionFactory;

import java.util.List;
import java.util.Set;

/**
 * @author zcl
 * @date 2022/1/13 10:54
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();

        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group == null) {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, groupName + "创建失败"));
        }
        List<Channel> membersChannel = groupSession.getMembersChannel(groupName);
        for (Channel channel : membersChannel) {
            channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入群:" + groupName));
        }
        ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + "创建成功"));
    }
}

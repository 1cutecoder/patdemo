package netty.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * @author zcl
 * @date 2022/1/12 13:54
 */
public interface GroupSession {
    Group createGroup(String name, Set<String> members);

    Group joinMembers(String name, String member);

    Group removeMembers(String name, String member);

    Group removeGroup(String name);

    Set<String> getMembers(String name);
    /**
     *  简单写才加这个获取操作
     */
    List<Channel> getMembersChannel(String name);
}

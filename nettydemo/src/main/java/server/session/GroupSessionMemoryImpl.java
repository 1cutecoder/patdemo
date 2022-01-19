package server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zcl
 * @date 2022/1/12 14:10
 */
public class GroupSessionMemoryImpl implements GroupSession {
    private final Map<String, Group> groupMap = new ConcurrentHashMap<>();

    @Override
    public Group createGroup(String name, Set<String> members) {
        Group group = new Group(name, members);
        return groupMap.put(name, group);
    }

    @Override
    public Group joinMembers(String name, String member) {
        return groupMap.computeIfPresent(name, (key, value) -> {
            value.getMembers().add(name);
            return value;
        });
    }

    @Override
    public Group removeMembers(String name, String member) {
        return groupMap.computeIfPresent(name, (key, valaue) -> {
            valaue.getMembers().remove(name);
            return valaue;
        });
    }

    @Override
    public Group removeGroup(String name) {
        return groupMap.remove(name);
    }

    @Override
    public Set<String> getMembers(String name) {
        return groupMap.getOrDefault(name, Group.EMPTY_GROUP).getMembers();
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        return getMembers(name).stream()
                .map(memberName -> SessionFactory.getSession().getChannel(memberName))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

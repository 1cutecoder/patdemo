package server.session;

import java.util.Collections;
import java.util.Set;

/**
 * @author zcl
 * @date 2022/1/12 12:01
 */
public class Group {
    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());
    private String name;
    private Set<String> members;

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }

    public Set<String> getMembers() {
        return members;
    }
}

package netty.server.session;

/**
 * @author zcl
 * @date 2022/1/12 14:02
 */
public abstract class GroupSessionFactory {

    private static GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}

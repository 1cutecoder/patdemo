package netty.server.session;

/**
 * @author zcl
 * @date 2022/1/12 14:01
 */
public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}

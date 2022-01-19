package server.service;

/**
 * @author zcl
 * @date 2022/1/12 12:00
 */
public abstract class UserServiceFactory {
    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}

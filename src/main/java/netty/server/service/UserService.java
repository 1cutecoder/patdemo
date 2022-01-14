package netty.server.service;

/**
 * @author zcl
 * @date 2022/1/12 11:58
 */
public interface UserService {
    boolean login(String username, String password);
}

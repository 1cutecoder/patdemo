package server.service;

/**
 * @author zcl
 * @date 2022/1/19 9:38
 */
public interface HelloService {
    /**
     * rpc test
     * @param msg
     * @return String
     */
   public String sayHello(String msg);
}

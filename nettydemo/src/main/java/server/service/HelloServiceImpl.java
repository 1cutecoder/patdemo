package server.service;

/**
 * @author zcl
 * @date 2022/1/19 11:00
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHello(String msg) {
        return msg+" received successfully";
    }
}

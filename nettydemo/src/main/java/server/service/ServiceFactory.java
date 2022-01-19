package server.service;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zcl
 * @date 2022/1/19 9:47
 */
public class ServiceFactory {
    static Properties properties;
    static Map<Class<?>,Object> map = new ConcurrentHashMap<>();

    static {

    }
}

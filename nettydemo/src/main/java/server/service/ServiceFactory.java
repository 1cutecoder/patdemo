package server.service;

import config.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zcl
 * @date 2022/1/19 9:47
 */
public class ServiceFactory {
    static Properties properties;
    static Map<Class<?>, Object> map = new ConcurrentHashMap<>();

    static {
        InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties = new Properties();
            properties.load(in);
            Set<String> names = properties.stringPropertyNames();
            for (String name : names) {
                if (name.endsWith("Service")) {
                    Class<?> interfaceClass = Class.forName(name);
                    Class<?> instanceClass = Class.forName(properties.getProperty(name));
                    map.put(interfaceClass, instanceClass.newInstance());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static  <T>T getService(Class<T> interfaceClass) {
        return (T) map.get(interfaceClass);
    }
}

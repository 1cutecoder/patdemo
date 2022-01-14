package netty.protocol;


import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;

/**
 * @author zcl
 * @date 2022/1/13 16:38
 * 序列化工具类
 */
public interface Serializer {
    <T> T deSerializer(Class<T> clazz, byte[] bytes);

    <T> byte[] serializer(T object);

    enum Algorithom implements Serializer {
        Java {
            @Override
            public <T> T deSerializer(Class<T> clazz, byte[] bytes) {

                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));

                    return (T) ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public <T> byte[] serializer(T object) {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                //6.获取内容的字节数组
                ObjectOutputStream outputStream = null;
                byte[] bytes = null;
                try {
                    outputStream = new ObjectOutputStream(arrayOutputStream);
                    outputStream.writeObject(object);
                    bytes = arrayOutputStream.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bytes;
            }
        },
        Json{

            @Override
            public <T> T deSerializer(Class<T> clazz, byte[] bytes) {
                ObjectMapper mapper = new ObjectMapper();
                T t = null;
                try {
                    t = mapper.readValue(bytes, clazz);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return t;
            }

            @Override
            public <T> byte[] serializer(T object) {
                ObjectMapper mapper = new ObjectMapper();
                byte[] bytes = new byte[0];
                try {
                    bytes = mapper.writeValueAsBytes(object);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bytes;
            }
        }
    }
}

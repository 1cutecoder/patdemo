package config;


import io.netty.channel.ChannelFuture;
import message.Message;
import message.RpcRequestMessage;
import message.RpcResponseMessage;
import protocol.Serializer;
import server.handler.RpcResponseMessageHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zcl
 */
public abstract class Config {
    static Properties properties;

    static {
        InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        String value = properties.getProperty("server.port");
        if (value == null) {
            return 8080;
        } else {
            return Integer.parseInt(value);
        }
    }

    public static Serializer.Algorithm getSerializerAlgorithm() {
        String value = properties.getProperty("serializer.algorithm");
        if (value == null) {
            return Serializer.Algorithm.Java;
        } else {
            return Serializer.Algorithm.valueOf(value);
        }
    }

    public static void main(String[] args) {
        RpcRequestMessage message = new RpcRequestMessage(
                1,
                "server.service.HelloService",
                "sayHello",
                String.class, new Class[]{String.class},
                new Object[]{"张三"}
        );
        byte[] bytes = Config.getSerializerAlgorithm().serializer(message);
        Serializer.Algorithm algorithm = Config.getSerializerAlgorithm();
        Message newMessage = null;
        Class<? extends Message> messageClass = Message.getMessageClass(101);
        newMessage = algorithm.deSerializer(messageClass, bytes);
        System.out.println(newMessage);

        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
        rpcResponseMessage.setReturnValue("received");
        rpcResponseMessage.setMessageType(102);
        byte[] bytes1 = Config.getSerializerAlgorithm().serializer(rpcResponseMessage);
        Message newMessage1 = null;
        Class<? extends Message> messageClass1 = Message.getMessageClass(102);
        newMessage1 = algorithm.deSerializer(messageClass1, bytes1);
        System.out.println(newMessage1);
    }
}

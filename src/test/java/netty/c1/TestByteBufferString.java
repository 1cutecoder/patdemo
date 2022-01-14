package netty.c1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static netty.c1.ByteBufferUtil.debugAll;

/**
 * 类描述
 *
 * @author zcl
 * @Description bytebuffer 字符串转换
 * @Date 2022/1/2 14:13
 */
public class TestByteBufferString {
    public static void main(String[] args) {
        System.out.println("first bytebuffer-------------------");
        byte[] bytes = "hello".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(bytes);
        debugAll(buffer);
        System.out.println("second charset--------------------");
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("hello");
        debugAll(buffer1);
        System.out.println("wrap---------------------");
        ByteBuffer buffer2 = ByteBuffer.wrap("hello".getBytes());
        debugAll(buffer2);
        System.out.println("buffer2.toString())-----------------------------------");
        System.out.println(StandardCharsets.UTF_8.decode(buffer2).toString());
        System.out.println("buffer decode------------------");
        System.out.println(StandardCharsets.UTF_8.decode(buffer).toString());
    }
}

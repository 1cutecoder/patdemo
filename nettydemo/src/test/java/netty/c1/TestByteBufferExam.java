package netty.c1;

import java.nio.ByteBuffer;

import static netty.c1.ByteBufferUtil.debugAll;

/**
 * 类描述
 *
 * @author zcl
 * @Description 处理粘包半包
 * @Date 2022/1/2 15:07
 */
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,World\nI'm Zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {

           if (source.get(i) == '\n') {
               int length = i - source.position() + 1;
               ByteBuffer target = ByteBuffer.allocate(length);
               for (int j = 0; j < length; j++) {
                   target.put(source.get());
               }
               debugAll(target);
           }
        }
        source.compact();

    }
}

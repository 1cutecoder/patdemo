package netty.c1;


import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static netty.c1.ByteBufferUtil.debugAll;

/**
 * @author zcl
 * @date 2021/12/31 15:58
 */
@Slf4j
public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();
       // buffer.get(new byte[4]);
        debugAll(buffer);
      /*buffer.rewind();
      debugAll(buffer);
      System.out.println((char)buffer.get());*/
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.mark();
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.reset();
        debugAll(buffer);
    }
}

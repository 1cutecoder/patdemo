package netty.c1;

import java.nio.ByteBuffer;

/**
 * @author zcl
 * @date 2021/12/31 17:26
 */
public class TestByteBufferAllocate {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(16).getClass());
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
    }
}

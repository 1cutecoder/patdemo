package netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import static netty.c4.TestByteBuf.log;

/**
 * 类描述
 *
 * @author zcl
 * @Description 切片
 * @Date 2022/1/9 12:12
 */
@Slf4j
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a','b','c','d','e','f','g','h','i','j'});
        log(buf);
        //切片过程中没有发生数据复制
        ByteBuf buf1 = buf.slice(0, 5);
        ByteBuf buf2 = buf.slice(5, 5);
        buf1.setByte(0,'d');
//        buf1.writeByte('x');
        log(buf1);
        log(buf2);
        log.debug("释放原有bytebuf 内存");
        buf1.retain();
        buf2.retain();
        buf.release();
        log(buf1);
    }
}

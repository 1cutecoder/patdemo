package netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;


/**
 * 类描述
 *
 * @author zcl
 * @Description ByteBuf创建
 * @Date 2022/1/8 16:35
 */
public class TestByteBuf {
    public static void main(String[] args) {
        //环境变量 -Dio.netty.allocator.type=unpooled
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();
        System.out.println(buf.getClass());
       log(buf);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append('a');
        }
        buf.writeBytes(sb.toString().getBytes());
        log(buf);
    }

    public static void log(ByteBuf buf) {
        int length = buf.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder sb = new StringBuilder(rows *80 * 2)
                .append("read index:").append(buf.readerIndex())
                .append(" write index:").append(buf.writerIndex())
                .append(" capacity:").append(buf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(sb,buf);
        System.out.println(sb.toString());

    }

}

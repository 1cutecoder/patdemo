package netty.advance.c1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述
 *
 * @author zcl
 * @Description 字段长度解码器
 * @Date 2022/1/9 17:54
 */
@Slf4j
public class TestLengthFieldDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,0,4,1,4),
                new LoggingHandler(LogLevel.DEBUG));
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        send(byteBuf, "Hello, World");
        send(byteBuf, "Hi");
        channel.writeInbound(byteBuf);
    }

    public static void send(ByteBuf byteBuf, String content) {
        byte[] bytes = content.getBytes();
        int length = bytes.length;
        byteBuf.writeInt(length);
        byteBuf.writeByte(1);
        byteBuf.writeBytes(bytes);
    }
}

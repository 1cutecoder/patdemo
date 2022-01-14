package netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import netty.message.LoginRequestMessage;

/**
 * @author zcl
 * @date 2022/1/12 10:15
 */
@Slf4j
public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new ProtocolFrameDecoder(),
                new LoggingHandler(),
                new MessageCodec());
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        //channel.writeOutbound(message);
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null,message,buf);
        ByteBuf buf1 = buf.slice(0, 100);
        ByteBuf buf2 = buf.slice(100, buf.readableBytes() - 100);
        buf1.retain();
        buf2.retain();
        channel.writeInbound(buf1);
        channel.writeInbound(buf2);
    }
}

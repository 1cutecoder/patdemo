package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import message.Message;

import java.util.List;

/**
 * @author zcl
 * @date 2022/1/10 17:57
 * 必须和LengthFieldBasedFrameDecoder一起使用，确保接受的ByteBuf是完整的
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf,Message> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        //magic num
        out.writeBytes(new byte[]{1,2,3,4});
        //version
        out.writeByte(1);
        //序列化方式 0 jdk 1 json
        out.writeByte(0);
        //指令类型
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        //对齐用 2的整数倍
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.Java.serializer(msg);
        //7.长度
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerAlgorithm = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content,0,length); Message message = null;
        if (serializerAlgorithm == 0) {
            message = Serializer.Algorithm.Java.deSerializer(Message.class, content);
        }
        log.debug("magicNum:{},version:{},serializerAlgorithm:{},messageType:{},sequenceId:{},length:{}",magicNum,version,serializerAlgorithm,messageType,sequenceId,length);
        log.debug("message:{}",message);
        out.add(message);
    }
}

package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import message.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author zcl
 * @date 2022/1/10 17:57
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
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
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        //6.获取内容的字节数组
        ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream);
        outputStream.writeObject(msg);
        byte[] bytes = arrayOutputStream.toByteArray();
        //7.长度
        out.writeInt(bytes.length);
        out.writeBytes(bytes);


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
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(content));
            message = (Message) ois.readObject();
        }
        log.debug("magicNum:{},version:{},serializerAlgorithm:{},messageType:{},sequenceId:{},length:{}",magicNum,version,serializerAlgorithm,messageType,sequenceId,length);
        log.debug("message:{}",message);
        out.add(message);

    }
}

package netty.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author zcl
 * @date 2022/1/12 14:16
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {
    public ProtocolFrameDecoder(){
        this(1024,12,4,0,0);
    }
    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}

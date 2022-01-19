package message;

 import com.fasterxml.jackson.annotation.JsonProperty;
 import lombok.*;

/**
 * @author zcl
 */
@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends Message {
    /**
     * 返回值
     */
    @JsonProperty("returnValue")
    private Object returnValue;
    /**
     * 异常值
     */
    @JsonProperty("exceptionValue")
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}

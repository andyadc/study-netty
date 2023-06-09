package com.andyadc.bh.rpc.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Serializable {

    public static final int RequestMessage = 1;
    public static final int ResponseMessage = 2;

    private static final long serialVersionUID = -4506084110102330691L;

    private int sequenceId;
    private int messageType;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(RequestMessage, RpcRequestMessage.class);
        messageClasses.put(ResponseMessage, RpcResponseMessage.class);
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    public abstract int getMessageType();

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    @Override
    public String toString() {
        return "Message{" +
                "sequenceId=" + sequenceId +
                ", messageType=" + messageType +
                '}';
    }
}

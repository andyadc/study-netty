package com.andyadc.bh.rpc.message;

import java.io.Serializable;

public abstract class Message implements Serializable {

    public static final int RequestMessage = 1;
    public static final int ResponseMessage = 2;
    private static final long serialVersionUID = -4506084110102330691L;
    private int sequenceId;
    private int messageType;

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
}

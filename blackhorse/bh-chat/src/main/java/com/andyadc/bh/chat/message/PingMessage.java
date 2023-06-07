package com.andyadc.bh.chat.message;

public class PingMessage extends Message {

    private static final long serialVersionUID = 8481656985171883330L;

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}

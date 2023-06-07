package com.andyadc.bh.chat.message;

public class PongMessage extends Message {

    private static final long serialVersionUID = 9015277205205375192L;

    @Override
    public int getMessageType() {
        return PongMessage;
    }
}

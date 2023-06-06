package com.andyadc.bh.chat.message;

public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}

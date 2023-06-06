package com.andyadc.bh.chat.message;

public class ChatRequestMessage extends Message {

    private static final long serialVersionUID = -3724182743318365367L;

    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }
}

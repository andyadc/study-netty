package com.andyadc.bh.chat.message;

public class GroupQuitRequestMessage extends Message {

    private static final long serialVersionUID = -7792296957517517314L;

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}

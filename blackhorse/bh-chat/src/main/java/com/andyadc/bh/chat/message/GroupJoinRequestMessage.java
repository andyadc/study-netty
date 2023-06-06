package com.andyadc.bh.chat.message;

public class GroupJoinRequestMessage extends Message {

    private static final long serialVersionUID = 4068716292347547553L;

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }
}

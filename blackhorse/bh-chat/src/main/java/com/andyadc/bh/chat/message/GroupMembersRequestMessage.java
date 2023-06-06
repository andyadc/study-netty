package com.andyadc.bh.chat.message;

public class GroupMembersRequestMessage extends Message {

    private static final long serialVersionUID = 583427065669971386L;

    @Override
    public int getMessageType() {
        return GroupMembersRequestMessage;
    }
}

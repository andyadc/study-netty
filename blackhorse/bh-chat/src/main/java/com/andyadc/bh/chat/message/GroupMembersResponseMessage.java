package com.andyadc.bh.chat.message;

public class GroupMembersResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = 58296653715261651L;

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }
}

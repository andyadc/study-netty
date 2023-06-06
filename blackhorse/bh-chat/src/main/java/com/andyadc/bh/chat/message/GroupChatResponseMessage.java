package com.andyadc.bh.chat.message;

public class GroupChatResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = -7841937257848463459L;

    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
    }
}

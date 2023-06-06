package com.andyadc.bh.chat.message;

public class GroupCreateRequestMessage extends Message {

    private static final long serialVersionUID = 3193734443179579235L;

    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}

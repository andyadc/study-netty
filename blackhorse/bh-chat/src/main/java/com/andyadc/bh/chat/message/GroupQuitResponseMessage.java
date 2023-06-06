package com.andyadc.bh.chat.message;

public class GroupQuitResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = 3349393234691366270L;

    @Override
    public int getMessageType() {
        return GroupQuitResponseMessage;
    }
}

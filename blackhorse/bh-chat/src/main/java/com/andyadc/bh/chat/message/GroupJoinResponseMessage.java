package com.andyadc.bh.chat.message;

public class GroupJoinResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = -6143793066259991276L;

    @Override
    public int getMessageType() {
        return GroupJoinResponseMessage;
    }
}

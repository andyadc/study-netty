package com.andyadc.bh.chat.message;

public class GroupChatRequestMessage extends Message {

    private static final long serialVersionUID = -6226487210897958733L;

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}

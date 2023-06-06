package com.andyadc.bh.chat.message;

public class ChatResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = -4177221009419761759L;

    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }
}

package com.andyadc.bh.chat.message;

public class ChatResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = -4177221009419761759L;

    private String from;
    private String content;

    public ChatResponseMessage() {
    }

    public ChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }

    @Override
    public String toString() {
        return "ChatResponseMessage{" +
                "from=" + from +
                ", content=" + content +
                "} " + super.toString();
    }
}

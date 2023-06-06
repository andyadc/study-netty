package com.andyadc.bh.chat.message;

public class ChatRequestMessage extends Message {

    private static final long serialVersionUID = -3724182743318365367L;

    private String from;
    private String to;
    private String content;

    public ChatRequestMessage() {
    }

    public ChatRequestMessage(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }

    @Override
    public String toString() {
        return "ChatRequestMessage{" +
                "from=" + from +
                ", to=" + to +
                ", content=" + content +
                "} " + super.toString();
    }
}

package com.andyadc.bh.chat.message;

public class GroupChatResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = -7841937257848463459L;

    private String from;
    private String content;

    public GroupChatResponseMessage() {
    }

    public GroupChatResponseMessage(String from, String content) {
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
        return GroupChatResponseMessage;
    }

    @Override
    public String toString() {
        return "GroupChatResponseMessage{" +
                "from=" + from +
                ", content=" + content +
                "} " + super.toString();
    }
}

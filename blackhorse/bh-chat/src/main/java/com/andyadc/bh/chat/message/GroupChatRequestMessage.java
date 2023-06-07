package com.andyadc.bh.chat.message;

public class GroupChatRequestMessage extends Message {

    private static final long serialVersionUID = -6226487210897958733L;

    private String from;
    private String groupName;
    private String content;

    public GroupChatRequestMessage() {
    }

    public GroupChatRequestMessage(String from, String groupName, String content) {
        this.from = from;
        this.groupName = groupName;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }

    @Override
    public String toString() {
        return "GroupChatRequestMessage{" +
                "from=" + from +
                ", groupName=" + groupName +
                ", content=" + content +
                "} " + super.toString();
    }
}

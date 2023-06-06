package com.andyadc.bh.chat.message;

public class GroupQuitRequestMessage extends Message {

    private static final long serialVersionUID = -7792296957517517314L;

    private String username;
    private String groupName;

    public GroupQuitRequestMessage() {
    }

    public GroupQuitRequestMessage(String username, String groupName) {
        this.username = username;
        this.groupName = groupName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}

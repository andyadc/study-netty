package com.andyadc.bh.chat.message;

public class GroupJoinRequestMessage extends Message {

    private static final long serialVersionUID = 4068716292347547553L;

    private String username;
    private String groupName;

    public GroupJoinRequestMessage() {
    }

    public GroupJoinRequestMessage(String username, String groupName) {
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
        return GroupJoinRequestMessage;
    }

    @Override
    public String toString() {
        return "GroupJoinRequestMessage{" +
                "username=" + username +
                ", groupName=" + groupName +
                "} " + super.toString();
    }
}

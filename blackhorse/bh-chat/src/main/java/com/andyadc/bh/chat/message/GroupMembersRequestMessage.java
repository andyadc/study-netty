package com.andyadc.bh.chat.message;

public class GroupMembersRequestMessage extends Message {

    private static final long serialVersionUID = 583427065669971386L;

    private String groupName;

    public GroupMembersRequestMessage() {
    }

    public GroupMembersRequestMessage(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int getMessageType() {
        return GroupMembersRequestMessage;
    }
}

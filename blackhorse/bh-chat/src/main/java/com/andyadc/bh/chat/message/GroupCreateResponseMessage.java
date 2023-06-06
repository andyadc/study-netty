package com.andyadc.bh.chat.message;

public class GroupCreateResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = 5533811333941664043L;

    private String groupName;

    public GroupCreateResponseMessage() {
    }

    public GroupCreateResponseMessage(String groupName) {
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
        return GroupCreateResponseMessage;
    }
}

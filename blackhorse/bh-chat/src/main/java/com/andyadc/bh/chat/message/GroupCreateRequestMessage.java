package com.andyadc.bh.chat.message;

import java.util.Set;

public class GroupCreateRequestMessage extends Message {

    private static final long serialVersionUID = 3193734443179579235L;

    private String creator;
    private String groupName;
    private Set<String> members;

    public GroupCreateRequestMessage() {
    }

    public GroupCreateRequestMessage(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }

    @Override
    public String toString() {
        return "GroupCreateRequestMessage{" +
                "creator=" + creator +
                ", groupName=" + groupName +
                ", members=" + members +
                "} " + super.toString();
    }
}

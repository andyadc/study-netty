package com.andyadc.bh.chat.message;

import java.util.Set;

public class GroupCreateRequestMessage extends Message {

    private static final long serialVersionUID = 3193734443179579235L;

    private String groupName;
    private Set<String> members;

    public GroupCreateRequestMessage() {
    }

    public GroupCreateRequestMessage(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
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
}

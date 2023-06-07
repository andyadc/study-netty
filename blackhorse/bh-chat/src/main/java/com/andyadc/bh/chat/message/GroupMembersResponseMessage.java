package com.andyadc.bh.chat.message;

import java.util.Set;

public class GroupMembersResponseMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = 58296653715261651L;

    private Set<String> members;

    public GroupMembersResponseMessage() {
    }

    public GroupMembersResponseMessage(Set<String> members) {
        this.members = members;
    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }

    @Override
    public String toString() {
        return "GroupMembersResponseMessage{" +
                "members=" + members +
                "} " + super.toString();
    }
}

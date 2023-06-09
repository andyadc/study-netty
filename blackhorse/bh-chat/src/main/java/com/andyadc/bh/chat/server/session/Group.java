package com.andyadc.bh.chat.server.session;

import java.util.Collections;
import java.util.Set;

/**
 * 聊天组
 */
public class Group {

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    // 聊天室名称
    private String name;
    // 聊天室成员
    private Set<String> members;

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name=" + name +
                ", members=" + members +
                '}';
    }
}

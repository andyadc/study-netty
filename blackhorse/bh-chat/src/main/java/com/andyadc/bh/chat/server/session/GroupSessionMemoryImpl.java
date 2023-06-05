package com.andyadc.bh.chat.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

public class GroupSessionMemoryImpl implements GroupSession {

    @Override
    public Group createGroup(String name, Set<String> members) {
        return null;
    }

    @Override
    public Group joinMember(String name, String member) {
        return null;
    }

    @Override
    public Group removeMember(String name, String member) {
        return null;
    }

    @Override
    public Group removeGroup(String name) {
        return null;
    }

    @Override
    public Set<String> getMembers(String name) {
        return null;
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        return null;
    }
}

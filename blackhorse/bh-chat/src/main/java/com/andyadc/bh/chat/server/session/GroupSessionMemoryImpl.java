package com.andyadc.bh.chat.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GroupSessionMemoryImpl implements GroupSession {

    private static final Map<String, Group> groupMembersMap = new ConcurrentHashMap<>();

    @Override
    public Group createGroup(String name, Set<String> members) {
        Group group = null;
        if (groupMembersMap.get(name) == null) {
            group = new Group(name, members);
            groupMembersMap.put(name, group);
        }
        return group;
    }

    @Override
    public Group joinMember(String name, String member) {
        return groupMembersMap.computeIfPresent(name, (k, v) -> {
            v.getMembers().add(member);
            return v;
        });
    }

    @Override
    public Group removeMember(String name, String member) {
        return groupMembersMap.computeIfPresent(name, (k, v) -> {
            v.getMembers().remove(member);
            return v;
        });
    }

    @Override
    public Group removeGroup(String name) {
        return groupMembersMap.remove(name);
    }

    @Override
    public Set<String> getMembers(String name) {
        return groupMembersMap.get(name).getMembers();
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        return getMembers(name)
                .stream()
                .map(member -> SessionFactory.getSession().getChannel(member))
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                ;
    }
}
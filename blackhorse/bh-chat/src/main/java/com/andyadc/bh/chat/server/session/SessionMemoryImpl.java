package com.andyadc.bh.chat.server.session;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMemoryImpl implements Session {

    private static final Map<String, Channel> userChannel = new ConcurrentHashMap<>();

    @Override
    public void bind(Channel channel, String username) {
        userChannel.put(username, channel);
    }

    @Override
    public void unbind(Channel channel) {
        Collection<Channel> values = userChannel.values();
    }

    @Override
    public Object getAttr(Channel channel, String name) {
        return null;
    }

    @Override
    public void setAttr(Channel channel, String name, Object value) {

    }

    @Override
    public Channel getChannel(String username) {
        return userChannel.get(username);
    }
}

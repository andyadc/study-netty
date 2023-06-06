package com.andyadc.bh.chat.server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMemoryImpl implements Session {

    private static final Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    private static final Map<Channel, String> channelUserMap = new ConcurrentHashMap<>();
    private final Map<Channel, Map<String, Object>> channelAttrMap = new ConcurrentHashMap<>();// 每个 channel 包含的属性

    @Override
    public void bind(Channel channel, String username) {
        userChannelMap.put(username, channel);
        channelUserMap.put(channel, username);
        channelAttrMap.put(channel, new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(Channel channel) {
        String username = channelUserMap.remove(channel);
        if (username != null) {
            userChannelMap.remove(username);
            channelAttrMap.remove(channel);
        }
    }

    @Override
    public Object getAttr(Channel channel, String name) {
        return channelAttrMap.get(channel).get(name);
    }

    @Override
    public void setAttr(Channel channel, String name, Object value) {
        channelAttrMap.get(channel).put(name, value);
    }

    @Override
    public Channel getChannel(String username) {
        return userChannelMap.get(username);
    }
}

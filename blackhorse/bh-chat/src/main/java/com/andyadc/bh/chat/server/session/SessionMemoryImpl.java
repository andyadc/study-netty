package com.andyadc.bh.chat.server.session;

import io.netty.channel.Channel;

public class SessionMemoryImpl implements Session {

    @Override
    public void bind(Channel channel, String username) {

    }

    @Override
    public void unbind(Channel channel) {

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
        return null;
    }
}

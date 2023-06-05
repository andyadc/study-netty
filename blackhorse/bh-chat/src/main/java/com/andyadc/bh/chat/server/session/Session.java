package com.andyadc.bh.chat.server.session;

import io.netty.channel.Channel;

/**
 * 会话管理
 */
public interface Session {

    /**
     * 绑定会话
     *
     * @param channel  channel
     * @param username 用户名
     */
    void bind(Channel channel, String username);

    /**
     * 解绑会话
     *
     * @param channel 需要解绑的会话
     */
    void unbind(Channel channel);

    /**
     * 获取属性值
     *
     * @param channel channel
     * @param name    属性名
     * @return 属性值
     */
    Object getAttr(Channel channel, String name);

    /**
     * 设置属性
     *
     * @param channel channel
     * @param name    属性名
     * @param value   属性值
     */
    void setAttr(Channel channel, String name, Object value);

    /**
     * 根据用户名获取channel
     *
     * @param username 用户名
     * @return channel
     */
    Channel getChannel(String username);
}

package com.andyadc.bh.chat.server.handler;

import com.andyadc.bh.chat.message.GroupChatRequestMessage;
import com.andyadc.bh.chat.message.GroupChatResponseMessage;
import com.andyadc.bh.chat.server.session.GroupSession;
import com.andyadc.bh.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class GroupChatHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String from = msg.getFrom();
        String content = msg.getContent();

        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        List<Channel> membersChannel = groupSession.getMembersChannel(groupName);
        membersChannel.forEach(channel -> {
            GroupChatResponseMessage groupChatResponseMessage = new GroupChatResponseMessage(from, content);
            groupChatResponseMessage.setSuccess(true);
            channel.writeAndFlush(groupChatResponseMessage);
        });
    }
}

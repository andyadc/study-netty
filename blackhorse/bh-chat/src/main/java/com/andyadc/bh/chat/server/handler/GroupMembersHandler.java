package com.andyadc.bh.chat.server.handler;

import com.andyadc.bh.chat.message.GroupMembersRequestMessage;
import com.andyadc.bh.chat.message.GroupMembersResponseMessage;
import com.andyadc.bh.chat.server.session.GroupSession;
import com.andyadc.bh.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;

@ChannelHandler.Sharable
public class GroupMembersHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();

        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Set<String> members = groupSession.getMembers(groupName);

        GroupMembersResponseMessage groupMembersResponseMessage = new GroupMembersResponseMessage();
        groupMembersResponseMessage.setMembers(members);
        groupMembersResponseMessage.setSuccess(true);

        ctx.writeAndFlush(groupMembersResponseMessage);
    }
}

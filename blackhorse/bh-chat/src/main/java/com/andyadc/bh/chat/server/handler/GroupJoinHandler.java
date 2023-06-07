package com.andyadc.bh.chat.server.handler;

import com.andyadc.bh.chat.message.GroupJoinRequestMessage;
import com.andyadc.bh.chat.message.GroupJoinResponseMessage;
import com.andyadc.bh.chat.server.session.Group;
import com.andyadc.bh.chat.server.session.GroupSession;
import com.andyadc.bh.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupJoinHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();

        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.joinMember(groupName, username);

        GroupJoinResponseMessage groupJoinResponseMessage = new GroupJoinResponseMessage();
        if (group == null) {
            groupJoinResponseMessage.setSuccess(false);
            groupJoinResponseMessage.setMessage("join group [" + groupName + "] error");
        } else {
            groupJoinResponseMessage.setSuccess(true);
            groupJoinResponseMessage.setMessage("join group [" + groupName + "] success");
        }
        ctx.writeAndFlush(groupJoinResponseMessage);
    }
}

package com.andyadc.bh.chat.server.handler;

import com.andyadc.bh.chat.message.GroupQuitRequestMessage;
import com.andyadc.bh.chat.message.GroupQuitResponseMessage;
import com.andyadc.bh.chat.server.session.Group;
import com.andyadc.bh.chat.server.session.GroupSession;
import com.andyadc.bh.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupQuitHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();

        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.removeMember(groupName, username);

        GroupQuitResponseMessage groupQuitResponseMessage = new GroupQuitResponseMessage();
        if (group == null) {
            groupQuitResponseMessage.setSuccess(false);
            groupQuitResponseMessage.setMessage("Quit group [" + groupName + "] error");
        } else {
            groupQuitResponseMessage.setSuccess(true);
            groupQuitResponseMessage.setMessage("Quit group [" + groupName + "] success");
        }
        ctx.writeAndFlush(groupQuitResponseMessage);
    }
}

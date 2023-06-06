package com.andyadc.bh.chat.server.handler;

import com.andyadc.bh.chat.message.GroupCreateRequestMessage;
import com.andyadc.bh.chat.message.GroupCreateResponseMessage;
import com.andyadc.bh.chat.server.session.Group;
import com.andyadc.bh.chat.server.session.GroupSession;
import com.andyadc.bh.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

@ChannelHandler.Sharable
public class GroupCreateHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);

        GroupCreateResponseMessage groupCreateResponseMessage = new GroupCreateResponseMessage(groupName);
        if (group == null) {
            groupCreateResponseMessage.setSuccess(false);
            groupCreateResponseMessage.setError("create error");
            ctx.writeAndFlush(groupCreateResponseMessage);
            return;
        }
        groupCreateResponseMessage.setSuccess(true);
        ctx.writeAndFlush(groupCreateResponseMessage);

        // 发送拉群消息
        List<Channel> membersChannel = groupSession.getMembersChannel(groupName);
        membersChannel.forEach((channel) -> {
            channel.writeAndFlush(groupCreateResponseMessage);
        });
    }
}

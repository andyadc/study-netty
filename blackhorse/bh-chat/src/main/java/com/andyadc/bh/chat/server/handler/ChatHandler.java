package com.andyadc.bh.chat.server.handler;

import com.andyadc.bh.chat.message.ChatRequestMessage;
import com.andyadc.bh.chat.message.ChatResponseMessage;
import com.andyadc.bh.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String from = msg.getFrom();
        String to = msg.getTo();
        String content = msg.getContent();
        Channel toChannel = SessionFactory.getSession().getChannel(to);
        if (toChannel != null) {
            ChatResponseMessage chatResponseMessage = new ChatResponseMessage(from, content);
            chatResponseMessage.setSuccess(true);
            toChannel.writeAndFlush(chatResponseMessage);
        } else {
            ChatResponseMessage chatResponseMessage = new ChatResponseMessage();
            chatResponseMessage.setSuccess(false);
            chatResponseMessage.setError("[" + to + "] offline");
            ctx.channel().writeAndFlush(chatResponseMessage);
        }
    }
}

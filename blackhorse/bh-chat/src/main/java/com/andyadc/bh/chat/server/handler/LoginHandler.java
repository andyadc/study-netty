package com.andyadc.bh.chat.server.handler;

import com.andyadc.bh.chat.message.LoginRequestMessage;
import com.andyadc.bh.chat.message.LoginResponseMessage;
import com.andyadc.bh.chat.server.service.UserServiceFactory;
import com.andyadc.bh.chat.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LoginHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage loginResponseMessage;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            loginResponseMessage = new LoginResponseMessage(true);
        } else {
            loginResponseMessage = new LoginResponseMessage(false, "login failed");
        }
        ctx.writeAndFlush(loginResponseMessage);
    }
}

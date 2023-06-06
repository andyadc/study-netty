package com.andyadc.bh.chat.server.handler;

import com.andyadc.bh.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(QuitHandler.class);

    /**
     * 连接断开时
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SessionFactory.getSession().unbind(channel);
        logger.info("{} disconnect", channel);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        SessionFactory.getSession().unbind(channel);
        logger.info("{} disconnect cause {}", channel, cause.getMessage());

        super.exceptionCaught(ctx, cause);
    }
}

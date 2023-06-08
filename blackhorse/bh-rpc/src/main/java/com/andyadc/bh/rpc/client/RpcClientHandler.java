package com.andyadc.bh.rpc.client;

import com.andyadc.bh.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        logger.info("{}", msg);
    }
}

package com.andyadc.bh.starter.test;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class EmbeddedChannelTests {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedChannelTests.class);

    public static void main(String[] args) {

        ChannelInboundHandlerAdapter handler1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.info("handler 1");
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter handler2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.info("handler 2");
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter handler3 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.info("handler 3");
                super.channelRead(ctx, msg);
            }
        };

        ChannelOutboundHandlerAdapter handler4 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.info("handler 4");
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter handler5 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.info("handler 5");
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter handler6 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.info("handler 6");
                super.write(ctx, msg, promise);
            }
        };

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(handler1, handler2, handler3, handler4, handler5, handler6);

        // 模拟入站操作
        embeddedChannel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes(StandardCharsets.UTF_8)));

        // 模拟出站操作
        embeddedChannel.writeOutbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("netty".getBytes(StandardCharsets.UTF_8)));

    }
}

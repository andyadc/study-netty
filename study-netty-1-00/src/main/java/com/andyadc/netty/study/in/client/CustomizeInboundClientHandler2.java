package com.andyadc.netty.study.in.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class CustomizeInboundClientHandler2 extends ChannelInboundHandlerAdapter {

    private final String className = this.getClass().getSimpleName();
    private final String prefix = className + " - ";

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty 2".getBytes(CharsetUtil.UTF_8)));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("receive data from server: " + ctx.channel().remoteAddress()
                + ", msg: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}

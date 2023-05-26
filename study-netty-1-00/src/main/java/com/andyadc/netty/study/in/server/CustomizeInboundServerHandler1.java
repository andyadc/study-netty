package com.andyadc.netty.study.in.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class CustomizeInboundServerHandler1 extends ChannelInboundHandlerAdapter {

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

        super.channelActive(ctx);
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
        System.out.println("receive data from client: " + ctx.channel().remoteAddress()
                + ", data: " + byteBuf.toString(CharsetUtil.UTF_8));

        //获取到线程池eventLoop，添加线程，执行
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3L);
                System.out.println("I wake up");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        ctx.writeAndFlush(Unpooled.copiedBuffer("server received you message.".getBytes(CharsetUtil.UTF_8)));
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

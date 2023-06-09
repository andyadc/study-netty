package com.andyadc.netty.study.out.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

public class CustomizeOutboundServerHandler3 extends ChannelOutboundHandlerAdapter {

    private final String className = this.getClass().getSimpleName();
    private final String prefix = className + " - ";

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.bind(ctx, localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.disconnect(ctx, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.close(ctx, promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.deregister(ctx, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.write(ctx, msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        String methodName = new Object() {
        }.getClass().getEnclosingMethod().getName();

        System.out.println(prefix + methodName);

        super.flush(ctx);
    }
}

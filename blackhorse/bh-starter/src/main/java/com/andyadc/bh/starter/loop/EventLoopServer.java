package com.andyadc.bh.starter.loop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class EventLoopServer {

    private static final Logger logger = LoggerFactory.getLogger(EventLoopServer.class);

    public static void main(String[] args) {
        EventLoopGroup busyGroup = new NioEventLoopGroup(1, new ThreadFactory() {
            final AtomicInteger count = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "busyMan-" + count.getAndDecrement());
            }
        });

        new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                logger.info("- {}", byteBuf.toString(CharsetUtil.UTF_8));

                                ctx.fireChannelRead(msg); // 将消息传递给下一个handler
                            }
                        });

                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                logger.warn("-- {}", byteBuf.toString(CharsetUtil.UTF_8));

                                ctx.fireChannelRead(msg); // 将消息传递给下一个handler
                            }
                        });

                        // 指定新的group执行
                        channel.pipeline().addLast(busyGroup, "busyMan", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                logger.warn("--- {}", byteBuf.toString(CharsetUtil.UTF_8));
                            }
                        });
                    }
                }).bind(8987);

        logger.info("server started.");
    }
}

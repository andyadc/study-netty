package com.andyadc.bh.starter.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {

    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        // 服务端启动器
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class) // 支持的ServerSocketChannel实现, nio/oio/epoll/...
                // boss 处理连接, worker(child)处理读写,
                .childHandler(new ChannelInitializer<NioSocketChannel>() { // channel 代表和客户端进行数据读写通道, Initializer初始化, 负责添加handler
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new StringDecoder());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                });

        serverBootstrap.bind(8987);
    }
}

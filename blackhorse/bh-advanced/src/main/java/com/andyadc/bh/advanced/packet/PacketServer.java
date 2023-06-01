package com.andyadc.bh.advanced.packet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketServer {

    private static final Logger logger = LoggerFactory.getLogger(PacketServer.class);

    public static void main(String[] args) throws Exception {
        EventLoopGroup boss = new NioEventLoopGroup(2);
        EventLoopGroup worker = new NioEventLoopGroup(4);

        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
//                .option(ChannelOption.SO_RCVBUF, 10) // 调整系统的接收缓冲区(滑动窗口)滑动窗口
//                .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16)) // 调整Netty的接收缓冲区
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new FixedLengthFrameDecoder(10));
                        channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(9879).sync();
        logger.info("server start on {}", 9879);

        channelFuture.channel().closeFuture().sync();

        logger.info("server stop");
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}

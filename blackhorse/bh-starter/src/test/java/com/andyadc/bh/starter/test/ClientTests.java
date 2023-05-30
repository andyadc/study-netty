package com.andyadc.bh.starter.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ClientTests {

    private static final Logger logger = LoggerFactory.getLogger(ClientTests.class);

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(2);
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        channel.pipeline().addLast(new StringEncoder());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8987);
        Channel channel = channelFuture.sync().channel();
        logger.info("{}", channel);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("quit".equalsIgnoreCase(line)) {
                    channel.close();
                    logger.info("close");
                    return;
                }
                channel.writeAndFlush(line);
            }
        }, "console-read").start();

        ChannelFuture closeFuture = channel.closeFuture();
        logger.info("waiting close {}", closeFuture);
        closeFuture.sync();
        logger.info("channel closed {}", closeFuture);

        group.shutdownGracefully();

//        closeFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                logger.info("{} closed", channelFuture);
//        group.shutdownGracefully();
//            }
//        });
    }

}

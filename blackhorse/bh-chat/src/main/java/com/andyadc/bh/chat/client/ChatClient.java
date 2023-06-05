package com.andyadc.bh.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatClient {

    private static final Logger logger = LoggerFactory.getLogger(ChatClient.class);

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline().addLast(loggingHandler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9897).sync();
            logger.info("client started");

            channelFuture.channel().closeFuture().sync();
            logger.info("client closed");
        } catch (Exception e) {
            logger.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}

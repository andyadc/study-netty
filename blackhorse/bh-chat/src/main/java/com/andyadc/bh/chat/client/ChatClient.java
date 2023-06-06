package com.andyadc.bh.chat.client;

import com.andyadc.bh.chat.protocol.ProtocolFrameDecoder;
import com.andyadc.bh.chat.protocol.SharableMessageCodec;
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
        SharableMessageCodec messageCodec = new SharableMessageCodec();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new ProtocolFrameDecoder());
                            channel.pipeline().addLast(loggingHandler);
                            channel.pipeline().addLast(messageCodec);

                            channel.pipeline().addLast("client-handler", new ClientHandler());
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

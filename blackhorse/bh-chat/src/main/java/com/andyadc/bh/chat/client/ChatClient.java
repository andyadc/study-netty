package com.andyadc.bh.chat.client;

import com.andyadc.bh.chat.message.PingMessage;
import com.andyadc.bh.chat.protocol.ProtocolFrameDecoder;
import com.andyadc.bh.chat.protocol.SharableMessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
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
//                            channel.pipeline().addLast(loggingHandler);
                            channel.pipeline().addLast(messageCodec);

                            // 判断是否 读空闲时间过长或者写空闲时间过长
                            channel.pipeline().addLast(new IdleStateHandler(0, 5, 0));
                            channel.pipeline().addLast(new ChannelDuplexHandler() {
                                // 用来触发特殊事件
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    IdleStateEvent event = (IdleStateEvent) evt;
                                    // 触发写空闲事件
                                    if (event.state() == IdleState.WRITER_IDLE) {
//                                        logger.info("write no data exceed");
                                        ctx.channel().writeAndFlush(new PingMessage());
                                    }
                                }
                            });

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

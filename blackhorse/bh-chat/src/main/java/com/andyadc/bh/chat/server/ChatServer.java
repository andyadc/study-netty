package com.andyadc.bh.chat.server;

import com.andyadc.bh.chat.protocol.ProtocolFrameDecoder;
import com.andyadc.bh.chat.protocol.SharableMessageCodec;
import com.andyadc.bh.chat.server.handler.ChatHandler;
import com.andyadc.bh.chat.server.handler.GroupChatHandler;
import com.andyadc.bh.chat.server.handler.GroupCreateHandler;
import com.andyadc.bh.chat.server.handler.GroupJoinHandler;
import com.andyadc.bh.chat.server.handler.GroupMembersHandler;
import com.andyadc.bh.chat.server.handler.GroupQuitHandler;
import com.andyadc.bh.chat.server.handler.LoginHandler;
import com.andyadc.bh.chat.server.handler.QuitHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatServer {

    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        SharableMessageCodec messageCodec = new SharableMessageCodec();

        LoginHandler loginHandler = new LoginHandler();
        ChatHandler chatHandler = new ChatHandler();
        GroupCreateHandler groupCreateHandler = new GroupCreateHandler();
        GroupChatHandler groupChatHandler = new GroupChatHandler();
        GroupJoinHandler groupJoinHandler = new GroupJoinHandler();
        GroupMembersHandler groupMembersHandler = new GroupMembersHandler();
        GroupQuitHandler groupQuitHandler = new GroupQuitHandler();
        QuitHandler quitHandler = new QuitHandler();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {

                            channel.pipeline().addLast(new ProtocolFrameDecoder());
                            channel.pipeline().addLast(loggingHandler);
                            channel.pipeline().addLast(messageCodec);

                            // 判断是否 读空闲时间过长或者写空闲时间过长
                            channel.pipeline().addLast(new IdleStateHandler(10, 0, 0));
                            channel.pipeline().addLast(new ChannelDuplexHandler() {
                                // 用来触发特殊事件
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    IdleStateEvent event = (IdleStateEvent) evt;
                                    // 触发读空闲事件
                                    if (event.state() == IdleState.READER_IDLE) {
                                        logger.warn("read no data exceed");
                                        ctx.channel().close();
                                    }
                                }
                            });

                            channel.pipeline().addLast(loginHandler);
                            channel.pipeline().addLast(chatHandler);
                            channel.pipeline().addLast(groupCreateHandler);
                            channel.pipeline().addLast(groupChatHandler);
                            channel.pipeline().addLast(groupJoinHandler);
                            channel.pipeline().addLast(groupMembersHandler);
                            channel.pipeline().addLast(groupQuitHandler);
                            channel.pipeline().addLast(quitHandler);

                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(9897).sync();
            logger.info("server started");

            channelFuture.channel().closeFuture().sync();
            logger.info("server closed");
        } catch (Exception e) {
            logger.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

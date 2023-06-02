package com.andyadc.bh.advanced.packet;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Random;

public class PacketClient {

    private static final Logger logger = LoggerFactory.getLogger(PacketClient.class);

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup(3);
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
//                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//                            @Override
//                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
//                                for (int i = 0; i < 10; i++) {
//                                    ByteBuf buf = ctx.alloc().buffer(16);
//                                    buf.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
//                                    ctx.channel().writeAndFlush(buf);
//                                }
//
//                                super.channelActive(ctx);
//                            }
//                        });

                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ByteBuf buf = ctx.alloc().buffer();
                                Random random = new SecureRandom();
                                char c = '0';
                                for (int i = 0; i < 10; i++) {
                                    String str = makeString(c, random.nextInt(256) + 1);
                                    c++;
                                    buf.writeBytes(str.getBytes(CharsetUtil.UTF_8));
                                }
                                ctx.channel().writeAndFlush(buf);
                                super.channelActive(ctx);
                            }
                        });

                    }
                });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9879).sync();

        channelFuture.channel().closeFuture().sync();
        logger.info("client stop");

        group.shutdownGracefully();
    }

    private static String makeString(char c, int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(c);
        }
        builder.append("\n");
        return builder.toString();
    }
}

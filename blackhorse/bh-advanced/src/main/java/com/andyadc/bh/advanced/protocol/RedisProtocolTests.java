package com.andyadc.bh.advanced.protocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

public class RedisProtocolTests {

    /**
     * set key value
     * set name zhangsan
     * *3 3个字段
     * $3 set
     * $4 name
     * $8 zhangshan
     */
    public static void main(String[] args) throws Exception {
        final byte[] line = {13, 10}; // 换行

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new LoggingHandler());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                // set name zhangsan
                                ByteBuf buffer = ctx.alloc().buffer();
                                buffer.writeBytes("*3".getBytes(CharsetUtil.UTF_8));
                                buffer.writeBytes(line);
                                buffer.writeBytes("$3".getBytes(CharsetUtil.UTF_8));
                                buffer.writeBytes(line);
                                buffer.writeBytes("set".getBytes(CharsetUtil.UTF_8));
                                buffer.writeBytes(line);
                                buffer.writeBytes("$4".getBytes(CharsetUtil.UTF_8));
                                buffer.writeBytes(line);
                                buffer.writeBytes("name".getBytes(CharsetUtil.UTF_8));
                                buffer.writeBytes(line);
                                buffer.writeBytes("$8".getBytes(CharsetUtil.UTF_8));
                                buffer.writeBytes(line);
                                buffer.writeBytes("zhangsan".getBytes(CharsetUtil.UTF_8));
                                buffer.writeBytes(line);

                                ctx.channel().writeAndFlush(buffer);
                                super.channelActive(ctx);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6379).sync();

        channelFuture.channel().closeFuture().sync();

        group.shutdownGracefully();
    }
}

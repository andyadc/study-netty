package com.andyadc.bh.starter.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipelineTests {

    private static final Logger logger = LoggerFactory.getLogger(PipelineTests.class);

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {

                        ChannelPipeline pipeline = channel.pipeline();

                        // netty 默认内置2个handler head -> xxx -> tail

                        pipeline.addLast("handler-7", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                logger.info("handler 7 write");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("handler-1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                logger.info("handler 1 read");
                                ByteBuf byteBuf = (ByteBuf) msg;
                                String s = byteBuf.toString(CharsetUtil.UTF_8);
                                super.channelRead(ctx, s); // 将数据传递给下一个handler, 如果不调用, 调用链会断开
                            }
                        });

                        pipeline.addLast("handler-2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                logger.info("handler 2 read");
                                logger.info("{}", msg);
                                Student student = new Student((String) msg);
                                super.channelRead(ctx, student);
                            }
                        });

                        pipeline.addLast("handler-3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                logger.info("handler 3 read");
                                Student student = (Student) msg;
                                logger.info("Timestamp - {}", student.getTimestamp());

//                                ctx.channel().writeAndFlush(ctx.alloc().buffer().writeBytes("hello".getBytes(CharsetUtil.UTF_8)));

                                // 从当前位置向前查找触发出站(outbound)事件
                                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server".getBytes(CharsetUtil.UTF_8)));

                                super.channelRead(ctx, msg);
                            }
                        });

                        // outbound

                        pipeline.addLast("handler-4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                logger.info("handler 4 write");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("handler-5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                logger.info("handler 5 write");
                                super.write(ctx, msg, promise);
                            }
                        });

                        pipeline.addLast("handler-6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                logger.info("handler 6 write");
                                super.write(ctx, msg, promise);
                            }
                        });

                    }
                })
                .bind(8987);

        logger.info("server started");
    }

    static class Student {
        private final Long timestamp;
        private String name;

        public Student(String name) {
            this.name = name;
            this.timestamp = System.currentTimeMillis();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getTimestamp() {
            return timestamp;
        }
    }
}

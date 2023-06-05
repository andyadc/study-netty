package com.andyadc.bh.advanced.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProtocolServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpProtocolServer.class);

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        channel.pipeline().addLast(new HttpServerCodec()); // http

                        // 根据消息类型(泛型)选择处理
                        channel.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                                logger.info("{}", msg.uri());

                                // 返回响应对象
                                byte[] bytes = "<h1>hello netty</h1>".getBytes(CharsetUtil.UTF_8);
                                DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
                                httpResponse.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
                                httpResponse.content().writeBytes(bytes);

                                //
                                ctx.channel().writeAndFlush(httpResponse);
                            }
                        });

//                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//                            @Override
//                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                // 一次HTTP请求, Netty HttpServerCodec会调用2次
//                                logger.info("msg - {}", msg);
//                                if (msg instanceof HttpRequest) { // 请求行, 请求头
//
//                                } else if (msg instanceof HttpContent) { // 请求体
//
//                                }
//
//                                super.channelRead(ctx, msg);
//                            }
//                        });

                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();

        channelFuture.channel().closeFuture().sync();

        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}

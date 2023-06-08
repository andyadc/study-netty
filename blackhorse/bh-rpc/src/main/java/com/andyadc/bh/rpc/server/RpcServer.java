package com.andyadc.bh.rpc.server;

import com.andyadc.bh.rpc.protocol.ProtocolFrameDecoder;
import com.andyadc.bh.rpc.protocol.SharableMessageCodec;
import com.andyadc.bh.rpc.server.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        SharableMessageCodec messageCodec = new SharableMessageCodec();
        RpcServerHandler rpcServerHandler = new RpcServerHandler();

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
                            channel.pipeline().addLast(rpcServerHandler);

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

package com.andyadc.bh.rpc.client;

import com.andyadc.bh.rpc.message.RpcRequestMessage;
import com.andyadc.bh.rpc.protocol.ProtocolFrameDecoder;
import com.andyadc.bh.rpc.protocol.SharableMessageCodec;
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

public class RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();

        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        SharableMessageCodec messageCodec = new SharableMessageCodec();
        RpcClientHandler rpcClientHandler = new RpcClientHandler();

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
                            channel.pipeline().addLast(rpcClientHandler);

                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9897).sync();
            logger.info("client started");

            // test
            RpcRequestMessage message = new RpcRequestMessage();
            message.setClassName("com.andyadc.bh.rpc.server.service.HelloService");
            message.setMethodName("hello");
            message.setParameterTypes(new Class[]{String.class});
            message.setParameterValues(new Object[]{"netty"});
            message.setReturnType(String.class);
            channelFuture.channel().writeAndFlush(message);

            channelFuture.channel().closeFuture().sync();
            logger.info("client closed");
        } catch (Exception e) {
            logger.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}

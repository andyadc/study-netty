package com.andyadc.bh.rpc.client;

import com.andyadc.bh.rpc.message.RpcRequestMessage;
import com.andyadc.bh.rpc.protocol.ProtocolFrameDecoder;
import com.andyadc.bh.rpc.protocol.SequenceIdGenerator;
import com.andyadc.bh.rpc.protocol.SharableMessageCodec;
import com.andyadc.bh.rpc.server.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

public class RpcClientManager {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientManager.class);

    private static final Object LOCK = new Object();
    private static Channel channel;

    public static void main(String[] args) throws Exception {
        HelloService service = getProxyService(HelloService.class);
        String result = service.hello("proxy");
        System.out.println(">>> " + result);
//        service.hello("java");
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T getProxyService(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, (proxy, method, args) -> {
            RpcRequestMessage message = new RpcRequestMessage();
            int sequenceId = SequenceIdGenerator.nextId();
            message.setSequenceId(sequenceId);
            message.setClassName(service.getName());
            message.setMethodName(method.getName());
            message.setParameterTypes(method.getParameterTypes());
            message.setParameterValues(args);
            message.setReturnType(method.getReturnType());

            Channel channel = getChannel();
            channel.writeAndFlush(message);

            // 准备一个 Promise 对象来接收结果, 指定 Promise 对象异步接收结果线程
            DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
            RpcClientHandler.promiseMap.put(sequenceId, promise);
            promise.await(); // 等待结果
            if (promise.isSuccess()) {
                return promise.getNow();
            }
            throw new RuntimeException(promise.cause());
        });
    }

    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) {
            if (channel != null) {
                return channel;
            }
            initChannel();
        }
        return channel;
    }

    /**
     * 初始化 channel
     */
    public static void initChannel() {
        EventLoopGroup group = new NioEventLoopGroup();

        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        SharableMessageCodec messageCodec = new SharableMessageCodec();
        RpcClientHandler rpcClientHandler = new RpcClientHandler();

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

        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9897).sync();
            logger.info("client started");
            channel = channelFuture.channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
                logger.info("client closed");
            });
        } catch (Exception e) {
            logger.error("client error", e);
        }
    }
}

package com.andyadc.bh.starter.test;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NettyFutureTests {

    private static final Logger logger = LoggerFactory.getLogger(NettyFutureTests.class);

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup group = new NioEventLoopGroup(3);
        EventLoop eventLoop = group.next();

        eventLoop.execute(() -> {
            logger.info("execute");
        });

        Future<Integer> future = eventLoop.submit(() -> {
            logger.info("submit");
            TimeUnit.SECONDS.sleep(3);
            return 500;
        });

        logger.info("- {}", future.getNow()); // 非阻塞
        logger.info("- {}", future.get()); // 阻塞
        logger.info("-- {}", future.sync().get());

        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                logger.info("--- {}", future.get());
            }
        });

        eventLoop.shutdownGracefully();
        group.shutdownGracefully();
    }
}

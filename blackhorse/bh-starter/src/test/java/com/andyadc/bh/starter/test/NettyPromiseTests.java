package com.andyadc.bh.starter.test;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NettyPromiseTests {

    private static final Logger logger = LoggerFactory.getLogger(NettyPromiseTests.class);

    public static void main(String[] args) throws Exception {

        EventLoop eventLoop = new NioEventLoopGroup().next();

        //
        Promise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {

            logger.info("sleep 3s");
            try {
                int i = 1 / 0;
                TimeUnit.SECONDS.sleep(3);
                promise.setSuccess(100);
            } catch (Exception e) {
                promise.setFailure(e);
                e.printStackTrace();
            }

        }).start();

        logger.info("{}", promise.get());
    }
}

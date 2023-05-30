package com.andyadc.bh.starter.test;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopTests {

    private static final Logger logger = LoggerFactory.getLogger(EventLoopTests.class);

    @Test
    private void testEventLoopGroup() {
        // 创建事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2); // io事件, 普通任务, 定时任务
        EventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup(); // 普通任务, 定时任务

        // 获取下一个事件循环组
        System.out.println(eventLoopGroup.next());
        System.out.println(eventLoopGroup.next());
        System.out.println(eventLoopGroup.next());

        // 执行任务
        eventLoopGroup.execute(() -> {
            logger.info("eventLoopGroup");
        });

        // 执行定时任务
        eventLoopGroup.next().scheduleAtFixedRate(() -> {
            logger.info("schedule");
        }, 3, 2, TimeUnit.SECONDS);

        logger.info("--- end ---");
    }
}

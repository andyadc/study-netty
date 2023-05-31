package com.andyadc.bh.starter.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class JdkFutureTests {

    private static final Logger logger = LoggerFactory.getLogger(JdkFutureTests.class);

    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
            logger.info("execute");
        });

        Future<Integer> future = executorService.submit(() -> {
            logger.info("submit");
            TimeUnit.SECONDS.sleep(3);
            return 50;
        });

        Integer result = future.get();
        logger.info("{}", result);

        executorService.shutdown();
    }
}

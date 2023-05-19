package com.andyadc.netty.study.test;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tests {

    private static final Logger logger = LoggerFactory.getLogger(Tests.class);

    @Test
    public void test() {
        logger.info("--- info ---");
        logger.debug("--- debug ---");
        logger.warn("--- warn ---");
        logger.error("--- error ---");
        logger.trace("--- trace ---");
    }
}

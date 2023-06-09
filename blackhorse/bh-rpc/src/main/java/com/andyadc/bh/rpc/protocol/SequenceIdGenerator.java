package com.andyadc.bh.rpc.protocol;

import java.util.concurrent.atomic.AtomicInteger;

public final class SequenceIdGenerator {

    public static final AtomicInteger id = new AtomicInteger(0);

    public static int nextId() {
        return id.getAndIncrement();
    }
}

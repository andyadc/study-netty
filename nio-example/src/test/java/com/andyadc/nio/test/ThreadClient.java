package com.andyadc.nio.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ThreadClient {

    private static final Logger logger = LoggerFactory.getLogger(ThreadClient.class);

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(7079));
        logger.info("client connected. {}", socketChannel);

        socketChannel.write(ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8)));

        System.in.read();
    }
}

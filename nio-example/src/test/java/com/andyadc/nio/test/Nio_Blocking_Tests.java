package com.andyadc.nio.test;

import com.andyadc.nio.example.utils.ByteBufferUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Nio_Blocking_Tests {

    private static final Logger logger = LoggerFactory.getLogger(Nio_Blocking_Tests.class);

    @Test
    public void testClient() throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9997));

        socketChannel.write(ByteBuffer.wrap("hello nio\nhello nio\n".getBytes(StandardCharsets.UTF_8)));

        System.in.read();
    }

    /**
     * nio 阻塞模式, 单线程
     */
    @Test
    public void testNonBlockingServer() throws Exception {
        // 创建服务断
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false); // 设置非阻塞

        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9997));

        ByteBuffer buffer = ByteBuffer.allocate(16);

        //
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
            // 建立客户端链接
            SocketChannel socketChannel = serverSocketChannel.accept(); // 非阻塞, 如果没有链接则返回null,
            if (socketChannel != null) {
                logger.info("connnected... {}", socketChannel);
                socketChannel.configureBlocking(false); // 设置非阻塞模式
                channelList.add(socketChannel);

            }

            for (SocketChannel channel : channelList) {
                int read = channel.read(buffer);// 非阻塞, 如果没有读到则返回 0,
                if (read > 0) {
                    buffer.flip();
                    ByteBufferUtil.debugAll(buffer);
                    buffer.clear();
                    logger.info("after read... {}", channel);
                }
            }
        }
    }

    /**
     * nio 阻塞模式, 单线程
     */
    @Test
    public void testBlockingServer() throws Exception {
        // 创建服务断
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9997));

        ByteBuffer buffer = ByteBuffer.allocate(16);

        //
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
            logger.info("connecting...");
            // 建立客户端链接
            SocketChannel socketChannel = serverSocketChannel.accept(); // 阻塞方法
            logger.info("connnected... {}", socketChannel);
            channelList.add(socketChannel);
            for (SocketChannel channel : channelList) {
                logger.info("before read... {}", channel);
                channel.read(buffer); // 阻塞方法

                buffer.flip();
                ByteBufferUtil.debugAll(buffer);
                buffer.clear();
                logger.info("after read... {}", channel);
            }
        }
    }
}

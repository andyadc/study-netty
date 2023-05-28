package com.andyadc.nio.test;

import com.andyadc.nio.example.utils.ByteBufferUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorTests {

    private static final Logger logger = LoggerFactory.getLogger(SelectorTests.class);

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 找到一条完整的信息
            if (source.get(i) == '\n') {
                int len = i + 1 - source.position();
                // 把这个完整的信息写入新的
                ByteBuffer target = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    target.put(source.get());
                }
                ByteBufferUtil.debugAll(target);
            }
        }
        source.compact();
    }

    @Test
    public void testSelectorServer() throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // 创建 selector, 管理多个 channel
        Selector selector = Selector.open();
        // 建立联系
        // SelectionKey 事件发生后, 可以知道事件和 channel
        SelectionKey serverSelectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);
//        selectionKey.interestOps();
        logger.info("ServerSocketChannel register selector - {}", serverSelectionKey);

        serverSocketChannel.bind(new InetSocketAddress(9997));

        while (true) {
            int select = selector.select(); // 没有事件会阻塞, 但在时间未处理时, 不会阻塞, 事件发生后要不处理, 要不取消

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator(); // 需要动态删除, 必须要用迭代器
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                logger.info("iterator key - {}", key);
                keyIterator.remove(); // 处理完必须删除

                // 区分事件类型
                if (key.isAcceptable()) { // accept 事件
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = channel.accept();
                    logger.info("accept socketChannel - {}", socketChannel);

                    socketChannel.configureBlocking(false);
                    ByteBuffer att = ByteBuffer.allocate(5);
                    SelectionKey socketSelectionKey = socketChannel.register(selector, 0, att);
                    socketSelectionKey.interestOps(SelectionKey.OP_READ);
                    logger.info("SocketChannel register selector - {}", socketSelectionKey);
                }

                if (key.isReadable()) { // read 事件
                    SelectableChannel selectableChannel = key.channel();
                    SocketChannel socketChannel = (SocketChannel) selectableChannel;
                    logger.info("read socketChannel - {}", selectableChannel);
                    try {
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = socketChannel.read(buffer);
                        if (read > 0) {
//                            buffer.flip();
//                            ByteBufferUtil.debugRead(buffer);
//                            logger.info("read data - {}", StandardCharsets.UTF_8.decode(buffer).toString());
                            split(buffer);
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer tmp = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                tmp.put(buffer);
                                key.attach(tmp);
                            }
                        } else {
                            // 断开连接也是读事件
                            logger.info("channel read 0");
                            socketChannel.close();
                            key.cancel();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        socketChannel.close();
                        key.cancel(); // 断开连接需要将 key取消
                    }
                }
            }
        }
    }
}

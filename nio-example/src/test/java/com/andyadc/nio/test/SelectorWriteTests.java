package com.andyadc.nio.test;

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
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class SelectorWriteTests {

    private static final Logger logger = LoggerFactory.getLogger(SelectorWriteTests.class);

    @Test
    public void testWriteClient() throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.configureBlocking(false);

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9998));
        logger.info("client connected. {}", socketChannel);

        // 接受数据
        int count = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            count += socketChannel.read(buffer);
            logger.info("current count - {}", count);

            buffer.clear();
        }
    }

    @Test
    public void testWriteServer() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9998));
        logger.info("server bind. {}", serverSocketChannel);

        while (true) {
            selector.select();

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();

                if (selectionKey.isAcceptable()) {
                    logger.info("Acceptable >>>");

                    SocketChannel socketChannel = serverSocketChannel.accept(); // 当前就是同一个 ServerSocketChannel
                    socketChannel.configureBlocking(false);
                    SelectionKey key = socketChannel.register(selector, 0, null);
                    key.interestOps(SelectionKey.OP_READ);

                    // 向客户端发送大量数据
                    StringBuilder data = new StringBuilder();
                    for (int i = 0; i < 5000000; i++) {
                        data.append("a");
                    }
                    ByteBuffer dataBuffer = StandardCharsets.UTF_8.encode(data.toString());
//                    ByteBuffer dataBuffer = Charset.defaultCharset().encode(data.toString());

                    // 返回值代表实际写入数量
                    int writed = socketChannel.write(dataBuffer);
                    logger.info("Writed {}", writed);

                    while (dataBuffer.hasRemaining()) { // TODO ? 无线循环, 始终大于0
                        logger.info("remaining - {}", dataBuffer.remaining());
                        // 关注可写事件
                        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE); // 添加

                        // 把未写完的数据放入SelectionKey
                        key.attach(dataBuffer);
//                        break;
                    }
                }

                if (selectionKey.isWritable()) {
                    logger.info("Writable >>>");
                    ByteBuffer dataBuffer = (ByteBuffer) selectionKey.attachment();

                    SelectableChannel channel = selectionKey.channel();
                    SocketChannel socketChannel = (SocketChannel) channel;

                    int writed = socketChannel.write(dataBuffer);
                    logger.info("Writed {}", writed);

                    //清理
                    if (!dataBuffer.hasRemaining()) {
                        logger.info("remove op_write");
                        selectionKey.attach(null); //内容写完, 需要清除 buffer
                        selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE); // 去除可写事件
                    }
                }
            }

        }
    }
}

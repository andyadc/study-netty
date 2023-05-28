package com.andyadc.nio.test;

import com.andyadc.nio.example.utils.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadServer {

    private static final Logger logger = LoggerFactory.getLogger(ThreadServer.class);

    public static void main(String[] args) throws Exception {
        Thread.currentThread().setName("boss");

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        serverSocketChannel.bind(new InetSocketAddress(7079));
        logger.info("server started. {}", serverSocketChannel);

        // 创建固定数量 worker
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }

        AtomicInteger idx = new AtomicInteger(0);
        while (true) {
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    logger.info("Accepting... {}", socketChannel);

                    // 关联
                    workers[idx.getAndIncrement() % workers.length].register(socketChannel);
                    logger.info("after register");
                }
            }
        }
    }

    static class Worker implements Runnable {

        private final String name;
        private final ConcurrentLinkedQueue<Runnable> runnableQueue = new ConcurrentLinkedQueue<>();
        private Thread thread;
        private Selector selector;
        private volatile boolean start = false;

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel socketChannel) throws IOException {
            if (!start) {
                selector = Selector.open();
                thread = new Thread(this, name);
                thread.start();
                start = true;
            }
            // 向队列添加任务, 任务没有立即执行
            runnableQueue.add(() -> {
                try {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            this.selector.wakeup(); //唤醒 selector
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select(); // 阻塞, 需要wakeup
                    Runnable task = runnableQueue.poll();
                    if (task != null) {
                        task.run(); // 执行了socketChannel.register
                    }
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        keyIterator.remove();
                        if (selectionKey.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            logger.info("Reading... {}", socketChannel);
                            socketChannel.read(buffer);
                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

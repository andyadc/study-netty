package com.andyadc.nio.example.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    private static final int PORT = 8789;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public GroupChatServer() {
        try {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", PORT));
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }

    /**
     * 监听，并且接受客户端消息，转发到其他客户端
     */
    public void listen() {
        try {
            while (true) {
                int timeout = selector.select(3000L);
                if (timeout > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + " online");
                        }
                        if (selectionKey.isReadable()) {
                            read(selectionKey);
                        }
                        keyIterator.remove();
                    }
                } else {
                    System.out.println("Waiting ...");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(byteBuffer);
            if (count > 0) {
                String data = new String(byteBuffer.array(), StandardCharsets.UTF_8);
                System.out.println("from client: " + data);
                notifyAllClients(socketChannel, data);
            }
        } catch (Exception e) {
            e.printStackTrace();

            try {
                System.out.println(socketChannel.getRemoteAddress() + " offline");
                selectionKey.channel();
                socketChannel.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void notifyAllClients(SocketChannel excludeSocketChannel, String data) throws IOException {
        System.out.println(">>>");
        for (SelectionKey selectionKey : selector.keys()) {
            Channel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && channel != excludeSocketChannel) {
                SocketChannel socketChannel = (SocketChannel) channel;
                ByteBuffer byteBuffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
                socketChannel.write(byteBuffer);
            }
        }
    }
}

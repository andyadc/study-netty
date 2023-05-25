package com.andyadc.nio.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIOClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8989);
        //连接服务器
        boolean connect = socketChannel.connect(address);
        //判断是否连接成功
        if (!connect) {
            //等待连接的过程中
            while (!socketChannel.finishConnect()) {
                System.out.println("连接服务器需要时间，期间可以做其他事情...");
            }
        }

        String message = "hello nio";
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));

        socketChannel.write(byteBuffer);

        System.in.read();
    }
}

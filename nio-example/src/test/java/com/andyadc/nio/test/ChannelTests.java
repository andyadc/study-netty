package com.andyadc.nio.test;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ChannelTests {

    @Test
    public void testSocketChannel() throws Exception {
        //获取ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8789);
        //绑定地址，端口号
        serverSocketChannel.bind(address);
        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true) {
            //获取SocketChannel
            SocketChannel channel = serverSocketChannel.accept();
            while (channel.read(byteBuffer) != -1) {
                System.out.println(new String(byteBuffer.array(), StandardCharsets.UTF_8));
                //清空缓冲区
                byteBuffer.clear();
            }
        }
    }

    @Test
    public void testFileChannel() throws Exception {
        File file = new File("pom.xml");
        FileInputStream inputStream = new FileInputStream(file);
        //从文件输入流获取通道
        FileChannel inputStreamChannel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("pom");
        //从文件输出流获取通道
        FileChannel outputStreamChannel = outputStream.getChannel();

        //创建一个byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //把输入流通道的数据读取到缓冲区
        inputStreamChannel.read(byteBuffer);

        //切换成读模式
        byteBuffer.flip();

        //把数据从缓冲区写入到输出流通道
        outputStreamChannel.write(byteBuffer);

        //关闭通道
        outputStream.close();
        inputStream.close();
        outputStreamChannel.close();
        inputStreamChannel.close();
    }

    @Test
    public void testFileChannel2() throws Exception {

        File file = new File("pom.xml");
        FileInputStream inputStream = new FileInputStream(file);
        //从文件输入流获取通道
        FileChannel inputStreamChannel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("pom");
        //从文件输出流获取通道
        FileChannel outputStreamChannel = outputStream.getChannel();

        //创建一个byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //transferTo：把源通道的数据传输到目的通道中。
        inputStreamChannel.transferTo(0, byteBuffer.limit(), outputStreamChannel);

        //关闭通道
        outputStream.close();
        inputStream.close();
        outputStreamChannel.close();
        inputStreamChannel.close();
    }

    @Test
    public void testFileChannel3() throws Exception {
        File file = new File("pom.xml");
        FileInputStream inputStream = new FileInputStream(file);
        //从文件输入流获取通道
        FileChannel inputStreamChannel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("pom");
        //从文件输出流获取通道
        FileChannel outputStreamChannel = outputStream.getChannel();

        //创建一个byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // transferFrom：把来自源通道的数据传输到目的通道。
        outputStreamChannel.transferFrom(inputStreamChannel, 0, byteBuffer.limit());

        //关闭通道
        outputStream.close();
        inputStream.close();
        outputStreamChannel.close();
        inputStreamChannel.close();
    }

    /**
     * 分散读取，聚合写入
     */
    @Test
    public void testFileChannel4() throws Exception {
        File file = new File("nio.txt");
        FileInputStream inputStream = new FileInputStream(file);
        //从文件输入流获取通道
        FileChannel inputStreamChannel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("2.txt");
        //从文件输出流获取通道
        FileChannel outputStreamChannel = outputStream.getChannel();

        //创建三个缓冲区，分别都是5
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(5);
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(5);
        ByteBuffer byteBuffer3 = ByteBuffer.allocate(5);

        // 创建一个缓冲区数组
        ByteBuffer[] byteBuffers = new ByteBuffer[]{byteBuffer1, byteBuffer2, byteBuffer3};
        //循环写入到buffers缓冲区数组中，分散读取
        long read;
        long sumLength = 0;
        while ((read = inputStreamChannel.read(byteBuffers)) != -1) {
            sumLength += read;
            Arrays.stream(byteBuffers)
                    .map(buffer -> " posstion=" + buffer.position() + ", limit=" + buffer.limit())
                    .forEach(System.out::println);

            //切换模式
            Arrays.stream(byteBuffers).forEach(Buffer::flip);
            //聚合写入到文件输出通道
            outputStreamChannel.write(byteBuffers);
            //清空缓冲区
            Arrays.stream(byteBuffers).forEach(Buffer::clear);
        }

        System.out.println("总长度: " + sumLength);
        //关闭通道
        outputStream.close();
        inputStream.close();
        outputStreamChannel.close();
        inputStreamChannel.close();
    }

    @Test
    public void testFileChannel5() throws Exception {
        long start = System.currentTimeMillis();
        File file = new File("D:\\temp\\Postman.exe");
        FileInputStream inputStream = new FileInputStream(file);
        //从文件输入流获取通道
        FileChannel inputStreamChannel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("D:\\temp\\Postman2.exe");
        //从文件输出流获取通道
        FileChannel outputStreamChannel = outputStream.getChannel();

        //创建一个直接缓冲区
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(5 * 1024 * 1024);
        //创建一个非直接缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(5 * 1024 * 1024);

        //写入到缓冲区
        while (inputStreamChannel.read(byteBuffer) != -1) {
            //切换读模式
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        //关闭通道
        outputStream.close();
        inputStream.close();
        outputStreamChannel.close();
        inputStreamChannel.close();

        System.out.println("消耗时间: " + (System.currentTimeMillis() - start));
    }
}

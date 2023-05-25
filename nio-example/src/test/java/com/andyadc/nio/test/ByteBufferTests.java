package com.andyadc.nio.test;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteBufferTests {

    /**
     * HeapByteBuffer所创建的字节缓冲区就是在JVM堆中的，即JVM内部所维护的字节数组。
     * DirectByteBuffer是直接操作操作系统本地代码创建的内存缓冲数组。
     */
    @Test
    public void testByteBuffer1() {
        // JVM堆内内存块Buffer(非直接缓冲区)
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(1024);
        System.out.println(byteBuffer1);

        ByteBuffer byteBuffer2 = ByteBuffer.wrap("adc".getBytes(StandardCharsets.UTF_8));
        System.out.println(byteBuffer2);

        // 创建堆外内存块(直接缓冲区)DirectByteBuffer
        ByteBuffer byteBuffer3 = ByteBuffer.allocateDirect(1024);
        System.out.println(byteBuffer3);
    }

    /**
     * 缓存区是双向的，既可以往缓冲区写入数据，也可以从缓冲区读取数据。但是不能同时进行，需要切换
     */
    @Test
    public void testByteBuffer2() {
        String message = "hello nio";
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

        // 创建一个固定大小的buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //写入数据到Buffer中
        byteBuffer.put(bytes);

        //切换成读模式
        byteBuffer.flip();

        //创建一个临时数组，用于存储获取到的数据
        byte[] tempBytes = new byte[bytes.length];
        int i = 0;
        //
        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            tempBytes[i] = b;
            i++;
        }
        System.out.println(new String(tempBytes, StandardCharsets.UTF_8));
    }
}

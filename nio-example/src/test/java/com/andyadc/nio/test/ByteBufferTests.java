package com.andyadc.nio.test;

import com.andyadc.nio.example.utils.ByteBufferUtil;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteBufferTests {

    @Test
    public void testByteBufferSplit() {
        ByteBuffer source = ByteBuffer.allocate(100);
        source.put("hello world\ni'm andyadc\nho".getBytes(StandardCharsets.UTF_8));
        split(source);
        source.put("w are you\n".getBytes(StandardCharsets.UTF_8));
        split(source);
    }

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

    @Test
    public void testByteBuffer03() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte) 0x61);
        ByteBufferUtil.debugAll(byteBuffer);

        byteBuffer.put(new byte[]{0x62, 0x63});
        ByteBufferUtil.debugAll(byteBuffer);

//        System.out.println(byteBuffer.get());
//        System.out.println((char) byteBuffer.get(1));

        byteBuffer.flip();
        System.out.println((char) byteBuffer.get()); // 指针会向后走
//        System.out.println((char) byteBuffer.get(1)); // get(i) 不会移动指针position
        ByteBufferUtil.debugAll(byteBuffer);

        byteBuffer.compact();
//        byteBuffer.put((byte) 0x64);
        ByteBufferUtil.debugAll(byteBuffer);

        byteBuffer.put(new byte[]{0x65, 0x66});
        ByteBufferUtil.debugAll(byteBuffer);
    }

    @Test
    public void testByteBuffer04() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(new byte[]{'a', 'b', 'c', 'd', 'e'});
        ByteBufferUtil.debugAll(byteBuffer);

        byteBuffer.flip();

        byteBuffer.get(new byte[3]);
        ByteBufferUtil.debugAll(byteBuffer);

        // rewind 从头开始读
        byteBuffer.rewind();
        ByteBufferUtil.debugAll(byteBuffer);
        System.out.println((char) byteBuffer.get());
    }

    @Test
    public void testByteBuffer05() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(new byte[]{'a', 'b', 'c', 'd', 'e'});
        ByteBufferUtil.debugAll(byteBuffer);

        // mark & reset
        // mark 做一个标记, 记录 position 位置, reset 是讲 position 重置到 mark 位置
        byteBuffer.flip();

        System.out.println((char) byteBuffer.get());
        System.out.println((char) byteBuffer.get());
        ByteBufferUtil.debugAll(byteBuffer);

        byteBuffer.mark(); // 加标记
        System.out.println((char) byteBuffer.get());
        System.out.println((char) byteBuffer.get());

        byteBuffer.reset(); // 将 position 重置
        System.out.println((char) byteBuffer.get());
    }
}

package com.andyadc.bh.starter.test.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * -Dio.netty.allocator.type={unpooled|pooled}
 * v4.1 后默认启用池化的ByteBuf
 */
public class ByteBufTests {

    public static void main(String[] args) {

        // 默认256, 会自动扩容
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(byteBuf.getClass());
        log(byteBuf);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            builder.append("a");
        }
        byteBuf.writeBytes(builder.toString().getBytes(CharsetUtil.UTF_8));
        log(byteBuf);

        // 基于堆的 ByteBuf
        byteBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        System.out.println(byteBuf.getClass());

        // 基于直接内存的 ByteBuf
        byteBuf = ByteBufAllocator.DEFAULT.directBuffer();
        System.out.println(byteBuf.getClass());

    }

    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}

package com.andyadc.bh.starter.test.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * -Dio.netty.allocator.type={unpooled|pooled}
 * v4.1 后默认启用池化的ByteBuf
 */
public class ByteBufTests {

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

    @Test
    public void testByteBuf() {

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

    @Test
    public void testWrite() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(8);

        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});
        log(byteBuf);

        byteBuf.writeByte(5);
        log(byteBuf);

        byteBuf.writeInt(6); // 4个字节
        log(byteBuf);

        byteBuf.writeCharSequence("a", CharsetUtil.UTF_8);
        log(byteBuf);

        byte readByte = byteBuf.readByte();
        log(byteBuf);
        System.out.println(readByte);
    }

    @Test
    public void testSlice() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        byteBuf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k'});
        log(byteBuf);

        // 切片过程中没有发生数据复制
        ByteBuf slice1 = byteBuf.slice(0, 5);
        log(slice1);

        ByteBuf slice2 = byteBuf.slice(5, 5);
        log(slice2);

        slice1.setByte(1, 'x');
        log(slice1);
        log(byteBuf);

        // slice 不能扩容
//        slice1.writeByte(1);
//        log(slice1);

        // release 后不能再使用
//        byteBuf.release();
//        log(slice1);
//        log(byteBuf);

        // retain 后计数器加一,
        slice1.retain();
        byteBuf.release();
        log(slice1);
        log(byteBuf);

        // 复制所有内容, 没有max capacity限制, 与原ByteBuf共用同一块内存, 读写是独立的
        ByteBuf duplicate = byteBuf.duplicate();

        // 深拷贝, 无论读写都与原ByteBuf无关
        ByteBuf copy = byteBuf.copy();
    }

    @Test
    public void testComposite() {
        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.buffer(10);
        buffer1.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e'});

        ByteBuf buffer2 = ByteBufAllocator.DEFAULT.buffer(10);
        buffer2.writeBytes(new byte[]{'f', 'g', 'h', 'j', 'k'});

        ByteBuf buffer3 = ByteBufAllocator.DEFAULT.buffer();
        buffer3.writeBytes(buffer1).writeBytes(buffer2);
        log(buffer3);

        // CompositeByteBuf
        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        compositeByteBuf.addComponents(true, buffer1, buffer2);

        log(compositeByteBuf);
    }

}

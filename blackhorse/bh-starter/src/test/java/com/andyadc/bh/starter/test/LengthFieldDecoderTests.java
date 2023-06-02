package com.andyadc.bh.starter.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

public class LengthFieldDecoderTests {

    public static void main(String[] args) {

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        // 4 个字节的内容长度, 实际内容
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        send(byteBuf, "hello netty");
        send(byteBuf, "hello java");

        embeddedChannel.writeInbound(byteBuf);
    }

    private static void send(ByteBuf byteBuf, String message) {
        byte[] bytes = message.getBytes(CharsetUtil.UTF_8); // 实际内容
        int length = bytes.length; // 实际长度

        byteBuf.writeInt(length);
        byteBuf.writeByte(1); // version
        byteBuf.writeBytes(bytes);
    }
}

package com.andyadc.bh.chat.test;

import com.andyadc.bh.chat.message.LoginRequestMessage;
import com.andyadc.bh.chat.protocol.MessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;

public class MessageCodecTests {

    @Test
    public void testMessageCodec() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new MessageCodec()
        );

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage();
        loginRequestMessage.setNickname("adc");
        loginRequestMessage.setUsername("adc");
        loginRequestMessage.setPassword("123");

        // 模拟出站, 会调用 MessageCodec#encode
//        channel.writeOutbound(loginRequestMessage);

        // 模拟入站, 会调用 MessageCodec#decode
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, loginRequestMessage, byteBuf);
        channel.writeInbound(byteBuf);
    }

    /**
     * 半包
     */
    @Test
    public void testMessageCodec2() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                // 粘包/半包
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new MessageCodec()
        );

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage();
        loginRequestMessage.setNickname("adc");
        loginRequestMessage.setUsername("adc");
        loginRequestMessage.setPassword("123");

        // 模拟入站, 会调用 MessageCodec#decode
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, loginRequestMessage, byteBuf);

        // 模拟半包
        ByteBuf slice = byteBuf.slice(0, 100);
        ByteBuf slice2 = byteBuf.slice(100, byteBuf.readableBytes() - 100);

        slice.retain(); // 引用计数 +1

        channel.writeInbound(slice); // release
        channel.writeInbound(slice2);
    }
}

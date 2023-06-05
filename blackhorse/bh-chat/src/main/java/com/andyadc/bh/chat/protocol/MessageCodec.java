package com.andyadc.bh.chat.protocol;

import com.andyadc.bh.chat.message.Message;
import com.andyadc.bh.chat.serialization.JdkSerialization;
import com.andyadc.bh.chat.serialization.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageCodec extends ByteToMessageCodec<Message> {

    private static final Logger logger = LoggerFactory.getLogger(MessageCodec.class);

    @Override
    public void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
        // 魔数, 6个字节
        out.writeBytes(new byte[]{6, 9, 8, 7});
        // 版本
        out.writeByte(1);
        // 序列化方法, 0-jdk, 1-json
        out.writeByte(0);
        // 指令类型
        out.writeByte(message.getMessageType());
        // 请求序号, 4个字节
        out.writeInt(message.getSequenceId());
        // 无意义, 对齐填充, 一般长度为2的整数倍
        out.writeByte(0xff);
        // 序列号
        Serialization serialization = new JdkSerialization();
        byte[] bytes = serialization.serialize(message);
        // 消息长度
        out.writeInt(bytes.length);
        // 消息内容字节
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt(); // 4字节魔数
        byte version = in.readByte(); // 版本
        byte serializationType = in.readByte(); // 序列化方式
        byte messageType = in.readByte(); // 指令类型
        int sequenceId = in.readInt(); // 请求序号
        in.readByte();
        int length = in.readInt(); // 消息长度
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        Serialization serialization = new JdkSerialization();
        Message message = serialization.deserialize(bytes, Message.class);

        logger.info("{} - {} - {} - {} - {} - {} ", magicNum, version, serializationType, messageType, sequenceId, length);
        logger.info("{}", message);

        out.add(message);
    }
}

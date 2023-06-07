package com.andyadc.bh.rpc.protocol;

import com.andyadc.bh.rpc.message.Message;
import com.andyadc.bh.rpc.serialization.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 必须和LengthFieldBasedFrameDecoder一起使用, 确保接收到的消息是完整的
 */
@ChannelHandler.Sharable
public class SharableMessageCodec extends MessageToMessageCodec<ByteBuf, Message> {

    private static final Logger logger = LoggerFactory.getLogger(SharableMessageCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();

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
        // 序列化
        byte[] bytes = Serializer.Type.Json.serialize(message);
        // 消息长度
        out.writeInt(bytes.length);
        // 消息内容字节
        out.writeBytes(bytes);

        outList.add(out);
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

        Message message = Serializer.Type.Json.deserialize(bytes, Message.class);

        logger.info("{} - {} - {} - {} - {} - {} ", magicNum, version, serializationType, messageType, sequenceId, length);
        logger.info("{}", message);

        out.add(message);
    }
}

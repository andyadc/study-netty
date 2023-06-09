package com.andyadc.bh.rpc.client;

import com.andyadc.bh.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

    public static final Map<Integer, Promise<Object>> promiseMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        logger.info("{}", msg);
        int sequenceId = msg.getSequenceId();
        Promise<Object> promise = promiseMap.remove(sequenceId); // 使用完需要移除
        if (promise != null) {
            if (msg.getError() != null) {
                promise.setFailure(new Throwable(msg.getError()));
            } else {
                promise.setSuccess(msg.getResult());
            }
        }
    }
}

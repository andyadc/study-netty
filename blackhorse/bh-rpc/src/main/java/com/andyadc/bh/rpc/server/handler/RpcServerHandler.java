package com.andyadc.bh.rpc.server.handler;

import com.andyadc.bh.rpc.message.RpcRequestMessage;
import com.andyadc.bh.rpc.message.RpcResponseMessage;
import com.andyadc.bh.rpc.server.service.HelloService;
import com.andyadc.bh.rpc.server.service.ServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    // test
    public static void main(String[] args) throws Exception {
        final RpcRequestMessage message = new RpcRequestMessage();
        message.setClassName("com.andyadc.bh.rpc.server.service.HelloService");
        message.setMethodName("hello");
        message.setParameterTypes(new Class[]{String.class});
        message.setParameterValues(new Object[]{"netty"});
        message.setReturnType(String.class);

        Class<?> interfaceClazz = Class.forName(message.getClassName());
        HelloService service = (HelloService) ServiceFactory.getService(interfaceClazz);
        Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
        Object invoke = method.invoke(service, message.getParameterValues());

        System.out.println(invoke);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
        final RpcResponseMessage response = new RpcResponseMessage();
        response.setSequenceId(msg.getSequenceId());
        try {
            final Class<?> interfaceClazz = Class.forName(msg.getClassName());
            final HelloService service = (HelloService) ServiceFactory.getService(interfaceClazz);
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValues());

            response.setResult(invoke);
        } catch (Exception e) {
            response.setError(e.getMessage());
            logger.error("rpc handle error", e);
        }
        ctx.writeAndFlush(response);
    }
}

package com.andyadc.netty.study;

import com.andyadc.netty.study.in.client.CustomizeInboundClientHandler1;
import com.andyadc.netty.study.in.client.CustomizeInboundClientHandler2;
import com.andyadc.netty.study.in.client.CustomizeInboundClientHandler3;
import com.andyadc.netty.study.out.client.CustomizeOutboundClientHandler1;
import com.andyadc.netty.study.out.client.CustomizeOutboundClientHandler2;
import com.andyadc.netty.study.out.client.CustomizeOutboundClientHandler3;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            // inbound
                            socketChannel.pipeline().addLast(new CustomizeInboundClientHandler1());
                            socketChannel.pipeline().addLast(new CustomizeInboundClientHandler2());
                            socketChannel.pipeline().addLast(new CustomizeInboundClientHandler3());

                            // outbound
                            socketChannel.pipeline().addFirst(new CustomizeOutboundClientHandler1());
                            socketChannel.pipeline().addFirst(new CustomizeOutboundClientHandler2());
                            socketChannel.pipeline().addFirst(new CustomizeOutboundClientHandler3());
                        }
                    });

            System.out.println(">");

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9997).sync();
            System.out.println(">>");
            channelFuture.addListener((ChannelFutureListener) future -> System.out.println(future.isSuccess() ? "success" : "fail"));

            channelFuture.channel().closeFuture().sync();
            System.out.println(">>>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}

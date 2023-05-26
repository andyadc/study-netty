package com.andyadc.netty.study;

import com.andyadc.netty.study.in.server.CustomizeInboundServerHandler1;
import com.andyadc.netty.study.in.server.CustomizeInboundServerHandler2;
import com.andyadc.netty.study.in.server.CustomizeInboundServerHandler3;
import com.andyadc.netty.study.out.server.CustomizeOutboundServerHandler1;
import com.andyadc.netty.study.out.server.CustomizeOutboundServerHandler2;
import com.andyadc.netty.study.out.server.CustomizeOutboundServerHandler3;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    /**
     * option()设置的是服务端用于接收进来的连接，也就是bossGroup线程。
     * childOption()是提供给父管道接收到的连接，也就是workerGroup线程。
     */
    public static void main(String[] args) {
        //创建两个线程组 boosGroup、workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置两个线程组boosGroup和workerGroup
            bootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    // 服务端可连接队列个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 立即发送数据
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //给pipeline管道设置处理器

                            // inbound
                            socketChannel.pipeline().addLast(new CustomizeInboundServerHandler1());
                            socketChannel.pipeline().addLast(new CustomizeInboundServerHandler2());
                            socketChannel.pipeline().addLast(new CustomizeInboundServerHandler3());

                            // outbound
                            socketChannel.pipeline().addFirst(new CustomizeOutboundServerHandler1());
                            socketChannel.pipeline().addFirst(new CustomizeOutboundServerHandler2());
                            socketChannel.pipeline().addFirst(new CustomizeOutboundServerHandler3());
                        }
                    });

            System.out.println(">");

            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(9997).sync();
            System.out.println(">>");
            channelFuture.addListener((ChannelFutureListener) future -> System.out.println(future.isSuccess() ? "success" : "fail"));

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
            System.out.println(">>>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

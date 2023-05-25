package com.andyadc.netty.study;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * https://mp.weixin.qq.com/s?__biz=MzU1OTgzNTAzNQ==&mid=2247483871&idx=1&sn=713a6e11422a815d9f98e1ccf71d19c2&chksm=fc10732bcb67fa3d111431e5ae9792b63a92e3758acf406046db1f95705850878383da22cab8&cur_album_id=1445826382487207937&scene=189#wechat_redirect
 */
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
                            socketChannel.pipeline().addLast(new CustomeServerHandler());
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

package com.mocent.netty.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by hadoop on 2017/9/16.
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args!=null && args.length>0){
            port = Integer.valueOf(args[0]);
        }
        new TimeServer().bind(port);
    }

    public void bind(int port) {
        //创建两个NioEventLoopGroup线程组，用于处理网络事件，相当于nio到Reactor。一个用于服务端接受客户端连接，另一个用于SocketChannel读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //声明服务器启动辅助类
            ServerBootstrap b = new ServerBootstrap();
            //配置服务器辅助启动类
            b.group(bossGroup, workGroup)
                    //功能对应于nio的ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //设置NIO的ServerSocketChannel的TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //绑定IO事件的处理类ChildChannelHandler，相当于Reactor中的Handler类
                    .childHandler(new ChildChannelHandler());
            //绑定监听接口，调用同步方法等待绑定操作完成，返回ChannelFuture用于异步操作到通知回调
            ChannelFuture f = b.bind(port).sync();
            //阻塞，等待服务器链路关闭后才退出
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }
}



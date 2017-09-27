package com.mocent.netty.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by hadoop on 2017/9/19.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new TimeClient().connect(port, "127.0.0.1");
    }

    public void connect(int port, String host) {
        //处理IO读写的EventLoopGroup线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //客户端辅助启动类
            Bootstrap b = new Bootstrap();
            //配置辅助启动类
            b.group(group)
                    //配置通道 跟server不同
                    .channel(NioSocketChannel.class)
                    //配置TCP参数
                    .option(ChannelOption.TCP_NODELAY, true)
                    //配置IO处理方法
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });

            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }


}

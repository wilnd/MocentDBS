package com.mocent.netty.io.nio;


/**
 * Created by hadoop on 2017/9/15.
 */
public class TimeServer {
    public static void main(String[] args) {
        //定义服务端到默认端口号
        int port = 8080;
        //如果传了参数进来，端口号为外在参数
        if (args != null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        //创建一个多路复用器，它是一个独立到线程，负责轮询多路复用器Selector，可以处理多个客户端到并发接入
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread (timeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}

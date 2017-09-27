package com.mocent.netty.io.nio;

/**
 * Created by hadoop on 2017/9/15.
 */
public class TimeClient {
    public static void main(String[] args) {
        //定义要传输信息的服务端的端口号
        int port = 8080;
        if (args != null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        //新建一个客户端线程发起连接
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-0001").start();
    }
}

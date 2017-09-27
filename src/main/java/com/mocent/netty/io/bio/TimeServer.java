package com.mocent.netty.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hadoop on 2017/9/14.
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
        ServerSocket server = null;
        try {
            //创建一个服务端socket
            server = new ServerSocket(port);
            System.out.println("the time server is start in port:"+port);
            Socket socket =    null;
            //利用循环来监听客户端到连接，
            while(true){
                //如果没有连接接入，则阻塞在这一步
                socket = server.accept();
                //如果有连接接入
                new Thread(new TimeServerHandler(socket)).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (server != null){
                System.out.println("The time server close");
                try {
                    server.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
                server=null;
            }
        }

    }
}

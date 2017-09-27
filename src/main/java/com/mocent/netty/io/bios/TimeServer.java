package com.mocent.netty.io.bios;

import com.mocent.netty.io.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hadoop on 2017/9/15.
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
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
            //新建一个服务端socket
            server = new ServerSocket(port);
            System.out.println("The time server is  start in port:"+port);
            Socket socket = null;
            //新建一个线程池
            TimeServerHandlerExecutePool  singleExecutor = new TimeServerHandlerExecutePool(50,1000);
            while (true) {
                //如果没有请求过来阻塞在accept
                socket = server.accept();
                //如果有请求过来交由线程池来管理
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (server!= null) {
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}

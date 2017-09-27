package com.mocent.netty.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hadoop on 2017/9/14.
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
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            //新建socket连接
            socket = new Socket("127.0.0.1",port);
            //从socket通道读取数据
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //写取数据到socket通道
            out = new PrintWriter(socket.getOutputStream(),true);
            //先向socket发送数据，然后从socket读取数据，再打印出来
            out.println("QUERY TIME ORDER");
            System.out.println("send order 2 server succeed");
            String resp = in.readLine();
            System.out.println("now is :"+resp);
        }catch (Exception e){

        }finally {
            //关闭输出流
            if (out!=null){
                out.close();
                out= null;
            }
            //关闭输入流
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in=null;
            }
            //关闭socket通道
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }

    }
}

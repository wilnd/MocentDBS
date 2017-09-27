package com.mocent.netty.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * Created by hadoop on 2017/9/14.
 */
public class TimeServerHandler implements Runnable {
    private Socket socket;
    //获取服务端的ServerSocket
    public TimeServerHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        //定义IO流读方法
        BufferedReader in = null;
        //定义IO流写方法
        PrintWriter  out = null;
        try {
            //读取socket的输入流
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            //写到socket到输出流
            out = new PrintWriter(this.socket.getOutputStream(),true);
            String currentTime = null;
            String body = null;
            while (true){
                body = in.readLine();
                if (body ==null){
                    break;
                }
                System.out.println("The time server receive order");
                //如果从socket获取到数据是QUERY TIME ORDER，将当前到系统时间发送给客户端，否则将BAD ORDER发送给客户端
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString():"BAD ORDER";
                out.println(currentTime);
            }
        }catch (Exception e){
            if (in != null){
                try {
                    //关闭输入流
                    in.close();
                }catch (Exception el){
                    el.printStackTrace();
                }
            }
            if (out!= null){
                //关闭输出流
                out.close();
                out=null;
            }
            if (this.socket!=null){
                try{
                    //关闭socket
                    this.socket.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
                this.socket = null;
            }

        }
    }
}

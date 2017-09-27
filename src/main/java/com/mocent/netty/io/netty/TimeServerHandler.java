package com.mocent.netty.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 用于网络事件到读写操作
 * Created by hadoop on 2017/9/16.
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctc,Object msg) throws UnsupportedEncodingException {
        //类型转换，将Object转换为Netty到ByteBuf对象
        ByteBuf buf = (ByteBuf) msg;
        //获取缓冲区可读字节数
        byte[] req = new byte[buf.readableBytes()];
        //将缓冲区字节数复制到新建的byte数组中
        buf.readBytes(req);
        //通过new String构造函数，获取请求消息
        String body = new String(req,"UTF-8");
        System.out.println("time server order"+body);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
        //将string转换为Netty的ByteBuf对象
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        //一步发送应答消息给客户端
        ctc.write(resp);
    }

    public void channelReadComplete(ChannelHandlerContext ctx){
        //将消息队列的消息写入到SocketChannel发送给对方
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        //发生异常时，关闭ChannelHandlerContext，释放ChannelHandlerContext相关联的句柄等资源
        ctx.close();
    }
}

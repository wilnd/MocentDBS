package com.mocent.netty.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by hadoop on 2017/9/19.
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte [] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    /**
     * 当客户端与服务端的TCP链路建立成功后,Netty的NIO线程会调用channelActive方法
     * 发送查询时间的指令给服务端
     * @param ctx
     */
    public void channelActive (ChannelHandlerContext ctx){
        //将请求消息发送给服务端
        ctx.writeAndFlush(firstMessage);
    }

    /**
     * 当服务端有应答时，channelRead方法会被调用
     * @param ctx
     * @param msg
     * @throws UnsupportedEncodingException
     */
    public void channelRead(ChannelHandlerContext ctx,  Object msg) throws UnsupportedEncodingException {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("Now is :"+body);

    }

    /**
     * 发生异常时，打印异常日志，释放客户端资源
     * @param ctx
     * @param cause
     */
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        ctx.close();
    }
}

package com.mocent.netty.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by hadoop on 2017/9/15.
 */
public class TimeClientHandle implements Runnable {

    private String host;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop;

    /**
     *初始化客户端参数，
     * @param host 地址
     * @param port 端口号
     */
    public TimeClientHandle(String host,int port){
        this.host = host ==null?"127.0.0.1":host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void run() {
        try {
            //连接
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop) {
            try {
                //每秒轮询遍历一次所有通道
                selector.select(1000);
                //获取所有通道到key
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                //遍历所有通道
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    //将已遍历到通道从通道集合中移除
                    it.remove();
                    try {
                        //处理当前通道事件
                        handelInput(key);
                    }catch (Exception e){
                        //如果发生异常，将当前通道关闭
                        if (key!= null){
                            key.cancel();
                            if (key.channel()!= null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //读完数据将多路复用选择器关闭
        if (selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理当前通道
     * @param key 通道的key
     * * @throws IOException
     */
    private void handelInput(SelectionKey key) throws IOException {
        //如果当前通道是有效的
        if (key.isValid()){
            SocketChannel sc = (SocketChannel) key.channel();
            //如果当前通道没有完成，或者没有
            if (key.isConnectable()) {
                if (sc.finishConnect()){
                    //注册为读状态
                    sc.register(selector,SelectionKey.OP_READ);
                    //通过通道发送数据
                    doWrite(sc);
                }else {
                    System.exit(1);
                }
            }
            //如果当前通道是可读到
            if (key.isReadable()){
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes>0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("Now is："+  body);
                    this.stop = true;
                }else if (readBytes<0){
                    //对端链路关闭
                    key.cancel();
                    sc.close();
                }else {
                    //读到0字节 忽略
                }
            }
        }
    }

    /**
     * 如果通信通道没有建立，则将通道绑定在多路复用器上；
     * 如果通信通道已建立，将当前通道状态置为读
     * @throws IOException
     */
    private void doConnect() throws IOException {
        //如果通信通道已建立
        if (socketChannel.connect(new InetSocketAddress(host,port))){
            //注册到多路复用器上，状态为读。
            socketChannel.register(selector,SelectionKey.OP_READ);
            //发送请求数据
            doWrite(socketChannel);
        }else {
            //如果通信通道没有建立，则向多路复用器发起连接
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    /**
     *
     * @param socketChannel socket通道
     * @throws IOException
     */
    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()){
            System.out.println("send order 2 server succeed");
        }
    }
}

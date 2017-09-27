package com.mocent.netty.io.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by hadoop on 2017/9/15.
 */
public class MultiplexerTimeServer implements Runnable {
    //多路复用器
    private Selector selector;

    private ServerSocketChannel serverChannel;

    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     * @param port 端口号
     */
    public  MultiplexerTimeServer(int port){
        try {
            //设置多路复用器
            selector = Selector.open();
            //设置服务端通信通道，监听流式socket
            serverChannel = ServerSocketChannel.open();
            //设置服务端通信通道为非阻塞模式
            serverChannel.configureBlocking(false);
            //设置TCP的相关参数：地址，backlog
            serverChannel.socket().bind(new InetSocketAddress(port),1024);
            //将服务端通信通道注册到多路复用器上，监听SelectionKey.OP_ACCEPT操作位
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start int port :"+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        while(!stop){
            try {
                //每隔一秒多路复用器轮询一次准备就绪的channel
                selector.select(1000);
                //当有处于就绪状态的channel时，selector将返回channel的SelectionKey集合。
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                //遍历SelectionKey集合，即遍历当前有效的通道
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    //获取当前所遍历的key，赋值给新的饮用
                    key = it.next();
                    //将当前遍历的key从集合里面移除
                    it.remove();
                    try {
                        //处理当前的通道
                        handleInput(key);
                    }catch (Exception e){
                        if (key != null){
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理新接入客户端请求消息，通过selectionKey判断网络事件类型
     * @param key 就绪状态的通道
     * @throws IOException
     */
    private void handleInput(SelectionKey key) throws IOException {
        //如果当前通道是有效的
        if (key.isValid()){
            //下述操作相当于完成了TCP三次握手，TCP物理链路正式成立
            //如果当前key的通道已经准备好接收一个新的socket连接
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                //设置SocketChannel为异步非阻塞模式
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }
            //如果当前key的通道准备好读取数据
            if (key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                //创建一个ByteBuffer,无法知道客户端发送的码流大小，开辟一个1MB到缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //使用socketChannel到read方法读取请求码流
                int readBytes = sc.read(readBuffer);
                //返回值大于0表示读取到了字节，对字节进行编解码
                if (readBytes>0){
                    //limit=position , position=0,重置mask。一般是结束buf操作，将buf写入输出流时调用，这个必须要调用，否则极有可能position!=limit，导致position后面没有数据，每次写入数据到输出流时，必须确保position=limit。
                    readBuffer.flip();
                    //返回缓冲区剩余容量，取值=容量-位置
                    byte [] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String (bytes,"UTF-8");
                    System.out.println("The time server receive order:"+body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD QUERY";
                    doWrite(sc,currentTime);
                }else if (readBytes<0){
                    //返回值为-1表示链路已经关闭，需要关闭SocketChannel，释放资源
                    key.cancel();
                    sc.close();
                }else {
                    //=0表示没有读取到字节
                }
            }
        }
    }

    /**
     * 将应答消息异步发送给客户端
     * @param channel 将要写入数据的通道
     * @param response 写入数据到内容
     * @throws IOException
     */
    private void doWrite(SocketChannel channel,String response) throws IOException {
        if (response!=null && response.trim().length()>0){
            //将字符串编码成字节数组
            byte[] bytes = response.getBytes();
            //根据字节数组到容量创建ByteBuffer
            ByteBuffer  writeBuffer  = ByteBuffer.allocate(bytes.length);
            //将自己数组复制到缓冲区
            writeBuffer.put(bytes);
            //对缓冲区进行flip操作
            writeBuffer.flip();
            //将缓冲区到字节数组发送出去
            channel.write(writeBuffer);
        }
    }

}

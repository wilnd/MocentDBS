package com.mocent.netty.io.nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hadoop on 2017/9/16.
 */
public class NioTest {
    public static void main(String[] args) {
        int threadNum =10000;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        for (int i=0;i<threadNum;i++){
            executor.execute(new TimeClientHandle("127.0.0.1",8080));
        }
    }
}

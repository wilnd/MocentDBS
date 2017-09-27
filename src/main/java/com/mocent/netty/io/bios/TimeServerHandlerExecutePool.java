package com.mocent.netty.io.bios;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by hadoop on 2017/9/15.
 */
public class TimeServerHandlerExecutePool {
    //声明ExecutorService线程池
    private ExecutorService executor;
    //构造方法给线程池赋值
    public TimeServerHandlerExecutePool(int maxPoolSize , int queueSize) {
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }
    //线程执行方法
    public void execute(java.lang.Runnable task) {
        executor.execute(task);
    }
}

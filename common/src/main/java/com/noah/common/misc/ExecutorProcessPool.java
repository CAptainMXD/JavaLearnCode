package com.noah.common.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class ExecutorProcessPool {

    private ExecutorService executorService;

    private static ExecutorProcessPool executorProcessPool = new ExecutorProcessPool();

    public ExecutorProcessPool getExecutorProcessPool(){return executorProcessPool;}

    private ExecutorProcessPool(){
        executorService = Executors.newFixedThreadPool(10,getThreadFactory());
    }

    public Future<?> submit(Runnable runnable){
        return executorService.submit(runnable);
    }

    public void execute(Runnable runnable){
        executorService.execute(runnable);
    }

    private ThreadFactory getThreadFactory(){
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        };
    }
}

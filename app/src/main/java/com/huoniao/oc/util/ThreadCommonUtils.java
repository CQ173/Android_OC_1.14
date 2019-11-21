package com.huoniao.oc.util;

import com.huoniao.oc.MyApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Cloud on 2017/5/4.
 * 线程工具类
 */

public class ThreadCommonUtils {
    //在主线程执行的一条任务
    public static void runonuiThread(Runnable r){
        MyApplication.mainHandler.post(r);
    }

    //在子线程执行
    public static void runOnBackgroundThread(Runnable r){
        new Thread(r).start();
    }

    //使用线程池     可以复用空闲线程
    public static Executor executorThread(){
        Executor  executor =null;
        if(executor == null) {
            executor = Executors.newCachedThreadPool(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable);
                }
            });
        }
        return  executor;
    }
}

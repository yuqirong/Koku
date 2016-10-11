package com.yuqirong.koku.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Anyway on 2015/8/28.
 */
public class MyApplication extends Application {

    private static Application sContext;
    private static ThreadPoolExecutor executor;
    private static String sAccessToken;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    private static RequestQueue mQueue;

    /**
     * 得到volley队列
     * @return
     */
    public static RequestQueue getRequestQueue() {
            return mQueue;
    }

    public static Application getContext() {
        return sContext;
    }

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public static String getAccessToken() {
        return sAccessToken;
    }

    public static void setAccessToken(String sAccessToken) {
        MyApplication.sAccessToken = sAccessToken;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
        mQueue = Volley.newRequestQueue(sContext);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

}

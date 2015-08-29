package com.yuqirong.koku.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Anyway on 2015/8/28.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}

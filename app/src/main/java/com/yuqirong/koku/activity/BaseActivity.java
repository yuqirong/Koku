package com.yuqirong.koku.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anyway on 2015/8/28.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final List<BaseActivity> activities = new LinkedList<>();
    public RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        synchronized (activities) {
            activities.add(this);
        }
        initView();
        initToolBar();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }

    public void removeAllActivities() {
        synchronized (activities) {
            for (BaseActivity a : activities) {
                if (!a.isFinishing()) {
                    a.finish();
                }
            }
            activities.clear();
        }
    }

    protected abstract void initData();

    protected abstract void initToolBar();

    protected abstract void initView();

}

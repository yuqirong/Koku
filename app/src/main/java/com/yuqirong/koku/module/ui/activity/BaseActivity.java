package com.yuqirong.koku.module.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Activity基类
 * Created by Anyway on 2015/8/28.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final List<BaseActivity> activities = new LinkedList<>();
    public RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mQueue = Volley.newRequestQueue(this);
        synchronized (activities) {
            activities.add(this);
        }
        initView();
        initToolBar();
        ButterKnife.bind(this);
        initData(savedInstanceState);
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

    protected abstract void initData(Bundle savedInstanceState);

    protected void initToolBar() {
    }

    protected abstract void initView();

    public abstract int getContentViewId();

    /**
     * 从服务器上获取数据StringRequest
     *
     * @param url
     * @param listener
     * @param errorListener
     */
    protected void getData(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        mQueue.add(stringRequest);
    }

    protected void getJsonData(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        mQueue.add(jsonObjectRequest);
    }

    protected void actionStart(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    protected void actionStart(Context context, Class clazz, String bundleName, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(bundleName, bundle);
        context.startActivity(intent);
    }

}

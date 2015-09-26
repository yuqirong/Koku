package com.yuqirong.koku.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.BitmapUtils;
import com.yuqirong.koku.util.BitmapUtil;

/**
 * Created by Anyway on 2015/8/28.
 */
public abstract class BaseFragment extends Fragment {

    public Context context;
    protected BitmapUtils bitmapUtils;
    private RequestQueue mQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        bitmapUtils = BitmapUtil.getBitmapUtils(context);
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    public abstract void initData(Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater,container,savedInstanceState);
        return view;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 从服务器上获取数据
     * @param url
     * @param listener
     * @param errorListener
     */
    protected void getData(String url,Response.Listener listener,Response.ErrorListener errorListener){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener,errorListener );
        mQueue.add(stringRequest);
    }

}

package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.SearchUserAdapter;
import com.yuqirong.koku.entity.User;
import com.yuqirong.koku.util.JsonUtils;

import java.util.List;

/**
 * Created by Anyway on 2015/9/23.
 */
public class SearchUserFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private List<User> user;
    private SearchUserAdapter adapter;

    public Response.Listener<String> getListener() {
        return listener;
    }

    public Response.ErrorListener getErrorListener() {
        return errorListener;
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            adapter.clearData();
            user = JsonUtils.getListFromJson(response, User.class);
            for (User u : user) {
                adapter.addData(adapter.list.size(), u);
            }

        }
    };



    Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_search_user,null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        //创建默认的线性LayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //创建并设置Adapter
        adapter = new SearchUserAdapter(context);
        mRecyclerView.setAdapter(adapter);
        return view;
    }
}

package com.yuqirong.koku.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.SearchUserActivity;
import com.yuqirong.koku.adapter.SearchUserAdapter;
import com.yuqirong.koku.adapter.WeiboRecycleViewAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.User;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 搜索用户和@好友共用的fragment
 * Created by Anyway on 2015/9/23.
 */
public class SearchUserFragment extends BaseFragment {

    private TextView tv_tips;
    private RecyclerView mRecyclerView;
    private List<User> user;
    private SearchUserAdapter searchUserAdapter;
    private int type;
    private ProgressBar mProgressBar;
    private TextView tv_friends_list;


    public Response.ErrorListener getErrorListener() {
        return errorListener;
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            searchUserAdapter.setIsSearchMode(false);
            searchUserAdapter.clearData();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                String json = jsonObject.getString("users");
                user = JsonUtils.getListFromJson(json, User.class);
                mProgressBar.setVisibility(View.GONE);
                searchUserAdapter.getList().addAll(user);
                searchUserAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public Response.Listener<String> getSearchlistener() {
        return searchlistener;
    }

    Response.Listener<String> searchlistener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            searchUserAdapter.setIsSearchMode(true);
            tv_friends_list.setText(R.string.search_result_list);
            searchUserAdapter.clearData();
            user = JsonUtils.getListFromJson(response, User.class);
            searchUserAdapter.getList().addAll(user);
            searchUserAdapter.notifyDataSetChanged();
        }
    };


    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        type = arguments.getInt("type", 0);
        if (type == SearchUserActivity.SEARCH_USER) {
            mProgressBar.setVisibility(View.GONE);
            tv_friends_list.setText(R.string.search_result_list);
            //TODO 搜索用户

        } else if (type == SearchUserActivity.AT_USER) {
            tv_tips.setText(R.string.follower_num_limit);
            searchUserAdapter.setOnItemClickListener(onItemClicklistener);
            String url = AppConstant.FRIENDSHIPS_FRIENDS_URL + "?access_token=" + SharePrefUtil.getString(context, "access_token", "") + "&uid=" + SharePrefUtil.getString(context, "uid", "") + "&count=50";
            getData(url, listener, errorListener);
        }
    }

    WeiboRecycleViewAdapter.OnItemClickListener onItemClicklistener = new WeiboRecycleViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            String screen_name = ((TextView) view.findViewById(R.id.tv_screen_name)).getText().toString();
            LogUtils.i(screen_name + " , position=" + position);
            Intent intent = new Intent();
            intent.putExtra("screen_name", screen_name);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }

        @Override
        public void onItemLongClick(View view, int position) {
        }
    };


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_search_user, null);
        tv_tips = (TextView) view.findViewById(R.id.tv_tips);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.mProgressBar);
        tv_friends_list = (TextView) view.findViewById(R.id.tv_friends_list);
        //创建默认的线性LayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchUserAdapter = new SearchUserAdapter(context);
        mRecyclerView.setAdapter(searchUserAdapter);
        //创建并设置Adapter
        return view;
    }

}
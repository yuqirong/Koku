package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.adapter.LoadMoreAdapter;
import com.yuqirong.koku.adapter.WeiboCommentAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.Comment;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.AutoLoadRecyclerView;
import com.yuqirong.koku.view.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 微博详情页面评论
 * Created by Anyway on 2015/10/11.
 */
public class WeiboDetailsCommentFragment extends BaseFragment {

    private View headerView;
    private AutoLoadRecyclerView mAutoLoadRecyclerView;
    private WeiboCommentAdapter adapter;

    private long next_cursor;
    private boolean load;
    private int total_number;

    public WeiboDetailsCommentFragment(View headerView) {
        this();
        this.headerView = headerView;
    }

    public WeiboDetailsCommentFragment() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getDataFromServer();
    }

    private void getDataFromServer() {
        String idstr = getArguments().getString("idstr");
        if (!TextUtils.isEmpty(idstr)) {
            String url = AppConstant.COMMENTS_SHOW_URL + "?count=20&id=" + idstr +
                    "&access_token=" + SharePrefUtil.getString(context, "access_token", "") + "&max_id=" + next_cursor;
            LogUtils.i("评论url ：" + url);
            getJsonData(url, listener, errorListener);
        }
    }

    Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject object) {
            processData(object);
        }
    };

    private void processData(JSONObject object) {
        try {
            String str = object.getString("comments");
            next_cursor = object.getLong("next_cursor");
            total_number = object.getInt("total_number");
            adapter.getList().addAll(adapter.getList().size() - 1, JsonUtils.getListFromJson(str, Comment.class));
            if (load) {
                adapter.completeLoadMore(true);
                load = false;
            }
            if (total_number == adapter.getList().size() - 2) {
                adapter.setLoadFinish();
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAutoLoadRecyclerView = new AutoLoadRecyclerView(context);
        mAutoLoadRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        adapter = new WeiboCommentAdapter(context);
        if (headerView != null) {
            ViewParent parent = headerView.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(headerView);
            }
            adapter.addHeaderView(true, headerView);
        }
        adapter.getList().add(new Comment());
        adapter.getList().add(new Comment());
        adapter.setOnLoadingMoreListener(new LoadMoreAdapter.OnLoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                load = true;
                getDataFromServer();
            }
        });
        mAutoLoadRecyclerView.setAdapter(adapter);
        return mAutoLoadRecyclerView;
    }

}

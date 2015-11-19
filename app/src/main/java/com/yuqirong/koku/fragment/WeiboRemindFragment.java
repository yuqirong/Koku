package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.CommentRemindAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.RemindComment;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.AutoLoadRecyclerView;
import com.yuqirong.koku.view.FixedSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 提醒中@我的微博
 * Created by Administrator on 2015/11/12.
 */
public class WeiboRemindFragment extends BaseFragment {

    private AutoLoadRecyclerView mRecyclerView;
    private FixedSwipeRefreshLayout mSwipeRefreshLayout;
    private CommentRemindAdapter adapter;
    private String max_id;

    @Override
    public void initData(Bundle savedInstanceState) {
        getDataFromServer();

    }

    private void getDataFromServer() {
        String url = AppConstant.COMMENTS_MENTIONS_URL + "?access_token="
                + SharePrefUtil.getString(context, "access_token", "") + "&count=20&max_id=" + max_id;
        LogUtils.i("获取@我的微博列表" + url);
        getData(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String comments = jsonObject.getString("comments");
                    List<RemindComment> remindComments = JsonUtils.getListFromJson(comments, RemindComment.class);
                    adapter.getList().addAll(remindComments);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_remind, null);
        mRecyclerView = (AutoLoadRecyclerView) view.findViewById(R.id.mRecyclerView);
        setupRecyclerView(mRecyclerView);
        mSwipeRefreshLayout = (FixedSwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(FixedSwipeRefreshLayout.SWIPE_REFRESH_LAYOUT_COLOR);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        return view;
    }

    private void setupRecyclerView(AutoLoadRecyclerView mRecyclerView) {
        adapter = new CommentRemindAdapter();
//        adapter.setOnLoadingMoreListener(loadingMoreListener);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 下拉刷新Listener
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

        }
    };

}

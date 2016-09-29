package com.yuqirong.koku.module.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.module.ui.adapter.CommentRemindAdapter;
import com.yuqirong.koku.module.ui.adapter.LoadMoreAdapter.OnLoadingMoreListener;
import com.yuqirong.koku.app.MyApplication;
import com.yuqirong.koku.app.AppConstant;
import com.yuqirong.koku.module.model.entity.RemindComment;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.module.ui.weidgt.AutoLoadRecyclerView;
import com.yuqirong.koku.module.ui.weidgt.FixedSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 提醒中关于我的评论
 * Created by Administrator on 2015/11/12.
 */
public class CommentRemindFragment extends BaseFragment {

    private AutoLoadRecyclerView mRecyclerView;
    private FixedSwipeRefreshLayout mSwipeRefreshLayout;
    private CommentRemindAdapter adapter;
    private String max_id;
    private static Handler mHandler = new Handler();

    @Override
    public void initData(Bundle savedInstanceState) {
        getDataFromServer();
    }

    private void getDataFromServer() {
        String url = AppConstant.COMMENTS_TO_ME_URL + "?access_token="
                + SharePrefUtil.getString(context, "access_token", "") + "&count=20&max_id=" + max_id;
        LogUtils.i("获取当前登录用户所接收到的评论列表url:" + url);
        getData(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    max_id = jsonObject.getString("next_cursor");
                    if (max_id.equals("0")) {
                        adapter.setEndText(context.getString(R.string.load_finish));
                    }
                    String comments = jsonObject.getString("comments");
                    processData(comments);
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

    private void processData(final String comments) {
        MyApplication.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final List<RemindComment> remindComments = JsonUtils.getListFromJson(comments, RemindComment.class);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            adapter.getList().clear();
                        }
                        adapter.getList().addAll(remindComments);
                        adapter.notifyDataSetChanged();
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else if (adapter.isLoadingMore()) {
                            if (max_id.equals("0")) {
                                adapter.setEndText(context.getString(R.string.load_finish));
                            } else {
                                adapter.completeLoadMore(true);
                            }
                        }
                    }
                });
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
        adapter.setOnLoadingMoreListener(loadingMoreListener);
        mRecyclerView.setAdapter(adapter);
    }

    OnLoadingMoreListener loadingMoreListener = new OnLoadingMoreListener() {

        @Override
        public void onLoadingMore() {
            getDataFromServer();
        }
    };

    /**
     * 下拉刷新Listener
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            max_id = null;
            adapter.initFooterViewHolder();
            getDataFromServer();
        }
    };

}

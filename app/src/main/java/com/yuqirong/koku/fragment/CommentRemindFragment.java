package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqirong.koku.R;
import com.yuqirong.koku.view.FixedSwipeRefreshLayout;

/**
 * 提醒中关于我的评论
 * Created by Administrator on 2015/11/12.
 */
public class CommentRemindFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private FixedSwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_remind,null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = (FixedSwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);

        return view;
    }

}

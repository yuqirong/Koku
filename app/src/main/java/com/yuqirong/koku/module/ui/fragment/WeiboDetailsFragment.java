package com.yuqirong.koku.module.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqirong.koku.R;
import com.yuqirong.koku.module.model.entity.Status;

/**
 * 微博详情页面
 * Created by Anyway on 2015/9/18.
 */
public class WeiboDetailsFragment extends BaseFragment {

    private FragmentManager fragmentManager;
    private WeiboDetailsCommentFragment commentFragment;
    private WeiboDetailsRepostFragment repostFragment;
    private Status status = null;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (status != null) {
            outState.putSerializable("Status", status);
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                status = (Status) intent.getSerializableExtra("Status");
            }
        } else {
            status = (Status) savedInstanceState.getSerializable("Status");
        }
        commentFragment = new WeiboDetailsCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Status", status);
        commentFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.mFrameLayout, commentFragment, "CommentFragment").commit();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_weibo_details, null);
        return view;
    }
}

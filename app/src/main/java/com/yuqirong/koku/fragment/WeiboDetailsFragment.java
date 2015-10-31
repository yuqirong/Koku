package com.yuqirong.koku.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.Status;

/**
 * 微博详情页面
 * Created by Anyway on 2015/9/18.
 */
public class WeiboDetailsFragment extends BaseFragment {

    private FragmentManager fragmentManager;
    private WeiboDetailsCommentFragment commentFragment;
    private WeiboDetailsRepostFragment repostFragment;

    @Override
    public void initData(Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        Status status = null;
        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                status = (Status) intent.getSerializableExtra("Status");
            }
        } else {
            // TODO: 2015/10/12
        }
        commentFragment = new WeiboDetailsCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Status",status);
        commentFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.mFrameLayout, commentFragment, "commentFragment").commit();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_weibo_details, null);
        return view;
    }
}

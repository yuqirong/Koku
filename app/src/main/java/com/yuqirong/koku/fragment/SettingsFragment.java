package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqirong.koku.R;

/**
 * Created by Administrator on 2015/11/4.
 */
public class SettingsFragment extends BaseFragment {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_about_item,null);
        return view;
    }

}

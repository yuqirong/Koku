package com.yuqirong.koku.fragment;

import android.os.Bundle;

import com.yuqirong.koku.app.AppConstant;

/**
 * Created by Anyway on 2015/9/16.
 */
public class FragmentFactory {


    public static WeiboTimeLineFragment newInstance(String url) {
        WeiboTimeLineFragment f = new WeiboTimeLineFragment();
        Bundle b = new Bundle();
        b.putString("url", url);
        f.setArguments(b);
        return f;
    }

    public static UserDetailsFragment getDetailsFragment(String id,int feature) {
        UserDetailsFragment f = new UserDetailsFragment();
        Bundle b = new Bundle();
        b.putString("url", AppConstant.STATUSES_USER_TIMELINE_URL + "?uid=" + id + "&feature=" + feature);
        f.setArguments(b);
        return f;
    }

}

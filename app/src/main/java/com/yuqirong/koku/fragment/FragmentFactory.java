package com.yuqirong.koku.fragment;

import android.os.Bundle;

/**
 * Created by Anyway on 2015/9/16.
 */
public class FragmentFactory {

    public static WeiboTimeLineFragment newInstance(String url) {
        WeiboTimeLineFragment f = new WeiboTimeLineFragment();
        Bundle b = new Bundle();
        b.putCharSequence("url", url);
        f.setArguments(b);
        return f;
    }
}

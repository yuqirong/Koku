package com.yuqirong.koku.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 热门话题的Activity
 * Created by Anyway on 2015/10/1.
 */
public class HotTopicActivity extends BaseActivity {

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initView() {

    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,HotTopicActivity.class);
        context.startActivity(intent);
    }

}

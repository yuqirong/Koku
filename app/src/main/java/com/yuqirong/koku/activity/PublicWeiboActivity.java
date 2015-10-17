package com.yuqirong.koku.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.yuqirong.koku.R;
import com.yuqirong.koku.view.swipeback.SwipeBackLayout;
import com.yuqirong.koku.view.swipeback.app.SwipeBackActivity;

/**
 * 公共微博的Activity
 * Created by Anyway on 2015/10/1.
 */
public class PublicWeiboActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.public_weibo);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_public_weibo);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,PublicWeiboActivity.class);
        context.startActivity(intent);
    }

}

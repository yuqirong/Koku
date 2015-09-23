package com.yuqirong.koku.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.WeiboRecycleViewAdapter;
import com.yuqirong.koku.entity.User;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.view.DividerItemDecoration;

/**
 * Created by Anyway on 2015/9/21.
 */
public class UserDetailsActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private WeiboRecycleViewAdapter adapter;

    @Override
    protected void initData(Bundle savedInstanceState) {
        adapter = new WeiboRecycleViewAdapter(this);
        WeiboItem item = new WeiboItem();
        item.user = new User();
        item.user.name = "选哦三";
        item.user.screen_name = "选哦三";
        item.text = "sdfsdfffffffffffffffffffffffffff";
        for (int i=0;i<15;i++){
            adapter.getList().add(item);
        }
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_details);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mToolbar = (Toolbar)findViewById(R.id.mToolbar);
    }
}

package com.yuqirong.koku.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.Status;
import com.yuqirong.koku.view.swipeback.SwipeBackLayout;
import com.yuqirong.koku.view.swipeback.app.SwipeBackActivity;

/**
 * 单条微博详情Activity
 * Created by Anyway on 2015/9/17.
 */
public class WeiboDetailsActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;
    private Toolbar mToolbar;
//    private TabLayout mTabLayout;
//    private ViewPager mViewPager;
//    private FragmentManager fm;
//    private MainActivity.FragmentAdapter adapter;
    private Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        status = (Status) getIntent().getSerializableExtra("status");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_weibo_details);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
//        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
//        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
//
//        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
//        mTabLayout.setSelectedTabIndicatorHeight(CommonUtil.dip2px(this, 4));
//        mTabLayout.setTabTextColors(getResources().getColor(R.color.unselected_text_color), getResources().getColor(android.R.color.white));
//
//        fm = getSupportFragmentManager();
//        adapter = new MainActivity.FragmentAdapter(fm);
////        adapter.addFragment(FragmentFactory.newInstance(),"评论");
////        adapter.addFragment(FragmentFactory.newInstance(),"转发");
//        mViewPager.setAdapter(adapter);
//        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weibo_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

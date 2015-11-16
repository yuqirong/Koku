package com.yuqirong.koku.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.FragmentAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.fragment.CommentRemindFragment;
import com.yuqirong.koku.fragment.FragmentFactory;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.view.swipeback.SwipeBackLayout;
import com.yuqirong.koku.view.swipeback.app.SwipeBackActivity;

/**
 * 通知页面
 * Created by Administrator on 2015/10/19.
 */
public class RemindActivity extends SwipeBackActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SwipeBackLayout swipeBackLayout;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_remind);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        if (mTabLayout != null) {
            setupTabLayoutContent(mTabLayout);
        }
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        setViewPager(mViewPager);
    }

    private void setViewPager(ViewPager mViewPager) {
        adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new CommentRemindFragment(), getResources().getString(R.string.comment));
        adapter.addFragment(FragmentFactory.newInstance(AppConstant.STATUSES_BILATERAL_TIMELINE_URL), getResources().getString(R.string.refer_to));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private void setupTabLayoutContent(TabLayout mTabLayout) {
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
        mTabLayout.setSelectedTabIndicatorHeight(CommonUtil.dip2px(this, 4));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.unselected_text_color), getResources().getColor(android.R.color.white));
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

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RemindActivity.class);
        context.startActivity(intent);
    }
}

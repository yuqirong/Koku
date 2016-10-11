package com.yuqirong.koku.module.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yuqirong.koku.R;
import com.yuqirong.koku.module.ui.adapter.FragmentAdapter;
import com.yuqirong.koku.app.AppConstant;
import com.yuqirong.koku.module.ui.fragment.CommentRemindFragment;
import com.yuqirong.koku.module.ui.fragment.FragmentFactory;
import com.yuqirong.koku.module.ui.fragment.WeiboRemindFragment;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.module.ui.weidgt.swipeback.SwipeBackLayout;
import com.yuqirong.koku.module.ui.weidgt.swipeback.app.SwipeBackActivity;

import butterknife.BindView;

/**
 * 通知页面
 * Created by Administrator on 2015/10/19.
 */
public class RemindActivity extends SwipeBackActivity {

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initView() {
        if (mTabLayout != null) {
            setupTabLayoutContent(mTabLayout);
        }
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.notify);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        setViewPager(mViewPager);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_remind;
    }

    private void setViewPager(ViewPager mViewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new CommentRemindFragment(), getResources().getString(R.string.comment));
        adapter.addFragment(new WeiboRemindFragment(), getResources().getString(R.string.refer_to));
        adapter.addFragment(FragmentFactory.newInstance(AppConstant.STATUSES_BILATERAL_TIMELINE_URL), getResources().getString(R.string.private_letter));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private void setupTabLayoutContent(TabLayout mTabLayout) {
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
        mTabLayout.setSelectedTabIndicatorHeight(CommonUtil.dip2px(this, 2));
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

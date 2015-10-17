package com.yuqirong.koku.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.FragmentAdapter;
import com.yuqirong.koku.entity.User;
import com.yuqirong.koku.fragment.FragmentFactory;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.CircleImageView;
import com.yuqirong.koku.view.swipeback.SwipeBackLayout;
import com.yuqirong.koku.view.swipeback.app.SwipeBackActivity;

/**
 * 用户详情页面
 * Created by Anyway on 2015/9/21.
 */
public class UserDetailsActivity extends SwipeBackActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ImageView iv_cover;
    private CircleImageView civ_avatar;
    private TextView tv_screen_name;
    private TextView tv_location;
    private TextView tv_follower_num;
    private TextView tv_follow_num;
    private TextView tv_weibo_num;
    private TextView tv_verified_reason;
    private TextView tv_description;
    private FragmentManager fragmentManager;
    private FragmentAdapter adapter;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();
    private TabLayout mTabLayout;
    private User user;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            user = (User) getIntent().getSerializableExtra("User");
        } else {

        }
        initHeaderViewData();
        setupViewPagerContent();
    }

    private void initHeaderViewData() {
        if (user != null) {
            imageLoader.displayImage(user.avatar_large, civ_avatar, options);
            imageLoader.displayImage(user.cover_image_phone, iv_cover, options);
            tv_screen_name.setText(user.screen_name);
            Drawable drawable = null;
            if ("m".equals(user.gender)) {
                drawable = getResources().getDrawable(R.drawable.male);
            } else if ("f".equals(user.gender)) {
                drawable = getResources().getDrawable(R.drawable.female);
            }
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_screen_name.setCompoundDrawables(null, null, drawable, null);
                tv_screen_name.setCompoundDrawablePadding(CommonUtil.dip2px(this, 5));
            }
            tv_location.setText(user.location);
            tv_follower_num.setText(getResources().getString(R.string.follower) + CommonUtil.getNumString(user.followers_count));
            tv_follow_num.setText(getResources().getString(R.string.follow) + CommonUtil.getNumString(user.friends_count));
            tv_weibo_num.setText(getResources().getString(R.string.weibo) + CommonUtil.getNumString(user.statuses_count));
            if (user.verified) {
                tv_verified_reason.setText(user.verified_reason);
            } else {
                tv_verified_reason.setVisibility(View.GONE);
            }
            tv_description.setText(user.description);
        }
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
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        setupTabLayoutContent(mTabLayout);
        initHeaderView();
    }

    private void setupTabLayoutContent(TabLayout mTabLayout) {
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.Indigo_colorPrimary));
        mTabLayout.setSelectedTabIndicatorHeight(CommonUtil.dip2px(this, 4));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.unselected_text_color), getResources().getColor(R.color.Indigo_colorPrimary));
    }

    //设置ViewPager内容
    private void setupViewPagerContent() {
        fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager);
        adapter.addFragment(FragmentFactory.getDetailsFragment(user.idstr, 0), getResources().getString(R.string.all));
        adapter.addFragment(FragmentFactory.getDetailsFragment(user.idstr, 1), getResources().getString(R.string.original));
        adapter.addFragment(FragmentFactory.getDetailsFragment(user.idstr, 2), getResources().getString(R.string.picture));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initHeaderView() {
        iv_cover = (ImageView) findViewById(R.id.iv_cover);
        civ_avatar = (CircleImageView) findViewById(R.id.civ_avatar);
        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_follower_num = (TextView) findViewById(R.id.tv_follower_num);
        tv_follow_num = (TextView) findViewById(R.id.tv_follow_num);
        tv_weibo_num = (TextView) findViewById(R.id.tv_weibo_num);
        tv_verified_reason = (TextView) findViewById(R.id.tv_verified_reason);
        tv_description = (TextView) findViewById(R.id.tv_description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!user.idstr.equals(SharePrefUtil.getString(this, "uid", ""))) {
            getMenuInflater().inflate(R.menu.menu_user_details, menu);
            if (user.following) {
                menu.getItem(0).setTitle(R.string.cancel_follow);
            } else {
                if ("m".equals(user.gender)) {
                    menu.getItem(0).setTitle(R.string.follow_he);
                } else if ("f".equals(user.gender)) {
                    menu.getItem(0).setTitle(R.string.follow_she);
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

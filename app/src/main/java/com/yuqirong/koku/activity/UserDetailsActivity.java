package com.yuqirong.koku.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.FragmentAdapter;
import com.yuqirong.koku.entity.Status;
import com.yuqirong.koku.view.CircleImageView;

/**
 * Created by Anyway on 2015/9/21.
 */
public class UserDetailsActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ImageView iv_cover;
    private CircleImageView civ_avatar;
    private TextView tv_screen_name;
    private TextView tv_city;
    private TextView tv_follower_num;
    private TextView tv_follow_num;
    private TextView tv_weibo_num;
    private TextView tv_description;
    private FragmentManager fragmentManager;
    private FragmentAdapter adapter;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
                                                .cacheInMemory(true)
                                                .cacheOnDisk(true)
                                                .bitmapConfig(Bitmap.Config.RGB_565)
                                                .build();

    @Override
    protected void initData(Bundle savedInstanceState) {
        Status status = null;
        if (savedInstanceState == null) {
            status = (Status) getIntent().getSerializableExtra("Status");
        } else {

        }
        if (status != null) {
            imageLoader.displayImage(status.user.avatar_large,civ_avatar,options);
            imageLoader.displayImage(status.user.cover_image_phone,iv_cover,options);
            tv_screen_name.setText(status.user.screen_name);
            tv_city.setText(status.user.city);
            tv_follower_num.setText(getResources().getString(R.string.follower) + status.user.followers_count);
            tv_follow_num.setText(getResources().getString(R.string.follow) + status.user.friends_count);
            tv_weibo_num.setText(getResources().getString(R.string.weibo) + status.user.statuses_count);
            tv_description.setText(status.user.description);
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
        initHeaderView();
        fragmentManager = getSupportFragmentManager();
        adapter = new FragmentAdapter(fragmentManager);
    }

    private void initHeaderView() {
        iv_cover = (ImageView) findViewById(R.id.iv_cover);
        civ_avatar = (CircleImageView) findViewById(R.id.civ_avatar);
        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_follower_num = (TextView) findViewById(R.id.tv_follower_num);
        tv_follow_num = (TextView) findViewById(R.id.tv_follow_num);
        tv_weibo_num = (TextView) findViewById(R.id.tv_weibo_num);
        tv_description = (TextView) findViewById(R.id.tv_description);
    }

}

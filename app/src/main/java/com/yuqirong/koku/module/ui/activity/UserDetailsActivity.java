package com.yuqirong.koku.module.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.module.ui.adapter.FragmentAdapter;
import com.yuqirong.koku.app.AppConstant;
import com.yuqirong.koku.module.model.entity.User;
import com.yuqirong.koku.module.ui.fragment.FragmentFactory;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.ImageLoader;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.module.ui.weidgt.CircleImageView;
import com.yuqirong.koku.module.ui.weidgt.swipeback.SwipeBackLayout;
import com.yuqirong.koku.module.ui.weidgt.swipeback.app.SwipeBackActivity;

import butterknife.BindView;

/**
 * 用户详情页面
 * Created by Anyway on 2015/9/21.
 */
public class UserDetailsActivity extends SwipeBackActivity {

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.civ_avatar)
    CircleImageView civ_avatar;
    @BindView(R.id.tv_screen_name)
    TextView tv_screen_name;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_follower_num)
    TextView tv_follower_num;
    @BindView(R.id.tv_follow_num)
    TextView tv_follow_num;
    @BindView(R.id.tv_weibo_num)
    TextView tv_weibo_num;
    @BindView(R.id.tv_verified_reason)
    TextView tv_verified_reason;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.mTabLayout)
    private TabLayout mTabLayout;

    private String screen_name;
    private User user;
    private Menu menu;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("screen_name", screen_name);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            screen_name = getIntent().getStringExtra("screen_name");
        } else {
            screen_name = savedInstanceState.getString("screen_name");
        }
        if (TextUtils.isEmpty(screen_name)) {
            return;
        }
        String url = AppConstant.USERS_SHOW_URL + "?access_token=" +
                SharePrefUtil.getString(this, "access_token", "") + "&screen_name=" + screen_name;
        LogUtils.i("用户信息url ：" + url);
        getData(url, listener, errorListener);
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String result) {
            user = JsonUtils.getBeanFromJson(result, User.class);
            initHeaderViewData();
            initMenu();
            setupViewPagerContent();
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    private void initHeaderViewData() {
        if (user != null) {
            ImageLoader.loadImage(this, user.getAvatar_large(), civ_avatar);
            ImageLoader.loadImage(this, user.getCover_image_phone(), iv_cover);
            tv_screen_name.setText(user.getScreen_name());
            Drawable drawable = null;
            if ("m".equals(user.getGender())) {
                drawable = getResources().getDrawable(R.drawable.male);
            } else if ("f".equals(user.getGender())) {
                drawable = getResources().getDrawable(R.drawable.female);
            }
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_screen_name.setCompoundDrawables(null, null, drawable, null);
                tv_screen_name.setCompoundDrawablePadding(CommonUtil.dip2px(this, 5));
            }
            tv_location.setText(user.getLocation());
            tv_follower_num.setText(getResources().getString(R.string.follower) + CommonUtil.getNumString(user.getFollowers_count()));
            tv_follow_num.setText(getResources().getString(R.string.follow) + CommonUtil.getNumString(user.getFriends_count()));
            tv_weibo_num.setText(getResources().getString(R.string.weibo) + CommonUtil.getNumString(user.getStatuses_count()));
            if (user.isVerified()) {
                tv_verified_reason.setText(user.getVerified_reason());
            } else {
                tv_verified_reason.setVisibility(View.GONE);
            }
            tv_description.setText(user.getDescription());
        }
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setupTabLayoutContent(mTabLayout);
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_details;
    }

    private void setupTabLayoutContent(TabLayout mTabLayout) {
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.Indigo_colorPrimary));
        mTabLayout.setSelectedTabIndicatorHeight(CommonUtil.dip2px(this, 2));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.unselected_text_color), getResources().getColor(R.color.Indigo_colorPrimary));
    }

    //设置ViewPager内容
    private void setupViewPagerContent() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(fragmentManager);
        adapter.addFragment(FragmentFactory.getDetailsFragment(user.getIdstr(), 0), getResources().getString(R.string.all));
        adapter.addFragment(FragmentFactory.getDetailsFragment(user.getIdstr(), 1), getResources().getString(R.string.original));
        adapter.addFragment(FragmentFactory.getDetailsFragment(user.getIdstr(), 2), getResources().getString(R.string.picture));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_user_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initMenu() {
        if (user != null &&
                !SharePrefUtil.getString(this, "uid", "").equals(user.getIdstr())) {
            if (user.isFollowing()) {
                menu.getItem(0).setTitle(R.string.cancel_follow);
            } else {
                if ("m".equals(user.getGender())) {
                    menu.getItem(0).setTitle(R.string.follow_he);
                } else if ("f".equals(user.getGender())) {
                    menu.getItem(0).setTitle(R.string.follow_she);
                }
            }
        }
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

    public static void actionStart(Context context, String screen_name) {
        Intent intent = new Intent(context, UserDetailsActivity.class);
        intent.putExtra("screen_name", screen_name);
        context.startActivity(intent);
    }

}

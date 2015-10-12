package com.yuqirong.koku.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.fragment.FragmentFactory;
import com.yuqirong.koku.fragment.WeiboTimeLineFragment;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {

    /**
     * 抽屉
     */
    private DrawerLayout mDrawerLayout;
    /**
     * 在ActionBar上的抽屉按钮
     */
    private ActionBarDrawerToggle toggle;

    private Toolbar mToolbar;

    private TabLayout mTabLayout;
    /**
     * 判断DrawerLayout是否打开
     */
    private boolean isDrawerOpened = false;

    private NavigationView mNavigationView;
    private FloatingActionButton mFloatingActionButton;
    private Handler handler = new Handler();

    private ViewPager mViewPager;

    private FragmentAdapter adapter;
    private FragmentManager fm;
    public static final int SEND_NEW_WEIBO = 1001;
    public static final int SEND_NEW_COMMENT = 1003;
    public static final int SEND_NEW_REPOST = 1005;
    private boolean isLastShown = false; //用于fabbutton上一个状态是否显示
    private long firstTime;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void initData(Bundle savedInstanceState) {
        checkTokenExpireIn();
    }

    //查询用户access_token的授权相关信息
    private void checkTokenExpireIn() {
        final String access_token = SharePrefUtil.getString(this, "access_token", "");
        if (access_token != "") {
            String url = AppConstant.GET_TOKEN_INFO_URL + "?access_token=" + access_token;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        String expire_in = jsonObject.getString("expire_in");
                        int second = Integer.parseInt(expire_in);
                        if (second > 0) {
                            SharePrefUtil.saveString(MainActivity.this, "expire_in", expire_in);
                        } else {
                            goToAuthorizeActivity();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, errorListener) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("access_token", access_token);
                    return map;
                }
            };
            mQueue.add(jsonObjectRequest);
        } else {
            goToAuthorizeActivity();
        }
    }

    private void goToAuthorizeActivity() {
        AuthorizeActivity.actionStart(this);
        finish();
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            LogUtils.i(volleyError.toString());
        }
    };

    @Override
    protected void initToolBar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                if (isLastShown) {
                    mFloatingActionButton.show();
                }
                isDrawerOpened = false;
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
                isLastShown = mFloatingActionButton.isShown();
                mFloatingActionButton.hide();
                super.onDrawerOpened(drawerView);
            }
        };
        //把toggle和ActionBar同步状态
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        if (mTabLayout != null) {
            setupTabLayoutContent(mTabLayout);
        }
        mNavigationView = (NavigationView) findViewById(R.id.mNavigationView);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.mFloatingActionButton);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.mCoordinatorLayout);
        if (adapter == null) {
            setupViewPagerContent();
        }
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }
        if (mFloatingActionButton != null) {
            setupFloatingActionButtonContent(mFloatingActionButton);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SEND_NEW_WEIBO:
                if (resultCode == PublishActivity.SEND_WEIBO_SUCCESS) {
                    // 发布广播
                    LogUtils.i("send weibo success");
                    CommonUtil.setVubator(MainActivity.this, 300);
                    final Fragment item = adapter.getItem(0);
                    View rootView = item.getView();
                    CommonUtil.showSnackbar(rootView, R.string.publish_success, getResources().getColor(R.color.Indigo_colorPrimary), Snackbar.LENGTH_LONG, R.string.click_to_refresh, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((WeiboTimeLineFragment) item).refreshWeibo();
                        }
                    });
                }
                break;
            case SEND_NEW_COMMENT:
                if (resultCode == PublishActivity.SEND_COMMENT_SUCCESS) {
                    CommonUtil.setVubator(MainActivity.this, 300);
                }
                break;
            case SEND_NEW_REPOST:
                if (resultCode == PublishActivity.SEND_REPOST_SUCCESS) {
                    CommonUtil.setVubator(MainActivity.this, 300);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupTabLayoutContent(TabLayout mTabLayout) {
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
        mTabLayout.setSelectedTabIndicatorHeight(CommonUtil.dip2px(this, 4));
        mTabLayout.setTabTextColors(getResources().getColor(R.color.unselected_text_color), getResources().getColor(android.R.color.white));
    }

    private void setupFloatingActionButtonContent(FloatingActionButton mFloatingActionButton) {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 得到状态栏的高度
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int topOffset = dm.heightPixels - mCoordinatorLayout.getMeasuredHeight();

                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth() / 2;
                startingLocation[1] = startingLocation[1] - topOffset + view.getHeight() / 2;
                Intent intent = new Intent(MainActivity.this, PublishActivity.class);
                intent.putExtra("type", PublishActivity.SEND_WEIBO);
                intent.putExtra("isFromFAButton", true);
                intent.putExtra(PublishActivity.ARG_REVEAL_START_LOCATION, startingLocation);
                startActivityForResult(intent, SEND_NEW_WEIBO);
                overridePendingTransition(0, 0);
            }
        });
    }

    //设置ViewPager内容
    private void setupViewPagerContent() {
        fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm);
        adapter.addFragment(FragmentFactory.newInstance(AppConstant.HOME_TIMELINE_URL), getResources().getString(R.string.all_weibo));
        adapter.addFragment(FragmentFactory.newInstance(AppConstant.STATUSES_BILATERAL_TIMELINE_URL), getResources().getString(R.string.bilateral_weibo));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //设置navigationView内容
    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        LogUtils.i(menuItem.getItemId() + "");
                        menuItem.setCheckable(false);
                        isDrawerOpened = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mDrawerLayout.closeDrawers();
                            }
                        }, 100);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_search:
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        SearchUserActivity.actionStart(MainActivity.this, SearchUserActivity.SEARCH_USER);
                                    }
                                }, 500);
                                break;
                            case R.id.nav_nearly:
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        NearlyDynamicActivity.actionStart(MainActivity.this);
                                    }
                                }, 500);
                                break;
                            case R.id.nav_hot:
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        HotTopicActivity.actionStart(MainActivity.this);
                                    }
                                }, 500);
                                break;
                            case R.id.nav_favorite:
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyFavoriteActivity.actionStart(MainActivity.this);
                                    }
                                }, 500);
                                break;
                            case R.id.nav_draft:
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        DraftActivity.actionStart(MainActivity.this);
                                    }
                                }, 500);
                                break;
                        }
                        return true;
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return toggle.onOptionsItemSelected(item) | super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isDrawerOpened) {
                mDrawerLayout.closeDrawers();
                isDrawerOpened = false;
            } else {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    CommonUtil.showSnackbar(mViewPager.getChildAt(mViewPager.getCurrentItem()), R.string.again_to_exit, getResources().getColor(R.color.Indigo_colorPrimary));
                    firstTime = secondTime;
                } else {
                    finish();
                    removeAllActivities();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class FragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}

package com.yuqirong.koku.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    /**
     * 抽屉
     */
    private DrawerLayout mDrawerLayout;
    /**
     * 在ActionBar上的抽屉按钮
     */
    private ActionBarDrawerToggle toggle;

    private FrameLayout fl_main;
    private Toolbar mToolbar;
    /**
     * 判断DrawerLayout是否打开
     */
    private boolean isDrawerOpened = false;

    @Override
    protected void initData() {
        checkTokenExpireIn();
    }

    //查询用户access_token的授权相关信息
    private void checkTokenExpireIn() {
        final String access_token = SharePrefUtil.getString(this, "access_token", "");
        if (access_token != "") {
            String url = AppConstant.GET_TOKEN_INFO_URL + access_token;
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
            LogUtils.e("Network Error");
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
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
                super.onDrawerOpened(drawerView);
            }
        };
        //把toggle和ActionBar同步状态
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    NavigationView mNavigationView;
    FloatingActionButton fab;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mNavigationView = (NavigationView) findViewById(R.id.mNavigationView);
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }
        fab = (FloatingActionButton) findViewById(R.id.mFloatingActionButton);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setCheckable(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case 0:
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(new Intent(MainActivity.this, SearchUserActivity.class));
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
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
        if (isDrawerOpened && keyCode == KeyEvent.KEYCODE_BACK) {
            mDrawerLayout.closeDrawers();
            isDrawerOpened = false;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

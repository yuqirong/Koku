package com.yuqirong.koku.activity;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lidroid.xutils.BitmapUtils;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.DrawerLayoutAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends BaseActivity {
    /**
     * 抽屉
     */
    private DrawerLayout dl_main;
    /**
     * 在ActionBar上的抽屉按钮
     */
    private ActionBarDrawerToggle toggle;
    private ViewPager vp_main;
    private Toolbar tb_main;
    private FragmentManager fm;
    /**
     * 用户头像
     */
    private CircleImageView civ_avatar;
    /**
     * 用户昵称
     */
    private TextView tv_screen_name;
    /**
     * 用户封面图
     */
    private ImageView iv_cover;
    private BitmapUtils bitmapUtils;
    /**
     * 头像url
     */
    private String avatar_large;
    /**
     * 封面图url
     */
    private String cover_image_phone;
    /**
     * 用户昵称
     */
    private String screen_name;
    /**
     *
     */
    private ListView lv_left;
    private DrawerLayoutAdapter adapter;
    private List<String> list;
    @Override
    protected void initData() {
        bitmapUtils = new BitmapUtils(this);
        list = CommonUtil.getStringFromArrays(R.array.user_operation);
        adapter = new DrawerLayoutAdapter(this,list);
        lv_left.setAdapter(adapter);
        getCache();
        String access_token = SharePrefUtil.getString(this, "access_token", "");
        String uid = SharePrefUtil.getString(this, "uid", "");
        if (access_token != "" && uid != "") {
            String url = AppConstant.USERS_SHOW_URL + "?access_token=" + access_token + "&uid=" + uid;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, listener, errorListener);
            mQueue.add(jsonObjectRequest);
        }
    }

    /**
     * 得到缓存数据
     */
    private void getCache() {
        screen_name = SharePrefUtil.getString(MainActivity.this, "screen_name","");
        cover_image_phone = SharePrefUtil.getString(MainActivity.this,"cover_image_phone","");
        avatar_large = SharePrefUtil.getString(MainActivity.this,"avatar_large","");
        processData();
    }

    Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                screen_name = jsonObject.getString("screen_name");
                cover_image_phone = jsonObject.getString("cover_image_phone");
                avatar_large = jsonObject.getString("avatar_large");
                processData();
                SharePrefUtil.saveString(MainActivity.this, "screen_name", screen_name);
                SharePrefUtil.saveString(MainActivity.this,"cover_image_phone",cover_image_phone);
                SharePrefUtil.saveString(MainActivity.this,"avatar_large",avatar_large);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 处理数据
     */
    private void processData() {
        tv_screen_name.setText(screen_name);
        bitmapUtils.display(iv_cover, cover_image_phone);
        bitmapUtils.display(civ_avatar, avatar_large);
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            LogUtils.e("Network Error");
        }
    };

    @Override
    protected void initToolBar() {
        tb_main.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        toggle = new ActionBarDrawerToggle(this, dl_main, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        //把toggle和ActionBar同步状态
        dl_main.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        dl_main = (DrawerLayout) findViewById(R.id.dl_main);
        civ_avatar = (CircleImageView) dl_main.findViewById(R.id.civ_avatar);
        iv_cover = (ImageView) dl_main.findViewById(R.id.iv_cover);
        tv_screen_name = (TextView) dl_main.findViewById(R.id.tv_screen_name);
        lv_left = (ListView) dl_main.findViewById(R.id.lv_left);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        tb_main = (Toolbar) findViewById(R.id.tb_main);
        fm = getSupportFragmentManager();
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


}

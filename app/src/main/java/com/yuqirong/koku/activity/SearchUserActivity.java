package com.yuqirong.koku.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.SearchUserAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.User;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


/**
 * 搜索用户
 * Created by Anyway on 2015/9/8.
 */
public class SearchUserActivity extends BaseActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RequestQueue mQueue;
    private List<User> user;
    private SearchUserAdapter adapter;

    @Override
    protected void initData() {
        mQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitle(R.string.serach_user);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_user);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        //创建默认的线性LayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //创建并设置Adapter
        adapter = new SearchUserAdapter(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(onQueryTextListener);
        return true;
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            try {
                if (!TextUtils.isEmpty(s)) {
                    String url = AppConstant.SEARCH_USER_URL + "?count=20&q=" + URLEncoder.encode(s, "UTF-8") + "&access_token=" + SharePrefUtil.getString(SearchUserActivity.this, "access_token", "");
                    LogUtils.i(url);
                    getData(url,listener,errorListener);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return true;
        }
    };

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            adapter.clearData();
            user = JsonUtils.getListFromJson(response, User.class);
            for (User u : user) {
                adapter.addData(adapter.list.size(), u);
            }

        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

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

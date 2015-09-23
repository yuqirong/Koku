package com.yuqirong.koku.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.yuqirong.koku.R;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.fragment.SearchUserFragment;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 搜索用户
 * Created by Anyway on 2015/9/8.
 */
public class SearchUserActivity extends BaseActivity {

    private Toolbar mToolbar;
    private FrameLayout mFrameLayout;
    private FragmentManager fm;

    @Override
    protected void initData(Bundle savedInstanceState) {
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mFrameLayout, new SearchUserFragment(), "SearchUserFragment").commit();
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
        mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);
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
                    SearchUserFragment searchUserFragment = (SearchUserFragment) fm.findFragmentByTag("SearchUserFragment");
                    getData(url, searchUserFragment.getListener(), searchUserFragment.getErrorListener());
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

package com.yuqirong.koku.module.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.app.AppConstant;
import com.yuqirong.koku.module.presenter.AuthorizePresenter;
import com.yuqirong.koku.module.view.IAuthorizeView;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.alibaba.fastjson.util.Base64.IA;
import static com.yuqirong.koku.app.AppConstant.RESPONSE_URL;

/**
 * 授权认证Activity
 * Created by Anyway on 2015/8/29.
 */
public class AuthorizeActivity extends BaseActivity implements IAuthorizeView {

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.mWebView)
    WebView mWebView;
    private AuthorizePresenter mAuthorizePresenter;

    @Override
    protected void initData(Bundle savedInstanceState) {
        mAuthorizePresenter = new AuthorizePresenter();
        mAuthorizePresenter.attachView(this);
        String url = AppConstant.AUTHORIZE_URL + "?redirect_uri=" + AppConstant.REDIRECT_URL +
                "&display=mobile&response_type=code" + "&client_id=" + AppConstant.APP_KEY +
                "&scope=friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog";
        if (URLUtil.isNetworkUrl(url)) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void initView() {
        mToolbar.setTitle(R.string.app_authorize);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_drawer_oauth_sina_normal);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(false);
        }
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith(AppConstant.RESPONSE_URL)) {
                    final String code = url.substring(url.indexOf("=") + 1);
                    Map<String, String> filedMap = new HashMap<String, String>();
                    filedMap.put("client_id", AppConstant.APP_KEY);
                    filedMap.put("client_secret", AppConstant.APP_SECRET);
                    filedMap.put("grant_type", "authorization_code");
                    filedMap.put("code", code);
                    filedMap.put("redirect_uri", AppConstant.REDIRECT_URL);
                    mAuthorizePresenter.getOAuthInfo(filedMap);
                }
                return false;
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_authorize;
    }

    @Override
    public void goToMainActivity() {
        Intent intent = new Intent(AuthorizeActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
        mWebView = null;
        mAuthorizePresenter.detachView();
    }
}

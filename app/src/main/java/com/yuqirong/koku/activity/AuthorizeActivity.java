package com.yuqirong.koku.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anyway on 2015/8/29.
 */
public class AuthorizeActivity extends BaseActivity {

    private Toolbar tb_main;
    private WebView wv_main;
    private WebSettings settings;
    public static final String RESPONSE_URL = "https://api.weibo.com/oauth2/default.html?code=";

    public static void actionStart(Context context){
        Intent intent = new Intent(context,AuthorizeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initToolBar() {
        tb_main.setTitle(R.string.app_authorize);
        tb_main.setTitleTextColor(Color.WHITE);
        tb_main.setNavigationIcon(R.drawable.ic_drawer_oauth_sina_normal);
        setSupportActionBar(tb_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_authorize);
        wv_main = (WebView) findViewById(R.id.wv_main);
        tb_main = (Toolbar) findViewById(R.id.tb_main);
        settings = wv_main.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        wv_main.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith(RESPONSE_URL)) {
                    final String code = url.substring(url.indexOf("=")+1);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.ACCESS_TOKEN_URL, listener, errorListener) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("client_id", AppConstant.APP_KEY);
                            map.put("client_secret", AppConstant.APP_SECRET);
                            map.put("grant_type", "authorization_code");
                            map.put("code", code);
                            map.put("redirect_uri", AppConstant.REDIRECT_URL);
                            return map;
                        }
                    };
                    mQueue.add(stringRequest);

                }
                return false;
            }
        });
    }

    protected Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            processData(s);
        }
    };

    private void processData(String s){
            String access_token = JsonUtils.getString(s,"access_token");
            String expires_in = JsonUtils.getString(s,"expires_in");
            String remind_in = JsonUtils.getString(s,"remind_in");
            String uid = JsonUtils.getString(s,"uid");
            if(access_token!=null) {
                SharePrefUtil.saveString(AuthorizeActivity.this, "access_token", access_token);
            }
            if(expires_in!=null) {
                SharePrefUtil.saveString(AuthorizeActivity.this, "expires_in", expires_in);
            }
            if(remind_in!=null) {
                SharePrefUtil.saveString(AuthorizeActivity.this, "remind_in", remind_in);
            }
            if(uid!=null) {
                SharePrefUtil.saveString(AuthorizeActivity.this, "uid", uid);
            }
            Intent intent = new Intent(AuthorizeActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
    }

    protected Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            LogUtils.e("network error :" + volleyError.getMessage());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (URLUtil.isNetworkUrl(AppConstant.AUTHORIZE_URL)) {
            wv_main.loadUrl(AppConstant.AUTHORIZE_URL);
        }
    }
}

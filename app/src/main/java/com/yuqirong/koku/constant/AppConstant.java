package com.yuqirong.koku.constant;

/**
 * Created by Anyway on 2015/8/29.
 */
public class AppConstant {

    /**
     * App Key
     */
    public static final String APP_KEY = "2146675350";
    /**
     * App Secret
     */
    public static final String APP_SECRET = "2c990a9d6f2fd0a59a3f76da68663a58";

    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    /**
     * OAuth2的authorize接口，默认回调页面
     */
    public static final String AUTHORIZE_URL = "https://open.weibo.cn/oauth2/authorize?redirect_uri="+REDIRECT_URL+"&display=mobile&response_type=code"+"&client_id="+APP_KEY;

    /**
     * OAuth2的access_token接口
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";

    /**
     * 根据用户ID获取用户信息
     */
    public static final String USERS_SHOW_URL ="https://api.weibo.com/2/users/show.json";
}

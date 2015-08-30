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
    /**
     * 默认回调页面
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    /**
     * OAuth2的authorize接口
     */
    public static final String AUTHORIZE_URL = "https://open.weibo.cn/oauth2/authorize?redirect_uri="+REDIRECT_URL+"&display=mobile&response_type=code"+"&client_id="+APP_KEY+"&scope=friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog";
    /**
     * OAuth2的access_token接口
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    /**
     * 根据用户ID获取用户信息
     */
    public static final String USERS_SHOW_URL ="https://api.weibo.com/2/users/show.json";
    /**
     * 查询用户access_token的授权相关信息，包括授权时间，过期时间和scope权限。
     */
    public static final String GET_TOKEN_INFO_URL ="https://api.weibo.com/oauth2/get_token_info?access_token=";
    /**
     * 获取当前登录用户及其所关注用户的最新微博
     */
    public static final String FRIENDS_TIMELINE_URL ="https://api.weibo.com/2/statuses/home_timeline.json?access_token=";


}

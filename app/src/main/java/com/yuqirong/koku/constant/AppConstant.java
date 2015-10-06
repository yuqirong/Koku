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
    public static final String AUTHORIZE_URL = "https://open.weibo.cn/oauth2/authorize";
    /**
     * OAuth2的access_token接口
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    /**
     * 根据用户ID获取用户信息
     */
    public static final String USERS_SHOW_URL = "https://api.weibo.com/2/users/show.json";
    /**
     * 查询用户access_token的授权相关信息，包括授权时间，过期时间和scope权限。
     */
    public static final String GET_TOKEN_INFO_URL = "https://api.weibo.com/oauth2/get_token_info";
    /**
     * 获取当前登录用户及其所关注用户的最新微博
     */
    public static final String HOME_TIMELINE_URL = "https://api.weibo.com/2/statuses/home_timeline.json";
    /**
     * 搜索用户时的联想搜索建议
     */
    public static final String SEARCH_USER_URL = "https://api.weibo.com/2/search/suggestions/users.json";
    /**
     * 根据地理信息坐标返回实际地址
     */
    public static final String GEO_TO_ADDRESS_URL = "https://api.weibo.com/2/location/geo/geo_to_address.json";
    /**
     * 发布一条新微博
     */
    public static final String STATUSES_UPDATE_URL = "https://api.weibo.com/2/statuses/update.json";
    /**
     * 上传图片并发布一条新微博
     */
    public static final String STATUSES_UPLOAD_URL = "https://upload.api.weibo.com/2/statuses/upload.json";
    /**
     * 获取双向关注用户的最新微博
     */
    public static final String STATUSES_BILATERAL_TIMELINE_URL = "https://api.weibo.com/2/statuses/bilateral_timeline.json";
    /**
     * 转发一条微博
     */
    public static final String STATUSES_REPOST_URL = "https://api.weibo.com/2/statuses/repost.json";
    /**
     * 对一条微博进行评论
     */
    public static final String COMMENTS_CREATE_URL = "https://api.weibo.com/2/comments/create.json";
    /**
     * 获取用户的关注列表
     */
    public static final String FRIENDSHIPS_FRIENDS_URL = "https://api.weibo.com/2/friendships/friends.json";
    /**
     * 获取某个位置周边的动态
     */
    public static final String PLACE_NEARBY_TIMELINE_URL = "https://api.weibo.com/2/place/nearby_timeline.json";
    /**
     * 获取我的收藏
     */
    public static final String MY_FAVORITE_URL = "https://api.weibo.com/2/favorites.json";
    /**
     * 添加一条微博到收藏里
     */
    public static final String FAVORITE_CREATE_URL = "https://api.weibo.com/2/favorites/create.json";
    /**
     * 取消收藏一条微博
     */
    public static final String FAVORITE_DESTROY_URL = "https://api.weibo.com/2/favorites/destroy.json";

    public static final int[] SWIPE_REFRESH_LAYOUT_COLOR = new int[]{android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_purple};



}

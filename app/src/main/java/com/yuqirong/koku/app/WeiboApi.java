package com.yuqirong.koku.app;

import com.yuqirong.koku.module.model.entity.AccessTokenInfo;
import com.yuqirong.koku.module.model.entity.OAuthInfo;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016-9-29.
 */
public interface WeiboApi {

    /**
     * OAuth2的authorize接口  base_url:https://open.weibo.cn
     */
    @POST("/oauth2/authorize")
    Observable<String> authorizeUser(@Query("redirect_uri") String redirectUri, @Query("display") String display,
                                     @Query("response_type") String responseType, @Query("client_id") String clientId,
                                     @Query("scope") String scope);

    /**
     * OAuth2的access_token接口
     */
    @FormUrlEncoded
    @POST("/oauth2/access_token")
    Observable<OAuthInfo> getOAuthInfo(@FieldMap Map<String, String> filedMap);

    /**
     * 根据用户ID获取用户信息
     */
    @GET("/2/users/show.json")
    Observable<String> getUserInfo(@Query("access_token") String accessToken, @Query("screen_name") String screen_name);

    /**
     * 查询用户access_token的授权相关信息，包括授权时间，过期时间和scope权限。
     */
    @GET("/oauth2/get_token_info")
    Observable<AccessTokenInfo> getAccessTokenInfo(@Field("access_token") String accessToken);

    /**
     * 获取当前登录用户及其所关注用户的最新微博
     */
    @GET("/2/statuses/home_timeline.json")
    Observable<String> getHomeTimeline(@Query("access_token") String accessToken, @Query("max_id") String maxId);

    /**
     * 搜索用户时的联想搜索建议
     */
    @GET("/2/search/suggestions/users.json")
    Observable<String> getSearchUser(@Query("count") String count, @Query("q") String q, @Query("access_token") String accessToken);

    /**
     * 根据地理信息坐标返回实际地址
     */
    @GET("/2/location/geo/geo_to_address.json")
    Observable<String> getAddress(@Query("access_token") String accessToken, @Query("coordinate") String coordinate);

    /**
     * 发布一条新微博
     */
    @POST("/2/statuses/update.json")
    Observable<String> sendNewWeibo(@FieldMap Map<String, String> fieldMap);

    /**
     * 上传图片并发布一条新微博
     */
    @POST("/2/statuses/upload.json")
    Observable<String> sendNewWeiboWithImage(@FieldMap Map<String, String> fieldMap);

    /**
     * 获取双向关注用户的最新微博
     */
    @POST("/2/statuses/bilateral_timeline.json")
    Observable<String> getBilateralTimeline(@Query("access_token") String accessToken, @Query("max_id") String maxId);

    /**
     * 转发一条微博
     */
    @POST("/2/statuses/repost.json")
    Observable<String> repostWeibo(@FieldMap Map<String, String> fieldMap);

    /**
     * 对一条微博进行评论
     */
    @POST("/2/comments/create.json")
    Observable<String> commentWeibo(@FieldMap Map<String, String> fieldMap);

    /**
     * 获取用户的关注列表
     */
    @POST("/2/friendships/friends.json")
    Observable<String> getUserFollowList(@FieldMap Map<String, String> fieldMap);

    /**
     * 获取某个位置周边的动态
     */
    @POST("/2/place/nearby_timeline.json")
    Observable<String> getNearbyTimeline(@FieldMap Map<String, String> fieldMap);

    /**
     * 获取我的收藏
     */
    @POST("/2/favorites.json")
    Observable<String> getMyFavorite(@FieldMap Map<String, String> fieldMap);

    /**
     * 添加一条微博到收藏里
     */
    @POST("/2/favorites/create.json")
    Observable<String> addMyFavorite(@FieldMap Map<String, String> fieldMap);

    /**
     * 取消收藏一条微博
     */
    @POST("/2/favorites/destroy.json")
    Observable<String> deleteMyFavorite(@FieldMap Map<String, String> fieldMap);

    /**
     * 根据微博ID返回某条微博的评论列表
     */
    @POST("/2/comments/show.json")
    Observable<String> showComments(@FieldMap Map<String,String> fieldMap);

    /**
     * 获取指定微博的转发微博列表
     */
    public static final String STATUSES_REPOST_TIMELINE_URL = "https://api.weibo.com/2/statuses/repost_timeline.json";
    /**
     * 批量获取指定微博的转发数评论数
     */
    public static final String STATUSES_COUNT_URL = "https://api.weibo.com/2/statuses/count.json";
    /**
     * 回复一条评论
     */
    public static final String COMMENTS_REPLY_URL = "https://api.weibo.com/2/comments/reply.json";
    /**
     * 获取某个用户最新发表的微博列表
     */
    public static final String STATUSES_USER_TIMELINE_URL = "https://api.weibo.com/2/statuses/user_timeline.json";
    /**
     * 返回最新的公共微博
     */
    public static final String STATUSES_PUBLIC_TIMELINE_URL = "https://api.weibo.com/2/statuses/public_timeline.json";
    /**
     * 获取某个用户的各种消息未读数
     */
    public static final String REMIND_UNREAD_COUNT_URL = "https://rm.api.weibo.com/2/remind/unread_count.json";
    /**
     * 授权回收接口，帮助开发者主动取消用户的授权。
     */
    public static final String OAUTH2_REVOKEOAUTH2_URL = "https://api.weibo.com/oauth2/revokeoauth2";
    /**
     * 获取最新的提到当前登录用户的评论，即@我的评论
     */
    public static final String COMMENTS_MENTIONS_URL = "https://api.weibo.com/2/comments/mentions.json";
    /**
     * 获取当前登录用户所接收到的评论列表
     */
    public static final String COMMENTS_TO_ME_URL = "https://api.weibo.com/2/comments/to_me.json";
    /**
     * 获取最新的提到登录用户的微博列表，即@我的微博
     */
    public static final String STATUSES_MENTIONS_URL = "https://api.weibo.com/2/statuses/mentions.json";

}

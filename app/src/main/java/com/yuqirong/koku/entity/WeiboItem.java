package com.yuqirong.koku.entity;

/**
 * Created by Anyway on 2015/8/30.
 */
public class WeiboItem {

    // weibo创建时间
    public String created_at;
    // weibo来源
    public String source;
    // weibo转发数
    public String reposts_count;
    // weibo评论数
    public String comments_count;
    // weibo用户
    public User user;
    // weibo内容
    public String text;

    public class User {

        // 头像url
        public String profile_image_url;
        // 友好显示名称
        public String name;
        // 是否经认证
        public boolean verified;
    }

}

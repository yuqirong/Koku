package com.yuqirong.koku.entity;

import java.util.List;

/**
 * weibo实例类
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
    // 微博作者的用户信息字段
    public User user;
    // 微博信息内容
    public String text;
    // 微博配图url
    public List<Pic_urls> pic_urls;
    // 转发微博状态
    public Retweeted_status retweeted_status;

    public class Retweeted_status {
        // 字符串型的微博ID
        public String idstr;
        // 微博信息内容
        public String text;
        // 微博作者的用户信息字段
        public User user;
        // weibo转发数
        public String reposts_count;
        // weibo评论数
        public String comments_count;
        // 微博配图url
        public List<Pic_urls> pic_urls;
    }

}

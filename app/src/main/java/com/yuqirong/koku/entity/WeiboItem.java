package com.yuqirong.koku.entity;

/**
 * Created by Anyway on 2015/8/30.
 */
public class WeiboItem {

    public String created_at;
    public String source;
    public String reposts_count;
    public String comments_count;
    public String profile_image_url;
    public String name;
    public boolean verified;
    public String text;

    public WeiboItem(String created_at, String source, String reposts_count, String comments_count, String profile_image_url, String name, boolean verified,String text) {
        this.created_at = created_at;
        this.source = source;
        this.reposts_count = reposts_count;
        this.comments_count = comments_count;
        this.profile_image_url = profile_image_url;
        this.name = name;
        this.verified = verified;
        this.text =text;
    }
}

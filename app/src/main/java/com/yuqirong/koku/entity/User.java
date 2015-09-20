package com.yuqirong.koku.entity;

import java.io.Serializable;

/**
 * 用户实例类
 * Created by Anyway on 2015/9/2.
 */
public class User implements Serializable{

    public String id;

    public String uid;
    // 用户昵称
    public String screen_name;
    // 封面图url
    public String cover_image_phone;
    // 用户头像地址（大图），180×180像素
    public String avatar_large;
    // 用户头像地址（中图），50×50像素
    public String profile_image_url;
    // 友好显示名称
    public String name;
    // 是否经认证
    public boolean verified;
    // 认证原因
    public String verified_reason;
    // 粉丝数
    public String followers_count;
    // 认证类型
    public int verified_type;

}


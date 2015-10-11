package com.yuqirong.koku.entity;

import java.io.Serializable;

/**
 * 用户实例类
 * Created by Anyway on 2015/9/2.
 */
public class User implements Serializable {

    /**
     * allow_all_act_msg : false
     * allow_all_comment : true
     * avatar_hd : http://ww4.sinaimg.cn/crop.175.0.618.618.1024/a39355a0tw1e8r0aofqlnj20q40hbdlf.jpg
     * bi_followers_count : 38
     * block_app : 0
     * block_word : 0
     * city : 1000
     * class : 1
     * created_at : Wed Mar 21 16:43:02 +0800 2012
     * credit_score : 80
     * description : 现在在渡鸦科技做美工
     * domain :
     * favourites_count : 30
     * follow_me : false
     * following : true
     * friends_count : 166
     * gender : m
     * geo_enabled : true
     * idstr : 2744341920
     * lang : zh-cn
     * location : 北京
     * mbrank : 0
     * mbtype : 0
     * online_status : 0
     * pagefriends_count : 1
     * profile_url : u/2744341920
     * province : 11
     * ptype : 0
     * remark :
     * star : 0
     * status_id : 3890199370994640
     * statuses_count : 91
     * urank : 13
     * url :
     * user_ability : 0
     * verified_reason_url :
     * verified_source :
     * verified_source_url :
     * verified_trade :
     * weihao :
     */
    //id
    public long id;
    //当前授权用户的UID
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
    public int followers_count;
    // 认证类型
    public int verified_type;
    //是否允许所有人给我发私信，true：是，false：否
    public boolean allow_all_act_msg;
    //是否允许所有人对我的微博进行评论，true：是，false：否
    public boolean allow_all_comment;
    //用户头像地址（高清），高清头像原图
    public String avatar_hd;
    //用户的互粉数
    public int bi_followers_count;
    //用户所在城市ID
    public String city;
    //用户创建（注册）时间
    public String created_at;
    //用户个人描述
    public String description;
    //用户的个性化域名
    public String domain;
    //收藏数
    public int favourites_count;
    //该用户是否关注当前登录用户，true：是，false：否
    public boolean follow_me;
    //当前登录用户是否关注该用户
    public boolean following;
    //关注数
    public int friends_count;
    //性别，m：男、f：女、n：未知
    public String gender;
    //是否允许标识用户的地理位置，true：是，false：否
    public boolean geo_enabled;
    //字符串型的用户UID
    public String idstr;
    //用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
    public String lang;
    //用户所在地
    public String location;
    //用户的在线状态，0：不在线、1：在线
    public int online_status;
    //用户的微博统一URL地址
    public String profile_url;
    //用户所在省级ID
    public String province;
    //用户备注信息，只有在查询用户关系时才返回此字段
    public String remark;
    //微博数
    public int statuses_count;
    //用户博客地址
    public String url;
    //用户的微号
    public String weihao;

}


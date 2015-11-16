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
    private long id;
    //当前授权用户的UID
    private String uid;
    // 用户昵称
    private String screen_name;
    // 封面图url
    private String cover_image_phone;
    // 用户头像地址（大图），180×180像素
    private String avatar_large;
    // 用户头像地址（中图），50×50像素
    private String profile_image_url;
    // 友好显示名称
    private String name;
    // 是否经认证
    private boolean verified;
    // 认证原因
    private String verified_reason;
    // 粉丝数
    private int followers_count;
    // 认证类型
    private int verified_type;
    //是否允许所有人给我发私信，true：是，false：否
    private boolean allow_all_act_msg;
    //是否允许所有人对我的微博进行评论，true：是，false：否
    private boolean allow_all_comment;
    //用户头像地址（高清），高清头像原图
    private String avatar_hd;
    //用户的互粉数
    private int bi_followers_count;
    //用户所在城市ID
    private String city;
    //用户创建（注册）时间
    private String created_at;
    //用户个人描述
    private String description;
    //用户的个性化域名
    private String domain;
    //收藏数
    private int favourites_count;
    //该用户是否关注当前登录用户，true：是，false：否
    private boolean follow_me;
    //当前登录用户是否关注该用户
    private boolean following;
    //关注数
    private int friends_count;
    //性别，m：男、f：女、n：未知
    private String gender;
    //是否允许标识用户的地理位置，true：是，false：否
    private boolean geo_enabled;
    //字符串型的用户UID
    private String idstr;
    //用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
    private String lang;
    //用户所在地
    private String location;
    //用户的在线状态，0：不在线、1：在线
    private int online_status;
    //用户的微博统一URL地址
    private String profile_url;
    //用户所在省级ID
    private String province;
    //用户备注信息，只有在查询用户关系时才返回此字段
    private String remark;
    //微博数
    private int statuses_count;
    //用户博客地址
    private String url;
    //用户的微号
    private String weihao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getCover_image_phone() {
        return cover_image_phone;
    }

    public void setCover_image_phone(String cover_image_phone) {
        this.cover_image_phone = cover_image_phone;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getVerified_type() {
        return verified_type;
    }

    public void setVerified_type(int verified_type) {
        this.verified_type = verified_type;
    }

    public boolean isAllow_all_act_msg() {
        return allow_all_act_msg;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public boolean isAllow_all_comment() {
        return allow_all_comment;
    }

    public void setAllow_all_comment(boolean allow_all_comment) {
        this.allow_all_comment = allow_all_comment;
    }

    public String getAvatar_hd() {
        return avatar_hd;
    }

    public void setAvatar_hd(String avatar_hd) {
        this.avatar_hd = avatar_hd;
    }

    public int getBi_followers_count() {
        return bi_followers_count;
    }

    public void setBi_followers_count(int bi_followers_count) {
        this.bi_followers_count = bi_followers_count;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public boolean isFollow_me() {
        return follow_me;
    }

    public void setFollow_me(boolean follow_me) {
        this.follow_me = follow_me;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWeihao() {
        return weihao;
    }

    public void setWeihao(String weihao) {
        this.weihao = weihao;
    }
}


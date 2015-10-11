package com.yuqirong.koku.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 单条weibo实例类
 * Created by Anyway on 2015/8/30.
 */
public class Status implements Serializable {

    /**
     * "attitudes_count": 0,
     * "biz_feature": 0,
     * "comments_count": 0,
     * "created_at": "Fri Sep 25 08:48:07 +0800 2015",
     * "darwin_tags": [],
     * "favorited": false,
     * "id": 3890882283247737,
     * "idstr": "3890882283247737",
     * "in_reply_to_screen_name": "",
     * "in_reply_to_status_id": "",
     * "in_reply_to_user_id": "",
     * "mid": "3890882283247737",
     * "mlevel": 0,
     * "pic_urls": [],
     * "reposts_count": 0,
     * "retweeted_status": {
     * "attitudes_count": 48,
     * "biz_feature": 0,
     * "bmiddle_pic": "http://ww3.sinaimg.cn/bmiddle/80292f4bjw1ewe1t89okvj20c80cd0um.jpg",
     * "cardid": "vip_008",
     * "comments_count": 2,
     * "created_at": "Fri Sep 25 02:12:03 +0800 2015",
     * "darwin_tags": [],
     * "favorited": false,
     * "id": 3890782605461147,
     * "idstr": "3890782605461147",
     * "in_reply_to_screen_name": "",
     * "in_reply_to_status_id": "",
     * "in_reply_to_user_id": "",
     * "mid": "3890782605461147",
     * "mlevel": 0,
     * "original_pic": "http://ww3.sinaimg.cn/large/80292f4bjw1ewe1t89okvj20c80cd0um.jpg",
     * "pic_urls": [
     * {
     * "thumbnail_pic": "http://ww3.sinaimg.cn/thumbnail/80292f4bjw1ewe1t89okvj20c80cd0um.jpg"
     * },
     * {
     * "thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/80292f4bjw1ewe1t8w3uaj20c80cdgnb.jpg"
     * },
     * {
     * "thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/80292f4bjw1ewe1t9lk6kj20c80cdabt.jpg"
     * },
     * {
     * "thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/80292f4bjw1ewe1ta17xmj20c80cddhq.jpg"
     * },
     * {
     * "thumbnail_pic": "http://ww3.sinaimg.cn/thumbnail/80292f4bjw1ewe1tal109j20c80cd765.jpg"
     * },
     * {
     * "thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/80292f4bjw1ewe1taedmxj20c80cd40b.jpg"
     * },
     * {
     * "thumbnail_pic": "http://ww3.sinaimg.cn/thumbnail/80292f4bjw1ewe1taxrtqj20c80cdq4q.jpg"
     * },
     * {
     * "thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/80292f4bjw1ewe1tbfqnaj20c80cd761.jpg"
     * },
     * {
     * "thumbnail_pic": "http://ww4.sinaimg.cn/thumbnail/80292f4bjw1ewe1tbtfevj20c80cdq4q.jpg"
     * }
     * ],
     * "reposts_count": 89,
     * "source": "<a href="http://weibo.com/" rel="nofollow">微博 weibo.com</a>",
     * "source_allowclick": 0,
     * "source_type": 1,
     * "text": "处在低迷期，有些迷茫的童鞋看看--自我激励的十八种办法。分享，共勉",
     * "thumbnail_pic": "http://ww3.sinaimg.cn/thumbnail/80292f4bjw1ewe1t89okvj20c80cd0um.jpg",
     * "truncated": false,
     * "user": {
     * "ability_tags": "媒体评论,编辑,媒体营销,出品人",
     * "allow_all_act_msg": true,
     * "allow_all_comment": true,
     * "avatar_hd": "http://tp4.sinaimg.cn/2150182731/180/5613586011/1",
     * "avatar_large": "http://tp4.sinaimg.cn/2150182731/180/5613586011/1",
     * "bi_followers_count": 34,
     * "block_app": 1,
     * "block_word": 0,
     * "cardid": "vip_008",
     * "city": "1",
     * "class": 1,
     * "cover_image_phone": "http://ww1.sinaimg.cn/crop.0.0.640.640.640/a1d3feabjw1ecat4uqw77j20hs0hsacp.jpg",
     * "created_at": "Fri May 27 22:18:39 +0800 2011",
     * "credit_score": 80,
     * "description": "无责任爆料吐槽，关注后极易任性、躺枪、呆萌、自黑、喝醉、笑抽。。。。莫愁前路无知己，土豪我们在一起！不说了，先看个笑话压压惊。。。",
     * "domain": "zhuzhu3610",
     * "favourites_count": 12,
     * "follow_me": false,
     * "followers_count": 16708560,
     * "following": false,
     * "friends_count": 55,
     * "gender": "m",
     * "geo_enabled": false,
     * "id": 2150182731,
     * "idstr": "2150182731",
     * "lang": "zh-cn",
     * "location": "北京 东城区",
     * "mbrank": 5,
     * "mbtype": 12,
     * "name": "猪猪爱讲冷笑话",
     * "online_status": 0,
     * "pagefriends_count": 3,
     * "profile_image_url": "http://tp4.sinaimg.cn/2150182731/50/5613586011/1",
     * "profile_url": "zhuzhu3610",
     * "province": "11",
     * "ptype": 8,
     * "remark": "",
     * "screen_name": "猪猪爱讲冷笑话",
     * "star": 0,
     * "statuses_count": 136762,
     * "urank": 28,
     * "url": "",
     * "user_ability": 0,
     * "verified": true,
     * "verified_contact_email": "",
     * "verified_contact_mobile": "",
     * "verified_contact_name": "",
     * "verified_level": 3,
     * "verified_reason": "秒拍搞笑博主 微博签约自媒体",
     * "verified_reason_modified": "",
     * "verified_reason_url": "",
     * "verified_source": "",
     * "verified_source_url": "",
     * "verified_state": 0,
     * "verified_trade": "3374",
     * "verified_type": 0,
     * "weihao": ""
     * },
     * "userType": 0,
     * "visible": {
     * "list_id": 0,
     * "type": 0
     * }
     * },
     * "rid": "0_0_2_2666870349471306194",
     * "source": "<a href="http://app.weibo.com/t/feed/6G7MnG" rel="nofollow">三星GALAXY S4</a>",
     * "source_allowclick": 0,
     * "source_type": 1,
     * "text": "转发微博",
     * "truncated": false,
     * "user": {
     * "allow_all_act_msg": false,
     * "allow_all_comment": true,
     * "avatar_hd": "http://ww2.sinaimg.cn/crop.0.0.1080.1080.1024/d60812fajw8etvsx5jcc3j20u00u0tck.jpg",
     * "avatar_large": "http://tp3.sinaimg.cn/3590853370/180/5731337978/1",
     * "bi_followers_count": 8,
     * "block_app": 0,
     * "block_word": 0,
     * "city": "81",
     * "class": 1,
     * "created_at": "Fri Jul 12 15:53:01 +0800 2013",
     * "credit_score": 80,
     * "description": "这个你想不到的世界！",
     * "domain": "",
     * "favourites_count": 0,
     * "follow_me": true,
     * "followers_count": 28,
     * "following": true,
     * "friends_count": 156,
     * "gender": "m",
     * "geo_enabled": true,
     * "id": 3590853370,
     * "idstr": "3590853370",
     * "lang": "zh-cn",
     * "location": "重庆 江津市",
     * "mbrank": 0,
     * "mbtype": 0,
     * "name": "宛螖",
     * "online_status": 0,
     * "pagefriends_count": 0,
     * "profile_image_url": "http://tp3.sinaimg.cn/3590853370/50/5731337978/1",
     * "profile_url": "u/3590853370",
     * "province": "50",
     * "ptype": 0,
     * "remark": "",
     * "screen_name": "宛螖",
     * "star": 0,
     * "statuses_count": 202,
     * "urank": 10,
     * "url": "",
     * "user_ability": 0,
     * "verified": false,
     * "verified_reason": "",
     * "verified_reason_url": "",
     * "verified_source": "",
     * "verified_source_url": "",
     * "verified_trade": "",
     * "verified_type": -1,
     * "weihao": ""
     * },
     * "userType": 0,
     * "visible": {
     * "list_id": 0,
     * "type": 0
     * }
     */

    // 字符串型的微博ID
    public String idstr;
    // weibo创建时间
    public String created_at;
    // 微博ID
    public long id;
    // 微博MID
    public long mid;
    // weibo来源
    public String source;
    //是否已收藏，true：是，false：否
    public boolean favorited;
    //是否被截断，true：是，false：否
    public boolean truncated;
    //地理信息字段
    public Geo geo;
    // weibo转发数
    public int reposts_count;
    // weibo评论数
    public int comments_count;
    //表态数
    public int attitudes_count;
    // 微博作者的用户信息字段
    public User user;
    // 微博信息内容
    public String text;
    // 微博配图url
    public List<Pic_urls> pic_urls;
    // 转发微博状态
    public Retweeted_status retweeted_status;
    //原始图片地址，没有时不返回此字段
    public String original_pic;
    //中等尺寸图片地址，没有时不返回此字段
    public String bmiddle_pic;
    //缩略图片地址，没有时不返回此字段
    public String thumbnail_pic;

    public List<Annotations> annotations;


    public class Retweeted_status implements Serializable {

        // 微博ID
        public long id;
        // 微博MID
        public long mid;
        // weibo创建时间
        public String created_at;
        //是否已收藏，true：是，false：否
        public boolean favorited;
        //是否被截断，true：是，false：否
        public boolean truncated;
        // 字符串型的微博ID
        public String idstr;
        // 微博信息内容
        public String text;
        // 微博作者的用户信息字段
        public User user;
        //地理信息字段
        public Geo geo;
        // weibo转发数
        public int reposts_count;
        // weibo评论数
        public int comments_count;
        //表态数
        public int attitudes_count;
        // 微博配图url
        public List<Pic_urls> pic_urls;
        //原始图片地址，没有时不返回此字段
        public String original_pic;
        //中等尺寸图片地址，没有时不返回此字段
        public String bmiddle_pic;
        //缩略图片地址，没有时不返回此字段
        public String thumbnail_pic;
        //被转发微博是否被删除  "1"为是
        public String deleted;
    }

    public class Annotations implements Serializable {
        public String client_mblogid;
        public Place place;
        public boolean mapi_request;
        public int shooting;

        public class Place implements Serializable {
            public double lat;
            public double lon;
            public String poiid;
            public String title;
            public String type;
        }

    }

}

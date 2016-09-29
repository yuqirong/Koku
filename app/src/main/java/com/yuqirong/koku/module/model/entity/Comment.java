package com.yuqirong.koku.module.model.entity;

/**
 * Created by Anyway on 2015/10/11.
 */
public class Comment {

    //评论创建时间
    public String created_at;
    //评论的ID
    public long id;
    //评论的内容
    public String text;
    //评论的来源
    public String source;
    //评论作者的用户信息字段
    public User user;
    //评论的MID
    public String mid;
    //字符串型的评论ID
    public String idstr;
    //评论的微博信息字段
    public Status status;
    //评论来源评论，当本评论属于对另一评论的回复时返回此字段
    public Reply_comment reply_comment;

    static class Reply_comment {

    }
}

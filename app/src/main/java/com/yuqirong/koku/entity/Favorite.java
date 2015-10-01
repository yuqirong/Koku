package com.yuqirong.koku.entity;

import java.util.List;

/**
 * 微博收藏类
 * Created by Anyway on 2015/10/1.
 */
public class Favorite {

    public String favorited_time;
    public Status status;
    public List<Tag> tags;

    public class Tag {
        public int count;
        public int id;
        public String tag;
    }

}

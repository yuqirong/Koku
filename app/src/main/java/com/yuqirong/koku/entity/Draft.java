package com.yuqirong.koku.entity;

import java.util.List;

/**
 * Created by Anyway on 2015/10/6.
 */
public class Draft {

    public Draft(){}

    public Draft(int id, int type, String text, List<String> pic_urls) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.pic_urls = pic_urls;
    }



    //id
    public int id;
    //类型
    public int type;
    //内容
    public String text;
    //图片url
    public List<String> pic_urls;
}

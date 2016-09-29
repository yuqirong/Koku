package com.yuqirong.koku.module.model.entity;

import java.io.Serializable;

/**
 * 地理信息类
 * Created by Anyway on 2015/9/25.
 */
public class Geo implements Serializable {

    // 经度坐标
    public String longitude;
    //纬度坐标
    public String latitude;
    //所在城市的城市代码
    public String city;
    //所在省份的省份代码
    public String province;
    // 所在城市的城市名称
    public String city_name;
    // 所在省份的省份名称
    public String province_name;
    //所在的实际地址，可以为空
    public String address;
    // 地址的汉语拼音，不是所有情况都会返回该字段
    public String pinyin;
    // 更多信息，不是所有情况都会返回该字段
    public String more;

}

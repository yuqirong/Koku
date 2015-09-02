package com.yuqirong.koku.util;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Anyway on 2015/8/29.
 */
public class JsonUtils {

    private static String tag = JsonUtils.class.getSimpleName();

    public static String getString(String json, String name) {
        String temp = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            temp = jsonObject.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 把json转化为bean
     *
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBeanFromJson(String jsonString, Class<T> cls) {
        T t = JSON.parseObject(jsonString, cls);
        return t;
    }

    /**
     * 把json转化为List
     *
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> getListFromJson(String jsonString, Class<T> cls) {
        List<T> list = JSON.parseArray(jsonString, cls);
        return list;
    }

}

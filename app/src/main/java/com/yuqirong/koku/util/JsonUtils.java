package com.yuqirong.koku.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anyway on 2015/8/29.
 */
public class JsonUtils {

    private static String tag = JsonUtils.class.getSimpleName();

    public static String getString(String json,String name){
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

}

package com.yuqirong.koku.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Anyway on 2015/8/29.
 */
public class CommonUtil {

    /**
     * 如果转发数或评论数超过一万，就简写成 n万
     * @param num
     * @return
     */
    public static String getNumString(String num) {
        if(!TextUtils.isEmpty(num)){
            int number = Integer.parseInt(num)/10000;
            if(number == 0){
                return num;
            }else{
                return number + "万";
            }
        }
        return "";
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}

package com.yuqirong.koku.util;

import android.content.Context;

import com.yuqirong.koku.R;
import com.yuqirong.koku.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/8/29.
 */
public class CommonUtil {

    /**
     * 从资源文件中读取String Array，添加到List中
     * @param resId
     * @return
     */
    public static List<String> getStringFromArrays(int resId) {
        String[] stringArray = MyApplication.getContext().getResources().getStringArray(R.array.user_operation);
        List<String> list = new ArrayList<>();
        for (String s : stringArray) {
            list.add(s);
        }
        return list;
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

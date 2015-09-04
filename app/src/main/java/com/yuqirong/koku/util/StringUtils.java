package com.yuqirong.koku.util;

import com.yuqirong.koku.R;
import com.yuqirong.koku.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/9/4.
 */
public class StringUtils {

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

}

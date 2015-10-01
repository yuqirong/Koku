package com.yuqirong.koku.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anyway on 2015/8/31.
 */
public class RelativeDateFormat {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";

    public static String format(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        long delta = System.currentTimeMillis() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 60L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        } else {
            String month = String.valueOf(c.get(Calendar.MONTH) + 1);
            String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            String minute = String.valueOf(c.get(Calendar.MINUTE));
//            String second = String.valueOf(c.get(Calendar.SECOND));
            if (c.get(Calendar.MONTH) + 1 < 10) {
                month = "0" + month;
            }
            if (c.get(Calendar.DAY_OF_MONTH) < 10) {
                day = "0" + day;
            }
            if (c.get(Calendar.HOUR_OF_DAY) < 10) {
                hour = "0" + hour;
            }
            if (c.get(Calendar.MINUTE) < 10) {
                minute = "0" + minute;
            }
//            if (c.get(Calendar.SECOND) < 10) {
//                second = "0" + second;
//            }
            return c.get(Calendar.YEAR) + "-" + month + "-" + day + " " + hour + ":" + minute;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }


}

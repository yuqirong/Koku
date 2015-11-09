package com.yuqirong.koku.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.UserDetailsActivity;
import com.yuqirong.koku.activity.WebViewActivity;
import com.yuqirong.koku.db.EmotionsDB;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anyway on 2015/9/4.
 */
public class StringUtils {

    // 定义正则表达式
    private static final String AT = "@[\u4e00-\u9fa5-\\w]+";// @人
    private static final String TOPIC = "#[\u4e00-\u9fa5\\w]+#";// ##话题
    private static final String EMOJI = "\\[[\u4e00-\u9fa5\\w]+\\]";// 表情
    private static final String URL = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";// url

    private static final String REGEX = "(" + AT + ")|(" + TOPIC + ")|(" + EMOJI + ")|(" + URL + ")";

    /**
     * 把String转化为List
     *
     * @param pic_urls
     * @return
     */
    public static List<String> convertStringToList(String pic_urls) {
        List<String> list = new ArrayList<>();
        String[] arrays = pic_urls.split(",");
        int size = arrays.length;
        for (int i = 0; i < size; i++) {
            list.add(arrays[i]);
        }
        return list;
    }

    /**
     * 把String转化为List
     *
     * @param list
     * @return
     */
    public static String convertListToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i) + ",");
        }
        return builder.toString();
    }

    /**
     * 设置微博内容样式
     *
     * @param context
     * @param source
     * @param textView
     * @return
     */
    public static SpannableString getWeiBoContent(final Context context, String source, TextView textView) {
        SpannableString spannableString = new SpannableString(source);

        //设置正则
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(spannableString);

        if (matcher.find()) {
            // 要实现文字的点击效果，这里需要做特殊处理
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            // 重置正则位置
            matcher.reset();
        }

        while (matcher.find()) {
            // 根据group的括号索引，可得出具体匹配哪个正则(0代表全部，1代表第一个括号)
            final String at = matcher.group(1);
            final String topic = matcher.group(2);
            String emoji = matcher.group(3);
            final String url = matcher.group(4);

            // 处理@符号
            if (at != null) {
                //获取匹配位置
                int start = matcher.start(1);
                int end = start + at.length();
                MyClickableSpan clickableSpan = new MyClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        //点击了用户跳转事件
                        LogUtils.i("点击了用户：" + at);
                        UserDetailsActivity.actionStart(context, at.substring(1));
                    }
                };
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // 处理话题##符号
            if (topic != null) {
                int start = matcher.start(2);
                int end = start + topic.length();
                MyClickableSpan clickableSpan = new MyClickableSpan(context) {

                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(context, "点击了话题：" + topic, Toast.LENGTH_LONG).show();
                    }
                };
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (emoji != null) {
                int start = matcher.start(3);
                int end = start + emoji.length();

                byte[] emotion = EmotionsDB.getEmotion(emoji);
                if (emotion != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(emotion, 0, emotion.length);
                    if (bitmap != null) {
                        // 设置表情
                        int size = (int) textView.getTextSize();
                        // 压缩Bitmap 设置为字体大小的1.5倍
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (size * 1.5), (int) (size * 1.5), true);

                        ImageSpan imageSpan = new ImageSpan(context, bitmap);
                        spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            // 处理url地址
            if (url != null) {
                int start = matcher.start(4);
                int end = start + url.length();
                MyClickableSpan clickableSpan = new MyClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        LogUtils.i("点击了网址：" + url);
                        if (SharePrefUtil.getBoolean(context, "built-in_browser", true)) {
                            WebViewActivity.actionStart(context, url);
                        } else {
                            CommonUtil.openInBrowser(context, url);
                        }
                    }
                };
                spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }

    /**
     * 继承ClickableSpan复写updateDrawState方法，自定义所需样式
     *
     * @author Rabbit_Lee
     */
    public static class MyClickableSpan extends ClickableSpan {

        private Context context;

        public MyClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(context.getResources().getColor(R.color.span_text_color));
            ds.setUnderlineText(false);
        }

    }

}

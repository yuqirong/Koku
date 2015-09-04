package com.yuqirong.koku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.LogUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anyway on 2015/8/31.
 */
public class WeiboListViewAdapter extends MBaseAdapter<WeiboItem, ListView> {

    private BitmapUtils bitmapUtils;
    public static final String AT = "@";
    public static final String BLANK_SPACE = " ";

    public WeiboListViewAdapter(Context context, List<WeiboItem> list) {
        super(context, list);
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeiboItem weiboItem = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.weibo_original_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_screen_name.setText(weiboItem.user.name);
        bitmapUtils.display(viewHolder.civ_avatar, weiboItem.user.profile_image_url);
        viewHolder.tv_time.setText(DateUtils.getWeiboDate(weiboItem.created_at));
        viewHolder.tv_device.setText(Html.fromHtml(weiboItem.source));
        if (weiboItem.user.verified) {
            viewHolder.iv_verified.setImageResource(R.drawable.avatar_vip);
        } else {
            viewHolder.iv_verified.setImageResource(android.R.drawable.screen_background_light_transparent);
        }
        viewHolder.tv_repost_count.setText(CommonUtil.getNumString(weiboItem.reposts_count));
        viewHolder.tv_comment_count.setText(CommonUtil.getNumString(weiboItem.comments_count));
        processClickableText(viewHolder,weiboItem.text);
        if (weiboItem.retweeted_status != null) {
            processRetweeted(convertView, viewHolder, weiboItem);
            viewHolder.ll_item.addView(viewHolder.view_retweeted);
        } else {
            if (viewHolder.view_retweeted != null)
                viewHolder.ll_item.removeView(viewHolder.view_retweeted);
        }
        return convertView;
    }

    // 处理被转发的View
    private void processRetweeted(View convertView, ViewHolder viewHolder, WeiboItem weiboItem) {
        if (viewHolder.view_retweeted == null) {
            viewHolder.view_retweeted = LayoutInflater.from(context).inflate(R.layout.weibo_retweeted_item, null);
            viewHolder.initRetweetedView();
        }
        setClickableSpan(viewHolder, weiboItem);
        viewHolder.tv_retweeted_repost_count.setText(CommonUtil.getNumString(weiboItem.retweeted_status.reposts_count));
        viewHolder.tv_retweeted_comment_count.setText(CommonUtil.getNumString(weiboItem.retweeted_status.comments_count));

        ViewParent parent = viewHolder.view_retweeted.getParent();
        if (parent != null) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(viewHolder.view_retweeted);
        }
        convertView.setTag(viewHolder);
    }

    // 设置文字中可点击span
    private void setClickableSpan(ViewHolder viewHolder, WeiboItem weiboItem) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(AT + weiboItem.retweeted_status.user.name);
        spannable.setSpan(clickableSpan, 0, weiboItem.retweeted_status.user.name.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tv_retweeted_name_text.setText(spannable);
        viewHolder.tv_retweeted_name_text.append(context.getResources().getString(R.string.colon) + weiboItem.retweeted_status.text);
        viewHolder.tv_retweeted_name_text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    // 处理内容中@ 字段
    public void processClickableText(ViewHolder viewHolder, String text) {
        if (text.contains(AT)){
            String substring = text.substring(text.indexOf(AT), text.indexOf(":", text.indexOf(AT)));
            if(substring.length()>0){
                Pattern p = Pattern.compile("@[a-zA-Z0-9\\u4e00-\\u9fa5_-]{4,30}");
                Matcher m = p.matcher(substring);
                if(m.matches()){
                    SpannableString spannable = new SpannableString(substring);
                    spannable.setSpan(clickableSpan, 0, substring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    text.replace(spannable,substring);
                }
            }
        }
        viewHolder.tv_text.setText(text);
    }


    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            LogUtils.i("click span");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(context.getResources().getColor(R.color.span_text_color));
            ds.setUnderlineText(false);
        }
    };

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_item;
        public TextView tv_screen_name;
        public ImageView civ_avatar;
        public TextView tv_time;
        public TextView tv_device;
        public ImageView iv_verified;
        public TextView tv_repost_count;
        public TextView tv_comment_count;
        public TextView tv_text;
        public TextView tv_retweeted_name_text;
        public View view_retweeted;
        public TextView tv_retweeted_repost_count;
        public TextView tv_retweeted_comment_count;

        private void initRetweetedView() {
            tv_retweeted_name_text = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_name_text);
            tv_retweeted_repost_count = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_repost_count);
            tv_retweeted_comment_count = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_comment_count);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            civ_avatar = (ImageView) itemView.findViewById(R.id.civ_avatar);
            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_device = (TextView) itemView.findViewById(R.id.tv_device);
            iv_verified = (ImageView) itemView.findViewById(R.id.iv_verified);
            tv_repost_count = (TextView) itemView.findViewById(R.id.tv_repost_count);
            tv_comment_count = (TextView) itemView.findViewById(R.id.tv_comment_count);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
        }

    }
}

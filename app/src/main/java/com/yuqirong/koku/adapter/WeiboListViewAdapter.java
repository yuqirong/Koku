package com.yuqirong.koku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.DateUtils;

import java.util.List;

/**
 * Created by Anyway on 2015/8/31.
 */
public class WeiboListViewAdapter extends MBaseAdapter<WeiboItem,ListView> {

    private BitmapUtils bitmapUtils;

    public WeiboListViewAdapter(Context context, List<WeiboItem> list) {
        super(context, list);
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeiboItem weiboItem = list.get(position);
        ViewHolder viewHolder;
        if(convertView ==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recycleview_weibo_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_screen_name.setText(weiboItem.user.name);
        bitmapUtils.display(viewHolder.civ_avatar, weiboItem.user.profile_image_url);
        viewHolder.tv_time.setText(DateUtils.getWeiboDate(weiboItem.created_at));
        viewHolder.tv_device.setText(Html.fromHtml(weiboItem.source));
        if (weiboItem.user.verified) {
            viewHolder.iv_verified.setImageResource(R.drawable.avatar_vip);
        }
        viewHolder.tv_repost_count.setText(CommonUtil.getNumString(weiboItem.reposts_count));
        viewHolder.tv_comment_count.setText(CommonUtil.getNumString(weiboItem.comments_count));
        viewHolder.tv_text.setText(weiboItem.text);
        return convertView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_screen_name;
        public ImageView civ_avatar;
        public TextView tv_time;
        public TextView tv_device;
        public ImageView iv_verified;
        public TextView tv_repost_count;
        public TextView tv_comment_count;
        public TextView tv_text;

        public ViewHolder(View itemView) {
            super(itemView);
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

package com.yuqirong.koku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.Pic_urls;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/8/31.
 */
public class WeiboListViewAdapter extends MBaseAdapter<WeiboItem, ListView> {

    private BitmapUtils bitmapUtils;
    public static final String AT = "@";
    public static final int DISK_CACHE_SIZE = 10 * 1024 * 1024;

    private static final int[] IMAGEVIEW_IDS = new int[]{R.id.iv_01, R.id.iv_02, R.id.iv_03, R.id.iv_04, R.id.iv_05, R.id.iv_06, R.id.iv_07, R.id.iv_08, R.id.iv_09};

    public WeiboListViewAdapter(Context context, List<WeiboItem> list) {
        super(context, list);
        String bitmap_cache_dir = context.getCacheDir() + File.separator + "bitmap";
        int cache = (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024 * 8));
        bitmapUtils = new BitmapUtils(context, bitmap_cache_dir, cache, DISK_CACHE_SIZE);
        bitmapUtils.configDefaultLoadingImage(context.getResources().getDrawable(R.drawable.thumbnail_default));
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
        //设置微博 转发数和评论数
        viewHolder.tv_repost_count.setText(CommonUtil.getNumString(weiboItem.reposts_count));
        viewHolder.tv_comment_count.setText(CommonUtil.getNumString(weiboItem.comments_count));

        //设置微博内容
        SpannableString weiBoContent = StringUtils.getWeiBoContent(context, weiboItem.text, viewHolder.tv_text);
        viewHolder.tv_text.setText(weiBoContent);

        if (weiboItem.pic_urls != null && weiboItem.pic_urls.size() > 0) {
            viewHolder.initImageView(viewHolder.rl_pics, viewHolder.iv_arrays, weiboItem.pic_urls);
        } else {
            if (viewHolder.iv_arrays != null)
                viewHolder.rl_pics.setVisibility(View.GONE);
        }

        if (weiboItem.retweeted_status != null) {
            processRetweeted(viewHolder, weiboItem);
            viewHolder.ll_item.addView(viewHolder.view_retweeted);
        } else {
            if (viewHolder.view_retweeted != null)
                viewHolder.ll_item.removeView(viewHolder.view_retweeted);
        }
        return convertView;
    }

    // 处理被转发的View
    private void processRetweeted(ViewHolder viewHolder, WeiboItem weiboItem) {
        if (viewHolder.view_retweeted == null) {
            viewHolder.view_retweeted = LayoutInflater.from(context).inflate(R.layout.weibo_retweeted_item, null);
            viewHolder.initRetweetedView();
        }

        //设置被转发微博内容
        SpannableString weiBoContent = StringUtils.getWeiBoContent(context, AT + weiboItem.retweeted_status.user.name + context.getResources().getString(R.string.colon) + weiboItem.retweeted_status.text, viewHolder.tv_retweeted_name_text);
        viewHolder.tv_retweeted_name_text.setText(weiBoContent);

        if (weiboItem.retweeted_status.pic_urls != null && weiboItem.retweeted_status.pic_urls.size() > 0) {
            viewHolder.initImageView(viewHolder.rl_retweeted_pics, viewHolder.iv_retweeted_arrays, weiboItem.retweeted_status.pic_urls);
        } else {
            if (viewHolder.iv_retweeted_arrays != null)
                viewHolder.rl_retweeted_pics.setVisibility(View.GONE);
        }

        //设置被转发微博 转发数和评论数
        viewHolder.tv_retweeted_repost_count.setText(CommonUtil.getNumString(weiboItem.retweeted_status.reposts_count));
        viewHolder.tv_retweeted_comment_count.setText(CommonUtil.getNumString(weiboItem.retweeted_status.comments_count));

        ViewParent parent = viewHolder.view_retweeted.getParent();
        if (parent != null) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(viewHolder.view_retweeted);
        }
    }

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_item;
        public TextView tv_screen_name;
        public ImageView civ_avatar;
        public TextView tv_time;
        public TextView tv_device;
        public ImageView iv_verified;
        public TextView tv_repost_count;
        public TextView tv_comment_count;
        public TextView tv_text;
        public List<ImageView> iv_arrays = new ArrayList<>();
        public List<ImageView> iv_retweeted_arrays = new ArrayList<>();
        public TextView tv_retweeted_name_text;
        public View view_retweeted;
        public TextView tv_retweeted_repost_count;
        public TextView tv_retweeted_comment_count;
        public RelativeLayout rl_pics;
        public RelativeLayout rl_retweeted_pics;

        private void initImageView(RelativeLayout rl, List<ImageView> iv_arrays, List<Pic_urls> pic_urls) {
            rl.setVisibility(View.VISIBLE);
            for (int i = 0; i < iv_arrays.size(); i++) {
                if (i < pic_urls.size()) {
                    iv_arrays.get(i).setVisibility(View.VISIBLE);
                    bitmapUtils.display(iv_arrays.get(i), pic_urls.get(i).thumbnail_pic);
                } else {
                    iv_arrays.get(i).setVisibility(View.GONE);
                }
            }
        }

        private void initRetweetedView() {
            tv_retweeted_name_text = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_name_text);
            tv_retweeted_repost_count = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_repost_count);
            tv_retweeted_comment_count = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_comment_count);
            rl_retweeted_pics = (RelativeLayout) view_retweeted.findViewById(R.id.rl_pics);
            for (int i = 0; i < IMAGEVIEW_IDS.length; i++) {
                ImageView iv = (ImageView) view_retweeted.findViewById(IMAGEVIEW_IDS[i]);
                iv_retweeted_arrays.add(iv);
            }
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
            rl_pics = (RelativeLayout) itemView.findViewById(R.id.rl_pics);
            for (int i = 0; i < IMAGEVIEW_IDS.length; i++) {
                ImageView iv = (ImageView) itemView.findViewById(IMAGEVIEW_IDS[i]);
                iv_arrays.add(iv);
            }
        }

    }
}

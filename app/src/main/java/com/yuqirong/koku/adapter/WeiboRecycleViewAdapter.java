package com.yuqirong.koku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.MainActivity;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.activity.UserDetailsActivity;
import com.yuqirong.koku.entity.Pic_urls;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 微博适配器
 * Created by Anyway on 2015/8/30.
 */
public class WeiboRecycleViewAdapter extends LoadMoreAdapter<WeiboItem> {

    private Context context;
    private BitmapUtils bitmapUtils;
    public static final String AT = "@";
    private static final int[] IMAGEVIEW_IDS = new int[]{R.id.iv_01, R.id.iv_02, R.id.iv_03, R.id.iv_04, R.id.iv_05, R.id.iv_06, R.id.iv_07, R.id.iv_08, R.id.iv_09};
    private WeiboItem weiboItem;

    public WeiboRecycleViewAdapter(Context context) {
        this.context = context;
        bitmapUtils = BitmapUtil.getBitmapUtils(context);
        list.add(new WeiboItem());
    }

    @Override
    public RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weibo_original_item, parent, false);
        WeiboViewHolder holder = new WeiboViewHolder(view);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(RecyclerView.ViewHolder holder, final int position) {
        weiboItem = list.get(position);
        WeiboViewHolder viewHolder = (WeiboViewHolder) holder;
        //创建监听器实例
        viewHolder.onMenuItemClickListener = new MyOnMenuItemClickListener(position);
        viewHolder.onClickListener = new WeiboWidghtOnClickListener(position,viewHolder.onMenuItemClickListener);

        viewHolder.tv_screen_name.setText(weiboItem.user.name);
        bitmapUtils.display(viewHolder.iv_avatar, weiboItem.user.profile_image_url);
        viewHolder.tv_time.setText(DateUtils.getWeiboDate(weiboItem.created_at));
        viewHolder.tv_device.setText(Html.fromHtml(weiboItem.source));
        //设置认证图标
        switch (weiboItem.user.verified_type) {
            case 0:
                viewHolder.iv_verified.setImageResource(R.drawable.avatar_vip);
                break;
            case -1:
                viewHolder.iv_verified.setImageResource(android.R.color.transparent);
                break;
            case 220:
                viewHolder.iv_verified.setImageResource(R.drawable.avatar_grassroot);
                break;
            default:
                viewHolder.iv_verified.setImageResource(R.drawable.avatar_enterprise_vip);
                break;
        }
        //设置微博 转发数和评论数
        viewHolder.tv_repost_count.setText(CommonUtil.getNumString(String.valueOf(weiboItem.reposts_count)));
        viewHolder.tv_comment_count.setText(CommonUtil.getNumString(String.valueOf(weiboItem.comments_count)));
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
            //设置监听器
            viewHolder.tv_retweeted_comment_count.setOnClickListener(viewHolder.onClickListener);
            viewHolder.tv_retweeted_repost_count.setOnClickListener(viewHolder.onClickListener);
        } else {
            if (viewHolder.view_retweeted != null)
                viewHolder.ll_item.removeView(viewHolder.view_retweeted);
        }
        //设置监听器
        viewHolder.tv_screen_name.setOnClickListener(viewHolder.onClickListener);
        viewHolder.iv_avatar.setOnClickListener(viewHolder.onClickListener);

        viewHolder.tv_comment_count.setOnClickListener(viewHolder.onClickListener);
        viewHolder.tv_repost_count.setOnClickListener(viewHolder.onClickListener);
        viewHolder.iv_overflow.setOnClickListener(viewHolder.onClickListener);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onItemLongClick(v, position);
                    return true;
                }
                return false;
            }
        });

    }

    // item上各组件的点击事件监听器
    class WeiboWidghtOnClickListener implements View.OnClickListener {

        private boolean isReweeted;
        private WeiboItem weiboItem;
        private MyOnMenuItemClickListener onMenuItemClickListener;

        public WeiboWidghtOnClickListener(int position, MyOnMenuItemClickListener onMenuItemClickListener) {
            weiboItem = list.get(position);
            this.onMenuItemClickListener = onMenuItemClickListener;
            isReweeted = (weiboItem.retweeted_status != null);
        }

        @Override
        public void onClick(View v) {
            if (weiboItem == null) {
                return;
            }
            int requestCode = 0;
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.tv_screen_name:
                    intent.setClass(context, UserDetailsActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.iv_avatar:
                    intent.setClass(context, UserDetailsActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.tv_comment_count:
                    requestCode = MainActivity.SEND_NEW_COMMENT;
                    intent.setClass(context, PublishActivity.class);
                    intent.putExtra("type", PublishActivity.SEND_COMMENT);
                    intent.putExtra("idstr", weiboItem.idstr);
                    intent.putExtra("isReweeted", isReweeted);
                    ((MainActivity) context).startActivityForResult(intent, requestCode);
                    break;
                case R.id.tv_repost_count:
                    requestCode = MainActivity.SEND_NEW_REPOST;
                    intent.setClass(context, PublishActivity.class);
                    intent.putExtra("type", PublishActivity.SEND_REPOST);
                    intent.putExtra("idstr", weiboItem.idstr);
                    if (isReweeted) {
                        intent.putExtra("text", "//@" + weiboItem.user.screen_name + context.getResources().getString(R.string.colon) + weiboItem.text);
                    }
                    ((MainActivity) context).startActivityForResult(intent, requestCode);
                    break;
                case R.id.iv_overflow:
                    CommonUtil.showPopupMenu(context, v, R.menu.overflow_popupmenu, onMenuItemClickListener);
                    break;
                case R.id.tv_retweeted_comment_count:
                    requestCode = MainActivity.SEND_NEW_COMMENT;
                    intent.setClass(context, PublishActivity.class);
                    intent.putExtra("type", PublishActivity.SEND_COMMENT);
                    intent.putExtra("idstr", weiboItem.retweeted_status.idstr);
                    ((MainActivity) context).startActivityForResult(intent, requestCode);
                    break;
                case R.id.tv_retweeted_repost_count:
                    requestCode = MainActivity.SEND_NEW_REPOST;
                    intent.setClass(context, PublishActivity.class);
                    intent.putExtra("type", PublishActivity.SEND_REPOST);
                    intent.putExtra("idstr", weiboItem.retweeted_status.idstr);
                    ((MainActivity) context).startActivityForResult(intent, requestCode);
                    break;
            }
        }
    }

    // item上的overflow弹出的PopupMenu的点击监听器
    class MyOnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private WeiboItem weiboItem;

        public MyOnMenuItemClickListener(int position) {
            weiboItem = list.get(position);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            LogUtils.i(weiboItem + weiboItem.user.screen_name);
            return true;
        }
    }


    // 处理被转发的View
    private void processRetweeted(RecyclerView.ViewHolder holder, WeiboItem weiboItem) {
        WeiboViewHolder viewHolder = (WeiboViewHolder) holder;
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

    /**
     * 设置成 “没有更多微博了”
     */
    public void setNoMoreWeibo() {
        completeLoadMore(false);
        loadSuccess = true;
        if (mFooterViewHolder != null)
            mFooterViewHolder.tv_load_fail.setText(context.getResources().getString(R.string.no_more_weibo));
    }

    public void initFooterViewHolder() {
        if (mFooterViewHolder != null) {
            mFooterViewHolder.tv_load_fail.setVisibility(View.INVISIBLE);
            mFooterViewHolder.ll_load_more.setVisibility(View.VISIBLE);
        }
    }

    // weibo item ViewHolder
    public class WeiboViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_item;
        public TextView tv_screen_name;
        public ImageView iv_avatar;
        public TextView tv_time;
        public TextView tv_device;
        public ImageView iv_verified;
        public TextView tv_repost_count;
        public TextView tv_comment_count;
        public ImageView iv_overflow;
        public TextView tv_text;
        public List<ImageView> iv_arrays = new ArrayList<>();
        public List<ImageView> iv_retweeted_arrays = new ArrayList<>();
        public TextView tv_retweeted_name_text;
        public View view_retweeted;
        public TextView tv_retweeted_repost_count;
        public TextView tv_retweeted_comment_count;
        public RelativeLayout rl_pics;
        public RelativeLayout rl_retweeted_pics;
        public WeiboWidghtOnClickListener onClickListener;
        public MyOnMenuItemClickListener onMenuItemClickListener;

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

        public WeiboViewHolder(View itemView) {
            super(itemView);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_device = (TextView) itemView.findViewById(R.id.tv_device);
            iv_verified = (ImageView) itemView.findViewById(R.id.iv_verified);
            tv_repost_count = (TextView) itemView.findViewById(R.id.tv_repost_count);
            tv_comment_count = (TextView) itemView.findViewById(R.id.tv_comment_count);
            iv_overflow = (ImageView) itemView.findViewById(R.id.iv_overflow);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            rl_pics = (RelativeLayout) itemView.findViewById(R.id.rl_pics);
            for (int i = 0; i < IMAGEVIEW_IDS.length; i++) {
                ImageView iv = (ImageView) itemView.findViewById(IMAGEVIEW_IDS[i]);
                iv_arrays.add(iv);
            }
        }

    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.listener = mOnItemClickListener;
    }

}
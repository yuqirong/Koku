package com.yuqirong.koku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.WeiboItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anyway on 2015/8/30.
 */
public class WeiboRecycleViewAdapter extends RecyclerView.Adapter<WeiboRecycleViewAdapter.ViewHolder> {

    public List<WeiboItem> list = new LinkedList<>();
    private Context context;
    private BitmapUtils bitmapUtils;

    public WeiboRecycleViewAdapter(Context context) {
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview_weibo_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // TODO: 2015/8/30
        WeiboItem weiboItem = list.get(position);
        holder.tv_screen_name.setText(weiboItem.name);
        bitmapUtils.display(holder.civ_avatar, weiboItem.profile_image_url);
        holder.tv_time.setText(weiboItem.time);
        holder.tv_device.setText(Html.fromHtml(weiboItem.source));
        if (weiboItem.verified) {
            holder.iv_verified.setImageResource(R.drawable.avatar_vip);
//            Drawable drawable = context.getResources().getDrawable(R.drawable.avatar_vip, null);
//            holder.tv_screen_name.setCompoundDrawables(null, null, drawable, null);
        }
        holder.tv_repost_count.setText(weiboItem.reposts_count);
        holder.tv_comment_count.setText(weiboItem.comments_count);
        holder.tv_text.setText(weiboItem.text);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(int position, WeiboItem weiboItem) {
        list.add(position, weiboItem);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
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

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
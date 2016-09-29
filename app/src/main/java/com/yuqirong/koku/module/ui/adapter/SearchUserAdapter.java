package com.yuqirong.koku.module.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuqirong.koku.R;
import com.yuqirong.koku.module.model.entity.User;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/9/8.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private Context context;
    private List<User> list = new ArrayList<>();
    private ImageLoader imageLoader;
    private static DisplayImageOptions options;

    public void setIsSearchMode(boolean isSearchMode) {
        this.isSearchMode = isSearchMode;
    }

    private boolean isSearchMode = true; //是否为搜索用户模式


    private WeiboRecycleViewAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(WeiboRecycleViewAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<User> getList() {
        return list;
    }

    public SearchUserAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        options = BitmapUtil.getDisplayImageOptions(R.drawable.img_empty_avatar, true, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.ViewHolder holder, final int position) {
        User user = list.get(position);
        holder.tv_screen_name.setText(user.getScreen_name());
        holder.tv_follower_count.setText(CommonUtil.getNumString(user.getFollowers_count()));
        if (!isSearchMode) {
            holder.iv_avatar.setVisibility(View.VISIBLE);
            imageLoader.displayImage(user.getProfile_image_url(),holder.iv_avatar,options);
        } else {
            holder.iv_avatar.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(int position, User user) {
        list.add(position, user);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void clearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_screen_name;
        public TextView tv_follower_count;
        public ImageView iv_avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tv_follower_count = (TextView) itemView.findViewById(R.id.tv_follower_count);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }

    }

}

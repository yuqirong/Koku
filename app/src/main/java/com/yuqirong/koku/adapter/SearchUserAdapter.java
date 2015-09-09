package com.yuqirong.koku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.User;
import com.yuqirong.koku.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/9/8.
 */
public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private Context context;
    public List<User> list = new ArrayList<>();

    public SearchUserAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.ViewHolder holder, int position) {
        User user = list.get(position);
        holder.tv_screen_name.setText(user.screen_name);
        holder.tv_follower_count.setText(CommonUtil.getNumString(user.followers_count));
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

    public void clearData(){
        int size = list.size();
        for(int i=0;i<size;i++){
            removeData(0);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_screen_name;
        public TextView tv_follower_count;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tv_follower_count = (TextView) itemView.findViewById(R.id.tv_follower_count);
        }

    }
}

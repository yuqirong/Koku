package com.yuqirong.koku.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.db.DraftDB;
import com.yuqirong.koku.entity.Draft;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class DraftRecyclerViewAdapter extends RecyclerView.Adapter<DraftRecyclerViewAdapter.ViewHolder> {

    private List<Draft> list = new ArrayList<>();

    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position,int id);

    }

    public synchronized List<Draft> getList() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weibo_draft_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Draft draft = list.get(position);
        switch (draft.type) {
            case PublishActivity.SEND_WEIBO:
                holder.tv_type.setText(R.string.publish_weibo);
                break;
            case PublishActivity.SEND_COMMENT:
                holder.tv_type.setText(R.string.comment_weibo);
                break;
            case PublishActivity.SEND_REPOST:
                holder.tv_type.setText(R.string.repost_weibo);
                break;
            default:
                break;
        }
        holder.tv_content.setText(draft.text);
        holder.listener = new OnClickListener(position,draft.id);
        holder.ib_delete.setOnClickListener(holder.listener);
        holder.ib_send.setOnClickListener(holder.listener);
        holder.itemView.setOnClickListener(holder.listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class OnClickListener implements View.OnClickListener {

        private int position;
        private int id;

        public OnClickListener(int position,int id) {
            this.position = position;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.onItemClick(v,position,id);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_type;
        public TextView tv_content;
        public ImageButton ib_delete;
        public ImageButton ib_send;
        public OnClickListener listener;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            ib_delete = (ImageButton) itemView.findViewById(R.id.ib_delete);
            ib_send = (ImageButton) itemView.findViewById(R.id.ib_send);
        }
    }
}

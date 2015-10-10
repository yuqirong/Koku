package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
 * 草稿箱Fragment
 * Created by Anyway on 2015/10/6.
 */
public class DraftFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private DraftRecyclerViewAdapter adapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter.getList().clear();
                adapter.getList().addAll(DraftDB.getDraftList());
                adapter.notifyDataSetChanged();
            }
        }).start();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draft, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DraftRecyclerViewAdapter();
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    static class DraftRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Draft> list = new ArrayList<>();

        public List<Draft> getList() {
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
            switch (draft.type){
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
            holder.ib_delete.setOnClickListener(listener);
            holder.ib_send.setOnClickListener(listener);
            holder.itemView.setOnClickListener(listener);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static View.OnClickListener listener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    // TODO: 2015/10/10   switch
                }
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_type;
        public TextView tv_content;
        public ImageButton ib_delete;
        public ImageButton ib_send;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            ib_delete = (ImageButton) itemView.findViewById(R.id.ib_delete);
            ib_send = (ImageButton) itemView.findViewById(R.id.ib_send);
        }
    }

}

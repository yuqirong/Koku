package com.yuqirong.koku.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuqirong.koku.R;

import java.util.LinkedList;
import java.util.List;

/**
 * RecyclerView加载更多Adapter基类
 * Created by Anyway on 2015/9/13.
 */
public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOOTER = 1001;
    protected FooterViewHolder mFooterViewHolder;
    public List<T> list = new LinkedList<>();
    protected boolean loadSuccess = true;

    // 是否为加载更多
    private boolean isLoadingMore;

    public void setIsLoadingMore(boolean isLoadingMore) {
        this.isLoadingMore = isLoadingMore;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_footer, parent, false);
            mFooterViewHolder = new FooterViewHolder(view);
            return mFooterViewHolder;
        } else {
            return createCustomViewHolder(parent, viewType);
        }
    }

    public abstract RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType);

    public abstract void bindCustomViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_FOOTER) {
            final FooterViewHolder viewHolder = (FooterViewHolder) holder;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!loadSuccess) {
                        loadSuccess = !loadSuccess;
                        viewHolder.ll_load_more.setVisibility(View.VISIBLE);
                        viewHolder.tv_load_fail.setVisibility(View.INVISIBLE);
                        if (listener != null) {
                            listener.onLoadingMore();
                        }
                    }
                }
            });
        } else {
            bindCustomViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    /**
     * 是否成功加载更多，加载失败显示 “加载失败，点击重试”
     */
    public void completeLoadMore(boolean success) {
        loadSuccess = success;
        if (mFooterViewHolder == null) {
            return;
        }
        if (!success) {
            mFooterViewHolder.ll_load_more.setVisibility(View.INVISIBLE);
            mFooterViewHolder.tv_load_fail.setText(mFooterViewHolder.itemView.getContext().getResources().getString(R.string.load_fail));
            mFooterViewHolder.tv_load_fail.setVisibility(View.VISIBLE);
        } else {
            isLoadingMore = false;
        }
    }

    /**
     * 增加一条数据
     *
     * @param position
     * @param t
     */
    public void addData(int position, T t) {
        list.add(position, t);
        notifyItemInserted(position);
    }

    /**
     * 删除一条数据
     *
     * @param position
     */
    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 清除recyclerview数据
     */
    public void clearData() {
        list.clear();
        notifyDataSetChanged();
    }

    //footer viewHolder
    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_load_more;
        public TextView tv_load_fail;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ll_load_more = (LinearLayout) itemView.findViewById(R.id.ll_load_more);
            tv_load_fail = (TextView) itemView.findViewById(R.id.tv_load_fail);
        }
    }

    // 加载更多监听器
    private OnLoadingMoreListener listener;

    /**
     * 设置加载更多监听器
     *
     * @return
     */
    public void setOnLoadingMoreListener(OnLoadingMoreListener listener) {
        this.listener = listener;
    }

    /**
     * 得到加载更多监听器
     *
     * @return
     */
    public OnLoadingMoreListener getListener() {
        return listener;
    }

    /**
     * 刷新的监听器
     */
    public interface OnLoadingMoreListener {
        void onLoadingMore();
    }
}

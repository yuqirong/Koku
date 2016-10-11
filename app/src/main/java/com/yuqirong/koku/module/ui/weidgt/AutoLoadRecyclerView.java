package com.yuqirong.koku.module.ui.weidgt;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.yuqirong.koku.module.ui.adapter.LoadMoreAdapter;

/**
 * 自动加载更多RecyclerView
 * Created by Anyway on 2015/9/12.
 */
public class AutoLoadRecyclerView extends RecyclerView {

    // 加载更多监听器
    private LoadMoreAdapter.OnLoadingMoreListener listener;

    private LoadMoreAdapter adapter;

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        this.adapter = (LoadMoreAdapter) adapter;
        listener = ((LoadMoreAdapter) adapter).getListener();

    }

    private void initView() {
        this.setHasFixedSize(true);
        this.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        this.setLayoutManager(mLinearLayoutManager);
//        this.addItemDecoration(new DividerItemDecoration(getsContext(), DividerItemDecoration.VERTICAL_LIST));
        this.addOnScrollListener(new OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                if (adapter == null) {
                    return;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastVisibleItem == adapter.getItemCount() - 1 && !adapter.isLoadingMore()) {
                    adapter.setIsLoadingMore(true);
                    if (listener != null) {
                        handler.sendEmptyMessageDelayed(0, 500);
                    }
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            listener.onLoadingMore();
        }
    };

}

package com.yuqirong.koku.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuqirong.koku.R;

/**
 * 自动加载的ListView
 * Created by Anyway on 2015/8/30.
 */
public class AutoLoadListView extends ListView implements OnScrollListener {

    private RelativeLayout mFooterLayout; // 加载更多的脚布局

    public onLoadingMoreListener listener;
    private boolean isLoadingMore; // 是否为加载更多
    private int footerMeasuredHeight; // 加载更多布局的高度
    private TextView tv_load_fail;
    private LinearLayout ll_load_more;
    private boolean loadSuccess = true;

    public AutoLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AutoLoadListView(Context context) {
        this(context, null);
    }

    private void initView() {
        this.setOnScrollListener(this);
        initFooterLayout();
    }


    // 初始化加载更多布局
    private void initFooterLayout() {
        mFooterLayout = (RelativeLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.layout_footer, null);
        ll_load_more = (LinearLayout) mFooterLayout
                .findViewById(R.id.ll_load_more);
        tv_load_fail = (TextView) mFooterLayout.findViewById(R.id.tv_load_fail);
        mFooterLayout.measure(0, 0);// 主动通知系统去测量该view;
        footerMeasuredHeight = mFooterLayout.getMeasuredHeight();
        mFooterLayout.setPadding(0, -footerMeasuredHeight, 0, 0);
        mFooterLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadSuccess) {
                    loadSuccess = !loadSuccess;
                    ll_load_more.setVisibility(View.VISIBLE);
                    tv_load_fail.setVisibility(View.INVISIBLE);
                    if (listener != null) {
                        listener.onLoadingMore();
                    }
                }
            }
        });
        addFooterView(mFooterLayout);
    }

    // 是否成功加载更多，加载失败显示 “加载失败，点击重试”
    public void completeLoadMore(boolean success) {
        loadSuccess = success;
        if (success) {
            ll_load_more.setVisibility(View.VISIBLE);
            tv_load_fail.setVisibility(View.INVISIBLE);
            mFooterLayout.setPadding(0, -footerMeasuredHeight, 0, 0);
            isLoadingMore = false;
        } else {
            ll_load_more.setVisibility(View.INVISIBLE);
            tv_load_fail.setText(getContext().getResources().getString(R.string.load_fail));
            tv_load_fail.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置成 “没有更多微博了”
     */
    public void setNoMoreWeibo(){
        completeLoadMore(false);
        tv_load_fail.setText(getContext().getResources().getString(R.string.no_more_weibo));
        loadSuccess = true;
    }


    public void setOnLoadingMoreListener(onLoadingMoreListener listener) {
        this.listener = listener;
    }

    /**
     * 刷新的监听器
     */
    public interface onLoadingMoreListener {
        public void onLoadingMore();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && getLastVisiblePosition() == (getCount() - 1)
                && !isLoadingMore) {
            isLoadingMore = true;
            mFooterLayout.setPadding(0, 0, 0, 0);// 显示出footerView
            setSelection(getCount());// 让listview最后一条显示出来
            if (listener != null) {
               handler.sendEmptyMessageDelayed(0,500);
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            listener.onLoadingMore();
        }
    };

}

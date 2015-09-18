package com.yuqirong.koku.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.WeiboDetailsActivity;
import com.yuqirong.koku.adapter.WeiboRecycleViewAdapter;
import com.yuqirong.koku.cache.ACache;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.AutoLoadRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 微博主页
 * Created by Anyway on 2015/8/30.
 */
public class WeiboTimeLineFragment extends BaseFragment {

    // 下拉刷新组件
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeiboRecycleViewAdapter adapter;
    // 判断是否为第一次进入主页,若是则自动刷新
    private boolean first = false;   //true
    // 判断是否上拉加载，默认为false
    private boolean load = false;

    private AutoLoadRecyclerView mRecyclerView;
    private String baseUrl = "";
    public String CACHE_FOLDER_NAME = "timeline";
    public String TIME_LINE_CACHE_NAME = "timeline_cache";
    protected ACache aCache;
    //若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
    private String max_id = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aCache = ACache.get(context, CACHE_FOLDER_NAME);
        Bundle args = getArguments();
        if (args != null) {
            baseUrl = args.getString("url");
            TIME_LINE_CACHE_NAME += baseUrl;
        }
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getCache();
        if (first) {
            refreshWeibo();
        }
    }

    /**
     * 刷新微博
     */
    public void refreshWeibo() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                handler.sendEmptyMessageDelayed(0, 1000);
                first = false;
            }
        });
    }

    /**
     * 获取缓存
     */
    private void getCache() {
        String cache = aCache.getAsString(TIME_LINE_CACHE_NAME);
        if (TextUtils.isEmpty(cache)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(cache);
            max_id = jsonObject.getString("max_id");
            String statuses = jsonObject.getString("statuses");
            processData(statuses);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器上获取数据
     */
    private void getDataFromServer() {
        String access_token = SharePrefUtil.getString(context, "access_token", "");
        if (!TextUtils.isEmpty(access_token)) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                max_id = "0";
                adapter.initFooterViewHolder();
            }
            String url = this.baseUrl + "?access_token=" + access_token + "&max_id=" + max_id;
            LogUtils.i("url  : " + url);
            getData(url, listener, errorListener);
        }
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String stringResult) {
            String statuses = null;
            try {
                JSONObject jsonObject = new JSONObject(stringResult);
                max_id = jsonObject.getString("max_id");
                statuses = jsonObject.getString("statuses");
                if (mSwipeRefreshLayout.isRefreshing()) {
                    aCache.put(TIME_LINE_CACHE_NAME, stringResult);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            processData(statuses);
        }
    };

    private void processData(String statuses) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            adapter.clearData();
            adapter.list.add(new WeiboItem());
        }
        adapter.list.addAll(adapter.list.size() - 1, JsonUtils.getListFromJson(statuses, WeiboItem.class));
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        if (load) {
            adapter.completeLoadMore(true);
            if ("0".equals(max_id)) {
                adapter.setNoMoreWeibo();
            }
            load = false;
        }
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            LogUtils.i(volleyError.toString());
            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
            if (load) {
                handler.sendEmptyMessageDelayed(1, 1000);
                load = false;
            }

        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        // 设置小箭头的颜色
        mSwipeRefreshLayout.setColorSchemeResources(AppConstant.SWIPE_REFRESH_LAYOUT_COLOR);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        mRecyclerView = (AutoLoadRecyclerView) view.findViewById(R.id.mRecyclerView);
        if (mRecyclerView != null) {
            setupRecyclerView(mRecyclerView);
        }
        return view;
    }

    private void setupRecyclerView(AutoLoadRecyclerView mRecyclerView) {
        adapter = new WeiboRecycleViewAdapter(context);
        adapter.setOnLoadingMoreListener(loadingMoreListener);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new WeiboRecycleViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                WeiboItem item = adapter.list.get(position);
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth() / 2;
                startingLocation[1] += view.getHeight() / 2;
                Intent intent = new Intent(context, WeiboDetailsActivity.class);
                intent.putExtra(WeiboDetailsActivity.ARG_REVEAL_START_LOCATION, startingLocation);
                intent.putExtra("WeiboItem",item);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                LogUtils.i("click the item " + position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                LogUtils.i("long click the item " + position);
            }
        });
    }

    WeiboRecycleViewAdapter.OnLoadingMoreListener loadingMoreListener = new WeiboRecycleViewAdapter.OnLoadingMoreListener() {
        @Override
        public void onLoadingMore() {
            load = true;
            if (!"0".equals(max_id))
                getDataFromServer();
        }
    };

    /**
     * 下拉刷新Listener
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getDataFromServer();
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    getDataFromServer();
                    break;
                case 1:
                    adapter.completeLoadMore(false);
                    break;
            }

        }
    };

}

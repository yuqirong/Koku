package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.WeiboListViewAdapter;
import com.yuqirong.koku.cache.ACache;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.AutoLoadListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * 微博主页
 * Created by Anyway on 2015/8/30.
 */
public class WeiboTimeLineFragment extends BaseFragment {

    private SwipeRefreshLayout srl_main;
    private WeiboListViewAdapter adapter;
    private WeiboItem item;
    private List<WeiboItem> list = new LinkedList<>();
    // 判断是否为第一次进入主页,若是则自动刷新
    private boolean first = true;
    // 判断是否上拉加载，默认为false
    private boolean load = false;
    // 返回结果的页码
    private int page = 1;
    // 自动加载的ListView
    private AutoLoadListView lv_main;
    public static final String CACHE_FOLDER_NAME = "timeline";
    public static final String TIME_LINE_CACHE_NAME = "timeline_cache";
    protected ACache aCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aCache = ACache.get(context, CACHE_FOLDER_NAME);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getCache();
        if (first) {
            srl_main.setProgressViewOffset(false, 0, CommonUtil.dip2px(context, 24));
            srl_main.setRefreshing(true);
            handler.sendEmptyMessageDelayed(0, 2000);
            first = false;
        }
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
            if (srl_main.isRefreshing()) {
                page = 1;
            }
            String url = AppConstant.FRIENDS_TIMELINE_URL + access_token + "&page=" + page;
            getData(url, listener, errorListener);
        }
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String stringResult) {
            String statuses = null;
            try {
                JSONObject jsonObject = new JSONObject(stringResult);
                statuses = jsonObject.getString("statuses");
                if (srl_main.isRefreshing()) {
                    aCache.put(TIME_LINE_CACHE_NAME, stringResult);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            processData(statuses);
        }
    };

    private void processData(String statuses) {
        if (srl_main.isRefreshing()) {
            list.clear();
        }
        list.addAll(JsonUtils.getListFromJson(statuses, WeiboItem.class));
        adapter.notifyDataSetChanged();
        srl_main.setRefreshing(false);
        if (load) {
            lv_main.completeLoadMore(true);
            load = false;
        }
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, null);
        srl_main = (SwipeRefreshLayout) view.findViewById(R.id.srl_main);
        // 设置小箭头的颜色
        srl_main.setColorSchemeResources(AppConstant.SWIPE_REFRESH_LAYOUT_COLOR);
        srl_main.setOnRefreshListener(onRefreshListener);

        lv_main = (AutoLoadListView) view.findViewById(R.id.lv_main);
        lv_main.setOnLoadingMoreListener(loadingMoreListener);
        adapter = new WeiboListViewAdapter(context, list);
        lv_main.setAdapter(adapter);
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.i("click item");
            }
        });
        return view;
    }

    AutoLoadListView.onLoadingMoreListener loadingMoreListener = new AutoLoadListView.onLoadingMoreListener() {
        @Override
        public void onLoadingMore() {
            load = true;
            page++;
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
            getDataFromServer();
        }
    };

}

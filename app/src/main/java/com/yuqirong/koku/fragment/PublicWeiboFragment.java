package com.yuqirong.koku.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.WeiboDetailsActivity;
import com.yuqirong.koku.adapter.WeiboRecycleViewAdapter;
import com.yuqirong.koku.cache.ACache;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.Status;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.AutoLoadRecyclerView;
import com.yuqirong.koku.view.FixedSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共微博Fragment
 * Created by Anyway on 2015/9/19.
 */
public class PublicWeiboFragment extends BaseFragment {

    // 下拉刷新组件
    private FixedSwipeRefreshLayout mSwipeRefreshLayout;
    private WeiboRecycleViewAdapter adapter;
    // 判断是否为第一次进入主页,若是则自动刷新
    private boolean first = false;   //true
    // 判断是否上拉加载，默认为false
    private boolean load = false;

    private AutoLoadRecyclerView mRecyclerView;
    public String CACHE_FOLDER_NAME = "timeline";
    public String TIME_LINE_CACHE_NAME = "public_weibo_cache";
    protected ACache aCache;
    //返回结果的页码，默认为1。
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aCache = ACache.get(context, CACHE_FOLDER_NAME);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCache();
            }
        }, 1000);
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
            getDataFromServer();
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(cache);
            //TODO
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
                page = 1;
                adapter.initFooterViewHolder();
            }
            String url = AppConstant.STATUSES_PUBLIC_TIMELINE_URL + "?access_token=" + access_token + "&count=20&page=" + page;
            LogUtils.i("公共微博 url ：" + url);
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
            adapter.getList().add(new Status());
        }
        adapter.getList().addAll(adapter.getList().size() - 1, JsonUtils.getListFromJson(statuses, Status.class));
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        if (load) {
            adapter.completeLoadMore(true);
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
        mSwipeRefreshLayout = (FixedSwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        // 设置小箭头的颜色
        mSwipeRefreshLayout.setColorSchemeResources(FixedSwipeRefreshLayout.SWIPE_REFRESH_LAYOUT_COLOR);
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
        adapter.setOnItemClickListener(new WeiboRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtils.i("click the item " + position);
                Status item = adapter.getList().get(position);
                Intent intent = new Intent(context, WeiboDetailsActivity.class);
                intent.putExtra("Status", item);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                int menuResId;
                LogUtils.i("long click the item " + position);
                final Status status = adapter.getList().get(position);
                if (status.favorited) {
                    menuResId = R.menu.overflow_popupmenu_02;
                } else {
                    menuResId = R.menu.overflow_popupmenu;
                }
                CommonUtil.showPopupMenu(context, view.findViewById(R.id.iv_overflow), menuResId, new PopupMenu.OnMenuItemClickListener() {
                    private ProgressDialog mProgressDialog;

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        LogUtils.i("click: " + status + status.user.screen_name);
                        switch (item.getItemId()) {
                            case R.id.overflow_share:
                                //TODO: 2015/10/4 分享
                                break;
                            case R.id.overflow_favorite:
                                processFavorite();
                                break;
                            case R.id.overflow_cancel_favorite:
                                processFavorite();
                                break;
                            case R.id.overflow_copy:
                                CommonUtil.copyToClipboard(context, status.text);
                                CommonUtil.showSnackbar(getView(), R.string.copy_weibo_to_clipboard, getResources().getColor(R.color.Indigo_colorPrimary));
                                break;
                        }
                        return true;
                    }

                    private void processFavorite() {
                        String url;
                        if (status.favorited) {
                            url = AppConstant.FAVORITE_DESTROY_URL;
                        } else {
                            url = AppConstant.FAVORITE_CREATE_URL;
                        }
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                }
                                CommonUtil.showSnackbar(getView(), R.string.operation_success, getResources().getColor(R.color.Indigo_colorPrimary));
                            }
                        }, errorListener) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap();
                                map.put("id", status.idstr);
                                map.put("access_token", SharePrefUtil.getString(context, "access_token", ""));
                                return map;
                            }
                        };
                        mQueue.add(stringRequest);
                        LogUtils.i("收藏微博url：" + AppConstant.FAVORITE_CREATE_URL + " , id=" + status.idstr);
                        mProgressDialog = CommonUtil.showProgressDialog(context, R.string.please_wait, true);
                    }
                });
            }
        });
    }

    WeiboRecycleViewAdapter.OnLoadingMoreListener loadingMoreListener = new WeiboRecycleViewAdapter.OnLoadingMoreListener() {
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

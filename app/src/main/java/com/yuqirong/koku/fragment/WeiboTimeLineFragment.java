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
import com.yuqirong.koku.application.MyApplication;
import com.yuqirong.koku.cache.ACache;
import com.yuqirong.koku.app.AppConstant;
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
 * 微博主页
 * Created by Anyway on 2015/8/30.
 */
public class WeiboTimeLineFragment extends BaseFragment {

    // 下拉刷新组件
    private FixedSwipeRefreshLayout mSwipeRefreshLayout;
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

    private static Handler mHandler = new Handler();

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
            if(SharePrefUtil.getBoolean(context,"timeline_refresh",true)){
                //若没有缓存，则从网络中读取新数据
                getDataFromServer();
            }
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
            getJsonData(url, listener, errorListener);
        }
    }

    Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(final JSONObject jsonObject) {
            String statuses = null;
            try {
                max_id = jsonObject.getString("max_id");
                statuses = jsonObject.getString("statuses");
                if (mSwipeRefreshLayout.isRefreshing()) {
                    MyApplication.getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            aCache.put(TIME_LINE_CACHE_NAME, jsonObject.toString());
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            processData(statuses);
        }
    };

    private void processData(final String statuses) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            adapter.getList().clear();
        }
        MyApplication.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                adapter.getList().addAll(JsonUtils.getListFromJson(statuses, Status.class));
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (load) {
                            adapter.completeLoadMore(true);
                            if ("0".equals(max_id)) {
                                adapter.setEndText(context.getString(R.string.no_more_weibo));
                            }
                            load = false;
                        }
                    }
                });
            }
        });

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
                Status status = adapter.getList().get(position);
                Intent intent = new Intent(context, WeiboDetailsActivity.class);
                intent.putExtra("Status", status);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                int menuResId;
                LogUtils.i("long click the item " + position);
                final Status status = adapter.getList().get(position);
                if (status.isFavorited()) {
                    menuResId = R.menu.overflow_popupmenu_02;
                } else {
                    menuResId = R.menu.overflow_popupmenu;
                }
                CommonUtil.showPopupMenu(context, view.findViewById(R.id.iv_overflow), menuResId, new PopupMenu.OnMenuItemClickListener() {

                    private ProgressDialog mProgressDialog;

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        LogUtils.i("click: " + status + status.getUser().getScreen_name());

                        switch (item.getItemId()) {
                            case R.id.overflow_share:
                                CommonUtil.shareWeibo(context, status);
                                break;
                            case R.id.overflow_favorite:
                                processFavorite();
                                break;
                            case R.id.overflow_cancel_favorite:
                                processFavorite();
                                break;
                            case R.id.overflow_copy:
                                CommonUtil.copyToClipboard(context, status.getText());
                                CommonUtil.showSnackbar(getView(), R.string.copy_weibo_to_clipboard, getResources().getColor(R.color.Indigo_colorPrimary));
                                break;
                        }
                        return true;
                    }

                    private void processFavorite() {
                        String url;
                        if (status.isFavorited()) {
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
                                map.put("id", status.getIdstr());
                                map.put("access_token", SharePrefUtil.getString(context, "access_token", ""));
                                return map;
                            }
                        };
                        mQueue.add(stringRequest);
                        LogUtils.i("收藏微博url：" + AppConstant.FAVORITE_CREATE_URL + " , id=" + status.getIdstr());
                        mProgressDialog = CommonUtil.showProgressDialog(context, R.string.please_wait, true);
                    }
                });
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
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

package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.MainActivity;
import com.yuqirong.koku.adapter.WeiboListViewAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.AutoLoadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * 微博主页
 * Created by Anyway on 2015/8/30.
 */
public class MainFragment extends BaseFragment {

//    private RecyclerView rv_main;
//    private WeiboRecycleViewAdapter adapter;

    private SwipeRefreshLayout srl_main;
    private WeiboListViewAdapter adapter;
    private WeiboItem item;
    private List<WeiboItem> list = new LinkedList<>();
    String created_at;
    String source;
    String reposts_count;
    String comments_count;
    String profile_image_url;
    String name;
    String time;
    String text;
    boolean verified;

    // 判断是否为第一次进入主页,若是则自动刷新
    private boolean first = true;
    // 判断是否上拉加载，默认为false
    private boolean load = false;
    // 返回结果的页码
    private int page = 1;
    // 自动加载的ListView
    private AutoLoadListView lv_main;

    private JsonObjectRequest jsonObjectRequest;

    @Override
    public void initData(Bundle savedInstanceState) {
        if (first){
            srl_main.setProgressViewOffset(false, 0, CommonUtil.dip2px(context, 24));
            srl_main.setRefreshing(true);
            handler.sendEmptyMessageDelayed(0, 2000);
            first = false;
        }
    }

    /**
     * 从服务器上获取数据
     */
    private void getDataFromServer(){
        String access_token = SharePrefUtil.getString(context, "access_token", "");
        if (access_token != "") {
            if(srl_main.isRefreshing()){
                page = 1;
            }
            String url = AppConstant.FRIENDS_TIMELINE_URL + access_token + "&page="+page;
            jsonObjectRequest = new JsonObjectRequest(url, null, listener, errorListener);
            ((MainActivity) context).mQueue.add(jsonObjectRequest);
        }
    }

    Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String statuses = jsonObject.getString("statuses");
                processData(statuses);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private void processData(String statuses) {
        try {
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray(statuses);
            if(srl_main.isRefreshing()){
                list.clear();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                created_at = jsonObject.getString("created_at");
                source = jsonObject.getString("source");
                reposts_count = jsonObject.getString("reposts_count");
                comments_count = jsonObject.getString("comments_count");
                text = jsonObject.getString("text");
                time = jsonObject.getString("created_at");
                String user = jsonObject.getString("user");
                jsonObject = new JSONObject(user);
                profile_image_url = jsonObject.getString("avatar_large");
                name = jsonObject.getString("name");
                verified = jsonObject.getBoolean("verified");
                item = new WeiboItem(created_at, source, CommonUtil.getNumString(reposts_count), CommonUtil.getNumString(comments_count), profile_image_url, name, DateUtils.getWeiboDate(time), verified, text);
                list.add(item);
            }
            adapter.notifyDataSetChanged();
            srl_main.setRefreshing(false);
            if(load){
                lv_main.completeLoadMore(true);
                load = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        srl_main = (SwipeRefreshLayout) view.findViewById(R.id.srl_main);
        // 设置小箭头的颜色
        srl_main.setColorSchemeResources(AppConstant.SWIPE_REFRESH_LAYOUT_COLOR);
        srl_main.setOnRefreshListener(onRefreshListener);

        lv_main = (AutoLoadListView) view.findViewById(R.id.lv_main);
        lv_main.setOnLoadingMoreListener(loadingMoreListener);
        adapter = new WeiboListViewAdapter(context,list);
        lv_main.setAdapter(adapter);
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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            getDataFromServer();
        }
    };



}

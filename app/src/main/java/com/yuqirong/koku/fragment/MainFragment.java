package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.MainActivity;
import com.yuqirong.koku.adapter.WeiboListViewAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.WeiboItem;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.SharePrefUtil;

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

    private ListView lv_main;

    @Override
    public void initData(Bundle savedInstanceState) {

//        adapter.list.clear();
        String access_token = SharePrefUtil.getString(context, "access_token", "");
        if (access_token != "") {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(AppConstant.FRIENDS_TIMELINE_URL + access_token, null, listener, errorListener);
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
                item = new WeiboItem(created_at, source, reposts_count, comments_count, profile_image_url, name, DateUtils.getWeiboDate(time), verified, text);
                list.add(item);
            }
            adapter.notifyDataSetChanged();
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

        lv_main = (ListView) view.findViewById(R.id.lv_main);
        adapter = new WeiboListViewAdapter(context,list);
        lv_main.setAdapter(adapter);
//        rv_main = (RecyclerView) view.findViewById(R.id.rv_main);
//        rv_main.setHasFixedSize(true);
//        rv_main.setLayoutManager(new LinearLayoutManager(context));
//        //设置Item增加、移除动画
//        rv_main.setItemAnimator(new DefaultItemAnimator());
//        adapter = new WeiboRecycleViewAdapter(context);
//        rv_main.setAdapter(adapter);
//        adapter.setOnItemClickLitener(new WeiboRecycleViewAdapter.OnItemClickLitener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(context,"click",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//                Toast.makeText(context,"long_click",Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            handler.sendEmptyMessageDelayed(0,5000);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(context,"lalala",Toast.LENGTH_SHORT).show();
            srl_main.setRefreshing(false);
        }
    };

}

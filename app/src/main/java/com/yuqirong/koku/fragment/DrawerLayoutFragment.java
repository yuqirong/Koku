package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.MainActivity;
import com.yuqirong.koku.adapter.DrawerLayoutAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 侧拉抽屉
 * Created by Anyway on 2015/8/30.
 */
public class DrawerLayoutFragment extends BaseFragment {

    /**
     * 头像url
     */
    private String avatar_large;
    /**
     * 封面图url
     */
    private String cover_image_phone;
    /**
     * 用户昵称
     */
    private String screen_name;
    /**
     * 用户头像
     */
    private CircleImageView civ_avatar;
    /**
     * 用户昵称
     */
    private TextView tv_screen_name;
    /**
     * 用户封面图
     */
    private ImageView iv_cover;
    /**
     * 列表：搜索用户，周边动态等
     */
    private ListView lv_left;
    private DrawerLayoutAdapter adapter;
    private List<String> list;

    @Override
    public void initData(Bundle savedInstanceState) {
        list = CommonUtil.getStringFromArrays(R.array.user_operation);
        adapter = new DrawerLayoutAdapter(context, list);
        lv_left.setAdapter(adapter);
        getCache();

        String access_token = SharePrefUtil.getString(context, "access_token", "");
        String uid = SharePrefUtil.getString(context, "uid", "");
        if (access_token != "" && uid != "") {
            String url = AppConstant.USERS_SHOW_URL + "?access_token=" + access_token + "&uid=" + uid;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
            ((MainActivity) context).mQueue.add(jsonObjectRequest);
        }
    }

    Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                screen_name = jsonObject.getString("screen_name");
                cover_image_phone = jsonObject.getString("cover_image_phone");
                avatar_large = jsonObject.getString("avatar_large");
                processData();
                SharePrefUtil.saveString(context, "screen_name", screen_name);
                SharePrefUtil.saveString(context, "cover_image_phone", cover_image_phone);
                SharePrefUtil.saveString(context, "avatar_large", avatar_large);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            LogUtils.e("Network Error");
        }
    };

    //得到缓存数据
    private void getCache() {
        screen_name = SharePrefUtil.getString(context, "screen_name", "");
        cover_image_phone = SharePrefUtil.getString(context, "cover_image_phone", "");
        avatar_large = SharePrefUtil.getString(context, "avatar_large", "");
        processData();
    }

    //处理数据
    private void processData() {
        tv_screen_name.setText(screen_name);
        bitmapUtils.display(iv_cover, cover_image_phone);
        bitmapUtils.display(civ_avatar, avatar_large);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_drawer_layout, null);
        civ_avatar = (CircleImageView) view.findViewById(R.id.civ_avatar);
        iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
        tv_screen_name = (TextView) view.findViewById(R.id.tv_screen_name);
        lv_left = (ListView) view.findViewById(R.id.lv_left);
        return view;
    }
}

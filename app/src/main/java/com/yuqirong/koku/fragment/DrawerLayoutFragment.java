package com.yuqirong.koku.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.DrawerLayoutAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.User;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.CircleImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * 侧拉抽屉
 * Created by Anyway on 2015/8/30.
 */
public class DrawerLayoutFragment extends BaseFragment {

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
    private List<String> list = new LinkedList<>();
    private User user;

    @Override
    public void initData(Bundle savedInstanceState) {
        list.addAll(CommonUtil.getStringFromArrays(R.array.user_operation));
        adapter.notifyDataSetChanged();
        // 加载缓存
        getCache();
        // 更新数据
        String access_token = SharePrefUtil.getString(context, "access_token", "");
        String uid = SharePrefUtil.getString(context, "uid", "");
        if (!TextUtils.isEmpty(access_token) && !TextUtils.isEmpty(uid)) {
            String url = AppConstant.USERS_SHOW_URL + "?access_token=" + access_token + "&uid=" + uid;
            getData(url, listener, errorListener);
        }
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String stringResult) {
            user = JsonUtils.getBeanFromJson(stringResult, User.class);
            processData(user);
            SharePrefUtil.saveString(context, "screen_name", user.screen_name);
            SharePrefUtil.saveString(context, "cover_image_phone", user.cover_image_phone);
            SharePrefUtil.saveString(context, "avatar_large", user.avatar_large);
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
        user = new User();
        user.screen_name = SharePrefUtil.getString(context, "screen_name", "");
        user.cover_image_phone = SharePrefUtil.getString(context, "cover_image_phone", "");
        user.avatar_large = SharePrefUtil.getString(context, "avatar_large", "");
        processData(user);
    }

    //处理数据
    private void processData(User user) {
        tv_screen_name.setText(user.screen_name);
        bitmapUtils.display(iv_cover, user.cover_image_phone);
        bitmapUtils.display(civ_avatar, user.avatar_large);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_drawer, null);
        civ_avatar = (CircleImageView) view.findViewById(R.id.civ_avatar);
        iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
        tv_screen_name = (TextView) view.findViewById(R.id.tv_screen_name);
        lv_left = (ListView) view.findViewById(R.id.lv_left);
        iv_cover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        adapter = new DrawerLayoutAdapter(context, list);
        lv_left.setAdapter(adapter);
        return view;
    }
}

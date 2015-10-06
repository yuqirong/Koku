package com.yuqirong.koku.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.BitmapUtils;
import com.yuqirong.koku.util.BitmapUtil;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Anyway on 2015/8/28.
 */
public abstract class BaseFragment extends Fragment {

    public Context context;
    protected BitmapUtils bitmapUtils;
    protected RequestQueue mQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        bitmapUtils = BitmapUtil.getBitmapUtils(context);
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    public abstract void initData(Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, savedInstanceState);
        return view;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 从服务器上获取数据
     *
     * @param url
     * @param listener
     * @param errorListener
     */
    protected void getData(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        mQueue.add(stringRequest);
    }

    /**
     * ShareSDK 分享
     * @param title
     * @param text
     */
    protected void showShare(String title, String text) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
//            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//            oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
//            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//            oks.setImagePath(status.pic_urls.get(0).thumbnail_pic);//确保SDcard下面存在此张图片
//            // url仅在微信（包括好友和朋友圈）中使用
//            oks.setUrl("http://sharesdk.cn");
//            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//            oks.setComment("我是测试评论文本");
//            // site是分享此内容的网站名称，仅在QQ空间使用
//            oks.setSite(context.getResources().getString(R.string.app_name));
//            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//            oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(context);
    }

}

package com.yuqirong.koku.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;

import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.ImagePagerAdapter;
import com.yuqirong.koku.view.swipeback.SwipeBackLayout;
import com.yuqirong.koku.view.swipeback.app.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/10/28.
 */
public class ImageBrowserActivity extends SwipeBackActivity {

    private ViewPager mViewPager;
    private List<String> imgUrls = new ArrayList<>();
    private int position;
    private ImagePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (imgUrls != null)
            outState.putStringArrayList("imgUrls",(ArrayList)imgUrls);
        outState.putInt("positon",position);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            imgUrls = getIntent().getStringArrayListExtra("imgUrls");
            position = getIntent().getIntExtra("position",0);
        } else {
            imgUrls = savedInstanceState.getStringArrayList("imgUrls");
            position = savedInstanceState.getInt("position");
        }
        adapter = new ImagePagerAdapter(this,imgUrls);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_image_browser);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
    }

    public static void actionStart(Context context, ArrayList imgUrls,int position) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putStringArrayListExtra("imgUrls", imgUrls);
        intent.putExtra("position",position);
        context.startActivity(intent);
    }
}

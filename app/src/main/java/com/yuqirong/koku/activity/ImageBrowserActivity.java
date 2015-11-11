package com.yuqirong.koku.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.ImagePagerAdapter;
import com.yuqirong.koku.view.LazyViewPager;
import com.yuqirong.koku.view.swipeback.SwipeBackLayout;
import com.yuqirong.koku.view.swipeback.app.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/10/28.
 */
public class ImageBrowserActivity extends SwipeBackActivity {

    private LazyViewPager mViewPager;
    private List<String> imgUrls = new ArrayList<>();
    private int position;
    private ImagePagerAdapter adapter;
    private TextView tv_pic_num;
    private String picNum;
    private ImageView iv_back;
    private TextView tv_save;

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
            outState.putStringArrayList("imgUrls", (ArrayList) imgUrls);
        outState.putInt("positon", position);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            imgUrls = getIntent().getStringArrayListExtra("imgUrls");
            position = getIntent().getIntExtra("position", 0);
        } else {
            imgUrls = savedInstanceState.getStringArrayList("imgUrls");
            position = savedInstanceState.getInt("position");
        }
        adapter = new ImagePagerAdapter(this, imgUrls);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(position);
        picNum = getString(R.string.pic_num);
        mViewPager.setOnPageChangeListener(onPageChangeListener);
        tv_pic_num.setText(String.format(picNum, position + 1, imgUrls.size()));
    }

    LazyViewPager.OnPageChangeListener onPageChangeListener = new LazyViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tv_pic_num.setText(String.format(picNum, position + 1, imgUrls.size()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void initToolBar() {
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_image_browser);
        mViewPager = (LazyViewPager) findViewById(R.id.mViewPager);
        tv_pic_num = (TextView) findViewById(R.id.tv_pic_num);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        iv_back.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.tv_save:
                    // TODO: 2015/11/11
                    View view = mViewPager.getChildAt(mViewPager.getCurrentItem());
                    PhotoView photoView = (PhotoView) view.findViewById(R.id.mPhotoView);
//                    photoView.getd
                    break;
            }
        }
    };

    public static void actionStart(Context context, ArrayList imgUrls, int position) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putStringArrayListExtra("imgUrls", imgUrls);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

}

package com.yuqirong.koku.module.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuqirong.koku.R;
import com.yuqirong.koku.module.ui.adapter.ImagePagerAdapter;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.module.ui.weidgt.swipeback.SwipeBackLayout;
import com.yuqirong.koku.module.ui.weidgt.swipeback.app.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.id;

/**
 * Created by Anyway on 2015/10/28.
 */
public class ImageBrowserActivity extends SwipeBackActivity {

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.tv_pic_num)
    TextView tv_pic_num;
    private List<String> imgUrls = new ArrayList<>();
    private int position;
    private ImagePagerAdapter adapter;
    private String picNum;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(onPageChangeListener);
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
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        tv_pic_num.setText(String.format(picNum, position + 1, imgUrls.size()));
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
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
    protected void initView() {
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_image_browser;
    }


    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                String url = imgUrls.get(mViewPager.getCurrentItem());
                if (url != null) {
                    if (SharePrefUtil.getBoolean(ImageBrowserActivity.this, "load_hd_pic", false)) {
                        url = url.replace("thumbnail", "large");
                    } else {
                        url = url.replace("thumbnail", "bmiddle");
                    }
                    adapter.savePicture(url);
                }
                break;
        }
    }


    public static void actionStart(Context context, ArrayList imgUrls, int position) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putStringArrayListExtra("imgUrls", imgUrls);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

}

package com.yuqirong.koku.module.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.yuqirong.koku.R;
import com.yuqirong.koku.module.ui.weidgt.CropImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/14.
 */
public class ClipImageActivity extends BaseActivity {

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String imagePath = getIntent().getStringExtra("imagePath");
                cropImageView.setImagePath(imagePath);
            }
        }, 100);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_clip_picture;
    }

    @OnClick(R.id.tv_back)
    public void onClick(View view) {
        onBackPressed();
    }

}

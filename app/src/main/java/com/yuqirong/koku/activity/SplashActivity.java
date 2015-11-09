package com.yuqirong.koku.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.yuqirong.koku.R;
import com.yuqirong.koku.application.MyApplication;
import com.yuqirong.koku.db.DraftDB;
import com.yuqirong.koku.db.EmotionsDB;
import com.yuqirong.koku.util.SharePrefUtil;

/**
 * 闪屏页面Activity
 * Created by Anyway on 2015/9/22.
 */
public class SplashActivity extends BaseActivity {

    private ImageView iv_splash;

    @Override
    protected void initData(Bundle savedInstanceState) {
        if(!SharePrefUtil.getBoolean(this,"inited",false)){ //是否已完成初始化
            SharePrefUtil.saveBoolean(this,"built-in_browser",true); //是否使用内置浏览器，默认是
            SharePrefUtil.saveBoolean(this, "vibrate_feedback", true); //是否使用振动反馈，默认是
            SharePrefUtil.saveInt(this, "font_size", 0); //字体大小，默认是标准大小
            SharePrefUtil.saveBoolean(this, "inited", true);
        }
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                MyApplication.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        EmotionsDB.checkEmotions();
                        DraftDB.checkDraft();
                    }
                });
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_splash.startAnimation(scaleAnim);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_splash);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
    }

    @Override
    public void onBackPressed() {
        //按返回键无效
//        super.onBackPressed();
    }
}

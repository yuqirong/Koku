package com.yuqirong.koku.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.yuqirong.koku.R;
import com.yuqirong.koku.db.EmotionsDB;

/**
 * 闪屏页面Activity
 * Created by Anyway on 2015/9/22.
 */
public class SplashActivity extends BaseActivity {

    private ImageView iv_splash;

    @Override
    protected void initData(Bundle savedInstanceState) {
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EmotionsDB.checkEmotions();
                    }
                });
                thread.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
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

}

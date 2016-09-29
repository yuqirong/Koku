package com.yuqirong.koku.module.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yuqirong.koku.R;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.module.ui.weidgt.swipeback.SwipeBackLayout;
import com.yuqirong.koku.module.ui.weidgt.swipeback.app.SwipeBackActivity;

/**
 * Created by Anyway on 2015/9/25.
 */
public class NearlyDynamicActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(R.string.nearly_dynamic);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_nearly_dynamic);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case MainActivity.SEND_NEW_COMMENT:
                if (resultCode == PublishActivity.SEND_COMMENT_SUCCESS) {
                    CommonUtil.showNotification(NearlyDynamicActivity.this, R.string.send_remind, R.string.send_success, R.drawable.ic_done_light, true);
                }
                break;
            case MainActivity.SEND_NEW_REPOST:
                if (resultCode == PublishActivity.SEND_REPOST_SUCCESS) {
                    CommonUtil.showNotification(NearlyDynamicActivity.this, R.string.send_remind, R.string.send_success, R.drawable.ic_done_light, true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NearlyDynamicActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

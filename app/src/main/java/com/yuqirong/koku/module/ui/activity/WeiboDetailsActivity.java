package com.yuqirong.koku.module.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.yuqirong.koku.R;
import com.yuqirong.koku.module.model.entity.Status;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.module.ui.weidgt.swipeback.SwipeBackLayout;
import com.yuqirong.koku.module.ui.weidgt.swipeback.app.SwipeBackActivity;

import butterknife.BindView;

/**
 * 单条微博详情Activity
 * Created by Anyway on 2015/9/17.
 */
public class WeiboDetailsActivity extends SwipeBackActivity {

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    private Status status;

    @Override
    protected void initData(Bundle savedInstanceState) {
        status = (Status) getIntent().getSerializableExtra("Status");
    }

    @Override
    protected void initView() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_weibo_details;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weibo_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int requestCode;
        boolean isReweeted = (status.getRetweeted_status() != null);
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_repost:
                requestCode = MainActivity.SEND_NEW_REPOST;
                intent.setClass(WeiboDetailsActivity.this, PublishActivity.class);
                intent.putExtra("type", PublishActivity.SEND_REPOST);
                intent.putExtra("idstr", status.getIdstr());
                if (isReweeted) {
                    intent.putExtra("text", "//@" + status.getUser().getScreen_name() + getResources().getString(R.string.colon) + status.getText());
                }
                startActivityForResult(intent, requestCode);
                break;
            case R.id.action_comment:
                requestCode = MainActivity.SEND_NEW_COMMENT;
                intent.setClass(WeiboDetailsActivity.this, PublishActivity.class);
                intent.putExtra("type", PublishActivity.SEND_COMMENT);
                intent.putExtra("idstr", status.getIdstr());
                intent.putExtra("isReweeted", isReweeted);
                startActivityForResult(intent, requestCode);
                break;
            case R.id.action_share:
                CommonUtil.shareWeibo(this, status);
                break;
            case R.id.action_favorite:

                break;
            case R.id.action_copy:
                CommonUtil.copyToClipboard(this,status.getText());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

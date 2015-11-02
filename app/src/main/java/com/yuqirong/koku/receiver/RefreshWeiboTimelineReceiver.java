package com.yuqirong.koku.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yuqirong.koku.service.CheckUnreadService;
import com.yuqirong.koku.util.LogUtils;

/**
 * Created by Anyway on 2015/9/16.
 */
public class RefreshWeiboTimelineReceiver extends BroadcastReceiver {

    private OnUpdateUIListener listener;

    public void setOnUpdateUIListener(OnUpdateUIListener listener) {
        this.listener = listener;
    }

    public interface OnUpdateUIListener{
        /**
         * 接受到广播后更新ui
         */
        void updateUI(int unreadCount);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i("broadcast receiver received");
        if(listener!=null){
            int unreadCount = intent.getIntExtra("unread_count",0);
            listener.updateUI(unreadCount);
        }
    }

}

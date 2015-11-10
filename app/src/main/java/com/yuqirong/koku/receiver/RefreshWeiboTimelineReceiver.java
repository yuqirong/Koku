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

    public static final String INTENT_FILTER_NAME = "com.yuqirong.koku.receiver.RefreshWeiboTimelineReceiver";
    private OnUpdateUIListener listener;

    public void setOnUpdateUIListener(OnUpdateUIListener listener) {
        this.listener = listener;
    }

    public interface OnUpdateUIListener{
        /**
         * 接受到广播后更新ui
         */
        void updateUI(int unreadCount);
        void updateFab();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.i("broadcast receiver received");
        int flag = intent.getIntExtra("flag",0); //如果是0，则是unreadcount，是1，则是updateFab
        if(listener!=null){
            if(flag == 0) {
                int unreadCount = intent.getIntExtra("unread_count", 0);
                listener.updateUI(unreadCount);
            }else if(flag == 1){
                listener.updateFab();
            }
        }
    }

}

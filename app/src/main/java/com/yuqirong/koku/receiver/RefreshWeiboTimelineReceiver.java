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

    public static final String INTENT_UNREAD_UPDATE = "com.yuqirong.koku.receiver.RefreshWeiboTimelineReceiver.update_unread";
    public static final String INTENT_FAB_CHANGE = "com.yuqirong.koku.receiver.RefreshWeiboTimelineReceiver.fab_change";

    private OnUpdateUIListener listener;

    public void setOnUpdateUIListener(OnUpdateUIListener listener) {
        this.listener = listener;
    }

    public interface OnUpdateUIListener {
        /**
         * 接受到广播后更新ui
         */
        void updateUI(int unreadCount);

        void updateFab();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            LogUtils.i("broadcast receiver received : Screen Off");
            Intent it = new Intent(context, CheckUnreadService.class);
            it.setAction(CheckUnreadService.STOP_TIMER);
            context.startService(it);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            LogUtils.i("broadcast receiver received : Screen Unlock");
            Intent it = new Intent(context, CheckUnreadService.class);
            it.setAction(CheckUnreadService.START_TIMER);
            context.startService(it);
        } else if (INTENT_UNREAD_UPDATE.equals(action)) {
            if (listener != null) {
                int unreadCount = intent.getIntExtra("unread_count", 0);
                listener.updateUI(unreadCount);
            }
        } else if (INTENT_FAB_CHANGE.equals(action)) {
            if (listener != null) {
                listener.updateFab();
            }
        }
    }

}

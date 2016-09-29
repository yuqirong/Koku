package com.yuqirong.koku.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuqirong.koku.application.MyApplication;
import com.yuqirong.koku.app.AppConstant;
import com.yuqirong.koku.receiver.RefreshWeiboTimelineReceiver;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 检查各种消息未读数
 * Created by Administrator on 2015/11/2.
 */
public class CheckUnreadService extends Service {

    private Timer mTimer;
    public static final String START_TIMER = "start_timer";
    public static final String STOP_TIMER = "stop_timer";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
//        Intent i = new Intent(this, RefreshWeiboTimelineReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        if (intent != null) {
            String action = intent.getAction();
            if (STOP_TIMER.equals(action)) {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                    mTimer = null;
                }
            } else if (START_TIMER.equals(action)) {
                if (mTimer == null) {
                    mTimer = new Timer();
                }
                int anHour = 60 * 1000; //60s
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        checkUpdate();
                    }
                }, 0, anHour);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void checkUpdate() {
        String access_token = SharePrefUtil.getString(this, "access_token", "");
        String uid = SharePrefUtil.getString(this, "uid", "");
        if (TextUtils.isEmpty(access_token) || TextUtils.isEmpty(uid)) {
            return;
        }
        String url = AppConstant.REMIND_UNREAD_COUNT_URL
                + "?access_token=" + access_token
                + "&uid=" + uid;
        LogUtils.i("未读消息的url ：" + url);
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int status_count = jsonObject.getInt("status");
                            LogUtils.i("微博未读数：" + status_count);
                            if (status_count > 0) {
                                Intent intent = new Intent();
                                intent.putExtra("unread_count", status_count);
                                intent.setAction(RefreshWeiboTimelineReceiver.INTENT_UNREAD_UPDATE);
                                sendBroadcast(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getRequestQueue().add(request);
    }

}

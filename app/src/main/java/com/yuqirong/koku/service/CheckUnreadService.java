package com.yuqirong.koku.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuqirong.koku.activity.AuthorizeActivity;
import com.yuqirong.koku.application.MyApplication;
import com.yuqirong.koku.constant.AppConstant;
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
//        Intent i = new Intent(this, RefreshWeiboTimelineReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        int anHour = 60 * 1000; //60s
        if (null == mTimer) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkUpdate();
            }
        }, 0, anHour);
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
                                // TODO: 2015/11/2
                                intent.setAction("com.yuqirong.koku.receiver.RefreshWeiboTimelineReceiver");
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

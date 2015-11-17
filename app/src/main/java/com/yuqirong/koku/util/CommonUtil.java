package com.yuqirong.koku.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.Status;

import java.util.List;

/**
 * Created by Anyway on 2015/8/29.
 */
public class CommonUtil {

    /**
     * 如果转发数或评论数超过一万，就简写成 n万
     *
     * @param num
     * @return
     */
    public static String getNumString(int num) {
        if (num < 100000) {
            return String.valueOf(num);
        } else {
            int number = num / 10000;
            return number + "万";
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 创建一个对话框
     */
    public static void createMessageAlertDialog(Context context, String title, String message,
                                                String negativeMessage, DialogInterface.OnClickListener negativeListener,
                                                String positiveMessage, DialogInterface.OnClickListener positiveListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setNegativeButton(negativeMessage, negativeListener).setPositiveButton(positiveMessage, positiveListener).setCancelable(cancelable).create().show();
    }

    public static void createMessageAlertDialog(Context context, int titleId, int messageId,
                                                int negativeMessageId, DialogInterface.OnClickListener negativeListener,
                                                int positiveMessageId, DialogInterface.OnClickListener positiveListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId).setMessage(messageId).setNegativeButton(negativeMessageId, negativeListener).setPositiveButton(positiveMessageId, positiveListener).setCancelable(cancelable).create().show();
    }

    public static void createItemAlertDialog(Context context, CharSequence[] items,
                                             DialogInterface.OnClickListener listener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, listener).setCancelable(cancelable).create().show();
    }

    public static void createSingleChoiceAlertDialog(Context context, int itemsId, int checkedId,
                                                     DialogInterface.OnClickListener singleChoiceItemListener, String negativeMessage,
                                                     DialogInterface.OnClickListener negativeListener, String positiveMessage,
                                                     DialogInterface.OnClickListener positiveListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(itemsId, checkedId, singleChoiceItemListener)
                .setNegativeButton(negativeMessage, negativeListener).setPositiveButton(positiveMessage, positiveListener)
                .setCancelable(cancelable).create().show();
    }


    /**
     * 得到地理信息
     *
     * @param context
     * @return
     */
    public static Location getLocation(Context context) {
        String provider = null;
        Location location = null;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.PASSIVE_PROVIDER)) {
            provider = LocationManager.PASSIVE_PROVIDER;
        } else {
            Toast.makeText(context, R.string.location_not_available, Toast.LENGTH_SHORT).show();
        }
        try {
            location = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            Toast.makeText(context, R.string.no_location_permission, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return location;
    }

    /**
     * 获取相机最后一次拍照的url
     *
     * @param activity
     * @return
     */
    public static String getLatestCameraPicture(Activity activity) {
        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE};
        final Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        if (cursor.moveToFirst()) {
            String path = cursor.getString(1);
            return path;
        }
        return null;
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @param milliseconds 震动的时长，单位是毫秒
     */
    public static void setVibrator(Context context, long milliseconds) {
        Vibrator vibator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE); // 取得震动服务
        vibator.vibrate(milliseconds);
    }

    /**
     * Context context
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public static void setVibrator(Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    /**
     * 发布通知
     *
     * @param context
     * @param titleId
     * @param contentId
     * @param drawableId icon资源文件
     * @param cancelable
     */
    public static void showNotification(Context context, int titleId, int contentId, int drawableId, boolean cancelable) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getString(titleId));
        builder.setContentText(context.getString(contentId));
        builder.setSmallIcon(drawableId);
        builder.setAutoCancel(cancelable);
        Notification mNotification = builder.build();
        notificationManager.notify(0, mNotification);
        if (SharePrefUtil.getBoolean(context, "vibrate_feedback", true)) {
            setVibrator(context, 300);
        }
    }

    /**
     * 显示Snackbar
     *
     * @param v
     * @param text
     * @param backgroundColor
     */
    public static void showSnackbar(View v, int text, int backgroundColor) {
        Snackbar sb = Snackbar.make(v, text, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(backgroundColor);
        sb.show();
    }

    public static void showSnackbar(View v, int text) {
        Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View v, int text, int backgroundColor, int length, int actionText, View.OnClickListener listener) {
        Snackbar sb = Snackbar.make(v, text, length).setAction(actionText, listener);
        sb.getView().setBackgroundColor(backgroundColor);
        sb.show();
    }

    public static void showPopupMenu(Context context, View v, int resId, PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu mPopupMenu = new PopupMenu(context, v);
        mPopupMenu.getMenuInflater().inflate(resId, mPopupMenu.getMenu());
        mPopupMenu.setOnMenuItemClickListener(listener);
        mPopupMenu.show();
    }

    /**
     * 显示ProgressDialog
     *
     * @param context
     * @param stringResId
     * @param cancelable
     */
    public static ProgressDialog showProgressDialog(Context context, int stringResId, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(stringResId));
        progressDialog.setCancelable(cancelable);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * 把文本复制到粘贴板上
     *
     * @param context
     * @param text
     */
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(text); // 复制
    }

    /**
     * @param context
     * @param status
     */
    public static void shareWeibo(Context context, Status status) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String title = context.getString(R.string.share_title);
        title = String.format(title, status.getUser().getScreen_name());
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        //设置分享的内容
        intent.putExtra(Intent.EXTRA_TEXT, status.getText());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

    /**
     * 获取当前应用的版本号
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void openInBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

}

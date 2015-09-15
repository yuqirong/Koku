package com.yuqirong.koku.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.yuqirong.koku.R;

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
    public static String getNumString(String num) {
        if (!TextUtils.isEmpty(num)) {
            int number = Integer.parseInt(num) / 10000;
            if (number == 0) {
                return num;
            } else {
                return number + "万";
            }
        }
        return "";
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
    public static void createMessageAlertDialog(Context context, String title, String message, String negativeMessage, DialogInterface.OnClickListener negativeListener, String positiveMessage, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setNegativeButton(negativeMessage, negativeListener).setPositiveButton(positiveMessage, positiveListener).create().show();
    }

    public static void createItemAlertDialog(Context context, CharSequence[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, listener).setCancelable(true).create().show();
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
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

}

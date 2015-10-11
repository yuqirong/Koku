package com.yuqirong.koku.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yuqirong.koku.R;

import java.io.File;

/**
 * Created by Anyway on 2015/9/14.
 */
public class BitmapUtil {

    private static final int DISK_CACHE_SIZE = 10 * 1024 * 1024;
    private static BitmapUtils bitmapUtils;
    private static DisplayImageOptions options;

    public static BitmapUtils getBitmapUtils(Context context) {
        String bitmap_cache_dir = context.getCacheDir() + File.separator + "bitmap";
        int cache = (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024 * 8));
        bitmapUtils = new BitmapUtils(context, bitmap_cache_dir, cache, DISK_CACHE_SIZE);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.thumbnail_default);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.timeline_image_failure);
        return bitmapUtils;
    }

    public static DisplayImageOptions getDisplayImageOptions(int drawableId, boolean cacheInMemory, boolean cacheOnDisk) {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                .showImageOnFail(R.drawable.timeline_image_failure)
                .cacheInMemory(cacheInMemory)
                .cacheOnDisk(cacheOnDisk)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromUrl(String url, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(url, options);
    }


}

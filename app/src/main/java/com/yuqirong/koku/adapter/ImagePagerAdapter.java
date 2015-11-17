package com.yuqirong.koku.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.yuqirong.koku.R;
import com.yuqirong.koku.application.MyApplication;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Anyway on 2015/10/28.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private static Context context;
    private List<String> list;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private TextView tv_progress;
    private PhotoView mPhotoView;
    private Map<String, Bitmap> mBitMaps = new ConcurrentHashMap<>();
    private static String path;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String pathName = String.format(context.getString(R.string.save_success),path);
                    CommonUtil.showToast(context,pathName);
                    break;
                case 1:
                    CommonUtil.showToast(context,context.getString(R.string.save_fail));
                    break;
                case 2:
                    CommonUtil.showToast(context, context.getString(R.string.no_sd_card));
                    break;
            }
        }
    };


    public ImagePagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.timeline_image_failure)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_image_browser, null);
        mPhotoView = (PhotoView) view.findViewById(R.id.mPhotoView);
        tv_progress = (TextView) view.findViewById(R.id.tv_progress);
        mPhotoView.enable();
        String url = list.get(position);
        if (SharePrefUtil.getBoolean(context, "load_hd_pic", false)) {
            url = url.replace("thumbnail", "large");
            LogUtils.i("原图尺寸图片地址 ：" + url);
        } else {
            url = url.replace("thumbnail", "bmiddle");
            LogUtils.i("中等尺寸图片地址 ：" + url);
        }
        imageLoader.displayImage(url, mPhotoView, options, listener, onProgressListener);
        container.addView(view);
        return view;
    }

    ImageLoadingListener listener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            mBitMaps.put(s, bitmap);
        }

        @Override
        public void onLoadingCancelled(String s, View view) {

        }
    };

    ImageLoadingProgressListener onProgressListener = new ImageLoadingProgressListener() {
        @Override
        public void onProgressUpdate(String imageUri, View view, int current, int total) {
            tv_progress.setVisibility(View.VISIBLE);
            int p = (int) (current * 100f / total);
            tv_progress.setText(p + "%");
            if (p == 100) {
                tv_progress.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void savePicture(final String url) {
        MyApplication.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = mBitMaps.get(url);
                    if (bitmap != null) {
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            path = Environment.getExternalStorageDirectory().getPath()
                                    + File.separator + "koku";
                        } else {
                            mHandler.sendEmptyMessage(2); //没有sd卡
                            return;
                        }
                        LogUtils.i("图片保存路径:" + path);
                        File folder = new File(path);
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        folder = new File(path + File.separator + System.currentTimeMillis() + ".jpg");
                        if (!folder.exists()) {
                            folder.createNewFile();
                        }
                        FileOutputStream outputStream = new FileOutputStream(folder);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        mHandler.sendEmptyMessage(0);  //保存成功
                    }
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(1);  //保存失败
                }

            }
        });
    }

}
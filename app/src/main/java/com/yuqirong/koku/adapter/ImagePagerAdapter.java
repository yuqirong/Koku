package com.yuqirong.koku.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.yuqirong.koku.R;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import java.util.List;

/**
 * Created by Anyway on 2015/10/28.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> list;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private TextView tv_progress;
    private PhotoView mPhotoView;

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
            if(p == 100){
                tv_progress.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
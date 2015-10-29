package com.yuqirong.koku.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuqirong.koku.R;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.LogUtils;

import java.util.List;

/**
 * Created by Anyway on 2015/10/28.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> list;
    private ImageLoader imageLoader;
    private static DisplayImageOptions options;

    public ImagePagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        imageLoader = ImageLoader.getInstance();
        options = BitmapUtil.getDisplayImageOptions(R.drawable.thumbnail_default, true, true);
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
        PhotoView mPhotoView = (PhotoView) view.findViewById(R.id.mPhotoView);
        mPhotoView.enable();
        String url = list.get(position);
        url = url.replace("thumbnail", "bmiddle");
        LogUtils.i("中等尺寸图片地址 ：" + url);
        imageLoader.displayImage(url, mPhotoView, options);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
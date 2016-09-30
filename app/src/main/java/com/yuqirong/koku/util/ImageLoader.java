package com.yuqirong.koku.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {

    private ImageLoader() {
        //no instance
    }

    public static void loadImage(Context context, String imgUrl, ImageView imageView){
        Glide.with(context).load(imgUrl).into(imageView);
    }

}

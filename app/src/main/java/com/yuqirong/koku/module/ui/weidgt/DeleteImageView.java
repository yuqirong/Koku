package com.yuqirong.koku.module.ui.weidgt;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yuqirong.koku.R;

/**
 * Created by Administrator on 2015/11/5.
 */
public class DeleteImageView extends RelativeLayout {

    private ImageView iv_delete;
    private ImageView iv_img;

    public DeleteImageView(Context context) {
        this(context, null);
    }

    public DeleteImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeleteImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_delete_image, this);
        iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        iv_img = (ImageView) view.findViewById(R.id.iv_img);
    }

    public void setImageBitmap(Bitmap mBitmap) {
        if (iv_img != null) {
            iv_img.setImageBitmap(mBitmap);
        }
    }

    public void setOnDeleteClickListener(OnClickListener listener) {
        if (iv_delete != null) {
            iv_delete.setOnClickListener(listener);
        }
    }

}

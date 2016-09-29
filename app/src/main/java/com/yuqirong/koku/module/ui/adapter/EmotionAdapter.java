package com.yuqirong.koku.module.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.yuqirong.koku.app.MyApplication;
import com.yuqirong.koku.db.EmotionsDB;
import com.yuqirong.koku.module.model.entity.Emotion;

import java.util.List;

/**
 * Created by Anyway on 2015/9/23.
 */
public class EmotionAdapter extends MBaseAdapter<Emotion, GridView> {

    public EmotionAdapter(Context context, final List<Emotion> list) {
        super(context, list);
        MyApplication.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                list.clear();
                list.addAll(EmotionsDB.getAllEmotions());
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Emotion emotion = getList().get(position);
        if (convertView == null) {
            convertView = new ImageView(context);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(emotion.getData(), 0, emotion.getData().length);
        if (bitmap != null) {
            ((ImageView) convertView).setImageBitmap(bitmap);
        }
        return convertView;
    }
}

package com.yuqirong.koku.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuqirong.koku.R;

/**
 * Created by Administrator on 2015/11/4.
 */
public class SettingsView extends LinearLayout {

    public SettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SettingsView);
        String title = a.getString(R.styleable.SettingsView_setting_subject);
        initView(context, title);
        a.recycle();
    }

    private void initView(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_settings_item,this);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(title);
    }

    public SettingsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingsView(Context context) {
        this(context, null);
    }




}

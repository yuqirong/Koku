package com.yuqirong.koku.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuqirong.koku.R;

/**
 * Created by Administrator on 2015/11/4.
 */
public class DoubleTextSettingsView extends LinearLayout {

    private CheckBox mCheckBox;

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener){
        mCheckBox.setOnCheckedChangeListener(listener);
    }

    public void setChecked(boolean isChecked){
        mCheckBox.setChecked(isChecked);
    }

    public DoubleTextSettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DoubleTextSettingsView);
        String title = a.getString(R.styleable.DoubleTextSettingsView_double_text_setting_subject);
        String content = a.getString(R.styleable.DoubleTextSettingsView_double_text_setting_content);
        initView(context, title,content);
        a.recycle();
    }

    private void initView(Context context, String title,String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_double_text_settings_item,this);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        mCheckBox = (CheckBox) view.findViewById(R.id.mCheckBox);
        tv_title.setText(title);
        tv_content.setText(content);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckBox.setChecked(!mCheckBox.isChecked());
            }
        });
    }

    public DoubleTextSettingsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleTextSettingsView(Context context) {
        this(context, null);
    }


}

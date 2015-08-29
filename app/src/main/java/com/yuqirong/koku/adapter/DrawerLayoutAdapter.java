package com.yuqirong.koku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yuqirong.koku.R;

import java.util.List;

/**
 * Created by Anyway on 2015/8/29.
 */
public class DrawerLayoutAdapter extends MBaseAdapter<String, ListView> {

    public static final int[] DRAWABLE_IDS = new int[]{R.drawable.ic_search_dark, R.drawable.ic_nearly_dark, R.drawable.ic_hot_dark, R.drawable.ic_favorite_dark, R.drawable.ic_draft_dark};

    public DrawerLayoutAdapter(Context context, List<String> list) {
        super(context, list);
    }

    public DrawerLayoutAdapter(Context context, List<String> list, ListView view) {
        super(context, list, view);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String str = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_user_operation, null);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iv_icon.setImageResource(DRAWABLE_IDS[position]);
        viewHolder.tv_item_name.setText(str);
        return convertView;
    }

    class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_item_name;
    }

}

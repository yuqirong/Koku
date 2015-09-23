package com.yuqirong.koku.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Adapter基类
 * 
 */
public abstract class MBaseAdapter<T, Q> extends BaseAdapter {

	protected Context context;
	private List<T> list;
	private Q view; // 这里不一定是ListView,比如GridView,CustomListView
	

	public MBaseAdapter(Context context, List<T> list, Q view) {
		this.context = context;
		this.list = list;
		this.view = view;
	}

	public MBaseAdapter(Context context, List<T> list) {
		this.context = context;
		this.list = list;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}

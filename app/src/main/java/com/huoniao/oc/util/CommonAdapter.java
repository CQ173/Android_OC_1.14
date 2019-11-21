package com.huoniao.oc.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


public abstract class CommonAdapter<T> extends BaseAdapter{
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	protected int mLayoutIds;
	
	
	public CommonAdapter(Context context, List<T> datas, int LayoutIds) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.mLayoutIds = LayoutIds;

	}
	
	@Override
	public int getCount() {
		
		return mDatas.size();
	}
	
	@Override
	public T getItem(int position) {
		
		return mDatas.get(position);
	}
	
	public void refreshData(List<T> datas){
		this.mDatas = datas;
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mLayoutIds, position);
		convert(holder, getItem(position));
		convert(holder, getItem(position), position);
		return holder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder, T t);

	public void convert(ViewHolder holder, T t, int pos){};
}

package com.huoniao.oc.util;

import java.util.List;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public abstract class CommonAdapter3<T> extends BaseAdapter{
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	protected int mLayoutIds;
	
	public CommonAdapter3(Context context, List<T> datas, int LayoutIds) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.mLayoutIds = LayoutIds;
	}
	@Override
	public int getCount() {
		
		if (mDatas.size() > 3) {
			return 3;
		}else {
			return mDatas.size();
		}
	}

	@Override
	public T getItem(int position) {
		
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mLayoutIds, position);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder, T t);
}

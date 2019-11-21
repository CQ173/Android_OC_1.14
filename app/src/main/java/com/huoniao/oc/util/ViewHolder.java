package com.huoniao.oc.util;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}
	
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position){
		
		if (convertView == null) {
			
			return new ViewHolder(context, parent, layoutId, position);
		}else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}		
	}
	/**
	 * 通过viewId获取控件
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId){
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		Log.d("debug", "(T) view =" + (T) view);
		return (T) view;
	}

	public View getConvertView() {
		
		return mConvertView;
	}
	
	/**
	 * 设置TextView的值
	 * @param viewId
	 * @param text
	 * @return
	 * 
	 * 可以根据项目item需要控件的需求添加设置各种控件的方法
	 */
	public ViewHolder setText(int viewId, String text){
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
		
	}
	
	/**
	 * 设置本地图片资源
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int resId){
		ImageView iv = getView(viewId);
		iv.setImageResource(resId);
		return this;
		
	}
	/**
	 * 设置CheckBox的选择状态
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setCheckd(int viewId, Boolean b){
		CheckBox cb = getView(viewId);
		cb.setChecked(b);
		return this;
		
	}
}

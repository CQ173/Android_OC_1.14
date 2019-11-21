package com.huoniao.oc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;

import java.util.ArrayList;
import java.util.List;

public class Gallery_adapter extends BaseAdapter {
	
	Context context;
	
	List<Object> list;

	public Gallery_adapter(Context context) {
		this.context = context;
		list = new ArrayList<Object>();
	}
	
	public void setList(List<Object> list) {
		this.list = list;
		this.notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null || convertView.getTag() == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent,false);
			
			viewHolder = new ViewHolder();
			
			viewHolder.gallery_iv = (ImageView) convertView.findViewById(R.id.gallery_iv);
			
			convertView.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.gallery_iv.setImageBitmap((Bitmap) list.get(position));
		try {
			Glide.with(MyApplication.mContext).load(list.get(position)).into(viewHolder.gallery_iv);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Glide.with(MyApplication.mContext).load(list.get(position)).into(viewHolder.gallery_iv);

		return convertView;
	}
	
	class ViewHolder{
		ImageView gallery_iv;
	}

}

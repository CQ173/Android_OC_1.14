package com.huoniao.oc.adapter;

import java.util.List;

import com.huoniao.oc.R;
import com.huoniao.oc.bean.OutletsMyMessageBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OltMsgMainAdapter extends BaseAdapter{
	private List<OutletsMyMessageBean> mdata;
	private LayoutInflater mInflater;
	
	public OltMsgMainAdapter(Context context, List<OutletsMyMessageBean> datas) {
		this.mdata = datas;
		this.mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		if (mdata.size() > 3) {
			return 3;
		}else {
			return mdata.size();
		}
		
	}

	@Override
	public Object getItem(int position) {
		return mdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.outlets_mymessage_mainitem, parent, false);
			holder.mContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.mData = (TextView) convertView.findViewById(R.id.tv_data);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		OutletsMyMessageBean msgBean = mdata.get(position);
		
		holder.mContent.setText(msgBean.getContent());
		holder.mData.setText(msgBean.getInfoDate());
		return convertView;
	}
	
	class ViewHolder{
		TextView mContent;
		TextView mData;
		TextView mStatus;
	}
}

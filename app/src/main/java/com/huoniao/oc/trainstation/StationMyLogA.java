package com.huoniao.oc.trainstation;



import java.util.List;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.OutletsMyLogBean;
import com.huoniao.oc.bean.OutletsMyMessageBean;
import com.huoniao.oc.bean.StationMyLogBean;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.ViewHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;


public class StationMyLogA extends BaseActivity implements OnClickListener{
	
	private ImageView iv_back;
	private ListView mListView;
	private List<StationMyLogBean> mDatas;
	
	private Intent intent;
	private String id;
	private String remoteAddr;
	private String content;
	private String createDate;
	private StationMyLogBean logBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_station_mylog);
			initView();
			initData();
//			ExitApplication.getInstance().addActivity(this);
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		intent = getIntent();
		
		mDatas = (List<StationMyLogBean>) intent.getSerializableExtra("stationLogData");
		
		mListView.setAdapter(new CommonAdapter<StationMyLogBean>(this, mDatas, R.layout.outlets_mylog_item) {

			@Override
			public void convert(ViewHolder holder, StationMyLogBean logBean) {
				holder.setText(R.id.tv_content, logBean.getContent())
					  .setText(R.id.tv_data, logBean.getCreateDate());
				
			}
		});
		Log.d("mydata", "content =" + content);
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		mListView = (ListView) findViewById(R.id.myLogListView);

		iv_back.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
		
	}
	
	
	
}

package com.huoniao.oc.trainstation;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.OutletsMyMessageBean;
import com.huoniao.oc.bean.StationMyMessageBean;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.SessionJsonObjectRequest;
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
import android.widget.Toast;

public class StationMyMessgeA extends BaseActivity implements OnClickListener{
	
	private ImageView iv_back;
	private ListView mListView;
	private List<StationMyMessageBean> mDatas;
	
	private Intent intent;
	private String id;
	private String infoStatus;
	private String content;
	private String infoDate;
	private StationMyMessageBean msgBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_station_mymessage);
			initView();
			initData();
//			ExitApplication.getInstance().addActivity(this);
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		intent = getIntent();
		
		mDatas = (List<StationMyMessageBean>) intent.getSerializableExtra("stationMsgData");
		
		mListView.setAdapter(new CommonAdapter<StationMyMessageBean>(this, mDatas, R.layout.outlets_mymessage_item) {

			@Override
			public void convert(ViewHolder holder, StationMyMessageBean msgBean) {
				holder.setText(R.id.tv_content, msgBean.getContent())
					  .setText(R.id.tv_data, msgBean.getInfoDate());
				
			}
		});
		Log.d("mydata", "content =" + content);
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		mListView = (ListView) findViewById(R.id.myMessageListView);

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

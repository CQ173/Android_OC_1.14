package com.huoniao.oc.user;



import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.util.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MainLogDetailsA extends BaseActivity implements OnClickListener{
	
	private ImageView iv_back;
	private TextView tv_logTime;
	private TextView tv_logContent;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mainlog_details);
		initView();
		initData();
//		ExitApplication.getInstance().addActivity(this);
	}
	
	private void initData() {
		intent = getIntent();
		tv_logTime.setText(intent.getStringExtra("date"));
		tv_logContent.setText(intent.getStringExtra("content"));
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_logTime = (TextView) findViewById(R.id.tv_logTime);
		tv_logContent = (TextView) findViewById(R.id.tv_logContent);
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

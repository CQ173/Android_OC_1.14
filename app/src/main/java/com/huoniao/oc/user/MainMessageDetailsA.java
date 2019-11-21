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

public class MainMessageDetailsA extends BaseActivity implements OnClickListener{
	
	private ImageView iv_back;
	private TextView tv_messageTime;
	private TextView tv_messageContent;
	private Intent intent;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mainmessage_details);
		initView();
		initData();
//		ExitApplication.getInstance().addActivity(this);
	}
	
	private void initData() {
		intent = getIntent();
		tv_messageTime.setText(intent.getStringExtra("date"));
		tv_messageContent.setText(intent.getStringExtra("content"));
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_messageTime = (TextView) findViewById(R.id.tv_messageTime);
		tv_messageContent = (TextView) findViewById(R.id.tv_messageContent);
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

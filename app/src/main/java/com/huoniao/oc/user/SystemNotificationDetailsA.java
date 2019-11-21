package com.huoniao.oc.user;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SystemNotificationDetailsA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private TextView tv_title, tv_type, tv_startTime, tv_endTime, tv_content;
	private String startDate, endDate;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifactiondetails);
		initView();
		initData();
	}
	
	private void initData() {
		intent = getIntent();
		tv_title.setText(intent.getStringExtra("title"));
		if (Define.COMMONLY_NOTIFICATION.equals(intent.getStringExtra("type"))) {
			tv_type.setText("一般通知");
		}else {
			tv_type.setText("重要通知");
		}
		startDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(intent.getStringExtra("startTime")));
		tv_startTime.setText(startDate);
		endDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(intent.getStringExtra("endTime")));
		tv_endTime.setText(endDate);
		tv_content.setText(intent.getStringExtra("content"));
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_startTime = (TextView) findViewById(R.id.tv_startTime);
		tv_endTime = (TextView) findViewById(R.id.tv_endTime);
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

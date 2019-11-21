package com.huoniao.oc.useragreement;


import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.util.ExitApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class OutletsUseA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_outletsuse);
		initView();
//		ExitApplication.getInstance().addActivity(this);
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		}
		
	}

}

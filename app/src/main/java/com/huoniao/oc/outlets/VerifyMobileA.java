package com.huoniao.oc.outlets;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class VerifyMobileA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private EditText et_verifiCode;
	private TextView tv_getVericode;
	private Button bt_next;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verifymobile);
		initView();
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_verifiCode = (EditText) findViewById(R.id.et_verifiCode);
		tv_getVericode = (TextView) findViewById(R.id.tv_getVericode);
		bt_next = (Button) findViewById(R.id.bt_next);
		iv_back.setOnClickListener(this);
		tv_getVericode.setOnClickListener(this);
		bt_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_getVericode:
			//TODO
			break;
			
		case R.id.bt_next:
			//TODO
			break;	
		default:
			break;
		}
		
	}

}

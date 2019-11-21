package com.huoniao.oc.admin;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;

import java.text.ParseException;

public class AdminCapitalFlowDetailsA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private Intent intent;
	private TextView tv_tradeDate, tv_flowId, tv_tradeName, tv_tradeAccount,tv_tradeAcct,
		tv_name, tv_tradeFee, tv_type, tv_tradeStatus, tv_accountBalance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_cptflowdetails);
		initView();
		initData();

	}

	private void initData() {
		intent = getIntent();
		String expireTime = intent.getStringExtra("expireTime");
		tv_tradeDate.setText(intent.getStringExtra("tradeDate"));
		tv_flowId.setText(intent.getStringExtra("flowId"));
		tv_tradeName.setText(intent.getStringExtra("tradeName"));
		tv_tradeAcct.setText(intent.getStringExtra("tradeAcct"));
		Double tradeAmount = Double.valueOf(intent.getStringExtra("tradeFee"));
		tv_tradeFee.setText(String.format("%.2f", tradeAmount));
		if (Define.TRADE_TYPE1.equals(intent.getStringExtra("type"))) {
			tv_type.setText("收入");
		}else {
			tv_type.setText("支出");
		}
		tv_tradeAccount.setText(intent.getStringExtra("loginName"));
		tv_name.setText(intent.getStringExtra("name"));
		tv_accountBalance.setText(intent.getStringExtra("balance"));
		
		if (Define.ACCT_TRADE_STATUS_SUCCESS.equals(intent.getStringExtra("tradeStatus"))) {
			tv_tradeStatus.setText("交易成功");
		}else if(Define.ACCT_TRADE_STATUS_PENDING.equals(intent.getStringExtra("tradeStatus"))){
			tv_tradeStatus.setText("等待付款");

			try {
				if(expireTime!=null){
					long etimes = DateUtils.dataFormentHaoMiao(expireTime);
					if(DateUtils.getSystemDataHaoMiao()>etimes){  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
						tv_tradeStatus.setText("交易关闭");
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}else if(Define.ACCT_TRADE_STATUS_FINISHED.equals(intent.getStringExtra("tradeStatus"))){
			tv_tradeStatus.setText("交易结束");
		}else if (Define.ACCT_TRADE_STATUS_SUSPENSE.equals(intent.getStringExtra("tradeStatus"))) {
			tv_tradeStatus.setText("等待处理");
		}else if (Define.ACCT_TRADE_STATUS_PENDING.equals(intent.getStringExtra("tradeStatus"))) {
			tv_tradeStatus.setText("等待收款");
		}else if (Define.ACCT_TRADE_STATUS_FAIL.equals(intent.getStringExtra("tradeStatus"))) {
			tv_tradeStatus.setText("交易失败");
		}else if (Define.ACCT_TRADE_STATUS_CLOSED.equals(intent.getStringExtra("tradeStatus"))) {
			tv_tradeStatus.setText("交易关闭");
		}else if (Define.ACCT_TRADE_STATUS_REFUNDING.equals(intent.getStringExtra("tradeStatus"))) {
			tv_tradeStatus.setText("退款中");
		}else if (Define.ACCT_TRADE_STATUS_REFUND.equals(intent.getStringExtra("tradeStatus"))) {
			tv_tradeStatus.setText("交易已退款");
		}else if (Define.ACC_TRADE_STATUS_REFUND_FAIL.equals(intent.getStringExtra("tradeStatus"))) {
			tv_tradeStatus.setText("退款失败");
		}
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_tradeDate = (TextView) findViewById(R.id.tv_tradeDate);
		tv_flowId = (TextView) findViewById(R.id.tv_flowId);
		tv_tradeName = (TextView) findViewById(R.id.tv_tradeName);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_tradeAccount = (TextView) findViewById(R.id.tv_tradeAccount);//交易账户
		tv_tradeAcct = (TextView) findViewById(R.id.tv_tradeAcct);//交易账号
		tv_tradeFee = (TextView) findViewById(R.id.tv_tradeFee);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_tradeStatus = (TextView) findViewById(R.id.tv_tradeStatus);
		tv_accountBalance = (TextView) findViewById(R.id.tv_accountBalance);
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

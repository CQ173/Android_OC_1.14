package com.huoniao.oc.outlets;


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

public class CapitalFlowDetailsA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private Intent intent;
	private TextView tv_tradeDate, tv_flowId, tv_tradeName,
		 tv_tradeFee, tv_type, tv_tradeStatus, tv_curBalance,
		 tv_updateDate, tv_acount , tv_Relation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outlets_cptflowdetails);
		initView();
		initData();

	}

	private void initData() {
		intent = getIntent();
		String expireTime = intent.getStringExtra("expireTime");
		tv_tradeDate.setText(intent.getStringExtra("tradeDate"));
		tv_flowId.setText(intent.getStringExtra("flowId"));
		tv_tradeName.setText(intent.getStringExtra("tradeName"));
		tv_curBalance.setText(intent.getStringExtra("curBalanceString"));
		tv_updateDate.setText(intent.getStringExtra("updateDate"));
		tv_Relation.setText(intent.getStringExtra("sourceFlowId"));

		Double tradeAmount = Double.valueOf(intent.getStringExtra("tradeFee"));
		tv_tradeFee.setText(String.format("%.2f", tradeAmount));
		tv_acount.setText(intent.getStringExtra("tradeAcct")==null ?"":intent.getStringExtra("tradeAcct")); //交易账号
		if (Define.TRADE_TYPE1.equals(intent.getStringExtra("type"))) {
			tv_type.setText("收入");
		}else {
			tv_type.setText("支出");
		}
		
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
		}else if (Define.ACCT_TRADE_STATUS_WAIT.equals(intent.getStringExtra("tradeStatus"))){
			tv_tradeStatus.setText("等待付款");
		}

	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_tradeDate = (TextView) findViewById(R.id.tv_tradeDate);
		tv_flowId = (TextView) findViewById(R.id.tv_flowId);
		tv_tradeName = (TextView) findViewById(R.id.tv_tradeName);

		tv_tradeFee = (TextView) findViewById(R.id.tv_tradeFee);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_tradeStatus = (TextView) findViewById(R.id.tv_tradeStatus);
		tv_acount = (TextView) findViewById(R.id.tv_acount);//交易账号
		tv_curBalance = (TextView) findViewById(R.id.tv_curBalance);
		tv_updateDate = (TextView) findViewById(R.id.tv_updateDate);
		tv_Relation = (TextView) findViewById(R.id.tv_Relation);
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

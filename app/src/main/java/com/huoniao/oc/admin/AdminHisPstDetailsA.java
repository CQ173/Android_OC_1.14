package com.huoniao.oc.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.PerfectInformationA;
import com.huoniao.oc.user.RegisterSuccessA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.wxapi.WXEntryActivity;

public class AdminHisPstDetailsA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private TextView tv_date, tv_trainStationAccount, tv_trainStationName, tv_outletsAccount,
		tv_outletsName, tv_shouldAmount, tv_alreadyAmount, tv_withholdStatus, tv_payDate,
		tv_railwaysZongJu, tv_railwaysfenJu, tv_payWinNumber, tv_ticketCount;
	private LinearLayout layout_trainStationName, layout_trainStationAccount, layout_railwaysZongJu
			, layout_railwaysfenJu, layout_debitAccount;
	private String date, payDate, officeCode, payWinNumber, outletsName, withholdStatus
				, ticketCount, shouldAmount, alreadyAmount, railwaysfenJu, trainStationName,
				 trainStationAccount, railwaysZongJu;
	private Intent intent;
	private String roleName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hispst_details);
		initView();
		intent = getIntent();
		initData();
	}
	
	private void initData() {
		User user = (User) ObjectSaveUtil.readObject(AdminHisPstDetailsA.this, "loginResult");
		roleName = user.getRoleName();
		if (roleName != null && !roleName.isEmpty()){
				if ("铁路总局".equals(roleName)){
					layout_railwaysZongJu.setVisibility(View.GONE);
					layout_railwaysfenJu.setVisibility(View.VISIBLE);
					layout_trainStationName.setVisibility(View.VISIBLE);
					layout_trainStationAccount.setVisibility(View.VISIBLE);
					layout_debitAccount.setVisibility(View.VISIBLE);
					setOutletsUpChangDate();
					railwaysfenJu = intent.getStringExtra("branchName");
					if (railwaysfenJu != null && !railwaysfenJu.isEmpty()){
						tv_railwaysfenJu.setText(railwaysfenJu);
					}else {
						tv_railwaysfenJu.setText("");
					}

					trainStationName = intent.getStringExtra("stationName");
					if (trainStationName != null && !trainStationName.isEmpty()){
						tv_trainStationName.setText(trainStationName);
					}else {
						tv_trainStationName.setText("");
					}

					trainStationAccount = intent.getStringExtra("stationAccount");
					if (trainStationAccount != null && !trainStationAccount.isEmpty()){
						tv_trainStationAccount.setText(trainStationAccount);
					}else {
						tv_trainStationAccount.setText("");
					}

					officeCode = intent.getStringExtra("officeCode");
					if (officeCode != null && !officeCode.isEmpty()){
						tv_outletsAccount.setText(officeCode);
					}else {
						tv_outletsAccount.setText("");
					}
				}
				if ("铁路分局".equals(roleName)){
					layout_railwaysZongJu.setVisibility(View.GONE);
					layout_railwaysfenJu.setVisibility(View.GONE);
					layout_trainStationName.setVisibility(View.VISIBLE);
					layout_trainStationAccount.setVisibility(View.VISIBLE);
					layout_debitAccount.setVisibility(View.VISIBLE);
					setOutletsUpChangDate();
					trainStationName = intent.getStringExtra("stationName");
					if (trainStationName != null && !trainStationName.isEmpty()){
						tv_trainStationName.setText(trainStationName);
					}else {
						tv_trainStationName.setText("");
					}

					trainStationAccount = intent.getStringExtra("stationAccount");
					if (trainStationAccount != null && !trainStationAccount.isEmpty()){
						tv_trainStationAccount.setText(trainStationAccount);
					}else {
						tv_trainStationAccount.setText("");
					}

					officeCode = intent.getStringExtra("officeCode");
					if (officeCode != null && !officeCode.isEmpty()){
						tv_outletsAccount.setText(officeCode);
					}else {
						tv_outletsAccount.setText("");
					}
				}
		}
		if (roleName.contains("系统管理员")) {
			setOutletsUpChangDate();
			layout_railwaysZongJu.setVisibility(View.VISIBLE);
			layout_railwaysfenJu.setVisibility(View.VISIBLE);
			layout_trainStationName.setVisibility(View.VISIBLE);
			layout_trainStationAccount.setVisibility(View.VISIBLE);
			layout_debitAccount.setVisibility(View.VISIBLE);
			railwaysZongJu = intent.getStringExtra("trunkName");
			if (railwaysZongJu != null && !railwaysZongJu.isEmpty()){
				tv_railwaysZongJu.setText(railwaysZongJu);
			}else {
				tv_railwaysZongJu.setText("");
			}

			railwaysfenJu = intent.getStringExtra("branchName");
			if (railwaysfenJu != null && !railwaysfenJu.isEmpty()){
				tv_railwaysfenJu.setText(railwaysfenJu);
			}else {
				tv_railwaysfenJu.setText("");
			}

			trainStationName = intent.getStringExtra("stationName");
			if (trainStationName != null && !trainStationName.isEmpty()){
				tv_trainStationName.setText(trainStationName);
			}else {
				tv_trainStationName.setText("");
			}

			trainStationAccount = intent.getStringExtra("stationAccount");
			if (trainStationAccount != null && !trainStationAccount.isEmpty()){
				tv_trainStationAccount.setText(trainStationAccount);
			}else {
				tv_trainStationAccount.setText("");
			}

			officeCode = intent.getStringExtra("officeCode");
			if (officeCode != null && !officeCode.isEmpty()){
				tv_outletsAccount.setText(officeCode);
			}else {
				tv_outletsAccount.setText("");
			}


		}
		if (Define.TRAINSTATION.equals(LoginA.IDENTITY_TAG) || Define.TRAINSTATION.equals(PerfectInformationA.IDENTITY_TAG)
					|| Define.TRAINSTATION.equals(WXEntryActivity.IDENTITY_TAG) || Define.TRAINSTATION.equals(RegisterSuccessA.IDENTITY_TAG)) {
			if (!"铁路分局".equals(roleName) && !"铁路总局".equals(roleName)) {
				layout_railwaysZongJu.setVisibility(View.GONE);
				layout_railwaysfenJu.setVisibility(View.GONE);
				layout_trainStationName.setVisibility(View.GONE);
				layout_trainStationAccount.setVisibility(View.GONE);
				layout_debitAccount.setVisibility(View.VISIBLE);
				setOutletsUpChangDate();
				officeCode = intent.getStringExtra("officeCode");
				if (officeCode != null && !officeCode.isEmpty()) {
					tv_outletsAccount.setText(officeCode);
				} else {
					tv_outletsAccount.setText("");
				}
			}
		}else if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
				|| Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
			layout_railwaysZongJu.setVisibility(View.GONE);
			layout_railwaysfenJu.setVisibility(View.GONE);
			layout_trainStationName.setVisibility(View.GONE);
			layout_trainStationAccount.setVisibility(View.GONE);
			layout_debitAccount.setVisibility(View.GONE);

			setOutletsUpChangDate();


		}
	}

	//设置数据和状态值
	private void setOutletsUpChangDate() {
		date = intent.getStringExtra("date");
		if (date != null && !date.isEmpty()){
			tv_date.setText(date);
		}else {
			tv_date.setText("");
		}
		payDate = intent.getStringExtra("createDate");
		if (payDate != null && !payDate.isEmpty()){
			tv_payDate.setText(payDate);
		}else {
			tv_payDate.setText("");
		}
		payWinNumber = intent.getStringExtra("winNumber");
		if (payWinNumber != null && !payWinNumber.isEmpty()){
			tv_payWinNumber.setText(payWinNumber);
		}else {
			tv_payWinNumber.setText("");
		}

		ticketCount = intent.getStringExtra("ticketCount");
		if (ticketCount != null && !ticketCount.isEmpty()){
			tv_ticketCount.setText(ticketCount);
		}else {
			tv_ticketCount.setText("");
		}

//		officeCode = intent.getStringExtra("outletsAccount");
//		if (officeCode != null && !officeCode.isEmpty()){
//			tv_outletsAccount.setText(officeCode);
//		}else {
//			tv_outletsAccount.setText("");
//		}

		outletsName = intent.getStringExtra("outletsName");
		if (outletsName != null && !outletsName.isEmpty()){
			tv_outletsName.setText(outletsName);
		}else {
			tv_outletsName.setText("");
		}

		shouldAmount = intent.getStringExtra("shouldAmount");
		if (shouldAmount != null && !shouldAmount.isEmpty()){
			tv_shouldAmount.setText(shouldAmount);
		}else {
			tv_shouldAmount.setText("");
		}

		alreadyAmount = intent.getStringExtra("alreadyAmount");
		if (alreadyAmount != null && !alreadyAmount.isEmpty()){
			tv_alreadyAmount.setText(alreadyAmount);
		}else {
			tv_alreadyAmount.setText("");
		}
           String  withholdStatusName =  intent.getStringExtra("withholdStatusName");
		tv_withholdStatus.setText(withholdStatusName);
	/*	withholdStatus = intent.getStringExtra("withholdStatus");
		if (Define.CHARG_SUCCESS.equals(withholdStatus)) {
			tv_withholdStatus.setText(R.string.charg_success);  //成功
		}else if (Define.CHARG_WAIT.equals(withholdStatus)) {
			tv_withholdStatus.setText(R.string.charg_wait);  //待扣款
		}else if(Define.CHARG_FAIL.equals(withholdStatus)){
			tv_withholdStatus.setText(R.string.charg_fail); //失败

		}else if(Define.CHARG_RECHARGE.equals(withholdStatus)){
			tv_withholdStatus.setText(R.string.charg_recharge); //待充值
		}else if(Define.CHARG_RECHARGE_SUCCESS.equals(withholdStatus)){
			tv_withholdStatus.setText(R.string.charg_recharge_success); //补缴成功
		}*/
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_trainStationAccount = (TextView) findViewById(R.id.tv_trainStationAccount);
		tv_trainStationName = (TextView) findViewById(R.id.tv_trainStationName);
		tv_outletsAccount = (TextView) findViewById(R.id.tv_outletsAccount);
		tv_outletsName = (TextView) findViewById(R.id.tv_outletsName);
		tv_shouldAmount = (TextView) findViewById(R.id.tv_shouldAmount);
		tv_alreadyAmount = (TextView) findViewById(R.id.tv_alreadyAmount);
		tv_withholdStatus = (TextView) findViewById(R.id.tv_withholdStatus);
		layout_trainStationName = (LinearLayout) findViewById(R.id.layout_trainStationName);
		layout_trainStationAccount = (LinearLayout) findViewById(R.id.layout_trainStationAccount);
		layout_railwaysZongJu = (LinearLayout) findViewById(R.id.layout_railwaysZongJu);
		layout_railwaysfenJu = (LinearLayout) findViewById(R.id.layout_railwaysfenJu);
		layout_debitAccount = (LinearLayout) findViewById(R.id.layout_debitAccount);
		tv_payDate = (TextView) findViewById(R.id.tv_payDate);
		tv_railwaysZongJu = (TextView) findViewById(R.id.tv_railwaysZongJu);
		tv_railwaysfenJu = (TextView) findViewById(R.id.tv_railwaysfenJu);
		tv_payWinNumber = (TextView) findViewById(R.id.tv_payWinNumber);
		tv_ticketCount = (TextView) findViewById(R.id.tv_ticketCount);
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

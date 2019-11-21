package com.huoniao.oc.outlets;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.BankCardBean;
import com.huoniao.oc.common.BankIcon;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

public class BankCardDetailsA extends BaseActivity implements OnClickListener{
	private String cardId, bankCode, cardType, lastNumber, openBankName, openBankAreaName;
	private ImageView iv_back, iv_bankLog;
	private TextView  tv_bankName, tv_cardType, tv_lastNumber;
	private String[] cardManage_list = { "修改", "删除" };
	private String cardManageOption;
	//private TextView tv_cardMannage;
	private ProgressDialog pd;
	private Intent intent;
	private TextView tv_custname;
	private Button bt_unbound;
	private TextView tv_title;
	private String isSigned;
	private String isPublic;
	private MyPopWindow myPopWindow;
	private BankCardBean.BankInfoBean bankInfo;
	private TextView tv_single_day_payment_limit;
	private TextView tv_single_payment_limit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankcard_details);
		initView();
		initData();
		initWidget();  //对控件操作
	}

	private void initWidget() {
		 setPremissionShowHideView(Premission.ACCT_USERCARD_EDIT,bt_unbound); //是否有 添加,修改,删除银卡权限
	}

	private void initData() {
		intent = getIntent();
		tv_title.setText("银行卡");
		cardId = intent.getStringExtra("cardId");
		bankCode = intent.getStringExtra("bankCode");
		openBankName = intent.getStringExtra("openBankName");
		openBankAreaName = intent.getStringExtra("openBankAreaName");
		tv_custname.setText(intent.getStringExtra("custname")==null?"":intent.getStringExtra("custname")); //开户人姓名
		isSigned = intent.getStringExtra("isSigned");   //是否签约         是否签约 0未签约，1 已签约     若未传或传空值这默认为0
		isPublic = intent.getStringExtra("isPublic");   //是否为对公账户    是否为对公账户  0不是， 1是   若未传或传空值这默认为0
		//银行信息
	    	bankInfo = (BankCardBean.BankInfoBean) intent.getSerializableExtra("bankInfo");
        if(bankInfo != null){
			tv_single_day_payment_limit.setText(bankInfo.getDailyLimit()==-1?"不限额":"￥"+bankInfo.getDailyLimit()+""); //单日支付限额
			tv_single_payment_limit.setText(bankInfo.getEveryLimit()==-1?"不限额": "￥"+bankInfo.getEveryLimit()+""); //单笔支付限额
		}
       String cardName = intent.getStringExtra("cardName");//银行名称
		tv_bankName.setText(cardName);
		iv_bankLog.setImageResource(BankIcon.getBankIcon(bankCode));
	/*	if ("BCOM".equals(bankCode)) { //交通银行
			iv_bankLog.setImageResource(R.drawable.jianshebank_log);
		}else if ("BOC".equals(bankCode)) { //中国银行
			iv_bankLog.setImageResource(R.drawable.zhongguo_w);
		}else if ("CEB".equals(bankCode)) { //中国光大银行
			iv_bankLog.setImageResource(R.drawable.jianshebank_log);
		}else if ("CMB".equals(bankCode)) { //招商银行
			iv_bankLog.setImageResource(R.drawable.zhaoshang_w);
		}else if ("HXB".equals(bankCode)) { //华夏银行
			iv_bankLog.setImageResource(R.drawable.jianshebank_log);
		}else if ("BOB".equals(bankCode)) { //北京银行
			iv_bankLog.setImageResource(R.drawable.jianshebank_log);
		}else if ("ICBC".equals(bankCode)) { //中国工商银行										 );
			iv_bankLog.setImageResource(R.drawable.gongshang_w);
		}else if ("CMBC".equals(bankCode)) { //中国民生银行
			iv_bankLog.setImageResource(R.drawable.jianshebank_log);
		}else if ("POST".equals(bankCode)) { //中国邮政储蓄银行
			iv_bankLog.setImageResource(R.drawable.jianshebank_log);
		}else if ("SPDB".equals(bankCode)) { //浦发银行
			iv_bankLog.setImageResource(R.drawable.pufa_w);
		}else if ("CCB".equals(bankCode)) {  // 中国建设银行
			iv_bankLog.setImageResource(R.drawable.jianshe_w);
		}else if ("NJCB".equals(bankCode)) { //南京银行
			iv_bankLog.setImageResource(R.drawable.jianshebank_log);
		}else if ("ABC".equals(bankCode)) { //中国农业银行
			iv_bankLog.setImageResource(R.drawable.jianshebank_log);
		}else if ("ECITIC".equals(bankCode)) { //中信银行
			iv_bankLog.setImageResource(R.drawable.zhongxin_w);
		}*/


		if (Define.SAVINGS_CARD.equals(isPublic)) {
			tv_cardType.setText("储蓄卡");
		}else if (Define.PUBLIC_ACCOUNT.equals(isPublic)) {
			tv_cardType.setText("对公账户");
		}

		lastNumber = intent.getStringExtra("cardNumber");
		if (lastNumber != null && !lastNumber.isEmpty() && lastNumber.length() > 4) {
			String newCardNumber = lastNumber.substring(lastNumber.length()-4, lastNumber.length());
			tv_lastNumber.setText(newCardNumber);
		}
		
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back); //返回键
		//iv_bankLog = (ImageView) findViewById(R.id.iv_bankLog); //银行图标
		//tv_cardMannage = (TextView) findViewById(R.id.tv_cardMannage); //管理 需要删除 这个是弹出修改和删除 需要权限
		//tv_bankName = (TextView) findViewById(R.id.tv_bankName); //银行名称
		//tv_cardType = (TextView) findViewById(R.id.tv_cardType);  //卡类型
		//tv_lastNumber = (TextView) findViewById(R.id.tv_lastNumber); //银行卡尾号
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_lastNumber = (TextView) findViewById(R.id.tv_cardCode); //银行卡尾号
		iv_bankLog =    (ImageView)  findViewById(R.id.iv_bankLog)  ; //银行图标
		tv_bankName =  (TextView) findViewById(R.id.tv_bankName) ;//银行名称
		tv_custname = (TextView)findViewById(R.id.tv_custname);//开户人姓名
		tv_cardType =  (TextView) findViewById(R.id.tv_cardType) ;//卡类型
		bt_unbound = (Button) findViewById(R.id.bt_unbound);	//解除绑定
		tv_single_day_payment_limit = (TextView) findViewById(R.id.tv_single_day_payment_limit);//单日支付限额
		tv_single_payment_limit = (TextView) findViewById(R.id.tv_single_payment_limit);	//单笔支付限额

		iv_back.setOnClickListener(this);
		bt_unbound.setOnClickListener(this);
		//tv_cardMannage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_unbound:
			if("1".equals(isSigned)){
				ToastUtils.showToast(BankCardDetailsA.this,"银行卡解绑失败，该银行卡已签约代扣！");
	             return;
					}
			if("1".equals(isPublic)){
				ToastUtils.showToast(BankCardDetailsA.this,"对公账户解绑请联系客服处理！");
			 return;
			}

			if(myPopWindow !=null && myPopWindow.isShow()){
				myPopWindow.dissmiss();
			}
			myPopWindow = new MyPopAbstract(){
				@Override
				protected void setMapSettingViewWidget(View view) {
				TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
				TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
					tv_confirm.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							deleteCard(); //解除绑定
							myPopWindow.dissmiss();
						}
					});
					tv_cancle.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							myPopWindow.dissmiss();
						}
					});
				}

				@Override
				protected int layout() {
					return R.layout.unbound_pop;
				}
			}.popWindowTouch(BankCardDetailsA.this).showAtLocation(tv_title, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

				break;
		/*case R.id.tv_cardMannage:
			showPersonalOptionDialog();
			break;*/
		default:
			break;
		}
		
	}
	
	
	
	
	  
	private void showPersonalOptionDialog() {
		AlertDialog.Builder builder = new Builder(this);

		builder.setItems(cardManage_list, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				cardManageOption = cardManage_list[arg1];
				if ("修改".equals(cardManageOption)) {
					intent = new Intent(BankCardDetailsA.this, UpdataCardA.class);
					intent.putExtra("cardId", cardId);
					intent.putExtra("bankCode", bankCode);
				//	intent.putExtra("cardType", cardType);
					intent.putExtra("cardNumber", lastNumber);
					intent.putExtra("openBankName", openBankName);
					intent.putExtra("openBankAreaName", openBankAreaName);
					startActivity(intent);
					overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				} else if("删除".equals(cardManageOption)) {
					//TODO
					deleteCard();
				}
			}
		});
		AlertDialog dialog = builder.create();// 获取dialog Window
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM);

		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// 实例化Window操作者 
		lp.x = 0; 
		// 新位置X坐标 
		lp.y = 150; // 新位置Y坐标
		// 放置属性
		dialogWindow.setAttributes(lp);

		dialog.show();
	}

	//删除银行卡
	private void deleteCard() {
		cpd.show();
		VolleyNetCommon  volleyNetCommon = new VolleyNetCommon();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("operateType", Define.BANKCARD_DELETE);
			jsonObject.put("id", cardId);
			jsonObject.put("bankCode", bankCode);
		//	jsonObject.put("cardType", cardType);
			jsonObject.put("cardNo", lastNumber);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = Define.URL + "acct/manageBankCard";
		JsonObjectRequest manageBankCard = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(BankCardDetailsA.this) {
			@Override
			public void volleyResponse(Object o) {

			}

			@Override
			public void volleyError(VolleyError volleyError) {
				Toast.makeText(BankCardDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void netVolleyResponese(JSONObject json) {
				Toast.makeText(BankCardDetailsA.this, "删除银行卡成功！", Toast.LENGTH_SHORT).show();
				setResult(2);          //关闭当前页去更新银行卡列表
				finish();
			}

			@Override
			protected void PdDismiss() {
				cpd.dismiss();
			}
			@Override
			protected void errorMessages(String message) {
				super.errorMessages(message);
				Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
			}
		}, "manageBankCard", true);
		volleyNetCommon.addQueue(manageBankCard);

	}

	/**
	 * 添加银行卡
	 * 
	 * @throws Exception
	 */
//	private void updataBankCard() throws Exception {
//		pd = new CustomProgressDialog(BankCardDetailsA.this, "正在加载中...", R.anim.frame_anim);
//		pd.show();
//	
//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put("operateType", Define.BANKCARD_ADD);
//			jsonObject.put("bankCode", bankCode);
//			jsonObject.put("cardType", cardType);
//			jsonObject.put("cardNo", cardNumber);
//			
//		} catch (JSONException e1) {
//			e1.printStackTrace();
//		}
//		String url = Define.URL + "acct/manageBankCard";
//		
//		
//		SessionJsonObjectRequest addCardRequest = new SessionJsonObjectRequest(Method.POST,
//				url, jsonObject, new Listener<JSONObject>() {
//			
//					@Override
//					public void onResponse(JSONObject response) {
//						if (response == null) {
//							return;
//						}
////						String data = response.toString();
////						if (data.isEmpty() || data == null) {
////							return;
////						}
//						Log.d("debug", "response =" + response.toString());
//						try {
//							String responseCode = response.getString("code");
//							String message = response.getString("msg");
//							if ("0".equals(responseCode)) {
////								int num = response.getInt("size");
//								pd.dismiss();
//								Toast.makeText(TemporaryAddCardA.this, "添加银行卡成功!", Toast.LENGTH_SHORT).show();
//								intent = new Intent(TemporaryAddCardA.this, BankCardListA.class);
//								startActivity(intent);
//								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//							} else if("46000".equals(responseCode)){
//								pd.dismiss();
//								Toast.makeText(TemporaryAddCardA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
//								intent = new Intent(TemporaryAddCardA.this, LoginA.class);
//								startActivity(intent);
//								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//							} else {
//								pd.dismiss();
//								Toast.makeText(TemporaryAddCardA.this, message, Toast.LENGTH_SHORT).show();
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//							Log.d("exciption", "exciption =" + e.toString());
//						}
//
//					}
//				}, new ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						pd.dismiss();
//						Toast.makeText(TemporaryAddCardA.this, "网络错误,请检查网络连接！", Toast.LENGTH_SHORT).show();
//						Log.d("debug", "error = " + error.toString());
//
//					}
//				});
//		// 解决重复请求后台的问题
//		addCardRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
//		addCardRequest.setTag("addCardRequest");
//		// 将请求加入全局队列中
//		MyApplication.getHttpQueues().add(addCardRequest);
//
//	}
}

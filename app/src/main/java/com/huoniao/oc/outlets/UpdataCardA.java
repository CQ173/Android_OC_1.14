package com.huoniao.oc.outlets;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SpinerPopWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdataCardA extends BaseActivity implements OnClickListener, MySpinerAdapter.IOnItemSelectListener{
	private ImageView iv_back;
	private TextView tv_bankType, tv_cardType, tv_cardName, tv_openBankArea;
	private LinearLayout layout_cardType, layout_openBankArea;
	private EditText et_cardNumber, et_openBank;
	private String bankCode, cardType, cardNumber, custname, cardId, openBankName, openBankAreaName;
	private RelativeLayout rl_choiceProvinces, rl_choiceCitys;
	private TextView tv_provinces, tv_citys;
	private Button bt_complete;
//	private ProgressDialog pd;
//	private String operationTag;
	private SpinerPopWindow mSpinerPopWindow;
	User user;
	private Button bt_save;
	private List<String> cardTypeList = new ArrayList<String>();
	private List<String> provinceNameList = new ArrayList<String>();
	private List<String> provinceCodeList = new ArrayList<String>();
	private List<String> cityNameList = new ArrayList<String>();
	private List<String> cityCodeList = new ArrayList<String>();
	private String provinceCode = "";
	private String cityCode = "";
	private String CHOICE_TAG;
	private String provinces, citys, location;
	private Intent intent;
	private static final int PROVINCE_LIST = 1;
	private static final int CITY_LIST = 2;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case PROVINCE_LIST: {
					@SuppressWarnings("unchecked")
					List<User> proList = (List<User>) msg.obj;

					for (User user : proList) {
						provinceNameList.add(user.getProvinceName());
						provinceCodeList.add(user.getProvinceCode());
					}

					break;

				}
				case CITY_LIST: {
					@SuppressWarnings("unchecked")
					List<User> cityList = (List<User>) msg.obj;
					cityNameList.clear();
					cityCodeList.clear();
					for (User user : cityList) {
						cityNameList.add(user.getCityName());
						cityCodeList.add(user.getCityCode());
					}

					break;

				}

			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatabankcard);
		initView();
		initData();
	}
	
	private void initData() {
		intent = getIntent();
		bankCode = intent.getStringExtra("bankCode");
		if ("BCOM".equals(bankCode)) {
			tv_bankType.setText("交通银行");
			
		}else if ("BOC".equals(bankCode)) {
			tv_bankType.setText("中国银行");
		}else if ("CEB".equals(bankCode)) {
			tv_bankType.setText("中国光大银行");
		}else if ("CMB".equals(bankCode)) {
			tv_bankType.setText("招商银行");
			
		}else if ("HXB".equals(bankCode)) {
			tv_bankType.setText("华夏银行");
		
		}else if ("BOB".equals(bankCode)) {
			tv_bankType.setText("北京银行");
			
		}else if ("ICBC".equals(bankCode)) {
			tv_bankType.setText("中国工商银行");
			
		}else if ("CMBC".equals(bankCode)) {
			tv_bankType.setText("中国民生银行");
			
		}else if ("POST".equals(bankCode)) {
			tv_bankType.setText("中国邮政储蓄银行");
			
		}else if ("SPDB".equals(bankCode)) {
			tv_bankType.setText("浦发银行");
			
		}else if ("CCB".equals(bankCode)) {
			tv_bankType.setText("中国建设银行");
			
		}else if ("NJCB".equals(bankCode)) {
			tv_bankType.setText("南京银行");
		
		}else if ("ABC".equals(bankCode)) {
			tv_bankType.setText("中国农业银行");
			
		}else if ("ECITIC".equals(bankCode)) {
			tv_bankType.setText("中信银行");
		
		}	
		cardType = intent.getStringExtra("cardType");
		cardNumber = intent.getStringExtra("cardNumber");
		if (cardNumber != null && !cardNumber.isEmpty()){
			et_cardNumber.setText(cardNumber);
		}
		cardId = intent.getStringExtra("cardId");
		openBankName = intent.getStringExtra("openBankName");
		openBankAreaName = intent.getStringExtra("openBankAreaName");
		if (openBankName != null && !openBankName.isEmpty()){
			et_openBank.setText(openBankName);
		}

		if (openBankAreaName != null && !openBankAreaName.isEmpty()){
			tv_openBankArea.setText(openBankAreaName);
		}
		user = (User) ObjectSaveUtil.readObject(UpdataCardA.this, "loginResult");
		tv_cardName.setText(user.getCorpName());
		cardTypeList.add("借记卡");
		cardTypeList.add("信用卡");
//		cardTypeList.add("储蓄卡");
		if (Define.DEBIT_CARD.equals(cardType)) {
			tv_cardType.setText("借记卡");
		}else {
			tv_cardType.setText("信用卡");
		}

		try {
			getAllProvince();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_bankType = (TextView) findViewById(R.id.tv_bankType);
		tv_cardType = (TextView) findViewById(R.id.tv_cardType);
		layout_cardType = (LinearLayout) findViewById(R.id.layout_cardType);
		tv_cardName = (TextView) findViewById(R.id.tv_cardName);
		et_cardNumber = (EditText) findViewById(R.id.et_cardNumber);
		tv_openBankArea = (TextView) findViewById(R.id.tv_openBankArea);
		layout_openBankArea = (LinearLayout) findViewById(R.id.layout_openBankArea);
		et_openBank = (EditText) findViewById(R.id.et_openBank);
		bt_save = (Button) findViewById(R.id.bt_save);
		iv_back.setOnClickListener(this);
		layout_cardType.setOnClickListener(this);
		bt_save.setOnClickListener(this);
		layout_openBankArea.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		//选择卡类型	
		case R.id.layout_cardType:
			mSpinerPopWindow = new SpinerPopWindow(this);
			CHOICE_TAG = "1";
			mSpinerPopWindow.refreshData(cardTypeList, 0);
			mSpinerPopWindow.setItemListener(this);
			showSpinWindow(layout_cardType);
			break;

		case R.id.layout_openBankArea:
			choiceLoctionDialog();
			break;

		case R.id.bt_save:
			
			cardNumber = et_cardNumber.getText().toString();
			if (cardNumber == null || cardNumber.isEmpty()) {
				Toast.makeText(UpdataCardA.this, "请输入银行卡号!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			cardType = tv_cardType.getText().toString();
			if ("请选择卡类型".equals(cardType)) {
				Toast.makeText(UpdataCardA.this, "请选择卡类型!", Toast.LENGTH_SHORT).show();
				return;
			}else if ("借记卡".equals(cardType)) {
				cardType = Define.DEBIT_CARD;
				if (cardNumber.length() != 19) {
					Toast.makeText(UpdataCardA.this, "请输入正确的银行卡号!", Toast.LENGTH_SHORT).show();
					return;
				}
			}else if ("信用卡".equals(cardType)) {
				cardType = Define.CREDIT_CARD;
				if (cardNumber.length() != 16) {
					Toast.makeText(UpdataCardA.this, "请输入正确的银行卡号!", Toast.LENGTH_SHORT).show();
					return;
				}
			}
				
			custname = tv_cardName.getText().toString();
			openBankName = et_openBank.getText().toString();
			if (openBankName == null || openBankName.isEmpty()){
				Toast.makeText(UpdataCardA.this, "请输入开户行名称!", Toast.LENGTH_SHORT).show();
				return;
			}
			openBankAreaName = tv_openBankArea.getText().toString();
			if ("请选择归属区域".equals(openBankAreaName)){
				Toast.makeText(UpdataCardA.this, "请选择归属区域!", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				updataBankCard();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;	
			
		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(int pos) {
		if ("1".equals(CHOICE_TAG)) {
			setIdentity(pos);
		} else if ("2".equals(CHOICE_TAG)) {
			setProvince(pos);
			provinceCode = getProvinceCode(pos);
			try {
				getAllCitys();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("3".equals(CHOICE_TAG)) {
			setCitys(pos);
			cityCode = getCityCode(pos);
		}
		
	}
	
	// 选择用户类型
		private void setIdentity(int pos) {
			if (pos >= 0 && pos <= cardTypeList.size()) {
				String value = cardTypeList.get(pos);

				tv_cardType.setText(value);

			}
		}
		
		private void showSpinWindow(View v) {
			Log.e("", "showSpinWindow");
			mSpinerPopWindow.setWidth(v.getWidth());
			mSpinerPopWindow.showAsDropDown(v);
		}



	private void choiceLoctionDialog() {
		// String url = Define.URL+"acct/getCode";
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.selectlocation_dialog, null);

		rl_choiceProvinces = (RelativeLayout) view.findViewById(R.id.rl_choiceProvinces);
		rl_choiceCitys = (RelativeLayout) view.findViewById(R.id.rl_choiceCitys);
		tv_provinces = (TextView) view.findViewById(R.id.tv_provinces);
		tv_citys = (TextView) view.findViewById(R.id.tv_citys);
		bt_complete = (Button) view.findViewById(R.id.bt_complete);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(view);
		final AlertDialog dialog = builder.create();// 获取dialog

		dialog.show();

		rl_choiceProvinces.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSpinerPopWindow = new SpinerPopWindow(UpdataCardA.this);
				CHOICE_TAG = "2";
				mSpinerPopWindow.refreshData(provinceNameList, 0);
				mSpinerPopWindow.setItemListener(UpdataCardA.this);
				showSpinWindow(rl_choiceProvinces);

			}
		});

		rl_choiceCitys.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (provinceCode.isEmpty() || provinceCode == null) {
					Toast.makeText(UpdataCardA.this, "请先选择省份！", Toast.LENGTH_SHORT).show();
					return;
				}
				mSpinerPopWindow = new SpinerPopWindow(UpdataCardA.this);
				CHOICE_TAG = "3";
				mSpinerPopWindow.refreshData(cityNameList, 0);
				mSpinerPopWindow.setItemListener(UpdataCardA.this);
				showSpinWindow(rl_choiceCitys);

			}
		});

		bt_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				provinces = tv_provinces.getText().toString();
				citys = tv_citys.getText().toString();
				if ("选择省".equals(provinces)) {
					Toast.makeText(UpdataCardA.this, "请选择省！", Toast.LENGTH_SHORT).show();
					return;
				}

				if ("选择市".equals(citys)) {
					Toast.makeText(UpdataCardA.this, "请选择市！", Toast.LENGTH_SHORT).show();
					return;
				}

				location = provinces + "/" + citys;
				tv_openBankArea.setText(location);
				dialog.dismiss();
			}
		});
	}

	// 选择省份
	private void setProvince(int pos) {
		if (pos >= 0 && pos <= provinceNameList.size()) {
			String value = provinceNameList.get(pos);

			tv_provinces.setText(value);
		}
	}

	// 获取省份code
	private String getProvinceCode(int pos) {
		if (pos >= 0 && pos <= provinceCodeList.size()) {
			provinceCode = provinceCodeList.get(pos);

		}
		return provinceCode;
	}

	// 选择城市
	private void setCitys(int pos) {
		if (pos >= 0 && pos <= cityNameList.size()) {
			String value = cityNameList.get(pos);

			tv_citys.setText(value);
		}
	}

	// 获取城市code
	private String getCityCode(int pos) {
		if (pos >= 0 && pos <= cityNameList.size()) {
			cityCode = cityCodeList.get(pos);

		}
		return cityCode;
	}
		
		/**
		 * 添加银行卡
		 * 
		 * @throws Exception
		 */
		private void updataBankCard() throws Exception {
//			pd = new CustomProgressDialog(UpdataCardA.this, "正在加载中...", R.anim.frame_anim);
			cpd.show();
		
			JSONObject jsonObject = new JSONObject();
			try {
				
				jsonObject.put("operateType", Define.BANKCARD_UPDATA);
				jsonObject.put("id", cardId);
				jsonObject.put("bankCode", bankCode);
				jsonObject.put("cardType", cardType);
				jsonObject.put("cardNo", cardNumber);
				jsonObject.put("custName", custname);
				jsonObject.put("openBankName", openBankName);
				jsonObject.put("openBankAreaCode", cityCode);
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			String url = Define.URL + "acct/manageBankCard";
			
			
			SessionJsonObjectRequest upadataCardRequest = new SessionJsonObjectRequest(Method.POST,
					url, jsonObject, new Listener<JSONObject>() {
				
						@Override
						public void onResponse(JSONObject response) {
							if (response == null) {
								return;
							}
//							String data = response.toString();
//							if (data.isEmpty() || data == null) {
//								return;
//							}
							Log.d("debug", "response =" + response.toString());
							try {
								String responseCode = response.getString("code");
								String message = response.getString("msg");
								if ("0".equals(responseCode)) {
//									int num = response.getInt("size");
									cpd.dismiss();
									Toast.makeText(UpdataCardA.this, "修改银行卡成功!", Toast.LENGTH_SHORT).show();
									intent = new Intent(UpdataCardA.this, BankCardListA.class);
									startActivity(intent);
									overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								} else if("46000".equals(responseCode)){
									cpd.dismiss();
									Toast.makeText(UpdataCardA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
									intent = new Intent(UpdataCardA.this, LoginA.class);
									startActivity(intent);
									overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								} else {
									cpd.dismiss();
									Toast.makeText(UpdataCardA.this, message, Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								cpd.dismiss();
								e.printStackTrace();
								Log.d("exciption", "exciption =" + e.toString());
							}

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							cpd.dismiss();
							Toast.makeText(UpdataCardA.this, R.string.netError, Toast.LENGTH_SHORT).show();
							Log.d("debug", "error = " + error.toString());

						}
					});
			// 解决重复请求后台的问题
			upadataCardRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
			upadataCardRequest.setTag("upadataCardRequest");
			// 将请求加入全局队列中
			MyApplication.getHttpQueues().add(upadataCardRequest);

		}

	/**
	 * 获取所有省份
	 *
	 * @throws Exception
	 */
	private void getAllProvince() throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("", "");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		cpd.show();
		String url = Define.URL + "user/getProvinceList";
		final List<User> provinceList = new ArrayList<User>();
		SessionJsonObjectRequest provinceListRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								JSONArray jsonArray = response.getJSONArray("data");

								for (int i = 0; i < jsonArray.length(); i++) {
									User user = new User();

									JSONObject provinceObj = (JSONObject) jsonArray.get(i);
									String provinceCode = provinceObj.optString("code");
									String provinceName = provinceObj.getString("name");

									user.setProvinceCode(provinceCode);
									user.setProvinceName(provinceName);

									provinceList.add(user);
								}

								Runnable proRunnable = new Runnable() {

									@Override
									public void run() {

										Message msg = new Message();
										msg.what = PROVINCE_LIST;
										msg.obj = provinceList;
										mHandler.sendMessage(msg);
									}
								};

								Thread proThread = new Thread(proRunnable);
								proThread.start();
								cpd.dismiss();
							} else {
								cpd.dismiss();
								Toast.makeText(UpdataCardA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							cpd.dismiss();
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(UpdataCardA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		provinceListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		provinceListRequest.setTag("updCardProvince");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(provinceListRequest);

	}

	/**
	 * 根据选择的省份code获取对应的城市列表
	 *
	 * @throws Exception
	 */
	private void getAllCitys() throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("code", provinceCode);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (provinceCode.isEmpty() || provinceCode == null) {
			Toast.makeText(UpdataCardA.this, "请先选择省份！", Toast.LENGTH_SHORT).show();
			return;
		}
		cpd.show();

		String url = Define.URL + "user/getCityListByProvice";
		final List<User> cityList = new ArrayList<User>();
		SessionJsonObjectRequest cityListRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								JSONArray jsonArray = response.getJSONArray("data");

								for (int i = 0; i < jsonArray.length(); i++) {
									User user = new User();

									JSONObject cityObj = (JSONObject) jsonArray.get(i);
									String cityCode = cityObj.optString("code");
									String cityName = cityObj.getString("name");

									user.setCityCode(cityCode);
									user.setCityName(cityName);

									cityList.add(user);
								}

								Runnable cityRunnable = new Runnable() {

									@Override
									public void run() {

										Message msg = new Message();
										msg.what = CITY_LIST;
										msg.obj = cityList;
										mHandler.sendMessage(msg);
									}
								};

								Thread cityThread = new Thread(cityRunnable);
								cityThread.start();
								cpd.dismiss();
							} else {
								cpd.dismiss();
								Toast.makeText(UpdataCardA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							cpd.dismiss();
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(UpdataCardA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		cityListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		cityListRequest.setTag("updCardCity");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(cityListRequest);

	}
		
		@Override
		protected void onStop() {
			super.onStop();
			MyApplication.getHttpQueues().cancelAll("upadataCardRequest");
			MyApplication.getHttpQueues().cancelAll("updCardCity");
			MyApplication.getHttpQueues().cancelAll("updCardProvince");
		}
}

package com.huoniao.oc.outlets;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.bean.BankCard;
import com.huoniao.oc.bean.BankCardBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.EditFilterUtils;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SpinerPopWindow;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TemporaryAddCardA extends BaseActivity implements OnClickListener, MySpinerAdapter.IOnItemSelectListener{
	private ImageView iv_back;
	private TextView tv_bankType, tv_cardType, tv_openBankArea;
	private LinearLayout layout_openBankArea;
	private EditText et_cardNumber, et_openBank ,tv_cardName ,tv_ID;
	private RelativeLayout rl_choiceProvinces, rl_choiceCitys;
	private TextView tv_provinces, tv_citys;
	private Button bt_complete;
	private String bankCode, cardType, cardNumber, custname ,Cardholder ,IDNumber;
	private List<String> provinceNameList = new ArrayList<String>();
	private List<String> provinceCodeList = new ArrayList<String>();
	private List<String> cityNameList = new ArrayList<String>();
	private List<String> cityCodeList = new ArrayList<String>();
	private String provinceCode = "";
	private String cityCode = "";
	private String CHOICE_TAG;
	private String provinces, citys, location;
	private String openBankName, openBankAreaCode;
//	private ProgressDialog pd;
//	private String operationTag;
	private SpinerPopWindow mSpinerPopWindow;
	User user;
	private String operatorName;//负责人
	private String corpName;//法人
	private Button bt_save;
	private List<String> cardTypeList = new ArrayList<String>();
	private Intent intent;
	private static final int PROVINCE_LIST = 1;
	private static final int CITY_LIST = 2;
	private float xs;          //计算popwindow的位置
	private float ys;//计算popwindow的位置
	private float xsRole;//计算popwindow的位置
	private float ysRole;//计算popwindow的位置
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
	private ImageView iv_card;
	private ListView lv_card;
	private JSONObject jsonObject;
	private String bankValue="";
	private LinearLayout ll_bankType;
	private String operation; //从代扣那边进来绑卡的一个标识

	private String id;
	private String bankName;
	private String isPublic;
	private String cardNo;
	private String bankCode2;
	private String cardType2;
	private List<BankCardBean> bankCardBeenList = new ArrayList<BankCardBean>();
	private String custName;
	private String everyLimit;//单笔限额
	private String dailyLimit;//单日限额
	private TextView tvTitle;
	private EditText et_bank_phone;
	private String bankPhone;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temporary_addbankcard);
		initView();
		initData();
	}
	
	private void initData() {
		MyApplication.getInstence().addActivity(this);
		intent = getIntent();
		operation = intent.getStringExtra("operation");
		user = (User) ObjectSaveUtil.readObject(TemporaryAddCardA.this, "loginResult");
		operatorName = user.getOperatorName();
		corpName = user.getCorpName();
		if (operatorName != null && !operatorName.isEmpty()){
			tv_cardName.setText(operatorName);
			tv_ID.setText(user.getOperatorIdNum()==null ?"":user.getOperatorIdNum()); //负责人身份证号
		}else {
			tv_cardName.setText(corpName);
			tv_ID.setText(user.getCorpIdNum()==null ?"":user.getCorpIdNum()); //法人身份证号
		}
		//tv_ID.setText();
		cardTypeList.add("借记卡");
		cardTypeList.add("信用卡");

		try {
			getAllProvince();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		cardTypeList.add("储蓄卡");
		tvTitle.setText("填写银行卡信息");
	}

	@Override
	protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
		super.dataSuccess(json, tag, pageNumber);
		switch (tag){
			case "getDictAry":
				Gson gson = new Gson();
				BankCard bankCard = gson.fromJson(json.toString(), BankCard.class);
				List<BankCard.DataBean> bankCardDataList =  bankCard.getData();
				showBankCardType(bankCardDataList);
				break;
			case "getBindCardUnDeductions":
				analysisUnDeductionsData(json);
				break;
		}
	}

	/**
	 * 解析未签约银行卡列表数据
	 * @param jsonObject
     */
	private void analysisUnDeductionsData(JSONObject jsonObject){
		try {
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length() ; i++) {
				BankCardBean unDeductionsCard = new BankCardBean();
				JSONObject obj = (JSONObject) jsonArray.get(i);
				analysisCardList(unDeductionsCard, obj);
			}


			SPUtils2.putList(TemporaryAddCardA.this, "unCardList", bankCardBeenList);//将数据保存起来
			MyApplication.getInstence().activityFinish();
			intent = new Intent(TemporaryAddCardA.this, OpWithHoldTwoA.class);
			startActivity(intent);//启动intent
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			finish();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void analysisCardList(BankCardBean bankCardBean, JSONObject jsonObject){
		//银行卡id
		id = jsonObject.optString("id");
		//银行卡名称
		bankName = jsonObject.optString("bankName");
		//是否为对公账户
		isPublic = jsonObject.optString("isPublic");
		//银行卡号
		cardNo = jsonObject.optString("cardNo");
		//银行标识
		bankCode2 = jsonObject.optString("bankCode");
		//银行卡类型
		cardType2 = jsonObject.optString("cardType");
		//开户人姓名
		custName = jsonObject.optString("custName");
		//单笔限额
		everyLimit = jsonObject.optString("everyLimit");
		//单日限额
		dailyLimit = jsonObject.optString("dailyLimit");

		bankCardBean.setId(id);
		bankCardBean.setCardName(bankName);
		bankCardBean.setIsPublic(isPublic);
		bankCardBean.setCardnumber(cardNo);
		bankCardBean.setBankCode(bankCode2);
		bankCardBean.setCardType(cardType2);
		bankCardBean.setCustname(custName);
		bankCardBean.setEveryLimit(everyLimit);
		bankCardBean.setDailyLimit(dailyLimit);
		bankCardBeenList.add(bankCardBean);

	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_bankType = (TextView) findViewById(R.id.tv_bankType);
		iv_card = (ImageView) findViewById(R.id.iv_card);
		//tv_cardType = (TextView) findViewById(R.id.tv_cardType);
		//layout_cardType = (LinearLayout) findViewById(R.id.layout_cardType);
		tv_cardName = (EditText) findViewById(R.id.tv_cardName);
		et_cardNumber = (EditText) findViewById(R.id.et_cardNumber);
		bt_save = (Button) findViewById(R.id.bt_save);
		tv_openBankArea = (TextView) findViewById(R.id.tv_openBankArea);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ll_bankType = (LinearLayout) findViewById(R.id.ll_bankType);//选择银行容器
		layout_openBankArea = (LinearLayout) findViewById(R.id.layout_openBankArea);
		et_openBank = (EditText) findViewById(R.id.et_openBank);
		tv_ID = (EditText) findViewById(R.id.tv_ID);//银行卡卡号
		et_bank_phone = (EditText) findViewById(R.id.et_bank_phone); //银行预留手机号

		iv_back.setOnClickListener(this);
		//layout_cardType.setOnClickListener(this);
		bt_save.setOnClickListener(this);
		layout_openBankArea.setOnClickListener(this);
		ll_bankType.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			case R.id.ll_bankType: //选择银行
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				View view = getWindow().peekDecorView();
				if (null != view) {
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				try {
					if(jsonObject ==null) {
						jsonObject = new JSONObject();
					}
					jsonObject.put("dictType", "acct_bank_code");    //银行卡编号
					requestNet(Define.Cash, jsonObject,"getDictAry","0",true, true); //0 不代表什么
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
		//选择卡类型	
	/*	case R.id.layout_cardType:
			mSpinerPopWindow = new SpinerPopWindow(this);
			CHOICE_TAG = "1";
			mSpinerPopWindow.refreshData(cardTypeList, 0);
			mSpinerPopWindow.setItemListener(this);
			showSpinWindow(layout_cardType);
			break;*/

		case R.id.layout_openBankArea:
			choiceLoctionDialog();

			break;
		case R.id.bt_save:
			//bankCode = "ECITIC";
			Cardholder = tv_cardName.getText().toString();
			IDNumber = tv_ID.getText().toString();
			cardNumber = et_cardNumber.getText().toString();
			bankPhone = et_bank_phone.getText().toString();
			if (Cardholder == null || Cardholder.isEmpty()){
				ToastUtils.showToast(TemporaryAddCardA.this,"请输入持卡人！");
				return;
			}
			if ( RegexUtils.isIDCard(IDNumber) == false){
				ToastUtils.showToast(TemporaryAddCardA.this,"请输入正确的身份证号！");
				return;
			}
			if(bankValue.isEmpty()){
				ToastUtils.showToast(TemporaryAddCardA.this,"请选择银行！");
				return;
			}
			if (cardNumber == null || cardNumber.isEmpty()) {
				Toast.makeText(TemporaryAddCardA.this, "请输入银行卡号!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (cardNumber.length() < 15 || !RegexUtils.checkBankID(cardNumber)) {
				Toast.makeText(TemporaryAddCardA.this, "请输入正确的银行卡号!", Toast.LENGTH_SHORT).show();
				return;
			}
			if(bankPhone==null || bankPhone.isEmpty()){
				ToastUtils.showToast(TemporaryAddCardA.this,"请输入银行预留手机号！");
				return;
			}
			if(!RegexUtils.isMobileNO(bankPhone)){
				ToastUtils.showToast(TemporaryAddCardA.this,"请输入正确的银行预留手机号！");
				return;
			}
			//cardType = tv_cardType.getText().toString();
		/*	if ("请选择卡类型".equals(cardType)) {
				Toast.makeText(TemporaryAddCardA.this, "请选择卡类型!", Toast.LENGTH_SHORT).show();
				return;
			}else if ("借记卡".equals(cardType)) {
				cardType = Define.DEBIT_CARD;
				if (cardNumber.length() > 19 || cardNumber.length() < 16) {
					Toast.makeText(TemporaryAddCardA.this, "请输入正确的银行卡号!", Toast.LENGTH_SHORT).show();
					return;
				}
			}else if ("信用卡".equals(cardType)) {
				cardType = Define.CREDIT_CARD;
				if (cardNumber.length() > 19 || cardNumber.length() < 16) {
					Toast.makeText(TemporaryAddCardA.this, "请输入正确的银行卡号!", Toast.LENGTH_SHORT).show();
					return;
				}
			}*/
				
//			custname = tv_cardName.getText().toString();
			openBankName = et_openBank.getText().toString();
			if (openBankName == null || openBankName.isEmpty()){
				Toast.makeText(TemporaryAddCardA.this, "请输入开户行!", Toast.LENGTH_SHORT).show();
				return;
			}else {
				String str = EditFilterUtils.stringFilter(openBankName);
				if (!openBankName.equals(str)) {
					ToastUtils.showToast(TemporaryAddCardA.this, "开户支行只能输入中文、字母和数字！");
					return;
				}
			}
				openBankAreaCode = tv_openBankArea.getText().toString();
			if ("请选择".equals(openBankAreaCode)){
				Toast.makeText(TemporaryAddCardA.this, "请选择开户行所在地!", Toast.LENGTH_SHORT).show();
				return;
			}
			openBankAreaCode = cityCode;

			try {
				addBankCard();
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
				mSpinerPopWindow = new SpinerPopWindow(TemporaryAddCardA.this);
				CHOICE_TAG = "2";
				mSpinerPopWindow.refreshData(provinceNameList, 0);
				mSpinerPopWindow.setItemListener(TemporaryAddCardA.this);
				showSpinWindow(rl_choiceProvinces);

			}
		});

		rl_choiceCitys.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (provinceCode.isEmpty() || provinceCode == null) {
					Toast.makeText(TemporaryAddCardA.this, "请先选择省份！", Toast.LENGTH_SHORT).show();
					return;
				}
				mSpinerPopWindow = new SpinerPopWindow(TemporaryAddCardA.this);
				CHOICE_TAG = "3";
				mSpinerPopWindow.refreshData(cityNameList, 0);
				mSpinerPopWindow.setItemListener(TemporaryAddCardA.this);
				showSpinWindow(rl_choiceCitys);

			}
		});

		bt_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				provinces = tv_provinces.getText().toString();
				citys = tv_citys.getText().toString();
				if ("选择省".equals(provinces)) {
					Toast.makeText(TemporaryAddCardA.this, "请选择省！", Toast.LENGTH_SHORT).show();
					return;
				}

				if ("选择市".equals(citys)) {
					Toast.makeText(TemporaryAddCardA.this, "请选择市！", Toast.LENGTH_SHORT).show();
					return;
				}

				location = provinces + "/" + citys;
				tv_openBankArea.setText(location);
				dialog.dismiss();
			}
		});
		provinceCode = "";
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
		private void addBankCard() throws Exception {
//			pd = new CustomProgressDialog(TemporaryAddCardA.this, "正在加载中...", R.anim.frame_anim);
			cpd.show();
		
			JSONObject jsonObject = new JSONObject();
			try {
				
				jsonObject.put("operateType", Define.BANKCARD_ADD);
				jsonObject.put("bankCode", bankValue);
				jsonObject.put("tel",bankPhone); //银行预留手机号
			//	jsonObject.put("cardType", cardType);
				jsonObject.put("cardNo", cardNumber);
				jsonObject.put("custName", Cardholder);
				jsonObject.put("openBankName", openBankName);
				jsonObject.put("openBankAreaCode", openBankAreaCode);
				if (operatorName != null && !operatorName.isEmpty()){
					jsonObject.put("idnumber",IDNumber);//负责人身份证号
				}else {
					jsonObject.put("idnumber",IDNumber);//法人身份证号
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			String url = Define.URL + "acct/manageBankCard";
			
			
			SessionJsonObjectRequest addCardRequest = new SessionJsonObjectRequest(Method.POST,
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
									Toast.makeText(TemporaryAddCardA.this, "添加银行卡成功!", Toast.LENGTH_SHORT).show();
								/*	intent = new Intent(TemporaryAddCardA.this, BankCardListA.class);
									startActivity(intent);
									overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);*/
									if ("withHoldCardBind".equals(operation)){
										requestBindCardUnDeductions();//
									}else {
										setResult(2);
										finish();
									}
								} else if("46000".equals(responseCode)){
									cpd.dismiss();
									Toast.makeText(TemporaryAddCardA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
									intent = new Intent(TemporaryAddCardA.this, LoginA.class);
									startActivity(intent);
									overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								} else {
									cpd.dismiss();
									Toast.makeText(TemporaryAddCardA.this, message, Toast.LENGTH_SHORT).show();
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
							Toast.makeText(TemporaryAddCardA.this, R.string.netError, Toast.LENGTH_SHORT).show();
							Log.d("debug", "error = " + error.toString());

						}
					});
			// 解决重复请求后台的问题
			addCardRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
			addCardRequest.setTag("addCardRequest");
			// 将请求加入全局队列中
			MyApplication.getHttpQueues().add(addCardRequest);

		}

	/**
	 * 获取所有省份
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
								Toast.makeText(TemporaryAddCardA.this, message, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(TemporaryAddCardA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		provinceListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		provinceListRequest.setTag("bankCardProvince");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(provinceListRequest);

	}

	/**
	 * 根据选择的省份code获取对应的城市列表
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
			Toast.makeText(TemporaryAddCardA.this, "请先选择省份！", Toast.LENGTH_SHORT).show();
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
								Toast.makeText(TemporaryAddCardA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							cpd.dismiss();
							e.printStackTrace();
						}
						if (provinceCode.equals(provinceCode)){
							tv_citys.setText("选择市");
						}
					}
				}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(TemporaryAddCardA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		cityListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		cityListRequest.setTag("bankCardCity");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(cityListRequest);

	}
	MyPopWindow myPopWindow ;
	private void showBankCardType(final List<BankCard.DataBean> bankCardDataList) {
        if(bankCardDataList !=null && bankCardDataList.size()<=0){
			ToastUtils.showToast(this,"没有可选择银行！");
			return;
		}
		if(myPopWindow !=null && myPopWindow.isShow()){
			myPopWindow.dissmiss();
		}

//		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//		View v = getWindow().peekDecorView();
//		if (null != v) {
//			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//		}
		myPopWindow = new MyPopAbstract() {
			@Override
			protected void setMapSettingViewWidget(View view) {
				lv_card = (ListView) view.findViewById(R.id.lv_audit_status);
				int[] arr = new int[2];
				iv_card.getLocationOnScreen(arr);
				view.measure(0, 0);
				Rect frame = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
				xs = arr[0] + iv_card.getWidth() - view.getMeasuredWidth();
				ys = arr[1] + iv_card.getHeight();

				CommonAdapter<BankCard.DataBean> commonAdapter = new CommonAdapter<BankCard.DataBean>(TemporaryAddCardA.this,bankCardDataList,R.layout.admin_item_audit_status_pop) {
					@Override
					public void convert(ViewHolder holder,BankCard.DataBean o) {
						holder.setText(R.id.tv_text, o.getLabel());
					}
				};

				lv_card.setAdapter(commonAdapter);
				lv_card.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
						BankCard.DataBean dataBean = bankCardDataList.get(i);
						//银行值
						bankValue = dataBean.getValue();
						tv_bankType.setText(dataBean.getLabel());
						myPopWindow.dissmiss();
					}
				});
			}

			@Override
			protected int layout() {
				return R.layout.admin_audit_status_pop;
			}
		}.popupWindowBuilder(this).create();
		myPopWindow.keyCodeDismiss(true); //返回键关闭
		myPopWindow.showAtLocation(iv_card, Gravity.NO_GRAVITY,(int)xs,(int)ys);
//		myPopWindow.showAsDropDown(iv_card);
	}

	/**
	 *  请求绑卡后的汇缴代扣未签约银行卡列表
	 */
	private void requestBindCardUnDeductions(){
		String url = Define.URL+"acct/getUnDeductions";
		JSONObject jsonObject = new JSONObject();

		requestNet(url, jsonObject, "getBindCardUnDeductions", "", true, true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("addCardRequest");
		MyApplication.getHttpQueues().cancelAll("bankCardProvince");
		MyApplication.getHttpQueues().cancelAll("bankCardCity");
	}
}

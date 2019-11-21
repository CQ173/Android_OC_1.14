package com.huoniao.oc.outlets;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chinaums.commondhjt.model.Action;
import com.chinaums.commondhjt.model.ClientCallback;
import com.chinaums.commondhjt.model.DHJTCallBack;
import com.chinaums.commondhjt.service.DHJTManager;
import com.chinaums.commondhjt.utils.MyLog;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.alipay.PayResult;
import com.huoniao.oc.bean.ConfigureBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.PosSuccessA;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.NumberFormatUtils;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ShowPopupWindow;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeA extends BaseActivity implements OnClickListener, View.OnLongClickListener, BDLocationListener {
	/**
	 * 支付宝支付业务：入参app_id
	 */
	public static final String APPID = "2016092801994741";


	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;
	private static final String POS_PAY = "pos_pay";  //pos支付tag
	private ImageView iv_back, iv_showQRCode;
	private LinearLayout layout_bankCardRecharge;//银行卡充值
	private LinearLayout layout_alipayRecharge;//支付宝充值
	private EditText et_rechargeAmount;
	private String rechargeAmount;//充值金额
	private String userId = "";//用户id
	private String parentId = "";//父账号id
	private Intent intent;
	private int permissionFlag = 1; //判断是否授权sd卡操作 默认没有
	private int permissionSuccesss = 2;  //授权成功
	private int permissionF = 1;      //授权失败
	private ShowPopupWindow showPopupWindow = null;   //显示   二维码页面的弹出框
	private String resultInfo;// 同步返回需要验证的信息
	private ImageView iv_rechargeQrCode;
	private VolleyNetCommon volleyNetCommon;
	private Bitmap bitmap;
	private CustomProgressDialog pd;
	private ImageRequest imageRequest;
	private LinearLayout layout_pos;  //pos刷卡
	private TextView tv_firmRecharge; 		//确认充值
	private String posMinMount; //pos机充值最低限额
	List<ConfigureBean.ListEntity> list ;
	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {

					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					/**
					 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
					resultInfo = payResult.getResult();// 同步返回需要验证的信息


					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
						Toast.makeText(RechargeA.this, "支付成功", Toast.LENGTH_SHORT).show();
						try {
							returnSign();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						Toast.makeText(RechargeA.this, "支付失败", Toast.LENGTH_SHORT).show();
					}


					break;
				}
			}
		}
	};
	private TextView tv_minimum_remaining_recharge;
	private String token;

	private LocationClient mLocationClient;
	private String country, city, province;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_outlets_recharge);
		initLocation();
		initView();
		initData();
//		ExitApplication.getInstance().addActivity(this);
		checkPermission(this);  //校验权限


	}



	/**
	 * 定位
	 */
	private void initLocation() {

		// 定位初始化
		// 声明LocationClient类
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(this);// 注册定位监听接口

		/**
		 * 设置定位参数
		 */
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setScanSpan(2000);// 设置发起定位请求的间隔时间,ms
		option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
		option.setOpenGps(true);// 打开gps
//		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();

	}

	private void initData() {
		if(list !=null){
			list.clear();
		}
		list = (List<ConfigureBean.ListEntity>) ObjectSaveUtil.readObject(RechargeA.this,"ListEntity"); //配置集合
		if(list!=null) {
			for (ConfigureBean.ListEntity entity :
					list) {
				if (entity.getEpos_min_amount() != null) {
					posMinMount = entity.getEpos_min_amount();
					break;
				}
			}
		}
		String balanceStr =	getIntent().getStringExtra("balanceStr");//账户余额
		String minimumStr =	 getIntent().getStringExtra("minimumStr");//最低限额
		if(balanceStr!=null && minimumStr!=null) {
			getConvertResidualMinimum(minimumStr,balanceStr,tv_minimum_remaining_recharge);
		}
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_showQRCode = (ImageView) findViewById(R.id.iv_showQRCode);
		layout_bankCardRecharge = (LinearLayout) findViewById(R.id.layout_bankCardRecharge);
		tv_firmRecharge = (TextView) findViewById(R.id.tv_firmRecharge);
		tv_minimum_remaining_recharge = (TextView) findViewById(R.id.tv_minimum_remaining_recharge);//剩余最低充值额

//		layout_alipayRecharge = (LinearLayout) findViewById(R.id.layout_alipayRecharge);
		et_rechargeAmount = (EditText) findViewById(R.id.et_rechargeAmount);
		layout_pos = (LinearLayout) findViewById(R.id.layout_pos);
		InputFilter[] filters = {new CashierInputFilter()};
		et_rechargeAmount.setFilters(filters);
		iv_back.setOnClickListener(this);
		layout_bankCardRecharge.setOnClickListener(this);
		tv_firmRecharge.setOnClickListener(this);
		layout_pos.setOnClickListener(this);
//		layout_alipayRecharge.setOnClickListener(this);
		iv_showQRCode.setOnClickListener(this);
		volleyNetCommon = new VolleyNetCommon();

		pd = new CustomProgressDialog(this, "正在加载中...", R.drawable.frame_anim);
	}


	/**
	 * //剩余最低呢充值额
	 * @param minimum  最低限额
	 * @param balance  账户余额
	 * @param textView
	 */
	public void getConvertResidualMinimum(String minimum, String balance,TextView textView){
		double minimumRemaining = Double.valueOf(minimum) - Double.valueOf(balance);
		double minimumRemainingRecharge = minimumRemaining <= 0 ? 0 : minimumRemaining;
		textView.setText("待缴纳金额 "+NumberFormatUtils.formatDigits(minimumRemainingRecharge) + "元");  //剩余最低充值额
	}

	public int paymentOptionsFlag = 0;   // 1 表示 银行卡支付  2 表示 pos刷卡支付

	@Override
	protected void onResume() {
		super.onResume();
		tv_firmRecharge.setEnabled(true);
	}

	private boolean isShowOrNot = false;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back:
				setResult(102);  //刷新余额
				finish();
				break;
			//银行卡充值
//			case R.id.layout_bankCardRecharge:
//				paymentOptionsFlag = 1;
//				layout_bankCardRecharge.setBackgroundColor(getResources().getColor(R.color.gray));
//				layout_pos.setBackgroundColor(getResources().getColor(R.color.white));
//				break;
			//支付宝充值
//		case R.id.layout_alipayRecharge:
//
//
//			 try {
//				getSign();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}


//			break;
			//微信/支付宝缴费二维码
			case R.id.iv_showQRCode:
//				rechargeQRCodeDialog();
				intent = new Intent(RechargeA.this, PaymentCodeA.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;
			case R.id.layout_pos: //pos刷卡支付
				paymentOptionsFlag = 2;
				layout_bankCardRecharge.setBackgroundColor(getResources().getColor(R.color.white));
				layout_pos.setBackgroundColor(getResources().getColor(R.color.gray));
				break;
			case R.id.tv_firmRecharge: //确认充值
				rechargeAmount = et_rechargeAmount.getText().toString();
				if (rechargeAmount.isEmpty() || rechargeAmount == null) {
					Toast.makeText(RechargeA.this, "请输入缴款金额", Toast.LENGTH_SHORT).show();
					return;
				}

				if ("0".equals(rechargeAmount) || "0.0".equals(rechargeAmount) || "0.00".equals(rechargeAmount)) {
					Toast.makeText(RechargeA.this, "请缴纳大于0的金额", Toast.LENGTH_SHORT).show();
					return;
				}
//					switch (paymentOptionsFlag) {
//						case 0:
//							ToastUtils.showToast(RechargeA.this, "请选择缴款方式");
//							break;
//						case 1:
//							if (RepeatClickUtils.repeatClick()) {
//								if(isShowOrNot == false){
//									tv_firmRecharge.setEnabled(false);
//									bankPay();  //银行卡充值
//									isShowOrNot = true;
//									tv_firmRecharge.setBackgroundColor(getResources().getColor(R.color.gray));//设置按钮变成灰色
//									tv_firmRecharge.setOnClickListener(null);//设置不可点击
//								}else {
//									isShowOrNot  = false;
//								}
//							}
//							break;
//						case 2:
							if (isLocationEnabled() == true) {
								if (country == null || country.isEmpty()) {
									ToastUtils.showLongToast(RechargeA.this, "位置信息获取失败，请检查位置服务是否打开或是否打开O计的定位权限！");
									return;
								} else {
									if (!("中国".equals(country)) || "香港特别行政区".equals(city) || "澳门特别行政区".equals(city)
											|| "台湾省".equals(province)) {
										ToastUtils.showLongToast(RechargeA.this, "您目前不在可使用地区，epos只限在中国大陆地区使用！");
										return;
									}
									if(isShowOrNot == false){
										tv_firmRecharge.setEnabled(false);
										getToken();
										isShowOrNot = true;
										tv_firmRecharge.setBackgroundColor(getResources().getColor(R.color.gray));//设置按钮变成灰色
										tv_firmRecharge.setOnClickListener(null);//设置不可点击
									}else {
										isShowOrNot  = false;
									}
								}
							} else {
								ToastUtils.showToast(RechargeA.this, "需打开手机定位服务才能使用EPOS功能！");
							}
							break;
//					}
//				break;
			default:
				break;
		}
	}

	////银行卡充值
	private void bankPay() {
//		rechargeAmount = et_rechargeAmount.getText().toString();
//		if (rechargeAmount.isEmpty() || rechargeAmount == null) {
//			Toast.makeText(RechargeA.this, "请输入缴款金额", Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		if ("0".equals(rechargeAmount) || "0.0".equals(rechargeAmount) || "0.00".equals(rechargeAmount)) {
//			Toast.makeText(RechargeA.this, "请缴纳大于0的金额", Toast.LENGTH_SHORT).show();
//			return;
//		}


//       	 Intent intent = new Intent();
//          intent.setAction("android.intent.action.VIEW");
//          Uri content_url = Uri.parse("http://ecy.120368.com/chinapay/chinapayapi.jsp?WIDtotal_fee=" + rechargeAmount + "&user_id=" + userId + "&parent_id=" + parentId);
//          intent.setData(content_url);
//          startActivity(intent);

//			Toast.makeText(RechargeA.this, "正在建设中", Toast.LENGTH_SHORT).show();
//			intent = new Intent(RechargeA.this, BankCardRechargeA.class);
//			intent.putExtra("rechargeAmount", rechargeAmount);
//			startActivity(intent);
//			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

		User user = (User) ObjectSaveUtil.readObject(RechargeA.this, "loginResult");
		userId = user.getId();
		parentId = user.getParentId();
		if (parentId.isEmpty() || parentId == null) {
			parentId = userId;
		}
		if (userId != null && !userId.isEmpty()) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(Define.UNIONPAY_URL + rechargeAmount + "&user_id=" + userId + "&parent_id=" + parentId);
			intent.setData(content_url);
			startActivity(intent);
		} else {
			Toast.makeText(RechargeA.this, "用户信息验证失败!", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	/**
	 * 获取Token
	 */
	private void getToken() {
		String url = Define.URL + "fb/preventRepeatSubmit";
		JSONObject jsonObject = new JSONObject();

		requestNet(url, jsonObject, "preventRepeatSubmitend", "0", true, false);
	}

	//pos刷卡
	private void posPay() {
		rechargeAmount = et_rechargeAmount.getText().toString();

		if (rechargeAmount.isEmpty() || rechargeAmount == null) {
			Toast.makeText(RechargeA.this, "请输入缴款金额", Toast.LENGTH_SHORT).show();
			tv_firmRecharge.setEnabled(true);
			return;
		}

		if ("0".equals(rechargeAmount) || "0.0".equals(rechargeAmount) || "0.00".equals(rechargeAmount)) {
			Toast.makeText(RechargeA.this, "请缴纳大于0的金额", Toast.LENGTH_SHORT).show();
			tv_firmRecharge.setEnabled(true);
			return;
		}

		Double et_mount=Double.parseDouble(rechargeAmount);
		Double  min_mount=Double.parseDouble(posMinMount);

		if(min_mount>et_mount){
			Toast.makeText(this, "POS最低刷卡限额￥"+posMinMount, Toast.LENGTH_SHORT).show();
			tv_firmRecharge.setEnabled(true);
			return;
		}

		cpd.show();

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("tradeName","Epos缴款");  //交易名称
			jsonObject.put("tradeFee",rechargeAmount); // 交易金额
			jsonObject.put("tradeType","income"); //交易类型
			jsonObject.put("channelType","EPOSE");
			jsonObject.put("preventRepeatToken",token);//防重复请求token
			JsonObjectRequest jsonPOS = volleyNetCommon.jsonObjectRequest(Method.POST, Define.URL + "acct/createTrade ", jsonObject, new VolleyAbstract(this) {

				@Override
				public void volleyResponse(Object o) {

				}

				@Override
				public void volleyError(VolleyError volleyError) {
					Toast.makeText(RechargeA.this, R.string.netError, Toast.LENGTH_SHORT).show();
				}

				@Override
				protected void netVolleyResponese(JSONObject json) {
					//处理成功响应结果
					//订单编号
					String flowId = json.optString("flowId");
					String cshnepos = json.optString("epos_mch_alias");
					User loginResult = (User) ObjectSaveUtil.readObject(RechargeA.this, "loginResult");
					String loginName = loginResult.getUserCode();	//用户名

					ContentValues cv = new ContentValues();
					cv.put("shopname", cshnepos);   //jhe     123456789012 生产   ,      app2       emp2  测试  ,       cshn      cshnepos
					cv.put("account",  loginName);  //这里需要改成自己的 员工编号（用户substringId） TODO  Pos
					loginSZ(cv,rechargeAmount, flowId);
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
			}, POS_PAY, false);

			volleyNetCommon.addQueue(jsonPOS);


		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


	@Override
	protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
		super.dataSuccess(json, tag, pageNumber);
		switch (tag){
			case "preventRepeatSubmitend":
				try {
					JSONObject jsonObject = json.getJSONObject("data");
					token = jsonObject.getString("preventRepeatToken");
					if (token != null){
						SPUtils2.putString(RechargeA.this, "preventRepeatToken", token);

						posPay();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
		}
	}


	@Override
	protected void onStart() {
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		super.onStart();
	}




	private void rechargeQRCodeDialog() {
		String url = Define.URL + "acct/getPayCode";
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.recharge_qrcode_dialog, null);
		ImageView iv_dismiss = (ImageView) view.findViewById(R.id.iv_dismiss);
		iv_rechargeQrCode = (ImageView) view.findViewById(R.id.iv_rechargeQrCode);
		loadImageByVolley(url, iv_rechargeQrCode);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(view);
		final AlertDialog dialog = builder.create();//获取dialog

		dialog.show();

		iv_dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		iv_rechargeQrCode.setOnLongClickListener(this);
	}

	//加载充值二维码
	private void loadImageByVolley(String url, final ImageView image) {
		pd.show();
		Map<String, String> map = new HashMap<>();

		//当前线程是主线程
		//获取图片
		//错误处理
		imageRequest = volleyNetCommon.imageRequest(url, new VolleyAbstract(this) {
			@Override
			protected void netVolleyResponese(JSONObject json) {

			}

			@Override
			protected void PdDismiss() {

			}
			@Override
			protected void errorMessages(String message) {
				super.errorMessages(message);
				Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void volleyResponse(final Object o) {
				//当前线程是主线程

				bitmap = (Bitmap) o;
				//获取图片
				image.setImageBitmap(bitmap);

				pd.dismiss();
			}

			@Override
			public void volleyError(VolleyError volleyError) {

				//错误处理
				Toast.makeText(RechargeA.this, R.string.netError, Toast.LENGTH_SHORT).show();
				pd.dismiss();
			}
		}, 0, 0, "rechargeQrCode");

		volleyNetCommon.addQueue(imageRequest);  //添加到队列

	}

	/**
	 * 获取sign信息
	 *
	 * @throws Exception
	 */
	private void getSign() throws Exception {
//		pd.show();
		rechargeAmount = et_rechargeAmount.getText().toString();
		if ("".equals(rechargeAmount) || rechargeAmount == null) {
			Toast.makeText(RechargeA.this, "请输入缴款金额", Toast.LENGTH_SHORT).show();
			return;
		}
		if ("0".equals(rechargeAmount) || "0.0".equals(rechargeAmount) || "0.00".equals(rechargeAmount)) {
			Toast.makeText(RechargeA.this, "请缴纳大于0的金额", Toast.LENGTH_SHORT).show();
			return;
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("subject", "O计平台缴款");
		jsonObject.put("body", "支付宝缴款");
		jsonObject.put("price", rechargeAmount);
		String url = Define.URL + "acct/orderSign";
//		final List<OutletsMyLogBean> oltLogList = new ArrayList<OutletsMyLogBean>();
		SessionJsonObjectRequest signRequest = new SessionJsonObjectRequest(Method.POST,
				url, jsonObject, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d("debug", "response =" + response.toString());
				try {
					String code = response.getString("code");
					String message = response.getString("msg");
					if ("0".equals(code)) {
						final String orderInfo = response.optString("data");
//								pd.dismiss();
						Runnable payRunnable = new Runnable() {

							@Override
							public void run() {
								PayTask alipay = new PayTask(RechargeA.this);
								Map<String, String> result = alipay.payV2(orderInfo, true);
								Log.i("msp", result.toString());

								Message msg = new Message();
								msg.what = SDK_PAY_FLAG;
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						};

						Thread payThread = new Thread(payRunnable);
						payThread.start();

					} else if ("46000".equals(code)) {
//								pd.dismiss();
						Toast.makeText(RechargeA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
						intent = new Intent(RechargeA.this, LoginA.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					} else {
//								pd.dismiss();
						Toast.makeText(RechargeA.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(RechargeA.this, R.string.netError, Toast.LENGTH_SHORT).show();
//						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

			}
		});
		// 解决重复请求后台的问题
		signRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		signRequest.setTag("signRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(signRequest);


	}

	/**
	 * 从服务器获取的回调信息
	 *
	 * @throws Exception
	 */
	private void returnSign() throws Exception {
//		pd.show();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultInfo", resultInfo);

		String url = Define.URL + "acct/returnSign";
//		final List<OutletsMyLogBean> oltLogList = new ArrayList<OutletsMyLogBean>();
		SessionJsonObjectRequest returnInfoRequest = new SessionJsonObjectRequest(Method.POST,
				url, jsonObject, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d("debug", "response =" + response.toString());
				try {
					String code = response.getString("code");
					String message = response.getString("msg");
					boolean retrunInfo = response.optBoolean("data");
					if ("0".equals(code) && retrunInfo == true) {
						intent = new Intent(RechargeA.this, WalletA.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//								refreshBalance();

//								finish();

					} else if ("46000".equals(code)) {
//								pd.dismiss();
						Toast.makeText(RechargeA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
						intent = new Intent(RechargeA.this, LoginA.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					} else {
//								pd.dismiss();
						Toast.makeText(RechargeA.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(RechargeA.this, R.string.netError, Toast.LENGTH_SHORT).show();
//						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

			}
		});
		// 解决重复请求后台的问题
		returnInfoRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		returnInfoRequest.setTag("returnInfoRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(returnInfoRequest);


	}



	/*
   权限校验
    */
	public  void checkPermission(Activity activity) {

		if (Build.VERSION.SDK_INT >= 23) {
			int readSdcard = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
			int writeSdcard = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			int mountUnmount = activity.checkSelfPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
			int requestCode = 0;
			ArrayList<String> permissions = new ArrayList<>();

			if (readSdcard != PackageManager.PERMISSION_GRANTED) {
				requestCode = 1;
				permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			}
			if (writeSdcard != PackageManager.PERMISSION_GRANTED) {
				requestCode = 2;
				permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}
			if (mountUnmount != PackageManager.PERMISSION_GRANTED) {
				requestCode = 3;
				permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
			}

			if (requestCode > 0) {
				String[] permission = new String[permissions.size()];
				activity.requestPermissions(permissions.toArray(permission), requestCode);
				return;
			}

		}else{
			permissionFlag = permissionSuccesss;   //改变授权状态  如果低6.0系统的不至于 保存不了
		}
	}

	/**
	 * 判断是否打开了系统定位服务
	 * @return
     */
	public boolean isLocationEnabled() {
		int locationMode = 0;
		String locationProviders;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		     try {
			         locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
			      } catch (Settings.SettingNotFoundException e) {
			            e.printStackTrace();
			            return false;
			         }
		         return locationMode != Settings.Secure.LOCATION_MODE_OFF;
		     } else {
		         locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		         return !TextUtils.isEmpty(locationProviders);
		     }
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1) {
			if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
				permissionFlag = permissionSuccesss;
				//权限申请授权成功
			}else if(grantResults[1]==PermissionChecker.PERMISSION_GRANTED){
				permissionFlag = permissionSuccesss;
			}else if(grantResults[2]==PermissionChecker.PERMISSION_GRANTED){
				permissionFlag = permissionSuccesss;
			}else {
				permissionFlag = permissionF;
				Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
			}
		}
	}
	@Override
	public boolean onLongClick(View view) {
		//弹出popwindow 确认保存图片
		if (showPopupWindow == null) {
			showPopupWindow = new ShowPopupWindow();
		}
		if (bitmap != null) {
			showPopupWindow.showPopuWindowcode(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, bitmap);

		}
		return true;
	}



	private void loginSZ(ContentValues cv, final String rechargeAmount , final String flowId) {
		cpd.show();
		MyLog.d("dhjt", "登录参数" + cv);
		DHJTManager.getInstance().loginSZ(cv, new ClientCallback() {

			@Override
			public void success(String result) {
				// TODO Auto-generated method stub
				//	Toast.makeText(RechargeA.this, "登录成功", Toast.LENGTH_SHORT).show();
				cpd.dismiss();
				//再加一个 运单号
				DHJTManager.getInstance().payInQuick(Action.PAYType_Card, flowId,rechargeAmount, new DHJTCallBack() {
					public void querySuccess(String result) {
						// TODO Auto-generated method stub
						Log.i("lg", "查询成功返回值：" + result.toString());

					}

					public void queryFail(String msg) {
						// TODO Auto-generated method stub
						Log.i("lg", "查询失败返回值：" + msg);
//					tv_show.setText(msg);

					}

					String payStatus = "success";  //交易状态
					@Override
					public void onSuccess(Bundle bundle) {
//						tv_firmRecharge.setEnabled(true);
						// TODO Auto-generated method stub
						Log.i("lg", "查询成功返回值：" + bundle.toString());
						payStatus = bundle.getString("payStatus");
						if (payStatus != null) {
							if (payStatus == "success") {
								Intent intent = new Intent(RechargeA.this, PosSuccessA.class);
								intent.putExtra("transactionStatus", "success");
								intent.putExtra("rechargeAmount", rechargeAmount);
								startActivity(intent);
							}
						}
					}

					@Override
					public void onNetError() {
//						tv_firmRecharge.setEnabled(true);
						Toast.makeText(RechargeA.this, R.string.netError, Toast.LENGTH_LONG).show();
					}

					public void onFail(final Bundle bundle) {
//						tv_firmRecharge.setEnabled(true);
						Log.i("aa",bundle.toString());
						String error =	bundle.getString("msg");
						if(error!=null) {
							Intent intent = new Intent(RechargeA.this, PosSuccessA.class);
							intent.putExtra("transactionStatus", "error");
							intent.putExtra("rechargeAmount", rechargeAmount);
							intent.putExtra("er",error);
							intent.putExtra("flowId",flowId);
							startActivity(intent);
							return;

						}
						//Bundle[{msg=用户取消交易, code=2}]
					}
				});

			}

			@Override
			public void onNetError() {
//			tv_firmRecharge.setEnabled(true);
				cpd.dismiss();
				Toast.makeText(RechargeA.this, R.string.netError, Toast.LENGTH_LONG).show();
			}

			@Override
			public void fail(final String result) {
//				tv_firmRecharge.setEnabled(true);
				cpd.dismiss();
				Toast.makeText(RechargeA.this, result, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("signRequest");
		MyApplication.getHttpQueues().cancelAll("returnInfoRequest");
		RequestQueue requestQueue = volleyNetCommon.getRequestQueue();
		if (requestQueue != null) {
			requestQueue.cancelAll("rechargeQrCode");
			requestQueue.cancelAll(POS_PAY);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap != null) {
			bitmap.recycle();
		}
	}


	@Override
	protected boolean onkeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(102);  //返回更新余额数目
			finish();
		}
		return super.onkeyDown(keyCode, event);
	}


	@Override
	public void onReceiveLocation(BDLocation bdLocation) {
		if (bdLocation == null) {
			return;
		}
		country = bdLocation.getCountry();
		city = bdLocation.getCity();
		province = bdLocation.getProvince();
//		ToastUtils.showLongToast(RechargeA.this, country);
		Log.d("country", country);

	}
}
package com.huoniao.oc.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

public class UserListDetailsA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private TextView tv_title;
	private TextView tv_userName, tv_name, tv_mobile, tv_email, tv_userType, tv_role,
			tv_orgName, tv_area, tv_jurisArea, tv_address, tv_corpName, tv_corpIdNum,
			tv_corpMobile, tv_contacts, tv_contactsMobile, tv_master, tv_contactPhone, tv_see_licence,
			tv_see_cardFornt, tv_see_cardRear, tv_creatTime, tv_auditingStatus,
			tv_minQuota, tv_orgTypeName, tv_firstAgent, tv_agentType, tv_fuZeRenName,
			tv_fuZeRenMobile, tv_fuZeRenIdNumber;
	private LinearLayout layout_minQuota;//最低限额设置
	private LinearLayout layout_notAudited;//未审核用户区域
	private EditText et_auditingReason, et_minQuota;
	private TextView tv_auditReason;
	private TextView tv_auditState;
	private TextView tv_operateTime;//操作时间
	private String minQuota; //最低限额
	private Button bt_passed, bt_noPassed;
	private LinearLayout layout_pass, layout_noPass;
	private ImageView iv_businessLicense, iv_identificationFront, iv_identificationOpposite,
			iv_stationContactFrist, iv_stationContactEnd, iv_articleDeposit, iv_nianJianCertificate,
			iv_paySystemCertificate, iv_fuzerenIdFront, iv_fuzerenIdOpposite;
	private LinearLayout layout_fuZeRenInfo, layout_shouQuanShu, layout_fuzerenIdFront, layout_fuzerenIdOpposite,
			layout_fuZeRenCertificates;
	private LinearLayout layout_auditedContent;
	private LinearLayout layout_noRelieveContent;
	private LinearLayout layout_auditingButon;
	private Intent intent;
	private String userId;//用户id
	String loginName, name, mobile, balance, minimum, userType, auditState, auditReason, roleNames,
			email, remarks, area_name, jurisArea, orgName, orgType, corpName, corpMobile, corpIdNum,
			address, master, contactPhone, winNumber, agent, agentType, operatorName, operatorMobile,
			operatorIdNum, agentCompanyName, corp_licence, corp_card_fornt, corp_card_rear, staContIndexSrc,
			staContLastSrc, staDepositSrc, staDepInspSrc, operatorCardforntSrc, operatorCardrearSrc,
			fareAuthorizationSrc, officeId;
	private static final int SCALE = 2;//照片放大缩小比例
//	private ProgressDialog cpd;
	private Bitmap bmCertificate, bmIdCardPositive, bmIdCardOpposite, bmContractFrist, bmContractLast,
				bmDepositSrc, bmDepInspSrc, bmOptPositive, bmOptOpposite, bmAuthorization;

	private String isBindQQ;
	private String isBindWx;

	private static final int auditParameter = 1;
	private JSONObject auditPrtObj;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case auditParameter: {

					auditPrtObj = (JSONObject) msg.obj;

					break;

				}
			}
		}
	};


	private final String  corpLicenceSrcTag ="corpLicenceSrc";//营业执照路径标识
	private final String  corpCardforntSrcTag ="corpCardforntSrc"; //法人身份证正面路径标识
	private final String  corpCardrearSrcTag  ="corpCardrearSrc";//法人身份证反面路径标识
	private final String  staContIndexSrcTag  ="staContIndexSrc";//车站合同首页路径标识
	private final String  staContLastSrcTag  ="staContLastSrc";//车站合同末页路径标识
	private final String  operatorCardforntSrcTag ="operatorCardforntSrc";//负责人身份证正面路径标识
	private final String  operatorCardrearSrcTag ="operatorCardrearSrc";//负责人身份证反面路径标识
	private final String  fareAuthorizationSrcTag="fareAuthorizationSrc";//票款汇缴授权书标识
	private File staContIndexSrcFile, staContLastSrcFile, operatorCardforntSrcFile,
			operatorCardrearSrcFile, fareAuthorizationSrcFile, corpLicenceSrcFile,
			corpCardforntSrcFile, corpCardrearSrcFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_useraudit);
		initView();
		initData();
	}
	
	private void initData() {
		intent = getIntent();
		userId = intent.getStringExtra("userId");
//		auditState = intent.getStringExtra("auditState");
		try {
			getUserInfo(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}


		/*userType = intent.getStringExtra("userType");
		if (Define.SYSTEM_MANAG_USER.equals(userType)) {
			tv_userType.setText("系统管理员");
		}else if (Define.ORGANIZATION_USER.equals(userType)) {
			tv_userType.setText("机构用户");
		}else {
			tv_userType.setText("个人用户");
		}

		tv_role.setText(intent.getStringExtra("roleName"));
		tv_orgName.setText(intent.getStringExtra("orgName"));
		tv_area.setText(intent.getStringExtra("area"));
		tv_jurisArea.setText("");
		tv_address.setText(intent.getStringExtra("address"));
		tv_corpName.setText(intent.getStringExtra("corpName"));
		tv_corpIdNum.setText(intent.getStringExtra("corpIdNum"));
		tv_corpMobile.setText(intent.getStringExtra("corpMobile"));
		tv_master.setText(intent.getStringExtra("master"));
		tv_contactPhone.setText(intent.getStringExtra("contactPhone"));
		
		auditReason = intent.getStringExtra("auditReason");
		if (!auditReason.isEmpty() && auditReason != null) {
			et_auditingReason.setText(auditReason);
		}
		
		tv_creatTime.setText(intent.getStringExtra("creatTime"));

		officeId = intent.getStringExtra("officeId");
		officeType = intent.getStringExtra("officeType");
		if (Define.TRAINSTATION.equals(officeType)) {
			layout_minQuota.setVisibility(View.GONE);
			tv_orgTypeName.setText("火车站名称");
		}
		corp_licence = intent.getStringExtra("corp_licence");
		corp_card_fornt = intent.getStringExtra("corp_card_fornt");
		corp_card_rear = intent.getStringExtra("corp_card_rear");
		if (Define.OUTLETS_NORMAL.equals(intent.getStringExtra("auditState"))) {
			tv_auditingStatus.setText("审核通过");
			layout_notAudited.setVisibility(View.GONE);
			tv_minQuota.setText("账户最低限额");
			et_minQuota.setText(intent.getStringExtra("minimum"));
			et_minQuota.setKeyListener(null);//如果已处理就让它不再能输入组队限额
			et_auditingReason.setKeyListener(null);//如果已处理就让它不再能输入审核理由
		}else if (Define.OUTLETS_PENDIG_AUDIT.equals(intent.getStringExtra("auditState"))) {
			tv_auditingStatus.setText("待审核");
		}else if (Define.OUTLETS_NOTPASS.equals(intent.getStringExtra("auditState"))){
			tv_auditingStatus.setText("审核不通过");
			et_auditingReason.setKeyListener(null);//如何已处理就让它不再能输入审核理由
			layout_notAudited.setVisibility(View.GONE);
			layout_minQuota.setVisibility(View.GONE);
		}else if (Define.QIANDIANDAI_NOTPASS.equals(intent.getStringExtra("auditState"))){
			tv_auditingStatus.setText("审核不通过");
			et_auditingReason.setKeyListener(null);//如何已处理就让它不再能输入审核理由
			layout_notAudited.setVisibility(View.GONE);
			layout_minQuota.setVisibility(View.GONE);
		}*/
		

	}

	private void initView() {
//		cpd = new CustomProgressDialog(UserListDetailsA.this, "正在加载中...", R.anim.frame_anim);
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_userName = (TextView) findViewById(R.id.tv_userName);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_userType = (TextView) findViewById(R.id.tv_userType);
		tv_role = (TextView) findViewById(R.id.tv_roleName);
		tv_orgName = (TextView) findViewById(R.id.tv_orgName);
		tv_area = (TextView) findViewById(R.id.tv_area);
		tv_jurisArea = (TextView) findViewById(R.id.tv_ruleArea);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_firstAgent = (TextView) findViewById(R.id.tv_firstAgent);
		tv_agentType = (TextView) findViewById(R.id.tv_firstAgentType);
		tv_corpName = (TextView) findViewById(R.id.tv_corpName);
		tv_corpIdNum = (TextView) findViewById(R.id.tv_corpIdCardNumber);
		tv_corpMobile = (TextView) findViewById(R.id.tv_corpMobile);
		tv_contacts = (TextView) findViewById(R.id.tv_contacts);
		tv_contactsMobile = (TextView) findViewById(R.id.tv_contactsMobile);
		tv_fuZeRenName = (TextView) findViewById(R.id.tv_fuZeRenName);
		tv_fuZeRenMobile  = (TextView) findViewById(R.id.tv_fuZeRenMobile);
		tv_fuZeRenIdNumber = (TextView) findViewById(R.id.tv_fuZeRenIdNumber);
		et_minQuota = (EditText) findViewById(R.id.et_minimum);
		InputFilter[] filters={new CashierInputFilter()};
		et_minQuota.setFilters(filters);
		iv_businessLicense = (ImageView) findViewById(R.id.iv_businessLicense);
		iv_identificationFront = (ImageView) findViewById(R.id.iv_identificationFront);
		iv_identificationOpposite = (ImageView) findViewById(R.id.iv_identificationOpposite);
		iv_stationContactFrist = (ImageView) findViewById(R.id.iv_stationContactFrist);
		iv_stationContactEnd = (ImageView) findViewById(R.id.iv_stationContactEnd);
		iv_articleDeposit = (ImageView) findViewById(R.id.iv_articleDeposit);
		iv_nianJianCertificate = (ImageView) findViewById(R.id.iv_nianJianCertificate);
		iv_paySystemCertificate = (ImageView) findViewById(R.id.iv_paySystemCertificate);
		iv_fuzerenIdFront = (ImageView) findViewById(R.id.iv_fuzerenIdFront);
		iv_fuzerenIdOpposite = (ImageView) findViewById(R.id.iv_fuzerenIdOpposite);
		layout_fuZeRenInfo = (LinearLayout) findViewById(R.id.layout_fuZeRenInfo);
		layout_shouQuanShu = (LinearLayout) findViewById(R.id.layout_shouQuanShu);
		layout_fuzerenIdFront = (LinearLayout) findViewById(R.id.layout_fuzerenIdFront);
		layout_fuzerenIdOpposite = (LinearLayout) findViewById(R.id.layout_fuzerenIdOpposite);
		layout_fuZeRenCertificates = (LinearLayout) findViewById(R.id.layout_fuZeRenCertificates);
		et_auditingReason = (EditText) findViewById(R.id.et_auditReason);
		tv_auditReason = (TextView) findViewById(R.id.tv_auditReason);
		tv_auditState = (TextView) findViewById(R.id.tv_auditState);
		tv_operateTime = (TextView) findViewById(R.id.tv_operateTime);
		layout_pass = (LinearLayout) findViewById(R.id.layout_pass);
		layout_noPass = (LinearLayout) findViewById(R.id.layout_noPass);
		layout_auditedContent = (LinearLayout) findViewById(R.id.layout_auditedContent);
		layout_noRelieveContent = (LinearLayout) findViewById(R.id.layout_noRelieveContent);
		layout_auditingButon = (LinearLayout) findViewById(R.id.layout_auditingButon);
		iv_businessLicense.setOnClickListener(this);
		iv_identificationFront.setOnClickListener(this);
		iv_identificationOpposite.setOnClickListener(this);
		iv_stationContactFrist.setOnClickListener(this);
		iv_stationContactEnd.setOnClickListener(this);
		iv_articleDeposit.setOnClickListener(this);
		iv_nianJianCertificate.setOnClickListener(this);
		iv_fuzerenIdFront.setOnClickListener(this);
		iv_fuzerenIdOpposite.setOnClickListener(this);
		iv_paySystemCertificate.setOnClickListener(this);

		/*tv_master = (TextView) findViewById(tv_master);
		tv_contactPhone = (TextView) findViewById(R.id.tv_contactPhone);
		tv_see_licence = (TextView) findViewById(tv_see_licence);
		tv_see_cardFornt = (TextView) findViewById(R.id.tv_see_cardFornt);
		tv_see_cardRear = (TextView) findViewById(R.id.tv_see_cardRear);
		tv_creatTime = (TextView) findViewById(tv_creatTime);
		layout_minQuota = (LinearLayout) findViewById(R.id.layout_minQuota);
		layout_notAudited = (LinearLayout) findViewById(R.id.layout_notAudited);
		tv_auditingStatus = (TextView) findViewById(R.id.tv_auditingStatus);

		tv_orgTypeName = (TextView) findViewById(tv_orgTypeName);

		bt_passed = (Button) findViewById(R.id.bt_passed);
		bt_noPassed = (Button) findViewById(R.id.bt_noPassed);*/

		iv_back.setOnClickListener(this);
		layout_pass.setOnClickListener(this);
		layout_noPass.setOnClickListener(this);
		/*tv_see_licence.setOnClickListener(this);
		tv_see_cardFornt.setOnClickListener(this);
		tv_see_cardRear.setOnClickListener(this);
		bt_passed.setOnClickListener(this);
		bt_noPassed.setOnClickListener(this);*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		//营业执照
		case R.id.iv_businessLicense:
			if (corpLicenceSrcFile != null) {
				myEnlargeImage(corpLicenceSrcFile);
			}
			break;

		//法人身份证正面
		case R.id.iv_identificationFront:
			if (corpCardforntSrcFile != null) {
				myEnlargeImage(corpCardforntSrcFile);
			}
			break;

		//法人身份证反面
		case R.id.iv_identificationOpposite:
			if (corpCardrearSrcFile != null) {
				myEnlargeImage(corpCardrearSrcFile);
			}
			break;
		//车站合同首页
		case R.id.iv_stationContactFrist:
			if (staContIndexSrcFile != null) {
				myEnlargeImage(staContIndexSrcFile);
			}
			break;
		//车站合同尾页
		case R.id.iv_stationContactEnd:
			if (staContLastSrcFile != null) {
				myEnlargeImage(staContLastSrcFile);
			}
			break;

	/*	//押金条
		case R.id.iv_articleDeposit:
			enlargeImage(UserListDetailsA.this, staDepositSrc, iv_back);
			break;

		//年检证书
		case R.id.iv_nianJianCertificate:
			enlargeImage(UserListDetailsA.this, staDepInspSrc, iv_back);
			break;*/

		//负责人身份证正面
		case R.id.iv_fuzerenIdFront:
			if (operatorCardforntSrcFile != null) {
				myEnlargeImage(operatorCardforntSrcFile);
			}
			break;
		//负责人身份证反面
		case R.id.iv_fuzerenIdOpposite:
			if (operatorCardrearSrcFile != null) {
				myEnlargeImage(operatorCardrearSrcFile);
			}
			break;
		//票款汇缴授权书
		case R.id.iv_paySystemCertificate:
			if (fareAuthorizationSrcFile != null) {
				myEnlargeImage(fareAuthorizationSrcFile);
			}
			break;

		//审核通过
		case R.id.layout_pass:
			userReview(Define.OUTLETS_NORMAL);
			
			break;
		//审核不通过
		case R.id.layout_noPass:
			userReview(Define.OUTLETS_NOTPASS);
			
			break;	
		default:
			break;
		}
		
	}

	/**
	 * 放大查看图片
	 * @param
	 */
	private void myEnlargeImage(final File file){
//        final Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() * SCALE, bitmap.getHeight() * SCALE);
		MyPopWindow myPopWindow = new MyPopAbstract() {
			@Override
			protected void setMapSettingViewWidget(View view) {
				PhotoView iv_enlarge = (PhotoView) view.findViewById(R.id.iv_enlarge);
//                iv_enlarge.setImageBitmap(newBitmap);
				try {
					Glide.with(UserListDetailsA.this).load(file).into(iv_enlarge);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			@Override
			protected int layout() {
				return R.layout.pop_lookimg;
			}
		}.popWindowTouch(UserListDetailsA.this).showAtLocation(iv_back, Gravity.CENTER,0,0);
	}
	
	/*private void documentImageDialog(Context context, String imgUrl){
//		cpd.show();
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.documentimage_dialog, null);
		documentImage = (ImageView) view.findViewById(R.id.documentImage);
		try {
			getDocumentImage(imgUrl, documentImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		documentImage.setOnTouchListener(new TouchListener()); 
//		String imageView = loadImageByVolley(serverUrl, imgUrl, documentImage);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setView(view);
		
//		documentImage.setImageBitmap(bitmap);
		final AlertDialog dialog = builder.create();//获取dialog   	
	    dialog.show();
	  
	   
	}*/
	
	

	
	private void getDocumentImage(String imgUrl, final ImageView imageView) throws Exception {
		cpd.show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("imageSrc", imgUrl);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		SessionJsonObjectRequest documentBitmapRequest = new SessionJsonObjectRequest(Method.POST,
				Define.URL+"fb/getImgBase64BySrc", jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("userInfo", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
							String imgString = 	response.optString("data");
							Bitmap licenceBitmap = Base64ConvertBitmap.base64ToBitmap(imgString);	
							imageView.setImageBitmap(licenceBitmap);
							cpd.dismiss();
							 

							} else if("46000".equals(responseCode)){
								cpd.dismiss();
								Toast.makeText(UserListDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UserListDetailsA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(UserListDetailsA.this, "无图片信息！", Toast.LENGTH_SHORT).show();
								Log.d("debug", message);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						cpd.dismiss();
						Toast.makeText(UserListDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

					}
				});
		// 解决重复请求后台的问题
		documentBitmapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		documentBitmapRequest.setTag("documentBitmap");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(documentBitmapRequest);

	}


	
	private void userReview(String reviewStatus){

		minQuota = et_minQuota.getText().toString();
		auditReason = et_auditingReason.getText().toString().trim();
		if (auditReason == null || auditReason.isEmpty()) {
			Toast.makeText(UserListDetailsA.this, "请输入审核理由", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			auditPrtObj.put("userId", userId);
			auditPrtObj.put("auditState", reviewStatus);
			auditPrtObj.put("minimum", minQuota);
			auditPrtObj.put("auditReason", auditReason);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		cpd.show();


		/*JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("userType", userType);
			jsonObject.put("mobile", mobile);
			jsonObject.put("minimum", minQuota);
			jsonObject.put("loginName", userName);
			jsonObject.put("officeId", officeId);
			jsonObject.put("officeType", officeType);
			jsonObject.put("auditState", reviewStatus);
			jsonObject.put("auditReason", auditReason);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
*/
		
		SessionJsonObjectRequest reviewUserRequest = new SessionJsonObjectRequest(Method.POST,
				Define.URL+"user/auditUser", auditPrtObj, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("data", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								cpd.dismiss();
								Toast.makeText(UserListDetailsA.this, "审核成功!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UserListDetailsA.this, UserManageA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

							} else if("46000".equals(responseCode)){
								cpd.dismiss();
								Toast.makeText(UserListDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UserListDetailsA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(UserListDetailsA.this, "系统错误！", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						cpd.dismiss();
						Toast.makeText(UserListDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

					}
				});
		// 解决重复请求后台的问题
		reviewUserRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		reviewUserRequest.setTag("reviewUserRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(reviewUserRequest);
		
	}

	/**
	 * 获取用户信息
	 *
	 * @throws Exception
	 */
	private void getUserInfo(String id) throws Exception {
		cpd.show();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", id);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String url = Define.URL + "user/queryUserInfo";
		SessionJsonObjectRequest userInfoRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						if (response == null) {
							Toast.makeText(UserListDetailsA.this, "服务器数据异常！", Toast.LENGTH_SHORT).show();
							cpd.dismiss();
							return;
						}
						Log.d("userInfo", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								cpd.dismiss();
								Gson gson = new Gson();
								User allUser = gson.fromJson( response.toString(), User.class);  //解析所有获取的数据

								int num = response.getInt("size");
								JSONArray jsonArray = response.getJSONArray("data");

								num = Math.min(num, jsonArray.length());
								User user = new User();

								for (int i = 0; i < num; i++) {

									JSONObject userObj = (JSONObject) jsonArray.get(i);
									String id = userObj.optString("id");// 用户ID
									loginName = userObj.optString("loginName");// 用户名
									name = userObj.optString("name");// 用户姓名
									mobile = userObj.optString("mobile");// 手机号码
									balance = userObj.optString("balanceString");// 余额
									minimum = userObj.optString("minimum");// 最低限额
									userType = userObj.optString("userType");// 用户类型
									auditState = userObj.optString("auditState");// 审核状态
									auditReason = userObj.optString("auditReason");// 审核理由
									roleNames = userObj.optString("roleNames");// 用户角色名
									email = userObj.optString("email");// 邮箱
//									String isBindQQ = userObj.optString("isBindQQ");// 是否绑定QQ
//									String isBindWx = userObj.optString("isBindWx");// 是否绑定微信
									remarks = userObj.optString("remarks");// 备注

									JSONObject office = userObj.optJSONObject("office");
									officeId = office.optString("id");// 机构ID
									String userCode = office.optString("code");// 用户编号
									area_name = "";
									if(office.optJSONObject("area")!=null){
										area_name = office.optJSONObject("area").getString("name")==null ? "": office.optJSONObject("area").getString("name");// 归属区域名称

									}
									jurisArea = "";
									if(office.optJSONObject("jurisArea") != null) {
										jurisArea = office.optJSONObject("jurisArea").getString("name") == null ? "" : office.optJSONObject("jurisArea").getString("name");// 管辖区域名称
									}
									orgName = office.optString("name");// 机构名称
									orgType = office.optString("type");// 机构类型 "1"：个人；"2"：火车站 "3"：代售点 "4"：证券用户" 5"：商户 "6"：铁路总局  "7"：铁路分局
									corpName = office.optString("corpName");// 法人姓名
									corpMobile = office.optString("corpMobile");// 法人手机
									corpIdNum = office.optString("corpIdNum");// 法人身份证号
									address = office.optString("address");// 联系地址
									master = office.optString("master");// 联系人
									contactPhone = office.optString("phone");// 联系人电话
									winNumber = office.optString("winNumber");// 窗口号
									agent = office.optString("agent");//第一代理人
									agentType = office.optString("agentType");//代理人类型
									operatorName = office.optString("operatorName");//负责人姓名
									operatorMobile = office.optString("operatorMobile");//负责人手机
									operatorIdNum = office.optString("operatorIdNum");//负责人身份证号
									agentCompanyName = office.optString("agentCompanyName");//公司名称
									corp_licence = office.optString("corpLicenceSrc");// 营业执照图片
									corp_card_fornt = office.optString("corpCardforntSrc");// 法人身份证正面图片
									corp_card_rear = office.optString("corpCardrearSrc");// 法人身份证反面图片
									staContIndexSrc = office.optString("staContIndexSrc");// 车站合同首页
									staContLastSrc = office.optString("staContLastSrc");// 车站合同尾页
									staDepositSrc = office.optString("staDepositSrc");// 车站押金条
									staDepInspSrc = office.optString("staDepInspSrc");// 车站押金年检证书
//									String bankFlowSrc = office.optString("bankFlowSrc");// 银行流水
									operatorCardforntSrc = office.optString("operatorCardforntSrc");// 负责人身份证正面
									operatorCardrearSrc = office.optString("operatorCardrearSrc");// 负责人身份证反面
									fareAuthorizationSrc = office.optString("fareAuthorizationSrc");// 票款汇缴授权书

									user.setId(id);
									user.setLoginName(loginName);
									user.setName(name);
									user.setMobile(mobile);
									user.setBalance(balance);
									user.setMinimum(minimum);
									user.setUserType(userType);
									user.setAuditState(auditState);
									user.setAuditReason(auditReason);
									user.setRoleNames(roleNames);
									user.setEmail(email);
									user.setRemarks(remarks);
									user.setUserCode(userCode);
									user.setArea_name(area_name);
									user.setJurisArea(jurisArea);
									user.setOrgName(orgName);
									user.setOfficeType(orgType);
									user.setCorpName(corpName);
									user.setCorpMobile(corpMobile);
									user.setCorpIdNum(corpIdNum);
									user.setAddress(address);
									user.setMaster(master);
									user.setContactPhone(contactPhone);
									user.setWinNumber(winNumber);
									user.setOfficeId(officeId);
									user.setCorp_licence(corp_licence);
									user.setCorp_card_fornt(corp_card_fornt);
									user.setCorp_card_rear(corp_card_rear);
									user.setAgent(agent);
									user.setAgentType(agentType);
									user.setAgentCompanyName(agentCompanyName);
									user.setStaContIndexSrc(staContIndexSrc);
									user.setStaContLastSrc(staContLastSrc);
									user.setStaDepositSrc(staDepositSrc);
									user.setStaDepInspSrc(staDepInspSrc);
									user.setOperatorCardforntSrc(operatorCardforntSrc);
									user.setOperatorCardrearSrc(operatorCardrearSrc);
									user.setFareAuthorizationSrc(fareAuthorizationSrc);
									user.setOperatorName(operatorName);
									user.setOperatorMobile(operatorMobile);
									user.setOperatorIdNum(operatorIdNum);

								}

								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject bindObj = (JSONObject) jsonArray.get(i);
									isBindQQ = bindObj.optString("isBindQQ");// 是否绑定QQ
									isBindWx = bindObj.optString("isBindWx");// 应扣总金额
								}
								ObjectSaveUtil.saveObject(UserListDetailsA.this, "usetInfo", user);  //保存部分用户信息
								ObjectSaveUtil.saveObject(UserListDetailsA.this,"allUser",allUser);  //保存所有用户信息
								if (loginName != null){
									tv_userName.setText(loginName);
								}else {
									tv_userName.setText("");
								}

								if (name != null){
									tv_name.setText(name);
								}else {
									tv_name.setText("");
								}

								if (mobile != null){
									tv_mobile.setText(mobile);
								}else {
									tv_mobile.setText("");
								}

								if (email != null){
									tv_email.setText(email);
								}else {
									tv_email.setText("");
								}

								if (userType != null ){
									if (Define.SYSTEM_MANAG_USER.equals(userType)) {
										tv_userType.setText("系统管理员");
									}else if (Define.ORGANIZATION_USER.equals(userType)) {
										tv_userType.setText("机构用户");
									}else {
										tv_userType.setText("个人用户");
									}
								}else {
									tv_userType.setText("");
								}

								if (roleNames != null){
									tv_role.setText(roleNames);
								}else {
									tv_role.setText("");
								}

								if (orgName != null){
									tv_orgName.setText(orgName);
								}else {
									tv_orgName.setText("");
								}

								if (area_name != null){
									tv_area.setText(area_name);
								}else {
									tv_area.setText("");
								}

								if (jurisArea != null){
									tv_jurisArea.setText(jurisArea);
								}else {
									tv_jurisArea.setText("");
								}
								if (agent != null){
									if (Define.AGENT_PERSONAL.equals(agent)){
										tv_firstAgent.setText("个人");
									}else if (Define.AGENT_LIVID.equals(agent)){
										tv_firstAgent.setText("铁青");
									}else if (Define.AGENT_POST.equals(agent)){
										tv_firstAgent.setText("邮政");
									}else if (Define.AGENT_BUSINESS.equals(agent)){
										tv_firstAgent.setText("企业");
									}
								}else {
									tv_firstAgent.setText("");
								}

								if (agentType != null){
									if (Define.agentType_ZHIYIN.equals(agentType)){
										tv_agentType.setText("直营");
									}else if (Define.agentType_CONTRACT.equals(agentType)){
										tv_agentType.setText("承包");
									}
								}else {
									tv_agentType.setText("");
								}

								if (Define.AGENT_PERSONAL.equals(agent) && Define.agentType_ZHIYIN.equals(agentType)){
									layout_fuZeRenInfo.setVisibility(View.GONE);
//									layout_shouQuanShu.setVisibility(View.INVISIBLE);
									layout_fuzerenIdFront.setVisibility(View.INVISIBLE);
//									layout_fuzerenIdOpposite.setVisibility(View.INVISIBLE);
									layout_fuZeRenCertificates.setVisibility(View.GONE);

								}else {
									if (operatorName != null){
										tv_fuZeRenName.setText(operatorName);
									}else {
										tv_fuZeRenName.setText("");
									}

									if (operatorMobile != null){
										tv_fuZeRenMobile.setText(operatorMobile);
									}else {
										tv_fuZeRenMobile.setText("");
									}

									if (operatorIdNum != null){
										tv_fuZeRenIdNumber.setText(operatorIdNum);
									}else {
										tv_fuZeRenIdNumber.setText("");
									}

									if (operatorCardforntSrc != null && !operatorCardforntSrc.isEmpty()){

										try {
//											getDocumentImage2(operatorCardforntSrc, "operatorCardforntSrc", 0);
											getDocumentImage3(operatorCardforntSrc, operatorCardforntSrcTag, 0, false, "");

										} catch (Exception e) {
											e.printStackTrace();
										}

									}

									if (operatorCardrearSrc != null && !operatorCardrearSrc.isEmpty()){

										try {
//											getDocumentImage2(operatorCardrearSrc, "operatorCardrearSrc", 0);
											getDocumentImage3(operatorCardrearSrc, operatorCardrearSrcTag, 0, false, "");

										} catch (Exception e) {
											e.printStackTrace();
										}

									}

									if (fareAuthorizationSrc != null && !fareAuthorizationSrc.isEmpty()){

										try {
//											getDocumentImage2(fareAuthorizationSrc, "fareAuthorizationSrc", 0);
											getDocumentImage3(fareAuthorizationSrc, fareAuthorizationSrcTag, 0, false, "");

										} catch (Exception e) {
											e.printStackTrace();
										}

									}


								}

								if (address != null){
									tv_address.setText(address);
								}else {
									tv_address.setText("");
								}

								if (corpName != null){
									tv_corpName.setText(corpName);
								}else {
									tv_corpName.setText("");
								}

								if (corpMobile != null){
									tv_corpMobile.setText(corpMobile);
								}else {
									tv_corpMobile.setText("");
								}

								if (corpIdNum != null){
									tv_corpIdNum.setText(corpIdNum);
								}else {
									tv_corpIdNum.setText("");
								}

								if (master != null){
									tv_contacts.setText(master);
								}else {
									tv_contacts.setText("");
								}

								if (contactPhone != null){
									tv_contactsMobile.setText(contactPhone);
								}else {
									tv_contactsMobile.setText("");
								}

								if (corp_licence != null && !corp_licence.isEmpty()){

									try {
//										getDocumentImage2(corp_licence, "corp_licence", 0);
										getDocumentImage3(corp_licence, corpLicenceSrcTag, 0, false, "");

									} catch (Exception e) {
										e.printStackTrace();
									}

								}

								if (corp_card_fornt != null && !corp_card_fornt.isEmpty()){

									try {
//										getDocumentImage2(corp_card_fornt, "corp_card_fornt", 0);
										getDocumentImage3(corp_card_fornt, corpCardforntSrcTag, 0, false, "");

									} catch (Exception e) {
										e.printStackTrace();
									}

								}

								if (corp_card_rear != null && !corp_card_rear.isEmpty()){


									try {
//										getDocumentImage2(corp_card_rear, "corp_card_rear", 0);
										getDocumentImage3(corp_card_rear, corpCardrearSrcTag, 0, false, "");

									} catch (Exception e) {
										e.printStackTrace();
									}
								}

								if (staContIndexSrc != null && !staContIndexSrc.isEmpty()){

									try {
//									 	getDocumentImage2(staContIndexSrc, "staContIndexSrc", 0);
										getDocumentImage3(staContIndexSrc, staContIndexSrcTag, 0, false, "");

									} catch (Exception e) {
										e.printStackTrace();
									}

								}

								if (staContLastSrc != null && !staContLastSrc.isEmpty()){

									try {
//										getDocumentImage2(staContLastSrc, "staContLastSrc", 0);
										getDocumentImage3(staContLastSrc, staContLastSrcTag, 0, false, "");

									} catch (Exception e) {
										e.printStackTrace();
									}

								}

								//获取图片回调
								setImgResultLinstener(new ImgResult() {
									@Override
									public void getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr) {
										switch (tag){
											case corpLicenceSrcTag:
												corpLicenceSrcFile = file;
												glideSetImg(file, iv_businessLicense);
												break;

											case corpCardforntSrcTag:
												corpCardforntSrcFile = file;
												glideSetImg(file, iv_identificationFront);
												break;

											case corpCardrearSrcTag:
												corpCardrearSrcFile = file;
												glideSetImg(file, iv_identificationOpposite);
												break;

											case staContIndexSrcTag:
												staContIndexSrcFile = file;
												glideSetImg(file, iv_stationContactFrist);

												break;
											case staContLastSrcTag:
												staContLastSrcFile = file;
												glideSetImg(file, iv_stationContactEnd);
												break;

											case operatorCardforntSrcTag:
												operatorCardforntSrcFile = file;
												glideSetImg(file, iv_fuzerenIdFront);
												break;

											case operatorCardrearSrcTag:
												operatorCardrearSrcFile = file;
												glideSetImg(file, iv_fuzerenIdOpposite);
												break;

											case fareAuthorizationSrcTag:
												fareAuthorizationSrcFile = file;
												glideSetImg(file, iv_paySystemCertificate);
												break;
										}
               /* if(i!=-1){
                    documentInformationBeenList.get(i).imageSrc =imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }*/


									}
								});



								if (Define.OUTLETS_NORMAL.equals(auditState)){
									tv_title.setText("用户详情");
									tv_auditState.setText("审核通过");
									layout_auditedContent.setVisibility(View.VISIBLE);
									layout_noRelieveContent.setVisibility(View.GONE);
									tv_auditReason.setText(auditReason);
									et_minQuota.setText(minimum);
									et_minQuota.setEnabled(false);
									layout_auditingButon.setVisibility(View.GONE);
								}else if (Define.OUTLETS_PENDIG_AUDIT.equals(auditState)){
									tv_title.setText("用户审核");
									tv_auditState.setText("待审核");
									layout_auditedContent.setVisibility(View.GONE);
									layout_noRelieveContent.setVisibility(View.VISIBLE);
								}else if (Define.OUTLETS_NOTPASS.equals(auditState)){
									tv_title.setText("用户详情");
									tv_auditState.setText("审核不通过");
									layout_auditedContent.setVisibility(View.VISIBLE);
									layout_noRelieveContent.setVisibility(View.GONE);
									tv_auditReason.setText(auditReason);
									et_minQuota.setText(minimum);
									et_minQuota.setEnabled(false);
									layout_auditingButon.setVisibility(View.GONE);
								}else if (Define.QIANDIANDAI_NOTPASS.equals(auditState)){
									tv_title.setText("用户审核");
									tv_auditState.setText("补充资料待审核");
								}



								final JSONObject jsonObject = new JSONObject();
								try {
//									jsonObject.put("userId", userId);
									jsonObject.put("userType", userType);
									jsonObject.put("mobile", mobile);

									jsonObject.put("loginName", loginName);
									jsonObject.put("officeId", officeId);
									jsonObject.put("officeType", orgType);

								} catch (JSONException e1) {
									e1.printStackTrace();
								}

								Runnable proRunnable = new Runnable() {

									@Override
									public void run() {

										Message msg = new Message();
										msg.what = auditParameter;
										msg.obj = jsonObject;
										mHandler.sendMessage(msg);
									}
								};

								Thread proThread = new Thread(proRunnable);
								proThread.start();
//								auditParameter(userId, userType, mobile, loginName, officeId, orgType);

							} else if ("46000".equals(responseCode)) {
								cpd.dismiss();
								Toast.makeText(UserListDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UserListDetailsA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(UserListDetailsA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(UserListDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
				Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

			}
		});
		// 解决重复请求后台的问题
		userInfoRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		userInfoRequest.setTag("userInfoAudit");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(userInfoRequest);

	}

	/*@Override
	protected void getImageBitmap(Bitmap licenceBitmap, String tag,int p) {
		if (licenceBitmap != null) {
			if ("operatorCardforntSrc".equals(tag)) {
				iv_fuzerenIdFront.setImageBitmap(licenceBitmap);
				bmOptPositive = licenceBitmap;
			}else if ("operatorCardrearSrc".equals(tag)){

				iv_fuzerenIdOpposite.setImageBitmap(licenceBitmap);
				bmOptOpposite = licenceBitmap;
			}else if ("corp_licence".equals(tag)){

				iv_businessLicense.setImageBitmap(licenceBitmap);
				bmCertificate = licenceBitmap;
			}else if ("corp_card_fornt".equals(tag)){

				iv_identificationFront.setImageBitmap(licenceBitmap);
				bmIdCardPositive = licenceBitmap;
			}else if ("corp_card_rear".equals(tag)){

				iv_identificationOpposite.setImageBitmap(licenceBitmap);
				bmIdCardOpposite = licenceBitmap;
			}else if ("fareAuthorizationSrc".equals(tag)){

				iv_paySystemCertificate.setImageBitmap(licenceBitmap);
				bmAuthorization = licenceBitmap;
			}else if ("staContIndexSrc".equals(tag)){

				iv_stationContactFrist.setImageBitmap(licenceBitmap);
				bmContractFrist = licenceBitmap;
			}else if ("staContLastSrc".equals(tag)){
				iv_stationContactEnd.setImageBitmap(licenceBitmap);
				bmContractLast = licenceBitmap;
			}
		}
	}*/
	/**
	 * 通过Glide设置图片到控件上
	 */
	private void glideSetImg(File file, final ImageView imageView){
		//设置圆角图片
       /* Glide.with(UpdatePersonalinformation.this).load(file).asBitmap().centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });*/
		//设置普通图片
		try {
			Glide.with(UserListDetailsA.this).load(file).into(imageView);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
//		MyApplication.getHttpQueues().cancelAll("documentBitmap");
		MyApplication.getHttpQueues().cancelAll("reviewUserRequest");
		MyApplication.getHttpQueues().cancelAll("userInfoAudit");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (bmContractFrist != null){
			bmContractFrist.recycle();
			bmContractFrist = null;
		}
		if (bmContractLast != null){
			bmContractLast.recycle();
			bmContractLast = null;
		}
		if (bmAuthorization != null){
			bmAuthorization.recycle();
			bmAuthorization = null;
		}
		if(bmIdCardPositive != null){
			bmIdCardPositive.recycle();
			bmIdCardPositive = null;
		}
		if(bmIdCardOpposite != null){
			bmIdCardOpposite.recycle();
			bmIdCardOpposite = null;
		}
		if(bmOptPositive != null){
			bmOptPositive.recycle();
			bmOptPositive = null;
		}
		if(bmOptOpposite != null){
			bmOptOpposite.recycle();
			bmOptOpposite = null;
		}
		if(bmCertificate != null){
			bmCertificate.recycle();
			bmCertificate = null;
		}

		System.gc();  //通知垃圾回收站回收垃圾
	}

}

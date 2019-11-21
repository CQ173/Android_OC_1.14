package com.huoniao.oc.user;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UnauditedLoginA extends BaseActivity implements OnClickListener{
	
	private ImageView iv_back;
	private TextView tv_userName, tv_userState, tv_corpName, tv_corpMobile, tv_corpIdNum,
				tv_area_name;
	private EditText et_name, et_mobile, et_email, et_remarks, et_orgName, et_address,
				et_master, et_contactPhone;
	
	private String corp_licence;//营业执照
	private String corp_card_fornt;//身份证正面
	private String corp_card_rear;//身份证反面
	
	private TextView tv_see_licence, tv_see_cardFornt, tv_see_cardRear;
	private Intent intent;
	private LinearLayout layout_noAdminContent;
	private static final int USER_INFO = 1;
	User myUser;
	
	private ProgressDialog pd;
	
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case USER_INFO: {

				myUser = (User) msg.obj;
				tv_userName.setText(myUser.getLoginName());
				if (Define.OUTLETS_NORMAL.equals(myUser.getAuditState())) {
					tv_userState.setText("审核通过");
				}else if (Define.OUTLETS_PENDIG_AUDIT.equals(myUser.getAuditState())) {
					tv_userState.setText("待审核");
				}else {
					tv_userState.setText("审核不通过");
				}
				tv_corpName.setText(myUser.getCorpName());
				tv_corpMobile.setText(myUser.getCorpMobile());
				tv_corpIdNum.setText(myUser.getCorpIdNum());
				tv_area_name.setText(myUser.getArea_name());
				et_name.setText(myUser.getName());
				et_mobile.setText(myUser.getMobile());
				et_orgName.setText(myUser.getOrgName());
				et_address.setText(myUser.getAddress());
				et_master.setText(myUser.getMaster());
				et_contactPhone.setText(myUser.getContactPhone());
				et_email.setText(myUser.getEmail());
				et_remarks.setText(myUser.getRemarks());
				corp_licence = myUser.getCorp_licence();
				corp_card_fornt = myUser.getCorp_card_fornt();
				corp_card_rear = myUser.getCorp_card_rear();
				break;

			}
			
			}
		}
	};	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unauditedlogin);
		initView();
		initData();
	}
	private void initData() {
		try {
			getUserInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		user = (User) ObjectSaveUtil.readObject(UnauditedLoginA.this, "usetInfo");
		
		
		
	}

	private void initView() {
		pd = new CustomProgressDialog(UnauditedLoginA.this, "正在加载中...", R.drawable.frame_anim);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_userName = (TextView) findViewById(R.id.tv_userName);
		tv_userState = (TextView) findViewById(R.id.tv_userState);
		tv_corpName = (TextView) findViewById(R.id.tv_corpName);
		tv_corpMobile = (TextView) findViewById(R.id.tv_corpMobile);
		tv_corpIdNum = (TextView) findViewById(R.id.tv_corpIdNum);
		
		tv_see_licence = (TextView) findViewById(R.id.tv_see_licence);
		tv_see_cardFornt = (TextView) findViewById(R.id.tv_see_cardFornt);
		tv_see_cardRear = (TextView) findViewById(R.id.tv_see_cardRear);
		
		tv_area_name = (TextView) findViewById(R.id.tv_area_name);
		et_name = (EditText) findViewById(R.id.et_name);
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_email = (EditText) findViewById(R.id.et_email);
		et_remarks = (EditText) findViewById(R.id.et_remarks);
		et_orgName = (EditText) findViewById(R.id.et_orgName);
		et_address = (EditText) findViewById(R.id.et_address);
		et_master = (EditText) findViewById(R.id.et_master);
		et_contactPhone = (EditText) findViewById(R.id.et_contactPhone);
		layout_noAdminContent = (LinearLayout) findViewById(R.id.layout_noAdminContent);
//		if (LoginA.IDENTITY_TAG.equals(Define.SYSTEM_MANAG_USER) || Define.SYSTEM_MANAG_USER.equals(PerfectInformationA.IDENTITY_TAG)) {
//			layout_noAdminContent.setVisibility(View.GONE);
//		}
		iv_back.setOnClickListener(this);
		tv_see_licence.setOnClickListener(this);
		tv_see_cardFornt.setOnClickListener(this);
		tv_see_cardRear.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
//			finish();	
			intent = new Intent(UnauditedLoginA.this, LoginA.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;
			
		case R.id.tv_see_licence:
			documentImageDialog(UnauditedLoginA.this, corp_licence);

			break;
		case R.id.tv_see_cardFornt:
			documentImageDialog(UnauditedLoginA.this, corp_card_fornt);
			break;
		case R.id.tv_see_cardRear:
			documentImageDialog(UnauditedLoginA.this, corp_card_rear);
			break;	
		default:
			break;
		}
		
	}
	
	private void documentImageDialog(Context context, String imgUrl){

		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.documentimage_dialog, null);
		ImageView documentImage = (ImageView) view.findViewById(R.id.documentImage);
		try {
			getDocumentImage(imgUrl, documentImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	   
	}
	
	private void getDocumentImage(String imgUrl, final ImageView imageView) throws Exception {
		pd.show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("imageSrc", imgUrl);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		SessionJsonObjectRequest documentImageRequest = new SessionJsonObjectRequest(Method.POST,
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
							pd.dismiss();

							} else if("46000".equals(responseCode)){
								pd.dismiss();
								Toast.makeText(UnauditedLoginA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UnauditedLoginA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(UnauditedLoginA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						pd.dismiss();
						Toast.makeText(UnauditedLoginA.this,R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

					}
				});
		// 解决重复请求后台的问题
		documentImageRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		documentImageRequest.setTag("unauditedDocumentImage");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(documentImageRequest);

	}
	
	
	/**
	 * 获取用户信息
	 * 
	 * @throws Exception
	 */
	private void getUserInfo() throws Exception {
		pd.show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("", "");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String url = Define.URL + "user/queryUserInfo";
		SessionJsonObjectRequest userInfoRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("userInfo", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								JSONArray jsonArray = response.getJSONArray("data");
								final User user = new User();

								for (int i = 0; i < jsonArray.length(); i++) {

									JSONObject userObj = (JSONObject) jsonArray.get(i);
									String id = userObj.optString("id");// 用户ID
									String loginName = userObj.optString("loginName");// 用户名
									String name = userObj.optString("name");// 用户姓名
									String mobile = userObj.optString("mobile");// 手机号码
									String balance = userObj.optString("balance");// 余额
									String minimum = userObj.optString("minimum");// 最低限额
									String userType = userObj.optString("userType");// 用户类型
									String auditState = userObj.optString("auditState");// 审核状态
									String auditReason = userObj.optString("auditReason");// 审核理由
									String roleNames = userObj.optString("roleNames");// 用户角色名
									String email = userObj.optString("email");// 邮箱
									String remarks = userObj.optString("remarks");// 备注

									JSONObject office = userObj.optJSONObject("office");
									String officeId = office.optString("officeId");// 机构ID
									String userCode = office.optString("code");// 用户编号
									String area_name = office.optJSONObject("area").getString("name");// 归属区域名称
									String orgName = office.optString("name");// 机构名称
									String corpName = office.optString("corpName");// 法人姓名
									String corpMobile = office.optString("corpMobile");// 法人手机
									String corpIdNum = office.optString("corpIdNum");// 法人身份证号
									String address = office.optString("address");// 联系地址
									String master = office.optString("master");// 联系人
									String contactPhone = office.optString("phone");// 联系人电话
									String winNumber = office.optString("winNumber");// 窗口号

									String corp_licence = office.optString("corpLicenceSrc");// 营业执照图片
									String corp_card_fornt = office.optString("corpCardforntSrc");// 身份证正面图片
									String corp_card_rear = office.optString("corpCardrearSrc");// 身份证反面图片

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
									user.setOrgName(orgName);
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

								}
								pd.dismiss();
								Runnable runnable = new Runnable() {

									@Override
									public void run() {

										Message msg = new Message();
										
										msg.what = USER_INFO;
										msg.obj = user;
										mHandler.sendMessage(msg);
									}
								};

								Thread thread = new Thread(runnable);
								thread.start();
							} else if ("46000".equals(responseCode)) {
								pd.dismiss();
								Toast.makeText(UnauditedLoginA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UnauditedLoginA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(UnauditedLoginA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						pd.dismiss();
						Toast.makeText(UnauditedLoginA.this, R.string.netError, Toast.LENGTH_SHORT).show();
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
		userInfoRequest.setTag("unauditedUserInfo");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(userInfoRequest);

	}
	
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                    Object mHelperUtils;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();

            } else {
//                    finish();
            	ExitApplication.getInstance().exit();
            }
            return true;
    }
    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("unauditedDocumentImage");
		MyApplication.getHttpQueues().cancelAll("unauditedUserInfo");
	}
}

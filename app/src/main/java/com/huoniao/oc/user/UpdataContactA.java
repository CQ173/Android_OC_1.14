package com.huoniao.oc.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.huoniao.oc.common.UserInfo;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdataContactA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private EditText et_newContacts, et_newContactMobile;
	private Button bt_submit;
	private String contactMobile = "";
	private String contacts = "";
	private String isBindQQ = "";//个人信息-用户是否绑定QQ
	private String isBindWx = "";//个人信息-用户是否绑定微信
	private ProgressDialog pd;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatacontacts);
		initView();
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_newContacts = (EditText) findViewById(R.id.et_newContacts);
		et_newContactMobile = (EditText) findViewById(R.id.et_newContactMobile);
		bt_submit = (Button) findViewById(R.id.bt_submit);
		iv_back.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		pd = new CustomProgressDialog(UpdataContactA.this, "正在加载中...", R.drawable.frame_anim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.bt_submit:
			contacts = et_newContacts.getText().toString();
	   		contactMobile = et_newContactMobile.getText().toString();
	   		if (contactMobile == null || contactMobile.isEmpty()) {
				Toast.makeText(UpdataContactA.this, "请输入联系人手机号码!", Toast.LENGTH_SHORT).show();
				return;
			}else if (RegexUtils.isMobileNO(contactMobile) == false) {
				Toast.makeText(UpdataContactA.this, "请输入合法的联系人手机号码!", Toast.LENGTH_SHORT).show();
				return;
			}
	   		if (contacts.isEmpty()) {
	   			Toast.makeText(UpdataContactA.this, "请输入联系人姓名!", Toast.LENGTH_SHORT).show();
				return;
			}
	   		
	   		updataContacts();
			break;
		default:
			break;
		}
		
	}
	
	 /**
   	 * 修改联系人
   	 */
   	private void updataContacts(){
   		
   		JSONObject jsonObj = new JSONObject();
   	    try {
   	    	
   			jsonObj.put("master",contacts);
   			jsonObj.put("phone",contactMobile);
   			
   		} catch (JSONException e) {
   			e.printStackTrace();
   		}
   	    SessionJsonObjectRequest verificationCreateAccount = new SessionJsonObjectRequest(Method.POST, Define.URL+"user/modifyLinkman", jsonObj, new Listener<JSONObject>() {

   			@Override
   			public void onResponse(JSONObject response) {
   				Log.d("debug", "response: "+ response.toString());
   				try {
   					String code = response.getString("code");
   					final String message = response.getString("msg");
   					if ("0".equals(code)) {
   						Toast.makeText(UpdataContactA.this, "修改联系人成功！", Toast.LENGTH_SHORT).show();
						UserInfo userInfo = new UserInfo();
						userInfo.getUserInfo(UpdataContactA.this,cpd, PersonalInformationA.class);
//   						getUserInfo();
   						finish();
   					}else {
   						Toast.makeText(UpdataContactA.this, message, Toast.LENGTH_LONG).show();
   					}
   				} catch (Exception e) {
   					e.printStackTrace();
   				}
   				
   				Log.d("debug", "vercode = " + response.toString());
   	
   			}
   		}, new ErrorListener() {

   			@Override
   			public void onErrorResponse(VolleyError error) {
   				Toast.makeText(UpdataContactA.this, R.string.netError, Toast.LENGTH_SHORT).show();
   				Log.d("REQUEST-ERROR", error.getMessage(), error);
//              	  	byte[] htmlBodyBytes = error.networkResponse.data;
//              	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
   			}
   		});
   	    
   	    //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
   	    verificationCreateAccount.setTag("submitNewContacts");
   	    //将请求加入全局队列中
   	    MyApplication.getHttpQueues().add(verificationCreateAccount);
   	}
   	
   	/**
   	 * 刷新个人信息
   	 */
   	private void getUserInfo(){
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
								int num = response.getInt("size");
								JSONArray jsonArray = response.getJSONArray("data");
								num = Math.min(num, jsonArray.length());
								User user = new User();

								for (int i = 0; i < num; i++) {

									JSONObject userObj = (JSONObject) jsonArray.get(i);
									String id = userObj.optString("id");// 用户ID
									String loginName = userObj.optString("loginName");// 用户名
									String name = userObj.optString("name");// 用户姓名
									String mobile = userObj.optString("mobile");// 手机号码
									String balance = userObj.optString("balanceString");// 余额
									String minimum = userObj.optString("minimum");// 最低限额
									String userType = userObj.optString("userType");// 用户类型
									String auditState = userObj.optString("auditState");// 审核状态
									String auditReason = userObj.optString("auditReason");// 审核理由
									String roleNames = userObj.optString("roleNames");// 用户角色名
									String email = userObj.optString("email");// 邮箱
//									String isBindQQ = userObj.optString("isBindQQ");// 是否绑定QQ
//									String isBindWx = userObj.optString("isBindWx");// 是否绑定微信
									String remarks = userObj.optString("remarks");// 备注

									JSONObject office = userObj.optJSONObject("office");
									String officeId = office.optString("id");// 机构ID
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
								
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject bindObj = (JSONObject) jsonArray.get(i);
									isBindQQ = bindObj.optString("isBindQQ");// 是否绑定QQ
									isBindWx = bindObj.optString("isBindWx");// 应扣总金额
								}

								ObjectSaveUtil.saveObject(UpdataContactA.this, "usetInfo", user);

								intent = new Intent(UpdataContactA.this, PersonalInformationA.class);
								intent.putExtra("isBindQQ", isBindQQ);
								intent.putExtra("isBindWx", isBindWx);
								pd.dismiss();
								startActivity(intent);

								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else if ("46000".equals(responseCode)) {
								pd.dismiss();
								Toast.makeText(UpdataContactA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UpdataContactA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(UpdataContactA.this, message, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(UpdataContactA.this, R.string.netError, Toast.LENGTH_SHORT).show();
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
		userInfoRequest.setTag("refreshContacts");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(userInfoRequest);

	}
   	
   	@Override
   	protected void onStop() {
   		super.onStop();
   		MyApplication.getHttpQueues().cancelAll("submitNewContacts");
   		MyApplication.getHttpQueues().cancelAll("refreshContacts");
   	}
}

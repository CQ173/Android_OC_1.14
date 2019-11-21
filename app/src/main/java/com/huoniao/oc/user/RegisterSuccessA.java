package com.huoniao.oc.user;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MainActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SPUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterSuccessA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private Button bt_rightLogin;
	private String acctnum, password;
	private String id;//用户ID
	private String name;//用户姓名
	private String mobile;//手机号码
	private String userType;//用户类型
	private String auditState;//用户审核状态
	private String area_name;//归属区域
	private String officeType;//机构类型
	private String userCode;//用户名
	private String orgName;//机构名称
	private String userState;//用户状态
	private String winNumber;//窗口号
	private String corpName;//法人姓名
	private String corpMobile;//法人手机
	private String corpIdNum;//法人身份证号
	private String operatorMobile;//负责人手机
	private String operatorName;//负责人姓名
	private String operatorIdNum;//负责人身份证号
	private String master;//联系人
	private String contactPhone;//联系人电话
	private String address;//联系地址
	private String balance;//余额
	private String minimum;//账户最低限额
	public static String IDENTITY_TAG;
	private ProgressDialog pd;
	private Intent intent;
	private String loginName;//用户名
	private String infoReceiveType;//消息接收方式
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);
		initView();
		initData();
	}
	
	private void initData() {
		intent = getIntent();
		acctnum = intent.getStringExtra("acctnum");
		password = intent.getStringExtra("password");
		
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		bt_rightLogin = (Button) findViewById(R.id.bt_rightLogin);
		
		iv_back.setOnClickListener(this);
		bt_rightLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			intent = new Intent(RegisterSuccessA.this, LoginA.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;
			
		case R.id.bt_rightLogin:
			try {
				registerLogin();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			intent = new Intent(RegisterSuccessA.this, LoginA.class);
//			startActivity(intent);
//			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;
			
		default:
			break;
		}
		
	}
	
	private void registerLogin() throws Exception{
		pd = new CustomProgressDialog(RegisterSuccessA.this, "正在登录中....", R.drawable.frame_anim);
		pd.show();
	    JSONObject jsonObj = new JSONObject();
	    try {
			jsonObj.put("acctNum",acctnum);
			jsonObj.put("password",password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    String url = Define.URL + "user/userLogin";
	    SessionJsonObjectRequest loginRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObj, new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {//jsonObject为请求成功返回的Json格式数据
			Log.d("debug", "response: "+ response.toString());
			try {
				String code = response.getString("code");
				if ("0".equals(code)) {
					JSONArray agreementArray = null;
					
					List<Integer> list = new ArrayList<Integer>();

					
					JSONArray jsonArray = response.getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i++) {  
					    JSONObject temp = (JSONObject) jsonArray.get(i);  
					    name = temp.optString("name");  
					    mobile = temp.optString("mobile"); 
					    userType = temp.optString("userType");
					    auditState = temp.optString("auditState");
//					    IDENTITY_TAG = temp.getString("officeType");
					    JSONObject office = temp.optJSONObject("office");
					    balance = temp.optString("balanceString");// 余额
					    minimum = temp.optString("minimum");// 账户最低限额
						loginName = temp.optString("loginName");
						infoReceiveType = temp.optString("infoReceiveType");
					    IDENTITY_TAG = office.optString("type");
					    userCode = office.optString("code");
					    userState = office.optString("state");
					    area_name = office.optJSONObject("area").getString("name");
					    orgName = office.optString("name");
					    corpName = office.optString("corpName");//
					    corpMobile = office.optString("corpMobile");
					    corpIdNum = office.optString("corpIdNum");
						operatorMobile = office.optString("operatorMobile");
						operatorName = office.optString("operatorName");
						operatorIdNum = office.optString("operatorIdNum");
					    address = office.optString("address");
					    master = office.optString("master");
					    contactPhone = office.optString("phone");
					    winNumber = office.optString("winNumber");
					    agreementArray = temp.optJSONArray("agreement");
					    if (agreementArray != null && !"".equals(agreementArray)) {
					    	 for (int j = 0; j < agreementArray.length(); j++) {  
							    	
							    	list.add(agreementArray.getInt(j));
							    }
						}
					   
					   
					   
					}
					 	Log.d("test", "list=" + list);
//					 	  if (!"".equals(agreementArray) || agreementArray != null) {
//						    	 for (int j = 0; j < agreementArray.length(); j++) {  
//								    	
//								    	list.add(agreementArray.getInt(j));
//								    }
//							}
					 	Integer[] array = new Integer[list.size()];
					 	for(int k=0;k<list.size();k++){
					 		array[k]=list.get(k);
					 	}
					 	//(String[])list.toArray(new String[list.size()]); 
					 	String agreement = Arrays.toString(array);
					 	
					 	SPUtils.put(RegisterSuccessA.this, "agreement", agreement);
					 	User user = new User();
					    user.setName(name);
					    user.setMobile(mobile);
					    user.setUserType(userType);
					    user.setAuditState(auditState);
					    user.setOfficeType(IDENTITY_TAG);
					    user.setUserCode(userCode);
					    user.setUserState(userState);
					    user.setArea_name(area_name);
					    user.setOrgName(orgName);
					    user.setCorpName(corpName);
					    user.setCorpMobile(corpMobile);
					    user.setCorpIdNum(corpIdNum);
						user.setOperatorMobile(operatorMobile);
						user.setOperatorName(operatorName);
						user.setOperatorIdNum(operatorIdNum);
					    user.setAddress(address);
					    user.setMaster(master);
					    user.setContactPhone(contactPhone);
					    user.setWinNumber(winNumber);
					    user.setBalance(balance);
					    user.setMinimum(minimum);
						user.setLoginName(loginName);
						user.setInfoReceiveType(infoReceiveType);
//					    boolean CheckBoxLogin = cb_rememberAccount.isChecked();      
//						 if (CheckBoxLogin){          
//							 
//							 SPUtils.put(LoginA.this, "userName", userName);
//							 SPUtils.put(LoginA.this, "password", passeword);
//							 SPUtils.put(LoginA.this, "checkboxBoolean", true);
//							
//							 } else {         
//								 SPUtils.put(LoginA.this, "userName", "");
//								 SPUtils.put(LoginA.this, "password", "");
//								 SPUtils.put(LoginA.this, "checkboxBoolean", false);
//								 }
						 
					    ObjectSaveUtil.saveObject(RegisterSuccessA.this, "loginResult", user);

					    
					    if (Define.OUTLETS_NOTPASS.equals(auditState)) {
					    	pd.dismiss();
					    	Toast.makeText(RegisterSuccessA.this, "您提交的资料审核未通过，请到电脑端登录O计系统修改个人资料重新提交审核！", Toast.LENGTH_LONG).show();
					    }else {
					    	intent = new Intent(RegisterSuccessA.this, MainActivity.class);
							intent.putExtra("name", name);
							intent.putExtra("mobile", mobile);
							intent.putExtra("userType", userType);
							intent.putExtra("auditState", auditState);
							intent.putExtra("officeType", IDENTITY_TAG);
							
							pd.dismiss();
							Toast.makeText(RegisterSuccessA.this, "登录成功!", Toast.LENGTH_SHORT).show();
							startActivity(intent);
							overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					    }
					   
				}else {
					pd.dismiss();
					
					Toast.makeText(RegisterSuccessA.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
				}		
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			
			
		}
	}, new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {//jsonObject为请求失败返回的Json格式数据
			pd.dismiss();
			
			Toast.makeText(RegisterSuccessA.this,R.string.netError, Toast.LENGTH_SHORT).show();
//			Log.d("REQUEST-ERROR", error.getMessage(), error);
//       	byte[] htmlBodyBytes = error.networkResponse.data;
//       	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
		}
	});
	
	    //解决重复请求后台的问题
        loginRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        10*1000,//默认超时时间，应设置一个稍微大点儿的
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
	            
	    //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
	    loginRequest.setTag("registerLogin");
	    //将请求加入全局队列中
	    MyApplication.getHttpQueues().add(loginRequest);
	
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("registerLogin");
	
	}
}

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.EventBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.UserInfo;
import com.huoniao.oc.util.CountDownTimerUtils;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class UpdataMobileTwoA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private EditText et_newMobile;
	private EditText et_verCode;
	private TextView tv_getVericode;
	private ImageView imageVerCode;
	private EditText imageVerCodeInput;
	CountDownTimerUtils mCountDownTimerUtils;
	private String mobile = "";
	private String verCode = "";
	private Button bt_submit;
	private String isBindQQ = "";//个人信息-用户是否绑定QQ
	private String isBindWx = "";//个人信息-用户是否绑定微信
	private ProgressDialog pd;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatemobile_two);
		initView();
		initData();
	}
	
	private void initData() {
		MyApplication.getInstence().addActivity(this);
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_newMobile = (EditText) findViewById(R.id.et_newMobile);
		et_verCode = (EditText) findViewById(R.id.et_verCode);
		tv_getVericode = (TextView) findViewById(R.id.tv_getVericode);
		bt_submit = (Button) findViewById(R.id.bt_submit);
		iv_back.setOnClickListener(this);
		tv_getVericode.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		pd = new CustomProgressDialog(UpdataMobileTwoA.this, "正在加载中...", R.drawable.frame_anim);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_getVericode:
			mobile = et_newMobile.getText().toString();
//			verCode = et_verCode.getText().toString();
			
			if ("".equals(mobile) || mobile == null) {
				Toast.makeText(UpdataMobileTwoA.this, "请输入您的手机号码!", Toast.LENGTH_SHORT).show();
			}else if (RegexUtils.isMobileNO(mobile) == false) {
				Toast.makeText(UpdataMobileTwoA.this, "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
			}
			else {
				getVeriCode();
				
			}
			break;
			
		case R.id.bt_submit:
			mobile = et_newMobile.getText().toString();
//			verCode = et_verCode.getText().toString();
			
			if ("".equals(mobile) || mobile == null) {
				Toast.makeText(UpdataMobileTwoA.this, "请输入您的手机号码!", Toast.LENGTH_SHORT).show();
				return;
			}else if (RegexUtils.isMobileNO(mobile) == false) {
				Toast.makeText(UpdataMobileTwoA.this, "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
				return;
			}
			requestSubmit();
			break;	
		default:
			break;
		}
		
	}
	
	 /**
     * 获取验证码
     * 
     */
    private void getVeriCode(){
//		LayoutInflater inflater = LayoutInflater.from(this);
//		View view = inflater.inflate(R.activity_windowanchored.dialog_imagevericode, null);
//		imageVerCode = (ImageView) view.findViewById(R.id.imageVeriCode);
//		imageVerCodeInput = (EditText) view.findViewById(R.id.et_imageVeriCode);
//		Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
//		Button bt_cancel = (Button) view.findViewById(R.id.bt_concel);
//		
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setView(view);
//		final AlertDialog dialog = builder.create();//获取dialog
//		final String imageUrl = Define.URL+"user/buildIconCode";
//		loadImageByVolley(imageUrl, imageVerCode);
//		
//		imageVerCode.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				loadImageByVolley(imageUrl, imageVerCode);
//				
//			}
//		});
//		
//		//确定按钮，在这里判断图片验证码是否正确
//		bt_confirm.setOnClickListener(new OnClickListener() {
//		   
//			@Override
//			public void onClick(View v) {
				 JSONObject jsonObj = new JSONObject();
				    try {
//				    	mobile = et_newMobile.getText().toString();
//				    	String imageCode = imageVerCodeInput.getText().toString();	
						jsonObj.put("mobile",mobile);
//						jsonObj.put("iconCode","");
//						Log.d("debug", "imageCode = "+ imageCode);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				    JsonObjectRequest request = new JsonObjectRequest(Method.POST, Define.URL+"user/validateMobile", jsonObj, new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Log.d("debug", "response: "+ response.toString());
							try {
								String code = response.getString("code");
								String message = response.getString("msg");
								if ("46004".equals(code)) {
//									dialog.dismiss();
									JSONObject jsonObject = new JSONObject();
									try {
										jsonObject.put("mobile",mobile);
										jsonObject.put("type", Define.UPTATA_BINDPHONE_VERCODE);
									} catch (JSONException e) {
										e.printStackTrace();
									}
									
									SessionJsonObjectRequest phoneVerCodeRequest = new SessionJsonObjectRequest(Method.POST, Define.URL+"user/getVerifyCode", 
											jsonObject, new Listener<JSONObject>() {

												@Override
												public void onResponse(JSONObject response) {
													mCountDownTimerUtils = new CountDownTimerUtils(tv_getVericode, 60000, 1000);
													mCountDownTimerUtils.start();
													
												}
											}, new ErrorListener() {

												@Override
												public void onErrorResponse(VolleyError error) {
													Toast.makeText(UpdataMobileTwoA.this, R.string.netError, Toast.LENGTH_LONG).show();
													
												}
											});
									 //解决重复请求后台的问题
									phoneVerCodeRequest.setRetryPolicy(
							                new DefaultRetryPolicy(
							                        10*1000,//默认超时时间，应设置一个稍微大点儿的
							                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
							                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
							                )
							        );
									//设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
									phoneVerCodeRequest.setTag("newMobileCode");
								    //将请求加入全局队列中
								    MyApplication.getHttpQueues().add(phoneVerCodeRequest);
//									mCountDownTimerUtils = new CountDownTimerUtils(tv_getVericode, 60000, 1000);
//									mCountDownTimerUtils.start();
								}else if ("0".equals(code)) {
									Toast.makeText(UpdataMobileTwoA.this, "手机号已被使用！", Toast.LENGTH_LONG).show();
								}else {
									Toast.makeText(UpdataMobileTwoA.this, message, Toast.LENGTH_LONG).show();
									Log.d("cofinm", message);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Log.d("debug", "vercode = " + response.toString());
//							dialog.dismiss();
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(UpdataMobileTwoA.this, R.string.netError, Toast.LENGTH_SHORT).show();
							
						}
					})
//				    {
//				    	//重写getHeaders 默认的key为cookie，value则为localCookie  
//				    	@Override
//				    	public Map<String, String> getHeaders() throws AuthFailureError {
//				    		if (Define.localCookie != null && Define.localCookie.length() > 0) {  
//		                        HashMap<String, String> headers = new HashMap<String, String>();  
//		                        headers.put("cookie", Define.localCookie);  
//		                        Log.d("调试", "headers----------------" + headers);  
//		                        return headers;  
//		                    }else {  
//		                        return super.getHeaders();  
//		                }  
//		            }  
//				    }
				    ;
				    //解决重复请求后台的问题
				    request.setRetryPolicy(
			                new DefaultRetryPolicy(
			                        10*1000,//默认超时时间，应设置一个稍微大点儿的
			                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
			                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
			                )
			        );
				    //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
				    request.setTag("vriNewMobileAndCode");
				    //将请求加入全局队列中
				    MyApplication.getHttpQueues().add(request);
				   
			}
//		});
		
//		bt_cancel.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				
//			}
//		});
//	        dialog.show();
	       
	     
//	}
    
    /**
    private void loadImageByVolley(String url, final ImageView image) {
//      String url = "http://ecy.120368.com/api/buildIconCode";
      pd.show();
      ImageRequest request = new ImageRequest(
                          url,
                          new Listener<Bitmap>() {
                              @Override
                              public void onResponse(Bitmap bitmap) {
                                  image.setImageBitmap(bitmap);
                                  pd.dismiss();
                              }
                          },
                          0, 0, Config.RGB_565,
                          new ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError volleyError) {
                              	pd.dismiss();
                              	Toast.makeText(UpdataMobileTwoA.this, "网络异常，请检查网络连接!", Toast.LENGTH_SHORT).show();
                                  image.setImageResource(R.mipmap.ic_launcher);
                              }
                          }){
      	@Override
      	protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
      		 Response<Bitmap> superResponse = super  
                       .parseNetworkResponse(response);  
               Map<String, String> responseHeaders = response.headers;  
               String rawCookies = responseHeaders.get("Set-Cookie");  
               //Define是一个自建的类，存储常用的全局变量  
               Define.localCookie = rawCookies.substring(0, rawCookies.indexOf(";"));  
               Log.d("sessionid", "sessionid----------------" + Define.localCookie);
      		return superResponse;
      	}
      };
      
      //解决重复请求后台的问题
      request.setRetryPolicy(
              new DefaultRetryPolicy(
                      10*1000,//默认超时时间，应设置一个稍微大点儿的
                      DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                      DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
              )
      );
      //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
      request.setTag("newMobileImgCode");
      //通过Tag标签取消请求队列中对应的全部请求
      MyApplication.getHttpQueues().add(request);
  }
    */
    
    /**
   	 * 修改手机
   	 */
   	private void updataMobile(){
   		mobile = et_newMobile.getText().toString();
   		if (mobile == null || "".equals(mobile)) {
			Toast.makeText(UpdataMobileTwoA.this, "请输入您的手机号码!", Toast.LENGTH_SHORT).show();
			return;
		}else if (RegexUtils.isMobileNO(mobile) == false) {
			Toast.makeText(UpdataMobileTwoA.this, "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
			return;
		}
		cpd.dismiss();
   		JSONObject jsonObj = new JSONObject();
   	    try {
   	    	
   			jsonObj.put("mobile",mobile);
   		

   		} catch (JSONException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
   	    SessionJsonObjectRequest verificationCreateAccount = new SessionJsonObjectRequest(Method.POST, Define.URL+"user/modifyMobile", jsonObj, new Listener<JSONObject>() {

   			@Override
   			public void onResponse(JSONObject response) {
   				Log.d("debug", "response: "+ response.toString());
   				try {
   					String code = response.getString("code");
   					final String message = response.getString("msg");
   					if ("0".equals(code)) {
   						Toast.makeText(UpdataMobileTwoA.this, "修改手机成功！", Toast.LENGTH_SHORT).show();
						  if(MyApplication.updatePersonMessagePhone) {
							  //使用粘性事件  发布消息   当分发完毕后  另一个activity 的oncreate还没有创建完毕  完毕后再发送一次消息
							  EventBus.getDefault().postSticky(new EventBean(mobile));

						  }else{
							  UserInfo userInfo = new UserInfo();
							  userInfo.getUserInfo(UpdataMobileTwoA.this,cpd,PersonalInformationA.class);
							//  getUserInfo();  //个人信息 点击手机号修改  进入这个方法
						  }
						finish();

   						}else {
   						Toast.makeText(UpdataMobileTwoA.this, message, Toast.LENGTH_LONG).show();
   					}

   				} catch (Exception e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				}
				cpd.dismiss();
   				Log.d("debug", "vercode = " + response.toString());
   	
   			}
   		}, new ErrorListener() {

   			@Override
   			public void onErrorResponse(VolleyError error) {
   				Toast.makeText(UpdataMobileTwoA.this, R.string.netError, Toast.LENGTH_SHORT).show();
   				Log.d("REQUEST-ERROR", error.getMessage(), error);
//              	  	byte[] htmlBodyBytes = error.networkResponse.data;
//              	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
				cpd.dismiss();
   			}
   		});
   	    
   	    //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
   	    verificationCreateAccount.setTag("submitNewMobile");
   	    //将请求加入全局队列中
   	    MyApplication.getHttpQueues().add(verificationCreateAccount);
   	}
   	
   	/**
   	 * 刷新个人信息
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

								  ObjectSaveUtil.saveObject(UpdataMobileTwoA.this, "usetInfo", user);
								  intent = new Intent(UpdataMobileTwoA.this, PersonalInformationA.class);
								  intent.putExtra("isBindQQ", isBindQQ);
								  intent.putExtra("isBindWx", isBindWx);
								  pd.dismiss();
								  startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								finish();
							} else if ("46000".equals(responseCode)) {
								pd.dismiss();
								Toast.makeText(UpdataMobileTwoA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UpdataMobileTwoA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(UpdataMobileTwoA.this, message, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(UpdataMobileTwoA.this, R.string.netError, Toast.LENGTH_SHORT).show();
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
		userInfoRequest.setTag("refreshUserInfo");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(userInfoRequest);

	}
   	
    /**
	 * 提交修改
	 */
	private void requestSubmit(){
		verCode = et_verCode.getText().toString();
		if (verCode.isEmpty()) {
			Toast.makeText(UpdataMobileTwoA.this, "请输入验证码!", Toast.LENGTH_LONG).show();
			return;
		}
		cpd.show();
		JSONObject jsonObj = new JSONObject();
	    try {
	    	
			jsonObj.put("mobile",mobile);
			jsonObj.put("type",Define.UPTATA_BINDPHONE_VERCODE);
			jsonObj.put("verifyCode",verCode);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    SessionJsonObjectRequest verificationCreateAccount = new SessionJsonObjectRequest(Method.POST, Define.URL+"user/validateVerifyCode", jsonObj, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d("debug", "response: "+ response.toString());
				try {
					String code = response.getString("code");
					final String message = response.getString("msg");
					if ("0".equals(code)) {

						updataMobile();
					}else {
						Toast.makeText(UpdataMobileTwoA.this, "验证码输入错误!", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.d("debug", "vercode = " + response.toString());
	    cpd.dismiss();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(UpdataMobileTwoA.this, R.string.netError, Toast.LENGTH_SHORT).show();
				Log.d("REQUEST-ERROR", error.getMessage(), error);
//           	  	byte[] htmlBodyBytes = error.networkResponse.data;
//           	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
				cpd.dismiss();
			}
		});
	    
	    //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
	    verificationCreateAccount.setTag("vriNewMobile");
	    //将请求加入全局队列中
	    MyApplication.getHttpQueues().add(verificationCreateAccount);
	}
	
	
	
	
	 @Override
	 protected void onStop() {
	   		super.onStop();
//	   		MyApplication.getHttpQueues().cancelAll("newMobileImgCode");
	   		MyApplication.getHttpQueues().cancelAll("newMobileCode");
	   		MyApplication.getHttpQueues().cancelAll("vriNewMobileAndCode");
	   		MyApplication.getHttpQueues().cancelAll("vriNewMobile");
	   		MyApplication.getHttpQueues().cancelAll("submitNewMobile");
	   		MyApplication.getHttpQueues().cancelAll("refreshUserInfo");
	   	}
}

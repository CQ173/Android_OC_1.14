package com.huoniao.oc.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.huoniao.oc.MainActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.PerfectInformationA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SPUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.huoniao.oc.MyApplication.infoNum;
import static com.huoniao.oc.MyApplication.waitAgency;
import static com.huoniao.oc.MyApplication.waitUser;
import static com.umeng.analytics.MobclickAgent.onProfileSignIn;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
//	 	private IWXAPI api;
	    private BaseResp resp = null;
	    // 获取第一步的code后，请求以下链接获取access_token
	    private String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	    // 获取用户个人信息
	    private String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
	    
//	    private String openid = "";
	    
	    /* 微信登录回调接口 */
//		public interface wechatLoginListenner{
//			void getOpenId(String openId);
//		} 
//		
//		private wechatLoginListenner mListenner;
//		
//		public void setWechatLogin(wechatLoginListenner listenner){
//			this.mListenner = listenner;
//		}
		
		
		private String id;//用户ID
		private String name;//用户姓名
		private String mobile;//手机号码
		private String userType;//用户类型
		private String auditState;//用户审核状态
		private String area_name;//归属区域
		private String officeType;//机构类型
		private String userCode;//用户编码
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
		private String isBinding;//第三方账号是否绑定标识
		private String parentId;//父账号id
		private String photoSrc;
		private String loginName;//用户名
		private String infoReceiveType;//消息接收方式
		private String roleName;//角色名
		private String dayActivity;//日访问量-管理员可见
		private String weekActivity;//周访问量-管理员可见
		private String monthActivity;//月访问量-管理员可见
		private String useRate;//本周使用率-管理员可见
		private String curDate;//统计时间
		private JSONObject activity;//活跃量统计
	//	private String corp_licence;//营业执照
	//	private String corp_card_fornt;//身份证正面
//	private String corp_card_rear;//身份证反面
		private JSONObject office;
		private String repayDay;
		private String dynaMinimum;
		private String provinceName;	//省份
		private static final String CARDNAME = "cardName";
		private static final String CARDNO = "cardNo";
		private static final String CARDTYPE="cardType";
		private static final String CARDID = "cardid";
		public static String IDENTITY_TAG;
		private Intent intent;
		private List<String> premissionsList = new ArrayList<>(); //权限
		private String officeId;//机构ID
//		private ProgressDialog pd;
//		private Handler mHandler = new Handler() {
//
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case RESULT_OPENID: {
//					
//					String open_id = (String) msg.obj;
//					mListenner.getOpenId(open_id);
//					Log.d("debug", open_id);
//					break;
//
//				}
//				
//				}
//			}
//		};
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        MyApplication.api = WXAPIFactory.createWXAPI(this, Define.WX_APPID, false);
	        MyApplication.api.handleIntent(getIntent(), this);
//	        pd = new CustomProgressDialog(WXEntryActivity.this, "正在登录中....", R.anim.frame_anim);
	    }

	    // 微信发送请求到第三方应用时，会回调到该方法
	    @Override
	    public void onReq(BaseReq req) {
	        finish();
	    }

	    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	    @Override
	    public void onResp(BaseResp resp) {
	        if (resp != null) {
	            resp = resp;
	        }
	        int a = resp.errCode;
	        switch (resp.errCode) {
	        case BaseResp.ErrCode.ERR_OK:
//	            Toast.makeText(this, "发送成功", Toast.LENGTH_LONG).show();
	            SendAuth.Resp newResp = (SendAuth.Resp) resp;      
	            //获取微信传回的code      
	            String code = newResp.code;

	            /*
	             * 将前面得到的AppID、AppSecret、code，拼接成URL 获取access_token等等的信息(微信)
	             */
	            String get_access_token = getCodeRequest(code);
	          
	            try {
					getAccessToken(get_access_token);
				} catch (Exception e) {
	
					e.printStackTrace();
				}

	            finish();
	            break;
	        case BaseResp.ErrCode.ERR_USER_CANCEL:
//	            Toast.makeText(this, "发送取消", Toast.LENGTH_LONG).show();
	        	Toast.makeText(this, "发送取消", Toast.LENGTH_LONG).show();
	            finish();
	            break;
	        case BaseResp.ErrCode.ERR_AUTH_DENIED:
	            Toast.makeText(this, "微信授权失败！", Toast.LENGTH_LONG).show();
	            finish();
	            break;
	        default:
	            Toast.makeText(this, "微信授权失败！", Toast.LENGTH_LONG).show();
	            finish();
	            break;
	       
	        }
	    }

	    /**
	     * 通过拼接的用户信息url获取用户信息
	     * 
	     *
	     */
//	    private void getUserInfo(String user_info_url) {
//	        AsyncHttpClient client = new AsyncHttpClient();
//	        client.get(user_info_url, new JsonHttpResponseHandler() {
//	            @Override
//	            public void onSuccess(int statusCode, JSONObject response) {
//	                // TODO Auto-generated method stub
//	                super.onSuccess(statusCode, response);
//	                try {
//
//	                    System.out.println("获取用户信息:" + response);
//
//	                    if (!response.equals("")) {
//	                        String openid = response.getString("openid");
//	                        String nickname = response.getString("nickname");
//	                        String headimgurl = response.getString("headimgurl");
//
//	                    }
//
//	                } catch (Exception e) {
//	                    // TODO Auto-generated catch block
//	                    e.printStackTrace();
//	                }
//	            }
//	        });
//	    }

	    @Override
	    protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);
	        setIntent(intent);
	        MyApplication.api.handleIntent(intent, this);
	        finish();
	    }

	    /**
	     * 获取access_token的URL（微信）
	     * 
	     * @param code
	     *            授权时，微信回调给的
	     * @return URL
	     */
	    private String getCodeRequest(String code) {
	        String result = null;
	        GetCodeRequest = GetCodeRequest.replace("APPID",
	                urlEnodeUTF8(Define.WX_APPID));
	        GetCodeRequest = GetCodeRequest.replace("SECRET",
	                urlEnodeUTF8(Define.WX_APPSECRET));
	        GetCodeRequest = GetCodeRequest.replace("CODE", urlEnodeUTF8(code));
	        result = GetCodeRequest;
	        return result;
	    }

	    /**
	     * 获取用户个人信息的URL（微信）
	     *
	     * @return URL
	     */
//	    private String getUserInfo(String access_token, String openid) {
//	        String result = null;
//	        GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN",
//	                urlEnodeUTF8(access_token));
//	        GetUserInfo = GetUserInfo.replace("OPENID", urlEnodeUTF8(openid));
//	        result = GetUserInfo;
//	        return result;
//	    }

	    private String urlEnodeUTF8(String str) {
	        String result = str;
	        try {
	            result = URLEncoder.encode(str, "UTF-8");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
	    
	    /**
		 *获取access_token
		 * 
		 * @throws Exception
		 */
		private void getAccessToken(String url) throws Exception {
//			this.mListenner = listenner;
			 JSONObject jsonObject = new JSONObject();
	    		try {
	    			jsonObject.put("", "");
	    		} catch (JSONException e1) {
	    
	    			e1.printStackTrace();
	    		}
	    		
	    		
	    		
	    		SessionJsonObjectRequest accessTokenRequest = new SessionJsonObjectRequest(Method.POST,
	    				url, jsonObject, new Listener<JSONObject>() {
	    			
	    					@Override
	    					public void onResponse(JSONObject response) {
	    						String data = response.toString();
	    						
	    						Log.d("debug", "response =" + data);
	    						try {
	    							if (!response.equals("")) {
	    								String type = "weixin";
	    								String unionid = response.getString("unionid");
	    								
	    								verifyAccount(unionid, type);
//	    								mListenner.getOpenId(openid);
//	    								new Thread(new Runnable() {
//											
//											@Override
//											public void run() {
//												Message msg = new Message();
//												msg.what = RESULT_OPENID;
//												msg.obj = openid;
//												mHandler.sendMessage(msg);
//												
//											}
//										}).start();
	    							}
	    						} catch (Exception e) {
	    							e.printStackTrace();
	    							Log.d("exciption", "exciption =" + e.toString());
	    						}
	    						}

	    					
	    						
	    				}, new ErrorListener() {

	    					@Override
	    					public void onErrorResponse(VolleyError error) {
//	    						pd.dismiss();
	    						Toast.makeText(WXEntryActivity.this, R.string.netError, Toast.LENGTH_SHORT).show();
	    						Log.d("debug", "error = " + error.toString());

	    					}
	    				});
	    		// 解决重复请求后台的问题
	    		accessTokenRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
	    				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
	    				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

	    		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
	    		accessTokenRequest.setTag("accessTokenRequest");
	    		// 将请求加入全局队列中
	    		MyApplication.getHttpQueues().add(accessTokenRequest);
		}
		
		
		/**
		 * 验证第微信账号是否已经注册
		 * 
		 * @throws Exception
		 */
		private void verifyAccount(final String openId, final String loginType) throws Exception {
//			pd.show();
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("openId", openId);
				jsonObject.put("loginType", loginType);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String url = Define.URL + "user/otherLogin";
			SessionJsonObjectRequest wxLoginRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
					new Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Log.d("debug", "response =" + response.toString());
							try {
								String responseCode = response.getString("code");
//								String message = response.getString("msg");
//								boolean flag = false;
								if ("0".equals(responseCode)) {

									try {

										waitAgency = response.getString("waitAgency")==null ?"--":response.getString("waitAgency");
										waitUser = response.getString("waitUser")==null ?"--":response.getString("waitUser");
										infoNum = response.getInt("infoNum");
									}catch (Exception e){
										e.printStackTrace();
									}

									JSONArray premissions = response.optJSONArray("premissions"); //权限
									Gson gson = new Gson();
									User userB= gson.fromJson(response.toString(),User.class);
									List<User.DataBean> data = userB.getData();
									if(data !=null && data.size()>0){
										User.DataBean.OfficeBean office = data.get(0).getOffice();
										ObjectSaveUtil.saveObject(WXEntryActivity.this,"mainAccountMapSetting",office);  //存储获取主账号地图数据
										MyApplication.payPasswordIsEmpty =data.get(0).getPayPasswordIsEmpty() ;  //记录用户是否设置过支付密码
									}

									if(premissions !=null) {
										premissionsList.clear();
										for (int i = 0; i < premissions.length(); i++) {
											premissionsList.add((String) premissions.get(i));
										}
									}

									JSONArray agreementArray = null;
									List<Integer> list = new ArrayList<Integer>();
									
									JSONArray jsonArray = response.getJSONArray("data");
									for (int i = 0; i < jsonArray.length(); i++) {  
									    JSONObject temp = (JSONObject) jsonArray.get(i);  
									    isBinding = temp.optString("isBinding");  
									    
									   
									}
									 	Log.d("test", "list=" + list);
//									 	  if (!"".equals(agreementArray) || agreementArray != null) {
//										    	 for (int j = 0; j < agreementArray.length(); j++) {  
//												    	
//												    	list.add(agreementArray.getInt(j));
//												    }
//											}
									 	//isBinding为空说明第三方账号已经绑定或注册本系统账号
									 	if (isBinding.isEmpty() || isBinding == null) {
											for (int i = 0; i < jsonArray.length(); i++) {
												JSONObject temp = (JSONObject) jsonArray.get(i);
												id = temp.optString("userId");
												parentId = temp.optString("parentId");
												name = temp.optString("name");
												mobile = temp.optString("mobile");
												userType = temp.optString("userType");
												auditState = temp.optString("auditState");
												balance = temp.optString("balanceString");// 余额
												minimum = temp.optString("minimum");// 账户最低限额
												//还款日
												repayDay = temp.optString("repayDay");
												//动态最低限额
												dynaMinimum = temp.optString("dynaMinimum");
												photoSrc = temp.optString("photoSrc");
												loginName = temp.optString("loginName");
												infoReceiveType = temp.optString("infoReceiveType");

												WXEntryActivity.this.office = temp.optJSONObject("office");
												IDENTITY_TAG = WXEntryActivity.this.office.optString("type");
//												ToastUtils.showToast(WXEntryActivity.this,IDENTITY_TAG);
												roleName = temp.optString("roleName");
												userCode = WXEntryActivity.this.office.optString("code");
												userState = WXEntryActivity.this.office.optString("state");
												area_name = WXEntryActivity.this.office.optJSONObject("area").getString("name");
												orgName = WXEntryActivity.this.office.optString("name");
												corpName = WXEntryActivity.this.office.optString("corpName");//
												corpMobile = WXEntryActivity.this.office.optString("corpMobile");
												corpIdNum = WXEntryActivity.this.office.optString("corpIdNum");
												operatorMobile = WXEntryActivity.this.office.optString("operatorMobile");
												operatorName = WXEntryActivity.this.office.optString("operatorName");
												operatorIdNum = WXEntryActivity.this.office.optString("operatorIdNum");
												address = WXEntryActivity.this.office.optString("address");
												master = WXEntryActivity.this.office.optString("master");
												contactPhone = WXEntryActivity.this.office.optString("phone");
												winNumber = WXEntryActivity.this.office.optString("winNumber");
												officeId = WXEntryActivity.this.office.optString("id");

												JSONObject areaJsonObject = WXEntryActivity.this.office.optJSONObject("area");  //下面的代码主要是用来获取省份的

												JSONObject parentObject = areaJsonObject.optJSONObject("parent");
												if(parentObject==null){
													provinceName = areaJsonObject.optString("name");
												}else {
													provinceName = parentObject.optString("name");    //省份
												}
												agreementArray = temp.optJSONArray("agreement");
												if (agreementArray != null && !"".equals(agreementArray)) {
													for (int j = 0; j < agreementArray.length(); j++) {

														list.add(agreementArray.getInt(j));
													}
												}

												ObjectSaveUtil.saveObject(WXEntryActivity.this, "agreementList", list); //存取地图是否设置过 "6" 表示这个过

											}
											Log.d("test", "list=" + list);
//								 	  if (!"".equals(agreementArray) || agreementArray != null) {
//									    	 for (int j = 0; j < agreementArray.length(); j++) {
//
//											    	list.add(agreementArray.getInt(j));
//											    }
//										}
											Integer[] array = new Integer[list.size()];
											for(int k=0;k<list.size();k++){
												array[k]=list.get(k);
											}
											//(String[])list.toArray(new String[list.size()]);
											String agreement = Arrays.toString(array);
											SPUtils.put(WXEntryActivity.this, "agreement", agreement);

											if (roleName.contains("系统管理员")) {
												activity = response.optJSONObject("activty");
												dayActivity = activity.optString("dayActivity");
												weekActivity = activity.optString("weekActivity");
												monthActivity = activity.optString("monthActivity");
												useRate = activity.optString("useRate");
												curDate = activity.optString("curDate");
											}

											User user = new User();
											user.setId(id);
											user.setParentId(parentId);
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
											user.setPhotoSrc(photoSrc);
											user.setPremissions(premissionsList); //权限
											user.setRoleName(roleName);
											user.setProvinceName(provinceName);
											user.setLoginName(loginName);
											user.setInfoReceiveType(infoReceiveType);
											user.setOfficeId(officeId);
											//主要是提现 切换账号清空银行卡缓存
											String acctNum=  (String)SPUtils.get(WXEntryActivity.this,"userId","");
											if(!acctNum.equals(id)){  //登录id不同清空缓存
												SPUtils.put(WXEntryActivity.this,CARDNO, "");  //存储银行卡卡号 作为显示
												SPUtils.put(WXEntryActivity.this,CARDNAME,"");  //存储银行姓名
												SPUtils.put(WXEntryActivity.this,CARDTYPE,""); //存储银行卡类型
												SPUtils.put(WXEntryActivity.this,CARDID,"");  //存储银行卡id  提现需要用到

												//用户切换切换账号 清除子账号用户协议记录的不弹出窗
												SPUtils.put(WXEntryActivity.this,"userAgreement",false);
											}

//											SPUtils.put(WXEntryActivity.this, "userName", userName);
//											SPUtils.put(WXEntryActivity.this, "password", passeword);
											SPUtils.put(WXEntryActivity.this,"repayDay",repayDay);
											SPUtils.put(WXEntryActivity.this,"dynaMinimum",dynaMinimum);
											SPUtils.put(WXEntryActivity.this,"balance",balance);
											SPUtils.put(WXEntryActivity.this,"minimum",minimum);
											SPUtils.put(WXEntryActivity.this,"userId",id);


											ObjectSaveUtil.saveObject(WXEntryActivity.this, "loginResult", user);


											intent = new Intent(WXEntryActivity.this, MainActivity.class);
//									    	intent.putExtra("userid", id);
											intent.putExtra("name", name);
											intent.putExtra("mobile", mobile);
											intent.putExtra("userType", userType);
											intent.putExtra("auditState", auditState);
											intent.putExtra("officeType", IDENTITY_TAG);
											intent.putExtra("dayActivity", dayActivity);
											intent.putExtra("weekActivity", weekActivity);
											intent.putExtra("monthActivity", monthActivity);
											intent.putExtra("useRate", useRate);
											intent.putExtra("curDate", curDate);

//											cpd.dismiss();
											Toast.makeText(WXEntryActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
											startActivity(intent);
											onProfileSignIn(user.getId()) ;  //友盟账号统计
											MobclickAgent.setDebugMode(true); //友盟统计调试模式
											MobclickAgent.openActivityDurationTrack(false);	// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
											MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);//友盟统计场景统计
											overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
										    
										}else {
//											pd.dismiss();
											intent = new Intent(WXEntryActivity.this, PerfectInformationA.class);
											intent.putExtra("openId", openId);
											intent.putExtra("loginType", loginType);
											
											startActivity(intent);
											overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
										}
									 
								}else {
//									pd.dismiss();
									
									Toast.makeText(WXEntryActivity.this, "用户名不存在或密码错误!", Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
//							pd.dismiss();
							Toast.makeText(WXEntryActivity.this, R.string.netError, Toast.LENGTH_SHORT).show();

						}
					});
			// 解决重复请求后台的问题
			wxLoginRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
			wxLoginRequest.setTag("wxLoginRequest");
			// 将请求加入全局队列中
			MyApplication.getHttpQueues().add(wxLoginRequest);

		}
		
		@Override
		protected void onStop() {
			super.onStop();
			MyApplication.getHttpQueues().cancelAll("accessTokenRequest");
			MyApplication.getHttpQueues().cancelAll("wxLoginRequest");
		}
}

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
import com.google.gson.Gson;
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
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.huoniao.oc.MyApplication.infoNum;
import static com.huoniao.oc.MyApplication.waitAgency;
import static com.huoniao.oc.MyApplication.waitUser;
import static com.umeng.analytics.MobclickAgent.onProfileSignIn;

public class PerfectInformationA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private TextView tv_bindHaveAccount, tv_registerBindAccount;
	private EditText et_userName, et_passWard;
	private Button bt_submit;
	private Intent intent;
	
	private String id;//用户ID
	private String parentId;//父账号id
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
//	private String corp_licence;//营业执照
//	private String corp_card_fornt;//身份证正面
//	private String corp_card_rear;//身份证反面
	private JSONObject office;
	private String openId;
	private String loginType;
	public static String IDENTITY_TAG;
	private ProgressDialog pd;
//	public static int infoNum=0;  //消息数量
//	public  static String waitAgency; //待审核窗口数量
//	public  static String waitUser; //待审核用户数量
	private String repayDay;
	private String dynaMinimum;
	private String provinceName;	//省份
	private String photoSrc;
	private String loginName;//用户名
	private String infoReceiveType;//消息接收方式
	private String roleName;//角色名
	private JSONObject activity;//活跃量统计
	private String dayActivity;//日访问量-管理员可见
	private String weekActivity;//周访问量-管理员可见
	private String monthActivity;//月访问量-管理员可见
	private String useRate;//本周使用率-管理员可见
	private String curDate;//统计时间

	private static final String CARDNAME = "cardName";
	private static final String CARDNO = "cardNo";
	private static final String CARDTYPE="cardType";
	private static final String CARDID = "cardid";
	private String userName;
	private String password;
	private String officeId;//机构ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_perfectinformation);
		initView();
		initData();
	}
	
	private void initData() {
		intent = getIntent();
		openId = intent.getStringExtra("openId");
		loginType = intent.getStringExtra("loginType");
		
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_bindHaveAccount = (TextView) findViewById(R.id.tv_bindHaveAccount);
		tv_registerBindAccount = (TextView) findViewById(R.id.tv_registerBindAccount);
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_passWard = (EditText) findViewById(R.id.et_passWard);
		bt_submit = (Button) findViewById(R.id.bt_submit);	
		iv_back.setOnClickListener(this);
		tv_bindHaveAccount.setOnClickListener(this);
		tv_registerBindAccount.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		pd = new CustomProgressDialog(PerfectInformationA.this, "正在登录中....", R.drawable.frame_anim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_submit:
			try {
				bindAccount(openId, loginType);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
			
		case R.id.tv_registerBindAccount:

			intent = new Intent(PerfectInformationA.this, RegisterA.class);
			intent.putExtra("openId", openId);
			intent.putExtra("loginType", loginType);		
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 第三方账号绑定OC账号
	 * 
	 * @throws Exception
	 */
	private void bindAccount(final String openId, final String loginType) throws Exception {
		pd.show();
		userName = et_userName.getText().toString();
		password = et_passWard.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("acctnum", userName);
			jsonObject.put("password", password);
			jsonObject.put("openId", openId);
			jsonObject.put("loginType", loginType);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = Define.URL + "user/otherLoginBinding";
		SessionJsonObjectRequest bindAccountRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
//							boolean flag = false;
							if ("0".equals(responseCode)) {
								Toast.makeText(PerfectInformationA.this, "账号绑定成功!", Toast.LENGTH_SHORT).show();

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
									ObjectSaveUtil.saveObject(PerfectInformationA.this,"mainAccountMapSetting",office);  //存储获取主账号地图数据
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

									office = temp.optJSONObject("office");
									IDENTITY_TAG = office.optString("type");
									roleName = temp.optString("roleName");
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
									officeId = office.optString("id");

									JSONObject areaJsonObject = office.optJSONObject("area");  //下面的代码主要是用来获取省份的

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

									ObjectSaveUtil.saveObject(PerfectInformationA.this, "agreementList", list); //存取地图是否设置过 "6" 表示这个过

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
								SPUtils.put(PerfectInformationA.this, "agreement", agreement);

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
								String acctNum=  (String)SPUtils.get(PerfectInformationA.this,"userId","");
								if(!acctNum.equals(id)){  //登录id不同清空缓存
									SPUtils.put(PerfectInformationA.this,CARDNO, "");  //存储银行卡卡号 作为显示
									SPUtils.put(PerfectInformationA.this,CARDNAME,"");  //存储银行姓名
									SPUtils.put(PerfectInformationA.this,CARDTYPE,""); //存储银行卡类型
									SPUtils.put(PerfectInformationA.this,CARDID,"");  //存储银行卡id  提现需要用到

									//用户切换切换账号 清除子账号用户协议记录的不弹出窗
									SPUtils.put(PerfectInformationA.this,"userAgreement",false);
								}

								/*if (userNameList.size() > 0){
									Collections.reverse(userNameList);     //实现list集合逆序排列
									for (int i = 0; i < userNameList.size() ; i++) {
										if (userName.equals(userNameList.get(i))){
											addUserNameFlag = false;
											break;
										}else {
											addUserNameFlag = true;
										}
									}
									if (addUserNameFlag == true){
										if (userNameList.size() < 5){
											userNameList.add(userName);
										}else {
											userNameList.remove(0);//如果用户名历史已超过5个，删除最前的那个，再把最新的添加进来
											userNameList.add(userName);
										}

									}
								}else {
									userNameList.add(userName);
								}*/
//								Collections.reverse(userNameList);     //实现list集合逆序排列
								SPUtils.put(PerfectInformationA.this, "userName", userName);
//								SPUtils2.putList(LoginA.this, "userNameList", userNameList);
								SPUtils.put(PerfectInformationA.this, "password", password);
								SPUtils.put(PerfectInformationA.this,"repayDay",repayDay);
								SPUtils.put(PerfectInformationA.this,"dynaMinimum",dynaMinimum);
								SPUtils.put(PerfectInformationA.this,"balance",balance);
								SPUtils.put(PerfectInformationA.this,"minimum",minimum);
								SPUtils.put(PerfectInformationA.this,"userId",id);




								ObjectSaveUtil.saveObject(PerfectInformationA.this, "loginResult", user);


//					    if (Define.OUTLETS_NOTPASS.equals(auditState)) {
//					    	pd.dismiss();
//					    	Toast.makeText(LoginA.this, "您提交的资料审核未通过，请到电脑端登录O计系统修改个人资料重新提交审核！", Toast.LENGTH_LONG).show();
//					    }else {
								intent = new Intent(PerfectInformationA.this, MainActivity.class);
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


								cpd.dismiss();
								Toast.makeText(PerfectInformationA.this, "登录成功!", Toast.LENGTH_SHORT).show();
								startActivity(intent);
								onProfileSignIn(user.getId()) ;  //友盟账号统计
								MobclickAgent.setDebugMode(true); //友盟统计调试模式
								MobclickAgent.openActivityDurationTrack(false);	// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
								MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);//友盟统计场景统计
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								finish();
								 
							}else {
								pd.dismiss();
				
								Toast.makeText(PerfectInformationA.this, message, Toast.LENGTH_SHORT).show();
								Log.d("errorInfo", message.toString());
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
						Toast.makeText(PerfectInformationA.this, R.string.netError, Toast.LENGTH_SHORT).show();

					}
				});
		// 解决重复请求后台的问题
		bindAccountRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		bindAccountRequest.setTag("bindAccountRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(bindAccountRequest);

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("bindAccountRequest");

	}
}

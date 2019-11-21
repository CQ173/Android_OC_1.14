package com.huoniao.oc.user;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.huoniao.oc.MainActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.CountDownTimerUtils;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.FileUtils;
import com.huoniao.oc.util.HttpUtils;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SPUtils;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SpinerPopWindow;
import com.huoniao.oc.versionupdate.UpdateManager;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.huoniao.oc.MyApplication.infoNum;
import static com.huoniao.oc.MyApplication.waitAgency;
import static com.huoniao.oc.MyApplication.waitUser;
import static com.umeng.analytics.MobclickAgent.onProfileSignIn;

public class LoginA extends Activity implements OnClickListener, MySpinerAdapter.IOnItemSelectListener{
	private Button bt_login;
	private Intent intent;
	private EditText et_userName,  et_passaword;
	private TextView tv_getVericode;
	private TextView tv_forgetPassword;
	private TextView tv_register;
	public static String IDENTITY_TAG;
	private String userName;
	private boolean addUserNameFlag = false;//用来标识是否可以添加到历史用户名集合中,false不能/true能
	private String passeword;

	private static final String CARDNAME = "cardName";
	private static final String CARDNO = "cardNo";
	private static final String CARDTYPE="cardType";
	private static final String CARDID = "cardid";
	//	private CheckBox cb_rememberAccount;
	//图片验证码
	private ImageView imageVerCode;
	private EditText imageVerCodeInput;

	private ImageView iv_weiXin, iv_qq, iv_zhiFuBao, iv_passVisual, iv_delete, iv_selectUserName;
	private LinearLayout ll_userName;
	private List<String> userNameList = new ArrayList<String>();
	private SpinerPopWindow mSpinerPopWindow;
	private String CHOICE_TAG;//
	CountDownTimerUtils mCountDownTimerUtils;
//	private ProgressDialog cpd;




	private String officeId;//机构ID
	private String id;//用户ID
	private String name;//用户姓名
	private String mobile;//手机号码
	private String userType;//用户类型
	private String auditState;//用户审核状态
	private String area_name;//归属区域
	private String officeType;//机构类型
	private String userCode;//用户编码
	private String loginName;//用户名
	private String infoReceiveType;//消息接收方式
	private String orgName;//机构名称
	private String userState;//用户状态
	private String winNumber;//窗口号
	private String corpName;//法人姓名
	private String corpMobile;//法人手机
	private String corpIdNum;//法人身份证号
	private String operatorName;//负责人姓名
	private String operatorMobile;//负责人手机
	private String operatorIdNum;//负责人身份证号
	private String master;//联系人
	private String contactPhone;//联系人电话
	private String address;//联系地址
	private String balance;//余额
	private String minimum;//账户最低限额
	private String isBinding;//第三方账号是否绑定标识
	private String parentId;//父账号id
	private String photoSrc;
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

	private List<String> premissionsList = new ArrayList<>(); //权限

	String TAG = "LOGIN_TAG";
	private boolean VISUAL_TAG = true;
	//    public static int infoNum=0;  //消息数量
	//QQ登录相关
	//正式QQ_APPID
//	private String QQ_APPID = "1105896121";
	//测式QQ_APPID
//	private String QQ_APPID = "1105977048";
	private Tencent mTencent;
	private IUiListener qqLoginListener;
	//首先需要用APP ID 获取到一个Tencent实例
//    mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());
	//初始化一个IUiListener对象，在IUiListener接口的回调方法中获取到有关授权的某些信息
	// （千万别忘了覆写onActivityResult方法，否则接收不到回调）
//    qqLoginListener = new LogInListener();

	private static final int UNION_RESULT = 1;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case UNION_RESULT:
					String uniResult = (String) msg.obj;
//				Toast.makeText(LoginA.this, "uniResult =" + uniResult, Toast.LENGTH_SHORT).show();
					String[] split = uniResult.split(":");
					uniResult = split[split.length - 1];
//				uniResult = split[1];
					split = uniResult.split("\"");
					uniResult = split[1];
//	            uniResult = split[0];
					String unionid = uniResult;
//	            Toast.makeText(LoginA.this, "unionid =" + unionid, Toast.LENGTH_SHORT).show();
					String type = "qq";
					try {
						verifyAccount(unionid, type);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;


			}

		}
	};
	private String repayDay;
	private String dynaMinimum;
	private String provinceName;	//省份
	private CustomProgressDialog cpd;
	//	public  static String waitAgency; //待审核窗口数量
//	public  static String waitUser; //待审核用户数量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppBaseTheme);
		super.onCreate(savedInstanceState);
//		setContentView(R.activity_windowanchored.activity_login);
		setConfigure();
		setContentView(R.layout.activity_newlogin);
		initView();
		initData();

	}

	private void setConfigure() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题
		setRequestedOrientation(ActivityInfo
				.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		ExitApplication.getInstance().addActivity(this);
		//PushAgent.getInstance(this).onAppStart();

		if(cpd == null) {
			cpd = new CustomProgressDialog(this, "正在登录中...", R.drawable.frame_anim);
			cpd.setCancelable(false);//设置进度条不可以按退回键取消
			cpd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
		}
	}

	UpdateManager manager = new UpdateManager(LoginA.this);
	private void initData() {
//		final ProgressDialog cd = new CustomProgressDialog(LoginA.this, "正在检查更新....", R.anim.frame_anim);
//		cd.show();
//
//		manager.getServiceVersionInfo(new UpdateManager.versionUpdateNotifyListenner() {
//			@Override
//			public void UpdateCheckResult(boolean result, boolean bNeedUpdate) {
//
//				if (!result) {
//					cd.dismiss();
//					Toast.makeText(LoginA.this, "服务器版本更新状态异常，请稍候重试", Toast.LENGTH_SHORT).show();
////					finish();
//					return;
//				}
//
//
//				if (!bNeedUpdate) {
//					cd.dismiss();
//					Toast.makeText(LoginA.this, R.string.soft_update_no, Toast.LENGTH_SHORT).show();

		userName = (String)SPUtils.get(LoginA.this, "userName", "");
		passeword = (String)SPUtils.get(LoginA.this, "password", "");
		List<String> userNameList2 = SPUtils2.getList(this, "userNameList");
		if (userNameList2 != null){
			userNameList.addAll(userNameList2);
		}


		if (!userName.isEmpty() && !passeword.isEmpty()) {
			doLogin();
		}


//					return;
//				}
//				cd.dismiss();
//				manager.checkUpdate();
//			}
//		});
//		UpdateManager manager = new UpdateManager(LoginA.this);
//
//		// 检查软件更新
//		manager.checkUpdate();

	}

	private void initView() {
		FileUtils.deleteFileCacle();//清除图片缓存
		bt_login = (Button) findViewById(R.id.bt_login);
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_passaword = (EditText) findViewById(R.id.et_passWard);
//		tv_getVericode = (TextView) findViewById(R.id.tv_getVericode);
		tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);
		tv_register = (TextView) findViewById(R.id.tv_register);
//		cb_rememberAccount = (CheckBox) findViewById(R.id.cb_rememberAccount);
		iv_weiXin = (ImageView) findViewById(R.id.weiXin_login);
		iv_qq = (ImageView) findViewById(R.id.qq_login);
		iv_passVisual = (ImageView) findViewById(R.id.iv_passVisual);
		iv_delete = (ImageView) findViewById(R.id.iv_delete);
		iv_selectUserName = (ImageView) findViewById(R.id.iv_selectUserName);
		ll_userName = (LinearLayout) findViewById(R.id.ll_userName);
		et_userName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					iv_delete.setVisibility(View.GONE);
				} else {
					iv_delete.setVisibility(View.VISIBLE);
				}

			}
		});
//		iv_zhiFuBao = (ImageView) findViewById(R.id.zhifubao_login);

//		if ((Boolean) SPUtils.get(LoginA.this, "checkboxBoolean", false)){
//			et_userName.setText((String)SPUtils.get(LoginA.this, "userName", ""));
//			et_passaword.setText((String)SPUtils.get(LoginA.this, "password", ""));
//			cb_rememberAccount.setChecked(true);
//			}

		bt_login.setOnClickListener(this);
//		tv_getVericode.setOnClickListener(this);
		tv_forgetPassword.setOnClickListener(this);
		tv_register.setOnClickListener(this);
		iv_weiXin.setOnClickListener(this);
		iv_qq.setOnClickListener(this);
		iv_passVisual.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
		iv_selectUserName.setOnClickListener(this);
//		iv_zhiFuBao.setOnClickListener(this);



		et_userName.setText(SPUtils.get(this,"userName","")+"");  //退出登录界面 再次打开登录界面显示用户名
		et_passaword.setText(SPUtils.get(this,"password","")+"");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			//获取验证码
//		case R.id.tv_getVericode:
//			userName = et_userName.getText().toString();
//			passeword = et_passaword.getText().toString();
//			if ("".equals(userName) || userName == null) {
//				Toast.makeText(LoginA.this, "请输入手机号码!", Toast.LENGTH_SHORT).show();
//			}else {
//				showImageVeriCodeDialog();
//
//			}
//
//
//			break;

			// 忘了密码
			case R.id.tv_forgetPassword:
				intent = new Intent(LoginA.this, ForgetPasswordA.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;
			// 注册用户
			case R.id.tv_register:
				intent = new Intent(LoginA.this, RegisterA.class);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;

			//删除文本信息
			case R.id.iv_delete:
				et_userName.setText("");
				break;
			//选择历史登录账号
			case R.id.iv_selectUserName:
				if (userNameList.size() > 0) {
					mSpinerPopWindow = new SpinerPopWindow(this);
					CHOICE_TAG = "selectUserName";
					mSpinerPopWindow.refreshData(userNameList, 0);
					mSpinerPopWindow.setItemListener(this);
					showSpinWindow(ll_userName);
				}
				break;
			// 设置密码是否可见
			case R.id.iv_passVisual:
				if (VISUAL_TAG) {
					et_passaword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					VISUAL_TAG = false;
				}else {
					et_passaword.setTransformationMethod(PasswordTransformationMethod.getInstance());
					VISUAL_TAG =true;
				}
				break;
			//登录
			case R.id.bt_login:

				userName = et_userName.getText().toString();
				passeword = et_passaword.getText().toString();

//			String url = "http://192.168.0.117:8181/OC/app/user/userLogin";
//			volleyJSONObjectPost();



				if ("".equals(userName) || userName == null || "".equals(passeword) || passeword == null) {
					Toast.makeText(LoginA.this, "用户名或密码不能为空!", Toast.LENGTH_SHORT).show();
				}else {
					doLogin();


				}

//
				break;

			// 微信登录
			case R.id.weiXin_login:
//			Toast.makeText(LoginA.this, "正在建设中!", Toast.LENGTH_LONG).show();
				MyApplication.api = WXAPIFactory.createWXAPI(this, Define.WX_APPID, true);
				MyApplication.api.registerApp(Define.WX_APPID);
				SendAuth.Req req = new SendAuth.Req();
				//授权读取用户信息
				req.scope = "snsapi_userinfo";
				//自定义信息
				req.state = "wechat_sdk_oc_test";
				//向微信发送请求
				MyApplication.api.sendReq(req);

//			WXEntryActivity activity = new WXEntryActivity();
//			activity.setWechatLogin(new WXEntryActivity.wechatLoginListenner() {
//
//				@Override
//				public void getOpenId(String openId) {
//					String type = "weixin";
//					if (!openId.isEmpty()) {
//						try {
//							verifyAccount(openId, type);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//
//				}
//			});

				break;
			// QQ登录
			case R.id.qq_login:
				//首先需要用APP ID 获取到一个Tencent实例
				mTencent =  Tencent.createInstance(Define.QQ_APPID, getApplicationContext());
				//初始化一个IUiListener对象，在IUiListener接口的回调方法中获取到有关授权的某些信息
				// （千万别忘了覆写onActivityResult方法，否则接收不到回调）
				mTencent.login(this, "all", new LogInListener());


				break;
			// 支付宝登录
//		case R.id.zhifubao_login:
//			Toast.makeText(LoginA.this, "正在建设中!", Toast.LENGTH_LONG).show();
//			break;
			default:
				break;
		}

	}

	private void showSpinWindow(View v) {
		Log.e("", "showSpinWindow");
		mSpinerPopWindow.setWidth(v.getWidth());
		mSpinerPopWindow.showAsDropDown(v);
	}

	// 选择历史用户名
	private void setUserName(int pos) {
		if (pos >= 0 && pos <= userNameList.size()) {
			String value = userNameList.get(pos);

			et_userName.setText(value);

			userName = et_userName.getText().toString();

		}
	}


	private void doLogin() {
		String url = Define.URL + "user/userLogin";
		//	http://192.168.0.152:8080/OC/app/user/userLogin
		DateUtils.getNetDate(); //初始化网络时间
		cpd.show();

		final JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("acctNum",userName);
			jsonObj.put("password",passeword);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final SessionJsonObjectRequest loginRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObj, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {//jsonObject为请求成功返回的Json格式数据
				if (response == null) {
					Toast.makeText(LoginA.this, "服务器数据异常！", Toast.LENGTH_SHORT).show();
					cpd.dismiss();
					return;
				}
				Log.d("debug", "response: "+ response.toString());
				try {
					String code = response.getString("code");

					if ("0".equals(code)) {
						try {
							infoNum = response.getInt("infoNum");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						try {
							waitAgency = response.getString("waitAgency")==null ?"--":response.getString("waitAgency");
							waitUser = response.getString("waitUser")==null ?"--":response.getString("waitUser");
						}catch (Exception e){
							e.printStackTrace();
						}

						JSONArray premissions = response.optJSONArray("premissions"); //权限
						Gson gson = new Gson();
						User userB= gson.fromJson(response.toString(),User.class);
						List<User.DataBean> data = userB.getData();
						if(data !=null && data.size()>0){
							User.DataBean.OfficeBean office = data.get(0).getOffice();
							ObjectSaveUtil.saveObject(LoginA.this,"mainAccountMapSetting",office);  //存储获取主账号地图数据
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

							LoginA.this.office = temp.optJSONObject("office");
							IDENTITY_TAG = LoginA.this.office.optString("type");
							roleName = temp.optString("roleName");
							userCode = LoginA.this.office.optString("code");
							userState = LoginA.this.office.optString("state");
							area_name = LoginA.this.office.optJSONObject("area").getString("name");
							orgName = LoginA.this.office.optString("name");
							corpName = LoginA.this.office.optString("corpName");//
							corpMobile = LoginA.this.office.optString("corpMobile");
							operatorMobile = LoginA.this.office.optString("operatorMobile");
							corpIdNum = LoginA.this.office.optString("corpIdNum");
							operatorName = LoginA.this.office.optString("operatorName");
							operatorIdNum = LoginA.this.office.optString("operatorIdNum");
							address = LoginA.this.office.optString("address");
							master = LoginA.this.office.optString("master");
							contactPhone = LoginA.this.office.optString("phone");
							winNumber = LoginA.this.office.optString("winNumber");
							officeId = LoginA.this.office.optString("id");

							JSONObject areaJsonObject = LoginA.this.office.optJSONObject("area");  //下面的代码主要是用来获取省份的

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

							ObjectSaveUtil.saveObject(LoginA.this, "agreementList", list); //存取地图是否设置过 "6" 表示这个过

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
						SPUtils.put(LoginA.this, "agreement", agreement);

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
						String acctNum=  (String)SPUtils.get(LoginA.this,"userId","");
						if(!acctNum.equals(id)){  //登录id不同清空缓存
							SPUtils.put(LoginA.this,CARDNO, "");  //存储银行卡卡号 作为显示
							SPUtils.put(LoginA.this,CARDNAME,"");  //存储银行姓名
							SPUtils.put(LoginA.this,CARDTYPE,""); //存储银行卡类型
							SPUtils.put(LoginA.this,CARDID,"");  //存储银行卡id  提现需要用到

							//用户切换切换账号 清除子账号用户协议记录的不弹出窗
							SPUtils.put(LoginA.this,"userAgreement",false);
						}

						if (userNameList.size() > 0){
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
						}
						Collections.reverse(userNameList);     //实现list集合逆序排列
						SPUtils.put(LoginA.this, "userName", userName);
						SPUtils2.putList(LoginA.this, "userNameList", userNameList);
						SPUtils.put(LoginA.this, "password", passeword);
						SPUtils.put(LoginA.this,"repayDay",repayDay);
						SPUtils.put(LoginA.this,"dynaMinimum",dynaMinimum);
						SPUtils.put(LoginA.this,"balance",balance);
						SPUtils.put(LoginA.this,"minimum",minimum);
						SPUtils.put(LoginA.this,"userId",id);




						ObjectSaveUtil.saveObject(LoginA.this, "loginResult", user);


//					    if (Define.OUTLETS_NOTPASS.equals(auditState)) {
//					    	pd.dismiss();
//					    	Toast.makeText(LoginA.this, "您提交的资料审核未通过，请到电脑端登录O计系统修改个人资料重新提交审核！", Toast.LENGTH_LONG).show();
//					    }else {
						intent = new Intent(LoginA.this, MainActivity.class);
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
						Toast.makeText(LoginA.this, "登录成功!", Toast.LENGTH_SHORT).show();
						startActivity(intent);
						onProfileSignIn(user.getId()) ;  //友盟账号统计
						MobclickAgent.setDebugMode(true); //友盟统计调试模式
						MobclickAgent.openActivityDurationTrack(false);	// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
						MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);//友盟统计场景统计
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//					    }
					}else {
						cpd.dismiss();

						Toast.makeText(LoginA.this, "用户名不存在或密码错误!", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
					Toast.makeText(LoginA.this, "数据异常", Toast.LENGTH_SHORT).show();
					cpd.dismiss();
				}



			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {//jsonObject为请求失败返回的Json格式数据
				cpd.dismiss();
				if(error.toString().contains("ServerError")){
					Toast.makeText(LoginA.this, "服务器异常，请稍后重试...", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(LoginA.this, R.string.netError, Toast.LENGTH_SHORT).show();
					Log.d("REQUEST-ERROR", error.getMessage(), error);
//			byte[] htmlBodyBytes = error.networkResponse.data;
//			Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
				}
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
		loginRequest.setTag(TAG);
		//将请求加入全局队列中
		MyApplication.getHttpQueues().add(loginRequest);
	}

	private void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String openid = jsonObject.getString("openid");
			String token = jsonObject.getString("access_token");
			String expires = jsonObject.getString("expires_in");

			getQQUnionid(token);

			mTencent.setAccessToken(token, expires);
			mTencent.setOpenId(openid);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getQQUnionid(String token) {

//			pd.show();
//			Toast.makeText(LoginA.this, "token =" + token, Toast.LENGTH_SHORT).show();
		final String url = "https://graph.qq.com/oauth2.0/me?access_token=" + token + "&unionid=1";

		new Thread(new Runnable() {

			@Override
			public void run() {
				String resultStr = HttpUtils.getJsonContent(url);

				Message msg = new Message();
				msg.what = UNION_RESULT;
				msg.obj = resultStr;
				mHandler.sendMessage(msg);

			}
		}).start();
//			SessionJsonObjectRequest qqUnionidRequest = new SessionJsonObjectRequest(Method.GET,
//					url, null, new Listener<JSONObject>() {
//
//						@Override
//						public void onResponse(JSONObject response) {
//							String data = response.toString();
//							if (!data.isEmpty() && data != null) {
//								Log.d("debug", "response =" + data);
//								String s = new String(data);
//
//					            String[] split = s.split(":");
//					            s = split[split.length - 1];
//					            split = s.split("\"");
//					            s = split[1];
//					            String unionid = s;
//					            String type = "qq";
//					            try {
//									verifyAccount(unionid, type);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//							}
//
//					}, new ErrorListener() {
//
//						@Override
//						public void onErrorResponse(VolleyError error) {
//							pd.dismiss();
//							Toast.makeText(LoginA.this, "请求失败!", Toast.LENGTH_SHORT).show();
//							Log.d("debug", "error = " + error.toString());
//
//						}
//					});
//			// 解决重复请求后台的问题
//			qqUnionidRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
//					DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
//					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//			// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
//			qqUnionidRequest.setTag("qqUnionidRequest");
//			// 将请求加入全局队列中
//			MyApplication.getHttpQueues().add(qqUnionidRequest);


	}
//    private void getUserInfo() {
//
//        //sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
//        QQToken mQQToken = mTencent.getQQToken();
//        UserInfo userInfo = new UserInfo(LoginA.this, mQQToken);
//        userInfo.getUserInfo(new IUiListener() {
//                                 @Override
//                                 public void onComplete(final Object o) {
//                                     JSONObject userInfoJson = (JSONObject) o;
//                                     Message msgNick = new Message();
//                                     msgNick.what = 0;//昵称
//                                     try {
//                                         msgNick.obj = userInfoJson.getString("nickname");//直接传递一个昵称的内容过去
//                                     } catch (JSONException e) {
//                                         e.printStackTrace();
//                                     }
//                                     mHandler.sendMessage(msgNick);
//                                     //子线程 获取并传递头像图片，由Handler更新
//                                     new Thread(new Runnable() {
//                                         @Override
//                                         public void run() {
//                                             Bitmap bitmapHead = null;
//                                             if (((JSONObject) o).has("figureurl")) {
//                                                 try {
//                                                     String headUrl = ((JSONObject) o).getString("figureurl_qq_2");
//                                                     bitmapHead = Util.getbitmap(headUrl);
//                                                 } catch (JSONException e) {
//                                                     e.printStackTrace();
//                                                 }
//                                                 Message msgHead = new Message();
//                                                 msgHead.what = 1;
//                                                 msgHead.obj = bitmapHead;
//                                                 mHandler.sendMessage(msgHead);
//                                             }
//                                         }
//                                     }).start();
//
//                                 }
//
//                                 @Override
//                                 public void onError(UiError uiError) {
//                                     Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
//                                     Toast.makeText(LoginA.this, "获取qq用户信息错误", Toast.LENGTH_SHORT).show();
//                                 }
//
//                                 @Override
//                                 public void onCancel() {
//                                     Log.e("GET_QQ_INFO_CANCEL", "获取qq用户信息取消");
//                                     Toast.makeText(LoginA.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
//                                 }
//                             }
//        );
//    }

	/**
	 *  通过Volley加载网络图片
	 *
	 *  new ImageRequest(String url,Listener listener,int maxWidth,int maxHeight,Config decodeConfig,ErrorListener errorListener)
	 *  url：请求地址
	 *  listener：请求成功后的回调
	 *  maxWidth、maxHeight：设置图片的最大宽高，如果均设为0则表示按原尺寸显示
	 *  decodeConfig：图片像素的储存方式。Config.RGB_565表示每个像素占2个字节，Config.ARGB_8888表示每个像素占4个字节等。
	 *  errorListener：请求失败的回调
	 */
//    private void loadImageByVolley(String url, final ImageView image) {
////        String url = "http://ecy.120368.com/api/buildIconCode";
//        ImageRequest request = new ImageRequest(
//                            url,
//                            new Listener<Bitmap>() {
//                                @Override
//                                public void onResponse(Bitmap bitmap) {
//                                    image.setImageBitmap(bitmap);
//                                }
//                            },
//                            0, 0, Config.RGB_565,
//                            new ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError volleyError) {
//                                    image.setImageResource(R.mipmap.ic_launcher);
//                                }
//                            }){
//        	@Override
//        	protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
//        		 Response<Bitmap> superResponse = super
//                         .parseNetworkResponse(response);
//                 Map<String, String> responseHeaders = response.headers;
//                 String rawCookies = responseHeaders.get("Set-Cookie");
//                 //Define是一个自建的类，存储常用的全局变量
//                 Define.localCookie = rawCookies.substring(0, rawCookies.indexOf(";"));
//                 Log.d("sessionid", "sessionid----------------" + Define.localCookie);
//        		return superResponse;
//        	}
//        };
//
//        //解决重复请求后台的问题
//        request.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        10*1000,//默认超时时间，应设置一个稍微大点儿的
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                )
//        );
//        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
//        request.setTag("loadImage");
//        //通过Tag标签取消请求队列中对应的全部请求
//        MyApplication.getHttpQueues().add(request);
//    }
	/**
	 * 获取验证码


	 private void showImageVeriCodeDialog(){
	 LayoutInflater inflater = LayoutInflater.from(this);
	 View view = inflater.inflate(R.activity_windowanchored.dialog_imagevericode, null);
	 imageVerCode = (ImageView) view.findViewById(R.id.imageVeriCode);
	 imageVerCodeInput = (EditText) view.findViewById(R.id.et_imageVeriCode);
	 Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
	 Button bt_cancel = (Button) view.findViewById(R.id.bt_concel);

	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 builder.setView(view);
	 final AlertDialog dialog = builder.create();//获取dialog
	 final String imageUrl = "http://ecy.120368.com/app/user/buildIconCode";
	 loadImageByVolley(imageUrl, imageVerCode);

	 imageVerCode.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {
	loadImageByVolley(imageUrl, imageVerCode);

	}
	});

	 //确定按钮，在这里判断图片验证码是否正确
	 bt_confirm.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {
	JSONObject jsonObj = new JSONObject();
	try {
	String imageCode = imageVerCodeInput.getText().toString();
	userName = et_userName.getText().toString();
	jsonObj.put("mobile",userName);
	jsonObj.put("iconCode",imageCode);
	Log.d("debug", "imageCode = "+ imageCode);
	} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	JsonObjectRequest request = new JsonObjectRequest(Method.POST, "http://ecy.120368.com/app/user/validateMobile", jsonObj, new Listener<JSONObject>() {

	@Override
	public void onResponse(JSONObject response) {
	try {
	String code = response.getString("code");
	if ("0".equals(code)) {
	dialog.dismiss();
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("mobile",userName);
	jsonObject.put("type", Define.REGISTE_VERCODE);
	SessionJsonObjectRequest phoneVerCodeRequest = new SessionJsonObjectRequest(Method.POST, "http://ecy.120368.com/app/user/getVerifyCode",
	jsonObject, new Listener<JSONObject>() {

	@Override
	public void onResponse(JSONObject response) {
	mCountDownTimerUtils = new CountDownTimerUtils(tv_getVericode, 60000, 1000);
	mCountDownTimerUtils.start();

	}
	}, new ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
	Toast.makeText(LoginA.this, "请求失败!", Toast.LENGTH_LONG).show();

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
	phoneVerCodeRequest.setTag("registerCode");
	//将请求加入全局队列中
	MyApplication.getHttpQueues().add(phoneVerCodeRequest);
	//									mCountDownTimerUtils = new CountDownTimerUtils(tv_getVericode, 60000, 1000);
	//									mCountDownTimerUtils.start();
	}else {
	Toast.makeText(LoginA.this, "验证码输入错误!", Toast.LENGTH_LONG).show();
	}
	} catch (JSONException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}

	Log.d("debug", "vercode = " + response.toString());
	dialog.dismiss();
	}
	}, new ErrorListener() {

	@Override
	public void onErrorResponse(VolleyError error) {
	Toast.makeText(LoginA.this, error.toString(), Toast.LENGTH_SHORT).show();

	}
	}){
	//重写getHeaders 默认的key为cookie，value则为localCookie
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
	if (Define.localCookie != null && Define.localCookie.length() > 0) {
	HashMap<String, String> headers = new HashMap<String, String>();
	headers.put("cookie", Define.localCookie);
	Log.d("调试", "headers----------------" + headers);
	return headers;
	}else {
	return super.getHeaders();
	}
	}
	};

	//设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
	request.setTag("ImageCodePost");
	//将请求加入全局队列中
	MyApplication.getHttpQueues().add(request);
	}
	});

	 bt_cancel.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View v) {
	dialog.dismiss();

	}
	});
	 dialog.show();


	 }
	 */

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
				//ExitApplication.getInstance().exit();
				MyApplication.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(int pos) {
		if ("selectUserName".equals(CHOICE_TAG)) {
			setUserName(pos);
		}
	}

	/**
	 * Volley与Activity生命周期联动
	 * 当Activity执行onStop()时,将请求队列取消
	 */

	private class LogInListener implements IUiListener {

		@Override
		public void onComplete(Object o) {
			Toast.makeText(LoginA.this, "授权成功！", Toast.LENGTH_LONG).show();
			System.out.println("o.toString() ------------------------->        " + o.toString());
			JSONObject object = (JSONObject) o;

			//设置openid和token，否则获取不到下面的信息
			initOpenidAndToken(object);
			;
			Log.d("thread",Thread.currentThread().getName());
		}

		@Override
		public void onError(UiError uiError) {

			Toast.makeText(LoginA.this, "授权出错！", Toast.LENGTH_LONG).show();
			Log.d("thread",Thread.currentThread().getName());
		}

		@Override
		public void onCancel() {
			Toast.makeText(LoginA.this, "授权取消！", Toast.LENGTH_LONG).show();

			Log.d("thread",Thread.currentThread().getName());
		}
	}

	//确保能接收到回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Tencent.onActivityResultData(requestCode, resultCode, data, qqLoginListener);
	}

	/**
	 * 验证第三方账号是否已经注册
	 *
	 * @throws Exception
	 */
	private void verifyAccount(final String openId, final String loginType) throws Exception {
		DateUtils.getNetDate(); //初始化网络时间
		cpd.show();
//		Toast.makeText(LoginA.this, "进验证请求了", Toast.LENGTH_SHORT).show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("openId", openId);
			jsonObject.put("loginType", loginType);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block.
			e1.printStackTrace();
		}
		String url = Define.URL + "user/otherLogin";
		SessionJsonObjectRequest qqLoginRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
//						Toast.makeText(LoginA.this, "验证请求成功了！", Toast.LENGTH_SHORT).show();
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");

//							String message = response.getString("msg");
//							boolean flag = false;
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
									ObjectSaveUtil.saveObject(LoginA.this,"mainAccountMapSetting",office);  //存储获取主账号地图数据
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
//								 	  if (!"".equals(agreementArray) || agreementArray != null) {
//									    	 for (int j = 0; j < agreementArray.length(); j++) {
//
//											    	list.add(agreementArray.getInt(j));
//											    }
//										}
								//isBinding为空说明第三方账号已经绑定或注册本系统账号
								if (isBinding.isEmpty() || isBinding == null) {
//								 		Toast.makeText(LoginA.this, "已绑定OC账号，直接登录！", Toast.LENGTH_SHORT).show();
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

										LoginA.this.office = temp.optJSONObject("office");
										IDENTITY_TAG = LoginA.this.office.optString("type");
										roleName = temp.optString("roleName");
										userCode = LoginA.this.office.optString("code");
										userState = LoginA.this.office.optString("state");
										area_name = LoginA.this.office.optJSONObject("area").getString("name");
										orgName = LoginA.this.office.optString("name");
										corpName = LoginA.this.office.optString("corpName");//
										corpMobile = LoginA.this.office.optString("corpMobile");
										corpIdNum = LoginA.this.office.optString("corpIdNum");
										operatorMobile = LoginA.this.office.optString("operatorMobile");
										operatorName = LoginA.this.office.optString("operatorName");
										operatorIdNum = LoginA.this.office.optString("operatorIdNum");
										address = LoginA.this.office.optString("address");
										master = LoginA.this.office.optString("master");
										contactPhone = LoginA.this.office.optString("phone");
										winNumber = LoginA.this.office.optString("winNumber");
										officeId = LoginA.this.office.optString("id");

										JSONObject areaJsonObject = LoginA.this.office.optJSONObject("area");  //下面的代码主要是用来获取省份的

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

										ObjectSaveUtil.saveObject(LoginA.this, "agreementList", list); //存取地图是否设置过 "6" 表示这个过

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
									SPUtils.put(LoginA.this, "agreement", agreement);

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
									String acctNum=  (String)SPUtils.get(LoginA.this,"userId","");
									if(!acctNum.equals(id)){  //登录id不同清空缓存
										SPUtils.put(LoginA.this,CARDNO, "");  //存储银行卡卡号 作为显示
										SPUtils.put(LoginA.this,CARDNAME,"");  //存储银行姓名
										SPUtils.put(LoginA.this,CARDTYPE,""); //存储银行卡类型
										SPUtils.put(LoginA.this,CARDID,"");  //存储银行卡id  提现需要用到

										//用户切换切换账号 清除子账号用户协议记录的不弹出窗
										SPUtils.put(LoginA.this,"userAgreement",false);
									}

//										SPUtils.put(LoginA.this, "userName", userName);
//										SPUtils.put(LoginA.this, "password", passeword);
									SPUtils.put(LoginA.this,"repayDay",repayDay);
									SPUtils.put(LoginA.this,"dynaMinimum",dynaMinimum);
									SPUtils.put(LoginA.this,"balance",balance);
									SPUtils.put(LoginA.this,"minimum",minimum);
									SPUtils.put(LoginA.this,"userId",id);


									ObjectSaveUtil.saveObject(LoginA.this, "loginResult", user);


									intent = new Intent(LoginA.this, MainActivity.class);
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

									cpd.dismiss();
									Toast.makeText(LoginA.this, "登录成功!", Toast.LENGTH_SHORT).show();
									startActivity(intent);
									onProfileSignIn(user.getId()) ;  //友盟账号统计
									MobclickAgent.setDebugMode(true); //友盟统计调试模式
									MobclickAgent.openActivityDurationTrack(false);	// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
									MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);//友盟统计场景统计
									overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//									    }

								}else {
									cpd.dismiss();
//										Toast.makeText(LoginA.this, "未绑定OC账号，绑定或注册！", Toast.LENGTH_SHORT).show();
									intent = new Intent(LoginA.this, PerfectInformationA.class);
									intent.putExtra("openId", openId);
									intent.putExtra("loginType", loginType);
									startActivity(intent);
									overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
									finish();
								}

							}else if("61450".equals(responseCode)){
								cpd.dismiss();
								Toast.makeText(LoginA.this, "系统错误!", Toast.LENGTH_SHORT).show();

							}else {
								cpd.dismiss();
								Toast.makeText(LoginA.this, "用户名不存在或密码错误!", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(LoginA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		qqLoginRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		qqLoginRequest.setTag("qqLoginRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(qqLoginRequest);

	}

	@Override
	protected void onStop() {
		super.onStop();
		if (cpd != null){
			cpd.dismiss();
		}
		MyApplication.getHttpQueues().cancelAll(TAG);
		MyApplication.getHttpQueues().cancelAll("qqLoginRequest");
		MyApplication.getHttpQueues().cancelAll("qqUnionidRequest");

	}

	@Override
	public Resources getResources() {//设置app字体大小不受手机设置字体影响
		Resources res = super.getResources();
		Configuration config=new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config,res.getDisplayMetrics() );
		return res;
	}


}

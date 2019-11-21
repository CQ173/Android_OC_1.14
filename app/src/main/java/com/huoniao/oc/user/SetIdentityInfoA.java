package com.huoniao.oc.user;

		import android.app.AlertDialog;
		import android.content.Intent;
		import android.os.Bundle;
		import android.os.Handler;
		import android.os.Message;
		import android.util.Log;
		import android.view.LayoutInflater;
		import android.view.View;
		import android.view.View.OnClickListener;
		import android.widget.Button;
		import android.widget.EditText;
		import android.widget.ImageView;
		import android.widget.RelativeLayout;
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
		import com.huoniao.oc.adapter.MySpinerAdapter;
		import com.huoniao.oc.bean.User;
		import com.huoniao.oc.util.Define;
		import com.huoniao.oc.util.SessionJsonObjectRequest;
		import com.huoniao.oc.util.SpinerPopWindow;

		import org.json.JSONArray;
		import org.json.JSONException;
		import org.json.JSONObject;

		import java.util.ArrayList;
		import java.util.List;

public class SetIdentityInfoA extends BaseActivity implements OnClickListener, MySpinerAdapter.IOnItemSelectListener {

	private ImageView iv_back;
	private RelativeLayout rl_choiceIdentity, rl_choiceLoction, rl_choiceProvinces, rl_choiceCitys, rl_manageArea,
			rl_choiceOneLevel, rl_choiceTwoLevel, rl_choiceThirdLevel, rl_choiceFirstAgent, rl_choiceAgentType;
	private TextView tv_identity, tv_location, tv_manageArea, tv_oneLevel, tv_twoLevel,
			tv_thirdLevel, tv_firstAgent, tv_agentType, tv_provinces, tv_citys;
	private EditText et_windowNumber, et_userName, et_orgName, et_orgAddress, et_passaword, et_confirmPassaword,
			et_companyName;
	private String identity, location, provinces, citys, oneLevel, twoLevel, threeLevel, manageArea, firstAgent,
			agentType;
	private String phoneNumber, windowNumber, userName, orgName, orgAddress, passaword, confirmPassaword;

	private String openId, loginType;
	private Button bt_complete, bt_nextStep;
	private String provinceCode = "";
	private String cityCode = "";
	private String oneLevelCode = "";
	private String twoLevelCode = "";
	private String threeLevelCode = "";
	private Intent intent;
	private List<String> identityTypeList = new ArrayList<String>();
	private List<String> provinceNameList = new ArrayList<String>();
	private List<String> provinceCodeList = new ArrayList<String>();
	private List<String> cityNameList = new ArrayList<String>();
	private List<String> cityCodeList = new ArrayList<String>();
	private List<String> oneLevelCodeList = new ArrayList<String>();
	private List<String> oneLevelNameList = new ArrayList<String>();
	private List<String> twoLevelCodeList = new ArrayList<String>();
	private List<String> twoLevelNameList = new ArrayList<String>();
	private List<String> threeLevelNameList = new ArrayList<String>();
	private List<String> threeLevelCodeList = new ArrayList<String>();
	private List<String> fristAgentList = new ArrayList<String>();
	private List<String> agentTypeList = new ArrayList<String>();
	private SpinerPopWindow mSpinerPopWindow;
	private String CHOICE_TAG;//
	private String DIALOGSTYLE_TAG;//
	private String userNameVri;// 用于验证用户名是否被使用
	private static final int PROVINCE_LIST = 1;
	private static final int CITY_LIST = 2;
	private static final int USERNAME_VRI = 3;
	private static final int MANAGEAREA_LIST = 4;
	private static final int MANAGEAREA2_LIST = 5;
	private static final int MANAGEAREA3_LIST = 6;

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
				case USERNAME_VRI:
					userNameVri = (String) msg.obj;
					if ("true".equals(userNameVri)) {
						Toast.makeText(SetIdentityInfoA.this, "用户名或窗口号已被注册!", Toast.LENGTH_SHORT).show();
						return;
					} else if ("false".equals(userNameVri)) {
						// intent.putExtra("userName", userName);
						// Toast.makeText(SetIdentityInfoA.this, "用户名可以使用!",
						// Toast.LENGTH_SHORT).show();
						// userName = et_userName.getText().toString();
						intent.putExtra("userName", userName);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					}
					break;

				case MANAGEAREA_LIST:
					@SuppressWarnings("unchecked")
					List<User> manageAreaList = (List<User>) msg.obj;

					for (User user : manageAreaList) {
						oneLevelCodeList.add(user.getOneLevelCode());
						oneLevelNameList.add(user.getOneLevelName());
					}

					break;

				case MANAGEAREA2_LIST: {
					@SuppressWarnings("unchecked")
					List<User> twoAreaList = (List<User>) msg.obj;
					twoLevelNameList.clear();
					twoLevelCodeList.clear();
					for (User user : twoAreaList) {
						twoLevelNameList.add(user.getTwoLevelName());
						twoLevelCodeList.add(user.getTwoLevelCode());
					}

					break;

				}

				case MANAGEAREA3_LIST: {

					@SuppressWarnings("unchecked")
					List<User> twoAreaList = (List<User>) msg.obj;
					threeLevelNameList.clear();
					threeLevelCodeList.clear();
					for (User user : twoAreaList) {
						threeLevelNameList.add(user.getThreeLevelName());
						threeLevelCodeList.add(user.getThreeLevelCode());
					}

					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_setidentityinfo);
		initView();
		initData();
	}

	private void initData() {
		intent = getIntent();
		phoneNumber = intent.getStringExtra("poneNumber");
		openId = intent.getStringExtra("openId");
		loginType = intent.getStringExtra("loginType");
		// Log.d("debug", phoneNumber);
		identityTypeList.add("代售点");
//		identityTypeList.add("火车站");
		fristAgentList.add("个体户");
		fristAgentList.add("铁青");
		fristAgentList.add("邮政");
		fristAgentList.add("公司");
		agentTypeList.add("直营");
		agentTypeList.add("承包");
		// identityTypeList.add("管理员");

		try {
			getAllProvince();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			getAllManageArea();
		} catch (Exception e) {

			e.printStackTrace();
		}

		// try {

		// getAllCitys();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		rl_choiceIdentity = (RelativeLayout) findViewById(R.id.rl_choiceIdentity);
		rl_choiceLoction = (RelativeLayout) findViewById(R.id.rl_choiceLoction);
		rl_manageArea = (RelativeLayout) findViewById(R.id.rl_manageArea);
		rl_choiceFirstAgent = (RelativeLayout) findViewById(R.id.rl_choiceFirstAgent);
		rl_choiceAgentType = (RelativeLayout) findViewById(R.id.rl_choiceAgentType);
		tv_identity = (TextView) findViewById(R.id.tv_identity);
		// tv_provinces = (TextView) findViewById(R.id.tv_provinces);
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_manageArea = (TextView) findViewById(R.id.tv_manageArea);
		tv_firstAgent = (TextView) findViewById(R.id.tv_firstAgent);
		tv_agentType = (TextView) findViewById(R.id.tv_agentType);
		et_windowNumber = (EditText) findViewById(R.id.et_windowNumber);
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_orgAddress = (EditText) findViewById(R.id.et_orgAddress);
		et_orgName = (EditText) findViewById(R.id.et_orgName);
		et_passaword = (EditText) findViewById(R.id.et_passaword);
		et_confirmPassaword = (EditText) findViewById(R.id.et_confirmPassaword);
		et_companyName = (EditText) findViewById(R.id.et_companyName);
		bt_nextStep = (Button) findViewById(R.id.bt_nextStep);

		iv_back.setOnClickListener(this);
		bt_nextStep.setOnClickListener(this);
		rl_choiceIdentity.setOnClickListener(this);
		rl_choiceLoction.setOnClickListener(this);
		rl_manageArea.setOnClickListener(this);
		rl_choiceFirstAgent.setOnClickListener(this);
		rl_choiceAgentType.setOnClickListener(this);
		// rl_choiceProvinces.setOnClickListener(this);
		// rl_choiceCitys.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back:
				finish();
				break;

			case R.id.bt_nextStep:

				identity = tv_identity.getText().toString();
				if ("火车站".equals(identity)) {
					identity = Define.TRAINSTATION;

				} else if ("代售点".equals(identity)) {
					identity = Define.OUTLETS;
				} else if ("选择用户类型".equals(identity)) {
					identity = "";
				}

				windowNumber = et_windowNumber.getText().toString();

				userName = et_userName.getText().toString();
				orgName = et_orgName.getText().toString();
				orgName = removeAllSpace(orgName);
				orgAddress = et_orgAddress.getText().toString();
				orgAddress = removeAllSpace(orgAddress);
				passaword = et_passaword.getText().toString();
				confirmPassaword = et_confirmPassaword.getText().toString();
				firstAgent = tv_firstAgent.getText().toString();
				agentType = tv_agentType.getText().toString();

				if (identity.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请选择您要注册的用户类型！", Toast.LENGTH_SHORT).show();
					return;
				}

				if ("选择省".equals(provinces) || null == provinces || provinces.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请选择您机构所在的省份！", Toast.LENGTH_SHORT).show();
					return;
				}
				if ("选择市".equals(citys) || null == citys || citys.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请选择您机构所在的城市！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (Define.OUTLETS.equals(identity)) {
					if (windowNumber.isEmpty()) {
						Toast.makeText(SetIdentityInfoA.this, "请输入窗口号！", Toast.LENGTH_SHORT).show();
						return;
					}
					else if (windowNumber.length() < 4){
						{
							windowNumber = addZeroForNum(windowNumber, 4);
						}
					}
				}

				// if (Define.SYSTEM_MANAG_USER.equals(identity) &&
				// userName.isEmpty()) {
				// Toast.makeText(SetIdentityInfoA.this, "请输入用户名！",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }

				if (Define.TRAINSTATION.equals(identity) && userName.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (orgName == null || orgName.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请输入机构名称！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (orgAddress == null || orgAddress.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请输入机构地址！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (passaword.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请设置登录密码！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (confirmPassaword.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请确认登录密码！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (confirmPassaword.length() < 6) {
					Toast.makeText(SetIdentityInfoA.this, "密码不能少于6位！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (!passaword.equals(confirmPassaword)) {
					Toast.makeText(SetIdentityInfoA.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (firstAgent.equals("公司") && et_companyName.getText().toString().isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请输入公司名称！", Toast.LENGTH_SHORT).show();
					return;
				}

				if (threeLevelCode.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请指定所属管理区域！", Toast.LENGTH_SHORT).show();
					return;
				}

				intent = new Intent(SetIdentityInfoA.this, RealNameAuthenticationA.class);
				intent.putExtra("identity", identity);
				intent.putExtra("winNumber", windowNumber);
				// intent.putExtra("province", provinces);
				// intent.putExtra("provinceCode", provinceCode);
				// intent.putExtra("city", citys);
				intent.putExtra("cityCode", cityCode);
				intent.putExtra("openId", openId);
				intent.putExtra("loginType", loginType);

				intent.putExtra("orgName", orgName);
				intent.putExtra("orgAddress", orgAddress);
				intent.putExtra("phoneNumber", phoneNumber);
				intent.putExtra("password", passaword);
				try {
					intent.putExtra("agent", getAgentCode(firstAgent));
				} catch (Exception e2) {
					Toast.makeText(SetIdentityInfoA.this, "请指定第一代理人！", Toast.LENGTH_SHORT).show();
					return;
				}

				try {
					intent.putExtra("agentType", getAgentTypeCode(agentType));
				} catch (Exception e1) {
					Toast.makeText(SetIdentityInfoA.this, "请指定代理类型！", Toast.LENGTH_SHORT).show();
					return;
				}

				intent.putExtra("agentCompanyName", et_companyName.getText().toString());
				intent.putExtra("jurAreaCode", threeLevelCode);

				if (Define.OUTLETS.equals(identity)) {
					// intent.putExtra("windowNumber", windowNumber);
					// startActivity(intent);
					// overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					userName = provinceCode + cityCode + windowNumber;
				} else if (Define.TRAINSTATION.equals(identity)) {
					userName = et_userName.getText().toString();
				}

				if (userName != null && !userName.isEmpty()) {
					try {
						veriUserName(userName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// if (Define.TRAINSTATION.equals(identity)) {
				// try {
				// veriUserName(userName);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				//
				// }

				break;
			case R.id.rl_choiceIdentity:

				mSpinerPopWindow = new SpinerPopWindow(this);
				CHOICE_TAG = "1";
				mSpinerPopWindow.refreshData(identityTypeList, 0);
				mSpinerPopWindow.setItemListener(this);
				showSpinWindow(rl_choiceIdentity);
				break;
			// 选择地理区域
			case R.id.rl_choiceLoction:
				choiceLoctionDialog();
				// mSpinerPopWindow = new SpinerPopWindow(this);
				// CHOICE_TAG = "2";
				// mSpinerPopWindow.refreshData(provinceNameList, 0);
				// mSpinerPopWindow.setItemListener(this);
				// showSpinWindow(rl_choiceProvinces);
				break;
			// 选择管理区域
			case R.id.rl_manageArea:
				choiceManageAreaDialog();
				// if (provinceCode.isEmpty() || provinceCode == null) {
				// Toast.makeText(SetIdentityInfoA.this, "请先选择省份！",
				// Toast.LENGTH_SHORT).show();
				// return;
				// }
				// mSpinerPopWindow = new SpinerPopWindow(this);
				// CHOICE_TAG = "3";
				// mSpinerPopWindow.refreshData(cityNameList, 0);
				// mSpinerPopWindow.setItemListener(this);
				// showSpinWindow(rl_choiceCitys);
				break;

			case R.id.rl_choiceFirstAgent:

				mSpinerPopWindow = new SpinerPopWindow(this);
				CHOICE_TAG = "7";
				mSpinerPopWindow.refreshData(fristAgentList, 0);
				mSpinerPopWindow.setItemListener(this);
				showSpinWindow(rl_choiceFirstAgent);
				break;

			case R.id.rl_choiceAgentType:

				mSpinerPopWindow = new SpinerPopWindow(this);
				CHOICE_TAG = "8";
				mSpinerPopWindow.refreshData(agentTypeList, 0);
				mSpinerPopWindow.setItemListener(this);
				showSpinWindow(rl_choiceAgentType);
				break;
			default:
				break;
		}

	}

	private String getAgentTypeCode(String firstAgent2) throws Exception {
		if (agentType.equals("直营"))
			return "0";
		else if (agentType.equals("承包"))
			return "1";

		throw new Exception("");
	}

	private String getAgentCode(String agentType) throws Exception {
		if (firstAgent.equals("个体户"))
			return "0";
		else if (firstAgent.equals("铁青"))
			return "1";
		else if (firstAgent.equals("邮政"))
			return "2";
		else if (firstAgent.equals("公司"))
			return "3";

		throw new Exception("");
	}

	private String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);// 左补0
				// sb.append(str).append("0");//右补0
				str = sb.toString();
				strLen = str.length();
			}
		}

		return str;
	}

	private void showSpinWindow(View v) {
		Log.e("", "showSpinWindow");
		mSpinerPopWindow.setWidth(v.getWidth());
		mSpinerPopWindow.showAsDropDown(v);
	}

	// 选择用户类型
	private void setIdentity(int pos) {
		if (pos >= 0 && pos <= identityTypeList.size()) {
			String value = identityTypeList.get(pos);

			tv_identity.setText(value);

			identity = tv_identity.getText().toString();
			// if ("管理员".equals(identity)) {
			// identity = Define.SYSTEM_MANAG_USER;
			// et_windowNumber.setVisibility(View.GONE);
			// et_userName.setVisibility(View.VISIBLE);
			// }else
			if ("火车站".equals(identity)) {
				identity = Define.TRAINSTATION;
				et_windowNumber.setVisibility(View.GONE);
				et_userName.setVisibility(View.VISIBLE);
			} else if ("代售点".equals(identity)) {
				identity = Define.OUTLETS;
				et_windowNumber.setVisibility(View.VISIBLE);
				et_userName.setVisibility(View.GONE);
			}
		}
	}

	// 选择第一代理人
	private void setFristAgent(int pos) {
		if (pos >= 0 && pos <= fristAgentList.size()) {
			String value = fristAgentList.get(pos);

			tv_firstAgent.setText(value);

			firstAgent = tv_firstAgent.getText().toString();

			if ("公司".equals(firstAgent)) {
				// identity = Define.TRAINSTATION;
				et_companyName.setVisibility(View.VISIBLE);
			} else {
				et_companyName.setVisibility(View.GONE);
			}
		}
	}

	// 选择第一代理人
	private void setAgentType(int pos) {
		if (pos >= 0 && pos <= agentTypeList.size()) {
			String value = agentTypeList.get(pos);

			tv_agentType.setText(value);

			agentType = tv_agentType.getText().toString();

		}
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
				mSpinerPopWindow = new SpinerPopWindow(SetIdentityInfoA.this);
				CHOICE_TAG = "2";
				mSpinerPopWindow.refreshData(provinceNameList, 0);
				mSpinerPopWindow.setItemListener(SetIdentityInfoA.this);
				showSpinWindow(rl_choiceProvinces);

			}
		});

		rl_choiceCitys.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (provinceCode.isEmpty() || provinceCode == null) {
					Toast.makeText(SetIdentityInfoA.this, "请先选择省份！", Toast.LENGTH_SHORT).show();
					return;
				}
				mSpinerPopWindow = new SpinerPopWindow(SetIdentityInfoA.this);
				CHOICE_TAG = "3";
				mSpinerPopWindow.refreshData(cityNameList, 0);
				mSpinerPopWindow.setItemListener(SetIdentityInfoA.this);
				showSpinWindow(rl_choiceCitys);

			}
		});

		bt_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				provinces = tv_provinces.getText().toString();
				citys = tv_citys.getText().toString();
				if ("选择省".equals(provinces)) {
					Toast.makeText(SetIdentityInfoA.this, "请选择省！", Toast.LENGTH_SHORT).show();
					return;
				}

				if ("选择市".equals(citys)) {
					Toast.makeText(SetIdentityInfoA.this, "请选择市！", Toast.LENGTH_SHORT).show();
					return;
				}

				location = provinces + "/" + citys;
				tv_location.setText(location);
				dialog.dismiss();
			}
		});
		provinceCode = "";

	}

	private void choiceManageAreaDialog() {
		// String url = Define.URL+"acct/getCode";
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.selectmanagearea_dialog, null);

		rl_choiceOneLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceOneLevel);
		rl_choiceTwoLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceTwoLevel);
		rl_choiceThirdLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceThirdLevel);
		tv_oneLevel = (TextView) view.findViewById(R.id.tv_oneLevel);
		tv_twoLevel = (TextView) view.findViewById(R.id.tv_twoLevel);
		tv_thirdLevel = (TextView) view.findViewById(R.id.tv_thirdLevel);
		bt_complete = (Button) view.findViewById(R.id.bt_complete);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(view);
		final AlertDialog dialog = builder.create();// 获取dialog

		dialog.show();

		rl_choiceOneLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSpinerPopWindow = new SpinerPopWindow(SetIdentityInfoA.this);
				CHOICE_TAG = "4";
				mSpinerPopWindow.refreshData(oneLevelNameList, 0);
				mSpinerPopWindow.setItemListener(SetIdentityInfoA.this);
				showSpinWindow(rl_choiceOneLevel);

			}
		});

		rl_choiceTwoLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (oneLevelCode == null || oneLevelCode.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请先选择第一级！", Toast.LENGTH_SHORT).show();
					return;
				}
				mSpinerPopWindow = new SpinerPopWindow(SetIdentityInfoA.this);
				CHOICE_TAG = "5";
				mSpinerPopWindow.refreshData(twoLevelNameList, 0);
				mSpinerPopWindow.setItemListener(SetIdentityInfoA.this);
				showSpinWindow(rl_choiceTwoLevel);

			}
		});

		rl_choiceThirdLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (twoLevelCode == null || twoLevelCode.isEmpty()) {
					Toast.makeText(SetIdentityInfoA.this, "请先选择第二级！", Toast.LENGTH_SHORT).show();
					return;
				}
				mSpinerPopWindow = new SpinerPopWindow(SetIdentityInfoA.this);
				CHOICE_TAG = "6";
				mSpinerPopWindow.refreshData(threeLevelNameList, 0);
				mSpinerPopWindow.setItemListener(SetIdentityInfoA.this);
				showSpinWindow(rl_choiceThirdLevel);

			}
		});
		bt_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				oneLevel = tv_oneLevel.getText().toString();
				twoLevel = tv_twoLevel.getText().toString();
				threeLevel = tv_thirdLevel.getText().toString();
				if ("选择第一级".equals(oneLevel)) {
					Toast.makeText(SetIdentityInfoA.this, "请选择第一级！", Toast.LENGTH_SHORT).show();
					return;
				}

				if ("选择第二级".equals(twoLevel)) {
					Toast.makeText(SetIdentityInfoA.this, "请选择第二级！", Toast.LENGTH_SHORT).show();
					return;
				}

				if ("选择第三级".equals(threeLevel)) {
					Toast.makeText(SetIdentityInfoA.this, "请选择第三级！", Toast.LENGTH_SHORT).show();
					return;
				}

				manageArea = oneLevel + "/" + twoLevel + "/" + threeLevel;
				tv_manageArea.setText(manageArea);
				dialog.dismiss();
			}
		});
		oneLevelCode = "";
		twoLevelCode = "";
	}

	// 选择1级区域管理列表
	private void setManageArea(int pos) {
		if (pos >= 0 && pos <= oneLevelNameList.size()) {
			String value = oneLevelNameList.get(pos);

			tv_oneLevel.setText(value);
		}
	}

	// 获取1级管理区域code
	private String getOneLevelCode(int pos) {
		if (pos >= 0 && pos <= oneLevelCodeList.size()) {
			oneLevelCode = oneLevelCodeList.get(pos);

		}
		return oneLevelCode;
	}

	// 选择2级区域管理列表
	private void setManageArea2(int pos) {
		if (pos >= 0 && pos <= twoLevelNameList.size()) {
			String value = twoLevelNameList.get(pos);

			tv_twoLevel.setText(value);
		}
	}

	// 获取2级管理区域code
	private String getTwoLevelCode(int pos) {
		if (pos >= 0 && pos <= twoLevelCodeList.size()) {
			twoLevelCode = twoLevelCodeList.get(pos);

		}
		return twoLevelCode;
	}

	// 选择3级区域管理列表
	private void setManageArea3(int pos) {
		if (pos >= 0 && pos <= threeLevelNameList.size()) {
			String value = threeLevelNameList.get(pos);

			tv_thirdLevel.setText(value);
		}
	}

	// 获取3级管理区域code
	private String getThreeLevelCode(int pos) {
		if (pos >= 0 && pos <= threeLevelCodeList.size()) {
			threeLevelCode = threeLevelCodeList.get(pos);

		}
		return threeLevelCode;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("3".equals(CHOICE_TAG)) {
			setCitys(pos);
			cityCode = getCityCode(pos);
		} else if ("4".equals(CHOICE_TAG)) {
			setManageArea(pos);
			oneLevelCode = getOneLevelCode(pos);
			try {
				getAllManageAreaTwo();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("5".equals(CHOICE_TAG)) {
			setManageArea2(pos);
			twoLevelCode = getTwoLevelCode(pos);
			try {
				getAllManageAreaThree();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("6".equals(CHOICE_TAG)) {
			setManageArea3(pos);
			threeLevelCode = getThreeLevelCode(pos);
		} else if ("7".equals(CHOICE_TAG)) {
			setFristAgent(pos);
		} else if ("8".equals(CHOICE_TAG)) {
			setAgentType(pos);
		}

	}

	/**
	 * 获取所有省份
	 *
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
								Toast.makeText(SetIdentityInfoA.this, message, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(SetIdentityInfoA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		provinceListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		provinceListRequest.setTag("provinceListRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(provinceListRequest);

	}

	/**
	 * 获取所有管理区域
	 *
	 * @throws Exception
	 */
	private void getAllManageArea() throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("", "");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		cpd.show();
		// String url = Define.URL + "user/getProvinceList";
		String url = Define.URL + "user/getGroupList";
		final List<User> manageAreaList = new ArrayList<User>();
		SessionJsonObjectRequest manageAreaListRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						if (null == response) {
							Toast.makeText(SetIdentityInfoA.this, "服务器状态异常，请稍后再试", Toast.LENGTH_SHORT);
							return;
						}
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								JSONArray jsonArray = response.getJSONArray("data");

								for (int i = 0; i < jsonArray.length(); i++) {
									User user = new User();

									JSONObject provinceObj = (JSONObject) jsonArray.get(i);
									String oneLevelCode = provinceObj.optString("code");
									String oneLevelName = provinceObj.getString("name");

									user.setOneLevelName(oneLevelName);
									user.setOneLevelCode(oneLevelCode);
									;
									manageAreaList.add(user);
								}

								Runnable proRunnable = new Runnable() {

									@Override
									public void run() {

										Message msg = new Message();
										msg.what = MANAGEAREA_LIST;
										msg.obj = manageAreaList;
										mHandler.sendMessage(msg);
									}
								};

								Thread proThread = new Thread(proRunnable);
								proThread.start();
								cpd.dismiss();
							} else {
								cpd.dismiss();
								Toast.makeText(SetIdentityInfoA.this, message, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(SetIdentityInfoA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		manageAreaListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		manageAreaListRequest.setTag("manageAreaListRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(manageAreaListRequest);

	}

	/**
	 * 根据1级管理区域code获取管理区域列表
	 *
	 * @throws Exception
	 */
	private void getAllManageAreaTwo() throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("code", oneLevelCode);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (oneLevelCode.isEmpty() || oneLevelCode == null) {
			Toast.makeText(SetIdentityInfoA.this, "请先选择第一级菜单！", Toast.LENGTH_SHORT).show();
			return;
		}
		cpd.show();
		// String url = Define.URL + "user/getCityListByProvice";
		String url = Define.URL + "user/getChildByParent";
		final List<User> twoLeveArealList = new ArrayList<User>();
		SessionJsonObjectRequest twoLeveAreaRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
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

									JSONObject twoObj = (JSONObject) jsonArray.get(i);
									String twoLevelCode = twoObj.optString("code");
									String twoLevelName = twoObj.getString("name");

									user.setTwoLevelCode(twoLevelCode);
									;
									user.setTwoLevelName(twoLevelName);
									;

									twoLeveArealList.add(user);
								}

								Runnable cityRunnable = new Runnable() {

									@Override
									public void run() {

										Message msg = new Message();
										msg.what = MANAGEAREA2_LIST;
										msg.obj = twoLeveArealList;
										mHandler.sendMessage(msg);
									}
								};
								cpd.dismiss();

								Thread twoThread = new Thread(cityRunnable);
								twoThread.start();

							} else {
								cpd.dismiss();
								Toast.makeText(SetIdentityInfoA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							cpd.dismiss();
							e.printStackTrace();
						}
						if (oneLevelCode.equals(oneLevelCode)){
							tv_twoLevel.setText("选择第二级");
							tv_thirdLevel.setText("选择第三级");
						}

					}
				}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(SetIdentityInfoA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		twoLeveAreaRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		twoLeveAreaRequest.setTag("twoLeveAreaRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(twoLeveAreaRequest);

	}

	/**
	 * 根据2级管理区域code获取三级管理区域列表
	 *
	 * @throws Exception
	 */
	private void getAllManageAreaThree() throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("code", twoLevelCode);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (twoLevelCode.isEmpty() || twoLevelCode == null) {
			Toast.makeText(SetIdentityInfoA.this, "请先选择第二级菜单！", Toast.LENGTH_SHORT).show();
			return;
		}
		cpd.show();

		// String url = Define.URL + "user/getCityListByProvice";
		String url = Define.URL + "user/getChildByParent";
		final List<User> threeLeveArealList = new ArrayList<User>();
		SessionJsonObjectRequest threeLeveAreaRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
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

									JSONObject twoObj = (JSONObject) jsonArray.get(i);
									String threeLevelCode = twoObj.optString("code");
									String threeLevelName = twoObj.getString("name");

									user.setThreeLevelCode(threeLevelCode);
									user.setThreeLevelName(threeLevelName);

									threeLeveArealList.add(user);
								}

								Runnable threeRunnable = new Runnable() {

									@Override
									public void run() {

										Message msg = new Message();
										msg.what = MANAGEAREA3_LIST;
										msg.obj = threeLeveArealList;
										mHandler.sendMessage(msg);
									}
								};

								Thread twoThread = new Thread(threeRunnable);
								twoThread.start();
								cpd.dismiss();
							} else {
								cpd.dismiss();
								Toast.makeText(SetIdentityInfoA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							cpd.dismiss();
							e.printStackTrace();
						}
						if (twoLevelCode.equals(twoLevelCode)){
							tv_thirdLevel.setText("选择第三级");
						}
					}
				}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(SetIdentityInfoA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		threeLeveAreaRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		threeLeveAreaRequest.setTag("threeLeveAreaRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(threeLeveAreaRequest);

	}

	/**
	 * 根据选择的省份code获取对应的城市列表
	 *
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
			Toast.makeText(SetIdentityInfoA.this, "请先选择省份！", Toast.LENGTH_SHORT).show();
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
								Toast.makeText(SetIdentityInfoA.this, message, Toast.LENGTH_SHORT).show();
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
				Toast.makeText(SetIdentityInfoA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		cityListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		cityListRequest.setTag("cityListRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(cityListRequest);

	}

	/**
	 * 验证用户名或窗口号是否已经被使用
	 *
	 * @throws Exception
	 */
	private void veriUserName(String loginName) throws Exception {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("loginName", loginName);
			jsonObject.put("jurAreaCode", threeLevelCode);
			jsonObject.put("winNum", windowNumber);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

//		String url = Define.URL + "user/validateLoginName";
		String url = Define.URL + "user/validateWinNum";
		SessionJsonObjectRequest veriUserNameRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								JSONObject jsonObject = response.getJSONObject("data");
								final String isUsed = jsonObject.getString("isUsed");
								Message msg = new Message();
								msg.what = USERNAME_VRI;
								msg.obj = isUsed;
								mHandler.sendMessage(msg);
								// Runnable runnable = new Runnable() {
								//
								// @Override
								// public void run() {
								// Message msg = new Message();
								// msg.what = USERNAME_VRI;
								// msg.obj = isUsed;
								// mHandler.sendMessage(msg);
								//
								// }
								// };
								//
								// Thread userNameThread = new Thread(runnable);
								// userNameThread.start();

								// if (isUsed.equals("true")) {
								// Toast.makeText(SetIdentityInfoA.this,
								// "用户名已被注册!", Toast.LENGTH_SHORT).show();
								// return;
								// }else if (isUsed.equals("false")) {
								//// intent.putExtra("userName", userName);
								// Toast.makeText(SetIdentityInfoA.this,
								// "用户名可以使用!", Toast.LENGTH_SHORT).show();
								// }

							} else {
								Toast.makeText(SetIdentityInfoA.this, message, Toast.LENGTH_SHORT).show();
								Log.d("debug", message);

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// pd.dismiss();
				Toast.makeText(SetIdentityInfoA.this, R.string.netError, Toast.LENGTH_SHORT).show();

			}
		});
		// 解决重复请求后台的问题
		veriUserNameRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		veriUserNameRequest.setTag("veriUserNameRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(veriUserNameRequest);

	}

	/**
	 * 删除字符串空格
	 *
	 * @param str
	 * @return
	 */
	public String removeAllSpace(String str) {
		String tmpstr = str.replace(" ", "");
		return tmpstr;
	}

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("provinceListRequest");
		MyApplication.getHttpQueues().cancelAll("cityListRequest");
		MyApplication.getHttpQueues().cancelAll("veriUserNameRequest");
		MyApplication.getHttpQueues().cancelAll("manageAreaListRequest");
		MyApplication.getHttpQueues().cancelAll("twoLeveAreaRequest");
		MyApplication.getHttpQueues().cancelAll("threeLeveAreaRequest");
	}

}

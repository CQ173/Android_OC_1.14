package com.huoniao.oc.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.common.luban.Luban;
import com.huoniao.oc.common.luban.OnCompressListener;
import com.huoniao.oc.useragreement.RegisterAgreeA;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.SDCardUtil;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RealNameAuthenticationA extends BaseActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener{

	private ImageView iv_back;
	private ImageView iv_businessLicense, iv_identificationFront, iv_identificationOpposite;
	private EditText et_contactName, et_contactPhone, et_corporationName, et_corpIdNum, et_corpPhone, et_deposit;
	private String identity, provinceCode, cityCode;
	private String phoneNumber, windowNumber, userName, orgName, orgAddress, password;
	private String contactName, contactPhone, corpoName, corpIdNum;
	private String IMG_TAG;
	private String businessLicense = "", identificationFront = "", identificationOpposite = "";
	private String openId = "", loginType = "";
	private Button bt_registerSubmit;
	private Intent intent;
	private ProgressDialog pd;
	private String corpMobile = ""; // 法人手机号
	private String operatorName = ""; // 负责人姓名
	private String operatorIdNum = ""; // 负责人编号
	private String operatorMobile = ""; // 负责人手机
	private String staContIndexImg = ""; // 车站合同首页
	private String staContLastImg = ""; // 车站合同末页
	private String staDepositImg = ""; // 车站押金条
	private String staDepInspImg = ""; // 车站押金年检证书
	private String bankFlowImg = ""; // 半年银行流水
	private String operatorCardforntImg = ""; // 负责人身份证正面
	private String operatorCardrearImg = ""; // 负责人身份证背面
	private String fareAuthorizationImg = ""; // 票款汇缴授权书
	private EditText et_fuZeRenName, et_fuZeRenIdNum, et_fuZeRenPhone;
	private ImageView iv_stationContactFrist;
	private ImageView iv_stationContactEnd;
	private ImageView iv_articleDeposit;
	private ImageView iv_nianJianCertificate;
	private ImageView iv_paySystemCertificate;
	private ImageView iv_fuzerenIdFront;
	private ImageView iv_fuzerenIdOpposite;
	private ImageView iv_bankRunning;
	private LinearLayout layout_fuZeRenInfo, layout_fuZeRenCertificates;//负责人相关信息及证件
	private String agent, agentType;//第一代理人及代理人类型
	private CheckBox checkBox;//负责人信息控制单选框
	private LinearLayout layout_checkBox;//单选框区域
	private LinearLayout layout_shouQuanShu;//票款汇缴授权书区域
	private LinearLayout layout_fuzerenIdFront;//负责人身份证正面区域
	private boolean checkTag;
	private VolleyNetCommon volleyNetCommon;
	private String depositAmount = ""; // 代售点缴纳的押金

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_realname);
		initView();
		initData();
	}

	private void initData() {
		volleyNetCommon = new VolleyNetCommon();
		intent = getIntent();
		identity = intent.getStringExtra("identity");
		windowNumber = intent.getStringExtra("winNumber");
		// provinces = intent.getStringExtra("province");
		provinceCode = intent.getStringExtra("provinceCode");
		// citys = intent.getStringExtra("city");
		cityCode = intent.getStringExtra("cityCode");
		phoneNumber = intent.getStringExtra("phoneNumber");
		openId = intent.getStringExtra("openId");
		loginType = intent.getStringExtra("loginType");

		et_fuZeRenPhone.setText(phoneNumber);
		et_fuZeRenPhone.setEnabled(false);
		// if (Define.OUTLETS.equals(identity)) {
		// windowNumber = intent.getStringExtra("windowNumber");
		// }

		// if (Define.TRAINSTATION.equals(identity)) {
		// userName = intent.getStringExtra("userName");
		// }else if (Define.OUTLETS.equals(identity)) {
		// userName = provinceCode + cityCode + windowNumber;
		// }
		userName = intent.getStringExtra("userName");
		orgName = intent.getStringExtra("orgName");
		orgAddress = intent.getStringExtra("orgAddress");
		password = intent.getStringExtra("password");
		agent = intent.getStringExtra("agent");
		agentType = intent.getStringExtra("agentType");
		/*if ("0".equals(agent) && "0".equals(agentType)){
			layout_fuZeRenInfo.setVisibility(View.GONE);
			layout_fuZeRenCertificates.setVisibility(View.GONE);
			layout_shouQuanShu.setVisibility(View.INVISIBLE);
			et_corpPhone.setText(phoneNumber);
			et_corpPhone.setEnabled(false);
		}else {
			layout_checkBox.setVisibility(View.VISIBLE);

		}*/


	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_businessLicense = (ImageView) findViewById(R.id.iv_businessLicense);
		iv_identificationFront = (ImageView) findViewById(R.id.iv_identificationFront);
		iv_identificationOpposite = (ImageView) findViewById(R.id.iv_identificationOpposite);
		iv_stationContactFrist = (ImageView) findViewById(R.id.iv_stationContactFrist);
		iv_stationContactEnd = (ImageView) findViewById(R.id.iv_stationContactEnd);
		iv_articleDeposit = (ImageView) findViewById(R.id.iv_articleDeposit);
//		iv_bankRunning = (ImageView) findViewById(R.id.iv_bankRunning);
		iv_nianJianCertificate = (ImageView) findViewById(R.id.iv_nianJianCertificate);
		iv_paySystemCertificate = (ImageView) findViewById(R.id.iv_paySystemCertificate);
		iv_fuzerenIdFront = (ImageView) findViewById(R.id.iv_fuzerenIdFront);
		iv_fuzerenIdOpposite = (ImageView) findViewById(R.id.iv_fuzerenIdOpposite);
		et_contactName = (EditText) findViewById(R.id.et_contactName);
		et_contactPhone = (EditText) findViewById(R.id.et_contactPhone);
		et_deposit = (EditText) findViewById(R.id.et_deposit);
		InputFilter[] filters = {new CashierInputFilter()};
		et_deposit.setFilters(filters);

		et_corporationName = (EditText) findViewById(R.id.et_corporationName);
		et_corpIdNum = (EditText) findViewById(R.id.et_corpIdNum);
		bt_registerSubmit = (Button) findViewById(R.id.bt_registerSubmit);
		et_corpPhone = (EditText) findViewById(R.id.et_corpPhone);
		et_fuZeRenName = (EditText) findViewById(R.id.et_fuZeRenName);
		et_fuZeRenIdNum = (EditText) findViewById(R.id.et_fuZeRenIdNum);
		et_fuZeRenPhone = (EditText) findViewById(R.id.et_fuZeRenPhone);
		layout_fuZeRenInfo = (LinearLayout) findViewById(R.id.layout_fuZeRenInfo);
		layout_fuZeRenCertificates = (LinearLayout) findViewById(R.id.layout_fuZeRenCertificates);
		layout_shouQuanShu = (LinearLayout) findViewById(R.id.layout_shouQuanShu);
		layout_fuzerenIdFront = (LinearLayout) findViewById(R.id.layout_fuzerenIdFront);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		layout_checkBox = (LinearLayout) findViewById(R.id.layout_checkBox);

		findViewById(R.id.tv_Service_agreement).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(RealNameAuthenticationA.this, RegisterAgreeA.class);
				intent.putExtra("url", Define.PROTOCAL3_URL);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			}
		});
		;
		findViewById(R.id.tv_Custom_project).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(RealNameAuthenticationA.this, RegisterAgreeA.class);
				intent.putExtra("url", Define.PROTOCAL4_URL);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			}
		});

		findViewById(R.id.tv_Software_license).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(RealNameAuthenticationA.this, RegisterAgreeA.class);
				intent.putExtra("url", Define.PROTOCAL5_URL);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			}
		});

		findViewById(R.id.tv_Firebird_Pass).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(RealNameAuthenticationA.this, RegisterAgreeA.class);
				intent.putExtra("url", Define.PROTOCAL6_URL);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			}
		});

		findViewById(R.id.tv_Privacy_Policy).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(RealNameAuthenticationA.this, RegisterAgreeA.class);
				intent.putExtra("url", Define.PROTOCAL7_URL);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			}
		});


		iv_back.setOnClickListener(this);
		iv_identificationFront.setOnClickListener(this);
		iv_businessLicense.setOnClickListener(this);
		iv_identificationOpposite.setOnClickListener(this);
		iv_stationContactFrist.setOnClickListener(this);
		iv_stationContactEnd.setOnClickListener(this);
		iv_articleDeposit.setOnClickListener(this);
//		iv_bankRunning.setOnClickListener(this);
		iv_nianJianCertificate.setOnClickListener(this);
		iv_paySystemCertificate.setOnClickListener(this);
		iv_fuzerenIdFront.setOnClickListener(this);
		iv_fuzerenIdOpposite.setOnClickListener(this);
		bt_registerSubmit.setOnClickListener(this);
		checkBox.setOnCheckedChangeListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_businessLicense:
			IMG_TAG = "license";
			// 使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 1);
			break;

		case R.id.iv_identificationFront:
			IMG_TAG = "front";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 2);
			break;

		case R.id.iv_identificationOpposite:
			IMG_TAG = "opposite";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 3);
			break;

		case R.id.iv_stationContactFrist:
			IMG_TAG = "stationContactFirst";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 4);
			break;

		case R.id.iv_stationContactEnd:
			IMG_TAG = "stationContactEnd";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 5);
			break;
		case R.id.iv_articleDeposit: // 押金条
			IMG_TAG = "articleDeposit";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 6);
			break;
//		case R.id.iv_bankRunning: // 银行流水
//			IMG_TAG = "bankRunning";
//			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 7);
//			break;

		case R.id.iv_nianJianCertificate: // 年检证书
			IMG_TAG = "nianJianCertificate";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 8);
			break;

		case R.id.iv_paySystemCertificate: // 票款汇缴授权书
			IMG_TAG = "paySystemCertificate";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 9);
			break;

		case R.id.iv_fuzerenIdFront: // 负责人身份证正面
			IMG_TAG = "fuzerenIdFront";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 10);
			break;

		case R.id.iv_fuzerenIdOpposite: // 负责人身份证背面
			IMG_TAG = "fuzerenIdOpposite";
			startActivityForResult(new Intent(RealNameAuthenticationA.this, SelectPicPopupWindow.class), 11);
			break;

		// 注册协议
//		case R.id.tv_registerAgreement:
//			intent = new Intent(RealNameAuthenticationA.this, RegisterAgreeA.class);
//			startActivity(intent);
//			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//			break;

		case R.id.bt_registerSubmit:
			depositAmount = et_deposit.getText().toString();
			contactName = et_contactName.getText().toString();
			contactName = removeAllSpace(contactName);
			contactPhone = et_contactPhone.getText().toString();
			corpoName = et_corporationName.getText().toString();
			corpoName = removeAllSpace(corpoName);
			corpIdNum = et_corpIdNum.getText().toString();
			corpMobile = removeAllSpace(et_corpPhone.getText().toString()); // 法人手机号
			operatorName = removeAllSpace(et_fuZeRenName.getText().toString()); // 负责人姓名
			operatorIdNum = removeAllSpace(et_fuZeRenIdNum.getText().toString()); // 负责人身份证号码
			operatorMobile = removeAllSpace(et_fuZeRenPhone.getText().toString()); // 负责人手机
			/*if ("0".equals(agent) && "0".equals(agentType)){
				operatorName = corpoName;
				operatorIdNum = corpIdNum;
				operatorMobile = corpMobile;
				operatorCardforntImg = identificationFront;
				operatorCardrearImg = identificationOpposite;

			}else {*/
				if (checkTag == true){
					operatorName = corpoName;
					operatorIdNum = corpIdNum;
					operatorMobile = corpMobile;
					operatorCardforntImg = identificationFront;
					operatorCardrearImg = identificationOpposite;
				}else {
					if (operatorCardforntImg == null || operatorCardforntImg.isEmpty()){
						Toast.makeText(RealNameAuthenticationA.this, "请上传负责人身份证正面照!", Toast.LENGTH_SHORT).show();
						return;
					}

					if (operatorCardrearImg == null || operatorCardrearImg.isEmpty()){
						Toast.makeText(RealNameAuthenticationA.this, "请上传负责人身份证反面照!", Toast.LENGTH_SHORT).show();
						return;
					}

					if (fareAuthorizationImg == null || fareAuthorizationImg.isEmpty()){
						Toast.makeText(RealNameAuthenticationA.this, "请上传票款汇缴授权书!", Toast.LENGTH_SHORT).show();
						return;
					}
				}


//			}
			if (corpoName.isEmpty() || corpoName == null) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入法人姓名!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (corpIdNum.isEmpty() || corpIdNum == null) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入法人身份证号码!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (RegexUtils.checkIdCard(corpIdNum) == false) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入正确的法人身份证号码!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (contactPhone.isEmpty() || contactPhone == null) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入联系人手机号码!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (RegexUtils.isMobileNO(contactPhone) == false) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入正确的联系人手机号码！", Toast.LENGTH_SHORT).show();
				return;
			}

			if (contactName.isEmpty() || contactName == null) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入联系人姓名!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (businessLicense.isEmpty()) {
				Toast.makeText(RealNameAuthenticationA.this, "请上传营业执照!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (identificationFront.isEmpty()) {
				Toast.makeText(RealNameAuthenticationA.this, "请上传身份证正面照!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (identificationOpposite.isEmpty()) {
				Toast.makeText(RealNameAuthenticationA.this, "请上传身份证反面照!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (corpMobile.isEmpty()) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入法人手机号!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (RegexUtils.isMobileNO(corpMobile) == false) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入正确的法人手机号码！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (operatorName.isEmpty()) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入负责人姓名!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (operatorIdNum.isEmpty() || operatorIdNum == null) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入负责人身份证号码!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (RegexUtils.checkIdCard(operatorIdNum) == false) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入正确的负责人身份证号码!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (operatorMobile == null || operatorMobile.isEmpty()) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入负责人手机!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (RegexUtils.isMobileNO(operatorMobile) == false) {
				Toast.makeText(RealNameAuthenticationA.this, "请输入正确的负责人手机号码!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (staContIndexImg.isEmpty()) {
				Toast.makeText(RealNameAuthenticationA.this, "请上传车站合同首页!", Toast.LENGTH_SHORT).show();
				return;
			}

			if (staContLastImg.isEmpty()) {
				Toast.makeText(RealNameAuthenticationA.this, "请上传车站合同盖章尾页!", Toast.LENGTH_SHORT).show();
				return;
			}

			showAgreeBorrowMoneyDialog();
			break;
		default:
			break;
		}

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

	private void setPicToView(Bitmap image, String imageName) {
		if (!SDCardUtil.hasSDCard()) { // 检测sd是否可用
			return;
		}
		String path = SDCardUtil.getSDCardPath() + File.separatorChar;
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		String fileName = path + imageName;// 图片名字
		try {
			b = new FileOutputStream(fileName);
			image.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				if (b != null) {
					b.flush();
					b.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
		if(data==null){
			return;
		}
       String imagePath =	data.getStringExtra("absolutePath");

	//	final String imagePath = getExternalStorageDirectory() + "/OC/register/image.jpg";
			Luban.with(this)
				.load(new File(imagePath))                     //传人要压缩的图片
				.ignoreBy(300)
				.setCompressListener(new OnCompressListener() { //设置回调
					@Override
					public void onStart() {
						//TODO 压缩开始前调用，可以在方法内启动 loading UI
					}
					@Override
					public void onSuccess(File file) {
						switch (resultCode) {
							case 1:
								switch (requestCode) {
									case 1:
										businessLicense = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_businessLicense);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 2:
										identificationFront = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_identificationFront);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 3:
										identificationOpposite =Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_identificationOpposite);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 4:
										staContIndexImg = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_stationContactFrist);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 5:
										staContLastImg = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_stationContactEnd);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 6:
										staDepositImg = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_articleDeposit);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
//			case 7:
//				bankFlowImg = setImgResult(iv_bankRunning);
//				break;
									case 8:
										staDepInspImg = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_nianJianCertificate);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 9:
										fareAuthorizationImg = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_paySystemCertificate);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 10:
										operatorCardforntImg = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_fuzerenIdFront);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
									case 11:
										operatorCardrearImg = Base64ConvertBitmap.fileToBase64(file);
										try {
											Glide.with(RealNameAuthenticationA.this).load(file).into(iv_fuzerenIdOpposite);
										} catch (Exception e) {
											e.printStackTrace();
										}
										break;
								}

								break;

							default:
								break;

						}
					}

					@Override
					public void onError(Throwable e) {
						//TODO 当压缩过去出现问题时调用
					}
				}).launch();    //启动压缩


	}



	/**
	 * private void setImgResult(int requestCode, int resultCode, Intent data,
	 * ImageView imageView, String imgName){
	 *
	 * if (data != null) {
	 * //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意 Uri
	 * mImageCaptureUri = data.getData();
	 * //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取 if (mImageCaptureUri
	 * != null) { Bitmap image; try { //这个方法是根据Uri获取Bitmap图片的静态方法 image =
	 * MediaStore.Images.Media.getBitmap(this.getContentResolver(),
	 * mImageCaptureUri); if (image != null) {
	 *
	 * setPicToView(image, imgName);//保存在SD卡中
	 * imageView.setImageBitmap(image);//把图片显示到头像 } } catch (Exception e) {
	 * e.printStackTrace(); } } else { Bundle extras = data.getExtras(); if
	 * (extras != null) { //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片 Bitmap
	 * image = extras.getParcelable("data"); if (image != null) {
	 *
	 * // ByteArrayOutputStream stream = new ByteArrayOutputStream(); //
	 * image.compress(Bitmap.CompressFormat.JPEG, 60, stream); // byte[] b =
	 * stream.toByteArray(); // // 将图片流以字符串形式存储下来 // // String tp = new
	 * String(Base64Coder.encodeLines(b)); // // Bitmap dBitmap =
	 * BitmapFactory.decodeFile(tp); // Drawable drawable3 = new
	 * BitmapDrawable(dBitmap); Bitmap compressImg =
	 * compressImage(image);//得到压缩后的图片 if ("license".equals(IMG_TAG)) {
	 * businessLicense = Base64ConvertBitmap.bitmapToBase64(compressImg); }else
	 * if ("front".equals(IMG_TAG)) { identificationFront =
	 * Base64ConvertBitmap.bitmapToBase64(compressImg); }else {
	 * identificationOpposite = Base64ConvertBitmap.bitmapToBase64(compressImg);
	 * }
	 *
	 *
	 * imageView.setImageBitmap(image);//把图片显示到头像 //
	 * imageView.setImageBitmap(compressImg);//把图片显示到头像 } } }
	 *
	 * }
	 *
	 *
	 * Bitmap bitmap =
	 * BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +
	 * "/image.jpg"); // Bitmap compressImg = compressImage(bitmap);//得到压缩后的图片
	 * Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / 5,
	 * bitmap.getHeight() / 5);
	 *
	 * if ("license".equals(IMG_TAG)) { businessLicense =
	 * Base64ConvertBitmap.bitmapToBase64(bitmap); }else if
	 * ("front".equals(IMG_TAG)) { identificationFront =
	 * Base64ConvertBitmap.bitmapToBase64(bitmap); }else {
	 * identificationOpposite = Base64ConvertBitmap.bitmapToBase64(bitmap); }
	 *
	 * imageView.setImageBitmap(newBitmap);//把图片显示到界面 // newBitmap.recycle(); }
	 */

	// 获得照片的文件名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 图片质量压缩方法
	 *
	 * @param image
	 * @return
	 */
	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 90;

		while (baos.toByteArray().length / 1024 > 1024) { // 循环判断如果压缩后图片是否大于1024kb,大于继续压缩
			baos.reset(); // 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			// options -= 10;// 每次都减少10
		}
		Log.d("debug", "baos=" + baos.toByteArray().length);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	private void showAgreeBorrowMoneyDialog() {
		// String url = Define.URL+"acct/getCode";
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.agreedloan_dialog, null);

		Button bt_previousStep = (Button) view.findViewById(R.id.bt_previousStep);
		Button bt_complete = (Button) view.findViewById(R.id.bt_complete);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(view);
		final AlertDialog dialog = builder.create();// 获取dialog

		dialog.show();

		bt_previousStep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		bt_complete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 提交资料
				try {
					dialog.dismiss();
					RealNameAuthenticationA.this.requestRegister();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void showSuccessRegisterDialog() {
		// String url = Define.URL+"acct/getCode";
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.registercomplete_dialog, null);

		TextView tv_userName = (TextView) view.findViewById(R.id.tv_userName);
		tv_userName.setText(userName);
		Button bt_determine = (Button) view.findViewById(R.id.bt_determine);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setView(view);
		final AlertDialog dialog = builder.create();// 获取dialog
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.7f;
		window.setAttributes(lp);
		dialog.show();

		bt_determine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(RealNameAuthenticationA.this, LoginA.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			}
		});
	}

	/**
	 * 注册
	 *
	 * @throws Exception
	 */
//	List<Pair<String, String>> subImageList = new ArrayList<Pair<String, String>>();
	ErrorListener errorResponseListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			pd.dismiss();
			Toast.makeText(RealNameAuthenticationA.this,R.string.netError, Toast.LENGTH_SHORT).show();
			Log.d("REQUEST-ERROR", error.getMessage(), error);
			byte[] htmlBodyBytes = error.networkResponse.data;
			Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

		}
	};
	Listener<JSONObject> responseListener = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			if (null == response) {
				pd.dismiss();
				Toast.makeText(RealNameAuthenticationA.this, "服务端返回异常", Toast.LENGTH_SHORT);
				return;
			}
			Log.d("debug", "response =" + response.toString());
			try {
				String responseCode = response.getString("code");
				String message = response.getString("msg");
				if ("0".equals(responseCode)) {

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("loginName", userName);
					jsonObject.put("staContIndexImg", staContIndexImg);// 车站合同首页
					jsonObject.put("staContLastImg", staContLastImg);// 车站合同尾页
					jsonObject.put("operatorCardforntImg", operatorCardforntImg);// 负责人首页
					jsonObject.put("operatorCardrearImg", operatorCardrearImg);// 车站合同首页
					jsonObject.put("fareAuthorizationImg", fareAuthorizationImg);// 车站合同首页
					JsonObjectRequest supImgRequest = volleyNetCommon.jsonObjectRequest(Method.POST, Define.URL + "user/supImg", jsonObject, new VolleyAbstract(RealNameAuthenticationA.this) {
						@Override
						public void volleyResponse(Object o) {

						}

						@Override
						public void volleyError(VolleyError volleyError) {

						}

						@Override
						protected void netVolleyResponese(JSONObject json) {
							showSuccessRegisterDialog();
						}

						@Override
						protected void PdDismiss() {

						}
					},"supImg", true);
					volleyNetCommon.addQueue(supImgRequest);
					/*if (subImageList.isEmpty()) {
						Toast.makeText(RealNameAuthenticationA.this, "注册成功", Toast.LENGTH_SHORT).show();
						pd.dismiss();
						Message msg = new Message();
						msg.what = REGISTER_SUCCESS;
						mHandler.sendMessage(msg);
					} else {
						JSONObject jsonObject = new JSONObject();
						try {
							jsonObject.put("loginName", userName);
							// 每欠最多提交4个数据
							*//*for (int i = 0; !subImageList.isEmpty() ; i++) {
								jsonObject.put(subImageList.get(0).first, subImageList.get(0).second);
								subImageList.remove(0);
								i--;
							}*//*
							for (Pair pair: subImageList) {
								jsonObject.put((String) pair.first, pair.second);
							}
							subImageList.clear();

						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						SessionJsonObjectRequest subImageRequest = new SessionJsonObjectRequest(Method.POST,
								Define.URL + "user/supImg", jsonObject, responseListener, errorResponseListener);
						// 解决重复请求后台的问题
						subImageRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, // 默认超时时间，应设置一个稍微大点儿的
								DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
								DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

						// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
						subImageRequest.setTag("subImageRequest");
						// 将请求加入全局队列中
						MyApplication.getHttpQueues().add(subImageRequest);
					}*/
				} else {
					pd.dismiss();
					Toast.makeText(RealNameAuthenticationA.this, message, Toast.LENGTH_SHORT).show();
					Log.d("debug", message);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};

	private void requestRegister() throws Exception {
		pd = new CustomProgressDialog(RealNameAuthenticationA.this, "正在处理中...", R.drawable.frame_anim);
		pd.setCancelable(false);//设置进度条不可以按退回键取消
		pd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
		pd.show();
		Intent lastIntent = getIntent();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("winNumber", windowNumber);//补齐4位后的窗口号
			jsonObject.put("loginName", userName);
			jsonObject.put("officeType", identity);
			jsonObject.put("password", password);
			jsonObject.put("mobile", phoneNumber);
			jsonObject.put("officeName", orgName);
			jsonObject.put("areaCode", cityCode);
			jsonObject.put("address", orgAddress);
			jsonObject.put("corpName", corpoName);
			jsonObject.put("corpMobile", corpMobile); // 法人手机号
			jsonObject.put("operatorName", operatorName); // 负责人姓名
			jsonObject.put("operatorIdNum", operatorIdNum); // 负责人身份证号
			jsonObject.put("operatorMobile", operatorMobile); // 负责人手机号
			jsonObject.put("jurAreaCode", lastIntent.getStringExtra("jurAreaCode")); // 所属管辖区域
			jsonObject.put("agentCompanyName", lastIntent.getStringExtra("agentCompanyName")); // 代理人公司名称
			jsonObject.put("agent", lastIntent.getStringExtra("agent")); // 第一代理人
			jsonObject.put("agentType", lastIntent.getStringExtra("agentType")); // 代理人类型
			jsonObject.put("master", contactName);
			jsonObject.put("phone", contactPhone);
			jsonObject.put("corpIdNum", corpIdNum);
			// if (openId.isEmpty() || loginType.isEmpty()) {
			// jsonObject.put("openId", "");
			// jsonObject.put("registerType", "");
			//
			// }else {
			jsonObject.put("openId", openId);
			jsonObject.put("registerType", loginType);
			// }
			jsonObject.put("corpLicenceImg", businessLicense); // 营业执照
			jsonObject.put("corpCardForntImg", identificationFront);// 法人身份证正面
			jsonObject.put("corpCardRearImg", identificationOpposite);// 法人身份证负面
//			jsonObject.put("fareAuthorizationImg", fareAuthorizationImg);// 票款汇缴授权书
//			jsonObject.put("staDepositImg", staDepositImg); // 车站押金条

			// 分批提交，其它的先缓存
			/*if (!staContIndexImg.isEmpty())
				subImageList.add(new Pair<String, String>("staContIndexImg", staContIndexImg));

			if (!staContLastImg.isEmpty())
				subImageList.add(new Pair<String, String>("staContLastImg", staContLastImg));

//			if (!staDepInspImg.isEmpty())
//				subImageList.add(new Pair<String, String>("staDepInspImg", staDepInspImg));
//			if (!bankFlowImg.isEmpty())
//				subImageList.add(new Pair<String, String>("bankFlowImg", bankFlowImg));
			if (!operatorCardforntImg.isEmpty())
				subImageList.add(new Pair<String, String>("operatorCardforntImg", operatorCardforntImg));
			if (!operatorCardrearImg.isEmpty())
				subImageList.add(new Pair<String, String>("operatorCardrearImg", operatorCardrearImg));
			if (!fareAuthorizationImg.isEmpty())
				subImageList.add(new Pair<String, String>("fareAuthorizationImg", fareAuthorizationImg));*/


			jsonObject.put("staContIndexImg", ""); // 车站合同首页
			jsonObject.put("staContLastImg", ""); // 车站合同末页

			jsonObject.put("staDepInspImg", ""); // 车站押金年检证书
//			jsonObject.put("bankFlowImg", ""); // 半年银行流水
			jsonObject.put("operatorCardforntImg", ""); // 负责人身份证正面
			jsonObject.put("operatorCardrearImg", "");// 负责人身份证背面
			jsonObject.put("fareAuthorizationImg", ""); // 票款汇缴授权书
			jsonObject.put("deposit", depositAmount); // 所缴纳押金金额

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String url = Define.URL + "user/busRegister";
		SessionJsonObjectRequest completeRegisterRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				responseListener, errorResponseListener);
		// 解决重复请求后台的问题
		completeRegisterRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		completeRegisterRequest.setTag("completeRegisterRequest");

		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(completeRegisterRequest);

	}

	private static final int REGISTER_SUCCESS = 1;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REGISTER_SUCCESS: {
				showSuccessRegisterDialog();
				break;
			}
			}
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("completeRegisterRequest");
		MyApplication.getHttpQueues().cancelAll("subImageRequest");
		RequestQueue requestQueue = volleyNetCommon.getRequestQueue();
		if (requestQueue != null) {
			requestQueue.cancelAll("supImg");

		}
	}


	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		if (checkBox.isChecked()){
						layout_fuZeRenInfo.setVisibility(View.GONE);
						layout_fuZeRenCertificates.setVisibility(View.GONE);
//						layout_shouQuanShu.setVisibility(View.INVISIBLE);
//						layout_fuzerenIdFront.setVisibility(View.INVISIBLE);
						et_corpPhone.setText(phoneNumber);
						et_corpPhone.setEnabled(false);
						checkTag = true;
					}else {
						layout_fuZeRenInfo.setVisibility(View.VISIBLE);
						layout_fuZeRenCertificates.setVisibility(View.VISIBLE);
						layout_shouQuanShu.setVisibility(View.VISIBLE);
//						layout_fuzerenIdFront.setVisibility(View.VISIBLE);
						et_corpPhone.setEnabled(true);
						et_corpPhone.setText("");
						checkTag = false;
					}
	}
}

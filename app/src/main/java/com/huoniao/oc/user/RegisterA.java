package com.huoniao.oc.user;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.useragreement.RegisterAgreeA;
import com.huoniao.oc.util.CountDownTimerUtils;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private EditText et_phoneNumber,et_verCode;
	private String phoneNumber, verCode, openId, loginType;
	private TextView tv_getVericode;
	
	private ImageView imageVerCode;
	private EditText imageVerCodeInput;
	CountDownTimerUtils mCountDownTimerUtils;
	private Button bt_nextStep;
	private Intent intent;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
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
		et_phoneNumber = (EditText) findViewById(R.id.et_phoneNumber);
		et_verCode = (EditText) findViewById(R.id.et_verCode);
		tv_getVericode = (TextView) findViewById(R.id.tv_getVericode);
		bt_nextStep = (Button) findViewById(R.id.bt_nextStep);
	
		iv_back.setOnClickListener(this);
		tv_getVericode.setOnClickListener(this);
		bt_nextStep.setOnClickListener(this);
		
		pd = new CustomProgressDialog(RegisterA.this, "正在加载中...", R.drawable.frame_anim);
		pd.setCancelable(false);//设置进度条不可以按退回键取消
		pd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_getVericode:
			phoneNumber = et_phoneNumber.getText().toString();
			verCode = et_verCode.getText().toString();
			
			if ("".equals(phoneNumber) || phoneNumber == null) {
				Toast.makeText(RegisterA.this, "请输入您的手机号码!", Toast.LENGTH_SHORT).show();
			}else if (RegexUtils.isMobileNO(phoneNumber) == false) {
				Toast.makeText(RegisterA.this, "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
			}
			else {
				showImageVeriCodeDialog();
				
			}
			break;
		//注册协议	
//		case R.id.tv_registerAgreement:
//			intent = new Intent(RegisterA.this, RegisterAgreeA.class);
//			startActivity(intent);
//			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//			break;
		//下一步	
		case R.id.bt_nextStep:
			createAccount();;
			/*intent = new Intent(RegisterA.this, SetIdentityInfoA.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);*/
			break;
		default:
			break;
		}
		
	}
	
	
	
	
	/**
	 * 请求下一步
	 */
	private void createAccount(){
		phoneNumber = et_phoneNumber.getText().toString();
		verCode = et_verCode.getText().toString();
		if (verCode.isEmpty()) {
			Toast.makeText(RegisterA.this, "请输入验证码!", Toast.LENGTH_LONG).show();
			return;
		}
		
		JSONObject jsonObj = new JSONObject();
	    try {
	    	
			jsonObj.put("mobile",phoneNumber);
			jsonObj.put("type",Define.REGISTE_VERCODE);
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
						intent = new Intent(RegisterA.this, SetIdentityInfoA.class);
						Bundle bundle = new Bundle();
						bundle.putString("poneNumber", phoneNumber);
						bundle.putString("openId", openId);
						bundle.putString("loginType", loginType);
						intent.putExtras(bundle);
//						pd.dismiss();
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
						
					}else {
						Toast.makeText(RegisterA.this, "验证码输入错误!", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.d("debug", "vercode = " + response.toString());
	
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(RegisterA.this, R.string.netError, Toast.LENGTH_SHORT).show();
				Log.d("REQUEST-ERROR", error.getMessage(), error);
//           	  	byte[] htmlBodyBytes = error.networkResponse.data;
//           	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
			}
		});
	    
	    //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
	    verificationCreateAccount.setTag("verificationCreateAccount");
	    //将请求加入全局队列中
	    MyApplication.getHttpQueues().add(verificationCreateAccount);
	}
	
	 /**
     * 获取验证码
     */
    private void showImageVeriCodeDialog(){
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_imagevericode, null);
		imageVerCode = (ImageView) view.findViewById(R.id.imageVeriCode);
		imageVerCodeInput = (EditText) view.findViewById(R.id.et_imageVeriCode);
		Button bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_concel);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(view);
		final AlertDialog dialog = builder.create();//获取dialog
		final String imageUrl = Define.URL+"user/buildIconCode";
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
				    	phoneNumber = et_phoneNumber.getText().toString();
						jsonObj.put("mobile",phoneNumber);
						jsonObj.put("iconCode",imageCode);
						Log.d("debug", "imageCode = "+ imageCode);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
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
									dialog.dismiss();
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("mobile",phoneNumber);
									jsonObject.put("type", Define.REGISTE_VERCODE);
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
													Toast.makeText(RegisterA.this, R.string.netError, Toast.LENGTH_LONG).show();
													
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
								}else if ("61451".equals(code)) {
									Toast.makeText(RegisterA.this, "图形验证码输入错误！", Toast.LENGTH_LONG).show();
								}else {
									Toast.makeText(RegisterA.this, "手机号已注册！", Toast.LENGTH_LONG).show();
									Log.d("cofinm", message);
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
							Toast.makeText(RegisterA.this, R.string.netError, Toast.LENGTH_SHORT).show();
							
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
				    request.setTag("validateMobileAndCode");
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
    private void loadImageByVolley(String url, final ImageView image) {
//        String url = "http://ecy.120368.com/api/buildIconCode";
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
                                	Toast.makeText(RegisterA.this, R.string.netError, Toast.LENGTH_SHORT).show();
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
        request.setTag("loadRegisterImgCode");
        //通过Tag标签取消请求队列中对应的全部请求
        MyApplication.getHttpQueues().add(request);
    }
    
    @Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("loadRegisterImgCode");
		MyApplication.getHttpQueues().cancelAll("verificationCreateAccount");
		
		MyApplication.getHttpQueues().cancelAll("validateMobileAndCode");
		MyApplication.getHttpQueues().cancelAll("updataPawRequest");
	}
}

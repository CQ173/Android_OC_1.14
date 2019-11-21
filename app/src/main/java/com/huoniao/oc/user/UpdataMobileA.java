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
import com.huoniao.oc.util.CountDownTimerUtils;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdataMobileA extends BaseActivity implements OnClickListener{


	private ImageView iv_back;
	private TextView tv_phoneNumber, tv_getVericode;
	private EditText et_verCode;
	private Button bt_next;
	private String mobile;
	private ImageView imageVerCode;
	private EditText imageVerCodeInput;
	CountDownTimerUtils mCountDownTimerUtils;
	private String verCode;
	private ProgressDialog pd;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatemobile);
		initView();
		initData();
	}
	private void initData() {
		intent = getIntent();
		mobile = intent.getStringExtra("mobile");
		tv_phoneNumber.setText(mobile);
	}
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_phoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
		tv_getVericode = (TextView) findViewById(R.id.tv_getVericode);
		et_verCode = (EditText) findViewById(R.id.et_verCode);
		bt_next = (Button) findViewById(R.id.bt_next);
		iv_back.setOnClickListener(this);
		tv_getVericode.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		pd = new CustomProgressDialog(UpdataMobileA.this, "正在加载中...", R.drawable.frame_anim);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.tv_getVericode:
			showImageVeriCodeDialog();
			break;
			
		case R.id.bt_next:
			verCode = et_verCode.getText().toString();
			if (verCode.isEmpty()) {
				Toast.makeText(UpdataMobileA.this, "请输入验证码!", Toast.LENGTH_LONG).show();
				return;
			}
			requestNext();
			break;	
		default:
			break;
		}
		
	}
	
	 /**
     * 获取验证码
     * 
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
						jsonObj.put("mobile",mobile);
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
								if ("0".equals(code)) {
									dialog.dismiss();
									JSONObject jsonObject = new JSONObject();
									jsonObject.put("mobile",mobile);
									jsonObject.put("type", Define.UPTATA_BINDPHONE_VERCODE);
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
													Toast.makeText(UpdataMobileA.this, R.string.netError, Toast.LENGTH_LONG).show();
													
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
									phoneVerCodeRequest.setTag("updataMobileCode");
								    //将请求加入全局队列中
								    MyApplication.getHttpQueues().add(phoneVerCodeRequest);
//									mCountDownTimerUtils = new CountDownTimerUtils(tv_getVericode, 60000, 1000);
//									mCountDownTimerUtils.start();
								}else if ("61451".equals(code)) {
									Toast.makeText(UpdataMobileA.this, "图形验证码输入错误！", Toast.LENGTH_LONG).show();
								}else {
									Toast.makeText(UpdataMobileA.this, message, Toast.LENGTH_LONG).show();
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
							Toast.makeText(UpdataMobileA.this, R.string.netError, Toast.LENGTH_SHORT).show();
							
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
				    request.setTag("vriMobileAndCode");
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
                              	Toast.makeText(UpdataMobileA.this, R.string.netError, Toast.LENGTH_SHORT).show();
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
      request.setTag("updataMobileImgCode");
      //通过Tag标签取消请求队列中对应的全部请求
      MyApplication.getHttpQueues().add(request);
  }
    
    /**
	 * 请求下一步
	 */
	private void requestNext(){
		
		
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
						intent = new Intent(UpdataMobileA.this, UpdataMobileTwoA.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        finish();
					}else {
						Toast.makeText(UpdataMobileA.this, "验证码输入错误!", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				Log.d("debug", "vercode = " + response.toString());
	
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(UpdataMobileA.this, R.string.netError, Toast.LENGTH_SHORT).show();
				Log.d("REQUEST-ERROR", error.getMessage(), error);
//           	  	byte[] htmlBodyBytes = error.networkResponse.data;
//           	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
			}
		});
	    
	    //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
	    verificationCreateAccount.setTag("vriOldMobile");
	    //将请求加入全局队列中
	    MyApplication.getHttpQueues().add(verificationCreateAccount);

	}



	@Override
   	protected void onStop() {
   		super.onStop();
   		MyApplication.getHttpQueues().cancelAll("updataMobileImgCode");
   		MyApplication.getHttpQueues().cancelAll("updataMobileCode");
   		MyApplication.getHttpQueues().cancelAll("vriMobileAndCode");
   		MyApplication.getHttpQueues().cancelAll("vriOldMobile");
   	}
}

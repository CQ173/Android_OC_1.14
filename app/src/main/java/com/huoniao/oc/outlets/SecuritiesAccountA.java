package com.huoniao.oc.outlets;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.Define;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.huoniao.oc.util.ImageCompres.getBitmapFormUri;

public class SecuritiesAccountA extends BaseActivity implements OnClickListener{
	
	private ImageView iv_back, iv_QRcode;
	private Button bt_OpenAccount;
	final static int MESSAGE_SHOW_IMG = 0;
	final static int MESSAGE_RESULT_ERR = 1;
	Handler handler;
//	private ProgressDialog pd;
	private RelativeLayout bg_securities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_outlets_securitiesaccount);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		bt_OpenAccount = (Button) findViewById(R.id.bt_OpenAccount);
		bg_securities = (RelativeLayout) findViewById(R.id.securities_bg);
		initweight();
//		iv_QRcode = (ImageView) findViewById(R.id.iv_QRcode);

//		String url = "http://192.168.0.117:8181/OC/api/acct/getCode";
//		String url = "http://ecy.120368.com/api/acct/account/getCode";
//		loadImageByVolley(url, iv_QRcode);
		
		/**
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case MESSAGE_SHOW_IMG:
					iv_QRcode.setImageBitmap((Bitmap) msg.obj);
					break;
				case MESSAGE_RESULT_ERR:
					Toast.makeText(SecuritiesAccountA.this,"获取失败",Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			};
		};
		*/
//		ExitApplication.getInstance().addActivity(this);
	}

	private void initweight() {

		Resources r = getResources();
		Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
				+ r.getResourcePackageName(R.drawable.securities_account_bg) + "/"
				+ r.getResourceTypeName(R.drawable.securities_account_bg) + "/"
				+ r.getResourceEntryName(R.drawable.securities_account_bg));

		Bitmap bitmapFormUri = null;
		try {
			bitmapFormUri = getBitmapFormUri(this, uri);  // 通过uri获取图片并进行压缩
			Log.e("图片大小：",bitmapFormUri.getByteCount()/1024/1024+"mb");
			Drawable drawable =new BitmapDrawable(bitmapFormUri);
			bg_securities.setBackgroundDrawable(drawable);
		} catch (IOException e) {
			e.printStackTrace();
		}



		iv_back.setOnClickListener(this);
		bt_OpenAccount.setOnClickListener(this);
//		pd = new CustomProgressDialog(SecuritiesAccountA.this, "正在加载中...", R.anim.frame_anim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_OpenAccount:
			openAccountDialog();
			break;
		default:
			break;
		}
		
	}
	
	private void openAccountDialog(){
		String url = Define.URL+"acct/getCode";
//		String url = Define.URL+"acct/wallet/getPayCode";
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.securities_account_dialog, null);
		iv_QRcode = (ImageView) view.findViewById(R.id.iv_QRcode);
//		Button bt_download = (Button) view.findViewById(R.id.bt_download);
		Button bt_close = (Button) view.findViewById(R.id.bt_close);
		loadImageByVolley(url, iv_QRcode);
//		loadQRcodeImg(url);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setView(view);
		final AlertDialog dialog = builder.create();//获取dialog
	       
	    dialog.show();
	   
	   
	    bt_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
	}
	
	/**
	private void loadQRcodeImg(final String url){
		
		new Thread(new Runnable() {
			Bitmap bitmap = null;
			InputStream is;
			HttpURLConnection connection;
			
			@Override
			public void run() {
				try {
					
					URL imgUrl = new URL(url);
					connection = (HttpURLConnection) imgUrl.openConnection();
					connection.setConnectTimeout(3000);
					connection.setRequestMethod("GET");
					connection.setDoInput(true);
					int code = connection.getResponseCode();
					if (code == 200) {
						//请求成功
						is = connection.getInputStream();
						bitmap = BitmapFactory.decodeStream(is);
						 Message message = new Message();
						 //设置消息的数据
						 message.obj = bitmap;
						 //设置消息的类型值
						 message.what = MESSAGE_SHOW_IMG;
					     //发送消息到handler
					     handler.sendMessage(message);
					}else {
						//请求失败,同样也发送消息
						Message message = new Message();
						//设置消息的类型值
						message.what = MESSAGE_RESULT_ERR;
						//发送消息到handler
						handler.sendMessage(message);
					}
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
			
					e.printStackTrace();
				}finally{
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						connection.disconnect();
					}
				}
				
			}
		}).start();
	}	
	*/
	
	private void loadImageByVolley(String url, final ImageView image) {
//      String url = "http://ecy.120368.com/api/buildIconCode";
		cpd.show();
        ImageRequest qrCodeRequest = new ImageRequest(
                          url,
                          new Listener<Bitmap>() {
                              @Override
                              public void onResponse(Bitmap bitmap) {
                                  image.setImageBitmap(bitmap);
                                  cpd.dismiss();
                                 
                              }
                          },
                          0, 0, Config.RGB_565,
                          new ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError volleyError) {
                            	  cpd.dismiss();
                            	  Toast.makeText(SecuritiesAccountA.this, "二维码不足，请联系系统管理员!", Toast.LENGTH_LONG).show();
                            	  Log.d("LOGIN-ERROR", volleyError.getMessage(), volleyError);
//                            	  byte[] htmlBodyBytes = volleyError.networkResponse.data;
//                            	  Log.d("LOGIN-ERROR", new String(htmlBodyBytes), volleyError);
//                                  image.setImageResource(R.drawable.erweima);
                              }
                          }){
	    	//重写getHeaders 默认的key为cookie，value则为localCookie  
    	  	@Override
    	    public Map<String, String> getHeaders() throws AuthFailureError {
    	        Map<String, String> headers = super.getHeaders();

    	        if (headers == null
    	                || headers.equals(Collections.emptyMap())) {
    	            headers = new HashMap<String, String>();
    	        }

    	        MyApplication.newInstance().addSessionCookie(headers);

    	        return headers;
    	    }
	    };
		
//		SessionImageRequest qrCodeRequest = new SessionImageRequest(url, new Listener<Bitmap>() {
//
//			@Override
//			public void onResponse(Bitmap bitmap) {
//				image.setImageBitmap(bitmap);
//				
//			}
//		}, 0, 0, Config.RGB_565, new ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				Log.d("debug", "volleyError =" + error.toString());
//              image.setImageResource(R.mipmap.ic_launcher);
//				
//			}
//		});
      
      //解决重复请求后台的问题
		qrCodeRequest.setRetryPolicy(
              new DefaultRetryPolicy(
                      10*1000,//默认超时时间，应设置一个稍微大点儿的
                      DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                      DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
              )
      );
      //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		qrCodeRequest.setTag("loadQRcode");
      //通过Tag标签取消请求队列中对应的全部请求
      MyApplication.getHttpQueues().add(qrCodeRequest);
  }
	
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("loadQRcode");
	}
	

}

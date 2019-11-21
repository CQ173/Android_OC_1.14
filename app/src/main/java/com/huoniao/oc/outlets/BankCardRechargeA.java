package com.huoniao.oc.outlets;


import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.MD5Encoder;
import com.huoniao.oc.util.ObjectSaveUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BankCardRechargeA extends BaseActivity implements OnClickListener{
	
	private ImageView iv_back;
	private WebView webView;
	private String rechargeAmount;//充值金额
	private String userId = "";//用户id
	private String parentId = "";//父账号id
	private Intent intent;
//	private LinearLayout layout_bankCardOption;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outlets_bankcard_recharge);
		initView();
		
        
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		intent = getIntent();
		rechargeAmount = intent.getStringExtra("rechargeAmount");
		iv_back = (ImageView) findViewById(R.id.iv_back);
//		layout_bankCardOption = (LinearLayout) findViewById(R.id.layout_bankCardOption);
		iv_back.setOnClickListener(this);
//		layout_bankCardOption.setOnClickListener(this);
		webView = (WebView) findViewById(R.id.wv_unionPay);
		WebSettings webSettings = webView.getSettings();
		//支持JS
        webSettings.setJavaScriptEnabled(true);

        webSettings.setAllowFileAccess(true);

        webSettings.setDomStorageEnabled(true);
        User user = (User) ObjectSaveUtil.readObject(BankCardRechargeA.this, "loginResult");
        userId = user.getId();
        parentId = user.getParentId();
        if (parentId.isEmpty() || parentId == null) {
			parentId = userId;
		}
        
        if (!userId.isEmpty() && userId != null) {
//        	 Intent intent = new Intent();        
//             intent.setAction("android.intent.action.VIEW");             
//             Uri content_url = Uri.parse("http://ecy.120368.com/chinapay/chinapayapi.jsp?WIDtotal_fee=" + rechargeAmount + "&user_id=" + userId + "&parent_id=" + parentId);   
//             intent.setData(content_url);  
//             startActivity(intent);
        	 webView.loadUrl("http://ecy.120368.com/chinapay/chinapayapi.jsp?WIDtotal_fee=" + rechargeAmount + "&user_id=" + userId + "&parent_id=" + parentId);
             
             //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
             webView.setWebViewClient(new WebViewClient(){
                 @Override
              public boolean shouldOverrideUrlLoading(WebView view, String url) {
                  // TODO Auto-generated method stub
                     //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                   view.loadUrl(url);
                  return true;
              }
             });
		}else {
			Toast.makeText(BankCardRechargeA.this, "用户信息验证失败！", Toast.LENGTH_SHORT).show();	
			return;
		}
       
	}
	
	
	@Override
	public void onClick(View v) {                                                                                                                                                                                          
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
//		case R.id.layout_bankCardOption:
//			showBankCardOptionDialog();
//			break;
		default:
			break;
		}
	}
	
	/**
	@SuppressWarnings("deprecation")
	private void showBankCardOptionDialog(){
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.activity_windowanchored.item_bankcard_option, null);
		ImageView iv_dismiss = (ImageView) view.findViewById(R.id.iv_dismiss);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setView(view);
		final AlertDialog dialog = builder.create();//获取dialog
	        Window dialogWindow = dialog.getWindow();
	        dialogWindow.setGravity(Gravity.BOTTOM);
	          
	        dialog.show();
	       
	        WindowManager m = dialogWindow.getWindowManager();
	        //获得屏幕默认宽高
	        Display d = m.getDefaultDisplay();
	       // 实例化Window 
	        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	        lp.width = d.getWidth();//设置dialog的宽度为当前手机屏幕的宽度
//	        //实例化Window操作者
//	        lp.x = 0; // 新位置X坐标
//	        lp.y = 150; // 新位置Y坐标
	        //放置属性
	        dialogWindow.setAttributes(lp);
	        iv_dismiss.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
	}
	
	*/
	
}

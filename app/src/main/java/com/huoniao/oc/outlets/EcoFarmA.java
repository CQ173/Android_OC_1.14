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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class EcoFarmA extends BaseActivity{
	private WebView webView;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_outlets_ecofarm);
		
		webView = (WebView) findViewById(R.id.wv_ecoFarm);
		WebSettings webSettings = webView.getSettings();
		//支持JS
        webSettings.setJavaScriptEnabled(true);

        webSettings.setAllowFileAccess(true);

        webSettings.setDomStorageEnabled(true);
//        String sessionId = MyApplication.preferences.getString("JSESSIONID", "");
//		webView.loadUrl(Define.URL+"/partner/farm;"+"jsessionid=" + sessionId);
        User user = (User) ObjectSaveUtil.readObject(EcoFarmA.this, "loginResult");
        Log.d("mobile", user.getMobile());
        Log.d("userName", user.getUserCode());
//        webView.loadUrl(Define.FARM_URL+"/outlets/ologin.jhtml?username=" + user.getMobile() + "&outletsAccount=" + user.getUserCode());
//		ExitApplication.getInstance().addActivity(this);
        Intent intent = new Intent();        
        intent.setAction("android.intent.action.VIEW");
        String publicKey = user.getUserCode() + user.getMobile() + Define.ECO_PASSWORD;
        Log.d("debug", "md5PublicKey =" + publicKey);
        String md5PublicKey = MD5Encoder.encode(publicKey);
        Log.d("debug", "md5PublicKey =" + md5PublicKey);
        Uri content_url = Uri.parse(Define.FARM_URL+"/outlets/ologin.jhtml?username=" + user.getUserCode()+ "&mobile=" + user.getMobile() + "&publicKey=" + md5PublicKey);   
        intent.setData(content_url);  
        startActivity(intent);

	}
}

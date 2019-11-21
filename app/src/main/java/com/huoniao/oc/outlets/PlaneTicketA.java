package com.huoniao.oc.outlets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.alipay.AlipayCore;
import com.huoniao.oc.alipay.MD5;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ExitApplication;
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

public class PlaneTicketA extends BaseActivity{
	private WebView webView;
	@SuppressWarnings("rawtypes")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_outlets_planeticket);
		webView = (WebView) findViewById(R.id.wv_planeTicket);
		WebSettings webSettings = webView.getSettings();
		//支持JS
        webSettings.setJavaScriptEnabled(true);

        webSettings.setAllowFileAccess(true);

        webSettings.setDomStorageEnabled(true);
        
        User user = (User) ObjectSaveUtil.readObject(PlaneTicketA.this, "loginResult");
        Map<String, String> parameters = new TreeMap<String, String>();
		parameters.put("service","user_login");
		parameters.put("partner", "CSX_A0180386247");
		parameters.put("outer_app_token", "CSX_A0180386247_subagency_admin");
		parameters.put("outer_login_name", user.getUserCode());
		parameters.put("user_name", user.getName());
		parameters.put("goto_url", "http://www.1203688.com/caigou/manage/index.in?isLogin=true");
		parameters.put("user_type", "AGENCY_SINGLE_USER");
		parameters.put("return_url", "http://www.1203688.com");
		parameters.put("time_stamp", String.valueOf(System.currentTimeMillis()));
		parameters.put("input_charset", "utf-8");
		//除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(parameters);
        //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String prestr = AlipayCore.createLinkString(sPara);
		Log.d("debug", "prestr=" + prestr);
		String mysign = MD5.sign(prestr, "tdts_@(51).", "utf-8");
		//签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		sPara.put("sign_type", "MD5");
        
        StringBuffer sb = new StringBuffer();
        Set es = sPara.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(null != v && !"".equals(v)) {
				try {
					sb.append(k + "=" +  URLEncoder.encode(v,"utf-8") + "&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		String reqPars = sb.substring(0, sb.lastIndexOf("&"));
        String url = "http://aio.51book.com/partner/cooperate.in?"+reqPars;
        Log.d("debug", "url=" + url);

//        String sessionId = MyApplication.preferences.getString("JSESSIONID", "");
//		webView.loadUrl("http://www.1203688.com/caigou/manage/index.in?isLogin=true&fromTMS=1&global_token=EtCElrYwzf6MPkihsxm1Ng%3D%3D&ltywfz=0&gt_sc=L0M0THZLKKGHEVL");
//        webView.loadUrl(url);
//		ExitApplication.getInstance().addActivity(this);
        Intent intent = new Intent();        
        intent.setAction("android.intent.action.VIEW");    
        Uri content_url = Uri.parse(url);   
        intent.setData(content_url);  
        startActivity(intent);
	}
}

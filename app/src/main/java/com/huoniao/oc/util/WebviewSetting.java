package com.huoniao.oc.util;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.huoniao.oc.MyApplication;

/**
 * Created by Administrator on 2017/5/22.
 */

public class WebviewSetting {
    public static void setShopping(final WebView wvShopping , final ProgressBar pbShopping, String url) {


        WebSettings webSettings = wvShopping.getSettings();
        ////设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		//设置可以访问文件
		webSettings.setAllowFileAccess(true);
		//设置支持缩放
		webSettings.setBuiltInZoomControls(true);

		webSettings.setDomStorageEnabled(true);
		webSettings.setAppCacheEnabled(true); //设置缓存

		webSettings.setLoadWithOverviewMode(true);
	    webSettings.setSupportZoom(true);

	    webSettings.setUseWideViewPort(true);

	    webSettings.setSavePassword(true);
	    webSettings.setSaveFormData(true);
        webSettings.setDisplayZoomControls(false);  //不显示放大缩小按钮

      //  String url = Define.SHOPPING;

        CookieSyncManager.createInstance(MyApplication.mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, MyApplication.getSessionid());

        CookieSyncManager.getInstance().sync(); //同步cookie
       String s = cookieManager.getCookie(url);

        wvShopping.loadUrl(url);
        wvShopping.requestFocus();
        wvShopping.setHorizontalScrollBarEnabled(false);
        wvShopping.setVerticalScrollBarEnabled(false);

        wvShopping.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
              //  String s =  CookieManager.getInstance().getCookie(url);
                CookieSyncManager.createInstance(MyApplication.mContext);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.setCookie(url,MyApplication.getSessionid());
                CookieSyncManager.getInstance().sync(); //同步cookie
                String ss = cookieManager.getCookie(url);
                if(url.startsWith("intent")||url.startsWith("youku")){  //解决优酷视频无法播放问题
                    return true;
                }
                if(url.startsWith("newtab:")){
                    String realUrl=url.substring(7,url.length());
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setData(Uri.parse(realUrl));
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.mContext.startActivity(it);
                }else{
                    view.loadUrl(url);
                }
              //  view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String ss =	CookieManager.getInstance().getCookie(url);

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed(); // 接受所有网站的证书
            }



        });


        wvShopping.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pbShopping.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    pbShopping.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pbShopping.setProgress(newProgress);//设置进度值
                }

            }
        });

    }
}

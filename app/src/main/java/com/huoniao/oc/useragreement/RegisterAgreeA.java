package com.huoniao.oc.useragreement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterAgreeA extends BaseActivity {
	@InjectView(R.id.tv_title)
	TextView tv_title;
	@InjectView(R.id.wv_protocal)
	WebView webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_agreement);
		ButterKnife.inject(this);
		initView();
	}
	
	private void initView() {
		/*iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);*/

		WebSettings webSettings = webview.getSettings();

		// 设置与Js交互的权限
		webSettings.setJavaScriptEnabled(true);

		// 设置允许JS弹窗
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		webview.clearCache(true);//传入false仅清除内存缓存，传入true将同时清除内存和磁盘缓存
		webview.loadUrl(getIntent().getStringExtra("url"));
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				String title = view.getTitle();
				if (!TextUtils.isEmpty(title)) {
					tv_title.setText(title);
				}
			}
		});

	}

	@OnClick({R.id.iv_back})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		}
		
	}

	@Override
	public boolean
	onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			webview.goBack();//返回上个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);//退出H5界面
	}

}

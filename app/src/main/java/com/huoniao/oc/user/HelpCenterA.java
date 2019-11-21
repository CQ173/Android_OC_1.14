package com.huoniao.oc.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.WebviewSetting;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class HelpCenterA extends BaseActivity {
    @InjectView(R.id.progressBar1)
    ProgressBar progressBar1;
    @InjectView(R.id.wv_helpCenter)
    WebView webView;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.tv_customer_service_hotline)
    TextView tvCustomerServiceHotline;
    @InjectView(R.id.tv_work_time)
    TextView tvWorkTime;
    @InjectView(R.id.iv_customer_call_phone)
    ImageView ivCustomerCallPhone;
    private String customerPhone;
    private User user;
    private String roleName;
    private Intent intent;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpcenter);
        ButterKnife.inject(this);
        initWidget();
        String learn_more = getIntent().getStringExtra("learn_more");
        if (learn_more == null) {
            WebviewSetting.setShopping(webView, progressBar1, Define.HELP_CENTER); //帮助中心
        } else if (learn_more.equals("learn_more")) {
            WebviewSetting.setShopping(webView, progressBar1, Define.LEARN_MORE); //了解更多
        }
            /* WebSettings webSettings = webView.getSettings();
        ////设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);

        webSettings.setDomStorageEnabled(true);

      String url = Define.HELP_CENTER;

     //String url = "http://192.168.0.111:8181/OC/app/help.html"; //TODO    临时用

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
             //载入页面结束后，加载js,修改链接
           *//* @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript: var allLinks = document.getElementsByTagName('a'); if (allLinks) {var i;for (i=0; i<allLinks.length; i++) {var link = allLinks[i];var target = link.getAttribute('target'); if (target && target == '_blank') {link.setAttribute('target','_self');link.href = 'newtab:'+link.href;}}}");
            }*//*

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);
                handler.proceed(); // 接受所有网站的证书
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                System.out.println(url);
                if(url.startsWith("newtab:")){
                    String realUrl=url.substring(7,url.length());
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setData(Uri.parse(realUrl));
                    startActivity(it);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根

                if (newProgress == 100) {
                    progressBar1.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar1.setProgress(newProgress);//设置进度值
                }

            }
        });*/


    }

    private void initWidget() {
        customerPhone = "0731-88611852";
        tvCustomerServiceHotline.setText("客服热线: "+customerPhone);
        tvWorkTime.setText("客服工作时间：09:00-21:00");

        tvSave.setText("我要反馈");
        tvTitle.setText("帮助与反馈");

        user = (User) readObject(HelpCenterA.this, "loginResult");
        roleName = user.getRoleName(); //角色进来判断
        if (roleName != null && roleName.contains("系统管理员")) {
            tvSave.setVisibility(View.GONE);
        }else{
           // tvSave.setVisibility(View.VISIBLE);
            setPremissionShowHideView(Premission.FB_FEEDBACK_ADD, tvSave);  //是否有反馈消息这个权限  fb:feedback:add
        }


    }


    //设置返回键动作（防止按返回键直接退出当前界面)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                webView.goBack();
                return true;
            } else {//当webview处于第一页面时,直接退出程序
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);








      /*  Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");    
        Uri content_url = Uri.parse(url);   
        intent.setData(content_url);  
        startActivity(intent);*/
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        //释放webview
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }


    @OnClick({R.id.iv_back, R.id.tv_save, R.id.iv_customer_call_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save: //我要反馈
                intent = new Intent(HelpCenterA.this, UserFeedbackInfoA.class);
                startActivityIntent(intent);
                break;
            case R.id.iv_customer_call_phone:
                callPhone();
                break;
        }
    }

   MyPopWindow  myCallPhonePopwindow;
    /**
     * 打电话
     */
    private void callPhone() {
        if(myCallPhonePopwindow !=null && myCallPhonePopwindow.isShow()){
            myCallPhonePopwindow.dissmiss();
        }

        myCallPhonePopwindow = new MyPopAbstract(){
            @Override
            protected void setMapSettingViewWidget(View view) {
                TextView tv_pop_phone = (TextView) view.findViewById(R.id.tv_pop_phone);
                tv_pop_phone.setText(customerPhone);
                TextView cancle = (TextView) view.findViewById(R.id.tv_cancle); //取消
                TextView confirm = (TextView) view.findViewById(R.id.tv_call);//呼叫
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + customerPhone));
                        startActivity(intent);
                        myCallPhonePopwindow.dissmiss();
                    }
                });
                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        myCallPhonePopwindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.pop_message_all_delete2;
            }
        }.popWindowTouch(HelpCenterA.this).showAtLocation(ivBack, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

    }
}

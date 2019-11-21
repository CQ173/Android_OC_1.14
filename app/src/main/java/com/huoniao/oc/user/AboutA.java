package com.huoniao.oc.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.VersonCodeUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AboutA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_version_contrast)
    TextView tvVersionContrast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        tvTitle.setText("关于");

        /*提示：
        1、已是最新版本
        2、可更新至O计 O Count v1.6*/
        if(MyApplication.serviceCode!=null&&MyApplication.serviceVersionName!=null) {
            if (Integer.parseInt(MyApplication.serviceCode) > VersonCodeUtils.getVersionCode(AboutA.this)) {
                tvVersionContrast.setText("可更新至"+MyApplication.serviceVersionName);
            }else{
                tvVersionContrast.setText("已是最新版本"); //需要获取服务器版本号
            }
        }else{
            tvVersionContrast.setText("已是最新版本");
        }

    }

    @OnClick({R.id.iv_back, R.id.tv_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
}

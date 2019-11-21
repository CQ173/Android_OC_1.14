package com.huoniao.oc.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AccountLogOffSuccessA extends BaseActivity {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.tv_submitState)
    TextView tvSubmitState;
    @InjectView(R.id.tv_promptContent)
    TextView tvPromptContent;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;

    private Intent intent;
    private String operationTag;//操作标识
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log_off_success);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        operationTag = intent.getStringExtra("operationTag");
        if ("logout".equals(operationTag)){
            tvPromptContent.setText("      您已成功提交账户注销申请，系统将对您的资料进行审核，请耐心等候。");
        }
    }

    @OnClick({R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                MyApplication.getInstence().activityFinish();
                break;
        }
    }
}

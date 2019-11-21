package com.huoniao.oc.outlets;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LearnMoreA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_withholding_introduce)
    LinearLayout llWithholdingIntroduce;
    @InjectView(R.id.ll_withholding_rule)
    LinearLayout llWithholdingRule;
    @InjectView(R.id.ll_withholding_agreement)
    LinearLayout llWithholdingAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);
        ButterKnife.inject(this);
        initWidget();
    }

    private void initWidget() {
        tvTitle.setText("O计汇缴代扣");
    }

    @OnClick({R.id.iv_back, R.id.ll_withholding_introduce, R.id.ll_withholding_rule, R.id.ll_withholding_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:  //返回键
                finish();
                break;
            case R.id.ll_withholding_introduce: //汇缴代扣介绍
                break;
            case R.id.ll_withholding_rule: //汇缴代扣扣款规则
                break;
            case R.id.ll_withholding_agreement: //汇缴代扣协议
                break;
        }
    }
}

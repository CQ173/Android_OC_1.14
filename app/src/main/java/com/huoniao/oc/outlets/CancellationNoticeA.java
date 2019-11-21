package com.huoniao.oc.outlets;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 对公账户代扣解约提醒
 */

public class CancellationNoticeA extends BaseActivity{

    @InjectView(R.id.iv_back)
    ImageView ivBack;   //返回
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_add_card)
    TextView tvAddCard;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.tv_comfirm)
     TextView tvComfirm; //确定

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_notice);
        ButterKnife.inject(this);
        tvTitle.setText("对公账户代扣解约");
    }

    @OnClick({R.id.iv_back, R.id.tv_comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();

            case R.id.tv_comfirm:
                 finish();
                break;
        }
    }
}

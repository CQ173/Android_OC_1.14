package com.huoniao.oc.outlets;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/13.
 */

public class AlreadyOpWhListA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_learnMore)
    TextView tvLearnMore;
    @InjectView(R.id.mListView)
    ListView mListView;
    @InjectView(R.id.tv_addBankCardSign)
    TextView tvAddBankCardSign;
    @InjectView(R.id.bt_closeWithhold)
    Button btCloseWithhold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alreadyopened_withholdlist);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_learnMore, R.id.tv_addBankCardSign, R.id.bt_closeWithhold})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_learnMore:
                break;
            case R.id.tv_addBankCardSign:
                break;
            case R.id.bt_closeWithhold:
                break;
        }
    }
}

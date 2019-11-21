package com.huoniao.oc.outlets;

import android.os.Bundle;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;

/**
 * 扫码成功后 付款界面
 */
public class Payment extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }
}

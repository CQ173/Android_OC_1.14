package com.huoniao.oc.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;
import com.huoniao.oc.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PasswordManagement extends BaseActivity {

    @InjectView(R.id.ll_update_longin_password)
    LinearLayout llUpdateLonginPassword;
    @InjectView(R.id.ll_update_pay_pwd)
    LinearLayout llUpdatePayPwd;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_management);
        ButterKnife.inject(this);
        initWidget();
    }

    private void initWidget() {
        //代售点进来才有修改支付密码模块
        if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
            llUpdatePayPwd.setVisibility(View.VISIBLE);
        } else {
            llUpdatePayPwd.setVisibility(View.GONE);
        }

        tvTitle.setText("密码管理");

    }

    @OnClick({R.id.ll_update_longin_password, R.id.ll_update_pay_pwd,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_update_longin_password:
                updatePwd();
                break;
            case R.id.ll_update_pay_pwd:
                getPhoneAndsetPayPwd();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //修改登录密码
    public void updatePwd() {

        Intent intent = new Intent(PasswordManagement.this, UpdataPasswordA.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private void getPhoneAndsetPayPwd() {
        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();

        String url = Define.URL + "user/refreshMobile";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(PasswordManagement.this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(PasswordManagement.this,R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                try {
                    String mobile = json.getString("mobile");
                    Intent intent;
                    if (MyApplication.payPasswordIsEmpty) {  //true表示支付密码为空 没有设置
                        intent = new Intent(PasswordManagement.this, SettingPayPwd.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                    } else {
                        intent = new Intent(PasswordManagement.this, UpdatePayPwd.class);
                        intent.putExtra("mobile", mobile);

                    }
                    startActivityIntent(intent);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            protected void PdDismiss() {
                cpd.dismiss();
            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
        }, "tagPhone", true);

        volleyNetCommon.addQueue(jsonObjectRequest);
    }

}

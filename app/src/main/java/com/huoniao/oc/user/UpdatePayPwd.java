package com.huoniao.oc.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.CountDownTimerUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.ll_old_pay_pwd;

public class UpdatePayPwd extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;  //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.tv_add_card)
    TextView tvAddCard; //
    @InjectView(R.id.tv_save)
    TextView tvSave;  //因为公用  这个是忘记密码
    @InjectView(R.id.et_idNum)
    EditText etIdNum;    //身份证
    @InjectView(R.id.ll_idNum)
    LinearLayout llIdNum; //身份证容器
    @InjectView(R.id.et_old_pay_pwd)
    EditText etOldPayPwd;      //旧支付密码
    @InjectView(ll_old_pay_pwd)
    LinearLayout llOldPayPwd;   //旧支付密码容器
    @InjectView(R.id.et_new_paypwd)
    EditText etNewPaypwd;  //新支付密码
    @InjectView(R.id.et_confirm_new_paypwd)
    EditText etConfirmNewPaypwd;  //确认新支付密码
    @InjectView(R.id.tv_phone)
    TextView tvPhone;  //手机号码
    @InjectView(R.id.et_verification_code)
    EditText etVerificationCode;  //验证码
    @InjectView(R.id.tv_send_code)
    TextView tvSendCode;  //发送验证码
    @InjectView(R.id.tv_update_confirm)
    TextView tvUpdateConfirm;  //确认修改
    @InjectView(R.id.activity_setting_pay_pwd)
    LinearLayout activitySettingPayPwd;
    private CountDownTimerUtils mCountDownTimerUtils;
    private User user;
    private String mobile;
    private VolleyNetCommon volleyNetCommon;
    private String etoldPwd;
    private String newPwd;
    private String confirmNewPwd;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pay_pwd);
        ButterKnife.inject(this);
        initData();
        initWidget();
    }

    private void initWidget() {
        volleyNetCommon = new VolleyNetCommon();
        tvTitle.setText("修改支付密码");
        tvSave.setText("忘记密码");
        tvSave.setVisibility(View.VISIBLE);
        llOldPayPwd.setVisibility(View.VISIBLE);
        tvPhone.setText(mobile);
    }

    private void initData() {
        user = (User) ObjectSaveUtil.readObject(UpdatePayPwd.this, "loginResult");
        mobile = getIntent().getStringExtra("mobile");//手机号码
    }

    @OnClick({R.id.iv_back, R.id.tv_save, R.id.tv_send_code, R.id.tv_update_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                //忘记密码
                Intent intent = new Intent(this,ForgotPayPwd.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_send_code:
                String url = Define.URL+"user/getVerifyCode";
                startCode(url,1,"");     //发送验证码
                break;
            case R.id.tv_update_confirm:
                verificationCode(); //验证并且修改用户密码
                //确认修改
                break;
        }
    }

    private void verificationCode() {
      
        if(etOldPayPwd.getText().toString().trim().isEmpty()){
               //checkIdCard
               Toast.makeText(this, "请输入旧支付密码！", Toast.LENGTH_SHORT).show();
               return;
           }
        if(etNewPaypwd.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "请输入新支付密码！", Toast.LENGTH_SHORT).show();
           return;
        }
        if(etConfirmNewPaypwd.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "请输入确认新支付密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etVerificationCode.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
        return;
        }
        etoldPwd = etOldPayPwd.getText().toString().trim();
        newPwd = etNewPaypwd.getText().toString().trim();
        confirmNewPwd = etConfirmNewPaypwd.getText().toString().trim();
        verificationCode = etVerificationCode.getText().toString().trim();
        if(etoldPwd.length()<6){
            Toast.makeText(this, "旧密码不能小于6位！", Toast.LENGTH_SHORT).show();
             return;
        }else if(newPwd.length()<6){
             Toast.makeText(this, "新支付密码不能小于6位！", Toast.LENGTH_SHORT).show();
             return;
        }else if(!newPwd.equals(confirmNewPwd)){
             Toast.makeText(this, "新支付密码两次输入不正确，请重新输入！", Toast.LENGTH_SHORT).show();
             return;
        }

        String url = Define.URL+"user/validateVerifyCode";
        startCode(url,2, verificationCode);     //发送验证码

    }

    private void startCode(String url, final int tag,String verifyCode) {
        cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            if(tag ==1) {
                jsonObject.put("mobile", mobile);
                jsonObject.put("type", "7");
            }else if(tag == 2){
                jsonObject.put("mobile", mobile);
                jsonObject.put("type", "7");
                jsonObject.put("verifyCode",verifyCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequestUpdatePayPwd = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(UpdatePayPwd.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                if(tag ==1) {
                    Toast.makeText(UpdatePayPwd.this, "获取验证码成功！", Toast.LENGTH_SHORT).show();
                    //获取验证码成功后！进行倒计时
                    mCountDownTimerUtils = new CountDownTimerUtils(tvSendCode, 60000, 1000);
                    mCountDownTimerUtils.start();
                }else if(tag == 2){
                      //验证通过 开始 修改密码
                       setUpdatePwd();
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
        }, "UpdatePayPwd", true);

        volleyNetCommon.addQueue(jsonObjectRequestUpdatePayPwd);
    }


    private void setUpdatePwd() {
        cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type","1");
            jsonObject.put("password",newPwd);
            jsonObject.put("oldPassword",etoldPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url =Define.URL+"user/modifyPayPasswd";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {

            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Toast.makeText(UpdatePayPwd.this, "支付密码修改成功！", Toast.LENGTH_SHORT).show();
                finish();
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
        }, "modifyPayPasswd", true);

        volleyNetCommon.addQueue(jsonObjectRequest);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(volleyNetCommon !=null){
            volleyNetCommon.getRequestQueue().cancelAll("UpdatePayPwd");
            volleyNetCommon.getRequestQueue().cancelAll("modifyPayPasswd");
        }
    }
}

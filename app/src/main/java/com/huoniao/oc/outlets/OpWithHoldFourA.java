package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.CountDownTimerUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/10/12.
 */

public class OpWithHoldFourA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @InjectView(R.id.et_verCode)
    EditText etVerCode;
    @InjectView(R.id.bt_next)
    Button btNext;
    @InjectView(R.id.tv_getVericode)
    TextView tvGetVericode;
    private String reqSn;//申请接口的请求交易号
    private String userCardId;//银行卡id
    private Intent intent;
    private String verifyCode;
    private String phoneNumber;
    private String cardName;
    private CountDownTimerUtils mCountDownTimerUtils;
    private String signState;//用来判定是从签约点进来还是解约点进来
//    private String reqSn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openwithhold4);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        signState = intent.getStringExtra("signState");
        if ("surrender".equals(signState)){
            tvTitle.setText("银行卡代扣解约");
        }
        phoneNumber = intent.getStringExtra("phoneNumber");
        cardName = intent.getStringExtra("cardName");
        if (phoneNumber != null){
            StringBuffer buffer = new StringBuffer(phoneNumber);
            buffer.replace(3,7,"****");//手机号码的第四位到第七位用*好代替
            String newPhoneNumber = buffer.toString();
            tvPhoneNumber.setText(newPhoneNumber);
        }else {
            tvPhoneNumber.setText("");
        }
        userCardId = intent.getStringExtra("userCardId");
        reqSn = intent.getStringExtra("reqSn");
        mCountDownTimerUtils = new CountDownTimerUtils(tvGetVericode, 60000, 1000);
        mCountDownTimerUtils.start();
    }

    @OnClick({R.id.iv_back, R.id.tv_getVericode, R.id.bt_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_getVericode:
//                if ("surrender".equals(signState)){//如果解约进来就重新调解约申请
//                    requestTerminationApply();
//                }else {//否则签约进来就调短信重发
                    requestRepeatSMS();
//                }
                break;
            case R.id.bt_next:
                verifyCode = etVerCode.getText().toString();
                if (verifyCode == null || verifyCode.isEmpty()) {
                    ToastUtils.showToast(OpWithHoldFourA.this, "请输入验证码！");
                    return;
                }

                if (verifyCode.length() < 6) {
                    ToastUtils.showToast(OpWithHoldFourA.this, "请输入正确的6位验证码！");
                    return;
                }

                if ("surrender".equals(signState)){
                    requestTerminationComfirm();//解约进来调解约的接口
                }else {
                    requestSignedComfirm();//默认调签约接口
                }

                break;
        }
    }

    /**
     * 银联签约确认
     */
    private void requestSignedComfirm() {
        String url = Define.URL + "acct/signedComfirm";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userCardId", userCardId);
            jsonObject.put("protocolReqSn", reqSn);
            jsonObject.put("verifyCode", verifyCode);
            jsonObject.put("mobile",phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestNet(url, jsonObject, "signedComfirmRequest", "", true, true);
    }

    /**
     * 银联解约确认
     */
    private void requestTerminationComfirm() {
        String url = Define.URL + "acct/terminationComfirm";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("deductionId", userCardId);
            jsonObject.put("protocolReqSn", reqSn);
            jsonObject.put("verifyCode", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestNet(url, jsonObject, "terminationComfirmRequest", "", true, true);
    }

    /**
     * 重新发短信获取短信验证码
     */
    private void requestRepeatSMS() {
        String url = Define.URL + "acct/repeatSMS";
        JSONObject jsonObject = new JSONObject();
        try {
            if ("中信银行".equals(cardName)){
                jsonObject.put("cardId" , userCardId);
                jsonObject.put("mobile" , phoneNumber);
                jsonObject.put("type" , "surrender".equals(signState)? "1":"0");
            }else {
                jsonObject.put("protocolReqSn", reqSn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "repeatSMSRequest", "", true, true);
    }

    /**
     *  银联解约申请
     *
     */
    private void requestTerminationApply(){
        String url = Define.URL+"acct/terminationApply";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deductionId", userCardId);
            jsonObject.put("mobile", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "reTerminationApplyRequest", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "signedComfirmRequest":
                dataAnalysis(json);
//                try {
//                    JSONObject jsonObject = json.getJSONObject("data");
//                    String result = jsonObject.getString("result");
//                    if ("success".equals(result)) {
//                        intent = new Intent(OpWithHoldFourA.this, OpWithHoldFiveA.class);
//                        intent.putExtra("signState", signState);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                    } else {
//                        String msg = jsonObject.optString("msg");
//                        ToastUtils.showLongToast(OpWithHoldFourA.this, msg);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;

            case "terminationComfirmRequest":

                dataAnalysis(json);

                    break;

            case "repeatSMSRequest":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String result = jsonObject.getString("result");
                    if ("success".equals(result)) {
                        if ("中信银行".equals(cardName)) {
                            //判断是否是中信
                            reqSn = jsonObject.optString("reqSn");
                        }
                        mCountDownTimerUtils.start();
                    } else {
                        String msg = jsonObject.optString("msg");
                        ToastUtils.showLongToast(OpWithHoldFourA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

//            case "reTerminationApplyRequest":
//                jieYueApplyDataAnalysis(json);
//                break;
        }
    }

    /**
     * 数据解析
     * @param object 数据源
     */
    private void dataAnalysis(JSONObject object){
        try {
            JSONObject jsonObject = object.getJSONObject("data");
            String result = jsonObject.getString("result");
            if ("success".equals(result)) {
                intent = new Intent(OpWithHoldFourA.this, OpWithHoldFiveA.class);
                intent.putExtra("signState", signState);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            } else {
                String msg = jsonObject.optString("msg");
                ToastUtils.showLongToast(OpWithHoldFourA.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 数据解析
     * @param object 数据源
     */
    private void jieYueApplyDataAnalysis(JSONObject object){
        try {
            JSONObject jsonObject = object.getJSONObject("data");
            String result = jsonObject.getString("result");
            if ("success".equals(result)){
                reqSn = jsonObject.optString("reqSn");

                if ("surrender".equals(signState)){
                    userCardId = jsonObject.optString("deductionId");//解约进来解析解约的卡id
                }else {
                    userCardId = jsonObject.optString("userCardId");
                }

                mCountDownTimerUtils.start();//解约申请成功后发送验证码，倒计时

            }else {
                String msg = jsonObject.optString("msg");
                ToastUtils.showLongToast(OpWithHoldFourA.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

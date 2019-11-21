package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RegexUtils;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_bankCardInfo;

/**
 * Created by Administrator on 2017/10/12.
 */

public class OpWithHoldThreeA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(tv_bankCardInfo)
    TextView tvBankCardInfo;
    @InjectView(R.id.et_phoneNumber)
    EditText etPhoneNumber;
    @InjectView(R.id.bt_previousStep)
    Button btPreviousStep;
    @InjectView(R.id.bt_nextStep)
    Button btNextStep;
    @InjectView(R.id.ll_operationButton)
    LinearLayout llOperationButton;
    private Intent intent;
    private String cardId = "";;//银行卡id
    private String cardName = "";;//银行卡名称
    private String cardNumber = "";;//银行卡卡号
    private String cardType = "";;//银行卡类型
    private String cardNumberLast = "";//银行卡尾号
    private String phoneNumber = "";//手机号
    private String signState;//用来判定是从签约点进来还是解约点进来
//    private String terminationCardId;//解约时传过来的银行卡id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openwithhold3);
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
        cardId = intent.getStringExtra("cardId");
        cardName = intent.getStringExtra("cardName");
        cardNumber = intent.getStringExtra("cardNumber");
        cardType = intent.getStringExtra("cardType");
        if ("1".equals(cardType)){
            cardType = "储蓄卡";
        }else if("2".equals(cardType)){
            cardType = "信用卡";
        }

         if (cardNumber != null && !cardNumber.isEmpty() && cardNumber.length() > 4) {
             cardNumberLast = cardNumber.substring(cardNumber.length()-4, cardNumber.length());
        }
        tvBankCardInfo.setText(cardName + " " + cardType + " " + "（" + cardNumberLast + "）");
    }

    @OnClick({R.id.iv_back, R.id.bt_previousStep, R.id.bt_nextStep})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_previousStep:
                finish();
                break;
            case R.id.bt_nextStep:
                phoneNumber = etPhoneNumber.getText().toString();
                if (phoneNumber == null || "".equals(phoneNumber)) {
                    ToastUtils.showLongToast(OpWithHoldThreeA.this, "请输入的手机号码!");
                    return;
                }else if (RegexUtils.isMobileNO(phoneNumber) == false) {
                    ToastUtils.showLongToast(OpWithHoldThreeA.this, "请输入正确的手机号码!");
                    return;
                }
                if ("surrender".equals(signState)){
                    requestTerminationApply();//解约进来调解约的接口
                }else {
                    requestSignedApply();//默认调签约接口
                }

                break;
        }
    }

    /**
     *  银联签约申请请求
     *
     */
    private void requestSignedApply(){
        String url = Define.URL+"acct/signedApply";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userCardId", cardId);
            jsonObject.put("mobile", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "signedApplyRequest", "", true, true);
    }

    /**
     *  银联解约申请请求
     *
     */
    private void requestTerminationApply(){
        String url = Define.URL+"acct/terminationApply";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deductionId", cardId);
            jsonObject.put("mobile", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "terminationApplyRequest", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "signedApplyRequest":
                dataAnalysis(json);
                /*try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String result = jsonObject.getString("result");
                    if ("success".equals(result)){
                        String reqSn = jsonObject.optString("reqSn");
                        String userCardId = jsonObject.optString("userCardId");

                        intent = new Intent(OpWithHoldThreeA.this, OpWithHoldFourA.class);
                        intent.putExtra("reqSn", reqSn);
                        intent.putExtra("userCardId", userCardId);
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }else {
                        String msg = jsonObject.optString("msg");
                        ToastUtils.showLongToast(OpWithHoldThreeA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                break;

            case "terminationApplyRequest":
                dataAnalysis(json);
                /*try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String result = jsonObject.getString("result");
                    if ("success".equals(result)){
                        String reqSn = jsonObject.optString("reqSn");
                        String userCardId = jsonObject.optString("userCardId");

                        intent = new Intent(OpWithHoldThreeA.this, OpWithHoldFourA.class);
                        intent.putExtra("reqSn", reqSn);
                        intent.putExtra("userCardId", userCardId);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("signState", signState);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }else {
                        String msg = jsonObject.optString("msg");
                        ToastUtils.showLongToast(OpWithHoldThreeA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                break;
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
            if ("success".equals(result)){
                String reqSn = jsonObject.optString("reqSn");
                String userCardId = "";
                if ("surrender".equals(signState)){
                    userCardId = jsonObject.optString("deductionId");//解约进来解析解约的卡id
                }else {
                    userCardId = jsonObject.optString("userCardId");
                }


                intent = new Intent(OpWithHoldThreeA.this, OpWithHoldFourA.class);
                intent.putExtra("reqSn", reqSn);
                intent.putExtra("userCardId", userCardId);
                intent.putExtra("cardName" ,cardName);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("signState", signState);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }else {
                String msg = jsonObject.optString("msg");
                ToastUtils.showLongToast(OpWithHoldThreeA.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.ConfigureBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.HelpCenterA;
import com.huoniao.oc.useragreement.RegisterAgreeA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

/**
 * Created by Administrator on 2017/10/12.
 */

public class OpWithHoldOneA extends BaseActivity {
    @InjectView(R.id.tv_titles)
    TextView tv_titles;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_learnMore)
    TextView tvLearnMore;
    @InjectView(R.id.tv_orgName)
    TextView tvOrgName;
    @InjectView(R.id.tv_debitAccount)
    TextView tvDebitAccount;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_debitTime)
    TextView tvDebitTime;
    @InjectView(R.id.tv_debitMoney)
    TextView tvDebitMoney;
    @InjectView(R.id.tv_debitMode)
    TextView tvDebitMode;
    @InjectView(R.id.tv_takeEffectTime)
    TextView tvTakeEffectTime;
    @InjectView(R.id.tv_payWithHoldAgree)
    TextView tvPayWithHoldAgree;
    @InjectView(R.id.ll_cncb)
    LinearLayout llCNCB;//中信协议
    @InjectView(R.id.ll_agreement)
    LinearLayout llAgreement;//其他银行协议
    @InjectView(R.id.bt_cancel)
    Button btCancel;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    @InjectView(R.id.ll_operationButton)
    LinearLayout llOperationButton;

    private User user;
    private String orgName, debitAccount, name;
    private Intent intent;
    private String cardState;//签约状态，根据这个进行不同状态的页面展示以及逻辑
    List<ConfigureBean.ListEntity> list;//配置集合
    private String firstDeductionTime;
    private String secondDeductionTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openwithhold1);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initView() {
        tvPayWithHoldAgree.setText(Define.CNCB.equals(getIntent().getStringExtra("bankCode")) ? "《中信委托扣款服务三方协议》" : "《委托扣款服务三方协议》");
        switch (getIntent().getStringExtra("cardType")) {
            case "1"://1 没有中信
                llAgreement.setVisibility(View.VISIBLE);
                llCNCB.setVisibility(View.GONE);
                break;
            case "2":// 2 只有中信
                llAgreement.setVisibility(View.GONE);
                llCNCB.setVisibility(View.VISIBLE);
                break;
            case "3":// 3 都有
                llCNCB.setVisibility(View.GONE);
                break;
        }
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);

        if (list != null) {
            list.clear();
        }
        list = (List<ConfigureBean.ListEntity>) ObjectSaveUtil.readObject(OpWithHoldOneA.this, "ListEntity"); //配置集合
        if (list != null) {
            for (ConfigureBean.ListEntity entity : list) {

                if (entity.getFirstDeductionTime() != null) {
                    firstDeductionTime = entity.getFirstDeductionTime();

                }
                if (entity.getSecondDeductionTime() != null) {
                    secondDeductionTime = entity.getSecondDeductionTime();

                }

            }
        }
        tvDebitTime.setText("每天" + firstDeductionTime + ",  " + secondDeductionTime);

        Object loginResult = readObject(this, "loginResult");
        intent = getIntent();
        String type = intent.getStringExtra("OpTwoA");
        if ("OpTwoA".equals(type)){
            tv_titles.setText("银行卡代扣签约");
        }
        cardState = intent.getStringExtra("cardState");
        user = (User) loginResult;
        orgName = user.getOrgName();
        debitAccount = user.getLoginName();
        name = user.getName();
        if (orgName != null) {
            tvOrgName.setText(orgName);
        } else {
            tvOrgName.setText("");
        }

        if (debitAccount != null) {
            tvDebitAccount.setText(debitAccount);
        } else {
            tvDebitAccount.setText("");
        }

        if (name != null) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }
    }

    /**
     * 银联签约确认
     */
    private void requestSignedComfirm(String cardId) {
        String url = Define.URL + "acct/signedComfirm";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userCardId", cardId);
//            jsonObject.put("protocolReqSn", reqSn);
//            jsonObject.put("verifyCode", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "signedComfirmRequest", "", true, true);
    }

    @OnClick({R.id.tv_cncb_agreement,R.id.iv_back, R.id.tv_learnMore, R.id.tv_takeEffectTime, R.id.bt_cancel, R.id.bt_confirm, R.id.tv_payWithHoldAgree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cncb_agreement:
                intent = new Intent(OpWithHoldOneA.this, RegisterAgreeA.class);
                intent.putExtra("url", Define.CNCB_PAYMENT_AGREEMENT);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_learnMore:
                //startActivityMethod(HelpCenterA.class);
                intent = new Intent(OpWithHoldOneA.this, HelpCenterA.class);
                intent.putExtra("learn_more", "learn_more");
                startActivityIntent(intent);
                break;
            case R.id.tv_takeEffectTime:
                break;
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_confirm:
                if ("closeState".equals(cardState)) {
                    afreshOpenWithHold("1");//确认重新开通
                } else {
//                    if (Define.CNCB.equals(getIntent().getStringExtra("bankCode"))) {//中信银行直接签约
//                        requestSignedComfirm(getIntent().getStringExtra("cardId"));
//                    } else {//其他银行要手机号
                    intent = new Intent(OpWithHoldOneA.this, OpWithHoldThreeA.class);
                    intent.putExtra("cardId", getIntent().getStringExtra("cardId"));
                    intent.putExtra("cardName", getIntent().getStringExtra("cardName"));
                    intent.putExtra("cardNumber", getIntent().getStringExtra("cardNumber"));
                    intent.putExtra("cardType", getIntent().getStringExtra("cardType"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                    }
                }

                break;

            case R.id.tv_payWithHoldAgree:
                intent = new Intent(OpWithHoldOneA.this, RegisterAgreeA.class);
                intent.putExtra("url", Define.CNCB.equals(getIntent().getStringExtra("bankCode")) ? Define.CNCB_PAYMENT_AGREEMENT : Define.PAY_WITHHOLDAGREE);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                break;
        }
    }

    /**
     * @param operation 做什么操作(开通或关闭)
     */
    private void afreshOpenWithHold(String operation) {
        String url = Define.URL + "acct/switchDeductions";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isDeductions", operation);//1 开通 2关闭
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "resetOpenWithHold", "", true, true);
    }

    /**
     * 数据解析
     *
     * @param object 数据源
     */
    private void dataAnalysis(JSONObject object) {
        try {
            JSONObject jsonObject = object.getJSONObject("data");
            String result = jsonObject.getString("result");
            if ("success".equals(result)) {
                intent = new Intent(OpWithHoldOneA.this, OpWithHoldFiveA.class);
//                intent.putExtra("signState", signState);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            } else {
                String msg = jsonObject.optString("msg");
                ToastUtils.showLongToast(OpWithHoldOneA.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "signedComfirmRequest":
                dataAnalysis(json);
                break;
            case "resetOpenWithHold":
                MyApplication.getInstence().activityFinish();
                intent = new Intent(OpWithHoldOneA.this, HasOpenedConsolidatedWithholdingManagementA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }
}

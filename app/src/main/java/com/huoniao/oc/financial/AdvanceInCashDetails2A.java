package com.huoniao.oc.financial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.PaymentPayListBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class AdvanceInCashDetails2A extends BaseActivity {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_stationName)
    TextView tvStationName;
    @InjectView(R.id.tv_paymentAmount)
    TextView tvPaymentAmount;
    @InjectView(R.id.tv_state)
    TextView tvState;
    @InjectView(R.id.tv_sourceOfFunds)
    TextView tvSourceOfFunds;
    @InjectView(R.id.tv_advanceCashCode)
    TextView tvAdvanceCashCode;
    @InjectView(R.id.tv_advanceCashAmount)
    TextView tvAdvanceCashAmount;
    @InjectView(R.id.tv_repaymentSerialNumber)
    TextView tvRepaymentSerialNumber;
    @InjectView(R.id.tv_5911repaymentAmount)
    TextView tv5911repaymentAmount;
    @InjectView(R.id.tv_supplementRepaymentAmount)
    TextView tvSupplementRepaymentAmount;
    @InjectView(R.id.tv_operator)
    TextView tvOperator;
    @InjectView(R.id.bt_left)
    Button btLeft;
    @InjectView(R.id.bt_right)
    Button btRight;


    private User user;
    private String roleName;
    private String id;
    private String paymentPaySumId;
    private String state;
    private String officeIds = "1";
    private String remark = "";
    private Intent intent;
    private String fbPayAmountString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_in_cash_details2);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        user = (User) readObject(AdvanceInCashDetails2A.this, "loginResult");
        roleName = user.getRoleName();
        PaymentPayListBean.DataBean dataBean = (PaymentPayListBean.DataBean) ObjectSaveUtil.readObject(AdvanceInCashDetails2A.this, "paymentPayList");
        id = dataBean.getId();
        paymentPaySumId = dataBean.getPaymentPaySumId();
        fbPayAmountString = dataBean.getFbPayAmountString();
        state = dataBean.getState();
        tvDate.setText(dataBean.getTime());
        tvStationName.setText(dataBean.getStationName());
        tvPaymentAmount.setText(dataBean.getPaymentAmountString() + "元");
        tvState.setText(dataBean.getStateName());
        tvSourceOfFunds.setText(dataBean.getSourceName());
        tvAdvanceCashCode.setText(dataBean.getSpotNo());
        tvAdvanceCashAmount.setText(dataBean.getSpotAmountString() + "元");
        tvRepaymentSerialNumber.setText(dataBean.getReturnNo());
        tv5911repaymentAmount.setText(dataBean.getMainReturnAmountString() + "元");
        tvSupplementRepaymentAmount.setText(dataBean.getFillReturnAmountString() + "元");
        tvOperator.setText("出纳：" + dataBean.getCashierName() + "/" + "会计：" + dataBean.getAccountantName());

        if (roleName.contains("出纳")){
            if (Define.PAYMENT_PAYSTATE_1.equals(state)) {
                btLeft.setVisibility(View.GONE);
                btRight.setVisibility(View.GONE);
            } else if (Define.PAYMENT_PAYSTATE_2.equals(state)) {
                btLeft.setVisibility(View.VISIBLE);
                btLeft.setText("借款");
                btRight.setVisibility(View.VISIBLE);
                btRight.setText("付款申请");
            } else if (Define.PAYMENT_PAYSTATE_3.equals(state)) {
                btLeft.setVisibility(View.VISIBLE);
                btLeft.setText("还款申请");
                btRight.setVisibility(View.GONE);
            } else if (Define.PAYMENT_PAYSTATE_5.equals(state)) {
                btLeft.setVisibility(View.GONE);
                btRight.setVisibility(View.GONE);
            } else if (Define.PAYMENT_PAYSTATE_6.equals(state)) {
                btLeft.setVisibility(View.VISIBLE);
                btLeft.setText("还款申请");
                btRight.setVisibility(View.GONE);
            } else if (Define.PAYMENT_PAYSTATE_8.equals(state)) {
                btLeft.setVisibility(View.GONE);
                btRight.setVisibility(View.GONE);
            }else {
                btLeft.setVisibility(View.GONE);
                btRight.setVisibility(View.GONE);
            }
        }
    }


    @OnClick({R.id.iv_back, R.id.bt_left, R.id.bt_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_left:
                if (RepeatClickUtils.repeatClick()) {
                    if (roleName.contains("出纳")) {
                        if (Define.PAYMENT_PAYSTATE_2.equals(state)) {
                            paymentPaySpot();
                        } else if (Define.PAYMENT_PAYSTATE_3.equals(state)) {
                            intent = new Intent(AdvanceInCashDetails2A.this, AepaymentApplyA.class);
                            intent.putExtra("performAction", "还款");
                            intent.putExtra("paymentPaySumId", paymentPaySumId);
                            intent.putExtra("state", state);
                            startActivityForResult(intent, 15);
                            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        } else if (Define.PAYMENT_PAYSTATE_6.equals(state)) {
                            intent = new Intent(AdvanceInCashDetails2A.this, AepaymentApplyA.class);
                            intent.putExtra("performAction", "还款");
                            intent.putExtra("paymentPaySumId", paymentPaySumId);
                            intent.putExtra("state", state);
                            startActivityForResult(intent, 15);
                            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        }
                    }
                }
                break;
            case R.id.bt_right:
                    if (roleName.contains("出纳") && Define.PAYMENT_PAYSTATE_2.equals(state)) {
                        intent = new Intent(AdvanceInCashDetails2A.this, AepaymentApplyA.class);
                        intent.putExtra("performAction", "付款");
                        intent.putExtra("paymentPayId", id);
                        intent.putExtra("fbPayAmountString", fbPayAmountString);
                        intent.putExtra("state", state);
                    }

                break;
        }
    }

    /**
     * 请求借款
     */
    private void paymentPaySpot() {
        String url = Define.URL + "acct/paymentPaySpot";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentPayId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentPaySpot", "0", true, false); //0 不代表什么
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "paymentPaySpot":
                handlerAuditing(json, tag);
                break;

//            case "paymentPayReturnAudit":
//                handlerAuditing(json, tag);
//                break;
//
//            case "paymentPayFbPayAudit":
//                handlerAuditing(json, tag);
//                break;
        }
    }

    private void handlerAuditing(JSONObject jsonObject, String tag) {
        try {
            JSONObject mJobj = jsonObject.getJSONObject("data");
            String result = mJobj.optString("result");
            if ("success".equals(result)) {
                setResult(15);
//                ToastUtils.showToast(AdvanceInCashDetails2A.this, "借款申请！");

                finish();
            } else {
                String msg = mJobj.optString("msg");
                ToastUtils.showToast(AdvanceInCashDetails2A.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

package com.huoniao.oc.financial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.ContainsEmojiEditText;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FinancialRefundsA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_money)
    TextView tvMoney;
    @InjectView(R.id.tv_applyType)
    TextView tvApplyType;
    @InjectView(R.id.tv_remarks)
    TextView tvRemarks;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_agentAccount)
    TextView tvAgentAccount;
    @InjectView(R.id.et_auditReason)
    ContainsEmojiEditText etAuditReason;
    @InjectView(R.id.bt_confirmRefunds)
    Button btConfirmRefunds;

    private String preventRepeatToken;
    private String financeApplyId;
    private String refundReason;

    private String agentAccount, name, money, applyTypeName,
            statusName, remarks;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_refunds);
        ButterKnife.inject(this);
        initData();

    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        financeApplyId = intent.getStringExtra("id");
        money = intent.getStringExtra("applyFee");
        if (money != null && !money.isEmpty()) {
            tvMoney.setText(money);
        } else {
            tvMoney.setText("");
        }

        applyTypeName = intent.getStringExtra("applyTypeName");
        if (applyTypeName != null && !applyTypeName.isEmpty()) {
            tvApplyType.setText(applyTypeName);

        } else {
            tvApplyType.setText("");
        }

        remarks = intent.getStringExtra("remark");
        if (remarks != null && !remarks.isEmpty()) {
            tvRemarks.setText(remarks);
        } else {
            tvRemarks.setText("");
        }

        statusName = intent.getStringExtra("stateName");
        if (statusName != null && !statusName.isEmpty()) {
            tvStatus.setText(statusName);

        } else {
            tvStatus.setText("");
        }

        name = intent.getStringExtra("agencyUserName");
        if (name != null && !name.isEmpty()) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        agentAccount = intent.getStringExtra("agencyLoginName");
        if (agentAccount != null && !agentAccount.isEmpty()) {
            tvAgentAccount.setText(agentAccount);
        } else {
            tvAgentAccount.setText("");
        }

    }

    @OnClick({R.id.iv_back, R.id.bt_confirmRefunds})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_confirmRefunds:
                refundReason = removeAllSpace(etAuditReason.getText().toString());
                if (refundReason == null || refundReason.isEmpty()) {
                    Toast.makeText(FinancialRefundsA.this, "请输入退款理由!", Toast.LENGTH_SHORT).show();
                    return;
                }
                showDialog("确认退款？");
                break;
        }
    }

    /**
     * 确认和拒绝操作提示弹窗
     *
     * @param msg 提示信息
     */
    private void showDialog(String msg) {
        new AlertDialog.Builder(FinancialRefundsA.this)
                .setTitle("提示！")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //根据不同请求弹出不同提示的弹窗

                        getToken();


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setCancelable(false)
                .show();

    }

    /**
     * 获取Token
     */
    private void getToken() {
        String url = Define.URL + "fb/preventRepeatSubmit";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "finRefundPreventRepeatSubmit", "0", false, false);
    }


    /**
     * 请求退款
     */
    private void requestFinanceApplyRefund() {
        String url = Define.URL + "acct/financeApplyRefund";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("financeApplyId", financeApplyId);
            jsonObject.put("reason", refundReason);
            jsonObject.put("preventRepeatToken", preventRepeatToken);  //防重复请求token
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "financeApplyRefund", "0", true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "finRefundPreventRepeatSubmit":

                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    preventRepeatToken = jsonObject.optString("preventRepeatToken");
                    requestFinanceApplyRefund();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case "financeApplyRefund":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String result = jsonObject.optString("result");
                    String msg = jsonObject.optString("msg");
                    if ("success".equals(result)){
                        ToastUtils.showToast(FinancialRefundsA.this, "退款成功！");
//                        setResult(10);
                        intent = new Intent(FinancialRefundsA.this, FinancialListA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        MyApplication.getInstence().activityFinish();
                    }else {
                        ToastUtils.showLongToast(FinancialRefundsA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    /**
     * 删除字符串空格
     *
     * @param str
     * @return
     */
    public String removeAllSpace(String str) {
        String tmpstr = str.replace(" ", "");
        return tmpstr;
    }
}

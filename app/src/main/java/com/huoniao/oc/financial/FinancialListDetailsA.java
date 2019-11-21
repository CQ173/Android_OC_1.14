package com.huoniao.oc.financial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.ContainsEmojiEditText;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

/**
 * Created by Administrator on 2017/8/21.
 */

public class FinancialListDetailsA extends BaseActivity {


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
    @InjectView(R.id.tv_accountName)
    TextView tvAccountName;
    @InjectView(R.id.tv_bankName)
    TextView tvBankName;
    @InjectView(R.id.tv_bankNumber)
    TextView tvBankNumber;
    @InjectView(R.id.layout_accountInfo)
    LinearLayout layoutAccountInfo;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_applyName)
    TextView tvApplyName;
    @InjectView(R.id.tv_organization)
    TextView tvOrganization;
    @InjectView(R.id.tv_tradeNumber)
    TextView tvTradeNumber;
    @InjectView(R.id.tv_receivablesAccount)
    TextView tvReceivablesAccount;
    @InjectView(R.id.tv_receivablesName)
    TextView tvReceivablesName;
    @InjectView(R.id.layout_payeeInfo)
    LinearLayout layoutPayeeInfo;
    @InjectView(R.id.tv_ashierOperator)
    TextView tvAshierOperator;
    @InjectView(R.id.rb_maxAmount)
    RadioButton rbMaxAmount;
    @InjectView(R.id.rb_minAmount)
    RadioButton rbMinAmount;
    @InjectView(R.id.ll_selectQuota)
    LinearLayout llSelectQuota;
    @InjectView(R.id.tv_reason)
    TextView tvReason;
    @InjectView(R.id.layout_yesRelieveArea)
    LinearLayout layoutYesRelieveArea;
    @InjectView(R.id.et_auditReason)
    ContainsEmojiEditText etAuditReason;
    @InjectView(R.id.layout_noRelieveArea)
    LinearLayout layoutNoRelieveArea;
    @InjectView(R.id.tv_prompt)
    TextView tvPrompt;
    @InjectView(R.id.bt_passed)
    Button btPassed;
    @InjectView(R.id.bt_noPassed)
    Button btNoPassed;
    @InjectView(R.id.ll_operationButton)
    LinearLayout llOperationButton;
    @InjectView(R.id.tv_accountOperator)
    TextView tvAccountOperator;
    @InjectView(R.id.tv_refund)
    TextView tvRefund;
    @InjectView(R.id.et_actualFee)
    EditText etActualFee;
    @InjectView(R.id.ll_actualFeeArea)
    LinearLayout llActualFeeArea;
    @InjectView(R.id.tv_moneyTitle)
    TextView tvMoneyTitle;
    @InjectView(R.id.textView16)
    TextView textView16;
    @InjectView(R.id.tv_officeParent)
    TextView tvOfficeParent;

    private Intent intent;
    private String date, organization, agentAccount, name, applyTypeName, applyType, tradeNumber,
            money, accountName, bankName, bankNumber, applyName, receivablesAccount,
            receivablesName, statusName, status, remarks, reason, ashierOperator,
            accountOperator, ZXswitch, officeParentName;
    User user;
    private String roleName;
    private String id;
    private String auditReason;
    private String selectQuotaStr = "";//额度选择
    private String requestTag;//用来标识调用哪个请求
    private String message;//用来根据不同请求传递不同提示
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String preventRepeatToken;

    private CustomProgressDialog myCpd;

    //    private String preventRepeatToken;
    private String actualFee = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_financialdetails);
        ButterKnife.inject(this);
        initData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        InputFilter[] filters = {new CashierInputFilter()};
        etActualFee.setFilters(filters);
        myCpd = new CustomProgressDialog(this, "正在处理中...", R.drawable.frame_anim);
        myCpd.setCancelable(false);//设置进度条不可以按退回键取消
        myCpd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
        user = (User) readObject(FinancialListDetailsA.this, "loginResult");
        roleName = user.getRoleName();
        intent = getIntent();
        id = intent.getStringExtra("id");

        ZXswitch = intent.getStringExtra("ZXswitch");

        date = intent.getStringExtra("createDate");
        if (date != null && !date.isEmpty()) {
            tvDate.setText(date);
        } else {
            tvDate.setText("");
        }

        organization = intent.getStringExtra("agencyUserOfficeName");
        if (organization != null && !organization.isEmpty()) {
            tvOrganization.setText(organization);
        } else {
            tvOrganization.setText("");
        }

        agentAccount = intent.getStringExtra("agencyLoginName");
        if (agentAccount != null && !agentAccount.isEmpty()) {
            tvAgentAccount.setText(agentAccount);
        } else {
            tvAgentAccount.setText("");
        }

        name = intent.getStringExtra("agencyUserName");
        if (name != null && !name.isEmpty()) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        applyTypeName = intent.getStringExtra("applyTypeName");
        if (applyTypeName != null && !applyTypeName.isEmpty()) {
            tvApplyType.setText(applyTypeName);
          /*  if ("汇缴付款".equals(applyTypeName) || "提现".equals(applyTypeName)) {
                layoutAccountInfo.setVisibility(View.VISIBLE);
            }
            if ("代付".equals(applyTypeName)) {
                layoutPayeeInfo.setVisibility(View.VISIBLE);
            }

            if (roleName.contains("出纳") && "汇缴付款".equals(applyTypeName)){
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
                btNoPassed.setVisibility(View.GONE);
            }*/

        } else {
            tvApplyType.setText("");
        }

        applyType = intent.getStringExtra("applyType");
        if (applyType != null && !applyType.isEmpty()) {
            if (Define.finApplyType1.equals(applyType) || Define.finApplyType2.equals(applyType)) {
                layoutAccountInfo.setVisibility(View.VISIBLE);
            }
            if (Define.finApplyType4.equals(applyType)) {
                layoutPayeeInfo.setVisibility(View.VISIBLE);
            }

            if (Define.finApplyType1.equals(applyType)) {
                llActualFeeArea.setVisibility(View.VISIBLE);
                tvMoneyTitle.setText("待付金额");
            } else {
                llActualFeeArea.setVisibility(View.GONE);
                tvMoneyTitle.setText("金额");
            }
        }

        tradeNumber = intent.getStringExtra("transferId");
        if (tradeNumber != null && !tradeNumber.isEmpty()) {
            tvTradeNumber.setText(tradeNumber);
        } else {
            tvTradeNumber.setText("");
        }

        money = intent.getStringExtra("applyFee");
        if (money != null && !money.isEmpty()) {
            tvMoney.setText(money);
        } else {
            tvMoney.setText("");
        }

        actualFee = intent.getStringExtra("actualFee");
        if (actualFee != null && !actualFee.isEmpty()) {
            etActualFee.setText(actualFee);
        } else {
            etActualFee.setText("");
        }

        accountName = intent.getStringExtra("accountName");
        if (accountName != null && !accountName.isEmpty()) {
            tvAccountName.setText(accountName);
        } else {
            tvAccountName.setText("");
        }

        bankName = intent.getStringExtra("openBankName");
        if (bankName != null && !bankName.isEmpty()) {
            tvBankName.setText(bankName);
        } else {
            tvBankName.setText("");
        }

        bankNumber = intent.getStringExtra("cardNo");
        if (bankNumber != null && !bankNumber.isEmpty()) {
            tvBankNumber.setText(bankNumber);
        } else {
            tvBankNumber.setText("");
        }

        applyName = intent.getStringExtra("applyUserName");
        if (applyName != null && !applyName.isEmpty()) {
            tvApplyName.setText(applyName);
        } else {
            tvApplyName.setText("");
        }

        receivablesAccount = intent.getStringExtra("receiptLoginName");
        if (receivablesAccount != null && !receivablesAccount.isEmpty()) {
            tvReceivablesAccount.setText(receivablesAccount);
        } else {
            tvReceivablesAccount.setText("");
        }

        receivablesName = intent.getStringExtra("receiptName");
        if (receivablesName != null && !receivablesName.isEmpty()) {
            tvReceivablesName.setText(receivablesName);
        } else {
            tvReceivablesName.setText("");
        }

        statusName = intent.getStringExtra("stateName");
        if (statusName != null && !statusName.isEmpty()) {
            tvStatus.setText(statusName);
           /* if (roleName.contains("出纳") && "待付款".equals(statusName)){
                layoutNoRelieveArea.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
                btPassed.setText("付款");
                btNoPassed.setText("拒绝");
            }


            if (roleName.contains("会计") && "待审核".equals(statusName)){
                layoutNoRelieveArea.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
                btPassed.setText("通过");
                btNoPassed.setText("不通过");
            }

            if (roleName.contains("会计") && "待确认".equals(statusName)){
                layoutNoRelieveArea.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
                btPassed.setText("确认");
                btNoPassed.setText("拒绝");
            }
            if ((roleName.contains("出纳") && "汇缴付款".equals(applyTypeName) && "付款失败".equals(statusName)) ||
                    (roleName.contains("出纳") && "汇缴付款".equals(applyTypeName) && "拒绝付款".equals(statusName))){
                llOperationButton.setVisibility(View.VISIBLE);
                btNoPassed.setVisibility(View.GONE);
                btPassed.setText("重新付款");
            }*/

        } else {
            tvStatus.setText("");
        }


        status = intent.getStringExtra("state");
        if (status != null && !status.isEmpty()) {
            if (roleName.contains("出纳") && Define.finApplyState4.equals(status)) {
                layoutNoRelieveArea.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
                btPassed.setText("付款");
                btNoPassed.setText("拒绝");
            }

            if (roleName.contains("出纳") && Define.finApplyType1.equals(applyType) && Define.finApplyState4.equals(status)) {
                layoutNoRelieveArea.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
//                btNoPassed.setVisibility(View.GONE);
                btPassed.setText("付款");
                btNoPassed.setText("拒绝");
                etActualFee.setEnabled(true);
            } else {
                etActualFee.setEnabled(false);
            }

            if ((roleName.contains("出纳") && Define.finApplyType1.equals(applyType) && Define.finApplyState6.equals(status)) ||
                    (roleName.contains("出纳") && Define.finApplyType1.equals(applyType) && Define.finApplyState9.equals(status))) {
                llOperationButton.setVisibility(View.VISIBLE);
                btNoPassed.setVisibility(View.GONE);
                btPassed.setText("重新付款");
            }

            if (roleName.contains("出纳") && Define.finApplyState12.equals(status)) {
                layoutNoRelieveArea.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
                btPassed.setText("通过");
                btNoPassed.setText("不通过");
            }
            if (roleName.contains("会计") && Define.finApplyState1.equals(status)) {
                layoutNoRelieveArea.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
                btPassed.setText("通过");
                btNoPassed.setText("不通过");
            }

            if (roleName.contains("会计") && Define.finApplyState7.equals(status)) {
                layoutNoRelieveArea.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.VISIBLE);
                layoutYesRelieveArea.setVisibility(View.GONE);
                if ("false".equals(ZXswitch)) {
                    tvPrompt.setVisibility(View.VISIBLE);
                } else {
                    tvPrompt.setVisibility(View.GONE);
                    llSelectQuota.setVisibility(View.VISIBLE);
                }
                btPassed.setText("确认");
                btNoPassed.setText("拒绝");
            }

            if (Define.finApplyType1.equals(applyType) && Define.finApplyState4.equals(status)) {
                etActualFee.setText(money);
            }
        }

        remarks = intent.getStringExtra("remark");
        if (remarks != null && !remarks.isEmpty()) {
            tvRemarks.setText(remarks);
        } else {
            tvRemarks.setText("");
        }

        reason = intent.getStringExtra("reason");
        if (reason != null && !reason.isEmpty()) {
            tvReason.setText(reason);
        } else {
            tvReason.setText("");
        }
        ashierOperator = intent.getStringExtra("cashierUserName");
        if (ashierOperator != null && !ashierOperator.isEmpty()) {
            tvAshierOperator.setText(ashierOperator);
        } else {
            tvAshierOperator.setText("");
        }

        accountOperator = intent.getStringExtra("auditUserName");
        if (accountOperator != null && !accountOperator.isEmpty()) {
            tvAccountOperator.setText(accountOperator);
        } else {
            tvAccountOperator.setText("");
        }


        officeParentName = intent.getStringExtra("officeParentName");
        if (officeParentName != null && !officeParentName.isEmpty()) {
            tvOfficeParent.setText(officeParentName);
        } else {
            tvOfficeParent.setText("");
        }

//        preventRepeatToken = SPUtils2.getString(FinancialListDetailsA.this, "preventRepeatToken");
        if (Define.finApplyState4.equals(status) || Define.finApplyState7.equals(status)
                || Define.finApplyState6.equals(status) || Define.finApplyState9.equals(status)) {
//            tvRefund.setVisibility(View.VISIBLE);
            if (applyType.equals(Define.finApplyType1)) {
                setPremissionShowHideView(Premission.ACCT_FINANCEAPPLY_REFUND, tvRefund);    // 是否有财务退款的权限
            }
        }
    }


    @OnClick({R.id.iv_back, R.id.bt_passed, R.id.bt_noPassed, R.id.tv_refund})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(10);
                finish();
                break;

            case R.id.bt_passed:
                auditReason = removeAllSpace(etAuditReason.getText().toString());
                actualFee = etActualFee.getText().toString();
                if (roleName.contains("出纳") && Define.finApplyState4.equals(status)) {
                    requestTag = "1";
                    message = "确认付款？";
                    showDialog(message);
//                    try {
//                        cashierConfirm("comfirm");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
                if (roleName.contains("会计") && Define.finApplyState7.equals(status)) {
                    requestTag = "2";
                    message = "确认通过？";
                    showDialog(message);
//                    try {
//                        accountantConfirm("comfirm");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

                if (roleName.contains("会计") && Define.finApplyState1.equals(status)) {
                    requestTag = "3";
                    message = "确认审核通过？";
                    showDialog(message);
//                    try {
//                        accountantAudit("comfirm");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

//                if (roleName.contains("出纳") && Define.finApplyType1.equals(applyType) && Define.finApplyState4.equals(status)) {
//                    requestTag = "4";
//                    message = "确认付款？";
//                    showDialog(message);
//
//                }

                if ((roleName.contains("出纳") && Define.finApplyType1.equals(applyType) && Define.finApplyState6.equals(status)) ||
                        (roleName.contains("出纳") && Define.finApplyType1.equals(applyType) && Define.finApplyState9.equals(status))) {
                    requestTag = "5";
                    message = "确认重新付款？";
                    showDialog(message);
//                    try {
//                        paySysAgain();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

                if (roleName.contains("出纳") && Define.finApplyState12.equals(status)) {
                    requestTag = "9";
                    message = "确认审核通过？";
                    showDialog(message);

                }

                break;
            case R.id.bt_noPassed:
                auditReason = removeAllSpace(etAuditReason.getText().toString());
                if (auditReason == null || auditReason.isEmpty()) {
                    Toast.makeText(FinancialListDetailsA.this, "请输入审核理由!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (roleName.contains("出纳") && Define.finApplyState4.equals(status)) {
                    requestTag = "6";
                    message = "确认拒绝付款？";
                    showDialog(message);
//                    try {
//                        cashierConfirm("refuse");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

                if (roleName.contains("会计") && Define.finApplyState7.equals(status)) {
                    requestTag = "7";
                    message = "确认不通过？";
                    showDialog(message);
//                    try {
//                        accountantConfirm("refuse");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

                if (roleName.contains("会计") && Define.finApplyState1.equals(status)) {
                    requestTag = "8";
                    message = "确认审核不通过？";
                    showDialog(message);
//                    try {
//                        accountantAudit("refuse");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

                if (roleName.contains("出纳") && Define.finApplyState12.equals(status)) {
                    requestTag = "10";
                    message = "确认审核不通过？";
                    showDialog(message);

                }

                break;
            case R.id.tv_refund:
                /*auditReason = removeAllSpace(etAuditReason.getText().toString());
                if (auditReason == null || auditReason.isEmpty()) {
                    Toast.makeText(FinancialListDetailsA.this, "请输入审核理由!", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestTag = "11";
                message = "确认退款？";
                showDialog(message);*/
                intent = new Intent(FinancialListDetailsA.this, FinancialRefundsA.class);
                intent.putExtra("id", id);
                intent.putExtra("applyFee", money);
                intent.putExtra("applyTypeName", applyTypeName);
                intent.putExtra("remark", remarks);
                intent.putExtra("stateName", statusName);
                intent.putExtra("agencyUserName", name);
                intent.putExtra("agencyLoginName", agentAccount);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(10);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 确认和拒绝操作提示弹窗
     *
     * @param msg 提示信息
     */
    private void showDialog(String msg) {
        new AlertDialog.Builder(FinancialListDetailsA.this)
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

        requestNet(url, jsonObject, "cashierAccountPreventRepeatSubmit", "0", true, false);
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "cashierAccountPreventRepeatSubmit":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    preventRepeatToken = jsonObject.getString("preventRepeatToken");
                    if (preventRepeatToken != null) {
                        SPUtils2.putString(FinancialListDetailsA.this, "preventRepeatToken", preventRepeatToken);
                        if ("1".equals(requestTag)) {
                            try {
                                cashierConfirm("comfirm");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("2".equals(requestTag)) {
                            try {
                                accountantConfirm("comfirm");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("3".equals(requestTag)) {
                            try {
                                accountantAudit("comfirm");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("5".equals(requestTag)) {
                            try {
                                paySysAgain();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("6".equals(requestTag)) {
                            try {
                                cashierConfirm("refuse");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("7".equals(requestTag)) {
                            try {
                                accountantConfirm("refuse");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("8".equals(requestTag)) {
                            try {
                                accountantAudit("refuse");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("9".equals(requestTag)) {
                            try {
                                accountantAudit("comfirm");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if ("10".equals(requestTag)) {
                            try {
                                accountantAudit("refuse");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

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


    /**
     * 出纳确认
     *
     * @throws Exception
     */
    private void cashierConfirm(String auditType) throws Exception {
        myCpd.show();
//		pd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("financeApplyId", id);
            jsonObject.put("auditType", auditType);
            jsonObject.put("reason", auditReason);
            if ("comfirm".equals(auditType)) {
                jsonObject.put("actualFeeString", actualFee);
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Define.URL + "acct/cashierComfirm";


        SessionJsonObjectRequest cashierConfirmRequest = new SessionJsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response == null) {
                    Toast.makeText(FinancialListDetailsA.this, "服务器异常!", Toast.LENGTH_SHORT).show();
                }
                Log.d("debug", "response =" + response.toString());
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, "操作成功!", Toast.LENGTH_SHORT).show();
//                        intent = new Intent(FinancialListDetailsA.this, FinancialListA.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        setResult(10);
                        finish();

                    } else if ("46000".equals(responseCode)) {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(FinancialListDetailsA.this, LoginA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        finish();
                    } else {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    myCpd.dismiss();
                    e.printStackTrace();
                    Log.d("exciption", "exciption =" + e.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                myCpd.dismiss();
                Toast.makeText(FinancialListDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("debug", "error = " + error.toString());

            }
        });
        // 解决重复请求后台的问题
        cashierConfirmRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        cashierConfirmRequest.setTag("cashierConfirmRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(cashierConfirmRequest);

    }

    /**
     * 会计确认
     *
     * @throws Exception
     */
    private void accountantConfirm(String auditType) throws Exception {

        if (rbMaxAmount.isChecked()) {
            selectQuotaStr = "big";
        } else if (rbMinAmount.isChecked()) {
            selectQuotaStr = "small";
        }

        myCpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("financeApplyId", id);
            jsonObject.put("auditType", auditType);
            jsonObject.put("reason", auditReason);
            jsonObject.put("payType", selectQuotaStr);
            jsonObject.put("applyFee", money);
            jsonObject.put("preventRepeatToken", preventRepeatToken);  //防重复请求token
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Define.URL + "acct/accountantComfirm";


        SessionJsonObjectRequest accountantConfirmRequest = new SessionJsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String result = "";
                String msg = "";
                if (response == null) {
                    myCpd.dismiss();
                    Toast.makeText(FinancialListDetailsA.this, "服务器异常!", Toast.LENGTH_SHORT).show();
                }
                Log.d("debug", "response =" + response.toString());
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
                        myCpd.dismiss();
                        JSONObject json = response.getJSONObject("data");
                        result = json.optString("result");
                        msg = json.optString("msg");
                        if ("success".equals(result)) {
                            Toast.makeText(FinancialListDetailsA.this, "操作成功!", Toast.LENGTH_SHORT).show();
//                            intent = new Intent(FinancialListDetailsA.this, FinancialListA.class);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            setResult(10);
                            finish();
                        } else if ("fail".equals(result)) {
                            ToastUtils.showLongToast(FinancialListDetailsA.this, msg);
                        }


                    } else if ("46000".equals(responseCode)) {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(FinancialListDetailsA.this, LoginA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        finish();
                    } else {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    myCpd.dismiss();
                    e.printStackTrace();
                    Log.d("exciption", "exciption =" + e.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                myCpd.dismiss();
                Toast.makeText(FinancialListDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("debug", "error = " + error.toString());

            }
        });
        // 解决重复请求后台的问题
        accountantConfirmRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                0, // 默认最大尝试次数
                0f));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        accountantConfirmRequest.setTag("accountantConfirmRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(accountantConfirmRequest);

    }

    /**
     * 会计审核
     *
     * @throws Exception
     */
    private void accountantAudit(String auditType) throws Exception {
        myCpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("financeApplyId", id);
            jsonObject.put("auditType", auditType);
            jsonObject.put("reason", auditReason);
            jsonObject.put("preventRepeatToken", preventRepeatToken);  //防重复请求token
//            jsonObject.put("preventRepeatToken", preventRepeatToken);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Define.URL + "acct/accountantAudit";


        SessionJsonObjectRequest accountantAuditRequest = new SessionJsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                String result = "";
                String msg = "";
                if (response == null) {
                    Toast.makeText(FinancialListDetailsA.this, "服务器异常!", Toast.LENGTH_SHORT).show();
                }
                Log.d("debug", "response =" + response.toString());
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
                        myCpd.dismiss();

                        JSONObject json = response.getJSONObject("data");
                        result = json.optString("result");
                        msg = json.optString("msg");
                        if ("success".equals(result)) {
                            Toast.makeText(FinancialListDetailsA.this, "操作成功!", Toast.LENGTH_SHORT).show();
//                            intent = new Intent(FinancialListDetailsA.this, FinancialListA.class);
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            setResult(10);
                            finish();
                        } else if ("fail".equals(result)) {
                            ToastUtils.showLongToast(FinancialListDetailsA.this, msg);
                        }


                    } else if ("46000".equals(responseCode)) {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(FinancialListDetailsA.this, LoginA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        finish();
                    } else {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    myCpd.dismiss();
                    e.printStackTrace();
                    Log.d("exciption", "exciption =" + e.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                myCpd.dismiss();
                Toast.makeText(FinancialListDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("debug", "error = " + error.toString());

            }
        });
        // 解决重复请求后台的问题
        accountantAuditRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                0, // 默认最大尝试次数
                0f));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        accountantAuditRequest.setTag("accountantAuditRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(accountantAuditRequest);

    }

    /**
     * 汇缴重新付款(出纳)
     *
     * @throws Exception
     */
    private void paySysAgain() throws Exception {
        myCpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("financeApplyId", id);
            jsonObject.put("preventRepeatToken", preventRepeatToken);  //防重复请求token

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Define.URL + "acct/payAgain";


        SessionJsonObjectRequest paySysAgainRequest = new SessionJsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response == null) {
                    Toast.makeText(FinancialListDetailsA.this, "服务器异常!", Toast.LENGTH_SHORT).show();
                }
                Log.d("debug", "response =" + response.toString());
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, "操作成功!", Toast.LENGTH_SHORT).show();
//                        intent = new Intent(FinancialListDetailsA.this, FinancialListA.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        setResult(10);
                        finish();

                    } else if ("46000".equals(responseCode)) {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(FinancialListDetailsA.this, LoginA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        finish();
                    } else {
                        myCpd.dismiss();
                        Toast.makeText(FinancialListDetailsA.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    myCpd.dismiss();
                    e.printStackTrace();
                    Log.d("exciption", "exciption =" + e.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                myCpd.dismiss();
                Toast.makeText(FinancialListDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("debug", "error = " + error.toString());

            }
        });
        // 解决重复请求后台的问题
        paySysAgainRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                0, // 默认最大尝试次数
                0f));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        paySysAgainRequest.setTag("paySysAgainRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(paySysAgainRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        MyApplication.getHttpQueues().cancelAll("cashierConfirmRequest");
        MyApplication.getHttpQueues().cancelAll("accountantConfirmRequest");
        MyApplication.getHttpQueues().cancelAll("accountantAuditRequest");
        MyApplication.getHttpQueues().cancelAll("paySysAgainRequest");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("FinancialListDetailsA Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
}

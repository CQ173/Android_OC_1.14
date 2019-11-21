package com.huoniao.oc.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/13.
 * 暂时没用到，统一挪到了financial包下的FinancialListDetailsA
 */

public class FinancialApplyDetailsA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_organization)
    TextView tvOrganization;
    @InjectView(R.id.tv_agentAccount)
    TextView tvAgentAccount;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_applyType)
    TextView tvApplyType;
    @InjectView(R.id.tv_tradeNumber)
    TextView tvTradeNumber;
    @InjectView(R.id.tv_money)
    TextView tvMoney;
    @InjectView(R.id.tv_accountName)
    TextView tvAccountName;
    @InjectView(R.id.tv_bankName)
    TextView tvBankName;
    @InjectView(R.id.tv_bankNumber)
    TextView tvBankNumber;
    @InjectView(R.id.layout_accountInfo)
    LinearLayout layoutAccountInfo;
    @InjectView(R.id.tv_applyName)
    TextView tvApplyName;
    @InjectView(R.id.tv_receivablesAccount)
    TextView tvReceivablesAccount;
    @InjectView(R.id.tv_receivablesName)
    TextView tvReceivablesName;
    @InjectView(R.id.layout_payeeInfo)
    LinearLayout layoutPayeeInfo;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.tv_remarks)
    TextView tvRemarks;
    @InjectView(R.id.tv_reason)
    TextView tvReason;
    @InjectView(R.id.tv_ashierOperator)
    TextView tvAshierOperator;
    @InjectView(R.id.tv_accountOperator)
    TextView tvAccountOperator;

    private Intent intent;
    private String date, organization, agentAccount, name, applyType, tradeNumber, money,
                accountName, bankName, bankNumber, applyName, receivablesAccount,
                receivablesName, status, remarks, reason, ashierOperator,
                accountOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_financialdetails);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        intent = getIntent();
        date = intent.getStringExtra("createDate");
        if (date != null && !date.isEmpty()){
            tvDate.setText(date);
        }else {
            tvDate.setText("");
        }

        organization = intent.getStringExtra("agencyUserOfficeName");
        if (organization != null && !organization.isEmpty()){
            tvOrganization.setText(organization);
        }else {
            tvOrganization.setText("");
        }

        agentAccount = intent.getStringExtra("agencyLoginName");
        if (agentAccount != null && !agentAccount.isEmpty()){
            tvAgentAccount.setText(agentAccount);
        }else {
            tvAgentAccount.setText("");
        }

        name = intent.getStringExtra("agencyUserName");
        if (name != null && !name.isEmpty()){
            tvName.setText(name);
        }else {
            tvName.setText("");
        }

        applyType = intent.getStringExtra("applyTypeName");
        if (applyType != null && !applyType.isEmpty()){
            tvApplyType.setText(applyType);
            if ("汇缴付款".equals(applyType) || "提现".equals(applyType)){
                layoutAccountInfo.setVisibility(View.VISIBLE);
            }
            if ("代付".equals(applyType)){
                layoutPayeeInfo.setVisibility(View.VISIBLE);
            }
        }else {
            tvApplyType.setText("");
        }

        tradeNumber = intent.getStringExtra("transferId");
        if (tradeNumber != null && !tradeNumber.isEmpty()){
            tvTradeNumber.setText(tradeNumber);
        }else {
            tvTradeNumber.setText("");
        }

        money = intent.getStringExtra("applyFee");
        if (money != null && !money.isEmpty()){
            tvMoney.setText(money);
        }else {
            tvMoney.setText("");
        }

        accountName = intent.getStringExtra("accountName");
        if (accountName != null && !accountName.isEmpty()){
            tvAccountName.setText(accountName);
        }else {
            tvAccountName.setText("");
        }

        bankName = intent.getStringExtra("openBankName");
        if (bankName != null && !bankName.isEmpty()){
            tvBankName.setText(bankName);
        }else {
            tvBankName.setText("");
        }

        bankNumber = intent.getStringExtra("cardNo");
        if (bankNumber != null && !bankNumber.isEmpty()){
            tvBankNumber.setText(bankNumber);
        }else {
            tvBankNumber.setText("");
        }

        applyName = intent.getStringExtra("applyUserName");
        if (applyName != null && !applyName.isEmpty()){
            tvApplyName.setText(applyName);
        }else {
            tvApplyName.setText("");
        }

        receivablesAccount = intent.getStringExtra("receiptLoginName");
        if (receivablesAccount != null && !receivablesAccount.isEmpty()){
            tvReceivablesAccount.setText(receivablesAccount);
        }else {
            tvReceivablesAccount.setText("");
        }

        receivablesName = intent.getStringExtra("receiptName");
        if (receivablesName != null && !receivablesName.isEmpty()){
            tvReceivablesName.setText(receivablesName);
        }else {
            tvReceivablesName.setText("");
        }

        status = intent.getStringExtra("stateName");
        if (status != null && !status.isEmpty()){
            tvStatus.setText(status);
        }else {
            tvStatus.setText("");
        }

        remarks = intent.getStringExtra("remark");
        if (remarks != null && !remarks.isEmpty()){
            tvRemarks.setText(remarks);
        }else {
            tvRemarks.setText("");
        }

        reason = intent.getStringExtra("reason");
        if (reason != null && !reason.isEmpty()){
            tvReason.setText(reason);
        }else {
            tvReason.setText("");
        }
        ashierOperator = intent.getStringExtra("cashierUserName");
        if (ashierOperator != null && !ashierOperator.isEmpty()){
            tvAshierOperator.setText(ashierOperator);
        }else {
            tvAshierOperator.setText("");
        }

        accountOperator = intent.getStringExtra("auditUserName");
        if (accountOperator != null && !accountOperator.isEmpty()){
            tvAccountOperator.setText(accountOperator);
        }else {
            tvAccountOperator.setText("");
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

package com.huoniao.oc.financial;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.NewPaymentPaySumListBean;
import com.huoniao.oc.util.ObjectSaveUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewAdvanceInCashDetailsA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_paymentMoney)
    TextView tvPaymentMoney;
    @InjectView(R.id.tv_shouldPaymentMoney)
    TextView tvShouldPaymentMoney;
    @InjectView(R.id.tv_realPaymentMoney)
    TextView tvRealPaymentMoney;
    @InjectView(R.id.tv_advanceRealMoney)
    TextView tvAdvanceRealMoney;
    @InjectView(R.id.tv_myselfAdvanceMoney)
    TextView tvMyselfAdvanceMoney;
    @InjectView(R.id.tv_shouldMoney)
    TextView tvShouldMoney;
    @InjectView(R.id.tv_shouldQuanFuTong)
    TextView tvShouldQuanFuTong;
    @InjectView(R.id.tv_shouldXinEfu)
    TextView tvShouldXinEfu;
    @InjectView(R.id.tv_poundage)
    TextView tvPoundage;
    @InjectView(R.id.tv_poundageQuanFuTong)
    TextView tvPoundageQuanFuTong;
    @InjectView(R.id.tv_poundageXinEfu)
    TextView tvPoundageXinEfu;
    @InjectView(R.id.tv_receivableMoney)
    TextView tvReceivableMoney;
    @InjectView(R.id.tv_receivaQuanFuTong)
    TextView tvReceivaQuanFuTong;
    @InjectView(R.id.tv_receivaXinEfu)
    TextView tvReceivaXinEfu;
    @InjectView(R.id.tv_zxSettlement)
    TextView tvZxSettlement;
    @InjectView(R.id.tv_advanceInCashingMoney)
    TextView tvAdvanceInCashingMoney;
    @InjectView(R.id.tv_zxzhSurplusAmountString)
    TextView tvZxzhSurplusAmountString;
    private NewPaymentPaySumListBean.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advance_in_cash_details);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        dataBean = (NewPaymentPaySumListBean.DataBean) ObjectSaveUtil.readObject(NewAdvanceInCashDetailsA.this, "paymentPayTemSumList");
        tvPaymentMoney.setText(dataBean.getPaymentAmountString());
        tvShouldPaymentMoney.setText(dataBean.getShouldPayAmountString() + "元");
        tvRealPaymentMoney.setText(dataBean.getActualPayAmountSumString() + "元");
        tvAdvanceRealMoney.setText(dataBean.getZxActualPayAmountString() + "元");
        tvAdvanceInCashingMoney.setText(dataBean.getSpotingAmountString() + "元");
        tvMyselfAdvanceMoney.setText(dataBean.getOwnActualPayAmountString() + "元");
        tvShouldMoney.setText(dataBean.getShouldReceiveAmountSumString() + "元");
        tvShouldQuanFuTong.setText(dataBean.getQftShouldReceiveAmountString() + "元");
        tvShouldXinEfu.setText(dataBean.getXefShouldReceiveAmountString() + "元");
        tvPoundage.setText(dataBean.getPoundageAmountSumString() + "元");
        tvPoundageQuanFuTong.setText(dataBean.getQftPoundageAmountString() + "元");
        tvPoundageXinEfu.setText(dataBean.getXefPoundageAmountString() + "元");
        tvReceivableMoney.setText(dataBean.getActualReceiveAmountSumString() + "元");
        tvReceivaQuanFuTong.setText(dataBean.getQftActualReceiveAmountString() + "元");
        tvReceivaXinEfu.setText(dataBean.getXefActualReceiveAmountString() + "元");
        tvZxSettlement.setText(dataBean.getZxzhSettlementAmountString() + "元");
        tvZxzhSurplusAmountString.setText(dataBean.getZxzhSurplusAmountString() + "元");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}

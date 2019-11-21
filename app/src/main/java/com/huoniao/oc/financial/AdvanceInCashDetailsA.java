package com.huoniao.oc.financial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.PaymentPaySumListBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.ObjectSaveUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class AdvanceInCashDetailsA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_paymentAmount)
    TextView tvPaymentAmount;
    @InjectView(R.id.tv_staImportCount)
    TextView tvStaImportCount;
    @InjectView(R.id.tv_advanceCashSum)
    TextView tvAdvanceCashSum;
    @InjectView(R.id.tv_unUsedQuota)
    TextView tvUnUsedQuota;
    @InjectView(R.id.tv_paymentSum)
    TextView tvPaymentSum;
//    @InjectView(R.id.tv_5911PaymentAmount)
//    TextView tv5911PaymentAmount;
//    @InjectView(R.id.tv_supplementPaymentAmount)
//    TextView tvSupplementPaymentAmount;
    @InjectView(R.id.tv_repaymentSum)
    TextView tvRepaymentSum;
    @InjectView(R.id.tv_5911repaymentAmount)
    TextView tv5911repaymentAmount;
    @InjectView(R.id.tv_supplementRepaymentAmount)
    TextView tvSupplementRepaymentAmount;
    @InjectView(R.id.tv_unRepaymentSum)
    TextView tvUnRepaymentSum;
    @InjectView(R.id.bt_repaymentApply)
    Button btRepaymentApply;
    private Intent intent;
    private String id;
    private User user;
    private String roleName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_in_cash_details);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        user = (User) readObject(AdvanceInCashDetailsA.this, "loginResult");
        roleName = user.getRoleName();

        PaymentPaySumListBean.DataBean dataBean = (PaymentPaySumListBean.DataBean) ObjectSaveUtil.readObject(AdvanceInCashDetailsA.this, "paymentPaySumList");
        id = dataBean.getId();
        tvPaymentAmount.setText(dataBean.getPaymentAmountString()+"元");
        tvStaImportCount.setText(dataBean.getShouldPaymentCount()+"("+dataBean.getAlreadyPaymentCount()+")家");
        tvAdvanceCashSum.setText(dataBean.getSpotAmountString()+"元");
        tvUnUsedQuota.setText(dataBean.getUnUseSpotAmountString()+"元");
        tvPaymentSum.setText(dataBean.getFbPayAmountString()+"元");
        tvRepaymentSum.setText(dataBean.getReturnAmountString()+"元");
        tv5911repaymentAmount.setText(dataBean.getMainReturnAmountString()+"元");
        tvSupplementRepaymentAmount.setText(dataBean.getFillReturnAmountString()+"元");
        tvUnRepaymentSum.setText(dataBean.getNotReturnAmountString()+"元");

        if (roleName.contains("出纳")){
            if (!"0.00".equals(dataBean.getNotReturnAmountString())) {
                btRepaymentApply.setVisibility(View.VISIBLE);
            }else {
                btRepaymentApply.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.bt_repaymentApply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_repaymentApply:
                intent = new Intent(AdvanceInCashDetailsA.this, AepaymentApplyA.class);
                intent.putExtra("performAction", "还款");
                intent.putExtra("paymentPaySumId", id);
                intent.putExtra("state", "3");//统计列表这里状态都传3
                startActivityForResult(intent, 15);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }
}

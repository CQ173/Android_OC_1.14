package com.huoniao.oc.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.util.Define;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PosSuccessA extends BaseActivity {

    @InjectView(R.id.tv_rechargeAmount)
    TextView tvRechargeAmount;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_state)
    ImageView ivState;
    @InjectView(R.id.tv_state)
    TextView tvState;
    @InjectView(R.id.tv_error)
    TextView tv_error;
    @InjectView(R.id.tv_amountTitle)
    TextView tvAmountTitle;
    private String flowId;//交易流水号
    private String receivable;//收款那边进来的标识
    /*@InjectView(R.id.tv_rechargeTime)
    TextView tvRechargeTime;
    @InjectView(R.id.tv_transactionNumber)
    TextView tvTransactionNumber;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_success);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        receivable = intent.getStringExtra("receivable");
        String success = intent.getStringExtra("transactionStatus"); //成功 和失败
        String rechargeAmount = intent.getStringExtra("rechargeAmount"); //金额
        String er = intent.getStringExtra("er");  //错误消息
        flowId = intent.getStringExtra("flowId");
        if (success != null) {
            if (success.equals("success")) {
                if (receivable == null) {
                    tvTitle.setText("缴费成功");
                    tvState.setText("缴费成功");
                    tvAmountTitle.setText("缴费金额");
                }else {
                    tvTitle.setText("收款成功");
                    tvState.setText("收款成功");
                    tvAmountTitle.setText("收款金额");
                }
                ivState.setImageDrawable(getResources().getDrawable(R.drawable.pos_success));
                tvRechargeAmount.setText(rechargeAmount+"元");
            } else {

                if (receivable == null) {
                    tvTitle.setText("缴费失败");
                    tvState.setText("缴费失败");
                    tvAmountTitle.setText("缴费金额");
                }else {
                    tvTitle.setText("收款失败");
                    tvState.setText("收款失败");
                    tvAmountTitle.setText("收款金额");
                }
                ivState.setImageDrawable(getResources().getDrawable(R.drawable.pos_er));
                tvRechargeAmount.setText(rechargeAmount+"元");
                tv_error.setText(er);
                requestCloseTradeStatus();
            }
        }

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 通知后台交易关闭
     */
    private void requestCloseTradeStatus() {
        String url = Define.URL + "acct/closeTradeStatus";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("flowId", flowId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "closeTradeStatus", "", true, true);
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "closeTradeStatus":

                break;
        }
    }
}

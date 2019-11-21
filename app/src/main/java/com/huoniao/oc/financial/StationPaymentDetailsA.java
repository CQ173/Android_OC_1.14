package com.huoniao.oc.financial;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.TrainPaymentBean;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StationPaymentDetailsA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_refund)
    TextView tvRefund;
    @InjectView(R.id.tv_stationName)
    TextView tvStationName;
    @InjectView(R.id.tv_stationAccount)
    TextView tvStationAccount;
    @InjectView(R.id.tv_ownershipOrg)
    TextView tvOwnershipOrg;
    @InjectView(R.id.tv_dataQuantity)
    TextView tvDataQuantity;
    @InjectView(R.id.tv_bookingNum)
    TextView tvBookingNum;
    @InjectView(R.id.tv_totalFareAmount)
    TextView tvTotalFareAmount;
    @InjectView(R.id.tv_actualPayMoney)
    TextView tvActualPayMoney;
    @InjectView(R.id.tv_paymentState)
    TextView tvPaymentState;
    @InjectView(R.id.tv_paymentNumber)
    TextView tvPaymentNumber;
    @InjectView(R.id.tv_importDate)
    TextView tvImportDate;

    private TrainPaymentBean.DataBean dataBean;
    private String trainPaymentId;
    private String payType;
    private String payState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_payment_details);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {

        dataBean = (TrainPaymentBean.DataBean) ObjectSaveUtil.readObject(StationPaymentDetailsA.this, "trainPaymentList");
        if (dataBean != null) {
            trainPaymentId = dataBean.getId();
            tvStationName.setText(dataBean.getStationName());
            tvStationAccount.setText(dataBean.getStationCode());
            tvOwnershipOrg.setText(dataBean.getParentName());
            tvDataQuantity.setText(dataBean.getDataCount() + "");
            tvBookingNum.setText(dataBean.getTicketCount() + "");
            tvTotalFareAmount.setText(dataBean.getShouldAmountString());
            tvActualPayMoney.setText(dataBean.getPayAmountString());
            tvPaymentState.setText(dataBean.getPayStateName());
            tvPaymentNumber.setText(dataBean.getTradeNumberString());
            tvImportDate.setText(dataBean.getCreateDateString());
            payType = dataBean.getPayType();
            payState = dataBean.getPayState();
            if ("0".equals(payType) && "0".equals(payState)){
                setPremissionShowHideView(Premission.FB_TRAINPAYMENT_REFUND, tvRefund); //车站汇缴退款按钮
            }
        }
    }


    /**
     * 请求汇缴列表退款
     */
    public void requestTrainPaymentRefund() {

        String url = Define.URL + "fb/trainPaymentRefund";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("trainPaymentId", trainPaymentId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "trainPaymentRefund", "1", true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "trainPaymentRefund":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String result = jsonObject.optString("result");
                    if ("success".equals(result)) {
                        ToastUtils.showToast(StationPaymentDetailsA.this, "退款成功！");
                        setResult(60);
                        finish();
                    } else {
                        String message = jsonObject.optString("msg");
                        ToastUtils.showToast(StationPaymentDetailsA.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_refund})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_refund:
                requestTrainPaymentRefund();
                break;
        }
    }
}

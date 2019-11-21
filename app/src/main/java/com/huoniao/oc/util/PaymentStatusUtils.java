package com.huoniao.oc.util;

/**
 * Created by Administrator on 2017/12/4.
 */

public class PaymentStatusUtils {
    public static String paymentState(String state) {
        if ("等待付款".equals(state)) {
            return Define.ACCT_TRADE_STATUS_WAIT;
        } else if ("等待处理".equals(state)) {
            return Define.ACCT_TRADE_STATUS_SUSPENSE;
        } else if ("交易失败".equals(state)) {
            return Define.ACCT_TRADE_STATUS_FAIL;
        } else if ("交易关闭".equals(state)) {
            return Define.ACCT_TRADE_STATUS_CLOSED;
        } else if ("等待收款".equals(state)) {
            return Define.ACCT_TRADE_STATUS_PENDING;
        } else if ("交易成功".equals(state)) { //表示支付成功的
            return Define.ACCT_TRADE_STATUS_SUCCESS;
        } else if ("交易结束".equals(state)) {
            return Define.ACCT_TRADE_STATUS_FINISHED;
        } else if ("退款中".equals(state)) {

                     /*   tv_tradeStatus.setText("交易成功"); //退款中
                        setRefundState(View.VISIBLE, false, R.color.yellow, "退款中", R.color.white);*/
            //    tv_tradeStatus.setText("退款中");
            return Define.ACCT_TRADE_STATUS_REFUNDING;

        } else if ("已退款".equals(state)) {
                      /*  tv_tradeStatus.setText("交易成功");  // 本来状态改为  已退款  现在把已退款放到按钮上
                        setRefundState(View.VISIBLE, false, R.color.grayss, "已退款", R.color.white);*/
            //tv_tradeStatus.setText("已退款");
            return Define.ACCT_TRADE_STATUS_REFUND;
        } else if ("退款失败".equals(state)) {
                     /*   tv_tradeStatus.setText("交易失败");
                        setRefundState(View.VISIBLE, false, R.color.colorAccent, "退款失败", R.color.white);*/
            //tv_tradeStatus.setText("退款失败");
            return Define.ACC_TRADE_STATUS_REFUND_FAIL;
        }
        return "";
    }

}

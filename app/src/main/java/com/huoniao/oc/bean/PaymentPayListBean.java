package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/15.
 */

public class PaymentPayListBean implements Serializable {

    /**
     * code : 0
     * data : [{"accountantName":"无","cashierName":"无","delFlag":"0","fbPayAmountString":"------","fillReturnAmountString":"0.00","id":"4200a765c5844d01a246dbcd3b185c90","mainReturnAmountString":"4444.00","payOrReturnAmount":"4444.00","paymentAmountString":"4444.00","paymentPaySumId":"8875d90a5a974c68ae9704c3e4ef213f","returnNo":"------","source":"0","sourceName":"中信","spotAmountString":"4444.00","spotNo":"ABCS20180808054231000021515","state":"6","stateName":"还款失败","stationId":"d457ea9122df45ae8a4d9c9178d5a39c","stationName":"长沙市火车站(测试专用)","time":"2018-08-15(三)"},{"accountantName":"超级管理员","cashierName":"超级管理员","delFlag":"0","fbPayAmountString":"2222.00","fillReturnAmountString":"------","id":"1a8f1c35b39d43899ba2941b9fc9607f","mainReturnAmountString":"------","payOrReturnAmount":"2222.00","paymentAmountString":"2222.00","paymentPaySumId":"8875d90a5a974c68ae9704c3e4ef213f","remarks":"载左左","returnNo":"------","source":"1","sourceName":"火鸟","spotAmountString":"2222.00","spotNo":"ABCS20180808055140000021519","state":"6","stateName":"还款失败","stationId":"d457ea9122df45ae8a4d9c9178d5a39c","stationName":"长沙市火车站(测试专用)","time":"2018-08-15(三)"}]
     * msg : 请求成功
     * next : -1
     * size : 2
     */

    private String code;
    private String msg;
    private int next;
    private int size;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * accountantName : 无
         * cashierName : 无
         * delFlag : 0
         * fbPayAmountString : ------
         * fillReturnAmountString : 0.00
         * id : 4200a765c5844d01a246dbcd3b185c90
         * mainReturnAmountString : 4444.00
         * payOrReturnAmount : 4444.00
         * paymentAmountString : 4444.00
         * paymentPaySumId : 8875d90a5a974c68ae9704c3e4ef213f
         * returnNo : ------
         * source : 0
         * sourceName : 中信
         * spotAmountString : 4444.00
         * spotNo : ABCS20180808054231000021515
         * state : 6
         * stateName : 还款失败
         * stationId : d457ea9122df45ae8a4d9c9178d5a39c
         * stationName : 长沙市火车站(测试专用)
         * time : 2018-08-15(三)
         * remarks : 载左左
         */

        private String accountantName;
        private String cashierName;
        private String delFlag;
        private String fbPayAmountString;
        private String fillReturnAmountString;
        private String id;
        private String mainReturnAmountString;
        private String payOrReturnAmount;
        private String paymentAmountString;
        private String paymentPaySumId;
        private String returnNo;
        private String source;
        private String sourceName;
        private String spotAmountString;
        private String spotNo;
        private String state;
        private String stateName;
        private String stationId;
        private String stationName;
        private String time;
        private String remarks;

        public String getAccountantName() {
            return accountantName;
        }

        public void setAccountantName(String accountantName) {
            this.accountantName = accountantName;
        }

        public String getCashierName() {
            return cashierName;
        }

        public void setCashierName(String cashierName) {
            this.cashierName = cashierName;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getFbPayAmountString() {
            return fbPayAmountString;
        }

        public void setFbPayAmountString(String fbPayAmountString) {
            this.fbPayAmountString = fbPayAmountString;
        }

        public String getFillReturnAmountString() {
            return fillReturnAmountString;
        }

        public void setFillReturnAmountString(String fillReturnAmountString) {
            this.fillReturnAmountString = fillReturnAmountString;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMainReturnAmountString() {
            return mainReturnAmountString;
        }

        public void setMainReturnAmountString(String mainReturnAmountString) {
            this.mainReturnAmountString = mainReturnAmountString;
        }

        public String getPayOrReturnAmount() {
            return payOrReturnAmount;
        }

        public void setPayOrReturnAmount(String payOrReturnAmount) {
            this.payOrReturnAmount = payOrReturnAmount;
        }

        public String getPaymentAmountString() {
            return paymentAmountString;
        }

        public void setPaymentAmountString(String paymentAmountString) {
            this.paymentAmountString = paymentAmountString;
        }

        public String getPaymentPaySumId() {
            return paymentPaySumId;
        }

        public void setPaymentPaySumId(String paymentPaySumId) {
            this.paymentPaySumId = paymentPaySumId;
        }

        public String getReturnNo() {
            return returnNo;
        }

        public void setReturnNo(String returnNo) {
            this.returnNo = returnNo;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getSpotAmountString() {
            return spotAmountString;
        }

        public void setSpotAmountString(String spotAmountString) {
            this.spotAmountString = spotAmountString;
        }

        public String getSpotNo() {
            return spotNo;
        }

        public void setSpotNo(String spotNo) {
            this.spotNo = spotNo;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getStationId() {
            return stationId;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}

package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/5.
 */

public class NewPaymentPaySumListBean implements Serializable {


    /**
     * code : 0
     * data : [{"actualPayAmountSumString":"9.00","actualReceiveAmountSumString":"17.00","delFlag":"0","id":"4aef5f8a0f064b6e8df6f7df010a673d","ownActualPayAmountString":"5.00","paymentAmountString":"1.00","poundageAmountSumString":"1.00","qftActualReceiveAmountString":"8.00","qftPoundageAmountString":"0.00","qftShouldReceiveAmountString":"6.00","shouldPayAmountString":"3.00","shouldReceiveAmountSumString":"13.00","spotingAmountString":"2.00","time":"2018-11-02(五)","xefActualReceiveAmountString":"9.00","xefPoundageAmountString":"1.00","xefShouldReceiveAmountString":"7.00","zxActualPayAmountString":"4.00","zxzhSettlementAmountString":"0.00"},{"actualPayAmountSumString":"13.00","actualReceiveAmountSumString":"2.00","delFlag":"0","id":"5126a33997b24c4fa9c90365632f155e","ownActualPayAmountString":"7.00","paymentAmountString":"2.00","poundageAmountSumString":"2.00","qftActualReceiveAmountString":"1.00","qftPoundageAmountString":"1.00","qftShouldReceiveAmountString":"8.00","shouldPayAmountString":"5.00","shouldReceiveAmountSumString":"17.00","spotingAmountString":"34.00","time":"2018-11-02(五)","xefActualReceiveAmountString":"1.00","xefPoundageAmountString":"1.00","xefShouldReceiveAmountString":"9.00","zxActualPayAmountString":"6.00","zxzhSettlementAmountString":"0.00"}]
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
         * actualPayAmountSumString : 9.00
         * actualReceiveAmountSumString : 17.00
         * delFlag : 0
         * id : 4aef5f8a0f064b6e8df6f7df010a673d
         * ownActualPayAmountString : 5.00
         * paymentAmountString : 1.00
         * poundageAmountSumString : 1.00
         * qftActualReceiveAmountString : 8.00
         * qftPoundageAmountString : 0.00
         * qftShouldReceiveAmountString : 6.00
         * shouldPayAmountString : 3.00
         * shouldReceiveAmountSumString : 13.00
         * spotingAmountString : 2.00
         * time : 2018-11-02(五)
         * xefActualReceiveAmountString : 9.00
         * xefPoundageAmountString : 1.00
         * xefShouldReceiveAmountString : 7.00
         * zxActualPayAmountString : 4.00
         * zxzhSettlementAmountString : 0.00
         */

        private String actualPayAmountSumString;
        private String actualReceiveAmountSumString;
        private String delFlag;
        private String id;
        private String ownActualPayAmountString;
        private String paymentAmountString;
        private String poundageAmountSumString;
        private String qftActualReceiveAmountString;
        private String qftPoundageAmountString;
        private String qftShouldReceiveAmountString;
        private String shouldPayAmountString;
        private String shouldReceiveAmountSumString;
        private String spotingAmountString;
        private String time;
        private String xefActualReceiveAmountString;
        private String xefPoundageAmountString;
        private String xefShouldReceiveAmountString;
        private String zxActualPayAmountString;
        private String zxzhSettlementAmountString;
        private String zxzhSurplusAmountString;


        public String getActualPayAmountSumString() {
            return actualPayAmountSumString;
        }

        public void setActualPayAmountSumString(String actualPayAmountSumString) {
            this.actualPayAmountSumString = actualPayAmountSumString;
        }

        public String getActualReceiveAmountSumString() {
            return actualReceiveAmountSumString;
        }

        public void setActualReceiveAmountSumString(String actualReceiveAmountSumString) {
            this.actualReceiveAmountSumString = actualReceiveAmountSumString;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOwnActualPayAmountString() {
            return ownActualPayAmountString;
        }

        public void setOwnActualPayAmountString(String ownActualPayAmountString) {
            this.ownActualPayAmountString = ownActualPayAmountString;
        }

        public String getPaymentAmountString() {
            return paymentAmountString;
        }

        public void setPaymentAmountString(String paymentAmountString) {
            this.paymentAmountString = paymentAmountString;
        }

        public String getPoundageAmountSumString() {
            return poundageAmountSumString;
        }

        public void setPoundageAmountSumString(String poundageAmountSumString) {
            this.poundageAmountSumString = poundageAmountSumString;
        }

        public String getQftActualReceiveAmountString() {
            return qftActualReceiveAmountString;
        }

        public void setQftActualReceiveAmountString(String qftActualReceiveAmountString) {
            this.qftActualReceiveAmountString = qftActualReceiveAmountString;
        }

        public String getQftPoundageAmountString() {
            return qftPoundageAmountString;
        }

        public void setQftPoundageAmountString(String qftPoundageAmountString) {
            this.qftPoundageAmountString = qftPoundageAmountString;
        }

        public String getQftShouldReceiveAmountString() {
            return qftShouldReceiveAmountString;
        }

        public void setQftShouldReceiveAmountString(String qftShouldReceiveAmountString) {
            this.qftShouldReceiveAmountString = qftShouldReceiveAmountString;
        }

        public String getShouldPayAmountString() {
            return shouldPayAmountString;
        }

        public void setShouldPayAmountString(String shouldPayAmountString) {
            this.shouldPayAmountString = shouldPayAmountString;
        }

        public String getShouldReceiveAmountSumString() {
            return shouldReceiveAmountSumString;
        }

        public void setShouldReceiveAmountSumString(String shouldReceiveAmountSumString) {
            this.shouldReceiveAmountSumString = shouldReceiveAmountSumString;
        }

        public String getSpotingAmountString() {
            return spotingAmountString;
        }

        public void setSpotingAmountString(String spotingAmountString) {
            this.spotingAmountString = spotingAmountString;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getXefActualReceiveAmountString() {
            return xefActualReceiveAmountString;
        }

        public void setXefActualReceiveAmountString(String xefActualReceiveAmountString) {
            this.xefActualReceiveAmountString = xefActualReceiveAmountString;
        }

        public String getXefPoundageAmountString() {
            return xefPoundageAmountString;
        }

        public void setXefPoundageAmountString(String xefPoundageAmountString) {
            this.xefPoundageAmountString = xefPoundageAmountString;
        }

        public String getXefShouldReceiveAmountString() {
            return xefShouldReceiveAmountString;
        }

        public void setXefShouldReceiveAmountString(String xefShouldReceiveAmountString) {
            this.xefShouldReceiveAmountString = xefShouldReceiveAmountString;
        }

        public String getZxActualPayAmountString() {
            return zxActualPayAmountString;
        }

        public void setZxActualPayAmountString(String zxActualPayAmountString) {
            this.zxActualPayAmountString = zxActualPayAmountString;
        }

        public String getZxzhSettlementAmountString() {
            return zxzhSettlementAmountString;
        }

        public void setZxzhSettlementAmountString(String zxzhSettlementAmountString) {
            this.zxzhSettlementAmountString = zxzhSettlementAmountString;
        }

        public String getZxzhSurplusAmountString() {
            return zxzhSurplusAmountString;
        }

        public void setZxzhSurplusAmountString(String zxzhSurplusAmountString) {
            this.zxzhSurplusAmountString = zxzhSurplusAmountString;
        }
    }
}

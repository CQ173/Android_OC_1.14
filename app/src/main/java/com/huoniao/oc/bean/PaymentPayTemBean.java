package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */

public class PaymentPayTemBean implements Serializable {


    /**
     * code : 0
     * data : [{"actualPayAmountString":"2.00","createDate":"2018-11-02 17:14:59","id":"4aef5f8a0f064b6e8df6f7df010a673d","ownActualPayAmountString":"4.00","paymentPayTemSumId":"a","poundageAmountString":"5.00","shouldPayAmountString":"1.00","state":"0","stateName":"成功","tradeNumber":"123","zxActualPayAmountString":"3.00"},{"actualPayAmountString":"3.00","batch":1,"createDate":"2018-11-02 17:14:59","id":"5126a33997b24c4fa9c90365632f155e","ownActualPayAmountString":"5.00","paymentPayTemSumId":"b","poundageAmountString":"6.00","shouldPayAmountString":"2.00","state":"1","stateName":"失败","tradeNumber":"456","zxActualPayAmountString":"4.00"}]
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
         * actualPayAmountString : 2.00
         * createDate : 2018-11-02 17:14:59
         * id : 4aef5f8a0f064b6e8df6f7df010a673d
         * ownActualPayAmountString : 4.00
         * paymentPayTemSumId : a
         * poundageAmountString : 5.00
         * shouldPayAmountString : 1.00
         * state : 0
         * stateName : 成功
         * tradeNumber : 123
         * zxActualPayAmountString : 3.00
         * batch : 1
         */

        private String actualPayAmountString;
        private String createDate;
        private String id;
        private String ownActualPayAmountString;
        private String paymentPayTemSumId;
        private String poundageAmountString;
        private String shouldPayAmountString;
        private String state;
        private String stateName;
        private String tradeNumber;
        private String zxActualPayAmountString;
        private int batch;

        public String getActualPayAmountString() {
            return actualPayAmountString;
        }

        public void setActualPayAmountString(String actualPayAmountString) {
            this.actualPayAmountString = actualPayAmountString;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
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

        public String getPaymentPayTemSumId() {
            return paymentPayTemSumId;
        }

        public void setPaymentPayTemSumId(String paymentPayTemSumId) {
            this.paymentPayTemSumId = paymentPayTemSumId;
        }

        public String getPoundageAmountString() {
            return poundageAmountString;
        }

        public void setPoundageAmountString(String poundageAmountString) {
            this.poundageAmountString = poundageAmountString;
        }

        public String getShouldPayAmountString() {
            return shouldPayAmountString;
        }

        public void setShouldPayAmountString(String shouldPayAmountString) {
            this.shouldPayAmountString = shouldPayAmountString;
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

        public String getTradeNumber() {
            return tradeNumber;
        }

        public void setTradeNumber(String tradeNumber) {
            this.tradeNumber = tradeNumber;
        }

        public String getZxActualPayAmountString() {
            return zxActualPayAmountString;
        }

        public void setZxActualPayAmountString(String zxActualPayAmountString) {
            this.zxActualPayAmountString = zxActualPayAmountString;
        }

        public int getBatch() {
            return batch;
        }

        public void setBatch(int batch) {
            this.batch = batch;
        }
    }
}

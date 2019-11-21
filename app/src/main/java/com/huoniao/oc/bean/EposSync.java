package com.huoniao.oc.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/1.
 */

public class EposSync implements Serializable {

    /**
     * code : 0
     * data : {"account":"621792******0880","balance":-8214339.88,"status":"交易成功","tradeNo":"002518","updateDate":"2017-12-01 16:40:26"}
     * msg : 请求成功
     */

    private String code;
    private DataBean data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean implements Serializable{
        /**
         * account : 621792******0880
         * balance : -8214339.88
         * status : 交易成功
         * tradeNo : 002518
         * updateDate : 2017-12-01 16:40:26
         */

        private String account;
        private double balance;
        private String status;
        private String tradeNo;
        private String updateDate;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }
    }
}

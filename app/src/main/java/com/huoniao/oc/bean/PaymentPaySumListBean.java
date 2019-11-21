package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/14.
 */

public class PaymentPaySumListBean implements Serializable {


    /**
     * code : 0
     * data : [{"alreadyPaymentCount":2,"delFlag":"0","fbPayAmountString":"0.00","fillReturnAmountString":"0.00","id":"3069f8f29d80498888950361f47d32dc","mainReturnAmountString":"0.00","maxSpotAmountString":"20000000.00","notReturnAmountString":"10006.00","paymentAmountString":"10006.00","returnAmountString":"0.00","shouldPaymentCount":1,"spotAmountString":"10006.00","time":"2018-08-13(一)","unUseSpotAmountString":"19989994.00"},{"alreadyPaymentCount":4,"delFlag":"0","fbPayAmountString":"0.00","fillReturnAmountString":"0.00","id":"5c641672af0e491abbd49addf924b784","mainReturnAmountString":"0.00","maxSpotAmountString":"20000000.00","notReturnAmountString":"35552.00","paymentAmountString":"35552.00","returnAmountString":"0.00","shouldPaymentCount":1,"spotAmountString":"35552.00","time":"2018-08-12(日)","unUseSpotAmountString":"19964448.00"}]
     * msg : 请求成功
     * size : 2
     */

    private String code;
    private String msg;
    private String size;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
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
         * alreadyPaymentCount : 2
         * delFlag : 0
         * fbPayAmountString : 0.00
         * fillReturnAmountString : 0.00
         * id : 3069f8f29d80498888950361f47d32dc
         * mainReturnAmountString : 0.00
         * maxSpotAmountString : 20000000.00
         * notReturnAmountString : 10006.00
         * paymentAmountString : 10006.00
         * returnAmountString : 0.00
         * shouldPaymentCount : 1
         * spotAmountString : 10006.00
         * time : 2018-08-13(一)
         * unUseSpotAmountString : 19989994.00
         */

        private int alreadyPaymentCount;
        private String delFlag;
        private String fbPayAmountString;
        private String fillReturnAmountString;
        private String id;
        private String mainReturnAmountString;
        private String maxSpotAmountString;
        private String notReturnAmountString;
        private String paymentAmountString;
        private String returnAmountString;
        private int shouldPaymentCount;
        private String spotAmountString;
        private String time;
        private String unUseSpotAmountString;

        public int getAlreadyPaymentCount() {
            return alreadyPaymentCount;
        }

        public void setAlreadyPaymentCount(int alreadyPaymentCount) {
            this.alreadyPaymentCount = alreadyPaymentCount;
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

        public String getMaxSpotAmountString() {
            return maxSpotAmountString;
        }

        public void setMaxSpotAmountString(String maxSpotAmountString) {
            this.maxSpotAmountString = maxSpotAmountString;
        }

        public String getNotReturnAmountString() {
            return notReturnAmountString;
        }

        public void setNotReturnAmountString(String notReturnAmountString) {
            this.notReturnAmountString = notReturnAmountString;
        }

        public String getPaymentAmountString() {
            return paymentAmountString;
        }

        public void setPaymentAmountString(String paymentAmountString) {
            this.paymentAmountString = paymentAmountString;
        }

        public String getReturnAmountString() {
            return returnAmountString;
        }

        public void setReturnAmountString(String returnAmountString) {
            this.returnAmountString = returnAmountString;
        }

        public int getShouldPaymentCount() {
            return shouldPaymentCount;
        }

        public void setShouldPaymentCount(int shouldPaymentCount) {
            this.shouldPaymentCount = shouldPaymentCount;
        }

        public String getSpotAmountString() {
            return spotAmountString;
        }

        public void setSpotAmountString(String spotAmountString) {
            this.spotAmountString = spotAmountString;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUnUseSpotAmountString() {
            return unUseSpotAmountString;
        }

        public void setUnUseSpotAmountString(String unUseSpotAmountString) {
            this.unUseSpotAmountString = unUseSpotAmountString;
        }
    }
}

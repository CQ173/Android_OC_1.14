package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/13.
 */

public class PaymentPaySumTisBean {


    /**
     * code : 0
     * data : {"paymentAmount":["0.00","0.00","0.00"],"result":"success","returnAmount":["0.00","0.00","0.00"],"spotAmount":["0.00","0.00","0.00"],"time":["2018-08-11","2018-08-12","2018-08-13"]}
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

    public static class DataBean {
        /**
         * paymentAmount : ["0.00","0.00","0.00"]
         * result : success
         * returnAmount : ["0.00","0.00","0.00"]
         * spotAmount : ["0.00","0.00","0.00"]
         * time : ["2018-08-11","2018-08-12","2018-08-13"]
         */

        private String result;
        private String msg;
        private List<String> paymentAmount;
        private List<String> returnAmount;
        private List<String> spotAmount;
        private List<String> time;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<String> getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(List<String> paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public List<String> getReturnAmount() {
            return returnAmount;
        }

        public void setReturnAmount(List<String> returnAmount) {
            this.returnAmount = returnAmount;
        }

        public List<String> getSpotAmount() {
            return spotAmount;
        }

        public void setSpotAmount(List<String> spotAmount) {
            this.spotAmount = spotAmount;
        }

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }
    }
}

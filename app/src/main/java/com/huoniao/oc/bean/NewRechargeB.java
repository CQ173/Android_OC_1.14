package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/4/30.
 */

public class NewRechargeB implements Serializable {

    private String code;
    private String msg;
    private RechargeBean data;

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

    public RechargeBean getData() {
        return data;
    }

    public void setData(RechargeBean data) {
        this.data = data;
    }

    public static class RechargeBean implements Serializable{

        private String serviceFeeAmount;
        private String poundageAmount;

        public String getServiceFeeAmount() {
            return serviceFeeAmount;
        }

        public void setServiceFeeAmount(String serviceFeeAmount) {
            this.serviceFeeAmount = serviceFeeAmount;
        }

        public String getPoundageAmount() {
            return poundageAmount;
        }

        public void setPoundageAmount(String poundageAmount) {
            this.poundageAmount = poundageAmount;
        }
    }
}

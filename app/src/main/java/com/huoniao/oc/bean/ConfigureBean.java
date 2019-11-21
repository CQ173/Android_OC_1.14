package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class ConfigureBean implements Serializable {


    /**
     * code : 0
     * msg : 请求成功
     * list : [{"epos_min_amount":"5000"},{"test":"311"}]
     */

    private String code;
    private String msg;
    /**
     * epos_min_amount : 5000
     */

    private List<ListEntity> list;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity implements Serializable {
        private String epos_min_amount;
        private String cash_min_amount;
        private String firstDeductionTime;
        private String secondDeductionTime;
        private String creditScoreSwitch;
        private int deductionsMaxCount;

        public int getDeductionsMaxCount() {
            return deductionsMaxCount;
        }

        public void setDeductionsMaxCount(int deductionsMaxCount) {
            this.deductionsMaxCount = deductionsMaxCount;
        }

        public String getCash_min_amount() {
            return cash_min_amount;
        }

        public void setCash_min_amount(String cash_min_amount) {
            this.cash_min_amount = cash_min_amount;
        }

        public void setEpos_min_amount(String epos_min_amount) {
            this.epos_min_amount = epos_min_amount;
        }

        public String getEpos_min_amount() {
            return epos_min_amount;
        }

        public String getFirstDeductionTime() {
            return firstDeductionTime;
        }

        public void setFirstDeductionTime(String firstDeductionTime) {
            this.firstDeductionTime = firstDeductionTime;
        }

        public String getSecondDeductionTime() {
            return secondDeductionTime;
        }

        public void setSecondDeductionTime(String secondDeductionTime) {
            this.secondDeductionTime = secondDeductionTime;
        }

        public String getCreditScoreSwitch() {
            return creditScoreSwitch;
        }

        public void setCreditScoreSwitch(String creditScoreSwitch) {
            this.creditScoreSwitch = creditScoreSwitch;
        }
    }
}

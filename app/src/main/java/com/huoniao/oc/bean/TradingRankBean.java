package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */

public class TradingRankBean {

    /**
     * code : 0
     * data : {"income":[{"standby1":"gd190200012","standby2":"杭其昌","sumString":"1980.00"},{"standby1":"gd190200521","standby2":"交水建集团有限公司数据","sumString":"557.00"},{"standby1":"hn1807315556","standby2":"赵程","sumString":"200.00"},{"standby1":"gd190200101","standby2":"李飞","sumString":"35.00"},{"standby1":"hn180731356656","standby2":"宣冉","sumString":"0.01"}],"payout":[{"standby1":"gd190201234","standby2":"刘军","sumString":"17777.00"},{"standby1":"ah1205501001","standby2":"李琪","sumString":"17777.00"},{"standby1":"bj0101005251","standby2":"成贵江","sumString":"13562.00"},{"standby1":"gd1905500122","standby2":"章丽","sumString":"4569.50"},{"standby1":"gd190200012","standby2":"杭其昌","sumString":"2618.00"},{"standby1":"gd190200521","standby2":"交水建集团有限公司数据","sumString":"414.00"}]}
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
        private List<IncomeBean> income;
        private List<PayoutBean> payout;

        public List<IncomeBean> getIncome() {
            return income;
        }

        public void setIncome(List<IncomeBean> income) {
            this.income = income;
        }

        public List<PayoutBean> getPayout() {
            return payout;
        }

        public void setPayout(List<PayoutBean> payout) {
            this.payout = payout;
        }

        public static class IncomeBean {
            /**
             * standby1 : gd190200012
             * standby2 : 杭其昌
             * sumString : 1980.00
             */

            private String standby1;
            private String standby2;
            private String sumString;

            public String getStandby1() {
                return standby1;
            }

            public void setStandby1(String standby1) {
                this.standby1 = standby1;
            }

            public String getStandby2() {
                return standby2;
            }

            public void setStandby2(String standby2) {
                this.standby2 = standby2;
            }

            public String getSumString() {
                return sumString;
            }

            public void setSumString(String sumString) {
                this.sumString = sumString;
            }
        }

        public static class PayoutBean {
            /**
             * standby1 : gd190201234
             * standby2 : 刘军
             * sumString : 17777.00
             */

            private String standby1;
            private String standby2;
            private String sumString;

            public String getStandby1() {
                return standby1;
            }

            public void setStandby1(String standby1) {
                this.standby1 = standby1;
            }

            public String getStandby2() {
                return standby2;
            }

            public void setStandby2(String standby2) {
                this.standby2 = standby2;
            }

            public String getSumString() {
                return sumString;
            }

            public void setSumString(String sumString) {
                this.sumString = sumString;
            }
        }
    }
}

package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class FinOjiSummaryData {


    /**
     * code : 0
     * msg : 请求成功
     * data : {"payout":{"sum":"-56728.50","childList":[{"childList":[],"name":"滞纳金","sum":"-11.00","rate":"0.02"},{"name":"提现到银行卡","sum":"-414.00","childList":[],"rate":"0.73"},{"name":"汇缴扣款","sum":"-56303.50","childList":[],"rate":"99.25"}],"name":"支出"},"income":{"sum":"2772.02","childList":[{"name":"备注","sum":"150.00","childList":[],"rate":"5.41"},{"childList":[],"name":"提现失败返还","sum":"407.00","rate":"14.68"},{"name":"用户充值","sum":"2215.02","childList":[{"name":"银联充值","sum":"11.01","childList":[],"rate":"0.50"},{"name":"支付宝","childList":[],"sum":"735.00","rate":"33.18"},{"name":"微信","childList":[],"sum":"1469.01","rate":"66.32"}],"rate":"79.91"}],"name":"收入"}}
     */

    private String code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * payout : {"sum":"-56728.50","childList":[{"childList":[],"name":"滞纳金","sum":"-11.00","rate":"0.02"},{"name":"提现到银行卡","sum":"-414.00","childList":[],"rate":"0.73"},{"name":"汇缴扣款","sum":"-56303.50","childList":[],"rate":"99.25"}],"name":"支出"}
         * income : {"sum":"2772.02","childList":[{"name":"备注","sum":"150.00","childList":[],"rate":"5.41"},{"childList":[],"name":"提现失败返还","sum":"407.00","rate":"14.68"},{"name":"用户充值","sum":"2215.02","childList":[{"name":"银联充值","sum":"11.01","childList":[],"rate":"0.50"},{"name":"支付宝","childList":[],"sum":"735.00","rate":"33.18"},{"name":"微信","childList":[],"sum":"1469.01","rate":"66.32"}],"rate":"79.91"}],"name":"收入"}
         */

        private PayoutBean payout;
        private IncomeBean income;

        public PayoutBean getPayout() {
            return payout;
        }

        public void setPayout(PayoutBean payout) {
            this.payout = payout;
        }

        public IncomeBean getIncome() {
            return income;
        }

        public void setIncome(IncomeBean income) {
            this.income = income;
        }

        public static class PayoutBean {
            /**
             * sum : -56728.50
             * childList : [{"childList":[],"name":"滞纳金","sum":"-11.00","rate":"0.02"},{"name":"提现到银行卡","sum":"-414.00","childList":[],"rate":"0.73"},{"name":"汇缴扣款","sum":"-56303.50","childList":[],"rate":"99.25"}]
             * name : 支出
             */

            private String sum;
            private String name;
            private List<ChildListBean> childList;

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ChildListBean> getChildList() {
                return childList;
            }

            public void setChildList(List<ChildListBean> childList) {
                this.childList = childList;
            }

            public static class ChildListBean {
                /**
                 * childList : []
                 * name : 滞纳金
                 * sum : -11.00
                 * rate : 0.02
                 */

                private String name;
                private String sum;
                private String rate;
                private List<ChildListBean> childList;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSum() {
                    return sum;
                }

                public void setSum(String sum) {
                    this.sum = sum;
                }

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public List<ChildListBean> getChildList() {
                    return childList;
                }

                public void setChildList(List<ChildListBean> childList) {
                    this.childList = childList;
                }
            }
        }

        public static class IncomeBean {
            /**
             * sum : 2772.02
             * childList : [{"name":"备注","sum":"150.00","childList":[],"rate":"5.41"},{"childList":[],"name":"提现失败返还","sum":"407.00","rate":"14.68"},{"name":"用户充值","sum":"2215.02","childList":[{"name":"银联充值","sum":"11.01","childList":[],"rate":"0.50"},{"name":"支付宝","childList":[],"sum":"735.00","rate":"33.18"},{"name":"微信","childList":[],"sum":"1469.01","rate":"66.32"}],"rate":"79.91"}]
             * name : 收入
             */

            private String sum;
            private String name;
            private List<ChildListBeanX> childList;

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ChildListBeanX> getChildList() {
                return childList;
            }

            public void setChildList(List<ChildListBeanX> childList) {
                this.childList = childList;
            }

            public static class ChildListBeanX {
                /**
                 * name : 备注
                 * sum : 150.00
                 * childList : []
                 * rate : 5.41
                 */

                private String name;
                private String sum;
                private String rate;
                private List<ChildListBeanX> childList;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSum() {
                    return sum;
                }

                public void setSum(String sum) {
                    this.sum = sum;
                }

                public String getRate() {
                    return rate;
                }

                public void setRate(String rate) {
                    this.rate = rate;
                }

                public List<ChildListBeanX> getChildList() {
                    return childList;
                }

                public void setChildList(List<ChildListBeanX> childList) {
                    this.childList = childList;
                }
            }
        }
    }
}

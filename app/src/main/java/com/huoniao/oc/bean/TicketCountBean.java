package com.huoniao.oc.bean;

/**
 * Created by Administrator on 2018/7/25.
 */

public class TicketCountBean {


    /**
     * code : 0
     * data : {"map":{"beginDate":"2018-07-26","endDate":"2018-07-26"},"payable":"0.00","payable1":"0.00","receivable":"0.00","result":"success","ticketAmount":"0.00","todayPay":"0.00","todayWait":"8514.35"}
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
         * map : {"beginDate":"2018-07-26","endDate":"2018-07-26"}
         * payable : 0.00
         * payable1 : 0.00
         * receivable : 0.00
         * result : success
         * ticketAmount : 0.00
         * todayPay : 0.00
         * todayWait : 8514.35
         */

        private MapBean map;
        private String payable;
        private String payable1;
        private String receivable;
        private String result;
        private String ticketAmount;
        private String todayPay;
        private String todayWait;

        public MapBean getMap() {
            return map;
        }

        public void setMap(MapBean map) {
            this.map = map;
        }

        public String getPayable() {
            return payable;
        }

        public void setPayable(String payable) {
            this.payable = payable;
        }

        public String getPayable1() {
            return payable1;
        }

        public void setPayable1(String payable1) {
            this.payable1 = payable1;
        }

        public String getReceivable() {
            return receivable;
        }

        public void setReceivable(String receivable) {
            this.receivable = receivable;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getTicketAmount() {
            return ticketAmount;
        }

        public void setTicketAmount(String ticketAmount) {
            this.ticketAmount = ticketAmount;
        }

        public String getTodayPay() {
            return todayPay;
        }

        public void setTodayPay(String todayPay) {
            this.todayPay = todayPay;
        }

        public String getTodayWait() {
            return todayWait;
        }

        public void setTodayWait(String todayWait) {
            this.todayWait = todayWait;
        }

        public static class MapBean {
            /**
             * beginDate : 2018-07-26
             * endDate : 2018-07-26
             */

            private String beginDate;
            private String endDate;

            public String getBeginDate() {
                return beginDate;
            }

            public void setBeginDate(String beginDate) {
                this.beginDate = beginDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }
        }
    }
}

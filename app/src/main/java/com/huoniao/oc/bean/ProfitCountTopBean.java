package com.huoniao.oc.bean;

/**
 * Created by Administrator on 2018/7/25.
 */

public class ProfitCountTopBean {


    /**
     * code : 0
     * data : {"date":"2018-07-26","monthEarnings":"4475.00","monthTicketAmount":"10067.00","todayReceivable":"0.00","todayWait":"1999396.24","weekDay":"四"}
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
         * date : 2018-07-26
         * monthEarnings : 4475.00
         * monthTicketAmount : 10067.00
         * todayReceivable : 0.00
         * todayWait : 1999396.24
         * weekDay : 四
         */

        private String date;
        private String monthEarnings;
        private String monthTicketAmount;
        private String todayReceivable;
        private String todayWait;
        private String weekDay;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMonthEarnings() {
            return monthEarnings;
        }

        public void setMonthEarnings(String monthEarnings) {
            this.monthEarnings = monthEarnings;
        }

        public String getMonthTicketAmount() {
            return monthTicketAmount;
        }

        public void setMonthTicketAmount(String monthTicketAmount) {
            this.monthTicketAmount = monthTicketAmount;
        }

        public String getTodayReceivable() {
            return todayReceivable;
        }

        public void setTodayReceivable(String todayReceivable) {
            this.todayReceivable = todayReceivable;
        }

        public String getTodayWait() {
            return todayWait;
        }

        public void setTodayWait(String todayWait) {
            this.todayWait = todayWait;
        }

        public String getWeekDay() {
            return weekDay;
        }

        public void setWeekDay(String weekDay) {
            this.weekDay = weekDay;
        }
    }
}

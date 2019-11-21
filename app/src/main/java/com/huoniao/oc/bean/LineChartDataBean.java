package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class LineChartDataBean {

    /**
     * code : 0
     * msg : 请求成功
     * data : [{"week":[[{"date":"2017-09-13","value":8881}],[{"date":"2017-09-14","value":1.16}],[{"date":"2017-09-15","value":8888}],[{"date":"2017-09-16","value":0}],[{"date":"2017-09-17","value":0}],[{"date":"2017-09-18","value":0}],[{"date":"2017-09-19","value":0}],[{"shouldAmountTotal":17770.16}],[{"shouldAmountAvg":2538.6}]]},{"month":[[{"date":"2017-08-20","value":0}],[{"date":"2017-08-21","value":0}],[{"date":"2017-08-22","value":0}],[{"date":"2017-08-23","value":0}],[{"date":"2017-08-24","value":0}],[{"date":"2017-08-25","value":0}],[{"date":"2017-08-26","value":0}],[{"date":"2017-08-27","value":0}],[{"date":"2017-08-28","value":0}],[{"date":"2017-08-29","value":0}],[{"date":"2017-08-30","value":0}],[{"date":"2017-08-31","value":0}],[{"date":"2017-09-01","value":0}],[{"date":"2017-09-02","value":0}],[{"date":"2017-09-03","value":0}],[{"date":"2017-09-04","value":0}],[{"date":"2017-09-05","value":0}],[{"date":"2017-09-06","value":0}],[{"date":"2017-09-07","value":0}],[{"date":"2017-09-08","value":0}],[{"date":"2017-09-09","value":0}],[{"date":"2017-09-10","value":0}],[{"date":"2017-09-11","value":0}],[{"date":"2017-09-12","value":0}],[{"date":"2017-09-13","value":8881}],[{"date":"2017-09-14","value":1.16}],[{"date":"2017-09-15","value":8888}],[{"date":"2017-09-16","value":0}],[{"date":"2017-09-17","value":0}],[{"date":"2017-09-18","value":0}],[{"date":"2017-09-19","value":0}],[{"shouldAmountTotal":17770.16}],[{"shouldAmountAvg":573.24}]]},{"year":[[{"date":"2016-10","value":0}],[{"date":"2016-11","value":0}],[{"date":"2016-12","value":0}],[{"date":"2017-01","value":0}],[{"date":"2017-02","value":0}],[{"date":"2017-03","value":0}],[{"date":"2017-04","value":0}],[{"date":"2017-05","value":0}],[{"date":"2017-06","value":0}],[{"date":"2017-07","value":0.03}],[{"date":"2017-08","value":0}],[{"date":"2017-09","value":3554.5}],[{"shouldAmountTotal":3554.53}],[{"shouldAmountAvg":296.22}]]},{"amount":17772.62},{"count":10}]
     */

    private String code;
    private String msg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * week : [[{"date":"2017-09-13","value":8881}],[{"date":"2017-09-14","value":1.16}],[{"date":"2017-09-15","value":8888}],[{"date":"2017-09-16","value":0}],[{"date":"2017-09-17","value":0}],[{"date":"2017-09-18","value":0}],[{"date":"2017-09-19","value":0}],[{"shouldAmountTotal":17770.16}],[{"shouldAmountAvg":2538.6}]]
         * month : [[{"date":"2017-08-20","value":0}],[{"date":"2017-08-21","value":0}],[{"date":"2017-08-22","value":0}],[{"date":"2017-08-23","value":0}],[{"date":"2017-08-24","value":0}],[{"date":"2017-08-25","value":0}],[{"date":"2017-08-26","value":0}],[{"date":"2017-08-27","value":0}],[{"date":"2017-08-28","value":0}],[{"date":"2017-08-29","value":0}],[{"date":"2017-08-30","value":0}],[{"date":"2017-08-31","value":0}],[{"date":"2017-09-01","value":0}],[{"date":"2017-09-02","value":0}],[{"date":"2017-09-03","value":0}],[{"date":"2017-09-04","value":0}],[{"date":"2017-09-05","value":0}],[{"date":"2017-09-06","value":0}],[{"date":"2017-09-07","value":0}],[{"date":"2017-09-08","value":0}],[{"date":"2017-09-09","value":0}],[{"date":"2017-09-10","value":0}],[{"date":"2017-09-11","value":0}],[{"date":"2017-09-12","value":0}],[{"date":"2017-09-13","value":8881}],[{"date":"2017-09-14","value":1.16}],[{"date":"2017-09-15","value":8888}],[{"date":"2017-09-16","value":0}],[{"date":"2017-09-17","value":0}],[{"date":"2017-09-18","value":0}],[{"date":"2017-09-19","value":0}],[{"shouldAmountTotal":17770.16}],[{"shouldAmountAvg":573.24}]]
         * year : [[{"date":"2016-10","value":0}],[{"date":"2016-11","value":0}],[{"date":"2016-12","value":0}],[{"date":"2017-01","value":0}],[{"date":"2017-02","value":0}],[{"date":"2017-03","value":0}],[{"date":"2017-04","value":0}],[{"date":"2017-05","value":0}],[{"date":"2017-06","value":0}],[{"date":"2017-07","value":0.03}],[{"date":"2017-08","value":0}],[{"date":"2017-09","value":3554.5}],[{"shouldAmountTotal":3554.53}],[{"shouldAmountAvg":296.22}]]
         * amount : 17772.62
         * count : 10
         */

        private double amount = -1;
        private int count = -1;
        private List<List<WeekBean>> week;
        private List<List<MonthBean>> month;
        private List<List<YearBean>> year;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<List<WeekBean>> getWeek() {
            return week;
        }

        public void setWeek(List<List<WeekBean>> week) {
            this.week = week;
        }

        public List<List<MonthBean>> getMonth() {
            return month;
        }

        public void setMonth(List<List<MonthBean>> month) {
            this.month = month;
        }

        public List<List<YearBean>> getYear() {
            return year;
        }

        public void setYear(List<List<YearBean>> year) {
            this.year = year;
        }

        public static class WeekBean {
            /**
             * date : 2017-09-13
             * value : 8881
             */

            private String date;
            private double value;
             private double  shouldAmountTotal=-1; //汇缴总金额
            private double shouldAmountAvg=-1; //平均值

            public double getShouldAmountTotal() {
                return shouldAmountTotal;
            }

            public void setShouldAmountTotal(double shouldAmountTotal) {
                this.shouldAmountTotal = shouldAmountTotal;
            }

            public double getShouldAmountAvg() {
                return shouldAmountAvg;
            }

            public void setShouldAmountAvg(double shouldAmountAvg) {
                this.shouldAmountAvg = shouldAmountAvg;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class MonthBean {
            /**
             * date : 2017-08-20
             * value : 0
             */

            private String date;
            private double value;
            private double  shouldAmountTotal=-1; //汇缴总金额
            private double shouldAmountAvg=-1; //平均值

            public double getShouldAmountAvg() {
                return shouldAmountAvg;
            }

            public void setShouldAmountAvg(double shouldAmountAvg) {
                this.shouldAmountAvg = shouldAmountAvg;
            }

            public double getShouldAmountTotal() {
                return shouldAmountTotal;
            }

            public void setShouldAmountTotal(double shouldAmountTotal) {
                this.shouldAmountTotal = shouldAmountTotal;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class YearBean {
            /**
             * date : 2016-10
             * value : 0
             */

            private String date;
            private double value;
            private double  shouldAmountTotal=-1; //汇缴总金额
            private double shouldAmountAvg=-1; //平均值

            public double getShouldAmountTotal() {
                return shouldAmountTotal;
            }

            public void setShouldAmountTotal(double shouldAmountTotal) {
                this.shouldAmountTotal = shouldAmountTotal;
            }

            public double getShouldAmountAvg() {
                return shouldAmountAvg;
            }

            public void setShouldAmountAvg(double shouldAmountAvg) {
                this.shouldAmountAvg = shouldAmountAvg;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public double getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }
}

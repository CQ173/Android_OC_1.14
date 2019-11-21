package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */

public class FinancialOverview {


    /**
     * code : 0
     * msg : 请求成功
     * size : 5
     * data : {"dayArr":[{"type":"deductions","count":2,"sum":3.34,"standby2":"1","sumString":"3.34"},{"type":"payment","count":0,"sum":0,"standby2":"0","sumString":"0.00"},{"type":"enchash","count":0,"sum":0,"standby2":"0","sumString":"0.00"},{"type":"depositary","count":0,"sum":0,"standby2":"0","sumString":"0.00"},{"type":"helpbuy","count":0,"sum":0,"standby2":"0","sumString":"0.00"}],"monthArr":[{"type":"deductions","count":3,"sum":10000003,"standby2":"1","sumString":"10000003.34"},{"type":"enchash","count":8,"sum":828,"standby2":"1","sumString":"828.00"},{"type":"payment","count":6,"sum":53328,"standby2":"0","sumString":"53328.00"},{"type":"depositary","count":0,"sum":0,"standby2":"0","sumString":"0.00"},{"type":"helpbuy","count":0,"sum":0,"standby2":"0","sumString":"0.00"}]}
     */

    private String code;
    private String msg;
    private int size;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<DayArrBean> dayArr;
        private List<MonthArrBean> monthArr;

        public List<DayArrBean> getDayArr() {
            return dayArr;
        }

        public void setDayArr(List<DayArrBean> dayArr) {
            this.dayArr = dayArr;
        }

        public List<MonthArrBean> getMonthArr() {
            return monthArr;
        }

        public void setMonthArr(List<MonthArrBean> monthArr) {
            this.monthArr = monthArr;
        }

        public static class DayArrBean {
            /**
             * type : deductions
             * count : 2
             * sum : 3.34
             * standby2 : 1
             * sumString : 3.34
             */

            private String type;
            private int count;
//            private double sum;
            private String standby2;
            private String sumString;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

//            public double getSum() {
//                return sum;
//            }
//
//            public void setSum(double sum) {
//                this.sum = sum;
//            }

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

        public static class MonthArrBean {
            /**
             * type : deductions
             * count : 3
             * sum : 10000003
             * standby2 : 1
             * sumString : 10000003.34
             */

            private String type;
            private int count;
//            private int sum;
            private String standby2;
            private String sumString;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

//            public int getSum() {
//                return sum;
//            }
//
//            public void setSum(int sum) {
//                this.sum = sum;
//            }

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

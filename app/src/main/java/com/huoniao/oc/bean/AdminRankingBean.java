package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class AdminRankingBean {

    /**
     * code : 0
     * data : {"month":[{"agencyName":"耀群票务中心","officeCode":"gd190200311","shouldAmountString":"17769.77"},{"agencyName":"开福火车票代售处","officeCode":"gd190200521","shouldAmountString":"2.70"}],"week":[{"agencyName":"耀群票务中心","officeCode":"gd190200311","shouldAmountString":"17769.77"},{"agencyName":"开福火车票代售处","officeCode":"gd190200521","shouldAmountString":"2.70"}],"year":[{"agencyName":"耀群票务中心","officeCode":"gd190200311","shouldAmountString":"17769.92"},{"agencyName":"开福火车票代售处","officeCode":"gd190200521","shouldAmountString":"2.70"}]}
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
        private List<MonthBean> month;
        private List<WeekBean> week;
        private List<YearBean> year;

        public List<MonthBean> getMonth() {
            return month;
        }

        public void setMonth(List<MonthBean> month) {
            this.month = month;
        }

        public List<WeekBean> getWeek() {
            return week;
        }

        public void setWeek(List<WeekBean> week) {
            this.week = week;
        }

        public List<YearBean> getYear() {
            return year;
        }

        public void setYear(List<YearBean> year) {
            this.year = year;
        }

        public static class MonthBean {
            /**
             * agencyName : 耀群票务中心
             * officeCode : gd190200311
             * shouldAmountString : 17769.77
             */

            private String agencyName;
            private String officeCode;
            private String shouldAmountString;

            public String getAgencyName() {
                return agencyName;
            }

            public void setAgencyName(String agencyName) {
                this.agencyName = agencyName;
            }

            public String getOfficeCode() {
                return officeCode;
            }

            public void setOfficeCode(String officeCode) {
                this.officeCode = officeCode;
            }

            public String getShouldAmountString() {
                return shouldAmountString;
            }

            public void setShouldAmountString(String shouldAmountString) {
                this.shouldAmountString = shouldAmountString;
            }
        }

        public static class WeekBean {
            /**
             * agencyName : 耀群票务中心
             * officeCode : gd190200311
             * shouldAmountString : 17769.77
             */

            private String agencyName;
            private String officeCode;
            private String shouldAmountString;

            public String getAgencyName() {
                return agencyName;
            }

            public void setAgencyName(String agencyName) {
                this.agencyName = agencyName;
            }

            public String getOfficeCode() {
                return officeCode;
            }

            public void setOfficeCode(String officeCode) {
                this.officeCode = officeCode;
            }

            public String getShouldAmountString() {
                return shouldAmountString;
            }

            public void setShouldAmountString(String shouldAmountString) {
                this.shouldAmountString = shouldAmountString;
            }
        }

        public static class YearBean {
            /**
             * agencyName : 耀群票务中心
             * officeCode : gd190200311
             * shouldAmountString : 17769.92
             */

            private String agencyName;
            private String officeCode;
            private String shouldAmountString;

            public String getAgencyName() {
                return agencyName;
            }

            public void setAgencyName(String agencyName) {
                this.agencyName = agencyName;
            }

            public String getOfficeCode() {
                return officeCode;
            }

            public void setOfficeCode(String officeCode) {
                this.officeCode = officeCode;
            }

            public String getShouldAmountString() {
                return shouldAmountString;
            }

            public void setShouldAmountString(String shouldAmountString) {
                this.shouldAmountString = shouldAmountString;
            }
        }
    }
}

package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/2/15.
 */

public class RemittanceDetailsBean implements Serializable{


    /**
     * code : 0
     * data : {"list":[{"officeId":"edc034d92c5c4709b9a6b30025c6e28d","officeName":"广铁分局","officeType":"7","realPay":"0.00","realReceive":"0.00","shouldReceive":"0.00"}],"statistical":{"officeId":"27edba57c03b437da224fa9042cc138d","officeName":"铁路总局","officeType":"6","realPay":"0.00","realReceive":"0.00","shouldReceive":"0.00","size":1}}
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
         * list : [{"officeId":"edc034d92c5c4709b9a6b30025c6e28d","officeName":"广铁分局","officeType":"7","realPay":"0.00","realReceive":"0.00","shouldReceive":"0.00"}]
         * statistical : {"officeId":"27edba57c03b437da224fa9042cc138d","officeName":"铁路总局","officeType":"6","realPay":"0.00","realReceive":"0.00","shouldReceive":"0.00","size":1}
         */

        private StatisticalBean statistical;
        private List<ListBean> list;

        public StatisticalBean getStatistical() {
            return statistical;
        }

        public void setStatistical(StatisticalBean statistical) {
            this.statistical = statistical;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class StatisticalBean {
            /**
             * officeId : 27edba57c03b437da224fa9042cc138d
             * officeName : 铁路总局
             * officeType : 6
             * realPay : 0.00
             * realReceive : 0.00
             * shouldReceive : 0.00
             * size : 1
             */

            private String officeId;
            private String officeName;
            private String officeType;
            private String realPay;
            private String realReceive;
            private String shouldReceive;
            private int size;

            public String getOfficeId() {
                return officeId;
            }

            public void setOfficeId(String officeId) {
                this.officeId = officeId;
            }

            public String getOfficeName() {
                return officeName;
            }

            public void setOfficeName(String officeName) {
                this.officeName = officeName;
            }

            public String getOfficeType() {
                return officeType;
            }

            public void setOfficeType(String officeType) {
                this.officeType = officeType;
            }

            public String getRealPay() {
                return realPay;
            }

            public void setRealPay(String realPay) {
                this.realPay = realPay;
            }

            public String getRealReceive() {
                return realReceive;
            }

            public void setRealReceive(String realReceive) {
                this.realReceive = realReceive;
            }

            public String getShouldReceive() {
                return shouldReceive;
            }

            public void setShouldReceive(String shouldReceive) {
                this.shouldReceive = shouldReceive;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }
        }

        public static class ListBean implements Serializable{
            /**
             * officeId : edc034d92c5c4709b9a6b30025c6e28d
             * officeName : 广铁分局
             * officeType : 7
             * realPay : 0.00
             * realReceive : 0.00
             * shouldReceive : 0.00
             */

            private String officeId;
            private String officeName;
            private String officeType;
            private String realPay;
            private String realReceive;
            private String shouldReceive;

            public String getOfficeId() {
                return officeId;
            }

            public void setOfficeId(String officeId) {
                this.officeId = officeId;
            }

            public String getOfficeName() {
                return officeName;
            }

            public void setOfficeName(String officeName) {
                this.officeName = officeName;
            }

            public String getOfficeType() {
                return officeType;
            }

            public void setOfficeType(String officeType) {
                this.officeType = officeType;
            }

            public String getRealPay() {
                return realPay;
            }

            public void setRealPay(String realPay) {
                this.realPay = realPay;
            }

            public String getRealReceive() {
                return realReceive;
            }

            public void setRealReceive(String realReceive) {
                this.realReceive = realReceive;
            }

            public String getShouldReceive() {
                return shouldReceive;
            }

            public void setShouldReceive(String shouldReceive) {
                this.shouldReceive = shouldReceive;
            }
        }
    }
}

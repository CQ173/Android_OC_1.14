package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/6/4.
 */

public class StationStatisticsBean {

    /**
     * code : 0
     * msg : 请求成功
     * size : 0
     * data : {"statistical":{"beginDate":"2019-05-04","endDate":"2019-06-04","listCount":3,"agencyCountSum":0,"shouldAmountSum":"15329.00","payAmountSum":"4444.00"},"list":[{"dataCount":212,"ticketCount":1998,"agencyCount":0,"stationName":"长沙市火车站(测试专用)","parentName":"广州车务段-广铁分局","stationCode":"cstrain","officeParentNames":"广铁分局-广州车务段","shouldAmountString":"15329.00","payAmountString":"4444.00"}]}
     * next : -1
     */

    private String code;
    private String msg;
    private int size;
    private DataBean data;
    private int next;

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

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public static class DataBean {
        /**
         * statistical : {"beginDate":"2019-05-04","endDate":"2019-06-04","listCount":3,"agencyCountSum":0,"shouldAmountSum":"15329.00","payAmountSum":"4444.00"}
         * list : [{"dataCount":212,"ticketCount":1998,"agencyCount":0,"stationName":"长沙市火车站(测试专用)","parentName":"广州车务段-广铁分局","stationCode":"cstrain","officeParentNames":"广铁分局-广州车务段","shouldAmountString":"15329.00","payAmountString":"4444.00"}]
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
             * beginDate : 2019-05-04
             * endDate : 2019-06-04
             * listCount : 3
             * agencyCountSum : 0
             * shouldAmountSum : 15329.00
             * payAmountSum : 4444.00
             */

            private String beginDate;
            private String endDate;
            private int listCount;
            private int agencyCountSum;
            private String shouldAmountSum;
            private String payAmountSum;

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

            public int getListCount() {
                return listCount;
            }

            public void setListCount(int listCount) {
                this.listCount = listCount;
            }

            public int getAgencyCountSum() {
                return agencyCountSum;
            }

            public void setAgencyCountSum(int agencyCountSum) {
                this.agencyCountSum = agencyCountSum;
            }

            public String getShouldAmountSum() {
                return shouldAmountSum;
            }

            public void setShouldAmountSum(String shouldAmountSum) {
                this.shouldAmountSum = shouldAmountSum;
            }

            public String getPayAmountSum() {
                return payAmountSum;
            }

            public void setPayAmountSum(String payAmountSum) {
                this.payAmountSum = payAmountSum;
            }
        }

        public static class ListBean {
            /**
             * dataCount : 212
             * ticketCount : 1998
             * agencyCount : 0
             * stationName : 长沙市火车站(测试专用)
             * parentName : 广州车务段-广铁分局
             * stationCode : cstrain
             * officeParentNames : 广铁分局-广州车务段
             * shouldAmountString : 15329.00
             * payAmountString : 4444.00
             */

            private int dataCount;
            private int ticketCount;
            private int agencyCount;
            private String stationName;
            private String parentName;
            private String stationCode;
            private String officeParentNames;
            private String shouldAmountString;
            private String payAmountString;
            private String stationId;

            public String getStationId() {
                return stationId;
            }

            public void setStationId(String stationId) {
                this.stationId = stationId;
            }

            public int getDataCount() {
                return dataCount;
            }

            public void setDataCount(int dataCount) {
                this.dataCount = dataCount;
            }

            public int getTicketCount() {
                return ticketCount;
            }

            public void setTicketCount(int ticketCount) {
                this.ticketCount = ticketCount;
            }

            public int getAgencyCount() {
                return agencyCount;
            }

            public void setAgencyCount(int agencyCount) {
                this.agencyCount = agencyCount;
            }

            public String getStationName() {
                return stationName;
            }

            public void setStationName(String stationName) {
                this.stationName = stationName;
            }

            public String getParentName() {
                return parentName;
            }

            public void setParentName(String parentName) {
                this.parentName = parentName;
            }

            public String getStationCode() {
                return stationCode;
            }

            public void setStationCode(String stationCode) {
                this.stationCode = stationCode;
            }

            public String getOfficeParentNames() {
                return officeParentNames;
            }

            public void setOfficeParentNames(String officeParentNames) {
                this.officeParentNames = officeParentNames;
            }

            public String getShouldAmountString() {
                return shouldAmountString;
            }

            public void setShouldAmountString(String shouldAmountString) {
                this.shouldAmountString = shouldAmountString;
            }

            public String getPayAmountString() {
                return payAmountString;
            }

            public void setPayAmountString(String payAmountString) {
                this.payAmountString = payAmountString;
            }
        }
    }
}

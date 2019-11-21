package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/2/16.
 */

public class TotalIronImportBean implements Serializable{


    /**
     * code : 0
     * count : 6
     * data : {"list":[{"officeName":"ddd","officeParentNames":"广铁分局-广州车务段-长沙市火车站(测试专用)","shouldAmountString":"0.20","ticketCount":1},{"officeName":"变动代售点","officeParentNames":"广铁分局-广州车务段-广州南车站","shouldAmountString":"0.50","ticketCount":1},{"officeName":"测试代售点q","officeParentNames":"广铁分局-衡阳火车站1","shouldAmountString":"0.50","ticketCount":1},{"officeName":"ddd","officeParentNames":"广铁分局-广州车务段-长沙市火车站(测试专用)","shouldAmountString":"0.20","ticketCount":1},{"officeName":"ddd","officeParentNames":"广铁分局-广州车务段-长沙市火车站(测试专用)","shouldAmountString":"0.50","ticketCount":1},{"officeName":"ddd","officeParentNames":"广铁分局-广州车务段-长沙市火车站(测试专用)","shouldAmountString":"0.30","ticketCount":1}],"statistical":{"alreadySumString":"2.20","paymentSize":6,"shouldSumString":"2.20","ticketSum":6}}
     * msg : 请求成功
     * next : -1
     * size : 6
     */

    private String code;
    private int count;
    private DataBean data;
    private String msg;
    private int next;
    private int size;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static class DataBean {
        /**
         * list : [{"officeName":"ddd","officeParentNames":"广铁分局-广州车务段-长沙市火车站(测试专用)","shouldAmountString":"0.20","ticketCount":1},{"officeName":"变动代售点","officeParentNames":"广铁分局-广州车务段-广州南车站","shouldAmountString":"0.50","ticketCount":1},{"officeName":"测试代售点q","officeParentNames":"广铁分局-衡阳火车站1","shouldAmountString":"0.50","ticketCount":1},{"officeName":"ddd","officeParentNames":"广铁分局-广州车务段-长沙市火车站(测试专用)","shouldAmountString":"0.20","ticketCount":1},{"officeName":"ddd","officeParentNames":"广铁分局-广州车务段-长沙市火车站(测试专用)","shouldAmountString":"0.50","ticketCount":1},{"officeName":"ddd","officeParentNames":"广铁分局-广州车务段-长沙市火车站(测试专用)","shouldAmountString":"0.30","ticketCount":1}]
         * statistical : {"alreadySumString":"2.20","paymentSize":6,"shouldSumString":"2.20","ticketSum":6}
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
             * alreadySumString : 2.20
             * paymentSize : 6
             * shouldSumString : 2.20
             * ticketSum : 6
             */

            private String alreadySumString;
            private int paymentSize;
            private String shouldSumString;
            private int ticketSum;

            public String getAlreadySumString() {
                return alreadySumString;
            }

            public void setAlreadySumString(String alreadySumString) {
                this.alreadySumString = alreadySumString;
            }

            public int getPaymentSize() {
                return paymentSize;
            }

            public void setPaymentSize(int paymentSize) {
                this.paymentSize = paymentSize;
            }

            public String getShouldSumString() {
                return shouldSumString;
            }

            public void setShouldSumString(String shouldSumString) {
                this.shouldSumString = shouldSumString;
            }

            public int getTicketSum() {
                return ticketSum;
            }

            public void setTicketSum(int ticketSum) {
                this.ticketSum = ticketSum;
            }
        }

        public static class ListBean implements Serializable{
            /**
             * officeName : ddd
             * officeParentNames : 广铁分局-广州车务段-长沙市火车站(测试专用)
             * shouldAmountString : 0.20
             * ticketCount : 1
             */

            private String officeName;
            private String officeParentNames;
            private String shouldAmountString;
            private int ticketCount;

            public String getOfficeName() {
                return officeName;
            }

            public void setOfficeName(String officeName) {
                this.officeName = officeName;
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

            public int getTicketCount() {
                return ticketCount;
            }

            public void setTicketCount(int ticketCount) {
                this.ticketCount = ticketCount;
            }
        }
    }
}

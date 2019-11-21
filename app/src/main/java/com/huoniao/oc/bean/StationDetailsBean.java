package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/6/6.
 */

public class StationDetailsBean {


    /**
     * code : 0
     * msg : 请求成功
     * size : 0
     * data : {"statistical":{"beginDate":"2019-01-01","endDate":"2019-01-01","listCount":"0","ticketCountSum":"0","shouldAmountSum":"0.00","payAmountSum":"0.00"},"list":[]}
     * next : -1
     */

    private String code;
    private String msg;
    private int size;
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public static class DataBean {
        /**
         * statistical : {"beginDate":"2019-01-01","endDate":"2019-01-01","listCount":"0","ticketCountSum":"0","shouldAmountSum":"0.00","payAmountSum":"0.00"}
         * list : []
         */

        private StatisticalBean statistical;
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public StatisticalBean getStatistical() {
            return statistical;
        }

        public void setStatistical(StatisticalBean statistical) {
            this.statistical = statistical;
        }


        public static class StatisticalBean {
            /**
             * beginDate : 2019-01-01
             * endDate : 2019-01-01
             * listCount : 0
             * ticketCountSum : 0
             * shouldAmountSum : 0.00
             * payAmountSum : 0.00
             */

            private String listCount;
            private String ticketCountSum;
            private String shouldAmountSum;
            private String payAmountSum;

            public String getListCount() {
                return listCount;
            }

            public void setListCount(String listCount) {
                this.listCount = listCount;
            }

            public String getTicketCountSum() {
                return ticketCountSum;
            }

            public void setTicketCountSum(String ticketCountSum) {
                this.ticketCountSum = ticketCountSum;
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

            private String standby1;
            private String standby2;
            private String sum1String;
            private String sum2String;
            private String sum3String;
            private String sum4String;

            public String getSum4String() {
                return sum4String;
            }

            public void setSum4String(String sum4String) {
                this.sum4String = sum4String;
            }

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

            public String getSum1String() {
                return sum1String;
            }

            public void setSum1String(String sum1String) {
                this.sum1String = sum1String;
            }

            public String getSum2String() {
                return sum2String;
            }

            public void setSum2String(String sum2String) {
                this.sum2String = sum2String;
            }

            public String getSum3String() {
                return sum3String;
            }

            public void setSum3String(String sum3String) {
                this.sum3String = sum3String;
            }
        }

    }
}

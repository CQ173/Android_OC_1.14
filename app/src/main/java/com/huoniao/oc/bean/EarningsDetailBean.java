package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/26.
 */

public class EarningsDetailBean {


    /**
     * code : 0
     * data : {"earnings":["0.00","0.00","0.00","0.00","0.00","44440.00","0.00","100.00"],"map":{"beginDate":"2018-07-18","endDate":"2018-07-25","timeType":"date"},"result":"success","ticket":["0.00","0.00","0.00","0.00","0.00","8888.00","0.00","20.00"],"time":["2018-07-18","2018-07-19","2018-07-20","2018-07-21","2018-07-22","2018-07-23","2018-07-24","2018-07-25"]}
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
         * earnings : ["0.00","0.00","0.00","0.00","0.00","44440.00","0.00","100.00"]
         * map : {"beginDate":"2018-07-18","endDate":"2018-07-25","timeType":"date"}
         * result : success
         * ticket : ["0.00","0.00","0.00","0.00","0.00","8888.00","0.00","20.00"]
         * time : ["2018-07-18","2018-07-19","2018-07-20","2018-07-21","2018-07-22","2018-07-23","2018-07-24","2018-07-25"]
         */

        private MapBean map;
        private String result;
        private List<String> earnings;
        private List<String> ticket;
        private List<String> time;

        public MapBean getMap() {
            return map;
        }

        public void setMap(MapBean map) {
            this.map = map;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public List<String> getEarnings() {
            return earnings;
        }

        public void setEarnings(List<String> earnings) {
            this.earnings = earnings;
        }

        public List<String> getTicket() {
            return ticket;
        }

        public void setTicket(List<String> ticket) {
            this.ticket = ticket;
        }

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public static class MapBean {
            /**
             * beginDate : 2018-07-18
             * endDate : 2018-07-25
             * timeType : date
             */

            private String beginDate;
            private String endDate;
            private String timeType;

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

            public String getTimeType() {
                return timeType;
            }

            public void setTimeType(String timeType) {
                this.timeType = timeType;
            }
        }
    }
}

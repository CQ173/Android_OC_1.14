package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public class AdminConsolidateRecordBean implements Serializable {

    /**
     * code : 0
     * msg : 请求成功
     * count : 5
     * size : 3
     * data : [{"date":"2017-07-17 00:00:00","withholdStatus":"0","officeCode":"gd190200521","officeName":"开福火车票代售处","winNumber":"0521","agencyName":"开福火车票代售处","createDate":"2017-09-14 16:10:39","ticketCount":5,"agentName":"开福火车票代售处","branchName":"广铁集团","trunkName":"铁路总局","shouldAmountString":"1.50","alreadyAmountString":"1.50","railwayStationName":"火车南车站","railwayStationId":"gzntrain"},{"date":"2017-07-17 00:00:00","withholdStatus":"0","officeCode":"gd190200521","officeName":"开福火车票代售处","winNumber":"0521","agencyName":"开福火车票代售处","createDate":"2017-09-14 15:09:14","ticketCount":5,"agentName":"开福火车票代售处","branchName":"广铁集团","trunkName":"铁路总局","shouldAmountString":"1.20","alreadyAmountString":"1.20","railwayStationName":"火车南车站","railwayStationId":"gzntrain"},{"date":"2017-09-12 00:00:00","withholdStatus":"0","officeCode":"gd190200311","officeName":"耀群票务中心","winNumber":"0311","agencyName":"耀群票务中心","createDate":"2017-09-14 15:04:57","ticketCount":666,"agentName":"耀群票务中心","branchName":"长沙分局","trunkName":"铁路总局","shouldAmountString":"8881.00","alreadyAmountString":"8881.00","railwayStationName":"长沙车站","railwayStationId":"cstrain"},{"alreadySumString":"17772.47","shouldSumString":"17772.47","paymentSize":5,"ticketSum":1343}]
     * next : 2
     */

    private String code;
    private String msg;
    private int count;
    private int size;
    private int next;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * date : 2017-07-17 00:00:00
         * withholdStatus : 0
         * officeCode : gd190200521
         * officeName : 开福火车票代售处
         * winNumber : 0521
         * agencyName : 开福火车票代售处
         * createDate : 2017-09-14 16:10:39
         * ticketCount : 5
         * agentName : 开福火车票代售处
         * branchName : 广铁集团
         * trunkName : 铁路总局
         * shouldAmountString : 1.50
         * alreadyAmountString : 1.50
         * railwayStationName : 火车南车站
         * railwayStationId : gzntrain
         * alreadySumString : 17772.47
         * shouldSumString : 17772.47
         * paymentSize : 5
         * ticketSum : 1343
         */

        private String date;
        private String withholdStatus;
        private String officeCode;
        private String officeName;
        private String winNumber;
        private String agencyName;
        private String createDate;
        private int ticketCount;
        private String agentName;
        private String branchName;
        private String trunkName;
        private String shouldAmountString;
        private String alreadyAmountString;
        private String railwayStationName;
        private String railwayStationId;
        private String alreadySumString;
        private String shouldSumString;
        private int paymentSize;
        private int ticketSum;
        private String withholdStatusName;
        private String cwdNameString;

        public String getWithholdStatusName() {
            return withholdStatusName;
        }

        public void setWithholdStatusName(String withholdStatusName) {
            this.withholdStatusName = withholdStatusName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWithholdStatus() {
            return withholdStatus;
        }

        public void setWithholdStatus(String withholdStatus) {
            this.withholdStatus = withholdStatus;
        }

        public String getOfficeCode() {
            return officeCode;
        }

        public void setOfficeCode(String officeCode) {
            this.officeCode = officeCode;
        }

        public String getOfficeName() {
            return officeName;
        }

        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public String getWinNumber() {
            return winNumber;
        }

        public void setWinNumber(String winNumber) {
            this.winNumber = winNumber;
        }

        public String getAgencyName() {
            return agencyName;
        }

        public void setAgencyName(String agencyName) {
            this.agencyName = agencyName;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getTicketCount() {
            return ticketCount;
        }

        public void setTicketCount(int ticketCount) {
            this.ticketCount = ticketCount;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getTrunkName() {
            return trunkName;
        }

        public void setTrunkName(String trunkName) {
            this.trunkName = trunkName;
        }

        public String getShouldAmountString() {
            return shouldAmountString;
        }

        public void setShouldAmountString(String shouldAmountString) {
            this.shouldAmountString = shouldAmountString;
        }

        public String getAlreadyAmountString() {
            return alreadyAmountString;
        }

        public void setAlreadyAmountString(String alreadyAmountString) {
            this.alreadyAmountString = alreadyAmountString;
        }

        public String getRailwayStationName() {
            return railwayStationName;
        }

        public void setRailwayStationName(String railwayStationName) {
            this.railwayStationName = railwayStationName;
        }

        public String getRailwayStationId() {
            return railwayStationId;
        }

        public void setRailwayStationId(String railwayStationId) {
            this.railwayStationId = railwayStationId;
        }

        public String getAlreadySumString() {
            return alreadySumString;
        }

        public void setAlreadySumString(String alreadySumString) {
            this.alreadySumString = alreadySumString;
        }

        public String getShouldSumString() {
            return shouldSumString;
        }

        public void setShouldSumString(String shouldSumString) {
            this.shouldSumString = shouldSumString;
        }

        public int getPaymentSize() {
            return paymentSize;
        }

        public void setPaymentSize(int paymentSize) {
            this.paymentSize = paymentSize;
        }

        public int getTicketSum() {
            return ticketSum;
        }

        public void setTicketSum(int ticketSum) {
            this.ticketSum = ticketSum;
        }

        public String getCwdNameString() {
            return cwdNameString;
        }

        public void setCwdNameString(String cwdNameString) {
            this.cwdNameString = cwdNameString;
        }
    }
}

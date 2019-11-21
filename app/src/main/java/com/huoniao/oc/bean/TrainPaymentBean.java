package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */

public class TrainPaymentBean implements Serializable {


    /**
     * code : 0
     * data : [{"createDateEnd":"","createDateStart":"","createDateString":"2018-12-14 16:49:34","dataCount":1,"delFlag":"0","id":"866a1cac95914c27a60ecab1f892c797","ids":"","parentName":"广铁分局-铁路总局","payAmount":"","payAmountString":"0.00","payState":"0","payStateName":"待支付","payTime":"","payTimeString":"","payType":"1","paymentPayTemSumId":"","remarks":"","shouldAmount":0.9,"shouldAmountString":"0.90","stationCode":"cstrain","stationName":"长沙市火车站(测试专用)","ticketCount":2,"tradeNumber":"","tradeNumberString":"","updateDateEnd":"","updateDateStart":""}]
     * msg : 请求成功
     * next : -1
     * size : 1
     */

    private String code;
    private String msg;
    private int next;
    private int size;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * createDateEnd :
         * createDateStart :
         * createDateString : 2018-12-14 16:49:34
         * dataCount : 1
         * delFlag : 0
         * id : 866a1cac95914c27a60ecab1f892c797
         * ids :
         * parentName : 广铁分局-铁路总局
         * payAmount :
         * payAmountString : 0.00
         * payState : 0
         * payStateName : 待支付
         * payTime :
         * payTimeString :
         * payType : 1
         * paymentPayTemSumId :
         * remarks :
         * shouldAmount : 0.9
         * shouldAmountString : 0.90
         * stationCode : cstrain
         * stationName : 长沙市火车站(测试专用)
         * ticketCount : 2
         * tradeNumber :
         * tradeNumberString :
         * updateDateEnd :
         * updateDateStart :
         */

        private String createDateEnd;
        private String createDateStart;
        private String createDateString;
        private int dataCount;
        private String delFlag;
        private String id;
        private String ids;
        private String parentName;
        private String payAmount;
        private String payAmountString;
        private String payState;
        private String payStateName;
        private String payTime;
        private String payTimeString;
        private String payType;
        private String paymentPayTemSumId;
        private String remarks;
        private double shouldAmount;
        private String shouldAmountString;
        private String stationCode;
        private String stationName;
        private int ticketCount;
        private String tradeNumber;
        private String tradeNumberString;
        private String updateDateEnd;
        private String updateDateStart;

        public String getCreateDateEnd() {
            return createDateEnd;
        }

        public void setCreateDateEnd(String createDateEnd) {
            this.createDateEnd = createDateEnd;
        }

        public String getCreateDateStart() {
            return createDateStart;
        }

        public void setCreateDateStart(String createDateStart) {
            this.createDateStart = createDateStart;
        }

        public String getCreateDateString() {
            return createDateString;
        }

        public void setCreateDateString(String createDateString) {
            this.createDateString = createDateString;
        }

        public int getDataCount() {
            return dataCount;
        }

        public void setDataCount(int dataCount) {
            this.dataCount = dataCount;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getPayAmountString() {
            return payAmountString;
        }

        public void setPayAmountString(String payAmountString) {
            this.payAmountString = payAmountString;
        }

        public String getPayState() {
            return payState;
        }

        public void setPayState(String payState) {
            this.payState = payState;
        }

        public String getPayStateName() {
            return payStateName;
        }

        public void setPayStateName(String payStateName) {
            this.payStateName = payStateName;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getPayTimeString() {
            return payTimeString;
        }

        public void setPayTimeString(String payTimeString) {
            this.payTimeString = payTimeString;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getPaymentPayTemSumId() {
            return paymentPayTemSumId;
        }

        public void setPaymentPayTemSumId(String paymentPayTemSumId) {
            this.paymentPayTemSumId = paymentPayTemSumId;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public double getShouldAmount() {
            return shouldAmount;
        }

        public void setShouldAmount(double shouldAmount) {
            this.shouldAmount = shouldAmount;
        }

        public String getShouldAmountString() {
            return shouldAmountString;
        }

        public void setShouldAmountString(String shouldAmountString) {
            this.shouldAmountString = shouldAmountString;
        }

        public String getStationCode() {
            return stationCode;
        }

        public void setStationCode(String stationCode) {
            this.stationCode = stationCode;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public int getTicketCount() {
            return ticketCount;
        }

        public void setTicketCount(int ticketCount) {
            this.ticketCount = ticketCount;
        }

        public String getTradeNumber() {
            return tradeNumber;
        }

        public void setTradeNumber(String tradeNumber) {
            this.tradeNumber = tradeNumber;
        }

        public String getTradeNumberString() {
            return tradeNumberString;
        }

        public void setTradeNumberString(String tradeNumberString) {
            this.tradeNumberString = tradeNumberString;
        }

        public String getUpdateDateEnd() {
            return updateDateEnd;
        }

        public void setUpdateDateEnd(String updateDateEnd) {
            this.updateDateEnd = updateDateEnd;
        }

        public String getUpdateDateStart() {
            return updateDateStart;
        }

        public void setUpdateDateStart(String updateDateStart) {
            this.updateDateStart = updateDateStart;
        }
    }
}

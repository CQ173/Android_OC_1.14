package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class PayWarningBean implements Serializable {


    /**
     * code : 0
     * data : [{"averageAmount":"123","averagePrice":"123.00","averagePriceString":"123.00","conditions":"bbbb","createDate":"2017-11-07 14:55:34","createDateString":"2017年11月07日 14:55:34","delFlag":"0","handleName":"超级管理员","id":"1","instructions":"1222222222222222222222222222222222222222222222222","loginName":"hn1807310000","paymentAmount":"89.00","state":"0","stateName":"异常","stationName":"长沙市火车站(测试专用)","ticketCount":79,"updateDate":"2017-11-15 09:59:06","updateDateString":"2017年11月15日 09:59:06","winNumber":"0001"}]
     * msg : 请求成功
     * size : 1
     */

    private String code;
    private String msg;
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

    public static class DataBean {
        /**
         * averageAmount : 123
         * averagePrice : 123.00
         * averagePriceString : 123.00
         * conditions : bbbb
         * createDate : 2017-11-07 14:55:34
         * createDateString : 2017年11月07日 14:55:34
         * delFlag : 0
         * handleName : 超级管理员
         * id : 1
         * instructions : 1222222222222222222222222222222222222222222222222
         * loginName : hn1807310000
         * paymentAmount : 89.00
         * state : 0
         * stateName : 异常
         * stationName : 长沙市火车站(测试专用)
         * ticketCount : 79
         * updateDate : 2017-11-15 09:59:06
         * updateDateString : 2017年11月15日 09:59:06
         * winNumber : 0001
         */

        private String averageAmount;
        private String averagePrice;
        private String averagePriceString;
        private String conditions;
        private String createDate;
        private String createDateString;
        private String delFlag;
        private String handleName;
        private String id;
        private String instructions;
        private String loginName;
        private String paymentAmount;
        private String state;
        private String stateName;
        private String stationName;
        private int ticketCount;
        private String updateDate;
        private String updateDateString;
        private String winNumber;

        public String getAverageAmount() {
            return averageAmount;
        }

        public void setAverageAmount(String averageAmount) {
            this.averageAmount = averageAmount;
        }

        public String getAveragePrice() {
            return averagePrice;
        }

        public void setAveragePrice(String averagePrice) {
            this.averagePrice = averagePrice;
        }

        public String getAveragePriceString() {
            return averagePriceString;
        }

        public void setAveragePriceString(String averagePriceString) {
            this.averagePriceString = averagePriceString;
        }

        public String getConditions() {
            return conditions;
        }

        public void setConditions(String conditions) {
            this.conditions = conditions;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCreateDateString() {
            return createDateString;
        }

        public void setCreateDateString(String createDateString) {
            this.createDateString = createDateString;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getHandleName() {
            return handleName;
        }

        public void setHandleName(String handleName) {
            this.handleName = handleName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(String paymentAmount) {
            this.paymentAmount = paymentAmount;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
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

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getUpdateDateString() {
            return updateDateString;
        }

        public void setUpdateDateString(String updateDateString) {
            this.updateDateString = updateDateString;
        }

        public String getWinNumber() {
            return winNumber;
        }

        public void setWinNumber(String winNumber) {
            this.winNumber = winNumber;
        }
    }
}

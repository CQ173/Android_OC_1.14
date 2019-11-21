package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class LateFeeBean implements Serializable {


    /**
     * code : 0
     * data : [{"agencyCode":"hn1807310000","applyReason":"ddd","createByName":"长沙市火车站","createDateEnd":"","createDateStart":"","createDateString":"2018-12-25 10:53:31","days":"2018-12-06, 2018-12-07","daysCount":2,"daysList":["2018-12-06"," 2018-12-07"],"feeString":"222.00","id":"78ca0c7bac8046cca3b46b1970f9d57c","ids":"","instructionSrc":"/userfiles/hytrain/images/e9d50570657e7d5c841369973978473d.jpg","isCanHandle":false,"operatorName":"朱峰","refuseReason":"","remarks":"","state":"1","stateName":"处理中","updateDateEnd":"","updateDateStart":""},{"agencyCode":"hn1807310000","applyReason":"导入数据出错（重复、遗漏等）,","createByName":"长沙市火车站","createDateEnd":"","createDateStart":"","createDateString":"2018-12-25 10:40:16","days":"2018-12-05, 2018-12-07, 2018-12-08","daysCount":3,"daysList":["2018-12-05"," 2018-12-07"," 2018-12-08"],"feeString":"111.00","id":"ad83595e4e18462a8992cdc31830f766","ids":"","instructionSrc":"/userfiles/hytrain/images/a9c5c57b4e4757abcf33e367b700d639.jpg","isCanHandle":false,"operatorName":"朱峰","refuseReason":"","remarks":"","state":"2","stateName":"处理成功","updateDateEnd":"","updateDateStart":""}]
     * msg : 请求成功
     * next : 2
     * size : 7
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

    public static class DataBean implements Serializable {
        /**
         * agencyCode : hn1807310000
         * applyReason : ddd
         * createByName : 长沙市火车站
         * createDateEnd :
         * createDateStart :
         * createDateString : 2018-12-25 10:53:31
         * days : 2018-12-06, 2018-12-07
         * daysCount : 2
         * daysList : ["2018-12-06"," 2018-12-07"]
         * feeString : 222.00
         * id : 78ca0c7bac8046cca3b46b1970f9d57c
         * ids :
         * instructionSrc : /userfiles/hytrain/images/e9d50570657e7d5c841369973978473d.jpg
         * isCanHandle : false
         * operatorName : 朱峰
         * refuseReason :
         * remarks :
         * state : 1
         * stateName : 处理中
         * updateDateEnd :
         * updateDateStart :
         */

        private String agencyCode;
        private String applyReason;
        private String createByName;
        private String createDateEnd;
        private String createDateStart;
        private String createDateString;
        private String days;
        private int daysCount;
        private String feeString;
        private String id;
        private String ids;
        private String instructionSrc;
        private boolean isCanHandle;
        private String operatorName;
        private String refuseReason;
        private String remarks;
        private String state;
        private String stateName;
        private String updateDateEnd;
        private String updateDateStart;
        private List<String> daysList;

        public String getAgencyCode() {
            return agencyCode;
        }

        public void setAgencyCode(String agencyCode) {
            this.agencyCode = agencyCode;
        }

        public String getApplyReason() {
            return applyReason;
        }

        public void setApplyReason(String applyReason) {
            this.applyReason = applyReason;
        }

        public String getCreateByName() {
            return createByName;
        }

        public void setCreateByName(String createByName) {
            this.createByName = createByName;
        }

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

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public int getDaysCount() {
            return daysCount;
        }

        public void setDaysCount(int daysCount) {
            this.daysCount = daysCount;
        }

        public String getFeeString() {
            return feeString;
        }

        public void setFeeString(String feeString) {
            this.feeString = feeString;
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

        public String getInstructionSrc() {
            return instructionSrc;
        }

        public void setInstructionSrc(String instructionSrc) {
            this.instructionSrc = instructionSrc;
        }

        public boolean isIsCanHandle() {
            return isCanHandle;
        }

        public void setIsCanHandle(boolean isCanHandle) {
            this.isCanHandle = isCanHandle;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getRefuseReason() {
            return refuseReason;
        }

        public void setRefuseReason(String refuseReason) {
            this.refuseReason = refuseReason;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
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

        public List<String> getDaysList() {
            return daysList;
        }

        public void setDaysList(List<String> daysList) {
            this.daysList = daysList;
        }
    }
}

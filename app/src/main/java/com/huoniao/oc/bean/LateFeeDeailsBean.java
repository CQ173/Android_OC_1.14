package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/12/25.
 */

public class LateFeeDeailsBean implements Serializable {


    /**
     * code : 0
     * data : {"agencyCode":"hn1807310000","applyReason":"ddd","currentRole":"铁路总局","daysString":"2018-12-06, 2018-12-07","fee":"222.00","id":"78ca0c7bac8046cca3b46b1970f9d57c","instructionSrc":"/userfiles/hytrain/images/e9d50570657e7d5c841369973978473d.jpg","isCanHandle":false,"operatorName":"朱峰","processRecordList":[{"auditStateName":"通过","auditUserName":"广铁分局","createDate":"2018-12-25 14:00:03"}],"refuseReason":"","state":"1","stateName":"处理中"}
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

    public static class DataBean implements Serializable {
        /**
         * agencyCode : hn1807310000
         * applyReason : ddd
         * currentRole : 铁路总局
         * daysString : 2018-12-06, 2018-12-07
         * fee : 222.00
         * id : 78ca0c7bac8046cca3b46b1970f9d57c
         * instructionSrc : /userfiles/hytrain/images/e9d50570657e7d5c841369973978473d.jpg
         * isCanHandle : false
         * operatorName : 朱峰
         * processRecordList : [{"auditStateName":"通过","auditUserName":"广铁分局","createDate":"2018-12-25 14:00:03"}]
         * refuseReason :
         * state : 1
         * stateName : 处理中
         */

        private String agencyCode;
        private String applyReason;
        private String currentRole;
        private String daysString;
        private String fee;
        private String id;
        private String instructionSrc;
        private boolean isCanHandle;
        private String operatorName;
        private String refuseReason;
        private String state;
        private String stateName;
        private List<ProcessRecordListBean> processRecordList;

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

        public String getCurrentRole() {
            return currentRole;
        }

        public void setCurrentRole(String currentRole) {
            this.currentRole = currentRole;
        }

        public String getDaysString() {
            return daysString;
        }

        public void setDaysString(String daysString) {
            this.daysString = daysString;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public List<ProcessRecordListBean> getProcessRecordList() {
            return processRecordList;
        }

        public void setProcessRecordList(List<ProcessRecordListBean> processRecordList) {
            this.processRecordList = processRecordList;
        }

        public static class ProcessRecordListBean implements Serializable {
            /**
             * auditStateName : 通过
             * auditUserName : 广铁分局
             * createDate : 2018-12-25 14:00:03
             */

            private String auditStateName;
            private String auditUserName;
            private String createDate;

            public String getAuditStateName() {
                return auditStateName;
            }

            public void setAuditStateName(String auditStateName) {
                this.auditStateName = auditStateName;
            }

            public String getAuditUserName() {
                return auditUserName;
            }

            public void setAuditUserName(String auditUserName) {
                this.auditUserName = auditUserName;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }
        }
    }
}

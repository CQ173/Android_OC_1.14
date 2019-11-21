package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/14.
 */

public class NewPayWarningBean implements Serializable {


    /**
     * code : 0
     * data : [{"alarmNumber":"2018111400021793","content":"截止2018-11-14 16:27:01，该用户欠缴票款1.00元，已超过其车站押金1000.00的一半。","createDateString":"2018-11-14 16:27:01","handleDate":"","handleDateString":"","handleResult":"","handleState":"0","handleStateName":"未处理","handleUserName":"","id":"180fae38fc9b49b4b88911f6c315cae8","ids":"","parentName":"长沙市火车站(测试专用)","state":"0","stateName":"未恢复","triggerUserName":"11","type":"1","typeName":"欠缴越界","winNumber":"0888"},{"alarmNumber":"2018111400021794","content":"截止2018-11-14 16:27:01，该代售点已连续13日未足额缴款。","createDateString":"2018-11-14 16:27:01","handleDate":"","handleDateString":"","handleResult":"","handleState":"0","handleStateName":"未处理","handleUserName":"","id":"aa5f57ec0f094da89d58b8478949278c","ids":"","parentName":"长沙市火车站(测试专用)","state":"0","stateName":"未恢复","triggerUserName":"11","type":"2","typeName":"连续欠缴","winNumber":"0888"}]
     * msg : 请求成功
     * next : 2
     * size : 95
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
         * alarmNumber : 2018111400021793
         * content : 截止2018-11-14 16:27:01，该用户欠缴票款1.00元，已超过其车站押金1000.00的一半。
         * createDateString : 2018-11-14 16:27:01
         * handleDate :
         * handleDateString :
         * handleResult :
         * handleState : 0
         * handleStateName : 未处理
         * handleUserName :
         * id : 180fae38fc9b49b4b88911f6c315cae8
         * ids :
         * parentName : 长沙市火车站(测试专用)
         * state : 0
         * stateName : 未恢复
         * triggerUserName : 11
         * type : 1
         * typeName : 欠缴越界
         * winNumber : 0888
         */

        private String alarmNumber;
        private String content;
        private String createDateString;
        private String handleDate;
        private String handleDateString;
        private String handleResult;
        private String handleState;
        private String handleStateName;
        private String handleUserName;
        private String id;
        private String ids;
        private String parentName;
        private String state;
        private String stateName;
        private String triggerUserName;
        private String type;
        private String typeName;
        private String winNumber;

        public String getAlarmNumber() {
            return alarmNumber;
        }

        public void setAlarmNumber(String alarmNumber) {
            this.alarmNumber = alarmNumber;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateDateString() {
            return createDateString;
        }

        public void setCreateDateString(String createDateString) {
            this.createDateString = createDateString;
        }

        public String getHandleDate() {
            return handleDate;
        }

        public void setHandleDate(String handleDate) {
            this.handleDate = handleDate;
        }

        public String getHandleDateString() {
            return handleDateString;
        }

        public void setHandleDateString(String handleDateString) {
            this.handleDateString = handleDateString;
        }

        public String getHandleResult() {
            return handleResult;
        }

        public void setHandleResult(String handleResult) {
            this.handleResult = handleResult;
        }

        public String getHandleState() {
            return handleState;
        }

        public void setHandleState(String handleState) {
            this.handleState = handleState;
        }

        public String getHandleStateName() {
            return handleStateName;
        }

        public void setHandleStateName(String handleStateName) {
            this.handleStateName = handleStateName;
        }

        public String getHandleUserName() {
            return handleUserName;
        }

        public void setHandleUserName(String handleUserName) {
            this.handleUserName = handleUserName;
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

        public String getTriggerUserName() {
            return triggerUserName;
        }

        public void setTriggerUserName(String triggerUserName) {
            this.triggerUserName = triggerUserName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getWinNumber() {
            return winNumber;
        }

        public void setWinNumber(String winNumber) {
            this.winNumber = winNumber;
        }
    }
}

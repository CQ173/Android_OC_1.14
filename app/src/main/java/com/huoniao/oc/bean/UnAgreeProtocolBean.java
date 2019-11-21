package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/9/3.
 */

public class UnAgreeProtocolBean implements Serializable {


    /**
     * code : 0
     * data : [{"createDate":"2018-08-31 16:06:52","id":"19dc5ebe7eb748f49d9e2bef5bb4fc19","isUsed":"1","isUsedName":"是","name":"用户使用协议","role":"5","type":"1","url":"/userfiles/protocol/html/d04c291ed4970e8344c2f509bc3fcf5b.html","version":7}]
     * msg : 请求成功
     */

    private String code;
    private String msg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * createDate : 2018-08-31 16:06:52
         * id : 19dc5ebe7eb748f49d9e2bef5bb4fc19
         * isUsed : 1
         * isUsedName : 是
         * name : 用户使用协议
         * role : 5
         * type : 1
         * url : /userfiles/protocol/html/d04c291ed4970e8344c2f509bc3fcf5b.html
         * version : 7
         */

        private String createDate;
        private String id;
        private String isUsed;
        private String isUsedName;
        private String name;
        private String role;
        private String type;
        private String url;
        private int version;
        private boolean checkState;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsUsed() {
            return isUsed;
        }

        public void setIsUsed(String isUsed) {
            this.isUsed = isUsed;
        }

        public String getIsUsedName() {
            return isUsedName;
        }

        public void setIsUsedName(String isUsedName) {
            this.isUsedName = isUsedName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public boolean isCheckState() {
            return checkState;
        }

        public void setCheckState(boolean checkState) {
            this.checkState = checkState;
        }
    }
}

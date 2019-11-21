package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class AdminConsolidateStateBean {

    /**
     * code : 0
     * msg : 请求成功
     * size : 5
     * data : [{"updateDate":"2017-05-19 15:14:28","delFlag":"0","id":"99","label":"已完成","value":"0","type":"bf_payment_status","description":"汇缴扣款状态","sort":20},{"delFlag":"0","id":"101","label":"待扣款","value":"1","type":"bf_payment_status","description":"汇缴扣款状态","sort":30},{"updateDate":"2017-05-19 15:14:18","delFlag":"0","id":"100","label":"异常","value":"2","type":"bf_payment_status","description":"汇缴扣款状态","sort":10},{"createDate":"2017-05-19 15:14:53","updateDate":"2017-05-19 15:14:53","delFlag":"0","id":"2c8907b5d1314c20ac1806f57b264ccb","label":"待充值","value":"3","type":"bf_payment_status","description":"汇缴扣款状态","sort":40},{"createDate":"2017-05-19 15:15:16","updateDate":"2017-05-19 15:15:16","delFlag":"0","id":"9b34fae3691f49c3bd7fb4aa777e07cd","label":"补缴成功","value":"4","type":"bf_payment_status","description":"汇缴扣款状态","sort":50}]
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
         * updateDate : 2017-05-19 15:14:28
         * delFlag : 0
         * id : 99
         * label : 已完成
         * value : 0
         * type : bf_payment_status
         * description : 汇缴扣款状态
         * sort : 20
         * createDate : 2017-05-19 15:14:53
         */

        private String updateDate;
        private String delFlag;
        private String id;
        private String label;
        private String value;
        private String type;
        private String description;
        private int sort;
        private String createDate;

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
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

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}

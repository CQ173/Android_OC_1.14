package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18.
 */

public class DictAryBean implements Serializable {


    /**
     * code : 0
     * data : [{"createDate":"2018-07-04 14:42:31","delFlag":"0","description":"滞纳金返还状态","id":"ec92dd69fbe24580ba6f0be50d46cea1","label":"递交中","sort":10,"type":"fb_latefee_state","updateDate":"2018-07-04 14:42:31","value":"0"},{"createDate":"2018-07-04 14:42:38","delFlag":"0","description":"滞纳金返还状态","id":"2afbe7c29f3948f7b9ede4945f163ad2","label":"处理中","sort":20,"type":"fb_latefee_state","updateDate":"2018-07-04 14:42:38","value":"1"},{"createDate":"2018-07-04 14:42:45","delFlag":"0","description":"滞纳金返还状态","id":"10f952975bdc401da6445fe7454c814a","label":"处理成功","sort":30,"type":"fb_latefee_state","updateDate":"2018-07-04 14:43:10","value":"2"},{"createDate":"2018-07-04 14:42:51","delFlag":"0","description":"滞纳金返还状态","id":"25450db7b8a64a27b17aa5792b867ad7","label":"处理失败","sort":40,"type":"fb_latefee_state","updateDate":"2018-07-04 14:43:13","value":"3"},{"createDate":"2018-07-04 14:43:05","delFlag":"0","description":"滞纳金返还状态","id":"20d4a8c1fab6413dade7bf2f9082d913","label":"已完结","sort":50,"type":"fb_latefee_state","updateDate":"2018-07-04 14:43:16","value":"4"}]
     * msg : 请求成功
     * size : 5
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
         * createDate : 2018-07-04 14:42:31
         * delFlag : 0
         * description : 滞纳金返还状态
         * id : ec92dd69fbe24580ba6f0be50d46cea1
         * label : 递交中
         * sort : 10
         * type : fb_latefee_state
         * updateDate : 2018-07-04 14:42:31
         * value : 0
         */

        private String createDate;
        private String delFlag;
        private String description;
        private String id;
        private String label;
        private int sort;
        private String type;
        private String updateDate;
        private String value;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

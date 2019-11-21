package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */

public class TypeOfInstitutionBean {

    /**
     * code : 0
     * msg : 请求成功
     * size : 7
     * data : [{"createDate":"2016-09-18 11:26:03","updateDate":"2016-09-18 11:26:10","delFlag":"0","id":"5ca987bd59e44b4e8139f1678d4d9c45","label":"个人","value":"1","type":"sys_office_type","description":"机构类型","sort":50},{"remarks":"2013/5/27","updateDate":"2016-09-13 09:08:22","delFlag":"0","id":"21","label":"火车站","value":"2","type":"sys_office_type","description":"机构类型","sort":60},{"remarks":"2013/5/27","updateDate":"2016-09-14 09:14:48","delFlag":"0","id":"22","label":"代售点","value":"3","type":"sys_office_type","description":"机构类型","sort":70},{"createDate":"2016-11-10 15:32:19","updateDate":"2016-11-10 16:47:00","delFlag":"0","id":"c2651742f5884e5691d63710eff429af","label":"证券公司","value":"4","type":"sys_office_type","description":"机构类型","sort":80},{"createDate":"2016-12-30 13:33:49","updateDate":"2016-12-30 13:34:31","delFlag":"0","id":"59c109bc85ae4897b3a288c744d040ee","label":"商户","value":"5","type":"sys_office_type","description":"机构类型","sort":90},{"createDate":"2017-06-28 11:31:11","updateDate":"2017-06-28 11:31:14","delFlag":"0","id":"871","label":"铁路总局","value":"6","type":"sys_office_type","description":"机构类型","sort":100},{"createDate":"2017-06-28 11:35:23","updateDate":"2017-06-28 11:35:25","delFlag":"0","id":"872","label":"铁路分局","value":"7","type":"sys_office_type","description":"机构类型","sort":110}]
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
         * createDate : 2016-09-18 11:26:03
         * updateDate : 2016-09-18 11:26:10
         * delFlag : 0
         * id : 5ca987bd59e44b4e8139f1678d4d9c45
         * label : 个人
         * value : 1
         * type : sys_office_type
         * description : 机构类型
         * sort : 50
         * remarks : 2013/5/27
         */

        private String createDate;
        private String updateDate;
        private String delFlag;
        private String id;
        private String label;
        private String value;
        private String type;
        private String description;
        private int sort;
        private String remarks;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

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

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}

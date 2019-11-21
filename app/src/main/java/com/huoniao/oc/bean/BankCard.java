package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/12.
 */

public class BankCard {

    /**
     * code : 0
     * data : [{"createDate":"2017-04-14 19:33:46","delFlag":"0","description":"银行编码","id":"29976a83099647b4895fba2fbe22a2c3","label":"中信银行","sort":5,"type":"acct_bank_code","updateDate":"2017-04-14 19:33:46","value":"ECITIC"}]
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
         * createDate : 2017-04-14 19:33:46
         * delFlag : 0
         * description : 银行编码
         * id : 29976a83099647b4895fba2fbe22a2c3
         * label : 中信银行
         * sort : 5
         * type : acct_bank_code
         * updateDate : 2017-04-14 19:33:46
         * value : ECITIC
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

package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CashBean {

    /**
     * code : 0
     * msg : 请求成功
     * size : 14
     * data : [{"createDate":"2016-11-14 11:21:18","updateDate":"2016-11-14 11:34:56","delFlag":"0","id":"b645258233c94aefa48824e72232aa83","label":"中国农业银行","value":"ABC","type":"acct_bank_code","description":"银行编码","sort":60},{"createDate":"2016-11-14 11:21:58","updateDate":"2016-11-14 11:35:11","delFlag":"0","id":"11647cade2844e91976380a4710262f8","label":"交通银行","value":"BCOM","type":"acct_bank_code","description":"银行编码","sort":90},{"createDate":"2016-11-14 11:24:36","updateDate":"2016-11-14 11:35:41","delFlag":"0","id":"46bd4782d5c74b32a28fff677925ad63","label":"北京银行","value":"BOB","type":"acct_bank_code","description":"银行编码","sort":130},{"createDate":"2016-11-14 11:19:24","updateDate":"2016-11-14 11:19:24","delFlag":"0","id":"1fc09fb569454b37ab58c81aa121bbd7","label":"中国银行","value":"BOC","type":"acct_bank_code","description":"银行编码","sort":10},{"createDate":"2016-11-14 11:21:00","updateDate":"2016-11-14 11:34:50","delFlag":"0","id":"9b22db17ab3144f5a8cc4162f61e2c29","label":"中国建设银行","value":"CCB","type":"acct_bank_code","description":"银行编码","sort":50},{"createDate":"2016-11-14 11:22:37","updateDate":"2016-11-14 11:35:31","delFlag":"0","id":"20017cc9b20a4f5a95e58f2364a6d573","label":"中国光大银行","value":"CEB","type":"acct_bank_code","description":"银行编码","sort":110},{"createDate":"2016-11-14 11:20:48","updateDate":"2016-11-14 11:34:44","delFlag":"0","id":"2d966c01cd36440783fa8c3c597a0b47","label":"招商银行","value":"CMB","type":"acct_bank_code","description":"银行编码","sort":40},{"createDate":"2016-11-14 11:20:04","updateDate":"2016-11-14 11:20:04","delFlag":"0","id":"751d3e001cb94660882a02791c21478a","label":"中国民生银行","value":"CMBC","type":"acct_bank_code","description":"银行编码","sort":30},{"createDate":"2017-04-14 19:39:43","updateDate":"2017-04-14 19:39:51","delFlag":"0","id":"5d86ed016c7645d2a74546592eea3727","label":"中信银行","value":"ECITIC","type":"acct_bank_code","description":"银行编码","sort":5},{"createDate":"2016-11-14 11:22:08","updateDate":"2016-11-14 11:35:15","delFlag":"0","id":"38bb6fddac2840fd9c6c12bae24d4f93","label":"华夏银行","value":"HXB","type":"acct_bank_code","description":"银行编码","sort":100},{"createDate":"2016-11-14 11:21:44","updateDate":"2016-11-14 11:35:06","delFlag":"0","id":"724b84fd092c4f6cbfc1c03519b7ce5c","label":"中国工商银行","value":"ICBC","type":"acct_bank_code","description":"银行编码","sort":80},{"createDate":"2016-11-14 11:23:24","updateDate":"2016-11-14 11:35:36","delFlag":"0","id":"a2e57914568e4590be7fd374cfe69498","label":"南京银行","value":"NJCB","type":"acct_bank_code","description":"银行编码","sort":120},{"createDate":"2016-11-14 11:21:34","updateDate":"2016-11-14 11:35:02","delFlag":"0","id":"929511116f88414182443fd4100ff29a","label":"中国邮政储蓄银行","value":"POST","type":"acct_bank_code","description":"银行编码","sort":70},{"createDate":"2016-11-14 11:19:51","updateDate":"2016-11-14 11:19:51","delFlag":"0","id":"9631671315a34aacb3444b17917e181f","label":"浦发银行","value":"SPDB","type":"acct_bank_code","description":"银行编码","sort":20}]
     */

    private String code;
    private String msg;
    private int size;
    /**
     * createDate : 2016-11-14 11:21:18
     * updateDate : 2016-11-14 11:34:56
     * delFlag : 0
     * id : b645258233c94aefa48824e72232aa83
     * label : 中国农业银行
     * value : ABC
     * type : acct_bank_code
     * description : 银行编码
     * sort : 60
     */

    private List<DataEntity> data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getSize() {
        return size;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String createDate;
        private String updateDate;
        private String delFlag;
        private String id;
        private String label;
        private String value;
        private String type;
        private String description;
        private int sort;

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public void setDelFlag(String delFlag) {
            this.delFlag = delFlag;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getCreateDate() {
            return createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public String getDelFlag() {
            return delFlag;
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public int getSort() {
            return sort;
        }
    }
}

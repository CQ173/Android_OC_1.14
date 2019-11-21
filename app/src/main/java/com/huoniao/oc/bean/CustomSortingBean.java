package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/6/5.
 */

public class CustomSortingBean {

    /**
     * code : 0
     * msg : 请求成功
     * data : [{"id":"7","type":"trainPayment","officeName":"广州南车站"},{"id":"8","type":"trainPayment","officeName":"衡阳火车站1"},{"id":"9","type":"trainPayment","officeName":"长沙市火车站(测试专用)"}]
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

    public static class DataBean {
        /**
         * id : 7
         * type : trainPayment
         * officeName : 广州南车站
         */

        private String id;
        private String type;
        private String officeName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOfficeName() {
            return officeName;
        }

        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }
    }
}

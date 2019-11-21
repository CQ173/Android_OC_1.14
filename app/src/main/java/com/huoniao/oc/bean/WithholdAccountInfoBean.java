package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class WithholdAccountInfoBean implements Serializable {

    /**
     * code : 0
     * data : [{"address":"湖南长沙市芙蓉区五一大道金禧0大楼","agent":"0","agentCompanyName":"天下第一企业","agentType":"0","area":{"admin":false,"code":"0731","id":"219","name":"长沙市"},"code":"hn1807310000","corpIdNum":"510824197602074290","corpMobile":"13687343431","corpName":"祝峰","geogPosition":"湖南长沙市芙蓉区五一大道金禧0大楼","id":"9de06e952eaf428daa28ec85bebb8fa9","jurisArea":{"admin":false,"code":"101","id":"101","name":"长沙段"},"lat":"28.200861","lng":"112.98854","master":"祝峰","name":"文武代售点","operatorIdNum":"510824197602074290","operatorMobile":"13687343431","operatorName":"祝峰","phone":"15874132251","state":"0","winNumber":"0000"}]
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
         * address : 湖南长沙市芙蓉区五一大道金禧0大楼
         * agent : 0
         * agentCompanyName : 天下第一企业
         * agentType : 0
         * area : {"admin":false,"code":"0731","id":"219","name":"长沙市"}
         * code : hn1807310000
         * corpIdNum : 510824197602074290
         * corpMobile : 13687343431
         * corpName : 祝峰
         * geogPosition : 湖南长沙市芙蓉区五一大道金禧0大楼
         * id : 9de06e952eaf428daa28ec85bebb8fa9
         * jurisArea : {"admin":false,"code":"101","id":"101","name":"长沙段"}
         * lat : 28.200861
         * lng : 112.98854
         * master : 祝峰
         * name : 文武代售点
         * operatorIdNum : 510824197602074290
         * operatorMobile : 13687343431
         * operatorName : 祝峰
         * phone : 15874132251
         * state : 0
         * winNumber : 0000
         */

        private String address;
        private String agent;
        private String agentCompanyName;
        private String agentType;
        private AreaBean area;
        private String code;
        private String corpIdNum;
        private String corpMobile;
        private String corpName;
        private String geogPosition;
        private String id;
        private JurisAreaBean jurisArea;
        private String lat;
        private String lng;
        private String master;
        private String name;
        private String operatorIdNum;
        private String operatorMobile;
        private String operatorName;
        private String phone;
        private String state;
        private String winNumber;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public String getAgentCompanyName() {
            return agentCompanyName;
        }

        public void setAgentCompanyName(String agentCompanyName) {
            this.agentCompanyName = agentCompanyName;
        }

        public String getAgentType() {
            return agentType;
        }

        public void setAgentType(String agentType) {
            this.agentType = agentType;
        }

        public AreaBean getArea() {
            return area;
        }

        public void setArea(AreaBean area) {
            this.area = area;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCorpIdNum() {
            return corpIdNum;
        }

        public void setCorpIdNum(String corpIdNum) {
            this.corpIdNum = corpIdNum;
        }

        public String getCorpMobile() {
            return corpMobile;
        }

        public void setCorpMobile(String corpMobile) {
            this.corpMobile = corpMobile;
        }

        public String getCorpName() {
            return corpName;
        }

        public void setCorpName(String corpName) {
            this.corpName = corpName;
        }

        public String getGeogPosition() {
            return geogPosition;
        }

        public void setGeogPosition(String geogPosition) {
            this.geogPosition = geogPosition;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public JurisAreaBean getJurisArea() {
            return jurisArea;
        }

        public void setJurisArea(JurisAreaBean jurisArea) {
            this.jurisArea = jurisArea;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOperatorIdNum() {
            return operatorIdNum;
        }

        public void setOperatorIdNum(String operatorIdNum) {
            this.operatorIdNum = operatorIdNum;
        }

        public String getOperatorMobile() {
            return operatorMobile;
        }

        public void setOperatorMobile(String operatorMobile) {
            this.operatorMobile = operatorMobile;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getWinNumber() {
            return winNumber;
        }

        public void setWinNumber(String winNumber) {
            this.winNumber = winNumber;
        }

        public static class AreaBean {
            /**
             * admin : false
             * code : 0731
             * id : 219
             * name : 长沙市
             */

            private boolean admin;
            private String code;
            private String id;
            private String name;

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class JurisAreaBean {
            /**
             * admin : false
             * code : 101
             * id : 101
             * name : 长沙段
             */

            private boolean admin;
            private String code;
            private String id;
            private String name;

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}

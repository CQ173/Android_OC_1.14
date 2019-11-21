package com.huoniao.oc.bean;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class AdminUserListBean {

    /**
     * code : 0
     * msg : 请求成功
     * size : 5
     * data : [{"remarks":"系统管理员","id":"2","loginName":"admin","name":"管理员","email":"admin@qq.com","phone":"8676","mobile":"13975817080a","minimum":0,"dynaMinimum":0,"userType":"1","auditState":"0","officeName":"O计平台","roleNames":"系统管理员"},{"remarks":"测试专用账号0000","createDate":"2016-12-14 09:52:12","id":"e6ec2d07dd33425a99b385b10f03e870","loginName":"hn1807310000","name":"祝峰","mobile":"13687343431","minimum":1000,"dynaMinimum":0,"userType":"2","auditState":"0","officeName":"零零零零代售点(测试专用)","roleNames":"代售点管理员"},{"createDate":"2017-05-20 22:27:27","id":"6896e410b6044d62a89692ff14d511eb","loginName":"cstrain","name":"长沙市火车站","mobile":"18964998784a","minimum":0,"dynaMinimum":0,"userType":"2","auditState":"0","photoSrc":"/userfiles/cstrain/images/6176ca855222c31a2048968494ce7ac.jpg","officeName":"长沙市火车站(测试专用)","roleNames":"火车站管理员"},{"createDate":"2017-05-23 11:08:42","id":"e9609544699648f19255f23ba32d30c6","loginName":"OCaccountant","name":"郭秋萍","mobile":"13319561957a","minimum":0,"dynaMinimum":0,"userType":"1","auditState":"0","officeName":"O计平台","roleNames":"会计"},{"createDate":"2017-05-23 11:11:22","id":"1e38314d45c8401ba670265d8e5a6b38","loginName":"OCcashier","name":"王莲枝","mobile":"18075127053a","minimum":0,"dynaMinimum":0,"userType":"1","auditState":"0","officeName":"O计平台","roleNames":"出纳"}]
     * next : -1
     */

    private String code;
    private String msg;
    private int size;
    private int next;
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

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * remarks : 系统管理员
         * id : 2
         * loginName : admin
         * name : 管理员
         * email : admin@qq.com
         * phone : 8676
         * mobile : 13975817080a
         * minimum : 0
         * dynaMinimum : 0
         * userType : 1
         * auditState : 0
         * officeName : O计平台
         * roleNames : 系统管理员
         * createDate : 2016-12-14 09:52:12
         * photoSrc : /userfiles/cstrain/images/6176ca855222c31a2048968494ce7ac.jpg
         */

        private String balanceString;
        private String remarks;
        private String id;
        private String loginName;
        private String name;
        private String email;
        private String phone;
        private String mobile;
        private double minimum;
        private int dynaMinimum;
        private String userType;
        private String auditState;
        private String officeName;
        private String roleNames;
        private String createDate;
        private String photoSrc;

        private File file;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getBalanceString() {
            return balanceString;
        }

        public void setBalanceString(String balanceString) {
            this.balanceString = balanceString;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public double getMinimum() {
            return minimum;
        }

        public void setMinimum(double minimum) {
            this.minimum = minimum;
        }

        public int getDynaMinimum() {
            return dynaMinimum;
        }

        public void setDynaMinimum(int dynaMinimum) {
            this.dynaMinimum = dynaMinimum;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getAuditState() {
            return auditState;
        }

        public void setAuditState(String auditState) {
            this.auditState = auditState;
        }

        public String getOfficeName() {
            return officeName;
        }

        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public String getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(String roleNames) {
            this.roleNames = roleNames;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getPhotoSrc() {
            return photoSrc;
        }

        public void setPhotoSrc(String photoSrc) {
            this.photoSrc = photoSrc;
        }
    }
}

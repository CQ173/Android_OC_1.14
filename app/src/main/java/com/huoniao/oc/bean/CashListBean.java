package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CashListBean {

    /**
     * code : 0
     * msg : 请求成功
     * size : 13
     * data : [{"createDate":"2017-05-18 16:07:21","updateDate":"2017-05-18 16:07:21","delFlag":"0","id":"f7eb241f7d5147ee9cbff750586bca23","bankCode":"CEB","cardType":"1","cardNo":"1234568254125412","custName":"123","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 04:15:47","updateDate":"2017-04-19 04:15:47","delFlag":"0","id":"3ae9d27f24494ad7947a0b70bf090478","bankCode":"ECITIC","cardType":"1","cardNo":"1234567890123456","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 04:15:38","updateDate":"2017-04-19 04:15:38","delFlag":"0","id":"1e043e4e70b340e282b49446fdcfce81","bankCode":"ECITIC","cardType":"2","cardNo":"1234567890123456","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 04:15:22","updateDate":"2017-04-19 04:15:22","delFlag":"0","id":"f89dae8db7964274865ce2752778f1e7","bankCode":"ECITIC","cardType":"1","cardNo":"1234567890123456","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 04:14:53","updateDate":"2017-04-19 04:14:53","delFlag":"0","id":"1a7bf50d99d24aed9daf468fc37c2eef","bankCode":"ECITIC","cardType":"1","cardNo":"1234567890123456789","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 04:14:40","updateDate":"2017-04-19 04:14:40","delFlag":"0","id":"948dae39105c420f983bbe9e0d501cfa","bankCode":"ECITIC","cardType":"1","cardNo":"1234567890123456","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 03:51:19","updateDate":"2017-04-19 03:51:19","delFlag":"0","id":"d0d5b521146948e399ffba9191ee8e2b","bankCode":"ECITIC","cardType":"1","cardNo":"6217002920120515365","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 03:50:49","updateDate":"2017-04-19 03:50:49","delFlag":"0","id":"9a95b83d97de45f5831e4edc894e7538","bankCode":"ECITIC","cardType":"1","cardNo":"6217003920125058333","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 00:45:49","updateDate":"2017-04-19 00:45:49","delFlag":"0","id":"93f87ea89a044da08ece7ed032a22dca","bankCode":"ECITIC","cardType":"1","cardNo":"8888888888888888888","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"createDate":"2017-04-19 00:42:51","updateDate":"2017-04-19 00:42:51","delFlag":"0","id":"40dba5a5a869419f893c81683ed73299","bankCode":"ECITIC","cardType":"1","cardNo":"6217684600152323333","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"updateDate":"2017-04-16 09:53:42","delFlag":"0","id":"4ad41cf738e547c4aa899f07ca40295a","bankCode":"ECITIC","cardType":"1","cardNo":"4566544566545555333","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"updateDate":"2017-04-19 03:52:53","delFlag":"0","id":"854ca462aa744c4e91342ce86cc58ee4","bankCode":"ECITIC","cardType":"2","cardNo":"6217002568251242","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}},{"updateDate":"2017-04-19 04:05:38","delFlag":"0","id":"c91ec5d813904180b95e8eed8e5298d8","bankCode":"ECITIC","cardType":"1","cardNo":"1234567890123456789","custName":"张三","user":{"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}}]
     */

    private String code;
    private String msg;
    private int size;
    /**
     * createDate : 2017-05-18 16:07:21
     * updateDate : 2017-05-18 16:07:21
     * delFlag : 0
     * id : f7eb241f7d5147ee9cbff750586bca23
     * bankCode : CEB
     * cardType : 1
     * cardNo : 1234568254125412
     * custName : 123
     * user : {"updateDate":"2017-05-16 11:25:00","delFlag":"0","id":"8907fd8fd6a84dcd980a1dbba21bee73","loginName":"hn1807312095","name":"张三","email":"superusr123@qq.com","mobile":"15132165487","minimum":50000,"dynaMinimum":0,"repayDay":"2","userType":"2","auditState":"3","auditReason":"可以","loginIp":"42.48.85.228","loginDate":"2017-05-18 15:58:54","roleNames":"代售点管理员","balanceString":"55883.95","admin":false}
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

    public static class DataEntity implements Serializable {
        private String createDate;
        private String updateDate;
        private String delFlag;
        private String id;   //用户id
        private String bankCode; //银行卡编号
        private String cardType;  //银行卡类型
        private String cardNo;  //银行卡号
        private String custName; //客户姓名
        private String cardName;  //银行卡姓名
        private boolean checkState;//选中状态
         private BankInfoBean bankInfo;

        public boolean isCheckState() {
            return checkState;
        }

        public void setCheckState(boolean checkState) {
            this.checkState = checkState;
        }

        public BankInfoBean getBankInfo() {
            return bankInfo;
        }

        public void setBankInfo(BankInfoBean bankInfo) {
            this.bankInfo = bankInfo;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

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

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public void setCustName(String custName) {
            this.custName = custName;
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

        public String getBankCode() {
            return bankCode;
        }

        public String getCardType() {
            return cardType;
        }

        public String getCardNo() {
            return cardNo;
        }

        public String getCustName() {
            return custName;
        }

        public static class BankInfoBean implements Serializable{
           private  String id;
            private String bankCode;
            private int everyLimit;
            private int dailyLimit;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getBankCode() {
                return bankCode;
            }

            public void setBankCode(String bankCode) {
                this.bankCode = bankCode;
            }

            public int getEveryLimit() {
                return everyLimit;
            }

            public void setEveryLimit(int everyLimit) {
                this.everyLimit = everyLimit;
            }

            public int getDailyLimit() {
                return dailyLimit;
            }

            public void setDailyLimit(int dailyLimit) {
                this.dailyLimit = dailyLimit;
            }
        }
    }
}

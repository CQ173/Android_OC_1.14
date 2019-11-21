package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class FinancialBankListBean {

    /**
     * code : 0
     * msg : 请求成功
     * data : [{"id":"10146e539eaa49afaf86e952c2e61849","bankCode":"ICBC","cardType":"1","cardNo":"6212261913002226994","custName":"祝峰","idNumber":"510824197602074290","isSigned":"1","isPublic":"0","bankInfo":{"id":"1","bankCode":"ICBC","everyLimit":100},"cardName":"中国工商银行","bankName":"中国工商银行","everyLimit":"100.00"},{"id":"31bda681dbfc47da8e88639c9fe42334","bankCode":"ICBC","cardType":"1","cardNo":"6228480801416266110","custName":"祝峰","idNumber":"510824197602074290","isSigned":"0","isPublic":"0","bankInfo":{"id":"1","bankCode":"ICBC","everyLimit":100},"cardName":"中国工商银行","bankName":"中国工商银行","everyLimit":"100.00"},{"id":"57daa58174c8422fa1c2c939abbc4fcd","bankCode":"ICBC","cardType":"1","cardNo":"6212262201023557228","custName":"祝峰","idNumber":"510824197602074290","isSigned":"1","isPublic":"0","bankInfo":{"id":"1","bankCode":"ICBC","everyLimit":100},"cardName":"中国工商银行","bankName":"中国工商银行","everyLimit":"100.00"},{"id":"5f9d7f9d9f9c4cca877ee4db2daf39fb","bankCode":"ICBC","cardType":"1","cardNo":"6228480402564890018","custName":"dasd","openBankName":"213123","isSigned":"1","isPublic":"1","bankInfo":{"id":"1","bankCode":"ICBC","everyLimit":100},"openBankAreaName":"贵阳市","cardName":"中国工商银行","bankName":"中国工商银行","everyLimit":"100.00"}]
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
         * id : 10146e539eaa49afaf86e952c2e61849
         * bankCode : ICBC
         * cardType : 1
         * cardNo : 6212261913002226994
         * custName : 祝峰
         * idNumber : 510824197602074290
         * isSigned : 1
         * isPublic : 0
         * bankInfo : {"id":"1","bankCode":"ICBC","everyLimit":100}
         * cardName : 中国工商银行
         * bankName : 中国工商银行
         * everyLimit : 100.00
         * openBankName : 213123
         * openBankAreaName : 贵阳市
         */

        private String id;
        private String bankCode;
        private String cardType;
        private String cardNo;
        private String custName;
        private String idNumber;
        private String isSigned;
        private String isPublic;
        private BankInfoBean bankInfo;
        private String cardName;
        private String bankName;
        private String everyLimit;
        private String openBankName;
        private String openBankAreaName;

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

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getIsSigned() {
            return isSigned;
        }

        public void setIsSigned(String isSigned) {
            this.isSigned = isSigned;
        }

        public String getIsPublic() {
            return isPublic;
        }

        public void setIsPublic(String isPublic) {
            this.isPublic = isPublic;
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

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getEveryLimit() {
            return everyLimit;
        }

        public void setEveryLimit(String everyLimit) {
            this.everyLimit = everyLimit;
        }

        public String getOpenBankName() {
            return openBankName;
        }

        public void setOpenBankName(String openBankName) {
            this.openBankName = openBankName;
        }

        public String getOpenBankAreaName() {
            return openBankAreaName;
        }

        public void setOpenBankAreaName(String openBankAreaName) {
            this.openBankAreaName = openBankAreaName;
        }

        public static class BankInfoBean {
            /**
             * id : 1
             * bankCode : ICBC
             * everyLimit : 100
             */

            private String id;
            private String bankCode;
            private int everyLimit;

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
        }
    }
}

package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */

public class FilterDataBean {

    /**
     * code : 0
     * data : [{"createDate":"2016-11-23 16:03:16","delFlag":"0","description":"交易状态","id":"ad46622e71e54448a0c7a2c9b0b20e0b","label":"等待处理","remarks":"(NULL)","sort":15,"type":"acct_trade_status","updateDate":"2016-11-23 16:03:16","value":"ENCHASH_SUSPENSE"},{"createDate":"2016-10-20 11:20:12","delFlag":"0","description":"交易状态","id":"3a265ec2b0de40b1837c938e64ec27f1","label":"交易关闭","sort":20,"type":"acct_trade_status","updateDate":"2016-10-20 11:20:12","value":"TRADE_CLOSED"},{"createDate":"2016-12-13 10:46:10","delFlag":"0","description":"交易状态","id":"39c7bacee8c747c59e6a66ec2c8cd8ca","label":"交易失败","remarks":"(NULL)","sort":60,"type":"acct_trade_status","updateDate":"2016-12-13 10:46:10","value":"TRADE_FAIL"},{"createDate":"2016-10-20 11:22:15","delFlag":"0","description":"交易状态","id":"128841697d98406da6ccd7ca2bf616a8","label":"交易结束","sort":50,"type":"acct_trade_status","updateDate":"2016-10-20 11:22:15","value":"TRADE_FINISHED"},{"createDate":"2016-10-20 11:22:02","delFlag":"0","description":"交易状态","id":"cc5557a966df4322a7d523762ec6ff03","label":"等待收款","sort":40,"type":"acct_trade_status","updateDate":"2016-10-20 11:22:02","value":"TRADE_PENDING"},{"createDate":"2017-05-05 13:19:46","delFlag":"0","description":"交易状态","id":"311","label":"已退款","sort":80,"type":"acct_trade_status","updateDate":"2017-05-05 13:19:49","value":"TRADE_REFUND"},{"createDate":"2017-05-05 13:19:46","delFlag":"0","description":"交易状态","id":"312","label":"退款中","sort":70,"type":"acct_trade_status","updateDate":"2017-05-05 13:19:49","value":"TRADE_REFUNDING"},{"createDate":"2017-05-05 13:19:46","delFlag":"0","description":"交易状态","id":"313","label":"退款失败","sort":90,"type":"acct_trade_status","updateDate":"2017-05-05 13:19:49","value":"TRADE_REFUND_FAIL"},{"createDate":"2016-10-20 11:20:30","delFlag":"0","description":"交易状态","id":"5a8f8100d6e14cacbd9ea8afc037e198","label":"交易成功","sort":30,"type":"acct_trade_status","updateDate":"2016-10-20 11:20:30","value":"TRADE_SUCCESS"},{"createDate":"2016-10-20 11:19:55","delFlag":"0","description":"交易状态","id":"cd1a7b50d1aa4e1a932980cd1263ed8a","label":"等待付款","sort":10,"type":"acct_trade_status","updateDate":"2016-10-20 11:19:55","value":"WAIT_BUYER_PAY"}]
     * msg : 请求成功
     * size : 10
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

    public static class DataBean  extends  BaseData{
        /**
         * createDate : 2016-11-23 16:03:16
         * delFlag : 0
         * description : 交易状态
         * id : ad46622e71e54448a0c7a2c9b0b20e0b
         * label : 等待处理
         * remarks : (NULL)
         * sort : 15
         * type : acct_trade_status
         * updateDate : 2016-11-23 16:03:16
         * value : ENCHASH_SUSPENSE
         */

        private String createDate;
        private String delFlag;
        private String description;

        private String remarks;

        private String type;
        private String updateDate;



        @Override
        public boolean isFlag() {
            return flag;
        }

        @Override
        public void setFlag(boolean flag) {
            this.flag = flag;
        }

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

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
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

    }
}

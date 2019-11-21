package com.huoniao.oc.bean;

/**
 * Created by Administrator on 2018/8/13.
 */

public class PaymentPaySumSpotInfoBean {

    /**
     * code : 0
     * data : {"canEdit":false,"couldUseAmount":"19887789.00","maxSpotAmount":20000000,"usedAmount":"112211.00"}
     * msg : 请求成功
     */

    private String code;
    private DataBean data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * canEdit : false
         * couldUseAmount : 19887789.00
         * maxSpotAmount : 20000000
         * usedAmount : 112211.00
         */

        private boolean canEdit;
        private String couldUseAmount;
        private int maxSpotAmount;
        private String usedAmount;

        public boolean isCanEdit() {
            return canEdit;
        }

        public void setCanEdit(boolean canEdit) {
            this.canEdit = canEdit;
        }

        public String getCouldUseAmount() {
            return couldUseAmount;
        }

        public void setCouldUseAmount(String couldUseAmount) {
            this.couldUseAmount = couldUseAmount;
        }

        public int getMaxSpotAmount() {
            return maxSpotAmount;
        }

        public void setMaxSpotAmount(int maxSpotAmount) {
            this.maxSpotAmount = maxSpotAmount;
        }

        public String getUsedAmount() {
            return usedAmount;
        }

        public void setUsedAmount(String usedAmount) {
            this.usedAmount = usedAmount;
        }
    }
}

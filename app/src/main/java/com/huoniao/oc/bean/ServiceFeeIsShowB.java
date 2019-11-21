package com.huoniao.oc.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/5/5.
 */

public class ServiceFeeIsShowB  implements Serializable {
    private String code;
    private String msg;
    private ServiceFeeIsShowBean data;

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

    public ServiceFeeIsShowBean getData() {
        return data;
    }

    public void setData(ServiceFeeIsShowBean data) {
        this.data = data;
    }

    public  static class ServiceFeeIsShowBean implements Serializable{

        private String serviceFeeSwitch;

        public String getServiceFeeSwitch() {
            return serviceFeeSwitch;
        }

        public void setServiceFeeSwitch(String serviceFeeSwitch) {
            this.serviceFeeSwitch = serviceFeeSwitch;
        }
    }
}

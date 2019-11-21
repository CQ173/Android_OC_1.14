package com.huoniao.oc.bean;

/**
 * Created by Administrator on 2017/9/7.
 */

public class InstitutionalStateBean {
    /**
     "0"; //正常
     "1"; //待审核
     "2"; //审核不通过
     "3"; //冻结
     */
  public String stateName;
  public String value_ ;

    public InstitutionalStateBean(String stateName, String value_) {
        this.stateName = stateName;
        this.value_ = value_;
    }
}

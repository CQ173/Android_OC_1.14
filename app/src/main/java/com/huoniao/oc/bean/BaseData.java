package com.huoniao.oc.bean;

/**
 * Created by admin on 2017/8/30.
 */

public class BaseData  {
    public  String  id;
    public  boolean flag = false;
    public  String label ;
    private String value;
    private int sort;
    public BaseData(){}
    public BaseData(String label,boolean flag,String id){
        this.flag = flag;
        this.label = label;
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

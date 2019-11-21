package com.huoniao.oc.bean;

/**
 * Created by Administrator on 2017/9/21.
 */

public class TreeNewDataBean {
    public TreeNewDataBean(){}
    public TreeNewDataBean(String name, String sum, String rate,int id, int pid) {
        this.name = name;
        this.sum = sum;
        this.rate = rate;
        this.id = id;
        this.pid = pid;
    }

    public String name;
    public String sum;
    public String rate;
    public  int id;
    public  int pid;
}

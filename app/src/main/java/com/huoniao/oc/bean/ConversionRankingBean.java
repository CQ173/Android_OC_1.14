package com.huoniao.oc.bean;

/**
 * Created by Administrator on 2017/9/19.
 */

public class ConversionRankingBean {
    /**
     * agencyName : 耀群票务中心
     * officeCode : gd190200311
     * shouldAmountString : 17769.92
     */
    public String agencyName;
    public String officeCode;
    public String shouldAmountString;
    public int number;

    public ConversionRankingBean(){}
    public ConversionRankingBean(String agencyName, String officeCode, String shouldAmountString, int number) {
        this.agencyName = agencyName;
        this.officeCode = officeCode;
        this.shouldAmountString = shouldAmountString;
        this.number = number;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getShouldAmountString() {
        return shouldAmountString;
    }

    public void setShouldAmountString(String shouldAmountString) {
        this.shouldAmountString = shouldAmountString;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

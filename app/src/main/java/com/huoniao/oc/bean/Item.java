package com.huoniao.oc.bean;

import java.io.Serializable;


public class Item implements Serializable{
    private int id;
    private String name;
    private int img;         //银行图片
    private String bankName ; //银行名称
    private String  cardId;        // 银行卡id
    private String  bankCode ; //银行code 银行编号
    private String  cardType ; //1储蓄卡2信用卡
    private String  cardNo ;   //卡号
    private String  isPublic ; //是否对公账户0不是 1是
    private String custname; //开户姓名
    private String everyLimit;//单笔限额
    private String dailyLimit;//单日限额
    public Item(){}

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Item(int id, String name, int img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public Item(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getEveryLimit() {
        return everyLimit;
    }

    public void setEveryLimit(String everyLimit) {
        this.everyLimit = everyLimit;
    }

    public String getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(String dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
}

package com.huoniao.oc.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class StationWindowManageBean implements Serializable {
    private String id;//子代售点id
    private String winNumber;//子代售点窗口号
    private String agencyName;//子代售点名称
    private String auditState;//子代售点审核状态
    private String auditReason;//子代售点审核理由
    private String operatorName;//子代售点负责人姓名
    private String operatorMobile;//子代售点负责人手机
    private String operatorIdNum;//子代售点负责人身份证号
    private String operatorCardforntSrc;//子代售点身份证正面
    private String operatorCardrearSrc;//子代售点身份证反面
    private String staContIndexSrc;//子代售点合同首页
    private String staContLastSrc;//子代售点合同盖章尾页
    private String fareAuthorizationSrc;//子代售点授权书
    private String officeAreaName;//主代售点区域名称
    private String officeCorpName;//主代售点法人姓名
    private String officeCorpMobile;//主代售点法人手机号
    private String officeCorpIdNum;//主代售点法人身份证号
    private String officeOperatorName;//主代售点负责人姓名
    private String officeOperatorMoblie;//主代售点负责人手机
    private String officeOperatorIdNum;//主代售点负责人身份证号
    private String officeCode;//主代售点编号
    private String officeName;//主代售点名称
    private String officeWinNumber;//主代售点窗口号
    private String updateDate;//操作时间
    public StationWindowManageBean() {

    }

    public StationWindowManageBean(String officeOperatorName, String id, String winNumber, String agencyName, String auditState, String operatorName, String operatorMobile, String operatorIdNum, String operatorCardforntSrc, String operatorCardrearSrc, String staContIndexSrc, String staContLastSrc, String fareAuthorizationSrc, String officeAreaName, String officeCorpName, String officeCorpMobile, String officeCorpIdNum,
                                   String officeOperatorMoblie, String officeOperatorIdNum, String officeCode, String officeName, String officeWinNumber, String updateDate, String auditReason) {
        this.officeOperatorName = officeOperatorName;
        this.id = id;
        this.winNumber = winNumber;
        this.agencyName = agencyName;
        this.auditState = auditState;
        this.operatorName = operatorName;
        this.operatorMobile = operatorMobile;
        this.operatorIdNum = operatorIdNum;
        this.operatorCardforntSrc = operatorCardforntSrc;
        this.operatorCardrearSrc = operatorCardrearSrc;
        this.staContIndexSrc = staContIndexSrc;
        this.staContLastSrc = staContLastSrc;
        this.fareAuthorizationSrc = fareAuthorizationSrc;
        this.officeAreaName = officeAreaName;
        this.officeCorpName = officeCorpName;
        this.officeCorpMobile = officeCorpMobile;
        this.officeCorpIdNum = officeCorpIdNum;
        this.officeOperatorMoblie = officeOperatorMoblie;
        this.officeOperatorIdNum = officeOperatorIdNum;
        this.officeCode = officeCode;
        this.officeName = officeName;
        this.officeWinNumber = officeWinNumber;
        this.updateDate = updateDate;
        this.auditReason = auditReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(String winNumber) {
        this.winNumber = winNumber;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorMobile() {
        return operatorMobile;
    }

    public void setOperatorMobile(String operatorMobile) {
        this.operatorMobile = operatorMobile;
    }

    public String getOperatorIdNum() {
        return operatorIdNum;
    }

    public void setOperatorIdNum(String operatorIdNum) {
        this.operatorIdNum = operatorIdNum;
    }

    public String getOperatorCardforntSrc() {
        return operatorCardforntSrc;
    }

    public void setOperatorCardforntSrc(String operatorCardforntSrc) {
        this.operatorCardforntSrc = operatorCardforntSrc;
    }

    public String getOperatorCardrearSrc() {
        return operatorCardrearSrc;
    }

    public void setOperatorCardrearSrc(String operatorCardrearSrc) {
        this.operatorCardrearSrc = operatorCardrearSrc;
    }

    public String getStaContIndexSrc() {
        return staContIndexSrc;
    }

    public void setStaContIndexSrc(String staContIndexSrc) {
        this.staContIndexSrc = staContIndexSrc;
    }

    public String getStaContLastSrc() {
        return staContLastSrc;
    }

    public void setStaContLastSrc(String staContLastSrc) {
        this.staContLastSrc = staContLastSrc;
    }

    public String getFareAuthorizationSrc() {
        return fareAuthorizationSrc;
    }

    public void setFareAuthorizationSrc(String fareAuthorizationSrc) {
        this.fareAuthorizationSrc = fareAuthorizationSrc;
    }

    public String getOfficeAreaName() {
        return officeAreaName;
    }

    public void setOfficeAreaName(String officeAreaName) {
        this.officeAreaName = officeAreaName;
    }

    public String getOfficeCorpName() {
        return officeCorpName;
    }

    public void setOfficeCorpName(String officeCorpName) {
        this.officeCorpName = officeCorpName;
    }

    public String getOfficeCorpMobile() {
        return officeCorpMobile;
    }

    public void setOfficeCorpMobile(String officeCorpMobile) {
        this.officeCorpMobile = officeCorpMobile;
    }

    public String getOfficeCorpIdNum() {
        return officeCorpIdNum;
    }

    public void setOfficeCorpIdNum(String officeCorpIdNum) {
        this.officeCorpIdNum = officeCorpIdNum;
    }

    public String getOfficeOperatorName() {
        return officeOperatorName;
    }

    public void setOfficeOperatorName(String officeOperatorName) {
        this.officeOperatorName = officeOperatorName;
    }

    public String getOfficeOperatorMoblie() {
        return officeOperatorMoblie;
    }

    public void setOfficeOperatorMoblie(String officeOperatorMoblie) {
        this.officeOperatorMoblie = officeOperatorMoblie;
    }

    public String getOfficeOperatorIdNum() {
        return officeOperatorIdNum;
    }

    public void setOfficeOperatorIdNum(String officeOperatorIdNum) {
        this.officeOperatorIdNum = officeOperatorIdNum;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficeWinNumber() {
        return officeWinNumber;
    }

    public void setOfficeWinNumber(String officeWinNumber) {
        this.officeWinNumber = officeWinNumber;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }
}

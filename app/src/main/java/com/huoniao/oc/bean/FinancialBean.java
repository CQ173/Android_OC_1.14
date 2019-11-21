package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class FinancialBean implements Serializable {
    private String id;//财务记录id
    private String createDate;//时间
    private String agencyUserOfficeName;//机构名称
    private String agencyLoginName;//代理账户
    private String agencyUserName;//姓名
    private String applyTypeName;//申请类型名称
    private String transferId;//交易单号
    private String applyFee;//待付金额
    private String actualFee;//实付金额
    private String applyType;//申请类型代号
    private String accountName;//账户名称
    private String openBankName;//开户行
    private String cardNo;//卡号
    private String applyUserName;//申请人
    private String receiptLoginName;//收款账号
    private String receiptName;//收款人
    private String stateName;//状态名称
    private String state;//状态代号
    private String remark;//备注
    private String reason;//理由
    private String cashierUserName;//出纳操作人
    private String auditUserName;//会计操作人
    private String ZXswitch;// 银企直联功能未开启提示
    private String officeParentNames;//上级机构

    private String labe;//申请类型或审核状态标签名称
    private String value;//申请类型或审核状态对应的代号
    private int sort;//申请类型或审核状态对应的排序号
    private String itemClickTag;//用于标识审核状态标签是否被点击
    private List<FinancialBean> data;
    public FinancialBean() {
    }

    public FinancialBean(String id, String createDate, String agencyUserOfficeName, String agencyLoginName, String agencyUserName, String applyTypeName,
                         String transferId, String applyFee, String applyType, String accountName, String openBankName, String cardNo, String applyUserName,
                         String receiptLoginName, String receiptName, String stateName, String remark, String reason, String cashierUserName, String auditUserName
                        , String state, String ZXswitch, String labe, String value, int sort, String itemClickTag, List<FinancialBean> data, String actualFee) {
        this.id = id;
        this.createDate = createDate;
        this.agencyUserOfficeName = agencyUserOfficeName;
        this.agencyLoginName = agencyLoginName;
        this.agencyUserName = agencyUserName;
        this.applyTypeName = applyTypeName;
        this.transferId = transferId;
        this.applyFee = applyFee;
        this.applyType = applyType;
        this.accountName = accountName;
        this.openBankName = openBankName;
        this.cardNo = cardNo;
        this.applyUserName = applyUserName;
        this.receiptLoginName = receiptLoginName;
        this.receiptName = receiptName;
        this.stateName = stateName;
        this.state = state;
        this.remark = remark;
        this.reason = reason;
        this.cashierUserName = cashierUserName;
        this.auditUserName = auditUserName;
        this.ZXswitch = ZXswitch;
        this.labe = labe;
        this.value = value;
        this.sort = sort;
        this.itemClickTag = itemClickTag;
        this.data = data;
        this.actualFee = actualFee;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAgencyUserOfficeName() {
        return agencyUserOfficeName;
    }

    public void setAgencyUserOfficeName(String agencyUserOfficeName) {
        this.agencyUserOfficeName = agencyUserOfficeName;
    }

    public String getAgencyLoginName() {
        return agencyLoginName;
    }

    public void setAgencyLoginName(String agencyLoginName) {
        this.agencyLoginName = agencyLoginName;
    }

    public String getAgencyUserName() {
        return agencyUserName;
    }

    public void setAgencyUserName(String agencyUserName) {
        this.agencyUserName = agencyUserName;
    }

    public String getApplyTypeName() {
        return applyTypeName;
    }

    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getApplyFee() {
        return applyFee;
    }

    public void setApplyFee(String applyFee) {
        this.applyFee = applyFee;
    }

    public String getActualFee() {
        return actualFee;
    }

    public void setActualFee(String actualFee) {
        this.actualFee = actualFee;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOpenBankName() {
        return openBankName;
    }

    public void setOpenBankName(String openBankName) {
        this.openBankName = openBankName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getReceiptLoginName() {
        return receiptLoginName;
    }

    public void setReceiptLoginName(String receiptLoginName) {
        this.receiptLoginName = receiptLoginName;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCashierUserName() {
        return cashierUserName;
    }

    public void setCashierUserName(String cashierUserName) {
        this.cashierUserName = cashierUserName;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZXswitch() {
        return ZXswitch;
    }

    public void setZXswitch(String ZXswitch) {
        this.ZXswitch = ZXswitch;
    }

    public String getLabe() {
        return labe;
    }

    public void setLabe(String labe) {
        this.labe = labe;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getItemClickTag() {
        return itemClickTag;
    }

    public void setItemClickTag(String itemClickTag) {
        this.itemClickTag = itemClickTag;
    }

    public List<FinancialBean> getData() {
        return data;
    }

    public void setData(List<FinancialBean> data) {
        this.data = data;
    }

    public String getOfficeParentNames() {
        return officeParentNames;
    }

    public void setOfficeParentNames(String officeParentNames) {
        this.officeParentNames = officeParentNames;
    }
}

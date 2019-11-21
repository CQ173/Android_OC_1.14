package com.huoniao.oc.bean;

import org.json.JSONObject;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OltsCapitalFlowBean implements Serializable{
	
	private String flowId;//交易流水号
	private String tradeName;//名称
	private JSONObject tradeUser;//交易的用户信息
	private String tradeAcct;//交易账号
	private String tradeFee;//交易金额
	private String curBalance;//用户当前余额
	private String type;//交易类型 income:收入 payout:支出
	private String tradeStatus;//交易状态 
	private String tradeDate;//交易日期
	private String loginName;//用户名-交易账号
	private String name;//用户姓名
	private String channelType; //交易渠道
	private String expireTime ; //操作时间
	private String updateDate; //更新时间

	private String sourceFlowId;//关联流水号

	//服务器后天没有添加的字段
	private String income;
	private String payout;

	public OltsCapitalFlowBean() {
		
	}

	public OltsCapitalFlowBean(String flowId, String tradeName, JSONObject tradeUser, String tradeAcct, String tradeFee,
							   String curBalance, String type, String tradeStatus, String tradeDate, String loginName, String name,
							   String channelType, String expireTime, String updateDate, String sourceFlowId) {
		this.flowId = flowId;
		this.tradeName = tradeName;
		this.tradeUser = tradeUser;
		this.tradeAcct = tradeAcct;
		this.tradeFee = tradeFee;
		this.curBalance = curBalance;
		this.type = type;
		this.tradeStatus = tradeStatus;

		this.tradeDate = tradeDate;
		this.loginName = loginName;
		this.name = name;
		this.channelType = channelType;
		this.expireTime = expireTime;
		this.updateDate = updateDate;
		this.sourceFlowId = sourceFlowId;
	}

	public String getSourceFlowId() {
		return sourceFlowId;
	}

	public void setSourceFlowId(String sourceFlowId) {
		this.sourceFlowId = sourceFlowId;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public JSONObject getTradeUser() {
		return tradeUser;
	}

	public void setTradeUser(JSONObject tradeUser) {
		this.tradeUser = tradeUser;
	}

	public String getTradeAcct() {
		return tradeAcct;
	}

	public void setTradeAcct(String tradeAcct) {
		this.tradeAcct = tradeAcct;
	}

	public String getTradeFee() {
		return tradeFee;
	}

	public void setTradeFee(String tradeFee) {
		this.tradeFee = tradeFee;
	}

	public String getCurBalance() {
		return curBalance;
	}

	public void setCurBalance(String curBalance) {
		this.curBalance = curBalance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}


	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getPayout() {
		return payout;
	}

	public void setPayout(String payout) {
		this.payout = payout;
	}
}

package com.huoniao.oc.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PaySystemBean implements Serializable{
	private String date; //票款时间
	private String officeCode;//扣款账号
	private String agentName;
	private String shouldAmount;
	private String alreadyAmount;
	private String withholdStatus;
	private String railwayStationName;//火车站名称
	private String railwayStationId;//火车站编号
	private String shouldSum;
	private String alreadySum;
	private String createDate;//汇缴时间
	private String winNumber;//窗口号
	private String branchName;//铁路分局
	private String trunkName;//铁路总局
	private String ticketCount;//实售票数
	public PaySystemBean() {
		
	}

	public PaySystemBean(String date, String officeCode, String agentName, String shouldAmount, String alreadyAmount,
			String withholdStatus, String shouldSum, String alreadySum, String railwayStationName, String railwayStationId,
			 String createDate, String winNumber, String branchName, String trunkName, String ticketCount) {
		this.date = date;
		this.officeCode = officeCode;
		this.agentName = agentName;
		this.shouldAmount = shouldAmount;
		this.alreadyAmount = alreadyAmount;
		this.withholdStatus = withholdStatus;
		this.shouldSum = shouldSum;
		this.alreadySum = alreadySum;
		this.railwayStationName = railwayStationName;
		this.railwayStationId = railwayStationId;
		this.createDate = createDate;
		this.winNumber = winNumber;
		this.branchName = branchName;
		this.trunkName = trunkName;
		this.ticketCount = ticketCount;
	}

	private String withholdStatusName;

	public String getWithholdStatusName() {
		return withholdStatusName;
	}

	public void setWithholdStatusName(String withholdStatusName) {
		this.withholdStatusName = withholdStatusName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getShouldAmount() {
		return shouldAmount;
	}

	public void setShouldAmount(String shouldAmount) {
		this.shouldAmount = shouldAmount;
	}

	public String getAlreadyAmount() {
		return alreadyAmount;
	}

	public void setAlreadyAmount(String alreadyAmount) {
		this.alreadyAmount = alreadyAmount;
	}

	public String getWithholdStatus() {
		return withholdStatus;
	}

	public void setWithholdStatus(String withholdStatus) {
		this.withholdStatus = withholdStatus;
	}

	public String getShouldSum() {
		return shouldSum;
	}

	public void setShouldSum(String shouldSum) {
		this.shouldSum = shouldSum;
	}

	public String getAlreadySum() {
		return alreadySum;
	}

	public void setAlreadySum(String alreadySum) {
		this.alreadySum = alreadySum;
	}

	public String getRailwayStationName() {
		return railwayStationName;
	}

	public void setRailwayStationName(String railwayStationName) {
		this.railwayStationName = railwayStationName;
	}

	public String getRailwayStationId() {
		return railwayStationId;
	}

	public void setRailwayStationId(String railwayStationId) {
		this.railwayStationId = railwayStationId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getTrunkName() {
		return trunkName;
	}

	public void setTrunkName(String trunkName) {
		this.trunkName = trunkName;
	}

	public String getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(String ticketCount) {
		this.ticketCount = ticketCount;
	}
}

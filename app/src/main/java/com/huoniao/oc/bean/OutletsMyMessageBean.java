package com.huoniao.oc.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OutletsMyMessageBean implements Serializable{
	
	private String id;
	private String infoStatus;
	private String content;
	private String infoDate;
	private boolean checkFlag;//checkbox的状态
	public OutletsMyMessageBean() {
		
	}

	public OutletsMyMessageBean(String id, String infoStatus, String content, String infoDate, boolean checkFlag) {
		this.id = id;
		this.infoStatus = infoStatus;
		this.content = content;
		this.infoDate = infoDate;
		this.checkFlag = checkFlag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfoStatus() {
		return infoStatus;
	}

	public void setInfoStatus(String infoStatus) {
		this.infoStatus = infoStatus;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getInfoDate() {
		return infoDate;
	}

	public void setInfoDate(String infoDate) {
		this.infoDate = infoDate;
	}

	public boolean isCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(boolean checkFlag) {
		this.checkFlag = checkFlag;
	}
	
	
}

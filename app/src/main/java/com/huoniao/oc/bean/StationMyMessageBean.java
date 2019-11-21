package com.huoniao.oc.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class StationMyMessageBean implements Serializable{
	
	private String id;
	private String infoStatus;
	private String content;
	private String infoDate;
	
	public StationMyMessageBean() {
		
	}

	public StationMyMessageBean(String id, String infoStatus, String content, String infoDate) {
		this.id = id;
		this.infoStatus = infoStatus;
		this.content = content;
		this.infoDate = infoDate;
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
	
	
}

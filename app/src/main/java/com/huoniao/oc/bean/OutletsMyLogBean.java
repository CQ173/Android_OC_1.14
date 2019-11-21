package com.huoniao.oc.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OutletsMyLogBean implements Serializable{
	
	private String id;//编号
	private String remoteAddr;//操作IP地址
	private String content;//日志内容
	private String createDate;//创建时间
	
	public OutletsMyLogBean() {
		
	}

	public OutletsMyLogBean(String id, String remoteAddr, String content, String createDate) {
		this.id = id;
		this.remoteAddr = remoteAddr;
		this.content = content;
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	
	
	
}

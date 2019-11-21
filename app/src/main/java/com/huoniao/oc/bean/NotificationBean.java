package com.huoniao.oc.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NotificationBean implements Serializable{
	private String notificationId;//通知Id
	private String notificationTitle;//通知标题
	private String notificationType;//通知类型
	private String notificationContent;//通知类型
	private String startTime;//有效期开始时间
	private String endTime;//有效期结束时间
	private boolean checkFlag;//checkbox的状态
	private String createDate;//通知创建时间
	private String url;//链接
	public NotificationBean() {
		super();
	}

	public NotificationBean(String notificationId,String notificationTitle, String notificationType, String notificationContent,
			String startTime, String endTime, boolean checkFlag, String createDate, String url) {
		super();
		this.notificationId = notificationId;
		this.notificationTitle = notificationTitle;
		this.notificationType = notificationType;
		this.notificationContent = notificationContent;
		this.startTime = startTime;
		this.endTime = endTime;
		this.checkFlag = checkFlag;
		this.createDate = createDate;
		this.url = url;
	}
	
	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getNotificationTitle() {
		return notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationContent() {
		return notificationContent;
	}

	public void setNotificationContent(String notificationContent) {
		this.notificationContent = notificationContent;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	

	public boolean isCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(boolean checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

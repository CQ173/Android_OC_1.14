package com.huoniao.oc.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FeedbackBean implements Serializable{
	private String id;//反馈信息ID
	private String opinion;//处理意见
	private String userName;//用户名
	private String feedbackType;//反馈类型
	private String feedbackTime;//反馈时间
	private String feedbackContent;//反馈内容
	private String status;//处理状态
	
	
	public FeedbackBean() {
		
	}


	public FeedbackBean(String userName, String feedbackType, String feedbackTime, String feedbackContent,
			String status, String id, String opinion) {
		this.id = id;
		this.userName = userName;
		this.opinion = opinion;
		this.feedbackType = feedbackType;
		this.feedbackTime = feedbackTime;
		this.feedbackContent = feedbackContent;
		this.status = status;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getFeedbackType() {
		return feedbackType;
	}


	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}


	public String getFeedbackTime() {
		return feedbackTime;
	}


	public void setFeedbackTime(String feedbackTime) {
		this.feedbackTime = feedbackTime;
	}


	public String getFeedbackContent() {
		return feedbackContent;
	}


	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getOpinion() {
		return opinion;
	}


	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}


	@Override
	public String toString() {
		return "FeedbackBean [id=" + id + ", opinion=" + opinion + ", userName=" + userName + ", feedbackType="
				+ feedbackType + ", feedbackTime=" + feedbackTime + ", feedbackContent=" + feedbackContent + ", status="
				+ status + "]";
	}


	
	
	
	
	
}

package com.huoniao.oc.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BankCardBean implements Serializable{
	private String id;//银行卡id
	private int cardIcon;//银行卡logo
	private String bankType;//银行类型
	private String cardType;//卡类型
	private String cardnumber;//卡号
	private String bankCode;//银行编码
	private String custname;//开户姓名
	private String openBankName;//开户行名称
	private String openBankAreaName;//开户行所在地区名
    private String isSigned;  //签约  是否签约 0未签约，1 已签约     若未传或传空值这默认为0
    private String isPublic; //对公账户 是否为对公账户  0不是， 1是   若未传或传空值这默认为0
    private String cardName; //银行名称
	private boolean checkState;
    private BankInfoBean bankInfo;
	private String everyLimit;//单笔限额
	private String dailyLimit;//单日限额
	public BankCardBean() {
		super();
	}


	public BankCardBean(int cardIcon, String bankType, String cardType, String cardnumber, String id,
			String bankCode, String custname, String openBankName, String openBankAreaName, boolean checkState
			, String everyLimit, String dailyLimit) {
		super();
		this.cardIcon = cardIcon;
		this.bankType = bankType;
		this.cardType = cardType;
		this.cardnumber = cardnumber;
		this.id = id;
		this.bankCode = bankCode;
		this.custname = custname;
		this.openBankName = openBankName;
		this.openBankAreaName = openBankAreaName;
		this.checkState = checkState;
		this.everyLimit = everyLimit;
		this.dailyLimit = dailyLimit;
	}


	public BankInfoBean getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(BankInfoBean bankInfo) {
		this.bankInfo = bankInfo;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getIsSigned() {
		return isSigned;
	}

	public void setIsSigned(String isSigned) {
		this.isSigned = isSigned;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public int getCardIcon() {
		return cardIcon;
	}


	public void setCardIcon(int cardIcon) {
		this.cardIcon = cardIcon;
	}


	public String getBankType() {
		return bankType;
	}


	public void setBankType(String bankType) {
		this.bankType = bankType;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public String getCardnumber() {
		return cardnumber;
	}


	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public String getCustname() {
		return custname;
	}


	public void setCustname(String custname) {
		this.custname = custname;
	}


	public String getOpenBankName() {
		return openBankName;
	}

	public void setOpenBankName(String openBankName) {
		this.openBankName = openBankName;
	}

	public String getOpenBankAreaName() {
		return openBankAreaName;
	}

	public void setOpenBankAreaName(String openBankAreaName) {
		this.openBankAreaName = openBankAreaName;
	}

	public boolean isCheckState() {
		return checkState;
	}

	public void setCheckState(boolean checkState) {
		this.checkState = checkState;
	}

	public static class BankInfoBean implements Serializable{
		private  String id;
		private String bankCode;
		private double everyLimit=-1; //本日最低限额   默认-1表示没有把字段传过来
		private double dailyLimit =-1;//单笔最低限额

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getBankCode() {
			return bankCode;
		}

		public void setBankCode(String bankCode) {
			this.bankCode = bankCode;
		}

		public double getEveryLimit() {
			return everyLimit;
		}

		public void setEveryLimit(double everyLimit) {
			this.everyLimit = everyLimit;
		}

		public double getDailyLimit() {
			return dailyLimit;
		}

		public void setDailyLimit(double dailyLimit) {
			this.dailyLimit = dailyLimit;
		}
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

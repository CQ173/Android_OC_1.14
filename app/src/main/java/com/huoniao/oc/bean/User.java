package com.huoniao.oc.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class User implements Serializable {
	private String id;// 用户ID
	private String parentId;// 父账号ID
	private String name;// 用户姓名
	private String mobile;// 手机号码
	private String userType;// 用户类型
	private String officeId;// 机构ID
	private String officeType;// 机构类型
	private String userCode;// 用户编号
	private String orgName;// 机构名称
	private String orgAddress;// 机构地址
	private String userState;// 用户状态
	private String winNumber;// 窗口号
	private String corpName;// 法人姓名
	private String corpMobile;// 法人手机
	private String corpIdNum;// 法人身份证号
	private String master;// 联系人
	private String contactPhone;// 联系人电话
	private String area_name;// 归属区域
	private String jurisArea;//管辖区域
	private String provinceCode;// 省份code
	private String cityCode;// 省份code
	private String provinceName;// 省份名称
	private String cityName;// 城市名称
	private String address;// 联系地址
	private String corp_licence;// 营业执照
	private String corp_card_fornt;// 身份证正面
	private String corp_card_rear;// 身份证反面
	private String loginName;// 用户名
	private String infoReceiveType;//消息接收方式
	private String balance;// 余额
	private String minimum;// 最低限额
	private String email;// 邮箱
	private String remarks;// 备注
	private String auditState;// 审核状态
	private String auditReason;// 审核理由
	private String roleNames;// 用户角色名
	private String creatTime;// 创建时间
	private String oneLevelCode;// 管辖区域第一级code
	private String oneLevelName;// 管辖区域第一级name
	private String twoLevelCode;// 管辖区域第二级code
	private String twoLevelName;// 管辖区域第二级name
	private String threeLevelCode;// 管辖区域第三级code
	private String threeLevelName;// 管辖区域第三级name
	private String agent;//第一代理人
	private String agentType;//代理人类型
	private String operatorName;//负责人姓名
	private String operatorMobile;//负责人手机
	private String operatorIdNum;//负责人身份证号
	private String agentCompanyName;//公司名称
	private String staContIndexSrc;//车站合同首页
	private String staContLastSrc;//车站合同尾页
	private String staDepositSrc;//车站押金条
	private String staDepInspSrc;// 车站押金年检证书
	private String bankFlowSrc;// 银行流水
	private String operatorCardforntSrc;// 负责人身份证正面
	private String operatorCardrearSrc;// 负责人身份证反面
	private String fareAuthorizationSrc;// 票款汇缴授权书

	private String repayDay;  ///还款日
	private String dynaMinimum; //动态最低限额

	private String photoSrc; //头像链接
	private String roleName;//角色名
	private List<String> premissions; //权限
	private List<AgencysBean> agencys; //地图显示配置地址集合
	private List<DataBean> data;


	public User() {

	}

	public User(String id, String name, String mobile, String userType, String officeType, String userCode,
			String orgName, String orgAddress, String userState, String winNumber, String corpName, String corpMobile,
			String corpIdNum, String master, String contactPhone, String area_name, String address, String corp_licence,
			String corp_card_fornt, String corp_card_rear, String balance, String minimum, String auditState,
			String loginName, String auditReason, String roleNames, String email, String remarks, String officeId,
			String creatTime, String provinceCode, String cityCode, String provinceName, String cityName,
			String parentId, String oneLevelCode, String oneLevelName, String twoLevelCode, String twoLevelName
			,String threeLevelCode, String threeLevelName, String agent, String agentType, String agentCompanyName
			, String staContIndexSrc, String staContLastSrc, String staDepositSrc, String staDepInspSrc,
			String bankFlowSrc, String operatorCardforntSrc, String operatorCardrearSrc, String fareAuthorizationSrc
			, String operatorName, String operatorMobile, String operatorIdNum, String jurisArea, String roleName,
			String infoReceiveType) {
		super();
		this.id = id;
		this.name = name;
		this.mobile = mobile;
		this.userType = userType;
		this.officeType = officeType;
		this.userCode = userCode;
		this.orgName = orgName;
		this.orgAddress = orgAddress;
		this.userState = userState;
		this.winNumber = winNumber;
		this.corpName = corpName;
		this.corpMobile = corpMobile;
		this.corpIdNum = corpIdNum;
		this.master = master;
		this.contactPhone = contactPhone;
		this.area_name = area_name;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.address = address;
		this.corp_licence = corp_licence;
		this.corp_card_fornt = corp_card_fornt;
		this.corp_card_rear = corp_card_rear;
		this.loginName = loginName;
		this.balance = balance;
		this.minimum = minimum;
		this.auditState = auditState;
		this.auditReason = auditReason;
		this.roleNames = roleNames;
		this.email = email;
		this.remarks = remarks;
		this.officeId = officeId;
		this.creatTime = creatTime;
		this.provinceName = provinceName;
		this.cityName = cityName;
		this.parentId = parentId;
		this.oneLevelCode = oneLevelCode;
		this.oneLevelName = oneLevelName;
		this.twoLevelName = twoLevelName;
		this.twoLevelCode = twoLevelCode;
		this.threeLevelCode = threeLevelCode;
		this.threeLevelName = threeLevelName;
		this.agent = agent;
		this.agentType = agentType;
		this.agentCompanyName = agentCompanyName;
		this.staContIndexSrc = staContIndexSrc;
		this.staContLastSrc = staContLastSrc;
		this.staDepositSrc = staDepositSrc;
		this.staDepInspSrc = staDepInspSrc;
		this.bankFlowSrc = bankFlowSrc;
		this.operatorCardforntSrc = operatorCardforntSrc;
		this.operatorCardrearSrc = operatorCardrearSrc;
		this.fareAuthorizationSrc = fareAuthorizationSrc;
		this.operatorName = operatorName;
		this.operatorMobile = operatorMobile;
		this.operatorIdNum = operatorIdNum;
		this.jurisArea = jurisArea;
		this.roleName = roleName;
		this.infoReceiveType = infoReceiveType;
	}


	/**
	 * code : 0
	 * msg : 请求成功
	 * premissions : ["","partner:partnerMall:view","acct:account:edit","sys:user:info","sys:subUser:view","acct:account:edit","acct:tradeFlow:myWallet","sys:user:index","sys:user:modifyPwd","acct:userCard:edit","sys:subUser:edit","acct:userCard:view","acct:account:export","acct:account:view","acct:tradeFlow:view"]
	 */



	public void setPremissions(List<String> premissions) {
		this.premissions = premissions;
	}

	public List<String> getPremissions() {
		return premissions;
	}


	public String getPhotoSrc() {
		return photoSrc;
	}

	public void setPhotoSrc(String photoSrc) {
		this.photoSrc = photoSrc;
	}

	public String getRepayDay() {
		return repayDay;
	}

	public void setRepayDay(String repayDay) {
		this.repayDay = repayDay;
	}

	public String getDynaMinimum() {
		return dynaMinimum;
	}

	public void setDynaMinimum(String dynaMinimum) {
		this.dynaMinimum = dynaMinimum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getOfficeType() {
		return officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgAddress() {
		return orgAddress;
	}

	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getCorpMobile() {
		return corpMobile;
	}

	public void setCorpMobile(String corpMobile) {
		this.corpMobile = corpMobile;
	}

	public String getCorpIdNum() {
		return corpIdNum;
	}

	public void setCorpIdNum(String corpIdNum) {
		this.corpIdNum = corpIdNum;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCorp_licence() {
		return corp_licence;
	}

	public void setCorp_licence(String corp_licence) {
		this.corp_licence = corp_licence;
	}

	public String getCorp_card_fornt() {
		return corp_card_fornt;
	}

	public void setCorp_card_fornt(String corp_card_fornt) {
		this.corp_card_fornt = corp_card_fornt;
	}

	public String getCorp_card_rear() {
		return corp_card_rear;
	}

	public void setCorp_card_rear(String corp_card_rear) {
		this.corp_card_rear = corp_card_rear;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getInfoReceiveType() {
		return infoReceiveType;
	}

	public void setInfoReceiveType(String infoReceiveType) {
		this.infoReceiveType = infoReceiveType;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getMinimum() {
		return minimum;
	}

	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getOneLevelCode() {
		return oneLevelCode;
	}

	public void setOneLevelCode(String oneLevelCode) {
		this.oneLevelCode = oneLevelCode;
	}

	public String getOneLevelName() {
		return oneLevelName;
	}

	public void setOneLevelName(String oneLevelName) {
		this.oneLevelName = oneLevelName;
	}

	public String getTwoLevelCode() {
		return twoLevelCode;
	}

	public void setTwoLevelCode(String twoLevelCode) {
		this.twoLevelCode = twoLevelCode;
	}

	public String getTwoLevelName() {
		return twoLevelName;
	}

	public void setTwoLevelName(String twoLevelName) {
		this.twoLevelName = twoLevelName;
	}

	public String getThreeLevelCode() {
		return threeLevelCode;
	}

	public void setThreeLevelCode(String threeLevelCode) {
		this.threeLevelCode = threeLevelCode;
	}

	public String getThreeLevelName() {
		return threeLevelName;
	}

	public void setThreeLevelName(String threeLevelName) {
		this.threeLevelName = threeLevelName;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getAgentCompanyName() {
		return agentCompanyName;
	}

	public void setAgentCompanyName(String agentCompanyName) {
		this.agentCompanyName = agentCompanyName;
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

	public String getStaDepositSrc() {
		return staDepositSrc;
	}

	public void setStaDepositSrc(String staDepositSrc) {
		this.staDepositSrc = staDepositSrc;
	}

	public String getStaDepInspSrc() {
		return staDepInspSrc;
	}

	public void setStaDepInspSrc(String staDepInspSrc) {
		this.staDepInspSrc = staDepInspSrc;
	}

	public String getBankFlowSrc() {
		return bankFlowSrc;
	}

	public void setBankFlowSrc(String bankFlowSrc) {
		this.bankFlowSrc = bankFlowSrc;
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

	public String getFareAuthorizationSrc() {
		return fareAuthorizationSrc;
	}

	public void setFareAuthorizationSrc(String fareAuthorizationSrc) {
		this.fareAuthorizationSrc = fareAuthorizationSrc;
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

	public String getJurisArea() {
		return jurisArea;
	}

	public void setJurisArea(String jurisArea) {
		this.jurisArea = jurisArea;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<AgencysBean> getAgencys() {
		return agencys;
	}

	public void setAgencys(List<AgencysBean> agencys) {
		this.agencys = agencys;
	}

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}


	public static class AgencysBean implements Serializable {
		/**
		 * id : 3a597c1e49bc47a8a852fbf771f87179
		 * winNumber : 2255
		 * agencyName : 一线天代售点
		 * auditState : 0
		 * auditReason : 12
		 * operatorCardforntSrc : /userfiles/gd1905611016/images/1af6d3ac1e7936606edf8ff337517dc0.jpg
		 * operatorCardrearSrc : /userfiles/gd1905611016/images/eeaa03c1f366418679f4e0d4c2c27396.jpg
		 * staContIndexSrc : /userfiles/gd1905611016/images/661569ccf2a6a5173c10ffba568c1dc0.jpg
		 * staContLastSrc : /userfiles/gd1905611016/images/765b2cad93646ccabc122eb0e3b137c0.jpg
		 * fareAuthorizationSrc : /userfiles/gd1905611016/images/7b7e6aa6829d4593c34c12d0523bc325.jpg
		 * operatorName : 李旺
		 * operatorMobile : 18273161371
		 * operatorIdNum : 532128199509160735
		 * lat : 33.960022
		 * lng : 116.79145
		 * geogPosition : 淮北市
		 * officeName : 深圳北站
		 * officeWinNumber : 1016
		 * officeAreaName : 淮北市
		 * officeCorpName : 楚乔
		 * officeCorpMobile : 15575366041
		 * officeCorpIdNum : 451028199212075277
		 * officeOperatorName : 楚乔
		 * officeOperatorMoblie : 15575366041
		 * officeOperatorIdNum : 430681199512036548
		 * officeCode : gd1905611016
		 */

		@SerializedName("id")
		private String idX;
		@SerializedName("winNumber")
		private String winNumberX;
		private String agencyName;
		@SerializedName("auditState")
		private String auditStateX;
		@SerializedName("auditReason")
		private String auditReasonX;
		@SerializedName("operatorCardforntSrc")
		private String operatorCardforntSrcX;
		@SerializedName("operatorCardrearSrc")
		private String operatorCardrearSrcX;
		@SerializedName("staContIndexSrc")
		private String staContIndexSrcX;
		@SerializedName("staContLastSrc")
		private String staContLastSrcX;
		@SerializedName("fareAuthorizationSrc")
		private String fareAuthorizationSrcX;
		@SerializedName("operatorName")
		private String operatorNameX;
		@SerializedName("operatorMobile")
		private String operatorMobileX;
		@SerializedName("operatorIdNum")
		private String operatorIdNumX;
		private String lat;
		private String lng;
		private String geogPosition;
		private String officeName;
		private String officeWinNumber;
		private String officeAreaName;
		private String officeCorpName;
		private String officeCorpMobile;
		private String officeCorpIdNum;
		private String officeOperatorName;
		private String officeOperatorMoblie;
		private String officeOperatorIdNum;
		private String officeCode;

		private String mainAddress; // 主账号地址 自己手动额外添加
		private String mainAddressFlag;//主账号标记 自己手动额外添加
		public String getMainAddressFlag(){
			return  mainAddressFlag;
		}
		public void setMainAddressFlag(String mainAddressFlag){
			this.mainAddressFlag = mainAddressFlag;
		}
		public  String getMainAddress(){
			return mainAddress;
		}
		public void setMainAddress(String mainAddress){
			this.mainAddress = mainAddress;
		}
		public String getIdX() {
			return idX;
		}

		public void setIdX(String idX) {
			this.idX = idX;
		}

		public String getWinNumberX() {
			return winNumberX;
		}

		public void setWinNumberX(String winNumberX) {
			this.winNumberX = winNumberX;
		}

		public String getAgencyName() {
			return agencyName;
		}

		public void setAgencyName(String agencyName) {
			this.agencyName = agencyName;
		}

		public String getAuditStateX() {
			return auditStateX;
		}

		public void setAuditStateX(String auditStateX) {
			this.auditStateX = auditStateX;
		}

		public String getAuditReasonX() {
			return auditReasonX;
		}

		public void setAuditReasonX(String auditReasonX) {
			this.auditReasonX = auditReasonX;
		}

		public String getOperatorCardforntSrcX() {
			return operatorCardforntSrcX;
		}

		public void setOperatorCardforntSrcX(String operatorCardforntSrcX) {
			this.operatorCardforntSrcX = operatorCardforntSrcX;
		}

		public String getOperatorCardrearSrcX() {
			return operatorCardrearSrcX;
		}

		public void setOperatorCardrearSrcX(String operatorCardrearSrcX) {
			this.operatorCardrearSrcX = operatorCardrearSrcX;
		}

		public String getStaContIndexSrcX() {
			return staContIndexSrcX;
		}

		public void setStaContIndexSrcX(String staContIndexSrcX) {
			this.staContIndexSrcX = staContIndexSrcX;
		}

		public String getStaContLastSrcX() {
			return staContLastSrcX;
		}

		public void setStaContLastSrcX(String staContLastSrcX) {
			this.staContLastSrcX = staContLastSrcX;
		}

		public String getFareAuthorizationSrcX() {
			return fareAuthorizationSrcX;
		}

		public void setFareAuthorizationSrcX(String fareAuthorizationSrcX) {
			this.fareAuthorizationSrcX = fareAuthorizationSrcX;
		}

		public String getOperatorNameX() {
			return operatorNameX;
		}

		public void setOperatorNameX(String operatorNameX) {
			this.operatorNameX = operatorNameX;
		}

		public String getOperatorMobileX() {
			return operatorMobileX;
		}

		public void setOperatorMobileX(String operatorMobileX) {
			this.operatorMobileX = operatorMobileX;
		}

		public String getOperatorIdNumX() {
			return operatorIdNumX;
		}

		public void setOperatorIdNumX(String operatorIdNumX) {
			this.operatorIdNumX = operatorIdNumX;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getLng() {
			return lng;
		}

		public void setLng(String lng) {
			this.lng = lng;
		}

		public String getGeogPosition() {
			return geogPosition;
		}

		public void setGeogPosition(String geogPosition) {
			this.geogPosition = geogPosition;
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
	}

	public static class DataBean  implements Serializable  {
		/**
		 * auditReason : asd
		 * auditState : 0
		 * balanceString : 600700.00
		 * dynaMinimum : 0
		 * id : ede55b6246cc4f299ee155fe7e86fc4b
		 * loginName : gd1905611016
		 * minimum : 50000
		 * mobile : 13687343431
		 * name : 楚乔
		 * office : {"address":"深圳北站详细地址","agent":"0","agentCompanyName":"深圳北站","agentType":"0","area":{"code":"0561","id":"138","name":"淮北市","parent":{"code":"ah12","id":"13","name":"安徽","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,13,","type":"3"},"code":"gd1905611016","corpCardforntSrc":"/userfiles/gd1905611016/images/e70f5058f272cf30aa165ba72a061a78.png","corpCardrearSrc":"/userfiles/gd1905611016/images/661353273e910189f239ccaa2cdb25bf.png","corpIdNum":"451028199212075277","corpLicenceSrc":"/userfiles/gd1905611016/images/7525287b113cda329c571f7e3f021a63.png","corpMobile":"15575366041","corpName":"楚乔","geogPosition":"深圳北站详细地址","id":"8f14c43b433c4c19993bddc3f661b3bd","jurisArea":{"code":"91","id":"91","name":"广州南","parent":{"code":"9","id":"9","name":"广州地区","parent":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1","type":"2"},"parentIds":"0,1,9","type":"3"},"lat":"22.573631","lng":"114.12312","master":"楚鼬","name":"深圳北站","operatorCardforntSrc":"/userfiles/gd1905611016/images/e70f5058f272cf30aa165ba72a061a78.png","operatorCardrearSrc":"/userfiles/gd1905611016/images/661353273e910189f239ccaa2cdb25bf.png","operatorIdNum":"430681199512036548","operatorMobile":"15575366041","operatorName":"楚乔","parent":{"address":"地址","area":{"code":"020","id":"233","name":"广州市","parent":{"code":"gd19","id":"20","name":"广东","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,20,","type":"3"},"code":"gzntrain","corpIdNum":"610621199312165138","corpMobile":"13100000024","corpName":"吕夜雪","id":"c5c46aec419246b994f4d08aecf8f123","jurisArea":{"code":"91","id":"91","name":"广州南","parent":{"code":"9","id":"9","name":"广州地区","parent":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1","type":"2"},"parentIds":"0,1,9","type":"3"},"master":"吕夜雪","name":"火车南车站","operatorIdNum":"420203198008108716","operatorMobile":"13100000025","operatorName":"吕夜雪","parent":{"area":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"code":"tlfj","id":"8da05512c38246099d6468bf512ac526","jurisArea":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"name":"广铁集团","parent":{"area":{"code":"ah12","id":"13","name":"安徽","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"code":"tlzj","id":"b8d79b441de04b3784c03693943b4a34","jurisArea":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"name":"铁路总局","parent":{"area":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"code":"86","corpName":"陈","id":"1","name":"O计平台","parentIds":"0,","remarks":"2015-12-01 11:22:15","root":true,"state":"0","type":"1"},"parentIds":"0,1,","root":false,"state":"0","type":"2"},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,","root":false,"state":"0","type":"2"},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,","phone":"13100000018","root":false,"state":"0","type":"2"},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,c5c46aec419246b994f4d08aecf8f123,","phone":"15580782366","root":false,"staContIndexSrc":"/userfiles/gd1905611016/images/1d1a98f934e094a49a576141afad3741.png","staContLastSrc":"/userfiles/gd1905611016/images/b79da63c42585d8f6e5971cd40052a97.png","staDepInspSrc":"/userfiles/gd1905611016/images/f365cf1634364282713721b6d18945da.png","staDepositSrc":"/userfiles/gd1905611016/images/850b5bfb86c6c9adc48935edf2014c0e.png","state":"0","type":"3","winNumber":"1016"}
		 * roleNames : 代售点管理员
		 * userType : 2
		 * isBindQQ : false
		 * isBindWx : false
		 */

		@SerializedName("auditReason")
		private String auditReasonX;
		@SerializedName("auditState")
		private String auditStateX;
		private String balanceString;
		@SerializedName("dynaMinimum")
		private double dynaMinimumX;
		@SerializedName("id")
		private String idX;
		@SerializedName("loginName")
		private String loginNameX;
		@SerializedName("minimum")
		private double minimumX;
		@SerializedName("mobile")
		private String mobileX;
		@SerializedName("name")
		private String nameX;
		private OfficeBean office;
		@SerializedName("roleNames")
		private String roleNamesX;
		@SerializedName("userType")
		private String userTypeX;
		private boolean isBindQQ;
		private boolean isBindWx;
		@SerializedName("payPasswordIsEmpty")
		private boolean payPasswordIsEmpty;
		public  void setPayPasswordIsEmpty(boolean payPasswordIsEmpty){
			this.payPasswordIsEmpty = payPasswordIsEmpty;
		}

		public  boolean getPayPasswordIsEmpty(){
			return  this.payPasswordIsEmpty;
		}
		public String getAuditReasonX() {
			return auditReasonX;
		}

		public void setAuditReasonX(String auditReasonX) {
			this.auditReasonX = auditReasonX;
		}

		public String getAuditStateX() {
			return auditStateX;
		}

		public void setAuditStateX(String auditStateX) {
			this.auditStateX = auditStateX;
		}

		public String getBalanceString() {
			return balanceString;
		}

		public void setBalanceString(String balanceString) {
			this.balanceString = balanceString;
		}

		public double getDynaMinimumX() {
			return dynaMinimumX;
		}

		public void setDynaMinimumX(double dynaMinimumX) {
			this.dynaMinimumX = dynaMinimumX;
		}

		public String getIdX() {
			return idX;
		}

		public void setIdX(String idX) {
			this.idX = idX;
		}

		public String getLoginNameX() {
			return loginNameX;
		}

		public void setLoginNameX(String loginNameX) {
			this.loginNameX = loginNameX;
		}

		public double getMinimumX() {
			return minimumX;
		}

		public void setMinimumX(double minimumX) {
			this.minimumX = minimumX;
		}

		public String getMobileX() {
			return mobileX;
		}

		public void setMobileX(String mobileX) {
			this.mobileX = mobileX;
		}

		public String getNameX() {
			return nameX;
		}

		public void setNameX(String nameX) {
			this.nameX = nameX;
		}

		public OfficeBean getOffice() {
			return office;
		}

		public void setOffice(OfficeBean office) {
			this.office = office;
		}

		public String getRoleNamesX() {
			return roleNamesX;
		}

		public void setRoleNamesX(String roleNamesX) {
			this.roleNamesX = roleNamesX;
		}

		public String getUserTypeX() {
			return userTypeX;
		}

		public void setUserTypeX(String userTypeX) {
			this.userTypeX = userTypeX;
		}

		public boolean isIsBindQQ() {
			return isBindQQ;
		}

		public void setIsBindQQ(boolean isBindQQ) {
			this.isBindQQ = isBindQQ;
		}

		public boolean isIsBindWx() {
			return isBindWx;
		}

		public void setIsBindWx(boolean isBindWx) {
			this.isBindWx = isBindWx;
		}

		public static class OfficeBean implements Serializable {
			/**
			 * address : 深圳北站详细地址
			 * agent : 0
			 * agentCompanyName : 深圳北站
			 * agentType : 0
			 * area : {"code":"0561","id":"138","name":"淮北市","parent":{"code":"ah12","id":"13","name":"安徽","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,13,","type":"3"}
			 * code : gd1905611016
			 * corpCardforntSrc : /userfiles/gd1905611016/images/e70f5058f272cf30aa165ba72a061a78.png
			 * corpCardrearSrc : /userfiles/gd1905611016/images/661353273e910189f239ccaa2cdb25bf.png
			 * corpIdNum : 451028199212075277
			 * corpLicenceSrc : /userfiles/gd1905611016/images/7525287b113cda329c571f7e3f021a63.png
			 * corpMobile : 15575366041
			 * corpName : 楚乔
			 * geogPosition : 深圳北站详细地址
			 * id : 8f14c43b433c4c19993bddc3f661b3bd
			 * jurisArea : {"code":"91","id":"91","name":"广州南","parent":{"code":"9","id":"9","name":"广州地区","parent":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1","type":"2"},"parentIds":"0,1,9","type":"3"}
			 * lat : 22.573631
			 * lng : 114.12312
			 * master : 楚鼬
			 * name : 深圳北站
			 * operatorCardforntSrc : /userfiles/gd1905611016/images/e70f5058f272cf30aa165ba72a061a78.png
			 * operatorCardrearSrc : /userfiles/gd1905611016/images/661353273e910189f239ccaa2cdb25bf.png
			 * operatorIdNum : 430681199512036548
			 * operatorMobile : 15575366041
			 * operatorName : 楚乔
			 * parent : {"address":"地址","area":{"code":"020","id":"233","name":"广州市","parent":{"code":"gd19","id":"20","name":"广东","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,20,","type":"3"},"code":"gzntrain","corpIdNum":"610621199312165138","corpMobile":"13100000024","corpName":"吕夜雪","id":"c5c46aec419246b994f4d08aecf8f123","jurisArea":{"code":"91","id":"91","name":"广州南","parent":{"code":"9","id":"9","name":"广州地区","parent":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1","type":"2"},"parentIds":"0,1,9","type":"3"},"master":"吕夜雪","name":"火车南车站","operatorIdNum":"420203198008108716","operatorMobile":"13100000025","operatorName":"吕夜雪","parent":{"area":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"code":"tlfj","id":"8da05512c38246099d6468bf512ac526","jurisArea":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"name":"广铁集团","parent":{"area":{"code":"ah12","id":"13","name":"安徽","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"code":"tlzj","id":"b8d79b441de04b3784c03693943b4a34","jurisArea":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"name":"铁路总局","parent":{"area":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"code":"86","corpName":"陈","id":"1","name":"O计平台","parentIds":"0,","remarks":"2015-12-01 11:22:15","root":true,"state":"0","type":"1"},"parentIds":"0,1,","root":false,"state":"0","type":"2"},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,","root":false,"state":"0","type":"2"},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,","phone":"13100000018","root":false,"state":"0","type":"2"}
			 * parentIds : 0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,c5c46aec419246b994f4d08aecf8f123,
			 * phone : 15580782366
			 * root : false
			 * staContIndexSrc : /userfiles/gd1905611016/images/1d1a98f934e094a49a576141afad3741.png
			 * staContLastSrc : /userfiles/gd1905611016/images/b79da63c42585d8f6e5971cd40052a97.png
			 * staDepInspSrc : /userfiles/gd1905611016/images/f365cf1634364282713721b6d18945da.png
			 * staDepositSrc : /userfiles/gd1905611016/images/850b5bfb86c6c9adc48935edf2014c0e.png
			 * state : 0
			 * type : 3
			 * winNumber : 1016
			 */

			@SerializedName("address")
			private String addressX;
			@SerializedName("agent")
			private String agentX;
			@SerializedName("agentCompanyName")
			private String agentCompanyNameX;
			@SerializedName("agentType")
			private String agentTypeX;
			private AreaBean area;
			private String code;
			private String corpCardforntSrc;
			private String corpCardrearSrc;
			@SerializedName("corpIdNum")
			private String corpIdNumX;
			private String corpLicenceSrc;
			@SerializedName("corpMobile")
			private String corpMobileX;
			@SerializedName("corpName")
			private String corpNameX;
			private String geogPosition;
			@SerializedName("id")
			private String idX;
			@SerializedName("jurisArea")
			private JurisAreaBean jurisAreaX;
			private String lat;
			private String lng;
			@SerializedName("master")
			private String masterX;
			@SerializedName("name")
			private String nameX;
			@SerializedName("operatorCardforntSrc")
			private String operatorCardforntSrcX;
			@SerializedName("operatorCardrearSrc")
			private String operatorCardrearSrcX;
			@SerializedName("operatorIdNum")
			private String operatorIdNumX;
			@SerializedName("operatorMobile")
			private String operatorMobileX;
			@SerializedName("operatorName")
			private String operatorNameX;
			private ParentBeanXXXXXXXXXXXX parent;
			private String parentIds;
			private String phone;
			private boolean root;
			@SerializedName("staContIndexSrc")
			private String staContIndexSrcX;
			@SerializedName("staContLastSrc")
			private String staContLastSrcX;
			@SerializedName("staDepInspSrc")
			private String staDepInspSrcX;
			@SerializedName("staDepositSrc")
			private String staDepositSrcX;
			private String state;
			private String type;
			@SerializedName("winNumber")
			private String winNumberX;

			public String getAddressX() {
				return addressX;
			}

			public void setAddressX(String addressX) {
				this.addressX = addressX;
			}

			public String getAgentX() {
				return agentX;
			}

			public void setAgentX(String agentX) {
				this.agentX = agentX;
			}

			public String getAgentCompanyNameX() {
				return agentCompanyNameX;
			}

			public void setAgentCompanyNameX(String agentCompanyNameX) {
				this.agentCompanyNameX = agentCompanyNameX;
			}

			public String getAgentTypeX() {
				return agentTypeX;
			}

			public void setAgentTypeX(String agentTypeX) {
				this.agentTypeX = agentTypeX;
			}

			public AreaBean getArea() {
				return area;
			}

			public void setArea(AreaBean area) {
				this.area = area;
			}

			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			public String getCorpCardforntSrc() {
				return corpCardforntSrc;
			}

			public void setCorpCardforntSrc(String corpCardforntSrc) {
				this.corpCardforntSrc = corpCardforntSrc;
			}

			public String getCorpCardrearSrc() {
				return corpCardrearSrc;
			}

			public void setCorpCardrearSrc(String corpCardrearSrc) {
				this.corpCardrearSrc = corpCardrearSrc;
			}

			public String getCorpIdNumX() {
				return corpIdNumX;
			}

			public void setCorpIdNumX(String corpIdNumX) {
				this.corpIdNumX = corpIdNumX;
			}

			public String getCorpLicenceSrc() {
				return corpLicenceSrc;
			}

			public void setCorpLicenceSrc(String corpLicenceSrc) {
				this.corpLicenceSrc = corpLicenceSrc;
			}

			public String getCorpMobileX() {
				return corpMobileX;
			}

			public void setCorpMobileX(String corpMobileX) {
				this.corpMobileX = corpMobileX;
			}

			public String getCorpNameX() {
				return corpNameX;
			}

			public void setCorpNameX(String corpNameX) {
				this.corpNameX = corpNameX;
			}

			public String getGeogPosition() {
				return geogPosition;
			}

			public void setGeogPosition(String geogPosition) {
				this.geogPosition = geogPosition;
			}

			public String getIdX() {
				return idX;
			}

			public void setIdX(String idX) {
				this.idX = idX;
			}

			public JurisAreaBean getJurisAreaX() {
				return jurisAreaX;
			}

			public void setJurisAreaX(JurisAreaBean jurisAreaX) {
				this.jurisAreaX = jurisAreaX;
			}

			public String getLat() {
				return lat;
			}

			public void setLat(String lat) {
				this.lat = lat;
			}

			public String getLng() {
				return lng;
			}

			public void setLng(String lng) {
				this.lng = lng;
			}

			public String getMasterX() {
				return masterX;
			}

			public void setMasterX(String masterX) {
				this.masterX = masterX;
			}

			public String getNameX() {
				return nameX;
			}

			public void setNameX(String nameX) {
				this.nameX = nameX;
			}

			public String getOperatorCardforntSrcX() {
				return operatorCardforntSrcX;
			}

			public void setOperatorCardforntSrcX(String operatorCardforntSrcX) {
				this.operatorCardforntSrcX = operatorCardforntSrcX;
			}

			public String getOperatorCardrearSrcX() {
				return operatorCardrearSrcX;
			}

			public void setOperatorCardrearSrcX(String operatorCardrearSrcX) {
				this.operatorCardrearSrcX = operatorCardrearSrcX;
			}

			public String getOperatorIdNumX() {
				return operatorIdNumX;
			}

			public void setOperatorIdNumX(String operatorIdNumX) {
				this.operatorIdNumX = operatorIdNumX;
			}

			public String getOperatorMobileX() {
				return operatorMobileX;
			}

			public void setOperatorMobileX(String operatorMobileX) {
				this.operatorMobileX = operatorMobileX;
			}

			public String getOperatorNameX() {
				return operatorNameX;
			}

			public void setOperatorNameX(String operatorNameX) {
				this.operatorNameX = operatorNameX;
			}

			public ParentBeanXXXXXXXXXXXX getParent() {
				return parent;
			}

			public void setParent(ParentBeanXXXXXXXXXXXX parent) {
				this.parent = parent;
			}

			public String getParentIds() {
				return parentIds;
			}

			public void setParentIds(String parentIds) {
				this.parentIds = parentIds;
			}

			public String getPhone() {
				return phone;
			}

			public void setPhone(String phone) {
				this.phone = phone;
			}

			public boolean isRoot() {
				return root;
			}

			public void setRoot(boolean root) {
				this.root = root;
			}

			public String getStaContIndexSrcX() {
				return staContIndexSrcX;
			}

			public void setStaContIndexSrcX(String staContIndexSrcX) {
				this.staContIndexSrcX = staContIndexSrcX;
			}

			public String getStaContLastSrcX() {
				return staContLastSrcX;
			}

			public void setStaContLastSrcX(String staContLastSrcX) {
				this.staContLastSrcX = staContLastSrcX;
			}

			public String getStaDepInspSrcX() {
				return staDepInspSrcX;
			}

			public void setStaDepInspSrcX(String staDepInspSrcX) {
				this.staDepInspSrcX = staDepInspSrcX;
			}

			public String getStaDepositSrcX() {
				return staDepositSrcX;
			}

			public void setStaDepositSrcX(String staDepositSrcX) {
				this.staDepositSrcX = staDepositSrcX;
			}

			public String getState() {
				return state;
			}

			public void setState(String state) {
				this.state = state;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getWinNumberX() {
				return winNumberX;
			}

			public void setWinNumberX(String winNumberX) {
				this.winNumberX = winNumberX;
			}

			public static class AreaBean  implements Serializable  {
				/**
				 * code : 0561
				 * id : 138
				 * name : 淮北市
				 * parent : {"code":"ah12","id":"13","name":"安徽","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"}
				 * parentIds : 0,1,13,
				 * type : 3
				 */

				private String code;
				@SerializedName("id")
				private String idX;
				@SerializedName("name")
				private String nameX;
				private ParentBeanX parent;
				private String parentIds;
				private String type;

				public String getCode() {
					return code;
				}

				public void setCode(String code) {
					this.code = code;
				}

				public String getIdX() {
					return idX;
				}

				public void setIdX(String idX) {
					this.idX = idX;
				}

				public String getNameX() {
					return nameX;
				}

				public void setNameX(String nameX) {
					this.nameX = nameX;
				}

				public ParentBeanX getParent() {
					return parent;
				}

				public void setParent(ParentBeanX parent) {
					this.parent = parent;
				}

				public String getParentIds() {
					return parentIds;
				}

				public void setParentIds(String parentIds) {
					this.parentIds = parentIds;
				}

				public String getType() {
					return type;
				}

				public void setType(String type) {
					this.type = type;
				}

				public static class ParentBeanX  implements Serializable  {
					/**
					 * code : ah12
					 * id : 13
					 * name : 安徽
					 * parent : {"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"}
					 * parentIds : 0,1,
					 * type : 2
					 */

					private String code;
					@SerializedName("id")
					private String idX;
					@SerializedName("name")
					private String nameX;
					private ParentBean parent;
					private String parentIds;
					private String type;

					public String getCode() {
						return code;
					}

					public void setCode(String code) {
						this.code = code;
					}

					public String getIdX() {
						return idX;
					}

					public void setIdX(String idX) {
						this.idX = idX;
					}

					public String getNameX() {
						return nameX;
					}

					public void setNameX(String nameX) {
						this.nameX = nameX;
					}

					public ParentBean getParent() {
						return parent;
					}

					public void setParent(ParentBean parent) {
						this.parent = parent;
					}

					public String getParentIds() {
						return parentIds;
					}

					public void setParentIds(String parentIds) {
						this.parentIds = parentIds;
					}

					public String getType() {
						return type;
					}

					public void setType(String type) {
						this.type = type;
					}

					public static class ParentBean  implements Serializable  {
						/**
						 * code : 00
						 * id : 1
						 * name : 中国
						 * parentIds : 0,
						 * type : 1
						 */

						private String code;
						@SerializedName("id")
						private String idX;
						@SerializedName("name")
						private String nameX;
						private String parentIds;
						private String type;

						public String getCode() {
							return code;
						}

						public void setCode(String code) {
							this.code = code;
						}

						public String getIdX() {
							return idX;
						}

						public void setIdX(String idX) {
							this.idX = idX;
						}

						public String getNameX() {
							return nameX;
						}

						public void setNameX(String nameX) {
							this.nameX = nameX;
						}

						public String getParentIds() {
							return parentIds;
						}

						public void setParentIds(String parentIds) {
							this.parentIds = parentIds;
						}

						public String getType() {
							return type;
						}

						public void setType(String type) {
							this.type = type;
						}
					}
				}
			}

			public static class JurisAreaBean  implements Serializable  {
				/**
				 * code : 91
				 * id : 91
				 * name : 广州南
				 * parent : {"code":"9","id":"9","name":"广州地区","parent":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1","type":"2"}
				 * parentIds : 0,1,9
				 * type : 3
				 */

				private String code;
				@SerializedName("id")
				private String idX;
				@SerializedName("name")
				private String nameX;
				private ParentBeanXXX parent;
				private String parentIds;
				private String type;

				public String getCode() {
					return code;
				}

				public void setCode(String code) {
					this.code = code;
				}

				public String getIdX() {
					return idX;
				}

				public void setIdX(String idX) {
					this.idX = idX;
				}

				public String getNameX() {
					return nameX;
				}

				public void setNameX(String nameX) {
					this.nameX = nameX;
				}

				public ParentBeanXXX getParent() {
					return parent;
				}

				public void setParent(ParentBeanXXX parent) {
					this.parent = parent;
				}

				public String getParentIds() {
					return parentIds;
				}

				public void setParentIds(String parentIds) {
					this.parentIds = parentIds;
				}

				public String getType() {
					return type;
				}

				public void setType(String type) {
					this.type = type;
				}

				public static class ParentBeanXXX   implements Serializable {
					/**
					 * code : 9
					 * id : 9
					 * name : 广州地区
					 * parent : {"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"}
					 * parentIds : 0,1
					 * type : 2
					 */

					private String code;
					@SerializedName("id")
					private String idX;
					@SerializedName("name")
					private String nameX;
					private ParentBeanXX parent;
					private String parentIds;
					private String type;

					public String getCode() {
						return code;
					}

					public void setCode(String code) {
						this.code = code;
					}

					public String getIdX() {
						return idX;
					}

					public void setIdX(String idX) {
						this.idX = idX;
					}

					public String getNameX() {
						return nameX;
					}

					public void setNameX(String nameX) {
						this.nameX = nameX;
					}

					public ParentBeanXX getParent() {
						return parent;
					}

					public void setParent(ParentBeanXX parent) {
						this.parent = parent;
					}

					public String getParentIds() {
						return parentIds;
					}

					public void setParentIds(String parentIds) {
						this.parentIds = parentIds;
					}

					public String getType() {
						return type;
					}

					public void setType(String type) {
						this.type = type;
					}

					public static class ParentBeanXX   implements Serializable {
						/**
						 * code : 00
						 * id : 1
						 * name : 广州铁路集团
						 * parentIds : 0,
						 * type : 1
						 */

						private String code;
						@SerializedName("id")
						private String idX;
						@SerializedName("name")
						private String nameX;
						private String parentIds;
						private String type;

						public String getCode() {
							return code;
						}

						public void setCode(String code) {
							this.code = code;
						}

						public String getIdX() {
							return idX;
						}

						public void setIdX(String idX) {
							this.idX = idX;
						}

						public String getNameX() {
							return nameX;
						}

						public void setNameX(String nameX) {
							this.nameX = nameX;
						}

						public String getParentIds() {
							return parentIds;
						}

						public void setParentIds(String parentIds) {
							this.parentIds = parentIds;
						}

						public String getType() {
							return type;
						}

						public void setType(String type) {
							this.type = type;
						}
					}
				}
			}

			public static class ParentBeanXXXXXXXXXXXX  implements Serializable  {
				/**
				 * address : 地址
				 * area : {"code":"020","id":"233","name":"广州市","parent":{"code":"gd19","id":"20","name":"广东","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,20,","type":"3"}
				 * code : gzntrain
				 * corpIdNum : 610621199312165138
				 * corpMobile : 13100000024
				 * corpName : 吕夜雪
				 * id : c5c46aec419246b994f4d08aecf8f123
				 * jurisArea : {"code":"91","id":"91","name":"广州南","parent":{"code":"9","id":"9","name":"广州地区","parent":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1","type":"2"},"parentIds":"0,1,9","type":"3"}
				 * master : 吕夜雪
				 * name : 火车南车站
				 * operatorIdNum : 420203198008108716
				 * operatorMobile : 13100000025
				 * operatorName : 吕夜雪
				 * parent : {"area":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"code":"tlfj","id":"8da05512c38246099d6468bf512ac526","jurisArea":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"name":"广铁集团","parent":{"area":{"code":"ah12","id":"13","name":"安徽","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"code":"tlzj","id":"b8d79b441de04b3784c03693943b4a34","jurisArea":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"name":"铁路总局","parent":{"area":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"code":"86","corpName":"陈","id":"1","name":"O计平台","parentIds":"0,","remarks":"2015-12-01 11:22:15","root":true,"state":"0","type":"1"},"parentIds":"0,1,","root":false,"state":"0","type":"2"},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,","root":false,"state":"0","type":"2"}
				 * parentIds : 0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,
				 * phone : 13100000018
				 * root : false
				 * state : 0
				 * type : 2
				 */

				@SerializedName("address")
				private String addressX;
				private AreaBeanX area;
				private String code;
				@SerializedName("corpIdNum")
				private String corpIdNumX;
				@SerializedName("corpMobile")
				private String corpMobileX;
				@SerializedName("corpName")
				private String corpNameX;
				@SerializedName("id")
				private String idX;
				@SerializedName("jurisArea")
				private JurisAreaBeanX jurisAreaX;
				@SerializedName("master")
				private String masterX;
				@SerializedName("name")
				private String nameX;
				@SerializedName("operatorIdNum")
				private String operatorIdNumX;
				@SerializedName("operatorMobile")
				private String operatorMobileX;
				@SerializedName("operatorName")
				private String operatorNameX;
				private ParentBeanXXXXXXXXXXX parent;
				private String parentIds;
				private String phone;
				private boolean root;
				private String state;
				private String type;

				public String getAddressX() {
					return addressX;
				}

				public void setAddressX(String addressX) {
					this.addressX = addressX;
				}

				public AreaBeanX getArea() {
					return area;
				}

				public void setArea(AreaBeanX area) {
					this.area = area;
				}

				public String getCode() {
					return code;
				}

				public void setCode(String code) {
					this.code = code;
				}

				public String getCorpIdNumX() {
					return corpIdNumX;
				}

				public void setCorpIdNumX(String corpIdNumX) {
					this.corpIdNumX = corpIdNumX;
				}

				public String getCorpMobileX() {
					return corpMobileX;
				}

				public void setCorpMobileX(String corpMobileX) {
					this.corpMobileX = corpMobileX;
				}

				public String getCorpNameX() {
					return corpNameX;
				}

				public void setCorpNameX(String corpNameX) {
					this.corpNameX = corpNameX;
				}

				public String getIdX() {
					return idX;
				}

				public void setIdX(String idX) {
					this.idX = idX;
				}

				public JurisAreaBeanX getJurisAreaX() {
					return jurisAreaX;
				}

				public void setJurisAreaX(JurisAreaBeanX jurisAreaX) {
					this.jurisAreaX = jurisAreaX;
				}

				public String getMasterX() {
					return masterX;
				}

				public void setMasterX(String masterX) {
					this.masterX = masterX;
				}

				public String getNameX() {
					return nameX;
				}

				public void setNameX(String nameX) {
					this.nameX = nameX;
				}

				public String getOperatorIdNumX() {
					return operatorIdNumX;
				}

				public void setOperatorIdNumX(String operatorIdNumX) {
					this.operatorIdNumX = operatorIdNumX;
				}

				public String getOperatorMobileX() {
					return operatorMobileX;
				}

				public void setOperatorMobileX(String operatorMobileX) {
					this.operatorMobileX = operatorMobileX;
				}

				public String getOperatorNameX() {
					return operatorNameX;
				}

				public void setOperatorNameX(String operatorNameX) {
					this.operatorNameX = operatorNameX;
				}

				public ParentBeanXXXXXXXXXXX getParent() {
					return parent;
				}

				public void setParent(ParentBeanXXXXXXXXXXX parent) {
					this.parent = parent;
				}

				public String getParentIds() {
					return parentIds;
				}

				public void setParentIds(String parentIds) {
					this.parentIds = parentIds;
				}

				public String getPhone() {
					return phone;
				}

				public void setPhone(String phone) {
					this.phone = phone;
				}

				public boolean isRoot() {
					return root;
				}

				public void setRoot(boolean root) {
					this.root = root;
				}

				public String getState() {
					return state;
				}

				public void setState(String state) {
					this.state = state;
				}

				public String getType() {
					return type;
				}

				public void setType(String type) {
					this.type = type;
				}

				public static class AreaBeanX   implements Serializable {
					/**
					 * code : 020
					 * id : 233
					 * name : 广州市
					 * parent : {"code":"gd19","id":"20","name":"广东","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"}
					 * parentIds : 0,1,20,
					 * type : 3
					 */

					private String code;
					@SerializedName("id")
					private String idX;
					@SerializedName("name")
					private String nameX;
					private ParentBeanXXXXX parent;
					private String parentIds;
					private String type;

					public String getCode() {
						return code;
					}

					public void setCode(String code) {
						this.code = code;
					}

					public String getIdX() {
						return idX;
					}

					public void setIdX(String idX) {
						this.idX = idX;
					}

					public String getNameX() {
						return nameX;
					}

					public void setNameX(String nameX) {
						this.nameX = nameX;
					}

					public ParentBeanXXXXX getParent() {
						return parent;
					}

					public void setParent(ParentBeanXXXXX parent) {
						this.parent = parent;
					}

					public String getParentIds() {
						return parentIds;
					}

					public void setParentIds(String parentIds) {
						this.parentIds = parentIds;
					}

					public String getType() {
						return type;
					}

					public void setType(String type) {
						this.type = type;
					}

					public static class ParentBeanXXXXX   implements Serializable {
						/**
						 * code : gd19
						 * id : 20
						 * name : 广东
						 * parent : {"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"}
						 * parentIds : 0,1,
						 * type : 2
						 */

						private String code;
						@SerializedName("id")
						private String idX;
						@SerializedName("name")
						private String nameX;
						private ParentBeanXXXX parent;
						private String parentIds;
						private String type;

						public String getCode() {
							return code;
						}

						public void setCode(String code) {
							this.code = code;
						}

						public String getIdX() {
							return idX;
						}

						public void setIdX(String idX) {
							this.idX = idX;
						}

						public String getNameX() {
							return nameX;
						}

						public void setNameX(String nameX) {
							this.nameX = nameX;
						}

						public ParentBeanXXXX getParent() {
							return parent;
						}

						public void setParent(ParentBeanXXXX parent) {
							this.parent = parent;
						}

						public String getParentIds() {
							return parentIds;
						}

						public void setParentIds(String parentIds) {
							this.parentIds = parentIds;
						}

						public String getType() {
							return type;
						}

						public void setType(String type) {
							this.type = type;
						}

						public static class ParentBeanXXXX  implements Serializable  {
							/**
							 * code : 00
							 * id : 1
							 * name : 中国
							 * parentIds : 0,
							 * type : 1
							 */

							private String code;
							@SerializedName("id")
							private String idX;
							@SerializedName("name")
							private String nameX;
							private String parentIds;
							private String type;

							public String getCode() {
								return code;
							}

							public void setCode(String code) {
								this.code = code;
							}

							public String getIdX() {
								return idX;
							}

							public void setIdX(String idX) {
								this.idX = idX;
							}

							public String getNameX() {
								return nameX;
							}

							public void setNameX(String nameX) {
								this.nameX = nameX;
							}

							public String getParentIds() {
								return parentIds;
							}

							public void setParentIds(String parentIds) {
								this.parentIds = parentIds;
							}

							public String getType() {
								return type;
							}

							public void setType(String type) {
								this.type = type;
							}
						}
					}
				}

				public static class JurisAreaBeanX  implements Serializable  {
					/**
					 * code : 91
					 * id : 91
					 * name : 广州南
					 * parent : {"code":"9","id":"9","name":"广州地区","parent":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1","type":"2"}
					 * parentIds : 0,1,9
					 * type : 3
					 */

					private String code;
					@SerializedName("id")
					private String idX;
					@SerializedName("name")
					private String nameX;
					private ParentBeanXXXXXXX parent;
					private String parentIds;
					private String type;

					public String getCode() {
						return code;
					}

					public void setCode(String code) {
						this.code = code;
					}

					public String getIdX() {
						return idX;
					}

					public void setIdX(String idX) {
						this.idX = idX;
					}

					public String getNameX() {
						return nameX;
					}

					public void setNameX(String nameX) {
						this.nameX = nameX;
					}

					public ParentBeanXXXXXXX getParent() {
						return parent;
					}

					public void setParent(ParentBeanXXXXXXX parent) {
						this.parent = parent;
					}

					public String getParentIds() {
						return parentIds;
					}

					public void setParentIds(String parentIds) {
						this.parentIds = parentIds;
					}

					public String getType() {
						return type;
					}

					public void setType(String type) {
						this.type = type;
					}

					public static class ParentBeanXXXXXXX  implements Serializable  {
						/**
						 * code : 9
						 * id : 9
						 * name : 广州地区
						 * parent : {"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"}
						 * parentIds : 0,1
						 * type : 2
						 */

						private String code;
						@SerializedName("id")
						private String idX;
						@SerializedName("name")
						private String nameX;
						private ParentBeanXXXXXX parent;
						private String parentIds;
						private String type;

						public String getCode() {
							return code;
						}

						public void setCode(String code) {
							this.code = code;
						}

						public String getIdX() {
							return idX;
						}

						public void setIdX(String idX) {
							this.idX = idX;
						}

						public String getNameX() {
							return nameX;
						}

						public void setNameX(String nameX) {
							this.nameX = nameX;
						}

						public ParentBeanXXXXXX getParent() {
							return parent;
						}

						public void setParent(ParentBeanXXXXXX parent) {
							this.parent = parent;
						}

						public String getParentIds() {
							return parentIds;
						}

						public void setParentIds(String parentIds) {
							this.parentIds = parentIds;
						}

						public String getType() {
							return type;
						}

						public void setType(String type) {
							this.type = type;
						}

						public static class ParentBeanXXXXXX   implements Serializable {
							/**
							 * code : 00
							 * id : 1
							 * name : 广州铁路集团
							 * parentIds : 0,
							 * type : 1
							 */

							private String code;
							@SerializedName("id")
							private String idX;
							@SerializedName("name")
							private String nameX;
							private String parentIds;
							private String type;

							public String getCode() {
								return code;
							}

							public void setCode(String code) {
								this.code = code;
							}

							public String getIdX() {
								return idX;
							}

							public void setIdX(String idX) {
								this.idX = idX;
							}

							public String getNameX() {
								return nameX;
							}

							public void setNameX(String nameX) {
								this.nameX = nameX;
							}

							public String getParentIds() {
								return parentIds;
							}

							public void setParentIds(String parentIds) {
								this.parentIds = parentIds;
							}

							public String getType() {
								return type;
							}

							public void setType(String type) {
								this.type = type;
							}
						}
					}
				}

				public static class ParentBeanXXXXXXXXXXX  implements Serializable  {
					/**
					 * area : {"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"}
					 * code : tlfj
					 * id : 8da05512c38246099d6468bf512ac526
					 * jurisArea : {"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"}
					 * name : 广铁集团
					 * parent : {"area":{"code":"ah12","id":"13","name":"安徽","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"code":"tlzj","id":"b8d79b441de04b3784c03693943b4a34","jurisArea":{"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"name":"铁路总局","parent":{"area":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"code":"86","corpName":"陈","id":"1","name":"O计平台","parentIds":"0,","remarks":"2015-12-01 11:22:15","root":true,"state":"0","type":"1"},"parentIds":"0,1,","root":false,"state":"0","type":"2"}
					 * parentIds : 0,1,b8d79b441de04b3784c03693943b4a34,
					 * root : false
					 * state : 0
					 * type : 2
					 */

					private AreaBeanXX area;
					private String code;
					@SerializedName("id")
					private String idX;
					@SerializedName("jurisArea")
					private JurisAreaBeanXX jurisAreaX;
					@SerializedName("name")
					private String nameX;
					private ParentBeanXXXXXXXXXX parent;
					private String parentIds;
					private boolean root;
					private String state;
					private String type;

					public AreaBeanXX getArea() {
						return area;
					}

					public void setArea(AreaBeanXX area) {
						this.area = area;
					}

					public String getCode() {
						return code;
					}

					public void setCode(String code) {
						this.code = code;
					}

					public String getIdX() {
						return idX;
					}

					public void setIdX(String idX) {
						this.idX = idX;
					}

					public JurisAreaBeanXX getJurisAreaX() {
						return jurisAreaX;
					}

					public void setJurisAreaX(JurisAreaBeanXX jurisAreaX) {
						this.jurisAreaX = jurisAreaX;
					}

					public String getNameX() {
						return nameX;
					}

					public void setNameX(String nameX) {
						this.nameX = nameX;
					}

					public ParentBeanXXXXXXXXXX getParent() {
						return parent;
					}

					public void setParent(ParentBeanXXXXXXXXXX parent) {
						this.parent = parent;
					}

					public String getParentIds() {
						return parentIds;
					}

					public void setParentIds(String parentIds) {
						this.parentIds = parentIds;
					}

					public boolean isRoot() {
						return root;
					}

					public void setRoot(boolean root) {
						this.root = root;
					}

					public String getState() {
						return state;
					}

					public void setState(String state) {
						this.state = state;
					}

					public String getType() {
						return type;
					}

					public void setType(String type) {
						this.type = type;
					}

					public static class AreaBeanXX   implements Serializable {
						/**
						 * code : 00
						 * id : 1
						 * name : 中国
						 * parentIds : 0,
						 * type : 1
						 */

						private String code;
						@SerializedName("id")
						private String idX;
						@SerializedName("name")
						private String nameX;
						private String parentIds;
						private String type;

						public String getCode() {
							return code;
						}

						public void setCode(String code) {
							this.code = code;
						}

						public String getIdX() {
							return idX;
						}

						public void setIdX(String idX) {
							this.idX = idX;
						}

						public String getNameX() {
							return nameX;
						}

						public void setNameX(String nameX) {
							this.nameX = nameX;
						}

						public String getParentIds() {
							return parentIds;
						}

						public void setParentIds(String parentIds) {
							this.parentIds = parentIds;
						}

						public String getType() {
							return type;
						}

						public void setType(String type) {
							this.type = type;
						}
					}

					public static class JurisAreaBeanXX  implements Serializable  {
						/**
						 * code : 00
						 * id : 1
						 * name : 广州铁路集团
						 * parentIds : 0,
						 * type : 1
						 */

						private String code;
						@SerializedName("id")
						private String idX;
						@SerializedName("name")
						private String nameX;
						private String parentIds;
						private String type;

						public String getCode() {
							return code;
						}

						public void setCode(String code) {
							this.code = code;
						}

						public String getIdX() {
							return idX;
						}

						public void setIdX(String idX) {
							this.idX = idX;
						}

						public String getNameX() {
							return nameX;
						}

						public void setNameX(String nameX) {
							this.nameX = nameX;
						}

						public String getParentIds() {
							return parentIds;
						}

						public void setParentIds(String parentIds) {
							this.parentIds = parentIds;
						}

						public String getType() {
							return type;
						}

						public void setType(String type) {
							this.type = type;
						}
					}

					public static class ParentBeanXXXXXXXXXX  implements Serializable  {
						/**
						 * area : {"code":"ah12","id":"13","name":"安徽","parent":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"}
						 * code : tlzj
						 * id : b8d79b441de04b3784c03693943b4a34
						 * jurisArea : {"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"}
						 * name : 铁路总局
						 * parent : {"area":{"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"},"code":"86","corpName":"陈","id":"1","name":"O计平台","parentIds":"0,","remarks":"2015-12-01 11:22:15","root":true,"state":"0","type":"1"}
						 * parentIds : 0,1,
						 * root : false
						 * state : 0
						 * type : 2
						 */

						private AreaBeanXXX area;
						private String code;
						@SerializedName("id")
						private String idX;
						@SerializedName("jurisArea")
						private JurisAreaBeanXXX jurisAreaX;
						@SerializedName("name")
						private String nameX;
						private ParentBeanXXXXXXXXX parent;
						private String parentIds;
						private boolean root;
						private String state;
						private String type;

						public AreaBeanXXX getArea() {
							return area;
						}

						public void setArea(AreaBeanXXX area) {
							this.area = area;
						}

						public String getCode() {
							return code;
						}

						public void setCode(String code) {
							this.code = code;
						}

						public String getIdX() {
							return idX;
						}

						public void setIdX(String idX) {
							this.idX = idX;
						}

						public JurisAreaBeanXXX getJurisAreaX() {
							return jurisAreaX;
						}

						public void setJurisAreaX(JurisAreaBeanXXX jurisAreaX) {
							this.jurisAreaX = jurisAreaX;
						}

						public String getNameX() {
							return nameX;
						}

						public void setNameX(String nameX) {
							this.nameX = nameX;
						}

						public ParentBeanXXXXXXXXX getParent() {
							return parent;
						}

						public void setParent(ParentBeanXXXXXXXXX parent) {
							this.parent = parent;
						}

						public String getParentIds() {
							return parentIds;
						}

						public void setParentIds(String parentIds) {
							this.parentIds = parentIds;
						}

						public boolean isRoot() {
							return root;
						}

						public void setRoot(boolean root) {
							this.root = root;
						}

						public String getState() {
							return state;
						}

						public void setState(String state) {
							this.state = state;
						}

						public String getType() {
							return type;
						}

						public void setType(String type) {
							this.type = type;
						}

						public static class AreaBeanXXX  implements Serializable {
							/**
							 * code : ah12
							 * id : 13
							 * name : 安徽
							 * parent : {"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"}
							 * parentIds : 0,1,
							 * type : 2
							 */

							private String code;
							@SerializedName("id")
							private String idX;
							@SerializedName("name")
							private String nameX;
							private ParentBeanXXXXXXXX parent;
							private String parentIds;
							private String type;

							public String getCode() {
								return code;
							}

							public void setCode(String code) {
								this.code = code;
							}

							public String getIdX() {
								return idX;
							}

							public void setIdX(String idX) {
								this.idX = idX;
							}

							public String getNameX() {
								return nameX;
							}

							public void setNameX(String nameX) {
								this.nameX = nameX;
							}

							public ParentBeanXXXXXXXX getParent() {
								return parent;
							}

							public void setParent(ParentBeanXXXXXXXX parent) {
								this.parent = parent;
							}

							public String getParentIds() {
								return parentIds;
							}

							public void setParentIds(String parentIds) {
								this.parentIds = parentIds;
							}

							public String getType() {
								return type;
							}

							public void setType(String type) {
								this.type = type;
							}

							public static class ParentBeanXXXXXXXX   implements Serializable {
								/**
								 * code : 00
								 * id : 1
								 * name : 中国
								 * parentIds : 0,
								 * type : 1
								 */

								private String code;
								@SerializedName("id")
								private String idX;
								@SerializedName("name")
								private String nameX;
								private String parentIds;
								private String type;

								public String getCode() {
									return code;
								}

								public void setCode(String code) {
									this.code = code;
								}

								public String getIdX() {
									return idX;
								}

								public void setIdX(String idX) {
									this.idX = idX;
								}

								public String getNameX() {
									return nameX;
								}

								public void setNameX(String nameX) {
									this.nameX = nameX;
								}

								public String getParentIds() {
									return parentIds;
								}

								public void setParentIds(String parentIds) {
									this.parentIds = parentIds;
								}

								public String getType() {
									return type;
								}

								public void setType(String type) {
									this.type = type;
								}
							}
						}

						public static class JurisAreaBeanXXX  implements Serializable  {
							/**
							 * code : 00
							 * id : 1
							 * name : 广州铁路集团
							 * parentIds : 0,
							 * type : 1
							 */

							private String code;
							@SerializedName("id")
							private String idX;
							@SerializedName("name")
							private String nameX;
							private String parentIds;
							private String type;

							public String getCode() {
								return code;
							}

							public void setCode(String code) {
								this.code = code;
							}

							public String getIdX() {
								return idX;
							}

							public void setIdX(String idX) {
								this.idX = idX;
							}

							public String getNameX() {
								return nameX;
							}

							public void setNameX(String nameX) {
								this.nameX = nameX;
							}

							public String getParentIds() {
								return parentIds;
							}

							public void setParentIds(String parentIds) {
								this.parentIds = parentIds;
							}

							public String getType() {
								return type;
							}

							public void setType(String type) {
								this.type = type;
							}
						}

						public static class ParentBeanXXXXXXXXX  implements Serializable  {
							/**
							 * area : {"code":"00","id":"1","name":"中国","parentIds":"0,","type":"1"}
							 * code : 86
							 * corpName : 陈
							 * id : 1
							 * name : O计平台
							 * parentIds : 0,
							 * remarks : 2015-12-01 11:22:15
							 * root : true
							 * state : 0
							 * type : 1
							 */

							private AreaBeanXXXX area;
							private String code;
							@SerializedName("corpName")
							private String corpNameX;
							@SerializedName("id")
							private String idX;
							@SerializedName("name")
							private String nameX;
							private String parentIds;
							@SerializedName("remarks")
							private String remarksX;
							private boolean root;
							private String state;
							private String type;

							public AreaBeanXXXX getArea() {
								return area;
							}

							public void setArea(AreaBeanXXXX area) {
								this.area = area;
							}

							public String getCode() {
								return code;
							}

							public void setCode(String code) {
								this.code = code;
							}

							public String getCorpNameX() {
								return corpNameX;
							}

							public void setCorpNameX(String corpNameX) {
								this.corpNameX = corpNameX;
							}

							public String getIdX() {
								return idX;
							}

							public void setIdX(String idX) {
								this.idX = idX;
							}

							public String getNameX() {
								return nameX;
							}

							public void setNameX(String nameX) {
								this.nameX = nameX;
							}

							public String getParentIds() {
								return parentIds;
							}

							public void setParentIds(String parentIds) {
								this.parentIds = parentIds;
							}

							public String getRemarksX() {
								return remarksX;
							}

							public void setRemarksX(String remarksX) {
								this.remarksX = remarksX;
							}

							public boolean isRoot() {
								return root;
							}

							public void setRoot(boolean root) {
								this.root = root;
							}

							public String getState() {
								return state;
							}

							public void setState(String state) {
								this.state = state;
							}

							public String getType() {
								return type;
							}

							public void setType(String type) {
								this.type = type;
							}

							public static class AreaBeanXXXX   implements Serializable {
								/**
								 * code : 00
								 * id : 1
								 * name : 中国
								 * parentIds : 0,
								 * type : 1
								 */

								private String code;
								@SerializedName("id")
								private String idX;
								@SerializedName("name")
								private String nameX;
								private String parentIds;
								private String type;

								public String getCode() {
									return code;
								}

								public void setCode(String code) {
									this.code = code;
								}

								public String getIdX() {
									return idX;
								}

								public void setIdX(String idX) {
									this.idX = idX;
								}

								public String getNameX() {
									return nameX;
								}

								public void setNameX(String nameX) {
									this.nameX = nameX;
								}

								public String getParentIds() {
									return parentIds;
								}

								public void setParentIds(String parentIds) {
									this.parentIds = parentIds;
								}

								public String getType() {
									return type;
								}

								public void setType(String type) {
									this.type = type;
								}
							}
						}
					}
				}
			}
		}
	}
}

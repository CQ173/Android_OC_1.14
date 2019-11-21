package com.huoniao.oc.bean;



import org.json.JSONObject;

import java.io.Serializable;



@SuppressWarnings("serial")
public class StationOltsManage implements Serializable{
	private String id; //代售点ID
	private String code;//代售点机构编码
	private JSONObject area;//归属地市信息
	private String name;//代售点名称
	private String address;//联系地址
	private String master;//联系人
	private String phone;//联系人电话
	private String winNumber;//窗口号
	private String corpName; //法人姓名
	private String corpMobile;//法人手机
	private String corpIdNum;//法人身份证号
	private String operatorName; //负责人姓名
	private String operatorMobile; //负责人手机
	private String operatorIdNum; //负责人身份证号

	private String state;//状态 0: 正常1: 待审核2: 审核不通过
	private String city;//用于接收'area'中的所属城市
	private boolean checkFlag;//checkbox的状态
	public StationOltsManage() {
		
	}

	public StationOltsManage(String id, String code, JSONObject area, String name, String address, String master,
			String phone, String winNumber, String corpName, String corpMobile, String corpIdNum, String state, 
			String city, boolean checkFlag, String operatorName, String operatorMobile, String operatorIdNum) {
		
		this.id = id;
		this.code = code;
		this.area = area;
		this.name = name;
		this.address = address;
		this.master = master;
		this.phone = phone;
		this.winNumber = winNumber;
		this.corpName = corpName;
		this.corpMobile = corpMobile;
		this.corpIdNum = corpIdNum;
		this.state = state;
		this.city = city;
		this.checkFlag = checkFlag;
		this.operatorName = operatorName;
		this.operatorMobile = operatorMobile;
		this.operatorIdNum = operatorIdNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public JSONObject getArea() {
		return area;
	}

	public void setArea(JSONObject area) {
		this.area = area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(boolean checkFlag) {
		this.checkFlag = checkFlag;
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

	//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(id);  
//        dest.writeString(code);
//        dest.writeString(name);
//        dest.writeString(address);
//        dest.writeString(master);
//        dest.writeString(phone);
//        dest.writeString(winNumber);
//        dest.writeString(corpName);
//        dest.writeString(corpMobile);
//        dest.writeString(corpIdNum);
//        dest.writeString(state);
//        dest.writeString(city);
////      dest.writeBooleanArray(checkFlag);
////        dest.writeString(checkFlag);
//	}
//	
//	 public static final Parcelable.Creator<StationOltsManage> CREATOR  = new Creator<StationOltsManage>() {  
//	        //实现从source中创建出类的实例的功能  
//	        @Override  
//	        public StationOltsManage createFromParcel(Parcel source) {  
//	        	StationOltsManage oltsInfo  = new StationOltsManage(); 
//	        	oltsInfo.id = source.readString();
//	        	oltsInfo.code = source.readString();
//	        	oltsInfo.name = source.readString();
//	        	oltsInfo.address = source.readString(); 
//	        	oltsInfo.master = source.readString(); 
//	        	oltsInfo.phone = source.readString(); 
//	        	oltsInfo.winNumber = source.readString(); 
//	        	oltsInfo.corpName = source.readString(); 
//	        	oltsInfo.corpMobile = source.readString(); 
//	        	oltsInfo.corpIdNum = source.readString(); 
//	        	oltsInfo.state = source.readString(); 
//	        	oltsInfo.city = source.readString();
//	        	
//	            return oltsInfo;  
//	        }  
//	        //创建一个类型为T，长度为size的数组  
//	        @Override  
//	        public StationOltsManage[] newArray(int size) {  
//	            return new StationOltsManage[size];  
//	        }  
//	    };     
}

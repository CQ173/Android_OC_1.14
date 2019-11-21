package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class AdminUserBean {

    /**
     * code : 0
     * msg : 请求成功
     * size : 1
     * data : [{"id":"206899b9f59842bd90157203634982f8","office":{"id":"960ce1011eb149fd971efe79d7c8bdf7","parent":{"id":"c5c46aec419246b994f4d08aecf8f123","parent":{"id":"8da05512c38246099d6468bf512ac526","parent":{"id":"b8d79b441de04b3784c03693943b4a34","parent":{"remarks":"2015-12-01 11:22:15","id":"1","parentIds":"0,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"code":"86","name":"O计平台","type":"1","corpName":"陈","state":"0","root":true},"parentIds":"0,1,","area":{"id":"13","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"ah12","name":"安徽","type":"2"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlzj","name":"铁路总局","type":"6","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlfj","name":"广铁集团","type":"7","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,","area":{"id":"233","parent":{"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"},"parentIds":"0,1,20,","code":"020","name":"广州市","type":"3"},"jurisArea":{"id":"91","parent":{"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"},"parentIds":"0,1,9,","code":"91","name":"广州南","type":"3"},"code":"gzntrain","name":"火车南车站","type":"2","address":"地址","master":"吕夜雪","phone":"13100000018","operatorName":"吕夜雪","operatorMobile":"13100000025","operatorIdNum":"420203198008108716","corpName":"吕夜雪","corpMobile":"13100000024","corpIdNum":"610621199312165138","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,c5c46aec419246b994f4d08aecf8f123,","area":{"id":"233","parent":{"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"},"parentIds":"0,1,20,","code":"020","name":"广州市","type":"3"},"jurisArea":{"id":"91","parent":{"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"},"parentIds":"0,1,9,","code":"91","name":"广州南","type":"3"},"code":"gd190200012","name":"嘉禾街代售处","type":"3","agent":"0","agentType":"1","address":"详细地址详细地址","master":"江雁","phone":"15115012322","winNumber":"0012","operatorName":"杭其昌","operatorMobile":"18812341234","operatorIdNum":"340303199112224469","corpName":"杭其昌","corpMobile":"18812341234","corpIdNum":"340303199112224469","state":"1","lat":"23.022297","lng":"113.749473","geogPosition":"广东省东莞市东莞市市辖区莞太路63号","root":false},"loginName":"gd190200012","name":"杭其昌","mobile":"18812341234","minimum":2000,"dynaMinimum":0,"userType":"2","auditState":"1","photoSrc":"/userfiles/gd190200012/images/7cddc84776840ac30b167e53f729102c.png","officeName":"嘉禾街代售处","balanceString":"8414.00","roleNames":"待审核用户"},{"isBindQQ":true,"isBindWx":false}]
     * agencys : []
     */

    private String code;
    private String msg;
    private int size;
    private List<DataBean> data;
    private List<AgencysBean> agencys;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<AgencysBean> getAgencys() {
        return agencys;
    }

    public void setAgencys(List<AgencysBean> agencys) {
        this.agencys = agencys;
    }

    public static class AgencysBean {
        /**
         * agencyName : 中山市大涌镇博文票务中心
         * auditReason : 222
         * auditState : 0
         * geogPosition : 中山市大涌镇博文票务中心
         * id : a48c5bc14fe343e4b3931e2162d565ca
         * jurisArea : {"admin":false,"code":"91","id":"91","name":"广州南","parent":{"admin":false,"code":"9","id":"9","name":"广州地区","parent":{"admin":true,"code":"00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,9,","type":"3"}
         * lat : 22.515196
         * lng : 113.39192
         * officeAreaName : 广州市
         * officeCode : gd1905611016
         * officeCorpIdNum : 451028199212075277
         * officeCorpMobile : 15575366041
         * officeCorpName : 楚乔
         * officeName : 深圳北站代售点
         * officeOperatorIdNum : 430681199512036548
         * officeOperatorMoblie : 15575366041
         * officeOperatorName : 楚乔
         * officeWinNumber : 1016
         * operatorIdNum : 513425199202158328
         * operatorMobile : 15555551110
         * operatorName : 负责人姓
         * winNumber : 1019
         */

        private String agencyName;
        private String auditReason;
        private String auditState;
        private String geogPosition;
        private String id;
        private String lat;
        private String lng;
        private String officeAreaName;
        private String officeCode;
        private String officeCorpIdNum;
        private String officeCorpMobile;
        private String officeCorpName;
        private String officeName;
        private String officeOperatorIdNum;
        private String officeOperatorMoblie;
        private String officeOperatorName;
        private String officeWinNumber;
        private String operatorIdNum;
        private String operatorMobile;
        private String operatorName;
        private String winNumber;

        public String getAgencyName() {
            return agencyName;
        }

        public void setAgencyName(String agencyName) {
            this.agencyName = agencyName;
        }

        public String getAuditReason() {
            return auditReason;
        }

        public void setAuditReason(String auditReason) {
            this.auditReason = auditReason;
        }

        public String getAuditState() {
            return auditState;
        }

        public void setAuditState(String auditState) {
            this.auditState = auditState;
        }

        public String getGeogPosition() {
            return geogPosition;
        }

        public void setGeogPosition(String geogPosition) {
            this.geogPosition = geogPosition;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getOfficeAreaName() {
            return officeAreaName;
        }

        public void setOfficeAreaName(String officeAreaName) {
            this.officeAreaName = officeAreaName;
        }

        public String getOfficeCode() {
            return officeCode;
        }

        public void setOfficeCode(String officeCode) {
            this.officeCode = officeCode;
        }

        public String getOfficeCorpIdNum() {
            return officeCorpIdNum;
        }

        public void setOfficeCorpIdNum(String officeCorpIdNum) {
            this.officeCorpIdNum = officeCorpIdNum;
        }

        public String getOfficeCorpMobile() {
            return officeCorpMobile;
        }

        public void setOfficeCorpMobile(String officeCorpMobile) {
            this.officeCorpMobile = officeCorpMobile;
        }

        public String getOfficeCorpName() {
            return officeCorpName;
        }

        public void setOfficeCorpName(String officeCorpName) {
            this.officeCorpName = officeCorpName;
        }

        public String getOfficeName() {
            return officeName;
        }

        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public String getOfficeOperatorIdNum() {
            return officeOperatorIdNum;
        }

        public void setOfficeOperatorIdNum(String officeOperatorIdNum) {
            this.officeOperatorIdNum = officeOperatorIdNum;
        }

        public String getOfficeOperatorMoblie() {
            return officeOperatorMoblie;
        }

        public void setOfficeOperatorMoblie(String officeOperatorMoblie) {
            this.officeOperatorMoblie = officeOperatorMoblie;
        }

        public String getOfficeOperatorName() {
            return officeOperatorName;
        }

        public void setOfficeOperatorName(String officeOperatorName) {
            this.officeOperatorName = officeOperatorName;
        }

        public String getOfficeWinNumber() {
            return officeWinNumber;
        }

        public void setOfficeWinNumber(String officeWinNumber) {
            this.officeWinNumber = officeWinNumber;
        }

        public String getOperatorIdNum() {
            return operatorIdNum;
        }

        public void setOperatorIdNum(String operatorIdNum) {
            this.operatorIdNum = operatorIdNum;
        }

        public String getOperatorMobile() {
            return operatorMobile;
        }

        public void setOperatorMobile(String operatorMobile) {
            this.operatorMobile = operatorMobile;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getWinNumber() {
            return winNumber;
        }

        public void setWinNumber(String winNumber) {
            this.winNumber = winNumber;
        }


    }

    public static class DataBean {
        /**
         * id : 206899b9f59842bd90157203634982f8
         * office : {"id":"960ce1011eb149fd971efe79d7c8bdf7","parent":{"id":"c5c46aec419246b994f4d08aecf8f123","parent":{"id":"8da05512c38246099d6468bf512ac526","parent":{"id":"b8d79b441de04b3784c03693943b4a34","parent":{"remarks":"2015-12-01 11:22:15","id":"1","parentIds":"0,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"code":"86","name":"O计平台","type":"1","corpName":"陈","state":"0","root":true},"parentIds":"0,1,","area":{"id":"13","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"ah12","name":"安徽","type":"2"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlzj","name":"铁路总局","type":"6","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlfj","name":"广铁集团","type":"7","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,","area":{"id":"233","parent":{"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"},"parentIds":"0,1,20,","code":"020","name":"广州市","type":"3"},"jurisArea":{"id":"91","parent":{"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"},"parentIds":"0,1,9,","code":"91","name":"广州南","type":"3"},"code":"gzntrain","name":"火车南车站","type":"2","address":"地址","master":"吕夜雪","phone":"13100000018","operatorName":"吕夜雪","operatorMobile":"13100000025","operatorIdNum":"420203198008108716","corpName":"吕夜雪","corpMobile":"13100000024","corpIdNum":"610621199312165138","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,c5c46aec419246b994f4d08aecf8f123,","area":{"id":"233","parent":{"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"},"parentIds":"0,1,20,","code":"020","name":"广州市","type":"3"},"jurisArea":{"id":"91","parent":{"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"},"parentIds":"0,1,9,","code":"91","name":"广州南","type":"3"},"code":"gd190200012","name":"嘉禾街代售处","type":"3","agent":"0","agentType":"1","address":"详细地址详细地址","master":"江雁","phone":"15115012322","winNumber":"0012","operatorName":"杭其昌","operatorMobile":"18812341234","operatorIdNum":"340303199112224469","corpName":"杭其昌","corpMobile":"18812341234","corpIdNum":"340303199112224469","state":"1","lat":"23.022297","lng":"113.749473","geogPosition":"广东省东莞市东莞市市辖区莞太路63号","root":false}
         * loginName : gd190200012
         * name : 杭其昌
         * mobile : 18812341234
         * minimum : 2000
         * dynaMinimum : 0
         * userType : 2
         * auditState : 1
         * photoSrc : /userfiles/gd190200012/images/7cddc84776840ac30b167e53f729102c.png
         * officeName : 嘉禾街代售处
         * balanceString : 8414.00
         * roleNames : 待审核用户
         * isBindQQ : true
         * isBindWx : false
         */

        private String id;
        private OfficeBean office;
        private String loginName;
        private String name;
        private String mobile;
        private double minimum;
        private int dynaMinimum;
        private String userType;
        private String auditState;
        private String photoSrc;
        private String officeName;
        private String balanceString;
        private String roleNames;
        private boolean isBindQQ;
        private boolean isBindWx;
        private String auditReason;
        private String createDate;
        private String posNo;




        public String getPosNo() {
            return posNo;
        }

        public void setPosNo(String posNo) {
            this.posNo = posNo;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getAuditReason() {
            return auditReason;
        }

        public void setAuditReason(String auditReason) {
            this.auditReason = auditReason;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public OfficeBean getOffice() {
            return office;
        }

        public void setOffice(OfficeBean office) {
            this.office = office;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public double getMinimum() {
            return minimum;
        }

        public void setMinimum(double minimum) {
            this.minimum = minimum;
        }

        public int getDynaMinimum() {
            return dynaMinimum;
        }

        public void setDynaMinimum(int dynaMinimum) {
            this.dynaMinimum = dynaMinimum;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getAuditState() {
            return auditState;
        }

        public void setAuditState(String auditState) {
            this.auditState = auditState;
        }

        public String getPhotoSrc() {
            return photoSrc;
        }

        public void setPhotoSrc(String photoSrc) {
            this.photoSrc = photoSrc;
        }

        public String getOfficeName() {
            return officeName;
        }

        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public String getBalanceString() {
            return balanceString;
        }

        public void setBalanceString(String balanceString) {
            this.balanceString = balanceString;
        }

        public String getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(String roleNames) {
            this.roleNames = roleNames;
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

        public static class OfficeBean {
            /**
             * id : 960ce1011eb149fd971efe79d7c8bdf7
             * parent : {"id":"c5c46aec419246b994f4d08aecf8f123","parent":{"id":"8da05512c38246099d6468bf512ac526","parent":{"id":"b8d79b441de04b3784c03693943b4a34","parent":{"remarks":"2015-12-01 11:22:15","id":"1","parentIds":"0,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"code":"86","name":"O计平台","type":"1","corpName":"陈","state":"0","root":true},"parentIds":"0,1,","area":{"id":"13","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"ah12","name":"安徽","type":"2"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlzj","name":"铁路总局","type":"6","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlfj","name":"广铁集团","type":"7","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,","area":{"id":"233","parent":{"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"},"parentIds":"0,1,20,","code":"020","name":"广州市","type":"3"},"jurisArea":{"id":"91","parent":{"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"},"parentIds":"0,1,9,","code":"91","name":"广州南","type":"3"},"code":"gzntrain","name":"火车南车站","type":"2","address":"地址","master":"吕夜雪","phone":"13100000018","operatorName":"吕夜雪","operatorMobile":"13100000025","operatorIdNum":"420203198008108716","corpName":"吕夜雪","corpMobile":"13100000024","corpIdNum":"610621199312165138","state":"0","root":false}
             * parentIds : 0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,c5c46aec419246b994f4d08aecf8f123,
             * area : {"id":"233","parent":{"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"},"parentIds":"0,1,20,","code":"020","name":"广州市","type":"3"}
             * jurisArea : {"id":"91","parent":{"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"},"parentIds":"0,1,9,","code":"91","name":"广州南","type":"3"}
             * code : gd190200012
             * name : 嘉禾街代售处
             * type : 3
             * agent : 0
             * agentType : 1
             * address : 详细地址详细地址
             * master : 江雁
             * phone : 15115012322
             * winNumber : 0012
             * operatorName : 杭其昌
             * operatorMobile : 18812341234
             * operatorIdNum : 340303199112224469
             * corpName : 杭其昌
             * corpMobile : 18812341234
             * corpIdNum : 340303199112224469
             * state : 1
             * lat : 23.022297
             * lng : 113.749473
             * geogPosition : 广东省东莞市东莞市市辖区莞太路63号
             * root : false
             */

            private String id;
            private ParentBeanXXXXXXXX parent;
            private String parentIds;
            private AreaBeanXXXX area;
            private JurisAreaBeanXXX jurisArea;
            private String code;
            private String name;
            private String type;
            private String agent;
            private String agentType;
            private String address;
            private String master;
            private String phone;
            private String winNumber;
            private String operatorName;
            private String operatorMobile;
            private String operatorIdNum;
            private String corpName;
            private String corpMobile;
            private String corpIdNum;
            private String state;
            private String lat;
            private String lng;
            private String geogPosition;
            private boolean root;
            private String agentCompanyName;  //公司

            private String  corpLicenceSrc;	        //营业执照路径
            private String  corpCardforntSrc;	        //法人身份证正面路径
            private String  corpCardrearSrc ;     	    //法人身份证反面路径
            private String  staContIndexSrc;	        //车站合同首页路径
            private String  staContLastSrc	 ;           //车站合同末页路径
            private String  staDepositSrc;	             //车站押金条路径
            private String  staDepInspSrc;	            //车站押金年检证书路径
            private String  operatorCardforntSrc;   	 //负责人身份证正面路径
            private String  operatorCardrearSrc;    	 //负责人身份证反面路径
            private String  fareAuthorizationSrc;    	 //票款汇缴授权书
            private String depositString;               //押金

            public String getAgentCompanyName() {
                return agentCompanyName;
            }

            public void setAgentCompanyName(String agentCompanyName) {
                this.agentCompanyName = agentCompanyName;
            }

            public String getCorpLicenceSrc() {
                return corpLicenceSrc;
            }

            public void setCorpLicenceSrc(String corpLicenceSrc) {
                this.corpLicenceSrc = corpLicenceSrc;
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

            public String getDepositString() {
                return depositString;
            }

            public void setDepositString(String depositString) {
                this.depositString = depositString;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public AreaBeanXXXX getArea() {
                return area;
            }

            public void setArea(AreaBeanXXXX area) {
                this.area = area;
            }

            public JurisAreaBeanXXX getJurisArea() {
                return jurisArea;
            }

            public void setJurisArea(JurisAreaBeanXXX jurisArea) {
                this.jurisArea = jurisArea;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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

            public boolean isRoot() {
                return root;
            }

            public void setRoot(boolean root) {
                this.root = root;
            }


            public static class ParentBeanXXXXXXXX {
                /**
                 * id : c5c46aec419246b994f4d08aecf8f123
                 * parent : {"id":"8da05512c38246099d6468bf512ac526","parent":{"id":"b8d79b441de04b3784c03693943b4a34","parent":{"remarks":"2015-12-01 11:22:15","id":"1","parentIds":"0,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"code":"86","name":"O计平台","type":"1","corpName":"陈","state":"0","root":true},"parentIds":"0,1,","area":{"id":"13","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"ah12","name":"安徽","type":"2"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlzj","name":"铁路总局","type":"6","state":"0","root":false},"parentIds":"0,1,b8d79b441de04b3784c03693943b4a34,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlfj","name":"广铁集团","type":"7","state":"0","root":false}
                 * parentIds : 0,1,b8d79b441de04b3784c03693943b4a34,8da05512c38246099d6468bf512ac526,
                 * area : {"id":"233","parent":{"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"},"parentIds":"0,1,20,","code":"020","name":"广州市","type":"3"}
                 * jurisArea : {"id":"91","parent":{"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"},"parentIds":"0,1,9,","code":"91","name":"广州南","type":"3"}
                 * code : gzntrain
                 * name : 火车南车站
                 * type : 2
                 * address : 地址
                 * master : 吕夜雪
                 * phone : 13100000018
                 * operatorName : 吕夜雪
                 * operatorMobile : 13100000025
                 * operatorIdNum : 420203198008108716
                 * corpName : 吕夜雪
                 * corpMobile : 13100000024
                 * corpIdNum : 610621199312165138
                 * state : 0
                 * root : false
                 */

                private String id;
                private ParentBeanXXX parent;
                private String parentIds;
                private AreaBeanXXX area;
                private JurisAreaBeanXX jurisArea;
                private String code;
                private String name;
                private String type;
                private String address;
                private String master;
                private String phone;
                private String operatorName;
                private String operatorMobile;
                private String operatorIdNum;
                private String corpName;
                private String corpMobile;
                private String corpIdNum;
                private String state;
                private boolean root;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

                public AreaBeanXXX getArea() {
                    return area;
                }

                public void setArea(AreaBeanXXX area) {
                    this.area = area;
                }

                public JurisAreaBeanXX getJurisArea() {
                    return jurisArea;
                }

                public void setJurisArea(JurisAreaBeanXX jurisArea) {
                    this.jurisArea = jurisArea;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
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

                public boolean isRoot() {
                    return root;
                }

                public void setRoot(boolean root) {
                    this.root = root;
                }

                public static class ParentBeanXXX {
                    /**
                     * id : 8da05512c38246099d6468bf512ac526
                     * parent : {"id":"b8d79b441de04b3784c03693943b4a34","parent":{"remarks":"2015-12-01 11:22:15","id":"1","parentIds":"0,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"code":"86","name":"O计平台","type":"1","corpName":"陈","state":"0","root":true},"parentIds":"0,1,","area":{"id":"13","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"ah12","name":"安徽","type":"2"},"jurisArea":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"code":"tlzj","name":"铁路总局","type":"6","state":"0","root":false}
                     * parentIds : 0,1,b8d79b441de04b3784c03693943b4a34,
                     * area : {"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"}
                     * jurisArea : {"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"}
                     * code : tlfj
                     * name : 广铁集团
                     * type : 7
                     * state : 0
                     * root : false
                     */

                    private String id;
                    private ParentBeanXX parent;
                    private String parentIds;
                    private AreaBeanXX area;
                    private JurisAreaBeanX jurisArea;
                    private String code;
                    private String name;
                    private String type;
                    private String state;
                    private boolean root;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
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

                    public AreaBeanXX getArea() {
                        return area;
                    }

                    public void setArea(AreaBeanXX area) {
                        this.area = area;
                    }

                    public JurisAreaBeanX getJurisArea() {
                        return jurisArea;
                    }

                    public void setJurisArea(JurisAreaBeanX jurisArea) {
                        this.jurisArea = jurisArea;
                    }

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getState() {
                        return state;
                    }

                    public void setState(String state) {
                        this.state = state;
                    }

                    public boolean isRoot() {
                        return root;
                    }

                    public void setRoot(boolean root) {
                        this.root = root;
                    }

                    public static class ParentBeanXX {
                        /**
                         * id : b8d79b441de04b3784c03693943b4a34
                         * parent : {"remarks":"2015-12-01 11:22:15","id":"1","parentIds":"0,","area":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"code":"86","name":"O计平台","type":"1","corpName":"陈","state":"0","root":true}
                         * parentIds : 0,1,
                         * area : {"id":"13","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"ah12","name":"安徽","type":"2"}
                         * jurisArea : {"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"}
                         * code : tlzj
                         * name : 铁路总局
                         * type : 6
                         * state : 0
                         * root : false
                         */

                        private String id;
                        private ParentBean parent;
                        private String parentIds;
                        private AreaBeanX area;
                        private JurisAreaBean jurisArea;
                        private String code;
                        private String name;
                        private String type;
                        private String state;
                        private boolean root;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
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

                        public AreaBeanX getArea() {
                            return area;
                        }

                        public void setArea(AreaBeanX area) {
                            this.area = area;
                        }

                        public JurisAreaBean getJurisArea() {
                            return jurisArea;
                        }

                        public void setJurisArea(JurisAreaBean jurisArea) {
                            this.jurisArea = jurisArea;
                        }

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getType() {
                            return type;
                        }

                        public void setType(String type) {
                            this.type = type;
                        }

                        public String getState() {
                            return state;
                        }

                        public void setState(String state) {
                            this.state = state;
                        }

                        public boolean isRoot() {
                            return root;
                        }

                        public void setRoot(boolean root) {
                            this.root = root;
                        }

                        public static class ParentBean {
                            /**
                             * remarks : 2015-12-01 11:22:15
                             * id : 1
                             * parentIds : 0,
                             * area : {"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"}
                             * code : 86
                             * name : O计平台
                             * type : 1
                             * corpName : 陈
                             * state : 0
                             * root : true
                             */

                            private String remarks;
                            private String id;
                            private String parentIds;
                            private AreaBean area;
                            private String code;
                            private String name;
                            private String type;
                            private String corpName;
                            private String state;
                            private boolean root;

                            public String getRemarks() {
                                return remarks;
                            }

                            public void setRemarks(String remarks) {
                                this.remarks = remarks;
                            }

                            public String getId() {
                                return id;
                            }

                            public void setId(String id) {
                                this.id = id;
                            }

                            public String getParentIds() {
                                return parentIds;
                            }

                            public void setParentIds(String parentIds) {
                                this.parentIds = parentIds;
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

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }

                            public String getType() {
                                return type;
                            }

                            public void setType(String type) {
                                this.type = type;
                            }

                            public String getCorpName() {
                                return corpName;
                            }

                            public void setCorpName(String corpName) {
                                this.corpName = corpName;
                            }

                            public String getState() {
                                return state;
                            }

                            public void setState(String state) {
                                this.state = state;
                            }

                            public boolean isRoot() {
                                return root;
                            }

                            public void setRoot(boolean root) {
                                this.root = root;
                            }

                            public static class AreaBean {
                                /**
                                 * id : 1
                                 * parentIds : 0,
                                 * code : 00
                                 * name : 中国
                                 * type : 1
                                 */

                                private String id;
                                private String parentIds;
                                private String code;
                                private String name;
                                private String type;

                                public String getId() {
                                    return id;
                                }

                                public void setId(String id) {
                                    this.id = id;
                                }

                                public String getParentIds() {
                                    return parentIds;
                                }

                                public void setParentIds(String parentIds) {
                                    this.parentIds = parentIds;
                                }

                                public String getCode() {
                                    return code;
                                }

                                public void setCode(String code) {
                                    this.code = code;
                                }

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public String getType() {
                                    return type;
                                }

                                public void setType(String type) {
                                    this.type = type;
                                }
                            }
                        }

                        public static class AreaBeanX {
                            /**
                             * id : 13
                             * parent : {"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"}
                             * parentIds : 0,1,
                             * code : ah12
                             * name : 安徽
                             * type : 2
                             */

                            private String id;
                            private ParentBeanX parent;
                            private String parentIds;
                            private String code;
                            private String name;
                            private String type;

                            public String getId() {
                                return id;
                            }

                            public void setId(String id) {
                                this.id = id;
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

                            public String getCode() {
                                return code;
                            }

                            public void setCode(String code) {
                                this.code = code;
                            }

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }

                            public String getType() {
                                return type;
                            }

                            public void setType(String type) {
                                this.type = type;
                            }

                            public static class ParentBeanX {
                                /**
                                 * id : 1
                                 * parentIds : 0,
                                 * code : 00
                                 * name : 中国
                                 * type : 1
                                 */

                                private String id;
                                private String parentIds;
                                private String code;
                                private String name;
                                private String type;

                                public String getId() {
                                    return id;
                                }

                                public void setId(String id) {
                                    this.id = id;
                                }

                                public String getParentIds() {
                                    return parentIds;
                                }

                                public void setParentIds(String parentIds) {
                                    this.parentIds = parentIds;
                                }

                                public String getCode() {
                                    return code;
                                }

                                public void setCode(String code) {
                                    this.code = code;
                                }

                                public String getName() {
                                    return name;
                                }

                                public void setName(String name) {
                                    this.name = name;
                                }

                                public String getType() {
                                    return type;
                                }

                                public void setType(String type) {
                                    this.type = type;
                                }
                            }
                        }

                        public static class JurisAreaBean {
                            /**
                             * id : 1
                             * parentIds : 0,
                             * code : 00
                             * name : 广州铁路集团
                             * type : 1
                             */

                            private String id;
                            private String parentIds;
                            private String code;
                            private String name;
                            private String type;

                            public String getId() {
                                return id;
                            }

                            public void setId(String id) {
                                this.id = id;
                            }

                            public String getParentIds() {
                                return parentIds;
                            }

                            public void setParentIds(String parentIds) {
                                this.parentIds = parentIds;
                            }

                            public String getCode() {
                                return code;
                            }

                            public void setCode(String code) {
                                this.code = code;
                            }

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }

                            public String getType() {
                                return type;
                            }

                            public void setType(String type) {
                                this.type = type;
                            }
                        }
                    }

                    public static class AreaBeanXX {
                        /**
                         * id : 1
                         * parentIds : 0,
                         * code : 00
                         * name : 中国
                         * type : 1
                         */

                        private String id;
                        private String parentIds;
                        private String code;
                        private String name;
                        private String type;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getParentIds() {
                            return parentIds;
                        }

                        public void setParentIds(String parentIds) {
                            this.parentIds = parentIds;
                        }

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getType() {
                            return type;
                        }

                        public void setType(String type) {
                            this.type = type;
                        }
                    }

                    public static class JurisAreaBeanX {
                        /**
                         * id : 1
                         * parentIds : 0,
                         * code : 00
                         * name : 广州铁路集团
                         * type : 1
                         */

                        private String id;
                        private String parentIds;
                        private String code;
                        private String name;
                        private String type;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getParentIds() {
                            return parentIds;
                        }

                        public void setParentIds(String parentIds) {
                            this.parentIds = parentIds;
                        }

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getType() {
                            return type;
                        }

                        public void setType(String type) {
                            this.type = type;
                        }
                    }
                }

                public static class AreaBeanXXX {
                    /**
                     * id : 233
                     * parent : {"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"}
                     * parentIds : 0,1,20,
                     * code : 020
                     * name : 广州市
                     * type : 3
                     */

                    private String id;
                    private ParentBeanXXXXX parent;
                    private String parentIds;
                    private String code;
                    private String name;
                    private String type;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
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

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public static class ParentBeanXXXXX {
                        /**
                         * id : 20
                         * parent : {"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"}
                         * parentIds : 0,1,
                         * code : gd19
                         * name : 广东
                         * type : 2
                         */

                        private String id;
                        private ParentBeanXXXX parent;
                        private String parentIds;
                        private String code;
                        private String name;
                        private String type;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
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

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getType() {
                            return type;
                        }

                        public void setType(String type) {
                            this.type = type;
                        }

                        public static class ParentBeanXXXX {
                            /**
                             * id : 1
                             * parentIds : 0,
                             * code : 00
                             * name : 中国
                             * type : 1
                             */

                            private String id;
                            private String parentIds;
                            private String code;
                            private String name;
                            private String type;

                            public String getId() {
                                return id;
                            }

                            public void setId(String id) {
                                this.id = id;
                            }

                            public String getParentIds() {
                                return parentIds;
                            }

                            public void setParentIds(String parentIds) {
                                this.parentIds = parentIds;
                            }

                            public String getCode() {
                                return code;
                            }

                            public void setCode(String code) {
                                this.code = code;
                            }

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
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

                public static class JurisAreaBeanXX {
                    /**
                     * id : 91
                     * parent : {"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"}
                     * parentIds : 0,1,9,
                     * code : 91
                     * name : 广州南
                     * type : 3
                     */

                    private String id;
                    private ParentBeanXXXXXXX parent;
                    private String parentIds;
                    private String code;
                    private String name;
                    private String type;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
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

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public static class ParentBeanXXXXXXX {
                        /**
                         * id : 9
                         * parent : {"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"}
                         * parentIds : 0,1,
                         * code : 9
                         * name : 广州地区
                         * type : 2
                         */

                        private String id;
                        private ParentBeanXXXXXX parent;
                        private String parentIds;
                        private String code;
                        private String name;
                        private String type;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
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

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getType() {
                            return type;
                        }

                        public void setType(String type) {
                            this.type = type;
                        }

                        public static class ParentBeanXXXXXX {
                            /**
                             * id : 1
                             * parentIds : 0,
                             * code : 00
                             * name : 广州铁路集团
                             * type : 1
                             */

                            private String id;
                            private String parentIds;
                            private String code;
                            private String name;
                            private String type;

                            public String getId() {
                                return id;
                            }

                            public void setId(String id) {
                                this.id = id;
                            }

                            public String getParentIds() {
                                return parentIds;
                            }

                            public void setParentIds(String parentIds) {
                                this.parentIds = parentIds;
                            }

                            public String getCode() {
                                return code;
                            }

                            public void setCode(String code) {
                                this.code = code;
                            }

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
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

            public static class AreaBeanXXXX {
                /**
                 * id : 233
                 * parent : {"id":"20","parent":{"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"},"parentIds":"0,1,","code":"gd19","name":"广东","type":"2"}
                 * parentIds : 0,1,20,
                 * code : 020
                 * name : 广州市
                 * type : 3
                 */

                private String id;
                private ParentBeanXXXXXXXXXX parent;
                private String parentIds;
                private String code;
                private String name;
                private String type;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public static class ParentBeanXXXXXXXXXX {
                    /**
                     * id : 20
                     * parent : {"id":"1","parentIds":"0,","code":"00","name":"中国","type":"1"}
                     * parentIds : 0,1,
                     * code : gd19
                     * name : 广东
                     * type : 2
                     */

                    private String id;
                    private ParentBeanXXXXXXXXX parent;
                    private String parentIds;
                    private String code;
                    private String name;
                    private String type;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
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

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public static class ParentBeanXXXXXXXXX {
                        /**
                         * id : 1
                         * parentIds : 0,
                         * code : 00
                         * name : 中国
                         * type : 1
                         */

                        private String id;
                        private String parentIds;
                        private String code;
                        private String name;
                        private String type;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getParentIds() {
                            return parentIds;
                        }

                        public void setParentIds(String parentIds) {
                            this.parentIds = parentIds;
                        }

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
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

            public static class JurisAreaBeanXXX {
                /**
                 * id : 91
                 * parent : {"id":"9","parent":{"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"},"parentIds":"0,1,","code":"9","name":"广州地区","type":"2"}
                 * parentIds : 0,1,9,
                 * code : 91
                 * name : 广州南
                 * type : 3
                 */

                private String id;
                private ParentBeanXXXXXXXXXXXX parent;
                private String parentIds;
                private String code;
                private String name;
                private String type;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public static class ParentBeanXXXXXXXXXXXX {
                    /**
                     * id : 9
                     * parent : {"id":"1","parentIds":"0,","code":"00","name":"广州铁路集团","type":"1"}
                     * parentIds : 0,1,
                     * code : 9
                     * name : 广州地区
                     * type : 2
                     */

                    private String id;
                    private ParentBeanXXXXXXXXXXX parent;
                    private String parentIds;
                    private String code;
                    private String name;
                    private String type;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
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

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public static class ParentBeanXXXXXXXXXXX {
                        /**
                         * id : 1
                         * parentIds : 0,
                         * code : 00
                         * name : 广州铁路集团
                         * type : 1
                         */

                        private String id;
                        private String parentIds;
                        private String code;
                        private String name;
                        private String type;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getParentIds() {
                            return parentIds;
                        }

                        public void setParentIds(String parentIds) {
                            this.parentIds = parentIds;
                        }

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
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

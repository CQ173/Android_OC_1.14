package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/2/18.
 */

public class OutletsListBean implements Serializable{


    /**
     * code : 0
     * msg : 请求成功
     * size : 3
     * data : [{"id":"eaf3a98744a54c358bf31b55031927bc","area":{"id":"228","code":"0735","name":"郴州市","admin":false},"jurisArea":{"id":"9","code":"9","name":"广州地区","admin":false},"code":"hn1807350022","name":"变动代售点","agent":"0","agentType":"0","address":"123456","master":"123456","phone":"15111112222","winNumber":"0022","operatorName":"123456","operatorMobile":"15122223344","operatorIdNum":"23038119920602121X","corpName":"123456","corpMobile":"15122223344","corpIdNum":"23038119920602121X","corpLicenceSrc":"/userfiles/hn1807350022/images/3257edacd4175b7e64205e6c34f51596.jpg","corpCardforntSrc":"/userfiles/hn1807350022/images/9a0e5ee8918a269c2d66cc2e5dcff07f.jpg","corpCardrearSrc":"/userfiles/hn1807350022/images/4029220b55c8c366432ed9574cf675f9.jpg","staContIndexSrc":"/userfiles/hn1807350022/images/8c4decd116bddef144f3bf43c3ee46bf.jpg","staContLastSrc":"/userfiles/hn1807350022/images/a18e70fea12b421bce7106b6c3f1d5ae.jpg","operatorCardforntSrc":"/userfiles/hn1807350022/images/9a0e5ee8918a269c2d66cc2e5dcff07f.jpg","operatorCardrearSrc":"/userfiles/hn1807350022/images/4029220b55c8c366432ed9574cf675f9.jpg","state":"0","userInfo":{"id":"ab18cf596eeb4abc8573abe61aa05509","name":"123456","loginName":"hn1807350022","waitRecharge":0.5,"creditScore":100,"deposit":100000,"creditLevelName":"信用优秀","creditLevel":"0","payMchId":"102595700842"},"parentNames":"广州南车站-广州车务段","depositString":"100000.00"},{"id":"d91312935be34c83969e8074a16c178f","area":{"id":"219","code":"0731","name":"长沙市","admin":false},"jurisArea":{"id":"101","code":"101","name":"长沙段","admin":false},"code":"hn1807311111","name":"ddd","agent":"0","agentType":"0","address":"123456","master":"ddd","phone":"15111112222","winNumber":"1111","operatorName":"DDD","operatorMobile":"15111111115","operatorIdNum":"23038119920602121X","corpName":"DDD","corpMobile":"15111111115","corpIdNum":"23038119920602121X","corpLicenceSrc":"/userfiles/hn1807311111/images/aa24da326a746ff196bbdb0b58581b74.jpg","corpCardforntSrc":"/userfiles/hn1807311111/images/5e9fa8f20a31f6280867ec2cfbc555b5.jpg","corpCardrearSrc":"/userfiles/hn1807311111/images/16c9dbe04278041a6ab67b7cf8828b2.jpg","staContIndexSrc":"/userfiles/hn1807311111/images/4855672377e55fdbb5a5a547fe21e575.jpg","staContLastSrc":"/userfiles/hn1807311111/images/68c6135cc7d7279e6485dff5b1c64bdc.jpg","operatorCardforntSrc":"/userfiles/hn1807311111/images/5e9fa8f20a31f6280867ec2cfbc555b5.jpg","operatorCardrearSrc":"/userfiles/hn1807311111/images/16c9dbe04278041a6ab67b7cf8828b2.jpg","fareAuthorizationSrc":"/userfiles/hn1807311111/images/4bd9a10e37c9166d9c51362298d40ec.png","state":"0","lat":"28.213478","lng":"112.979355","geogPosition":"长沙市","userInfo":{"id":"352af3046ba64f3a846c101b6fdd6a67","name":"DDD","loginName":"hn1807311111","waitRecharge":1014.9,"cumulativeCount":4,"continuousCount":4,"deposit":333,"creditLevelName":"信用较差","creditLevel":"2","payMchId":"102595700842"},"parentNames":"长沙市火车站(测试专用)-广州车务段","depositString":"333.00"},{"id":"d4b62e0c4f9c4a0184f6d43b0eccaab0","area":{"id":"219","code":"0731","name":"长沙市","admin":false},"jurisArea":{"id":"111","code":"111","name":"衡阳","admin":false},"code":"hn1807310000","name":"测试代售点q","agent":"0","agentType":"0","address":"123456q","master":"朱峰","phone":"15111112222","winNumber":"0000","operatorName":"朱峰q","operatorMobile":"15111113333","operatorIdNum":"23038119920602121X","corpName":"朱峰q","corpMobile":"15111112222","corpIdNum":"23038119920602121X","corpLicenceSrc":"/userfiles/hn1807310000/images/191dbbfdd2cb4fd315d4ab7553760b58.jpg","corpCardforntSrc":"/userfiles/hn1807310000/images/d48474518421830ce862e6bef34232d3.jpg","corpCardrearSrc":"/userfiles/hn1807310000/images/a17a33f079c44340ecd3fa9d735bbd4b.jpg","staContIndexSrc":"/userfiles/hn1807310000/images/e9ffa29a7ce9117100b5b564a98b4103.jpg","staContLastSrc":"/userfiles/hn1807310000/images/7278c120de36d255790828de050281bf.jpg","operatorCardforntSrc":"/userfiles/hn1807310000/images/d48474518421830ce862e6bef34232d3.jpg","operatorCardrearSrc":"/userfiles/hn1807310000/images/a17a33f079c44340ecd3fa9d735bbd4b.jpg","state":"0","lat":"28.213478","lng":"112.979355","geogPosition":"长沙市","userInfo":{"id":"36854d5b8e8243819f5cf06d8e6744e3","name":"朱峰","loginName":"hn1807310000","waitRecharge":2,"creditScore":61,"cumulativeCount":5,"continuousCount":5,"deposit":222,"creditLevelName":"信用良好","creditLevel":"1","payMchId":"102595700842"},"parentNames":"衡阳火车站1-广铁分局","depositString":"222.00"}]
     * next : -1
     */

    private String code;
    private String msg;
    private int size;
    private int next;
    private List<DataBean> data;

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

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : eaf3a98744a54c358bf31b55031927bc
         * area : {"id":"228","code":"0735","name":"郴州市","admin":false}
         * jurisArea : {"id":"9","code":"9","name":"广州地区","admin":false}
         * code : hn1807350022
         * name : 变动代售点
         * agent : 0
         * agentType : 0
         * address : 123456
         * master : 123456
         * phone : 15111112222
         * winNumber : 0022
         * operatorName : 123456
         * operatorMobile : 15122223344
         * operatorIdNum : 23038119920602121X
         * corpName : 123456
         * corpMobile : 15122223344
         * corpIdNum : 23038119920602121X
         * corpLicenceSrc : /userfiles/hn1807350022/images/3257edacd4175b7e64205e6c34f51596.jpg
         * corpCardforntSrc : /userfiles/hn1807350022/images/9a0e5ee8918a269c2d66cc2e5dcff07f.jpg
         * corpCardrearSrc : /userfiles/hn1807350022/images/4029220b55c8c366432ed9574cf675f9.jpg
         * staContIndexSrc : /userfiles/hn1807350022/images/8c4decd116bddef144f3bf43c3ee46bf.jpg
         * staContLastSrc : /userfiles/hn1807350022/images/a18e70fea12b421bce7106b6c3f1d5ae.jpg
         * operatorCardforntSrc : /userfiles/hn1807350022/images/9a0e5ee8918a269c2d66cc2e5dcff07f.jpg
         * operatorCardrearSrc : /userfiles/hn1807350022/images/4029220b55c8c366432ed9574cf675f9.jpg
         * state : 0
         * userInfo : {"id":"ab18cf596eeb4abc8573abe61aa05509","name":"123456","loginName":"hn1807350022","waitRecharge":0.5,"creditScore":100,"deposit":100000,"creditLevelName":"信用优秀","creditLevel":"0","payMchId":"102595700842"}
         * parentNames : 广州南车站-广州车务段
         * depositString : 100000.00
         * fareAuthorizationSrc : /userfiles/hn1807311111/images/4bd9a10e37c9166d9c51362298d40ec.png
         * lat : 28.213478
         * lng : 112.979355
         * geogPosition : 长沙市
         */

        private String id;
        private AreaBean area;
        private JurisAreaBean jurisArea;
        private String code;
        private String name;
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
        private String corpLicenceSrc;
        private String corpCardforntSrc;
        private String corpCardrearSrc;
        private String staContIndexSrc;
        private String staContLastSrc;
        private String operatorCardforntSrc;
        private String operatorCardrearSrc;
        private String state;
        private UserInfoBean userInfo;
        private String parentNames;
        private String depositString;
        private String fareAuthorizationSrc;
        private String lat;
        private String lng;
        private String geogPosition;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public AreaBean getArea() {
            return area;
        }

        public void setArea(AreaBean area) {
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public String getParentNames() {
            return parentNames;
        }

        public void setParentNames(String parentNames) {
            this.parentNames = parentNames;
        }

        public String getDepositString() {
            return depositString;
        }

        public void setDepositString(String depositString) {
            this.depositString = depositString;
        }

        public String getFareAuthorizationSrc() {
            return fareAuthorizationSrc;
        }

        public void setFareAuthorizationSrc(String fareAuthorizationSrc) {
            this.fareAuthorizationSrc = fareAuthorizationSrc;
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

        public static class AreaBean implements Serializable{
            /**
             * id : 228
             * code : 0735
             * name : 郴州市
             * admin : false
             */

            private String id;
            private String code;
            private String name;
            private boolean admin;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }
        }

        public static class JurisAreaBean implements Serializable{
            /**
             * id : 9
             * code : 9
             * name : 广州地区
             * admin : false
             */

            private String id;
            private String code;
            private String name;
            private boolean admin;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }
        }

        public static class UserInfoBean implements Serializable{
            /**
             * id : ab18cf596eeb4abc8573abe61aa05509
             * name : 123456
             * loginName : hn1807350022
             * waitRecharge : 0.5
             * creditScore : 100
             * deposit : 100000
             * creditLevelName : 信用优秀
             * creditLevel : 0
             * payMchId : 102595700842
             */

            private String id;
            private String name;
            private String loginName;
            private double waitRecharge;
            private int creditScore;
            private int deposit;
            private String creditLevelName;
            private String creditLevel;
            private String payMchId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLoginName() {
                return loginName;
            }

            public void setLoginName(String loginName) {
                this.loginName = loginName;
            }

            public double getWaitRecharge() {
                return waitRecharge;
            }

            public void setWaitRecharge(double waitRecharge) {
                this.waitRecharge = waitRecharge;
            }

            public int getCreditScore() {
                return creditScore;
            }

            public void setCreditScore(int creditScore) {
                this.creditScore = creditScore;
            }

            public int getDeposit() {
                return deposit;
            }

            public void setDeposit(int deposit) {
                this.deposit = deposit;
            }

            public String getCreditLevelName() {
                return creditLevelName;
            }

            public void setCreditLevelName(String creditLevelName) {
                this.creditLevelName = creditLevelName;
            }

            public String getCreditLevel() {
                return creditLevel;
            }

            public void setCreditLevel(String creditLevel) {
                this.creditLevel = creditLevel;
            }

            public String getPayMchId() {
                return payMchId;
            }

            public void setPayMchId(String payMchId) {
                this.payMchId = payMchId;
            }
        }
    }
}

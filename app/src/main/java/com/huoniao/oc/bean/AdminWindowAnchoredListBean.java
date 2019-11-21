package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class AdminWindowAnchoredListBean implements Serializable {


    /**
     * code : 0
     * count : 10
     * data : [{"agencyName":"木叶暗部","auditReason":"132","auditState":"0","createDate":"2017-07-24 17:33:59","geogPosition":"广州市","id":"229192c3a55245b5b409d86a8232e340","jurisArea":{"admin":false,"code":"91","createDate":"2017-04-14 19:52:09","id":"91","name":"广州南","parent":{"admin":false,"code":"9","createDate":"2017-04-14 19:51:12","id":"9","name":"广州地区","parent":{"admin":true,"code":"00","createDate":"2017-04-14 19:38:00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,9,","type":"3"},"lat":"23.120049","lng":"113.30765","officeAreaName":"广州市","officeCode":"gd190200101","officeCorpIdNum":"330922198403316879","officeCorpMobile":"13666606666","officeCorpName":"李飞","officeName":"长城代售点","officeOperatorIdNum":"330922198403316879","officeOperatorMoblie":"13666606666","officeOperatorName":"李飞","officeWinNumber":"0101","operatorIdNum":"430921199112017918","operatorMobile":"15575366041","operatorName":"宇智波鼬","winNumber":"0608"},{"agencyName":"毛利侦探事务所","auditReason":"hhh","auditState":"2","createDate":"2017-07-24 17:26:26","geogPosition":"毛利侦探事务所","id":"ca2b041850f44da785960af2e7f489b2","jurisArea":{"admin":false,"code":"91","createDate":"2017-04-14 19:52:09","id":"91","name":"广州南","parent":{"admin":false,"code":"9","createDate":"2017-04-14 19:51:12","id":"9","name":"广州地区","parent":{"admin":true,"code":"00","createDate":"2017-04-14 19:38:00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,9,","type":"3"},"lat":"24.07583","lng":"105.8851","officeAreaName":"广州市","officeCode":"gd190200521","officeCorpIdNum":"513425199202158328","officeCorpMobile":"15351224445","officeCorpName":"章小小","officeName":"开福火车票代售处啊啊啊啊啊","officeOperatorIdNum":"513425199202158328","officeOperatorMoblie":"15351224445","officeOperatorName":"章小小","officeWinNumber":"0521","operatorIdNum":"430921199112017918","operatorMobile":"15580782366","operatorName":"工藤新一","winNumber":"0504"},{"agencyName":"岳麓挂靠代售点","auditState":"1","createDate":"2017-06-12 16:37:08","geogPosition":"岳麓挂靠代售点","id":"695ceb3fe4ee418f89a10cb22e9216d8","jurisArea":{"admin":false,"code":"91","createDate":"2017-04-14 19:52:09","id":"91","name":"广州南","parent":{"admin":false,"code":"9","createDate":"2017-04-14 19:51:12","id":"9","name":"广州地区","parent":{"admin":true,"code":"00","createDate":"2017-04-14 19:38:00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,9,","type":"3"},"lat":"28.202707","lng":"112.9087","officeAreaName":"长沙市","officeCode":"gd190200311","officeCorpIdNum":"340303199112224469","officeCorpMobile":"15022244454","officeCorpName":"郝开梦","officeName":"耀群票务中心","officeOperatorIdNum":"430811198905249636","officeOperatorMoblie":"15170322417","officeOperatorName":"刘鹏","officeWinNumber":"0311","operatorIdNum":"430621198910270474","operatorMobile":"15115029648","operatorName":"晏刚","winNumber":"10011"}]
     * msg : 请求成功
     * next : 2
     * size : 3
     */

    private String code;
    private int count;
    private String msg;
    private int next;
    private int size;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
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

    public static class DataBean implements Serializable {
        /**
         * agencyName : 木叶暗部
         * auditReason : 132
         * auditState : 0
         * createDate : 2017-07-24 17:33:59
         * geogPosition : 广州市
         * id : 229192c3a55245b5b409d86a8232e340
         * jurisArea : {"admin":false,"code":"91","createDate":"2017-04-14 19:52:09","id":"91","name":"广州南","parent":{"admin":false,"code":"9","createDate":"2017-04-14 19:51:12","id":"9","name":"广州地区","parent":{"admin":true,"code":"00","createDate":"2017-04-14 19:38:00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"},"parentIds":"0,1,9,","type":"3"}
         * lat : 23.120049
         * lng : 113.30765
         * officeAreaName : 广州市
         * officeCode : gd190200101
         * officeCorpIdNum : 330922198403316879
         * officeCorpMobile : 13666606666
         * officeCorpName : 李飞
         * officeName : 长城代售点
         * officeOperatorIdNum : 330922198403316879
         * officeOperatorMoblie : 13666606666
         * officeOperatorName : 李飞
         * officeWinNumber : 0101
         * operatorIdNum : 430921199112017918
         * operatorMobile : 15575366041
         * operatorName : 宇智波鼬
         * winNumber : 0608
         */

        private String agencyName;
        private String auditReason;
        private String auditState;
        private String createDate;
        private String geogPosition;
        private String id;
        private JurisAreaBean jurisArea;
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
      private String  operatorCardforntSrc;		//负责人身份证正面路径
      private String  operatorCardrearSrc;	   //	负责人身份证反面路径
      private String  staContIndexSrc;		   //  车站合同首页路径
      private String  staContLastSrc ;          //	车站合同末页路径
      private String  fareAuthorizationSrc;	   //票款汇缴授权书

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

        public String getFareAuthorizationSrc() {
            return fareAuthorizationSrc;
        }

        public void setFareAuthorizationSrc(String fareAuthorizationSrc) {
            this.fareAuthorizationSrc = fareAuthorizationSrc;
        }

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

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
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

        public JurisAreaBean getJurisArea() {
            return jurisArea;
        }

        public void setJurisArea(JurisAreaBean jurisArea) {
            this.jurisArea = jurisArea;
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

        public static class JurisAreaBean implements Serializable {
            /**
             * admin : false
             * code : 91
             * createDate : 2017-04-14 19:52:09
             * id : 91
             * name : 广州南
             * parent : {"admin":false,"code":"9","createDate":"2017-04-14 19:51:12","id":"9","name":"广州地区","parent":{"admin":true,"code":"00","createDate":"2017-04-14 19:38:00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"},"parentIds":"0,1,","type":"2"}
             * parentIds : 0,1,9,
             * type : 3
             */

            private boolean admin;
            private String code;
            private String createDate;
            private String id;
            private String name;
            private ParentBeanX parent;
            private String parentIds;
            private String type;

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

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

            public static class ParentBeanX implements Serializable{
                /**
                 * admin : false
                 * code : 9
                 * createDate : 2017-04-14 19:51:12
                 * id : 9
                 * name : 广州地区
                 * parent : {"admin":true,"code":"00","createDate":"2017-04-14 19:38:00","id":"1","name":"广州铁路集团","parentIds":"0,","type":"1"}
                 * parentIds : 0,1,
                 * type : 2
                 */

                private boolean admin;
                private String code;
                private String createDate;
                private String id;
                private String name;
                private ParentBean parent;
                private String parentIds;
                private String type;

                public boolean isAdmin() {
                    return admin;
                }

                public void setAdmin(boolean admin) {
                    this.admin = admin;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(String createDate) {
                    this.createDate = createDate;
                }

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

                public static class ParentBean  implements Serializable{
                    /**
                     * admin : true
                     * code : 00
                     * createDate : 2017-04-14 19:38:00
                     * id : 1
                     * name : 广州铁路集团
                     * parentIds : 0,
                     * type : 1
                     */

                    private boolean admin;
                    private String code;
                    private String createDate;
                    private String id;
                    private String name;
                    private String parentIds;
                    private String type;

                    public boolean isAdmin() {
                        return admin;
                    }

                    public void setAdmin(boolean admin) {
                        this.admin = admin;
                    }

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getCreateDate() {
                        return createDate;
                    }

                    public void setCreateDate(String createDate) {
                        this.createDate = createDate;
                    }

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

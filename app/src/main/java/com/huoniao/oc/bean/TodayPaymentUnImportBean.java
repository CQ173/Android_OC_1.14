package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/1/24.
 */

public class TodayPaymentUnImportBean implements Serializable{


    /**
     * code : 0
     * data : {"list":[{"bigDecimal1":"","bigDecimal1String":"0.00","bigDecimal2":"","bigDecimal2String":"0.00","count":"","integer1":1,"integer2":"","standby1":"1f8130a0bf664d4aab0f578628c991a5","standby2":"广州南车站","standby3":"gzntrain","standby4":"","standby4String":"0.00","string1":"广州车务段","sum":"","sum10String":"0.00","sum11String":"0.00","sum12String":"0.00","sum1String":"0.00","sum2String":"0.00","sum3String":"0.00","sum4String":"0.00","sum5String":"0.00","sum6String":"0.00","sum7String":"0.00","sum8String":"0.00","sum9String":"0.00","sumString":"0.00","time":"","type":""},{"bigDecimal1":"","bigDecimal1String":"0.00","bigDecimal2":"","bigDecimal2String":"0.00","count":"","integer1":2,"integer2":"","standby1":"5b6cc6bbb9fc4a6bb77a63fe4b58dab8","standby2":"衡阳火车站1","standby3":"hytrain","standby4":"","standby4String":"0.00","string1":"广铁分局","sum":"","sum10String":"0.00","sum11String":"0.00","sum12String":"0.00","sum1String":"0.00","sum2String":"0.00","sum3String":"0.00","sum4String":"0.00","sum5String":"0.00","sum6String":"0.00","sum7String":"0.00","sum8String":"0.00","sum9String":"0.00","sumString":"0.00","time":"","type":""}],"statistical":{"unImportCount":2}}
     * msg : 请求成功
     * next : -1
     * prev : -1
     * size : 2
     */

    private String code;
    private DataBean data;
    private String msg;
    private int next;
    private int prev;
    private int size;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static class DataBean implements Serializable{
        /**
         * list : [{"bigDecimal1":"","bigDecimal1String":"0.00","bigDecimal2":"","bigDecimal2String":"0.00","count":"","integer1":1,"integer2":"","standby1":"1f8130a0bf664d4aab0f578628c991a5","standby2":"广州南车站","standby3":"gzntrain","standby4":"","standby4String":"0.00","string1":"广州车务段","sum":"","sum10String":"0.00","sum11String":"0.00","sum12String":"0.00","sum1String":"0.00","sum2String":"0.00","sum3String":"0.00","sum4String":"0.00","sum5String":"0.00","sum6String":"0.00","sum7String":"0.00","sum8String":"0.00","sum9String":"0.00","sumString":"0.00","time":"","type":""},{"bigDecimal1":"","bigDecimal1String":"0.00","bigDecimal2":"","bigDecimal2String":"0.00","count":"","integer1":2,"integer2":"","standby1":"5b6cc6bbb9fc4a6bb77a63fe4b58dab8","standby2":"衡阳火车站1","standby3":"hytrain","standby4":"","standby4String":"0.00","string1":"广铁分局","sum":"","sum10String":"0.00","sum11String":"0.00","sum12String":"0.00","sum1String":"0.00","sum2String":"0.00","sum3String":"0.00","sum4String":"0.00","sum5String":"0.00","sum6String":"0.00","sum7String":"0.00","sum8String":"0.00","sum9String":"0.00","sumString":"0.00","time":"","type":""}]
         * statistical : {"unImportCount":2}
         */

        private StatisticalBean statistical;
        private List<ListBean> list;

        public StatisticalBean getStatistical() {
            return statistical;
        }

        public void setStatistical(StatisticalBean statistical) {
            this.statistical = statistical;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class StatisticalBean implements Serializable{
            /**
             * unImportCount : 2
             */

            private int unImportCount;

            public int getUnImportCount() {
                return unImportCount;
            }

            public void setUnImportCount(int unImportCount) {
                this.unImportCount = unImportCount;
            }
        }

        public static class ListBean implements Serializable{
            /**
             * bigDecimal1 :
             * bigDecimal1String : 0.00
             * bigDecimal2 :
             * bigDecimal2String : 0.00
             * count :
             * integer1 : 1
             * integer2 :
             * standby1 : 1f8130a0bf664d4aab0f578628c991a5
             * standby2 : 广州南车站
             * standby3 : gzntrain
             * standby4 :
             * standby4String : 0.00
             * string1 : 广州车务段
             * sum :
             * sum10String : 0.00
             * sum11String : 0.00
             * sum12String : 0.00
             * sum1String : 0.00
             * sum2String : 0.00
             * sum3String : 0.00
             * sum4String : 0.00
             * sum5String : 0.00
             * sum6String : 0.00
             * sum7String : 0.00
             * sum8String : 0.00
             * sum9String : 0.00
             * sumString : 0.00
             * time :
             * type :
             */

            private String bigDecimal1;
            private String bigDecimal1String;
            private String bigDecimal2;
            private String bigDecimal2String;
            private String count;
            private int integer1;
            private String integer2;
            private String standby1;
            private String standby2;
            private String standby3;
            private String standby4;
            private String standby4String;
            private String string1;
            private String sum;
            private String sum10String;
            private String sum11String;
            private String sum12String;
            private String sum1String;
            private String sum2String;
            private String sum3String;
            private String sum4String;
            private String sum5String;
            private String sum6String;
            private String sum7String;
            private String sum8String;
            private String sum9String;
            private String sumString;
            private String time;
            private String type;

            public String getBigDecimal1() {
                return bigDecimal1;
            }

            public void setBigDecimal1(String bigDecimal1) {
                this.bigDecimal1 = bigDecimal1;
            }

            public String getBigDecimal1String() {
                return bigDecimal1String;
            }

            public void setBigDecimal1String(String bigDecimal1String) {
                this.bigDecimal1String = bigDecimal1String;
            }

            public String getBigDecimal2() {
                return bigDecimal2;
            }

            public void setBigDecimal2(String bigDecimal2) {
                this.bigDecimal2 = bigDecimal2;
            }

            public String getBigDecimal2String() {
                return bigDecimal2String;
            }

            public void setBigDecimal2String(String bigDecimal2String) {
                this.bigDecimal2String = bigDecimal2String;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public int getInteger1() {
                return integer1;
            }

            public void setInteger1(int integer1) {
                this.integer1 = integer1;
            }

            public String getInteger2() {
                return integer2;
            }

            public void setInteger2(String integer2) {
                this.integer2 = integer2;
            }

            public String getStandby1() {
                return standby1;
            }

            public void setStandby1(String standby1) {
                this.standby1 = standby1;
            }

            public String getStandby2() {
                return standby2;
            }

            public void setStandby2(String standby2) {
                this.standby2 = standby2;
            }

            public String getStandby3() {
                return standby3;
            }

            public void setStandby3(String standby3) {
                this.standby3 = standby3;
            }

            public String getStandby4() {
                return standby4;
            }

            public void setStandby4(String standby4) {
                this.standby4 = standby4;
            }

            public String getStandby4String() {
                return standby4String;
            }

            public void setStandby4String(String standby4String) {
                this.standby4String = standby4String;
            }

            public String getString1() {
                return string1;
            }

            public void setString1(String string1) {
                this.string1 = string1;
            }

            public String getSum() {
                return sum;
            }

            public void setSum(String sum) {
                this.sum = sum;
            }

            public String getSum10String() {
                return sum10String;
            }

            public void setSum10String(String sum10String) {
                this.sum10String = sum10String;
            }

            public String getSum11String() {
                return sum11String;
            }

            public void setSum11String(String sum11String) {
                this.sum11String = sum11String;
            }

            public String getSum12String() {
                return sum12String;
            }

            public void setSum12String(String sum12String) {
                this.sum12String = sum12String;
            }

            public String getSum1String() {
                return sum1String;
            }

            public void setSum1String(String sum1String) {
                this.sum1String = sum1String;
            }

            public String getSum2String() {
                return sum2String;
            }

            public void setSum2String(String sum2String) {
                this.sum2String = sum2String;
            }

            public String getSum3String() {
                return sum3String;
            }

            public void setSum3String(String sum3String) {
                this.sum3String = sum3String;
            }

            public String getSum4String() {
                return sum4String;
            }

            public void setSum4String(String sum4String) {
                this.sum4String = sum4String;
            }

            public String getSum5String() {
                return sum5String;
            }

            public void setSum5String(String sum5String) {
                this.sum5String = sum5String;
            }

            public String getSum6String() {
                return sum6String;
            }

            public void setSum6String(String sum6String) {
                this.sum6String = sum6String;
            }

            public String getSum7String() {
                return sum7String;
            }

            public void setSum7String(String sum7String) {
                this.sum7String = sum7String;
            }

            public String getSum8String() {
                return sum8String;
            }

            public void setSum8String(String sum8String) {
                this.sum8String = sum8String;
            }

            public String getSum9String() {
                return sum9String;
            }

            public void setSum9String(String sum9String) {
                this.sum9String = sum9String;
            }

            public String getSumString() {
                return sumString;
            }

            public void setSumString(String sumString) {
                this.sumString = sumString;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
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

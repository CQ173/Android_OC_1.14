package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class ImportRecordBean implements Serializable {


    /**
     * code : 0
     * data : [{"bigDecimal1":0,"count":121,"integer1":0,"standby1":"395f2256c91a46d08befbd400d55c378","standby2":"长沙火车站","standby3":"changshastation","standby4":11818,"standby4String":"11818","string1":"广州铁路集团-铁路总局","sum":3050595.5,"sumString":"3050595.50","time":"2018-07-16 15:24:38","type":""},{"bigDecimal1":0,"count":80,"integer1":0,"standby1":"23fec4f287e344179fdb834daf60cdc3","standby2":"广州南车站","standby3":"guangzhounan","standby4":6624,"standby4String":"6624","string1":"广州铁路集团-铁路总局","sum":1504364,"sumString":"1504364.00","time":"2018-07-16 12:51:37","type":""},{"bigDecimal1":0,"count":92,"integer1":0,"standby1":"b48d0b7eff144ba19cc28a94e6c9edf3","standby2":"佛山火车站","standby3":"foshanstation","standby4":7167,"standby4String":"7167","string1":"广州铁路集团-铁路总局","sum":1746636,"sumString":"1746636.00","time":"2018-07-16 11:54:38","type":""},{"bigDecimal1":0,"count":82,"integer1":0,"standby1":"48c0ad2f7318479b95063f332a91129d","standby2":"衡阳火车站","standby3":"hengyangstation","standby4":4777,"standby4String":"4777","string1":"广州铁路集团-铁路总局","sum":721964.5,"sumString":"721964.50","time":"2018-07-16 11:05:45","type":""},{"bigDecimal1":0,"count":14,"integer1":0,"standby1":"47874e08f5be4f43a8e11079afd33388","standby2":"溆浦火车站","standby3":"xupustation","standby4":595,"standby4String":"595","string1":"广州铁路集团-铁路总局","sum":98668,"sumString":"98668.00","time":"2018-07-16 11:00:02","type":""},{"bigDecimal1":0,"count":23,"integer1":0,"standby1":"3ad829899654475596bf123b9a1dccb4","standby2":"益阳火车站","standby3":"yiyangstation","standby4":1037,"standby4String":"1037","string1":"广州铁路集团-铁路总局","sum":223559.5,"sumString":"223559.50","time":"2018-07-16 10:30:23","type":""},{"bigDecimal1":0,"count":18,"integer1":0,"standby1":"55dfaf6aeabe431fbb385184e6a57734","standby2":"怀化火车站","standby3":"huaihuastation","standby4":1063,"standby4String":"1063","string1":"广州铁路集团-铁路总局","sum":190765.5,"sumString":"190765.50","time":"2018-07-16 10:25:03","type":""},{"bigDecimal1":0,"count":18,"integer1":0,"standby1":"c77f9f3437514edeb0d611a613619fb5","standby2":"肇庆火车站","standby3":"zhaoqingstation","standby4":1523,"standby4String":"1523","string1":"广州铁路集团-铁路总局","sum":254796.5,"sumString":"254796.50","time":"2018-07-16 10:23:04","type":""},{"bigDecimal1":0,"count":14,"integer1":0,"standby1":"b951b57a41ee4095a0dd3ea395b3ba07","standby2":"衡山火车站","standby3":"hengshanstation","standby4":613,"standby4String":"613","string1":"广州铁路集团-铁路总局","sum":91320,"sumString":"91320.00","time":"2018-07-16 09:43:30","type":""},{"bigDecimal1":0,"count":46,"integer1":0,"standby1":"f1fb8558ec1f4a07bab549fe39d97483","standby2":"岳阳火车站","standby3":"yueyangstation","standby4":3004,"standby4String":"3004","string1":"广州铁路集团-铁路总局","sum":668465,"sumString":"668465.00","time":"2018-07-16 09:23:54","type":""},{"bigDecimal1":0,"count":31,"integer1":0,"standby1":"15b03173c74b4a40b6ebd4f0cc91b0a2","standby2":"常德火车站","standby3":"changdestation","standby4":2244,"standby4String":"2244","string1":"广州铁路集团-铁路总局","sum":426854,"sumString":"426854.00","time":"2018-07-16 09:12:23","type":""},{"bigDecimal1":0,"count":8,"integer1":0,"standby1":"09317364e46c4bca8a92844b2090f37f","standby2":"汨罗火车站","standby3":"miluostation","standby4":464,"standby4String":"464","string1":"广州铁路集团-铁路总局","sum":103075,"sumString":"103075.00","time":"2018-07-16 09:08:31","type":""}]
     * msg : 请求成功
     * size : 12
     */

    private String code;
    private String msg;
    private int size;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bigDecimal1 : 0
         * count : 121
         * integer1 : 0
         * standby1 : 395f2256c91a46d08befbd400d55c378
         * standby2 : 长沙火车站
         * standby3 : changshastation
         * standby4 : 11818
         * standby4String : 11818
         * string1 : 广州铁路集团-铁路总局
         * sum : 3050595.5
         * sumString : 3050595.50
         * time : 2018-07-16 15:24:38
         * type :
         */

        private int bigDecimal1;
        private int count;
        private int integer1;
        private String standby1;
        private String standby2;
        private String standby3;
        private int standby4;
        private String standby4String;
        private String string1;
        private double sum;
        private String sumString;
        private String time;
        private String type;

        public int getBigDecimal1() {
            return bigDecimal1;
        }

        public void setBigDecimal1(int bigDecimal1) {
            this.bigDecimal1 = bigDecimal1;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getInteger1() {
            return integer1;
        }

        public void setInteger1(int integer1) {
            this.integer1 = integer1;
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

        public int getStandby4() {
            return standby4;
        }

        public void setStandby4(int standby4) {
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

        public double getSum() {
            return sum;
        }

        public void setSum(double sum) {
            this.sum = sum;
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

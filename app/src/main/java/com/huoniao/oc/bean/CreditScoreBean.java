package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class CreditScoreBean {


    /**
     * code : 0
     * count : 7
     * data : [{"createDate":"2018-03-21 14:54:36","id":"ac3b143709b24b508cda8d8fdfe35997","instructions":"测试","score":1,"type":"1"},{"createDate":"2018-03-21 14:44:17","id":"5362c71e2b3c456d873a525d9849195a","instructions":"测试","score":100,"type":"0"}]
     * msg : 请求成功
     * next : 2
     * size : 2
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

    public static class DataBean {
        /**
         * createDate : 2018-03-21 14:54:36
         * id : ac3b143709b24b508cda8d8fdfe35997
         * instructions : 测试
         * score : 1
         * type : 1
         */

        private String createDate;
        private String id;
        private String instructions;
        private int score;
        private String type;

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

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

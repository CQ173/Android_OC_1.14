package com.huoniao.oc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class UserRoleTypeBean {

    /**
     * code : 0
     * list : [{"id":"1","name":"系统管理员"},{"id":"4","name":"火车站管理员"},{"id":"5","name":"代售点管理员"},{"id":"6","name":"普通用户"},{"id":"7","name":"证券公司"},{"id":"8","name":"会计"},{"id":"9","name":"待审核用户"},{"id":"10","name":"出纳"},{"id":"11","name":"商户"},{"id":"12","name":"子账号用户"},{"id":"13","name":"铁路分局"},{"id":"14","name":"铁路总局"}]
     * msg : 请求成功
     */

    private String code;
    private String msg;
    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * name : 系统管理员
         */

        private String id;
        private String name;

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
    }
}

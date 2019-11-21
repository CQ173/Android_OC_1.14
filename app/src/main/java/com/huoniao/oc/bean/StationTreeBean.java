package com.huoniao.oc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */

public class StationTreeBean implements Serializable {


    /**
     * code : 0
     * data : {"childList":[{"childList":[{"childList":[{"id":"d457ea9122df45ae8a4d9c9178d5a39c","name":"长沙市火车站(测试专用)","parentId":"edc034d92c5c4709b9a6b30025c6e28d","parentIds":"0,1,27edba57c03b437da224fa9042cc138d,edc034d92c5c4709b9a6b30025c6e28d,"}],"id":"edc034d92c5c4709b9a6b30025c6e28d","name":"广铁分局","parentId":"27edba57c03b437da224fa9042cc138d","parentIds":"0,1,27edba57c03b437da224fa9042cc138d,","type":"7"}],"id":"27edba57c03b437da224fa9042cc138d","name":"铁路总局","parentId":"1","parentIds":"0,1,","type":"6"}],"id":"1","name":"O计平台","parentId":"","parentIds":"0,","type":"1"}
     * msg : 请求成功
     */

    private String code;
    private DataBean data;
    private String msg;

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

    public static class DataBean {
        /**
         * childList : [{"childList":[{"childList":[{"id":"d457ea9122df45ae8a4d9c9178d5a39c","name":"长沙市火车站(测试专用)","parentId":"edc034d92c5c4709b9a6b30025c6e28d","parentIds":"0,1,27edba57c03b437da224fa9042cc138d,edc034d92c5c4709b9a6b30025c6e28d,"}],"id":"edc034d92c5c4709b9a6b30025c6e28d","name":"广铁分局","parentId":"27edba57c03b437da224fa9042cc138d","parentIds":"0,1,27edba57c03b437da224fa9042cc138d,","type":"7"}],"id":"27edba57c03b437da224fa9042cc138d","name":"铁路总局","parentId":"1","parentIds":"0,1,","type":"6"}]
         * id : 1
         * name : O计平台
         * parentId :
         * parentIds : 0,
         * type : 1
         */

        private String id;
        private String name;
        private String parentId;
        private String parentIds;
        private String type;
        private List<ChildListBeanXX> childList;

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

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
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

        public List<ChildListBeanXX> getChildList() {
            return childList;
        }

        public void setChildList(List<ChildListBeanXX> childList) {
            this.childList = childList;
        }

        public static class ChildListBeanXX {
            /**
             * childList : [{"childList":[{"id":"d457ea9122df45ae8a4d9c9178d5a39c","name":"长沙市火车站(测试专用)","parentId":"edc034d92c5c4709b9a6b30025c6e28d","parentIds":"0,1,27edba57c03b437da224fa9042cc138d,edc034d92c5c4709b9a6b30025c6e28d,"}],"id":"edc034d92c5c4709b9a6b30025c6e28d","name":"广铁分局","parentId":"27edba57c03b437da224fa9042cc138d","parentIds":"0,1,27edba57c03b437da224fa9042cc138d,","type":"7"}]
             * id : 27edba57c03b437da224fa9042cc138d
             * name : 铁路总局
             * parentId : 1
             * parentIds : 0,1,
             * type : 6
             */

            private String id;
            private String name;
            private String parentId;
            private String parentIds;
            private String type;
            private List<ChildListBeanX> childList;

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

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
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

            public List<ChildListBeanX> getChildList() {
                return childList;
            }

            public void setChildList(List<ChildListBeanX> childList) {
                this.childList = childList;
            }

            public static class ChildListBeanX {
                /**
                 * childList : [{"id":"d457ea9122df45ae8a4d9c9178d5a39c","name":"长沙市火车站(测试专用)","parentId":"edc034d92c5c4709b9a6b30025c6e28d","parentIds":"0,1,27edba57c03b437da224fa9042cc138d,edc034d92c5c4709b9a6b30025c6e28d,"}]
                 * id : edc034d92c5c4709b9a6b30025c6e28d
                 * name : 广铁分局
                 * parentId : 27edba57c03b437da224fa9042cc138d
                 * parentIds : 0,1,27edba57c03b437da224fa9042cc138d,
                 * type : 7
                 */

                private String id;
                private String name;
                private String parentId;
                private String parentIds;
                private String type;
                private List<ChildListBean> childList;

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

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
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

                public List<ChildListBean> getChildList() {
                    return childList;
                }

                public void setChildList(List<ChildListBean> childList) {
                    this.childList = childList;
                }

                public static class ChildListBean {
                    /**
                     * id : d457ea9122df45ae8a4d9c9178d5a39c
                     * name : 长沙市火车站(测试专用)
                     * parentId : edc034d92c5c4709b9a6b30025c6e28d
                     * parentIds : 0,1,27edba57c03b437da224fa9042cc138d,edc034d92c5c4709b9a6b30025c6e28d,
                     */

                    private String id;
                    private String name;
                    private String parentId;
                    private String parentIds;

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

                    public String getParentId() {
                        return parentId;
                    }

                    public void setParentId(String parentId) {
                        this.parentId = parentId;
                    }

                    public String getParentIds() {
                        return parentIds;
                    }

                    public void setParentIds(String parentIds) {
                        this.parentIds = parentIds;
                    }
                }
            }
        }
    }
}

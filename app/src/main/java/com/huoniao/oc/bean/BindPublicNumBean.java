package com.huoniao.oc.bean;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/3/10.
 */

public class BindPublicNumBean {


    /**
     * code : 0
     * data : [{"headimgUrl":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIN6WxtFttx5iaJOcSmoxxjA2zMtsnjn6X2ylzhT8QsHHic7mkDdgWguHibDhL0nicO29yB6e9BR4mePg/132","id":"145ac15f5c834c6cb9d6eff7b8093476","nickName":"Je pense a toi!! ldiot"},{"headimgUrl":"http://thirdwx.qlogo.cn/mmopen/vi_32/g4icy3ibvOalCNsxh9QpicUePFjUQ1UuvcZlqa6Gp0Rguey5xEp8Tlia6OChxhNdBtAvxpKOspbtMcXaFPA9m6NZLg/132","id":"14eb53bfcc2e4cb9a2c35176533f2663","nickName":"crystal"},{"headimgUrl":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erUJEPGdAU3PCLu616LGibI0toy9WBqSITuUWxQ7clB8QiaUib1Fpkzn6EandzhQYI5HkWb3yr2I6FIw/132","id":"73656dbe685242668bbab938a33b900e","nickName":"懒小兔"}]
     * msg : 请求成功
     * size : 3
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
         * headimgUrl : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIN6WxtFttx5iaJOcSmoxxjA2zMtsnjn6X2ylzhT8QsHHic7mkDdgWguHibDhL0nicO29yB6e9BR4mePg/132
         * id : 145ac15f5c834c6cb9d6eff7b8093476
         * nickName : Je pense a toi!! ldiot
         */

        private String headimgUrl;
        private String id;
        private String nickName;

        private File file;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getHeadimgUrl() {
            return headimgUrl;
        }

        public void setHeadimgUrl(String headimgUrl) {
            this.headimgUrl = headimgUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}

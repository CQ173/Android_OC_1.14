package com.huoniao.oc.bean;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/22.
 */

public class CarouseImgBean implements Serializable {
    private int level;//轮播图序号
    private String imgUrl;//轮播图路径
    private String remarks;//轮播图备注
    private String tag;
    private int postion;
    private File file;
    private String linkUrl;//轮播图链接

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private Bitmap bitmap;

    public CarouseImgBean() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public CarouseImgBean(int level, String imgUrl, String remarks, String linkUrl) {
        this.level = level;
        this.imgUrl = imgUrl;
        this.remarks = remarks;
        this.linkUrl = linkUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}

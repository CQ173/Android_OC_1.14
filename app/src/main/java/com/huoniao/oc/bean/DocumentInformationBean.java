package com.huoniao.oc.bean;

import java.io.File;

/**
 * Created by Administrator on 2017/9/8.
 */

public class DocumentInformationBean {
     public String imageFlag;  //图片标记
     public File imageFile; //图片文件
     public String imageName; //图片文字
      public String imageSrc;

    public DocumentInformationBean(String imageFlag, String imageName) {
        this.imageFlag = imageFlag;
        this.imageName = imageName;
    }


}

package com.huoniao.oc.util;

import java.text.DecimalFormat;

/**
 * Created by Cloud on 2017/5/16.
 */

public class NumberFormatUtils {
    //格式化数据保留两位小数 并且带￥
    public static String formatDigits(double price){
        DecimalFormat df   =   new   DecimalFormat("###############0.00");
        String s=df.format(price);
        return s;
       /* NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(price).replace("$","");*/
    }




}

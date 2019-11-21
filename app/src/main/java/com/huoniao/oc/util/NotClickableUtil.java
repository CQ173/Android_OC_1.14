package com.huoniao.oc.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2019/4/11.
 */

public class NotClickableUtil {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
//    private static final int MIN_CLICK_DELAY_TIME = 1000;
//    private static long lastClickTime;
//
//    public static boolean isFastClick() {
//        boolean flag = false;
//        long curClickTime = System.currentTimeMillis();
//        if ((curClickTime - lastClickTime) <= MIN_CLICK_DELAY_TIME) {
//            flag = true;
//        }
//        lastClickTime = curClickTime;
//        return flag;
//    }

    /**
     * 功能：判断是否是进行了快速点击的操作
     */
    private static long lastClickTimea = 0;//上次点击的时间
    private static int spaceTime = 1000;//时间间隔   
    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();//当前系统时间       
        boolean isAllowClick;//是否允许点击       
        if (currentTime - lastClickTimea > spaceTime) {
            isAllowClick = false;
        } else {
            isAllowClick = true;
        }
        lastClickTimea = currentTime;
        return isAllowClick;
    }

}

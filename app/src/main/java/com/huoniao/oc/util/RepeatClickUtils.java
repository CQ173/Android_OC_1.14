package com.huoniao.oc.util;

/**
 * 防止重复点击
 */

public class RepeatClickUtils {

    private static long endTime;
    public static boolean repeatClick(){
          long statTime = System.currentTimeMillis();
        if(statTime - endTime >3000){
           endTime  = statTime;
            return true;
        }
        return false;
    }
}

package com.huoniao.oc.util;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/6/5.
 */

public class LisostringUtil {

    public static String listToString(ArrayList<Object> stringList){
        if (stringList == null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (Object string : stringList) {
            if (flag) {
                result.append(","); // 分隔符
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }
}

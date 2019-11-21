package com.huoniao.oc.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/10/23.
 */

public class VersonCodeUtils {
    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context)
    {
        int versionCode = 0;
        try
        {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.huoniao.oc", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getVersionName(Context context)
    {
        String versionCode ="";
        try
        {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.huoniao.oc", 0).versionName;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

}

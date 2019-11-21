package com.huoniao.oc.util;

import android.app.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class FinishAllActivity  {
    private List<Activity> mActivityList;

    public FinishAllActivity(){
        mActivityList = new ArrayList<>();
    }

    /**
     * 添加单个Activity
     */
    public void addActivity(Activity activity) {
        if (!mActivityList.contains(activity)) {
            mActivityList.add(activity);
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity);
            if (activity != null){
                activity.finish();
            }
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeAllActivity() {
        for (Activity activity : mActivityList) {
            if (activity != null){
                activity.finish();
            }
        }
    }

}

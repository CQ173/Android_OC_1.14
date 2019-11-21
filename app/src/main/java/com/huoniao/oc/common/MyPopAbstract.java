package com.huoniao.oc.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by  on 2017/6/26.
 */

public abstract class MyPopAbstract {
    @Deprecated
     public MyPopWindow poPwindow(Activity activity,boolean focusable ){
        MyPopWindow myPopWindow = null;
         View view= LayoutInflater.from(activity).inflate(layout(),null);
         setMapSettingViewWidget(view);
        if (myPopWindow == null) {
            myPopWindow = new MyPopWindow.PopupWindowBuilder(activity)
                    .setView(view)
                    .setFocusable(focusable)  //true  表示可以获取焦点 false 不可以获取焦点   由于popwindow 在有的手机设置了 这个 也设置了下面一系列设置 就是为了不关闭popwindow但是有的手机还会关闭  现在就控制focusable
                    .setClippingEnable(false)
                    .setOnTouchTouchable(false)
                    .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    //  .setAnimationStyle(R.style.popwindow_anim)
                    .create();
        }
         myPopWindow.keyCodeDismiss(false);
         return myPopWindow;
     }
    @Deprecated
    public MyPopWindow popWindowTouch(Activity activity){
        View view= LayoutInflater.from(activity).inflate(layout(),null);
        setMapSettingViewWidget(view);
        MyPopWindow myPopWindow  = new MyPopWindow.PopupWindowBuilder(activity)
                .setView(view)
                .setFocusable(true)
                .setClippingEnable(true)
                .setOnTouchTouchable(true)
                .size(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
               // .setAnimationStyle(R.style.popwindow_anim)
                .create();
        myPopWindow.keyCodeDismiss(true);
        return myPopWindow;
    }

    @Deprecated
    public MyPopWindow popWindowTouch2(Activity activity){
        View view= LayoutInflater.from(activity).inflate(layout(),null);
        setMapSettingViewWidget(view);
        MyPopWindow myPopWindow  = new MyPopWindow.PopupWindowBuilder(activity)
                .setView(view)
                .setFocusable(true)
                .setClippingEnable(true)
                .setOnTouchTouchable(true)
                .size(-2,-2)
              //  .setAnimationStyle(R.style.popwindow_anim)
                .create();
        myPopWindow.keyCodeDismiss(true);
        return myPopWindow;
    }


    /**
     *   此方法可以 自己设置popwindow的属性
     * @param activity
     * @return
     */
    public MyPopWindow.PopupWindowBuilder popupWindowBuilder(Activity activity){
        View view= LayoutInflater.from(activity).inflate(layout(),null);
        setMapSettingViewWidget(view);
        MyPopWindow.PopupWindowBuilder myPopupWindowBuilder  = new MyPopWindow.PopupWindowBuilder(activity)
                .setView(view)
                .setFocusable(true)
                .setClippingEnable(true)
                .setOnTouchTouchable(true)
                .setBgDarkAlpha(0.5f)
               // .setAnimationStyle(R.style.popwindow_anim)
                .enableBackgroundDark(true);
        return myPopupWindowBuilder;
    }


    protected abstract void setMapSettingViewWidget(View view);

    protected abstract int layout();

}

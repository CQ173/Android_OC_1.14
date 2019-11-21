package com.huoniao.oc.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huoniao.oc.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */

public  class ShowPopupWindowTwo {
    private View popUpView;
    private PopupWindow popUpWindow;
       //公共弹出pop
    public void popWindow(Context context,View view, int width, int height, int layout,int confirmid,int cancleid) {
        popUpView = LayoutInflater.from(context).inflate(layout, null);

        if (popUpWindow == null) {
            popUpWindow = new PopupWindow(popUpView, width, height, true);
            popUpWindow.setFocusable(true);
            popUpWindow.setOutsideTouchable(true);
            popUpWindow.setClippingEnabled(false);  //设置覆盖状态栏
        }



        popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popUpWindow.dismiss();
            }
        });

        TextView confirm_id = (TextView) popUpView.findViewById(confirmid);
        TextView cancle_id = (TextView) popUpView.findViewById(cancleid);
        confirm_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpWindow.dismiss();
                if(pop!=null) {
                    pop.confirm();
                }

            }
        });
        cancle_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpWindow.dismiss();
                if(pop!=null) {
                    pop.cancle();
                }

            }
        });



        //在指定控件的下面
        popUpWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }



    //
    public void popWindows(Context context, View view, int width, int height, List list) {
           View   popView = LayoutInflater.from(context).inflate(R.layout.map_setting_pop, null);

            popUpWindow = new PopupWindow(popView, width, height, true);
            popUpWindow.setFocusable(true);
            popUpWindow.setOutsideTouchable(true);
            popUpWindow.setClippingEnabled(false);  //设置覆盖状态栏


        popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popUpWindow.dismiss();
            }
        });
        ListView lv_setting_map =(ListView) popView.findViewById(R.id.lv_setting_map);


       /* TextView tv_mainAccount=(TextView) popView.findViewById(tv_mainAccount);   //主账号
        TextView tv_location = (TextView) popView.findViewById(R.id.tv_location);  // 地理位置
        TextView tv_windowNumbe = (TextView) popView.findViewById(R.id.tv_windowNumbe); //挂靠窗口号
        TextView tv_nick = (TextView) popView.findViewById(R.id.tv_nick); //代售点名称
        TextView tv_legalPerson = (TextView) popView.findViewById(R.id.tv_legalPerson); //法人
        TextView tv_personInCharge = (TextView) popView.findViewById(R.id.tv_personInCharge); //负责人*/
        TextView tv_confirm = (TextView) popView.findViewById(R.id.tv_confirm);
      tv_confirm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if(pop != null){
                  pop.confirm();
              }
          }
      });

        //在指定控件的下面
        popUpWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }




    public PopupWindow getRefundPopWindow(){
        if(popUpWindow != null){
            return popUpWindow;
        }
        return null;
    }

    public void popDismiss(){
        if(popUpWindow != null){
           popUpWindow.dismiss();
        }
    }

    //回调是否点击确定按钮或者取消按钮  做相应的操作
    private PopRefund pop;
    public void setPopRefundListener(PopRefund popRefund){
        this.pop = popRefund;
    }
    public interface PopRefund{
        void confirm();
        void cancle();
    }

}

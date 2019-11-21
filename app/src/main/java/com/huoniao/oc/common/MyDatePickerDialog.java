package com.huoniao.oc.common;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MyDatePickerDialog {
    private  Calendar calendar;
    private DatePickerDialog mDialog2;

    public MyDatePickerDialog(){
        calendar = Calendar.getInstance();
    }


    public void datePickerDialog(final Context context){

        mDialog2 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String month = "";
            if ((monthOfYear + 1) < 10){
                month = "0" + (monthOfYear + 1);
            }else {
                month = "" + (monthOfYear + 1);
            }
            String day = "";
            if ((dayOfMonth) < 10){
                day = "0" + (dayOfMonth);
            }else {
                day = "" + (dayOfMonth);
            }
            String  date = year + "-" + month + "-" + day;
                if(datePicker !=null){
                    datePicker.date(date);
                    datePicker.splitDate(year,monthOfYear,dayOfMonth);
                }
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

              mDialog2.show();


    }

    private DatePicker datePicker;

    public void setDatePickerListener(DatePicker datePicker){
        this.datePicker = datePicker;
    }

    public interface DatePicker{
        void date(String date);
        void splitDate(int year, int monthOfYear, int dayOfMonth);
    }

    public void calendarMethod(int  year,int monthOfYear,int dayOfMonth){
        if(calendar !=null) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    }





}

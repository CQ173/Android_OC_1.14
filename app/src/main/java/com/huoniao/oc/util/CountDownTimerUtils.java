package com.huoniao.oc.util;  
  
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.huoniao.oc.R;
  
/** 
 * Created by lpj on 2016/11/02. 
 */  
public class CountDownTimerUtils extends CountDownTimer {  
    private TextView mTextView;  
  
   
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {  
        super(millisInFuture, countDownInterval);  
        this.mTextView = textView;  
    }  
  
    @Override  
    public void onTick(long millisUntilFinished) {  
        mTextView.setClickable(false); //设置不可点击  
        mTextView.setText("已发送("+millisUntilFinished / 1000 + "S)");  //设置倒计时时间
        mTextView.setBackgroundResource(R.drawable.bg_identify_code_press); //设置按钮为灰色，这时是不能点击的  
  
       
        SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字  
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);  
        /** 
         * public void setSpan(Object what, int start, int end, int flags) { 
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。 
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。 
         */  
        spannableString.setSpan(span, 4, 6, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.setText(spannableString);  
    }  
  
    @Override  
    public void onFinish() {  
        mTextView.setText("重新发送");
        mTextView.setClickable(true);//重新获得点击  
        mTextView.setBackgroundResource(R.drawable.bg_identify_code_normal);  //还原背景色  
    }  
}  
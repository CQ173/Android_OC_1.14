package com.huoniao.oc.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

//public class MyListView extends ListView {
public class MyListView2 extends ListView {

    public MyListView2(Context context) {
        super(context);
    }

    public MyListView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


        int action = ev.getAction();

        switch (action){
            case MotionEvent.ACTION_MOVE:
                Log.d("MyListView", "dispatchTouchEvent move");
//                requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
//                requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//        int action = ev.getAction();
//
//        switch (action){
//            case MotionEvent.ACTION_MOVE:
//                Log.d("MyListView", "onInterceptTouchEvent move");
//                break;
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }

    // -------------------------
    // 使用 onMeasure 方法，来解决尺寸高度的问题，以及事件冲突的问题；


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        int hSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (hMode){
            case MeasureSpec.UNSPECIFIED:
                Log.d("MyListView", "hMode UNSPECIFIED");
                break;
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                break;
        }

        Log.d("MyListView", "hSize = " + hSize);

        // 因为 ScrollView + ListView 时，高度模式强制设置为 UNSPECIFIED，
        // ListView 只会计算一条的高度；
        // 将 height 的模式，强制设置为 AT_MOST 就会进行计算实际的高度了；
        // 考虑到ListView条目数量不确定，高度也不确定，height size 应该是
        // 是一个最大值；

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE>>2,
                MeasureSpec.AT_MOST
        );

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

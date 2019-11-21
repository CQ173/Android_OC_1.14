package com.huoniao.oc.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/6/26.
 */


    public class MyMapView extends FrameLayout {

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                getParent().requestDisallowInterceptTouchEvent(true);//拦截父类事件

            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                getParent().requestDisallowInterceptTouchEvent(false);
            }

            return super.dispatchTouchEvent(ev);
        }

        public MyMapView(Context context) {
            super(context);
        }

        public MyMapView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyMapView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

}

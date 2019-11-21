package com.huoniao.oc.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.huoniao.oc.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TextSwitcherView extends TextSwitcher implements ViewFactory {
    private ArrayList<String> reArrayList = new ArrayList<String>();
    private int resIndex = -1;
    private final int UPDATE_TEXTSWITCHER = 1;
    private int timerStartAgainCount = 0;
    private Context mContext;
    private OnItemClickListener itemClickListener;
    public TextSwitcherView(Context context) {

        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        init();
    }
    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
        // TODO Auto-generated constructor stub
    }

    private void init(){
        this.setFactory(this);
        this.setInAnimation(getContext(),R.anim.anim_marquee_in);
        this.setOutAnimation(getContext(), R.anim.anim_marquee_out);
        Timer timer = new Timer();
        timer.schedule(timerTask, 1,3000);
    }
    TimerTask timerTask =  new TimerTask() {

        @Override
        public void run() {   //不能在这里创建任何UI的更新，toast也不行
            // TODO Auto-generated method stub
                Message msg = new Message();
                msg.what = UPDATE_TEXTSWITCHER;
                handler.sendMessage(msg);
        }
    };
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case UPDATE_TEXTSWITCHER:
                updateTextSwitcher();

                break;
            default:
                break;
            }

        };
    };
    /**
     * 需要传递的资源
     * @param reArrayList
     */
    public void getResource(ArrayList<String> reArrayList) {
        this.reArrayList = reArrayList;
    }
    public void updateTextSwitcher() {
        if (this.reArrayList != null && this.reArrayList.size()>0) {
        	resIndex = (resIndex + 1) % this.reArrayList.size();
            this.setText(this.reArrayList.get(resIndex));    
        }

    }
    @Override
    public View makeView() {
        TextView tView = new TextView(getContext());
        tView.setTextSize(14);
        tView.setTextColor(Color.rgb(255, 159, 63));
        tView.setMaxLines(1);
        tView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        tView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && reArrayList.size() > 0 && resIndex >= 0) {
                   itemClickListener.onItemClick(resIndex % reArrayList.size());
//                	 itemClickListener.onItemClick(resIndex);
                }
            }
        });
        return tView;
    }
    
    /**
     * 设置点击事件监听
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 轮播文本点击监听器
     */
    public interface OnItemClickListener {
        /**
         * 点击回调
         * @param position 当前点击ID
         */
        void onItemClick(int position);
    }
}
package com.huoniao.oc.common;

/**
 * Created by admin on 2017/10/9.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.R;

/**
 * 可伸展的文本显示布局
 * @author jan
 */
public class StretchyTextView extends LinearLayout implements View.OnClickListener {
    //默认显示的最大行数
    private static final int DEFAULT_MAX_LINE_COUNT = 2;
    //当前展开标志显示的状态
    private static final int SPREADTEXT_STATE_NONE = 0;
    private static final int SPREADTEXT_STATE_RETRACT = 1;
    private static final int SPREADTEXT_STATE_SPREAD = 2;

    private TextView contentText;
    private TextView operateText;
    private LinearLayout bottomTextLayout;

    private String shrinkup;
    private String spread;
    private int mState;
    private boolean flag = false;
    private int maxLineCount = DEFAULT_MAX_LINE_COUNT;
    private InnerRunnable runable;

    public StretchyTextView(Context context) {
        this(context, null);
    }

    public StretchyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        shrinkup = context.getString(R.string.retract);
        spread = context.getString(R.string.spread);
        View view = inflate(context, R.layout.stretchy_text_layout, this);
        view.setPadding(0, -1, 0, 0);
        contentText = (TextView) view.findViewById(R.id.content_textview);
        operateText = (TextView) view.findViewById(R.id.bottom_textview);
        bottomTextLayout = (LinearLayout) view.findViewById(R.id.bottom_text_layout);
        setBottomTextGravity(Gravity.LEFT);
        operateText.setOnClickListener(this);
        runable = new InnerRunnable();


    }
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    // 半角转化为全角的方法
    public String ToSBC(String input) {
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127 && c[i]>32)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    public void getEllipsisCount(){
        contentText.post(new Runnable() {
            @Override
            public void run() {
                Layout l = contentText.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();

                    if (lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            if(ellipsisCount !=null){
                                ellipsisCount.getEllipsisCount(true);
                            }
                        }
                    }
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        flag = false;
        requestLayout();
    }

    public final void setContent(String charSequence) {
        contentText.setText(ToDBC(charSequence), TextView.BufferType.NORMAL);
        mState = SPREADTEXT_STATE_SPREAD;
        Log.d("setContent", "count lines="+contentText.getLineCount()+",flag="+flag);
        flag = false;
        requestLayout();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!flag) {
            flag = !flag;
            if (contentText.getLineCount() <= DEFAULT_MAX_LINE_COUNT) {
                mState = SPREADTEXT_STATE_NONE;
                operateText.setVisibility(View.GONE);
                contentText.setMaxLines(DEFAULT_MAX_LINE_COUNT + 1);
            }else {
                post(runable);
            }
        }
    }

    class InnerRunnable implements Runnable {
        @Override
        public void run() {
            if (mState == SPREADTEXT_STATE_SPREAD) {
                contentText.setMaxLines(maxLineCount);
                setEllipsisCountLinsenter(new EllipsisCount() {
                    @Override
                    public void getEllipsisCount(boolean ellipsis) {
                        operateText.setVisibility(View.VISIBLE);
                        operateText.setText(spread);
                    }
                });

                mState = SPREADTEXT_STATE_RETRACT;
            } else if (mState == SPREADTEXT_STATE_RETRACT) {
                contentText.setMaxLines(Integer.MAX_VALUE);
                operateText.setVisibility(View.VISIBLE);
                operateText.setText(shrinkup);
                mState = SPREADTEXT_STATE_SPREAD;
            }
            getEllipsisCount();
        }
    }

    public void setMaxLineCount(int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    public void setContentTextColor(int color){
        this.contentText.setTextColor(color);
    }

    public void setContentTextSize(float size){
        this.contentText.setTextSize(size);
    }
    /**
     * 内容字体加粗
     */
    public void setContentTextBold(){
        TextPaint textPaint = contentText.getPaint();
        textPaint.setFakeBoldText(true);
    }
    /**
     * 设置展开标识的显示位置
     * @param gravity
     */
    public void setBottomTextGravity(int gravity){
        bottomTextLayout.setGravity(gravity);
    }

    private  EllipsisCount ellipsisCount ;
    private  void setEllipsisCountLinsenter(EllipsisCount ellipsisCountLinsenter){
        this.ellipsisCount = ellipsisCountLinsenter;
    }

  interface EllipsisCount{
      void getEllipsisCount(boolean ellipsis);
  }


}

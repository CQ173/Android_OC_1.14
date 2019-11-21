package com.huoniao.oc.trainstation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.CalendarDate;
import com.huoniao.oc.bean.ChoiceDatesEvent;
import com.huoniao.oc.fragment.CalendarViewFragment;
import com.huoniao.oc.fragment.CalendarViewPagerFragment;
import com.huoniao.oc.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class ChoiceDateA extends BaseActivity implements
        CalendarViewPagerFragment.OnPageChangeListener,
        CalendarViewFragment.OnDateClickListener,
        CalendarViewFragment.OnDateCancelListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_dateMoth)
    TextView tvDateMoth;
    @InjectView(R.id.tv_complete)
    TextView tvComplete;

    private List<CalendarDate> mListDate = new ArrayList<>();
    private String dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_date);
        ButterKnife.inject(this);
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        // Fragment fragment = new CalendarViewPagerFragment();
        Fragment fragment = CalendarViewPagerFragment.newInstance(false);
        tx.replace(R.id.fl_content, fragment);
        tx.commit();
    }

    @OnClick({R.id.iv_back, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_complete:
                if (mListDate.size() > 10){
                    ToastUtils.showToast(ChoiceDateA.this, "选择日期不能超过10个！");
                }else if (mListDate.size() < 1){
                    ToastUtils.showToast(ChoiceDateA.this, "请至少选择一个日期！");
                }else {
                    dates = listToString(mListDate);
                    EventBus.getDefault().postSticky(new ChoiceDatesEvent(dates));
                    finish();
                }
                break;
        }
    }

    @Override
    public void onDateCancel(CalendarDate calendarDate) {
        int count = mListDate.size();
        for (int i = 0; i < count; i++) {
            CalendarDate date = mListDate.get(i);
            if (date.getSolar().solarDay == calendarDate.getSolar().solarDay) {
                mListDate.remove(i);
                break;
            }
        }
    }

    @Override
    public void onDateClick(CalendarDate calendarDate) {
        int year = calendarDate.getSolar().solarYear;
        int month = calendarDate.getSolar().solarMonth;
//        int day = calendarDate.getSolar().solarDay;
        tvDateMoth.setText(year + "-" + month);
        mListDate.add(calendarDate);
    }

    @Override
    public void onPageChange(int year, int month) {
        tvDateMoth.setText(year + "-" + month);
    }


    private static String listToString(List<CalendarDate> list) {
        StringBuffer stringBuffer = new StringBuffer();
        String month = "";
        String day = "";
        if (list.size() > 0) {
            for (CalendarDate date : list) {
                if (date.getSolar().solarMonth < 10){
                    month = "0" + date.getSolar().solarMonth;
                }else {
                    month = "" + date.getSolar().solarMonth;
                }

                if (date.getSolar().solarDay < 10){
                    day = "0" + date.getSolar().solarDay;
                }else {
                    day = "" + date.getSolar().solarDay;
                }

                stringBuffer.append(date.getSolar().solarYear + "-" + month + "-" + day).append(",");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }
}

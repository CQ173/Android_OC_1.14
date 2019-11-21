package com.huoniao.oc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.huoniao.oc.fragment.CalendarViewFragment;
import com.huoniao.oc.util.DateUtils2;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joybar on Administrator on 16/8/18.
 */
public class CalendarViewPagerAdapter extends FragmentStatePagerAdapter {

    public static final int NUM_ITEMS = 200;
    public static final int NUM_ITEMS_CURRENT = NUM_ITEMS/2;
    private int mThisMonthPosition = DateUtils2.getYear()*12+DateUtils2.getMonth()-1;//---100 -position
    private int number = mThisMonthPosition - NUM_ITEMS_CURRENT;
    private boolean isChoiceModelSingle;
    private static   List<CalendarViewFragment> arrayList = new ArrayList();

    public CalendarViewPagerAdapter(FragmentManager fm,boolean isChoiceModelSingle) {
        super(fm);
        this.isChoiceModelSingle = isChoiceModelSingle;
        for (int i = 0; i < NUM_ITEMS; i++) {
            int year = getYearByPosition(i);
            int month = getMonthByPosition(i);
               CalendarViewFragment fragment = CalendarViewFragment.newInstance(year,month,isChoiceModelSingle);
               arrayList.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return  arrayList.get(position);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    public  int getYearByPosition(int position){
        int year = (position+number)/12;
        return year;
    }
    public  int getMonthByPosition(int position){
        int month = (position + number) % 12 + 1;
        return month;
    }
}

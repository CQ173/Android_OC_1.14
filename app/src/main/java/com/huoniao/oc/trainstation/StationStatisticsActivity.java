package com.huoniao.oc.trainstation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MyfragmentViewpageAdapter;
import com.huoniao.oc.trainstation.stationfragment.TimeQueryFragment;
import com.huoniao.oc.trainstation.stationfragment.YearQueryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/6/3.
 */

public class StationStatisticsActivity extends BaseActivity{

    @InjectView(R.id.tv_Time_query)
    TextView but_Time_query;
    @InjectView(R.id.tv_Year_query)
    TextView but_Year_query;
    @InjectView(R.id.my_viewpager)
    ViewPager my_viewpager;
    @InjectView(R.id.ll_Time_query)
    LinearLayout ll_Time_query;
    @InjectView(R.id.ll_Year_query)
    LinearLayout ll_Year_query;
    @InjectView(R.id.tv_time_back)
    TextView tv_time_back;
    @InjectView(R.id.tv_year_back)
    TextView tv_year_back;

    private List<Fragment> list;
    private MyfragmentViewpageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_statistics);
        ButterKnife.inject(this);
        initView();
    }

    private void initView()
    {

        //绑定点击事件
        my_viewpager.setOnPageChangeListener(new MyPagerChangeListener()) ;
        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new TimeQueryFragment() );
        list.add(new YearQueryFragment() );
        adapter = new MyfragmentViewpageAdapter(getSupportFragmentManager(), list);
        my_viewpager.setAdapter(adapter);
        my_viewpager.setCurrentItem(0);  //初始化显示第一个页面

    }

    @OnClick({R.id.ll_Time_query , R.id.ll_Year_query , R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_Time_query:
                ll_Time_query  .setBackgroundColor(Color.parseColor("#FCFCFE"));
                ll_Year_query  .setBackgroundColor(Color.parseColor("#E0DFDF"));
                but_Time_query  .setTextColor(Color.parseColor("#131314"));
                but_Year_query  .setTextColor(Color.parseColor("#6E6E6E"));
                tv_time_back.setVisibility(View.VISIBLE);
                tv_year_back.setVisibility(View.GONE);
                my_viewpager.setCurrentItem(0);
                break;
            case R.id.ll_Year_query:
                ll_Time_query  .setBackgroundColor(Color.parseColor("#E0DFDF"));
                ll_Year_query  .setBackgroundColor(Color.parseColor("#FCFCFE"));
                but_Time_query .setTextColor(Color.parseColor("#6E6E6E"));
                but_Year_query .setTextColor(Color.parseColor("#131314"));
                tv_time_back.setVisibility(View.GONE);
                tv_year_back.setVisibility(View.VISIBLE);
                my_viewpager.setCurrentItem(1);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     *
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {//状态改变时底部对应的字体颜色改变
                case 0:
                    ll_Time_query  .setBackgroundColor(Color.parseColor("#FCFCFE"));
                    ll_Year_query  .setBackgroundColor(Color.parseColor("#E0DFDF"));
                    but_Time_query  .setTextColor(Color.parseColor("#131314"));
                    but_Year_query  .setTextColor(Color.parseColor("#6E6E6E"));
                    tv_time_back.setVisibility(View.VISIBLE);
                    tv_year_back.setVisibility(View.GONE);
                    break;
                case 1:
                    ll_Time_query  .setBackgroundColor(Color.parseColor("#E0DFDF"));
                    ll_Year_query  .setBackgroundColor(Color.parseColor("#FCFCFE"));
                    but_Time_query .setTextColor(Color.parseColor("#6E6E6E"));
                    but_Year_query .setTextColor(Color.parseColor("#131314"));
                    tv_time_back.setVisibility(View.GONE);
                    tv_year_back.setVisibility(View.VISIBLE);
                    break;
            }

        }

    }

}

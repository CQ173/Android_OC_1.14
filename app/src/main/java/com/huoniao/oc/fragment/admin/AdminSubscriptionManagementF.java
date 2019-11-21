package com.huoniao.oc.fragment.admin;


import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.AdminConsolidatedRecord;
import com.huoniao.oc.bean.AdminRankingBean;
import com.huoniao.oc.bean.ConversionRankingBean;
import com.huoniao.oc.bean.LineChartDataBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyOnChartGestureListener;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.NewMarkerView;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.custom.MyLineChart;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.custom.MySwipeRefreshLayout;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.trainstation.NewWarningInfoListA;
import com.huoniao.oc.trainstation.StationOutletsDetailsA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.NumberFormatUtils;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.MyApplication.treeIdList2;
import static com.huoniao.oc.util.NumberFormatUtils.formatDigits;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminSubscriptionManagementF extends BaseFragment implements OnChartValueSelectedListener, View.OnClickListener {
    private TextView tv_admin_ownership_institution;
    private TextView tv_admin_ownership_institution2;
    private TextView tv_importedPaysysRecord;
    private TextView tv_importedSumMoney;
    private TextView tv_noFinishPaysysRecord;
    private TextView tv_noFinishSumMoney;
    private TextView tv_admin_amount_paid;
    private TextView tv_admin_consolidated_record;
    private TextView tv_admin_total_amount_paid;
    private TextView tv_admin_average;
    private TextView tv_admin_week;
    private TextView tv_admin_month;
    private TextView tv_admin_year;
    private TextView tv_admin_week2;
    private TextView tv_admin_month2;
    private TextView tv_admin_year2;
    private MyLineChart lcAdminLineChart;
    private MyListView admin_lv_ranking;
    private TextView tv_linechart_explain;
    private LinearLayout ll_imported;
    private LinearLayout ll_noFinish;
    private Intent intent;
    private ScrollView scrollView;
    private String requestSuccessIsClosed="0"; //0表示可以关闭  1表示不可以
    private View v;

    private LinearLayout ll_payWaitDoEarlyWarning;//汇缴-汇缴预警-待处理
    private TextView tv_payWaitDoEarlyWarning;//汇缴-汇缴预警-待处理
    private TextView lookMore_payEarlyWarning;//汇缴-汇缴预警-查看更多
    private TextView day_payEarlyWarningNum;//汇缴-汇缴预警-今日
    private TextView moth_payEarlyWarningNum;//汇缴-汇缴预警-今日
    private TextView year_payEarlyWarningNum;//汇缴-汇缴预警-今日

    private final int WAITDO_WARNING = 30;//返回主界面是刷新首页财务总览数据标识
    private LinearLayout ll_early_warning;
    private MySwipeRefreshLayout myswipeRefresh;
    private String institutionTag;//用来记录是哪个地方的归属机构

    public AdminSubscriptionManagementF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.admin_view_subscription_management,null);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        v = view.findViewById(R.id.v);
        tv_admin_ownership_institution = (TextView) view.findViewById(R.id.tv_admin_ownership_institution);    //管理员归属机构
        tv_admin_ownership_institution2 = (TextView) view.findViewById(R.id.tv_admin_ownership_institution2);    //管理员归属机构
        tv_importedPaysysRecord = (TextView) view.findViewById(R.id.tv_importedPaysysRecord); //汇缴记录已导入条数
        tv_importedSumMoney = (TextView)view.findViewById(R.id.tv_importedSumMoney);   //已导入总金额
        tv_noFinishPaysysRecord = (TextView)view.findViewById(R.id.tv_noFinishPaysysRecord);  //未完成 汇缴记录条数
        tv_noFinishSumMoney = (TextView)view.findViewById(R.id.tv_noFinishSumMoney);  //未完成 总金额
        tv_admin_amount_paid = (TextView)view.findViewById(R.id.tv_admin_amount_paid);  //累计汇缴金额
        tv_admin_consolidated_record = (TextView)view.findViewById(R.id.tv_admin_consolidated_record);  //累计汇缴记录
        tv_admin_total_amount_paid = (TextView)view.findViewById(R.id.tv_admin_total_amount_paid);       //汇缴总金额
        tv_admin_average = (TextView)view.findViewById(R.id.tv_admin_average);   //平均值
        tv_admin_week = (TextView)view.findViewById(R.id.tv_admin_week);  //本周
        tv_admin_month = (TextView)view.findViewById(R.id.tv_admin_month);   //本月
        tv_admin_year = (TextView)view.findViewById(R.id.tv_admin_year);//本年
        tv_admin_week2 = (TextView)view.findViewById(R.id.tv_admin_week2);      //代售点业绩排行本周
        tv_admin_month2 = (TextView)view.findViewById(R.id.tv_admin_month2);  //代售点业绩排行本月
        tv_admin_year2 = (TextView)view.findViewById(R.id.tv_admin_year2); //代售点业绩排行本年
        lcAdminLineChart = (MyLineChart) view.findViewById(R.id.lc_admin_lineChart);   //折线图
        admin_lv_ranking = (MyListView) view.findViewById(R.id.admin_lv_ranking);  //代售点业绩排行listview
        tv_linechart_explain = (TextView) view.findViewById(R.id.tv_linechart_explain); //折线图底下文字描述
        ll_imported = (LinearLayout) view.findViewById(R.id.ll_imported2); //已导入（已完成）汇缴
        ll_noFinish = (LinearLayout) view.findViewById(R.id.ll_noFinish2);        //未完成 汇缴

        ll_payWaitDoEarlyWarning = (LinearLayout) view.findViewById(R.id.ll_payWaitDoEarlyWarning);  //汇缴-汇缴预警-待处理
        tv_payWaitDoEarlyWarning = (TextView) view.findViewById(R.id.tv_payWaitDoEarlyWarning);//汇缴-汇缴预警-待处理
        lookMore_payEarlyWarning = (TextView) view.findViewById(R.id.lookMore_payEarlyWarning);//汇缴-汇缴预警-查看更多
        day_payEarlyWarningNum = (TextView) view.findViewById(R.id.day_payEarlyWarningNum);//汇缴-汇缴预警-今日
        moth_payEarlyWarningNum = (TextView) view.findViewById(R.id.moth_payEarlyWarningNum);//汇缴-汇缴预警-本月
        year_payEarlyWarningNum = (TextView) view.findViewById(R.id.year_payEarlyWarningNum);//汇缴-汇缴预警-本年
        myswipeRefresh = (MySwipeRefreshLayout) view.findViewById(R.id.myswipeRefresh);//刷新控件
        ll_early_warning = (LinearLayout) view.findViewById(R.id.ll_early_warning);  //汇缴预警模块容器
        lcAdminLineChart.setFocusable(false);
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();
    }



    private void initWidget() {
        tv_admin_week.setOnClickListener(this);
        tv_admin_month.setOnClickListener(this);
        tv_admin_year.setOnClickListener(this);
        tv_admin_week2.setOnClickListener(this);
        tv_admin_month2.setOnClickListener(this);
        tv_admin_year2.setOnClickListener(this);
        tv_admin_ownership_institution.setOnClickListener(this);
        tv_admin_ownership_institution2.setOnClickListener(this);
        ll_imported.setOnClickListener(this);
        ll_noFinish.setOnClickListener(this);

        //汇缴预警的
        ll_payWaitDoEarlyWarning.setOnClickListener(this);
        lookMore_payEarlyWarning.setOnClickListener(this);

        setPremissionShowHideView(Premission.FB_ALARM_VIEW,ll_early_warning); //汇缴预警 权限


        myswipeRefresh.setColorScheme(colors);
        myswipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myswipeRefresh.isRefreshing()) {
                    switchBackgroud(tv_admin_week2,false); //状态选择器
                    switchBackgroud(tv_admin_week,true);
                    getFinTodayPaysys("",false,false);  //今日汇缴
                }
            }
        });
    }






    private void initData() {
        getFinTodayPaysys("",false,false);  //今日汇缴
        switchBackgroud(tv_admin_week2,false);
        switchBackgroud(tv_admin_week,true);
    }

    /**
     * 从后台获取财务首页的今日汇缴数据
     * @param  officeId  归属机构id
     * @param  type   false 是汇缴界面
     * @param requestMode  true为不连续加载   false 为连续多个请求 主要是为了关闭加载框
     */
    private void getFinTodayPaysys(final String officeId, final boolean type, final boolean requestMode){
        cpd.show();
        JSONObject finTodayPayObj = new JSONObject();
        try {
            if(!type) {
                finTodayPayObj.put("officeId", officeId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL+"acct/todayPayment";
        final JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, finTodayPayObj, new VolleyAbstract(baseActivity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                requestSuccessIsClosed="1";
                if(!requestMode){
                    setRequestPaymentWarnCount(false);// 预警
                }

                Log.d("todayPaymentData",json.toString());
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    int todayCount = jsonObject.optInt("todayCount");
                    String todayCountStr = String.valueOf(todayCount);
                    String todaySumStr = jsonObject.optString("todaySum");
                    int unCompleteCount = jsonObject.optInt("unCompleteCount");
                    String unCompleteCountStr = String.valueOf(unCompleteCount);
                    String unCompleteSumStr = jsonObject.optString("unCompleteSum");

                        tv_importedPaysysRecord.setText(todayCountStr == null ? "" : todayCountStr);
                        ;//汇缴记录已导入条数
                        tv_importedSumMoney.setText(todaySumStr == null ? "" : todaySumStr);
                        ;//已导入总金额
                        tv_noFinishPaysysRecord.setText(unCompleteCountStr == null ? "" : unCompleteCountStr); //未完成 汇缴记录条数
                        tv_noFinishSumMoney.setText(unCompleteSumStr == null ? "" : unCompleteSumStr);        ; //未完成 总金额

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            @Override
            protected void PdDismiss() {
                if(requestSuccessIsClosed.equals("0")) {
                    if(!requestMode){
                        setRequestPaymentWarnCount(false);// 预警   上一个网络请求失败接着下一个网络请求
                    }
                    cpd.dismiss();
                }
                requestSuccessIsClosed="0";
                if(requestMode){   //单个调用请求 可以关闭加载框
                    cpd.dismiss();
                }
            }

            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }


        }, "finTodayPayment", true);

        volleyNetCommon.addQueue(jsonObjectRequest);

    }



    /**
     *  请求折线图数据
     */
    private  void  setRequestlineChartData(boolean off){
        String url = Define.URL + "fb/getTotalList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId", officeIdStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url,jsonObject,"getTotalList","1",off, false); //1没有实际意义值

    }

    /**
     *  请求汇缴预警首页数据
     *  @param off  控制关闭加载框
     */
    private  void  setRequestPaymentWarnCount(boolean off){
        String url = Define.URL + "fb/paymentWarnCount";
        JSONObject jsonObject = new JSONObject();
        requestNet(url,jsonObject,"paymentWarnCount","1",off, true); //1没有实际意义值


    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber,controlOff);
        switch (tag){
            case "paymentWarnCount"://火车站首页汇缴预警（包括汇缴模块的汇缴预警）
                     paymentWarnData(json);
                if(!controlOff){
                    setRequestlineChartData(false);//折线图
                }
                break;

            case "getTotalList":   // 系统管理员 汇缴页面 获取折线图数据
                sqlitLineChartData(json);
                String rankingUrl = Define.URL+"fb/getTop10";
                if(!controlOff) {
                    requestNet(rankingUrl, new JSONObject(), "getTop10", "1", true, false); //1没有实际意义 获取代售点业绩排行数据
                }
                break;
            case "getTop10": //系统管理员 汇缴页面

                getRankingData(json);
                break;

        }
    }

    /**
     * 预警数据
     * @param json
     */
    private void paymentWarnData(JSONObject json) {
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            String waitDo_earlyWarning = jsonObject.optString("waitHandle");
            if (waitDo_earlyWarning != null && !"0".equals(waitDo_earlyWarning)){
//                tv_payWaitDoEarlyWarning.setText(waitDo_earlyWarning + "条待处理");
                tv_payWaitDoEarlyWarning.setText(Html.fromHtml( "<font color='#4D90E7'>" +waitDo_earlyWarning  + "</font>" + "条待处理"));
            }else {
                ll_payWaitDoEarlyWarning.setVisibility(View.GONE);
            }
            String dayWarningNum = jsonObject.optString("day");
            if (dayWarningNum != null){
                day_payEarlyWarningNum.setText(dayWarningNum);
            }else {
                day_payEarlyWarningNum.setText("0");
            }
            String mothWarningNum = jsonObject.optString("month");
            if (mothWarningNum != null){
                moth_payEarlyWarningNum.setText(mothWarningNum);
            }else {
                moth_payEarlyWarningNum.setText("0");
            }
            String yearWarningNum = jsonObject.optString("year");
            if (yearWarningNum != null){
                year_payEarlyWarningNum.setText(yearWarningNum);
            }else {
                year_payEarlyWarningNum.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void closeDismiss(CustomProgressDialog cpd, String tag,boolean controlOff) {
        super.closeDismiss(cpd, tag,controlOff);
        switch (tag){
            case "getTop10":
                 cpd.dismiss();  //最后一个直接关闭
                stopRefreshing();
                break;
        }
    }

    /**
     * 网络错误加载下一个请求
     * @param cpd
     * @param tag
     * @param controlOff
     */
    @Override
    protected void loadControlExceptionDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.loadControlExceptionDismiss(cpd, tag, controlOff);
        switch (tag){
            case "paymentWarnCount":
                 if(!controlOff){
                     setRequestlineChartData(false);//折线图
                 }
                break;
            case "getTotalList":
                if(!controlOff) {
                    String rankingUrl = Define.URL+"fb/getTop10";
                    requestNet(rankingUrl, new JSONObject(), "getTop10", "1", true, false); //1没有实际意义 获取代售点业绩排行数据
                }
                break;
            case "getTop10":
                break;
        }
    }

    private	 List<List<LineChartDataBean.DataBean.WeekBean>> weekLine =new ArrayList<>(); //周集合
    private  List<String> weekDateList = new ArrayList<>(); //周的天数集合
    private  List<Double> weekValue = new ArrayList<>(); //周的天数对应的值
    private List<List<LineChartDataBean.DataBean.MonthBean>> monthLine = new ArrayList<>(); //月集合
    private List<String> monthDateList = new ArrayList<>(); //月的天数集合
    private List<Double> monthValueList = new ArrayList<>(); //月对应的值
    private List<List<LineChartDataBean.DataBean.YearBean>> yearLine = new ArrayList<>(); // 年集合
    private List<String> yearDataList = new ArrayList<>(); //年对应的天数集合
    private List<Double> yearValueList = new ArrayList<>();// 年对应的值得集合
    private 	String weekShouldAmountAvg = "";
    private 	String weekShouldAmountTotal ="";
    private 	String monthShouldAmountAvg = "";
    private 	String monthShouldAmountTotal ="";
    private 	String yearShouldAmountAvg = "";
    private 	String yearShouldAmountTotal ="";
    private String monthFormat=""; //折线图标签下面的文字描述需要的日期
    private	String yearFormat=""; //折线图标签下面的文字描述需要的日期

    /**
     * 拆分获取的数据
     * @param jsonObject
     */
    private void sqlitLineChartData(JSONObject jsonObject){
        weekLine.clear(); //周集合
        weekDateList.clear();   //周的天数集合
        weekValue.clear();  //周的天数对应的值
        monthLine.clear(); //月集合
        monthDateList.clear();  //月的天数集合
        monthValueList.clear();  //月对应的值
        yearLine.clear(); // 年集合
        yearDataList.clear();  //年对应的天数集合
        yearValueList.clear();// 年对应的值得集合
/*
		        weekDateList.add(0,"0");
				weekValue.add(0,0f);
	         	monthDateList.add(0,"0");
		        monthValueList.add(0,0f);
		        yearDataList.add(0,"0");
		        yearValueList.add(0,0f);*/
        //获取当前系统时间
        String	 initDate;
        if (DateUtils.nowTime != null) {
            initDate = DateUtils.nowTime; //网络
        } else {
            initDate = DateUtils.getTime(); //系统
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy年MM月dd");
        Date date = Date.valueOf(initDate);
        String format = fmt.format(date);

        monthFormat = format.substring(0, format.indexOf("月"));
        yearFormat = format.substring(0, format.indexOf("年"));
        tv_linechart_explain.setText(monthFormat.isEmpty()?"---":monthFormat+"月本周累计汇缴数据表");
        Gson gson = new Gson();
        LineChartDataBean lineChartDataBean = gson.fromJson(jsonObject.toString(), LineChartDataBean.class);
        List<LineChartDataBean.DataBean> dataBean = lineChartDataBean.getData();

        for (int i = 0; i < dataBean.size(); i++) {
            LineChartDataBean.DataBean getAttributeBean = dataBean.get(i);

            if(getAttributeBean.getWeek()!=null){
                weekLine = getAttributeBean.getWeek();  //周
            }

            if(getAttributeBean.getMonth() !=null){
                monthLine = getAttributeBean.getMonth(); //月
            }
            if(getAttributeBean.getYear() != null){
                yearLine = getAttributeBean.getYear(); //年
            }



            if(getAttributeBean.getAmount()!=-1){
                tv_admin_amount_paid.setText(formatDigits(getAttributeBean.getAmount()));
            }
            if(getAttributeBean.getCount() !=-1){
                tv_admin_consolidated_record	.setText(getAttributeBean.getCount()+"");
            }
        }


        if(weekLine.size()>0) {  //周数据
            for (int i = 0; i < weekLine.size(); i++) {
                List<LineChartDataBean.DataBean.WeekBean> weekBeen = weekLine.get(i);

                if(weekBeen.get(0).getDate() !=null) {

                    weekDateList.add(i, weekBeen.get(0).getDate());
                    weekValue.add(i,  weekBeen.get(0).getValue());
                }

                if(weekBeen.get(0).getShouldAmountAvg()!=-1){
                    tv_admin_average.setText(formatDigits(weekBeen.get(0).getShouldAmountAvg())+""); //汇缴平均值
                    weekShouldAmountAvg = formatDigits(weekBeen.get(0).getShouldAmountAvg())+"";
                }
                if(weekBeen.get(0).getShouldAmountTotal() !=-1){
                    tv_admin_total_amount_paid.setText(formatDigits(weekBeen.get(0).getShouldAmountTotal())+""); //汇缴总金额
                    weekShouldAmountTotal = formatDigits(weekBeen.get(0).getShouldAmountTotal())+"";
                }
            }

        }

        if(monthLine.size()>0){ //月数据
            for (int i = 0; i < monthLine.size(); i++) {
                List<LineChartDataBean.DataBean.MonthBean> monthBeen = monthLine.get(i);
                if(monthBeen.get(0).getDate() !=null) {

                    monthDateList.add(i, monthBeen.get(0).getDate());
                    monthValueList.add(i, monthBeen.get(0).getValue());
                }
                if(monthBeen.get(0).getShouldAmountAvg()!=-1){
                    monthShouldAmountAvg = formatDigits(monthBeen.get(0).getShouldAmountAvg())+""; //汇缴平均值
                }
                if(monthBeen.get(0).getShouldAmountTotal() !=-1){
                    monthShouldAmountTotal= formatDigits(monthBeen.get(0).getShouldAmountTotal())+""; //汇缴总金额
                }
            }

        }

        if(yearLine.size()>0){ //年数据
            for (int i = 0; i < yearLine.size(); i++) {
                List<LineChartDataBean.DataBean.YearBean> yearBeen = yearLine.get(i);
                if (yearBeen.get(0).getDate() != null) {
                    yearDataList.add(i, yearBeen.get(0).getDate());
                    yearValueList.add(i, yearBeen.get(0).getValue());
                }

                if(yearBeen.get(0).getShouldAmountAvg()!=-1){
                    yearShouldAmountAvg = formatDigits(yearBeen.get(0).getShouldAmountAvg())+""; //汇缴平均值
                }
                if(yearBeen.get(0).getShouldAmountTotal() !=-1){
                    yearShouldAmountTotal = formatDigits(yearBeen.get(0).getShouldAmountTotal())+""; //汇缴总金额
                }
            }


        }

        setLineChartDataTo(weekDateList,weekValue,"1");  //默认进来的是周

    }


    SimpleDateFormat fmt2 = new SimpleDateFormat("MM/dd");
    Date date2 ;
    //设置数据到折线图
    /**
     *
     * @param dateListName x轴上的数据
     * @param valueList  折线图y轴上的数据
     * @param  tag //标记 是否年月日进来    日月是1 年是2
     */
    private void setLineChartDataTo(final List<String> dateListName, final List<Double> valueList, final String tag) {
        lcAdminLineChart.clear();
       // lcAdminLineChart.requestLayout();
        //刷新
        lcAdminLineChart.invalidate();
        if(dateListName.size()<=0 || valueList.size()<=0){
            return;
        }
        //设置手势滑动事件
        lcAdminLineChart.setOnChartGestureListener(new MyOnChartGestureListener()
        {
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
                    lcAdminLineChart.highlightValues(null);
            }
        });
        //设置数值选择监听
        lcAdminLineChart.setOnChartValueSelectedListener(this);
        //后台绘制
        lcAdminLineChart.setDrawGridBackground(false);
        //设置描述文本
        lcAdminLineChart.getDescription().setEnabled(false);
        //设置支持触控手势
        lcAdminLineChart.setTouchEnabled(true);
        //设置缩放
        lcAdminLineChart.setDragEnabled(true);
        //设置推动
        lcAdminLineChart.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        lcAdminLineChart.setPinchZoom(true);
        //隐藏X轴竖网格线
        lcAdminLineChart.getXAxis().setDrawGridLines(false);
       // lcAdminLineChart.resetViewPortOffsets();
        lcAdminLineChart.setExtraOffsets(5,0,40,0); //设置偏移量
        lcAdminLineChart.setNoDataText("数据空了");

        //x轴
        LimitLine llXAxis = new LimitLine(10f, "标记");
        //设置线宽
        llXAxis.setLineWidth(4f);
        //
        llXAxis.enableDashedLine(10f, 10f, 0f);
        //设置
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = lcAdminLineChart.getXAxis();

        //重置所有限制线,以避免重叠线
        xAxis.removeAllLimitLines();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(3);    //强制有多少个刻度
        //设置x轴的最小值
        xAxis.setAxisMinimum(0f);
        //具体实现就是这行代码
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                try {
                    if(tag.equals("1")) {
                        date2 = Date.valueOf(dateListName.get((int) value));
                        return String.valueOf(fmt2.format(date2));
                    }else if(tag.equals("2")){
                        return String.valueOf(dateListName.get((int)value));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                return value+"";
            }


            public int getDecimalDigits() {
                return 0;
            }
        });




        //y轴左边
        YAxis leftAxis = lcAdminLineChart.getAxisLeft();
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();
        //y轴最大
        //  leftAxis.setAxisMaximum(200f);
        //y轴最小
        leftAxis.setAxisMinimum(0f);  //设置了这个在x轴就就不会和网格线重叠了
        leftAxis.enableGridDashedLine(3f, 1f, 0f);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setGridColor(getResources().getColor(R.color.green_chart)); //网格线颜色
        // 限制数据(而不是背后的线条勾勒出了上面)
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {


            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "" + formatDigits(value);
            }
        });

        lcAdminLineChart.getAxisRight().setEnabled(false);


        final ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < valueList.size(); i++) {

            values.add(new Entry(i,Float.parseFloat( formatDigits(valueList.get(i))) ));
        }

        //设置数据
        setData(values);

        //默认动画
        lcAdminLineChart.animateX(2500);
        //刷新
        //mChart.invalidate();
        // 得到这个文字
        Legend l = lcAdminLineChart.getLegend();
        l.setEnabled(false); //不显示标签
        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);

        final NewMarkerView markerView = new NewMarkerView(MyApplication.mContext, R.layout.ll_text);
        markerView.setChartView(lcAdminLineChart);
        markerView.setCallBack(new NewMarkerView.CallBack() {
            @Override
            public void onCallBack(float x, String value) {
                int index = (int) x;
                if (index >= values.size()) {
                    return;
                }

                if(!TextUtils.isEmpty(value)) {
                    markerView.getTvContent().setVisibility(View.VISIBLE);
          /*          float yValues = values.get(index).getY();
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    String yValuesStr = decimalFormat.format(yValues);//format 返回的是字符串*/

                    String   yValuesStr =  NumberFormatUtils.formatDigits(valueList.get(index));
//					markerView.getTvContent().setText(dateListName.get(index)+ "\n"+values.get(index).getY()+"元");
                    markerView.getTvContent().setText(dateListName.get(index)+ "\n"+ yValuesStr +"元");
                }
            }
        });

        lcAdminLineChart.setMarkerView(markerView);
    }
    private LineDataSet set1;
    //传递数据集
    private void setData(ArrayList<Entry> values) {
        if (lcAdminLineChart.getData() != null && lcAdminLineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lcAdminLineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lcAdminLineChart.getData().notifyDataChanged();
            lcAdminLineChart.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
            set1 = new LineDataSet(values, "---");
            // 在这里设置线
            set1.enableDashedLine(0f, 0f, 0f);  //绘制折线虚线的间距

            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(getResources().getColor(R.color.green_chart));
            set1.setCircleColor(getResources().getColor(R.color.alpha_bule_chart));
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(true);  //设置是否空心圆
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setValueFormatter(new IValueFormatter() {

                @Override
                public String getFormattedValue(float arg0, Entry arg1, int arg2,
                                                ViewPortHandler arg3) {
                    return ""+arg0;//不显示值
                }
            });

            set1.setFillColor(getResources().getColor(R.color.alpha_green_chart));

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            //添加数据集
            dataSets.add(set1);

            //创建一个数据集的数据对象
            LineData data = new LineData(dataSets);

            //谁知数据
            lcAdminLineChart.setData(data);
            //获取到当前值 并且不显示顶点值
            List<ILineDataSet> sets = lcAdminLineChart.getData().getDataSets();
            for (ILineDataSet iSet : sets) {
                LineDataSet set = (LineDataSet) iSet;
                //切换显示/隐藏
                set.setDrawValues(!set.isDrawValuesEnabled());
            }
            //刷新
            lcAdminLineChart.invalidate();
        }
    }


    List<AdminRankingBean.DataBean.MonthBean> monthBeanList;
    List<AdminRankingBean.DataBean.WeekBean> weekBeanList ;
    List<AdminRankingBean.DataBean.YearBean> yearBeanList ;
    //获取排行数据
    public void getRankingData(JSONObject json){
        if(monthBeanList !=null){
            monthBeanList.clear();
        }
        if(weekBeanList !=null){
            weekBeanList.clear();
        }
        if(yearBeanList !=null){
            yearBeanList.clear();
        }
        json.toString();
        Gson gson = new Gson();
        AdminRankingBean adminRankingBean = gson.fromJson(json.toString(), AdminRankingBean.class);
        AdminRankingBean.DataBean data = adminRankingBean.getData();
        monthBeanList = data.getMonth();
        weekBeanList = data.getWeek();
        yearBeanList = data.getYear();
        setAdapter("1");
    }



    /**
     *  	//代售点排名数据适配器
     * @param type  1 代表 周  2 代表月 3 代表年
     */
    private void setAdapter(String type){
        List<ConversionRankingBean> listConversionRanking = new ArrayList<>();
        listConversionRanking.clear();
        if(type.equals("1")){
            if(weekBeanList !=null && weekBeanList.size()>0){
                for (int i = 0; i < weekBeanList.size(); i++) {
                    if (i < 10) {
                        AdminRankingBean.DataBean.WeekBean weekBean = weekBeanList.get(i);
                        listConversionRanking.add(new ConversionRankingBean(weekBean.getAgencyName(), weekBean.getOfficeCode(), weekBean.getShouldAmountString(), i));
                    }
                }
            }
        }
        if(type.equals("2")){
            if(monthBeanList !=null && monthBeanList.size()>0){
                for (int i = 0; i < monthBeanList.size(); i++) {
                    if (i < 10) {
                        AdminRankingBean.DataBean.MonthBean monthBean = monthBeanList.get(i);
                        listConversionRanking.add(new ConversionRankingBean(monthBean.getAgencyName(), monthBean.getOfficeCode(), monthBean.getShouldAmountString(), i));
                    }
                }
            }
        }
        if(type.equals("3")){
            if(yearBeanList !=null && yearBeanList.size()>0){
                for (int i = 0; i < yearBeanList.size(); i++) {
                    if (i < 10) {
                        AdminRankingBean.DataBean.YearBean yearBean = yearBeanList.get(i);
                        listConversionRanking.add(new ConversionRankingBean(yearBean.getAgencyName(), yearBean.getOfficeCode(), yearBean.getShouldAmountString(), i));
                    }
                }
            }
        }
        setTopRankingAdapter(listConversionRanking,admin_lv_ranking);

    }



    //设置代售点交易排行适配器
    public void	setTopRankingAdapter(final List<ConversionRankingBean> listConversionRanking, MyListView myListView){
        CommonAdapter<ConversionRankingBean> commonAdapter = new CommonAdapter<ConversionRankingBean>(MyApplication.mContext,listConversionRanking,R.layout.item_admin_subscription_management_ranking) {
            @Override
            public void convert(ViewHolder holder, ConversionRankingBean o) {
                ImageView iv_head = holder.getView(R.id.iv_head);//排名图片
                TextView tv_ranking =	holder.getView(R.id.tv_ranking);//排名字母
                TextView tv_debit_account =	holder.getView(R.id.tv_debit_account);//扣款账号
                TextView tv_name =	holder.getView(R.id.tv_name);//代售点名称
                TextView tv_amount =	holder.getView(R.id.tv_amount);//票款金额
                iv_head.setTag(o.number);
                if(iv_head.getTag().equals(o.number)) {
                    iv_head.setVisibility(View.VISIBLE);
                    tv_ranking.setVisibility(View.GONE);
                    switch (o.number){
                        case 0:
                            iv_head.setImageDrawable(getResources().getDrawable(R.drawable.crown));
                            break;
                        case 1:
                            iv_head.setImageDrawable(getResources().getDrawable(R.drawable.crown_second));
                            break;
                        case 2:
                            iv_head.setImageDrawable(getResources().getDrawable(R.drawable.crown_third));
                            break;
                    }
                }
                tv_debit_account.setText(o.officeCode);

                tv_name.setText(o.agencyName);
                tv_amount.setText(o.shouldAmountString);
                if(o.number>2){
                    iv_head.setVisibility(View.GONE);
                    tv_ranking.setVisibility(View.VISIBLE);
                    tv_ranking.setText((o.number+1)+"");
                }

            }
        };
        myListView.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ConversionRankingBean conversionRankingBean = listConversionRanking.get(i);
                String officeCode = conversionRankingBean.getOfficeCode();
                intent = new Intent(MyApplication.mContext, StationOutletsDetailsA.class);
                intent.putExtra("code", officeCode);
                startActivityIntent(intent);
            }
        });
    }


    /**
     * 切换系统管理员 背景颜色
     * @param paramView
     * @param paramBoolean
     */
    private void switchBackgroud(View paramView, boolean paramBoolean){
        if (paramBoolean)
        {
            this.tv_admin_week.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style3));
            this.tv_admin_month.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style3));
            this.tv_admin_year.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style3));
        }else{
            this.tv_admin_week2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style3));
            this.tv_admin_month2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style3));
            this.tv_admin_year2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style3));
        }

        if (paramView != null)
            paramView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style));


    }
   MyPopWindow myPopWindow;
    MapData  mapData;
    private float xs;
    private  float ys;
    private String	officeIdStr = "";
    private String officeNodeName ;
    /**
     * 获取归属机构数据 弹出归属机构
     */
    private void showOwnershipPop(final TextView tvInstitution) {
        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
//得到状态栏高度
//返回键关闭
        if(myPopWindow!=null){
            myPopWindow.dissmiss();
        }
        mapData = new MapData(baseActivity, cpd, false) {
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }

                myPopWindow = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        ListView lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                        mapData.setTrainOwnershipData(lv_audit_status);
                        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                        int[] arr = new int[2];
                        tvInstitution.getLocationOnScreen(arr);
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        baseActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                        xs = arr[0] + tvInstitution.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + tvInstitution.getHeight();
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node officeId) {
                                officeIdStr = String.valueOf(officeId.getAllTreeNode().id); // 归属机构筛选id
                                officeNodeName = officeId.getAllTreeNode().name;
                                if ("todayPayment".equals(institutionTag)){
                                    getFinTodayPaysys(officeIdStr,false,true);
                                }else if ("getTotalList".equals(institutionTag)){
                                    setRequestlineChartData(true);
                                    switchBackgroud(tv_admin_week,true);
                                }

                                tvInstitution.setText(officeNodeName);

                                myPopWindow.dissmiss();

                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop3;
                    }
                }.popupWindowBuilder(baseActivity).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                myPopWindow.showAtLocation(tvInstitution, Gravity.NO_GRAVITY, (int) xs, (int) ys);
            }
        };
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_admin_week:    //周
                setLineChartDataTo(weekDateList,weekValue,"1");
                switchBackgroud(tv_admin_week,true);
                tv_admin_average.setText(weekShouldAmountAvg); //周的汇缴平均值
                tv_admin_total_amount_paid.setText(weekShouldAmountTotal);  //周的汇缴总金额
                tv_linechart_explain.setText(monthFormat.isEmpty()?"---":monthFormat+"月本周累计汇缴数据表");
                break;
            case R.id.tv_admin_month: //月
                setLineChartDataTo(monthDateList,monthValueList,"1");
                switchBackgroud(tv_admin_month,true);
                tv_admin_average.setText(monthShouldAmountAvg); //月的汇缴平均值
                tv_admin_total_amount_paid.setText(monthShouldAmountTotal);  //月的汇缴总金额
                tv_linechart_explain.setText(monthFormat.isEmpty()?"---":monthFormat+"月本月累计汇缴数据表");
                break;
            case R.id.tv_admin_year:  //年
                setLineChartDataTo(yearDataList,yearValueList,"2");
                switchBackgroud(tv_admin_year,true);
                tv_admin_average.setText(yearShouldAmountAvg); //月的汇缴平均值
                tv_admin_total_amount_paid.setText(yearShouldAmountTotal);  //月的汇缴总金额
                tv_linechart_explain.setText(yearFormat.isEmpty()?"---":yearFormat+"年本年累计汇缴数据表");
                break;
            case R.id.tv_admin_week2:  //代售点业绩排行 周
                setAdapter("1");
                switchBackgroud(tv_admin_week2,false);
                break;
            case R.id.tv_admin_month2://代售点业绩排行 月
                setAdapter("2");
                switchBackgroud(tv_admin_month2,false);
                break;
            case R.id.tv_admin_year2://代售点业绩排行 年
                setAdapter("3");
                switchBackgroud(tv_admin_year2,false);
                break;
            case R.id.tv_admin_ownership_institution://管理员汇缴界面归属机构
                institutionTag = "todayPayment";
                if(RepeatClickUtils.repeatClick()) {
                    showOwnershipPop(tv_admin_ownership_institution);
                }
                break;
            case R.id.tv_admin_ownership_institution2://管理员汇缴界面归属机构
                institutionTag = "getTotalList";
                if(RepeatClickUtils.repeatClick()) {
                    showOwnershipPop(tv_admin_ownership_institution2);
                }
                break;

            case R.id.ll_imported2:  //系统管理员 汇缴页面 已导入(已完成)
                baseActivity.startActivityMethod(AdminConsolidatedRecord.class);
                break;

            case R.id.ll_noFinish2: //系统管理员 汇缴页面 未完成汇缴
                intent = new Intent(MyApplication.mContext,AdminConsolidatedRecord.class);
                intent.putExtra("paidUpState","paidUpState");
                baseActivity.startActivityIntent(intent);
                break;

            case R.id.ll_payWaitDoEarlyWarning://火车站-汇缴-汇缴预警-待处理预警
//                intent = new Intent(MyApplication.mContext, WarningInfoListA.class);
//                intent.putExtra("processingState",Define.WARN_WAITDO);
//                startActivityForResult(intent, WAITDO_WARNING);
                intent = new Intent(MyApplication.mContext, NewWarningInfoListA.class);
                intent.putExtra("processingState", "0");//未处理状态，传0
                startActivityForResult(intent, WAITDO_WARNING);
                break;
            case R.id.lookMore_payEarlyWarning://火车站-首页-汇缴预警-查看更多
//                intent = new Intent(MyApplication.mContext, WarningInfoListA.class);
//                startActivityForResult(intent, WAITDO_WARNING);
                intent = new Intent(MyApplication.mContext, NewWarningInfoListA.class);
                startActivityForResult(intent, WAITDO_WARNING);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case WAITDO_WARNING:
                setRequestPaymentWarnCount(true);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    //关闭刷新动画
    public void stopRefreshing(){
        //刷新完成关闭动画
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myswipeRefresh != null) {
                    myswipeRefresh.setRefreshing(false);
                }
            }
        });
    }

}

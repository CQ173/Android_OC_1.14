package com.huoniao.oc.fragment.financial;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.AdminConsolidatedRecord;
import com.huoniao.oc.admin.AdminOJiTransactionDetails;
import com.huoniao.oc.admin.AdminUserManageA;
import com.huoniao.oc.bean.FinancialOverview;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.HomepageCommon;
import com.huoniao.oc.custom.MySwipeRefreshLayout;
import com.huoniao.oc.financial.FinancialListA;
import com.huoniao.oc.financial.ImportRecordA;
import com.huoniao.oc.financial.LaunchApplyA;
import com.huoniao.oc.financial.NewAdvanceInCashA;
import com.huoniao.oc.financial.StationPaymentA;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.trainstation.ReturnLateFeeA;
import com.huoniao.oc.user.IncomeStatisticsA;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SmartImageView;
import com.huoniao.oc.util.TextSwitcherView;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinancialHomepageF extends BaseFragment implements OnChartValueSelectedListener, View.OnClickListener {


    private TextSwitcherView tv_notifications;
    private LinearLayout ll_noticeBar;
    private MySwipeRefreshLayout financialRoleRefresh;
    private SmartImageView main_gallery;
    private LinearLayout main_lin;
    private LinearLayout layout_launchApply;
    private LinearLayout layout_financialDetails;
    private LinearLayout layout_userManage;
    private TextView tv_today;
    private TextView tv_toMoth;
    private RelativeLayout rl_withdrawals;
    private LinearLayout ll_waitWithdrawals;
    private TextView tv_wdCircle;
    private TextView tv_wdSingular;
    private TextView tv_wdMoney;
    private RelativeLayout rl_withhold;
    private LinearLayout ll_waitWithhold;
    private TextView tv_whCircle;
    private TextView tv_whSingular;
    private TextView tv_whMoney;
    private RelativeLayout rl_daiFu;
    private LinearLayout ll_waitDaiFu;
    private TextView tv_dfCircle;
    private TextView tv_dfSingular;
    private TextView tv_dfMoney;
    private RelativeLayout rl_depositary;
    private LinearLayout ll_waitDepositary;
    private TextView tv_dtCircle;
    private TextView tv_dtSingular;
    private TextView tv_dtMoney;
    private RelativeLayout rl_payment;
    private LinearLayout ll_waitPayment;
    private RelativeLayout rl_payment2;
    private LinearLayout ll_waitPayment2;
    private TextView tv_pmCircle;
    private TextView tv_pmSingular;
    private TextView tv_pmMoney;
    private TextView tv_stationNumber;
    private TextView tv_pmMoney2;
    private TextView tv_tradeDetailed;
    private TextView tv_income;
    private TextView tv_expenditure;
    private TextView tv_fnBalance;
    private BarChart tradeBarChart;
    private TextView tv_lookFinPaysys;
    private LinearLayout ll_imported;
    private TextView tv_importedPaysysRecord2;
    private TextView tv_importedSumMoney2;
    private LinearLayout ll_noFinish;
    private TextView tv_noFinishPaysysRecord2;
    private TextView tv_noFinishSumMoney2;
    private HomepageCommon homepageCommon;
    private String pmCircleStr, wdCircleStr, dtCircleStr, dfCircleStr, whCircleStr;
    private String finTextCickTag = "toDay";//记录今日和本月按钮点击
    private User user;
    private String roleName;//角色名
    private LinearLayout ll_tradeModule;//交易概况模块
    private Intent intent;
    private LinearLayout ll_pay_system;
    private LinearLayout ll_financial_overview;
    private LinearLayout ll_ticketImport, ll_importDetails;
    private TextView tv_ticketImportStation, tv_ticketNoImportStation;
    private TextView tv_outsIncomeCount;
    private TextView tv_returnLateFee;
    private LinearLayout ll_returnLateFeeArea;
    private TextView tv_stationPaymentInfo;

    public FinancialHomepageF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.financial_view_newhomepage,null);
        tv_notifications = (TextSwitcherView) view.findViewById(R.id.sysTemNotification);
        ll_noticeBar = (LinearLayout) view.findViewById(R.id.ll_noticeBar);
        financialRoleRefresh = (MySwipeRefreshLayout) view.findViewById(R.id.newhomepage_swipeRefresh);
        main_gallery = (SmartImageView) view.findViewById(R.id.main_gallery);
        main_gallery.setRatio(2.33f);
        main_lin = (LinearLayout) view.findViewById(R.id.main_lin);
        layout_launchApply = (LinearLayout) view.findViewById(R.id.layout_launchApply);
        layout_financialDetails = (LinearLayout) view.findViewById(R.id.layout_financialDetails);
        layout_userManage = (LinearLayout) view.findViewById(R.id.layout_userManage);
        ll_pay_system = (LinearLayout) view.findViewById(R.id.ll_pay_system); //今日汇缴容器
        tv_today = (TextView) view.findViewById(R.id.tv_today);  //首页-财务总览-今日
        tv_toMoth = (TextView) view.findViewById(R.id.tv_toMoth);  //首页-财务总览-本月
        rl_withdrawals = (RelativeLayout) view.findViewById(R.id.rl_withdrawals);//首页-财务总览-提现
        ll_waitWithdrawals = (LinearLayout) view.findViewById(R.id.ll_waitWithdrawals);  //首页-财务总览-提现待办数点击入口
        tv_wdCircle = (TextView) view.findViewById(R.id.tv_wdCircle);//首页-财务总览-提现待办数
        tv_wdCircle.setVisibility(View.GONE);
        tv_wdSingular = (TextView) view.findViewById(R.id.tv_wdSingular); //首页-财务总览-提现笔数
        tv_wdMoney = (TextView) view.findViewById(R.id.tv_wdMoney);  //首页-财务总览-提现总金额
        rl_withhold = (RelativeLayout) view.findViewById(R.id.rl_withhold); //首页-财务总览-代扣
        ll_waitWithhold = (LinearLayout) view.findViewById(R.id.ll_waitWithhold);  //首页-财务总览-代扣待办数点击入口
        tv_whCircle = (TextView) view.findViewById(R.id.tv_whCircle); //首页-财务总览-代扣待办数
        tv_whCircle.setVisibility(View.GONE);
        tv_whSingular = (TextView) view.findViewById(R.id.tv_whSingular); //首页-财务总览-代扣笔数
        tv_whMoney = (TextView) view.findViewById(R.id.tv_whMoney); //首页-财务总览-代扣总金额
        rl_daiFu = (RelativeLayout) view.findViewById(R.id.rl_daiFu);    //首页-财务总览-代付
        ll_waitDaiFu = (LinearLayout) view.findViewById(R.id.ll_waitDaiFu);       //首页-财务总览-代付待办数点击入口
        tv_dfCircle = (TextView) view.findViewById(R.id.tv_dfCircle);//首页-财务总览-代付待办数
        tv_dfCircle.setVisibility(View.GONE);
        tv_dfSingular = (TextView) view.findViewById(R.id.tv_dfSingular);  //首页-财务总览-代付笔数
        tv_dfMoney = (TextView) view.findViewById(R.id.tv_dfMoney); //首页-财务总览-代付总金额
        rl_depositary = (RelativeLayout) view.findViewById(R.id.rl_depositary);  //首页-财务总览-代存
        ll_waitDepositary = (LinearLayout) view.findViewById(R.id.ll_waitDepositary);  //首页-财务总览-代存待办数点击入口
        tv_dtCircle = (TextView) view.findViewById(R.id.tv_dtCircle); //首页-财务总览-待办数
        tv_dtCircle.setVisibility(View.GONE);
        tv_dtSingular = (TextView) view.findViewById(R.id.tv_dtSingular);  //首页-财务总览-代存笔数
        tv_dtMoney = (TextView) view.findViewById(R.id.tv_dtMoney);//首页-财务总览-代存总金额
        rl_payment = (RelativeLayout) view.findViewById(R.id.rl_payment);   //首页-财务总览-付款
        ll_waitPayment = (LinearLayout) view.findViewById(R.id.ll_waitPayment);  //首页-财务总览-付款待办数点击入口
        rl_payment2 = (RelativeLayout) view.findViewById(R.id.rl_payment2);   //首页-财务总览-付款
        ll_waitPayment2 = (LinearLayout) view.findViewById(R.id.ll_waitPayment2);  //首页-财务总览-汇缴待办数点击入口
        tv_pmCircle = (TextView) view.findViewById(R.id.tv_pmCircle); //首页-财务总览-付款待办数
        tv_pmCircle.setVisibility(View.GONE);
        tv_pmSingular = (TextView) view.findViewById(R.id.tv_pmSingular); //首页-财务总览-付款笔数
        tv_pmMoney = (TextView) view.findViewById(R.id.tv_pmMoney);//首页-财务总览-付款总金额
        tv_stationNumber = (TextView) view.findViewById(R.id.tv_stationNumber); //首页-财务总览-导入车站数
        tv_pmMoney2 = (TextView) view.findViewById(R.id.tv_pmMoney2); //首页-财务总览-付款总金额
        tv_tradeDetailed = (TextView) view.findViewById(R.id.tv_tradeDetailed);  //首页-交易概况-交易明细
        tv_income = (TextView) view.findViewById(R.id.tv_income);//首页-交易概况-收入
        tv_expenditure = (TextView) view.findViewById(R.id.tv_expenditure);  //首页-交易概况-支出
        tv_fnBalance = (TextView) view.findViewById(R.id.tv_fnBalance); //首页-交易概况-结余
        tradeBarChart = (BarChart) view.findViewById(R.id.tradeBarChart); //首页-交易概况-统计图

        tv_lookFinPaysys = (TextView) view.findViewById(R.id.tv_lookFinPaysys);  //首页-今日汇缴-查看
        ll_imported = (LinearLayout) view.findViewById(R.id.ll_imported);//首页-今日汇缴-已导入
        tv_importedPaysysRecord2 = (TextView) view.findViewById(R.id.tv_importedPaysysRecord);  //首页-今日汇缴-已导入汇缴记录
        tv_importedSumMoney2 = (TextView) view.findViewById(R.id.tv_importedSumMoney);    //首页-今日汇缴-已导入汇缴总金额
        ll_noFinish = (LinearLayout) view.findViewById(R.id.ll_noFinish); //首页-今日汇缴-未完成
        tv_noFinishPaysysRecord2 = (TextView) view.findViewById(R.id.tv_noFinishPaysysRecord); //首页-今日汇缴-未完成汇缴记录
        tv_noFinishSumMoney2 = (TextView) view.findViewById(R.id.tv_noFinishSumMoney); //首页-今日汇缴-未完成汇缴总金额
        ll_tradeModule = (LinearLayout) view.findViewById(R.id.ll_tradeModule);  //交易概况模块
       // setPremissionShowHideView(Premission.ACCT_TRADEFLOW_VIEW,ll_tradeModule); //是否有"交易流水"菜单权限
        ll_financial_overview = (LinearLayout) view.findViewById(R.id.ll_financial_overview);  //财务总览模块容器
        ll_ticketImport = (LinearLayout) view.findViewById(R.id.ll_ticketImport);  //今日票款导入模块
        ll_importDetails = (LinearLayout) view.findViewById(R.id.ll_importDetails);  //查看票款导入列表
        tv_ticketImportStation = (TextView) view.findViewById(R.id.tv_ticketImportStation);//导入票款车站数
        tv_ticketNoImportStation = (TextView) view.findViewById(R.id.tv_ticketNoImportStation);//未导入票款车站数
        tv_outsIncomeCount = (TextView) view.findViewById(R.id.tv_outsIncomeCount);//代售点收益统计
        tv_returnLateFee = (TextView) view.findViewById(R.id.tv_returnLateFee);//滞纳金返还
        ll_returnLateFeeArea = (LinearLayout) view.findViewById(R.id.ll_returnLateFeeArea); //滞纳金返还容器
        tv_stationPaymentInfo = (TextView) view.findViewById(R.id.tv_stationPaymentInfo);////财务-车站汇缴信息
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSp();
        initWidget();
        initData();
    }

    private void initSp() {
        user = (User) readObject(MyApplication.mContext, "loginResult");
        roleName = user.getRoleName();
    }


    private void initWidget() {
        initBarChart();
        financialRoleRefresh.setColorScheme(colors);
        financialRoleRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (financialRoleRefresh.isRefreshing()) {
                    try {
                        if(homepageCommon!=null) {
                            homepageCommon.getMainNotificationList(ll_noticeBar, tv_notifications, cpd); //获取系统通知
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        layout_launchApply.setOnClickListener(this);
        layout_financialDetails.setOnClickListener(this);
        layout_userManage.setOnClickListener(this);
        tv_today.setOnClickListener(this);
        tv_toMoth.setOnClickListener(this);
        rl_withdrawals.setOnClickListener(this);
        rl_withhold.setOnClickListener(this);
        rl_daiFu.setOnClickListener(this);
        rl_depositary.setOnClickListener(this);
        rl_payment.setOnClickListener(this);
        rl_payment2.setOnClickListener(this);
        ll_waitWithdrawals.setOnClickListener(this);
        ll_waitWithhold.setOnClickListener(this);
        ll_waitDaiFu.setOnClickListener(this);
        ll_waitDepositary.setOnClickListener(this);
        ll_waitPayment.setOnClickListener(this);
        ll_waitPayment2.setOnClickListener(this);
        tv_tradeDetailed.setOnClickListener(this);
        tv_lookFinPaysys.setOnClickListener(this);
        ll_imported.setOnClickListener(this);
        ll_noFinish.setOnClickListener(this);
        ll_importDetails.setOnClickListener(this);
        tv_outsIncomeCount.setOnClickListener(this);
        tv_returnLateFee.setOnClickListener(this);
        tv_stationPaymentInfo.setOnClickListener(this);
        setPremissionShowHideView(Premission.SYS_USER_VIEW,layout_userManage); //用户管理菜单权限
        setPremissionShowHideView(Premission.ACCT_FINANCEAPPLY_VIEW,layout_financialDetails); //财务明细 权限
        setPremissionShowHideView(Premission.ACCT_FINANCEAPPLY_VIEW,ll_financial_overview); //财务总览和财务明细权限一样 权限
        setPremissionShowHideView(Premission.FB_PAYMENT_VIEW,ll_pay_system); //今日汇缴权限
        setPremissionShowHideView(Premission.ACCT_FINANCEAPPLY_FORM,layout_launchApply); //财务申请
        setPremissionShowHideView(Premission.ACCT_TRADEFLOW_VIEW,ll_tradeModule); //交易概况    就是交易流水的权限
        setPremissionShowHideView(Premission.FB_PAYMENT_PAYMENTINFO,ll_ticketImport); //汇缴导入记录的权限
        setPremissionShowHideView(Premission.FB_TRAINPAYMENT_VIEW,tv_stationPaymentInfo); //车站汇缴信息
        setPremissionShowHideView(Premission.FB_LATEFEERETURN_VIEW, ll_returnLateFeeArea); //滞纳金返还容器

    }

    private void initBarChart() {
        tradeBarChart.setOnChartValueSelectedListener(this);
        tradeBarChart.setDrawBarShadow(false);
        tradeBarChart.setDrawValueAboveBar(true);
        tradeBarChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        tradeBarChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        tradeBarChart.setPinchZoom(false);
        tradeBarChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        List<String> xAxisValue = new ArrayList<String>();
        xAxisValue.add("今日收入（元）");
        xAxisValue.add("今日支出（元）");

        //x坐标轴设置
        XAxis xAxis = tradeBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setXOffset(0);//设置坐标在原点
        tradeBarChart.getAxisLeft().setAxisMinValue(0f);//设置坐标最小值
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(xAxisValue.size());
//			xAxis.setCenterAxisLabels(true);//设置标签居中
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValue));

        //y轴设置
//			YAxis leftAxis = tradeBarChart.getAxisLeft();
//			leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//			leftAxis.setDrawGridLines(false);
//			leftAxis.setDrawLabels(false);
//			leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = tradeBarChart.getAxisRight();//设置Y轴右边属性
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        Legend legend = tradeBarChart.getLegend();
        legend.setEnabled(false);//设置比例图标隐藏
    }

    private void initData() {
        homepageCommon = new HomepageCommon(baseActivity){
            @Override
            protected void mainNotificationCpdClose(CustomProgressDialog cpd, String state) {  //系统通知
                super.mainNotificationCpdClose(cpd, state);
                //根据业务去判断是否需要关闭加载框
                switch (state){
                    case "0":
                        requestFinancialOverview(false);
                        break;
                    case "1":
                        cpd.dismiss(); //走到这里说明网络异常了或者别的异常
                        financiaStopRefreshing();
                        requestFinancialOverview(false); //上个请求网络异常接着下个请求
                        break;
                }
            }
        };
        homepageCommon.transferControl(main_gallery,main_lin);
        homepageCommon.carouselFigure("1");
        homepageCommon.create();

        try {
            homepageCommon.getMainNotificationList(ll_noticeBar,tv_notifications,cpd); //获取系统通知
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 请求获取财务首页的财务总览数据
     * @param off  开关 请求结束是否关闭加载框
     */
    private void requestFinancialOverview(boolean off){
        String url = Define.URL+"acct/financeApplyIndexTypeCount";
        requestNet(url,new JSONObject(),"financeApplyIndexTypeCount","0",off, true);
    }

    /**
     * 请求财务首页的交易概况数据
     */
    private void requestTradeOverview(boolean off){
        String url = Define.URL+"acct/tradingOverview";
        requestNet(url,new JSONObject(),"tradingOverview","0",off, false);
    }

    /**
     * 请求财务首页的今日汇缴数据
     * @param off   开关控制加载框
     */
    private void requestFinTodayPaysys(boolean off){
        String url = Define.URL+"acct/todayPayment";
        requestNet(url,new JSONObject(),"todayPayment","0",off, true);
    }


    /**
     * 请求财务首页的汇缴导入数
     * @param off   开关控制加载框
     */
    private void requestPaymentCount(boolean off){
        String url = Define.URL+"fb/paymentCount";
        requestNet(url,new JSONObject(),"finPaymentCount","0",off, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag){
            case "financeApplyIndexTypeCount":
                getFinancialOverviewData(json,controlOff);
                break;
            case "tradingOverview":
                Log.d("tradingOverviewData", json.toString());
                getTradeOverview(json,controlOff);
                break;
            case "todayPayment":
                getFinTodayPaysys(json,controlOff);
                break;
            case "finPaymentCount":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String alreadyPayment = jsonObject.optString("alreadyPayment");
                    String unPayment = jsonObject.optString("unPayment");
                    if (alreadyPayment != null) {
                        tv_ticketImportStation.setText(alreadyPayment);
                    }
                    if (unPayment != null) {
                        tv_ticketNoImportStation.setText(unPayment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void closeDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.closeDismiss(cpd, tag, controlOff);
        switch (tag){
            case "financeApplyIndexTypeCount":
            case "tradingOverview":
            case "todayPayment":
            case "finPaymentCount":
                if(controlOff){
                    cpd.dismiss();
                    financiaStopRefreshing();
                }
                break;
        }

    }

    /**
     * 出现异常把下拉控件关闭
     */
    @Override
    protected void loadControlExceptionDismiss(CustomProgressDialog cpd,String tag,boolean controlOff) {
        super.loadControlExceptionDismiss(cpd,tag,controlOff);
        switch (tag){
            case "financeApplyIndexTypeCount":
                    if(!controlOff){    //网络出错 加载下一个请求
                        requestTradeOverview(false);
                    }
               break;
            case "tradingOverview":
                if(!controlOff){ //继续下一个请求
                    requestFinTodayPaysys(false);
                }
                break;
            case "todayPayment":
                if(!controlOff){ //继续下一个请求
                    requestPaymentCount(true);
                }
                break;
        }
        financiaStopRefreshing();
    }

    /**
     * 从后台获取财务首页的财务总览数据
     */
    private void getFinancialOverviewData(JSONObject json,boolean controlOff) {
        if(!controlOff){
            //加载下一个请求
            requestTradeOverview(false);
        }
        Gson gson = new Gson();
        FinancialOverview financialOverview = gson.fromJson(json.toString(), FinancialOverview.class);
        List<FinancialOverview.DataBean.DayArrBean> dayArr = financialOverview.getData().getDayArr();
        List<FinancialOverview.DataBean.MonthArrBean> monthArr = financialOverview.getData().getMonthArr();
        int dayArrSize = dayArr.size();
        int monthArrSize = monthArr.size();
        if ("toDay".equals(finTextCickTag)) {
            try {
                if (dayArrSize > 0) {
                    for (int i = 0; i < dayArrSize; i++) {
                        FinancialOverview.DataBean.DayArrBean dayArrBean = dayArr.get(i);
                        switch (dayArrBean.getType()) {
                            case Define.finApplyType1:
                                int pmSingular = dayArrBean.getCount();
                                String pmSingularStr = String.valueOf(pmSingular);
                                if (pmSingularStr != null) {
                                    tv_pmSingular.setText(pmSingularStr);
                                } else {
                                    tv_pmSingular.setText("");
                                }

                                pmCircleStr = dayArrBean.getStandby2()==null ?"0": dayArrBean.getStandby2();
                                if (!"0".equals(pmCircleStr)) {

                                    tv_pmCircle.setVisibility(View.VISIBLE);
                                    tv_pmCircle.setText(pmCircleStr);

//										if(roleName.contains("系统管理员")) {
//											tv_pmCircleAdmin.setText(pmCircleStr);
//										}
                                } else {
                                    if(tv_pmCircle != null) {
                                        tv_pmCircle.setVisibility(View.GONE);
                                    }

                                }
                                String pmMoneyStr = dayArrBean.getSumString();
                                if (pmMoneyStr != null) {
                                    tv_pmMoney.setText(pmMoneyStr + "元");
                                } else {
                                    tv_pmMoney.setText("");
                                }

                                break;
                            case Define.finApplyType2:
                                wdCircleStr = dayArrBean.getStandby2()==null ?"0": dayArrBean.getStandby2();
                                if (!"0".equals(wdCircleStr)) {

                                    tv_wdCircle.setVisibility(View.VISIBLE);
                                    tv_wdCircle.setText(wdCircleStr);

//										if(roleName.contains("系统管理员")) {
//											tv_wdCircleAdmin.setText(wdCircleStr);
//										}
                                } else {
                                    if(tv_wdCircle !=null) {
                                        tv_wdCircle.setVisibility(View.GONE);
                                    }
                                }

                                int wdSingular = dayArrBean.getCount();
                                String wdSingularStr = String.valueOf(wdSingular);
                                if (wdSingularStr != null) {
                                    tv_wdSingular.setText(wdSingularStr);
                                } else {
                                    tv_wdSingular.setText("");
                                }

                                String wdMoneyStr = dayArrBean.getSumString();
                                if (wdMoneyStr != null) {
                                    tv_wdMoney.setText(wdMoneyStr + "元");
                                } else {
                                    tv_wdMoney.setText("");
                                }

                                break;

                            case Define.finApplyType3:
                                dtCircleStr = dayArrBean.getStandby2()==null ?"0": dayArrBean.getStandby2();
                                if (!"0".equals(dtCircleStr)) {

                                    tv_dtCircle.setVisibility(View.VISIBLE);
                                    tv_dtCircle.setText(dtCircleStr);

//										if(roleName.contains("系统管理员")) {
//											tv_dtCircleAdmin.setText(dtCircleStr);
//										}
                                } else {
                                    if(tv_dtCircle !=null) {
                                        tv_dtCircle.setVisibility(View.GONE);
                                    }
                                }

                                int dtSingular = dayArrBean.getCount();
                                String dtSingularStr = String.valueOf(dtSingular);
                                if (dtSingularStr != null) {
                                    tv_dtSingular.setText(dtSingularStr);
                                } else {
                                    tv_dtSingular.setText("");
                                }

                                String dtMoneyStr = dayArrBean.getSumString();
                                if (dtMoneyStr != null) {
                                    tv_dtMoney.setText(dtMoneyStr + "元");
                                } else {
                                    tv_dtMoney.setText("");
                                }
                                break;
                            case Define.finApplyType4:
                                dfCircleStr = dayArrBean.getStandby2()==null ?"0": dayArrBean.getStandby2();
                                if (!"0".equals(dfCircleStr)) {

                                    tv_dfCircle.setVisibility(View.VISIBLE);
                                    tv_dfCircle.setText(dfCircleStr);

//										if(roleName.contains("系统管理员")) {
//											tv_dfCircleAdmin.setText(dfCircleStr);
//										}
                                } else {
                                    if(tv_dfCircle != null) {
                                        tv_dfCircle.setVisibility(View.GONE);
                                    }
                                }

                                int dfSingular = dayArrBean.getCount();
                                String dfSingularStr = String.valueOf(dfSingular);
                                if (dfSingularStr != null) {
                                    tv_dfSingular.setText(dfSingularStr);
                                } else {
                                    tv_dfSingular.setText("");
                                }

                                String dfMoneyStr = dayArrBean.getSumString();
                                if (dfMoneyStr != null) {
                                    tv_dfMoney.setText(dfMoneyStr + "元");
                                } else {
                                    tv_dfMoney.setText("");
                                }
                                break;

                            case Define.finApplyType5:
                                whCircleStr = dayArrBean.getStandby2()==null?"0":dayArrBean.getStandby2();
                                if (!"0".equals(whCircleStr)) {

                                    tv_whCircle.setVisibility(View.VISIBLE);
                                    tv_whCircle.setText(whCircleStr);

//										if(roleName.contains("系统管理员")) {
//											tv_whCircleAdmin.setText(whCircleStr);
//										}
                                } else {
                                    if(tv_whCircle!=null) {
                                        tv_whCircle.setVisibility(View.GONE);
                                    }
                                }

                                int whSingular = dayArrBean.getCount();
                                String whSingularStr = String.valueOf(whSingular);
                                if (whSingularStr != null) {
                                    tv_whSingular.setText(whSingularStr);
                                } else {
                                    tv_whSingular.setText("");
                                }

                                String whMoneyStr = dayArrBean.getSumString();
                                if (whMoneyStr != null) {
                                    tv_whMoney.setText(whMoneyStr + "元");
                                } else {
                                    tv_whMoney.setText("");
                                }
                                break;

                            case  Define.finApplyType7:
                                int stationNumber = dayArrBean.getCount();
                                String stationNumberStr = String.valueOf(stationNumber);
                                if (stationNumberStr != null) {
                                    tv_stationNumber.setText(stationNumberStr);
                                } else {
                                    tv_stationNumber.setText("");
                                }


                                String pmMoneyStr2 = dayArrBean.getSumString();
                                if (pmMoneyStr2 != null) {
                                    tv_pmMoney2.setText(pmMoneyStr2 + "元");
                                } else {
                                    tv_pmMoney2.setText("");
                                }

                                break;
                        }

                    }


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if ("toMoth".equals(finTextCickTag)){
            try {
                if (monthArrSize > 0) {
                    for (int i = 0; i < monthArrSize; i++) {
                        FinancialOverview.DataBean.MonthArrBean monthArrBean = monthArr.get(i);
                        switch (monthArrBean.getType()) {
                            case Define.finApplyType1:
                                int pmSingular = monthArrBean.getCount();
                                String pmSingularStr = String.valueOf(pmSingular);
                                if (pmSingularStr != null) {
                                    tv_pmSingular.setText(pmSingularStr);
                                } else {
                                    tv_pmSingular.setText("");
                                }

                                pmCircleStr = monthArrBean.getStandby2()==null ?"0": monthArrBean.getStandby2();
                                if (!"0".equals(pmCircleStr)) {

                                    tv_pmCircle.setVisibility(View.VISIBLE);
                                    tv_pmCircle.setText(pmCircleStr);

//										if(roleName.contains("系统管理员")) {
//											tv_pmCircleAdmin.setText(pmCircleStr);
//										}
                                } else {
                                    if (tv_pmCircle != null) {
                                        tv_pmCircle.setVisibility(View.GONE);
                                    }
                                }
                                String pmMoneyStr = monthArrBean.getSumString();
                                if (pmMoneyStr != null) {
                                    tv_pmMoney.setText(pmMoneyStr + "元");
                                } else {
                                    tv_pmMoney.setText("");
                                }

                                break;
                            case Define.finApplyType2:
                                wdCircleStr = monthArrBean.getStandby2()==null ?"0": monthArrBean.getStandby2();
                                if (!"0".equals(wdCircleStr)) {

                                    tv_wdCircle.setVisibility(View.VISIBLE);
                                    tv_wdCircle.setText(wdCircleStr);

//										if(roleName.contains("系统管理员")) {
//											tv_wdCircleAdmin.setText(wdCircleStr);
//										}
                                } else {
                                    if (tv_wdCircle != null) {
                                        tv_wdCircle.setVisibility(View.GONE);
                                    }
                                }

                                int wdSingular = monthArrBean.getCount();
                                String wdSingularStr = String.valueOf(wdSingular);
                                if (wdSingularStr != null) {
                                    tv_wdSingular.setText(wdSingularStr);
                                } else {
                                    tv_wdSingular.setText("");
                                }

                                String wdMoneyStr = monthArrBean.getSumString();
                                if (wdMoneyStr != null) {
                                    tv_wdMoney.setText(wdMoneyStr + "元");
                                } else {
                                    tv_wdMoney.setText("");
                                }

                                break;

                            case Define.finApplyType3:
                                dtCircleStr = monthArrBean.getStandby2()==null ?"0": monthArrBean.getStandby2();
                                if (!"0".equals(dtCircleStr)) {

                                    tv_dtCircle.setVisibility(View.VISIBLE);
                                    tv_dtCircle.setText(dtCircleStr);
//										if(roleName.contains("系统管理员")) {
//											tv_dtCircleAdmin.setText(dtCircleStr);
//										}

                                } else {
                                    if (tv_dtCircle != null) {
                                        tv_dtCircle.setVisibility(View.GONE);
                                    }
                                }

                                int dtSingular = monthArrBean.getCount();
                                String dtSingularStr = String.valueOf(dtSingular);
                                if (dtSingularStr != null) {
                                    tv_dtSingular.setText(dtSingularStr);
                                } else {
                                    tv_dtSingular.setText("");
                                }

                                String dtMoneyStr = monthArrBean.getSumString();
                                if (dtMoneyStr != null) {
                                    tv_dtMoney.setText(dtMoneyStr + "元");
                                } else {
                                    tv_dtMoney.setText("");
                                }
                                break;
                            case Define.finApplyType4:
                                dfCircleStr = monthArrBean.getStandby2()==null ?"0":monthArrBean.getStandby2();
                                if (!"0".equals(dfCircleStr)) {

                                    tv_dfCircle.setVisibility(View.VISIBLE);
                                    tv_dfCircle.setText(dfCircleStr);
//										if(roleName.contains("系统管理员")) {
//											tv_dfCircleAdmin.setText(dfCircleStr);
//										}

                                } else {
                                    if (tv_dfCircle != null) {
                                        tv_dfCircle.setVisibility(View.GONE);
                                    }
                                }

                                int dfSingular = monthArrBean.getCount();
                                String dfSingularStr = String.valueOf(dfSingular);
                                if (dfSingularStr != null) {
                                    tv_dfSingular.setText(dfSingularStr);
                                } else {
                                    tv_dfSingular.setText("");
                                }

                                String dfMoneyStr = monthArrBean.getSumString();
                                if (dfMoneyStr != null) {
                                    tv_dfMoney.setText(dfMoneyStr + "元");
                                } else {
                                    tv_dfMoney.setText("");
                                }
                                break;

                            case Define.finApplyType5:

                                whCircleStr = monthArrBean.getStandby2()==null ?"0":monthArrBean.getStandby2();
                                if (!"0".equals(whCircleStr)) {

                                    tv_whCircle.setVisibility(View.VISIBLE);
                                    tv_whCircle.setText(whCircleStr);
//										if(roleName.contains("系统管理员")) {
//											tv_whCircleAdmin.setText(whCircleStr);
//										}

                                } else {
                                    if (tv_whCircle != null) {
                                        tv_whCircle.setVisibility(View.GONE);
                                    }
                                }

                                int whSingular = monthArrBean.getCount();
                                String whSingularStr = String.valueOf(whSingular);
                                if (whSingularStr != null) {
                                    tv_whSingular.setText(whSingularStr);
                                } else {
                                    tv_whSingular.setText("");
                                }

                                String whMoneyStr = monthArrBean.getSumString();
                                if (whMoneyStr != null) {
                                    tv_whMoney.setText(whMoneyStr + "元");
                                } else {
                                    tv_whMoney.setText("");
                                }
                                break;


                            case  Define.finApplyType7:
                                int stationNumber = monthArrBean.getCount();
                                String stationNumberStr = String.valueOf(stationNumber);
                                if (stationNumberStr != null) {
                                    tv_stationNumber.setText(stationNumberStr);
                                } else {
                                    tv_stationNumber.setText("");
                                }


                                String pmMoneyStr2 = monthArrBean.getSumString();
                                if (pmMoneyStr2 != null) {
                                    tv_pmMoney2.setText(pmMoneyStr2 + "元");
                                } else {
                                    tv_pmMoney2.setText("");
                                }

                                break;
                        }

                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }




    /**
     * 从后台获取财务首页的交易概况数据
     */
    private void getTradeOverview(JSONObject json,boolean off){
        if(!off){
            //继续下一个请求
            requestFinTodayPaysys(false);
        }
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            String todayIn = jsonObject.optString("todayIn");
            if (todayIn != null){
                tv_income.setText(todayIn);
            }else {
                tv_income.setText("0.00");
            }
            String todayOut = jsonObject.optString("todayOut");
            String newTodayOut = "";
            if (todayOut != null){
                if (todayOut.contains("-")){
                    newTodayOut = todayOut.replace("-", "");
                    tv_expenditure.setText(newTodayOut);
                }else {
                    tv_expenditure.setText(todayOut);
                }
            }else {
                tv_expenditure.setText("0.00");
            }

            Double d_todayIn = Double.parseDouble(todayIn);
            Double d_todayOut = Double.parseDouble(todayOut);
            Double d_newTodayOut = Math.abs(d_todayOut);//取支出的绝对值
            Double d_fnBalance = d_todayIn - d_newTodayOut;//结余等于收入减去支出的绝对值
            DecimalFormat df = new DecimalFormat("0.00");
            String fnBalance = df.format(d_fnBalance);
            tv_fnBalance.setText(fnBalance);
            float f_todayIn = Float.parseFloat(d_todayIn+"");
            float f_todayOut = Float.parseFloat(d_newTodayOut+"");
            List<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0f, f_todayIn));
            entries.add(new BarEntry(1f, f_todayOut));
            BarDataSet set = new BarDataSet(entries, "");
            set.setColors(new int[]{Color.rgb(197, 197, 83), Color.rgb(251, 154, 121)});//设置柱状图的颜色
            BarData data = new BarData(set);
            data.setBarWidth(0.3f); //设置自定义条形宽度
            tradeBarChart.setData(data);
            tradeBarChart.setFitBars(true); //使x轴完全适合所有条形
            tradeBarChart.invalidate(); // refresh



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从后台获取财务首页的今日汇缴数据
     */
    private void getFinTodayPaysys(JSONObject json,boolean off){

        if(!off){
            //继续下一个请求
            requestPaymentCount(true);
        }
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            int todayCount = jsonObject.optInt("todayCount");
            String todayCountStr = String.valueOf(todayCount);
            String todaySumStr = jsonObject.optString("todaySum");
            int unCompleteCount = jsonObject.optInt("unCompleteCount");
            String unCompleteCountStr = String.valueOf(unCompleteCount);
            String unCompleteSumStr = jsonObject.optString("unCompleteSum");

                if (todayCountStr != null) {
                    tv_importedPaysysRecord2.setText(todayCountStr);
                } else {
                    tv_importedPaysysRecord2.setText("");
                }
                if (todaySumStr != null) {
                    tv_importedSumMoney2.setText(todaySumStr);
                } else {
                    tv_importedSumMoney2.setText("");
                }
                if (unCompleteCountStr != null) {
                    tv_noFinishPaysysRecord2.setText(unCompleteCountStr);
                } else {
                    tv_noFinishPaysysRecord2.setText("");
                }
                if (unCompleteSumStr != null) {
                    tv_noFinishSumMoney2.setText(unCompleteSumStr);
                } else {
                    tv_noFinishSumMoney2.setText("");
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    //财务角色--关闭刷新动画
    public void financiaStopRefreshing(){
        //刷新完成关闭动画
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (financialRoleRefresh != null) {
                    financialRoleRefresh.setRefreshing(false);
                }
            }
        });
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
    private final int BACK_REFRESH_FINOVERVIEW = 20;//返回主界面是刷新首页财务总览数据标识
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //财务-首页-发起申请（出纳）
            case R.id.layout_launchApply:
                if (roleName.contains("出纳")) {
                 startActivityMethod(LaunchApplyA.class);
                }else {
                    ToastUtils.showToast(MyApplication.mContext, "只有出纳用户才能发起申请！");
                }
                break;
            //财务-首页-财务明细
            case R.id.layout_financialDetails:
                startActivityMethod(FinancialListA.class);
                break;
            //财务-首页-用户管理
            case R.id.layout_userManage:
                startActivityMethod(AdminUserManageA.class);
                break;
            //财务-首页-财务总览-今日
            case R.id.tv_today:
                finTextCickTag = "toDay";
                setTextStyle(tv_today, R.drawable.textbackgroud5, Color.rgb(255, 255, 255));
                setTextStyle(tv_toMoth, R.drawable.textbackgroud6, Color.rgb(74, 74, 74));
                requestFinancialOverview(true);
                break;

            //财务-首页-财务总览-今月
            case R.id.tv_toMoth:
                finTextCickTag = "toMoth";
                setTextStyle(tv_toMoth, R.drawable.textbackgroud5, Color.rgb(255, 255, 255));
                setTextStyle(tv_today, R.drawable.textbackgroud6, Color.rgb(74, 74, 74));
                requestFinancialOverview(true);
                break;
            //财务-首页-财务总览-提现
            case R.id.rl_withdrawals:
                intent = new Intent(MyApplication.mContext, FinancialListA.class);
                intent.putExtra("finTextCickTag", finTextCickTag);
                intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
//			intent.putExtra("waitHandleType", Define.finApplyType2);
                intent.putExtra("type", Define.finApplyType2);
                 startActivityIntent(intent);
                break;
            //财务-首页-财务总览-代扣
            case R.id.rl_withhold:
                intent = new Intent(MyApplication.mContext, FinancialListA.class);
                intent.putExtra("finTextCickTag", finTextCickTag);
                intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
//			intent.putExtra("waitHandleType", Define.finApplyType5);
                intent.putExtra("type", Define.finApplyType5);
                startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
//			startActivity(intent);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            //财务-首页-财务总览-代付
            case R.id.rl_daiFu:
                intent = new Intent(MyApplication.mContext, FinancialListA.class);
                intent.putExtra("finTextCickTag", finTextCickTag);
                intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
//			intent.putExtra("waitHandleType", Define.finApplyType4);
                intent.putExtra("type", Define.finApplyType4);
//			startActivity(intent);
                startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            //财务-首页-财务总览-代存
            case R.id.rl_depositary:
                intent = new Intent(MyApplication.mContext, FinancialListA.class);
                intent.putExtra("finTextCickTag", finTextCickTag);
                intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
//			intent.putExtra("waitHandleType", Define.finApplyType3);
                intent.putExtra("type", Define.finApplyType3);
//			startActivity(intent);
                startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            //财务-首页-财务总览-汇缴付款
            case R.id.rl_payment:
//                startActivityMethod(AdvanceInCashA.class);
                intent = new Intent(MyApplication.mContext, FinancialListA.class);
                intent.putExtra("finTextCickTag", finTextCickTag);
                intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                intent.putExtra("type", Define.finApplyType1);
                startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;

            //财务-首页-财务总览-汇缴付款
            case R.id.rl_payment2:
                startActivityMethod(NewAdvanceInCashA.class);
                /*intent = new Intent(MyApplication.mContext, FinancialListA.class);
                intent.putExtra("finTextCickTag", finTextCickTag);
                intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                intent.putExtra("type", Define.finApplyType1);
                startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);*/
                break;
            //财务-首页-财务总览-提现待办数入口
            case R.id.ll_waitWithdrawals:
                if (wdCircleStr != null && !("0".equals(wdCircleStr))) {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("waitHandleType", Define.finApplyType2);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("type", Define.finApplyType2);
//				intent.putExtra("waitHandleType", Define.finApplyType2);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;
            //财务-首页-财务总览-代扣待办数入口
            case R.id.ll_waitWithhold:
                if (whCircleStr != null && !("0".equals(whCircleStr))) {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("waitHandleType", Define.finApplyType5);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("type", Define.finApplyType5);
//				intent.putExtra("waitHandleType", Define.finApplyType5);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;
            //财务-首页-财务总览-代付待办数入口
            case R.id.ll_waitDaiFu:
                if (dfCircleStr != null && !("0".equals(dfCircleStr))) {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("waitHandleType", Define.finApplyType4);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("type", Define.finApplyType4);
//				intent.putExtra("waitHandleType", Define.finApplyType4);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;
            //财务-首页-财务总览-代存待办数入口
            case R.id.ll_waitDepositary:
                if (dtCircleStr != null && !("0".equals(dtCircleStr))) {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("waitHandleType", Define.finApplyType3);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("type", Define.finApplyType3);
//				intent.putExtra("waitHandleType", Define.finApplyType3);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;
            //财务-首页-财务总览-汇缴付款待办数入口
            case R.id.ll_waitPayment:
                if (pmCircleStr != null && !("0".equals(pmCircleStr))) {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("waitHandleType", Define.finApplyType1);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else {
                    intent = new Intent(MyApplication.mContext, FinancialListA.class);
                    intent.putExtra("finTextCickTag", finTextCickTag);
                    intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
                    intent.putExtra("type", Define.finApplyType1);
//				intent.putExtra("waitHandleType", Define.finApplyType1);
//				startActivity(intent);
                    startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                break;

            //财务-首页-财务总览-汇缴待办数入口
            case R.id.ll_waitPayment2:
                //TODO

                break;
            //财务-首页-交易概况-交易明细
            case R.id.tv_tradeDetailed:
                startActivityMethod(AdminOJiTransactionDetails.class);
                break;

            //财务-首页-今日汇缴-查看
            case R.id.tv_lookFinPaysys:
                startActivityMethod(AdminConsolidatedRecord.class);
                break;
            case R.id.ll_imported:  //今日汇缴 已导入(已完成)
                startActivityMethod(AdminConsolidatedRecord.class);
                break;
            case R.id.ll_noFinish: //系统管理员 汇缴页面 未完成汇缴
                intent = new Intent(MyApplication.mContext,AdminConsolidatedRecord.class);
                intent.putExtra("paidUpState","paidUpState");
                startActivityIntent(intent);
                break;
            case R.id.ll_importDetails:
                startActivityMethod(ImportRecordA.class);
                break;
            case R.id.tv_outsIncomeCount:
                baseActivity.startActivityMethod(IncomeStatisticsA.class);
                break;

            case R.id.tv_returnLateFee:
                intent = new Intent(MyApplication.mContext, ReturnLateFeeA.class);
                startActivityIntent(intent);
                break;

            case R.id.tv_stationPaymentInfo:
                intent = new Intent(MyApplication.mContext, StationPaymentA.class);
                intent.putExtra("paymentInfoTag", "homePage");
                baseActivity.startActivityIntent(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case BACK_REFRESH_FINOVERVIEW:
                requestFinancialOverview(true);
                break;
        }
    }

    /**
     * 设置文本背景样式
     * @param textView 控件
     * @param resid 资源id
     * @param color 颜色值
     */
    private void setTextStyle(TextView textView, int resid, int color){
        textView.setBackgroundResource(resid);
        textView.setTextColor(color);
    }
}

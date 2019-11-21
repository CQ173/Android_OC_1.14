package com.huoniao.oc.fragment.train_station;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.AdminConsolidatedRecord;
import com.huoniao.oc.common.HomepageCommon;
import com.huoniao.oc.custom.MySwipeRefreshLayout;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.trainstation.NewWarningInfoListA;
import com.huoniao.oc.trainstation.ProxyDistributionA;
import com.huoniao.oc.trainstation.RemittanceDetailsA;
import com.huoniao.oc.trainstation.StationStatisticsActivity;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SmartImageView;
import com.huoniao.oc.util.TextSwitcherView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/1/18.
 */

public class AdministrationHomepageF extends BaseFragment implements OnChartValueSelectedListener {


    @InjectView(R.id.sysTemNotification)
    TextSwitcherView sysTemNotification;
    @InjectView(R.id.ll_noticeBar)
    LinearLayout llNoticeBar;
    @InjectView(R.id.main_gallery)
    SmartImageView mainGallery;
    @InjectView(R.id.main_lin)
    LinearLayout mainLin;
    @InjectView(R.id.ll_orgManagement)
    LinearLayout llOrgManagement;
    @InjectView(R.id.ll_proxyDistribution)
    LinearLayout llProxyDistribution;
    @InjectView(R.id.tv_todayWarning)
    TextView tvTodayWarning;
    @InjectView(R.id.tv_toMonthWarning)
    TextView tvToMonthWarning;
    @InjectView(R.id.tv_toYearWarning)
    TextView tvToYearWarning;
    @InjectView(R.id.ll_paymentCount)
    LinearLayout llPaymentCount;
    @InjectView(R.id.mProgressBar1)
    ProgressBar mProgressBar1;
    @InjectView(R.id.mProgressBar2)
    ProgressBar mProgressBar2;
    @InjectView(R.id.tv_amountReceived)
    TextView tvAmountReceived;
    @InjectView(R.id.mProgressBar3)
    ProgressBar mProgressBar3;
    @InjectView(R.id.tv_agencyPayment)
    TextView tvAgencyPayment;
    @InjectView(R.id.ll_agentPaymentCount)
    LinearLayout llAgentPaymentCount;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.pieChart1)
    PieChart pieChart1;
    @InjectView(R.id.tv_finishNumber)
    TextView tvFinishNumber;
    @InjectView(R.id.tv_noFinishNumber)
    TextView tvNoFinishNumber;
    @InjectView(R.id.pieChart2)
    PieChart pieChart2;
    @InjectView(R.id.tv_finishMoney)
    TextView tvFinishMoney;
    @InjectView(R.id.tv_noFinishMoney)
    TextView tvNoFinishMoney;
    @InjectView(R.id.newhomepage_swipeRefresh)
    MySwipeRefreshLayout newhomepageSwipeRefresh;
    @InjectView(R.id.tv_shouldReceive)
    TextView tvShouldReceive;
    @InjectView(R.id.tv_waitDo_earlyWarning)
    TextView tvWaitDoEarlyWarning;
    @InjectView(R.id.tv_lookMore_earlyWarning)
    TextView tvLookMoreEarlyWarning;
    @InjectView(R.id.ll_waitDo_earlyWarning)
    LinearLayout llWaitDoEarlyWarning;

    private HomepageCommon homepageCommon;
    private boolean refreshFlag;//刷新标识
    private Intent intent;
    private final int WAITDO_WARNING = 30;//返回主界面是刷新首页预警数据标识



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.adminstation_view_homepage_f, null);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPieChart(pieChart1);
        initPieChart(pieChart2);
        initData();

    }

    private void initData() {
        tvDate.setText(DateUtils.getOldDate(-1));//获取前一天的时间
        mainGallery.setRatio(2.33f);
        newhomepageSwipeRefresh.setColorScheme(colors);
        newhomepageSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (newhomepageSwipeRefresh.isRefreshing()) {
                    try {
                        refreshFlag = true;
                        homepageCommon = new HomepageCommon(baseActivity) {
                            @Override
                            protected void mainNotificationCpdClose(CustomProgressDialog cpd, String state) {
                                super.mainNotificationCpdClose(cpd, state);

                                //根据业务去判断是否需要关闭加载框
                                if (state.equals("0")) {
                                    // cpd.dismiss();
                                    //成功 需要做的处理
                                    setRequestPaymentWarnCount(false);
                                } else if (state.equals("1")) {
                                    //失败
                                    adminStopRefreshing();
                                }
                            }
                        };

                        homepageCommon.getMainNotificationList(llNoticeBar, sysTemNotification, cpd); //获取系统通知

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        baseActivity = (BaseActivity) getActivity();
        homepageCommon = new HomepageCommon(baseActivity) {
            @Override
            protected void mainNotificationCpdClose(CustomProgressDialog cpd, String state) {  //系统通知
                super.mainNotificationCpdClose(cpd, state);
                //根据业务去判断是否需要关闭加载框
                switch (state) {
                    case "0":
                        cpd.dismiss();
                        setRequestPaymentWarnCount(false);
                        break;
                    case "1":
                        cpd.dismiss();
                        break;
                }
            }
        };
        homepageCommon.transferControl(mainGallery, mainLin);
        homepageCommon.carouselFigure("1");
        homepageCommon.create();

        try {
            homepageCommon.getMainNotificationList(llNoticeBar, sysTemNotification, cpd); //获取系统通知
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll_orgManagement, R.id.ll_proxyDistribution, R.id.ll_paymentCount, R.id.ll_agentPaymentCount, R.id.ll_waitDo_earlyWarning, R.id.tv_lookMore_earlyWarning})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_orgManagement:
                startActivityMethod(StationStatisticsActivity.class);
                break;
            case R.id.ll_proxyDistribution:
                startActivityMethod(ProxyDistributionA.class);
                break;
            case R.id.ll_paymentCount:
                startActivityMethod(RemittanceDetailsA.class);
                break;
            case R.id.ll_agentPaymentCount:
                intent = new Intent(MyApplication.mContext, AdminConsolidatedRecord.class);
                intent.putExtra("intoTag", "agentPaymentCount");
                startActivityIntent(intent);
                break;
//            case R.id.tv_ownership_institution:
//                if (RepeatClickUtils.repeatClick()) {
//                    showOwnershipPop();
//                }

            case R.id.ll_waitDo_earlyWarning:

                intent = new Intent(MyApplication.mContext, NewWarningInfoListA.class);
                intent.putExtra("processingState", "0");//未处理状态，传0
                startActivityForResult(intent, WAITDO_WARNING);
                break;

            case R.id.tv_lookMore_earlyWarning:

                intent = new Intent(MyApplication.mContext, NewWarningInfoListA.class);
                startActivityForResult(intent, WAITDO_WARNING);
                break;
        }
    }


    private void initPieChart(PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

//        pieChart1.setCenterTextTypeface(mTfLight);
//        pieChart1.setCenterText(generateCenterSpannableText());


// 设置 pieChart 内部圆环属性
        pieChart.setDrawHoleEnabled(true);              //是否显示PieChart内部圆环(true:下面属性才有意义)
        pieChart.setHoleRadius(28f);                    //设置PieChart内部圆的半径(这里设置28.0f)
        pieChart.setTransparentCircleRadius(31f);       //设置PieChart内部透明圆的半径(这里设置31.0f)
        pieChart.setTransparentCircleColor(Color.BLACK);//设置PieChart内部透明圆与内部圆间距(31f-28f)填充颜色
        pieChart.setTransparentCircleAlpha(50);         //设置PieChart内部透明圆与内部圆间距(31f-28f)透明度[0~255]数值越小越透明
        pieChart.setHoleColor(Color.WHITE);


        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

//        pieChart.setHoleRadius(58f);
//        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

//        setData();

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        pieChart.setEntryLabelColor(Color.WHITE);
//        pieChart1.setEntryLabelTypeface(mTfRegular);
        pieChart.setEntryLabelTextSize(12f);

        Legend legend = pieChart.getLegend();//设置比例图
        legend.setEnabled(false);//图例不显示
    }


    /**
     * 获取归属机构数据 弹出归属机构
     */
   /* private void showOwnershipPop() {
        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
        //得到状态栏高度
        //返回键关闭
        if (myPopWindow != null) {
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
                        tvOwnershipInstitution.getLocationOnScreen(arr);
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        baseActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                        xs = arr[0] + tvOwnershipInstitution.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + tvOwnershipInstitution.getHeight();
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node officeId) {
                                officeIdStr = String.valueOf(officeId.getAllTreeNode().id); // 归属机构筛选id
                                officeNodeName = officeId.getAllTreeNode().name;

//                                getFinTodayPaysys(officeIdStr,false,true);


                                tvOwnershipInstitution.setText(officeNodeName);

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
                myPopWindow.showAtLocation(tvOwnershipInstitution, Gravity.NO_GRAVITY, (int) xs, (int) ys);
            }
        };
    }*/

    //铁总首页--关闭刷新动画
    public void adminStopRefreshing() {
        //刷新完成关闭动画
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (newhomepageSwipeRefresh != null) {
                    newhomepageSwipeRefresh.setRefreshing(false);
                    refreshFlag = false;
                }
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    /**
     * 请求汇缴预警首页数据
     *
     * @param off 控制关闭加载框
     */
    private void setRequestPaymentWarnCount(boolean off) {
        String url = Define.URL + "fb/paymentWarnCount";
        JSONObject jsonObject = new JSONObject();
        requestNet(url, jsonObject, "paymentWarnCount", "1", off, true); //1没有实际意义值
    }


    /**
     * 请求首页汇缴统计数据
     *
     * @param off 控制关闭加载框
     */
    private void getOwnPaymentStatistical(boolean off) {
        String url = Define.URL + "fb/getOwnPaymentStatistical";
        JSONObject jsonObject = new JSONObject();
        requestNet(url, jsonObject, "getOwnPaymentStatistical", "1", off, true); //1没有实际意义值
    }

    /**
     * 请求首页代理汇缴统计数据
     *
     * @param off 控制关闭加载框
     */
    private void getPaymentStatisticalDetails(boolean off) {
        String url = Define.URL + "fb/getAgencyPaymentStatistical";
        JSONObject jsonObject = new JSONObject();
        requestNet(url, jsonObject, "getAgencyPaymentStatistical", "1", off, true); //1没有实际意义值
    }
    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag) {
            case "paymentWarnCount"://火车站首页汇缴预警（包括汇缴模块的汇缴预警）
                paymentWarnData(json);
                if (!controlOff) {
                    getOwnPaymentStatistical(false);
                }
                break;

            case "getOwnPaymentStatistical":
                setOwnPaymentData(json);
                if (!controlOff) {
                    getPaymentStatisticalDetails(true);
                }
                break;

            case "getAgencyPaymentStatistical":
                setAgentPaymentData(json);

                break;
        }
    }

    private void setAgentPaymentData(JSONObject json) {
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            String compeleteCountStr = jsonObject.optString("compeleteCount");
            String unCompeleteCountStr = jsonObject.optString("unCompeleteCount");
            String compeleteAmountStr = jsonObject.optString("compeleteAmount");
            String unCompeleteAmountStr = jsonObject.optString("unCompeleteAmount");

            if (compeleteCountStr != null) {
                tvFinishNumber.setText(compeleteCountStr);
            }
            if (unCompeleteCountStr != null) {
                tvNoFinishNumber.setText(unCompeleteCountStr);
            }
            if (compeleteAmountStr != null) {
                tvFinishMoney.setText(compeleteAmountStr);
            }

            if (unCompeleteAmountStr != null) {
                tvNoFinishMoney.setText(unCompeleteAmountStr);
            }

            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            ArrayList<PieEntry> entries2 = new ArrayList<PieEntry>();

            if ("0.00".equals(compeleteCountStr) && "0.00".equals(unCompeleteCountStr)){//防止都是0的时候不显示饼图
                entries.add(new PieEntry(Float.parseFloat(compeleteCountStr) + 1f));
                entries.add(new PieEntry(Float.parseFloat(unCompeleteCountStr) + 1f));
            }else {
                entries.add(new PieEntry(Float.parseFloat("50")+1f));
                entries.add(new PieEntry(Float.parseFloat("50")+1f));
            }

            if ("0.00".equals(compeleteAmountStr) && "0.00".equals(unCompeleteAmountStr)){
                entries2.add(new PieEntry(Float.parseFloat(compeleteAmountStr) + 1f));
                entries2.add(new PieEntry(Float.parseFloat(unCompeleteAmountStr) + 1f));
            }else {
                entries2.add(new PieEntry(Float.parseFloat("50")+1f));
                entries2.add(new PieEntry(Float.parseFloat("50")+1f));
            }

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setDrawIcons(false);
            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            PieDataSet dataSet2 = new PieDataSet(entries2, "");
            dataSet2.setDrawIcons(false);
            dataSet2.setSliceSpace(3f);
            dataSet2.setIconsOffset(new MPPointF(0, 40));
            dataSet2.setSelectionShift(5f);
            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(ContextCompat.getColor(MyApplication.mContext, R.color.bule_chart2));
            colors.add(ContextCompat.getColor(MyApplication.mContext, R.color.bule_chart3));
            dataSet.setColors(colors);

            ArrayList<Integer> colors2 = new ArrayList<Integer>();
            colors2.add(ContextCompat.getColor(MyApplication.mContext, R.color.yellow_chart));
            colors2.add(ContextCompat.getColor(MyApplication.mContext, R.color.yellow_chart2));
            dataSet2.setColors(colors2);
            //dataSet.setSelectionShift(0f);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());//将数值设置成百分比
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);

            PieData data2 = new PieData(dataSet2);
            data2.setValueFormatter(new PercentFormatter());//将数值设置成百分比
            data2.setValueTextSize(11f);
            data2.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(mTfLight);

            pieChart1.setData(data);
            pieChart1.highlightValues(null);
            pieChart1.invalidate();

            pieChart2.setData(data2);
            pieChart2.highlightValues(null);
            pieChart2.invalidate();



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setOwnPaymentData(JSONObject json) {
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            String shouldReceiveStr = jsonObject.optString("shouldReceive");
            String realReceiveStr = jsonObject.optString("realReceive");
            String realPayStr = jsonObject.optString("realPay");

            double shouldReceiveD;
            double realReceiveD;
            double realPayD;

            int shouldReceiveInt = 0;
            int realReceiveInt = 0;
            int realPayInt = 0;
            if (shouldReceiveStr != null) {
                shouldReceiveD = Double.valueOf(shouldReceiveStr);
                shouldReceiveInt = getInt(shouldReceiveD * 100);//为了设置进度的时候精确到小数点，扩大100倍让它成为整数
                tvShouldReceive.setText(shouldReceiveStr);
            }
            if (realReceiveStr != null) {
                realReceiveD = Double.valueOf(realReceiveStr);
                realReceiveInt = getInt(realReceiveD * 100);
                tvAmountReceived.setText(realReceiveStr);
            }
            if (realPayStr != null) {
                realPayD = Double.valueOf(realPayStr);
                realPayInt = getInt(realPayD * 100);
                tvAgencyPayment.setText(realPayStr);
            }



            int max = (shouldReceiveInt>realReceiveInt)?shouldReceiveInt:realReceiveInt;//比较三个数值大小
            max = (max>realPayInt)?max:realPayInt;
            mProgressBar1.setMax(max);
            mProgressBar2.setMax(max);
            mProgressBar3.setMax(max);
            mProgressBar1.setProgress(shouldReceiveInt);
            mProgressBar2.setProgress(realReceiveInt);
            mProgressBar3.setProgress(realPayInt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * double转int四舍五入
     * @param number
     * @return
     */
    private int getInt(double number){
        BigDecimal bd=new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }

    @Override
    protected void closeDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.closeDismiss(cpd, tag, controlOff);
        switch (tag) {
            case "getAgencyPaymentStatistical":
              adminStopRefreshing();
                break;
        }
    }


    /**
     * 网络异常关闭加载框
     */
    @Override
    protected void loadControlExceptionDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.loadControlExceptionDismiss(cpd, tag, controlOff);
        switch (tag) {
            case "paymentWarnCount":
                if (!controlOff) {
                    getOwnPaymentStatistical(false);
                }
                break;

            case "getOwnPaymentStatistical":
                if (!controlOff) {
                    getPaymentStatisticalDetails(true);
                }
                break;


        }
        adminStopRefreshing();
    }

    /**
     * 预警数据
     *
     * @param json
     */
    private void paymentWarnData(JSONObject json) {
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            String waitDo_earlyWarning = jsonObject.optString("waitHandle");
            if (waitDo_earlyWarning != null && !"0".equals(waitDo_earlyWarning)) {
                tvWaitDoEarlyWarning.setText(waitDo_earlyWarning + "条待处理");
            } else {
                llWaitDoEarlyWarning.setVisibility(View.GONE);
            }
            String dayWarningNum = jsonObject.optString("day");
            if (dayWarningNum != null) {
                tvTodayWarning.setText(dayWarningNum);

            } else {
                tvTodayWarning.setText("0");
            }
            String mothWarningNum = jsonObject.optString("month");
            if (mothWarningNum != null) {
                tvToMonthWarning.setText(mothWarningNum);
            } else {
                tvToMonthWarning.setText("0");
            }

            String yearWarningNum = jsonObject.optString("year");
            if (yearWarningNum != null) {
                tvToYearWarning.setText(yearWarningNum);
            } else {
                tvToYearWarning.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case WAITDO_WARNING:  //预警
                setRequestPaymentWarnCount(true);
                break;
        }
    }


}

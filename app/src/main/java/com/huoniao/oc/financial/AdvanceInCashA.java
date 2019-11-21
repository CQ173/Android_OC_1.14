package com.huoniao.oc.financial;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.DictAryBean;
import com.huoniao.oc.bean.PaymentPayListBean;
import com.huoniao.oc.bean.PaymentPaySumListBean;
import com.huoniao.oc.bean.PaymentPaySumSpotInfoBean;
import com.huoniao.oc.bean.PaymentPaySumTisBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.myOnTabSelectedListener;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class AdvanceInCashA extends BaseActivity implements OnChartValueSelectedListener {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.v_backgroud)
    View vBackgroud;
    @InjectView(R.id.v_backgroud2)
    View vBackgroud2;
    @InjectView(R.id.tb_layout)
    TabLayout tbLayout;
    @InjectView(R.id.tv_quota)
    TextView tvQuota;
    @InjectView(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.tv_alreadyUsed)
    TextView tvAlreadyUsed;
    @InjectView(R.id.tv_unused)
    TextView tvUnused;
    @InjectView(R.id.tradeBarChart)
    BarChart tradeBarChart;
    @InjectView(R.id.tv_start_date)
    TextView tvStartDate;
    @InjectView(R.id.ll_start_date)
    LinearLayout llStartDate;
    @InjectView(R.id.tv_end_date)
    TextView tvEndDate;
    @InjectView(R.id.ll_end_date)
    LinearLayout llEndDate;
    @InjectView(R.id.tv_query)
    TextView tvQuery;
    @InjectView(R.id.ll_date)
    LinearLayout llDate;
    @InjectView(R.id.tv_recordCount)
    TextView tvRecordCount;
    @InjectView(R.id.mListView)
    MyListView mListView;
    @InjectView(R.id.sr_statistics)
    ScrollView srStatistics;
    @InjectView(R.id.tv_start_date2)
    TextView tvStartDate2;
    @InjectView(R.id.ll_start_date2)
    LinearLayout llStartDate2;
    @InjectView(R.id.tv_end_date2)
    TextView tvEndDate2;
    @InjectView(R.id.ll_end_date2)
    LinearLayout llEndDate2;
    @InjectView(R.id.ll_date2)
    LinearLayout llDate2;
    @InjectView(R.id.tv_sourceOfFunds)
    TextView tvSourceOfFunds;
    @InjectView(R.id.tv_state)
    TextView tvState;
    @InjectView(R.id.tv_query2)
    TextView tvQuery2;
    @InjectView(R.id.mPullToRefreshListView2)
    PullToRefreshListView mPullToRefreshListView2;
    @InjectView(R.id.ll_detailed)
    LinearLayout llDetailed;
    @InjectView(R.id.et_searchContent)
    EditText etSearchContent;
    @InjectView(R.id.tv_recordCount2)
    TextView tvRecordCount2;
    private String type = "0";
    private Intent intent;
    private CommonAdapter<PaymentPaySumListBean.DataBean> commonAdapter;
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private String startDate = "";
    private String endDate = "";
    private String startDate2 = "";
    private String endDate2 = "";
    private List<DictAryBean.DataBean> dictAryBean = new ArrayList<>();
    private MyPopWindow myPopWindow;
    private ListView lv_advanceState;  //弹出框滞纳金返还状态列表
    private float xs;
    private float ys;
    private String stateValue = "";//选择的状态值
    private String sourceOfFundValue = "";//选择的资金来源的值
    private String dictAryTag;//用来区分点击的是状态还是资金来源的弹出框
    private String searchContent = "";//搜索内容
    private ListView mListView2;
    private String next = "";        //返回来的页数
    private User user;
    private String roleName;
    private CommonAdapter<PaymentPayListBean.DataBean> commonAdapter1;
    private  List<PaymentPayListBean.DataBean> paymentPayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_in_cash);
        ButterKnife.inject(this);
        initWidget();
        initData();
    }

    private void initData() {
        user = (User) readObject(AdvanceInCashA.this, "loginResult");
        roleName = user.getRoleName();
        getPaymentPaySumSpotInfo();
        getPaymentPaySumTis();
        getPaymentPaySumList();
        getPaymentPayList("1");
    }


    private void initWidget() {
        tbLayout.addTab(tbLayout.newTab().setText("垫款统计"));
        switchBackgroud(vBackgroud);
        tbLayout.addTab(tbLayout.newTab().setText("垫款明细"));
        setIndicator(tbLayout, 30, 30);

        tbLayout.setOnTabSelectedListener(new myOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中了tab的逻辑
                switch (tab.getPosition()) {
                    case 0:  //今日已导入
                        switchBackgroud(vBackgroud);
                        type = "0";
                        srStatistics.setVisibility(View.VISIBLE);
                        llDetailed.setVisibility(View.GONE);
//                        if (myListData != null){
//                            myListData.clear();
//                        }
//                        requestPaymentInfo();
                        break;
                    case 1:  //今日未导入
                        switchBackgroud(vBackgroud2);
                        type = "1";
                        srStatistics.setVisibility(View.GONE);
                        llDetailed.setVisibility(View.VISIBLE);
//                        if (myListData != null){
//                            myListData.clear();
//                        }
//                        requestPaymentInfo();
                        break;
                }
            }

        });

        if (myDatePickerDialog == null) {
            myDatePickerDialog = new MyDatePickerDialog();
        }

        mPullToRefreshListView2.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView2.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView2.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullToRefreshListView2.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView2 = mPullToRefreshListView2.getRefreshableView();
        startDate2 = DateUtils.getTime();
        endDate2 = DateUtils.getTime();
        tvStartDate2.setText(startDate2);
        tvEndDate2.setText(endDate2);
        initPullRefreshLinstener();
    }

    //控件背景颜色切换
    private void switchBackgroud(View view) {
        vBackgroud.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        vBackgroud2.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        if (view != null) {
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * tablayout下滑线设置
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
        tabStrip = null;
        llTab = null;
    }


    private void initBarChart(List<String> timeList, List<String> paymentAmountList,
                              List<String> returnAmountList, List<String> spotAmountList) {
        tradeBarChart.setOnChartValueSelectedListener(this);
        tradeBarChart.setDrawBarShadow(false);
        tradeBarChart.setDrawValueAboveBar(true);
        tradeBarChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
//        tradeBarChart.setMaxVisibleValueCount(12);
        // scaling can now only be done on x- and y-axis separately
        tradeBarChart.setPinchZoom(false);
        tradeBarChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        List<String> xAxisValue = new ArrayList<String>();
        if (timeList != null) {
            xAxisValue.addAll(timeList);
        }

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
        legend.setEnabled(true);
        List<BarEntry> entries1 = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            entries1.add(new BarEntry(i, Float.parseFloat(paymentAmountList.get(i))));//汇缴金额遍历
        }
        List<BarEntry> entries2 = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            entries2.add(new BarEntry(i, Float.parseFloat(spotAmountList.get(i))));// 垫款金额遍历
        }
        List<BarEntry> entries3 = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            entries3.add(new BarEntry(i, Float.parseFloat(returnAmountList.get(i))));// 还款金额遍历
        }

        BarDataSet set1 = new BarDataSet(entries1, "汇缴金额");
        set1.setColors(Color.rgb(75, 127, 175));//设置柱状图的颜色

        BarDataSet set2 = new BarDataSet(entries2, "中信垫资");
        set2.setColors(Color.rgb(90, 155, 213));//设置柱状图的颜色

        BarDataSet set3 = new BarDataSet(entries3, "已还款");
        set3.setColors(Color.rgb(173, 198, 229));//设置柱状图的颜色

        BarData data = new BarData(set1, set2, set3);
//        BarData data2 = new BarData(set2);
//        BarData data3 = new BarData(set3);

        tradeBarChart.setData(data);

        float barSpace = 0.02f;
        float groupSpace = 0.3f;
//        int groupCount = 3;
        data.setBarWidth(0.15f);
        tradeBarChart.getXAxis().setAxisMinimum(0);
//        tradeBarChart.getXAxis().setAxisMaximum(0 + tradeBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        tradeBarChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        tradeBarChart.setFitBars(true); //使x轴完全适合所有条形
        tradeBarChart.invalidate(); // refresh
    }


    @OnClick({R.id.iv_back, R.id.ll_start_date, R.id.ll_end_date, R.id.tv_query, R.id.ll_start_date2, R.id.ll_end_date2, R.id.tv_sourceOfFunds, R.id.tv_state, R.id.tv_query2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_start_date:
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        startDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(AdvanceInCashA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvStartDate.setText(date);
                        getPaymentPaySumList();
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.ll_end_date:
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        endDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(AdvanceInCashA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvEndDate.setText(date);

                        getPaymentPaySumList();
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.tv_query:
                getPaymentPaySumList();
                break;
            case R.id.ll_start_date2:
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        startDate2 = date;
                        if ((!startDate2.isEmpty()) && (!endDate2.isEmpty())) {
                            if (Date.valueOf(startDate2).after(Date.valueOf(endDate2))) {
                                Toast.makeText(AdvanceInCashA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvStartDate2.setText(date);
//                        getPaymentPaySumList();
                        getPaymentPayList("1");
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.ll_end_date2:
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        endDate2 = date;
                        if ((!startDate2.isEmpty()) && (!endDate2.isEmpty())) {
                            if (Date.valueOf(startDate2).after(Date.valueOf(endDate2))) {
                                Toast.makeText(AdvanceInCashA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvEndDate2.setText(date);
                        getPaymentPayList("1");
//                        getPaymentPaySumList();
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.tv_sourceOfFunds:
                dictAryTag = "sourceOfFunds";
                queryDictAry("acct_paymentPay_source");
                break;
            case R.id.tv_state:
                dictAryTag = "state";
                queryDictAry("acct_paymentPay_state");
                break;
            case R.id.tv_query2:
                getPaymentPayList("1");
                break;
        }
    }

    /**
     * 展示申滞纳金返还状态下拉框
     */
    private void showReturnState(final TextView mTextView) {
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                lv_advanceState = (ListView) view.findViewById(R.id.lv_audit_status);
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //重新测量
//                int w =  lv_applyType.getMeasuredWidth();
//                cow = Math.abs(w - xOffset);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                mTextView.getLocationOnScreen(arr);
                view.measure(0, 0);
                xs = arr[0] + mTextView.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + mTextView.getHeight();

                CommonAdapter<DictAryBean.DataBean> commonAdapter = new CommonAdapter<DictAryBean.DataBean>(AdvanceInCashA.this, dictAryBean, R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, DictAryBean.DataBean data) {
                        holder.setText(R.id.tv_text, data.getLabel());

                    }
                };

                lv_advanceState.setAdapter(commonAdapter);
                lv_advanceState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String applyTypeStr = dictAryBean.get(i).getLabel(); //获取点击的文字
                        mTextView.setText(applyTypeStr);
                        //获取点击的類型對應的代號
                        if ("sourceOfFunds".equals(dictAryTag)) {
                            sourceOfFundValue = dictAryBean.get(i).getValue();
                        } else if ("state".equals(dictAryTag)) {
                            stateValue = dictAryBean.get(i).getValue();
                        }
                        getPaymentPayList("1");
                        myPopWindow.dissmiss();
//                        queryLateFeeList("1");
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(this).create();
        myPopWindow.keyCodeDismiss(true); //返回键关闭
        myPopWindow.showAtLocation(mTextView, Gravity.NO_GRAVITY, (int) xs, (int) ys);
    }

    /**
     * //上拉加载更多
     */
    private void initPullRefreshLinstener() {
        mPullToRefreshListView2.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {  //上拉加载更多
                if (next.equals("-1")) {
//                    Toast.makeText(AdvanceInCashA.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ToastUtils.showToast(AdvanceInCashA.this, "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView2.onRefreshComplete();
                        }
                    });
                } else {
                    getPaymentPayList(next);
                }
            }
        });
    }

    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        mPullToRefreshListView2.onRefreshComplete();
    }

    /**
     * 请求垫款统计垫款额度信息
     */
    public void getPaymentPaySumSpotInfo() {
        String url = Define.URL + "acct/getPaymentPaySumSpotInfo";
        JSONObject jsonObject = new JSONObject();
        requestNet(url, jsonObject, "getPaymentPaySumSpotInfo", "1", true, false);
    }

    /**
     * 请求垫款统计柱形图接口
     */
    public void getPaymentPaySumTis() {
        String url = Define.URL + "acct/getPaymentPaySumTis";
        JSONObject jsonObject = new JSONObject();
        requestNet(url, jsonObject, "getPaymentPaySumTis", "1", true, false);
    }

    /**
     * 请求垫款统计列表接口
     */
    public void getPaymentPaySumList() {
        String url = Define.URL + "acct/getPaymentPaySumList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getPaymentPaySumList", "1", true, false);
    }


    private void queryDictAry(String dictType) {
        String url = Define.URL + "sys/getDictAry";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dictType", dictType);//通过字典接口查询滞纳金返还状态
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getAdvanceDictAry", "0", true, false); //0 不代表什么
    }

    private void getPaymentPayList(String pageNo) {
        searchContent = etSearchContent.getText().toString().trim();
        String url = Define.URL + "acct/paymentPayList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", startDate2);
            jsonObject.put("endDate", endDate2);
            jsonObject.put("states", stateValue);
            jsonObject.put("sources", sourceOfFundValue);
            jsonObject.put("query", searchContent);
            jsonObject.put("pageNo", pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentPayList", pageNo, true, false); //0 不代表什么
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getPaymentPaySumSpotInfo":
                Log.d("paymentPaySumSpotInfo", json.toString());
                Gson gson = new Gson();
                PaymentPaySumSpotInfoBean paymentPaySumSpotInfoBean = gson.fromJson(json.toString(), PaymentPaySumSpotInfoBean.class);
                PaymentPaySumSpotInfoBean.DataBean dataBeen = paymentPaySumSpotInfoBean.getData();
                tvQuota.setText(dataBeen.getMaxSpotAmount() + "");
                tvAlreadyUsed.setText(dataBeen.getUsedAmount());
                tvUnused.setText(dataBeen.getCouldUseAmount());
                mProgressBar.setMax(dataBeen.getMaxSpotAmount());
                double usedAmountD = Double.parseDouble(dataBeen.getUsedAmount());
                DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数
                int usedAmountI = Integer.parseInt(df.format(usedAmountD));
                mProgressBar.setProgress(usedAmountI);
                break;
            case "getPaymentPaySumTis":
                Log.d("getPaymentPaySumTis", json.toString());
                setBarChartData(json);
                break;

            case "getPaymentPaySumList":
                Log.d("paymentPaySumList", json.toString());
                setAdapter(json);
                break;

            case "getAdvanceDictAry":
                dictAryBean.clear();
                Gson gson2 = new Gson();
                DictAryBean dataBean = gson2.fromJson(json.toString(), DictAryBean.class);
                List<DictAryBean.DataBean> dictAryList = dataBean.getData();
                DictAryBean.DataBean dataBean1 = new DictAryBean.DataBean();
                dataBean1.setLabel("全部");
                dataBean1.setValue("");
                dictAryBean.add(dataBean1);
                if (dictAryList != null && dictAryList.size() > 0) {
                    dictAryBean.addAll(dictAryList);
                    if ("state".equals(dictAryTag)) {
                        showReturnState(tvState);
                    } else if ("sourceOfFunds".equals(dictAryTag)) {
                        showReturnState(tvSourceOfFunds);
                    }
                }
                Log.d("dictAryBean", dictAryBean.toString());
                break;

            case "paymentPayList":
                Log.d("paymentPayList", json.toString());
                setPaymentPayAdpter(json, pageNumber);
                break;
        }
    }

    private void setPaymentPayAdpter(JSONObject jsonObject, String pageNo) {
        Gson gson = new Gson();
        PaymentPayListBean paymentPayListBean = gson.fromJson(jsonObject.toString(), PaymentPayListBean.class);
        if (pageNo.equals("1")) {
            //集成清空处理
            paymentPayList.clear();
            if (commonAdapter1 != null) {
                mListView2.setAdapter(commonAdapter1);
            }
        }
        List<PaymentPayListBean.DataBean> dataBeenList = paymentPayListBean.getData();
        if (dataBeenList != null && dataBeenList.size() > 0) {
            paymentPayList.addAll(dataBeenList);
        }
        next = String.valueOf(paymentPayListBean.getNext());
        tvRecordCount2.setText(paymentPayListBean.getSize() + "");
        if (commonAdapter1 == null) {
            commonAdapter1 = new CommonAdapter<PaymentPayListBean.DataBean>(AdvanceInCashA.this, paymentPayList, R.layout.item_advanceincash_details2) {
                @Override
                public void convert(ViewHolder holder, PaymentPayListBean.DataBean dataBean) {
                    holder.setText(R.id.tv_date, dataBean.getTime())
                            .setText(R.id.tv_stationName, dataBean.getStationName())
                            .setText(R.id.tv_paymentAmount, dataBean.getPaymentAmountString() + "元")
                            .setText(R.id.tv_state, dataBean.getStateName())
                            .setText(R.id.tv_spotAmount, dataBean.getSpotAmountString() + "元");
                }
            };
            mListView2.setAdapter(commonAdapter1);
        }
        commonAdapter1.notifyDataSetChanged();
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PaymentPayListBean.DataBean dataBean = paymentPayList.get(position - 1);
                String state = dataBean.getState();
                ObjectSaveUtil.saveObject(AdvanceInCashA.this, "paymentPayList", dataBean);
                if (roleName.contains("会计") && Define.PAYMENT_PAYSTATE_4.equals(state)) {
                    intent = new Intent(AdvanceInCashA.this, AepaymentAuditingA.class);
                    intent.putExtra("performAction", "还款");
                }else if (roleName.contains("会计") && Define.PAYMENT_PAYSTATE_7.equals(state)) {
                    intent = new Intent(AdvanceInCashA.this, AepaymentAuditingA.class);
                    intent.putExtra("performAction", "付款");
                }else {
                    intent = new Intent(AdvanceInCashA.this, AdvanceInCashDetails2A.class);
                }
                startActivityForResult(intent, 15);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
    }

    private void setAdapter(JSONObject jsonObject) {
        Gson gson = new Gson();
        PaymentPaySumListBean paymentPaySumListBean = gson.fromJson(jsonObject.toString(), PaymentPaySumListBean.class);
        final List<PaymentPaySumListBean.DataBean> dataBeenList = paymentPaySumListBean.getData();
        tvRecordCount.setText(paymentPaySumListBean.getSize() + "");
//        if (commonAdapter == null) {
        commonAdapter = new CommonAdapter<PaymentPaySumListBean.DataBean>(AdvanceInCashA.this, dataBeenList, R.layout.item_advanceincash_details1) {
            @Override
            public void convert(ViewHolder holder, PaymentPaySumListBean.DataBean dataBean) {
                holder.setText(R.id.tv_date, dataBean.getTime())
                        .setText(R.id.tv_stationImportCount, dataBean.getShouldPaymentCount() + "(" + dataBean.getAlreadyPaymentCount() + ")" + "家")
                        .setText(R.id.tv_paymentAmount, dataBean.getPaymentAmountString() + "元")
                        .setText(R.id.tv_advanceCashSum, dataBean.getSpotAmountString() + "元")
                        .setText(R.id.tv_noRepaymentSum, dataBean.getNotReturnAmountString() + "元");
            }
        };
        mListView.setAdapter(commonAdapter);
//        }
//        commonAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PaymentPaySumListBean.DataBean dataBean = dataBeenList.get(position);
                ObjectSaveUtil.saveObject(AdvanceInCashA.this, "paymentPaySumList", dataBean);
                intent = new Intent(AdvanceInCashA.this, AdvanceInCashDetailsA.class);
                startActivityForResult(intent, 15);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

            }
        });
    }

    private void setBarChartData(JSONObject jsonObject) {
        Gson gson = new Gson();
        PaymentPaySumTisBean paymentPaySumTisBean = gson.fromJson(jsonObject.toString(), PaymentPaySumTisBean.class);
        PaymentPaySumTisBean.DataBean dataBeen = paymentPaySumTisBean.getData();
        if ("success".equals(dataBeen.getResult())) {
            List<String> timeList = dataBeen.getTime();
            List<String> paymentAmountList = dataBeen.getPaymentAmount();
            List<String> returnAmountList = dataBeen.getReturnAmount();
            List<String> spotAmountList = dataBeen.getSpotAmount();
            initBarChart(timeList, paymentAmountList, returnAmountList, spotAmountList);

        } else {
            ToastUtils.showToast(AdvanceInCashA.this, dataBeen.getMsg());
        }

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 15:
                getPaymentPaySumList();
                getPaymentPayList("1");
                break;
        }
    }


}

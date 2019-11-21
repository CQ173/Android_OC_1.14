package com.huoniao.oc.user;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.EarningsDetailBean;
import com.huoniao.oc.bean.ProfitCountTopBean;
import com.huoniao.oc.bean.TicketCountBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

import static com.huoniao.oc.MyApplication.treeIdList2;

public class IncomeStatisticsA extends BaseActivity implements OnChartValueSelectedListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_todayWaitPayment)
    TextView tvTodayWaitPayment;
    //    @InjectView(R.id.tv_todayIncome)
//    TextView tvTodayIncome;
    @InjectView(R.id.tv_todayReceivables)
    TextView tvTodayReceivables;
    @InjectView(R.id.tv_toMothIncome)
    TextView tvToMothIncome;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;
    @InjectView(R.id.ll_ownership_institution)
    LinearLayout llOwnershipInstitution;
    @InjectView(R.id.tv_start_date)
    TextView tvStartDate;
    @InjectView(R.id.ll_start_date)
    LinearLayout llStartDate;
    @InjectView(R.id.tv_end_date)
    TextView tvEndDate;
    @InjectView(R.id.ll_end_date)
    LinearLayout llEndDate;
    @InjectView(R.id.ll_date)
    LinearLayout llDate;
    @InjectView(R.id.pieChart1)
    PieChart pieChart1;
    @InjectView(R.id.pieChart2)
    PieChart pieChart2;
    @InjectView(R.id.tv_fareSum)
    TextView tvFareSum;
    //    @InjectView(R.id.tv_yesterdayReceivables)
//    TextView tvYesterdayReceivables;
//    @InjectView(R.id.tv_todayShouldPay)
//    TextView tvTodayShouldPay;
    @InjectView(R.id.tv_todayAlreadyPay)
    TextView tvTodayAlreadyPay;
    @InjectView(R.id.tv_todayWaitPay)
    TextView tvTodayWaitPay;
    @InjectView(R.id.rb_day)
    RadioButton rbDay;
    @InjectView(R.id.rb_month)
    RadioButton rbMonth;
    @InjectView(R.id.segmented4)
    SegmentedGroup segmented4;
    @InjectView(R.id.tv_ownership_institution2)
    TextView tvOwnershipInstitution2;
    @InjectView(R.id.ll_ownership_institution2)
    LinearLayout llOwnershipInstitution2;
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
    @InjectView(R.id.combinedChart)
    CombinedChart combinedChart;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_toMothTotalSales)
    TextView tvToMothTotalSales;
    @InjectView(R.id.tv_receivables)
    TextView tvReceivables;
    @InjectView(R.id.tv_payment)
    TextView tvPayment;

    private MapData mapData;
    private MyPopWindow myPopWindow;
    private ListView lv_audit_status;  //弹出框审核状态列表
    private float xs;          //计算popwindow的位置
    private float ys;//计算popwindow的位置
    private String linOfficeId = "";  //临时记录 选中归属机构id  只有查询后生效
    private String startDate = "";
    private String endDate = "";
    private String startDate2 = "";
    private String endDate2 = "";
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private MyDatePickerDialog myDatePickerDialog2;  // 时间控件2
    private String dateChoiceTag = "date"; //O计-记录选择年或月或日的标识
    private boolean OUTLETS_TAG = false;//用来标识是不是代售点的身份

    private String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    private String institutionTag;
    private String officeId1, officeId2;
    private List<String> timeList = new ArrayList<>();
    private List<String> earningsList = new ArrayList<>();
    private List<String> ticketList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_statistics);
        ButterKnife.inject(this);
        initPieChart(pieChart1);
        initPieChart(pieChart2);
        initData();
    }


    private void initData() {

        if (myDatePickerDialog == null) {
            myDatePickerDialog = new MyDatePickerDialog();
        }

        if (myDatePickerDialog2 == null) {
            myDatePickerDialog2 = new MyDatePickerDialog();
        }

        segmented4.setTintColor(Color.rgb(103, 159, 233));


        segmented4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_day:
                        dateChoiceTag = "date";
                        startDate2 = "";
                        endDate2 = "";
                        tvStartDate2.setText(startDate2);
                        tvEndDate2.setText(endDate2);
                        getEarningsDetail();
                        break;
                    case R.id.rb_month:
                        dateChoiceTag = "month";
                        startDate2 = "";
                        endDate2 = "";
                        tvStartDate2.setText(startDate2);
                        tvEndDate2.setText(endDate2);
                        getEarningsDetail();
                        break;

                }
            }
        });


        if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
            OUTLETS_TAG = true;
        }

        getEarningsTop();
        getTicketStatistical();
        getEarningsDetail();
    }

    private void initPieChart(PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

//        pieChart1.setCenterTextTypeface(mTfLight);
//        pieChart1.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

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
    }




    private float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    @OnClick({R.id.iv_back, R.id.tv_ownership_institution, R.id.ll_start_date, R.id.ll_end_date,
            R.id.tv_ownership_institution2, R.id.ll_start_date2, R.id.ll_end_date2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_ownership_institution:
                institutionTag = "ticketStatistics";
                showOwnershipPop(llOwnershipInstitution, tvOwnershipInstitution);
                break;
            case R.id.ll_start_date:

                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {

                        startDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(IncomeStatisticsA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvStartDate.setText(date);
                        getTicketStatistical();

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
                                Toast.makeText(IncomeStatisticsA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvEndDate.setText(date);

                        getTicketStatistical();

                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.tv_ownership_institution2:
                institutionTag = "profitDetails";
                showOwnershipPop(llOwnershipInstitution2, tvOwnershipInstitution2);
                break;
            case R.id.ll_start_date2:

                Calendar cal = Calendar.getInstance();

                DatePickerDialog mDialog = new DatePickerDialog(IncomeStatisticsA.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = "";
                        String day = "";
                        if ((monthOfYear + 1) < 10){
                            month = "0" + (monthOfYear + 1);
                        }else {
                            month = "" + (monthOfYear + 1);
                        }
                        if (dayOfMonth < 10){
                            day = "0" + dayOfMonth;
                        }else {
                            day = "" + dayOfMonth;
                        }

                        startDate2 = year + "-" + month + "-" + day;
                        if ((!startDate2.isEmpty()) && (!endDate2.isEmpty())) {
                            if (Date.valueOf(startDate2).after(Date.valueOf(endDate2))) {
                                Toast.makeText(IncomeStatisticsA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvStartDate2.setText(startDate2);
                        getEarningsDetail();

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                mDialog.show();
                //隐藏不想显示的子控件，这里隐藏日控件
                DatePicker dp = findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());
                if (dp != null) {
                    if ("month".equals(dateChoiceTag)) {
                        ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.ll_end_date2:

                Calendar cal2 = Calendar.getInstance();

                DatePickerDialog mDialog2 = new DatePickerDialog(IncomeStatisticsA.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String month = "";
                        String day = "";
                        if ((monthOfYear + 1) < 10){
                            month = "0" + (monthOfYear + 1);
                        }else {
                            month = "" + (monthOfYear + 1);
                        }

                        if (dayOfMonth < 10){
                            day = "0" + dayOfMonth;
                        }else {
                            day = "" + dayOfMonth;
                        }
                        endDate2 = year + "-" + month + "-" + day;
                        if ((!startDate2.isEmpty()) && (!endDate2.isEmpty())) {
                            if (Date.valueOf(startDate2).after(Date.valueOf(endDate2))) {
                                Toast.makeText(IncomeStatisticsA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvEndDate2.setText(endDate2);
                        getEarningsDetail();

                    }
                }, cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));

                mDialog2.show();
                //隐藏不想显示的子控件，这里隐藏日控件
                DatePicker dp2 = findDatePicker((ViewGroup) mDialog2.getWindow().getDecorView());
                if (dp2 != null) {
                    if ("month".equals(dateChoiceTag)) {
                        ((ViewGroup) ((ViewGroup) dp2.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                    }
                }
                break;
        }
    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    /**
     * 弹出归属机构
     */
    private void showOwnershipPop(final LinearLayout llOwnInt, final TextView tvOwnInt) {
        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
//得到状态栏高度
//返回键关闭
        mapData = new MapData(IncomeStatisticsA.this, cpd, OUTLETS_TAG) {
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }

                myPopWindow = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                        mapData.setTrainOwnershipData(lv_audit_status);
                        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                        int[] arr = new int[2];
                        llOwnInt.getLocationOnScreen(arr);
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                        xs = arr[0] + llOwnInt.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + llOwnInt.getHeight();
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node officeId) {
                                linOfficeId = String.valueOf(officeId.getAllTreeNode().id);
                                if ("ticketStatistics".equals(institutionTag)){
                                    officeId1 = linOfficeId;
                                    getTicketStatistical();
                                }else if ("profitDetails".equals(institutionTag)){
                                    officeId2 = linOfficeId;
                                    getEarningsDetail();
                                }
                                tvOwnInt.setText(officeId.getAllTreeNode().name);
                                // if(officeId.getAllTreeNode().type.equals("2")) {
                                myPopWindow.dissmiss();
                                //}
                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop2;
                    }
                }.popupWindowBuilder(IncomeStatisticsA.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                myPopWindow.showAtLocation(llOwnInt, Gravity.NO_GRAVITY, (int) xs, (int) ys);
            }
        };
        //  mapData.setTrainOwnershipData();


    }

    /**
     * 通过遍历方法查找DatePicker里的子控件：年、月、日
     *
     * @param group
     * @return
     */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }


    /**
     * 获取收益统计数据
     */
    private void getEarningsTop(){

        String url = Define.URL + "fb/getEarningsTop";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "getEarningsTop", "0", true, false); //0 不代表什么，请求成功后不关闭加载
    }

    /**
     * 获取票款统计数据
     */
    private void getTicketStatistical(){

        String url = Define.URL + "fb/getTicketStatistical";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("officeId", officeId1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getTicketStatistical", "0", true, false); //0 不代表什么，请求成功后不关闭加载
    }

    /**
     * 获取收益明细数据
     */
    private void getEarningsDetail(){

        String url = Define.URL + "fb/getEarningsDetail";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("timeType", dateChoiceTag);
            jsonObject.put("beginDate", startDate2);
            jsonObject.put("endDate", endDate2);
            jsonObject.put("officeId", officeId2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getEarningsDetail", "0", true, false); //0 不代表什么，请求成功后不关闭加载
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "getEarningsTop":
                setEarningsTopData(json);
                break;

            case "getTicketStatistical":
                setTicketCountData(json);

                break;

            case "getEarningsDetail":
                setEarningsDetailData(json);
                break;
        }
    }


    private void setEarningsTopData(JSONObject jsonObject){
        Log.d("earningsTopData", jsonObject.toString());
        Gson gson = new Gson();
        ProfitCountTopBean profitCountTopBean = gson.fromJson(jsonObject.toString(), ProfitCountTopBean.class);
        ProfitCountTopBean.DataBean dataBean = profitCountTopBean.getData();
        tvDate.setText(dataBean.getDate() + "（" + dataBean.getWeekDay() + ")");
        tvTodayWaitPayment.setText(dataBean.getTodayWait() + "");
        tvTodayReceivables.setText(dataBean.getTodayReceivable());
        tvToMothIncome.setText(dataBean.getMonthEarnings() + "");
        tvToMothTotalSales.setText(dataBean.getMonthTicketAmount());
    }

    private void setTicketCountData(JSONObject jsonObject) {
        Log.d("ticketCountData", jsonObject.toString());
        Gson gson = new Gson();
        TicketCountBean ticketCountBean = gson.fromJson(jsonObject.toString(), TicketCountBean.class);
        TicketCountBean.DataBean dataBean = ticketCountBean.getData();

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<PieEntry> entries2 = new ArrayList<PieEntry>();
        TicketCountBean.DataBean.MapBean mapBean = dataBean.getMap();
//        if (startDate.isEmpty()) {
//            startDate = mapBean.getBeginDate();
//        }
//        if (endDate.isEmpty()) {
//            endDate = mapBean.getEndDate();
//        }
//        tvStartDate.setText(mapBean.getBeginDate());
//        tvEndDate.setText(mapBean.getEndDate());
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
       /* for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
                    mParties[i % mParties.length],
                    getResources().getDrawable(R.drawable.star)));
        }*/
        if ("0.00".equals(dataBean.getReceivable()) && "0.00".equals(dataBean.getPayable1())){//防止都是0的时候不显示饼图
            entries.add(new PieEntry(Float.parseFloat(dataBean.getReceivable()) + 1f, "收款"));
            entries.add(new PieEntry(Float.parseFloat(dataBean.getPayable1()) + 1f, "缴费"));
        }else {
            entries.add(new PieEntry(Float.parseFloat(dataBean.getReceivable()), "收款"));
            entries.add(new PieEntry(Float.parseFloat(dataBean.getPayable1()), "缴费"));
        }

        if ("0.00".equals(dataBean.getTodayPay()) && "0.00".equals(dataBean.getTodayWait())){
            entries2.add(new PieEntry(Float.parseFloat(dataBean.getTodayPay()) + 1f, "今日已缴"));
            entries2.add(new PieEntry(Float.parseFloat(dataBean.getTodayWait()) + 1f, "今日待缴"));
        }else {
            entries2.add(new PieEntry(Float.parseFloat(dataBean.getTodayPay()), "今日已缴"));
            entries2.add(new PieEntry(Float.parseFloat(dataBean.getTodayWait()), "今日待缴"));
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
        colors.add(ContextCompat.getColor(IncomeStatisticsA.this, R.color.yellow));
        colors.add(ContextCompat.getColor(IncomeStatisticsA.this, R.color.gbColor));
        dataSet.setColors(colors);

        ArrayList<Integer> colors2 = new ArrayList<Integer>();
        colors2.add(ContextCompat.getColor(IncomeStatisticsA.this, R.color.sgrenn));
        colors2.add(ContextCompat.getColor(IncomeStatisticsA.this, R.color.grayfont));
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

        tvFareSum.setText(dataBean.getTicketAmount());
        tvReceivables.setText(dataBean.getReceivable());
        tvPayment.setText(dataBean.getPayable() + "");
        tvTodayAlreadyPay.setText(dataBean.getTodayPay());
        tvTodayWaitPay.setText(dataBean.getTodayWait() + "");
    }


    private void setEarningsDetailData(JSONObject jsonObject) {
        timeList.clear();
        ticketList.clear();
        earningsList.clear();
        Log.d("earningsDetailData", jsonObject.toString());
        Gson gson = new Gson();
        EarningsDetailBean earningsDetailBean = gson.fromJson(jsonObject.toString(), EarningsDetailBean.class);
        EarningsDetailBean.DataBean dataBean = earningsDetailBean.getData();
        final List<String> time = dataBean.getTime();
        List<String> earnings = dataBean.getEarnings();
        List<String> ticket = dataBean.getTicket();

        timeList.addAll(time);
        ticketList.addAll(ticket);
        earningsList.addAll(earnings);
        EarningsDetailBean.DataBean.MapBean mapBean = dataBean.getMap();
//        if (startDate2.isEmpty()){//如果是第一次startDate没有值时给它赋值
//            startDate2 = mapBean.getBeginDate();
//        }
//
//        if (endDate2.isEmpty()){
//            endDate2 = mapBean.getEndDate();
//        }
//        tvStartDate2.setText(mapBean.getBeginDate());
//        tvEndDate2.setText(mapBean.getEndDate());


        combinedChart.setDrawBorders(true); // 显示边界
        combinedChart.getDescription().setEnabled(false);  // 不显示备注信息
        combinedChart.setPinchZoom(true); // 比例缩放
        combinedChart.animateY(1500);


        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        /*解决左右两端柱形图只显示一半的情况 只有使用CombinedChart时会出现，如果单独使用BarChart不会有这个问题*/
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(timeList.size() - 0.5f); //mMonths.length

        xAxis.setLabelCount(timeList.size()); // 设置X轴标签数量 mMonths.length
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴标签位置，BOTTOM在底部显示，TOP在顶部显示
        xAxis.setLabelRotationAngle(-60);//设置X轴的值倾斜60度显示



//        if (time.size() > 0) {
        xAxis.setValueFormatter(new IAxisValueFormatter() { // 转换要显示的标签文本，value值默认是int从0开始
            @Override
            public String getFormattedValue(float value, AxisBase axis) {  //
//                return mMonths[(int) value];
                int i = (int) value;
                if(timeList.size()>0 && i >=0){
                    try {
                        return timeList.get((int) value);
                    }catch (Exception ex){
                        return "";
                    }

                }
                return "";


            }
        });

//        }


        YAxis axisLeft = combinedChart.getAxisLeft(); // 获取左边Y轴操作类
        axisLeft.setAxisMinimum(0); // 设置最小值
        axisLeft.setGranularity(10); // 设置Label间隔
        axisLeft.setLabelCount(10);// 设置标签数量
//        axisLeft.setValueFormatter(new IAxisValueFormatter() { // 在左边Y轴标签文本后加上%号
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return value + "%";
//            }
//        });

        YAxis axisRight = combinedChart.getAxisRight(); // 获取右边Y轴操作类
        axisRight.setDrawGridLines(false); // 不绘制背景线，上面左边Y轴并没有设置此属性，因为不设置默认是显示的
        axisRight.setGranularity(10); // 设置Label间隔
        axisRight.setAxisMinimum(0); // 设置最小值
        axisRight.setLabelCount(10); // 设置标签个数
//        axisRight.setValueFormatter(new IAxisValueFormatter() { // 在右边Y轴标签文本后加上%号
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return value + "%";
//            }
//        });

        /**
         * 初始化柱形图的数据
         * 此处用suppliers的数量做循环，因为总共所需要的数据源数量应该和标签个数一致
         * 其中BarEntry是柱形图数据源的实体类，包装xy坐标数据
         */
        /******************BarData start********************/
        List<BarEntry> barEntries = new ArrayList<>();
        if (timeList.size() > 0) {
            for (int i = 0; i < timeList.size(); i++) {// mMonths.length
                barEntries.add(new BarEntry(i, Float.parseFloat(earningsList.get(i))));
            }
        }else {
            barEntries.add(new BarEntry(0, 0));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "收益");  // 新建一组柱形图，"LAR"为本组柱形图的Label
        barDataSet.setColor(Color.parseColor("#0288d1")); // 设置柱形图颜色
        barDataSet.setValueTextColor(Color.parseColor("#0288d1")); //  设置柱形图顶部文本颜色

        BarData barData = new BarData();

        barData.addDataSet(barDataSet);// 添加一组柱形图，如果有多组柱形图数据，则可以多次addDataSet来设置
        /******************BarData end********************/

        /**
         * 初始化折线图数据
         * 说明同上
         */
        /******************LineData start********************/
        List<Entry> lineEntries = new ArrayList<>();
        if (timeList.size() > 0) {
            for (int i = 0; i < timeList.size(); i++) {
                lineEntries.add(new Entry(i, Float.parseFloat(ticketList.get(i))));// mMonths.length
            }
        }else {
            lineEntries.add(new Entry(0, 0));// mMonths.length
        }
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "票数");
        lineDataSet.setColor(Color.parseColor("#4D90E7"));
        lineDataSet.setCircleColor(Color.parseColor("#4D90E7"));
        lineDataSet.setValueTextColor(Color.parseColor("#4D90E7"));
        lineDataSet.setLineWidth(3f);
        lineDataSet.setHighlightEnabled(false);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        /******************LineData end********************/

        CombinedData combinedData = new CombinedData(); // 创建组合图的数据源
        combinedData.setData(barData);  // 添加柱形图数据源
        combinedData.setData(lineData); // 添加折线图数据源

        if (timeList.size() > 0) {
            combinedChart.setData(combinedData); // 为组合图设置数据源
        }


    }
}

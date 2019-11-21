package com.huoniao.oc.fragment.train_station;

import android.graphics.DashPathEffect;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.huoniao.oc.adapter.SimpleTreeListViewAdapter4;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.bean.LineChartDataBean;
import com.huoniao.oc.bean.RankingBean;
import com.huoniao.oc.bean.TodayPaymentImportBean;
import com.huoniao.oc.bean.TodayPaymentUnImportBean;
import com.huoniao.oc.bean.TreeBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyOnChartGestureListener;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.NewMarkerView;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.common.tree.adapter.TreeListViewAdapter;
import com.huoniao.oc.custom.MyLineChart;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.trainstation.TotalIronImportDeailsA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.NumberFormatUtils;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

import static com.huoniao.oc.MyApplication.treeIdList2;
import static com.huoniao.oc.R.id.tv_paymentAmount;
import static com.huoniao.oc.util.NumberFormatUtils.formatDigits;

/**
 * Created by Administrator on 2019/1/23.
 */

public class AdministrationPaymentF extends BaseFragment implements OnChartValueSelectedListener {


    @InjectView(R.id.ll_todayPayment)
    LinearLayout llTodayPayment;
    @InjectView(R.id.tv_alreadyImport)
    TextView tvAlreadyImport;
    @InjectView(R.id.tv_noImport)
    TextView tvNoImport;
    @InjectView(R.id.tv_importNumberTitle)
    TextView tvImportNumberTitle;
    @InjectView(R.id.tv_importNumber)
    TextView tvImportNumber;
    @InjectView(R.id.tv_importAmount)
    TextView tvImportAmount;
    @InjectView(R.id.ll_importAmount)
    LinearLayout llImportAmount;
    @InjectView(R.id.ll_alreadyImportType)
    LinearLayout llAlreadyImportType;
    @InjectView(R.id.ll_noImportType)
    LinearLayout llNoImportType;
    @InjectView(R.id.todayPaymentListView)
    MyListView todayPaymentListView;
    @InjectView(R.id.tv_previousPage)
    TextView tvPreviousPage;
    @InjectView(R.id.tv_nextPage)
    TextView tvNextPage;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;
    @InjectView(R.id.ll_cumulativePayment)
    LinearLayout llCumulativePayment;
    //    @InjectView(R.id.tv_alreadyImportCumulative)
//    TextView tvAlreadyImportCumulative;
//    @InjectView(R.id.tv_noImportCumulative)
//    TextView tvNoImportCumulative;
    @InjectView(R.id.tv_paymentNumber)
    TextView tvPaymentNumber;
    @InjectView(tv_paymentAmount)
    TextView tvPaymentAmount;
    @InjectView(R.id.rb_day)
    RadioButton rbDay;
    @InjectView(R.id.rb_month)
    RadioButton rbMonth;
    @InjectView(R.id.rb_year)
    RadioButton rbYear;
    @InjectView(R.id.segmented1)
    SegmentedGroup segmented1;
    @InjectView(R.id.lc_admin_lineChart)
    MyLineChart lcAdminLineChart;
    @InjectView(R.id.ll_top10)
    LinearLayout llTop10;
    @InjectView(R.id.tv_passengerTransport)
    TextView tvPassengerTransport;
    @InjectView(R.id.tv_freightTransport)
    TextView tvFreightTransport;
    @InjectView(R.id.rb_topDay)
    RadioButton rbTopDay;
    @InjectView(R.id.rb_topMonth)
    RadioButton rbTopMonth;
    @InjectView(R.id.rb_topYear)
    RadioButton rbTopYear;
    @InjectView(R.id.segmented2)
    SegmentedGroup segmented2;
    @InjectView(R.id.achievementListView)
    MyListView achievementListView;
    @InjectView(R.id.scroll_view)
    ScrollView scrollView;
//    @InjectView(R.id.myswipeRefresh)
//    MySwipeRefreshLayout myswipeRefresh;
    @InjectView(R.id.tv_linechart_explain)
    TextView tvLinechartExplain;
    @InjectView(R.id.tv_retract)
    TextView tvRetract;
    @InjectView(R.id.ll_inConstruction)
    LinearLayout llInConstruction;
    @InjectView(R.id.layout_title)
    LinearLayout layoutTitle;
    @InjectView(R.id.tv_admin_amount_paid)
    TextView tv_admin_amount_paid;
    @InjectView(R.id.tv_admin_consolidated_record)
    TextView tv_admin_consolidated_record;
    @InjectView(R.id.tv_average_value)
    TextView tv_average_value;
//    @InjectView(R.id.lv_train_ownership)
//    ListView lv_train_ownership;
    private MyPopWindow myPopWindow;
    private MapData mapData;
    private float xs;
    private float ys;
    private String officeIdStr = "";  //临时的归属机构id
    private String officeNodeName = "";
    private String todayPaymentTag = "alreadyImport";//今日汇缴tab点击标识，默认已导入
    private String achievementTopTag = "passengerTransport";//业绩排行tab点击标识，默认客运
    private List<TodayPaymentImportBean.DataBean.ListBean> todayPaymentImportList = new ArrayList<>();
    private List<TodayPaymentUnImportBean.DataBean.ListBean> todayPaymentUnImportList = new ArrayList<>();
    private CommonAdapter<TodayPaymentImportBean.DataBean.ListBean> importAdpter;
    private CommonAdapter<TodayPaymentUnImportBean.DataBean.ListBean> unImportAdpter;
    private String next = "";        //返回来的下一页
    private String prev = ""; //上一页
    private User user;
    private String selfOfficeId;
    private String timeType = "week";//选择周月年标识，默认周
    private String timeType2 = "week";//选择周月年标识，默认周

    private SimpleTreeListViewAdapter4 simpleTreeListViewAdapter4 = null ;
    private String defaultId;
    private int positionnew;
    private List<AllTreeNode> allTreelist = new ArrayList<>();
    private String itemid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adminstation_view_payment_f, null);
        ButterKnife.inject(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();
    }

    private void initData() {
        user = (User) ObjectSaveUtil.readObject(MyApplication.mContext, "loginResult");
        selfOfficeId = user.getOfficeId();
        requestTodayPaymentImport(false, "1");
    }

    private void initWidget() {
       /* myswipeRefresh.setColorScheme(colors);
        myswipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myswipeRefresh.isRefreshing()) {
                    tvImportNumberTitle.setText("导入数量：");
                    llImportAmount.setVisibility(View.VISIBLE);
                    llAlreadyImportType.setVisibility(View.VISIBLE);
                    llNoImportType.setVisibility(View.GONE);
                    setViewStyle(tvAlreadyImport, tvNoImport);
                    rbDay.setChecked(true);
                    rbTopDay.setChecked(true);
                    requestTodayPaymentImport(false, "1");
                    cpd.dismiss();
                }


            }
        });
*/

        segmented1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_day:
                        timeType2 = "week";
                        setLineChartDataTo(weekDateList, weekValue, "1");
                        tvPaymentAmount.setText(weekShouldAmountTotal);
                        tv_average_value.setText(weekShouldAmountAvg);
                        tvLinechartExplain.setText(monthFormat.isEmpty() ? "---" : monthFormat + "月本周累计汇缴数据表");
                        break;

                    case R.id.rb_month:
                        timeType2 = "month";
                        setLineChartDataTo(monthDateList, monthValueList, "1");
                        tvPaymentAmount.setText(monthShouldAmountTotal);
                        tv_average_value.setText(monthShouldAmountAvg);
                        tvLinechartExplain.setText(monthFormat.isEmpty() ? "---" : monthFormat + "月本月累计汇缴数据表");

                        break;

                    case R.id.rb_year:
                        timeType2 = "year";
                        setLineChartDataTo(yearDataList, yearValueList, "2");
                        tvPaymentAmount.setText(yearShouldAmountTotal);
                        tv_average_value.setText(yearShouldAmountAvg);
                        tvLinechartExplain.setText(yearFormat.isEmpty() ? "---" : yearFormat + "年本年累计汇缴数据表");
                        break;
                }
            }
        });


        segmented2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_topDay:
                        timeType = "week";
                        treeIdList2.clear(); //清空归属机构记录的节点
                        achievementListView.setAdapter(null);
                        if ("passengerTransport".equals(achievementTopTag)) {
                            requestPerformanceRanking(true);
                        } else if ("freightTransport".equals(achievementTopTag)) {

                        }
                        break;

                    case R.id.rb_topMonth:
                        timeType = "month";
                        treeIdList2.clear(); //清空归属机构记录的节点
                        achievementListView.setAdapter(null);
                        if ("passengerTransport".equals(achievementTopTag)) {
                            requestPerformanceRanking(true);
                        } else if ("freightTransport".equals(achievementTopTag)) {

                        }
                        break;

                    case R.id.rb_topYear:
                        timeType = "year";
                        treeIdList2.clear(); //清空归属机构记录的节点
                        achievementListView.setAdapter(null);
                        if ("passengerTransport".equals(achievementTopTag)) {
                            requestPerformanceRanking(true);
                        } else if ("freightTransport".equals(achievementTopTag)) {

                        }
                        break;
                }
            }
        });

        lcAdminLineChart.setFocusable(false);
        achievementListView.setFocusable(false);
//        lv_train_ownership.setFocusable(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.ll_todayPayment, R.id.tv_alreadyImport, R.id.tv_noImport, R.id.tv_previousPage, R.id.tv_nextPage, R.id.tv_ownership_institution,
            R.id.tv_passengerTransport, R.id.tv_freightTransport, R.id.tv_retract})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_todayPayment:
                startActivityMethod(TotalIronImportDeailsA.class);
                break;

            case R.id.tv_alreadyImport:
                todayPaymentTag = "alreadyImport";
                tvImportNumberTitle.setText("导入数量：");
                llImportAmount.setVisibility(View.VISIBLE);
                llAlreadyImportType.setVisibility(View.VISIBLE);
                llNoImportType.setVisibility(View.GONE);
                setViewStyle(tvAlreadyImport, tvNoImport);
                requestTodayPaymentImport(true, "1");

                break;
            case R.id.tv_noImport:
                todayPaymentTag = "noImport";
                tvImportNumberTitle.setText("未导入数量：");
                llImportAmount.setVisibility(View.GONE);
                llAlreadyImportType.setVisibility(View.GONE);
                llNoImportType.setVisibility(View.VISIBLE);
                setViewStyle(tvAlreadyImport, tvNoImport);
                requestTodayPaymentUnImport(true, "1");

                break;
            case R.id.tv_previousPage:
                if ("-1".equals(prev)) {
                    ToastUtils.showToast(MyApplication.mContext, "已经是第一页了！");
                    return;
                }
                if ("alreadyImport".equals(todayPaymentTag)) {
                    requestTodayPaymentImport(true, prev);
                } else if ("noImport".equals(todayPaymentTag)) {
                    requestTodayPaymentUnImport(true, prev);
                }

                break;
            case R.id.tv_nextPage:
                if ("-1".equals(next)) {
                    ToastUtils.showToast(MyApplication.mContext, "已经是最后一页了！");
                    return;
                }
                if ("alreadyImport".equals(todayPaymentTag)) {
                    requestTodayPaymentImport(true, next);
                } else if ("noImport".equals(todayPaymentTag)) {
                    requestTodayPaymentUnImport(true, next);
                }
                break;
            case R.id.tv_ownership_institution:
                if (RepeatClickUtils.repeatClick()) {
                    showOwnershipPop();
                }
                break;


            case R.id.tv_passengerTransport:
                achievementTopTag = "passengerTransport";
                setViewStyle2(tvPassengerTransport, tvFreightTransport);
                layoutTitle.setVisibility(View.VISIBLE);
                segmented2.setVisibility(View.VISIBLE);
                requestPerformanceRanking(true);
                llInConstruction.setVisibility(View.GONE);
                achievementListView.setVisibility(View.VISIBLE);
//                lv_train_ownership.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_freightTransport:
                achievementTopTag = "freightTransport";
                setViewStyle2(tvPassengerTransport, tvFreightTransport);
                layoutTitle.setVisibility(View.GONE);
                segmented2.setVisibility(View.GONE);
                llInConstruction.setVisibility(View.VISIBLE);
                achievementListView.setVisibility(View.GONE);
//                lv_train_ownership.setVisibility(View.GONE);
                break;
            case R.id.tv_retract:

                break;

        }
    }


    /**
     * 获取归属机构数据 弹出归属机构
     */
    private void showOwnershipPop() {
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

                                setRequestlineChartData(true);
                                rbDay.setChecked(true);
                                rbMonth.setChecked(false);
                                rbYear.setChecked(false);

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
    }


   /* //关闭刷新动画
    public void stopRefreshing() {
        //刷新完成关闭动画
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myswipeRefresh != null) {
                    myswipeRefresh.setRefreshing(false);
                }
            }
        });
    }*/


    @Override
    protected void closeDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.closeDismiss(cpd, tag, controlOff);
        switch (tag) {
            case "performanceRanking":
                cpd.dismiss();  //最后一个直接关闭
//                stopRefreshing();
                break;
        }
    }

    /**
     * 网络错误加载下一个请求
     *
     * @param cpd
     * @param tag
     * @param controlOff
     */
    @Override
    protected void loadControlExceptionDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.loadControlExceptionDismiss(cpd, tag, controlOff);
        switch (tag) {
            case "todayPaymentImport":
                if (!controlOff) {
                    setRequestlineChartData(false);//折线图
                }
                break;
            case "getTotalList":
                if (!controlOff) {
                    requestPerformanceRanking(true);
                }

                break;

        }

//        stopRefreshing();
    }


    /**
     * 请求铁总汇缴页-今日汇缴-已导入
     *
     * @param off 控制关闭加载框
     */
    private void requestTodayPaymentImport(boolean off, String pageNo) {
        String url = Define.URL + "fb/todayPaymentImport";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNo", pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "todayPaymentImport", pageNo, off, true); //1没有实际意义值
    }


    /**
     * 请求铁总汇缴页-今日汇缴-未导入
     *
     * @param off 控制关闭加载框
     */
    private void requestTodayPaymentUnImport(boolean off, String pageNo) {
        String url = Define.URL + "fb/todayPaymentUnImport";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNo", pageNo);
//            jsonObject.put("pageSize", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "todayPaymentUnImport", pageNo, off, true); //1没有实际意义值
    }


    /**
     * 请求折线图数据
     */
    private void setRequestlineChartData(boolean off) {
        String url = Define.URL + "fb/getTotalList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId", officeIdStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getTotalList", "1", off, false); //1没有实际意义值

    }

    /**
     * 请求业绩排行top10
     */
    private void requestPerformanceRanking(boolean off) {
        String url = Define.URL + "fb/performanceRanking";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId", selfOfficeId);
            jsonObject.put("timeType", timeType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "performanceRanking", "1", off, false); //1没有实际意义值

    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag) {
            case "todayPaymentImport"://铁总首页-今日汇缴-已导入
                Log.d("todayPaymentImportData", json.toString());
                setImportAdapter(json, pageNumber);
                if (!controlOff) {
                    setRequestlineChartData(false);
                }
                break;

            case "todayPaymentUnImport":
                //铁总首页-今日汇缴-未导入
                Log.d("unImportData", json.toString());
                setUnImportAdapter(json, pageNumber);
                break;

            case "getTotalList":   // 系统管理员 汇缴页面 获取折线图数据
                sqlitLineChartData(json);
                if (!controlOff) {
                    requestPerformanceRanking(true);
                }
                break;

            case "performanceRanking":
                Log.d("rankingData", json.toString());
                setRankingAdapter(json);
                break;

            case "nextPerformanceRanking":
                Log.d("rankingData", "next---"+json.toString());
                setNextRankingAdapter(json);
                break;

        }
    }
    private List<RankingBean.DataBean> allRankingDataList = new ArrayList<>();
    List<AllTreeNode> allTreeNodesList = new ArrayList<>();
    private List<TreeBean> trainOwnershipList;
    List<RankingBean.DataBean> rankingDataList;

    private void setRankingAdapter(JSONObject jsonObject) {
        allTreeNodesList.clear();
        Gson gson = new Gson();
        RankingBean rankingBean = gson.fromJson(jsonObject.toString(), RankingBean.class);
        rankingDataList = rankingBean.getData();

        if (rankingDataList != null && rankingDataList.size() > 0) {
            for (int i = 0; i < rankingDataList.size(); i++) {
                AllTreeNode allTree = new AllTreeNode();

                allTree.id = rankingBean.getData().get(i).getStandby1();
                allTree.parentId = rankingBean.getData().get(i).getStandby2();
                allTree.officeName = rankingBean.getData().get(i).getString1();
                allTree.salesAmount = rankingBean.getData().get(i).getSumString();
                allTree.ranking = rankingBean.getData().get(i).getStandby3();
                allTree.char1 = rankingBean.getData().get(i).getChar1();
                allTreeNodesList.add(allTree);
            }
        }
        trainOwnershipList = new ArrayList<>();
        trainOwnershipList.clear();

        if (allTreeNodesList != null && allTreeNodesList.size() > 0) {


            for (int i = 0; i < allTreeNodesList.size(); i++) {
                AllTreeNode allTreeNode = allTreeNodesList.get(i);
                if (!"3".equals(allTreeNode.char1)) {
                    allTreeNode.parentId = "2324323232131312a"; //顶级节点随便取
                    long id = 1;
                    if (allTreeNode.id.equals("1")) {
                        id = Long.parseLong(allTreeNode.id);
                    } else {
                        id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
                    }

                    long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
                    String name = allTreeNode.officeName;
                    trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));

                } else {
                    if (!"3".equals(allTreeNode.char1)) {
                        long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
                        String name = allTreeNode.officeName;
                        long pid = Long.parseLong(allTreeNode.parentId);
                        trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
                    } else {
                        if (allTreeNode.id.length() > 16 && allTreeNode.parentId.length() > 16) {
                            long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
                            long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
                            String name = allTreeNode.officeName;
                            trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
                        }
                    }
                    Log.e("yyyyy", "id>>>>>>>" + allTreeNode.id + "--------parentId>>>>>" + allTreeNode.parentId + "\n");
                }
            }
            //adapter展示
            try {
                simpleTreeListViewAdapter4 = new SimpleTreeListViewAdapter4<TreeBean>(achievementListView, MyApplication.mContext,
                        trainOwnershipList, 0);  //1表示只默认只展开
                achievementListView.setAdapter(simpleTreeListViewAdapter4);
                simpleTreeListViewAdapter4.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                    @Override
                    public void onClick(Node node, int position) {
                         itemid = node.getAllTreeNode().id;
                        //清空数据
                        allTreelist.clear();
//                        ToastUtils.showToast(getContext()  ,"ID+"+node.getAllTreeNode().id +"下标+"+position+node.getName());
                        //获取点击条目的ID
                        positionnew = position;

                        if (node.isLeaf()) {
                      /*  Toast.makeText(MyApplication.mContext, node.getAllTreeNode().name,
                                Toast.LENGTH_SHORT).show();*/
                        }
                        if (!"3".equals(node.getAllTreeNode().char1)) {  //处理节点选择器
                            simpleTreeListViewAdapter4.setSelectedItem(position, Long.parseLong(node.getAllTreeNode().id.substring(0,15),16));
                            simpleTreeListViewAdapter4.notifyDataSetChanged();
                        }
                        //判断char1不等于3，就调取接口
                        if(!"3".equals(node.getAllTreeNode().char1)) { //如果char1为3不调取接口
                            getNextLevel(node.getAllTreeNode().id); //获取点击之后动态加载tree节点数据
                        }
                    }
                });
                simpleTreeListViewAdapter4.notifyDataSetChanged();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    //点击item后再次调取接口返回子节点数据
    private void setNextRankingAdapter(JSONObject jsonObject) {

        Gson gson = new Gson();
        RankingBean rankingBean = gson.fromJson(jsonObject.toString(), RankingBean.class);
        List<RankingBean.DataBean> rankingDataList = rankingBean.getData();

        if (rankingDataList != null && rankingDataList.size() > 0) {
            for (int i = 0; i < rankingDataList.size(); i++) {
                AllTreeNode allTree = new AllTreeNode();

                allTree.id = rankingBean.getData().get(i).getStandby1();
                allTree.parentId = rankingBean.getData().get(i).getStandby2();
                allTree.officeName = rankingBean.getData().get(i).getString1();
                allTree.salesAmount = rankingBean.getData().get(i).getSumString();
                allTree.ranking = rankingBean.getData().get(i).getStandby3();
                allTree.char1 = rankingBean.getData().get(i).getChar1();
                allTreelist.add(allTree);
            }
            //给被点击的item设置唯一标识 ，判断展开/关闭
            if(!treeIdList2.contains(itemid)) {
                Collections.reverse(allTreelist);//倒序集合数据
                simpleTreeListViewAdapter4.addExtraNode( positionnew ,allTreelist);  //动态增加火车站层级树节点，
                allTreeNodesList.addAll(allTreelist);
                treeIdList2.add(itemid);
            }
        }

    }

//    private void setNextRankingAdapter(JSONObject jsonObject) {
//        Gson gson = new Gson();
//        RankingBean rankingBean = gson.fromJson(jsonObject.toString(), RankingBean.class);
//        List<RankingBean.DataBean> rankingDataList = rankingBean.getData();
//
//        if (rankingDataList != null && rankingDataList.size() > 0) {
//            String defaultId = rankingDataList.get(0).getStandby1() == null ? "" : rankingDataList.get(0).getStandby1(); //默认请求
//            for (int i = 0; i < rankingDataList.size(); i++) {
//                AllTreeNode allTree = new AllTreeNode();
//
//                allTree.id = rankingBean.getData().get(i).getStandby1();
//                allTree.parentId = rankingBean.getData().get(i).getStandby2();
//                allTree.officeName = rankingBean.getData().get(i).getString1();
//                allTree.salesAmount = rankingBean.getData().get(i).getSumString();
//                allTree.ranking = rankingBean.getData().get(i).getStandby3();
//                allTree.char1 = rankingBean.getData().get(i).getChar1();
//                Log.i("char1" , "next" +rankingBean.getData().get(i).getChar1());
//                allTreeNodesList.add(allTree);
//            }
//
//            getNextLevel(defaultId);
//        }
////        trainOwnershipList = new ArrayList<>();
////        trainOwnershipList.clear();
////
////        if (allTreeNodesList != null && allTreeNodesList.size() > 0) {
////
////
////            for (int i = 0; i < allTreeNodesList.size(); i++) {
////                AllTreeNode allTreeNode = allTreeNodesList.get(i);
////                if (allTreeNode.id.equals("1") && allTreeNode.parentId.isEmpty()) {
////                    allTreeNode.parentId = "2324323232131312a"; //顶级节点随便取
////                    long id = 1;
////                    if (allTreeNode.id.equals("1")) {
////                        id = Long.parseLong(allTreeNode.id);
////                    } else {
////                        id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
////                    }
////                    //  allTreeNode.id = "a566565656565656566";
////
////                    long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
////                    String name = allTreeNode.officeName;
////                    trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
////                    if (!treeIdList2.contains(allTreeNode.id)) {  // 层级树最顶层层id
////                        treeIdList2.add(allTreeNode.id);  //记录火车站层级树归属机构 已经通过id搜索过得节点
////                    }
////
////                } else {
////                    if (allTreeNode.parentId.equals("1")) {
////                        //   allTreeNode.parentId = "a566565656565656566"; //顶级节点随便取
////                        long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
////                        // long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
////                        String name = allTreeNode.officeName;
////                        long pid = Long.parseLong(allTreeNode.parentId);
////                        trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
////                    } else {
////                        if (allTreeNode.id.length() > 16 && allTreeNode.parentId.length() > 16) {
////                            long id = Long.parseLong(allTreeNode.id.substring(0, 15), 16);
////                            long pid = Long.parseLong(allTreeNode.parentId.substring(0, 15), 16);
////                            String name = allTreeNode.officeName;
////                            trainOwnershipList.add(new TreeBean(id, pid, name, allTreeNode));
////                        }
////                    }
////                    Log.e("yyyyy", "id>>>>>>>" + allTreeNode.id + "--------parentId>>>>>" + allTreeNode.parentId + "\n");
////                }
////            }
////
////            try {
////                simpleTreeListViewAdapter4 = new SimpleTreeListViewAdapter4<TreeBean>(achievementListView, MyApplication.mContext,
////                        trainOwnershipList, 0);  //1表示只默认只展开
////                achievementListView.setAdapter(simpleTreeListViewAdapter4);
////                simpleTreeListViewAdapter4.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
////                    @Override
////                    public void onClick(Node node, int position) {
////                        ToastUtils.showToast(getContext()  , position+"下标"+node.getName());
////                    }
////                });
////                simpleTreeListViewAdapter4.notifyDataSetChanged();
////                //	simpleTreeListViewAdapter3.expandOrCollapseA();
////            } catch (IllegalAccessException e) {
////                e.printStackTrace();
////            }
////
////        }
//    }

    private void getNextLevel(String officeId ) {
        String url = Define.URL + "fb/performanceRanking";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId", officeId);
            jsonObject.put("timeType", timeType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "nextPerformanceRanking", "1", true, false); //1没有实际意义值
    }

    private void setUnImportAdapter(JSONObject jsonObject, String pageNo) {
        Gson gson = new Gson();
        TodayPaymentUnImportBean toDayPaymentUnImportBean = gson.fromJson(jsonObject.toString(), TodayPaymentUnImportBean.class);
        if (pageNo.equals("1")) {
            TodayPaymentUnImportBean.DataBean.StatisticalBean statisticalBean = toDayPaymentUnImportBean.getData().getStatistical();
            tvImportNumber.setText(statisticalBean.getUnImportCount() + "");


//            //集成清空处理
//            todayPaymentImportList.clear();
//            if (importAdpter != null) {
//                todayPaymentListView.setAdapter(importAdpter);
//            }
        }
        next = String.valueOf(toDayPaymentUnImportBean.getNext());
        prev = String.valueOf(toDayPaymentUnImportBean.getPrev());
        todayPaymentUnImportList = toDayPaymentUnImportBean.getData().getList();
//        if (todayPaymentImportList.size() > 0){

//            if (unImportAdpter == null) {

        unImportAdpter = new CommonAdapter<TodayPaymentUnImportBean.DataBean.ListBean>(MyApplication.mContext, todayPaymentUnImportList, R.layout.item_todaypayment_unimport) {
            @Override
            public void convert(ViewHolder holder, TodayPaymentUnImportBean.DataBean.ListBean listBean) {
                double sum1D = Double.valueOf(listBean.getSum1String());
                int sum1Int = getInt(sum1D);
                holder.setText(R.id.tv_serialNumber, listBean.getInteger1() + "")
                        .setText(R.id.tv_parentOrganization, listBean.getString1())
                        .setText(R.id.tv_orgName, listBean.getStandby2());

            }
        };


        todayPaymentListView.setAdapter(unImportAdpter);
//            }
//        }else {
//                unImportAdpter.notifyDataSetChanged();
//            }


    }

    private void setImportAdapter(JSONObject jsonObject, String pageNo) {
        Gson gson = new Gson();
        TodayPaymentImportBean toDayPaymentImportBean = gson.fromJson(jsonObject.toString(), TodayPaymentImportBean.class);
        if (pageNo.equals("1")) {
            TodayPaymentImportBean.DataBean.StatisticalBean statisticalBean = toDayPaymentImportBean.getData().getStatistical();
            if (statisticalBean.getImportCount() != null) {
                tvImportNumber.setText(statisticalBean.getImportCount());
            }

            if (statisticalBean.getImportAmount() != null) {
                tvImportAmount.setText(statisticalBean.getImportAmount());
            }
//            //集成清空处理
//            todayPaymentImportList.clear();
//            if (importAdpter != null) {
//                todayPaymentListView.setAdapter(importAdpter);
//            }
        }
        next = String.valueOf(toDayPaymentImportBean.getNext());
        prev = String.valueOf(toDayPaymentImportBean.getPrev());
        todayPaymentImportList = toDayPaymentImportBean.getData().getList();
//        if (todayPaymentImportList.size() > 0){

//            if (importAdpter == null) {

        importAdpter = new CommonAdapter<TodayPaymentImportBean.DataBean.ListBean>(MyApplication.mContext, todayPaymentImportList, R.layout.item_todaypayment) {
            @Override
            public void convert(ViewHolder holder, TodayPaymentImportBean.DataBean.ListBean listBean) {
                double sum1D = Double.valueOf(listBean.getSum1String());
                int sum1Int = getInt(sum1D);
                holder.setText(R.id.tv_stationName, listBean.getString1())
                        .setText(R.id.tv_salesVolume, sum1Int + "")
                        .setText(R.id.tv_salesAmount, listBean.getSumString());

            }
        };


        todayPaymentListView.setAdapter(importAdpter);
        todayPaymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }


    /**
     * double转int四舍五入
     *
     * @param number
     * @return
     */
    private int getInt(double number) {
        BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }


    private void setViewStyle(TextView textOne, TextView textTwo) {

        if ("alreadyImport".equals(todayPaymentTag)) {
            textOne.setTextColor(getResources().getColor(R.color.blues));
            textOne.setBackgroundResource(R.drawable.shape_editext_white3);
            textTwo.setTextColor(getResources().getColor(R.color.white));
            textTwo.setBackgroundResource(R.drawable.shape_editext_white4);
        } else if ("noImport".equals(todayPaymentTag)) {
            textOne.setTextColor(getResources().getColor(R.color.white));
            textOne.setBackgroundResource(R.drawable.shape_editext_white4);
            textTwo.setTextColor(getResources().getColor(R.color.blues));
            textTwo.setBackgroundResource(R.drawable.shape_editext_white3);

        }


    }


    private void setViewStyle2(TextView textOne, TextView textTwo) {


        if ("passengerTransport".equals(achievementTopTag)) {
            textOne.setTextColor(getResources().getColor(R.color.blues));
            textOne.setBackgroundResource(R.drawable.shape_editext_white3);
            textTwo.setTextColor(getResources().getColor(R.color.white));
            textTwo.setBackgroundResource(R.drawable.shape_editext_white4);
        } else if ("freightTransport".equals(achievementTopTag)) {
            textOne.setTextColor(getResources().getColor(R.color.white));
            textOne.setBackgroundResource(R.drawable.shape_editext_white4);
            textTwo.setTextColor(getResources().getColor(R.color.blues));
            textTwo.setBackgroundResource(R.drawable.shape_editext_white3);
        }

    }

    SimpleDateFormat fmt2 = new SimpleDateFormat("MM/dd");
    Date date2;

    /**
     * @param dateListName x轴上的数据
     * @param valueList    折线图y轴上的数据
     * @param tag          //标记 是否年月日进来    日月是1 年是2
     */
    private void setLineChartDataTo(final List<String> dateListName, final List<Double> valueList, final String tag) {
        lcAdminLineChart.clear();
        // lcAdminLineChart.requestLayout();
        //刷新
        lcAdminLineChart.invalidate();
        if (dateListName.size() <= 0 || valueList.size() <= 0) {
            return;
        }
        //设置手势滑动事件
        lcAdminLineChart.setOnChartGestureListener(new MyOnChartGestureListener() {
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
        lcAdminLineChart.setExtraOffsets(5, 0, 40, 0); //设置偏移量
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
                    if (tag.equals("1")) {
                        date2 = Date.valueOf(dateListName.get((int) value));
                        return String.valueOf(fmt2.format(date2));
                    } else if (tag.equals("2")) {
                        return String.valueOf(dateListName.get((int) value));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value + "";
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

            values.add(new Entry(i, Float.parseFloat(formatDigits(valueList.get(i)))));
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

                if (!TextUtils.isEmpty(value)) {
                    markerView.getTvContent().setVisibility(View.VISIBLE);
          /*          float yValues = values.get(index).getY();
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    String yValuesStr = decimalFormat.format(yValues);//format 返回的是字符串*/

                    String yValuesStr = NumberFormatUtils.formatDigits(valueList.get(index));
//					markerView.getTvContent().setText(dateListName.get(index)+ "\n"+values.get(index).getY()+"元");
                    markerView.getTvContent().setText(dateListName.get(index) + "\n" + yValuesStr + "元");
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
                    return "" + arg0;//不显示值
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


    private List<List<LineChartDataBean.DataBean.WeekBean>> weekLine = new ArrayList<>(); //周集合
    private List<String> weekDateList = new ArrayList<>(); //周的天数集合
    private List<Double> weekValue = new ArrayList<>(); //周的天数对应的值
    private List<List<LineChartDataBean.DataBean.MonthBean>> monthLine = new ArrayList<>(); //月集合
    private List<String> monthDateList = new ArrayList<>(); //月的天数集合
    private List<Double> monthValueList = new ArrayList<>(); //月对应的值
    private List<List<LineChartDataBean.DataBean.YearBean>> yearLine = new ArrayList<>(); // 年集合
    private List<String> yearDataList = new ArrayList<>(); //年对应的天数集合
    private List<Double> yearValueList = new ArrayList<>();// 年对应的值得集合
    private String weekShouldAmountAvg = "";
    private String weekShouldAmountTotal = "";
    private String monthShouldAmountAvg = "";
    private String monthShouldAmountTotal = "";
    private String yearShouldAmountAvg = "";
    private String yearShouldAmountTotal = "";
    private String monthFormat = ""; //折线图标签下面的文字描述需要的日期
    private String yearFormat = ""; //折线图标签下面的文字描述需要的日期

    /**
     * 拆分获取的数据
     *
     * @param jsonObject
     */
    private void sqlitLineChartData(JSONObject jsonObject) {
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
        String initDate;
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
        tvLinechartExplain.setText(monthFormat.isEmpty() ? "---" : monthFormat + "月本周累计汇缴数据表");
        Gson gson = new Gson();
        LineChartDataBean lineChartDataBean = gson.fromJson(jsonObject.toString(), LineChartDataBean.class);
        List<LineChartDataBean.DataBean> dataBean = lineChartDataBean.getData();

        for (int i = 0; i < dataBean.size(); i++) {
            LineChartDataBean.DataBean getAttributeBean = dataBean.get(i);

            if (getAttributeBean.getWeek() != null) {
                weekLine = getAttributeBean.getWeek();  //周
            }

            if (getAttributeBean.getMonth() != null) {
                monthLine = getAttributeBean.getMonth(); //月
            }
            if (getAttributeBean.getYear() != null) {
                yearLine = getAttributeBean.getYear(); //年
            }


          /*  if (getAttributeBean.getAmount() != -1) {
                tvPaymentAmount.setText(formatDigits(getAttributeBean.getAmount()));
            }
            if (getAttributeBean.getCount() != -1) {
                tvPaymentNumber.setText(getAttributeBean.getCount() + "");
            }*/
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
                    tv_average_value.setText(formatDigits(weekBeen.get(0).getShouldAmountAvg())+""); //汇缴平均值
                    weekShouldAmountAvg = formatDigits(weekBeen.get(0).getShouldAmountAvg())+"";
                }
                if(weekBeen.get(0).getShouldAmountTotal() !=-1){
                    tvPaymentAmount.setText(formatDigits(weekBeen.get(0).getShouldAmountTotal())+""); //汇缴总金额
                    weekShouldAmountTotal = formatDigits(weekBeen.get(0).getShouldAmountTotal())+"";
                }
            }

        }

        if (monthLine.size() > 0) { //月数据
            for (int i = 0; i < monthLine.size(); i++) {
                List<LineChartDataBean.DataBean.MonthBean> monthBeen = monthLine.get(i);
                if (monthBeen.get(0).getDate() != null) {

                    monthDateList.add(i, monthBeen.get(0).getDate());
                    monthValueList.add(i, monthBeen.get(0).getValue());
                }
                if (monthBeen.get(0).getShouldAmountAvg() != -1) {
                    monthShouldAmountAvg = formatDigits(monthBeen.get(0).getShouldAmountAvg()) + ""; //汇缴平均值
                }
                if (monthBeen.get(0).getShouldAmountTotal() != -1) {
                    monthShouldAmountTotal = formatDigits(monthBeen.get(0).getShouldAmountTotal()) + ""; //汇缴总金额
                }
            }

        }

        if (yearLine.size() > 0) { //年数据
            for (int i = 0; i < yearLine.size(); i++) {
                List<LineChartDataBean.DataBean.YearBean> yearBeen = yearLine.get(i);
                if (yearBeen.get(0).getDate() != null) {
                    yearDataList.add(i, yearBeen.get(0).getDate());
                    yearValueList.add(i, yearBeen.get(0).getValue());
                }

                if (yearBeen.get(0).getShouldAmountAvg() != -1) {
                    yearShouldAmountAvg = formatDigits(yearBeen.get(0).getShouldAmountAvg()) + ""; //汇缴平均值
                }
                if (yearBeen.get(0).getShouldAmountTotal() != -1) {
                    yearShouldAmountTotal = formatDigits(yearBeen.get(0).getShouldAmountTotal()) + ""; //汇缴总金额
                }
            }


        }

        setLineChartDataTo(weekDateList, weekValue, "1");  //默认进来的是周

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}

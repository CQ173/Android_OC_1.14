package com.huoniao.oc.admin;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AdminConsolidateRecordBean;
import com.huoniao.oc.bean.AdminConsolidateStateBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.myOnTabSelectedListener;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.treeIdList2;
import static com.huoniao.oc.util.DateUtils.nowTime;

/**
 * 汇缴记录
 */
public class AdminConsolidatedRecord extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;    // 返回
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.v_backgroud)
    View vBackgroud; //tablayot 选项卡背景
    @InjectView(R.id.v_backgroud2)
    View vBackgroud2;
    @InjectView(R.id.tb_layout)
    TabLayout tbLayout;  //选项卡
    @InjectView(R.id.et_station_account_number_str)
    EditText etStationAccountNumberStr;   //扣款账号，窗口号，车站账号
    @InjectView(R.id.et_station_account_number_str2)
    EditText etStationAccountNumberStr2;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution; //归属机构
    @InjectView(R.id.ll_consolidated_record_state)
    LinearLayout llConsolidatedRecordState;   //汇缴记录查询状态容器
    @InjectView(R.id.tv_consolidated_record_state)
    TextView tvConsolidatedRecordState; //汇缴记录查询状态
    @InjectView(R.id.tv_start_date)
    TextView tvStartDate;  //开始日期
    @InjectView(R.id.ll_start_date)
    LinearLayout llStartDate;  //开始日期容器
    @InjectView(R.id.tv_end_date)
    TextView tvEndDate;      //结束日期
    @InjectView(R.id.ll_end_date)
    LinearLayout llEndDate;  //结束日期容器
    @InjectView(R.id.tv_query)
    TextView tvQuery;  //查看
    //    @InjectView(tb_layout_date)
//    TabLayout tbLayoutDate;  //日期选项卡
    @InjectView(R.id.tv_total_number)
    TextView tvTotalNumber;  // 汇缴记录总条数
    @InjectView(R.id.tv_total_amount)
    TextView tvTotalAmount;  //总金额
    @InjectView(R.id.userPullToRefreshListView)
    PullToRefreshListView mPullRefreshListView;
    ListView mListView; //包裹的listview
    @InjectView(R.id.ll_ownership_institution)
    LinearLayout ll_ownership_institution;  //归属机构容器
    @InjectView(R.id.ll_date)
    LinearLayout ll_date;   //日期容器
    @InjectView(R.id.ll_consolidated)
    LinearLayout ll_consolidated;  //容器 汇缴记录
    @InjectView(R.id.ll_OutstandingContributions)
    LinearLayout ll_OutstandingContributions; //未完成汇缴界面
    @InjectView(R.id.ll_ownership_institution2)
    LinearLayout ll_ownership_institution2; //未完成汇缴界面 归属机构容器
    @InjectView(R.id.tv_ownership_institution2)
    TextView tvOwnershipInstitution2;// 未完成汇缴界面  归属机构
    @InjectView(R.id.tv_query2)
    TextView tv_query2; //未完成汇缴界面 查询
    @InjectView(R.id.ll_searchTitle)
    LinearLayout llSearchTitle;

    private boolean switchConsolidatedFlag = true; // 汇缴 未完成汇缴界面切换

    List<AdminConsolidateStateBean.DataBean> auditStateList = new ArrayList<>(); //按汇缴记录查询集合

    private MyPopWindow myPopWindow;
    private ListView lv_audit_status; //归属机构弹出框里面的listview
    private MapData mapData;
    private float xs;
    private float ys;
    private String linOfficeId = "";  //临时的归属机构id
    private String linConsolidated = ""; //临时的汇缴状态
    private String officeId = "";
    private String consolidated = "";
    private MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog();   //时间控件
    private MyDatePickerDialog myDatePickerDialog2 = new MyDatePickerDialog();  // 时间控件2

    private String endDateFlag = "";
    private String startDateFlag = "";
    private String startDate = ""; // 开始时间
    private String endDate = "";//结束时间
    private String today = "";  //今日
    private String toMonthOne = ""; //本月一号


    List<AdminConsolidateRecordBean.DataBean> allListData = new ArrayList<>(); //listview列表集合
    private int paymentSize;  ///汇缴记录总数
    private String next = "";        //返回来的页数
    private CommonAdapter<AdminConsolidateRecordBean.DataBean> commonAdapter;
    private String argument = ""; //窗口号  扣款账号  车站账号
    private String withholdStatusNotComplete = "2,3,5";  //未完成汇缴状态
    private String argumentNotComplete = ""; //未完成汇缴  /窗口号  扣款账号  车站账号
    private String linOfficeIdNotComplete = "";  //临时的归属机构id
    private String officeIdNotComplete = ""; //归属机构id
    private String paidUpState; //获取的状态 1 是已完成 2 是未完成

    private String intoTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_consolidated_record);
        ButterKnife.inject(this);

        paidUpState = getIntent().getStringExtra("paidUpState"); //获取的状态 未完成 别的界面传过来的
        initDefault();
        initWidget();
        initData();
    }

    private void initDefault() {
        //获取当前系统时间
        if (nowTime != null) {
            startDate = nowTime;
        } else {
            startDate = DateUtils.getTime();
        }
        today = startDate;//今日


        int lastIndex = startDate.lastIndexOf("-");
        toMonthOne = startDate.substring(0, lastIndex) + "-1"; //本月1号

        endDate = startDate;  //默认进来开始时间和结束时间一样
        startDateFlag = startDate; //记录默认时间tag 作为查询标记
        endDateFlag = endDate;
        tvStartDate.setText(startDate); //默认今天
        tvEndDate.setText(endDate);
    }


    //加载数据
    private void initData() {
        intoTag = getIntent().getStringExtra("intoTag");
        if ("agentPaymentCount".equals(intoTag)){
            startDate = DateUtils.getOldDate(-1);
            endDate = DateUtils.getOldDate(-1);
            tvStartDate.setText(startDate);//获取前一天的时间
            tvEndDate.setText(endDate);
        }
        String account = getIntent().getStringExtra("winNumber");  //窗口号
        String login = getIntent().getStringExtra("loginName"); //  用户
        if (paidUpState == null) {
            if (account != null || login != null) {   //别的界面进来的
                if (account != null) {
                    argument = account;
                }
                if (login != null) {
                    argument = login;
                }
                etStationAccountNumberStr.setText(argument);
                requestConsolidatedData(startDate, endDate, "", argument, "", "1");
            } else {
                requestConsolidatedData(startDate, endDate, "", "", "", "1");
            }
        }

    }


    /**
     * @param createDateBegin//开始日期
     * @param createDateEnd//结束日期
     * @param withholdStatus        //汇缴状态
     * @param argument              窗口号\扣款账号\车站账号
     * @param officeId              归属机构
     * @param pageNo                当前页码
     */
    public void requestConsolidatedData(String createDateBegin, String createDateEnd,
                                        String withholdStatus, String argument, String officeId, String pageNo) {
        String url = Define.URL + "fb/getPaymentList";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("createDateBegin", createDateBegin);
            jsonObject.put("createDateEnd", createDateEnd);
            jsonObject.put("withholdStatus", withholdStatus);
            jsonObject.put("argument", argument);
            jsonObject.put("officeId", officeId);
            jsonObject.put("pageNo", pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "getPaymentList", pageNo, true, true);
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getPaymentList":
                setAdapterData(json, pageNumber);
                break;
            case "getDictAry":
                auditStateList.clear();
                Gson gson = new Gson();
                AdminConsolidateStateBean adminConsolidateStateBean = gson.fromJson(json.toString(), AdminConsolidateStateBean.class);
                List<AdminConsolidateStateBean.DataBean> data = adminConsolidateStateBean.getData();
                if (data != null && data.size() > 0) {
                    auditStateList.addAll(data);
                    showPop(llConsolidatedRecordState, auditStateList, "auditStateList", 1);
                }

                break;
        }
    }

    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        mPullRefreshListView.onRefreshComplete();
    }

    private void setAdapterData(JSONObject json, String pageNumber) {
        Gson gson = new Gson();
        AdminConsolidateRecordBean adminConsolidateRecordBean = gson.fromJson(json.toString(), AdminConsolidateRecordBean.class);
        if (pageNumber.equals("1")) {
            //集成清空处理
            allListData.clear();
            if (commonAdapter != null) {
                mListView.setAdapter(commonAdapter);
            }
        }
        List<AdminConsolidateRecordBean.DataBean> data = adminConsolidateRecordBean.getData();

        if (data != null && data.size() > 0) {
            if (pageNumber.equals("1")) {  //如果是第一页  就需要  条数减一
                for (int i = 0; i < data.size() - 1; i++) {
                    allListData.add(data.get(i));
                }

                AdminConsolidateRecordBean.DataBean dataBean = data.get(data.size() - 1);
                paymentSize = dataBean.getPaymentSize();  //汇缴记录总数
                String shouldSumString = dataBean.getShouldSumString();//总金额
                tvTotalNumber.setText(paymentSize + "");
                tvTotalAmount.setText(shouldSumString);

            } else {
               /* for (int i = 0; i < data.size(); i++) {
                    allListData.add(data.get(i));
                }*/
                allListData.addAll(data);
            }
        } else {
            if (allListData.size() <= 0) {
                tvTotalNumber.setText("0");
                tvTotalAmount.setText("0.00");
            }
        }


        //当前页数
        next = String.valueOf(adminConsolidateRecordBean.getNext());

        setListViewAdapter();

    }

    private void setListViewAdapter() {

        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<AdminConsolidateRecordBean.DataBean>(AdminConsolidatedRecord.this, allListData, R.layout.admin_item_consolidate_record) {
                @Override
                public void convert(ViewHolder holder, AdminConsolidateRecordBean.DataBean dataBean) {
                    TextView tv_date = holder.getView(R.id.tv_date);//汇缴日期
                    TextView tv_debit_account = holder.getView(R.id.tv_debit_account); //扣款账号
                    TextView tv_money = holder.getView(R.id.tv_money); //金额
                    TextView tv_window_number = holder.getView(R.id.tv_window_number);//窗口号
                    TextView tv_stationName = holder.getView(R.id.tv_stationName);//车站账号
                    TextView tv_recharge = holder.getView(R.id.tv_recharge);//扣款状态
                    tv_date.setText(dataBean.getCreateDate());
                    tv_debit_account.setText(dataBean.getOfficeCode());
                    tv_money.setText(dataBean.getShouldAmountString());
                    tv_window_number.setText(dataBean.getWinNumber());
                    tv_stationName.setText(dataBean.getRailwayStationName());
                    getPaymentStatusColor(dataBean.getWithholdStatus() == null ? "" : dataBean.getWithholdStatus(), tv_recharge);
                    tv_recharge.setText(dataBean.getWithholdStatusName());

                }
            };
            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();
    }

    //控件操作
    private void initWidget() {
        tvTitle.setText("汇缴记录");
        tbLayout.addTab(tbLayout.newTab().setText("汇缴记录"));
        if (paidUpState == null) {
            tbLayout.addTab(tbLayout.newTab().setText("未完成汇缴"));
            switchBackgroud(vBackgroud);
        } else {
            tbLayout.addTab(tbLayout.newTab().setText("未完成汇缴"), true);   //外面如果想要进入 未完成汇缴界面会走这里
            tvTitle.setText("历史汇缴");
            switchConsolidatedFlag = false;
            switchBackgroud(vBackgroud2);
            ll_consolidated.setVisibility(View.GONE);
            ll_OutstandingContributions.setVisibility(View.VISIBLE);
//            tbLayoutDate.setVisibility(View.GONE);
            llSearchTitle.setVisibility(View.GONE);
            requestConsolidatedData("", "", withholdStatusNotComplete, argumentNotComplete, officeIdNotComplete, "1");
        }
        setIndicator(tbLayout, 30, 30);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);

        //汇缴
        tbLayout.setOnTabSelectedListener(new myOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中了tab的逻辑
                restoreDate(myDatePickerDialog, startDate, tvStartDate, "startDateFlag");
                restoreDate(myDatePickerDialog2, endDate, tvEndDate, "endDateFlag");
                switch (tab.getPosition()) {
                    case 0:  //汇缴记录
                        tvTitle.setText("汇缴记录");
                        switchConsolidatedFlag = true;
                        switchBackgroud(vBackgroud);
                        ll_consolidated.setVisibility(View.VISIBLE);
//                        tbLayoutDate.setVisibility(View.VISIBLE);
                        llSearchTitle.setVisibility(View.VISIBLE);
                        ll_OutstandingContributions.setVisibility(View.GONE);
                       /* etStationAccountNumberStr.setText("");  //清空 扣款账号编辑器
                        argument="";
                        tvOwnershipInstitution.setText("归属机构");
                        if(tbLayoutDate!=null){
                            tbLayoutDate.removeAllTabs(); //删除原来的选项卡
                            addTabMonth();  //重新添加
                        }
                        initData();*/
                        requestConsolidatedData(startDate, endDate, consolidated, argument, officeId, "1");
                        break;
                    case 1:  //未完成汇缴
                        tvTitle.setText("历史汇缴");
                        switchConsolidatedFlag = false;
                        switchBackgroud(vBackgroud2);
                        ll_consolidated.setVisibility(View.GONE);
                        ll_OutstandingContributions.setVisibility(View.VISIBLE);
//                        tbLayoutDate.setVisibility(View.GONE);
                        llSearchTitle.setVisibility(View.GONE);

                        requestConsolidatedData("", "", withholdStatusNotComplete, argumentNotComplete, officeIdNotComplete, "1");
                        break;
                }
            }

        });


//          addTabMonth();

        initPullRefreshLinstener();

    }

    /*private void addTabMonth() {
        tbLayoutDate.addTab(tbLayoutDate.newTab().setText("今日"));
        tbLayoutDate.addTab(tbLayoutDate.newTab().setText("本月"));
        setIndicator(tbLayoutDate,15,15);
        //月份
        tbLayoutDate.setOnTabSelectedListener(new myOnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                switch (tab.getPosition()) {
                    case 0:    //今日
                        startDate = today; //今日
                        endDate = today;
                        requestConsolidatedData(startDate,endDate,consolidated,argument,officeId,"1");
                        break;
                    case 1:  //本月
                        startDate = toMonthOne; //本月1号
                        endDate = today;  //今日
                        requestConsolidatedData(startDate,endDate,consolidated,argument,officeId,"1");
                        break;
                }
                startDateFlag =startDate;
                endDateFlag = endDate;
                restoreDate(myDatePickerDialog,startDate,tvStartDate,"startDateFlag");
                restoreDate(myDatePickerDialog2,endDate,tvEndDate,"endDateFlag");

            }

        });

    }*/


    /**
     * //上拉加载更多
     */
    private void initPullRefreshLinstener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {  //上拉加载更多
                if (next.equals("-1")) {
                    Toast.makeText(AdminConsolidatedRecord.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    });
                } else {
                    if (switchConsolidatedFlag) {
                        requestConsolidatedData(startDate, endDate, consolidated, argument, officeId, next); //汇缴记录界面的
                    } else {
                        requestConsolidatedData("", "", withholdStatusNotComplete, argumentNotComplete, officeIdNotComplete, next); //未完成汇缴界面的
                    }
                }
            }
        });
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


    //控件背景颜色切换
    private void switchBackgroud(View view) {
        vBackgroud.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        vBackgroud2.setBackgroundColor(getResources().getColor(R.color.alpha_gray));
        if (view != null) {
            view.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_ownership_institution, R.id.ll_consolidated_record_state, R.id.ll_start_date, R.id.ll_end_date, R.id.tv_query,
            R.id.tv_query2, R.id.tv_ownership_institution2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:  //返回
                finish();
                break;
            case R.id.tv_ownership_institution:  //归属机构
                if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))) {
                    ToastUtils.showToast(AdminConsolidatedRecord.this, "开始日期不能大于结束日期，请重新选择日期！");
                    break;
                }
                if (RepeatClickUtils.repeatClick()) {
                    showOwnershipPop();
                }
                break;
            case R.id.ll_consolidated_record_state:  //汇缴记录查询
                if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))) {
                    ToastUtils.showToast(AdminConsolidatedRecord.this, "开始日期不能大于结束日期，请重新选择日期！");
                    break;
                }
                requestConsolidatedState();

                break;
            case R.id.ll_start_date: //开始日期

                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        startDateFlag = date;
                        tvStartDate.setText(date);
                        if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))) {
                            ToastUtils.showToast(AdminConsolidatedRecord.this, "开始日期不能大于结束日期，请重新选择日期！");
                            return;
                        }

                        queryPaymenList();

                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });

                break;
            case R.id.ll_end_date: //结束日期

                myDatePickerDialog2.datePickerDialog(this);
                myDatePickerDialog2.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        endDateFlag = date;
                        tvEndDate.setText(date);
                        if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))) {
                            ToastUtils.showToast(AdminConsolidatedRecord.this, "开始日期不能大于结束日期，请重新选择日期！");
                            return;
                        }

                        queryPaymenList();

                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.tv_query:  //查询
                if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))) {
                    ToastUtils.showToast(AdminConsolidatedRecord.this, "开始日期不能大于结束日期，请重新选择日期！");
                    break;
                }
                queryPaymenList();

                break;
            case R.id.tv_query2:   //未完成汇缴查询
                argumentNotComplete = etStationAccountNumberStr2.getText().toString().trim();  //未完成汇缴界面  窗口号  扣款账号  车站账号
                officeIdNotComplete = linOfficeIdNotComplete;  //未完成汇缴页面 归属机构id
                requestConsolidatedData("", "", withholdStatusNotComplete, argumentNotComplete, officeIdNotComplete, "1");
                break;
            case R.id.tv_ownership_institution2:  //未完成汇缴 归属机构
                if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))) {
                    ToastUtils.showToast(AdminConsolidatedRecord.this, "开始日期不能大于结束日期，请重新选择日期！");
                    break;
                }
                if (RepeatClickUtils.repeatClick()) {
                    showOwnershipPop();
                }
                break;
        }
    }

    /**
     * 请求汇缴状态
     */
    private void requestConsolidatedState() {

        try {
            String url = Define.URL + "sys/getDictAry";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dictType", "bf_payment_status");
            requestNet(url, jsonObject, "getDictAry", "0", true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 在没有点击查询按钮之前 改变的日期都不生效  并还原点击了查询生效的日期 和默认日期
     *
     * @param myDatePickerDialog
     * @param date
     * @param tvDate
     * @param dataTag
     */
    private void restoreDate(MyDatePickerDialog myDatePickerDialog, String date, TextView tvDate, String dataTag) {
        if (myDatePickerDialog != null) {
            String str = date;
            if (!str.isEmpty()) {
                String strs[] = str.split("-");
                int year = Integer.parseInt(strs[0]);
                int month = Integer.parseInt(strs[1]);
                int day = Integer.parseInt(strs[2]);
                myDatePickerDialog.calendarMethod(year, month - 1, day);
                tvDate.setText(formentDate(date));
                if ("startDateFlag".equals(dataTag)) {
                    startDateFlag = formentDate(date);
                } else if ("endDateFlag".equals(dataTag)) {
                    endDateFlag = formentDate(date);
                }
            }

        }
    }

    @NonNull
    private String formentDate(String dateString) {
        String date[] = dateString.split("-");
        return date[0] + "-" + Integer.parseInt(date[1]) + "-" + Integer.parseInt(date[2]);
    }


    @Override
    protected void setDataGetView(ViewHolder holder, Object o, String tag) {
        super.setDataGetView(holder, o, tag);
        switch (tag) {
            case "auditStateList":
                TextView textView = holder.getView(R.id.tv_text);
                AdminConsolidateStateBean.DataBean data = (AdminConsolidateStateBean.DataBean) o;
                textView.setText(data.getLabel() == null ? "" : data.getLabel());
                break;
        }
    }

    @Override
    protected void itemPopClick(AdapterView<?> adapterView, View view, int i, long l, String tag) {
        super.itemPopClick(adapterView, view, i, l, tag);
        switch (tag) {
            case "auditStateList":            //汇缴状态
                AdminConsolidateStateBean.DataBean state = auditStateList.get(i); //获取的文字
                linConsolidated = state.getValue();
                tvConsolidatedRecordState.setText(state.getLabel() == null ? "" : state.getLabel());
               /* if ("已完成".equals(state)) {
                    linConsolidated = Define.CHARG_SUCCESS;    //
                } else if ("待扣款".equals(state)) {
                    linConsolidated = Define.CHARG_WAIT;
                }else if ("待充值".equals(state)) {
                    linConsolidated = Define.CHARG_RECHARGE;
                }else if("异常".equals(state)){
                    linConsolidated = Define.CHARG_FAIL;
                }else if("补缴成功".equals(state)){
                    linConsolidated = Define.CHARG_RECHARGE_SUCCESS;
                }else if("全部".equals(state)){
                    linConsolidated = "";
                }*/
                queryPaymenList();
        }
    }

    /**
     * 弹出归属机构
     */
    private void showOwnershipPop() {


        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
        //得到状态栏高度
        //返回键关闭
        mapData = new MapData(AdminConsolidatedRecord.this, cpd, false) {
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
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                        if (switchConsolidatedFlag) {  //true 表示汇缴记录界面  false 未完成汇缴界面
                            ll_ownership_institution.getLocationOnScreen(arr);
                            xs = arr[0] + ll_ownership_institution.getWidth() - view.getMeasuredWidth();
                            ys = arr[1] + ll_ownership_institution.getHeight();
                        }
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node oneOfficeId) {


                                if (switchConsolidatedFlag) {
                                    linOfficeId = String.valueOf(oneOfficeId.getAllTreeNode().id);  //临时的 汇缴记录页面
                                    tvOwnershipInstitution.setText(oneOfficeId.getAllTreeNode().name);

                                    queryPaymenList();//请求汇缴记录
                                } else {
                                    tvOwnershipInstitution2.setText(oneOfficeId.getAllTreeNode().name);
                                    linOfficeIdNotComplete = String.valueOf(oneOfficeId.getAllTreeNode().id);  //临时的 未完成汇缴界面

                                    argumentNotComplete = etStationAccountNumberStr2.getText().toString().trim();  //未完成汇缴界面  窗口号  扣款账号  车站账号
                                    officeIdNotComplete = linOfficeIdNotComplete;  //未完成汇缴页面 归属机构id
                                    requestConsolidatedData("", "", withholdStatusNotComplete, argumentNotComplete, officeIdNotComplete, "1");
                                }

                                //  if (officeId.getAllTreeNode().type.equals("2")) {
                                myPopWindow.dissmiss();
                                //   }
                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop2;
                    }
                }.popupWindowBuilder(AdminConsolidatedRecord.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                if (switchConsolidatedFlag) {
                    myPopWindow.showAsDropDown(ll_ownership_institution);
                } else {
                    myPopWindow.showAsDropDown(ll_OutstandingContributions);
                }
            }
        };
    }


    /**
     * 根据状态获取名称
     *
     * @param state
     * @param tv_recharge
     * @return
     */
    public void getPaymentStatusColor(String state, TextView tv_recharge) {
        switch (state) {
            case "2":
                tv_recharge.setTextColor(getResources().getColor(R.color.colorAccent));
            default:
                tv_recharge.setTextColor(getResources().getColor(R.color.grenns));
                break;
        }
    }

    //listivew的item点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AdminConsolidateRecordBean.DataBean dataBean = allListData.get(i - 1);
        Intent intent = new Intent(AdminConsolidatedRecord.this, AdminConsolidateRecordDetails.class);
        intent.putExtra("recordDetails", dataBean);
        startActivityIntent(intent);

    }

    /**
     * 赋值和请求很多地方用到整合一下
     */
    private void queryPaymenList(){
        startDate = startDateFlag; //查询状态 把临时的日期赋值给真正要用到的地方
        endDate = endDateFlag;
        argument = etStationAccountNumberStr.getText().toString().trim(); //窗口号  扣款账号  车站账号
        officeId = linOfficeId;
        consolidated = linConsolidated;
        requestConsolidatedData(startDate, endDate, consolidated, argument, officeId, "1");
    }

}

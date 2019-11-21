package com.huoniao.oc.admin;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AdminConsolidateStateBean;
import com.huoniao.oc.bean.EposSync;
import com.huoniao.oc.bean.FilterDataBean;
import com.huoniao.oc.bean.TransactionDetailsBean;
import com.huoniao.oc.common.Filter;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.PaymentStatusUtils;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_count;

public class AdminOJiTransactionDetails extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.iv_back)
    ImageView ivBack;   //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.tv_save)
    TextView tvSave;  //筛选
    @InjectView(R.id.tb_layout)
    TabLayout tbLayout;  //选项卡
    @InjectView(R.id.tv_start_date)
    TextView tvStartDate;  //开始日期
    @InjectView(R.id.tv_end_date)
    TextView tvEndDate;  //结束日期
    @InjectView(R.id.lv_mPullRefreshListView)
    PullToRefreshListView mPullRefreshListView;  //上拉控件
    @InjectView(R.id.activity_admin_oji)
    LinearLayout activityAdminOji;   //
    @InjectView(R.id.ll_start_date)  //开始日期容器
            LinearLayout llStartDate;
    @InjectView(R.id.ll_end_date)    //结束日期容器
            LinearLayout llEndDate;
    @InjectView(tv_count)
    TextView tvCount;  //交易总条数
    @InjectView(R.id.tv_incomeSum)
    TextView tvIncomeSum;    //收入总金额
    @InjectView(R.id.tv_payoutSum)
    TextView tvPayoutSum;   //支出总金额
    @InjectView(R.id.et_queryType)
    EditText etQueryType;    //查询类型
    @InjectView(R.id.tv_query)
    TextView tvQuery;  //查询按钮
    @InjectView(R.id.view)
    View view;   // 作为popwindow的参考
    @InjectView(R.id.ll_consolidated_record_state)
    LinearLayout ll_consolidated_record_state;  //交易渠道
    @InjectView(R.id.tv_consolidated_record_state)
    TextView tv_consolidated_record_state;  //交易渠道text
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private MyDatePickerDialog myDatePickerDialog2;  // 时间控件2
    private ListView mListView;//包裹的listview布局没有
    private VolleyNetCommon volleyNetCommon;  //网络封装类
    private GridView gv_filter_transaction;  //筛选容器
    private MyPopWindow myfilterPopAbstract;  //筛选弹出框

    private String startDate; // 开始时间
    private String endDate;//结束时间
    private String incomeSum; //收入总金额
    private String payoutSum;//支出总金额
    private List<TransactionDetailsBean.DataBean> allData = new ArrayList<>();
    private String pageNo;
    private CommonAdapter<TransactionDetailsBean.DataBean> commonAdapter;
    private String nextPage = "1";  //页数
    private String queryTypeString = "";
    private String tradeStatusList = "" ; //筛选的条件
    //交易类型 income:收入 payout:支出
    private String transactionType = ""; //交易类型默认全部
    private String endDateFlag="";
    private String startDateFlag = "";
    private int endYear, endMonthOfYear, endDayOfMonth;
    private int startYear,startMonthOfYear, startDayOfMonth;
    private Filter filter;

    List<AdminConsolidateStateBean.DataBean> auditStateList = new ArrayList<>(); //按汇缴记录查询集合
    private String linConsolidated = ""; //交易渠道
    private String consolidated = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_oji_transaction_details);
        ButterKnife.inject(this);
        //获取当前系统时间
        if (DateUtils.nowTime != null) {
            startDate = DateUtils.nowTime;
        } else {
            startDate = DateUtils.getTime();
        }
        endDate = startDate;  //默认进来开始时间和结束时间一样
          startDateFlag = startDate; //记录默认时间tag 作为查询标记
          endDateFlag = endDate;
        initWidget();
       /* tvStartDate.setText(startDate+"");
        tvEndDate.setText(endDate+"");*/
        tvStartDate.setText(formentDate(startDate));
        tvEndDate.setText(formentDate(endDate));

        String loginName = getIntent().getStringExtra("loginName"); //别的界面进来需要有用
        if(loginName==null){
            getNetData(startDate, endDate, "1", "", "", false,tradeStatusList,"","" , linConsolidated); // 默认日期可以为空 为空就是今日的数据
        }else{
            getNetData(startDate, endDate, "1", queryTypeString, transactionType, true,tradeStatusList,"1",loginName,linConsolidated);
        }

        initLinsener();

    }

    @NonNull
    private String formentDate(String dateString) {
        String date[] = dateString.split("-");
        return date[0]+"-"+Integer.parseInt(date[1])+"-"+Integer.parseInt(date[2]);
    }

    /**
     * 在没有点击查询按钮之前 改变的日期都不生效  并还原点击了查询生效的日期 和默认日期
     * @param myDatePickerDialog
     * @param date
     * @param tvDate
     * @param dataTag
     */
    private void restoreDate(MyDatePickerDialog myDatePickerDialog ,String date,TextView tvDate,String dataTag) {
        if(myDatePickerDialog != null) {
            String str = date;
            if(!str.isEmpty()) {
                String strs[] = str.split("-");
                int year = Integer.parseInt(strs[0]);
                int month = Integer.parseInt(strs[1]);
                int day = Integer.parseInt(strs[2]);
                myDatePickerDialog.calendarMethod(year, month - 1, day);
                tvDate.setText(formentDate(date));
                if ("startDateFlag".equals(dataTag)){
                    startDateFlag = formentDate(date);
                   }else if("endDateFlag".equals(dataTag)){
                    endDateFlag = formentDate(date);
                }
            }

        }
    }


    private void initLinsener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (nextPage.equals("-1")) {
                    Toast.makeText(AdminOJiTransactionDetails.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    });
                } else {
                    // 1 开始日期   结束日期    下一页    交易账号查询   选项卡选择   上拉加载false  筛选列表  详情页面用户tag   用户名查询
                    getNetData(startDate, endDate, nextPage, queryTypeString, transactionType, false,tradeStatusList,"","",linConsolidated);  //上拉刷新
                }
            }
        });
    }


    /**
     *
     * @param startDate // 1 开始日期
     * @param endDate  结束日期
     * @param pageNum 下一页
     * @param QueryType  交易账号查询
     * @param type 选项卡选择
     * @param clickBoolean 上拉加载false
     * @param tradeStatusList  筛选列表
     * @param loginNameFlag 详情页面用户tag 标记
     * @param loginName  用户名查询
     */
    private void getNetData(String startDate, String endDate, final String pageNum, String QueryType, String type, final boolean clickBoolean,String tradeStatusList,String loginNameFlag,String loginName ,String recordstate) {

        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("pageNo", pageNum);

            if("1".equals(loginNameFlag)){
                etQueryType.setText(loginName);
                queryTypeString =loginName; //把交易账号记住 作为后面查询
                jsonObject.put("userLoginName",loginName);
            }else{
                jsonObject.put("str", QueryType);
            }
            jsonObject.put("type", type);
            jsonObject.put("tradeStatusList",tradeStatusList);
            jsonObject.put("tradeChannel",recordstate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL + "acct/tradeFlowList";
        final JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(AdminOJiTransactionDetails.this, R.string.netError, Toast.LENGTH_SHORT).show();
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                if (clickBoolean) {  //如果是点击进来的
                    allData.clear();
                    if (commonAdapter != null) {
                        mListView.setAdapter(commonAdapter);
                        commonAdapter.notifyDataSetChanged();
                    }
                }
                mPullRefreshListView.onRefreshComplete();
                Gson gson = new Gson();
                TransactionDetailsBean transactionDetails = gson.fromJson(json.toString(), TransactionDetailsBean.class);
                tvCount.setText(transactionDetails.getCount() + "");
                nextPage = transactionDetails.getNext() + "";  //返回的页数
                List<TransactionDetailsBean.DataBean> dataBeanList = transactionDetails.getData();
                if (dataBeanList != null && dataBeanList.size() > 1) {
                    if (pageNum.equals("1")) {  //如果是第一页  就需要  条数减一
                        for (int i = 0; i < dataBeanList.size() - 1; i++) {
                            allData.add(dataBeanList.get(i));
                        }
                    } else {
                        for (int i = 0; i < dataBeanList.size(); i++) {
                            allData.add(dataBeanList.get(i));
                        }
                    }
                }


                if (pageNum.equals("1")) {
                    for (TransactionDetailsBean.DataBean data : dataBeanList
                            ) {
                        if (data.getIncomeSum() != null && data.getPayoutSum() != null) {
                           String out="0";

                            if(data.getPayoutSum().contains("-")){
                                 out = data.getPayoutSum().replace("-","");
                            }
                            tvIncomeSum.setText(data.getIncomeSum() == null ? "收入：0元" : "收入：" + data.getIncomeSum() + "元");//收入总金额
                            tvPayoutSum.setText(data.getPayoutSum() == null ? "支出：0元" : "支出：" + out + "元"); //支出总金额
                            break;
                        }
                    }
                }
                setListData(); //设置数据到apdater

            }

            @Override
            protected void PdDismiss() {
                cpd.dismiss();
            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
        }, "tradeFlowList",true);

        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    //设置数据到apdater
    private void setListData() {
        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<TransactionDetailsBean.DataBean>(this, allData, R.layout.admin_item_oji_ll_transaction) {
                @Override
                public void convert(ViewHolder holder, TransactionDetailsBean.DataBean dataBean) {
                    TextView tv_userName = holder.getView(R.id.tv_userName); //用户名
                    TextView tv_date = holder.getView(R.id.tv_date);  //日期
                    TextView tv_name = holder.getView(R.id.tv_name);//姓名
                    TextView tv_recharge_mode = holder.getView(R.id.tv_recharge_mode); //充值方式
                    TextView tv_money = holder.getView(R.id.tv_money); //金额
                    TextView tv_transaction_status = holder.getView(R.id.tv_transaction_status);//交易状态
                    TransactionDetailsBean.DataBean.TradeUserBean tradeUser = dataBean.getTradeUser();//用户信息
                    if (tradeUser != null) {
                        tv_userName.setText(tradeUser.getLoginName()); //用户名
                        tv_name.setText(tradeUser.getName());   //姓名
                    }else{
                        tv_userName.setText(""); //用户名
                        tv_name.setText("");   //姓名
                    }
                    tv_date.setText(dataBean.getTradeDate());
                    tv_recharge_mode.setText(dataBean.getTradeName());
                  String  tradeFee = dataBean.getTradeFeeString()==null?"":dataBean.getTradeFeeString();
                    if(!tradeFee.isEmpty()){
                        if(tradeFee.contains("-")){
                            tv_money.setTextColor(Color.parseColor("#4D90E7"));//colorAccent
                        }else{
                            tv_money.setTextColor(getResources().getColor(R.color.blue));
                        }
                    }
                    tv_money.setText(dataBean.getTradeFeeString());


                    if (Define.ACCT_TRADE_STATUS_WAIT.equals(dataBean.getTradeStatus())) {
                        tv_transaction_status.setText("等待付款");
                        long etimes = 0; //表示只有在规定时间内才可以 完成显示退款功能
                        try {
                            String formentHaoMiao = dataBean.getExpireTime();
                            if (formentHaoMiao != null) {
                                etimes = DateUtils.dataFormentHaoMiao(dataBean.getExpireTime());
                                if (DateUtils.getSystemDataHaoMiao() > etimes) {  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
                                    tv_transaction_status.setText("交易关闭");

                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (Define.ACCT_TRADE_STATUS_SUSPENSE.equals(dataBean.getTradeStatus())) {

                        tv_transaction_status.setText("等待处理");
                    } else if (Define.ACCT_TRADE_STATUS_FAIL.equals(dataBean.getTradeStatus())) {

                        tv_transaction_status.setText("交易失败");
                    } else if (Define.ACCT_TRADE_STATUS_CLOSED.equals(dataBean.getTradeStatus())) {

                        tv_transaction_status.setText("交易关闭");
                    } else if (Define.ACCT_TRADE_STATUS_PENDING.equals(dataBean.getTradeStatus())) {

                        tv_transaction_status.setText("等待收款");

                    } else if (Define.ACCT_TRADE_STATUS_SUCCESS.equals(dataBean.getTradeStatus())) { //表示支付成功的

                        tv_transaction_status.setText("交易成功");
                    } else if (Define.ACCT_TRADE_STATUS_FINISHED.equals(dataBean.getTradeStatus())) {

                        tv_transaction_status.setText("交易结束");
                    } else if (Define.ACCT_TRADE_STATUS_REFUNDING.equals(dataBean.getTradeStatus())) {

                     /*   tv_tradeStatus.setText("交易成功"); //退款中
                        setRefundState(View.VISIBLE, false, R.color.yellow, "退款中", R.color.white);*/
                        tv_transaction_status.setText("退款中");

                    } else if (Define.ACCT_TRADE_STATUS_REFUND.equals(dataBean.getTradeStatus())) {
                      /*  tv_tradeStatus.setText("交易成功");  // 本来状态改为  已退款  现在把已退款放到按钮上
                        setRefundState(View.VISIBLE, false, R.color.grayss, "已退款", R.color.white);*/
                        tv_transaction_status.setText("已退款");
                    } else if (Define.ACC_TRADE_STATUS_REFUND_FAIL.equals(dataBean.getTradeStatus())) {
                     /*   tv_tradeStatus.setText("交易失败");
                        setRefundState(View.VISIBLE, false, R.color.colorAccent, "退款失败", R.color.white);*/
                        tv_transaction_status.setText("退款失败");
                    }

                }
            };


            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();
    }

    /**
     * 控件操作
     */
    private void initWidget() {
        tvTitle.setText("交易明细");
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText("筛选");
        tbLayout.addTab(tbLayout.newTab().setText("全部"));
        tbLayout.addTab(tbLayout.newTab().setText("收入"));
        tbLayout.addTab(tbLayout.newTab().setText("支出"));
        setIndicator(tbLayout, 30, 30);

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);

        tbLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //交易类型 income:收入 payout:支出
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中了tab的逻辑
             //   Toast.makeText(AdminOJiTransactionDetails.this, "" + tab.getPosition(), Toast.LENGTH_SHORT).show();
                restoreDate(myDatePickerDialog,startDate,tvStartDate,"startDateFlag");
                restoreDate(myDatePickerDialog2,endDate,tvEndDate,"endDateFlag");
                switch (tab.getPosition()) {
                    case 0:  //全部
                        transactionType = "";   //全部
                        getNetData(startDate, endDate, "1", queryTypeString, transactionType, true,tradeStatusList,"","",linConsolidated);
                        break;
                    case 1:  //收入
                        transactionType = "income"; //收入查询

                        getNetData(startDate, endDate, "1", queryTypeString, transactionType, true,tradeStatusList,"","",linConsolidated);
                        break;
                    case 2: //支出
                        transactionType = "payout"; //支出查询

                        getNetData(startDate, endDate, "1", queryTypeString, transactionType, true,tradeStatusList,"","",linConsolidated);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//未选中tab的逻辑

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {//再次选中tab的逻辑

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
    }



    /**
     * 点击事件操作
     *
     * @param view
     */
    @OnClick({R.id.iv_back, R.id.tv_save, R.id.ll_start_date, R.id.ll_end_date, R.id.tv_query,R.id.ll_consolidated_record_state})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                filterM();
                break;
            case R.id.ll_start_date:  //开始日期
                if (myDatePickerDialog == null) {
                    myDatePickerDialog = new MyDatePickerDialog();
                }
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        startDateFlag = date;
                        tvStartDate.setText(date);
                        if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))){
                            Toast.makeText(AdminOJiTransactionDetails.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });


                break;
            case R.id.ll_end_date:   //结束日期
                if (myDatePickerDialog2 == null) {
                    myDatePickerDialog2 = new MyDatePickerDialog();
                }
                myDatePickerDialog2.datePickerDialog(this);
                myDatePickerDialog2.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        endDateFlag = date;
                        tvEndDate.setText(date);
                        if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))){
                            Toast.makeText(AdminOJiTransactionDetails.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.tv_query:  //查询

                if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))){
                    Toast.makeText(AdminOJiTransactionDetails.this, "开始日期不能大于结束日期，请重新选择日期！", Toast.LENGTH_SHORT).show();
                    break;
                }
                queryTypeString = etQueryType.getText().toString().trim();
                startDate = startDateFlag;
                endDate = endDateFlag;
                getNetData(startDate, endDate, "1", queryTypeString, transactionType, true,tradeStatusList,"","",linConsolidated);  // 查询
                break;
            case R.id.ll_consolidated_record_state:
                if (Date.valueOf(startDateFlag).after(Date.valueOf(endDateFlag))) {
                    ToastUtils.showToast(this, "开始日期不能大于结束日期，请重新选择日期！");
                    break;
                }
                requestConsolidatedState();
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
            jsonObject.put("dictType", "acct_trade_tradeChannel");
            requestNet(url, jsonObject, "getDictAry", "0", true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 筛选
     */
    private void filterM() {
        restoreDate(myDatePickerDialog,startDate,tvStartDate,"startDateFlag");
        restoreDate(myDatePickerDialog2,endDate,tvEndDate,"endDateFlag");
        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        String url = Define.URL + "sys/getDictAry";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dictType", "acct_trade_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(AdminOJiTransactionDetails.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Gson gson = new Gson();
                FilterDataBean filterDataBean = gson.fromJson(json.toString(), FilterDataBean.class);
                List<FilterDataBean.DataBean> data = filterDataBean.getData();
                for (FilterDataBean.DataBean bean : data
                        ) {
                    if(tradeStatusList.contains(bean.getValue())) {
                        if(tradeStatusList.length()>0) {
                            String[] split = tradeStatusList.split(",");
                            for (int i = 0; i < split.length; i++) {
                                if(split[i].equals(bean.getValue())){
                                    bean.setFlag(true);
                                }
                            }
                        }

                    }else{
                        bean.setFlag(false);
                    }
                }
                filter = new Filter(data);
                filter.filterState(AdminOJiTransactionDetails.this,view);
                filter.setFilterLinstener(new Filter.FilterLinstener() {
                    @Override
                    public void result(String s) {
                        tvSave.setVisibility(View.VISIBLE);
                        tradeStatusList = s; //把筛选条件记录

                         // 1 开始日期   结束日期    下一页    交易账号查询   选项卡选择   上拉加载false
                            getNetData(startDate, endDate, "1", queryTypeString, transactionType, true, tradeStatusList,"","",linConsolidated);  //上拉刷新
                         //   Toast.makeText(AdminOJiTransactionDetails.this, s, Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void cancle() {
                        tvSave.setVisibility(View.VISIBLE);
                    }
                });
                tvSave.setVisibility(View.GONE);
            }

            @Override
            protected void PdDismiss() {
                   cpd.dismiss();
            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
        }, "getDictAry", true);

        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    int position;

    //listview条目点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            TransactionDetailsBean.DataBean dataBean = allData.get(i-1);
            Intent intent = new Intent(AdminOJiTransactionDetails.this,TransactionDetails.class);
            intent.putExtra("transactionDetails",dataBean);
            position = i-1;
            startActivityForResult(intent,1);
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                //更新条目
                 updateItem(data);
                break;
        }
    }

    /**
     *   更新条目
     * @param data
     */
    private void updateItem(Intent data) {
        if(data!=null) {
            TransactionDetailsBean.DataBean dataBean = allData.get(position);
            EposSync.DataBean   eposDataSync = (EposSync.DataBean) data.getSerializableExtra("eposDataSync");
            if(eposDataSync!=null) {
                dataBean.setTradeAcct(eposDataSync.getAccount() == null ? "" : eposDataSync.getAccount());//交易账号
                dataBean.setTradeNo(eposDataSync.getTradeNo() == null ? "" : eposDataSync.getTradeNo()); //交易号
                dataBean.setCurBalanceString(eposDataSync.getBalance() + "");//账户余额
                dataBean.setTradeStatus(PaymentStatusUtils.paymentState(eposDataSync.getStatus()==null ?"":eposDataSync.getStatus()));//交易状态
             //   eposDataSync.getStatus() == null ? "" : eposDataSync.getStatus());
                dataBean.setUpdateDate(eposDataSync.getUpdateDate() == null ? "" : eposDataSync.getUpdateDate());//更新时间
                if (commonAdapter != null) {
                    commonAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(volleyNetCommon !=null){
            volleyNetCommon.getRequestQueue().cancelAll("getDictAry");
            volleyNetCommon.getRequestQueue().cancelAll("tradeFlowList");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(filter != null && filter.myPopWindow !=null){
                   if(filter.myPopWindow.isShow()){
                        //如果popwindow是显示状态就 不要做任何处理
                       return true;
                   }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getDictAry":
                auditStateList.clear();
                Gson gson = new Gson();
                AdminConsolidateStateBean adminConsolidateStateBean = gson.fromJson(json.toString(), AdminConsolidateStateBean.class);
                List<AdminConsolidateStateBean.DataBean> data = adminConsolidateStateBean.getData();
                AdminConsolidateStateBean.DataBean dataBean = new AdminConsolidateStateBean.DataBean();
                dataBean.setLabel("全部");
                if (data != null && data.size() > 0) {
                    data.add(0,dataBean);
                    auditStateList.addAll(data);
                    showPop(ll_consolidated_record_state, auditStateList, "StateList", 1);
                }

                break;
        }
    }

    @Override
    protected void setDataGetView(ViewHolder holder, Object o, String tag) {
        super.setDataGetView(holder, o, tag);
        switch (tag) {
            case "StateList":
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
            case "StateList":            //汇缴状态
                AdminConsolidateStateBean.DataBean state = auditStateList.get(i); //获取的文字
                linConsolidated = state.getValue();
                tv_consolidated_record_state.setText(state.getLabel() == null ? "" : state.getLabel());
                // 1 开始日期   结束日期    下一页    交易账号查询   选项卡选择   上拉加载false  筛选列表  详情页面用户tag   用户名查询
                if (0==i){
                    getNetData(startDate, endDate, "1", "", "", false,tradeStatusList,"","" , linConsolidated); // 默认日期可以为空 为空就是今日的数据
                }else {
                    getNetData(startDate, endDate, "1", queryTypeString, transactionType, true,tradeStatusList,"","" ,linConsolidated );  //上拉刷新
                }
                break;
        }
    }

}

package com.huoniao.oc.fragment.admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.AdminConsolidatedRecord;
import com.huoniao.oc.admin.AdminOJiTransactionDetails;
import com.huoniao.oc.bean.OutletsMyLogBean;
import com.huoniao.oc.common.HomepageCommon;
import com.huoniao.oc.custom.MySwipeRefreshLayout;
import com.huoniao.oc.financial.FinancialListA;
import com.huoniao.oc.financial.StationPaymentA;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.trainstation.ReturnLateFeeA;
import com.huoniao.oc.user.IncomeStatisticsA;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.MainLogDetailsA;
import com.huoniao.oc.util.CommonAdapter3;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SmartImageView;
import com.huoniao.oc.util.TextSwitcherView;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.R.id.tv_data;
import static com.huoniao.oc.common.HomepageCommon.handler;


/**
 * 管理员首页
 */
public class AdminHomepageF extends BaseFragment implements View.OnClickListener {


    private TextSwitcherView tv_notifications;
    private LinearLayout ll_noticeBar;
    private LinearLayout layout_adminModular;
    private LinearLayout ll_journal;
    private LinearLayout layout_tradeflow;
    private LinearLayout layout_paysysSum;
    private LinearLayout layout_caiWu;
    private TextView tv_pageViewForMonth;
    private TextView tv_pageViewForWeek;
    private TextView tv_pageViewForDay;
    private TextView tv_utilizationRate;
    private TextView tv_surveyDate;
    private LinearLayout layout_receivable;
    private LinearLayout layout_netReceipts;
    private RelativeLayout rl_stalookMoreLog;
    private ListView oltMainLogListView;
    private LinearLayout main_lin;
    private LinearLayout ll_map_container;
    private SmartImageView main_gallery;
    private HomepageCommon homepageCommon;
    private BaseActivity baseActivity;
    private Intent intent;
    private MySwipeRefreshLayout newAdminSwipeRefresh;
    private boolean refreshFlag;
    private TextView tv_outsIncomeCount;
    private TextView tv_returnLateFee;
    private TextView tv_stationPaymentInfo;
    private LinearLayout ll_returnLateFeeArea;

    public AdminHomepageF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_homepage_f,null);
        tv_notifications = (TextSwitcherView) view.findViewById(R.id.sysTemNotification);
        ll_noticeBar = (LinearLayout) view.findViewById(R.id.ll_noticeBar);
        //			main_gallery = (SmartImageView) view.findViewById(R.id.main_gallery);
        //管理员模块区域
        layout_adminModular = (LinearLayout) view.findViewById(R.id.layout_adminModular);
        //系统提醒
        ll_journal = (LinearLayout) view.findViewById(R.id.ll_journal);
        ll_journal.setVisibility(View.VISIBLE);


        layout_tradeflow = (LinearLayout) view.findViewById(R.id.layout_tradeflow);   //管理员-交易流水
        layout_paysysSum = (LinearLayout) view.findViewById(R.id.layout_paysysSum);    //管理员-汇缴概况
        layout_caiWu = (LinearLayout) view.findViewById(R.id.layout_caiWu);   //管理员-财务明细
        tv_pageViewForMonth = (TextView) view.findViewById(R.id.tv_pageViewForMonth);   //管理员-平台每月访问量
        tv_pageViewForWeek = (TextView) view.findViewById(R.id.tv_pageViewForWeek); //管理员-平台每周访问量
        tv_pageViewForDay = (TextView) view.findViewById(R.id.tv_pageViewForDay);    //管理员-平台每日访问量
        tv_utilizationRate = (TextView) view.findViewById(R.id.tv_utilizationRate);  //管理员-平台使用率
        tv_surveyDate = (TextView) view.findViewById(R.id.tv_surveyDate); //管理员-概况时间
        layout_receivable = (LinearLayout) view.findViewById(R.id.layout_receivable);//管理员-应收应付
        layout_netReceipts = (LinearLayout) view.findViewById(R.id.layout_netReceipts); //管理员-实收实付
        rl_stalookMoreLog = (RelativeLayout) view.findViewById(R.id.rl_stalookMoreLog);
        oltMainLogListView = (ListView) view.findViewById(R.id.stationLogListView);

        newAdminSwipeRefresh = (MySwipeRefreshLayout) view.findViewById(R.id.newhomepage_swipeRefresh); //刷新控件
        main_gallery = (SmartImageView) view.findViewById(R.id.main_gallery);
        main_gallery.setRatio(2.33f);
        main_lin = (LinearLayout) view.findViewById(R.id.main_lin);

        tv_outsIncomeCount = (TextView) view.findViewById(R.id.tv_outsIncomeCount); //管理员-代售点收益统计
        tv_stationPaymentInfo = (TextView) view.findViewById(R.id.tv_stationPaymentInfo); //管理员-车站汇缴信息
        tv_returnLateFee = (TextView) view.findViewById(R.id.tv_returnLateFee);//滞纳金返还
        ll_returnLateFeeArea = (LinearLayout) view.findViewById(R.id.ll_returnLateFeeArea); //滞纳金返还容器
        return view;
    }

    /**
     * onCreateView 执行完后立马执行这个方法
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();

    }

    private void initData() {
        baseActivity = (BaseActivity) getActivity();
        homepageCommon = new HomepageCommon(baseActivity){
            @Override
            protected void mainNotificationCpdClose(CustomProgressDialog cpd, String state) {  //系统通知
                super.mainNotificationCpdClose(cpd, state);
                //根据业务去判断是否需要关闭加载框
                switch (state){
                    case "0":
                        getHomepageLog();
                        break;
                    case "1":
                        cpd.dismiss();
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
        intent = getActivity().getIntent();
        initPlatformOverview();//平台概况

    }

    /**
     * 平台概况
     */
    private void initPlatformOverview() {
       String  dayActivity = intent.getStringExtra("dayActivity");
       String weekActivity = intent.getStringExtra("weekActivity");
       String  monthActivity = intent.getStringExtra("monthActivity");
       String  useRate = intent.getStringExtra("useRate");
       String curDate = intent.getStringExtra("curDate");

        if (dayActivity != null && !dayActivity.isEmpty()){
            tv_pageViewForDay.setText(dayActivity);
        }else {
            tv_pageViewForDay.setText("");
        }

        if (weekActivity != null && !weekActivity.isEmpty()){
            tv_pageViewForWeek.setText(weekActivity);
        }else {
            tv_pageViewForWeek.setText("");
        }

        if (monthActivity != null && !monthActivity.isEmpty()){
            tv_pageViewForMonth.setText(monthActivity);
        }else {
            tv_pageViewForMonth.setText("");
        }

        if (useRate != null && !useRate.isEmpty()){

            tv_utilizationRate.setText(useRate + "%");
        }else {
            tv_utilizationRate.setText("");
        }

        if (curDate != null && !curDate.isEmpty()){

            tv_surveyDate.setText(curDate);
        }else {
            tv_surveyDate.setText("");
        }

    }


    private void initWidget() {
        layout_tradeflow.setOnClickListener(this);
        rl_stalookMoreLog.setOnClickListener(this);
        layout_paysysSum.setOnClickListener(this);
        layout_caiWu.setOnClickListener(this);
        tv_outsIncomeCount.setOnClickListener(this);
        tv_returnLateFee.setOnClickListener(this);
        tv_stationPaymentInfo.setOnClickListener(this);
        newAdminSwipeRefresh.setColorScheme(colors);
        newAdminSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (newAdminSwipeRefresh.isRefreshing()) {
                    try {
                        refreshFlag = true;
                        // getMainPaySysData();
                        //getMainNotificationList();
                        homepageCommon = new HomepageCommon(baseActivity)                             {
                                @Override
                                protected void mainNotificationCpdClose(CustomProgressDialog cpd, String state) {
                                super.mainNotificationCpdClose(cpd, state);

                                //根据业务去判断是否需要关闭加载框
                                if(state.equals("0")){
                                    // cpd.dismiss();
                                    //成功 需要做的处理
                                    getHomepageLog();
                                }else if(state.equals("1")){
                                    //失败
                                    adminStopRefreshing();
                                }
                              }
                            };

                        homepageCommon.getMainNotificationList(ll_noticeBar,tv_notifications,cpd); //获取系统通知

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        setPremissionShowHideView(Premission.FB_USERLOG_VIEW,ll_journal); //隐藏首页系统提醒 模块
        setPremissionShowHideView(Premission.FB_PAYMENT_VIEW,layout_paysysSum); //汇缴概况权限
        setPremissionShowHideView(Premission.ACCT_TRADEFLOW_VIEW,layout_tradeflow);  //交易明细
        setPremissionShowHideView(Premission.ACCT_FINANCEAPPLY_VIEW,layout_caiWu);  //财务明细
        setPremissionShowHideView(Premission.FB_LATEFEERETURN_VIEW,ll_returnLateFeeArea); //滞纳金返还容器
        setPremissionShowHideView(Premission.FB_TRAINPAYMENT_VIEW,tv_stationPaymentInfo); //车站汇缴信息
    }

    /**
     * 页面切换做处理
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    // 获取主页-我的日志
    private void getHomepageLog() {
        cpd.show();
        final List<OutletsMyLogBean> oltLogMainList = new ArrayList<OutletsMyLogBean>();
        JSONObject logObject = new JSONObject();
        try {
            logObject.put("limit", 20);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        SessionJsonObjectRequest myLogRequest = new SessionJsonObjectRequest(Request.Method.POST,
                Define.URL + "user/userLogList", logObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response == null) {
                    cpd.dismiss();
                    Toast.makeText(MyApplication.mContext, "服务器数据异常！", Toast.LENGTH_SHORT).show();
                    if(refreshFlag){  //   刷新状态
                        adminStopRefreshing(); //关闭动画
                    }
                    return;
                }
                Log.d("debug", "response =" + response.toString());
                try {
                    String code = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            OutletsMyLogBean oltLogMainBean = new OutletsMyLogBean();
//									oltLogMainBean = new OutletsMyLogBean();
                            JSONObject myLogData = (JSONObject) jsonArray.get(i);
                            String id = myLogData.optString("id");
                            String remoteAddr = myLogData.optString("remoteAddr");
                            String content = myLogData.optString("content");
                            String createDate = myLogData.optString("createDate");
                            oltLogMainBean.setContent(content);
                            oltLogMainBean.setRemoteAddr(remoteAddr);
                            oltLogMainBean.setCreateDate(createDate);
                            oltLogMainBean.setId(id);

                            Log.d("debug", oltLogMainBean.getCreateDate());
                            oltLogMainList.add(oltLogMainBean);
                        }

                        oltMainLogListView.setAdapter(new CommonAdapter3<OutletsMyLogBean>(MyApplication.mContext,
                                oltLogMainList, R.layout.outlets_mylog_mainitem) {

                            @Override
                            public void convert(ViewHolder holder, OutletsMyLogBean logBean) {
                                holder.setText(R.id.tv_content, logBean.getContent()).setText(tv_data,
                                        logBean.getCreateDate());

                            }
                        });

                        oltMainLogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                OutletsMyLogBean oltLogBean = oltLogMainList.get(position);

                                intent = new Intent(MyApplication.mContext, MainLogDetailsA.class);
                                intent.putExtra("date", oltLogBean.getCreateDate());
                                intent.putExtra("content", oltLogBean.getContent());
                                baseActivity.startActivityIntent(intent);

                            }
                        });


                        cpd.dismiss();
                        if(refreshFlag) {
                            Toast.makeText(MyApplication.mContext, "刷新成功！", Toast.LENGTH_SHORT).show();
                                adminStopRefreshing(); //关闭动画

                        }

                    } else if ("46000".equals(code)) {
                        cpd.dismiss();
                        adminStopRefreshing(); //关闭动画
                        Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MyApplication.mContext, LoginA.class);
                        baseActivity.startActivityIntent(intent);
                    } else {
                        cpd.dismiss();
                        Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                        adminStopRefreshing(); //关闭动画
                    }
                } catch (JSONException e) {
                    cpd.dismiss();
                    adminStopRefreshing(); //关闭动画
                    e.printStackTrace();
                }

              /*  if(refreshFlag){
                    stopRefreshing();
                    adminStopRefreshing();
                }*/

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                adminStopRefreshing(); //关闭动画
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
              /*  if(refreshFlag){
                    stopRefreshing();
                    adminStopRefreshing();
                }*/

            }
        });
        // 解决重复请求后台的问题
        myLogRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        myLogRequest.setTag("oltLogMain");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(myLogRequest);


    }

    //管理员--关闭刷新动画
    public void adminStopRefreshing(){
        //刷新完成关闭动画
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (newAdminSwipeRefresh != null) {
                    newAdminSwipeRefresh.setRefreshing(false);
                    refreshFlag = false;
                }
            }
        });
    }


    /**
     * 接收从activity传过来的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
           case R.id.layout_tradeflow:
                baseActivity.startActivityMethod(AdminOJiTransactionDetails.class); //跳转交易明细
                break;
           case R.id.rl_stalookMoreLog: //查看更多
               try {
                   homepageCommon.getOutletsLogData();
               } catch (Exception e) {
                   e.printStackTrace();
               }
               break;

           case R.id.layout_paysysSum:  //跳转汇缴记录
            baseActivity.startActivityMethod(AdminConsolidatedRecord.class);
            break;
           case R.id.layout_caiWu:
              intent = new Intent(MyApplication.mContext, FinancialListA.class);
              baseActivity.startActivityIntent(intent);
               break;
            case R.id.tv_outsIncomeCount:
                baseActivity.startActivityMethod(IncomeStatisticsA.class);
                break;

            case R.id.tv_stationPaymentInfo:
                intent = new Intent(MyApplication.mContext, StationPaymentA.class);
                intent.putExtra("paymentInfoTag", "homePage");
                baseActivity.startActivityIntent(intent);
                break;

            case R.id.tv_returnLateFee:
                intent = new Intent(MyApplication.mContext, ReturnLateFeeA.class);
                startActivityIntent(intent);
                break;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁activity之前先关闭定时器
        if(homepageCommon !=null && homepageCommon.handler!=null) {
                if (homepageCommon.timeadv != null) {
                    handler.removeCallbacks(homepageCommon.timeadv);
                }
        }
    }


}

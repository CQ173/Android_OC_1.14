package com.huoniao.oc.fragment.train_station;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.AdminConsolidatedRecord;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.bean.OutletsMyLogBean;
import com.huoniao.oc.bean.StationOltsManage;
import com.huoniao.oc.common.HomepageCommon;
import com.huoniao.oc.common.MapData2;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.MySwipeRefreshLayout;
import com.huoniao.oc.financial.StationPaymentA;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.trainstation.NewWarningInfoListA;
import com.huoniao.oc.trainstation.ReturnLateFeeA;
import com.huoniao.oc.trainstation.StationOutletsManageA;
import com.huoniao.oc.trainstation.StationOutletsRelationA;
import com.huoniao.oc.trainstation.WindowsAnchoredListA;
import com.huoniao.oc.user.IncomeStatisticsA;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.MainLogDetailsA;
import com.huoniao.oc.util.CommonAdapter3;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SmartImageView;
import com.huoniao.oc.util.TextSwitcherView;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.MyApplication.treeIdList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainStationHomepageF extends BaseFragment implements View.OnClickListener {


    private TextSwitcherView tv_notifications;
    private LinearLayout ll_noticeBar;
    private RelativeLayout rl_stalookMoreLog;
    private ListView oltMainLogListView;
    private MySwipeRefreshLayout newhomepageSwipeRefresh;
    private LinearLayout layout_outletsList;
    private LinearLayout layout_outletsRelation;
    private LinearLayout layout_windowAnchored;
    private TextView tv_lookFinPaysys2;
    private TextView tv_importedPaysysRecord_station;
    private TextView tv_importedSumMoney_station;
    private TextView tv_noFinishPaysysRecord_station;
    private TextView tv_noFinishSumMoney_station;
    private LinearLayout ll_imported_station;
    private LinearLayout ll_noFinish_station;
    private SmartImageView main_gallery;
    private LinearLayout main_lin;
    private TextureMapView tv_map;
    private TextView tv_affiliation;
    private LinearLayout ll_tree_outlets;
    private TextView tv_trainTicketName;
    private TextView tv_trainTicket_location;
    private TextView tv_trainTicket_legalPerson;
    private TextView tv_trainTicket_phone;
    private LinearLayout ll_journal;
    private Intent intent;
    private String requestSuccessIsClosed="0"; //0表示可以关闭  1表示不可以
    private boolean refreshFlag;
    private HomepageCommon homepageCommon;

    private LinearLayout ll_stationHomePageMode;//火车站-首页-汇缴预警模块
    private LinearLayout ll_waitDo_earlyWarning;//首页-汇缴预警-待处理
    private final int WAITDO_WARNING = 30;//返回主界面是刷新首页财务总览数据标识
    private TextView tv_waitDo_earlyWarning;//首页-汇缴预警-待处理
    private TextView tv_lookMore_earlyWarning;//首页-汇缴预警-查看更多
    private TextView day_earlyWarningNum;//首页-汇缴预警-今日
    private TextView moth_earlyWarningNum;//首页-汇缴预警-本月
    private TextView year_earlyWarningNum;//首页-汇缴预警-本年
    private LinearLayout ll_consolidated_today_station;
    private LinearLayout ll_stationHomePageMode1;
    private TextView tv_outsIncomeCount;
    private TextView tv_returnLateFee;
    private LinearLayout ll_returnLateFeeArea;
    private TextView tv_stationPaymentInfo;


    public TrainStationHomepageF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.train_station_view_homepage_f,null);
        tv_notifications = (TextSwitcherView) view.findViewById(R.id.sysTemNotification);
        ll_noticeBar = (LinearLayout) view.findViewById(R.id.ll_noticeBar);
      
        //RelativeLayout    rl_staLookMorePaySys = (RelativeLayout) view.findViewById(R.id.rl_staLookMorePaySys);
       // TextView   tv_noData = (TextView) view.findViewById(R.id.tv_noData);
       // LinearLayout ll_payment_train = (LinearLayout) view.findViewById(R.id.ll_payment_train);
        rl_stalookMoreLog = (RelativeLayout) view.findViewById(R.id.rl_stalookMoreLog);
        oltMainLogListView = (ListView) view.findViewById(R.id.stationLogListView);
       // ListView  lv_staMainpaySys = (ListView) view.findViewById(R.id.lv_staMainpaySys);
        newhomepageSwipeRefresh = (MySwipeRefreshLayout) view.findViewById(R.id.newhomepage_swipeRefresh); //刷新控件
        layout_outletsList = (LinearLayout) view.findViewById(R.id.layout_outletsList); //火车站代售点列表
        layout_outletsRelation = (LinearLayout) view.findViewById(R.id.layout_outletsRelation);     //火车站代售点关联
        layout_windowAnchored = (LinearLayout) view.findViewById(R.id.layout_windowAnchored);   //火车站窗口挂靠
        ll_consolidated_today_station = (LinearLayout) view.findViewById(R.id.ll_consolidated_today_station);//今日汇缴容器
        tv_lookFinPaysys2 = (TextView) view.findViewById(R.id.tv_lookFinPaysys2);       //火车站首页 今日汇缴查看
        tv_importedPaysysRecord_station = (TextView) view.findViewById(R.id.tv_importedPaysysRecord_station);   //已导入汇缴记录
        tv_importedSumMoney_station = (TextView) view.findViewById(R.id.tv_importedSumMoney_station);//已导入总金额
        tv_noFinishPaysysRecord_station = (TextView) view.findViewById(R.id.tv_noFinishPaysysRecord_station);  //未完成汇缴记录
        tv_noFinishSumMoney_station = (TextView) view.findViewById(R.id.tv_noFinishSumMoney_station);//未完成总金额
        ll_imported_station = (LinearLayout) view.findViewById(R.id.ll_imported_station);//已导入容器
        ll_noFinish_station = (LinearLayout) view.findViewById(R.id.ll_noFinish_station); //未导入容器
        main_gallery = (SmartImageView) view.findViewById(R.id.main_gallery);
        main_gallery.setRatio(2.33f);
        main_lin = (LinearLayout) view.findViewById(R.id.main_lin);
        tv_map = (TextureMapView) view.findViewById(R.id.tv_map);
        tv_affiliation = (TextView) view.findViewById(R.id.tv_affiliation); //归属机构
        ll_tree_outlets = (LinearLayout) view.findViewById(R.id.ll_tree_outlets);  //火车站点击锚点展示信息容器
        tv_trainTicketName = (TextView) view.findViewById(R.id.tv_trainTicketName);  //火车票代售点名称
        tv_trainTicket_location = (TextView) view.findViewById(R.id.tv_trainTicket_location);    //地址
        tv_trainTicket_legalPerson = (TextView) view.findViewById(R.id.tv_trainTicket_legalPerson);//法人
        tv_trainTicket_phone = (TextView) view.findViewById(R.id.tv_trainTicket_phone);//手机号
        ll_journal = (LinearLayout) view.findViewById(R.id.ll_journal);        //系统提醒 也就是我的日志  //权限控制

        ll_stationHomePageMode = (LinearLayout) view.findViewById(R.id.ll_stationHomePageMode);  //火车站-首页-汇缴预警模块
        ll_stationHomePageMode.setVisibility(View.VISIBLE);
        ll_waitDo_earlyWarning = (LinearLayout) view.findViewById(R.id.ll_waitDo_earlyWarning);  //首页-汇缴预警-待处理
        tv_waitDo_earlyWarning = (TextView) view.findViewById(R.id.tv_waitDo_earlyWarning);//首页-汇缴预警-待处理
        tv_lookMore_earlyWarning = (TextView) view.findViewById(R.id.tv_lookMore_earlyWarning);//首页-汇缴预警-查看更多
        day_earlyWarningNum = (TextView) view.findViewById(R.id.day_earlyWarningNum);//首页-汇缴预警-今日
        moth_earlyWarningNum = (TextView) view.findViewById(R.id.moth_earlyWarningNum);//首页-汇缴预警-本月
        year_earlyWarningNum = (TextView) view.findViewById(R.id.year_earlyWarningNum);//首页-汇缴预警-本年

        ll_stationHomePageMode1 = (LinearLayout) view.findViewById(R.id.ll_stationHomePageMode);  // 汇缴预警容器
        tv_outsIncomeCount = (TextView) view.findViewById(R.id.tv_outsIncomeCount);//首页-代售点收益统计
        tv_returnLateFee = (TextView) view.findViewById(R.id.tv_returnLateFee);//滞纳金返还
        ll_returnLateFeeArea = (LinearLayout) view.findViewById(R.id.ll_returnLateFeeArea); //滞纳金返还容器
        tv_stationPaymentInfo = (TextView) view.findViewById(R.id.tv_stationPaymentInfo);////财务-车站汇缴信息
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();
    }




    private void initWidget() {
        setPremissionShowHideView(Premission.FB_AGENCYCONNECT_VIEW, layout_windowAnchored); //火车站窗口挂靠查看权限
        setPremissionShowHideView(Premission.FB_AGENCY_VIEW, layout_outletsRelation);//火车站代售点关联权限
        setPremissionShowHideView(Premission.FB_AGENCY_VIEW, layout_outletsList);     //火车站代售点列表关联权限
        setPremissionShowHideView(Premission.FB_USERLOG_VIEW,ll_journal); //隐藏首页系统提醒 模块
        setPremissionShowHideView(Premission.FB_PAYMENT_VIEW,ll_consolidated_today_station); //今日汇缴权限
        setPremissionShowHideView(Premission.FB_ALARM_VIEW,ll_stationHomePageMode1); //汇缴预警容器
        setPremissionShowHideView(Premission.FB_LATEFEERETURN_VIEW,ll_returnLateFeeArea); //滞纳金返还容器
        setPremissionShowHideView(Premission.FB_TRAINPAYMENT_VIEW,tv_stationPaymentInfo); //车站汇缴信息

        layout_windowAnchored.setOnClickListener(this);
        layout_outletsRelation.setOnClickListener(this);
        layout_outletsList.setOnClickListener(this);
        tv_lookFinPaysys2.setOnClickListener(this);
        ll_imported_station.setOnClickListener(this);
        ll_noFinish_station.setOnClickListener(this);
        tv_affiliation.setOnClickListener(this);


        ll_waitDo_earlyWarning.setOnClickListener(this);
        tv_lookMore_earlyWarning.setOnClickListener(this);
        tv_outsIncomeCount.setOnClickListener(this);
        tv_returnLateFee.setOnClickListener(this);
        tv_stationPaymentInfo.setOnClickListener(this);
        rl_stalookMoreLog.setOnClickListener(this);

    }


    private void initData() {
        //系统通知
        homepageCommon = new HomepageCommon(baseActivity){
            @Override
            protected void mainNotificationCpdClose(CustomProgressDialog cpd, String state) {  //系统通知
                super.mainNotificationCpdClose(cpd, state);
                //根据业务去判断是否需要关闭加载框
                switch (state){
                    case "0":
                        setRequestPaymentWarnCount(false);
                        break;
                    case "1":
                        cpd.dismiss();
                        stopRefreshing(); //走到这里说明网络异常了或者别的异常
                        setRequestPaymentWarnCount(false); //上个请求错误，下个请求继续开始
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


        newhomepageSwipeRefresh.setColorScheme(colors);
        newhomepageSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(newhomepageSwipeRefresh.isRefreshing()){
                    try {
                        refreshFlag = true;
                        homepageCommon.getMainNotificationList(ll_noticeBar,tv_notifications,cpd); //获取系统通知
                        MyApplication.treeIdList.clear();

                   /*    // getMainNotificationList();
                      //  getMainPaySysData();

                        //	getTrainMapData();  //火车站地图刷新

                      *//*  if(ll_tree_outlets!=null){
                            ll_tree_outlets.setVisibility(View.GONE);
                        }*//*
                        //getHomepageLog();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
                        stopRefreshing(); //关闭动画
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
                                holder.setText(R.id.tv_content, logBean.getContent()).setText(R.id.tv_data,
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
                            stopRefreshing(); //关闭动画

                        }

                    } else if ("46000".equals(code)) {
                        cpd.dismiss();
                        stopRefreshing(); //关闭动画
                        Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MyApplication.mContext, LoginA.class);
                        baseActivity.startActivityIntent(intent);
                    } else {
                        cpd.dismiss();
                        Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                        stopRefreshing(); //关闭动画
                    }
                } catch (JSONException e) {
                    cpd.dismiss();
                    stopRefreshing(); //关闭动画
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
                stopRefreshing(); //关闭动画
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
                    getHomepageLog();  //日志
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

                    tv_importedPaysysRecord_station.setText(todayCountStr == null ? "" : todayCountStr);
                    ;//汇缴记录已导入条数
                    tv_importedSumMoney_station.setText(todaySumStr == null ? "" : todaySumStr);
                    ;//已导入总金额
                    tv_noFinishPaysysRecord_station.setText(unCompleteCountStr == null ? "" : unCompleteCountStr); //未完成 汇缴记录条数
                    tv_noFinishSumMoney_station.setText(unCompleteSumStr == null ? "" : unCompleteSumStr);        ; //未完成 总金额

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            @Override
            protected void PdDismiss() {
                if(requestSuccessIsClosed.equals("0")) {
                    cpd.dismiss();
                    if(!requestMode){
                        getHomepageLog();  //日志   网络异常下个请求继续开始
                    }
                    stopRefreshing();
                }
                requestSuccessIsClosed="0";
                if(requestMode){   //单个调用请求 可以关闭加载框
                    cpd.dismiss();
                    stopRefreshing();
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

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             //火车站-首页-代售点列表
             case R.id.layout_outletsList:
                 startActivityMethod(StationOutletsManageA.class);
//                 try {
//                     getOltsManageList();
//                 } catch (Exception e5) {
//                     e5.printStackTrace();
//                 }
                 break;
             //火车站-O计-代售点关联
             case R.id.layout_outletsRelation:
                 try {
                     getRelationOltsList();
                 } catch (Exception e5) {
                     e5.printStackTrace();
                 }
                 break;

             //火车站-O计-窗口号挂靠
             case R.id.layout_windowAnchored:
                 intent = new Intent(MyApplication.mContext, WindowsAnchoredListA.class);
                 startActivity(intent);
                 baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                 break;
             case R.id.tv_lookFinPaysys2:
                 startActivityMethod(AdminConsolidatedRecord.class);
                 break;
             case R.id.ll_imported_station:
                 startActivityMethod(AdminConsolidatedRecord.class);
                 break;
             case R.id.ll_noFinish_station:
                 intent = new Intent(MyApplication.mContext,AdminConsolidatedRecord.class);
                 intent.putExtra("paidUpState","paidUpState");
                 startActivityIntent(intent);
                 break;
             case R.id.tv_affiliation: //系统管理员 O计界面归属机构
                 if(RepeatClickUtils.repeatClick()) {
                     treeIdList.clear();
                     getOjiAffiliationMapData();
                 }
                 break;

             case R.id.ll_waitDo_earlyWarning://火车站-首页-汇缴预警-待处理预警
//                 intent = new Intent(MyApplication.mContext, WarningInfoListA.class);
//                 intent.putExtra("processingState",Define.WARN_WAITDO);
//                 startActivityForResult(intent, WAITDO_WARNING);
                 intent = new Intent(MyApplication.mContext, NewWarningInfoListA.class);
                 intent.putExtra("processingState", "0");//未处理状态，传0
                 startActivityForResult(intent, WAITDO_WARNING);

                 break;
             case R.id.tv_lookMore_earlyWarning://火车站-首页-汇缴预警-查看更多
//                 intent = new Intent(MyApplication.mContext, WarningInfoListA.class);
//                 startActivityForResult(intent, WAITDO_WARNING);
                intent = new Intent(MyApplication.mContext, NewWarningInfoListA.class);
                startActivityForResult(intent, WAITDO_WARNING);

                 break;

             case R.id.tv_outsIncomeCount:
                 intent = new Intent(MyApplication.mContext, IncomeStatisticsA.class);
                 startActivityIntent(intent);
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

             case R.id.rl_stalookMoreLog: //查看更多
                 try {
                     homepageCommon.getOutletsLogData();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
         }
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
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag){
            case "paymentWarnCount"://火车站首页汇缴预警（包括汇缴模块的汇缴预警）
                paymentWarnData(json);
                if(!controlOff){
                    getFinTodayPaysys("",true,false); // 今日汇缴数据
                }
                break;
        }
    }

    @Override
    protected void closeDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.closeDismiss(cpd, tag, controlOff);
        switch (tag){
            case "paymentWarnCount":
                 if(controlOff){
                     cpd.dismiss();
                 }
                break;
        }
    }


    /**
     * 网络异常关闭加载框
     */
    @Override
    protected void loadControlExceptionDismiss(CustomProgressDialog cpd,String tag,boolean controlOff) {
        super.loadControlExceptionDismiss(cpd,tag,controlOff);
        switch (tag){
            case "paymentWarnCount":
                if(!controlOff){
                    getFinTodayPaysys("",true,false); // 今日汇缴数据
                }
                break;
        }
        stopRefreshing();
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
//                tv_waitDo_earlyWarning.setText(waitDo_earlyWarning + "条待处理");
                tv_waitDo_earlyWarning.setText(Html.fromHtml( "<font color='#4D90E7'>" +waitDo_earlyWarning  + "</font>" + "条待处理"));
            }else {
                ll_waitDo_earlyWarning.setVisibility(View.GONE);
            }
            String dayWarningNum = jsonObject.optString("day");
            if (dayWarningNum != null){
                day_earlyWarningNum.setText(dayWarningNum);

            }else {
                day_earlyWarningNum.setText("0");
            }
            String mothWarningNum = jsonObject.optString("month");
            if (mothWarningNum != null){
                moth_earlyWarningNum.setText(mothWarningNum);
            }else {
                moth_earlyWarningNum.setText("0");
            }

            String yearWarningNum = jsonObject.optString("year");
            if (yearWarningNum != null){
                year_earlyWarningNum.setText(yearWarningNum);
            }else {
                year_earlyWarningNum.setText("0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case WAITDO_WARNING:  //预警
                setRequestPaymentWarnCount(true);
                break;
        }
    }

    MapData2 mapData2;
    BaiduMap baiduMap2;
    private MyPopWindow myPopWindow;
    private 	View viewTreePop = null;
    private  View  viewTreePopT=null;
    private 	TextView tv_baidu_pop_t = null;
    private 	TextView tv_baidu_pop_w = null;
    private void getOjiAffiliationMapData(){
        baiduMap2 = tv_map.getMap();
        if(myPopWindow!=null){
            myPopWindow.dissmiss();
        }
        mapData2 = new MapData2(baseActivity,cpd){
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();

                myPopWindow = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        ListView   lv_train_ownership = (ListView) view.findViewById(R.id.lv_train_ownership);
                        mapData2.setTrainOwnershipData(lv_train_ownership,baiduMap2);
                        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myPopWindow.dissmiss();
                            }
                        });

                    }
                    @Override
                    protected int layout() {
                        return R.layout.train_ownership_institution_pop;
                    }
                }.poPwindow(baseActivity,true).showAtLocation(ll_noticeBar, Gravity.CENTER,0,0);
            }

            @Override
            protected void zoomToSpan(List<AllTreeNode> allTreeNodesList, AllTreeNode allTreeNode, String type) {
                super.zoomToSpan(allTreeNodesList, allTreeNode, type);
                setZoomToSpanMap(allTreeNodesList, allTreeNode, type);
            }

            @Override
            protected void setTreeSingleMessage(AllTreeNode allTreeNode) {
                super.setTreeSingleMessage(allTreeNode);
                setTreeSingleMessage2(allTreeNode);
            }
        };
    }


    //拿到数据进行展示管理员o计界面地图
    public void setZoomToSpanMap(List<AllTreeNode> allTreeNodesList, AllTreeNode all, String type){
        MyApplication.type = type;
        List<Overlay> mOverlayList =  new ArrayList<Overlay>();;
        baiduMap2.clear();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.train_location);
        for (AllTreeNode allTreeNode :allTreeNodesList
                ) {

            AllTreeNode info = new AllTreeNode();
            info.lat = allTreeNode.lat;
            info.lng = allTreeNode.lng;
            info.name = allTreeNode.name;
            info.corpName = allTreeNode.corpName;
            info.phone = allTreeNode.phone;
            info.geogPosition = allTreeNode.geogPosition;
            Bundle bundle = new Bundle();
            bundle.putSerializable("info",info);
            if(!(info.lat==0.0||info.lng==0.0)) {
                Overlay overlay = baiduMap2.addOverlay(new MarkerOptions().position(new LatLng(allTreeNode.lat, allTreeNode.lng)).icon(bitmap));
                overlay.setExtraInfo(bundle);
                mOverlayList.add(overlay);
            }
        }

        if (mOverlayList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : mOverlayList) {
                // polyline 中的点可能太多，只按marker 缩放
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            baiduMap2.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }




        if(MyApplication.type.equals("3") && all != null){
            if(!(all.lat==0.0||all.lng==0.0)) {
                MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(new LatLng(all.lat,all.lng));
                baiduMap2.animateMapStatus(uc);

                final InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        baiduMap2.hideInfoWindow();
                        MyApplication.type ="0";
                    }
                };


                if(viewTreePop==null) {
                    viewTreePop = baseActivity.getLayoutInflater().inflate(R.layout.baidu_pop, null);
                    tv_baidu_pop_t = (TextView) viewTreePop.findViewById(R.id.tv_baidu_pop); //百度地图弹窗文字
                }
                tv_baidu_pop_t.setText(all.name);
                //创建InfoWindow展示的view
                InfoWindow  mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(viewTreePop), new LatLng(all.lat,all.lng), -30, onInfoWindowClickListener);
                baiduMap2.showInfoWindow(mInfoWindow);

            }else{
                Toast.makeText(MyApplication.mContext, "你没有设置地理位置，无法再地图上找到你的地理位置！", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 地图 Marker 覆盖物点击事件监听函数
         * @param marker 被点击的 marker
         */
        baiduMap2.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                AllTreeNode info = (AllTreeNode) marker.getExtraInfo().get("info");

                // 将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                final InfoWindow.OnInfoWindowClickListener onInfoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        baiduMap2.hideInfoWindow();
                        MyApplication.type ="0";
                    }
                };


                if(viewTreePopT ==null) {
                    viewTreePopT = baseActivity.getLayoutInflater().inflate(R.layout.baidu_pop, null);
                    tv_baidu_pop_w = (TextView) viewTreePopT.findViewById(R.id.tv_baidu_pop); //百度地图弹窗文字
                }
                tv_baidu_pop_w.setText(info.name);
                InfoWindow 	mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(viewTreePopT), ll, -30, onInfoWindowClickListener);
                baiduMap2.showInfoWindow(mInfoWindow);

                setTreeSingleMessage2(info);  //设置点击锚点信息展示
                return true;

            }
        });

        baiduMap2.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                baiduMap2.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void setTreeSingleMessage2(AllTreeNode info) {
        ll_tree_outlets.setVisibility(View.VISIBLE);
        tv_trainTicketName.setText(info.name);
        tv_trainTicket_location.setText(info.geogPosition);
        tv_trainTicket_legalPerson.setText(info.corpName);
        tv_trainTicket_phone.setText(info.phone);
    }



    /**
     * 获取火车站代售点管理列表数据
     *
     * @throws Exception
     */
    private void getOltsManageList() throws Exception {
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = Define.URL + "fb/agencyList";
        final List<StationOltsManage> oltsManageList = new ArrayList<StationOltsManage>();
        SessionJsonObjectRequest OltsManageListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    StationOltsManage oltsManage = new StationOltsManage();

                                    JSONObject oltsManageObj = (JSONObject) jsonArray.get(i);
                                    String id = oltsManageObj.optString("id");
                                    String code = oltsManageObj.optString("code");
                                    JSONObject area = oltsManageObj.optJSONObject("area");
                                    String name = oltsManageObj.optString("name");
                                    String address = oltsManageObj.optString("address");
                                    String master = oltsManageObj.optString("master");
                                    String phone = oltsManageObj.optString("phone");
                                    String winNumber = oltsManageObj.optString("winNumber");
                                    String corpName = oltsManageObj.optString("corpName");
                                    String corpMobile = oltsManageObj.optString("corpMobile");
                                    String corpIdNum = oltsManageObj.optString("corpIdNum");
                                    String operatorName = oltsManageObj.optString("operatorName");
                                    String operatorMobile = oltsManageObj.optString("operatorMobile");
                                    String operatorIdNum = oltsManageObj.optString("operatorIdNum");
                                    String state = oltsManageObj.optString("state");
                                    String city = area.optString("name");
                                    oltsManage.setId(id);
                                    oltsManage.setCode(code);
                                    // oltsRelation.setArea(area);
                                    oltsManage.setName(name);
                                    oltsManage.setAddress(address);
                                    oltsManage.setMaster(master);
                                    oltsManage.setPhone(phone);
                                    oltsManage.setWinNumber(winNumber);
                                    oltsManage.setCorpName(corpName);
                                    oltsManage.setCorpMobile(corpMobile);
                                    oltsManage.setCorpIdNum(corpIdNum);
                                    oltsManage.setState(state);
                                    oltsManage.setCity(city);
                                    oltsManage.setOperatorName(operatorName);
                                    oltsManage.setOperatorMobile(operatorMobile);
                                    oltsManage.setOperatorIdNum(operatorIdNum);

                                    Log.d("debug", oltsManage.getName());
                                    oltsManageList.add(oltsManage);
                                }

                                intent = new Intent(MyApplication.mContext, StationOutletsManageA.class);
                                Bundle bundle = new Bundle();

                                bundle.putSerializable("oltsManageList", (Serializable) oltsManageList);
                                // bundle.putSerializable("msgBean",
                                // oltMsgBean);
                                intent.putExtras(bundle);
                                cpd.dismiss();
                                startActivity(intent);
                                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(MyApplication.mContext, LoginA.class);
                                startActivity(intent);
                                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                cpd.dismiss();
                                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cpd.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        OltsManageListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        OltsManageListRequest.setTag("outletsManage");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(OltsManageListRequest);

    }


    /**
     * 获取火车站关联代售点列表数据
     *
     * @throws Exception
     */
    private void getRelationOltsList() throws Exception {
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = Define.URL + "fb/canLinkList";
        final List<StationOltsManage> RelationOltsList = new ArrayList<StationOltsManage>();
        SessionJsonObjectRequest RelationOltsListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject oltsRelationObj = (JSONObject) jsonArray.get(i);
                                    String state = oltsRelationObj.optString("state");
                                 //   if (!"0".equals(state)) continue;     //不管审核状态为什么   数据都要
                                    StationOltsManage oltsRelation = new StationOltsManage();

                                    String id = oltsRelationObj.optString("id");
                                    String code = oltsRelationObj.optString("code");
                                    JSONObject area = oltsRelationObj.optJSONObject("area");
                                    String name = oltsRelationObj.optString("name");
                                    String address = oltsRelationObj.optString("address");
                                    String master = oltsRelationObj.optString("master");
                                    String phone = oltsRelationObj.optString("phone");
                                    String winNumber = oltsRelationObj.optString("winNumber");
                                    String corpName = oltsRelationObj.optString("corpName");
                                    String corpMobile = oltsRelationObj.optString("corpMobile");
                                    String corpIdNum = oltsRelationObj.optString("corpIdNum");

                                    String city = area.optString("name");
                                    oltsRelation.setId(id);
                                    oltsRelation.setCode(code);
                                    // oltsRelation.setArea(area);
                                    oltsRelation.setName(name);
                                    oltsRelation.setAddress(address);
                                    oltsRelation.setMaster(master);
                                    oltsRelation.setPhone(phone);
                                    oltsRelation.setWinNumber(winNumber);
                                    oltsRelation.setCorpName(corpName);
                                    oltsRelation.setCorpMobile(corpMobile);
                                    oltsRelation.setCorpIdNum(corpIdNum);
                                    oltsRelation.setState(state);
                                    oltsRelation.setCity(city);

                                    Log.d("debug", oltsRelation.getName());
                                    RelationOltsList.add(oltsRelation);
                                }

                                intent = new Intent(MyApplication.mContext, StationOutletsRelationA.class);
                                Bundle bundle = new Bundle();

                                bundle.putSerializable("outletsRelationList", (Serializable) RelationOltsList);
                                // bundle.putSerializable("msgBean",
                                // oltMsgBean);
                                intent.putExtras(bundle);
                                cpd.dismiss();
                                startActivity(intent);
                                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(MyApplication.mContext, LoginA.class);
                                startActivity(intent);
                                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                cpd.dismiss();
                                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cpd.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(MyApplication.mContext,R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        RelationOltsListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        RelationOltsListRequest.setTag("outletsRelation");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(RelationOltsListRequest);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    @Override
    public void onPause(){
        if(tv_map!=null) {
            tv_map.onPause();
        }
        super.onPause();
    }



    @Override
    public void onResume(){
        if(tv_map !=null) {
            tv_map.onResume();
        }
        super.onResume();
    }



    public void stopRefreshing(){
        //刷新完成关闭动画
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (newhomepageSwipeRefresh != null) {
                    newhomepageSwipeRefresh.setRefreshing(false);
                    refreshFlag = false;
                    requestSuccessIsClosed="0";
                }
            }
        });
    }


}

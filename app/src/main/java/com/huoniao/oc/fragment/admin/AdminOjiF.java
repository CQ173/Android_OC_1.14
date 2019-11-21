package com.huoniao.oc.fragment.admin;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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
import com.huoniao.oc.admin.AdminOJiTransactionDetails;
import com.huoniao.oc.admin.AdminUserManageA;
import com.huoniao.oc.admin.AdminWindowAnchored;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.bean.ConversionRankingBean;
import com.huoniao.oc.bean.FinancialOverview;
import com.huoniao.oc.bean.TradingRankBean;
import com.huoniao.oc.common.MapData2;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.custom.MySwipeRefreshLayout;
import com.huoniao.oc.financial.FinancialListA;
import com.huoniao.oc.financial.ImportRecordA;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.MyApplication.treeIdList;
import static com.huoniao.oc.MyApplication.waitAgency;
import static com.huoniao.oc.MyApplication.waitUser;
import static com.huoniao.oc.R.id.tv_dfCircle;
import static com.huoniao.oc.R.id.tv_pmCircle;
import static com.huoniao.oc.R.id.tv_wdCircle;
import static com.huoniao.oc.R.id.tv_whCircle;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminOjiF extends BaseFragment implements OnChartValueSelectedListener, View.OnClickListener {


    private LinearLayout layout_transaction_details;
    private LinearLayout layout_userManagement;
    private TextView tv_today;
    private TextView tv_toMoth;
    private RelativeLayout rl_withdrawals;
    private TextView tv_wdCircleAdmin;
    private TextView tv_wdSingular;
    private TextView tv_wdMoney;
    private RelativeLayout rl_withhold;
    private TextView tv_whCircleAdmin;
    private TextView tv_whSingular;
    private TextView tv_whMoney;
    private RelativeLayout rl_daiFu;
    private TextView tv_dfCircleAdmin;
    private TextView tv_dfSingular;
    private TextView tv_dfMoney;
    private RelativeLayout rl_depositary;
    private TextView tv_dtCircleAdmin;
    private TextView tv_dtSingular;
    private TextView tv_dtMoney;
    private RelativeLayout rl_payment;
    private TextView tv_pmCircleAdmin;
    private TextView tv_pmSingular;
    private TextView tv_pmMoney;
    private TextView tv_tradeDetailed;
    private TextView tv_income;
    private TextView tv_expenditure;
    private TextView tv_fnBalance;
    private BarChart tradeBarChart;
    private TextView tv_admin_income;
    private TextView tv_admin_expenditure;
    private MyListView admin_lv_ranking2;
    private TextView tv_admin_window_pending_audit;
    private TextView tv_admin_pending_user;
    private LinearLayout ll_admin_window_pending_audit;
    private LinearLayout ll_admin_pending_user;
    private TextureMapView tv_map2;
    private TextView tv_affiliation2;
    private LinearLayout ll_tree_outlets2;
    private TextView tv_trainTicketName2;
    private TextView tv_trainTicket_location2;
    private TextView tv_trainTicket_legalPerson2;
    private TextView tv_trainTicket_phone2;
    private LinearLayout layout_financial_details;
    private TextView tv_admin_view_more;
    private VolleyNetCommon volleyNetCommon;


    private String finTextCickTag = "toDay";//记录今日和本月按钮点击
    private String requestSuccessIsClosed="0"; //0表示可以关闭  1表示不可以
    private Intent intent;
    private final int BACK_REFRESH_FINOVERVIEW = 20;//返回主界面是刷新首页财务总览数据标识
    private View v;
    private ScrollView scroll_view;
    private LinearLayout ll_transaction_profile;
    private LinearLayout ll_user_management;
    private LinearLayout ll_financial_overview;
    private MySwipeRefreshLayout myswipeRefresh;

    private LinearLayout ll_ticketImport, ll_importDetails;
    private TextView tv_ticketImportStation, tv_ticketNoImportStation;

    public AdminOjiF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.admin_view_oji,null);
        layout_transaction_details = (LinearLayout) view.findViewById(R.id.layout_transaction_details); //交易明细
        layout_userManagement = (LinearLayout)view.findViewById(R.id.layout_userManagement);
        v = view.findViewById(R.id.v);
        scroll_view = (ScrollView) view.findViewById(R.id.scroll_view);
        //	-----------------------O计选项
        tv_today = (TextView) view.findViewById(R.id.tv_today); //O计-财务总览-今日
        tv_toMoth = (TextView) view.findViewById(R.id.tv_toMoth);    //O计-财务总览-本月
        rl_withdrawals = (RelativeLayout) view.findViewById(R.id.rl_withdrawals); //O计-财务总览-提现
        tv_wdCircleAdmin = (TextView) view.findViewById(tv_wdCircle); //O计-财务总览-提现待办数
        tv_wdSingular = (TextView) view.findViewById(R.id.tv_wdSingular);  //O计-财务总览-提现笔数
        tv_wdMoney = (TextView) view.findViewById(R.id.tv_wdMoney); //O计-财务总览-提现总金额
        rl_withhold = (RelativeLayout) view.findViewById(R.id.rl_withhold);  //O计-财务总览-代扣
        tv_whCircleAdmin = (TextView) view.findViewById(tv_whCircle);  //O计-财务总览-代扣待办数
        tv_whSingular = (TextView) view.findViewById(R.id.tv_whSingular);   //O计-财务总览-代扣笔数
        tv_whMoney = (TextView) view.findViewById(R.id.tv_whMoney); //O计-财务总览-代扣总金额
        rl_daiFu = (RelativeLayout) view.findViewById(R.id.rl_daiFu);   //O计-财务总览-代付
        tv_dfCircleAdmin = (TextView) view.findViewById(tv_dfCircle);  //O计-财务总览-代付待办数
        tv_dfSingular = (TextView) view.findViewById(R.id.tv_dfSingular);  //O计-财务总览-代付笔数
        tv_dfMoney = (TextView) view.findViewById(R.id.tv_dfMoney); //O计-财务总览-代付总金额
        rl_depositary = (RelativeLayout) view.findViewById(R.id.rl_depositary); //O计-财务总览-代存
        tv_dtCircleAdmin = (TextView) view.findViewById(R.id.tv_dtCircle); //O计-财务总览-待办数
        tv_dtSingular = (TextView) view.findViewById(R.id.tv_dtSingular);  //O计-财务总览-代存笔数
        tv_dtMoney = (TextView) view.findViewById(R.id.tv_dtMoney);  //O计-财务总览-代存总金额
        rl_payment = (RelativeLayout) view.findViewById(R.id.rl_payment); //O计-财务总览-付款
        tv_pmCircleAdmin = (TextView) view.findViewById(tv_pmCircle); //O计-财务总览-付款待办数
        tv_pmSingular = (TextView) view.findViewById(R.id.tv_pmSingular);   //O计-财务总览-付款笔数
        tv_pmMoney = (TextView) view.findViewById(R.id.tv_pmMoney); //O计-财务总览-付款总金额
        tv_tradeDetailed = (TextView) view.findViewById(R.id.tv_tradeDetailed); //O计-交易概况-交易明细
        tv_income = (TextView) view.findViewById(R.id.tv_income);  //O计-交易概况-收入
        tv_expenditure = (TextView) view.findViewById(R.id.tv_expenditure); //O计-交易概况-支出
        tv_fnBalance = (TextView) view.findViewById(R.id.tv_fnBalance);   //O计-交易概况-结余
        tradeBarChart = (BarChart) view.findViewById(R.id.tradeBarChart);    //O计-交易概况-统计图
        tv_admin_income = (TextView) view.findViewById(R.id.tv_admin_income);  //o计排行榜 收入
        tv_admin_expenditure = (TextView) view.findViewById(R.id.tv_admin_expenditure);  //o计排行榜支出
        admin_lv_ranking2 = (MyListView) view.findViewById(R.id.admin_lv_ranking2);
        tv_admin_window_pending_audit = (TextView)view.findViewById(R.id.tv_admin_window_pending_audit);  //窗口挂靠待审核
        tv_admin_pending_user = (TextView) view.findViewById(R.id.tv_admin_pending_user);   //待审核用户
        ll_admin_window_pending_audit = (LinearLayout) view.findViewById(R.id.ll_admin_window_pending_audit); //窗口挂靠待审核容器
        ll_admin_pending_user = (LinearLayout) view.findViewById(R.id.ll_admin_pending_user);   //待审核用户容器
        tv_map2 = (TextureMapView) view.findViewById(R.id.tv_map2);  //o计界面地图
        tv_affiliation2 = (TextView)  view.findViewById(R.id.tv_affiliation2); //o计归属机构
        ll_tree_outlets2 = (LinearLayout) view.findViewById(R.id.ll_tree_outlets); //隐藏的点击地图标注显示的详情容器
        tv_trainTicketName2 = (TextView) view.findViewById(R.id.tv_trainTicketName);  //隐藏的点击地图标注显示的详情 代售点名称
        tv_trainTicket_location2 = (TextView) view.findViewById(R.id.tv_trainTicket_location);   //隐藏的点击地图标注显示的详情 地理位置
        tv_trainTicket_legalPerson2 = (TextView) view.findViewById(R.id.tv_trainTicket_legalPerson); //隐藏的点击地图标注显示的详情 法人
        tv_trainTicket_phone2 = (TextView) view.findViewById(R.id.tv_trainTicket_phone);  ////隐藏的点击地图标注显示的详情  手机号
        layout_financial_details = (LinearLayout)view.findViewById(R.id.layout_financial_details); //财务明细
        tv_admin_view_more = (TextView) view.findViewById(R.id.tv_admin_view_more);   // oji 查看 更多进入用户管理
        ll_transaction_profile = (LinearLayout) view.findViewById(R.id.ll_transaction_profile); //交易概况容器
        ll_user_management = (LinearLayout) view.findViewById(R.id.ll_user_management); //O计底部用户管理模块容器
        ll_financial_overview = (LinearLayout) view.findViewById(R.id.ll_financial_overview); //财务总览容器
        myswipeRefresh = (MySwipeRefreshLayout) view.findViewById(R.id.myswipeRefresh);    //下拉刷新控件
        ll_ticketImport = (LinearLayout) view.findViewById(R.id.ll_ticketImport);  //今日票款导入模块
        ll_importDetails = (LinearLayout) view.findViewById(R.id.ll_importDetails);  //查看票款导入列表
        tv_ticketImportStation = (TextView) view.findViewById(R.id.tv_ticketImportStation);//导入票款车站数
        tv_ticketNoImportStation = (TextView) view.findViewById(R.id.tv_ticketNoImportStation);//未导入票款车站数
        tradeBarChart.setFocusable(false);
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
        volleyNetCommon = new VolleyNetCommon();
        switchBackgroudDayRanking(tv_admin_income); //默认选中今日交易排行收入
        tv_admin_window_pending_audit.setText(waitAgency); //窗口挂靠待审核
        tv_admin_pending_user.setText(waitUser);//待审核用户

        tv_admin_income.setOnClickListener(this);
        tv_admin_expenditure.setOnClickListener(this);
        layout_transaction_details.setOnClickListener(this);
        layout_financial_details.setOnClickListener(this);
        layout_userManagement.setOnClickListener(this);
        tv_tradeDetailed.setOnClickListener(this);
        tv_today.setOnClickListener(this);
        tv_toMoth.setOnClickListener(this);
        tv_admin_view_more.setOnClickListener(this);
        ll_admin_pending_user.setOnClickListener(this);
        ll_admin_window_pending_audit.setOnClickListener(this);
        rl_withdrawals.setOnClickListener(this);
        rl_withhold.setOnClickListener(this);
        rl_daiFu.setOnClickListener(this);
        rl_depositary.setOnClickListener(this);
        rl_payment.setOnClickListener(this);
        tv_affiliation2.setOnClickListener(this);
        ll_importDetails.setOnClickListener(this);
        setPremissionShowHideView(Premission.SYS_USER_VIEW,layout_userManagement); //用户管理菜单权限

        setPremissionShowHideView(Premission.ACCT_TRADEFLOW_VIEW,layout_transaction_details);  //交易明细
        setPremissionShowHideView(Premission.ACCT_TRADEFLOW_VIEW,ll_transaction_profile);  //交易概况
        setPremissionShowHideView(Premission.ACCT_FINANCEAPPLY_VIEW,layout_financial_details);  //财务明细
        setPremissionShowHideView(Premission.ACCT_FINANCEAPPLY_VIEW,ll_financial_overview);  //财务总览

        // setPremissionShowHideView(Premission.SYS_USER_VIEW,ll_user_management); //底部用户管理模块权限
        premissionNo(Premission.SYS_USER_VIEW,tv_admin_view_more); //查看更多 没有权限不可点击
        premissionNo(Premission.SYS_USER_VIEW,ll_admin_pending_user);//待审核用户 权限
        premissionNo(Premission.FB_AGENCYCONNECT_AUDIT,ll_admin_window_pending_audit);//窗口挂靠待审核
        setPremissionShowHideView(Premission.FB_PAYMENT_PAYMENTINFO, ll_ticketImport); //汇缴导入记录的权限

        myswipeRefresh.setColorScheme(colors);
        myswipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myswipeRefresh.isRefreshing()) {
                    switchBackgroudDayRanking(tv_admin_income); //默认选中今日交易排行收入
                    getTradeOverview();
                }
            }
        });



    }

    //管理员--关闭刷新动画
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

    //抽取权限
    public  void premissionNo(String premissionFlag,View view){
        for (String premissions: premissionsList ) {
            if(premissionFlag.equals(premissions)){
                view.setEnabled(true);
                return;
            }else{
                view.setEnabled(false);
            }
        }
    }

    private void initData() {
        initTradeBarChart();//初始化 o计统计图 属性
        getTradeOverview();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_admin_income:  ////o计 系统管理员 今日交易排行 收入
                setDayTradingRank("1");
                switchBackgroudDayRanking(tv_admin_income);
                break;
            case R.id.tv_admin_expenditure://o计 系统管理员 今日交易排行 支出
                setDayTradingRank("2"); //方法有参数说明
                switchBackgroudDayRanking(tv_admin_expenditure);
                break;
            case R.id.layout_transaction_details: //交易明细
                baseActivity.startActivityMethod(AdminOJiTransactionDetails.class);
                break;
            case R.id.layout_financial_details: //财务明细
                baseActivity.startActivityMethod(FinancialListA.class);
                break;
            case R.id.layout_userManagement: //用户管理
                baseActivity.startActivityMethod(AdminUserManageA.class);
                break;
            case R.id.tv_tradeDetailed:   //交易概况-交易明细
                baseActivity.startActivityMethod(AdminOJiTransactionDetails.class);
                break;
            case R.id.tv_today: //今日
                finTextCickTag = "toDay";
                setTextStyle(tv_today, R.drawable.textbackgroud5, Color.rgb(255, 255, 255));
                setTextStyle(tv_toMoth, R.drawable.textbackgroud6, Color.rgb(74, 74, 74));
                getFinancialOverview(true);
                break;
            case R.id.tv_toMoth: //本月
                finTextCickTag = "toMoth";
                setTextStyle(tv_toMoth, R.drawable.textbackgroud5, Color.rgb(255, 255, 255));
                setTextStyle(tv_today, R.drawable.textbackgroud6, Color.rgb(74, 74, 74));
                getFinancialOverview(true);
                break;
            case R.id.tv_admin_view_more: //查看更多
                    baseActivity.startActivityMethod(AdminUserManageA.class);
                break;
            case R.id.ll_admin_pending_user: //待审核用户容器
                intent = new Intent(MyApplication.mContext, AdminUserManageA.class);
                intent.putExtra("pending","1");
                baseActivity.startActivityIntent(intent);
                break;
            case R.id.ll_admin_window_pending_audit://窗口挂靠待审核容器
                intent = new Intent(MyApplication.mContext,AdminWindowAnchored.class);
                intent.putExtra("PendingAuditWindow","PendingAuditWindow");
                baseActivity.startActivityIntent(intent);
                break;
            //-财务总览-提现
            case R.id.rl_withdrawals:
                intent = new Intent(MyApplication.mContext, FinancialListA.class);
                intent.putExtra("finTextCickTag", finTextCickTag);
                intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
//			intent.putExtra("waitHandleType", Define.finApplyType2);
                intent.putExtra("type", Define.finApplyType2);
                baseActivity.startActivityIntent(intent);
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
                intent = new Intent(MyApplication.mContext, FinancialListA.class);
                intent.putExtra("finTextCickTag", finTextCickTag);
                intent.putExtra("mainIntoFinTag", "mainIntoFinTag");
//			intent.putExtra("waitHandleType", Define.finApplyType1);
                intent.putExtra("type", Define.finApplyType1);
//			startActivity(intent);
                startActivityForResult(intent, BACK_REFRESH_FINOVERVIEW);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.tv_affiliation2: //系统管理员 O计界面归属机构
                if(RepeatClickUtils.repeatClick()) {
                    treeIdList.clear();
                    getOjiAffiliationMapData();
                }
                break;

            case R.id.ll_importDetails:
                startActivityMethod(ImportRecordA.class);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case BACK_REFRESH_FINOVERVIEW:
                getFinancialOverview(true);
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
        baiduMap2 = tv_map2.getMap();
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
                }.poPwindow(baseActivity,true).showAtLocation(v, Gravity.CENTER,0,0);
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
        ll_tree_outlets2.setVisibility(View.VISIBLE);
        tv_trainTicketName2.setText(info.name);
        tv_trainTicket_location2.setText(info.geogPosition);
        tv_trainTicket_legalPerson2.setText(info.corpName);
        tv_trainTicket_phone2.setText(info.phone);
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

    /**
     * 初始化 o计柱状图 一些设置
     */
    private void  initTradeBarChart(){
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
        tradeBarChart.setNoDataText("该统计图没有数据了");
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



    /**
     * 从后台获取财务首页的交易概况数据
     */
    private void getTradeOverview(){
        cpd.show();
        JSONObject tradeOverviewObj = new JSONObject();
        String url = Define.URL+"acct/tradingOverview";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, tradeOverviewObj, new VolleyAbstract(baseActivity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
              /*  if(refreshFlag){
                    financiaStopRefreshing();
                }*/
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                requestSuccessIsClosed="1";
                getFinancialOverview(false);
                Log.d("tradeOverviewData",json.toString());
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

                    scroll_view.setFocusable(true);
                    scroll_view.smoothScrollTo(0,0);
                  /*  if(refreshFlag){
                        financiaStopRefreshing();
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                 /*   if(refreshFlag){
                        financiaStopRefreshing();
                    }*/
                }

            }

            @Override
            protected void PdDismiss() {
                if(requestSuccessIsClosed.equals("0")) {
                    getFinancialOverview(false);  //网络异常 继续执行下个请求  财务总览数据
                    cpd.dismiss();
                }
                requestSuccessIsClosed ="0";  //解决中途 网络中断 获取其他出错 无法关闭加载框

            }

            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }


        }, "tradeOverview", false);

        volleyNetCommon.addQueue(jsonObjectRequest);

    }




    /**
     * 从后台获取财务首页的财务总览数据
     * @param requestMode  true为不连续加载   false 为连续多个请求 主要是为了关闭加载框
     */
    private void getFinancialOverview(final boolean requestMode){
        cpd.show();
        JSONObject finOverObj = new JSONObject();
        String url = Define.URL+"acct/financeApplyIndexTypeCount";
        final JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, finOverObj, new VolleyAbstract(baseActivity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
             /*   if(refreshFlag){
                    financiaStopRefreshing();
                }*/
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                requestSuccessIsClosed ="1";
                  if(!requestMode){
                      dayRequsetTradingRank();
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

                                        String pmMoneyStr = dayArrBean.getSumString();
                                        if (pmMoneyStr != null) {
                                            tv_pmMoney.setText(pmMoneyStr + "元");
                                        } else {
                                            tv_pmMoney.setText("");
                                        }

                                        break;
                                    case Define.finApplyType2:


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


                                        String pmMoneyStr = monthArrBean.getSumString();
                                        if (pmMoneyStr != null) {
                                            tv_pmMoney.setText(pmMoneyStr + "元");
                                        } else {
                                            tv_pmMoney.setText("");
                                        }

                                        break;
                                    case Define.finApplyType2:

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
                                }

                            }

                        }
                    }catch (Exception e){
                      /*  if(refreshFlag){
                            financiaStopRefreshing();
                        }*/
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void PdDismiss() {
                if(requestSuccessIsClosed.equals("0")) {
                    if(!requestMode){  //主要是解决单个请求 不执行下个请求
                        dayRequsetTradingRank();
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


        }, "finOverRequest", true);

        volleyNetCommon.addQueue(jsonObjectRequest);

    }



    /**
     * 今日交易排行top10 数据	 *
     */
    public  void  dayRequsetTradingRank(){
        String url = Define.URL+"acct/getTradeTopList";
         requestNet(url,new JSONObject(),"getTradeTopList","-1",false, true); //-1没有实际意义
    }

    /**
     * 请求财务首页的汇缴导入数
     * @param off   开关控制加载框
     */
    private void requestPaymentCount(boolean off){
        String url = Define.URL+"fb/paymentCount";
        requestNet(url,new JSONObject(),"adminPaymentCount","0",off, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber,controlOff);
        switch (tag){
            case "getTradeTopList": //系统管理员O计界面  今日交易排行数据
                requestSuccessIsClosed ="1";
                if(!controlOff){
                    //继续下一个请求
                    requestPaymentCount(true);
                }
                getTradeRankData(json);
                break;

            case "adminPaymentCount":
                requestSuccessIsClosed ="0";
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
    protected void closeDismiss(CustomProgressDialog cpd,String tag,boolean controlOff) {
        super.closeDismiss(cpd,tag,controlOff);
        switch (tag){
            case "getTradeTopList":
            case "adminPaymentCount":
                if(controlOff){
                    cpd.dismiss();//最后一个请求直接关闭
                    stopRefreshing();
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
                    requestPaymentCount(true);
                }
                break;

        }
        stopRefreshing();
    }

    List<TradingRankBean.DataBean.IncomeBean> incomeList; //系统管理员o计界面 排行收入
    List<TradingRankBean.DataBean.PayoutBean> payoutList; //系统管理员 o计界面 排行支出
    /**
     * 获取数据并对数据拆分处理
     */
    public void getTradeRankData(JSONObject json){
        Gson gson = new Gson();
        TradingRankBean tradingRankBean = gson.fromJson(json.toString(), TradingRankBean.class);
        TradingRankBean.DataBean data = tradingRankBean.getData();
        incomeList = data.getIncome();
        payoutList = data.getPayout();
        setDayTradingRank("1"); //默认进来是收入排行
    }

    /**
     * type 1表示收入  2 表示支出
     * @param type
     */
    public void setDayTradingRank(String type){
        List<ConversionRankingBean> listConversionRanking = new ArrayList<>();
        listConversionRanking.clear();
        if(type.equals("1")){
            if(incomeList !=null && incomeList.size()>0){
                for (int i = 0; i < incomeList.size(); i++) {
                    if (i < 10) {
                        TradingRankBean.DataBean.IncomeBean incomeBean = incomeList.get(i);
                        listConversionRanking.add(new ConversionRankingBean(incomeBean.getStandby2(), incomeBean.getStandby1(), incomeBean.getSumString(), i));
                    }
                }
            }
        }
        if(type.equals("2")){
            if(payoutList !=null && payoutList.size()>0){
                for (int i = 0; i < payoutList.size(); i++) {
                    if (i < 10) {
                        TradingRankBean.DataBean.PayoutBean payoutBean = payoutList.get(i);
                        listConversionRanking.add(new ConversionRankingBean(payoutBean.getStandby2(), payoutBean.getStandby1(), payoutBean.getSumString(), i));
                    }
                }
            }
        }
        setTopRankingAdapter(listConversionRanking,admin_lv_ranking2); //设置数据给适配器
    }

    //今日交易排行收入支出背景切换
    private void switchBackgroudDayRanking(View paramView)
    {

        this.tv_admin_income.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style3));
        this.tv_admin_expenditure.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style3));

        if (paramView != null)
            paramView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bluebutton_click_style));

    }


    //设置代售点交易排行适配器
    public void	setTopRankingAdapter(List<ConversionRankingBean> listConversionRanking,MyListView myListView){
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
    }


    @Override
    public void onPause(){
        if(tv_map2!=null) {
            tv_map2.onPause();
        }
        super.onPause();
    }



    @Override
    public void onResume(){
        if(tv_map2 !=null) {
            tv_map2.onResume();
        }
        super.onResume();
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


}

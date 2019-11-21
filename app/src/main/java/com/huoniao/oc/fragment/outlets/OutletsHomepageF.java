package com.huoniao.oc.fragment.outlets;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.gson.Gson;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.alipay.AlipayCore;
import com.huoniao.oc.alipay.MD5;
import com.huoniao.oc.bean.ConfigureBean;
import com.huoniao.oc.bean.CreditScoreEvent;
import com.huoniao.oc.bean.OltsCapitalFlowBean;
import com.huoniao.oc.bean.PaySystemBean;
import com.huoniao.oc.bean.ServiceFeeIsShowB;
import com.huoniao.oc.bean.UnAgreeProtocolBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.HomepageCommon;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.MySwipeRefreshLayout;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.outlets.AposPayA;
import com.huoniao.oc.outlets.CapitalFlowA;
import com.huoniao.oc.outlets.CashA;
import com.huoniao.oc.outlets.NewAposPayA;
import com.huoniao.oc.outlets.NewReceivablesA;
import com.huoniao.oc.outlets.NewRechargeA;
import com.huoniao.oc.outlets.PaySystemA;
import com.huoniao.oc.outlets.ReceivablesA;
import com.huoniao.oc.outlets.RechargeA;
import com.huoniao.oc.outlets.WalletA;
import com.huoniao.oc.user.GeographicalPositionMapA;
import com.huoniao.oc.user.IncomeStatisticsA;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.OjiCreditA;
import com.huoniao.oc.useragreement.RegisterAgreeA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SPUtils;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SmartImageView;
import com.huoniao.oc.util.TextSwitcherView;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;

import static com.huoniao.oc.MainActivity.creditScore;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;
import static java.lang.Double.parseDouble;


/**
 * A simple {@link Fragment} subclass.
 */
public class OutletsHomepageF extends BaseFragment implements View.OnClickListener {

    private TextSwitcherView tv_notifications;
    private LinearLayout ll_noticeBar;
    private SmartImageView main_gallery;
    private MySwipeRefreshLayout outletsNewhomeSwipeRefresh;
    private LinearLayout ll_wallet_menu;
    private LinearLayout layout_wallet;
    private View v_white_line;
    private LinearLayout layout_recharge;
    private LinearLayout layout_withdrawals;
    private LinearLayout layout_borrowMoney;
    private LinearLayout layout_receivables;
    private TextView tv_balance;
    private TextView tv_lookMorePaysys;
    private TextView tv_shouldSum;
    private TextView tv_alreadySum;
    private TextView tv_debitStatus;
    private TextView tv_date;
    private RelativeLayout rl_planeTicket;
    private RelativeLayout rl_securitiesAccount;
    private RelativeLayout rl_ecoFarm;
    private TextView tv_lookMoreCapitalFlow;
    private LinearLayout layout_paySysContentArea;
    private TextView tv_noData;
    private LinearLayout layout_capitalFlowArea;
    private TextView tv_tradeAmount;
    private TextView tv_tradeName;
    private TextView tv_tradeStatus;
    private TextView tv_tradeDate;
    private TextView tv_noFlowData;
    private LinearLayout ll_payment;
    private LinearLayout ll_bill;
    private LinearLayout main_lin;
    private HomepageCommon homepageCommon;
    private MyPopWindow myPopWindowConfigurationMap;
    private List<User.AgencysBean> agencysList = new ArrayList<>();// 请求后解析地图列表数据结果
    private String mainCountId; // 主账号id
    private List<User.AgencysBean> allAgencysList = new ArrayList<>();  //地图设置列表总集合
    private MyPopWindow myPopWindowSetting;
    private ListView lv_setting_map;
    private CommonAdapter agencysBeanCommonAdapter;

    private String balanceStr;
    private String minimumStr;
    private boolean outletRefreshFlag = false;  // 代售点 - 刷新状态
    private Intent intent;
    private User user;
    private String dateDay;
    private TextView tv_creditScore, tv_creditRating;
    private LinearLayout layout_ojiCredit;
    private TextView tv_lookCredit;
    private String loginName;

    private LinearLayout layout_newBalanceUi;
    private LinearLayout layout_oldBalanceUi;
    private TextView tv_balance2;
    private List<String> premissionsList2 = new ArrayList<>(); //权限集合，这里有个信用权限要单独处理
    private List<ConfigureBean.ListEntity> entityList;//配置集合
    private String creditScoreSwitch;//积分开放开关
    private TextView tv_incomeStatistics;
    private String mtype;

    private String serviceFeeSwitch ;
    private String type;

    public OutletsHomepageF() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.outlets_view_newhomepage, null);
        tv_notifications = (TextSwitcherView) view.findViewById(R.id.sysTemNotification);//滚动通知内容
        ll_noticeBar = (LinearLayout) view.findViewById(R.id.ll_noticeBar);//滚动通知栏
        main_gallery = (SmartImageView) view.findViewById(R.id.main_gallery); //轮播图
        main_gallery.setRatio(2.33f);
        outletsNewhomeSwipeRefresh = (MySwipeRefreshLayout) view.findViewById(R.id.outlets_newhome_swipeRefresh); //刷新控件
        ll_wallet_menu = (LinearLayout) view.findViewById(R.id.ll_wallet_menu); //钱包 菜单
        layout_wallet = (LinearLayout) view.findViewById(R.id.layout_wallet); //代售点 - 主页- 钱包
        v_white_line = view.findViewById(R.id.v_white_line); //线条
        layout_recharge = (LinearLayout) view.findViewById(R.id.layout_recharge);//代售点 - 主页- 缴费
        layout_receivables = (LinearLayout) view.findViewById(R.id.layout_receivables);//代售点 - 主页- 收款
        layout_withdrawals = (LinearLayout) view.findViewById(R.id.layout_withdrawals);//代售点 - 主页- 提现
//        layout_borrowMoney = (LinearLayout) view.findViewById(R.id.layout_borrowMoney);//代售点 - 主页- 借钱
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);// 代售点-主页-余额
        tv_balance2 = (TextView) view.findViewById(R.id.tv_balance2);// 代售点-主页-余额
        tv_lookMorePaysys = (TextView) view.findViewById(R.id.tv_lookMorePaysys);//代售点-主页-查看汇缴详情
        tv_shouldSum = (TextView) view.findViewById(R.id.tv_shouldSum);//应扣金额（元）
        tv_alreadySum = (TextView) view.findViewById(R.id.tv_alreadySum); //已扣金额（元）
        tv_debitStatus = (TextView) view.findViewById(R.id.tv_debitStatus);//扣款状态
        tv_date = (TextView) view.findViewById(R.id.tv_date);//今日汇缴日期
        rl_planeTicket = (RelativeLayout) view.findViewById(R.id.rl_planeTicket); //优惠购票
        rl_securitiesAccount = (RelativeLayout) view.findViewById(R.id.rl_securitiesAccount); ////代售点-主页-证券开户
        rl_ecoFarm = (RelativeLayout) view.findViewById(R.id.rl_ecoFarm); //代售点-主页-生态农场
        tv_lookMoreCapitalFlow = (TextView) view.findViewById(R.id.tv_lookMoreCapitalFlow);//代售点-主页-查看更多资金流水
        layout_paySysContentArea = (LinearLayout) view.findViewById(R.id.layout_paySysContentArea);//代售点 - 主页- 汇缴内容区域
        tv_noData = (TextView) view.findViewById(R.id.tv_noData);//代售点 - 主页- 汇缴内容暂无数据
        layout_capitalFlowArea = (LinearLayout) view.findViewById(R.id.layout_capitalFlowArea);//代售点 - 主页- 资金流水内容区域
        tv_tradeAmount = (TextView) view.findViewById(R.id.tv_tradeAmount);//交易金额（元）
        tv_tradeName = (TextView) view.findViewById(R.id.tv_tradeName); //交易方式
        tv_tradeStatus = (TextView) view.findViewById(R.id.tv_tradeStatus);//交易状态
        tv_tradeDate = (TextView) view.findViewById(R.id.tv_tradeDate); //今日账单日期
//        tv_noFlowData = (TextView) view.findViewById(R.id.tv_noFlowData);
        ll_payment = (LinearLayout) view.findViewById(R.id.ll_payment);
        ll_bill = (LinearLayout) view.findViewById(R.id.ll_bill);  //今日账单
        main_lin = (LinearLayout) view.findViewById(R.id.main_lin);
        tv_creditScore = (TextView) view.findViewById(R.id.tv_creditScore);//信用积分
        tv_creditRating = (TextView) view.findViewById(R.id.tv_creditRating);//信用等级
        layout_ojiCredit = (LinearLayout) view.findViewById(R.id.layout_ojiCredit);
//        tv_lookCredit = (TextView) view.findViewById(R.id.tv_lookCredit);//查看信用
        layout_newBalanceUi = (LinearLayout) view.findViewById(R.id.layout_newBalanceUi);
        layout_oldBalanceUi = (LinearLayout) view.findViewById(R.id.layout_oldBalanceUi);
        tv_incomeStatistics = (TextView) view.findViewById(R.id.tv_incomeStatistics);//收益统计

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initSP();
        initData();
        getConfigure();
    }

    private void initSP() {
        boolean userAgreement = (boolean) SPUtils.get(MyApplication.mContext, "userAgreement", false); //用户协议 第一次弹出后面都不弹出
        balanceStr = user.getBalance();
        minimumStr = user.getMinimum();  //最低限额 //在充值页面需要用到
        loginName = user.getLoginName();
        if (balanceStr != null && !balanceStr.isEmpty()) {
            Double balance = Double.valueOf(balanceStr);
            tv_balance.setText(String.format("%.2f", balance));
            tv_balance2.setText(String.format("%.2f", balance));
        }
//        if(!userAgreement) {
//                userAgreementDialog();
//        }
    }

    private void initWidget() {
        // 手机型号
        mtype = android.os.Build.MODEL;
        user = (User) readObject(MyApplication.mContext, "loginResult");
        premissionsList2 = user.getPremissions();

        for (String premissions : premissionsList2) {
            if (Premission.FB_CREDITSCORE_VIEW.equals(premissions)) {
                layout_newBalanceUi.setVisibility(View.VISIBLE); //如果有这个权限就显示
                layout_oldBalanceUi.setVisibility(View.GONE);
                break;
            } else {
                layout_newBalanceUi.setVisibility(View.GONE);//没有就隐藏
                layout_oldBalanceUi.setVisibility(View.VISIBLE);
            }
        }
        dateDay = DateUtils.getInstance().getToday();
        tv_date.setText(dateDay);//默认获取今日时间
        setPremissionShowHideView(Premission.FB_PAYMENT_VIEW, ll_payment);             //是否有显示今日汇缴数据菜单权限
        setPremissionShowHideView(Premission.ACCT_TRADEFLOW_VIEW, ll_bill);           //是否有显示今日账单里面是资金流水菜单权限
        setPremissionShowHideView(Premission.ACCT_TRADEFLOW_MYWALLET, ll_wallet_menu); //钱包菜单权限
        setPremissionShowHideView(Premission.ACCT_TRADEFLOW_ENCHASHMENT, layout_withdrawals); //提现权限
        setPremissionShowHideView(Premission.FB_CREDITSCORE_VIEW, layout_ojiCredit);    // 是否有信用积分权限
        //代售点主页刷新
        outletsNewhomeSwipeRefresh.setColorScheme(colors);
        outletsNewhomeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (outletsNewhomeSwipeRefresh.isRefreshing()) {
                    outletRefreshFlag = true;
                    try {
                        if (homepageCommon != null) {
                            homepageCommon.getMainNotificationList(ll_noticeBar, tv_notifications, cpd); //获取系统通知
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        layout_wallet.setOnClickListener(this);
        layout_recharge.setOnClickListener(this);
        layout_receivables.setOnClickListener(this);
        layout_withdrawals.setOnClickListener(this);
        tv_lookMorePaysys.setOnClickListener(this);
        rl_planeTicket.setOnClickListener(this);
        tv_lookMoreCapitalFlow.setOnClickListener(this);
        layout_ojiCredit.setOnClickListener(this);
        tv_incomeStatistics.setOnClickListener(this);

    }

    private void initData() {
        //系统通知
//根据业务去判断是否需要关闭加载框
        homepageCommon = new HomepageCommon(baseActivity) {
            @Override
            protected void mainNotificationCpdClose(CustomProgressDialog cpd, String state) {  //系统通知
                super.mainNotificationCpdClose(cpd, state);
                //根据业务去判断是否需要关闭加载框
                switch (state) {
                    case "0":
                        refreshBalance(false);  //刷新余额
                        break;
                    case "1":
                        cpd.dismiss();
                        outletsStopRefreshing();
                        refreshBalance(false);  //刷新余额  //网络异常 继续下个请求
                        break;
                }
            }
        };
        homepageCommon.transferControl(main_gallery, main_lin);
        homepageCommon.carouselFigure("1");
        homepageCommon.create();

        try {
            homepageCommon.getMainNotificationList(ll_noticeBar, tv_notifications, cpd); //获取系统通知
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getToken(true);
    }

    private void userAgreementDialog(final List<UnAgreeProtocolBean.DataBean> unAgreeProtocolList) {

        if (unAgreeProtocolList.size() > 0) {
//            String agreement = (String) get(MyApplication.mContext, "agreement", "");
            // String[] sa = agreement.split(",");
            LayoutInflater inflater = LayoutInflater.from(baseActivity);
            View view = inflater.inflate(R.layout.useragreement_dialog, null);
            ListView mListView = (ListView) view.findViewById(R.id.mListView);
            CommonAdapter<UnAgreeProtocolBean.DataBean> commonAdapter = new CommonAdapter<UnAgreeProtocolBean.DataBean>(MyApplication.mContext, unAgreeProtocolList, R.layout.item_unagree_protocol) {
                @Override
                public void convert(ViewHolder holder, final UnAgreeProtocolBean.DataBean dataBean) {
                    TextView tv_unAgreeProtocol = holder.getView(R.id.tv_unAgreeProtocol);
                    tv_unAgreeProtocol.setText("《" + dataBean.getName() + "》");
                    CheckBox cb_unAgreeProtocol = holder.getView(R.id.cb_unAgreeProtocol);
                    cb_unAgreeProtocol.setChecked(true);
                    cb_unAgreeProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                dataBean.setCheckState(true);
                            } else {
                                dataBean.setCheckState(false);
                            }
                        }
                    });
                    cb_unAgreeProtocol.setChecked(dataBean.isCheckState());
                }
            };

            mListView.setAdapter(commonAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    UnAgreeProtocolBean.DataBean dataBean = unAgreeProtocolList.get(i);
                    intent = new Intent(MyApplication.mContext, RegisterAgreeA.class);
                    intent.putExtra("url", Define.IMG_URL + dataBean.getUrl());
                    startActivityIntent(intent);
                }
            });

            /*final CheckBox cb_oultletsUse = (CheckBox) view.findViewById(R.id.cb_oultletsUse);
            final CheckBox cb_partnermall = (CheckBox) view.findViewById(R.id.cb_partnermall);
            final CheckBox cb_electronicWallet = (CheckBox) view.findViewById(R.id.cb_electronicWallet);
            TextView tv_oultletsUse = (TextView) view.findViewById(R.id.tv_oultletsUse);
            TextView tv_partnermall = (TextView) view.findViewById(R.id.tv_partnermall);
            TextView tv_electronicWallet = (TextView) view.findViewById(R.id.tv_electronicWallet);

            if (agreement.indexOf(Define.O_SERVICE_AGREEMENT) != -1) {// O计服务协议
                cb_oultletsUse.setEnabled(false);
            }

            if (agreement.indexOf(Define.ELECTRONIC_WALLET) != -1) {// 电子钱包协议
                cb_electronicWallet.setEnabled(false);
            }

            if (agreement.indexOf(Define.PARTNER_MALL) != -1) {// 伙伴商城协议
                cb_partnermall.setEnabled(false);
            }*/

            AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<String> idList = new ArrayList<String>();
                    //找出所有勾选的协议
                    List<UnAgreeProtocolBean.DataBean> list = new ArrayList<>();
                    for (int i = 0; i < unAgreeProtocolList.size(); ++i) {
                        UnAgreeProtocolBean.DataBean dataBean = unAgreeProtocolList.get(i);
                        if (dataBean.isCheckState()) {
                            list.add(dataBean);
                        }
                        idList.add(dataBean.getId());
                    }

                    if (list.size() == unAgreeProtocolList.size()) {
                        // dialog.dismiss();
                        String sId = "";
                        for (String id : idList) {
                            if (!sId.isEmpty()) sId += ",";
                            sId += id;
                        }

                        try {
                            requesAgreeProtocol(true, sId);//请求同意协议接口
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            // 将mShowing变量设为false，表示对话框已关闭
                            field.set(dialog, true);
                            dialog.dismiss();
                            //同意用户协议了 后第二次就不会进来了
                            SPUtils.put(MyApplication.mContext, "userAgreement", true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
//                        Toast.makeText(MyApplication.mContext, "需同意以上所有协议才能进行下面的操作!", Toast.LENGTH_SHORT).show();
                        ToastUtils.showToast(MyApplication.mContext, "需同意以上所有协议才能进行下面的操作!");
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            // 将mShowing变量设为false，表示对话框已关闭
                            field.set(dialog, false);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            });
            builder.setCancelable(false);
            builder.setView(view);

            AlertDialog dialog = builder.create();// 获取dialog
          /*  if (agreement.indexOf(Define.O_SERVICE_AGREEMENT) == -1 || agreement.indexOf(Define.ELECTRONIC_WALLET) == -1
                    || agreement.indexOf(Define.PARTNER_MALL) == -1) {
                dialog.show();
            }*/
            dialog.show();

        }

    }

   /* private void consentAgreement() throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = Define.URL + "user/updateAgreement";
        SessionJsonObjectRequest consentAgreementRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                ToastUtils.showToast(MyApplication.mContext,"您已同意O计平台所有协议!");
                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                startActivityMethod(LoginA.class);
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
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

            }
        });
        // 解决重复请求后台的问题
        consentAgreementRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        consentAgreementRequest.setTag("consentAgreement");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(consentAgreementRequest);

    }*/

    public void onWindowFocusChanged() {
        //地图引导设置界面
        boolean mapConfig = false;
        List<Integer> agreementList = (List<Integer>) ObjectSaveUtil.readObject(MyApplication.mContext, "agreementList");
        if (agreementList != null) {
            for (Integer tem : agreementList
                    ) {
                String s = String.valueOf(tem);
                if (s.equals("6")) {  //表示地图是否设置 6表示设置过了
                    mapConfig = true;
                    break;
                }
            }
        }

        if (!mapConfig) {  //需要加 ！
            myPopWindowConfigurationMap = new MyPopAbstract() {
                @Override
                protected void setMapSettingViewWidget(View view) {
                    TextView tv_setting_map = (TextView) view.findViewById(R.id.tv_setting_map); //现在设置
                    TextView tv_nosetting_map = (TextView) view.findViewById(R.id.tv_nosetting_map); //暂时不设置
                    tv_setting_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setMapDefaultSetting(1); ////引导默认 地图默认设置
                            myPopWindowConfigurationMap.dissmiss();
                        }
                    });
                    tv_nosetting_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setMapDefaultSetting(2);
                            myPopWindowConfigurationMap.dissmiss();
                        }
                    });
                }

                @Override
                protected int layout() {
                    return R.layout.map_configuration_hints;
                }
            }.poPwindow(baseActivity, true).showAtLocation(ll_noticeBar, Gravity.CENTER, 0, 0);

        } else {
            setMapData(2);  // 已经设置过了    现在只是获取地图数据  不会弹出引导页
        }
    }

    //引导默认 地图默认设置
    public void setMapDefaultSetting(final int i) {
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isDefault", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL + "fb/setGeogPostion";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(baseActivity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Log.i("isDefaultMap", "地图默认设置成功");

                //再次请求 地图数据
                setMapData(i);
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

        }, "isDefaultMap", true);
        volleyNetCommon.addQueue(jsonObjectRequest);

    }

    //获取地图数据
    private void setMapData(final int i) {
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        final VolleyNetCommon volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL + "user/queryUserInfo";
        final JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(baseActivity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                if (agencysList != null) {
                    agencysList.clear();
                }
                Gson gson = new Gson();
                User userAll = gson.fromJson(json.toString(), User.class);
                agencysList = userAll.getAgencys();
                getConfigurationMapData(userAll);

                if (i != 2) { //不等于2  表示暂不设置  就不要弹出地图引导页了
                    setMapPop();
                }

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

        }, "loaction_config", true);

        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    //获取主账号地图数据
    private void getConfigurationMapData(User userAll) {
        // 主账号地图信息
        List<User.DataBean> data = userAll.getData();
        if (data != null) {
            User.DataBean.OfficeBean office = data.get(0).getOffice();
            if (office != null) {
                User.AgencysBean agencysBean = new User.AgencysBean();
                agencysBean.setWinNumberX(office.getWinNumberX()); //主账号挂靠窗口号
                agencysBean.setOperatorNameX(office.getOperatorNameX()); //负责人姓名
                agencysBean.setOperatorMobileX(office.getOperatorMobileX()); //负责人手机
                agencysBean.setOperatorIdNumX(office.getOperatorIdNumX()); //负责人身份证
                agencysBean.setLat(office.getLat()); // 纬度
                agencysBean.setLng(office.getLng()); //经度
                agencysBean.setGeogPosition(office.getGeogPosition()); //地理位置名称
                agencysBean.setIdX(office.getIdX());  //挂靠代售点id
                agencysBean.setMainAddress(office.getAddressX());  //主账号地址     默认第一次用 mainAddrress 搜索  设置过后用 经纬度搜索
                agencysBean.setMainAddressFlag("mainFlag"); //标记表示是主账号
                mainCountId = office.getIdX(); //获取主账号id 用户判断是否 是不是挂靠窗口号
                allAgencysList.add(agencysBean);
            }
        }

        if (agencysList != null && agencysList.size() > 0) {
            allAgencysList.addAll(agencysList);
        }

    }

    //TODO 地图
    private void setMapPop() {

        myPopWindowSetting = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                lv_setting_map = (ListView) view.findViewById(R.id.lv_setting_map);
                setConfigurationMapDate();
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindowSetting.dissmiss();
                    }
                });

                tv_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cpd.show();
                        cpd.setCustomPd("正在加载中...");
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("isDefault", 1);
                            StringBuilder sb = new StringBuilder();
                            for (User.AgencysBean agency : allAgencysList
                                    ) {
                                JSONObject json = new JSONObject();  //二级json
                                json.put("geogPosition", agency.getGeogPosition());
                                json.put("lng", agency.getLng());
                                json.put("lat", agency.getLat());
                                String mainFlag = agency.getMainAddressFlag() == null ? "" : agency.getMainAddressFlag();
                                if (mainFlag.equals("mainFlag")) { //表示我是主账号
                                    jsonObject.put("mainAgency", json.toString());
                                } else {
                                    //我是挂靠窗口号
                                    json.put("agencyId", agency.getIdX());
                                    sb.append(json.toString() + ",");
                                }
                            }
                            StringBuilder replace = new StringBuilder();
                            if (sb.length() > 2) {
                                replace = sb.replace(sb.length() - 1, sb.length(), "");
                            }
                            replace.toString();
                            jsonObject.put("agencyList", "[" + replace.toString() + "]");
                            String url = Define.URL + "fb/setGeogPostion";
                            volleyNetCommon = new VolleyNetCommon();
                            JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(baseActivity) {
                                @Override
                                public void volleyResponse(Object o) {

                                }

                                @Override
                                public void volleyError(VolleyError volleyError) {
                                    Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                protected void netVolleyResponese(JSONObject json) {
                                    Toast.makeText(MyApplication.mContext, "地图位置设置成功！", Toast.LENGTH_SHORT).show();
                                    myPopWindowSetting.dissmiss();
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

                            }, "setSavaMap", true);

                            volleyNetCommon.addQueue(jsonObjectRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            protected int layout() {
                return R.layout.map_setting_pop;
            }
        }.poPwindow(baseActivity, true).showAtLocation(tv_notifications, Gravity.CENTER, 0, 0);

    }

    /**
     * 地图设置列表数据
     */
    private void setConfigurationMapDate() {

        agencysBeanCommonAdapter = new CommonAdapter<User.AgencysBean>(MyApplication.mContext, allAgencysList, R.layout.map_setting_pop_item) {
            @Override
            public void convert(ViewHolder holder, User.AgencysBean agencysBean) {
                TextView tv_windowNumbe_nick = holder.getView(R.id.tv_windowNumbe_nick);
                tv_windowNumbe_nick.setTag(agencysBean.getIdX());
                TextView tv_windowNumbe = holder.getView(R.id.tv_windowNumbe);//挂靠窗口号
                TextView tv_setting_consignee_name = holder.getView(R.id.tv_setting_consignee_name); //代售点名称
                TextView tv_setting_location = holder.getView(R.id.tv_setting_location); //地理位置
                if (mainCountId == agencysBean.getIdX()) {  //如果代售点主账号id一样就是主账号
                    if (tv_windowNumbe_nick.getTag() == agencysBean.getIdX()) {
                        tv_windowNumbe_nick.setText("主账号");
                    }
                    tv_windowNumbe.setText(agencysBean.getWinNumberX() == null ? "" : agencysBean.getWinNumberX());
                    tv_setting_consignee_name.setText("");
                    tv_setting_location.setText(agencysBean.getGeogPosition() == null ? "" : agencysBean.getGeogPosition());

                } else {
                    if (tv_windowNumbe_nick.getTag() == agencysBean.getIdX()) {
                        tv_windowNumbe_nick.setText("挂靠窗口号");
                    }
                    tv_windowNumbe.setText(agencysBean.getWinNumberX() == null ? "" : agencysBean.getWinNumberX());
                    tv_setting_consignee_name.setText(agencysBean.getAgencyName());
                    tv_setting_location.setText(agencysBean.getGeogPosition() == null ? "" : agencysBean.getGeogPosition());

                }
            }
        };

        lv_setting_map.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                ThreadCommonUtils.runonuiThread(new Runnable() {
                    @Override
                    public void run() {
                        User.AgencysBean agencysBean = allAgencysList.get(i);
                        Intent intent = new Intent(MyApplication.mContext, GeographicalPositionMapA.class);
                        intent.putExtra("AgencysBean", agencysBean);
                        intent.putExtra("listItemNumber", i);
                        startActivityForResult(intent, 21);
                        //	startActivity(intent);
                    }
                });
            }
        });

        lv_setting_map.setAdapter(agencysBeanCommonAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {   //表示地图位置保存 数据返回  TODO 21
            case 21:
                int itemNumber = data.getIntExtra("itemNumber", 0);
                User.AgencysBean agencysBean = (User.AgencysBean) data.getSerializableExtra("agencysBean");
                User.AgencysBean agencys = allAgencysList.get(itemNumber);
                agencys.setLat(agencysBean.getLat());
                agencys.setLng(agencysBean.getLng());
                agencys.setGeogPosition(agencysBean.getGeogPosition());
                agencysBeanCommonAdapter.notifyDataSetChanged();
                break;
            case 102:
                try {
                    refreshBalance(true);  //刷新余额
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 充值后刷新余额
     *
     * @throws Exception
     */
    public void refreshBalance(boolean off) {
        String url = Define.URL + "user/getBalance";
        requestNet(url, new JSONObject(), "getBalance", "0", off, true);
    }

    //今日汇缴请求
    public void requestMainPaySysData(boolean off) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time", dateDay);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Define.URL + "fb/paymentList";
        requestNet(url, jsonObject, "paymentList", "0", off, true);
    }

    /**
     * 获取用户信用
     */
    private void getCredit(boolean off) {
        String url = Define.URL + "user/getCredit";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginName", loginName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getCredit", "0", off, true);
    }

    /**
     * 获取Token
     */
    /*private void getToken(boolean off) {
        String url = Define.URL + "fb/token";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "getToken", "0", off);
    }*/

    /**
     * 今日账单
     *
     * @param off
     */
    public void requestMainOltCtFlowData(boolean off) {
        String url = Define.URL + "acct/tradeFlowList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pageNo", "1");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        requestNet(url, jsonObject, "tradeFlowList", "0", off, true);
    }

    /**
     * 获取未同意协议列表
     *
     * @param off
     */
    public void getUnAgreeProtocol(boolean off) {
        String url = Define.URL + "user/getUnAgreeProtocol";
        JSONObject jsonObject = new JSONObject();
        requestNet(url, jsonObject, "getUnAgreeProtocol", "0", off, true);
    }

    /**
     * 请求同意协议接口
     *
     * @param off
     */
    public void requesAgreeProtocol(boolean off, String ids) {
        String url = Define.URL + "user/agreeProtocol";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ids", ids);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "agreeProtocol", "0", off, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag) {
            case "getBalance":
                getBalance(json, controlOff);
                break;
            case "paymentList":
                getMainPaySysData(json, controlOff);
                break;

            case "getCredit":

                if (!controlOff) {
                    requestMainOltCtFlowData(false); //今日账单
                }
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    creditScore = jsonObject.optString("creditScore");
                    String creditLevelName = jsonObject.optString("creditLevelName");
                    if (creditLevelName != null) {
                        SPUtils2.putString(MyApplication.mContext, "creditName", creditLevelName);
                    } else {
                        SPUtils2.putString(MyApplication.mContext, "creditName", "----");
                    }

                    if (creditScore != null) {
                        EventBus.getDefault().postSticky(new CreditScoreEvent(creditScore));
                        tv_creditScore.setText(creditScore + "分");
                    }

                    if (creditLevelName != null) {
                        tv_creditRating.setText(creditLevelName);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "tradeFlowList":
                getMainOltCtFlowData(json, controlOff);
                break;
           /* case "getToken":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String token = jsonObject.getString("token");
                    if (token != null){
                        SPUtils2.putString(MyApplication.mContext, "token", token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;*/
            case "getUnAgreeProtocol":
//                Log.d("unAgreeProtocol", json.toString());
                handlerUnAgreeProtocol(json);
                break;

            case "agreeProtocol":
                ToastUtils.showToast(MyApplication.mContext, "您已同意O计平台所有协议!");
                break;
            case "getServiceFeeIsShow":
                getServicebut(json);
                break;
        }
    }

    private void getServicebut(JSONObject json){
        Gson gson = new Gson();
        ServiceFeeIsShowB serviceFeeIsShowB = gson.fromJson(json.toString() , ServiceFeeIsShowB.class);
        ServiceFeeIsShowB.ServiceFeeIsShowBean serviceFeeIsShowBean = (ServiceFeeIsShowB.ServiceFeeIsShowBean) serviceFeeIsShowB.getData();
        serviceFeeSwitch = serviceFeeIsShowBean.getServiceFeeSwitch();
        if ("receivables".equals(type)){
            if (mtype.contains("APOS")) {
                if ("close".equals(serviceFeeSwitch)){
                    intent = new Intent(MyApplication.mContext, AposPayA.class);
                    intent.putExtra("moneyType", "收款");
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else if ("open".equals(serviceFeeSwitch)){
                    intent = new Intent(MyApplication.mContext, NewAposPayA.class);
                    intent.putExtra("moneyType", "收款");
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }

            } else {
                if ("close".equals(serviceFeeSwitch)){
                    intent = new Intent(MyApplication.mContext, ReceivablesA.class);
                    //               intent.putExtra("balanceStr",balanceStr);
                    //               intent.putExtra("minimumStr",minimumStr);
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else if ("open".equals(serviceFeeSwitch) ){
                    intent = new Intent(MyApplication.mContext, NewReceivablesA.class);
                    intent.putExtra("balanceStr", balanceStr);
                    intent.putExtra("minimumStr", minimumStr);
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
            }
        } else if ("recharge".equals(type)) {
            if (mtype.contains("APOS")) {
                if ("close".equals(serviceFeeSwitch)) {
                    intent = new Intent(MyApplication.mContext, AposPayA.class);
                    intent.putExtra("moneyType", "缴款");
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else if ("open".equals(serviceFeeSwitch)){
                    intent = new Intent(MyApplication.mContext, NewAposPayA.class);
                    intent.putExtra("moneyType", "缴款");
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
            } else {
                if ("close".equals(serviceFeeSwitch)){
                    intent = new Intent(MyApplication.mContext, RechargeA.class);
                    intent.putExtra("balanceStr", balanceStr);
                    intent.putExtra("minimumStr", minimumStr);
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }else if ("open".equals(serviceFeeSwitch) ){
                    intent = new Intent(MyApplication.mContext, NewRechargeA.class);
                    intent.putExtra("balanceStr", balanceStr);
                    intent.putExtra("minimumStr", minimumStr);
                    startActivityForResult(intent, 102);
                    baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
            }
        }
    }

    private void handlerUnAgreeProtocol(JSONObject jSONObject) {
        Gson gson = new Gson();
        UnAgreeProtocolBean unAgreeProtocolBean = gson.fromJson(jSONObject.toString(), UnAgreeProtocolBean.class);
        List<UnAgreeProtocolBean.DataBean> dataBeenList = unAgreeProtocolBean.getData();
        if (dataBeenList.size() > 0) {
            userAgreementDialog(dataBeenList);
        }
    }

    @Override
    protected void closeDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.closeDismiss(cpd, tag, controlOff);
        switch (tag) {
            case "getBalance":
                if (controlOff) {
                    cpd.dismiss();
                    outletsStopRefreshing();
                }
                break;
            case "paymentList":
                if (controlOff) {
                    cpd.dismiss();
                    outletsStopRefreshing();
                }
                break;
            case "tradeFlowList":
                if (controlOff) {
                    cpd.dismiss();
                    outletsStopRefreshing();
                }
                break;

            case "getCredit":
                if (controlOff) {
                    cpd.dismiss();
                    outletsStopRefreshing();
                }
                break;

            case "getUnAgreeProtocol":
                if (controlOff) {
                    cpd.dismiss();
                    outletsStopRefreshing();
                }
                break;

            case "agreeProtocol":
                if (controlOff) {
                    cpd.dismiss();
                    outletsStopRefreshing();
                }
                break;

        }
    }

    /**
     * 上拉刷新控件关闭
     */
    @Override
    protected void loadControlExceptionDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.loadControlExceptionDismiss(cpd, tag, controlOff);
        switch (tag) {
            case "getBalance":  //网络异常接着下一个请求
                if (!controlOff) {
                    requestMainPaySysData(false);
                }
                break;
            case "paymentList":
                if (!controlOff) {
                    getCredit(false); //获取信用分请求
                }
                break;

            case "getCredit":
                if (!controlOff) {
                    requestMainOltCtFlowData(false); //今日账单请求
                }
                break;

            case "tradeFlowList":
                if (!controlOff) {
                    getUnAgreeProtocol(true); //今日账单请求
                }
                break;

        }
        outletsStopRefreshing();
    }

    //账户余额
    private void getBalance(JSONObject json, boolean controlOff) {
        if (!controlOff) { //表示 这个请求是可以直接关闭的 所以如果等于true 就不进行下一个请求了
            requestMainPaySysData(false);
        }
        balanceStr = json.optString("data");        //账户余额
        minimumStr = json.optString("minimum");    //最低限额  //在充值页面需要用到
        if (!"".equals(balanceStr) && balanceStr != null) {
            Double balance2 = Double.valueOf(balanceStr);
            tv_balance.setText(String.format("%.2f", balance2));
            tv_balance2.setText(String.format("%.2f", balance2));
        }
    }

    //代售点
    public void outletsStopRefreshing() {
        //刷新完成关闭动画
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (outletsNewhomeSwipeRefresh != null) {
                    outletsNewhomeSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    String alreadySum = "";
    String shouldSum = "";

    private void getMainPaySysData(JSONObject json, boolean controlOff) {
        List<PaySystemBean> paySysData = new ArrayList<PaySystemBean>();
        if (!controlOff) {
            getCredit(false); //获取信用
        }
        int num = 0;
        try {
            num = json.getInt("size");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (num == 0) {
           /* layout_paySysContentArea.setVisibility(View.GONE);
            tv_noData.setVisibility(View.VISIBLE);*/
            tv_shouldSum.setText("暂无信息");
            tv_shouldSum.setTextColor(Color.parseColor("#fc6e22"));
            tv_alreadySum.setText("暂无信息");
            tv_debitStatus.setText("暂无信息");
            tv_date.setText(dateDay);
            return;
        }

        try {
            JSONArray jsonArray = json.getJSONArray("data");
            num = Math.min(num, jsonArray.length());
            // for (int i = 0; i < jsonArray.length(); i++)
            // {
            for (int i = 0; i < num; i++) {
                PaySystemBean paySysBean = new PaySystemBean();

                JSONObject paySysObj = (JSONObject) jsonArray.get(i);
                String date = paySysObj.optString("date");// 票款时间
                String createDate = paySysObj.optString("createDate");//汇缴时间
                String officeCode = paySysObj.optString("officeCode");// 机构编码
                String agentName = paySysObj.optString("agentName");// 代售点名称
                String shouldAmount = paySysObj.optString("shouldAmountString");// 应扣金额
                String alreadyAmount = paySysObj.optString("alreadyAmountString");// 已扣金额
                String withholdStatus = paySysObj.optString("withholdStatus");// 扣款状态
                String railwayStationName = paySysObj.optString("railwayStationName");//火车站名称
                String railwayStationId = paySysObj.optString("railwayStationId");//火车站id
                paySysBean.setDate(date);
                paySysBean.setCreateDate(createDate);
                paySysBean.setOfficeCode(officeCode);
                // oltsRelation.setArea(area);
                paySysBean.setAgentName(agentName);
                paySysBean.setShouldAmount(shouldAmount);
                paySysBean.setAlreadyAmount(alreadyAmount);
                paySysBean.setWithholdStatus(withholdStatus);
                paySysBean.setRailwayStationName(railwayStationName);
                paySysBean.setRailwayStationId(railwayStationId);
                Log.d("debug", paySysBean.getDate());
                paySysData.add(paySysBean);
            }

        /*    for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject paySysObj = (JSONObject) jsonArray.get(i);
                alreadySum = paySysObj.optString("alreadySumString");// 已扣总金额
                shouldSum = paySysObj.optString("shouldSumString");// 应扣总金额
            }*/

            PaySystemBean paySystemBean = paySysData.get(0);
            String shouldSum = paySystemBean.getShouldAmount();
            String alreadySum = paySystemBean.getAlreadyAmount();
            String debitStatus = paySystemBean.getWithholdStatus();
            String date = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(paySystemBean.getCreateDate()));
            String today = DateUtils.getInstance().getToday();//获取今日日期
            if (today.equals(date)) {
                tv_shouldSum.setText(shouldSum);
                tv_shouldSum.setTextColor(Color.parseColor("#4D90E7"));
                tv_alreadySum.setText(alreadySum);
                if (Define.CHARG_SUCCESS.equals(debitStatus)) {
                    tv_debitStatus.setText(R.string.charg_success);//"扣款成功"
                } else if (Define.CHARG_WAIT.equals(debitStatus)) {
                    tv_debitStatus.setText(R.string.charg_wait); //"等待扣款"
//											tv_debitStatus.setBackgroundResource(R.drawable.hollow_red);
                } else if (Define.CHARG_FAIL.equals(debitStatus)) {
                    tv_debitStatus.setText(R.string.charg_fail);  //"扣款失败"
//											tv_debitStatus.setBackgroundResource(R.drawable.hollow_red);
                } else if (Define.CHARG_RECHARGE.equals(debitStatus)) {
                    tv_debitStatus.setText(R.string.charg_recharge); //"待充值"
                } else if (Define.CHARG_RECHARGE_SUCCESS.equals(debitStatus)) {
                    tv_debitStatus.setText(R.string.charg_recharge_success); //"补缴成功"
                }

                tv_date.setText(date);
            } else {
                tv_shouldSum.setText("暂无信息");
                tv_shouldSum.setTextColor(Color.parseColor("#fc6e22"));
                tv_alreadySum.setText("暂无信息");
                tv_debitStatus.setText("暂无信息");
                tv_date.setText(today);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    List<OltsCapitalFlowBean> mainCtFlowData = new ArrayList<OltsCapitalFlowBean>();

    /**
     * 今日账单
     *
     * @param jsonObject
     * @param off
     */
    public void getMainOltCtFlowData(JSONObject jsonObject, boolean off) {
        if (!off) {
            getUnAgreeProtocol(true); //获取未读协议
        }
        mainCtFlowData.clear();
        try {
            int num = jsonObject.getInt("size");
            if (num == 0) {
//                layout_capitalFlowArea.setVisibility(View.GONE);
//                tv_noFlowData.setVisibility(View.VISIBLE);
                tv_tradeAmount.setText("暂无信息");
                tv_tradeAmount.setTextColor(Color.parseColor("#fc6e22"));
                tv_tradeName.setText("暂无信息");
                tv_tradeStatus.setText("暂无信息");
                return;
            } else {
//                layout_capitalFlowArea.setVisibility(View.VISIBLE);
//                tv_noFlowData.setVisibility(View.GONE);

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                num = Math.min(num, jsonArray.length());
                // for (int i = 0; i < jsonArray.length(); i++)
                // {
                for (int i = 0; i < num; i++) {
                    OltsCapitalFlowBean cptlFlowBean = new OltsCapitalFlowBean();

                    JSONObject cptlFlowObj = (JSONObject) jsonArray.get(i);
                    String flowId = cptlFlowObj.optString("flowId");// 交易流水号
                    String tradeName = cptlFlowObj.optString("tradeName");// 名称
                    JSONObject tradeUser = cptlFlowObj.optJSONObject("tradeUser");// 交易的用户信息
                    String curBalance = cptlFlowObj.optString("curBalanceString");// 用户当前余额
                    String loginName = tradeUser.optString("loginName");//交易账户-用户名
                    String name = tradeUser.optString("name");//用户姓名

                    // 用optString()解析字段可以防止字段没有值时抛异常
                    String tradeAcct = cptlFlowObj.optString("tradeAcct");// 交易账号
                    if ("".equals(tradeAcct) || tradeAcct == null) {
                        tradeAcct = "无";
                    }
                    String tradeFee = cptlFlowObj.optString("tradeFeeString");// 交易金额

                    String type = cptlFlowObj.optString("type");// 交易类型
                    // income:收入
                    // payout:支出
                    String tradeStatus = cptlFlowObj.optString("tradeStatus");// 交易状态
                    String tradeDate = cptlFlowObj.optString("tradeDate");// 交易日期

                    cptlFlowBean.setFlowId(flowId);
                    cptlFlowBean.setTradeName(tradeName);
                    cptlFlowBean.setLoginName(loginName);
                    cptlFlowBean.setName(name);
                    // oltsRelation.setArea(area);
                    cptlFlowBean.setTradeAcct(tradeAcct);
                    cptlFlowBean.setTradeFee(tradeFee);
                    cptlFlowBean.setCurBalance(curBalance);
                    cptlFlowBean.setType(type);
                    cptlFlowBean.setTradeStatus(tradeStatus);
                    cptlFlowBean.setTradeDate(tradeDate);
                    Log.d("debug", cptlFlowBean.getFlowId());
                    mainCtFlowData.add(cptlFlowBean);
                }
                OltsCapitalFlowBean mainctlFlowBean = mainCtFlowData.get(0);
                String tradeFee = mainctlFlowBean.getTradeFee();
                String tradeName = mainctlFlowBean.getTradeName();
                String tradeStatus = mainctlFlowBean.getTradeStatus();
                String tradeDate = mainctlFlowBean.getTradeDate();
                tv_tradeAmount.setText(tradeFee);
                tv_tradeAmount.setTextColor(Color.parseColor("#4D90E7"));
                tv_tradeName.setText(tradeName);
                tv_tradeDate.setText(tradeDate);

               /* if (Define.TRADE_STATUS1.equals(tradeStatus)) {
                    tv_tradeStatus.setText("交易成功");
                }else if(Define.TRADE_STATUS2.equals(tradeStatus)){
                    tv_tradeStatus.setText("等待付款");
                }else if(Define.TRADE_STATUS3.equals(tradeStatus)){
                    tv_tradeStatus.setText("交易结束");
                }else if(Define.TRADE_STATUS4.equals(tradeStatus)){
                    tv_tradeStatus.setText("交易关闭");
                }else if (Define.TRADE_STATUS5.equals(tradeStatus)) {
                    tv_tradeStatus.setText("等待处理");
                }else if (Define.TRADE_STATUS6.equals(tradeStatus)) {
                    tv_tradeStatus.setText("等待收款");
                }else if (Define.TRADE_STATUS7.equals(tradeStatus)) {
                    tv_tradeStatus.setText("交易失败");
                }*/

                if (Define.ACCT_TRADE_STATUS_WAIT.equals(tradeStatus)) {
                    tv_tradeStatus.setText("等待付款");
                } else if (Define.ACCT_TRADE_STATUS_SUSPENSE.equals(tradeStatus)) {
                    tv_tradeStatus.setText("等待处理");
                } else if (Define.ACCT_TRADE_STATUS_FAIL.equals(tradeStatus)) {
                    tv_tradeStatus.setText("交易失败");
                } else if (Define.ACCT_TRADE_STATUS_CLOSED.equals(tradeStatus)) {
                    tv_tradeStatus.setText("交易关闭");
                } else if (Define.ACCT_TRADE_STATUS_PENDING.equals(tradeStatus)) {
                    tv_tradeStatus.setText("等待收款");
                } else if (Define.ACCT_TRADE_STATUS_SUCCESS.equals(tradeStatus)) {
                    tv_tradeStatus.setText("交易成功");
                } else if (Define.ACCT_TRADE_STATUS_FINISHED.equals(tradeStatus)) {
                    tv_tradeStatus.setText("交易结束");
                } else if (Define.ACCT_TRADE_STATUS_REFUNDING.equals(tradeStatus)) {
                    tv_tradeStatus.setText("退款中"); //退款中
                } else if (Define.ACCT_TRADE_STATUS_REFUND.equals(tradeStatus)) {
                    tv_tradeStatus.setText("已退款");
                } else if (Define.ACC_TRADE_STATUS_REFUND_FAIL.equals(tradeStatus)) {
                    tv_tradeStatus.setText("退款失败");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断缴款/收款是否展示新界面开关接口
     */
    private void getServiceFeeIsShow(){
        String url = Define.URL + "acct/getServiceFeeIsShow";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginName", loginName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getServiceFeeIsShow", "0", true, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_wallet:  //我的钱包
                intent = new Intent(MyApplication.mContext, WalletA.class);
                startActivityForResult(intent, 102);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            //代售点-主页-缴费
            case R.id.layout_recharge:
                type = "recharge";
                getServiceFeeIsShow();
                break;
            //代售点-主页-收款
            case R.id.layout_receivables:
                type = "receivables";
                getServiceFeeIsShow();
                break;
            //代售点-主页-提现
            case R.id.layout_withdrawals:
                startActivityForResult(new Intent(MyApplication.mContext, CashA.class), 102);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            //代售点-主页-借钱
           /*case R.id.layout_borrowMoney:
               Toast.makeText(MyApplication.mContext, "正在建设中！", Toast.LENGTH_SHORT).show();
               break;*/
            //代售点-主页-查看更多汇缴数据
            case R.id.tv_lookMorePaysys:
                intent = new Intent(MyApplication.mContext, PaySystemA.class);
                intent.putExtra("type", "1");
                startActivityIntent(intent);
                break;
            //代售点-主页-机票
            case R.id.rl_planeTicket:
                loadePlaneTicket();
                break;
            //代售点-主页-查看更多资金流水
            case R.id.tv_lookMoreCapitalFlow:
                try {
                    getOltCapitalFlowData();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
                break;
            case R.id.layout_ojiCredit:
                if (entityList != null) {
                    for (ConfigureBean.ListEntity entity :
                            entityList) {
                        if (entity.getCreditScoreSwitch() != null) {
                            creditScoreSwitch = entity.getCreditScoreSwitch();
                            break;
                        }
                    }
                }
                if ("close".equals(creditScoreSwitch)) {
                    ToastUtils.showToast(MyApplication.mContext, "积分功能尚未开放！");
                } else {
                    intent = new Intent(MyApplication.mContext, OjiCreditA.class);
                    intent.putExtra("creditScore", creditScore);
                    startActivityIntent(intent);
                }
                break;
//           case R.id.tv_lookCredit:
//               intent = new Intent(MyApplication.mContext, OjiCreditA.class);
//               intent.putExtra("creditScore",creditScore);
//               startActivityIntent(intent);
//               break;
            case R.id.tv_incomeStatistics:
                intent = new Intent(MyApplication.mContext, IncomeStatisticsA.class);
//               intent.putExtra("creditScore",creditScore);
                startActivityIntent(intent);
                break;
        }
    }

    private static final String EPOS_MIN_AMOUNT = "epos_min_amount"; //pos充值最低金额tag

    //pos机最低限额
    private void getConfigure() {
//		cpd.setCustomPd("正在加载中...");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", "epos_min_amount");
            JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "sys/getAppProperty", jsonObject, new VolleyAbstract(baseActivity) {
                @Override
                public void volleyResponse(Object o) {

                }

                @Override
                public void volleyError(VolleyError volleyError) {
                    Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected void netVolleyResponese(JSONObject json) {
                    Gson gson = new Gson();
                    ConfigureBean configureBean = gson.fromJson(json.toString(), ConfigureBean.class);
                    entityList = configureBean.getList();
                    ObjectSaveUtil.saveObject(MyApplication.mContext, "ListEntity", entityList); //配置集合
                }

                @Override
                protected void PdDismiss() {
                }

                @Override
                protected void errorMessages(String message) {
                    super.errorMessages(message);
                    Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                }
            }, EPOS_MIN_AMOUNT, true);
            volleyNetCommon.addQueue(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void loadePlaneTicket() {
        //  User user = (User) readObject(MyApplication.mContext, "loginResult");
        Map<String, String> parameters = new TreeMap<String, String>();
        parameters.put("service", "user_login");
        parameters.put("partner", "CSX_A0180386247");
        parameters.put("outer_app_token", "CSX_A0180386247_subagency_admin");
        parameters.put("outer_login_name", user.getUserCode());
        parameters.put("user_name", user.getName());
        parameters.put("goto_url", "http://www.1203688.com/caigou/manage/index.in?isLogin=true");
        parameters.put("user_type", "AGENCY_SINGLE_USER");
        parameters.put("return_url", "http://www.1203688.com");
        parameters.put("time_stamp", String.valueOf(System.currentTimeMillis()));
        parameters.put("input_charset", "utf-8");
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(parameters);
        //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String prestr = AlipayCore.createLinkString(sPara);
        Log.d("debug", "prestr=" + prestr);
        String mysign = MD5.sign(prestr, "tdts_@(51).", "utf-8");
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", "MD5");

        StringBuffer sb = new StringBuffer();
        @SuppressWarnings("rawtypes") Set es = sPara.entrySet();
        @SuppressWarnings("rawtypes") Iterator it = es.iterator();
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (null != value && !"".equals(value)) {
                try {
                    sb.append(k + "=" + URLEncoder.encode(value, "utf-8") + "&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        String reqPars = sb.substring(0, sb.lastIndexOf("&"));

        String url = "http://aio.51book.com/partner/cooperate.in?" + reqPars;
        Log.d("debug", "url=" + url);

	/*	Intent intent = new Intent(this, TicketA.class);
		intent.putExtra("url",url);
		startActivity(intent);*/

//        String sessionId = MyApplication.preferences.getString("JSESSIONID", "");
//		webView.loadUrl("http://www.1203688.com/caigou/manage/index.in?isLogin=true&fromTMS=1&global_token=EtCElrYwzf6MPkihsxm1Ng%3D%3D&ltywfz=0&gt_sc=L0M0THZLKKGHEVL");
//        webView.loadUrl(url);
//		ExitApplication.getInstance().addActivity(this);
        intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    /**
     * 获取代售点资金流水
     *
     * @throws Exception
     */
    private void getOltCapitalFlowData() throws Exception {
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
            jsonObject.put("pageNo", "1");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = Define.URL + "acct/tradeFlowList";
        final List<OltsCapitalFlowBean> capitalFlowData = new ArrayList<OltsCapitalFlowBean>();
        SessionJsonObjectRequest capitalFlowRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            Toast.makeText(MyApplication.mContext, "服务器数据异常", Toast.LENGTH_SHORT).show();
                            cpd.dismiss();
                            return;
                        }
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                int num = response.getInt("size");
                                JSONArray jsonArray = response.getJSONArray("data");
                                num = Math.min(num, jsonArray.length());
                                // for (int i = 0; i < jsonArray.length(); i++)
                                // {
                                double incomeCount = 0;// 总收入
                                double payoutCount = 0; //总支出
                                for (int i = 0; i < num; i++) {
                                    OltsCapitalFlowBean cptlFlowBean = new OltsCapitalFlowBean();

                                    JSONObject cptlFlowObj = (JSONObject) jsonArray.get(i);
                                    String flowId = cptlFlowObj.optString("flowId");// 交易流水号
                                    String tradeName = cptlFlowObj.optString("tradeName");// 名称
                                    JSONObject tradeUser = cptlFlowObj.optJSONObject("tradeUser");// 交易的用户信息
                                    String curBalance = cptlFlowObj.optString("curBalanceString");// 用户当前余额
                                    String loginName = tradeUser.optString("loginName");//交易账户-用户名
                                    String name = tradeUser.optString("name");//用户姓名

                                    // 用optString()解析字段可以防止字段没有值时抛异常
                                    String tradeAcct = cptlFlowObj.optString("tradeAcct");// 交易账号
                                    if ("".equals(tradeAcct) || tradeAcct == null) {
                                        tradeAcct = "无";
                                    }
                                    String tradeFee = cptlFlowObj.optString("tradeFeeString");// 交易金额

                                    String type = cptlFlowObj.optString("type");// 交易类型
                                    // income:收入
                                    // payout:支出
                                    String tradeStatus = cptlFlowObj.optString("tradeStatus");// 交易状态
                                    String tradeDate = cptlFlowObj.optString("tradeDate");// 交易日期
                                    String channelType = cptlFlowObj.optString("channelType"); //交易渠道
                                    String expireTime = cptlFlowObj.optString("expireTime"); // 超时时间
                                    cptlFlowBean.setFlowId(flowId);
                                    cptlFlowBean.setTradeName(tradeName);
                                    cptlFlowBean.setLoginName(loginName);
                                    cptlFlowBean.setName(name);
                                    // oltsRelation.setArea(area);
                                    cptlFlowBean.setTradeAcct(tradeAcct);
                                    cptlFlowBean.setTradeFee(tradeFee);
                                    cptlFlowBean.setCurBalance(curBalance);
                                    cptlFlowBean.setType(type);
                                    cptlFlowBean.setTradeStatus(tradeStatus);
                                    cptlFlowBean.setTradeDate(tradeDate);
                                    cptlFlowBean.setChannelType(channelType);
                                    cptlFlowBean.setExpireTime(expireTime);
                                    Log.d("debug", cptlFlowBean.getFlowId());
                                    capitalFlowData.add(cptlFlowBean);


                                    // 收入总金额  交易失败不加 退款中不加，等待付款不加，已退款不加
                                    //支出总金额  一样
                                    if (Define.INCOME.equals(type)) {
                                        if (!(Define.ACCT_TRADE_STATUS_FAIL.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUNDING.equals(tradeStatus)
                                                || Define.ACCT_TRADE_STATUS_WAIT.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUND.equals(tradeStatus))) {
                                            incomeCount += parseDouble(tradeFee);
                                        }
                                    } else if (Define.PAYOUT.equals(type)) {
                                        if (!(Define.ACCT_TRADE_STATUS_FAIL.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUNDING.equals(tradeStatus)
                                                || Define.ACCT_TRADE_STATUS_WAIT.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUND.equals(tradeStatus))) {
                                            payoutCount += parseDouble(tradeFee);
                                        }

                                    }
                                }

                                intent = new Intent(MyApplication.mContext, CapitalFlowA.class);
                                Bundle bundle = new Bundle();

                                bundle.putSerializable("capitalFlowData", (Serializable) capitalFlowData);
                                bundle.putDouble("payoutCount", payoutCount);
                                bundle.putDouble("incomeCount", incomeCount);
                                // bundle.putSerializable("msgBean",
                                // oltMsgBean);
                                intent.putExtras(bundle);
                                cpd.dismiss();
                                startActivityIntent(intent);

                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(MyApplication.mContext, LoginA.class);
                                startActivityIntent(intent);

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
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
            }
        });
        // 解决重复请求后台的问题
        capitalFlowRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        capitalFlowRequest.setTag("capitalFlowMore");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(capitalFlowRequest);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

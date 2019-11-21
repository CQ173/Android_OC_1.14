package com.huoniao.oc.financial;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
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
import com.huoniao.oc.bean.NewPaymentPaySumListBean;
import com.huoniao.oc.bean.PaymentPayTemBean;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.myOnTabSelectedListener;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
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

public class NewAdvanceInCashA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.v_backgroud)
    View vBackgroud;
    @InjectView(R.id.v_backgroud2)
    View vBackgroud2;
    @InjectView(R.id.tb_layout)
    TabLayout tbLayout;
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
    @InjectView(R.id.et_searchContent)
    EditText etSearchContent;
    @InjectView(R.id.tv_query)
    TextView tvQuery;
    @InjectView(R.id.ll_paymentNumber)
    LinearLayout llPaymentNumber;
    @InjectView(R.id.tv_recordCount)
    TextView tvRecordCount;
    @InjectView(R.id.mPullToRefreshListView)
    PullToRefreshListView mPullToRefreshListView, mPullToRefreshListView2;
    @InjectView(R.id.ll_detailed)
    LinearLayout llDetailed;

    private String type = "0";
    private ListView mListView, mListView2;
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private String startDate = "";
    private String endDate = "";
    private String next = "";        //返回来的页数
    private String tradeNumber = "";
    private CommonAdapter<PaymentPayTemBean.DataBean> commonAdapter2;
    private  List<PaymentPayTemBean.DataBean> paymentPayList = new ArrayList<>();

    private CommonAdapter<NewPaymentPaySumListBean.DataBean> commonAdapter;
    private  List<NewPaymentPaySumListBean.DataBean> paymentPaySumList = new ArrayList<>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advance_in_cash);
        ButterKnife.inject(this);
        initWidget();
        initData();

    }

    private void initData() {
        startDate = DateUtils.getOldDate(-2);//获取前两天的日期
        endDate = DateUtils.getTime();
        tvStartDate.setText(startDate);
        tvEndDate.setText(endDate);
        getPaymentPayTemSumList("1");

    }


    private void initWidget() {
        llPaymentNumber.setVisibility(View.GONE);
        tbLayout.addTab(tbLayout.newTab().setText("垫款统计"));
        switchBackgroud(vBackgroud);
        tbLayout.addTab(tbLayout.newTab().setText("垫款明细"));
        setIndicator(tbLayout, 30, 30);

        tbLayout.setOnTabSelectedListener(new myOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中了tab的逻辑
                switch (tab.getPosition()) {
                    case 0:  //垫款统计
                        switchBackgroud(vBackgroud);
                        type = "0";

                        llPaymentNumber.setVisibility(View.GONE);
//                        if (myListData != null){
//                            myListData.clear();
//                        }
                        getPaymentPayTemSumList("1");
                        break;
                    case 1:  //垫款明细
                        switchBackgroud(vBackgroud2);
                        type = "1";
                        llPaymentNumber.setVisibility(View.VISIBLE);
//                        if (myListData != null){
//                            myListData.clear();
//                        }
                        getPaymentPayTemList("1");
                        break;
                }
            }

        });

        if (myDatePickerDialog == null) {
            myDatePickerDialog = new MyDatePickerDialog();
        }

        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mPullToRefreshListView2.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView2.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView2.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullToRefreshListView2.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");

        mListView = mPullToRefreshListView.getRefreshableView();
        mListView2 = mPullToRefreshListView2.getRefreshableView();

        initPullRefreshLinstener();
    }



    /**
     * //上拉加载更多
     */
    private void initPullRefreshLinstener() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {  //上拉加载更多
                if (next.equals("-1")) {
//                    Toast.makeText(AdvanceInCashA.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ToastUtils.showToast(NewAdvanceInCashA.this, "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                } else {
                    if ("0".equals(type)) {
                        getPaymentPayTemSumList(next);
                    }else if ("1".equals(type)){
                        getPaymentPayTemList(next);
                    }
                }
            }
        });
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

    @OnClick({R.id.iv_back, R.id.ll_start_date, R.id.ll_end_date, R.id.tv_query})
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
                                Toast.makeText(NewAdvanceInCashA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvStartDate.setText(date);
                        if ("0".equals(type)) {
                            getPaymentPayTemSumList("1");
                        }else if ("1".equals(type)){
                            getPaymentPayTemList("1");
                        }
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
                                Toast.makeText(NewAdvanceInCashA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvEndDate.setText(date);

                        if ("0".equals(type)) {
                            getPaymentPayTemSumList("1");
                        }else if ("1".equals(type)){
                            getPaymentPayTemList("1");
                        }
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.tv_query:

                if ("0".equals(type)) {
                    getPaymentPayTemSumList("1");
                }else if ("1".equals(type)){
                    getPaymentPayTemList("1");
                }
                break;
        }
    }


    /**
     * 请求垫款统计列表
     */
    public void getPaymentPayTemSumList(String pageNo) {
        tradeNumber = etSearchContent.getText().toString().trim();
        String url = Define.URL + "acct/getPaymentPayTemSumList";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("pageNo",pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "getPaymentPayTemSumList", pageNo, true, false);
    }


    /**
     * 请求垫款明细列表
     */
    public void getPaymentPayTemList(String pageNo) {
        tradeNumber = etSearchContent.getText().toString().trim();
        String url = Define.URL + "acct/getPaymentPayTemList";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("tradeNumber",tradeNumber);
            jsonObject.put("pageNo",pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "getPaymentPayTemList", pageNo, true, false);
    }

    /**
     * 请求重新付款
     */
    public void requestPaymentPayTemPayAgain(String paymentPayTemId) {
        tradeNumber = etSearchContent.getText().toString().trim();
        String url = Define.URL + "acct/paymentPayTemPayAgain";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("paymentPayTemId", paymentPayTemId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "paymentPayTemPayAgain", "1", true, false);
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getPaymentPayTemSumList":
                Log.d("getPaymentPayTemSumList", json.toString());
                setSumAdapter(json, pageNumber);
                break;

            case "getPaymentPayTemList":
                Log.d("getPaymentPayTemList", json.toString());
                setDetailedAdapter(json, pageNumber);
                break;
            case "paymentPayTemPayAgain":
                Log.d("paymentPayTemPayAgain", json.toString());
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String result = jsonObject.optString("result");
                    if ("success".equals(result)){ //重新付款成功后刷新列表
                        getPaymentPayTemList("1");
                    } else {
                        String message = jsonObject.optString("msg");
                        ToastUtils.showToast(NewAdvanceInCashA.this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setDetailedAdapter(JSONObject jsonObject, String pageNo) {
        Gson gson = new Gson();
        PaymentPayTemBean paymentPayTemBean = gson.fromJson(jsonObject.toString(), PaymentPayTemBean.class);

        if (pageNo.equals("1")) {
            //集成清空处理
            paymentPayList.clear();
            if (commonAdapter2 != null) {
                mListView2.setAdapter(commonAdapter2);
            }
        }
        List<PaymentPayTemBean.DataBean> dataBeenList = paymentPayTemBean.getData();

        if (dataBeenList != null && dataBeenList.size() > 0) {
            paymentPayList.addAll(dataBeenList);
        }
        next = String.valueOf(paymentPayTemBean.getNext());
        tvRecordCount.setText(paymentPayTemBean.getSize() + "");
        if (commonAdapter2 == null) {
            commonAdapter2 = new CommonAdapter<PaymentPayTemBean.DataBean>(NewAdvanceInCashA.this, paymentPayList, R.layout.item_advanceincash_mingxi) {
                @Override
                public void convert(ViewHolder holder, PaymentPayTemBean.DataBean dataBean) {
                    holder.setText(R.id.tv_paymentNumber, dataBean.getTradeNumber())
                            .setText(R.id.tv_state, dataBean.getStateName())
                            .setText(R.id.tv_date, dataBean.getCreateDate())
                            .setText(R.id.tv_shouldPayMoney, dataBean.getShouldPayAmountString() + "元")
                            .setText(R.id.tv_realPaymentMoney, dataBean.getActualPayAmountString() + "元")
                            .setText(R.id.tv_zxShouldPayMoney, dataBean.getZxActualPayAmountString() + "元")
                            .setText(R.id.tv_myselfRealPayMoney, dataBean.getOwnActualPayAmountString() + "元");

                    final String id = dataBean.getId();
                    String state = dataBean.getState();
                    TextView tv_rePayment = holder.getView(R.id.tv_rePayment);

                    if ("1".equals(state)){
                        tv_rePayment.setVisibility(View.VISIBLE);
                    }else {
                        tv_rePayment.setVisibility(View.GONE);
                    }


                    tv_rePayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (RepeatClickUtils.repeatClick()) {
                                requestPaymentPayTemPayAgain(id);
                            }
                        }
                    });
                }
            };
            mListView2.setAdapter(commonAdapter2);
        }
        commonAdapter2.notifyDataSetChanged();
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PaymentPayTemBean.DataBean dataBean = paymentPayList.get(position - 1);
                ObjectSaveUtil.saveObject(NewAdvanceInCashA.this, "paymentPayTemList", dataBean);
                intent = new Intent(NewAdvanceInCashA.this, StationPaymentA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

            }
        });
    }

    private void setSumAdapter(JSONObject jsonObject, String pageNo) {
        Gson gson = new Gson();
        NewPaymentPaySumListBean newPaymentPaySumListBean = gson.fromJson(jsonObject.toString(), NewPaymentPaySumListBean.class);

        if (pageNo.equals("1")) {
            //集成清空处理
            paymentPaySumList.clear();
            if (commonAdapter != null) {
                mListView.setAdapter(commonAdapter);
            }
        }
        List<NewPaymentPaySumListBean.DataBean> dataBeenList = newPaymentPaySumListBean.getData();

        if (dataBeenList != null && dataBeenList.size() > 0) {
            paymentPaySumList.addAll(dataBeenList);
        }
        next = String.valueOf(newPaymentPaySumListBean.getNext());
        tvRecordCount.setText(newPaymentPaySumListBean.getSize() + "");
        if (commonAdapter == null) {
        commonAdapter = new CommonAdapter<NewPaymentPaySumListBean.DataBean>(NewAdvanceInCashA.this, paymentPaySumList, R.layout.item_advanceincash_count) {
            @Override
            public void convert(ViewHolder holder, NewPaymentPaySumListBean.DataBean dataBean) {
                holder.setText(R.id.tv_date, dataBean.getTime())
                        .setText(R.id.tv_paymentMoney, dataBean.getPaymentAmountString() + "元")
                        .setText(R.id.tv_realPaymentMoney, dataBean.getActualPayAmountSumString() + "元")
                        .setText(R.id.tv_canAdvanceMoney, dataBean.getActualReceiveAmountSumString() + "元")
                        .setText(R.id.tv_zxSettlementMoney, dataBean.getZxzhSurplusAmountString() + "元");
            }
        };
        mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewPaymentPaySumListBean.DataBean dataBean = paymentPaySumList.get(position - 1);
                ObjectSaveUtil.saveObject(NewAdvanceInCashA.this, "paymentPayTemSumList", dataBean);
                intent = new Intent(NewAdvanceInCashA.this, NewAdvanceInCashDetailsA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

            }
        });
    }


    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        if ("0".equals(type)) {
            mPullToRefreshListView.onRefreshComplete();
        }else if ("1".equals(type)){
            mPullToRefreshListView2.onRefreshComplete();
        }
    }

}

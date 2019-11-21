package com.huoniao.oc.financial;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
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
import com.huoniao.oc.bean.PaymentPayTemBean;
import com.huoniao.oc.bean.TrainPaymentBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.treeIdList2;


public class StationPaymentA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;
    @InjectView(R.id.ll_ownership_institution)
    LinearLayout llOwnershipInstitution;
    @InjectView(R.id.et_station_account_number_str)
    EditText etStationAccountNumberStr;
    @InjectView(R.id.et_paymentNumber)
    EditText etPaymentNumber;
    @InjectView(R.id.tv_query)
    TextView tvQuery;
    @InjectView(R.id.dataNum)
    TextView dataNum;
    @InjectView(R.id.mPullToRefreshListView)
    PullToRefreshListView mPullToRefreshListView;
    @InjectView(R.id.activity_station_payment)
    LinearLayout activityStationPayment;
    @InjectView(R.id.tv_start_date)
    TextView tvStartDate;
    @InjectView(R.id.ll_start_date)
    LinearLayout llStartDate;
    @InjectView(R.id.tv_end_date)
    TextView tvEndDate;
    @InjectView(R.id.ll_end_date)
    LinearLayout llEndDate;



    private MapData mapData;
    private MyPopWindow myPopWindow;
    private ListView lv_audit_status; //归属机构弹出框里面的listview
    private float xs;//计算popwindow的位置
    private float ys;//计算popwindow的位置
    private String linOfficeId = "";  //临时记录 选中归属机构id  只有查询后生效
    private PaymentPayTemBean.DataBean dataBean;
    private String paymentNumber = "";
    private String stationAccountNumberStr = "";
    private List<TrainPaymentBean.DataBean> trainPaymentList = new ArrayList<>();
    private CommonAdapter<TrainPaymentBean.DataBean> commonAdapter;
    private ListView mListView;
    private String next = "";    //返回来的页数
    private Intent intent;
    private String paymentInfoTag;
    private String startDate = "";
    private String endDate = "";
    private MyDatePickerDialog myDatePickerDialog;   //时间控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_payment);
        ButterKnife.inject(this);
        initWidget();
        initData();


    }

    private void initWidget() {
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullToRefreshListView.getRefreshableView();
        if (myDatePickerDialog == null) {
            myDatePickerDialog = new MyDatePickerDialog();
        }
        initPullRefreshLinstener();
    }

    private void initData() {
        intent = getIntent();
        paymentInfoTag = intent.getStringExtra("paymentInfoTag");
        if ("homePage".equals(paymentInfoTag)){
            startDate = DateUtils.getTime();
            endDate = DateUtils.getTime();
            tvStartDate.setText(startDate);
            tvEndDate.setText(endDate);
        }else {

            dataBean = (PaymentPayTemBean.DataBean) ObjectSaveUtil.readObject(StationPaymentA.this, "paymentPayTemList");
            if (dataBean != null) {
                paymentNumber = dataBean.getTradeNumber();
                etPaymentNumber.setText(paymentNumber);
            }
        }

        getTrainPaymentList("1");
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
                    ToastUtils.showToast(StationPaymentA.this, "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                } else {
                    getTrainPaymentList(next);
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.ll_ownership_institution, R.id.tv_query, R.id.ll_start_date, R.id.ll_end_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_ownership_institution:
                if (RepeatClickUtils.repeatClick()) {
                    showOwnershipPop();
                }
                break;
            case R.id.tv_query:
                if (RepeatClickUtils.repeatClick()) {
                    getTrainPaymentList("1");
                }
                break;
            case R.id.ll_start_date:
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        startDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(StationPaymentA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvStartDate.setText(date);
                        getTrainPaymentList("1");

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
                                Toast.makeText(StationPaymentA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvEndDate.setText(date);
                        getTrainPaymentList("1");
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
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
        mapData = new MapData(StationPaymentA.this, cpd, false) {
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
                        llOwnershipInstitution.getLocationOnScreen(arr);
                        xs = arr[0] + llOwnershipInstitution.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + llOwnershipInstitution.getHeight();

                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node oneOfficeId) {

                                linOfficeId = String.valueOf(oneOfficeId.getAllTreeNode().id);  //临时的 汇缴记录页面
                                tvOwnershipInstitution.setText(oneOfficeId.getAllTreeNode().name);

//                                queryPaymenList();//请求汇缴记录


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
                }.popupWindowBuilder(StationPaymentA.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                myPopWindow.showAsDropDown(llOwnershipInstitution);

            }
        };
    }

    /**
     * 请求车站汇缴列表
     */
    public void getTrainPaymentList(String pageNo) {
        stationAccountNumberStr = etStationAccountNumberStr.getText().toString().trim();
        paymentNumber = etPaymentNumber.getText().toString().trim();
        String url = Define.URL + "fb/getTrainPaymentList";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("officeId", linOfficeId);
            jsonObject.put("query", stationAccountNumberStr);
            jsonObject.put("tradeNumber", paymentNumber);
            jsonObject.put("pageNo", pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "getTrainPaymentList", pageNo, true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getTrainPaymentList":
                Log.d("getTrainPaymentList", json.toString());
                setAdapter(json, pageNumber);
                break;
        }
    }

    private void setAdapter(JSONObject jsonObject, String pageNo) {
        Gson gson = new Gson();
        TrainPaymentBean trainPaymentBean = gson.fromJson(jsonObject.toString(), TrainPaymentBean.class);

        if (pageNo.equals("1")) {
            //集成清空处理
            trainPaymentList.clear();
            if (commonAdapter != null) {
                mListView.setAdapter(commonAdapter);
            }
        }
        List<TrainPaymentBean.DataBean> dataBeenList = trainPaymentBean.getData();

        if (dataBeenList != null && dataBeenList.size() > 0) {
            trainPaymentList.addAll(dataBeenList);
        }
        next = String.valueOf(trainPaymentBean.getNext());
        dataNum.setText(trainPaymentBean.getSize() + "");
        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<TrainPaymentBean.DataBean>(StationPaymentA.this, trainPaymentList, R.layout.item_advanceincash_stationpayment) {
                @Override
                public void convert(ViewHolder holder, TrainPaymentBean.DataBean dataBean) {
                    holder.setText(R.id.tv_stationName, dataBean.getStationName())
                            .setText(R.id.tv_totalFareAmount, dataBean.getShouldAmountString())
                            .setText(R.id.tv_actualPayMoney, dataBean.getPayAmountString())
                            .setText(R.id.tv_paymentState, dataBean.getPayStateName())
                            .setText(R.id.tv_importDate, dataBean.getCreateDateString());

                }
            };
            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TrainPaymentBean.DataBean dataBean = trainPaymentList.get(position - 1);
                ObjectSaveUtil.saveObject(StationPaymentA.this, "trainPaymentList", dataBean);
                intent = new Intent(StationPaymentA.this, StationPaymentDetailsA.class);
                startActivityForResult(intent, 60);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

            }
        });
    }

    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        mPullToRefreshListView.onRefreshComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 60:
             getTrainPaymentList("1");
                break;
        }
    }
}

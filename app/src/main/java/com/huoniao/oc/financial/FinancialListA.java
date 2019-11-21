package com.huoniao.oc.financial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.bean.FilterDataBean;
import com.huoniao.oc.bean.FinancialBean;
import com.huoniao.oc.common.Filter;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SpinerPopWindow;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/8/21.
 */

public class FinancialListA extends BaseActivity implements MySpinerAdapter.IOnItemSelectListener {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_applyType)
    TextView tvApplyType;
    //    @InjectView(R.id.tv_auditStatus)
//    TextView tvAuditStatus;
    @InjectView(R.id.mListView)
    PullToRefreshListView mPullRefreshListView;
    @InjectView(R.id.tv_selectState)
    TextView tvSelectState;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.layout_startDataChoice)
    RelativeLayout layoutStartDataChoice;
    @InjectView(R.id.tv_startDate)
    TextView tvStartDate;
    @InjectView(R.id.tv_endDate)
    TextView tvEndDate;
    @InjectView(R.id.layout_endDataChoice)
    RelativeLayout layoutEndDataChoice;
    @InjectView(R.id.tv_finRecordNum)
    TextView tvFinRecordNum;
    @InjectView(R.id.tv_totalAmountNum)
    TextView tvTotalAmountNum;
    @InjectView(R.id.layout_finRecord)
    LinearLayout layoutFinRecord;
    @InjectView(R.id.layout_totalAmount)
    LinearLayout layoutTotalAmount;
    @InjectView(R.id.view)
    View view;   // 作为popwindow的参考
    @InjectView(R.id.ll_applyType)
    LinearLayout llApplyType;
    @InjectView(R.id.et_loginName)
    EditText etLoginName;
    @InjectView(R.id.tv_select)
    TextView tvSelect;
    @InjectView(R.id.ll_overallSituation)
    LinearLayout llOverallSituation;
    @InjectView(R.id.tv_updata)
    TextView tvUpdata;

    private ListView mListView;
    private boolean refreshClose = true;//标记是否还有数据可加载
    private boolean datePickerDialogFlag = true;
    private String applyType = "";//申请类型
    private String applyState = "";//申请状态
    private String loginName = "";
    private List<FinancialBean> mDatas = new ArrayList<>();
    private CommonAdapter<FinancialBean> adapter;
    private CommonAdapter<FinancialBean> stateAdapter;
    private SpinerPopWindow mSpinerPopWindow;
    private String CHOICE_TAG;//
    private List<String> applyTypeList = new ArrayList<String>();
    private List<String> applyStateList = new ArrayList<String>();
    private Intent intent;
    private String zXswitch;
    private TextView tv_waitAudit, tv_auditPass, tv_auditNoPass, tv_waitPayment, tv_zzSuccess, tv_zzFail, tv_waitConfirm, tv_inPayment, tv_refusePayment, tv_rePayment, tv_downPayment;
    private Button bt_complete;
    //记录每个审核状态是否被选中的状态
    private boolean stateTag = true;

    private GridView gridView;
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private MyDatePickerDialog myDatePickerDialog2;  // 时间控件2
    private String startDate = "";
    private String endDate = "";
    private String count;
    private String amount;
    private String waitHandleType = "";//待办类型
    private String mainIntoFinTag = "";//首页待办进入标识
    private String finTextCickTag = "";//首页待办进入区分今日还是本月标识

    private List<FinancialBean> typeList = new ArrayList<FinancialBean>();
    private ListView lv_applyType;  //弹出框财务申请类型列表
    private MyPopWindow myPopWindow;
    private Filter filter;
    private float xs;
    private float ys;

    private VolleyNetCommon volleyNetCommon;
    private static final int TYPEORSTATE_LIST = 1;
    private static final int TYPE_LIST = 2;
    private final int BACK_REFRESH_FINOVERVIEW = 20;//返回主界面是刷新首页财务总览数据标识
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TYPEORSTATE_LIST:
                    @SuppressWarnings("unchecked")
                    List<FilterDataBean.DataBean> myList = (List<FilterDataBean.DataBean>) msg.obj;
                    for (FilterDataBean.DataBean bean : myList
                            ) {
                        if (applyState.contains(bean.getValue())) {
                            if (applyState.length() > 0) {
                                String[] split = applyState.split(",");
                                for (int i = 0; i < split.length; i++) {
                                    if (split[i].equals(bean.getValue())) {
                                        bean.setFlag(true);
                                    }
                                }
                            }

                        } else {
                            bean.setFlag(false);
                        }
                    }
                    tvSelectState.setVisibility(View.GONE);
                    filter = new Filter(myList);
                    filter.filterState(FinancialListA.this, view);
                    filter.setFilterLinstener(new Filter.FilterLinstener() {
                        @Override
                        public void result(String s) {
//                            Toast.makeText(FinancialListA.this, s, Toast.LENGTH_SHORT).show();
                            tvSelectState.setVisibility(View.VISIBLE);
                            applyState = s;
                            try {
                                if (mDatas != null) {
                                    mDatas.clear();
                                }
                                mainIntoFinTag = "";
                                getFinanciaList(applyType, applyState, 1);
                                refreshClose = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void cancle() {
                            tvSelectState.setVisibility(View.VISIBLE);
                        }
                    });
                    /*if (applyTypeList != null){
                        applyTypeList.clear();
                    }
                    if (applyStateList != null){
                        applyStateList.clear();
                    }

                    for (FinancialBean financialBean : myList) {
                        if ("1".equals(CHOICE_TAG)){
                            applyTypeList.add(financialBean.getLabe());

                        }
//                        else if ("2".equals(CHOICE_TAG)){
//                            applyStateList.add(financialBean.getLabe());
//                        }


                    }
                    if ("1".equals(CHOICE_TAG)){
                        mSpinerPopWindow = new SpinerPopWindow(FinancialListA.this);
                        mSpinerPopWindow.refreshData(applyTypeList, 0);
                        mSpinerPopWindow.setItemListener(FinancialListA.this);
                        showSpinWindow(tvApplyType);
                    }else if ("2".equals(CHOICE_TAG)){
                       selectStatePop(myList);
                    }*/

                    break;
                case TYPE_LIST:
                    typeList.clear();
                    FinancialBean financialBean = new FinancialBean();
                    financialBean.setLabe("全部");
                    financialBean.setValue("");
                    typeList.add(financialBean);
                    List<FinancialBean> list = (List<FinancialBean>) msg.obj;
                    typeList.addAll(list);
                    showApplyType();
                    /*if (applyTypeList != null) {
                        applyTypeList.clear();
                    }
                    for (FinancialBean financialBean : typeList) {
                        if ("1".equals(CHOICE_TAG)) {
                            applyTypeList.add(financialBean.getLabe());
                        }
                    }
                    if ("1".equals(CHOICE_TAG)) {
                        mSpinerPopWindow = new SpinerPopWindow(FinancialListA.this);
                        mSpinerPopWindow.refreshData(applyTypeList, 0);
                        mSpinerPopWindow.setItemListener(FinancialListA.this);
                        showSpinWindow(tvApplyType);
                    }*/

                    break;


            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_financiallist);
        ButterKnife.inject(this);
//        llOverallSituation.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                return false;
//            }
//        });

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullRefreshListView.getRefreshableView();
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        DateUtils dateUtils = new DateUtils();
        startDate = dateUtils.getFirstdayofThisMonth();//获取当月第一天日期
        endDate = dateUtils.getTime();  //获取今天日期
        tvStartDate.setText(startDate);
        tvEndDate.setText(endDate);
        intent = getIntent();
        waitHandleType = intent.getStringExtra("waitHandleType");
        applyType = intent.getStringExtra("type");
        if (Define.finApplyType1.equals(waitHandleType) || Define.finApplyType1.equals(applyType)) {
            tvApplyType.setText("汇缴付款");
            applyType = Define.finApplyType1;
        } else if (Define.finApplyType2.equals(waitHandleType) || Define.finApplyType2.equals(applyType)) {
            tvApplyType.setText("提现");
            applyType = Define.finApplyType2;
        } else if (Define.finApplyType3.equals(waitHandleType) || Define.finApplyType3.equals(applyType)) {
            tvApplyType.setText("代存");
            applyType = Define.finApplyType3;
        } else if (Define.finApplyType4.equals(waitHandleType) || Define.finApplyType4.equals(applyType)) {
            tvApplyType.setText("代付");
            applyType = Define.finApplyType4;
        } else if (Define.finApplyType5.equals(waitHandleType) || Define.finApplyType5.equals(applyType)) {
            tvApplyType.setText("代扣");
            applyType = Define.finApplyType5;
        }
        mainIntoFinTag = intent.getStringExtra("mainIntoFinTag");
        finTextCickTag = intent.getStringExtra("finTextCickTag");
        if ("mainIntoFinTag".equals(mainIntoFinTag) && "toDay".equals(finTextCickTag)) {
            startDate = dateUtils.getTime();  //获取今天日期
            tvStartDate.setText(startDate);
        }
        try {
            getFinanciaList(applyType, applyState, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        applyTypeList.add("申请类型");
//        applyTypeList.add("汇缴扣款");
//        applyTypeList.add("提现");
//        applyTypeList.add("代存");
//        applyTypeList.add("代付");
//        applyTypeList.add("代扣");

       /* applyStateList.add("审核状态");
        applyStateList.add("待审核");
        applyStateList.add("审核通过");
        applyStateList.add("审核不通过");
        applyStateList.add("待付款");
        applyStateList.add("转账成功");
        applyStateList.add("转账失败");
        applyStateList.add("待确认");
        applyStateList.add("付款中");
        applyStateList.add("拒绝付款");
        applyStateList.add("失败重新付款");
        applyStateList.add("线下付款");*/
    }

    @OnClick({R.id.iv_back, R.id.tv_applyType, R.id.tv_select, R.id.tv_selectState, R.id.layout_startDataChoice, R.id.layout_endDataChoice, R.id.tv_updata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(BACK_REFRESH_FINOVERVIEW);
                finish();
                break;
            case R.id.tv_applyType:
//                mSpinerPopWindow = new SpinerPopWindow(this);
                CHOICE_TAG = "1";
//                mSpinerPopWindow.refreshData(applyTypeList, 0);
//                mSpinerPopWindow.setItemListener(this);
//                showSpinWindow(tvApplyType);
                try {
                    getApplyType("acct_apply_type");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
//            case R.id.tv_auditStatus:
//                mSpinerPopWindow = new SpinerPopWindow(this);
//                CHOICE_TAG = "2";
//                mSpinerPopWindow.refreshData(applyStateList, 0);
//                mSpinerPopWindow.setItemListener(this);
//                showSpinWindow(tvAuditStatus);
//                break;
            case R.id.tv_select:
                try {
                    if (mDatas != null) {
                        mDatas.clear();
                    }
                    mainIntoFinTag = "";
                    getFinanciaList(applyType, applyState, 1);

                    refreshClose = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_selectState:
                CHOICE_TAG = "2";
                try {
                    getApplyTypeOrState("acct_apply_state");
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                selectStatePop();
                break;

            case R.id.layout_startDataChoice:
                if (myDatePickerDialog == null) {
                    myDatePickerDialog = new MyDatePickerDialog();
                }
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {

                        tvStartDate.setText(date);
                        startDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(FinancialListA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });

                break;

            case R.id.layout_endDataChoice:
                if (myDatePickerDialog2 == null) {
                    myDatePickerDialog2 = new MyDatePickerDialog();
                }
                myDatePickerDialog2.datePickerDialog(this);
                myDatePickerDialog2.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {

                        tvEndDate.setText(date);
                        endDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(FinancialListA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }


                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });

                break;
            case R.id.tv_updata:
                if (mDatas != null) {
                    mDatas.clear();
                }
                mainIntoFinTag = "";
                requestUpdateFinanceStatus();
                refreshClose = true;
                break;
        }
    }

    /**
     * 展示申请类型下拉框
     */
    private void showApplyType() {
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                lv_applyType = (ListView) view.findViewById(R.id.lv_audit_status);
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //重新测量
//                int w =  lv_applyType.getMeasuredWidth();
//                cow = Math.abs(w - xOffset);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                llApplyType.getLocationOnScreen(arr);
                view.measure(0, 0);
                xs = arr[0] + llApplyType.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + llApplyType.getHeight();

                CommonAdapter<FinancialBean> commonAdapter = new CommonAdapter<FinancialBean>(FinancialListA.this, typeList, R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, FinancialBean financialBean) {
                        holder.setText(R.id.tv_text, financialBean.getLabe());

                    }
                };

                lv_applyType.setAdapter(commonAdapter);
                lv_applyType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String applyTypeStr = typeList.get(i).getLabe(); //获取点击的文字
                        tvApplyType.setText(applyTypeStr);
                        //获取点击的類型對應的代號
                        applyType = typeList.get(i).getValue();

                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(this).create();
        myPopWindow.keyCodeDismiss(true); //返回键关闭
        myPopWindow.showAtLocation(llApplyType, Gravity.NO_GRAVITY, (int) xs, (int) ys);
    }

   /* private void showSpinWindow(View v) {
        Log.e("", "showSpinWindow");
        mSpinerPopWindow.setWidth(v.getWidth());
        mSpinerPopWindow.showAsDropDown(v);
    }

    // 选择申请类型
    private void setApplyType(int pos) {
        if (pos >= 0 && pos <= applyTypeList.size()) {
            String value = applyTypeList.get(pos);

            tvApplyType.setText(value);

            applyType = tvApplyType.getText().toString();

            if ("汇缴扣款".equals(applyType)) {
                applyType = Define.finApplyType1;
            } else if ("提现".equals(applyType)) {
                applyType = Define.finApplyType2;
            } else if ("代存".equals(applyType)) {
                applyType = Define.finApplyType3;
            } else if ("代付".equals(applyType)) {
                applyType = Define.finApplyType4;
            } else if ("代扣".equals(applyType)) {
                applyType = Define.finApplyType5;
            } else if ("申请类型".equals(applyType)) {
                applyType = "";
            }
        }
    }*/

    // 选择申请状态
   /* private void setApplyState(int pos) {
        if (pos >= 0 && pos <= applyStateList.size()) {
            String value = applyStateList.get(pos);

            tvAuditStatus.setText(value);

            applyState = tvAuditStatus.getText().toString();

            if ("待审核".equals(applyState)) {
                applyState = Define.finApplyState1;
            } else if ("审核通过".equals(applyState)) {
                applyState = Define.finApplyState2;
            } else if ("审核不通过".equals(applyState)) {
                applyState = Define.finApplyState3;
            } else if ("待付款".equals(applyState)) {
                applyState = Define.finApplyState4;
            } else if ("转账成功".equals(applyState)) {
                applyState = Define.finApplyState5;
            } else if ("转账失败".equals(applyState)) {
                applyState = Define.finApplyState6;
            } else if ("待确认".equals(applyState)) {
                applyState = Define.finApplyState7;
            } else if ("付款中".equals(applyState)) {
                applyState = Define.finApplyState8;
            } else if ("拒绝付款".equals(applyState)) {
                applyState = Define.finApplyState9;
            } else if ("失败重新付款".equals(applyState)) {
                applyState = Define.finApplyState10;
            } else if ("线下付款".equals(applyState)) {
                applyState = Define.finApplyState11;
            } else if ("审核状态".equals(applyState)) {
                applyState = "";
            }
        }
    }*/

    @Override
    public void onItemClick(int pos) {
        if ("1".equals(CHOICE_TAG)) {
//            setApplyType(pos);
        }
        /*else if ("2".equals(CHOICE_TAG)) {
            setApplyState(pos);
        }*/
    }

    /**
     * 上拉加载下一页
     *
     * @param nextPage 下一页
     */
    private void setRefreshPager(final int nextPage) {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshClose) {  //如果有数据 继续刷新
                    try {
//						pageNumber++;
                        getFinanciaList(applyType, applyState, nextPage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    Toast.makeText(FinancialListA.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    ToastUtils.showToast(FinancialListA.this, "没有更多数据了");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    });

                }
            }
        });
    }

    /**
     * 获取财务管理列表
     *
     * @throws Exception
     */
    private void getFinanciaList(String applyType, String applyState, final int pageNo) throws Exception {
        loginName = etLoginName.getText().toString();

        cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("loginName", loginName);
            jsonObject.put("applyType", applyType);
            jsonObject.put("state", applyState);
            if ("mainIntoFinTag".equals(mainIntoFinTag)) {
                jsonObject.put("waitHandleType", waitHandleType);
            }
            jsonObject.put("pageNo", pageNo);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        String url = Define.URL + "acct/financeApplyList";
        final List<FinancialBean> finList = new ArrayList<FinancialBean>();
        SessionJsonObjectRequest userListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            cpd.dismiss();
                            Toast.makeText(FinancialListA.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("financeList", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            int nextPage = response.getInt("next");
                            setRefreshPager(nextPage);
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                int num = response.getInt("size");
                                num = Math.min(num, jsonArray.length());

                                for (int i = 0; i < num; i++) {
                                    FinancialBean financialBean = new FinancialBean();
                                    JSONObject finObj = (JSONObject) jsonArray.get(i);
                                    String id = finObj.optString("id");//财务记录id
                                    String createDate = finObj.optString("createDate");// 时间
                                    String agencyUserOfficeName = finObj.optString("agencyUserOfficeName");// 机构名称
                                    String agencyLoginName = finObj.optString("agencyLoginName");// 代理账户
                                    String officeParentNames = finObj.optString("officeParentNames");// 上级机构
                                    String agencyUserName = finObj.optString("agencyUserName");// 姓名
                                    String applyTypeName = finObj.optString("applyTypeName");// 申请类型名称
                                    String applyType = finObj.optString("applyType");// 申请类型代号
                                    String transferId = finObj.optString("transferId");// 交易单号
                                    String applyFee = finObj.optString("applyFeeString");// 待付金额
                                    String actualFee = finObj.optString("actualFeeString");// 实付金额
                                    String accountName = finObj.optString("accountName");// 账户名称
                                    String openBankName = finObj.optString("openBankName");// 开户行
                                    String cardNo = finObj.optString("cardNo");// 卡号
                                    String applyUserName = finObj.optString("applyUserName");// 申请人
                                    String receiptLoginName = finObj.optString("receiptLoginName");// 收款账号
                                    String receiptName = finObj.optString("receiptName");// 收款人
                                    String stateName = finObj.optString("stateName");// 状态名称
                                    String state = finObj.optString("state");// 状态代号
                                    String remark = finObj.optString("remark");// 备注
                                    String reason = finObj.optString("reason");// 理由
//                                    String ZXswitch = finObj.optString("ZXswitch");// 银企直联功能未开启提示
                                    String cashierUserName = finObj.optString("cashierUserName");// 出纳操作人
                                    String auditUserName = finObj.optString("auditUserName");// 会计操作人

                                    financialBean.setId(id);
                                    financialBean.setCreateDate(createDate);
                                    financialBean.setAgencyUserOfficeName(agencyUserOfficeName);
                                    financialBean.setAgencyLoginName(agencyLoginName);
                                    financialBean.setAgencyUserName(agencyUserName);
                                    financialBean.setApplyTypeName(applyTypeName);
                                    financialBean.setTransferId(transferId);
                                    financialBean.setApplyFee(applyFee);
                                    financialBean.setActualFee(actualFee);
                                    financialBean.setApplyType(applyType);
                                    financialBean.setAccountName(accountName);
                                    financialBean.setOpenBankName(openBankName);
                                    financialBean.setCardNo(cardNo);
                                    financialBean.setApplyUserName(applyUserName);
                                    financialBean.setReceiptLoginName(receiptLoginName);
                                    financialBean.setReceiptName(receiptName);
                                    financialBean.setStateName(stateName);
                                    financialBean.setState(state);
                                    financialBean.setRemark(remark);
                                    financialBean.setReason(reason);
                                    financialBean.setCashierUserName(cashierUserName);
                                    financialBean.setAuditUserName(auditUserName);
                                    financialBean.setOfficeParentNames(officeParentNames);
//                                    financialBean.setZXswitch(ZXswitch);

                                    finList.add(financialBean);
                                }

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = (JSONObject) jsonArray.get(i);
                                    // 银企直联功能未开启提示
                                    zXswitch = object.optString("ZXswitch");
                                    count = object.optString("count");//财务记录
                                    amount = object.optString("amount");//总金额
                                }

                                if (count != null && !count.isEmpty()) {
                                    tvFinRecordNum.setText(count);
                                } else {
                                    tvFinRecordNum.setText("");
                                }

                                if (amount != null && !amount.isEmpty()) {
                                    tvTotalAmountNum.setText(amount);
                                } else {
                                    tvTotalAmountNum.setText("");
                                }


                                Log.d("debug", "财务列表：" + finList);

                                if (nextPage == 1) {
                                    mDatas.clear();
                                } else if (nextPage == -1) {
                                    refreshClose = false;
                                }
                                mPullRefreshListView.onRefreshComplete();
                                mDatas.addAll(finList);

                                if (adapter == null) {
                                    adapter = new CommonAdapter<FinancialBean>(FinancialListA.this, mDatas, R.layout.admin_financialmanage_item) {
                                        @Override
                                        public void convert(ViewHolder holder, FinancialBean financialBean) {
                                            holder.setText(R.id.tv_date, financialBean.getCreateDate())
                                                    .setText(R.id.tv_operationType, financialBean.getApplyTypeName())
                                                    .setText(R.id.tv_name, financialBean.getAgencyUserName())
                                                    .setText(R.id.tv_money, financialBean.getApplyFee())
                                                    .setText(R.id.tv_auditStatus, financialBean.getStateName())
                                                    .setText(R.id.tv_agentAccount, financialBean.getAgencyLoginName())
                                                    .setText(R.id.tradeRemark, financialBean.getRemark());

                                        }
                                    };

                                    mListView.setAdapter(adapter);
                                }
                                adapter.refreshData(mDatas);

                                cpd.dismiss();
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        FinancialBean financialBean = mDatas.get(position - 1);
                                        intent = new Intent(FinancialListA.this, FinancialListDetailsA.class);
                                        intent.putExtra("id", financialBean.getId());
                                        intent.putExtra("createDate", financialBean.getCreateDate());
                                        intent.putExtra("agencyUserOfficeName", financialBean.getAgencyUserOfficeName());
                                        intent.putExtra("agencyLoginName", financialBean.getAgencyLoginName());
                                        intent.putExtra("agencyUserName", financialBean.getAgencyUserName());
                                        intent.putExtra("applyTypeName", financialBean.getApplyTypeName());
                                        intent.putExtra("transferId", financialBean.getTransferId());
                                        intent.putExtra("applyFee", financialBean.getApplyFee());
                                        intent.putExtra("actualFee", financialBean.getActualFee());
                                        intent.putExtra("applyType", financialBean.getApplyType());
                                        intent.putExtra("accountName", financialBean.getAccountName());
                                        intent.putExtra("openBankName", financialBean.getOpenBankName());
                                        intent.putExtra("cardNo", financialBean.getCardNo());
                                        intent.putExtra("applyUserName", financialBean.getApplyUserName());
                                        intent.putExtra("receiptLoginName", financialBean.getReceiptLoginName());
                                        intent.putExtra("receiptName", financialBean.getReceiptName());
                                        intent.putExtra("stateName", financialBean.getStateName());
                                        intent.putExtra("state", financialBean.getState());
                                        intent.putExtra("remark", financialBean.getRemark());
                                        intent.putExtra("reason", financialBean.getReason());
                                        intent.putExtra("cashierUserName", financialBean.getCashierUserName());
                                        intent.putExtra("auditUserName", financialBean.getAuditUserName());
                                        intent.putExtra("ZXswitch", zXswitch);
                                        intent.putExtra("officeParentName", financialBean.getOfficeParentNames());
//
                                        startActivityForResult(intent, 10);
//                                        startActivity(intent);
                                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                                    }
                                });

                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(FinancialListA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(FinancialListA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                finish();
                            } else {
                                cpd.dismiss();
                                Toast.makeText(FinancialListA.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FinancialListA.this, R.string.netError, Toast.LENGTH_SHORT).show();
//						Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

            }
        });
        // 解决重复请求后台的问题
        userListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        userListRequest.setTag("financialList");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(userListRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 10:
                try {
                    if (mDatas != null) {
                        mDatas.clear();
                    }
                    getFinanciaList(applyType, applyState, 1);
                    refreshClose = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 获取审核状态
     *
     * @param dictType
     * @throws Exception
     */
    protected void getApplyTypeOrState(String dictType) throws Exception {
        cpd.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dictType", dictType);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (volleyNetCommon == null) {
            volleyNetCommon = new VolleyNetCommon();
        }
        JsonObjectRequest abb = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "sys/getDictAry", jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(FinancialListA.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Log.e("abb", json.toString());
                if (json == null) {
                    cpd.dismiss();
                    Toast.makeText(FinancialListA.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {

                    Gson gson = new Gson();
                    FilterDataBean filterDataBean = gson.fromJson(json.toString(), FilterDataBean.class);
                    List<FilterDataBean.DataBean> data = filterDataBean.getData();


                    Message msg = new Message();
                    msg.what = TYPEORSTATE_LIST;
                    msg.obj = data;
                    mHandler.sendMessage(msg);


                } catch (Exception e) {
                    e.printStackTrace();
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
        }, "applyTypeOrState", true);
        volleyNetCommon.addQueue(abb);


    }

    /**
     * 获取财务申请类型
     *
     * @param dictType
     * @throws Exception
     */
    protected void getApplyType(String dictType) throws Exception {
        cpd.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dictType", dictType);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (volleyNetCommon == null) {
            volleyNetCommon = new VolleyNetCommon();
        }
        JsonObjectRequest abb = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "sys/getDictAry", jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(FinancialListA.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Log.e("abb", json.toString());
                if (json == null) {
                    cpd.dismiss();
                    Toast.makeText(FinancialListA.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONArray jsonArray = json.getJSONArray("data");
                    final List<FinancialBean> list = new ArrayList<FinancialBean>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        FinancialBean financialBean = new FinancialBean();
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        String label = object.optString("label");// 标签名
                        int sort = object.optInt("sort");// 标签名对应序号
                        String value = object.optString("value");// 标签名对应代号

                        financialBean.setLabe(label);
                        financialBean.setValue(value);
                        financialBean.setSort(sort);
                        list.add(financialBean);
                    }
                   /* Gson gson = new Gson();
                    FinancialBean financialBean = gson.fromJson(json.toString(), FinancialBean.class);
                    List<FinancialBean> list = financialBean.getData();*/

                    Collections.sort(list, new TypeOrStateSort());


                    Message msg = new Message();
                    msg.what = TYPE_LIST;
                    msg.obj = list;
                    mHandler.sendMessage(msg);


                } catch (Exception e) {
                    e.printStackTrace();
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
        }, "applyType", true);
        volleyNetCommon.addQueue(abb);


    }


    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("financialList");
        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll("applyTypeOrState");
            volleyNetCommon.getRequestQueue().cancelAll("applyType");
        }
    }

    /**
     * 对从服务器获取的申请类型和状态进行排序
     */
    private class TypeOrStateSort implements Comparator<FinancialBean> {

        @Override
        public int compare(FinancialBean lhs, FinancialBean rhs) {
            return lhs.getSort() - rhs.getSort();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (filter != null && filter.myPopWindow != null) {
                if (filter.myPopWindow.isShow()) {
                    //如果popwindow是显示状态就 不要做任何处理
                    return true;
                }
            }
            setResult(BACK_REFRESH_FINOVERVIEW);
            finish();

        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 请求更新财务状态
     */
    private void requestUpdateFinanceStatus() {
        String url = Define.URL + "acct/updateFinanceStatus";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "updateFinanceStatus", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "updateFinanceStatus":
                JSONObject jsonObject = null;
                try {
                    jsonObject = json.getJSONObject("data");
                    String result = jsonObject.getString("result");
                    if ("success".equals(result)){
                        getFinanciaList(applyType, applyState, 1);
                    }else {
                        String msg = jsonObject.optString("msg");
                        ToastUtils.showLongToast(FinancialListA.this, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}


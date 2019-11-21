package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.huoniao.oc.bean.FinancialBean;
import com.huoniao.oc.bean.PayWarningBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RepeatClickUtils;
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

import static com.huoniao.oc.MyApplication.treeIdList2;

/**
 * Created by Administrator on 2017/11/10.
 */

public class WarningInfoListA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;
    @InjectView(R.id.ll_ownership_institution)
    LinearLayout llOwnershipInstitution;
    @InjectView(R.id.tv_processingState)
    TextView tvProcessingState;
    @InjectView(R.id.ll_processingState)
    LinearLayout llProcessingState;
    @InjectView(R.id.et_windowNumber)
    EditText etWindowNumber;
    @InjectView(R.id.tv_startDate)
    TextView tvStartDate;
    @InjectView(R.id.layout_startDataChoice)
    RelativeLayout layoutStartDataChoice;
    @InjectView(R.id.tv_endDate)
    TextView tvEndDate;
    @InjectView(R.id.layout_endDataChoice)
    RelativeLayout layoutEndDataChoice;
    @InjectView(R.id.tv_select)
    TextView tvSelect;
    @InjectView(R.id.mPullToRefreshListView)
    PullToRefreshListView mPullToRefreshListView;


    private MyPopWindow myPopWindow;
    private ListView lv_popWindow;  //弹出框list
    private float xs;
    private float ys;
    private MapData mapData;
    private String officeIdStr = "";  //选中归属机构id
    private List<FinancialBean> stateList = new ArrayList<FinancialBean>();
    private String processingState;
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private MyDatePickerDialog myDatePickerDialog2;  // 时间控件2
    private String startDate = "";
    private String endDate = "";
    private ListView mListView;
    private CommonAdapter<PayWarningBean.DataBean> adapter;
    private List<PayWarningBean.DataBean> data = new ArrayList<>();
    private Intent intent;
    private String windowNumber;
    private final int WAITDO_WARNING = 30;//返回主界面是刷新首页汇缴预警数据标识
    private boolean refreshClose = true;//标记是否还有数据可加载
    private String nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_paymentwarning);
        ButterKnife.inject(this);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullToRefreshListView.getRefreshableView();
        initData();
        setRefreshPager();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        processingState = intent.getStringExtra("processingState");
        if (processingState != null) {
            if (Define.WARN_NORMOL.equals(processingState)) {
                tvProcessingState.setText("正常");
            } else if (Define.WARN_EXCIPTION.equals(processingState)) {
                tvProcessingState.setText("异常");
            } else {
                tvProcessingState.setText("待处理");
            }

        }
        windowNumber = intent.getStringExtra("windowNumber");
        if (windowNumber != null) {
            etWindowNumber.setText(windowNumber);
        }
        DateUtils dateUtils = new DateUtils();
        startDate = dateUtils.getFirstdayofThisMonth();//获取当月第一天日期
        endDate = dateUtils.getTime();  //获取今天日期
        tvStartDate.setText(startDate);
        tvEndDate.setText(endDate);
        requestPaymentWarnList("1");
    }

    @OnClick({R.id.iv_back, R.id.tv_ownership_institution, R.id.tv_processingState, R.id.layout_startDataChoice, R.id.layout_endDataChoice, R.id.tv_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(WAITDO_WARNING);
                finish();
                break;
            case R.id.tv_ownership_institution:
                if(RepeatClickUtils.repeatClick()) {
                    showOwnershipPop();
                }
                break;
            case R.id.tv_processingState:
                try {
                    getProcessingState("fb_paymentWarn_state");
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                showProcessingState();
                break;
            case R.id.layout_startDataChoice:
                if (myDatePickerDialog == null) {
                    myDatePickerDialog = new MyDatePickerDialog();
                }
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        String sDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(date));
                        tvStartDate.setText(sDate);
                        startDate = sDate;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(WarningInfoListA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
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
                        String eDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(date));
                        tvEndDate.setText(eDate);
                        endDate = eDate;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(WarningInfoListA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }


                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });


                break;
            case R.id.tv_select:
                try {

                    data.clear();

                    requestPaymentWarnList("1");

//                    refreshClose = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 展示处理状态下拉框
     */
    private void showProcessingState() {
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                lv_popWindow = (ListView) view.findViewById(R.id.lv_audit_status);
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //重新测量
//                int w =  lv_applyType.getMeasuredWidth();
//                cow = Math.abs(w - xOffset);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                llProcessingState.getLocationOnScreen(arr);
                view.measure(0, 0);
                xs = arr[0] + llProcessingState.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + llProcessingState.getHeight();

                CommonAdapter<FinancialBean> commonAdapter = new CommonAdapter<FinancialBean>(WarningInfoListA.this, stateList, R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, FinancialBean financialBean) {
                        holder.setText(R.id.tv_text, financialBean.getLabe());

                    }
                };

                lv_popWindow.setAdapter(commonAdapter);
                lv_popWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String applyTypeStr = stateList.get(i).getLabe(); //获取点击的文字
                        tvProcessingState.setText(applyTypeStr);
                        //获取点击的類型對應的代號
                        processingState = stateList.get(i).getValue();

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
        myPopWindow.showAtLocation(llProcessingState, Gravity.NO_GRAVITY, (int) xs, (int) ys);
    }


    /**
     * 弹出归属机构
     */
    private void showOwnershipPop() {
        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局!
//得到状态栏高度
//返回键关闭
        mapData = new MapData(WarningInfoListA.this, cpd, true) {
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }

                myPopWindow = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        lv_popWindow = (ListView) view.findViewById(R.id.lv_audit_status);
                        mapData.setTrainOwnershipData(lv_popWindow);
                        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                        int[] arr = new int[2];
                        llOwnershipInstitution.getLocationOnScreen(arr);
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                        xs = arr[0] + llOwnershipInstitution.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + llOwnershipInstitution.getHeight();
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node officeId) {
                                officeIdStr = String.valueOf(officeId.getAllTreeNode().id);
                                tvOwnershipInstitution.setText(officeId.getAllTreeNode().name);
                                // if(officeId.getAllTreeNode().type.equals("2")) {
                                myPopWindow.dissmiss();
                                //}
                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop2;
                    }
                }.popupWindowBuilder(WarningInfoListA.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
               // myPopWindow.showAtLocation(llOwnershipInstitution, Gravity.NO_GRAVITY, (int) xs, (int) ys);
                myPopWindow.showAsDropDown(llOwnershipInstitution);
            }
        };
        //  mapData.setTrainOwnershipData();


    }

    /**
     * 获取处理状态
     *
     * @param dictType
     * @throws Exception
     */
    protected void getProcessingState(String dictType) throws Exception {
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
                Toast.makeText(WarningInfoListA.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Log.e("abb", json.toString());
                if (json == null) {
                    cpd.dismiss();
                    Toast.makeText(WarningInfoListA.this, "服务器异常！", Toast.LENGTH_SHORT).show();
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

                    Collections.sort(list, new TypeOrStateSort());
                    stateList.clear();
                    FinancialBean financialBean = new FinancialBean();
                    financialBean.setLabe("全部");
                    financialBean.setValue("");
                    stateList.add(financialBean);
                    stateList.addAll(list);
                    showProcessingState();
                   /* Message msg = new Message();
                    msg.what = TYPE_LIST;
                    msg.obj = list;
                    mHandler.sendMessage(msg);*/


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            protected void PdDismiss() {
                cpd.dismiss();
            }
        }, "applyType", true);
        volleyNetCommon.addQueue(abb);


    }

    /**
     * 对从服务器获取的处理状态进行升序排序
     */
    private class TypeOrStateSort implements Comparator<FinancialBean> {

        @Override
        public int compare(FinancialBean lhs, FinancialBean rhs) {
            return lhs.getSort() - rhs.getSort();
        }
    }

    /**
     * 请求汇缴预警列表数据
     */
    private void requestPaymentWarnList(String pageNo) {
        String url = Define.URL + "fb/paymentWarnList";
        windowNumber = etWindowNumber.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("winNumber", windowNumber);
            jsonObject.put("officeId", officeIdStr);
            jsonObject.put("state", processingState);
            jsonObject.put("pageNo", pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "paymentWarnList", pageNo, true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "paymentWarnList":
//                String jsonData = json.toString();

                setWarnListAdapter(json, pageNumber);
                closeRefresh();
                break;
        }
    }

    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        closeRefresh();
    }

    /**
     * 设置预警列表数据
     *
     * @param json       json数据
     * @param pageNumber
     *
     */
    private void setWarnListAdapter(JSONObject json, String pageNumber) {
        if ("1".equals(pageNumber)){
            data.clear();
        }
        Gson gson = new Gson();
        PayWarningBean payWarningBean = gson.fromJson(json.toString(), PayWarningBean.class);

        List<PayWarningBean.DataBean> myData = payWarningBean.getData();
        if (myData != null){
            data.addAll(myData);
        }
        try {
//            int next = json.getInt("next");
            nextPage = json.getString("next");


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (adapter == null) {

            adapter = new CommonAdapter<PayWarningBean.DataBean>(WarningInfoListA.this, data, R.layout.item_warninginfo_list) {
                @Override
                public void convert(ViewHolder holder, PayWarningBean.DataBean payWarnData) {
                    holder.setText(R.id.tv_warningDate, payWarnData.getCreateDate())
                            .setText(R.id.tv_windowNumber, payWarnData.getWinNumber())
                            .setText(R.id.tv_paymentAmount, payWarnData.getPaymentAmount() + "元")
                            .setText(R.id.tv_stationName, payWarnData.getStationName())
                            .setText(R.id.tv_averagePrice, payWarnData.getAveragePrice() + "元");

                    TextView tv_proState = holder.getView(R.id.tv_proState);
                    String proState = payWarnData.getState();
                    String proStateName = payWarnData.getStateName();
                    if (Define.WARN_NORMOL.equals(proState)) {
                        tv_proState.setBackgroundResource(R.drawable.textbackgroud12);
                        tv_proState.setText(proStateName);
                        tv_proState.setTextColor(Color.rgb(45, 169, 141));
                    } else if (Define.WARN_EXCIPTION.equals(proState)) {
                        tv_proState.setBackgroundResource(R.drawable.textbackgroud14);
                        tv_proState.setText(proStateName);
                        tv_proState.setTextColor(Color.rgb(250, 47, 43));
                    } else {
                        tv_proState.setBackgroundResource(R.drawable.textbackgroud13);
                        tv_proState.setText(proStateName);
                        tv_proState.setTextColor(Color.rgb(99, 168, 235));
                    }

                }
            };

            mListView.setAdapter(adapter);
        }
        adapter.refreshData(data);

        cpd.dismiss();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PayWarningBean.DataBean dataBean = data.get(position - 1);
                intent = new Intent(WarningInfoListA.this, WarningDetailsA.class);
                intent.putExtra("id", dataBean.getId());
                intent.putExtra("createDateString", dataBean.getCreateDateString());
                intent.putExtra("stationName", dataBean.getStationName());
                intent.putExtra("winNumber", dataBean.getWinNumber());
                intent.putExtra("loginName", dataBean.getLoginName());
                intent.putExtra("paymentAmount", dataBean.getPaymentAmount());
                intent.putExtra("ticketCount", dataBean.getTicketCount());
                intent.putExtra("averagePrice", dataBean.getAveragePrice());
                intent.putExtra("averageAmount", dataBean.getAverageAmount());
                intent.putExtra("conditions", dataBean.getConditions());
                intent.putExtra("handleName", dataBean.getHandleName());
                intent.putExtra("updateDate", dataBean.getUpdateDateString());
                intent.putExtra("stateName", dataBean.getStateName());
                intent.putExtra("instructions", dataBean.getInstructions());
                intent.putExtra("state", dataBean.getState());
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

            }
        });
    }

    /**
     * 上拉加载下一页
     *
     *
     */
    private void setRefreshPager() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("-1".equals(nextPage)) {

                        ToastUtils.showToast(WarningInfoListA.this, "没有更多数据了");
                        closeRefresh();


                } else {//如果有数据 继续刷新

                    requestPaymentWarnList(nextPage);

                }
            }
        });
    }

    private void closeRefresh() {
        ThreadCommonUtils.runonuiThread(new Runnable() {
            @Override
            public void run() {
                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            setResult(WAITDO_WARNING);
            finish();

        }

        return super.onKeyDown(keyCode, event);
    }
}

package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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
import com.huoniao.oc.bean.DictAryBean;
import com.huoniao.oc.bean.NewPayWarningBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.ContainsEmojiEditText;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
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
import static com.huoniao.oc.R.id.tv_query;

public class NewWarningInfoListA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
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
    @InjectView(R.id.tv_type)
    TextView tvType;
    @InjectView(R.id.ll_warningType)
    LinearLayout llWarningType;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;
    @InjectView(R.id.ll_ownership_institution)
    LinearLayout llOwnershipInstitution;
    @InjectView(R.id.tv_state)
    TextView tvState;
    @InjectView(R.id.ll_state)
    LinearLayout llState;
    @InjectView(R.id.tv_handlerState)
    TextView tvHandlerState;
    @InjectView(R.id.ll_handlerState)
    LinearLayout llHandlerState;
    @InjectView(R.id.et_searchContent)
    ContainsEmojiEditText etSearchContent;
    @InjectView(tv_query)
    TextView tvQuery;
    @InjectView(R.id.ll_paymentNumber)
    LinearLayout llPaymentNumber;
    @InjectView(R.id.tv_dateNum)
    TextView tvDateNum;
    @InjectView(R.id.mPullToRefreshListView)
    PullToRefreshListView mPullToRefreshListView;
    private String windowNumber = "";
    private String startDate = "";
    private String endDate = "";
    private MapData mapData;
    private MyPopWindow myPopWindow;
    private float xs;
    private float ys;
    private String officeIdStr = "";  //选中归属机构id
    private String state = ""; //预警状态
    private String handleState = ""; //处理状态
    private String type = ""; //类型
    private ListView mListView;
    private ListView lv_popWindow;  //弹出归属机构框list
    private ListView lv_wrnType;  //弹出预警类型框list
    private List<DictAryBean.DataBean> dictAryBean = new ArrayList<>();
    private List<DictAryBean.DataBean> stateList = new ArrayList<>();
    private List<DictAryBean.DataBean> handlerStateList = new ArrayList<>();
    private String next = "";        //返回来的页数
    private String windowsTag = "";//点击哪个下拉框的标识
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private List<NewPayWarningBean.DataBean> payWarningList = new ArrayList<>();
    private CommonAdapter<NewPayWarningBean.DataBean> commonAdapter;
    private Intent intent;

    private String processingState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_warning_info_list);
        ButterKnife.inject(this);
        initWeight();
        initData();
    }

    private void initWeight() {
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
        startDate = DateUtils.getTime();
        endDate = DateUtils.getTime();
        intent = getIntent();
        processingState = intent.getStringExtra("processingState");
        if (processingState == null){
            tvStartDate.setText(startDate);
            tvEndDate.setText(endDate);
        }else {
            startDate = "";
            endDate = "";
            handleState = processingState;
            tvHandlerState.setText("未处理");
        }


        getAlarmList("1");
    }


    /**
     * 弹出归属机构
     */
    private void showOwnershipPop() {
        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局!
        //得到状态栏高度
        //返回键关闭
        mapData = new MapData(NewWarningInfoListA.this, cpd, true) {
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
                                getAlarmList("1");
                                //}
                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop3;
                    }
                }.popupWindowBuilder(NewWarningInfoListA.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                myPopWindow.showAtLocation(llOwnershipInstitution, Gravity.NO_GRAVITY, (int) xs, (int) ys);
//                myPopWindow.showAsDropDown(llOwnershipInstitution);
            }
        };
        //  mapData.setTrainOwnershipData();


    }




    @OnClick({R.id.iv_back, R.id.ll_start_date, R.id.ll_end_date, R.id.ll_warningType, R.id.ll_ownership_institution, R.id.ll_state, R.id.ll_handlerState, R.id.tv_query})
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
                                Toast.makeText(NewWarningInfoListA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvStartDate.setText(date);
                        getAlarmList("1");

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
                                Toast.makeText(NewWarningInfoListA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvEndDate.setText(date);
                        getAlarmList("1");

                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.ll_warningType:
                queryDictAry("fb_alarm_type");
                break;
            case R.id.ll_ownership_institution:
                showOwnershipPop();
                break;
            case R.id.ll_state:
                windowsTag = "state";
                if (stateList.size() > 0){
                    stateList.clear();
                }
                DictAryBean.DataBean stateBean = new DictAryBean.DataBean();
                stateBean.setLabel("全部");
                stateBean.setValue("");
                stateList.add(stateBean);
                DictAryBean.DataBean stateBean2 = new DictAryBean.DataBean();
                stateBean2.setLabel("未恢复");
                stateBean2.setValue("0");
                stateList.add(stateBean2);
                DictAryBean.DataBean stateBean3 = new DictAryBean.DataBean();
                stateBean3.setLabel("已恢复");
                stateBean3.setValue("1");
                stateList.add(stateBean3);
                showWarningType(tvState, stateList);
                break;
            case R.id.ll_handlerState:
                windowsTag = "handlerState";
                if (handlerStateList.size() > 0){
                    handlerStateList.clear();
                }
                DictAryBean.DataBean handlerStateBean = new DictAryBean.DataBean();
                handlerStateBean.setLabel("全部");
                handlerStateBean.setValue("");
                handlerStateList.add(handlerStateBean);
                DictAryBean.DataBean handlerStateBean2 = new DictAryBean.DataBean();
                handlerStateBean2.setLabel("未处理");
                handlerStateBean2.setValue("0");
                handlerStateList.add(handlerStateBean2);
                DictAryBean.DataBean handlerStateBean3 = new DictAryBean.DataBean();
                handlerStateBean3.setLabel("已处理");
                handlerStateBean3.setValue("1");
                handlerStateList.add(handlerStateBean3);
                showWarningType(tvHandlerState, handlerStateList);
                break;

            case R.id.tv_query:
                getAlarmList("1");
                break;
        }
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
                    ToastUtils.showToast(NewWarningInfoListA.this, "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                } else {

                    getAlarmList(next);

                }
            }
        });
    }

    /**
     * 请求汇缴预警列表数据
     */
    private void getAlarmList(String pageNo) {
        String url = Define.URL + "fb/getAlarmList";
        windowNumber = etSearchContent.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("param", windowNumber);
            jsonObject.put("officeId", officeIdStr);
            jsonObject.put("type", type);
            jsonObject.put("state", state);
            jsonObject.put("handleState", handleState);
            jsonObject.put("pageNo", pageNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "getAlarmList", pageNo, true, true);
    }

    private void queryDictAry(String dictType){
        String url = Define.URL + "sys/getDictAry";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dictType", dictType);//通过字典接口查询滞纳金返还状态
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getDictAry", "0", true, false); //0 不代表什么
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getAlarmList":
                Log.d("getAlarmList", json.toString());
                setAlarmListAdapter(json, pageNumber);

                break;

            case "getDictAry":
                dictAryBean.clear();
                Gson gson = new Gson();
                DictAryBean dataBean = gson.fromJson(json.toString(), DictAryBean.class);
                List<DictAryBean.DataBean> dictAryList =  dataBean.getData();
                DictAryBean.DataBean dataBean1 = new DictAryBean.DataBean();
                dataBean1.setLabel("全部");
                dataBean1.setValue("");
                dictAryBean.add(dataBean1);
                windowsTag = "type";
                if (dictAryList != null && dictAryList.size() > 0){
                    dictAryBean.addAll(dictAryList);
                    showWarningType(tvType, dictAryBean);
                }
                Log.d("dictAryBean", dictAryBean.toString());
                break;
        }
    }

    private void showWarningType(final TextView textView, final List<DictAryBean.DataBean> dataList) {
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                lv_wrnType = (ListView) view.findViewById(R.id.lv_audit_status);
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //重新测量
//                int w =  lv_applyType.getMeasuredWidth();
//                cow = Math.abs(w - xOffset);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                textView.getLocationOnScreen(arr);
                view.measure(0, 0);
                xs = arr[0] + textView.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + textView.getHeight();

                CommonAdapter<DictAryBean.DataBean> commonAdapter = new CommonAdapter<DictAryBean.DataBean>(NewWarningInfoListA.this, dataList, R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, DictAryBean.DataBean data) {
                        holder.setText(R.id.tv_text, data.getLabel());

                    }
                };

                lv_wrnType.setAdapter(commonAdapter);
                lv_wrnType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String applyTypeStr = dataList.get(i).getLabel(); //获取点击的文字
                        textView.setText(applyTypeStr);
                        //获取点击的類型對應的代號
                        if ("type".equals(windowsTag)) {
                            type = dataList.get(i).getValue();
                        }else if ("state".equals(windowsTag)){
                            state = dataList.get(i).getValue();
                        }else if ("handlerState".equals(windowsTag)){
                            handleState = dataList.get(i).getValue();
                        }
                        myPopWindow.dissmiss();
                        getAlarmList("1");
//                        queryLateFeeList("1");
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(this).create();
        myPopWindow.keyCodeDismiss(true); //返回键关闭
        myPopWindow.showAtLocation(textView, Gravity.NO_GRAVITY, (int) xs, (int) ys);
    }

    private void setAlarmListAdapter(JSONObject json, String pageNo) {
        Gson gson = new Gson();
        NewPayWarningBean newPayWarningBean = gson.fromJson(json.toString(), NewPayWarningBean.class);

        if (pageNo.equals("1")) {
            //集成清空处理
            payWarningList.clear();
            if (commonAdapter != null) {
                mListView.setAdapter(commonAdapter);
            }
        }
        List<NewPayWarningBean.DataBean> dataBeenList = newPayWarningBean.getData();

        if (dataBeenList != null && dataBeenList.size() > 0) {
            payWarningList.addAll(dataBeenList);
        }
        next = String.valueOf(newPayWarningBean.getNext());
        tvDateNum.setText(newPayWarningBean.getSize() + "");
        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<NewPayWarningBean.DataBean>(NewWarningInfoListA.this, payWarningList, R.layout.item_newwarning_list) {
                @Override
                public void convert(ViewHolder holder, NewPayWarningBean.DataBean dataBean) {
                    holder.setText(R.id.tv_date, dataBean.getCreateDateString())
                            .setText(R.id.tv_ownership_institution, dataBean.getParentName())
                            .setText(R.id.tv_winNumber, dataBean.getWinNumber())
                            .setText(R.id.tv_warningNumber, dataBean.getAlarmNumber())
                            .setText(R.id.tv_warningType, dataBean.getTypeName())
                            .setText(R.id.tv_state, dataBean.getStateName())
                            .setText(R.id.tv_handlerState, dataBean.getHandleStateName());
                }
            };
            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NewPayWarningBean.DataBean dataBean = payWarningList.get(position - 1);
                ObjectSaveUtil.saveObject(NewWarningInfoListA.this, "alarmList", dataBean);
                intent = new Intent(NewWarningInfoListA.this, NewWarningDetailsA.class);
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
                getAlarmList("1");
                break;
        }
    }


}

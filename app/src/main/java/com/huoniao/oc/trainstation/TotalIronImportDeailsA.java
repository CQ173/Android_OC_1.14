package com.huoniao.oc.trainstation;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.TotalIronImportBean;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.treeIdList2;

public class TotalIronImportDeailsA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.tv_startDate)
    TextView tvStartDate;
    @InjectView(R.id.layout_startDataChoice)
    RelativeLayout layoutStartDataChoice;
    @InjectView(R.id.tv_endDate)
    TextView tvEndDate;
    @InjectView(R.id.layout_endDataChoice)
    RelativeLayout layoutEndDataChoice;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;
    @InjectView(R.id.tv_dataNum)
    TextView tvDataNum;
    @InjectView(R.id.mPullToRefreshListView)
    PullToRefreshListView mPullToRefreshListView;

    private MyPopWindow myPopWindow;
    private MapData mapData;
    private float xs;
    private float ys;
    private String officeIdStr = "";  //临时的归属机构id
    private String officeNodeName = "";
    private String beginDate;
    private String endDate;
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private String next = "";        //返回来的页数
    private ListView mListView;
    private List<TotalIronImportBean.DataBean.ListBean> importList = new ArrayList<>();
    private CommonAdapter<TotalIronImportBean.DataBean.ListBean> commonAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_iron_import_deails);
        ButterKnife.inject(this);
        initWeight();
        initData();
    }

    private void initData() {
        beginDate = DateUtils.getTime();
        endDate = DateUtils.getTime();
        tvStartDate.setText(beginDate);
        tvEndDate.setText(endDate);
        requestPaymentDetails("1", true);

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


    @OnClick({R.id.iv_back, R.id.layout_startDataChoice, R.id.layout_endDataChoice, R.id.tv_ownership_institution})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_startDataChoice:
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        beginDate = date;
                        if ((!beginDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(beginDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(TotalIronImportDeailsA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvStartDate.setText(date);
                        requestPaymentDetails("1", true);

                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.layout_endDataChoice:

                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        endDate = date;
                        if ((!beginDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(beginDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(TotalIronImportDeailsA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvEndDate.setText(date);
                        requestPaymentDetails("1", true);

                    }

                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.tv_ownership_institution:

                if (RepeatClickUtils.repeatClick()) {
                    showOwnershipPop();
                }
                break;
        }
    }


    /**
     * 获取归属机构数据 弹出归属机构
     */
    private void showOwnershipPop() {
        treeIdList2.clear(); //清空归属机构记录的节点
        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
        //得到状态栏高度
        //返回键关闭
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        mapData = new MapData(TotalIronImportDeailsA.this, cpd, false) {
            @Override
            protected void showTrainMapDialog() {
                super.showTrainMapDialog();
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }

                myPopWindow = new MyPopAbstract() {
                    @Override
                    protected void setMapSettingViewWidget(View view) {
                        ListView lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
                        mapData.setTrainOwnershipData(lv_audit_status);
                        //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                        int[] arr = new int[2];
                        tvOwnershipInstitution.getLocationOnScreen(arr);
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                        xs = arr[0] + tvOwnershipInstitution.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + tvOwnershipInstitution.getHeight();
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node officeId) {
                                officeIdStr = String.valueOf(officeId.getAllTreeNode().id); // 归属机构筛选id
                                officeNodeName = officeId.getAllTreeNode().name;

                                requestPaymentDetails("1", true);

                                tvOwnershipInstitution.setText(officeNodeName);

                                myPopWindow.dissmiss();

                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop3;
                    }
                }.popupWindowBuilder(TotalIronImportDeailsA.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                myPopWindow.showAtLocation(tvOwnershipInstitution, Gravity.NO_GRAVITY, (int) xs, (int) ys);
            }
        };
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
                    ToastUtils.showToast(TotalIronImportDeailsA.this, "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                } else {

                    requestPaymentDetails(next, true);

                }
            }
        });
    }

    /**
     * 请求汇缴详情
     *
     * @param off 控制关闭加载框
     */
    private void requestPaymentDetails(String pageNo, boolean off) {
        String url = Define.URL + "fb/getPaymentDetails";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", beginDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("officeId", officeIdStr);
            jsonObject.put("pageNo", pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getPaymentDetails", pageNo, off, true); //1没有实际意义值
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getPaymentDetails":
                Log.d("paymentDetailsData", json.toString());
                setAdapter(json, pageNumber);
                break;

        }
    }

    private void setAdapter(JSONObject jsonObject, String pageNo) {
        Gson gson = new Gson();
        TotalIronImportBean totalIronImportBean = gson.fromJson(jsonObject.toString(), TotalIronImportBean.class);
        TotalIronImportBean.DataBean.StatisticalBean statisticalBean = totalIronImportBean.getData().getStatistical();
        tvDataNum.setText(statisticalBean.getPaymentSize() + "");

        if (pageNo.equals("1")) {
            //集成清空处理
            importList.clear();
            if (commonAdapter != null) {
                mListView.setAdapter(commonAdapter);
            }
        }
        List<TotalIronImportBean.DataBean.ListBean> dataBeenList = totalIronImportBean.getData().getList();

        if (dataBeenList != null && dataBeenList.size() > 0) {
            importList.addAll(dataBeenList);
        }
        next = String.valueOf(totalIronImportBean.getNext());

//        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<TotalIronImportBean.DataBean.ListBean>(TotalIronImportDeailsA.this, importList, R.layout.item_totaliron_importdeails) {
                @Override
                public void convert(ViewHolder holder, TotalIronImportBean.DataBean.ListBean dataBean) {
                    holder.setText(R.id.tv_name, dataBean.getOfficeName())
                          .setText(R.id.tv_salesVolume, dataBean.getTicketCount() + "")
                          .setText(R.id.tv_salesAmount, dataBean.getShouldAmountString() + "元");
                }
            };
            mListView.setAdapter(commonAdapter);
//        }
//        commonAdapter.notifyDataSetChanged();

    }

}

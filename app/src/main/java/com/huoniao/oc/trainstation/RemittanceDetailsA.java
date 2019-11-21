package com.huoniao.oc.trainstation;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.RemittanceDetailsBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.treeIdList2;

public class RemittanceDetailsA extends BaseActivity {

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
    @InjectView(R.id.et_station_account_number_str)
    EditText etStationAccountNumberStr;
    @InjectView(R.id.tv_select)
    TextView tvSelect;
    @InjectView(R.id.ll_station_account_number_str)
    LinearLayout llStationAccountNumberStr;
    @InjectView(R.id.tv_dataNum)
    TextView tvDataNum;
    @InjectView(R.id.tv_shouldAmount)
    TextView tvShouldAmount;
    @InjectView(R.id.tv_amountReceived)
    TextView tvAmountReceived;
    @InjectView(R.id.tv_agencyPayment)
    TextView tvAgencyPayment;
    @InjectView(R.id.myListView)
    MyListView myListView;
    @InjectView(R.id.activity_remittance_details)
    LinearLayout activityRemittanceDetails;

    private MyPopWindow myPopWindow;
    private MapData mapData;
    private float xs;
    private float ys;
    private String officeIdStr = "";  //临时的归属机构id
    private String officeNodeName = "";
    private String beginDate;
    private String endDate;
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private String officeType = "";
    private String officeName = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remittance_details);
        ButterKnife.inject(this);
        initWeight();
        initData();
    }

    private void initWeight() {
        if (myDatePickerDialog == null) {
            myDatePickerDialog = new MyDatePickerDialog();
        }

    }

    private void initData() {
        beginDate = DateUtils.getOldDate(-1);//获取前一天日期
        endDate  = DateUtils.getOldDate(-1);
        tvStartDate.setText(beginDate);
        tvEndDate.setText(endDate);

        requestTodayPaymentImport(true);
    }

    @OnClick({R.id.iv_back, R.id.layout_startDataChoice, R.id.layout_endDataChoice, R.id.tv_ownership_institution, R.id.tv_select})
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
                                Toast.makeText(RemittanceDetailsA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvStartDate.setText(date);


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
                                Toast.makeText(RemittanceDetailsA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        tvEndDate.setText(date);


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
            case R.id.tv_select:
                if (RepeatClickUtils.repeatClick()) {
                    requestTodayPaymentImport(true);
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
        mapData = new MapData(RemittanceDetailsA.this, cpd, false) {
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

                                requestTodayPaymentImport(true);

                                tvOwnershipInstitution.setText(officeNodeName);

                                myPopWindow.dissmiss();

                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop3;
                    }
                }.popupWindowBuilder(RemittanceDetailsA.this).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                myPopWindow.showAtLocation(tvOwnershipInstitution, Gravity.NO_GRAVITY, (int) xs, (int) ys);
            }
        };
    }

    /**
     * 请求汇缴详情
     *
     * @param off 控制关闭加载框
     */
    private void requestTodayPaymentImport(boolean off) {
        officeName = etStationAccountNumberStr.getText().toString().trim();
        String url = Define.URL + "fb/getPaymentStatisticalDetails";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("beginDate", beginDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("officeId", officeIdStr);
            jsonObject.put("officeName", officeName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentStatisticalDetails", "1", off, true); //1没有实际意义值
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "paymentStatisticalDetails":
                Log.d("statisticalData", json.toString());
                setAdapter(json);
                break;

        }
    }

    private void setAdapter(JSONObject jsonObject) {
        Gson gson = new Gson();
        RemittanceDetailsBean remittanceDetailsBean = gson.fromJson(jsonObject.toString(), RemittanceDetailsBean.class);
        RemittanceDetailsBean.DataBean.StatisticalBean statisticalBean = remittanceDetailsBean.getData().getStatistical();
        if (statisticalBean.getShouldReceive() != null){
            tvShouldAmount.setText(statisticalBean.getShouldReceive() + "元");
        }

        if (statisticalBean.getRealReceive() != null){
            tvAmountReceived.setText(statisticalBean.getRealReceive() + "元");
        }

        if (statisticalBean.getRealPay() != null){
            tvAgencyPayment.setText(statisticalBean.getRealPay() + "元");
        }
        tvDataNum.setText(statisticalBean.getSize() + "");

        final List<RemittanceDetailsBean.DataBean.ListBean> listBean = remittanceDetailsBean.getData().getList();
        CommonAdapter<RemittanceDetailsBean.DataBean.ListBean> adpter = new CommonAdapter<RemittanceDetailsBean.DataBean.ListBean>(RemittanceDetailsA.this, listBean, R.layout.item_remittance_details) {
            @Override
            public void convert(ViewHolder holder, RemittanceDetailsBean.DataBean.ListBean listBean) {
                holder.setText(R.id.tv_officeName, listBean.getOfficeName())
                      .setText(R.id.tv_shouldReceive, listBean.getShouldReceive())
                      .setText(R.id.tv_amountReceived2, listBean.getRealReceive())
                      .setText(R.id.tv_agencyPayment2, listBean.getRealPay());

                ProgressBar mProgressBar1 = holder.getView(R.id.mProgressBar1);
                ProgressBar mProgressBar2 = holder.getView(R.id.mProgressBar2);
                ProgressBar mProgressBar3 = holder.getView(R.id.mProgressBar3);
                int shouldReceiveInt = 0;
                int realReceiveInt = 0;
                int realPayInt = 0;

                double shouldReceiveD;
                double realReceiveD;
                double realPayD;

                if (listBean.getShouldReceive() != null) {
                    shouldReceiveD = Double.valueOf(listBean.getShouldReceive());
                    shouldReceiveInt = getInt(shouldReceiveD * 100);
                }
                if (listBean.getRealReceive() != null) {
                    realReceiveD = Double.valueOf(listBean.getRealReceive());
                    realReceiveInt = getInt(realReceiveD * 100);
                }
                if (listBean.getRealPay() != null) {
                    realPayD = Double.valueOf(listBean.getRealPay());
                    realPayInt = getInt(realPayD * 100);
                }

                int max = (shouldReceiveInt>realReceiveInt)?shouldReceiveInt:realReceiveInt;//比较三个数值大小
                max = (max>realPayInt)?max:realPayInt;
                mProgressBar1.setMax(max);
                mProgressBar2.setMax(max);
                mProgressBar3.setMax(max);
                mProgressBar1.setProgress(shouldReceiveInt);
                mProgressBar2.setProgress(realReceiveInt);
                mProgressBar3.setProgress(realPayInt);

            }
        };

        myListView.setAdapter(adpter);

            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RemittanceDetailsBean.DataBean.ListBean dataBean = listBean.get(i);
                    officeIdStr = dataBean.getOfficeId();
                    officeType = dataBean.getOfficeType();
                    if (!Define.TRAINSTATION.equals(officeType)) {  //当到了火车站层级将不能点击
                        requestTodayPaymentImport(true);

                    }else {
                        return;
                    }

                }
            });


    }


    /**
     * double转int四舍五入
     * @param number
     * @return
     */
    private int getInt(double number){
        BigDecimal bd=new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
        return Integer.parseInt(bd.toString());
    }

}

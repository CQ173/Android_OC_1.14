package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.StationDetailsBean;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.util.ByteToInputStreamUtils;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.ContainsEmojiEditText;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.FileRequest;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/6/6.
 */

public class StationStatisticsDetailsActivity extends BaseActivity {

    @InjectView(R.id.tv_start_date)
    TextView tvStartDate;
    @InjectView(R.id.ll_start_date)
    LinearLayout llStartDate;
    @InjectView(R.id.tv_end_date)
    TextView tvEndDate;
    @InjectView(R.id.ll_end_date)
    LinearLayout llEndDate;
    @InjectView(R.id.mPullToRefreshListView)
    PullToRefreshListView mPullToRefreshListView;
    @InjectView(R.id.tv_Record)
    TextView tv_Record;
    @InjectView(R.id.et_searchContent)
    ContainsEmojiEditText etSearchContent;
    @InjectView(R.id.tv_Name_and_quantity)
    TextView tv_Name_and_quantity;
    @InjectView(R.id.tv_Total_sales_volume)
    TextView tv_Total_sales_volume;
    @InjectView(R.id.tv_Total_sales)
    TextView tv_Total_sales;
    @InjectView(R.id.tv_Total_Payment)
    TextView tv_Total_Payment;
    @InjectView(R.id.layout_appbar)
    AppBarLayout layout_appbar;

    private FileRequest fileRequest;
    private InputStream is;
    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private ListView mListView;
    private String next = "";        //返回来的页数

    private Intent intent;

    private String detailsstartDate = "";
    private String detailsendDate = "";
    private String stationId = "";
    private String order = "";
    private String agencyName = "";
    private int  agencyCount;
    private String stationName;

    private List<StationDetailsBean.DataBean.ListBean> payWarningList = new ArrayList<>();
    private CommonAdapter<StationDetailsBean.DataBean.ListBean> commonAdapter;

    private int quantitycount=0;
    private int amountcount=0;
    private int unpaidcount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_statistics_details);
        ButterKnife.inject(this);
        initWeight();
        initData();
    }

    private void initData() {

        intent = getIntent();//intent传递
        detailsstartDate = intent.getStringExtra("detailsstartDate"); //开始和结束时间
        detailsendDate = intent.getStringExtra("detailsendDate");
        stationName = intent.getStringExtra("StationName");//名称
        agencyCount = intent.getIntExtra("AgencyCount" ,1);//代售点个数
        stationId = intent.getStringExtra("StationId");//车站ID
        tv_Name_and_quantity.setText(stationName +"（代售点数量："+agencyCount + "家）"); //名称和代售点数量
        tvStartDate.setText(detailsstartDate);
        tvEndDate.setText(detailsendDate);
        getTrainPaymentSumDetail("1");

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
                    ToastUtils.showToast(StationStatisticsDetailsActivity.this, "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                } else {

                    getTrainPaymentSumDetail(next);

                }
            }
        });
    }

    @OnClick({R.id.ll_start_date , R.id.ll_end_date  , R.id.ll_Sales_volumes ,R.id.ll_sales_amount ,R.id.ll_Unpaid_amount ,
            R.id.tv_query ,R.id.iv_top ,R.id.tv_Import})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_start_date:
                myDatePickerDialog.datePickerDialog(this);
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        detailsstartDate = date;
                        if ((!detailsstartDate.isEmpty()) && (!detailsendDate.isEmpty())) {
                            if (Date.valueOf(detailsstartDate).after(Date.valueOf(detailsendDate))) {
                                Toast.makeText(StationStatisticsDetailsActivity.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvStartDate.setText(date);
                        getTrainPaymentSumDetail("1");
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
                        detailsendDate = date;
                        if ((!detailsstartDate.isEmpty()) && (!detailsendDate.isEmpty())) {
                            if (Date.valueOf(detailsstartDate).after(Date.valueOf(detailsendDate))) {
                                Toast.makeText(StationStatisticsDetailsActivity.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvEndDate.setText(date);
                        getTrainPaymentSumDetail("1");
                    }
                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.ll_Sales_volumes://销售数量
                quantitycount++;
                if (quantitycount != 0){
                    amountcount = 3;
                    unpaidcount=3;
                }
                if (quantitycount > 3){
                    quantitycount = 1;
                }
                Log.i("cunt--------" , quantitycount+"");
                if (quantitycount == 1){
                    order = "ticketCountAsc";
                    getTrainPaymentSumDetail("1");
                }else if (quantitycount == 2){
                    order = "ticketCountDesc";
                    getTrainPaymentSumDetail("1");
                }else if (quantitycount == 3){
                    order = "";
                    getTrainPaymentSumDetail("1");
                }
                break;
            case R.id.ll_sales_amount://销售金额
                amountcount++;
                if (amountcount != 0){
                    quantitycount = 3;
                    unpaidcount=3;
                }
                if (amountcount > 3){
                    amountcount = 1;
                }
                Log.i("cunt--------" , amountcount+"");
                if (amountcount == 1){
                    order = "shouldAmountAsc";
                    getTrainPaymentSumDetail("1");
                }else if (amountcount == 2){
                    order = "shouldAmountDesc";
                    getTrainPaymentSumDetail("1");
                }else if (amountcount == 3){
                    order = "";
                    getTrainPaymentSumDetail("1");
                }
                break;
            case R.id.ll_Unpaid_amount://未付金额
                unpaidcount++;
                if (unpaidcount != 0){
                    quantitycount = 3;
                    amountcount=3;
                }
                if (unpaidcount > 3){
                    unpaidcount = 1;
                }
                Log.i("cunt--------" , unpaidcount+"");
                if (unpaidcount == 1){
                    order = "unPayAmountAsc";
                    getTrainPaymentSumDetail("1");
                }else if (unpaidcount == 2){
                    order = "unPayAmountDesc";
                    getTrainPaymentSumDetail("1");
                }else if (unpaidcount == 3){
                    order = "";
                    getTrainPaymentSumDetail("1");
                }
                break;
            case R.id.tv_query:
                getTrainPaymentSumDetail("1");
                break;
            case R.id.iv_top:
                layout_appbar.setExpanded(true);
                break;
            case R.id.tv_Import:
                downloadFile("fb/downloadTrainPaymentSumDetail");
                break;
        }
    }

    //volley框架的文件下载方法
    public void downloadFile(final String url){
        agencyName = etSearchContent.getText().toString();
        volleyNetCommon = new VolleyNetCommon();
        Map<String, String> map = new HashMap<>();
        map.put("officeId",stationId);
        map.put("pageNo",next);
        map.put("beginDate",detailsstartDate);
        map.put("endDate",detailsendDate);
        map.put("agencyName",agencyName);
        map.put("order",order);

        fileRequest =volleyNetCommon.fileRequest(Request.Method.POST ,Define.URL + url, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = Environment.getExternalStorageDirectory() + "/OC/";
                try {
                    //byte转换成inputstream
                    is = ByteToInputStreamUtils.Byte2InputStream((byte[]) o);
                    long total = ((byte[]) o).length;
                    //获取文件名称
                    String filename = FileRequest.getfilename();
                    //设置文件名称
                    String setfilename = filename.substring(filename.indexOf("filename=")+9);
                    File file = new File(savePath, setfilename );
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                    }
                    fos.flush();
                    // 下载完成
                    ToastUtils.showToast(StationStatisticsDetailsActivity.this , "文件已经保存至" + savePath + "目录文件夹下");
                    Log.i("getdownloadresponse","download success");
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast(StationStatisticsDetailsActivity.this , "下载失败" );
                    Log.i("getdownloadresponse","download failed");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(StationStatisticsDetailsActivity.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {

            }

            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void PdDismiss() {

            }
        } ,"hahahaha" ,map);
        volleyNetCommon.addQueue(fileRequest);  //添加到队列
    }



    private void getTrainPaymentSumDetail( String page){
        String url = Define.URL + "fb/getTrainPaymentSumDetail";
        agencyName = etSearchContent.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId",stationId );
            jsonObject.put("pageNo", page );
            jsonObject.put("beginDate", detailsstartDate);
            jsonObject.put("endDate", detailsendDate);
            jsonObject.put("agencyName", agencyName);
            jsonObject.put("order", order);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "getTrainPaymentSumDetail", page, true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "getTrainPaymentSumDetail":
                getTrainPaymentSumDetailadapter(json , pageNumber);
                break;
        }
    }


    private void getTrainPaymentSumDetailadapter(JSONObject jsonObject , String pageNumber){
        Gson gson = new Gson();
        StationDetailsBean stationDetailsBean = gson.fromJson(jsonObject.toString() , StationDetailsBean.class);
        if (pageNumber.equals("1")) {
            //集成清空处理
            payWarningList.clear();
            if (commonAdapter != null) {
                mListView.setAdapter(commonAdapter);
            }
        }
        List<StationDetailsBean.DataBean.ListBean> dataBeanList =  stationDetailsBean.getData().getList();
        if (dataBeanList != null && dataBeanList.size() > 0) {
            payWarningList.addAll(dataBeanList);
        }
//        detailsstartDate = stationDetailsBean.getData().getStatistical().getBeginDate();
//        detailsendDate = stationDetailsBean.getData().getStatistical().getBeginDate();
        next = String.valueOf(stationDetailsBean.getNext());
        tv_Record.setText( "共"+ stationDetailsBean.getData().getStatistical().getListCount() +"条记录");
        tv_Total_sales_volume.setText("销售数量："+stationDetailsBean.getData().getStatistical().getTicketCountSum() +"张");
        tv_Total_sales.setText("销售总额："+stationDetailsBean.getData().getStatistical().getShouldAmountSum());
        tv_Total_Payment.setText("实付总额："+stationDetailsBean.getData().getStatistical().getPayAmountSum());

        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<StationDetailsBean.DataBean.ListBean>(this, payWarningList, R.layout.item_station_details) {
                @Override
                public void convert(ViewHolder holder, StationDetailsBean.DataBean.ListBean dataBean) {
                    holder.setText(R.id.tv_Name_of_agency, dataBean.getStandby1())
                            .setText(R.id.tv_Window_number, dataBean.getStandby2())
                            .setText(R.id.tv_Sales_volumes, dataBean.getSum1String())
                            .setText(R.id.tv_sales_amount, dataBean.getSum2String())
                            .setText(R.id.tv_Payment_amount, dataBean.getSum3String())
                            .setText(R.id.tv_Unpaid_amount, dataBean.getSum4String());
                }
            };
            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();

    }
}

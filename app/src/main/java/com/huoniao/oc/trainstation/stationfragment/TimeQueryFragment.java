package com.huoniao.oc.trainstation.stationfragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.StationStatisticsBean;
import com.huoniao.oc.common.MapData;
import com.huoniao.oc.common.MyDatePickerDialog;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.tree.Node;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.trainstation.CustomSortingActivity;
import com.huoniao.oc.trainstation.StationStatisticsDetailsActivity;
import com.huoniao.oc.util.ByteToInputStreamUtils;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.ContainsEmojiEditText;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RepeatClickUtils;
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

import static com.huoniao.oc.MyApplication.treeIdList2;

/**
 * Created by Administrator on 2019/6/3.
 */

public class TimeQueryFragment extends BaseFragment {

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
    @InjectView(R.id.tv_time)
    TextView tv_time;
    @InjectView(R.id.tv_number_commission_outlets)
    TextView tv_number_commission_outlets;
    @InjectView(R.id.tv_Total_amount_payable)
    TextView tv_Total_amount_payable;
    @InjectView(R.id.tv_Total_Platform_Contributions)
    TextView tv_Total_Platform_Contributions;
    @InjectView(R.id.tv_type)
    TextView tv_type;
    @InjectView(R.id.tv_this_year)
    TextView tv_this_year;
    @InjectView(R.id.tv_this_month)
    TextView tv_this_month;
    @InjectView(R.id.tv_this_tendayperiod)
    TextView tv_this_tendayperiod;
    @InjectView(R.id.tv_this_week)
    TextView tv_this_week;
    @InjectView(R.id.tv_this_Today)
    TextView tv_this_Today;
    @InjectView(R.id.et_searchContent)
    ContainsEmojiEditText etSearchContent;
    @InjectView(R.id.ll_LinearLayout_a)
    LinearLayout ll_LinearLayout_a;
    @InjectView(R.id.layout_appbar)
    AppBarLayout layout_appbar;

    private FileRequest fileRequest;
    private InputStream is;

    private MyDatePickerDialog myDatePickerDialog;   //时间控件
    private ListView mListView;
    private String next = "";        //返回来的页数

    private String officeId = "";
    private String query = "";
    private String startDate = "";
    private String endDate = "";
    private String quickQuery = "";
    private String queryType = "timeScope";
    private String Year = "";
    private String Month = "";
    private String tenDays = "";
    private String order = "";

    private int quantitycount=0;
    private int amountcount=0;

    private List<StationStatisticsBean.DataBean.ListBean> payWarningList = new ArrayList<>();
    private CommonAdapter<StationStatisticsBean.DataBean.ListBean> commonAdapter;
    private String detailsstartDate;
    private String detailsendDate;

    public TimeQueryFragment (){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_timequery,null);
        ButterKnife.inject(this, view);

        return view;
    }

    private void initData() {
        startDate = DateUtils.getTime();
        endDate = DateUtils.getTime();
        tvStartDate.setText(startDate);
        tvEndDate.setText(endDate);
        getRemittancedata("1");
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
                    ToastUtils.showToast(getContext(), "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                } else {

                    getRemittancedata(next);

                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWeight();
        initData();
    }

    @OnClick({R.id.ll_start_date , R.id.ll_end_date , R.id.ll_warningType , R.id.tv_this_year ,R.id.tv_this_month ,
            R.id.tv_this_tendayperiod , R.id.tv_this_week , R.id.tv_this_Today , R.id.tv_query , R.id.ll_By_quantity , R.id.ll_By_amount ,
            R.id.iv_top ,R.id.ll_Custom_Sorting,R.id.tv_Import})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_start_date:
                myDatePickerDialog.datePickerDialog(getContext());
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        startDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(getContext(), "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvStartDate.setText(date);
                        getRemittancedata("1");
                    }
                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.ll_end_date:
                myDatePickerDialog.datePickerDialog(getContext());
                myDatePickerDialog.setDatePickerListener(new MyDatePickerDialog.DatePicker() {
                    @Override
                    public void date(final String date) {
                        endDate = date;
                        if ((!startDate.isEmpty()) && (!endDate.isEmpty())) {
                            if (Date.valueOf(startDate).after(Date.valueOf(endDate))) {
                                Toast.makeText(getContext(), "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        tvEndDate.setText(date);
                        getRemittancedata("1");
                    }
                    @Override
                    public void splitDate(int year, int monthOfYear, int dayOfMonth) {

                    }
                });
                break;
            case R.id.ll_warningType:
                if (RepeatClickUtils.repeatClick()) {
                    showOwnershipPop();
                }
                break;
            case R.id.tv_this_year://年
                quickQuery = "year";
                String year = DateUtils.getTime12();
                tvStartDate.setText(year+"-01-01");
                getRemittancedata("1");
                break;
            case R.id.tv_this_month://月
                quickQuery = "month";
                String month = DateUtils.getFirstdayofThisMonth();
                tvStartDate.setText(month);
                getRemittancedata("1");
                break;
            case R.id.tv_this_tendayperiod://旬
                quickQuery = "tenDays";
                getRemittancedata("1");
                break;
            case R.id.tv_this_week://周
                quickQuery = "week";
                getRemittancedata("1");
                break;
            case R.id.tv_this_Today://日
                quickQuery = "day";
                getRemittancedata("1");
                break;
            case R.id.tv_query://查询按钮
                getRemittancedata("1");
                break;
            case R.id.ll_By_quantity://按代售点数量
                quantitycount++;
                if (quantitycount != 0){
                    amountcount = 3;
                }
                if (quantitycount > 3){
                    quantitycount = 1;
                }
                Log.i("cunt--------" , quantitycount+"");
                if (quantitycount == 1){
                    order = "agencyuntAsc";
                    getRemittancedata("1");
                }else if (quantitycount == 2){
                    order = "agencyCountDesc";
                    getRemittancedata("1");
                }else if (quantitycount == 3){
                    order = "";
                    getRemittancedata("1");
                }
                break;
            case R.id.ll_By_amount://按应缴款金额
                amountcount++;
                if (amountcount != 0){
                    quantitycount = 3;
                }
                if (amountcount > 3){
                    amountcount = 1;
                }
                Log.i("cunt--------" , amountcount+"");
                if (amountcount == 1){
                    order = "shouldAmountAsc";
                    getRemittancedata("1");
                }else if (amountcount == 2){
                    order = "shouldAmountDesc";
                    getRemittancedata("1");
                }else if (amountcount == 3){
                    order = "";
                    getRemittancedata("1");
                }
                break;
            case R.id.iv_top:
                layout_appbar.setExpanded(true);
                break;
            case R.id.ll_Custom_Sorting:
                startActivityMethod(CustomSortingActivity.class);
                break;
            case R.id.tv_Import:
                downloadFile("fb/downloadTrainPaymentSumList");
                break;
        }
    }


    //volley框架的文件下载方法
    public void downloadFile(final String url){
        query = etSearchContent.getText().toString();
        volleyNetCommon = new VolleyNetCommon();
        Map<String, String> map = new HashMap<>();
        map.put("officeId",officeId);
        map.put("query",query);
        map.put("pageNo",next);
        map.put("beginDate",startDate);
        map.put("endDate",endDate);
        map.put("quickQuery",quickQuery);
        map.put("queryType",queryType);
        map.put("year",Year);
        map.put("month",Month);
        map.put("tenDays",tenDays);
        map.put("order",order);

        fileRequest =volleyNetCommon.fileRequest(Request.Method.POST ,Define.URL + url, new VolleyAbstract(baseActivity) {
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
                    ToastUtils.showToast(getContext() , "文件已经保存至" + savePath + "目录文件夹下");
                    Log.i("getdownloadresponse","download success");
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast(getContext() , "下载失败" );
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
                Toast.makeText(getContext(), R.string.netError, Toast.LENGTH_SHORT).show();
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

    private MyPopWindow myPopWindow;
    private MapData mapData;
    private float xs;
    private float ys;
//    private String officeIdStr = "";  //临时的归属机构id
    private String officeNodeName = "";
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
        mapData = new MapData(baseActivity, cpd, false) {
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
                        tv_type.getLocationOnScreen(arr);
                        view.measure(0, 0);
                        Rect frame = new Rect();
                        baseActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
                        xs = arr[0] + tv_type.getWidth() - view.getMeasuredWidth();
                        ys = arr[1] + tv_type.getHeight();
                        mapData.setMapTreeNodeResultLinstener(new MapTreeNodeResult() {
                            @Override
                            public void treeNodeResult(Node node) {
                                officeId = String.valueOf(node.getAllTreeNode().id); // 归属机构筛选id
                                officeNodeName = node.getAllTreeNode().name;
                                tv_type.setText(officeNodeName);
                                myPopWindow.dissmiss();
                            }
                        });
                    }

                    @Override
                    protected int layout() {
                        return R.layout.admin_audit_status_pop3;
                    }
                }.popupWindowBuilder(baseActivity).create();
                myPopWindow.keyCodeDismiss(true); //返回键关闭
                myPopWindow.showAtLocation(tv_type, Gravity.NO_GRAVITY, (int) xs, (int) ys);
            }
        };
    }


    /**
     * 请求车站汇缴统计
     */
    private void getRemittancedata(String pageNo) {
        String url = Define.URL + "fb/getTrainPaymentSumList";
        query = etSearchContent.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("officeId",officeId );
            jsonObject.put("query", query);
            jsonObject.put("pageNo", pageNo );
            jsonObject.put("beginDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("quickQuery", quickQuery);
            jsonObject.put("queryType",queryType );
            jsonObject.put("year",Year );
            jsonObject.put("month", Month);
            jsonObject.put("tenDays", tenDays);
            jsonObject.put("order", order);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestNet(url, jsonObject, "getTrainPaymentSumList", pageNo, true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag){
            case "getTrainPaymentSumList":
//                Log.i("dataaaaaaaaa" , json.toString());
                setTrainPaymentListAdmin(json, pageNumber);
                break;
        }
    }

    private void setTrainPaymentListAdmin( JSONObject jsonObject , String pageNumber){
        Gson gson = new Gson();
        StationStatisticsBean stationStatisticsBean = gson.fromJson(jsonObject.toString() , StationStatisticsBean.class);
        if (pageNumber.equals("1")) {
            //集成清空处理
            payWarningList.clear();
            if (commonAdapter != null) {
                mListView.setAdapter(commonAdapter);
            }
        }
        List<StationStatisticsBean.DataBean.ListBean> dataBeanList = stationStatisticsBean.getData().getList();
        if (dataBeanList != null && dataBeanList.size() > 0) {
            payWarningList.addAll(dataBeanList);
        }
        detailsstartDate = stationStatisticsBean.getData().getStatistical().getBeginDate();
        detailsendDate = stationStatisticsBean.getData().getStatistical().getBeginDate();
        next = String.valueOf(stationStatisticsBean.getNext());
        tv_Record.setText( "共"+ dataBeanList.size() +"条记录");
        tv_time.setText(detailsstartDate + "到" + detailsendDate);
        tv_number_commission_outlets.setText("代售点总数量："+stationStatisticsBean.getData().getStatistical().getAgencyCountSum() +"家");
        tv_Total_amount_payable.setText("应缴款总额："+stationStatisticsBean.getData().getStatistical().getShouldAmountSum());
        tv_Total_Platform_Contributions.setText("平台缴款总额："+stationStatisticsBean.getData().getStatistical().getPayAmountSum());

        if (commonAdapter == null) {
            commonAdapter = new CommonAdapter<StationStatisticsBean.DataBean.ListBean>(getContext(), payWarningList, R.layout.item_station_statiscs_list) {
                @Override
                public void convert(ViewHolder holder, StationStatisticsBean.DataBean.ListBean dataBean) {
                    holder.setText(R.id.tv_Station_name, dataBean.getStationName())
                            .setText(R.id.tv_Number, dataBean.getAgencyCount()+"")
                            .setText(R.id.tv_Number_entries, dataBean.getDataCount()+"")
                            .setText(R.id.tv_Amount_payable, dataBean.getShouldAmountString())
                            .setText(R.id.tv_Payment_amount, dataBean.getPayAmountString());
                }
            };
            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StationStatisticsBean.DataBean.ListBean listBean = payWarningList.get(position -1);
//                ObjectSaveUtil.saveObject(getContext(), "listBean", listBean);
                Intent intent = new Intent(getContext() , StationStatisticsDetailsActivity.class);
                intent.putExtra("detailsstartDate" , detailsstartDate);
                intent.putExtra("detailsendDate" , detailsendDate);
                intent.putExtra("StationName" , listBean.getStationName());
                intent.putExtra("AgencyCount" , listBean.getAgencyCount());
                intent.putExtra("StationId" , listBean.getStationId());
                startActivity(intent);
                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

}

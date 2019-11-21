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
import com.huoniao.oc.bean.TimeBean;
import com.huoniao.oc.common.MapData;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MyApplication.treeIdList2;
import static com.huoniao.oc.util.DateUtils.getTime12;

/**
 * Created by Administrator on 2019/6/3.
 */

public class YearQueryFragment extends BaseFragment {

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
    @InjectView(R.id.et_searchContent)
    ContainsEmojiEditText etSearchContent;
    @InjectView(R.id.tv_type)
    TextView tv_type;
    @InjectView(R.id.tv_year)
    TextView tv_year;
    @InjectView(R.id.tv_month)
    TextView tv_month;
    @InjectView(R.id.tv_tenDays)
    TextView tv_tenDays;
    @InjectView(R.id.layout_appbar)
    AppBarLayout layout_appbar;
    @InjectView(R.id.tv_Upper_level)
    TextView tv_Upper_level;
    @InjectView(R.id.tv__next_lower_level)
    TextView tv__next_lower_level;

    private FileRequest fileRequest;
    private InputStream is;

    private ListView lv_wrnType;  //弹出选择类型框list
    private String windowsTag = "";//点击哪个下拉框的标识
    private ListView mListView;
    private String next = "";        //返回来的页数

    private String officeId = "";
    private String query = "";
    private String startDate = "";
    private String endDate = "";
    private String quickQuery = "";
    private String queryType = "yearMonthTenDay";
    private String Year = "";
    private String Month = "";
    private String tenDays = "";
    private String order = "";

    private List<StationStatisticsBean.DataBean.ListBean> payWarningList = new ArrayList<>();
    private CommonAdapter<StationStatisticsBean.DataBean.ListBean> commonAdapter;

    private int quantitycount=0;
    private int amountcount=0;
    private String detailsstartDate;
    private String detailsendDate;


    public YearQueryFragment (){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_yearquery,null);
        ButterKnife.inject(this, view);
        return view;
    }
    private String aYaer;
    private String aMonth;
    private int atenDays;
    private void init(){
        String settimes = DateUtils.getTime();
        String[] settime = settimes.split("-");
        for (int i = 0; i < settime.length; i++) {
            aYaer = settime[0];
            aMonth = settime[1];
            atenDays = Integer.parseInt(settime[2]);
        }
//        Log.i("年月日---" , settimes);
            Month = aMonth;
            Year = aYaer;

        tv_year.setText(aYaer+"年");
        tv_month.setText(aMonth+"月");
        if (atenDays>0 && atenDays<=10){
            tv_tenDays.setText("上旬");
            tenDays = "early";
        }else if (atenDays>11 && atenDays <=20){
            tv_tenDays.setText("中旬");
            tenDays = "middle";
        }else if (atenDays >21 && atenDays<=31){
            tv_tenDays.setText("下旬");
            tenDays = "last";
        }
        getRemittancedata("1");
    }

    private void initWeight() {
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullToRefreshListView.getRefreshableView();

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

    int yeara = 1;

    //    private List<String> stateList = new ArrayList<>();
    private List<TimeBean> timeBeanList = new ArrayList<>();
    @OnClick({R.id.tv_query , R.id.ll_warningType , R.id.tv_year , R.id.tv_month , R.id.tv_tenDays , R.id.ll_By_quantity , R.id.ll_By_amount ,
            R.id.iv_top , R.id.tv_Upper_level , R.id.tv__next_lower_level , R.id.ll_Custom_Sorting , R.id.tv_Import})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_query:
                getRemittancedata("1");
                break;
            case R.id.ll_warningType:
                if (RepeatClickUtils.repeatClick()) {
                    showOwnershipPop();
                }
                break;
            case R.id.tv_year:
                tv_Upper_level.setEnabled(true);
                tv__next_lower_level.setEnabled(true);
                windowsTag = "tv_year";
                if (timeBeanList.size() > 0){
                    timeBeanList.clear();
                }
                int year = Integer.parseInt(getTime12());
                for (int i = 2017; i <= year; i++) {
                    TimeBean timeBean = new TimeBean();
                    timeBean.setId(i+"");
                    timeBean.setName(i +"年");
                    timeBeanList.add(timeBean);
                }
                showWarningType(tv_year, timeBeanList);
                break;
            case R.id.tv_month:
                tv_Upper_level.setEnabled(true);
                tv__next_lower_level.setEnabled(true);
                windowsTag = "tv_month";
                if (timeBeanList.size() > 0){
                    timeBeanList.clear();
                }
                for (int i = 0; i <= 12; i++) {
                    TimeBean timeBean = new TimeBean();
                    if (i == 0){
                        timeBean.setId("");
                        timeBean.setName("无(空)");
                    }else if (i>0 && i<=9){
                        timeBean.setId("0"+i);
                        timeBean.setName(i+ "月");
                    }else if (i >= 10) {
                        timeBean.setId(i + "");
                        timeBean.setName(i + "月");
                    }
                    timeBeanList.add(timeBean);
                }
                showWarningType(tv_month, timeBeanList);
                break;
            case R.id.tv_tenDays:
                tv_Upper_level.setEnabled(true);
                tv__next_lower_level.setEnabled(true);
                windowsTag = "tv_tenDays";
                if (timeBeanList.size() > 0){
                    timeBeanList.clear();
                }
                for (int i = 0; i <= 3; i++) {
                    TimeBean timeBean = new TimeBean();
                    if (i == 0){
                        timeBean.setId("");
                        timeBean.setName("无(空)");
                    }else if (i==1){
                        timeBean.setId("early");
                        timeBean.setName("上旬");
                    }else if (i == 2) {
                        timeBean.setId("middle");
                        timeBean.setName("中旬");
                    }else if (i == 3) {
                        timeBean.setId("last");
                        timeBean.setName("下旬");
                    }
                    timeBeanList.add(timeBean);
                }
                showWarningType(tv_tenDays, timeBeanList);
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
            case R.id.tv_Upper_level:
                tv__next_lower_level.setEnabled(true);
                int intmonth; //转数据类型
                int intyear = Integer.parseInt(Year);
                if (Month.equals("")){
                    intmonth =  0;
                }else {
                    intmonth = Integer.parseInt(Month);
                }

                if (Year.equals("2017") && Month.equals("01") && tenDays.equals("early")){
                    ToastUtils.showToast(getContext() , "没有了");
                    tv_Upper_level.setEnabled(false);
                }else if (Year.equals("2017") && Month.equals("01") && tenDays.equals("")){
                    ToastUtils.showToast(getContext() , "没有了");
                    tv_Upper_level.setEnabled(false);
                }else if (Year.equals("2017") && Month.equals("") && tenDays.equals("")){
                    ToastUtils.showToast(getContext() , "没有了");
                    tv_Upper_level.setEnabled(false);
                }else if (tenDays.equals("last")){ //下跳中
                    tenDays = "middle";
                    tv_tenDays.setText("中旬");
                    getRemittancedata("1");
                }else if (tenDays.equals("middle")){ //中跳上
                    tenDays = "early";
                    tv_tenDays.setText("上旬");
                    getRemittancedata("1");
                }else if (tenDays.equals("early")){//上跳下
                    tenDays = "last";
                    tv_tenDays.setText("下旬");
                    if (!Month.equals("01")){ //如果月不等于1，那就 -1
                        if (intmonth<=10){
                            Month = String.valueOf("0"+(intmonth -1));
                        }else {
                            Month = String.valueOf((intmonth -1));
                        }
                        tv_month.setText(Month+"月");
                        getRemittancedata("1");
                    }else if (Month.equals("01")){
                        Year = String.valueOf((intyear-1));//如果月等于1，那么年-1 ，月回滚到 12
                        Month = "12";
                        tv_month.setText(Month+"月");
                        tv_year.setText(Year+"年");
                        getRemittancedata("1");
                    }
                }else  if ("".equals(tenDays) && "".equals(Month)){ //月旬为空
                    Year = String.valueOf((intyear - 1));
                    tv_year.setText(Year+"年");
                    Month = "";
                    tenDays = "";
                    getRemittancedata("1");
                }else if ("".equals(tenDays) && !"".equals(Month)){ //月不为空旬为空
                    if (!Month.equals("01")){ //如果月不等于1，那就 -1
                        if (intmonth<=10){
                            Month = String.valueOf("0"+(intmonth -1));
                        }else {
                            Month = String.valueOf((intmonth -1));
                        }
                        tv_month.setText(Month+"月");
                        getRemittancedata("1");
                    }else if (Month.equals("01")){
                        Year = String.valueOf((intyear-1));//如果月等于1，那么年-1 ，月回滚到 12
                        Month = "12";
                        tv_month.setText(Month+"月");
                        tv_year.setText(Year+"年");
                        getRemittancedata("1");
                    }
                }
                Log.i("年月日---" , "年"+Year+ "月"+Month+ "旬"+ tenDays);
                break;
            case R.id.tv__next_lower_level:
                int intmontha; //转数据类型
                int intyeara = Integer.parseInt(Year);
                if (Month.equals("")){
                    intmontha =  0;
                }else {
                    intmontha = Integer.parseInt(Month);
                }

                if (Year.equals(aYaer) && Month.equals(aMonth) && tenDays.equals(tenDays)){
                    ToastUtils.showToast(getContext() , "没有了");
                    tv__next_lower_level.setEnabled(false);
                }else if (Year.equals(aYaer) && Month.equals(aMonth) && tenDays.equals("")){
                    ToastUtils.showToast(getContext() , "没有了");
                    tv__next_lower_level.setEnabled(false);
                }else if (Year.equals(aYaer) && Month.equals("") && tenDays.equals("")){
                    ToastUtils.showToast(getContext() , "没有了");
                    tv__next_lower_level.setEnabled(false);
                }else if (tenDays.equals("early")){ //上跳中
                    tenDays = "middle";
                    tv_tenDays.setText("中旬");
                    getRemittancedata("1");
                }else if (tenDays.equals("middle")){ //中跳上
                    tenDays = "last";
                    tv_tenDays.setText("下旬");
                    getRemittancedata("1");
                }else if (tenDays.equals("last")){//上跳下
                    tenDays = "early";
                    tv_tenDays.setText("上旬");
                    if (!Month.equals("12")){ //如果月不等于1，那就 +1
                        if (intmontha<=8){
                            Month = String.valueOf("0"+(intmontha +1));
                        }else {
                            Month = String.valueOf((intmontha +1));
                        }
                        tv_month.setText(Month+"月");
                        getRemittancedata("1");
                    }else if (Month.equals("12")){
                        Year = String.valueOf((intyeara+1));//如果月等于12，那么年+1 ，月回滚到 1
                        Month = "01";
                        tv_month.setText(Month+"月");
                        tv_year.setText(Year+"年");
                        getRemittancedata("1");
                    }
                }else  if ("".equals(tenDays) && "".equals(Month)){ //月旬为空
                    if (!Year.equals(aYaer)){
                        Year = String.valueOf((intyeara + 1));
                        tv_year.setText(Year+"年");
                    }
                    Month = "";
                    tenDays = "";
                    getRemittancedata("1");
                }else if ("".equals(tenDays) && !"".equals(Month)){ //月不为空旬为空
                    if (!Month.equals("12")){ //如果月不等于1，那就 -1
                        if (intmontha<=8){
                            Month = String.valueOf("0"+(intmontha +1));
                        }else {
                            Month = String.valueOf((intmontha +1));
                        }
                        tv_month.setText(Month+"月");
                        getRemittancedata("1");
                    }else if (Month.equals("12")){
                        Year = String.valueOf((intyeara+1));//如果月等于1，那么年-1 ，月回滚到 12
                        Month = "01";
                        tv_month.setText(Month+"月");
                        tv_year.setText(Year+"年");
                        getRemittancedata("1");
                    }
                }
                Log.i("年月日---" , "年"+Year+ "月"+Month+ "旬"+ tenDays);
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
     * 请求汇缴预警列表数据
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

        requestNet(url, jsonObject, "getTrainPaymentSumListyear", pageNo, true, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWeight();
        init();
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag){
            case "getTrainPaymentSumListyear":
                setTrainPaymentListAdmin(json, pageNumber);
                break;
        }
    }

    private void setTrainPaymentListAdmin(JSONObject jsonObject , String pageNumber){
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

    private void showWarningType(final TextView textView, final List<TimeBean> dataList) {
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

                CommonAdapter<TimeBean> commonAdapter = new CommonAdapter<TimeBean>(getContext(), dataList, R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, TimeBean data) {
                        holder.setText(R.id.tv_text, data.getName());

                    }
                };

                lv_wrnType.setAdapter(commonAdapter);
                lv_wrnType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String applyTypeStr = dataList.get(i).getName(); //获取点击的文字
                        textView.setText(applyTypeStr);
                        //获取点击的類型對應的代號
                        if ("tv_year".equals(windowsTag)) {
                            Year = dataList.get(i).getId();
                        }else if ("tv_month".equals(windowsTag)){
                            if (dataList.get(i).getId().equals("")){
                                tenDays = "";
                                tv_tenDays.setText("无(空)");
                                tv_tenDays.setEnabled(false);
                            }else {
                                tv_tenDays.setEnabled(true);
                            }
                            Month = dataList.get(i).getId();
                        }else if ("tv_tenDays".equals(windowsTag)){
                            tenDays = dataList.get(i).getId();
                            Log.i("tv_tenDays--" , tenDays);
                        }
                        myPopWindow.dissmiss();
                        getRemittancedata("1");
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(getActivity()).create();
        myPopWindow.keyCodeDismiss(true); //返回键关闭
        myPopWindow.showAtLocation(textView, Gravity.NO_GRAVITY, (int) xs, (int) ys);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}

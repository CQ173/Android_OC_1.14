package com.huoniao.oc.fragment.financial;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.SimpleTreeListViewAdapter3;
import com.huoniao.oc.bean.AllTreeNode;
import com.huoniao.oc.bean.FinOjiSummaryData;
import com.huoniao.oc.bean.TreeBean;
import com.huoniao.oc.bean.TreeNewDataBean;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.custom.MySwipeRefreshLayout;
import com.huoniao.oc.fragment.BaseFragment;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinancialOjiF extends BaseFragment implements View.OnClickListener {


    private String finDateStr;
    private TextView tv_totalAssets;
    private TextView tv_actualBalance;
    private TextView tv_advanceFunds;
    private TextView tv_finDate;
    private RelativeLayout rl_resume;
    private ImageView iv_calendarImg;
    private RadioButton rb_day;
    private RadioButton rb_month;
    private RadioButton rb_year;
    private MyListView mFinSumListView;
    private SegmentedGroup segmentedGroup;
    private TextView tv_oJiIncome;
    private TextView tv_oJiExpenditure;
    private TextView tv_surplus;
    private TextView tv_incomeSummary;
    private TextView tv_expenditureSummary;
    private DateUtils dateUtils;
    private FinOjiSummaryData.DataBean.IncomeBean incomeBean;
    private FinOjiSummaryData.DataBean.PayoutBean payoutBean;
    private String oJiSummaryTag = "incomeSummary";//收入汇总or支出汇总标识
    private List<FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX> childListBeanXes;
    private MySwipeRefreshLayout myswipeRefresh;

    public FinancialOjiF() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.financial_view_newoji,null);
        tv_totalAssets = (TextView) view.findViewById(R.id.tv_totalAssets);    //O计-总资产
        tv_actualBalance = (TextView) view.findViewById(R.id.tv_actualBalance); //O计-实际余额
        tv_advanceFunds = (TextView) view.findViewById(R.id.tv_advanceFunds);  //O计-垫付资金
        tv_finDate = (TextView) view.findViewById(R.id.tv_finDate); //O计-日期
        rl_resume = (RelativeLayout)view.findViewById(R.id.rl_resume);   //焦点获取
        dateUtils = new DateUtils();
        finDateStr = dateUtils.getTime();
        tv_finDate.setText(dateUtils.getTime2());
        iv_calendarImg = (ImageView) view.findViewById(R.id.iv_calendarImg);    //O计-日期控件图片
        rb_day = (RadioButton) view.findViewById(R.id.rb_day); //O计-日选择
        rb_month = (RadioButton) view.findViewById(R.id.rb_month);       //O计-月选择
        rb_year = (RadioButton) view.findViewById(R.id.rb_year);       //O计-年选择
        segmentedGroup = (SegmentedGroup) view.findViewById(R.id.segmented4);  //O计-日期控件图片
        mFinSumListView = (MyListView) view.findViewById(R.id.mFinSumListView);// 财务树
        segmentedGroup.setTintColor(Color.rgb(103, 159, 233));
        tv_oJiIncome = (TextView) view.findViewById(R.id.tv_oJiIncome);  //O计-收入
        tv_oJiExpenditure = (TextView) view.findViewById(R.id.tv_oJiExpenditure);//O计-支出
        tv_surplus = (TextView) view.findViewById(R.id.tv_surplus); //O计-结余
        tv_incomeSummary = (TextView) view.findViewById(R.id.tv_incomeSummary); //O计-收入汇总
        tv_expenditureSummary = (TextView) view.findViewById(R.id.tv_expenditureSummary);  //O计-支出汇总
        myswipeRefresh = (MySwipeRefreshLayout) view.findViewById(R.id.myswipeRefresh);//刷新控件
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();
    }

    private String dateChoiceTag = "day"; //O计-记录选择年或月或日的标识
    private void initWidget() {
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.rb_day:
//							dateChoice = "daySelect";
                        tv_finDate.setText(dateUtils.getTime2());
                        dateChoiceTag = "day";
                        finDateStr = dateUtils.getTime();
                        requestFinOjiAccount(true);
                        break;
                    case R.id.rb_month:
//							dateChoice = "monthSelect";
                        tv_finDate.setText(dateUtils.getTime21());
                        dateChoiceTag = "month";
                        finDateStr = dateUtils.getTime11();
                        requestFinOjiAccount(true);
                        break;
                    case R.id.rb_year:
//							dateChoice = "yearSelect";
                        tv_finDate.setText(dateUtils.getTime22());
                        dateChoiceTag = "year";
                        finDateStr = dateUtils.getTime12();
                        requestFinOjiAccount(true);
                        break;
                }
            }
        });

        iv_calendarImg.setOnClickListener(this);
        tv_incomeSummary.setOnClickListener(this);
        tv_expenditureSummary.setOnClickListener(this);


        myswipeRefresh.setColorScheme(colors);
        myswipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myswipeRefresh.isRefreshing()) {
                    requestFinOjiAssets(true);   //目前只刷新了总资产
                }
            }
        });
    }


    private void initData() {
        requestFinOjiAssets(false);
    }

    /**
     * 请求财务O计账户（资产相关数据）
     */
    private void requestFinOjiAssets(boolean off){
        String url = Define.URL+"acct/OCountAssets";
        requestNet(url,new JSONObject(),"OCountAssets","0",off, true);
    }

    private void requestFinOjiAccount(boolean off){
        JSONObject finOjiAccountObj = new JSONObject();
        try {
            finOjiAccountObj.put("time", finDateStr);
            finOjiAccountObj.put("timeType", dateChoiceTag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Define.URL+"acct/OCountAccount";
        requestNet(url,finOjiAccountObj,"OCountAccount","",off, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {
        super.dataSuccess(json, tag, pageNumber, controlOff);
        switch (tag){
            case "OCountAssets":
                getFinOjiAssetsData(json,controlOff);
                break;
            case "OCountAccount":
                getFinOjiAccountData(json,controlOff);
                break;
        }
    }

    @Override
    protected void closeDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.closeDismiss(cpd, tag, controlOff);
        switch (tag){
            case "OCountAssets":
                if(controlOff){
                    cpd.dismiss();
                    stopRefreshing();
                }
                break;
            case "OCountAccount":
                if(controlOff){
                    cpd.dismiss();
                }
                break;
        }
    }


    @Override
    protected void loadControlExceptionDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {
        super.loadControlExceptionDismiss(cpd, tag, controlOff);
        switch (tag){
            case "OCountAssets":
                if(!controlOff){
                    requestFinOjiAccount(true);
                }
                break;
        }
    }

    /**
     * 从后台获取财务O计账户（资产相关数据）
     */
    private void getFinOjiAssetsData(JSONObject json,boolean controlOff){
        try {
            if(!controlOff){
                requestFinOjiAccount(true);
            }
            JSONObject jsonObject = json.getJSONObject("data");
            String userBalance = jsonObject.optString("userBalance");//总资产
            String userMinimum = jsonObject.optString("userMinimum");//垫付资金
            String realBalance = jsonObject.optString("realBalance");//实际余额
            if (userBalance != null){
                tv_totalAssets.setText(userBalance);
            }else {
                tv_totalAssets.setText("");
            }

            if (userMinimum != null){
                tv_advanceFunds.setText(userMinimum);
            }else {
                tv_advanceFunds.setText("");
            }

            if (realBalance != null){
                tv_actualBalance.setText(realBalance);
            }else {
                tv_actualBalance.setText("");
            }



        } catch (JSONException e) {
            e.printStackTrace();
//					if(refreshFlag){
//						financiaStopRefreshing();
//					}
        }

    }
    /**
     * 从后台获取财务O计账户（收入汇总、支出汇总、结余等数据）
     * @param json
     * @param controlOff
     */
    private void getFinOjiAccountData(JSONObject json, boolean controlOff){
        integratedTreeList.clear();  //
        allTreeListCai.clear();
        allTreeListPayOut.clear();
        integratedTreeListPayOut.clear();

        Gson gson = new Gson();
        FinOjiSummaryData finOjiSummaryData = gson.fromJson(json.toString(), FinOjiSummaryData.class);
        incomeBean = finOjiSummaryData.getData().getIncome();
        childListBeanXes = incomeBean.getChildList();
        payoutBean = finOjiSummaryData.getData().getPayout();


//				FinOjiSummaryData.DataBean.PayoutBean payoutBean =  finOjiSummaryData.getData().getPayout();


        String incomeStr = incomeBean.getSum();
        String payoutStr = payoutBean.getSum();
        if (incomeStr != null){
            if ("0".equals(incomeStr)){
                tv_oJiIncome.setText("0.00");
            }else {
                tv_oJiIncome.setText(incomeStr);
            }
        }else {
            tv_oJiIncome.setText("");
        }
        if (payoutStr != null) {
            if (payoutStr.contains("-")) {
                String newPayoutStr = payoutStr.replace("-", "");
                tv_oJiExpenditure.setText(newPayoutStr);
            } else {
                if ("0".equals(payoutStr)){
                    tv_oJiExpenditure.setText("0.00");
                }else {
                    tv_oJiExpenditure.setText(payoutStr);
                }

            }
        }else {
            tv_oJiExpenditure.setText("");
        }
        Double d_income = Double.parseDouble(incomeStr);
        Double d_payout = Double.parseDouble(payoutStr);
        Double d_newPayout = Math.abs(d_payout);//取支出的绝对值
        Double d_fnBalance = d_income - d_newPayout;//结余等于收入减去支出的绝对值
        DecimalFormat df = new DecimalFormat("0.00");
        String fnBalance = df.format(d_fnBalance);
        tv_surplus.setText(fnBalance);

        List<FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX> incomeArr1 = incomeBean.getChildList();
        List<FinOjiSummaryData.DataBean.PayoutBean.ChildListBean> payoutArr1 = payoutBean.getChildList();
        int incomeArr1Size = incomeArr1.size();
        int payoutArr1Size = payoutArr1.size();
        List<FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX> incomeList1 = new ArrayList<FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX>();
        List<FinOjiSummaryData.DataBean.PayoutBean.ChildListBean> payoutList1 = new ArrayList<FinOjiSummaryData.DataBean.PayoutBean.ChildListBean>();

        for (int i = 0; i < incomeArr1Size; i++) {
            FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX incomeData = incomeArr1.get(i);
            incomeList1.add(incomeData);
        }

        for (int i = 0; i < payoutArr1Size; i++) {
            FinOjiSummaryData.DataBean.PayoutBean.ChildListBean payoutData = payoutArr1.get(i);
            payoutList1.add(payoutData);
        }
        // 递归参数清除
        _id=0;
        __id =0;
        pid=0;
        _pid=0;

        setTreeRecursion(incomeList1);
        setTreeRecursion2(payoutList1);
        for (int i = 0; i <allTreeListCai.size() ; i++) {
            TreeNewDataBean treeNewDataBean = allTreeListCai.get(i);

            Log.i("ssssss",treeNewDataBean.name+"-----"+treeNewDataBean.rate+"------"+treeNewDataBean.sum+"-----"+treeNewDataBean.id+"-----"+treeNewDataBean.pid);
        }

        for (int i = 0; i < allTreeListCai.size(); i++) {
            TreeNewDataBean treeNewDataBean = allTreeListCai.get(i);


            TreeBean treeBean = new TreeBean();
            treeBean.set_id(treeNewDataBean.id);
            treeBean.setParentId(treeNewDataBean.pid);
            treeBean.setName(treeNewDataBean.name);

            AllTreeNode allTreeNode =new AllTreeNode();
            allTreeNode.name = treeNewDataBean.name;
            allTreeNode.sum = treeNewDataBean.sum;
            allTreeNode.rate = treeNewDataBean.rate;
            allTreeNode.id = treeNewDataBean.id+"";
            allTreeNode.parentId = treeNewDataBean.pid+"";
            allTreeNode.type="1";
            treeBean.setAllTreeNode(allTreeNode);
            integratedTreeList.add(treeBean);  //最后结果去设置给树 容器
        }

        for (int i = 0; i < allTreeListPayOut.size(); i++) {
            TreeNewDataBean treeNewDataBean = allTreeListPayOut.get(i);


            TreeBean treeBean = new TreeBean();
            treeBean.set_id(treeNewDataBean.id);
            treeBean.setParentId(treeNewDataBean.pid);
            treeBean.setName(treeNewDataBean.name);

            AllTreeNode allTreeNode =new AllTreeNode();
            allTreeNode.name = treeNewDataBean.name;
            allTreeNode.sum = treeNewDataBean.sum;
            allTreeNode.rate = treeNewDataBean.rate;
            allTreeNode.id = treeNewDataBean.id+"";
            allTreeNode.parentId = treeNewDataBean.pid+"";
            allTreeNode.type="1";
            treeBean.setAllTreeNode(allTreeNode);
            integratedTreeListPayOut.add(treeBean);  //最后结果去设置给树 容器
        }

        if(oJiSummaryTag.equals("incomeSummary")){
            setTreeAdapterFinance(oJiSummaryTag);
        }else if(oJiSummaryTag.equals("expenditureSummary")){
            setTreeAdapterFinance(oJiSummaryTag );
        }

    }



    private void setTreeAdapterFinance(String tag){
        if(tag.equals("incomeSummary")) { //收入的
            if (integratedTreeList != null && integratedTreeList.size() > 0) {

                try {
                    SimpleTreeListViewAdapter3 simpleTreeListViewAdapter = new SimpleTreeListViewAdapter3<TreeBean>(mFinSumListView, MyApplication.mContext,
                            integratedTreeList, 10);  //1表示只默认只展开2级菜单
                    mFinSumListView.setAdapter(simpleTreeListViewAdapter);
                    simpleTreeListViewAdapter.notifyDataSetChanged();
                    //	simpleTreeListViewAdapter.expandOrCollapseA();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }else{
                try {
                    List<TreeBean>  integrated = new ArrayList<>(); // 支出增加树形结构 需要的数据
                    SimpleTreeListViewAdapter3	 simpleTreeListViewAdapter4 = new SimpleTreeListViewAdapter3<TreeBean>(mFinSumListView, MyApplication.mContext,
                            integrated, 10);  //1表示只默认只展开
                    mFinSumListView.setAdapter(simpleTreeListViewAdapter4);
                    simpleTreeListViewAdapter4.notifyDataSetChanged();
                    //	simpleTreeListViewAdapter4.expandOrCollapseA();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }else if(tag.equals("expenditureSummary")){
            if (integratedTreeListPayOut != null && integratedTreeListPayOut.size() > 0) {

                try {
                    SimpleTreeListViewAdapter3	 simpleTreeListViewAdapter3 = new SimpleTreeListViewAdapter3<TreeBean>(mFinSumListView, MyApplication.mContext,
                            integratedTreeListPayOut, 10);  //1表示只默认只展开
                    mFinSumListView.setAdapter(simpleTreeListViewAdapter3);
                    simpleTreeListViewAdapter3.notifyDataSetChanged();
                    //	simpleTreeListViewAdapter3.expandOrCollapseA();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }else{
                try {
                    List<TreeBean>  integrated = new ArrayList<>(); // 支出增加树形结构 需要的数据
                    SimpleTreeListViewAdapter3	 simpleTreeListViewAdapter4 = new SimpleTreeListViewAdapter3<TreeBean>(mFinSumListView, MyApplication.mContext,
                            integrated, 10);  //1表示只默认只展开
                    mFinSumListView.setAdapter(simpleTreeListViewAdapter4);
                    simpleTreeListViewAdapter4.notifyDataSetChanged();
                    //simpleTreeListViewAdapter4.expandOrCollapseA();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }


        rl_resume.setFocusable(true);
        rl_resume.setFocusableInTouchMode(true);
        rl_resume.requestFocus();
    }
    private boolean datePickerDialogFlag = true;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //财务-O计-选择日期
            case R.id.iv_calendarImg:
                datePickerDialogFlag = true;
                Calendar cal = Calendar.getInstance();
                DatePickerDialog mDialog = new DatePickerDialog(baseActivity, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        int m1 = monthOfYear + 1;
                        String monthStr = "";
                        if (m1 < 10){
                            monthStr = "0" + m1;
                        }else {
                            monthStr = "" + m1;
                        }
                        if ("day".equals(dateChoiceTag)) {

                            finDateStr = year + "-" + monthStr + "-" + dayOfMonth;

//						finDateStr = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                            tv_finDate.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                        }else if ("month".equals(dateChoiceTag)){


                            finDateStr = year + "-" + monthStr;

                            tv_finDate.setText(year + "年" + (monthOfYear + 1) + "月" );
                        }else if ("year".equals(dateChoiceTag)){
                            finDateStr = year + "";
                            tv_finDate.setText(year + "年");
                        }
                        try {
//						if(mDatas != null){
//							mDatas.clear();
//						}
                            //加这个判断是为了兼容4.1版本以下DatePickerDialog默认点击两次确定的问题
                            if(datePickerDialogFlag) {
                                requestFinOjiAccount(true);
                                datePickerDialogFlag= false;
                            }

//						refreshClose = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                mDialog.show();
                //隐藏不想显示的子控件
                try {
                    DatePicker dp = findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());
                    if (dp != null) {
                        if ("month".equals(dateChoiceTag)) {
                            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                        }else if ("year".equals(dateChoiceTag)){
                            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                            ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            //财务-O计-收入汇总
            case R.id.tv_incomeSummary:
                oJiSummaryTag = "incomeSummary";
                setTextStyle(tv_incomeSummary, R.drawable.textbackgroud5, Color.rgb(255, 255, 255));
                setTextStyle(tv_expenditureSummary, R.drawable.textbackgroud6, Color.rgb(74, 74, 74));
                setTreeAdapterFinance(oJiSummaryTag);
                //TODO
                break;
            //财务-O计-支出汇总
            case R.id.tv_expenditureSummary:
                oJiSummaryTag = "expenditureSummary";
                setTextStyle(tv_expenditureSummary, R.drawable.textbackgroud5, Color.rgb(255, 255, 255));
                setTextStyle(tv_incomeSummary, R.drawable.textbackgroud6, Color.rgb(74, 74, 74));
                setTreeAdapterFinance(oJiSummaryTag);

                //TODO
                break;
        }
    }


    /**
     * 通过遍历方法查找DatePicker里的子控件：年、月、日
     * @param group
     * @return
     */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }


    List<TreeNewDataBean> allTreeListCai = new ArrayList<>();  //收入递归把数据获取到
    List<TreeBean>  integratedTreeList = new ArrayList<>(); // 收入 增加树形结构 需要的数据
    List<TreeNewDataBean> allTreeListPayOut = new ArrayList<>();  //支出递归把数据获取到
    List<TreeBean>  integratedTreeListPayOut = new ArrayList<>(); // 支出增加树形结构 需要的数据


    // 递归的代码-------------------------------------开始---------------------------------------
    private int _id=0;
    private int pid=0;
    /**
     *  设置 收入第一级他没有父类
     * @param childListBeanXes
     */
    private void setTreeRecursion(List<FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX>  childListBeanXes){
        for (int i = 0; i < childListBeanXes.size(); i++) {
            FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX childListBeanX = childListBeanXes.get(i);
            _id += i;
            pid = _id;
            if (childListBeanX.getChildList() != null && childListBeanX.getChildList().size() > 0) {
                allTreeListCai.add(new TreeNewDataBean(childListBeanX.getName(), childListBeanX.getSum(), childListBeanX.getRate(), _id, pid));

                treeRecursionIntermediate(childListBeanX.getChildList(),pid);
            } else {
                allTreeListCai.add(new TreeNewDataBean(childListBeanX.getName(), childListBeanX.getSum(), childListBeanX.getRate(), _id, pid));
            }
        }
    }

    /**
     *  收入 开始终极递归
     * @param childListBeanXes
     * @param pid
     */
    private void treeRecursionIntermediate(List<FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX>  childListBeanXes,int pid){

        if(childListBeanXes !=null && childListBeanXes.size()> 0) {
            for (int i = 0; i < childListBeanXes.size(); i++) {
                _id += i+1;
                FinOjiSummaryData.DataBean.IncomeBean.ChildListBeanX childListBeanX = childListBeanXes.get(i);
                allTreeListCai.add(new TreeNewDataBean(childListBeanX.getName(),childListBeanX.getSum(),childListBeanX.getRate(),_id,pid));
                if(childListBeanX.getChildList()!=null && childListBeanX.getChildList().size()>0){
                    this.pid = _id;
                    _id = _id+1;
                    treeRecursionIntermediate(childListBeanX.getChildList(),this.pid);
                }
            }
        }else{
            _id = 0;
            pid =0;
            return;
        }
    }


    private int __id=0;
    private int _pid=0;
    /**
     *  设置 支出第一级他没有父类
     * @param childListBeanXes
     */
    private void setTreeRecursion2(List<FinOjiSummaryData.DataBean.PayoutBean.ChildListBean>  childListBeanXes){
        for (int i = 0; i < childListBeanXes.size(); i++) {
            FinOjiSummaryData.DataBean.PayoutBean.ChildListBean childListBean = childListBeanXes.get(i);
            __id += i;
            _pid = __id;
            if (childListBean.getChildList() != null && childListBean.getChildList().size() > 0) {
                allTreeListPayOut.add(new TreeNewDataBean(childListBean.getName(), childListBean.getSum(), childListBean.getRate(), __id, _pid));
                treeRecursionIntermediate2(childListBean.getChildList(),_pid);
            } else {
                allTreeListPayOut.add(new TreeNewDataBean(childListBean.getName(), childListBean.getSum(), childListBean.getRate(), __id, _pid));
            }
        }
    }

    /**
     * 设置 支出 开始终极递归
     * @param childListBeanXes
     * @param _pid
     */
    private void treeRecursionIntermediate2(List<FinOjiSummaryData.DataBean.PayoutBean.ChildListBean>  childListBeanXes,int _pid){
        if(childListBeanXes !=null && childListBeanXes.size()> 0) {
            for (int i = 0; i < childListBeanXes.size(); i++) {
                __id += i+1;
                FinOjiSummaryData.DataBean.PayoutBean.ChildListBean childListBeanX = childListBeanXes.get(i);
                allTreeListPayOut.add(new TreeNewDataBean(childListBeanX.getName(),childListBeanX.getSum(),childListBeanX.getRate(),__id,_pid));
                if(childListBeanX.getChildList()!=null && childListBeanX.getChildList().size()>0){
                    this._pid = __id;
                    __id = __id+1;
                    treeRecursionIntermediate2(childListBeanX.getChildList(),this._pid);
                }
            }
        }else{
            __id = 0;
            _pid =0;
            return;
        }
    }
// 递归的代码-------------------------------------结束---------------------------------------


    /**
     * 设置文本背景样式
     * @param textView 控件
     * @param resid 资源id
     * @param color 颜色值
     */
    private void setTextStyle(TextView textView, int resid, int color){
        textView.setBackgroundResource(resid);
        textView.setTextColor(color);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    //关闭刷新动画
    public void stopRefreshing(){
        //刷新完成关闭动画
        baseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myswipeRefresh != null) {
                    myswipeRefresh.setRefreshing(false);
                }
            }
        });
    }
}

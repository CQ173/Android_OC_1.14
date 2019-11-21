package com.huoniao.oc.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.CreditScoreBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.DashboardView;
import com.huoniao.oc.useragreement.RegisterAgreeA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.MainActivity.creditScore;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class OjiCreditA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_creditRules)
    TextView tvCreditRules;
    @InjectView(R.id.dashboard_view)
    DashboardView dashboardView;
    @InjectView(R.id.mPullToRefreshListView)
    PullToRefreshListView mPullToRefreshListView;
    @InjectView(R.id.view_allPoints)
    View viewAllPoints;
    @InjectView(R.id.layout_allPoints)
    LinearLayout layoutAllPoints;
    @InjectView(R.id.view_bonusPoint)
    View viewBonusPoint;
    @InjectView(R.id.layout_bonusPoint)
    LinearLayout layoutBonusPoint;
    @InjectView(R.id.view_deductPoints)
    View viewDeductPoints;
    @InjectView(R.id.layout_deductPoints)
    LinearLayout layoutDeductPoints;
    private ListView mListView;
    private String clickTag = "allPoints";//点击选项标识
    private Intent intent;
//    private String creditScore;
    private User user;
    private String loginName;
    private String pageNext="1";  //请求返回 是否还有下一页 -1表示没有
    private  List<CreditScoreBean.DataBean> myDatas = new ArrayList<>();
    private CommonAdapter<CreditScoreBean.DataBean> commonAdapter;
    private String type = "";
    private String otherOperations;//其它地方跳到这里的标识
    private String creditName;//信用等级名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oji_credit);
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
        initLinsener();
    }

    private void initData() {
        intent = getIntent();
        otherOperations = intent.getStringExtra("adminLookUsr");
        creditName = SPUtils2.getString(OjiCreditA.this, "creditName");
        int creditScoreNum = 0;
        if (creditScore != null){
            creditScoreNum = Integer.parseInt(creditScore);
        }
        dashboardView.setCreditValue(creditScoreNum);
        dashboardView.setCreditName(creditName);
        Object loginResult = readObject(MyApplication.mContext, "loginResult");
        user = (User) loginResult;
        loginName = user.getLoginName();
        if ("adminLookUsr".equals(otherOperations)){//如果是管理员那边点进来
            loginName = intent.getStringExtra("loginName");
        }

        getCreditScoreList("1");
    }



    @OnClick({R.id.iv_back, R.id.tv_creditRules, R.id.layout_allPoints, R.id.layout_bonusPoint, R.id.layout_deductPoints})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_creditRules:
                intent = new Intent(OjiCreditA.this, RegisterAgreeA.class);
                intent.putExtra("url", Define.CREDIT_RULES);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;

            case R.id.layout_allPoints:
                clickTag = "allPoints";
                optionHandler();

                break;

            case R.id.layout_bonusPoint:
                clickTag = "bonusPoints";
                optionHandler();
                break;

            case R.id.layout_deductPoints:
                clickTag = "deductPoints";
                optionHandler();
                break;
        }
    }


    private void optionHandler(){
        if ("allPoints".equals(clickTag)){
            viewAllPoints.setVisibility(View.VISIBLE);
            viewBonusPoint.setVisibility(View.GONE);
            viewDeductPoints.setVisibility(View.GONE);
            type = "";
            getCreditScoreList("1");
        }else if ("bonusPoints".equals(clickTag)){
            viewAllPoints.setVisibility(View.GONE);
            viewBonusPoint.setVisibility(View.VISIBLE);
            viewDeductPoints.setVisibility(View.GONE);
            type = "0";
            getCreditScoreList("1");
        }else if ("deductPoints".equals(clickTag)){
            viewAllPoints.setVisibility(View.GONE);
            viewBonusPoint.setVisibility(View.GONE);
            viewDeductPoints.setVisibility(View.VISIBLE);
            type = "1";
            getCreditScoreList("1");
        }
    }

    /**
     * 获取用户信用积分列表
     */
    private void getCreditScoreList(String pageNo) {
        String url = Define.URL + "user/getCreditScoreList";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginName", loginName);
            jsonObject.put("type", type);
            jsonObject.put("pageNo",pageNo);//页数
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "getCreditScoreList", pageNo, true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "getCreditScoreList":
                setAdapter(json, pageNumber);
                break;
        }
    }

    private void setAdapter(JSONObject jsonObject, String pageNumber) {
        Gson gson = new Gson();
        CreditScoreBean creditScoreBean = gson.fromJson(jsonObject.toString(), CreditScoreBean.class);
        List<CreditScoreBean.DataBean> data = creditScoreBean.getData();

        try {
            if("1".equals(pageNumber)) {
                myDatas.clear();

            }
            pageNext = String.valueOf(jsonObject.getInt("next"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        myDatas.addAll(data);

        if(commonAdapter == null) {
            commonAdapter = new CommonAdapter<CreditScoreBean.DataBean>(OjiCreditA.this, myDatas, R.layout.item_credit_score) {
                @Override
                public void convert(ViewHolder holder, CreditScoreBean.DataBean dataBean) {
                    holder.setText(R.id.tv_instructions, dataBean.getInstructions())
                          .setText(R.id.tv_createDate, dataBean.getCreateDate());


                    TextView tv_score = holder.getView(R.id.tv_score);
                    int score = dataBean.getScore();
                    String scoreStr = String.valueOf(score);
                    String type = dataBean.getType();

                    if ("0".equals(type)){
                        tv_score.setText("+" + scoreStr);
                        tv_score.setTextColor(Color.rgb(77,144,231));
                    }else{
                        tv_score.setText("-" +scoreStr);
                        tv_score.setTextColor(Color.rgb(77,144,231));
                    }

                }
            };
            mListView.setAdapter(commonAdapter);
        }
        commonAdapter.notifyDataSetChanged();

    }


    private void initLinsener() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("-1".equals(pageNext)) {
//                    Toast.makeText(OjiCreditA.this, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                    ToastUtils.showToast(OjiCreditA.this, "没有更多数据了！");
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    });
                } else {
                    getCreditScoreList(pageNext); //查询第一页    //上拉刷新
                }
            }
        });
    }


    /**
     * 关闭刷新框
     */
    @Override
    protected void closeDismiss() {
        super.closeDismiss();
        mPullToRefreshListView.onRefreshComplete();
    }
}

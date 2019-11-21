package com.huoniao.oc.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.bean.StationWindowManageBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SpinerPopWindow;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_searchStatus;

/**
 * Created by Administrator on 2017/7/13.
 */

public class AnchoredAuditListA extends BaseActivity implements MySpinerAdapter.IOnItemSelectListener {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_searchArea)
    TextView tvSearchArea;
    @InjectView(R.id.et_searchContent)
    EditText etSearchContent;
    @InjectView(tv_searchStatus)
    TextView tvSearchStatus;
    @InjectView(R.id.tv_select)
    TextView tvSelect;

    private CommonAdapter<StationWindowManageBean> adapter;
    private Intent intent;
    private String auditState = "";//审核状态
    private String searchContent = "";//查询内容:窗口号/名称/账号
    private String jurisAreaCode = "";//归属区域code
    private SpinerPopWindow mSpinerPopWindow;
    private String CHOICE_TAG;//
    private List<String> auditStateList = new ArrayList<String>();
    private List<StationWindowManageBean> mDatas = new ArrayList<>();
    private boolean refreshClose = true;//标记是否还有数据可加载
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;

    private static final int MANAGEAREA_LIST = 4;
    private static final int MANAGEAREA2_LIST = 5;
    private static final int MANAGEAREA3_LIST = 6;
    private List<String> oneLevelCodeList = new ArrayList<String>();
    private List<String> oneLevelNameList = new ArrayList<String>();
    private List<String> twoLevelCodeList = new ArrayList<String>();
    private List<String> twoLevelNameList = new ArrayList<String>();
    private List<String> threeLevelNameList = new ArrayList<String>();
    private List<String> threeLevelCodeList = new ArrayList<String>();
    private String oneLevelCode = "";
    private String twoLevelCode = "";
    private String threeLevelCode = "";
    private String oneLevel, twoLevel, threeLevel, manageArea;
    private RelativeLayout rl_choiceOneLevel, rl_choiceTwoLevel, rl_choiceThirdLevel;
    private TextView tv_oneLevel, tv_twoLevel, tv_thirdLevel;
    private Button bt_complete;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MANAGEAREA_LIST:
                    @SuppressWarnings("unchecked")
                    List<User> manageAreaList = (List<User>) msg.obj;

                    for (User user : manageAreaList) {
                        oneLevelCodeList.add(user.getOneLevelCode());
                        oneLevelNameList.add(user.getOneLevelName());
                    }

                    break;

                case MANAGEAREA2_LIST: {
                    @SuppressWarnings("unchecked")
                    List<User> twoAreaList = (List<User>) msg.obj;
                    twoLevelNameList.clear();
                    twoLevelCodeList.clear();
                    for (User user : twoAreaList) {
                        twoLevelNameList.add(user.getTwoLevelName());
                        twoLevelCodeList.add(user.getTwoLevelCode());
                    }

                    break;

                }

                case MANAGEAREA3_LIST: {

                    @SuppressWarnings("unchecked")
                    List<User> twoAreaList = (List<User>) msg.obj;
                    threeLevelNameList.clear();
                    threeLevelCodeList.clear();
                    for (User user : twoAreaList) {
                        threeLevelNameList.add(user.getThreeLevelName());
                        threeLevelCodeList.add(user.getThreeLevelCode());
                    }

                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adimin_anchoredaudit);
        ButterKnife.inject(this);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.mListView);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullRefreshListView.getRefreshableView();
        initData();
    }

    private void initData() {

        auditStateList.add("审核通过");
        auditStateList.add("待审核");
        auditStateList.add("审核不通过");
//		auditStateList.add("补充资料待审核");

        try {
            getAnchoredAuditList(jurisAreaCode, searchContent, auditState, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getAllManageArea();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @OnClick({R.id.iv_back, R.id.tv_searchArea, tv_searchStatus, R.id.tv_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_searchArea:
                choiceManageAreaDialog();
                break;
            case tv_searchStatus:
                mSpinerPopWindow = new SpinerPopWindow(this);
                CHOICE_TAG = "1";
                mSpinerPopWindow.refreshData(auditStateList, 0);
                mSpinerPopWindow.setItemListener(this);
                showSpinWindow(tvSearchStatus);
                break;
            case R.id.tv_select:

                try {
                    if(mDatas != null){
                        mDatas.clear();
                    }
                    searchContent = etSearchContent.getText().toString();
                    getAnchoredAuditList(jurisAreaCode, searchContent, auditState, 1);
//						pageNumber = 1;
                    refreshClose = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void choiceManageAreaDialog() {
        // String url = Define.URL+"acct/getCode";
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.selectmanagearea_dialog, null);

        rl_choiceOneLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceOneLevel);
        rl_choiceTwoLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceTwoLevel);
        rl_choiceThirdLevel = (RelativeLayout) view.findViewById(R.id.rl_choiceThirdLevel);
        tv_oneLevel = (TextView) view.findViewById(R.id.tv_oneLevel);
        tv_twoLevel = (TextView) view.findViewById(R.id.tv_twoLevel);
        tv_thirdLevel = (TextView) view.findViewById(R.id.tv_thirdLevel);
        bt_complete = (Button) view.findViewById(R.id.bt_complete);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        final AlertDialog dialog = builder.create();// 获取dialog

        dialog.show();

        rl_choiceOneLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSpinerPopWindow = new SpinerPopWindow(AnchoredAuditListA.this);
                CHOICE_TAG = "4";
                mSpinerPopWindow.refreshData(oneLevelNameList, 0);
                mSpinerPopWindow.setItemListener(AnchoredAuditListA.this);
                showSpinWindow(rl_choiceOneLevel);

            }
        });

        rl_choiceTwoLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneLevelCode == null || oneLevelCode.isEmpty()) {
                    Toast.makeText(AnchoredAuditListA.this, "请先选择第一级！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSpinerPopWindow = new SpinerPopWindow(AnchoredAuditListA.this);
                CHOICE_TAG = "5";
                mSpinerPopWindow.refreshData(twoLevelNameList, 0);
                mSpinerPopWindow.setItemListener(AnchoredAuditListA.this);
                showSpinWindow(rl_choiceTwoLevel);

            }
        });

        rl_choiceThirdLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (twoLevelCode == null || twoLevelCode.isEmpty()) {
                    Toast.makeText(AnchoredAuditListA.this, "请先选择第二级！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSpinerPopWindow = new SpinerPopWindow(AnchoredAuditListA.this);
                CHOICE_TAG = "6";
                mSpinerPopWindow.refreshData(threeLevelNameList, 0);
                mSpinerPopWindow.setItemListener(AnchoredAuditListA.this);
                showSpinWindow(rl_choiceThirdLevel);

            }
        });
        bt_complete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                oneLevel = tv_oneLevel.getText().toString();
                twoLevel = tv_twoLevel.getText().toString();
                threeLevel = tv_thirdLevel.getText().toString();
                if ("选择第一级".equals(oneLevel)) {
                    Toast.makeText(AnchoredAuditListA.this, "请选择第一级！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("选择第二级".equals(twoLevel)) {
                    Toast.makeText(AnchoredAuditListA.this, "请选择第二级！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("选择第三级".equals(threeLevel)) {
                    Toast.makeText(AnchoredAuditListA.this, "请选择第三级！", Toast.LENGTH_SHORT).show();
                    return;
                }

                manageArea = oneLevel + "/" + twoLevel + "/" + threeLevel;
                tvSearchArea.setText(manageArea);
                dialog.dismiss();
            }
        });
    }

    // 选择1级区域管理列表
    private void setManageArea(int pos) {
        if (pos >= 0 && pos <= oneLevelNameList.size()) {
            String value = oneLevelNameList.get(pos);

            tv_oneLevel.setText(value);
        }
    }

    // 获取1级管理区域code
    private String getOneLevelCode(int pos) {
        if (pos >= 0 && pos <= oneLevelCodeList.size()) {
            oneLevelCode = oneLevelCodeList.get(pos);

        }
        return oneLevelCode;
    }

    // 选择2级区域管理列表
    private void setManageArea2(int pos) {
        if (pos >= 0 && pos <= twoLevelNameList.size()) {
            String value = twoLevelNameList.get(pos);

            tv_twoLevel.setText(value);
        }
    }

    // 获取2级管理区域code
    private String getTwoLevelCode(int pos) {
        if (pos >= 0 && pos <= twoLevelCodeList.size()) {
            twoLevelCode = twoLevelCodeList.get(pos);

        }
        return twoLevelCode;
    }

    // 选择3级区域管理列表
    private void setManageArea3(int pos) {
        if (pos >= 0 && pos <= threeLevelNameList.size()) {
            String value = threeLevelNameList.get(pos);

            tv_thirdLevel.setText(value);
        }
    }

    // 获取3级管理区域code
    private String getThreeLevelCode(int pos) {
        if (pos >= 0 && pos <= threeLevelCodeList.size()) {
            threeLevelCode = threeLevelCodeList.get(pos);

        }
        return threeLevelCode;
    }

    @Override
    public void onItemClick(int pos) {
        if ("1".equals(CHOICE_TAG)) {
            setAuditState(pos);
        } else if ("4".equals(CHOICE_TAG)) {
            setManageArea(pos);
            oneLevelCode = getOneLevelCode(pos);
            try {
                getAllManageAreaTwo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("5".equals(CHOICE_TAG)) {
            setManageArea2(pos);
            twoLevelCode = getTwoLevelCode(pos);
            try {
                getAllManageAreaThree();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("6".equals(CHOICE_TAG)) {
            setManageArea3(pos);
            threeLevelCode = getThreeLevelCode(pos);
            jurisAreaCode = threeLevelCode;
        }
    }

    private void showSpinWindow(View v) {
        Log.e("", "showSpinWindow");
        mSpinerPopWindow.setWidth(v.getWidth());
        mSpinerPopWindow.showAsDropDown(v);
    }

    // 选择用户类型
    private void setAuditState(int pos) {
        if (pos >= 0 && pos <= auditStateList.size()) {
            String value = auditStateList.get(pos);

            tvSearchStatus.setText(value);

            auditState = tvSearchStatus.getText().toString();
            // if ("管理员".equals(identity)) {
            // identity = Define.SYSTEM_MANAG_USER;
            // et_windowNumber.setVisibility(View.GONE);
            // et_userName.setVisibility(View.VISIBLE);
            // }else
            if ("审核通过".equals(auditState)) {
                auditState = Define.OUTLETS_NORMAL;
            } else if ("待审核".equals(auditState)) {
                auditState = Define.OUTLETS_PENDIG_AUDIT;
            }else if ("审核不通过".equals(auditState)) {
                auditState = Define.OUTLETS_NOTPASS;
            }
        }
    }

    private void setRefreshPager(final int nextPage) {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshClose) {  //如果有数据 继续刷新
                    try {
                        searchContent = etSearchContent.getText().toString();
//						pageNumber++;
                        getAnchoredAuditList(jurisAreaCode, searchContent, auditState, nextPage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(AnchoredAuditListA.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
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
     * 获取挂靠审核列表
     *
     * @throws Exception
     */
    private void getAnchoredAuditList(String jurisAreaCode, String content, String auditState, int pageNo) throws Exception {
       cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            // operateType: 1 查看  2 添加  3 修改  4 取消申请  5 解除绑定
//            if ("firstList".equals(tag)) {
//                jsonObject.put("operateType", "1");
//            }else if ("selectList".equals(tag)){
            jsonObject.put("jurisAreaCode", jurisAreaCode);
            jsonObject.put("str", content);
            jsonObject.put("auditState", auditState);
            jsonObject.put("pageNo", pageNo);
//            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        String url = Define.URL + "fb/agencyConnectList";
        final List<StationWindowManageBean> winManageList = new ArrayList<StationWindowManageBean>();
        SessionJsonObjectRequest winAnchoredListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            cpd.dismiss();
                            Toast.makeText(AnchoredAuditListA.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            int nextPage = response.getInt("next");
                            setRefreshPager(nextPage);
                            if ("0".equals(responseCode)) {

                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    StationWindowManageBean winManage = new StationWindowManageBean();

                                    JSONObject winManageObj = (JSONObject) jsonArray.get(i);
                                    String id = winManageObj.optString("id");//子代售点id
                                    String winNumber = winManageObj.optString("winNumber");//子代售点窗口号
                                    String agencyName = winManageObj.optString("agencyName");//子代售点名称
                                    String operatorName = winManageObj.optString("operatorName");//子代售点负责人姓名
                                    String operatorMobile = winManageObj.optString("operatorMobile");//子代售点负责人手机
                                    String operatorIdNum = winManageObj.optString("operatorIdNum");//子代售点负责人身份证号
                                    String operatorCardforntSrc = winManageObj.optString("operatorCardforntSrc");//子代售点身份证正面
                                    String operatorCardrearSrc = winManageObj.optString("operatorCardrearSrc");//子代售点身份证反面
                                    String staContIndexSrc = winManageObj.optString("staContIndexSrc");//子代售点合同首页
                                    String staContLastSrc = winManageObj.optString("staContLastSrc");//子代售点合同盖章尾页
                                    String fareAuthorizationSrc = winManageObj.optString("fareAuthorizationSrc");//子代售点授权书
                                    String auditState = winManageObj.optString("auditState");//子代售点审核状态
                                    String auditReason = winManageObj.optString("auditReason");//子代售点审核理由
                                    String officeAreaName = winManageObj.optString("officeAreaName");//主代售点区域名称
                                    String officeCorpName = winManageObj.optString("officeCorpName");//主代售点法人姓名
                                    String officeCorpMobile = winManageObj.optString("officeCorpMobile");//主代售点法人手机号
                                    String officeCorpIdNum = winManageObj.optString("officeCorpIdNum");//主代售点法人身份证号
                                    String officeOperatorName = winManageObj.optString("officeOperatorName");//主代售点负责人姓名
                                    String officeOperatorMoblie = winManageObj.optString("officeOperatorMoblie");//主代售点负责人手机
                                    String officeOperatorIdNum = winManageObj.optString("officeOperatorIdNum");//主代售点负责人身份证号
                                    String officeCode = winManageObj.optString("officeCode");//主代售点编号
                                    String officeName = winManageObj.optString("officeName");//主代售点名称
                                    String officeWinNumber = winManageObj.optString("officeWinNumber");//主代售点窗口号
                                    String updateDate = winManageObj.optString("updateDate");//操作时间


                                    winManage.setId(id);
                                    winManage.setWinNumber(winNumber);
                                    winManage.setAgencyName(agencyName);
                                    winManage.setOperatorName(operatorName);
                                    winManage.setOperatorMobile(operatorMobile);
                                    winManage.setOperatorIdNum(operatorIdNum);
                                    winManage.setOperatorCardforntSrc(operatorCardforntSrc);
                                    winManage.setOperatorCardrearSrc(operatorCardrearSrc);
                                    winManage.setStaContIndexSrc(staContIndexSrc);
                                    winManage.setStaContLastSrc(staContLastSrc);
                                    winManage.setFareAuthorizationSrc(fareAuthorizationSrc);
                                    winManage.setOfficeAreaName(officeAreaName);
                                    winManage.setOfficeCorpName(officeCorpName);
                                    winManage.setOfficeCorpMobile(officeCorpMobile);
                                    winManage.setOfficeCorpIdNum(officeCorpIdNum);
                                    winManage.setOfficeOperatorName(officeOperatorName);
                                    winManage.setOfficeOperatorMoblie(officeOperatorMoblie);
                                    winManage.setOfficeOperatorIdNum(officeOperatorIdNum);
                                    winManage.setOfficeCode(officeCode);
                                    winManage.setOfficeName(officeName);
                                    winManage.setOfficeWinNumber(officeWinNumber);
                                    winManage.setAuditState(auditState);
                                    winManage.setAuditReason(auditReason);
                                    winManage.setUpdateDate(updateDate);
                                    winManageList.add(winManage);
                                }
                                Log.d("debug","winManageList =" + winManageList.size());
                                if(nextPage == 1){
                                    mDatas.clear();
                                }else if (nextPage == -1){
                                    refreshClose = false;
                                }
                                mPullRefreshListView.onRefreshComplete();
                                mDatas.addAll(winManageList);

                                adapter = new CommonAdapter<StationWindowManageBean>(AnchoredAuditListA.this, mDatas, R.layout.win_auditing_item) {
                                    @Override
                                    public void convert(ViewHolder holder, StationWindowManageBean stationWindowManageBean) {
                                        if (stationWindowManageBean != null) {
                                            holder.setText(R.id.tv_outletName, stationWindowManageBean.getOfficeName())
                                                    .setText(R.id.tv_outletCode, stationWindowManageBean.getOfficeCode())
                                                    .setText(R.id.tv_windowNumber, stationWindowManageBean.getWinNumber())
                                                    .setText(R.id.tv_city, stationWindowManageBean.getOfficeAreaName());
                                            String status = stationWindowManageBean.getAuditState();
                                            TextView tv_auditStatus = holder.getView(R.id.tv_auditStatus);
                                            if (Define.AUDIT_STATE_PASS.equals(status)) {
                                                tv_auditStatus.setText("审核通过");

                                            } else if (Define.AUDIT_STATE_WAIT.equals(status)) {
                                                tv_auditStatus.setText("待审核");

                                            } else if (Define.AUDIT_STATE_REFUSE.equals(status)) {
                                                tv_auditStatus.setText("审核不通过");

                                            } else if (Define.ANCHORED_STATE_REMOVE.equals(status)) {
                                                tv_auditStatus.setText("解除挂靠");

                                            }
                                        }
                                    }
                                };
                                mListView.setAdapter(adapter);
                                adapter.refreshData(mDatas);
                                cpd.dismiss();

                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        StationWindowManageBean winManage = mDatas.get(i - 1);
                                        ObjectSaveUtil.saveObject(AnchoredAuditListA.this, "windowAnchoredInfo", winManage);
                                        intent = new Intent(AnchoredAuditListA.this, AnchoredAuditDetailsA.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                    }
                                });



                            } else if ("46000".equals(responseCode)) {
                               cpd.dismiss();
                                Toast.makeText(AnchoredAuditListA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(AnchoredAuditListA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                cpd.dismiss();
                                Toast.makeText(AnchoredAuditListA.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(AnchoredAuditListA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);


            }
        });
        // 解决重复请求后台的问题
        winAnchoredListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        winAnchoredListRequest.setTag("adminAnchoredList");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(winAnchoredListRequest);

    }

    /**
     * 获取所有管理区域
     *
     * @throws Exception
     */
    private void getAllManageArea() throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        cpd.show();
        // String url = Define.URL + "user/getProvinceList";
        String url = Define.URL + "user/getGroupList";
        final List<User> manageAreaList = new ArrayList<User>();
        SessionJsonObjectRequest manageAreaListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (null == response) {
                            Toast.makeText(AnchoredAuditListA.this, "服务器状态异常，请稍后再试", Toast.LENGTH_SHORT);
                            return;
                        }
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    User user = new User();

                                    JSONObject provinceObj = (JSONObject) jsonArray.get(i);
                                    String oneLevelCode = provinceObj.optString("code");
                                    String oneLevelName = provinceObj.getString("name");

                                    user.setOneLevelName(oneLevelName);
                                    user.setOneLevelCode(oneLevelCode);
                                    ;
                                    manageAreaList.add(user);
                                }

                                Runnable proRunnable = new Runnable() {

                                    @Override
                                    public void run() {

                                        Message msg = new Message();
                                        msg.what = MANAGEAREA_LIST;
                                        msg.obj = manageAreaList;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                Thread proThread = new Thread(proRunnable);
                                proThread.start();
                                cpd.dismiss();
                            } else {
                                cpd.dismiss();
                                Toast.makeText(AnchoredAuditListA.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AnchoredAuditListA.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        manageAreaListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        manageAreaListRequest.setTag("manageAreaList");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(manageAreaListRequest);

    }

    /**
     * 根据1级管理区域code获取管理区域列表
     *
     * @throws Exception
     */
    private void getAllManageAreaTwo() throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", oneLevelCode);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (oneLevelCode.isEmpty() || oneLevelCode == null) {
            Toast.makeText(AnchoredAuditListA.this, "请先选择第一级菜单！", Toast.LENGTH_SHORT).show();
            return;
        }
        cpd.show();
        // String url = Define.URL + "user/getCityListByProvice";
        String url = Define.URL + "user/getChildByParent";
        final List<User> twoLeveArealList = new ArrayList<User>();
        SessionJsonObjectRequest twoLeveAreaRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    User user = new User();

                                    JSONObject twoObj = (JSONObject) jsonArray.get(i);
                                    String twoLevelCode = twoObj.optString("code");
                                    String twoLevelName = twoObj.getString("name");

                                    user.setTwoLevelCode(twoLevelCode);
                                    ;
                                    user.setTwoLevelName(twoLevelName);
                                    ;

                                    twoLeveArealList.add(user);
                                }

                                Runnable cityRunnable = new Runnable() {

                                    @Override
                                    public void run() {

                                        Message msg = new Message();
                                        msg.what = MANAGEAREA2_LIST;
                                        msg.obj = twoLeveArealList;
                                        mHandler.sendMessage(msg);
                                    }
                                };
                                cpd.dismiss();

                                Thread twoThread = new Thread(cityRunnable);
                                twoThread.start();

                            } else {
                                cpd.dismiss();
                                Toast.makeText(AnchoredAuditListA.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AnchoredAuditListA.this, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        twoLeveAreaRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        twoLeveAreaRequest.setTag("twoLeveArea");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(twoLeveAreaRequest);

    }

    /**
     * 根据2级管理区域code获取三级管理区域列表
     *
     * @throws Exception
     */
    private void getAllManageAreaThree() throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", twoLevelCode);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (twoLevelCode.isEmpty() || twoLevelCode == null) {
            Toast.makeText(AnchoredAuditListA.this, "请先选择第二级菜单！", Toast.LENGTH_SHORT).show();
            return;
        }
        cpd.show();

        // String url = Define.URL + "user/getCityListByProvice";
        String url = Define.URL + "user/getChildByParent";
        final List<User> threeLeveArealList = new ArrayList<User>();
        SessionJsonObjectRequest threeLeveAreaRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    User user = new User();

                                    JSONObject twoObj = (JSONObject) jsonArray.get(i);
                                    String threeLevelCode = twoObj.optString("code");
                                    String threeLevelName = twoObj.getString("name");

                                    user.setThreeLevelCode(threeLevelCode);
                                    user.setThreeLevelName(threeLevelName);

                                    threeLeveArealList.add(user);
                                }

                                Runnable threeRunnable = new Runnable() {

                                    @Override
                                    public void run() {

                                        Message msg = new Message();
                                        msg.what = MANAGEAREA3_LIST;
                                        msg.obj = threeLeveArealList;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                Thread twoThread = new Thread(threeRunnable);
                                twoThread.start();
                                cpd.dismiss();
                            } else {
                                cpd.dismiss();
                                Toast.makeText(AnchoredAuditListA.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AnchoredAuditListA.this,R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        threeLeveAreaRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        threeLeveAreaRequest.setTag("threeLeveArea");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(threeLeveAreaRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("adminAnchoredList");
        MyApplication.getHttpQueues().cancelAll("manageAreaList");
        MyApplication.getHttpQueues().cancelAll("twoLeveArea");
        MyApplication.getHttpQueues().cancelAll("threeLeveArea");
    }


}

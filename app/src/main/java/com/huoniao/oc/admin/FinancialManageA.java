package com.huoniao.oc.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.huoniao.oc.bean.FinancialBean;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
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

import static com.huoniao.oc.R.id.userList;

/**
 * Created by Administrator on 2017/7/13.
 * 暂时没用到，统一挪到了financial包下的FinancialListA
 */

public class FinancialManageA extends BaseActivity implements MySpinerAdapter.IOnItemSelectListener{

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_applyType)
    TextView tvApplyType;
    @InjectView(R.id.tv_auditStatus)
    TextView tvAuditStatus;
    @InjectView(R.id.tv_select)
    TextView tvSelect;
    @InjectView(R.id.mListView)
    PullToRefreshListView mPullRefreshListView;

    private ListView mListView;
    private boolean refreshClose = true;//标记是否还有数据可加载
    private String applyType = "";//申请类型
    private String applyState = "";//申请状态
    private List<FinancialBean> mDatas = new ArrayList<>();
    CommonAdapter<FinancialBean> adapter;
    private SpinerPopWindow mSpinerPopWindow;
    private String CHOICE_TAG;//
    private MyPopWindow myPopWindow;
    private List<String> applyTypeList = new ArrayList<String>();
    private List<String> applyStateList = new ArrayList<String>();
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_financialmanage);
        ButterKnife.inject(this);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
        mListView = mPullRefreshListView.getRefreshableView();
        initData();
    }

    private void initData() {
        try {
            getFinanciaList(applyType, applyState, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        applyTypeList.add("汇缴付款");
        applyTypeList.add("提现");
        applyTypeList.add("代存");
        applyTypeList.add("代付");
        applyTypeList.add("代扣");

        applyStateList.add("待审核");
        applyStateList.add("审核通过");
        applyStateList.add("审核不通过");
        applyStateList.add("待付款");
        applyStateList.add("付款完成");
        applyStateList.add("付款失败");
        applyStateList.add("待确认");
        applyStateList.add("付款中");
        applyStateList.add("拒绝付款");
        applyStateList.add("失败重新付款");
        applyStateList.add("线下付款");
    }

    @OnClick({R.id.iv_back, R.id.tv_applyType, R.id.tv_auditStatus, R.id.tv_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_applyType:
                mSpinerPopWindow = new SpinerPopWindow(this);
                CHOICE_TAG = "1";
                mSpinerPopWindow.refreshData(applyTypeList, 0);
                mSpinerPopWindow.setItemListener(this);
                showSpinWindow(tvApplyType);
                break;
            case R.id.tv_auditStatus:
                mSpinerPopWindow = new SpinerPopWindow(this);
                CHOICE_TAG = "2";
                mSpinerPopWindow.refreshData(applyStateList, 0);
                mSpinerPopWindow.setItemListener(this);
                showSpinWindow(tvAuditStatus);
                break;
            case R.id.tv_select:
                try {
                    if(mDatas != null){
                        mDatas.clear();
                    }
                    getFinanciaList(applyType, applyState, 1);

                    refreshClose = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showSpinWindow(View v) {
        Log.e("", "showSpinWindow");
        mSpinerPopWindow.setWidth(v.getWidth());
        mSpinerPopWindow.showAsDropDown(v);
    }

    // 选择申请类型
    private void setApplyType(int pos) {
        if (pos >= 0 && pos <= applyTypeList.size()) {
            String value = applyTypeList.get(pos);

            tvApplyType.setText(value);

            applyType = tvApplyType.getText().toString();

            if ("汇缴付款".equals(applyType)) {
                applyType = Define.finApplyType1;
            } else if ("提现".equals(applyType)) {
                applyType = Define.finApplyType2;
            }else if ("代存".equals(applyType)) {
                applyType = Define.finApplyType3;
            }else if ("代付".equals(applyType)) {
                applyType = Define.finApplyType4;
            }else if ("代扣".equals(applyType)) {
                applyType = Define.finApplyType5;
            }
        }
    }

    // 选择申请状态
    private void setApplyState(int pos) {
        if (pos >= 0 && pos <= applyStateList.size()) {
            String value = applyStateList.get(pos);

            tvAuditStatus.setText(value);

            applyState = tvAuditStatus.getText().toString();

            if ("待审核".equals(applyState)) {
                applyState = Define.finApplyState1;
            } else if ("审核通过".equals(applyState)) {
                applyState = Define.finApplyState2;
            }else if ("审核不通过".equals(applyState)) {
                applyState = Define.finApplyState3;
            }else if ("待付款".equals(applyState)) {
                applyState = Define.finApplyState4;
            }else if ("付款完成".equals(applyState)) {
                applyState = Define.finApplyState5;
            }else if ("付款失败".equals(applyState)) {
                applyState = Define.finApplyState6;
            }else if ("待确认".equals(applyState)) {
                applyState = Define.finApplyState7;
            }else if ("付款中".equals(applyState)) {
                applyState = Define.finApplyState8;
            }else if ("拒绝付款".equals(applyState)) {
                applyState = Define.finApplyState9;
            }else if ("失败重新付款".equals(applyState)) {
                applyState = Define.finApplyState10;
            }else if ("线下付款".equals(applyState)) {
                applyState = Define.finApplyState11;
            }
        }
    }

    @Override
    public void onItemClick(int pos) {
        if ("1".equals(CHOICE_TAG)) {
            setApplyType(pos);
        }else if ("2".equals(CHOICE_TAG)){
            setApplyState(pos);
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
//						pageNumber++;
                        getFinanciaList(applyType, applyState, nextPage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(FinancialManageA.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
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
     * 获取财务管理列表
     *
     * @throws Exception
     */
    private void getFinanciaList(String applyType, String applyState, int pageNo) throws Exception {

        cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("applyType", applyType);
            jsonObject.put("state", applyState);
            jsonObject.put("pageNo", pageNo);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        String url = Define.URL + "acct/financeApplyList";
        final List<FinancialBean> finList = new ArrayList<FinancialBean>();
        SessionJsonObjectRequest userListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            cpd.dismiss();
                            Toast.makeText(FinancialManageA.this, "服务器异常！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("financeList", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            int nextPage = response.getInt("next");
                            setRefreshPager(nextPage);
                            if ("0".equals(responseCode)) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    FinancialBean financialBean = new FinancialBean();
                                    JSONObject finObj = (JSONObject) jsonArray.get(i);
                                    String createDate = finObj.optString("createDate");// 时间
                                    String agencyUserOfficeName = finObj.optString("agencyUserOfficeName");// 机构名称
                                    String agencyLoginName = finObj.optString("agencyLoginName");// 代理账户
                                    String agencyUserName = finObj.optString("agencyUserName");// 姓名
									String applyTypeName = finObj.optString("applyTypeName");// 申请类型
									String transferId = finObj.optString("transferId");// 交易单号
									String applyFee = finObj.optString("applyFee");// 金额
                                    String accountName = finObj.optString("accountName");// 账户名称
                                    String openBankName = finObj.optString("openBankName");// 开户行
									String cardNo = finObj.optString("cardNo");// 卡号
                                    String applyUserName = finObj.optString("applyUserName");// 申请人
									String receiptLoginName = finObj.optString("receiptLoginName");// 收款账号
									String receiptName = finObj.optString("receiptName");// 收款人
									String stateName = finObj.optString("stateName");// 状态
                                    String remark = finObj.optString("remark");// 备注
									String reason = finObj.optString("reason");// 理由
									String cashierUserName = finObj.optString("cashierUserName");// 出纳操作人
                                    String auditUserName = finObj.optString("auditUserName");// 会计操作人

                                    financialBean.setCreateDate(createDate);
                                    financialBean.setAgencyUserOfficeName(agencyUserOfficeName);
                                    financialBean.setAgencyLoginName(agencyLoginName);
                                    financialBean.setAgencyUserName(agencyUserName);
                                    financialBean.setApplyTypeName(applyTypeName);
                                    financialBean.setTransferId(transferId);
                                    financialBean.setApplyFee(applyFee);
                                    financialBean.setAccountName(accountName);
                                    financialBean.setOpenBankName(openBankName);
                                    financialBean.setCardNo(cardNo);
                                    financialBean.setApplyUserName(applyUserName);
                                    financialBean.setReceiptLoginName(receiptLoginName);
                                    financialBean.setReceiptName(receiptName);
                                    financialBean.setStateName(stateName);
                                    financialBean.setRemark(remark);
                                    financialBean.setReason(reason);
                                    financialBean.setCashierUserName(cashierUserName);
                                    financialBean.setAuditUserName(auditUserName);

                                    finList.add(financialBean);
                                }

                                Log.d("debug", "用户列表：" + userList);

                                if (nextPage == 1) {
                                    mDatas.clear();
                                } else if (nextPage == -1) {
                                    refreshClose = false;
                                }
                                mPullRefreshListView.onRefreshComplete();
                                mDatas.addAll(finList);

                            if (adapter == null) {
                                adapter = new CommonAdapter<FinancialBean>(FinancialManageA.this, mDatas, R.layout.admin_financialmanage_item) {
                                    @Override
                                    public void convert(ViewHolder holder, FinancialBean financialBean) {
                                        holder.setText(R.id.tv_operationType, financialBean.getApplyTypeName())
                                                .setText(R.id.tv_name, financialBean.getAgencyUserName())
                                                .setText(R.id.tv_money, financialBean.getApplyFee())
                                                .setText(R.id.tv_auditStatus, financialBean.getStateName())
                                                .setText(R.id.tv_agentAccount, financialBean.getAgencyLoginName());

                                    }
                                };

                                mListView.setAdapter(adapter);
                            }
                                adapter.refreshData(mDatas);

                                cpd.dismiss();
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        FinancialBean financialBean = mDatas.get(position - 1);
                                        intent = new Intent(FinancialManageA.this, FinancialApplyDetailsA.class);
                                        intent.putExtra("createDate", financialBean.getCreateDate());
										intent.putExtra("agencyUserOfficeName", financialBean.getAgencyUserOfficeName());
										intent.putExtra("agencyLoginName", financialBean.getAgencyLoginName());
										intent.putExtra("agencyUserName", financialBean.getAgencyUserName());
                                        intent.putExtra("applyTypeName", financialBean.getApplyTypeName());
                                        intent.putExtra("transferId", financialBean.getTransferId());
										intent.putExtra("applyFee", financialBean.getApplyFee());
										intent.putExtra("accountName", financialBean.getAccountName());
                                        intent.putExtra("openBankName", financialBean.getOpenBankName());
                                        intent.putExtra("cardNo", financialBean.getCardNo());
                                        intent.putExtra("applyUserName", financialBean.getApplyUserName());
										intent.putExtra("receiptLoginName", financialBean.getReceiptLoginName());
										intent.putExtra("receiptName", financialBean.getReceiptName());
										intent.putExtra("stateName", financialBean.getStateName());
										intent.putExtra("remark", financialBean.getRemark());
										intent.putExtra("reason", financialBean.getReason());
										intent.putExtra("cashierUserName", financialBean.getCashierUserName());
										intent.putExtra("auditUserName", financialBean.getAuditUserName());
//
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                    }
                                });

                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(FinancialManageA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(FinancialManageA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                cpd.dismiss();
                                Toast.makeText(FinancialManageA.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FinancialManageA.this, R.string.netError, Toast.LENGTH_SHORT).show();
//						Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

            }
        });
        // 解决重复请求后台的问题
        userListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        userListRequest.setTag("adminFinancial");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(userListRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("adminFinancial");
    }
}

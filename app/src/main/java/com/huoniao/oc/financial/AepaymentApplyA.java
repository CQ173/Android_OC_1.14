package com.huoniao.oc.financial;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.Node;
import com.huoniao.oc.bean.NodeResource;
import com.huoniao.oc.bean.StationTreeBean;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.TreeListView;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AepaymentApplyA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_applyType)
    TextView tvApplyType;
    @InjectView(R.id.tv_stationName)
    TextView tvStationName;
    @InjectView(R.id.ll_choiceStation)
    LinearLayout llChoiceStation;
    @InjectView(R.id.tv_repaymentSumMoney)
    TextView tvRepaymentSumMoney;
    @InjectView(R.id.tv_repaymentAccount)
    TextView tvRepaymentAccount;
    @InjectView(R.id.tv_accountBalance)
    TextView tvAccountBalance;
    @InjectView(R.id.et_repaymentMoney)
    EditText etRepaymentMoney;
    @InjectView(R.id.tv_repaymentAccount2)
    TextView tvRepaymentAccount2;
    @InjectView(R.id.et_accountBalance2)
    TextView etAccountBalance2;
    @InjectView(R.id.et_repaymentMoney2)
    EditText etRepaymentMoney2;
    @InjectView(R.id.et_remarks)
    TextView etRemarks;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    @InjectView(R.id.activity_aepayment_apply)
    LinearLayout activityAepaymentApply;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    private String paymentPaySumId;
    private String paymentPayId;
    private String performAction;
    private Intent intent;
    private String officeIds = "1";
    private String state;
    private String fillAmount;
    private String mainAmount;
    private String fillBalance;
    private String remark;
    private String fbPayAmountString;
    private MyPopWindow myPopWindow;
    private TreeListView treeListView;
    private List<NodeResource> nodeResourcesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aepayment_apply);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        InputFilter[] filters = {new CashierInputFilter()};
        etRepaymentMoney.setFilters(filters);
        etRepaymentMoney2.setFilters(filters);
        etAccountBalance2.setFilters(filters);
        intent = getIntent();
        paymentPaySumId = intent.getStringExtra("paymentPaySumId");
        paymentPayId = intent.getStringExtra("paymentPayId");
        performAction = intent.getStringExtra("performAction");
        fbPayAmountString = intent.getStringExtra("fbPayAmountString");
        if ("还款".equals(performAction)) {
            tvTitle.setText("还款申请");
        }else if("付款".equals(performAction)){
            tvTitle.setText("付款申请");
        }
        state = intent.getStringExtra("state");
        tvApplyType.setText(performAction);
        getPaymentPayTreeAmount();
        getPaymentPayFbBalance();
        paymentPayOfficesTree();
    }

    @OnClick({R.id.iv_back, R.id.ll_choiceStation, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_choiceStation:
                showStationPop();
                break;
            case R.id.bt_confirm:
                if ("还款".equals(performAction)) {
                    paymentPayReturnApply();
                }else if("付款".equals(performAction)){
                    paymentPayFbPayApply();
                }
                break;
        }
    }

    private void showStationPop(){
//        if (myPopWindow != null) {
//            myPopWindow.dissmiss();
//        }
        if (myPopWindow == null) {
            myPopWindow = new MyPopAbstract() {
                @Override
                protected void setMapSettingViewWidget(View view) {
              /*  List <NodeResource> list = new ArrayList<NodeResource>();
                NodeResource n1 = new NodeResource("1", "d457ea9122df45ae8a4d9c9178d5a39c", "全部城市", "dfs");//, R.drawable.icon_department
                list.add(n1);
                NodeResource n2 = new NodeResource("d457ea9122df45ae8a4d9c9178d5a39c", "d457ea9122df45ae8a4d9c9178d5a40c", "北京", "dfs");
                list.add(n2);
                NodeResource n3 = new NodeResource("d457ea9122df45ae8a4d9c9178d5a40c", "d457ea9122df45ae8a4d9c9178d5a41c", "金刚狼军团", "dfs");
                list.add(n3);*/

                    LinearLayout ll_listContainer = (LinearLayout) view.findViewById(R.id.ll_listContainer);
                    LinearLayout linearLayout = new LinearLayout(AepaymentApplyA.this);
                    linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    treeListView = new TreeListView(AepaymentApplyA.this, nodeResourcesList);
                    ll_listContainer.addView(treeListView);

                    ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                    iv_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myPopWindow.dissmiss();
                            List<Node> nodeList = treeListView.get();
                            List<String> nodeIdsList = new ArrayList<String>();
                            List<String> nodeNameList = new ArrayList<String>();
                            if (nodeList != null && nodeList.size() > 0) {
                                for (int i = 0; i < nodeList.size(); i++) {
                                    nodeIdsList.add(nodeList.get(i).getCurId());
                                    nodeNameList.add(nodeList.get(i).getTitle());
                                }
                                officeIds = listToString(nodeIdsList);
                                String name = nodeNameList.get(nodeList.size() - 1);
                                tvStationName.setText(name);

                            } else {
                                tvStationName.setText("全部");
                            }
                            getPaymentPayTreeAmount();
                        }
                    });

                }

                @Override
                protected int layout() {
                    return R.layout.fin_station_choice_pop;
                }
            }.poPwindow(this, false);
        }

        myPopWindow.showAtLocation(ivBack, Gravity.CENTER, 0, 0);

    }



    private static String listToString(List<String> officeIdsList) {
        StringBuffer stringBuffer = new StringBuffer();

            for (String ids : officeIdsList) {

                stringBuffer.append(ids).append(",");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);

        return stringBuffer.toString();
    }



    /**
     * .获取付款申请、还款申请、还款审核金额
     */
    public void getPaymentPayTreeAmount() {
        String url = Define.URL + "acct/paymentPayTreeAmount";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentPaySumId", paymentPaySumId);
            jsonObject.put("officeIds", officeIds);
            jsonObject.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentPayTreeAmount", "1", true, false);
    }

    /**
     * 获取余额
     */
    public void getPaymentPayFbBalance() {
        String url = Define.URL + "acct/paymentPayFbBalance";
        JSONObject jsonObject = new JSONObject();
        requestNet(url, jsonObject, "paymentPayFbBalance", "1", true, false);
    }


    /**
     * 还款申请
     */
    public void paymentPayReturnApply() {
        mainAmount = etRepaymentMoney.getText().toString();
        if (mainAmount == null || mainAmount.isEmpty()){
            ToastUtils.showToast(AepaymentApplyA.this, "5911账号还款金额不能为空！");
            return;
        }
        fillAmount = etRepaymentMoney2.getText().toString();
        if (fillAmount == null || fillAmount.isEmpty()){
            ToastUtils.showToast(AepaymentApplyA.this, "补款账号还款金额不能为空！");
            return;
        }
        fillBalance = etAccountBalance2.getText().toString();

        if (fillBalance == null || fillBalance.isEmpty()){
            ToastUtils.showToast(AepaymentApplyA.this, "补款账号余额不能为空！");
            return;
        }
        remark = etRemarks.getText().toString().trim();
        if (remark == null || remark.isEmpty()){
            ToastUtils.showToast(AepaymentApplyA.this, "备注不能为空！");
            return;
        }
//        fillBalance = "100";
        String url = Define.URL + "acct/paymentPayReturnApply";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentPaySumId", paymentPaySumId);
            jsonObject.put("officeIds", officeIds);
            jsonObject.put("mainAmount", mainAmount);
            jsonObject.put("fillAmount", fillAmount);
            jsonObject.put("fillBalance", fillBalance);
            jsonObject.put("remark", remark);
            jsonObject.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentPayReturnApply", "1", true, false);
    }


    /**
     * 付款申请
     */
    public void paymentPayFbPayApply() {
        remark = etRemarks.getText().toString().trim();
        if (remark == null || remark.isEmpty()){
            ToastUtils.showToast(AepaymentApplyA.this, "备注不能为空！");
            return;
        }
        String url = Define.URL + "acct/paymentPayFbPayApply";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentPayId", paymentPayId);
            jsonObject.put("amount", fbPayAmountString);
            jsonObject.put("remark", remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentPayFbPayApply", "1", true, false);
    }

    /**
     * 获取机构树
     */
    public void paymentPayOfficesTree() {
        String url = Define.URL + "acct/paymentPayOfficesTree";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentPaySumId", paymentPaySumId);
            jsonObject.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentPayOfficesTree", "1", true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "paymentPayTreeAmount":
                try {
                    JSONObject data = json.getJSONObject("data");
                    String result = data.optString("result");
                    if ("success".equals(result)) {
                        String amount = data.optString("amount");//总金额
                        //补款账号还款金额
                        fillAmount = data.optString("fillAmount");
                        //5911还款金额
                        mainAmount = data.optString("mainAmount");
                        tvRepaymentSumMoney.setText(amount);
                        etRepaymentMoney.setText(mainAmount);
                        etRepaymentMoney2.setText(fillAmount);
                    } else {
                        String msg = data.optString("msg");
                        ToastUtils.showToast(AepaymentApplyA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "paymentPayFbBalance":
                Log.d("paymentPayFbBalance", json.toString());
                try {
                    JSONObject data = json.getJSONObject("data");
                    String result = data.optString("result");
                    if ("success".equals(result)) {
                        //补款账户余额
                        fillBalance = data.optString("fillBalance");
                        String fbBalance = data.optString("fbBalce");//主账户余额
                        tvAccountBalance.setText("￥" + fbBalance);
                        etAccountBalance2.setText(fillBalance);
                    } else {
                        String msg = data.optString("msg");
                        ToastUtils.showToast(AepaymentApplyA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "paymentPayReturnApply":
                try {
                    JSONObject data = json.getJSONObject("data");
                    String result = data.optString("result");
                    if ("success".equals(result)) {
                        setResult(15);
                        MyApplication.getInstence().activityFinish();
                    }else {
                        String msg = data.optString("msg");
                        ToastUtils.showToast(AepaymentApplyA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

            case "paymentPayFbPayApply":
                try {
                    JSONObject data = json.getJSONObject("data");
                    String result = data.optString("result");
                    if ("success".equals(result)) {
                        setResult(15);
                        MyApplication.getInstence().activityFinish();
                    }else {
                        String msg = data.optString("msg");
                        ToastUtils.showToast(AepaymentApplyA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "paymentPayOfficesTree":
                Log.d("paymentPayOfficesTree", json.toString());
                handlerOfficesTree(json);
                break;
        }
    }

    private void handlerOfficesTree(JSONObject jsonObject) {
        nodeResourcesList = new ArrayList<NodeResource>();
        Gson gson = new Gson();
        StationTreeBean stationTreeBean = gson.fromJson(jsonObject.toString(), StationTreeBean.class);
        StationTreeBean.DataBean dataBeen = stationTreeBean.getData();
        NodeResource nodeResourceP = new NodeResource(dataBeen.getParentId(), dataBeen.getId(), dataBeen.getName(), "payOfficesTree");
        nodeResourcesList.add(nodeResourceP);
        List<StationTreeBean.DataBean.ChildListBeanXX> childListBeanXXes = dataBeen.getChildList();
        List<StationTreeBean.DataBean.ChildListBeanXX.ChildListBeanX> childListBeanXesAll = new ArrayList<>();
        List<StationTreeBean.DataBean.ChildListBeanXX.ChildListBeanX.ChildListBean> childListBeenAll = new ArrayList<>();
        if (childListBeanXXes != null && childListBeanXXes.size() > 0) {
            for (int i = 0; i < childListBeanXXes.size(); i++) {
                NodeResource nodeResource1 = new NodeResource(childListBeanXXes.get(i).getParentId(), childListBeanXXes.get(i).getId(), childListBeanXXes.get(i).getName(), "payOfficesTree");
                nodeResourcesList.add(nodeResource1);
                List<StationTreeBean.DataBean.ChildListBeanXX.ChildListBeanX> childListBeanXes = childListBeanXXes.get(i).getChildList();
                if (childListBeanXes != null) {
                    childListBeanXesAll.addAll(childListBeanXes);
                }
            }

            if (childListBeanXesAll != null && childListBeanXesAll.size() > 0) {
                for (int i = 0; i < childListBeanXesAll.size(); i++) {
                    NodeResource nodeResource2 = new NodeResource(childListBeanXesAll.get(i).getParentId(), childListBeanXesAll.get(i).getId(), childListBeanXesAll.get(i).getName(), "payOfficesTree");
                    nodeResourcesList.add(nodeResource2);
                    List<StationTreeBean.DataBean.ChildListBeanXX.ChildListBeanX.ChildListBean> childListBeen = childListBeanXesAll.get(i).getChildList();
                    if (childListBeen != null) {
                        childListBeenAll.addAll(childListBeen);
                    }
                }

                if (childListBeenAll != null && childListBeenAll.size() > 0) {
                    for (int i = 0; i < childListBeenAll.size(); i++) {
                        NodeResource nodeResource3 = new NodeResource(childListBeenAll.get(i).getParentId(), childListBeenAll.get(i).getId(), childListBeenAll.get(i).getName(), "payOfficesTree");
                        nodeResourcesList.add(nodeResource3);
                    }
                }
            }

        }
//        nodeResourcesList.addAll(nodeResourcesList2);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(myPopWindow !=null && myPopWindow.isShow()){
                //如果popwindow是显示状态就 不要做任何处理
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}

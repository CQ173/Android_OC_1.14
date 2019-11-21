package com.huoniao.oc.financial;

import android.content.Intent;
import android.os.Bundle;
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
import com.huoniao.oc.bean.PaymentPayListBean;
import com.huoniao.oc.bean.StationTreeBean;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.common.TreeListView;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AepaymentAuditingA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
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
    @InjectView(R.id.tv_repaymentMoney)
    TextView tvRepaymentMoney;
    @InjectView(R.id.tv_repaymentAccount2)
    TextView tvRepaymentAccount2;
    @InjectView(R.id.tv_accountBalance2)
    TextView tvAccountBalance2;
    @InjectView(R.id.tv_repaymentMoney2)
    TextView tvRepaymentMoney2;
    @InjectView(R.id.ll_aepaymentArea)
    LinearLayout llAepaymentArea;
    @InjectView(R.id.et_remarks)
    EditText etRemarks;
    @InjectView(R.id.bt_passed)
    Button btPassed;
    @InjectView(R.id.bt_noPassed)
    Button btNoPassed;

    private String paymentPaySumId;
    private String performAction;
    private Intent intent;
    private String officeIds = "1";
    private String state;
    private String fillAmount;
    private String mainAmount;
    private String fillBalance;
    private String remark;
    private String paymentPayId;

    private MyPopWindow myPopWindow;
    private TreeListView treeListView;
    private List<NodeResource> nodeResourcesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aepayment_auditing);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        PaymentPayListBean.DataBean dataBean = (PaymentPayListBean.DataBean) ObjectSaveUtil.readObject(AepaymentAuditingA.this, "paymentPayList");
        paymentPaySumId = dataBean.getPaymentPaySumId();
        performAction = intent.getStringExtra("performAction");
        paymentPayId = intent.getStringExtra("paymentPayId");
        if ("还款".equals(performAction)) {
            tvTitle.setText("还款审核");
        }else if("付款".equals(performAction)){
            tvTitle.setText("付款审核");
            llAepaymentArea.setVisibility(View.GONE);
        }
        state = dataBean.getState();
        tvApplyType.setText(performAction);
//        getPaymentPayTreeAmount();
//        getPaymentPayFbBalance();

        getPaymentPayTreeAmount();
        getPaymentPayFbBalance();
        paymentPayOfficesTree();
    }

    @OnClick({R.id.iv_back, R.id.ll_choiceStation, R.id.bt_passed, R.id.bt_noPassed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(15);
                finish();
                break;
            case R.id.ll_choiceStation:
                showStationPop();
                break;
            case R.id.bt_passed:
                remark = etRemarks.getText().toString().trim();
                if (RepeatClickUtils.repeatClick()) {
                    if ("还款".equals(performAction)) {
                        paymentPayReturnAudit("0");
                    } else if ("付款".equals(performAction)) {
                        paymentPayFbPayAudit("0");
                    }
                }
                break;
            case R.id.bt_noPassed:
                remark = etRemarks.getText().toString().trim();
                if (remark == null || remark.isEmpty()){
                    ToastUtils.showToast(AepaymentAuditingA.this, "请输入备注！");
                    return;
                }
                if (RepeatClickUtils.repeatClick()) {
                    if ("还款".equals(performAction)) {
                        paymentPayReturnAudit("1");
                    } else if ("付款".equals(performAction)) {
                        paymentPayFbPayAudit("1");
                    }
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
                    LinearLayout linearLayout = new LinearLayout(AepaymentAuditingA.this);
                    linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    treeListView = new TreeListView(AepaymentAuditingA.this, nodeResourcesList);
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
            }.poPwindow(this, true);
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
        requestNet(url, jsonObject, "paymentPayTreeAmount2", "1", true, false);
    }


    /**
     * 获取余额
     */
    public void getPaymentPayFbBalance() {
        String url = Define.URL + "acct/paymentPayFbBalance";
        JSONObject jsonObject = new JSONObject();
        requestNet(url, jsonObject, "paymentPayFbBalance2", "1", true, false);
    }

    /**
     *
     * 请求还款审核
     */
    private void paymentPayReturnAudit(String auditState) {
        String url = Define.URL + "acct/paymentPayReturnAudit";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentPaySumId", paymentPaySumId);
            jsonObject.put("officeIds", officeIds);
            jsonObject.put("auditState", auditState); // 审核状态0通过1不通过
            jsonObject.put("remark", remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentPayReturnAudit", "0", true, false); //0 不代表什么
    }

    /**
     *
     * 请求付款审核
     */
    private void paymentPayFbPayAudit(String auditState) {
        String url = Define.URL + "acct/paymentPayFbPayAudit";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentPaySumId", paymentPaySumId);
            jsonObject.put("auditState", auditState); // 审核状态0通过1不通过
            jsonObject.put("remark", remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentPayFbPayAudit", "0", true, false); //0 不代表什么
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
        requestNet(url, jsonObject, "paymentPayOfficesTree2", "1", true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "paymentPayReturnAudit":
                handlerAuditing(json, tag);
                break;

            case "paymentPayFbPayAudit":
                handlerAuditing(json, tag);
                break;

            case "paymentPayTreeAmount2":
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
                        tvRepaymentMoney.setText(mainAmount);
                        tvRepaymentMoney2.setText(fillAmount);
                    } else {
                        String msg = data.optString("msg");
                        ToastUtils.showToast(AepaymentAuditingA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "paymentPayFbBalance2":
                try {
                    JSONObject data = json.getJSONObject("data");
                    String result = data.optString("result");
                    if ("success".equals(result)) {
                        //补款账户余额
                        fillBalance = data.optString("fillBalance");
                        String fbBalance = data.optString("fbBalce");//主账户余额
                        tvAccountBalance.setText("￥" + fbBalance);
                        tvAccountBalance2.setText(fillBalance);
                    } else {
                        String msg = data.optString("msg");
                        ToastUtils.showToast(AepaymentAuditingA.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "paymentPayOfficesTree2":
                Log.d("paymentPayOfficesTree", json.toString());
                handlerOfficesTree(json);
                break;
        }
    }


    private void handlerAuditing(JSONObject jsonObject, String tag){
        try {
            JSONObject mJobj = jsonObject.getJSONObject("data");
            String result = mJobj.optString("result");
            if ("success".equals(result)){
                setResult(15);
//                if ("paymentPayReturnAudit".equals(tag)){
                    ToastUtils.showToast(AepaymentAuditingA.this, "审核成功！");
//                }else if ("paymentPayFbPayAudit".equals(tag)){
//                    ToastUtils.showToast(AepaymentAuditingA.this, "审核成功！");
//                }
                finish();
            }else {
                String msg = mJobj.optString("msg");
                ToastUtils.showToast(AepaymentAuditingA.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handlerOfficesTree(JSONObject jsonObject) {
        nodeResourcesList = new ArrayList<NodeResource>();
        Gson gson = new Gson();
        StationTreeBean stationTreeBean = gson.fromJson(jsonObject.toString(), StationTreeBean.class);
        StationTreeBean.DataBean dataBeen = stationTreeBean.getData();
        NodeResource nodeResourceP = new NodeResource("0", dataBeen.getId(), dataBeen.getName(), "payOfficesTree");
        nodeResourcesList.add(nodeResourceP);
        List<StationTreeBean.DataBean.ChildListBeanXX> childListBeanXXes = dataBeen.getChildList();
        List<StationTreeBean.DataBean.ChildListBeanXX.ChildListBeanX> childListBeanXesAll = new ArrayList<>();
        List<StationTreeBean.DataBean.ChildListBeanXX.ChildListBeanX.ChildListBean> childListBeenAll = new ArrayList<>();
        if (childListBeanXXes != null && childListBeanXXes.size() > 0) {
            for (int i = 0; i < childListBeanXXes.size(); i++) {
                NodeResource nodeResource1 = new NodeResource(childListBeanXXes.get(i).getParentId(), childListBeanXXes.get(i).getId(), childListBeanXXes.get(i).getName(), "payOfficesTree");
                nodeResourcesList.add(nodeResource1);
                List<StationTreeBean.DataBean.ChildListBeanXX.ChildListBeanX> childListBeanXes = childListBeanXXes.get(i).getChildList();
                childListBeanXesAll.addAll(childListBeanXes);
            }

            if (childListBeanXesAll != null && childListBeanXesAll.size() > 0) {
                for (int i = 0; i < childListBeanXesAll.size(); i++) {
                    NodeResource nodeResource2 = new NodeResource(childListBeanXesAll.get(i).getParentId(), childListBeanXesAll.get(i).getId(), childListBeanXesAll.get(i).getName(), "payOfficesTree");
                    nodeResourcesList.add(nodeResource2);
                    List<StationTreeBean.DataBean.ChildListBeanXX.ChildListBeanX.ChildListBean> childListBeen = childListBeanXesAll.get(i).getChildList();
                    childListBeenAll.addAll(childListBeen);
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

package com.huoniao.oc.financial;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MyTextWatcher;
import com.huoniao.oc.bean.FinancialBankListBean;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.ContainsEmojiEditText;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.bt_submit;
import static com.huoniao.oc.R.id.lv_audit_status;
import static com.huoniao.oc.R.id.tv_applyType;

/**
 * Created by Administrator on 2017/9/5.
 */

public class LaunchApplyA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_applyType)
    TextView tvApplyType;
    @InjectView(R.id.et_agencyAccount)
    EditText etAgencyAccount;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.et_amount)
    EditText etAmount;
    @InjectView(R.id.et_collectionAccount)
    EditText etCollectionAccount;
    @InjectView(R.id.et_payee)
    EditText etPayee;
    @InjectView(R.id.layout_payAnotherArea)
    LinearLayout layoutPayAnotherArea;
    @InjectView(R.id.bt_submit)
    Button btSubmit;
    @InjectView(R.id.bt_cancel)
    Button btCancel;
    @InjectView(R.id.ll_applyType)
    LinearLayout llApplyType;
    @InjectView(R.id.et_remarks)
    ContainsEmojiEditText etRemarks;
    @InjectView(R.id.tv_bank_content)
    TextView tvBankContent;  //银行卡
    @InjectView(R.id.ll_bank_list)
    LinearLayout llBankList; //银行卡列表
    @InjectView(R.id.ll_bankID)
    LinearLayout llBankID; //银行卡容器
    @InjectView(R.id.tv_agencyAccountPrompt)
    TextView tvAgencyAccountPrompt;
    @InjectView(R.id.tv_collectionAccountPrompt)
    TextView tvCollectionAccountPrompt;
    @InjectView(R.id.et_officeParent)
    EditText etOfficeParent;


    private String agencyAccount, name, amount, collectionAccount,
            payee, remarks;

    private MyPopWindow myPopWindow;
    private List<String> applyTypeList = new ArrayList<String>();
    private ListView lv_applyType;  //弹出框财务申请类型列表
    private String applyTypeString = "";
    private int xOffset;
    private int cow;
    private float xs;
    private float ys;
    private VolleyNetCommon volleyNetCommon;
    private MyPopWindow myPopWindowBank;
    private ListView lv_applyTypeBank;
    private String bankCardId = ""; //银行卡id
    private String preventRepeatToken;
    private String focusTag;
    private String applyType;
    private String agencyAccountStr;
    private String collectionAccountStr;
    private Intent intent;
    private String confirmDaiCun;//从其他地方传过来确定代存的标识
    private String latefeeReturnId;//从其他地方传过来确定代存的id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_launchapply);
        ButterKnife.inject(this);
        initWidget();
        initData();
    }

    private void initWidget() {
        etName.setEnabled(false);
        etName.setFocusable(false);
        etOfficeParent.setEnabled(false);
        etOfficeParent.setFocusable(false);
        etPayee.setEnabled(false);
        etPayee.setFocusable(false);
        etAgencyAccount.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                // 此处为失去焦点时的处理内容
                bankCardId = "";
                tvBankContent.setText("");
            }
        });

        etAgencyAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    //此处对得到焦点时的状态进行处理
                    agencyAccountStr = etAgencyAccount.getText().toString();
                    if (agencyAccountStr == null && agencyAccountStr.isEmpty()) {
                        tvAgencyAccountPrompt.setVisibility(View.GONE);
                    }
                } else {
                    //此处对失去焦点时的状态进行处理
                    agencyAccountStr = etAgencyAccount.getText().toString();
                    if (agencyAccountStr != null && !agencyAccountStr.isEmpty()) {
                        focusTag = "agencyAccountFocus";
                        getUserName(agencyAccountStr);
                        getOfficeParentNames(agencyAccountStr);
                    } else {
                        tvAgencyAccountPrompt.setVisibility(View.GONE);
                    }
                }
            }
        });

        etCollectionAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    //此处对得到焦点时的状态进行处理
                    collectionAccountStr = etCollectionAccount.getText().toString();
                    if (collectionAccountStr == null && collectionAccountStr.isEmpty()) {
                        tvCollectionAccountPrompt.setVisibility(View.GONE);
                    }
                } else {
                    //此处对失去焦点时的状态进行处理
                    collectionAccountStr = etCollectionAccount.getText().toString();
                    if (collectionAccountStr != null && !collectionAccountStr.isEmpty()) {
                        focusTag = "collectionAccountFocus";
                        getUserName(collectionAccountStr);
                    } else {
                        tvCollectionAccountPrompt.setVisibility(View.GONE);
                    }
                }
            }
        });


    }


    private void initData() {
        intent = getIntent();
        confirmDaiCun = intent.getStringExtra("confirmDaiCun");
        latefeeReturnId = intent.getStringExtra("latefeeReturnId");
        agencyAccount = intent.getStringExtra("agencyAccount");
        if (agencyAccount != null && !agencyAccount.isEmpty()) {
            etAgencyAccount.setText(agencyAccount);
            getOfficeParentNames(agencyAccount);

        }
        name = intent.getStringExtra("name");
        if (name != null) {
            etName.setText(name);
        }

        amount = intent.getStringExtra("amount");
        if (amount != null) {
            etAmount.setText(amount);
        }
        //为金额输入框制定规则
        InputFilter[] filters = {new CashierInputFilter()};
        etAmount.setFilters(filters);
//      applyTypeList.add("请选择");
        applyTypeList.add("代付");
        applyTypeList.add("代扣");
        applyTypeList.add("代存");
        applyTypeList.add("提现");
        applyType = applyTypeList.get(0);
        if (confirmDaiCun != null) {
            applyType = applyTypeList.get(2);
            tvApplyType.setText(applyType);
            layoutPayAnotherArea.setVisibility(View.GONE);
            applyTypeString = Define.finApplyType3;
            etRemarks.setText("滞纳金返还");
//            llBankID.setVisibility(View.GONE);
        } else {
            tvApplyType.setText(applyType);
            layoutPayAnotherArea.setVisibility(View.VISIBLE);
//            etRemarks.setEnabled(true);
//            llBankID.setVisibility(View.GONE);
        }
        etRemarks.setEnabled(true);
        llBankID.setVisibility(View.GONE);
    }

    private boolean submitFinancialApplyBoolean = true;

    @OnClick({R.id.iv_back, tv_applyType, bt_submit, R.id.bt_cancel, R.id.ll_bank_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case tv_applyType:
                if (RepeatClickUtils.repeatClick()) {
                    showApplyType();
                }
                break;
            case bt_submit:
//                long startTime = System.currentTimeMillis();   //获取开始时间
                Log.d("runtime", "runtimeStart = " + System.currentTimeMillis());

                Log.d("runtime", "runtimeEnd = " + System.currentTimeMillis());
                try {
                    if (RepeatClickUtils.repeatClick()) {
                        getToken();
//                        submitFinancialApply();
                    }

                  /*  if (submitFinancialApplyBoolean) {
                        submitFinancialApplyBoolean = false;
                        submitFinancialApply();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.ll_bank_list:
                if (RepeatClickUtils.repeatClick()) {
                    bankList(); //请求银行卡
                }
                break;
            case R.id.bt_cancel:
                finish();

                break;
        }
    }

    /**
     * 只有类型为提现状态下可以显示银行卡  并且 代理账户不能为空 不然点击银行卡列表不能加载银行卡列表
     */
    private void bankList() {
        //代理账户不能为空
        agencyAccount = removeAllSpace(etAgencyAccount.getText().toString());
        if (agencyAccount == null || agencyAccount.isEmpty()) {
            ToastUtils.showToast(LaunchApplyA.this, "请输入代理账户！");
            return;
        }

        try {
            String url = Define.URL + "acct/findCardByUser";//获取银行卡列表
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("loginName", agencyAccount);
            requestNet(url, jsonObject, "findCardByUser", "0", true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getUserName(String accountStr) {

        String url = Define.URL + "user/getUserName";//获取用户名
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginName", accountStr);
            requestNet(url, jsonObject, "getUserName", "0", true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取上级机构
     */
    private void getOfficeParentNames(String accountStr) {

        String url = Define.URL + "user/getOfficeParentNames";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginName", accountStr);
            requestNet(url, jsonObject, "getOfficeParentNames", "0", true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "findCardByUser":
                getBankListData(json);
                break;

            case "financialApplyPreventRepeatSubmit":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    preventRepeatToken = jsonObject.getString("preventRepeatToken");
                    if (preventRepeatToken != null) {
                        SPUtils2.putString(LaunchApplyA.this, "preventRepeatToken", preventRepeatToken);
                        submitFinancialApply();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "getUserName":
                try {
                    String message = json.optString("msg");
                    JSONObject jsonObject = json.optJSONObject("data");
                    if (jsonObject != null) {
                        String userName = jsonObject.optString("userName");

                        if (userName != null) {
                            if ("agencyAccountFocus".equals(focusTag)) {
                                tvAgencyAccountPrompt.setVisibility(View.GONE);
                                etName.setText(userName);
                            } else if ("collectionAccountFocus".equals(focusTag)) {
                                tvCollectionAccountPrompt.setVisibility(View.GONE);
                                etPayee.setText(userName);
                            }
                        }
                    } else {
                        if ("agencyAccountFocus".equals(focusTag)) {
                            tvAgencyAccountPrompt.setVisibility(View.VISIBLE);
                            tvAgencyAccountPrompt.setText(message);
                            etName.setText("");
                        } else if ("collectionAccountFocus".equals(focusTag)) {
                            tvCollectionAccountPrompt.setVisibility(View.VISIBLE);
                            tvCollectionAccountPrompt.setText(message);
                            etPayee.setText("");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "getOfficeParentNames":
                try {
                    String message = json.optString("msg");
                    JSONObject jsonObject = json.optJSONObject("data");
                    if (jsonObject != null) {
                        String officeParentNames = jsonObject.optString("officeParentNames");
                        if (officeParentNames != null) {
                            etOfficeParent.setText(officeParentNames);
                        }
                    } else {
                        ToastUtils.showToast(LaunchApplyA.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getBankListData(JSONObject json) {
        Gson gson = new Gson();
        FinancialBankListBean financialBankListBean = gson.fromJson(json.toString(), FinancialBankListBean.class);
        List<FinancialBankListBean.DataBean> data = financialBankListBean.getData();
        if (data != null && data.size() == 0) {
            ToastUtils.showToast(MyApplication.mContext, "暂无可用于提现的银行卡！");
            return;
        } else {
            showBankList(data);
        }

    }

    //弹出银行卡列表
    public void showBankList(final List<FinancialBankListBean.DataBean> data) {
        if (myPopWindowBank != null) {
            myPopWindowBank.dissmiss();
        }
        //重新测量
//                int w =  lv_applyType.getMeasuredWidth();
//                cow = Math.abs(w - xOffset);

        myPopWindowBank = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                lv_applyTypeBank = (ListView) view.findViewById(lv_audit_status);
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //重新测量
                //                int w =  lv_applyType.getMeasuredWidth();
                //                cow = Math.abs(w - xOffset);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                llBankList.getLocationOnScreen(arr);
                view.measure(0, 0);
                xs = arr[0] + llBankList.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + llBankList.getHeight();

                CommonAdapter<FinancialBankListBean.DataBean> commonAdapter = new CommonAdapter<FinancialBankListBean.DataBean>(LaunchApplyA.this, data, R.layout.admin_item_audit_status_pop2) {
                    @Override
                    public void convert(ViewHolder holder, FinancialBankListBean.DataBean o) {
                        holder.setText(R.id.tv_text, o.getCardName());
                        holder.setText(R.id.tv_cardID, o.getCardNo());
                    }
                };

                lv_applyTypeBank.setAdapter(commonAdapter);
                lv_applyTypeBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        FinancialBankListBean.DataBean dataBean = data.get(i);
                        bankCardId = dataBean.getId();
                        tvBankContent.setText(dataBean.getCardName());
                        if (dataBean.getCardName() == null || dataBean.getCardName().isEmpty()) {
                            bankCardId = "";
                            tvBankContent.setText("");
                        }
                        myPopWindowBank.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(this).create();
        myPopWindowBank.keyCodeDismiss(true); //返回键关闭
        myPopWindowBank.showAtLocation(llBankList, Gravity.NO_GRAVITY, (int) xs, (int) ys);

    }


  /*  private void getControlWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        xOffset = view.getMeasuredWidth();
    }*/

    /**
     * 展示申请类型下拉框
     */
    private void showApplyType() {
        if (myPopWindow != null) {
            myPopWindow.dissmiss();
        }
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                lv_applyType = (ListView) view.findViewById(lv_audit_status);
                view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //重新测量
//                int w =  lv_applyType.getMeasuredWidth();
//                cow = Math.abs(w - xOffset);
                //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
                int[] arr = new int[2];
                llApplyType.getLocationOnScreen(arr);
                view.measure(0, 0);
                xs = arr[0] + llApplyType.getWidth() - view.getMeasuredWidth();
                ys = arr[1] + llApplyType.getHeight();

                CommonAdapter commonAdapter = new CommonAdapter(LaunchApplyA.this, applyTypeList, R.layout.admin_item_audit_status_pop) {
                    @Override
                    public void convert(ViewHolder holder, Object o) {
                        holder.setText(R.id.tv_text, (String) o);

                    }
                };

                lv_applyType.setAdapter(commonAdapter);
                lv_applyType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //获取点击的文字
                        applyType = applyTypeList.get(i);
                        tvApplyType.setText(applyType);
                        etRemarks.setEnabled(true);
                        if ("代付".equals(applyType)) {
                            layoutPayAnotherArea.setVisibility(View.VISIBLE);
                            applyTypeString = Define.finApplyType4;    //审核状态
                            llBankID.setVisibility(View.GONE);
                        } else if ("代扣".equals(applyType)) {
                            layoutPayAnotherArea.setVisibility(View.GONE);
                            applyTypeString = Define.finApplyType5;
                            llBankID.setVisibility(View.GONE);
                        } else if ("代存".equals(applyType)) {
                            layoutPayAnotherArea.setVisibility(View.GONE);
                            applyTypeString = Define.finApplyType3;
                            llBankID.setVisibility(View.GONE);
                        } else if ("提现".equals(applyType)) {
                            layoutPayAnotherArea.setVisibility(View.GONE);
                            applyTypeString = Define.finApplyType6;
                            llBankID.setVisibility(View.VISIBLE);
                            etRemarks.setEnabled(false);
//                            etAmount.setText(100 + "");
                        }
                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.admin_audit_status_pop;
            }
        }.popupWindowBuilder(this).create();
        myPopWindow.keyCodeDismiss(true); //返回键关闭
        myPopWindow.showAtLocation(llApplyType, Gravity.NO_GRAVITY, (int) xs, (int) ys);
    }


    /**
     * 删除字符串空格
     *
     * @param str
     * @return
     */
    public String removeAllSpace(String str) {
        String tmpstr = str.replace(" ", "");
        return tmpstr;
    }

    /**
     * 提交财务申请
     *
     * @throws Exception
     */
    protected void submitFinancialApply() throws Exception {

        if (applyTypeString == null || applyTypeString.isEmpty()) {
            ToastUtils.showToast(LaunchApplyA.this, "请选择申请类型！");
            return;
        }

        agencyAccount = removeAllSpace(etAgencyAccount.getText().toString());
        if (agencyAccount == null || agencyAccount.isEmpty()) {
//            Toast.makeText(LaunchApplyA.this, "请输入代理账户!", Toast.LENGTH_SHORT).show();
            ToastUtils.showToast(LaunchApplyA.this, "请输入代理账户！");

            return;
        }

        name = removeAllSpace(etName.getText().toString());
        if (name == null || name.isEmpty()) {
//            Toast.makeText(LaunchApplyA.this, "请输入姓名!", Toast.LENGTH_SHORT).show();
            ToastUtils.showToast(LaunchApplyA.this, "请输入姓名！");
            return;
        }
        if (Define.finApplyType4.equals(applyTypeString)) {
            collectionAccount = removeAllSpace(etCollectionAccount.getText().toString());
            if (collectionAccount == null || collectionAccount.isEmpty()) {
//                Toast.makeText(LaunchApplyA.this, "请输入收款账户!", Toast.LENGTH_SHORT).show();
                ToastUtils.showToast(LaunchApplyA.this, "请输入收款账户！");

                return;
            }
            payee = removeAllSpace(etPayee.getText().toString());
            if (payee == null || payee.isEmpty()) {
//                Toast.makeText(LaunchApplyA.this, "请输入收款人!", Toast.LENGTH_SHORT).show();
                ToastUtils.showToast(LaunchApplyA.this, "请输入收款人！");
                return;
            }
        }

        if (applyTypeString.equals(Define.finApplyType6)) {
            if (bankCardId == null || bankCardId.isEmpty()) {
                ToastUtils.showToast(LaunchApplyA.this, "请选择银行卡！");
                return;
            }
        }


        remarks = removeAllSpace(etRemarks.getText().toString());
        if (remarks == null || remarks.isEmpty()) {
//            Toast.makeText(LaunchApplyA.this, "请输入备注!", Toast.LENGTH_SHORT).show();

            if (!(applyTypeString != null && applyTypeString.equals(Define.finApplyType6))) { //提现申请类型 不需要备注
                ToastUtils.showToast(LaunchApplyA.this, "请输入备注！");
                return;
            }

        }

        amount = etAmount.getText().toString();
        if (amount == null || amount.isEmpty()) {
//            Toast.makeText(LaunchApplyA.this, "请输入金额!", Toast.LENGTH_SHORT).show();
            ToastUtils.showToast(LaunchApplyA.this, "请输入金额！");
            return;
        }


        if (applyTypeString.equals(Define.finApplyType6)) { //提现选项
            Double money = Double.valueOf(amount);//上面做了判断不等于空
            if (money < 100) {
                ToastUtils.showToast(MyApplication.mContext, "提现金额不可小于100！");
                return;
            }
        } else {
            if ("0".equals(amount) || "0.0".equals(amount) || "0.00".equals(amount)) {
//            Toast.makeText(LaunchApplyA.this, "请输入大于0的金额", Toast.LENGTH_SHORT).show();
                ToastUtils.showToast(LaunchApplyA.this, "请输入大于0的金额！");
                return;
            }
        }


        cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("applyType", applyTypeString);
            jsonObject.put("applyFee", amount);
            jsonObject.put("agencyUserLoginName", agencyAccount);
            jsonObject.put("agencyUserName", name);
            jsonObject.put("preventRepeatToken", preventRepeatToken);  //防重复请求token

            if (applyTypeString.equals(Define.finApplyType6)) { //提现选项
                jsonObject.put("userCardId", bankCardId);
            } else {   //非提现选项有填写备注选项
                jsonObject.put("remark", remarks);
            }
            if (Define.finApplyType4.equals(applyTypeString)) {
                jsonObject.put("receiptLoginName", collectionAccount);
                jsonObject.put("receiptName", payee);
            }

            if (confirmDaiCun != null) {
                jsonObject.put("latefeeReturnId", latefeeReturnId);
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (volleyNetCommon == null) {
            volleyNetCommon = new VolleyNetCommon();
        }
        JsonObjectRequest abb = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "acct/financeApplySave", jsonObject, new VolleyAbstract(this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(LaunchApplyA.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Log.e("abb", json.toString());
                Toast.makeText(LaunchApplyA.this, "申请提交成功!", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            protected void PdDismiss() {
                cpd.dismiss();
            }

            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
        }, "submitFinancialApply", false);
        volleyNetCommon.addQueue(abb);
//        btSubmit.setEnabled(true);

    }


    /**
     * 获取Token
     */
    private void getToken() {
        String url = Define.URL + "fb/preventRepeatSubmit";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "financialApplyPreventRepeatSubmit", "0", true, false);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll("submitFinancialApply");

        }
    }
}

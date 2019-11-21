package com.huoniao.oc.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.UserInfo;
import com.huoniao.oc.util.CountDownTimerUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DataChangeApplyA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @InjectView(R.id.et_verCode)
    EditText etVerCode;
    @InjectView(R.id.bt_next)
    Button btNext;
    @InjectView(R.id.tv_operationContent)
    TextView tvOperationContent;
    @InjectView(R.id.tv_getVericode)
    TextView tvGetVericode;
    private User usetInfo, loginResult, user;
    private String operatorMobile;//负责人手机号
    private Intent intent;
    private String operationTag;//操作标识
    private CountDownTimerUtils mCountDownTimerUtils;
    private String verifyCode;
    private String newOperatorMobile;
    private String bankCardId;
    private String updateOperatorName;//作为从WriteDataChangeInfoA过来时修改了负责人姓名的标识


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_change_apply);
        ButterKnife.inject(this);
        initData();

    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        mCountDownTimerUtils = new CountDownTimerUtils(tvGetVericode, 60000, 1000);
//        mCountDownTimerUtils.start();
        usetInfo = (User) ObjectSaveUtil.readObject(DataChangeApplyA.this, "usetInfo");
//        loginResult = (User) ObjectSaveUtil.readObject(DataChangeApplyA.this, "loginResult");
//        if (usetInfo != null) {
        operatorMobile = usetInfo.getOperatorMobile();//默认获取个人信息里的最新负责人号码
//        }else {
//            operatorMobile = loginResult.getOperatorMobile();//如果没有就获取登录时的负责人号码
//        }
        if (operatorMobile != null) {
            tvPhoneNumber.setText(operatorMobile);
        }

        intent = getIntent();
        operationTag = intent.getStringExtra("operationTag");
        bankCardId = intent.getStringExtra("bankCardId");
        updateOperatorName = intent.getStringExtra("updateOperatorName");
        if ("logout".equals(operationTag)) {
            tvTitle.setText("账户注销申请");
            tvOperationContent.setText("您正在申请账户注销");
            getVerifyCode("19");
        } else if ("updateMobile".equals(operationTag)) {
            tvOperationContent.setText("您修改了负责人的手机号码");
            newOperatorMobile = intent.getStringExtra("newOperatorMobile");
            if (newOperatorMobile != null) {
                tvPhoneNumber.setText(newOperatorMobile);
            }

            getVerifyCode("21");
        } else if ("cancelLogout".equals(operationTag)) {
            tvTitle.setText("取消账户注销");
            tvOperationContent.setText("您正在取消账户注销");
            getVerifyCode("23");
        } else if ("cancelDataChange".equals(operationTag)) {
            tvTitle.setText("取消资料变更");
            tvOperationContent.setText("您正在取消资料变更");
            getVerifyCode("22");
        } else {
            getVerifyCode("20");
        }

        user = (User) ObjectSaveUtil.readObject(DataChangeApplyA.this, "dataChangeImg");

    }

    @OnClick({R.id.iv_back, R.id.tv_getVericode, R.id.bt_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_getVericode:
                if ("logout".equals(operationTag)) {
                    getVerifyCode("19");
                } else if ("updateMobile".equals(operationTag)) {
                    getVerifyCode("21");
                } else if ("cancelDataChange".equals(operationTag)) {
                    getVerifyCode("22");
                } else if ("cancelLogout".equals(operationTag)) {
                    getVerifyCode("23");
                } else {
                    getVerifyCode("20");
                }
                break;
            case R.id.bt_next:
                if ("logout".equals(operationTag)) {

                    validateVerifyCode("19");//logout标识进来表示是通过注销操作进来的

                } else if ("updateMobile".equals(operationTag)) {
                    validateVerifyCode("21");//验证修改后的负责人手机号
                } else if ("cancelDataChange".equals(operationTag)) {
                    validateVerifyCode("22");//取消资料变更验证
                } else if ("cancelLogout".equals(operationTag)) {
                    validateVerifyCode("23");//取消账户注销验证
                } else {
                    validateVerifyCode("20");//否则默认是资料变更操作

                }

                break;
        }
    }


    /**
     * 获取短信验证码
     */
    private void getVerifyCode(String type) {

        String url = Define.URL + "user/getVerifyCode";
        JSONObject jsonObject = new JSONObject();

        try {
            if ("updateMobile".equals(operationTag)) {
                jsonObject.put("mobile", newOperatorMobile);
            } else {
                jsonObject.put("mobile", operatorMobile);
            }
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestNet(url, jsonObject, "getVerifyCode", "", true, true);
    }


    /**
     * 验证短信验证码
     */
    private void validateVerifyCode(String type) {
        verifyCode = etVerCode.getText().toString().trim();
        if (verifyCode == null || verifyCode.isEmpty()) {
            ToastUtils.showToast(DataChangeApplyA.this, "请输入验证码！");
            return;
        }
        String url = Define.URL + "user/validateVerifyCode";
        JSONObject jsonObject = new JSONObject();

        try {
            if ("updateMobile".equals(operationTag)) {
                jsonObject.put("mobile", newOperatorMobile);
            } else {
                jsonObject.put("mobile", operatorMobile);
            }
            jsonObject.put("type", type);
            jsonObject.put("verifyCode", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestNet(url, jsonObject, "validateVerifyCode", "", true, true);
    }


    /**
     * 提交变更后的数据(修改负责人手机后的提交)
     */
    private void mobileChangeSubmit() {
        String url = Define.URL + "user/dataChangeComfirm";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", usetInfo.getId());
            jsonObject.put("officeName", user.getOrgName());
            jsonObject.put("corpName", user.getCorpName()); //法人姓名
            jsonObject.put("corpMobile", user.getCorpMobile()); //法人手机
            jsonObject.put("corpIdNum", user.getCorpIdNum()); //法人身份证
            jsonObject.put("operatorName", user.getOperatorName());//负责人姓名
            jsonObject.put("operatorIdNum", user.getOperatorIdNum());//负责人手身份证号
            jsonObject.put("operatorMobile", user.getOperatorMobile());// 负责人手机号
            jsonObject.put("address", user.getAddress());//地址

            jsonObject.put("corpLicenceImg", user.getCorp_licence());//营业执照
            jsonObject.put("corpCardForntImg", user.getCorp_card_fornt());//法人身份证正面路径
            jsonObject.put("corpCardRearImg", user.getCorp_card_rear());//法人身份证反面路径
            jsonObject.put("staContIndexImg", user.getStaContIndexSrc());//车站合同首页路径
            jsonObject.put("staContLastImg", user.getStaContLastSrc());//车站合同末页路径
            jsonObject.put("operatorCardforntImg", user.getOperatorCardforntSrc());//负责人身份证正面路径
            jsonObject.put("operatorCardrearImg", user.getOperatorCardrearSrc());//负责人身份证反面路径
            jsonObject.put("fareAuthorizationImg", user.getFareAuthorizationSrc());//票款汇缴授权书
            //jsonObject.put("staDepositImg", ivStationBarBase);//车站押金条路径
//          jsonObject.put("staDepInspImg", ivBusinessLicenseBase);//车站押金年检证书路径

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "mobileChangeSubmit", "", true, true);
    }


    /**
     * 用户取消资料变更申请或账户注销申请
     */
    private void cancelUserChange() {
        String url = Define.URL + "user/cancelUserChange";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "cancelUserChange", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "getVerifyCode":
                mCountDownTimerUtils.start();
                break;
            case "validateVerifyCode":
                if ("logout".equals(operationTag)) {
                    intent = new Intent(DataChangeApplyA.this, AccountLogOffPromptA.class);
                    intent.putExtra("operationTag", operationTag);
                    intent.putExtra("bankCardId", bankCardId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                } else if ("updateMobile".equals(operationTag)) {
                    if (updateOperatorName != null){
                        showDialog("因您修改了负责人姓名，审核通过后将删除原负责人名下所有银行卡，确认提交？");
                    }else {
                        mobileChangeSubmit();
                    }
                } else if ("cancelDataChange".equals(operationTag)) {
                    cancelUserChange();
                } else if ("cancelLogout".equals(operationTag)) {
                    cancelUserChange();
                } else {
                    try {
                        UserInfo userInfo = new UserInfo();
                        userInfo.getUserInfo(DataChangeApplyA.this, cpd, WriteDataChangeInfoA.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "mobileChangeSubmit":
                intent = new Intent(DataChangeApplyA.this, AccountLogOffSuccessA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;

            case "cancelUserChange":

                JSONObject jsonObject = null;
                try {
                    jsonObject = json.getJSONObject("data");
                    String result = jsonObject.getString("result");

                    if ("success".equals(result)) {
                        if ("cancelLogout".equals(operationTag)) {
                            ToastUtils.showToast(DataChangeApplyA.this, "取消账户注销申请成功！");
                        } else if ("cancelDataChange".equals(operationTag)) {
                            ToastUtils.showToast(DataChangeApplyA.this, "取消资料变更申请成功！");
                        }
                    } else {
                        String message = jsonObject.optString("msg");
                        ToastUtils.showToast(DataChangeApplyA.this, message);
                        return;

                    }

                    MyApplication.getInstence().activityFinish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    /**
     * 确认和拒绝操作提示弹窗
     *
     * @param msg 提示信息
     */
    private void showDialog(String msg) {
        new AlertDialog.Builder(DataChangeApplyA.this)
                .setTitle("提示！")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //根据不同请求弹出不同提示的弹窗

                        mobileChangeSubmit(); //开始修改个人信息


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setCancelable(false)
                .show();

    }


}

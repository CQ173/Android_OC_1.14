package com.huoniao.oc.trainstation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.LateFeeDeailsBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class LateFeeSubmitDetailsA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_outletsAccount)
    TextView tvOutletsAccount;
    @InjectView(R.id.tv_returnAmount)
    TextView tvReturnAmount;
    @InjectView(R.id.tv_returnReason)
    TextView tvReturnReason;
    @InjectView(R.id.tv_lookImage)
    TextView tvLookImage;
    @InjectView(R.id.tv_noPassReason)
    TextView tvNoPassReason;
    @InjectView(R.id.tv_pass)
    TextView tvPass;
    @InjectView(R.id.tv_noPass)
    TextView tvNoPass;
    @InjectView(R.id.ll_auditingButton)
    LinearLayout llAuditingButton;
    @InjectView(R.id.ll_refuseResonArea)
    LinearLayout llRefuseResonArea;
    @InjectView(R.id.tv_handlerState)
    TextView tvHandlerState;
    @InjectView(R.id.tv_currentRole)
    TextView tvCurrentRole;
    @InjectView(R.id.processRecordListView)
    MyListView processRecordListView;
    @InjectView(R.id.ll_currentRole)
    LinearLayout llCurrentRole;
    private Intent intent;

    private String dates, outletsAccount, returnAmount, returnReson,
            imageUrl, refuseReson, state, id;
    private String roleName;
    private User user;
    private String refuseReason;
    private String handlerState;
    private String currentRole;
    private boolean isCanHandle;
    private List<LateFeeDeailsBean.DataBean.ProcessRecordListBean> processRecordList;
    private CommonAdapter<LateFeeDeailsBean.DataBean.ProcessRecordListBean> commonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_late_fee_submit_details);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        processRecordList = new ArrayList<>();
        intent = getIntent();
//        state = intent.getStringExtra("state");
        id = intent.getStringExtra("id");
        latefeeReturnView();
//        dates = intent.getStringExtra("date");
//        outletsAccount = intent.getStringExtra("outletsAccount");
//        returnAmount = intent.getStringExtra("returnAmount");
//        returnReson = intent.getStringExtra("returnReson");
//        imageUrl = intent.getStringExtra("imageUrl");
//        refuseReson = intent.getStringExtra("refuseReson");


    }

    @OnClick({R.id.iv_back, R.id.tv_lookImage, R.id.tv_pass, R.id.tv_noPass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_lookImage:
                if (RepeatClickUtils.repeatClick()) {
                    enlargeImage(LateFeeSubmitDetailsA.this, imageUrl, ivBack);
                }
                break;

            case R.id.tv_pass:
                if (RepeatClickUtils.repeatClick()) {
                    auditingLatefee("2");
                }
                break;

            case R.id.tv_noPass:
                if (RepeatClickUtils.repeatClick()) {
                    refuseReasonDialog();
                }
                break;
        }
    }

    private void refuseReasonDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_audit_latefee, null);
        final EditText et_refuseReason = (EditText) view.findViewById(R.id.et_refuseReason);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("处理不通过！");
        builder.setView(view);
        final AlertDialog dialog = builder.create();//获取dialog
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refuseReason = et_refuseReason.getText().toString().trim();
                if (refuseReason == null || refuseReason.isEmpty()) {
                    ToastUtils.showToast(LateFeeSubmitDetailsA.this, "请输入拒绝理由");
                    return;
                }

                auditingLatefee("3");
                dialog.dismiss();

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void auditingLatefee(String handler) {

        String url = Define.URL + "fb/latefeeReturnHandle";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("state", handler);
            if ("3".equals(handler)) {
                jsonObject.put("refuseReason", refuseReason);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "latefeeReturnHandle", "0", true, false); //0 不代表什么
    }

    private void latefeeReturnView() {

        String url = Define.URL + "fb/latefeeReturnView";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "latefeeReturnView", "0", true, false); //0 不代表什么
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "latefeeReturnHandle":
                ToastUtils.showToast(LateFeeSubmitDetailsA.this, "处理成功");
                setResult(10);
                finish();
//                intent = new Intent(LateFeeSubmitDetailsA.this, ReturnLateFeeA.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                MyApplication.getInstence().activityFinish();
                break;

            case "latefeeReturnView":
                Gson lateFeeDeailsGson = new Gson();
                LateFeeDeailsBean lateFeeDeailsBean = lateFeeDeailsGson.fromJson(json.toString(), LateFeeDeailsBean.class);
                LateFeeDeailsBean.DataBean dataBean = lateFeeDeailsBean.getData();
                state = dataBean.getState();
                dates = dataBean.getDaysString();
                outletsAccount = dataBean.getAgencyCode();
                returnAmount = dataBean.getFee();
                returnReson = dataBean.getApplyReason();
                imageUrl = dataBean.getInstructionSrc();
                refuseReson = dataBean.getRefuseReason();
                handlerState = dataBean.getStateName();
                currentRole = dataBean.getCurrentRole();
                isCanHandle = dataBean.isIsCanHandle();
                processRecordList = dataBean.getProcessRecordList();

                if (currentRole == null || currentRole.isEmpty()){
                    llCurrentRole.setVisibility(View.GONE);
                }

                user = (User) readObject(LateFeeSubmitDetailsA.this, "loginResult");
                roleName = user.getRoleName();  //获取角色名

                if (Define.NUMBER_THREE.equals(state)) {
                    llRefuseResonArea.setVisibility(View.VISIBLE);
                } else {
                    llRefuseResonArea.setVisibility(View.GONE);
                }

                if (Define.NUMBER_ONE.equals(state) && isCanHandle == true) {
//                    llAuditingButton.setVisibility(View.VISIBLE);
                    setPremissionShowHideView(Premission.FB_LATEFEERETURN_HANDLE, llAuditingButton); //添加递交权限
                }

                if (dates != null) {
                    tvTime.setText(dates);
                }
                if (outletsAccount != null) {
                    tvOutletsAccount.setText(outletsAccount);
                }
                if (returnAmount != null) {
                    tvReturnAmount.setText(returnAmount);
                }

                if (returnReson != null) {
                    tvReturnReason.setText(returnReson);
                }
                if (refuseReson != null) {
                    tvNoPassReason.setText(refuseReson);
                }

                if (handlerState != null) {
                    tvHandlerState.setText(handlerState);
                }

                if (currentRole != null) {
                    tvCurrentRole.setText(currentRole);
                }
                setProcessRecordAdapter();
                break;
        }
    }

    private void setProcessRecordAdapter() {
        commonAdapter = new CommonAdapter<LateFeeDeailsBean.DataBean.ProcessRecordListBean>(LateFeeSubmitDetailsA.this, processRecordList, R.layout.item_processrecordlist) {
            @Override
            public void convert(ViewHolder holder, LateFeeDeailsBean.DataBean.ProcessRecordListBean processRecordListBean) {
                holder.setText(R.id.tv_auditUserName, processRecordListBean.getAuditUserName())
                        .setText(R.id.tv_auditStateName, processRecordListBean.getAuditStateName())
                        .setText(R.id.tv_createDate, processRecordListBean.getCreateDate());
            }
        };

        processRecordListView.setAdapter(commonAdapter);

    }

}

package com.huoniao.oc.trainstation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.NewPayWarningBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.PerfectInformationA;
import com.huoniao.oc.user.RegisterSuccessA;
import com.huoniao.oc.util.ContainsEmojiEditText;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class NewWarningDetailsA extends BaseActivity {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_winNumber)
    TextView tvWinNumber;
    @InjectView(R.id.tv_ownership_institution)
    TextView tvOwnershipInstitution;
    @InjectView(R.id.tv_warningNumber)
    TextView tvWarningNumber;
    @InjectView(R.id.tv_warningType)
    TextView tvWarningType;
    @InjectView(R.id.tv_warningContent)
    TextView tvWarningContent;
    @InjectView(R.id.tv_state)
    TextView tvState;
    @InjectView(R.id.tv_handlerState)
    TextView tvHandlerState;
    @InjectView(R.id.et_handlerResult)
    ContainsEmojiEditText etHandlerResult;
    @InjectView(R.id.tv_handlerResult)
    TextView tvHandlerResult;
    @InjectView(R.id.tv_handlerPeple)
    TextView tvHandlerPeple;
    @InjectView(R.id.tv_handlerDate)
    TextView tvHandlerDate;
    @InjectView(R.id.ll_finishContent)
    LinearLayout llFinishContent;
    @InjectView(R.id.tv_confirm)
    TextView tvConfirm;
    @InjectView(R.id.ll_auditingButton)
    LinearLayout llAuditingButton;
    @InjectView(R.id.ll_resultContentArea)
    LinearLayout llResultContentArea;
    @InjectView(R.id.ll_line)
    LinearLayout llLine;
    private NewPayWarningBean.DataBean dataBean;
    private String id = "";
    private String handlerResult = "";
    private String handlerState;
    private String roleName;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_warning_details);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        user = (User) readObject(NewWarningDetailsA.this, "loginResult");
        roleName = user.getRoleName();  //获取角色名
        dataBean = (NewPayWarningBean.DataBean) ObjectSaveUtil.readObject(this, "alarmList");
        if (dataBean != null) {
            handlerState = dataBean.getHandleState();
            tvDate.setText(dataBean.getCreateDateString());
            tvWinNumber.setText(dataBean.getWinNumber());
            tvOwnershipInstitution.setText(dataBean.getParentName());
            tvWarningNumber.setText(dataBean.getAlarmNumber());
            tvWarningType.setText(dataBean.getTypeName());
            tvWarningContent.setText(dataBean.getContent());
            tvState.setText(dataBean.getStateName());
            tvHandlerState.setText(dataBean.getHandleStateName());

            id = dataBean.getId();

            if ("1".equals(handlerState)){
                tvHandlerResult.setText(dataBean.getHandleResult());
                tvHandlerPeple.setText(dataBean.getHandleUserName());
                tvHandlerDate.setText(dataBean.getHandleDateString());
            }

            if (Define.TRAINSTATION.equals(LoginA.IDENTITY_TAG) || Define.TRAINSTATION.equals(PerfectInformationA.IDENTITY_TAG)
                    || Define.TRAINSTATION.equals(WXEntryActivity.IDENTITY_TAG) || Define.TRAINSTATION.equals(RegisterSuccessA.IDENTITY_TAG)
                    || "铁路总局".equals(roleName) || "铁路分局".equals(roleName)) {

                if ("0".equals(handlerState)){
//                    setPremissionShowHideView(Premission.FB_ALARM_VIEW, llResultContentArea); //汇缴预警 处理内容权限
                    llResultContentArea.setVisibility(View.VISIBLE);
                    llAuditingButton.setVisibility(View.VISIBLE);
                    llLine.setVisibility(View.VISIBLE);
                    llFinishContent.setVisibility(View.GONE);
                }else {
                    llResultContentArea.setVisibility(View.GONE);
                    llAuditingButton.setVisibility(View.GONE);
                    llLine.setVisibility(View.GONE);
                    llFinishContent.setVisibility(View.VISIBLE);
                }
            }else {
                if ("0".equals(handlerState)){
//                    setPremissionShowHideView(Premission.FB_ALARM_VIEW, llResultContentArea); //汇缴预警 处理内容权限
                    llResultContentArea.setVisibility(View.GONE);
                    llAuditingButton.setVisibility(View.GONE);
                    llLine.setVisibility(View.GONE);
                    llFinishContent.setVisibility(View.GONE);
                }else {
                    llResultContentArea.setVisibility(View.GONE);
                    llAuditingButton.setVisibility(View.GONE);
                    llLine.setVisibility(View.GONE);
                    llFinishContent.setVisibility(View.VISIBLE);
                }
            }

        }


    }


    @OnClick({R.id.iv_back, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                handlerResult = etHandlerResult.getText().toString().trim();
                if (handlerResult == null || handlerResult.isEmpty()) {
                    ToastUtils.showToast(NewWarningDetailsA.this, "请输入处理结果！");
                    return;
                }

                requestAlarmHandle();
                break;

        }
    }


    private void requestAlarmHandle() {
        String url = Define.URL + "fb/alarmHandle";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("handleResult", handlerResult);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "alarmHandle", "0", true, false); //0 不代表什么
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "alarmHandle":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String result = jsonObject.optString("result");
                    if ("success".equals(result)) {
                        setResult(60);
                        finish();
                    } else {
                        String message = jsonObject.optString("msg");
                        ToastUtils.showToast(NewWarningDetailsA.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


}

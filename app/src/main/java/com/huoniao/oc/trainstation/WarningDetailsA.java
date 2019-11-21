package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.AdminConsolidatedRecord;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.PerfectInformationA;
import com.huoniao.oc.user.RegisterSuccessA;
import com.huoniao.oc.util.ContainsEmojiEditText;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.wxapi.WXEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_warningStation;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

/**
 * Created by Administrator on 2017/11/10.
 */

public class WarningDetailsA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.textView16)
    TextView textView16;
    @InjectView(R.id.tv_warningTime)
    TextView tvWarningTime;
    @InjectView(tv_warningStation)
    TextView tvWarningStation;
    @InjectView(R.id.tv_warningWinNumber)
    TextView tvWarningWinNumber;
    @InjectView(R.id.tv_withholdAccount)
    TextView tvWithholdAccount;
    @InjectView(R.id.tv_payMoney)
    TextView tvPayMoney;
    @InjectView(R.id.tv_ticketNumber)
    TextView tvTicketNumber;
    @InjectView(R.id.tv_averageFare)
    TextView tvAverageFare;
    @InjectView(R.id.tv_averagePayMoney)
    TextView tvAveragePayMoney;
    @InjectView(R.id.tv_warningPrompt)
    TextView tvWarningPrompt;
    @InjectView(R.id.tv_handlePerson)
    TextView tvHandlePerson;
    @InjectView(R.id.tv_handleTime)
    TextView tvHandleTime;
    @InjectView(R.id.tv_handleResult)
    TextView tvHandleResult;
    @InjectView(R.id.exciptionExplain)
    ContainsEmojiEditText exciptionExplain;
    @InjectView(R.id.layout_noRelieveArea)
    LinearLayout layoutNoRelieveArea;
    @InjectView(R.id.bt_confirmNormal)
    Button btConfirmNormal;
    @InjectView(R.id.bt_confirmExciption)
    Button btConfirmExciption;
    @InjectView(R.id.ll_operationButton)
    LinearLayout llOperationButton;
    @InjectView(R.id.ll_handlerArea)
    LinearLayout llHandlerArea;
    private String createDateString, stationName, winNumber, loginName, paymentAmount,
            averagePrice, averageAmount, handleName, updateDate, stateName,
            instructions, state, conditions, id;
    private int ticketCount;
    private Intent intent;
    private String operationTag;
//    private String excption_instructions;
    private MyPopWindow myPopWindow;
    private String roleName;//角色名
    private User user;
    private String warningWinNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warningdetails);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        user = (User) readObject(WarningDetailsA.this, "loginResult");
        roleName = user.getRoleName();
        intent = getIntent();
        state = intent.getStringExtra("state");
        if (Define.WARN_WAITDO.equals(state)){
            llHandlerArea.setVisibility(View.GONE);

            if (Define.TRAINSTATION.equals(LoginA.IDENTITY_TAG) || Define.TRAINSTATION.equals(PerfectInformationA.IDENTITY_TAG)
                    || Define.TRAINSTATION.equals(WXEntryActivity.IDENTITY_TAG) || Define.TRAINSTATION.equals(RegisterSuccessA.IDENTITY_TAG)
                    || "铁路总局".equals(roleName) || "铁路分局".equals(roleName)) {
                llOperationButton.setVisibility(View.VISIBLE);
                exciptionExplain.setEnabled(true);
//                exciptionExplain.setFocusable(true);
//                exciptionExplain.setInputType(InputType.TYPE_CLASS_TEXT);

            }else {
                llOperationButton.setVisibility(View.GONE);
                exciptionExplain.setEnabled(false);
//                exciptionExplain.setFocusable(false);
//                exciptionExplain.setInputType(InputType.TYPE_NULL);
            }
        }else {
            llHandlerArea.setVisibility(View.VISIBLE);
            llOperationButton.setVisibility(View.GONE);
            exciptionExplain.setEnabled(false);
            exciptionExplain.setHint("");
//            exciptionExplain.setFocusable(false);
//            exciptionExplain.setInputType(InputType.TYPE_NULL);
        }
        id = intent.getStringExtra("id");
        createDateString = intent.getStringExtra("createDateString");
        if (createDateString != null) {
            tvWarningTime.setText(createDateString);
        }

        stationName = intent.getStringExtra("stationName");
        if (stationName != null) {
            tvWarningStation.setText(stationName);
        }
        winNumber = intent.getStringExtra("winNumber");
        if (winNumber != null) {
            tvWarningWinNumber.setText(winNumber);
        }

        loginName = intent.getStringExtra("loginName");
        if (loginName != null) {
            tvWithholdAccount.setText(loginName);
        }

        paymentAmount = intent.getStringExtra("paymentAmount");
        if (paymentAmount != null) {
            tvPayMoney.setText(paymentAmount + "元");
        }
        ticketCount = intent.getIntExtra("ticketCount", 0);
        tvTicketNumber.setText(ticketCount + "张");

        averagePrice = intent.getStringExtra("averagePrice");
        if (averagePrice != null) {
            tvAverageFare.setText(averagePrice + "元");
        }

        averageAmount = intent.getStringExtra("averageAmount");
        if (averageAmount != null) {
            tvAveragePayMoney.setText(averageAmount + "元");
        }

        conditions = intent.getStringExtra("conditions");
        if (conditions != null) {
            tvWarningPrompt.setText("预警提示：" + conditions);
        }

        handleName = intent.getStringExtra("handleName");
        if (handleName != null) {
            tvHandlePerson.setText(handleName);
        }

        updateDate = intent.getStringExtra("updateDate");
        if (updateDate != null) {
            tvHandleTime.setText(updateDate);
        }

        stateName = intent.getStringExtra("stateName");
        if (stateName != null) {
            tvHandleResult.setText(stateName);
        }

        instructions = intent.getStringExtra("instructions");
        if (instructions != null) {
            exciptionExplain.setText(instructions);
        }else {
            exciptionExplain.setText("");
        }
    }

    @OnClick({R.id.iv_back, R.id.bt_confirmNormal, R.id.bt_confirmExciption, R.id.tv_warningWinNumber, R.id.tv_withholdAccount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_confirmNormal:
                operationTag = "confirmNormal";
                instructions = exciptionExplain.getText().toString();
                if (instructions == null || instructions.isEmpty()) {
                    instructions = "";
                }
                showOperationHintsPop(operationTag);
                break;
            case R.id.bt_confirmExciption:
                operationTag = "confirmExciption";
                instructions = exciptionExplain.getText().toString();
                if (instructions == null || instructions.isEmpty()) {
                    ToastUtils.showToast(WarningDetailsA.this, "请输入异常情况说明！");
                    return;
                }

                showOperationHintsPop(operationTag);
                break;

            case R.id.tv_warningWinNumber:
                warningWinNumber = tvWarningWinNumber.getText().toString();
                intent = new Intent(WarningDetailsA.this, AdminConsolidatedRecord.class);
                intent.putExtra("winNumber", warningWinNumber);
                startActivity(intent);
//                MyApplication.getInstence().activityFinish();
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.tv_withholdAccount:
                intent = new Intent(WarningDetailsA.this, WithholdAccountInfoA.class);
                intent.putExtra("withholdAccount", loginName);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                break;
        }
    }

    /**
     *  请求汇缴预警处理
     *
     */
    private void requestpaymentWarnHandle(String proState){
        String url = Define.URL+"fb/paymentWarnHandle";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("state", proState);
            jsonObject.put("instructions", instructions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestNet(url, jsonObject, "paymentWarnHandle", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "paymentWarnHandle":
                MyApplication.getInstence().activityFinish();
                ToastUtils.showToast(WarningDetailsA.this, "处理成功！");
                intent = new Intent(WarningDetailsA.this, WarningInfoListA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }

    private void showOperationHintsPop(final String tag){
        myPopWindow = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                ImageView iv_promptImage = (ImageView) view.findViewById(R.id.iv_promptImage);
                TextView tv_promptContent = (TextView) view.findViewById(R.id.tv_promptContent);
                LinearLayout ll_cancel = (LinearLayout) view.findViewById(R.id.ll_cancel);
                LinearLayout ll_confirm = (LinearLayout) view.findViewById(R.id.ll_confirm);
                if ("confirmNormal".equals(tag)){
                    iv_promptImage.setImageResource(R.drawable.pos_success);
                    tv_promptContent.setText("是否确认该条预警信息正常？");
                }else {
                    iv_promptImage.setImageResource(R.drawable.pos_er);
                    tv_promptContent.setText("是否确认该条预警信息异常？");
                }

                ll_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();
                        if ("confirmNormal".equals(tag)){
                            requestpaymentWarnHandle(Define.WARN_NORMOL);
                        }else {
                            requestpaymentWarnHandle(Define.WARN_EXCIPTION);
                        }


                    }
                });
                ll_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myPopWindow.dissmiss();
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.warningoperation_pop_pop;
            }
        }.popWindowTouch(WarningDetailsA.this).showAtLocation(ivBack, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}

package com.huoniao.oc.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.UserInfo;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AccountLogOffNeedKnow extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
//    @InjectView(R.id.tv_accountAndWinNumber)
//    TextView tvAccountAndWinNumber;
    @InjectView(R.id.tv_choiceBankCard)
    TextView tvChoiceBankCard;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    @InjectView(R.id.bt_cancel)
    Button btCancel;
    @InjectView(R.id.tv_account)
    TextView tvAccount;
    @InjectView(R.id.tv_WinNumber)
    TextView tvWinNumber;

    private User loginResult;
    private Intent intent;
    private String userName, winNumber;
    private String bankCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log_off_need_know);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        bankCardId = intent.getStringExtra("bankCardId");
        loginResult = (User) ObjectSaveUtil.readObject(AccountLogOffNeedKnow.this, "loginResult");
        userName = loginResult.getLoginName();
        winNumber = loginResult.getWinNumber();
        tvAccount.setText(userName);
        tvWinNumber.setText(winNumber);

       /* String accountAndWinNumber = "您正在申请注销O计账户" + userName + "，您当前的汇缴窗口号为" + winNumber + "。";
        SpannableString spannableString = new SpannableString(accountAndWinNumber);
        //设置字符串中的一段文本的颜色
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#4d90e7")), 11, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAccountAndWinNumber.setText(spannableString);*/

    }

    @OnClick({R.id.iv_back, R.id.tv_choiceBankCard, R.id.bt_confirm, R.id.bt_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choiceBankCard:
                intent = new Intent(AccountLogOffNeedKnow.this, ChoiceBankCardA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.bt_confirm:
                requestCancellationCheck();
                break;
            case R.id.bt_cancel:
                MyApplication.getInstence().activityFinish();
                break;
        }
    }


    /**
     * 验证该账户是否可注销
     */
    private void requestCancellationCheck() {
        String url = Define.URL + "user/cancellationCheck";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "cancellationCheck", "", true, true);
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "cancellationCheck":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");

                    String result = jsonObject.getString("result");
                    if ("success".equals(result)) {
                        intent = new Intent();
                        intent.putExtra("operationTag", "logout");
                        intent.putExtra("bankCardId", bankCardId);
                        try {
                            UserInfo userInfo = new UserInfo();
                            userInfo.getUserInfo(AccountLogOffNeedKnow.this, cpd, DataChangeApplyA.class, intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        startActivity(intent);
//                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    } else {
                        String message = jsonObject.optString("msg");
                        intent = new Intent(AccountLogOffNeedKnow.this, AccountLogOffFailA.class);
                        intent.putExtra("operationTag", "logout");
                        intent.putExtra("message", message);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}

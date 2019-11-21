package com.huoniao.oc.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AccountLogOffPromptA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.tv_accountAndWinNumber)
    TextView tvAccountAndWinNumber;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    @InjectView(R.id.bt_cancel)
    Button btCancel;
    private Intent intent;
    private String operationTag;//操作标识
    private String userCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log_off_prompt);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        operationTag = intent.getStringExtra("operationTag");
//        userCardId = SPUtils2.getString(AccountLogOffPromptA.this, "userCardId");
        userCardId = intent.getStringExtra("bankCardId");
    }

    @OnClick({R.id.iv_back, R.id.bt_confirm, R.id.bt_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_confirm:
                cancellationComfirm();

                break;
            case R.id.bt_cancel:
                MyApplication.getInstence().activityFinish();
                break;
        }
    }

    /**
     * 确定注销
     */
    private void cancellationComfirm() {
        String url = Define.URL + "user/cancellationComfirm";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userCardId", userCardId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestNet(url, jsonObject, "cancellationComfirm", "", true, true);
    }


    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "cancellationComfirm":

                JSONObject jsonObject = null;
                try {
                    jsonObject = json.getJSONObject("data");
                    String result = jsonObject.getString("result");
                    if ("success".equals(result)){
                        intent = new Intent(AccountLogOffPromptA.this, AccountLogOffSuccessA.class);
                        intent.putExtra("operationTag", operationTag);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }else {
                        String message = json.optString("msg");
                        ToastUtils.showToast(AccountLogOffPromptA.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
        }
    }

}

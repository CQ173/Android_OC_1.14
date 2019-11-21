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
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AccountLogOffFailA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.tv_applyState)
    TextView tvApplyState;
    @InjectView(R.id.tv_promptContent)
    TextView tvPromptContent;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    @InjectView(R.id.mListView)
    MyListView mListView;

    private Intent intent;
    private String message;
    private List<String> msgList;
    private CommonAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log_off_fail);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        msgList = new ArrayList<>();
        intent = getIntent();
        message = intent.getStringExtra("message");
        if (message != null && !message.isEmpty()) {
            if (message.contains(";")) {
                String[] arr = message.split(";");
                msgList = Arrays.asList(arr);//数组转集合
            } else {
                msgList.add(message);
            }
        }
        if (msgList.size() > 0) {
            adapter = new CommonAdapter<String>(this, msgList, R.layout.item_failmsg_list) {
                @Override
                public void convert(ViewHolder holder, String s) {
                    holder.setText(R.id.tv_msgContent, s);

                }
            };

            mListView.setAdapter(adapter);
        }

    }

    @OnClick({R.id.iv_back, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_confirm:
                MyApplication.getInstence().activityFinish();
                break;
        }
    }
}

package com.huoniao.oc.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.huoniao.oc.bean.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class AccountLogOffAuditingA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.tv_submitState)
    TextView tvSubmitState;
    @InjectView(R.id.tv_promptContent)
    TextView tvPromptContent;
    @InjectView(R.id.bt_cancelApply)
    Button btCancelApply;
    private Intent intent;
    private String operationTag;
    private String message;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log_off_auditing);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        user = (User) readObject(AccountLogOffAuditingA.this, "loginResult");
       /* if (user.getParentId() != null && !user.getParentId().isEmpty()) {//getParentId 父账号ID
            btCancelApply.setVisibility(View.GONE);
        }*/
        intent = getIntent();
        operationTag = intent.getStringExtra("operationTag");
        if ("logout".equals(operationTag)){
            tvTitle.setText("账户注销申请");
            tvPromptContent.setText("系统正在审核您的账户注销申请");
        }

    }

    @OnClick({R.id.iv_back, R.id.bt_cancelApply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_cancelApply:
                if ("logout".equals(operationTag)){
                    message = "确定取消账户注销申请？";
                    operationTag = "cancelLogout";
                }else {
                    message = "确定取消资料变更申请？";
                    operationTag = "cancelDataChange";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountLogOffAuditingA.this);
                builder.setTitle("提示！")
                        .setMessage(message)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                intent = new Intent(AccountLogOffAuditingA.this, DataChangeApplyA.class);
                                intent.putExtra("operationTag", operationTag);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                                cancelUserChange();

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.create().show();

                break;
        }
    }


}

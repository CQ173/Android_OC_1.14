package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.StationWindowManageBean;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.Premission;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_updata;

/**
 * Created by Administrator on 2017/5/12.
 */

public class WindowsAnchoredDetailsA extends BaseActivity {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_relieveAnchored)
    TextView tvRelieveAnchored;
    @InjectView(R.id.tv_outlet_name)
    TextView tvOutletName;
    @InjectView(R.id.tv_outlet_windowNumber)
    TextView tvOutletWindowNumber;
    @InjectView(R.id.tv_outlet_number)
    TextView tvOutletNumber;
    @InjectView(R.id.tv_outlet_city)
    TextView tvOutletCity;
    @InjectView(R.id.tv_outlet_corpName)
    TextView tvOutletCorpName;
    @InjectView(R.id.tv_outlet_corpMobile)
    TextView tvOutletCorpMobile;
    @InjectView(R.id.tv_outlet_idNumber)
    TextView tvOutletIdNumber;
    @InjectView(R.id.tv_outlet_fuZeRen)
    TextView tvOutletFuZeRen;
    @InjectView(R.id.tv_outlet_fuZeRenPhone)
    TextView tvOutletFuZeRenPhone;
    @InjectView(R.id.tv_outlet_fuZeRenIdNum)
    TextView tvOutletFuZeRenIdNum;
    @InjectView(R.id.tv_waitRelationWinNumber)
    TextView tvWaitRelationWinNumber;
    @InjectView(R.id.textView3)
    TextView textView3;
    @InjectView(R.id.tv_auditState)
    TextView tvAuditState;
    @InjectView(tv_updata)
    TextView tvUpdata;
    @InjectView(R.id.layout_update)
    LinearLayout layoutUpdate;
    @InjectView(R.id.textView4)
    TextView textView4;
    @InjectView(R.id.layout_seeDetails)
    LinearLayout layoutSeeDetails;
    @InjectView(R.id.view_shuXian)
    View viewShuXian;

    private StationWindowManageBean staWinManage;
    private String auditState = "";
    private Intent intent;
    private String clickTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windownumber_manager);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        staWinManage = (StationWindowManageBean) ObjectSaveUtil.readObject(WindowsAnchoredDetailsA.this, "windowAnchoredInfo");
        tvOutletName.setText(staWinManage.getOfficeName());
        tvOutletWindowNumber.setText(staWinManage.getOfficeWinNumber());
        tvOutletNumber.setText(staWinManage.getOfficeCode());
        tvOutletCity.setText(staWinManage.getOfficeAreaName());
        tvOutletCorpName.setText(staWinManage.getOfficeCorpName());
        tvOutletCorpMobile.setText(staWinManage.getOfficeCorpMobile());
        tvOutletIdNumber.setText(staWinManage.getOfficeCorpIdNum());
        tvOutletFuZeRen.setText(staWinManage.getOfficeOperatorName());
        tvOutletFuZeRenPhone.setText(staWinManage.getOfficeOperatorMoblie());
        tvOutletFuZeRenIdNum.setText(staWinManage.getOfficeOperatorIdNum());
        tvWaitRelationWinNumber.setText(staWinManage.getWinNumber());
        auditState = staWinManage.getAuditState();
        if (Define.AUDIT_STATE_PASS.equals(auditState)) {
            tvAuditState.setText("审核通过");
           // tvRelieveAnchored.setVisibility(View.VISIBLE);
            setPremissionShowHideView(Premission.FB_AGENCYCONNECT_SAVE,tvRelieveAnchored); //标题右边 解除挂靠权限
            layoutUpdate.setVisibility(View.GONE);
            viewShuXian.setVisibility(View.GONE);
        } else if (Define.AUDIT_STATE_WAIT.equals(auditState)) {
            tvAuditState.setText("待审核");
            tvRelieveAnchored.setVisibility(View.GONE);
        } else if (Define.AUDIT_STATE_REFUSE.equals(auditState)) {
            tvAuditState.setText("审核不通过");
            tvRelieveAnchored.setVisibility(View.GONE);
        } else if (Define.ANCHORED_STATE_REMOVE.equals(auditState)) {
            tvAuditState.setText("解除挂靠");
            tvRelieveAnchored.setVisibility(View.GONE);
            tvUpdata.setText("重新申请");
        }

        setPremissionShowHideView(Premission.FB_AGENCYCONNECT_SAVE,layoutUpdate); //隐藏重新 申请挂靠和修改挂靠


    }

    @OnClick({R.id.iv_back, R.id.tv_relieveAnchored, R.id.layout_update, R.id.layout_seeDetails})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_relieveAnchored:
                clickTag = "relieveAnchored";
                intent = new Intent(WindowsAnchoredDetailsA.this, SonOutletsDetailsA.class);
                intent.putExtra("clickTag", clickTag);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.layout_update:
                clickTag = "updataInfo";
                intent = new Intent(WindowsAnchoredDetailsA.this, WindowsAnchoredApplyA.class);
                intent.putExtra("clickTag", clickTag);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.layout_seeDetails:
                clickTag = "seeDetails";
                intent = new Intent(WindowsAnchoredDetailsA.this, SonOutletsDetailsA.class);
                intent.putExtra("clickTag", clickTag);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }
}

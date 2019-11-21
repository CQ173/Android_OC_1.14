package com.huoniao.oc.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.AdminConsolidateRecordBean;
import com.huoniao.oc.bean.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class AdminConsolidateRecordDetails extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;  //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.tv_consignee_name)
    TextView tvConsigneeName;  // 代售点名称
    @InjectView(R.id.tv_amount_paid)
    TextView tvAmountPaid;  // 汇缴金额
    @InjectView(R.id.tv_payment_status)
    TextView tvPaymentStatus;   //汇缴状态
    @InjectView(R.id.tv_debit_account)
    TextView tvDebitAccount; //扣款账号
    @InjectView(R.id.tv_subscription_window_number)
    TextView tvSubscriptionWindowNumber; //汇缴窗口号
    @InjectView(R.id.tv_the_ticket_date)
    TextView tvTheTicketDate;  //票款日期
    @InjectView(R.id.tv_subscription_date)
    TextView tvSubscriptionDate; //汇缴日期
    @InjectView(R.id.tv_general_office_name)
    TextView tvGeneralOfficeName; //总局名称
    @InjectView(R.id.tv_branch_name)
    TextView tvBranchName; //分局名称
    @InjectView(R.id.tv_station_name)
    TextView tvStationName; //车站名称
    @InjectView(R.id.tv_station_account_number)
    TextView tvStationAccountNumber; //车站账号
    @InjectView(R.id.ll_consolidated_record)
    LinearLayout llConsolidatedRecord;  //  看查看改账号汇缴记录
    @InjectView(R.id.ll_transaction_record)
    LinearLayout llTransactionRecord;//看查看改账号交易记录
    @InjectView(R.id.activity_transaction_details)
    LinearLayout activityTransactionDetails;
    @InjectView(R.id.tv_trainDepotName)
    TextView tvTrainDepotName;
    private String officeCode; //扣款账号
    private String winNumber;
    private String roleName;//角色名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_consolidate_record_details);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        User user = (User) readObject(AdminConsolidateRecordDetails.this, "loginResult");
        roleName = user.getRoleName();

        if (roleName.contains("系统管理员") || roleName.contains("出纳") || roleName.contains("会计")) {
            llTransactionRecord.setVisibility(View.VISIBLE);

        }
        tvTitle.setText("汇缴详情");
        AdminConsolidateRecordBean.DataBean dataBean = (AdminConsolidateRecordBean.DataBean) getIntent().getSerializableExtra("recordDetails");
        tvConsigneeName.setText(dataBean.getAgentName() == null ? "" : dataBean.getAgentName());
        tvAmountPaid.setText(dataBean.getShouldAmountString() == null ? "" : dataBean.getShouldAmountString());    //票款金额
        getPaymentStatusName(dataBean.getWithholdStatus() == null ? "0" : dataBean.getWithholdStatus(), tvPaymentStatus);

        tvPaymentStatus.setText(dataBean.getWithholdStatusName());    //汇缴状态
        officeCode = dataBean.getOfficeCode() == null ? "" : dataBean.getOfficeCode();
        tvDebitAccount.setText(officeCode);  //扣款账号
        winNumber = dataBean.getWinNumber() == null ? "" : dataBean.getWinNumber();
        tvSubscriptionWindowNumber.setText(winNumber); //汇缴窗口
        tvTheTicketDate.setText(dataBean.getDate()==null ?"":dataBean.getDate()); //票款日期
        tvSubscriptionDate.setText(dataBean.getCreateDate()==null?"":dataBean.getCreateDate()); //汇缴日期
        tvGeneralOfficeName.setText(dataBean.getTrunkName()==null?"":dataBean.getTrunkName()); //总局名称
        tvBranchName.setText(dataBean.getBranchName()==null?"":dataBean.getBranchName());//分局名称
        tvStationName.setText(dataBean.getRailwayStationName()==null ?"":dataBean.getRailwayStationName()); //车站名称
        tvStationAccountNumber.setText(dataBean.getRailwayStationId() ==null ?"":dataBean.getRailwayStationId()); //车站账号

    }

    @OnClick({R.id.iv_back, R.id.ll_consolidated_record, R.id.ll_transaction_record, R.id.tv_debit_account})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_consolidated_record: //  看查看该窗口汇缴记录

                intent = new Intent(AdminConsolidateRecordDetails.this, AdminConsolidatedRecord.class);
//                 intent.putExtra("winNumber",winNumber);
                intent.putExtra("loginName", officeCode);
                startActivityIntent(intent);
                break;
            case R.id.ll_transaction_record:  //查看该账户交易记录
                intent = new Intent(AdminConsolidateRecordDetails.this, AdminOJiTransactionDetails.class);
                intent.putExtra("loginName", officeCode);
                startActivityIntent(intent);
                break;
            case R.id.tv_debit_account:  //扣款账好  点击查看用户资料      //需要改成id
                if (roleName.contains("系统管理员") || roleName.contains("出纳") || roleName.contains("会计")) {
                    intent = new Intent(AdminConsolidateRecordDetails.this, AdminUserDetails.class);
                    intent.putExtra("loginName", officeCode);  // 扣款账号
                    startActivityIntent(intent);

                }

                break;
        }
    }

    /**
     * 根据状态获取名称
     * @param state
     * @param tv_recharge
     * @return
     */

    /**
     * 根据状态获取名称
     *
     * @param state
     * @param tv_recharge
     * @return
     */
    public void getPaymentStatusName(String state, TextView tv_recharge) {
        switch (state) {
            case "2":
                tv_recharge.setTextColor(getResources().getColor(R.color.colorAccent));
            default:
                tv_recharge.setTextColor(getResources().getColor(R.color.grenns));
                break;
        }
    }
}

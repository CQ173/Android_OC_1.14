package com.huoniao.oc.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.EposSync;
import com.huoniao.oc.bean.TransactionDetailsBean;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_end_date;

public class TransactionDetails extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;  //返回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.tv_add_card)
    TextView tvAddCard;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.tv_account)
    TextView tvAccount; //交易账户
    @InjectView(R.id.tv_name)
    TextView tvName;  //用户姓名
    @InjectView(tv_end_date)
    TextView tvEndDate; //到账时间日期
    @InjectView(R.id.tv_money)
    TextView tvMoney;  //账户余额
    @InjectView(R.id.tv_date)
    TextView tvDate;  //交易时间
    @InjectView(R.id.tv_serial_number)
    TextView tvSerialNumber; //流水号
    @InjectView(R.id.tv_transaction_name)
    TextView tvTransactionName;//交易名称
    @InjectView(R.id.tv_transaction_account)
    TextView tvTransactionAccount;//交易账号
    @InjectView(R.id.tv_transaction_number)
    TextView tvTransactionNumber;//交易号
    @InjectView(R.id.tv_transaction_type)
    TextView tvTransactionType;//交易类型
    @InjectView(R.id.ll_consolidated_record)
    LinearLayout llConsolidatedRecord; // 查看该账户汇缴记录
    @InjectView(R.id.ll_transaction_record)
    LinearLayout llTransactionRecord; //查看该账户交易记录
    @InjectView(R.id.activity_transaction_details)
    LinearLayout activityTransactionDetails;//
    @InjectView(R.id.tv_tradeFeeString)
    TextView tv_tradeFeeString; //交易金额
    @InjectView(R.id.tv_tradeStatus)
    TextView tv_tradeStatus; //交易状态
    @InjectView(R.id.tv_update_button)
    TextView tvUpdateButton;
    @InjectView(R.id.tv_remark)
    TextView tvRemark;        //备注
    @InjectView(R.id.tv_tradeChannelName)
    TextView tv_tradeChannelName;   //交易渠道
    @InjectView(R.id.tv_sourceFlowId)
    TextView tv_sourceFlowId;   //关联流水号

    private String loginName;
   private String flowId;
    private EposSync.DataBean eposDataSync;
    private TransactionDetailsBean.DataBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        ButterKnife.inject(this);
        initData();
    }


    private void initData() {
        dataBean = (TransactionDetailsBean.DataBean) getIntent().getSerializableExtra("transactionDetails");
        TransactionDetailsBean.DataBean.TradeUserBean tradeUser = dataBean.getTradeUser();
        tvTitle.setText("交易详情");
        if (tradeUser != null) {
            loginName = tradeUser.getLoginName();
            tvAccount.setText(tradeUser.getLoginName()); //交易账户
            tvName.setText(tradeUser.getName()); //用户姓名
        }

            tvEndDate.setText(dataBean.getUpdateDate() == null ? "" : dataBean.getUpdateDate());//到期时间


             if(dataBean.getChannelType()!=null && dataBean.getChannelType().equals(Define.EPOSE)&& !(dataBean.getTradeStatus().equals("TRADE_SUCCESS"))){    //只有epose才有更新按钮

                 setPremissionShowHideView(Premission.ACCT_TRADEFLOW_EPOSSYNC,tvUpdateButton); //是否有 同步 权限
             }else{
                 tvUpdateButton.setVisibility(View.GONE);
             }
        tvRemark.setText(dataBean.getRemark()==null ?"":dataBean.getRemark()); //备注
        tv_tradeChannelName.setText(dataBean.getTradeChannelName());    //交易渠道
        tv_sourceFlowId.setText(dataBean.getSourceFlowId());    //关联流水号
        flowId = dataBean.getFlowId();
        tv_tradeFeeString.setText(dataBean.getTradeFeeString());
        tv_tradeStatus.setText(dataBean.getTradeStatus());
        if (Define.ACCT_TRADE_STATUS_WAIT.equals(dataBean.getTradeStatus())) {
            tv_tradeStatus.setText("等待付款");
            long etimes = 0; //表示只有在规定时间内才可以 完成显示退款功能
            try {
                String formentHaoMiao = dataBean.getExpireTime();
                if (formentHaoMiao != null) {
                    etimes = DateUtils.dataFormentHaoMiao(dataBean.getExpireTime());
                    if (DateUtils.getSystemDataHaoMiao() > etimes) {  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
                        tv_tradeStatus.setText("交易关闭");

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Define.ACCT_TRADE_STATUS_SUSPENSE.equals(dataBean.getTradeStatus())) {

            tv_tradeStatus.setText("等待处理");
        } else if (Define.ACCT_TRADE_STATUS_FAIL.equals(dataBean.getTradeStatus())) {

            tv_tradeStatus.setText("交易失败");
        } else if (Define.ACCT_TRADE_STATUS_CLOSED.equals(dataBean.getTradeStatus())) {

            tv_tradeStatus.setText("交易关闭");
        } else if (Define.ACCT_TRADE_STATUS_PENDING.equals(dataBean.getTradeStatus())) {

            tv_tradeStatus.setText("等待收款");

        } else if (Define.ACCT_TRADE_STATUS_SUCCESS.equals(dataBean.getTradeStatus())) { //表示支付成功的

            tv_tradeStatus.setText("交易成功");
        } else if (Define.ACCT_TRADE_STATUS_FINISHED.equals(dataBean.getTradeStatus())) {

            tv_tradeStatus.setText("交易结束");
        } else if (Define.ACCT_TRADE_STATUS_REFUNDING.equals(dataBean.getTradeStatus())) {

                     /*   tv_tradeStatus.setText("交易成功"); //退款中
                        setRefundState(View.VISIBLE, false, R.color.yellow, "退款中", R.color.white);*/
            tv_tradeStatus.setText("退款中");

        } else if (Define.ACCT_TRADE_STATUS_REFUND.equals(dataBean.getTradeStatus())) {
                      /*  tv_tradeStatus.setText("交易成功");  // 本来状态改为  已退款  现在把已退款放到按钮上
                        setRefundState(View.VISIBLE, false, R.color.grayss, "已退款", R.color.white);*/
            tv_tradeStatus.setText("已退款");
        } else if (Define.ACC_TRADE_STATUS_REFUND_FAIL.equals(dataBean.getTradeStatus())) {
                     /*   tv_tradeStatus.setText("交易失败");
                        setRefundState(View.VISIBLE, false, R.color.colorAccent, "退款失败", R.color.white);*/
            tv_tradeStatus.setText("退款失败");
        }

        tvMoney.setText(dataBean.getCurBalanceString()); //账户余额
        tvDate.setText(dataBean.getTradeDate()); //交易时间
        tvSerialNumber.setText(dataBean.getFlowId()); //流水号
        tvTransactionName.setText(dataBean.getTradeName()); //交易名称
        tvTransactionAccount.setText(dataBean.getTradeAcct() == null ? "" : dataBean.getTradeAcct()); //交易账号
        tvTransactionNumber.setText(dataBean.getTradeNo() == null ? "" : dataBean.getTradeNo()); //交易号  都是可能没有的
        switch (dataBean.getType()) {
            case "income":
                tvTransactionType.setText("收入");//交易类型
                break;
            case "payout":
                tvTransactionType.setText("支出");//交易类型
                break;
        }


    }

    @OnClick({R.id.iv_back, R.id.ll_consolidated_record, R.id.ll_transaction_record,R.id.tv_update_button})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                reSultEpos();
                finish();
                break;
            case R.id.ll_consolidated_record:  //跳转汇缴记录
                intent = new Intent(TransactionDetails.this, AdminConsolidatedRecord.class);
                intent.putExtra("loginName", loginName);
                startActivityIntent(intent);
                break;
            case R.id.ll_transaction_record:
                intent = new Intent(TransactionDetails.this, AdminOJiTransactionDetails.class);
                intent.putExtra("loginName", loginName);
                startActivityIntent(intent);
                finish();
                break;
            case R.id.tv_update_button:
                //同步按钮
                synchronization();
                break;
        }
    }

    //同步更新
    private void synchronization() {
        try {
            String url = Define.URL+"acct/eposSync";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("flowId",flowId);
            requestNet(url,jsonObject,"eposSync","0",true, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "eposSync":
                updateUi(json);
                break;

        }
    }

    /**
     * 同步后更新ui
     */
    private void updateUi(JSONObject json) {
        Gson gson = new Gson();
        EposSync eposSync = gson.fromJson(json.toString(), EposSync.class);
        eposDataSync = eposSync.getData();
        tvTransactionAccount.setText(eposDataSync.getAccount()==null?"": eposDataSync.getAccount()); //交易账号
        tvTransactionNumber.setText(eposDataSync.getTradeNo()==null?"": eposDataSync.getTradeNo()); //交易号
        tvMoney.setText(eposDataSync.getBalance()+""); //账户余额
        String status = eposDataSync.getStatus()==null?"": eposDataSync.getStatus();
        tv_tradeStatus.setText(status); //交易状态
        tvEndDate.setText(eposDataSync.getUpdateDate()==null?"": eposDataSync.getUpdateDate()); //更新时间
        if("交易成功".equals(status)){
            tvUpdateButton.setVisibility(View.GONE);
        }
    }


    @Override
    protected boolean onkeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            reSultEpos();
        }
        return super.onkeyDown(keyCode, event);
    }

    /**
     * 回到上个页面的数据
     */
    private void reSultEpos() {
        boolean b = premissionNo(Premission.ACCT_TRADEFLOW_EPOSSYNC);
        if(b){
            Intent intent = new Intent();
            intent.putExtra("eposDataSync",eposDataSync);
            setResult(1,intent);
        }
    }


    //权限判断返回键需不需要返回 结果给上一个界面
    public   boolean premissionNo(String premissionFlag){
        if(dataBean.getChannelType()!=null && dataBean.getChannelType().equals(Define.EPOSE)&& !(dataBean.getTradeStatus().equals("TRADE_SUCCESS"))) {    //只有epose才有更新按钮
            for (String premissions: premissionsList ) {
                if(premissionFlag.equals(premissions)){
                    return true;
                }
            }
         }
        return false;
    }


}

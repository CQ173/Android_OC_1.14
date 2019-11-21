package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.BankCardBean;
import com.huoniao.oc.common.BankIcon;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.bt_cancel;
import static com.huoniao.oc.R.id.ll_operationButton;
import static com.huoniao.oc.R.id.tv_bindNewCard;

/**
 * Created by Administrator on 2017/10/12.
 */

public class OpWithHoldTwoA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_signPrompt)
    TextView tvSignPrompt;
    @InjectView(R.id.tv_bindNewCard)
    TextView tvBindNewCard;
    @InjectView(R.id.ll_signBankCardInfo)
    LinearLayout llSignBankCardInfo;
    @InjectView(R.id.signBankCardListView)
    MyListView signBankCardListView;
    @InjectView(R.id.bt_previousStep)
    Button btPreviousStep;
    @InjectView(R.id.bt_nextStep)
    Button btNextStep;
    @InjectView(ll_operationButton)
    LinearLayout llOperationButton;
    @InjectView(bt_cancel)
    Button btCancel;
    @InjectView(R.id.bt_bindBankCard)
    Button btBindBankCard;
    @InjectView(R.id.ll_noCardView)
    LinearLayout llNoCardView;
    @InjectView(R.id.scr_haveCard)
    ScrollView scrHaveCard;
    @InjectView(R.id.tv_noCardInfo)
    TextView tvNoCardInfo;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    private Intent intent;
    private String cardState;//签约状态，根据这个进行不同状态的页面展示以及逻辑
    private List<BankCardBean> mList = new ArrayList<>();
    private CommonAdapter adapter;
    private String cardId = "";//银行卡id
    private String cardName = "";//银行卡名称
    private String cardNumber = "";//银行卡卡号
    private String cardType = "";//银行卡类型
    private String bankCode = "";//所属银行

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openwithhold2);
        ButterKnife.inject(this);
        rlTitle.setFocusable(true);
        rlTitle.setFocusableInTouchMode(true);
        rlTitle.requestFocus();
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        try {
            cardState = intent.getStringExtra("cardState");
            if (cardState == null) {
                List<BankCardBean> m = SPUtils2.getList(OpWithHoldTwoA.this, "unCardList");
                if (m != null) {
                    mList.addAll(m);
                    if (mList.size() > 0) {
                        mList.get(0).setCheckState(true);
                    }
//                    else {
//                        scrHaveCard.setVisibility(View.GONE);
//                        llNoCardView.setVisibility(View.VISIBLE);
//                    }
                }
            } else {
                List<BankCardBean> m2 = SPUtils2.getList(OpWithHoldTwoA.this, "closeCardList");
                mList.addAll(m2);
                if (mList.size() > 0) {
                    mList.get(0).setCheckState(true);
                } else {
                    tvNoCardInfo.setVisibility(View.VISIBLE);
                }
                tvTitle.setText("汇缴代扣管理");
                tvSignPrompt.setText("您已关闭火车票款汇缴代扣业务，如需再次开通,请点击重新开通");
                tvBindNewCard.setText("重新开通");
                llSignBankCardInfo.setVisibility(View.VISIBLE);
                llOperationButton.setVisibility(View.GONE);

            }


            adapter = new CommonAdapter<BankCardBean>(OpWithHoldTwoA.this, mList, R.layout.item_bankcardsign_list) {

                @Override
                public void convert(ViewHolder holder, BankCardBean bankCardBean) {
                    holder.setText(R.id.tv_bankName, bankCardBean.getCardName());
                    String cardNumber = bankCardBean.getCardnumber();
                    if (cardNumber != null && !cardNumber.isEmpty() && cardNumber.length() > 4) {
                        String newCardNumber = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
                        TextView tv_cardNumber = holder.getView(R.id.tv_cardNumber);
                        tv_cardNumber.setText(newCardNumber);
                    }
                    ImageView iv_bankLogo = holder.getView(R.id.iv_bankLog);
                    String bankCode = bankCardBean.getBankCode();
                    iv_bankLogo.setImageResource(BankIcon.getBankIcon(bankCode));

                    RadioButton rb_bankType = holder.getView(R.id.rb_bankType);
                    if (cardState == null) {
                        if (bankCardBean.isCheckState()) {
                            rb_bankType.setChecked(true);
                        } else {
                            rb_bankType.setChecked(false);
                        }
                    } else {
                        rb_bankType.setVisibility(View.GONE);
                        iv_bankLogo.setImageResource(BankIcon.getBankGrayIcon(bankCode));

                    }

                    TextView tv_everyLimit = holder.getView(R.id.tv_everyLimit);
                    String everyLimit = bankCardBean.getEveryLimit();
                    if (everyLimit != null && !everyLimit.isEmpty()) {
                        tv_everyLimit.setText("￥" + bankCardBean.getEveryLimit());
                    } else {
                        tv_everyLimit.setText("不限额");
                    }

                    TextView tv_dailyLimit = holder.getView(R.id.tv_dailyLimit);
                    String dailyLimit = bankCardBean.getDailyLimit();
                    if (dailyLimit != null && !dailyLimit.isEmpty()) {
                        tv_dailyLimit.setText("￥" + bankCardBean.getDailyLimit());
                    } else {
                        tv_dailyLimit.setText("不限额");
                    }
                }
            };
            signBankCardListView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        signBankCardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (cardState == null && mList.size() > 0) {
                    for (int j = 0; j < mList.size(); j++) {
                        BankCardBean bankCardBean = mList.get(j);
                        if (i == j) {
                            bankCardBean.setCheckState(true);

                        } else {
                            bankCardBean.setCheckState(false);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "signedComfirmRequest":
                dataAnalysis(json);
                break;
        }
    }

    /**
     * 数据解析
     *
     * @param object 数据源
     */
    private void dataAnalysis(JSONObject object) {
        try {
            JSONObject jsonObject = object.getJSONObject("data");
            String result = jsonObject.getString("result");
            if ("success".equals(result)) {
                intent = new Intent(OpWithHoldTwoA.this, OpWithHoldFiveA.class);
//                intent.putExtra("signState", signState);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            } else {
                String msg = jsonObject.optString("msg");
                ToastUtils.showLongToast(OpWithHoldTwoA.this, msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 银行卡类型
     *
     * @return 银行种类  1 没有中信  2 只有中信  3 都有
     */
    private String cardType() {
        String type = "";
        boolean isCNCB = false;//是否有中信银行卡
        boolean isRestsBank = false;//是否有别的银行卡

        for (BankCardBean bean : mList) {
            if (Define.CNCB.equals(bean.getBankCode())) {
                isCNCB = true;
            } else {
                isRestsBank = true;
            }
        }

        if (isCNCB && isRestsBank) {//都有
            type = "3";
        } else if (!isCNCB && isRestsBank) {//没有中信
            type = "1";
        } else if (isCNCB && !isRestsBank) {//只有中信
            type = "2";
        }
        return type;
    }

    @OnClick({R.id.iv_back, tv_bindNewCard, R.id.bt_previousStep, R.id.bt_nextStep, R.id.bt_cancel, R.id.bt_bindBankCard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_bindNewCard:
                if (cardState == null) {
                    intent = new Intent(OpWithHoldTwoA.this, TemporaryAddCardA.class);//绑定新卡
                    intent.putExtra("operation", "withHoldCardBind");
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                } else {//重新开通
                    intent = new Intent(OpWithHoldTwoA.this, OpWithHoldOneA.class);
                    intent.putExtra("cardState", cardState);
                    intent.putExtra("cardType", cardType());//银行种类  1 没有中信  2 只有中信  3 都有
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                }
                break;
            case R.id.bt_previousStep:
                finish();
                break;
            case R.id.bt_nextStep:
                if (mList.size() == 0){
                    ToastUtils.showToast(this , "您的账户内暂无可签约的储蓄卡，请绑定新卡");
                }else {
                    for (int i = 0; i < mList.size(); i++) {
                        BankCardBean bankCardBean = mList.get(i);
                        if (bankCardBean.isCheckState() == true) {
                            cardId = bankCardBean.getId();
                            cardName = bankCardBean.getCardName();
                            cardNumber = bankCardBean.getCardnumber();
                            cardType = bankCardBean.getCardType();
                            bankCode = bankCardBean.getBankCode();
                        }
                    }
//                if (Define.CNCB.equals(bankCode)) {//中信银行
//                    requestSignedComfirm(cardId);
//                } else {//其他银行
                    intent = new Intent(OpWithHoldTwoA.this, OpWithHoldOneA.class);
                    intent.putExtra("cardId", cardId);
                    intent.putExtra("cardName", cardName);
                    intent.putExtra("cardNumber", cardNumber);
                    intent.putExtra("cardType", cardType);
                    intent.putExtra("bankCode", bankCode);
                    intent.putExtra("OpTwoA" , "OpTwoA");
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                }
                }

                break;
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_bindBankCard:
                intent = new Intent(OpWithHoldTwoA.this, TemporaryAddCardA.class);//绑定新卡
                intent.putExtra("operation", "withHoldCardBind");
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }


}


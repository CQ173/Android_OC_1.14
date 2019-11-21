package com.huoniao.oc.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.CashListBean;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChoiceBankCardA extends BaseActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.rl_title)
    RelativeLayout rlTitle;
    @InjectView(R.id.lv_bankCard)
    MyListView lvBankCard;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    private Intent intent;

    private String cardId = "";
    List<CashListBean.DataEntity> jsonObjectSelectCashList ; //展示银行卡 信息结果
    private CommonAdapter<CashListBean.DataEntity> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_bank_card);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        manageBankCard();
    }

    @OnClick({R.id.iv_back, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_confirm:
                for (int i = 0; i < jsonObjectSelectCashList.size(); i++) {
                    CashListBean.DataEntity dataEntity = jsonObjectSelectCashList.get(i);
                    if (dataEntity.isCheckState() == true) {
                        cardId = dataEntity.getId();
                    }
                }
//                SPUtils2.putString(ChoiceBankCardA.this, "userCardId", cardId);//将所选的银行卡id保存起来
                intent = new Intent(ChoiceBankCardA.this, AccountLogOffNeedKnow.class);
                intent.putExtra("bankCardId", cardId);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }


    /**
     * 可提现银行卡列表
     */
    private void manageBankCard() {
        String url = Define.CRAD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operateType",5);   // 5表示 查询提现银行卡列表
            jsonObject.put("bankCode","");
            jsonObject.put("cardtype",""); //
            jsonObject.put("cardno","");
            jsonObject.put("custname","");
            jsonObject.put("id","");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestNet(url, jsonObject, "manageBankCard", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "manageBankCard":
                Gson gson = new Gson();
                CashListBean cashBean = gson.fromJson(json.toString(), CashListBean.class);
                jsonObjectSelectCashList = cashBean.getData();
                if (jsonObjectSelectCashList.size() > 0) {
                    jsonObjectSelectCashList.get(0).setCheckState(true);
                }
                setAdapter();
                break;
        }
    }

    private void setAdapter() {
        adapter = new CommonAdapter<CashListBean.DataEntity>(this, jsonObjectSelectCashList, R.layout.item_bankcard_list){
            @Override
            public void convert(ViewHolder holder, CashListBean.DataEntity dataEntity) {
                holder.setText(R.id.tv_bankName, dataEntity.getCardName());
                TextView tv_cardType = holder.getView(R.id.tv_cardType);
                if("1".equals(dataEntity.getCardType())) {
                    tv_cardType.setText("借记卡");
                }else {
                    tv_cardType.setText("信用卡");
                }
                TextView tv_lastNumber = holder.getView(R.id.tv_lastNumber);
                String cardNumber = dataEntity.getCardNo();
                if (cardNumber != null && !cardNumber.isEmpty() && cardNumber.length() > 4) {
                    String newCardNumber = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
                    tv_lastNumber.setText("(" + newCardNumber + ")");
                }

                RadioButton radioButton = holder.getView(R.id.radioButton);
                if (dataEntity.isCheckState()) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }

            }
        };
        lvBankCard.setAdapter(adapter);
        lvBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < jsonObjectSelectCashList.size(); j++) {
                    CashListBean.DataEntity dataEntity = jsonObjectSelectCashList.get(j);
                    if (i == j) {
                        dataEntity.setCheckState(true);
                    } else {
                        dataEntity.setCheckState(false);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
    }
}

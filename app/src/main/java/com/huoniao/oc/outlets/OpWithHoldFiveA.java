package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.BankCardBean;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SPUtils2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.tv_signStateInfo;

/**
 * Created by Administrator on 2017/10/12.
 */

public class OpWithHoldFiveA extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(tv_signStateInfo)
    TextView tvSignStateInfo;
    @InjectView(R.id.bt_confirm)
    Button btConfirm;
    private List<BankCardBean> bankCardList = new ArrayList<BankCardBean>();
    private String id;
    private String bankName;
    private String isPublic;
    private String cardNo;
    private String bankCode;
    private String cardType;
    private String custName;
    private Intent intent;
    private String signState;//用来判定是从签约点进来还是解约点进来
    private String everyLimit;//单笔限额
    private String dailyLimit;//单日限额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openwithhold5);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        MyApplication.getInstence().addActivity(this);
        intent = getIntent();
        signState = intent.getStringExtra("signState");
        if ("surrender".equals(signState)){
            tvTitle.setText("银行卡代扣解约");
            tvSignStateInfo.setText("您的银行卡代扣解约成功！");
        }
    }

    @OnClick({R.id.iv_back, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_confirm:
                requestPayWhMangerEntrance();
                break;
        }
    }


    /**
     * 请求汇缴代扣管理入口(签约成功后,需要调一次刷新数据)     */
    private void requestPayWhMangerEntrance(){
        String url = Define.URL+"acct/deductionsIndex";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "whMangerEntranceSuccess", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "whMangerEntranceSuccess":
                Log.d("whMangerEntranceSuccess", "entranceSuccessData = " + json.toString());
                if(bankCardList !=null){
                    bankCardList.clear();
                }
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String isDeductions = jsonObject.getString("isDeductions");//是否开通汇缴代扣标识
                   if (Define.OPEN.equals(isDeductions)){
                        JSONArray deductionsArr = jsonObject.getJSONArray("deductions");
                        for (int i = 0; i < deductionsArr.length() ; i++) {
                            BankCardBean deductionsCard = new BankCardBean();
                            JSONObject obj = (JSONObject) deductionsArr.get(i);
                            analysisCardList(deductionsCard, obj, isDeductions);
                        }

                        SPUtils2.putList(OpWithHoldFiveA.this, "cardList", bankCardList);//将数据保存起来
                        MyApplication.getInstence().activityFinish();
                        intent = new Intent(OpWithHoldFiveA.this, HasOpenedConsolidatedWithholdingManagementA.class);

                        startActivity(intent);//启动intent
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void analysisCardList(BankCardBean bankCardBean, JSONObject jsonObject, String isDeductions){
        //银行卡id
        id = jsonObject.optString("id");
        //银行卡名称
        bankName = jsonObject.optString("bankName");
        //是否为对公账户
        isPublic = jsonObject.optString("isPublic");
        //银行卡号
        cardNo = jsonObject.optString("cardNo");
        //银行标识
        bankCode = jsonObject.optString("bankCode");
        //银行卡类型
        cardType = jsonObject.optString("cardType");
        //开户人姓名
        custName = jsonObject.optString("custName");
        //单笔限额
        everyLimit = jsonObject.optString("everyLimit");
        //单日限额
        dailyLimit = jsonObject.optString("dailyLimit");

        bankCardBean.setId(id);
        bankCardBean.setCardName(bankName);
        bankCardBean.setIsPublic(isPublic);
        bankCardBean.setCardnumber(cardNo);
        bankCardBean.setBankCode(bankCode);
        bankCardBean.setCardType(cardType);
        bankCardBean.setCustname(custName);
        bankCardBean.setEveryLimit(everyLimit);
        bankCardBean.setDailyLimit(dailyLimit);
        bankCardList.add(bankCardBean);

    }
}

package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.CashListBean;
import com.huoniao.oc.common.BankIcon;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SPUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.rl_outletsPaySystem;

public class CardA extends BaseActivity implements AdapterView.OnItemClickListener {


    private static final int REQUESTCODE = 2;
    private static final int RESULTCODE = 2; //结果返回code
    private static final int RESULTCODEDATA = 3
            ;
    private static final String SELECTCARD = "selectCard";
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_add_card)
    TextView tvAddCard;
    @InjectView(R.id.lv_card)
    ListView lvCard;


    private JsonObjectRequest jsonObjectSelectCashListRequest;

    List<CashListBean.DataEntity>  jsonObjectSelectCashList ; //展示银行卡 信息结果

    private VolleyNetCommon volleyNetCommon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.inject(this);
        initWidget();
        initData();  //加载银行卡信息
    }

    private void initWidget() {
        tvTitle.setText("提现");
        setPremissionShowHideView(Premission.ACCT_USERCARD_EDIT,tvAddCard); //是否有 添加,修改,删除银卡权限
       // tvAddCard.setVisibility(View.VISIBLE);


        lvCard.setOnItemClickListener(this);
    }

    private void initData() {
        try {
        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        JSONObject userJson = new JSONObject();
            userJson.put("operateType",5);   // 1表示 查询
            userJson.put("bankCode","");
            userJson.put("cardtype",""); //
            userJson.put("cardno","");
            userJson.put("custname","");
            userJson.put("id","");
        jsonObjectSelectCashListRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.CRAD, userJson, new VolleyAbstract(this) {
            @Override
            protected void netVolleyResponese(JSONObject json) {
                Gson gson = new Gson();
                CashListBean cashBean = gson.fromJson(json.toString(), CashListBean.class);
                jsonObjectSelectCashList = cashBean.getData();
                showCash();
            }

            @Override
            protected void PdDismiss() {
                cpd.dismiss();
            }
            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }
            @Override
             public void volleyResponse(Object o) {

             }

             @Override
             public void volleyError(VolleyError volleyError) {
                 Toast.makeText(CardA.this,R.string.netError, Toast.LENGTH_SHORT).show();

             }
         },SELECTCARD, true);
                volleyNetCommon.addQueue(jsonObjectSelectCashListRequest);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showCash() {
        if(jsonObjectSelectCashList ==null){
            return;
        }
        CommonAdapter<CashListBean.DataEntity>  adapter = new CommonAdapter<CashListBean.DataEntity>(this, jsonObjectSelectCashList, R.layout.item_card_listview){

            @Override
            public void convert(ViewHolder holder, CashListBean.DataEntity dataEntity) {
                TextView tvBackName =  holder.getView(R.id.tv_bankName);//银行姓名
                TextView tvCardType = holder.getView(R.id.tv_cardType); //储蓄卡
                TextView tvCardCode = holder.getView(R.id.tv_cardCode); //银行卡号 需要截取
                ImageView imgGou = holder.getView(R.id.iv_right_arrow); //勾选
                ImageView rlOutletsPaySystem = holder.getView(rl_outletsPaySystem); //背景颜色
             ImageView iv_bankLog =   holder.getView(R.id.iv_bankLog);
                iv_bankLog.setTag(dataEntity.getId());

                if(iv_bankLog.getTag().equals(dataEntity.getId())){
                    iv_bankLog.setImageResource(BankIcon.getBankIcon(dataEntity.getBankCode()));
                }

                 imgGou.setTag(dataEntity.getId());

                if("1".equals(dataEntity.getCardType())) {
                    rlOutletsPaySystem.setTag("1");
                }else {
                    rlOutletsPaySystem.setTag("2");
                }
              //  Glide.with(CardA.this).load(R.drawable.blue_bg).into(rlOutletsPaySystem);//设置银行卡背景颜色
               tvBackName.setText(dataEntity.getCardName());

                String cardId = dataEntity.getId(); //银行卡id
                 if(dataEntity.getCardNo().equals(SPUtils.get(CardA.this,"cardNo",""))){
                     if(imgGou.getTag().equals(dataEntity.getId())) {
                         imgGou.setVisibility(View.VISIBLE);
                     }
                 }else{
                     if(imgGou.getTag().equals(dataEntity.getId())) {
                         imgGou.setVisibility(View.GONE);
                     }
                 }
                if("1".equals(dataEntity.getCardType())) {
                    if(rlOutletsPaySystem.getTag().equals("1")){
                        //Glide.with(CardA.this).load(R.drawable.blue_bg).into(rlOutletsPaySystem);//设置银行卡背景颜色
                        rlOutletsPaySystem.setBackgroundResource(R.drawable.backgroud_backcard);
                    }
                    tvCardType.setText("借记卡");
                }else {
                    if(rlOutletsPaySystem.getTag().equals("2")){
                        rlOutletsPaySystem.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    tvCardType.setText("信用卡");

                }
                String str = dataEntity.getCardNo(); // 银行卡号

                String cardNo = str.substring(str.length()-4,str.length());
                tvCardCode.setText(cardNo);


                /*rlOutletsPaySystem*/

            }
        };

        lvCard.setAdapter(adapter);
    }


    @OnClick({R.id.iv_back, R.id.tv_add_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_card:
              Intent intent = new Intent(CardA.this,TemporaryAddCardA.class);

             startActivityForResult(intent,REQUESTCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULTCODE){
            initData();  //加载银行卡信息
        }
    }

    //  条目点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       if(jsonObjectSelectCashList!=null){
           CashListBean.DataEntity dataEntity = jsonObjectSelectCashList.get(position);
           if("2".equals(dataEntity.getCardType())){
               Toast.makeText(this, "信用卡暂不支持提现！", Toast.LENGTH_SHORT).show();
           }else {
               Intent intent = new Intent();
               intent.putExtra("cash", dataEntity);
               setResult(RESULTCODEDATA, intent);
               finish();
           }
       }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(volleyNetCommon!=null){
            volleyNetCommon.getRequestQueue().cancelAll(SELECTCARD);
        }
    }
}

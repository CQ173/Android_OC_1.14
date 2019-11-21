package com.huoniao.oc.outlets;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.MySpinerAdapter;
import com.huoniao.oc.bean.CashBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SpinerPopWindow;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.et_cardNumber;
import static com.huoniao.oc.R.id.layout_cardType;
import static com.huoniao.oc.R.id.tv_bankType;
import static com.huoniao.oc.R.id.tv_cardName;
import static com.huoniao.oc.R.id.tv_cardType;

public class AddCardA extends BaseActivity implements MySpinerAdapter.IOnItemSelectListener {

    private static final String ADDCARDATAG = "addCardATag";
    private static final String ADDCARDATYPETAG = "addCardTypeTag";
    private static final String ADDCARDABACKCARD = "addCardBackCode";
    private static String CHOICE_TAG ;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(tv_bankType)
    TextView tvBankType;
    @InjectView(R.id.ll_selector_card)
    LinearLayout llSelectorCard;
    @InjectView(tv_cardType)
    TextView tvCardType;
    @InjectView(layout_cardType)
    LinearLayout layoutCardType;
    @InjectView(tv_cardName)
    TextView tvCardName;
    @InjectView(et_cardNumber)
    EditText etCardNumber;
    @InjectView(R.id.bt_save)
    Button btSave;
    private JsonObjectRequest jsonObjectRequest;
    private JsonObjectRequest jsonObjectRequestTypeCash;
    private VolleyNetCommon volleyNetCommon;



    private List<CashBean.DataEntity> dataEntityList; //请求响应结果集合

    private List<CashBean.DataEntity> dataEntityListTypeCash; //请求响应结果集合

    List<CashBean.DataEntity>  listUserCash;  //保存银行卡成功后 结果集合

    public List<String> dataListCashName;    //银行卡名字
    public List<String> dataListCashType;   //银行卡类型
    private SpinerPopWindow mSpinerPopWindow;   //弹出框
    private String cashName="";   //银行卡姓名
    private String cashType="";  //银行卡类别
    private String name;
    private String cashID;
    private String bankCode; //银行卡编码
    private JsonObjectRequest jsonUserCashObject;
    private int resultCode=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        ButterKnife.inject(this);
        initWeight();
        initData();
    }

    private void initWeight() {
        tvTitle.setText("添加银行卡");

    }

    private void initData() {
        cpd.show();
        User user = (User) ObjectSaveUtil.readObject(AddCardA.this, "loginResult");
        String operatorName = user.getOperatorName();//负责人姓名
        String corpName = user.getCorpName();// 法人姓名
        if (operatorName != null && !operatorName.isEmpty()){
            tvCardName.setText(operatorName);
        }else {
            tvCardName.setText(corpName);
        }

        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dictType", "acct_bank_code");    //银行卡编号
            jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.Cash, jsonObject, new VolleyAbstract(this) {
                @Override
                protected void netVolleyResponese(JSONObject json) {
                    Gson gson = new Gson();
                    CashBean cashBean = gson.fromJson(json.toString(), CashBean.class);
                    dataEntityList = cashBean.getData();
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
                    Toast.makeText(AddCardA.this, R.string.netError, Toast.LENGTH_SHORT).show();

                }
            },ADDCARDATAG, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        try {
            JSONObject jsonCashtype = new JSONObject();
            jsonCashtype.put("dictType","acct_card_type");   //银行卡类型

        jsonObjectRequestTypeCash = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.Cash, jsonCashtype, new VolleyAbstract(this) {
            @Override
            protected void netVolleyResponese(JSONObject json) {
                Gson gson = new Gson();
                CashBean cashBean = gson.fromJson(json.toString(), CashBean.class);
                dataEntityListTypeCash = cashBean.getData();

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
                Toast.makeText(AddCardA.this,R.string.netError, Toast.LENGTH_SHORT).show();

            }
        },ADDCARDATYPETAG, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        volleyNetCommon.addQueue(jsonObjectRequest);
        volleyNetCommon.addQueue(jsonObjectRequestTypeCash);

    }


    @OnClick({R.id.iv_back, tv_bankType, R.id.ll_selector_card, layout_cardType, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_bankType:
                CHOICE_TAG = "1";
                showPop(llSelectorCard,getListCashName());
                break;
            case R.id.ll_selector_card:

                break;
            case R.id.layout_cardType:
                CHOICE_TAG = "2";
                showPop(layoutCardType,getListCashType());
                break;
            case R.id.bt_save:
                sava();
                break;
        }
    }

    //保存操作
    private void sava() {
        name = tvCardName.getText().toString().trim();
        cashID = etCardNumber.getText().toString().trim();
        if (this.cashName.isEmpty()) {
            Toast.makeText(this, "银行不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (cashType.isEmpty()) {
            Toast.makeText(this, "卡类型不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (name.isEmpty()) {
            Toast.makeText(this, "户名不能为空！", Toast.LENGTH_SHORT).show();

            return;
        } else if (cashID.isEmpty()) {
            Toast.makeText(this, "卡号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (cashID.length() == 16 || cashID.length() == 19) {

        }else{
            Toast.makeText(this, "卡号位数为16或者卡号位数为19！", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            cpd.show();
            JSONObject userJson = new JSONObject();
            userJson.put("operateType",2);   //2表示 新增
            userJson.put("bankCode",bankCode);
            if("借记卡".equals(cashType)){
                userJson.put("cardtype",1);  // 1 表示 借记卡
            }else if("信用卡".equals(cashType)){
                userJson.put("cardtype",2); // 2 表示 信用卡
            }

            userJson.put("cardno",cashID);
            userJson.put("custname",name);
            userJson.put("id","");

            jsonUserCashObject = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.UserCash, userJson, new VolleyAbstract(this) {
                @Override
                protected void netVolleyResponese(JSONObject json) {
                    Gson gson = new Gson();
                    CashBean cashBean = gson.fromJson(json.toString(), CashBean.class);
                    listUserCash = cashBean.getData();
                    Toast.makeText(AddCardA.this, "添加银行卡成功！", Toast.LENGTH_SHORT).show();
                    cpd.dismiss();
                    setResult(resultCode);;
                    finish();
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
                    Toast.makeText(AddCardA.this, R.string.netError, Toast.LENGTH_SHORT).show();

                }
            },ADDCARDABACKCARD, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        volleyNetCommon.addQueue(jsonUserCashObject);


    }


    public void showPop(View view,List<String> list) {    //弹出框
        mSpinerPopWindow = new SpinerPopWindow(this);
        mSpinerPopWindow.refreshData(list, 0);
        mSpinerPopWindow.setItemListener(this);
       showSpinWindow(view);
    }
    private void showSpinWindow(View v) {
        Log.e("", "showSpinWindow");
        mSpinerPopWindow.setWidth(v.getWidth());
        mSpinerPopWindow.showAsDropDown(v);
    }




    //获取银行卡名字
    public List<String> getListCashName() {

        if (dataEntityList != null && dataEntityList.size() > 0) {
            dataListCashName = new ArrayList<>();
            for (CashBean.DataEntity entity : dataEntityList
                    ) {
                  dataListCashName.add(entity.getLabel());
            }
        }
          return dataListCashName;
    }

    //获取银行卡类型
    public List<String> getListCashType(){
        if (dataEntityListTypeCash != null && dataEntityListTypeCash.size() > 0) {
            dataListCashType = new ArrayList<>();
            for (CashBean.DataEntity entity : dataEntityListTypeCash
                    ) {
                dataListCashType.add(entity.getLabel());
            }
        }
        return dataListCashType;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll(ADDCARDATAG);
            volleyNetCommon.getRequestQueue().cancelAll(ADDCARDATYPETAG );
            volleyNetCommon.getRequestQueue().cancelAll(ADDCARDABACKCARD);
        }

    }

    @Override
    public void onItemClick(int pos) {
        if(pos<0){
            return;
        }
       if("1".equals(CHOICE_TAG)){
           cashName = dataListCashName.get(pos);
           tvBankType.setText(cashName);
           CashBean.DataEntity dataEntity = dataEntityList.get(pos);
           //银行编码
           bankCode = dataEntity.getValue();

       }else if("2".equals(CHOICE_TAG)){
           cashType = dataListCashType.get(pos);
           tvCardType.setText(cashType);
       }
    }
}

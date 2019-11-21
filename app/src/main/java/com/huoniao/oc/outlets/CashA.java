package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.CashListBean;
import com.huoniao.oc.bean.ConfigureBean;
import com.huoniao.oc.common.BankIcon;
import com.huoniao.oc.util.CashierInputFilter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.NumberFormatUtils;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SPUtils;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ShowPopupWindow;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import anet.channel.util.StringUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.R.id.et_money;
import static com.huoniao.oc.R.id.tv_cardCode;
import static com.huoniao.oc.util.SPUtils.get;

public class CashA extends BaseActivity {

    private static final int REQUESTCODE = 1;
    private static final int RESULTCODEDATA = 3;
    private static final String CARDNAME = "cardName";
    private static final String CARDNO = "cardNo";
    private static final String CARDTYPE="cardType";
    private static final Object TAG = "TAG";
    private static final String CARDID = "cardid";
    private static final String CASHATAG = "cashTag";
    private static final  String BANKCODE ="bankCode"; //银行编码
    @InjectView(R.id.iv_back)
    ImageView ivBack;   // 退回键
    @InjectView(R.id.tv_title)
    TextView tvTitle;  //标题
    @InjectView(R.id.rl_card)  //点击添加银行卡
            LinearLayout rlCard;
    @InjectView(et_money)
    EditText etMoney;  //金额
    @InjectView(R.id.tv_cash_withdrawal)
    TextView tvCashWithdrawal;
    @InjectView(R.id.tv_all_cash)   //全部提现
            TextView tvAllCash;
    @InjectView(R.id.tv_confirm_cash)
    TextView tvConfirmCash;  //  确认提现
    @InjectView(R.id.activity_cash)  //
            LinearLayout activityCash;
    @InjectView(R.id.iv_bankLog)
    ImageView ivBankLog;    //银行logo
    @InjectView(R.id.tv_bankName)
    TextView tvBankName;  //银行姓名
    @InjectView(R.id.tv_cardType)
    TextView tvCardType;  //银行类型
    @InjectView(tv_cardCode)
    TextView tvCardCode;  //银行卡号
    @InjectView(R.id.layout_nameAndType)
    LinearLayout layoutNameAndType;  // 没有作用
    @InjectView(R.id.rl_outletsPaySystem)
    RelativeLayout rlOutletsPaySystem;  //用于显示隐藏布局
    @InjectView(R.id.iv_cashs)
    ImageView ivCashs;
    @InjectView(R.id.iv_right_arrow)
    ImageView ivRightArrow;



   private String  momery  ;   //提现金额


    private  double canEnchashMoney ;
    private String balance;
    private String minimum;
    private String repayDay;
    private String dynaMinimum;
    private VolleyNetCommon volleyNetCommon;
    private String cashMinAmount;

    private String preventRepeatToken;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        ButterKnife.inject(this);
        InputFilter[] filters = {new CashierInputFilter()};
        etMoney.setFilters(filters);
        initWeight();
        doRefulash(); //重新刷新
    }



    private void initWeight() {

        Calendar c = Calendar.getInstance();
        int dateDay = c.get(Calendar.DATE);
        String repayDay = (String) get(CashA.this,"repayDay","");  //还款日
        String dynaMinimum = (String) get(CashA.this,"dynaMinimum",""); ////动态最低限额
        String balance = (String) get(CashA.this,"balance","");  //// 余额
        String minimum = (String) get(CashA.this,"minimum","");  //最低限额

        if(StringUtils.isNotBlank(repayDay) && Integer.parseInt(repayDay) ==dateDay) {
            //可提现金额 = 余额  - 动态最低限额
           // canEnchashMoney = user.getBalance().subtract(user.getDynaMinimum()).doubleValue() > 0 ? user.getBalance().subtract(user.getDynaMinimum()):new BigDecimal(0);
            canEnchashMoney = Double.parseDouble(balance) - Double.parseDouble(dynaMinimum);
        } else {
            //可提现金额 = 余额  - 动态最低限额 - 最低限额
          //  canEnchashMoney = user.getBalance().subtract(user.getMinimum().add(user.getDynaMinimum())).doubleValue() > 0 ? user.getBalance().subtract(user.getMinimum().add(user.getDynaMinimum())):new BigDecimal(0);
            canEnchashMoney = Double.parseDouble(balance) - Double.parseDouble(dynaMinimum)- Double.parseDouble(minimum);
        }

        tvCashWithdrawal.setText("可用余额"+NumberFormatUtils.formatDigits(canEnchashMoney)+"元");

        etMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //只能输入数字或者小数点
        rlOutletsPaySystem.setVisibility(View.GONE);
        ivRightArrow.setVisibility(View.GONE);
        String cardNo = (String) get(this, CARDNO, "");
        if(!cardNo.isEmpty()){
            ivRightArrow.setVisibility(View.VISIBLE);
            rlOutletsPaySystem.setVisibility(View.VISIBLE);
            tvBankName.setText(get(this,CARDNAME,"")+"");
            String s = cardNo.substring(cardNo.length()-4,cardNo.length());
            tvCardCode.setText(s);
            if("1".equals(get(this,CARDTYPE,""))) {
                tvCardType.setText("借记卡");
            }else {
                tvCardType.setText("信用卡");
            }
        }
        String bankCode = (String) get(CashA.this,BANKCODE,"");
         ivBankLog.setImageResource(BankIcon.getBankIcon(bankCode)); //银行图标
        tvTitle.setText("提现");

    }

    @OnClick({R.id.iv_back, R.id.rl_card, R.id.tv_all_cash, R.id.tv_confirm_cash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_card:
                Intent intent = new Intent(this, CardA.class);
                startActivityForResult(intent, REQUESTCODE);
                break;
            case R.id.tv_all_cash:

                if(Double.parseDouble(NumberFormatUtils.formatDigits(canEnchashMoney))<=0){
                    Toast.makeText(this, "提现金额不可以小于等于0.00元", Toast.LENGTH_SHORT).show();
                    break;
                }
                etMoney.setText(NumberFormatUtils.formatDigits(canEnchashMoney)+"");

                break;
            case R.id.tv_confirm_cash:
              if(!check()){
                 break;
              }else {
                  confirmCash(view);
              break;
              }
        }
    }
    List<ConfigureBean.ListEntity> entityList ;
    private boolean check() {
        if(entityList !=null){
            entityList.clear();
        }
        entityList = (List<ConfigureBean.ListEntity>) ObjectSaveUtil.readObject(CashA.this,"ListEntity"); //配置集合
        for (ConfigureBean.ListEntity entity :
                entityList) {
            if(entity.getCash_min_amount() !=null) {
                cashMinAmount = entity.getCash_min_amount();
                break;
            }

        }

        Double  min_mount=Double.parseDouble(cashMinAmount);

        // 校验输入框 等其他
        //String trim = tvCardCode.getText().toString().trim();
        String trime = get(this,CARDNO,"")+""; //  CARDNAME
        if(trime.isEmpty()){
            Toast.makeText(this, "银行卡号为空，请添加银行卡！", Toast.LENGTH_SHORT).show();
           return false;
        }
        String etMoneyCashSize = etMoney.getText().toString().trim();
        if(etMoneyCashSize.isEmpty()){
            Toast.makeText(this, "提现金额不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        } 
        if(Double.parseDouble(etMoneyCashSize)<min_mount){
             Toast.makeText(this, "提现金额不可以小于"+min_mount+"元！", Toast.LENGTH_SHORT).show();
          return false;
         }

        if(Double.parseDouble(NumberFormatUtils.formatDigits(canEnchashMoney)) <Double.parseDouble(etMoneyCashSize)){  //最低提现如果小于提现金额 就提示不让提现
            Toast.makeText(this, "输入金额不可以大于可用余额！", Toast.LENGTH_SHORT).show();
          return  false;
        }

     return true;


    }

    //确认提现
    private void confirmCash(View view) {

       String s = "^[0-9]+(\\.[0-9]{1,2})?$";
        momery = etMoney.getText().toString().trim();
        if(!momery.matches(s)){
            Toast.makeText(this, "提现金额不正确！", Toast.LENGTH_SHORT).show();
            return;
        }
        double m = Double.parseDouble(momery);

        ShowPopupWindow showPopupWindow = new ShowPopupWindow();
        showPopupWindow.showPopWindowPwd(view, ViewGroup.MarginLayoutParams.MATCH_PARENT,ViewGroup.MarginLayoutParams.MATCH_PARENT, NumberFormatUtils.formatDigits(m));
         showPopupWindow.setPopRefundListener(new ShowPopupWindow.PopRefund() {
             @Override
             public void confirm(Object object) {
                 pwd = (String) object;
                   //  最后支付密码框消失后   在此处获取密码   进行支付
                //  Toast.makeText(CashA.this, "密码是"+pwd, Toast.LENGTH_LONG).show();
//                  startCash();

                 getToken();
             }

             @Override
             public void cancle(Object object) {

             }
         });

    }


    /**
     * 开始提现
     */
    private void startCash() {
        cpd.show();
        try {
            volleyNetCommon = new VolleyNetCommon();
            JSONObject jsonObject = new JSONObject();
            //if() ;  需要对金额进行判断才可去支付
             //String o = (String) get(CashA.this, CARDNO, "");
           String o =  (String)SPUtils.get(CashA.this,CARDID,"");
            jsonObject.put("enchMoney",momery);  //提现金额
            jsonObject.put("userCardId", o); //银行卡id
            jsonObject.put("payPassword",pwd);  //提现密码
            jsonObject.put("preventRepeatToken",preventRepeatToken);  //防重复请求token
          //   String URL="http://192.168.0.111:8181/OC/app/acct/enchashSave";
            JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.WITHDRAWCASH, jsonObject, new VolleyAbstract(this) {
                @Override
                protected void netVolleyResponese(JSONObject json) {
                    doRefulash(); //重新刷新
                    Toast.makeText(CashA.this, "提现申请提交成功！", Toast.LENGTH_SHORT).show();
                    setResult(102); //刷新余额
                    finish();
                }

                @Override
                protected void PdDismiss() {
                    cpd.dismiss();
                }

                @Override
                public void volleyResponse(Object o) {

                }

                @Override
                protected void errorMessages(String message) {
                    super.errorMessages(message);
                    Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                    Toast.makeText(CashA.this, "提现失败！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void volleyError(VolleyError volleyError) {
                    cpd.dismiss();
                    Toast.makeText(CashA.this, R.string.netError, Toast.LENGTH_SHORT).show();

                }
            },CASHATAG, false);
            volleyNetCommon.addQueue(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取Token
     */
    private void getToken() {
        String url = Define.URL + "fb/preventRepeatSubmit";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "enchashSavePreventRepeatSubmit", "0", true, false);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag){
            case "enchashSavePreventRepeatSubmit":
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    preventRepeatToken = jsonObject.getString("preventRepeatToken");
                    if (preventRepeatToken != null){
                        SPUtils2.putString(CashA.this, "preventRepeatToken", preventRepeatToken);
                        startCash();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULTCODEDATA) {
            CashListBean.DataEntity cashData = (CashListBean.DataEntity) data.getSerializableExtra("cash");
            if (cashData != null) {
                rlOutletsPaySystem.setVisibility(View.VISIBLE);
                ivRightArrow.setVisibility(View.VISIBLE);
                try {
                    Glide.with(CashA.this).load(R.drawable.backgroud_backcard).into(ivCashs); //设置图片
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String cardNo = cashData.getCardNo();  // 银行卡号
                String cardName = cashData.getCardName(); //银行卡名字
                String cardType = cashData.getCardType();  //银行卡类型
                String cardId = cashData.getId(); //银行卡id
                 String bankCode = cashData.getBankCode(); //银行编号

                SPUtils.put(CashA.this,CARDNO, cardNo==null?"":cardNo);  //存储银行卡卡号 作为显示
                SPUtils.put(CashA.this,CARDNAME,cardName==null?"":cardName);  //存储银行姓名
                SPUtils.put(CashA.this,CARDTYPE,cardType==null?"":cardType); //存储银行卡类型
                SPUtils.put(CashA.this,CARDID,cardId == null ?"":cardId);  //存储银行卡id  提现需要用到
                SPUtils.put(CashA.this,BANKCODE,bankCode==null?"":bankCode); //存放银行编号
                initWeight(); //重新初始化
            }
        }
    }


//主要用于刷新刚开始登录的数据
    private void doRefulash() {
        String url = Define.URL + "user/userLogin";
        //	http://192.168.0.152:8080/OC/app/user/userLogin

        cpd.show();
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("acctNum", get(CashA.this, "userName", ""));
            jsonObj.put("password",  get(CashA.this, "password", ""));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final SessionJsonObjectRequest loginRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {//jsonObject为请求成功返回的Json格式数据
                if (response == null) {
                    Toast.makeText(CashA.this, "服务器数据异常！", Toast.LENGTH_SHORT).show();
                    cpd.dismiss();
                    return;
                }
                Log.d("debug", "response: "+ response.toString());
                try {
                    String code = response.getString("code");
                    if ("0".equals(code)) {
                        JSONArray agreementArray = null;

                        List<Integer> list = new ArrayList<Integer>();
                     /*   String repayDay = (String) SPUtils.get(CashA.this,"repayDay","");  //还款日
                        String dynaMinimum = (String) SPUtils.get(CashA.this,"dynaMinimum",""); ////动态最低限额
                        String balance = (String) SPUtils.get(CashA.this,"balance","");  //// 余额
                        String minimum = (String) SPUtils.get(CashA.this,"minimum","");  //最低限额*/

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject temp = (JSONObject) jsonArray.get(i);
                            // 余额
                            balance = temp.optString("balanceString");
                            // 账户最低限额
                            minimum = temp.optString("minimum");
                            repayDay = temp.optString("repayDay");
                            //动态最低限额
                            dynaMinimum = temp.optString("dynaMinimum");

                            agreementArray = temp.optJSONArray("agreement");
                            if (agreementArray != null && !"".equals(agreementArray)) {
                                for (int j = 0; j < agreementArray.length(); j++) {

                                    list.add(agreementArray.getInt(j));
                                }
                            }



                        }
                        Log.d("test", "list=" + list);
//								 	  if (!"".equals(agreementArray) || agreementArray != null) {
//									    	 for (int j = 0; j < agreementArray.length(); j++) {
//
//											    	list.add(agreementArray.getInt(j));
//											    }
//										}
                        Integer[] array = new Integer[list.size()];
                        for(int k=0;k<list.size();k++){
                            array[k]=list.get(k);
                        }
                        //(String[])list.toArray(new String[list.size()]);
                        String agreement = Arrays.toString(array);

                        SPUtils.put(CashA.this,"repayDay",repayDay);
                        SPUtils.put(CashA.this,"dynaMinimum",dynaMinimum);
                        SPUtils.put(CashA.this,"balance",balance);
                        SPUtils.put(CashA.this,"minimum",minimum);
                        cpd.dismiss();

                    }else {
                        cpd.dismiss();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {//jsonObject为请求失败返回的Json格式数据
                cpd.dismiss();

                Toast.makeText(CashA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//			byte[] htmlBodyBytes = error.networkResponse.data;
//			Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
            }
        });

        //解决重复请求后台的问题
        loginRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        10*1000,//默认超时时间，应设置一个稍微大点儿的
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        loginRequest.setTag(TAG);
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(loginRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(volleyNetCommon !=null) {
            volleyNetCommon.getRequestQueue().cancelAll(CASHATAG);

        }
    }
}

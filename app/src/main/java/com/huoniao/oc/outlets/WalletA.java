package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.BankCardBean;
import com.huoniao.oc.bean.ConfigureBean;
import com.huoniao.oc.bean.OltsCapitalFlowBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.MyGridView;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.SettingPayPwd;
import com.huoniao.oc.user.UpdatePayPwd;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.NumberFormatUtils;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SPUtils2;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;


public class WalletA extends BaseActivity implements OnClickListener {
    private ImageView iv_back;
    private TextView tv_balance, tv_minimum;
    //private LinearLayout layout_recharge, layout_withdrawals, layout_borrowMoney, layout_capitalFlow;

    private Intent intent;
    private SwipeRefreshLayout walletsSwipeRefreshLayout;
    private int[] colors = {R.color.colorPrimary, R.color.colorAccent, R.color.gray};
    private boolean walletsSwipeRefresh = false;  //钱包刷新状态
    //private LinearLayout layout_walletPaymentCode;  //收款码
    private MyGridView gv_menu;
    private List<String> menuList;
    private TextView tv_paySetting;
    private MyPopWindow popWindowPaySetting;  //设置
    private VolleyNetCommon volleyNetCommon;
    private LinearLayout ll_bank_card_management;
    private LinearLayout layout_capitalFlow;
    private LinearLayout ll_withholding_management;
    private TextView tv_residual_minimum;
    private User user;
    private String balanceStr;  //账户余额
    private String minimumStr; //账户最低限额
    private String id;
    private String bankName;
    private String isPublic;
    private String cardNo;
    private String bankCode;
    private String cardType;
    private List<BankCardBean> bankCardList = new ArrayList<BankCardBean>();
    private String custName;
    private String everyLimit;//单笔限额
    private String dailyLimit;//单日限额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
        initData();
        initWidget();
    }

    private void initWidget() {
        setPremissionShowHideView(Premission.ACCT_USERCARD_VIEW, ll_bank_card_management);  //是否有"银行卡管理"菜单权限
        setPremissionShowHideView(Premission.ACCT_TRADEFLOW_VIEW, layout_capitalFlow); //是否有"交易流水"菜单权限
        setPremissionShowHideView(Premission.ACCT_DEDUCTIONS_INDEX, ll_withholding_management); //汇缴代扣权限

		/*gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

				final String s = menuList.get(i);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						switch (s){
							case "recharge"://充值
								intent = new Intent(WalletA.this, RechargeA.class);
								intent.putExtra("balanceStr",balanceStr);
								intent.putExtra("minimumStr",minimumStr);
								startActivityForResult(intent,102);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								break;
							case "now"://提现
								startActivityForResult(new Intent(WalletA.this,CashA.class),102);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								break;
							case "money"://借钱
								Toast.makeText(WalletA.this, "正在建设中", Toast.LENGTH_SHORT).show();
								break;
							case "flowOfFunds": //资金流水
								try {
									getOltCapitalFlowData	();
								} catch (Exception e) {

									e.printStackTrace();
								}
								break;
							case "paymentCode":
								startActivityForResult(new Intent(WalletA.this,WalletMeQRCode.class),102);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								break;

						}
					}
				});
			}
		});*/
    }

    private void initData() {
        user = (User) readObject(WalletA.this, "loginResult");
        balanceStr = user.getBalance();  //账户余额
        if (balanceStr != null && !balanceStr.isEmpty()) {
            Double balance = Double.valueOf(balanceStr);
            tv_balance.setText(String.format("%.2f", balance));
        }

        try {
            refreshBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        minimumStr = user.getMinimum(); //最低限额
        if (minimumStr != null && !minimumStr.isEmpty()) {
            Double minimum = Double.valueOf(minimumStr);
            tv_minimum.setText(String.format("%.2f", minimum));
        }
        if (user.getMinimum() != null && user.getBalance() != null) {
            getConvertResidualMinimum(user.getMinimum(), user.getBalance(), tv_residual_minimum);
        }
        menuList = new ArrayList<>();
        menuList.clear();
        menuList.add("paymentCode"); //收款码
        menuList.add("recharge"); //充值


	     /*   menuList.add("money"); //借钱
	    menuList.add("flowOfFunds"); //资金流水
	      menuList.add("paymentCode"); //收款码
	      menuList.add("station"); //站位*/
        for (String premissions : premissionsList) {
            if (Premission.ACCT_TRADEFLOW_ENCHASHMENT.equals(premissions)) {
                menuList.add("now"); //提现
            }
        }
/*	 boolean flag =false;
	 //是否有"交易流水"菜单权限
		for (int i = 0; i < premissionsList.size(); i++) {
			String premissions = premissionsList.get(i);
			if(Premission.ACCT_TRADEFLOW_VIEW.equals(premissions)){
			    flag = true;
			}
		}

		if(!flag){
			menuList.remove(3);
		}*/

       /* gv_menu.setNumColumns(3);
		gv_menu.setAdapter(new CommonAdapter<String>(WalletA.this, menuList,R.layout.wallet_grid_menu) {
			@Override
			public void convert(ViewHolder holder, String s) {
			ImageView iv_menu =	 holder.getView(R.id.iv_menu);
			TextView tv_menu =	holder.getView(R.id.tv_menu);
			LinearLayout layout_recharge=	holder.getView(R.id.layout_recharge);
				switch (s){
					case "recharge":
						layout_recharge.setVisibility(View.VISIBLE);
						setMenu("缴费", iv_menu, tv_menu, recharge);

						break;
					case "now":
						layout_recharge.setVisibility(View.VISIBLE);
						setMenu("提现", iv_menu, tv_menu,R.drawable.withdrawals);
						break;
				*//*	case "money":
						layout_recharge.setVisibility(View.VISIBLE);
						setMenu("借钱", iv_menu, tv_menu,R.drawable.borrow_money);
						break;*//*
         *//*	case "flowOfFunds":
						layout_recharge.setVisibility(View.VISIBLE);
						setMenu("资金流水", iv_menu, tv_menu,R.drawable.capital_flow_logo);

						break;*//*
					case "paymentCode":
						layout_recharge.setVisibility(View.VISIBLE);
						setMenu("收款码", iv_menu, tv_menu,R.drawable.paymentcode2);
						break;
					*//*case "station":
         *//**//*	layout_recharge.setVisibility(View.VISIBLE);
						iv_menu.setBackgroundColor(getResources().getColor(R.color.white));
						tv_menu.setText("");  //占位*//**//*
						break;*//*
				}
			}
		});*/

    }

    private void setMenu(String s, ImageView iv_recharge, TextView tv_recharge, int drawableId) {
        iv_recharge.setImageDrawable(getResources().getDrawable(drawableId));
        tv_recharge.setText(s);
    }


    /**
     * //剩余最低呢充值额
     *
     * @param minimum  最低限额
     * @param balance  账户余额
     * @param textView
     */
    public void getConvertResidualMinimum(String minimum, String balance, TextView textView) {
        double minimumRemaining = Double.valueOf(minimum) - Double.valueOf(balance);
        double minimumRemainingRecharge = minimumRemaining <= 0 ? 0 : minimumRemaining;
        textView.setText(NumberFormatUtils.formatDigits(minimumRemainingRecharge) + "");  //剩余最低充值额
    }

    private void initView() {

        ll_bank_card_management = (LinearLayout) findViewById(R.id.ll_bank_card_management);//银行卡管理
        layout_capitalFlow = (LinearLayout) findViewById(R.id.layout_capitalFlow); //资金流水
        ll_withholding_management = (LinearLayout) findViewById(R.id.ll_withholding_management);//汇缴代扣管理
        tv_residual_minimum = (TextView) findViewById(R.id.tv_residual_minimum);//剩余最低充值额
        iv_back = (ImageView) findViewById(R.id.iv_back);

        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_minimum = (TextView) findViewById(R.id.tv_minimum);

        tv_paySetting = (TextView) findViewById(R.id.tv_paySetting);  //支付设置
        //	layout_recharge = (LinearLayout) findViewById(R.id.layout_recharge);
        //	layout_withdrawals = (LinearLayout) findViewById(layout_withdrawals);
        //	layout_borrowMoney = (LinearLayout) findViewById(layout_borrowMoney);
        //	layout_capitalFlow = (LinearLayout) findViewById(layout_capitalFlow);
        walletsSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.wallet_swipeRefresh);
        //	layout_walletPaymentCode = (LinearLayout) findViewById(R.id.layout_wallet_paymentCode);
//		gv_menu = (MyGridView) findViewById(R.id.gv_menu);

        iv_back.setOnClickListener(this);

        tv_paySetting.setOnClickListener(this);
        //layout_recharge.setOnClickListener(this);
        //	layout_withdrawals.setOnClickListener(this);
        //	layout_borrowMoney.setOnClickListener(this);
        //	layout_capitalFlow.setOnClickListener(this);
        //	layout_walletPaymentCode.setOnClickListener(this);

        ll_bank_card_management.setOnClickListener(this);
        layout_capitalFlow.setOnClickListener(this);
        ll_withholding_management.setOnClickListener(this);

        walletsSwipeRefreshLayout.setColorScheme(colors);
        walletsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (walletsSwipeRefreshLayout.isRefreshing()) {
                    walletsSwipeRefresh = true;
                    try {
                        refreshBalance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setResult(102); //去刷新主界面余额
                finish();
                break;


            case R.id.tv_paySetting:
                showPopPaySetting();
                break;
			
	/*	case R.id.layout_recharge:
			intent = new Intent(WalletA.this, RechargeA.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;*/
	/*	case layout_withdrawals:   //提现
			//Toast.makeText(WalletA.this, "正在建设中", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this,CashA.class));
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;	*/
	/*	case layout_borrowMoney:
			Toast.makeText(WalletA.this, "正在建设中", Toast.LENGTH_SHORT).show();
			break;	*/
	/*	case layout_capitalFlow:
			
			try {
				getOltCapitalFlowData();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			break;*/
		/*	case R.id.layout_wallet_paymentCode:
                  startActivity(new Intent(this,WalletMeQRCode.class));
				   overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				break;*/
            case R.id.ll_bank_card_management: //银行卡管理
                intent = new Intent(WalletA.this, BankCardListA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.layout_capitalFlow:  //资金流水
//                try {
//                    getOltCapitalFlowData();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                intent = new Intent(WalletA.this, CapitalFlowA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
            case R.id.ll_withholding_management: //汇缴代扣管理

                requestPayWhMangerEntrance();

                break;

            default:
                break;
        }

    }


    private void showPopPaySetting() {

        popWindowPaySetting = new MyPopAbstract() {
            @Override
            protected void setMapSettingViewWidget(View view) {
                TextView tv_paypwd = (TextView) view.findViewById(R.id.tv_paypwd);
                tv_paypwd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ThreadCommonUtils.runonuiThread(new Runnable() {
                            @Override
                            public void run() {
                                getPhone();

                            }
                        });
                    }
                });
            }

            @Override
            protected int layout() {
                return R.layout.paysetting_pop;
            }
        }.popWindowTouch(this).showAsDropDown(tv_paySetting);


    }

    private void getPhone() {
        cpd.show();
        volleyNetCommon = new VolleyNetCommon();
        JSONObject jsonObject = new JSONObject();

        String url = Define.URL + "user/refreshMobile";
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Method.POST, url, jsonObject, new VolleyAbstract(WalletA.this) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(WalletA.this, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                try {
                    String mobile = json.getString("mobile");
                    if (MyApplication.payPasswordIsEmpty) {  //true表示支付密码为空 没有设置
                        Intent intent = new Intent(WalletA.this, SettingPayPwd.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(WalletA.this, UpdatePayPwd.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                    }
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    popWindowPaySetting.dissmiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
        }, "tagPhone", true);

        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    /**
     * 充值后刷新余额
     *
     * @throws Exception
     */
    private void refreshBalance() throws Exception {
        cpd.show();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("", "");

        String url = Define.URL + "user/getBalance";
        // final List<OutletsMyLogBean> oltLogList = new
        // ArrayList<OutletsMyLogBean>();
        SessionJsonObjectRequest refreshBalanceRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
                new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String code = response.getString("code");
                            String message = response.getString("msg");
                            balanceStr = response.optString("data");
                            minimumStr = response.optString("minimum"); //最低限额
                            if ("0".equals(code)) {
                                if (!"".equals(balanceStr) && balanceStr != null) {
                                    Double balance2 = Double.valueOf(balanceStr);
                                    tv_balance.setText(String.format("%.2f", balance2));
                                }
                                if (!"".equals(balanceStr) && minimumStr != null && user.getBalance() != null) {
                                    Double minimums = Double.valueOf(minimumStr);
                                    tv_minimum.setText(String.format("%.2f", minimums));
                                    getConvertResidualMinimum(response.optString("minimum"), balanceStr, tv_residual_minimum);  //剩余最低充值额
                                }

                                if (walletsSwipeRefresh == true) {
                                    walletsSwipeRefresh = false;
                                    Toast.makeText(WalletA.this, "刷新成功！", Toast.LENGTH_SHORT).show();
                                }

                            } else if ("46000".equals(code)) {

                                Toast.makeText(WalletA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(WalletA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {

                                Toast.makeText(WalletA.this, message, Toast.LENGTH_SHORT).show();
                            }
                            cpd.dismiss();
                            walletStopRefreshing();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                walletStopRefreshing();
                Toast.makeText(WalletA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

            }
        });
        // 解决重复请求后台的问题
        refreshBalanceRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        refreshBalanceRequest.setTag("refreshBalanceRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(refreshBalanceRequest);

    }


    public void walletStopRefreshing() {
        //刷新完成关闭动画
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                walletsSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 获取银行卡列表
     *
     * @throws Exception
     */
    private void getBankCardList() throws Exception {
        cpd.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("operateType", Define.BANKCARD_SELECT);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        String url = Define.URL + "acct/manageBankCard";


        SessionJsonObjectRequest bankCardListRequest = new SessionJsonObjectRequest(Method.POST,
                url, jsonObject, new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response == null) {
                    return;
                }
//						String data = response.toString();
//						if (data.isEmpty() || data == null) {
//							return;
//						}
                Log.d("debug", "response =" + response.toString());
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
//								int num = response.getInt("size");
                        JSONArray jsonArray = response.getJSONArray("data");

                        final List<BankCardBean> bankCardData = new ArrayList<BankCardBean>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            BankCardBean bankCard = new BankCardBean();

                            JSONObject cardObj = (JSONObject) jsonArray.get(i);
                            String id = cardObj.optString("id");// 银行卡id
                            String bankCode = cardObj.optString("bankCode");// 银行卡编码
                            String cardType = cardObj.optString("cardType");// 银行卡类型
                            String cardno = cardObj.optString("cardNo");// 银行卡卡号
                            String custname = cardObj.optString("custName");// 开户姓名

                            bankCard.setId(id);
                            bankCard.setCardnumber(cardno);
                            bankCard.setCardType(cardType);
                            bankCard.setCustname(custname);
                            bankCard.setBankCode(bankCode);
                            bankCardData.add(bankCard);
                        }
                        intent = new Intent(WalletA.this, BankCardListA.class);
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("bankCardData", (Serializable) bankCardData);
                        // bundle.putSerializable("msgBean",
                        // oltMsgBean);
                        intent.putExtras(bundle);
                        cpd.dismiss();
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);


                    } else if ("46000".equals(responseCode)) {
                        cpd.dismiss();
                        Toast.makeText(WalletA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(WalletA.this, LoginA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    } else {
                        cpd.dismiss();
                        Toast.makeText(WalletA.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exciption", "exciption =" + e.toString());
                }

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(WalletA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("debug", "error = " + error.toString());

            }
        });
        // 解决重复请求后台的问题
        bankCardListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        bankCardListRequest.setTag("bankCardListRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(bankCardListRequest);

    }


    /**
     * 获取代售点资金流水
     *
     * @throws Exception
     */
//    private void getOltCapitalFlowData() throws Exception {
//        cpd.show();
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("pageNo", "1");
//        } catch (JSONException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        String url = Define.URL + "acct/tradeFlowList";
//        final List<OltsCapitalFlowBean> capitalFlowData = new ArrayList<OltsCapitalFlowBean>();
//        SessionJsonObjectRequest capitalFlowRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
//                new Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        if (response == null) {
//                            Toast.makeText(WalletA.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
//                            cpd.dismiss();
//                            return;
//                        }
//                        Log.d("debug", "response =" + response.toString());
//                        try {
//                            String responseCode = response.getString("code");
//                            String message = response.getString("msg");
//                            if ("0".equals(responseCode)) {
//                                int num = response.getInt("size");
//                                JSONArray jsonArray = response.getJSONArray("data");
//                                num = Math.min(num, jsonArray.length());
//                                // for (int i = 0; i < jsonArray.length(); i++)
//                                // {
//                                double incomeCount = 0;// 总收入
//                                double payoutCount = 0; //总支出
//                                for (int i = 0; i < num; i++) {
//                                    OltsCapitalFlowBean cptlFlowBean = new OltsCapitalFlowBean();
//
//                                    JSONObject cptlFlowObj = (JSONObject) jsonArray.get(i);
//                                    String flowId = cptlFlowObj.optString("flowId");// 交易流水号
//                                    String tradeName = cptlFlowObj.optString("tradeName");// 名称
//                                    JSONObject tradeUser = cptlFlowObj.optJSONObject("tradeUser");// 交易的用户信息
//                                    String curBalance = cptlFlowObj.optString("curBalanceString");// 用户当前余额
//                                    String loginName = tradeUser.optString("loginName");//交易账户-用户名
//                                    String name = tradeUser.optString("name");//用户姓名
//
//                                    // 用optString()解析字段可以防止字段没有值时抛异常
//                                    String tradeAcct = cptlFlowObj.optString("tradeAcct");// 交易账号
//                                    if ("".equals(tradeAcct) || tradeAcct == null) {
//                                        tradeAcct = "无";
//                                    }
//                                    String tradeFee = cptlFlowObj.optString("tradeFeeString");// 交易金额
//
//                                    String type = cptlFlowObj.optString("type");// 交易类型
//                                    // income:收入
//                                    // payout:支出
//                                    String tradeStatus = cptlFlowObj.optString("tradeStatus");// 交易状态
//                                    String tradeDate = cptlFlowObj.optString("tradeDate");// 交易日期
//                                    String channelType = cptlFlowObj.optString("channelType"); //交易渠道
//                                    String expireTime = cptlFlowObj.optString("expireTime"); // 超时时间
//                                    cptlFlowBean.setFlowId(flowId);
//                                    cptlFlowBean.setTradeName(tradeName);
//                                    cptlFlowBean.setLoginName(loginName);
//                                    cptlFlowBean.setName(name);
//                                    // oltsRelation.setArea(area);
//                                    cptlFlowBean.setTradeAcct(tradeAcct);
//                                    cptlFlowBean.setTradeFee(tradeFee);
//                                    cptlFlowBean.setCurBalance(curBalance);
//                                    cptlFlowBean.setType(type);
//                                    cptlFlowBean.setTradeStatus(tradeStatus);
//                                    cptlFlowBean.setTradeDate(tradeDate);
//                                    cptlFlowBean.setChannelType(channelType);
//                                    cptlFlowBean.setExpireTime(expireTime);
//                                    Log.d("debug", cptlFlowBean.getFlowId());
//                                    capitalFlowData.add(cptlFlowBean);
//
//
//                                    // 收入总金额  交易失败不加 退款中不加，等待付款不加，已退款不加
//                                    //支出总金额  一样
//                                    if (Define.INCOME.equals(type)) {
//                                        if (!(Define.ACCT_TRADE_STATUS_FAIL.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUNDING.equals(tradeStatus)
//                                                || Define.ACCT_TRADE_STATUS_WAIT.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUND.equals(tradeStatus))) {
//                                            incomeCount += Double.parseDouble(tradeFee);
//                                        }
//                                    } else if (Define.PAYOUT.equals(type)) {
//                                        if (!(Define.ACCT_TRADE_STATUS_FAIL.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUNDING.equals(tradeStatus)
//                                                || Define.ACCT_TRADE_STATUS_WAIT.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUND.equals(tradeStatus))) {
//                                            payoutCount += Double.parseDouble(tradeFee);
//                                        }
//
//                                    }
//                                }
//
//                                intent = new Intent(WalletA.this, CapitalFlowA.class);
//                                Bundle bundle = new Bundle();
//
//                                bundle.putSerializable("capitalFlowData", (Serializable) capitalFlowData);
//                                bundle.putDouble("payoutCount", payoutCount);
//                                bundle.putDouble("incomeCount", incomeCount);
//                                // bundle.putSerializable("msgBean",
//                                // oltMsgBean);
//                                intent.putExtras(bundle);
//                                cpd.dismiss();
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                            } else if ("46000".equals(responseCode)) {
//                                cpd.dismiss();
//                                Toast.makeText(WalletA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
//                                intent = new Intent(WalletA.this, LoginA.class);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
//                            } else {
//                                cpd.dismiss();
//                                Toast.makeText(WalletA.this, message, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//
//                        }
//
//                    }
//                }, new ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                cpd.dismiss();
//                Toast.makeText(WalletA.this, R.string.netError, Toast.LENGTH_SHORT).show();
//                Log.d("REQUEST-ERROR", error.getMessage(), error);
////						byte[] htmlBodyBytes = error.networkResponse.data;
////						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
//
//            }
//        });
//        // 解决重复请求后台的问题
//        capitalFlowRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
//        capitalFlowRequest.setTag("capitalFlow2");
//        // 将请求加入全局队列中
//        MyApplication.getHttpQueues().add(capitalFlowRequest);
//
//    }

    /**
     * 请求汇缴代扣管理入口
     */
    private void requestPayWhMangerEntrance() {
        String url = Define.URL + "acct/deductionsIndex";
        JSONObject jsonObject = new JSONObject();

        requestNet(url, jsonObject, "whMangerEntrance", "", true, true);
    }

    @Override
    protected void dataSuccess(JSONObject json, String tag, String pageNumber) {
        super.dataSuccess(json, tag, pageNumber);
        switch (tag) {
            case "whMangerEntrance":
                Log.d("whMangerEntrance", "entranceData = " + json.toString());
                if (bankCardList != null) {
                    bankCardList.clear();
                }
                try {
                    JSONObject jsonObject = json.getJSONObject("data");
                    String isDeductions = jsonObject.getString("isDeductions");//是否开通汇缴代扣标识
                    if (Define.NOT_OPENED.equals(isDeductions)) {
                        JSONArray unDeductionsArr = jsonObject.getJSONArray("unDeductions");
                        for (int i = 0; i < unDeductionsArr.length(); i++) {
                            BankCardBean unDeductionsCard = new BankCardBean();
                            JSONObject unObj = (JSONObject) unDeductionsArr.get(i);
                            analysisCardList(unDeductionsCard, unObj, isDeductions);
                        }
                        SPUtils2.putList(WalletA.this, "unCardList", bankCardList);//将数据保存起来
                        intent = new Intent(WalletA.this, OpWithHoldA.class);
                        startActivity(intent);//启动intent
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    } else if (Define.OPEN.equals(isDeductions)) {
                        JSONArray deductionsArr = jsonObject.getJSONArray("deductions");
                        for (int i = 0; i < deductionsArr.length(); i++) {
                            BankCardBean deductionsCard = new BankCardBean();
                            JSONObject obj = (JSONObject) deductionsArr.get(i);
                            analysisCardList(deductionsCard, obj, isDeductions);
                        }

                        SPUtils2.putList(WalletA.this, "cardList", bankCardList);//将数据保存起来
                        intent = new Intent(WalletA.this, HasOpenedConsolidatedWithholdingManagementA.class);

                        startActivity(intent);//启动intent
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                    } else if (Define.CLOSE.equals(isDeductions)) {
                        JSONArray deductionsArr = jsonObject.getJSONArray("deductions");
                        for (int i = 0; i < deductionsArr.length(); i++) {
                            BankCardBean deductionsCard = new BankCardBean();
                            JSONObject obj = (JSONObject) deductionsArr.get(i);
                            analysisCardList(deductionsCard, obj, isDeductions);
                        }
                        SPUtils2.putList(WalletA.this, "closeCardList", bankCardList);//将数据保存起来
                        SPUtils2.putList(WalletA.this, "cardList", bankCardList);//将数据保存起来
                        intent = new Intent(WalletA.this, OpWithHoldTwoA.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("cardState", "closeState");
                        intent.putExtras(bundle);//发送数据
                        startActivity(intent);//启动intent
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void analysisCardList(BankCardBean bankCardBean, JSONObject jsonObject, String isDeductions) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 102:
                try {
                    refreshBalance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("bankCardListRequest");
        MyApplication.getHttpQueues().cancelAll("refreshBalanceRequest");
        MyApplication.getHttpQueues().cancelAll("capitalFlow2");
        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll("tagPhone");
        }

    }

    @Override
    protected boolean onkeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(102);  //返回更新余额数目
            finish();
        }
        return super.onkeyDown(keyCode, event);
    }


}


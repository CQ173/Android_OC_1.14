package com.huoniao.oc.outlets;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.AdminCapitalFlowDetailsA;
import com.huoniao.oc.bean.OltsCapitalFlowBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.Keyboard;
import com.huoniao.oc.custom.PayEditText;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.PerfectInformationA;
import com.huoniao.oc.user.RegisterSuccessA;
import com.huoniao.oc.user.SettingPayPwd;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.NumberFormatUtils;
import com.huoniao.oc.util.ShowPopupWindow;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;
import com.huoniao.oc.wxapi.WXEntryActivity;

import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.huoniao.oc.R.id.tv_title;
import static com.huoniao.oc.util.NumberFormatUtils.formatDigits;
import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class CapitalFlowA extends BaseActivity implements OnClickListener {
    private static final String REFUNDMETHOD = "refundMethod";
    private ImageView iv_back, iv_dataChoice;
    private Intent intent;
    private ListView mListView;
    private PullToRefreshListView mPullRefreshListView;
    private List<OltsCapitalFlowBean> mDatas = new ArrayList<>();
    private TextView tv_tradeAmount, tv_tradeStatus, tv_startDate, tv_endDate, tv_paysysOnMonth;
    private CommonAdapter<OltsCapitalFlowBean> adapter;
    private String startDate, endDate;
    private LinearLayout layout_startDataChoice;
    private LinearLayout layout_endDataChoice;
    //    private ProgressDialog pd;
    private ShowPopupWindow showPopupWindow;
    private double payoutCount = 0;  //总收入值
    private double incomeCount = 0; //总支出值
    private TextView tv_incomeCount;   //总收入 控件
    private TextView tv_outpayCount;   //总支出控件
    private TextView tv_refund;   //显示退款按钮
    private ImageView iv_imgRefund;
    private String startMonth;
    private String endMonth;
    private VolleyNetCommon volleyNetCommon;
    private PayEditText pwEditText;
    private MyPopWindow myPopWindow;
    private TextView tv_promptContent;
    private TextView tv_refundTitle;
    private LinearLayout layout_pwArea;
    private LinearLayout layout_cofirmArea;
    private TextView tv_confirm;
    private int pageNumber = 1;
    private boolean refreshClose = true;
    private boolean datePickerDialogFlag = true;
    private User user;
    private String roleName;
    private EditText et_queryType;
    private String queryType = "";
    private TextView tv_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlets_capitalflow);
        initView();
        initData();

    }

    final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "<<", "0", "完成"
    };

    @SuppressWarnings("unchecked")
    private void initData() {
        DateUtils dateUtils = new DateUtils();
        startMonth = dateUtils.getTime();
        endMonth = dateUtils.getTime();  //获取今天日期
        intent = getIntent();
//		mDatas = (List<OltsCapitalFlowBean>) intent.getSerializableExtra("capitalFlowData");
//        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        try {
            getOltCapitalFlowDataE(startMonth, endMonth,1, queryType); //默认第一页
        } catch (Exception e) {
            e.printStackTrace();
        }
//			}
//		});



        Log.d("debug", "data = " + mDatas);
//        payoutCount = intent.getDoubleExtra("payoutCount", 0.00);
//        incomeCount = intent.getDoubleExtra("incomeCount", 0.00);
//        String incomeCounts = formatDigits(incomeCount);
//        String payoutCounts = NumberFormatUtils.formatDigits(payoutCount);
//        tv_incomeCount.setText(incomeCounts + "");
//        tv_outpayCount.setText(payoutCounts + "");

		/*if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
		adapter = new CommonAdapter<OltsCapitalFlowBean>(this, mDatas, R.layout.outlets_capitalflow_item) {

			@Override
			public void convert(ViewHolder holder, OltsCapitalFlowBean cptFlowBean) {

				if (!"".equals(cptFlowBean) || cptFlowBean != null) {
				holder.setText(R.id.tv_date, cptFlowBean.getTradeDate())
					  .setText(R.id.tv_tradeName, cptFlowBean.getTradeName());
                  final String channelType = cptFlowBean.getChannelType(); //交易渠道
				  String  expireTime = cptFlowBean.getExpireTime();  //超时时间

					tv_refund = holder.getView(R.id.tv_refund);
					tv_refund.setTag(cptFlowBean.getFlowId());
					iv_imgRefund = holder.getView(R.id.iv_img_refund); //本地图片
					final String flowId = cptFlowBean.getFlowId();
					tv_refund.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {

							try {
								final JSONObject jsonRequest = new JSONObject();
								jsonRequest.put("flowId",flowId);
//								showPopupWindow = new ShowPopupWindow();
//								showPopupWindow.showPopuWindowRefund(view,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//								showPopupWindow.setPopRefundListener(new ShowPopupWindow.PopRefund() {
//									@Override
//									public void confirm(Object o) {
//										String capitalPassword = o.toString();
//										try {
//											jsonRequest.put("payPassword",capitalPassword);
//										} catch (JSONException e) {
//											e.printStackTrace();
//										}
//										if(Define.ACCT_TRADE_CHAN_TYPE_ALI_QR.equals(channelType)) {
//											refundMethod(jsonRequest, Define.ALIREFUND,1);  //支付宝扫码退款
//										}else if(Define.ACCT_TRADE_CHAN_TYPE_WE_QR.equals(channelType)) {
//                                            refundMethod(jsonRequest,Define.WECHATREFUND,2); //微信扫码退款
//										}
//								}
//
//									@Override
//									public void cancle(Object o) {
//
//									}
//								});

//								ThreadCommonUtils.runonuiThread(new Runnable() {
//									@Override
//									public void run() {
								//设置键盘
//当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
// Toast.makeText(MyApplication.mContext, "您的密码是：" + payEditText.getText(), Toast.LENGTH_SHORT).show();
*//**
         * 当密码输入完成时的回调
         *//* // Toast.makeText(MyApplication.mContext, "您的密码是：" + password, Toast.LENGTH_SHORT).show();
//支付宝扫码退款
//微信扫码退款
								myPopWindow = new MyPopAbstract() {
                                    @Override
                                    protected void setMapSettingViewWidget(View view) {
                                        ImageView iv_deletepwd = (ImageView) view.findViewById(R.id.iv_deletepwd);
                                        pwEditText = (PayEditText) view.findViewById(R.id.PayEditText_pay);
//										tv_promptContent = (TextView) view.findViewById(R.id.tv_promptContent);
										tv_refundTitle = (TextView) view.findViewById(tv_title);
//										layout_pwArea = (LinearLayout) view.findViewById(R.id.layout_pwArea);
//										layout_cofirmArea = (LinearLayout) view.findViewById(R.id.layout_cofirmArea);
//										tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                                        Keyboard keyboardView_pay = (Keyboard) view.findViewById(R.id.KeyboardView_pay);
                                        iv_deletepwd.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                myPopWindow.dissmiss();
                                            }
                                        });
                                        //设置键盘
                                        keyboardView_pay.setKeyboardKeys(KEY);

                                        keyboardView_pay.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
                                            @Override
                                            public void onKeyClick(int position, String value) {
                                                if (position < 11 && position != 9) {
                                                    pwEditText.add(value);
                                                } else if (position == 9) {
                                                    pwEditText.remove();
                                                }else if (position == 11) {
                                                    //当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
                                                    // Toast.makeText(MyApplication.mContext, "您的密码是：" + payEditText.getText(), Toast.LENGTH_SHORT).show();
                                                    if(pwEditText.getText().toString().trim().length()<6){
                                                        Toast.makeText(MyApplication.mContext, "密码长度不够，请继续输入。", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }

													String password = pwEditText.getText().toString();
													try {
														jsonRequest.put("payPassword",password);
													} catch (JSONException e) {
														e.printStackTrace();
													}

													if(Define.ACCT_TRADE_CHAN_TYPE_ALI_QR.equals(channelType)) {
														refundMethod(jsonRequest, Define.ALIREFUND,1);  //支付宝扫码退款
													}else if(Define.ACCT_TRADE_CHAN_TYPE_WE_QR.equals(channelType)) {
														refundMethod(jsonRequest,Define.WECHATREFUND,2); //微信扫码退款
													}

                                                }
                                            }
                                        });


                                        *//**
         * 当密码输入完成时的回调
         *//*
//                                        pwEditText.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
//                                            @Override
//                                            public void onInputFinished(String password) {
//                                                // Toast.makeText(MyApplication.mContext, "您的密码是：" + password, Toast.LENGTH_SHORT).show();
//                                                try {
//                                                    jsonRequest.put("payPassword",password);
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                                if(Define.ACCT_TRADE_CHAN_TYPE_ALI_QR.equals(channelType)) {
//                                                    refundMethod(jsonRequest, Define.ALIREFUND,1);  //支付宝扫码退款
//                                                }else if(Define.ACCT_TRADE_CHAN_TYPE_WE_QR.equals(channelType)) {
//                                                    refundMethod(jsonRequest,Define.WECHATREFUND,2); //微信扫码退款
//                                                    }
//
//                                            }
//                                        });

                                    }

                                    @Override
                                    protected int layout() {
                                        return R.layout.refund_textpwdpop;
                                    }
                                }.poPwindow(CapitalFlowA.this)
                                        .showAtLocation(iv_back, Gravity.CENTER,0, 0);
//									}
//								});



							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					});
					tv_tradeAmount = holder.getView(R.id.tv_tradeAmount);
					tv_tradeStatus = holder.getView(R.id.tv_tradeStatus);
				String tradeType = cptFlowBean.getType();
				String tradeAmountStr = cptFlowBean.getTradeFee();
				if (!tradeAmountStr.isEmpty() && tradeAmountStr != null) {
					Double tradeAmount = Double.valueOf(tradeAmountStr);

				if (Define.TRADE_TYPE1.equals(tradeType)) {

					tv_tradeAmount.setText("+" + String.format("%.2f", tradeAmount));
				}else {
//					tv_tradeAmount.setText(cptFlowBean.getTradeFee());
					tv_tradeAmount.setText(String.format("%.2f", tradeAmount));
				}
			}


					//判断设置 图片
					if(Define.INCOME.equals(cptFlowBean.getType())){
						Glide.with(CapitalFlowA.this).load(R.drawable.shou).into(iv_imgRefund);
					}else{
						Glide.with(CapitalFlowA.this).load(R.drawable.kou).into(iv_imgRefund);
					}

					if (Define.ACCT_TRADE_STATUS_WAIT .equals(cptFlowBean.getTradeStatus())) {
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("等待付款");

						long etimes = 0; //表示只有在规定时间内才可以 完成显示退款功能
						try {
					 String formentHaoMiao =cptFlowBean.getExpireTime();
							if(formentHaoMiao!=null){
								etimes = DateUtils.dataFormentHaoMiao(cptFlowBean.getExpireTime());
								if(DateUtils.getSystemDataHaoMiao()>etimes){  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
									tv_tradeStatus.setText("交易关闭");
									tv_refund.setVisibility(View.GONE);
								}
							}


						} catch (ParseException e) {
							e.printStackTrace();
						}

					}else if(Define.ACCT_TRADE_STATUS_SUSPENSE .equals(cptFlowBean.getTradeStatus())){
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("等待处理");
					}else if(Define.ACCT_TRADE_STATUS_FAIL .equals(cptFlowBean.getTradeStatus())){
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("交易失败");
					}else if(Define.ACCT_TRADE_STATUS_CLOSED .equals(cptFlowBean.getTradeStatus())){
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("交易关闭");
					}else if (Define.ACCT_TRADE_STATUS_PENDING .equals(cptFlowBean.getTradeStatus())) {
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("等待收款");

					}else if (Define.ACCT_TRADE_STATUS_SUCCESS .equals(cptFlowBean.getTradeStatus())) { //表示支付成功的
					              //如果超时未处理 不显示 退款按钮
					                //如果是支付宝扫码支付 或者 微信扫码支付  就显示退款按钮   否则不显示退款
					                if(Define.ACCT_TRADE_CHAN_TYPE_ALI_QR.equals(cptFlowBean.getChannelType())  //表示 支付成功 按照  扫码的才可以退款的
 									   || Define.ACCT_TRADE_CHAN_TYPE_WE_QR.equals(cptFlowBean.getChannelType())
									   ) {
										try {
											long etimes = DateUtils.dataFormentHaoMiao(cptFlowBean.getExpireTime()); //表示只有在规定时间内才可以 完成显示退款功能
											if(DateUtils.getSystemDataHaoMiao()<etimes){  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
												if(tv_refund.getTag().equals(cptFlowBean.getFlowId())){
												tv_refund.setVisibility(View.VISIBLE); //屏蔽退款按钮
												tv_refund.setText("退款");
													tv_refund.setTextColor(getResources().getColor(R.color.blueCash));
												tv_refund.setEnabled(true);
												tv_refund.setBackgroundResource(R.drawable.text_round_ring);
											}
											}
										} catch (ParseException e) {
											e.printStackTrace();
										}
							   }else{
								   tv_refund.setVisibility(View.GONE);
							   }
						tv_tradeStatus.setText("交易成功");
					}else if (Define.ACCT_TRADE_STATUS_FINISHED .equals(cptFlowBean.getTradeStatus())) {
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("交易结束");
					}else if (Define.ACCT_TRADE_STATUS_REFUNDING.equals(cptFlowBean.getTradeStatus())) {

						tv_tradeStatus.setText("交易成功"); //退款中
						setRefundState(View.VISIBLE,false,R.color.yellow,"退款中",R.color.white);

					}else if (Define.ACCT_TRADE_STATUS_REFUND.equals(cptFlowBean.getTradeStatus())) {
						tv_tradeStatus.setText("交易成功");  // 本来状态改为  已退款  现在把已退款放到按钮上
						setRefundState(View.VISIBLE,false,R.color.grayss,"已退款",R.color.white);
					} else if(Define.ACC_TRADE_STATUS_REFUND_FAIL.equals(cptFlowBean.getTradeStatus())){
						tv_tradeStatus.setText("交易失败");
						setRefundState(View.VISIBLE,false,R.color.colorAccent,"退款失败",R.color.white);
					}
				}
		}

	};
		}else if (Define.SYSTEM_MANAG_USER.equals(LoginA.IDENTITY_TAG) || Define.SYSTEM_MANAG_USER.equals(PerfectInformationA.IDENTITY_TAG)
					|| Define.SYSTEM_MANAG_USER.equals(WXEntryActivity.IDENTITY_TAG) || Define.SYSTEM_MANAG_USER.equals(RegisterSuccessA.IDENTITY_TAG)) {
			adapter = new CommonAdapter<OltsCapitalFlowBean>(this, mDatas, R.layout.admin_capitalflow_item) {

				@Override
				public void convert(ViewHolder holder, OltsCapitalFlowBean cptFlowBean) {

					if (!"".equals(cptFlowBean) || cptFlowBean != null) {
					holder.setText(R.id.tv_date, cptFlowBean.getTradeDate())
						  .setText(R.id.tv_name, cptFlowBean.getName())
						  .setText(R.id.tv_tradeName, cptFlowBean.getTradeName());


					tv_tradeAmount = holder.getView(R.id.tv_tradeAmount);

		       	TextView tv_refund = holder.getView(R.id.tv_refund);

						tv_refund.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(mContext, "aa", Toast.LENGTH_SHORT).show();
									}
								});
							}
						});
						String tradeType = cptFlowBean.getType();
					Double tradeAmount = Double.valueOf(cptFlowBean.getTradeFee());
					if (Define.TRADE_TYPE1.equals(tradeType)) {
						tv_tradeAmount.setText("+" + String.format("%.2f", tradeAmount));
					}else {
						tv_tradeAmount.setText(String.format("%.2f", tradeAmount));
					}

					tv_tradeStatus = holder.getView(R.id.tv_tradeStatus);
					if (Define.TRADE_STATUS1.equals(cptFlowBean.getTradeStatus())) {
						tv_tradeStatus.setText("交易成功");
						tv_refund.setVisibility(View.VISIBLE);
					}else if(Define.TRADE_STATUS2.equals(cptFlowBean.getTradeStatus())){
						tv_tradeStatus.setText("等待付款");
						tv_refund.setVisibility(View.GONE);
					}else if(Define.TRADE_STATUS3.equals(cptFlowBean.getTradeStatus())){
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("交易结束");
					}else if(Define.TRADE_STATUS4.equals(cptFlowBean.getTradeStatus())){
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("交易关闭");
					}else if (Define.TRADE_STATUS5.equals(cptFlowBean.getTradeStatus())) {
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("等待处理");
					}else if (Define.TRADE_STATUS6.equals(cptFlowBean.getTradeStatus())) {
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("等待收款");
					}else if (Define.TRADE_STATUS7.equals(cptFlowBean.getTradeStatus())) {
						tv_refund.setVisibility(View.GONE);
						tv_tradeStatus.setText("交易失败");
					}
				}
			}

		};

		}
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				OltsCapitalFlowBean cptFlowBean = mDatas.get(position);

				if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
						|| Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
				intent = new Intent(CapitalFlowA.this, CapitalFlowDetailsA.class);
				intent.putExtra("tradeDate", cptFlowBean.getTradeDate());
				intent.putExtra("flowId", cptFlowBean.getFlowId());
				intent.putExtra("tradeName", cptFlowBean.getTradeName());
//				intent.putExtra("tradeAcct", cptFlowBean.getTradeAcct());
				intent.putExtra("tradeFee", cptFlowBean.getTradeFee());
				intent.putExtra("type", cptFlowBean.getType());
				intent.putExtra("tradeStatus", cptFlowBean.getTradeStatus());
				 intent.putExtra("expireTime",cptFlowBean.getExpireTime()); //超时时间
				 startActivity(intent);
				 overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				}else if (Define.SYSTEM_MANAG_USER.equals(LoginA.IDENTITY_TAG) || Define.SYSTEM_MANAG_USER.equals(PerfectInformationA.IDENTITY_TAG)
							|| Define.SYSTEM_MANAG_USER.equals(WXEntryActivity.IDENTITY_TAG) || Define.SYSTEM_MANAG_USER.equals(RegisterSuccessA.IDENTITY_TAG)) {
					intent = new Intent(CapitalFlowA.this, AdminCapitalFlowDetailsA.class);
					intent.putExtra("loginName", cptFlowBean.getLoginName());
					intent.putExtra("name", cptFlowBean.getName());
					intent.putExtra("balance", cptFlowBean.getCurBalance());
					intent.putExtra("tradeDate", cptFlowBean.getTradeDate());
					intent.putExtra("flowId", cptFlowBean.getFlowId());
					intent.putExtra("tradeName", cptFlowBean.getTradeName());
					intent.putExtra("tradeAcct", cptFlowBean.getTradeAcct());
					intent.putExtra("tradeFee", cptFlowBean.getTradeFee());
					intent.putExtra("type", cptFlowBean.getType());
					intent.putExtra("tradeStatus", cptFlowBean.getTradeStatus());
					startActivity(intent);
					overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				}
			}
		});*/

    }


    //设置退款按钮状态
    public void setRefundState(int visibility, boolean enable, int color, String contentState, int background) {
        tv_refund.setVisibility(visibility);
        tv_refund.setEnabled(enable);
        tv_refund.setBackgroundColor(getResources().getColor(background));
        tv_refund.setText(contentState);
        tv_refund.setTextColor(getResources().getColor(color));
    }


    /**
     * //退款
     *
     * @param jsonRequest
     * @param url         传入之前做判断 是支付宝扫码退款还是 微信扫码退款
     */
    private void refundMethod(final JSONObject jsonRequest, final String url, final int i) {
        cpd.show();

        ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    volleyNetCommon = new VolleyNetCommon();
                    final JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Method.POST, url, jsonRequest, new VolleyAbstract(CapitalFlowA.this) {
                        @Override
                        protected void netVolleyResponese(JSONObject json) {
//							tv_refundTitle.setText("退款成功");
//							layout_pwArea.setVisibility(View.GONE);
//							tv_promptContent.setVisibility(View.VISIBLE);
//							layout_cofirmArea.setVisibility(View.VISIBLE);
                            myPopWindow.dissmiss();

                            myPopWindow = new MyPopAbstract() {
                                @Override
                                protected void setMapSettingViewWidget(View view) {
                                    ImageView iv_deletepop = (ImageView) view.findViewById(R.id.iv_deletepop);
                                    tv_promptContent = (TextView) view.findViewById(R.id.tv_promptContent);
                                    tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);

                                    iv_deletepop.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myPopWindow.dissmiss();
                                        }
                                    });

                                }

                                @Override
                                protected int layout() {
                                    return R.layout.refundconfirm_pop;
                                }
                            }.poPwindow(CapitalFlowA.this,true).showAtLocation(iv_back, Gravity.CENTER, 0, 0);

                            //TODO   对相应的状态进行修改
                            if (i == 1) {
                                tv_promptContent.setText(" 退款申请成功，请关注支付宝的退款信息！");
                            } else if (i == 2) {
                                tv_promptContent.setText(" 退款申请成功，请关注微信的退款信息！");
                            }
                            tv_confirm.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    myPopWindow.dissmiss();
                                }
                            });

                            try {
                                if(mDatas != null){
                                    mDatas.clear();
                                }
                                getOltCapitalFlowDataE(startMonth, endMonth,1, queryType);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
//							PopupWindow refundPopWindow = showPopupWindow.getRefundPopWindow();
//							refundPopWindow.dismiss();
                            myPopWindow.dissmiss();
                        }

                        @Override
                        public void volleyError(VolleyError volleyError) {
                            Toast.makeText(CapitalFlowA.this,R.string.netError, Toast.LENGTH_SHORT).show();
                            showPopupWindow.setTvShang("网络错误，退款失败。");
                        }
                    }, REFUNDMETHOD, true);

                    volleyNetCommon.addQueue(jsonObjectRequest);  //添加到队列

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        user = (User) readObject(CapitalFlowA.this, "loginResult");
        roleName = user.getRoleName();
        iv_back = (ImageView) findViewById(R.id.iv_back);
//		iv_dataChoice = (ImageView) findViewById(R.id.iv_dataChoice);
        tv_startDate = (TextView) findViewById(R.id.tv_startDate);
        tv_endDate = (TextView) findViewById(R.id.tv_endDate);
        layout_startDataChoice = (LinearLayout) findViewById(R.id.layout_startDataChoice);
        layout_endDataChoice = (LinearLayout) findViewById(R.id.layout_endDataChoice);
        et_queryType = (EditText) findViewById(R.id.et_queryType);
        tv_query = (TextView) findViewById(R.id.tv_query);
        //总收入
        tv_incomeCount = (TextView) findViewById(R.id.tv_income_count);
        tv_outpayCount = (TextView) findViewById(R.id.tv_outpay_count);
//		tv_paysysOnMonth = (TextView) findViewById(R.id.tv_paysysOnMonth);
//        pd = new CustomProgressDialog(this, "正在加载中...", R.anim.frame_anim);
//        pd.setCancelable(false);//设置进度条不可以按退回键取消
//        pd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        tv_startDate.setText("(已选择" + sdf.format(new java.util.Date()) + "-");
        tv_endDate.setText(sdf.format(new java.util.Date()) + ")");
//		SimpleDateFormat sdf2 = new SimpleDateFormat("MM月");
//		tv_paysysOnMonth.setText(sdf2.format(new java.util.Date()));
//		mListView = (ListView) findViewById(R.id.capitalFlowListView);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.capitalFlowListView);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");


        // Set a listener to be invoked when the list should be refreshed.
//        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = android.text.format.DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
//                        android.text.format.DateUtils.FORMAT_SHOW_TIME | android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_ABBREV_ALL);
//
//                // Update the LastUpdatedLabel
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

        // Do work to refresh the list here.
//				new GetDataTask().execute();
//            }
//        });


        // Add an end-of-list listener


        mListView = mPullRefreshListView.getRefreshableView();

        iv_back.setOnClickListener(this);
//		iv_dataChoice.setOnClickListener(this);
        layout_startDataChoice.setOnClickListener(this);
        layout_endDataChoice.setOnClickListener(this);
        tv_query.setOnClickListener(this);
    }


    //获取退款后更新后的数据
    @Override
    protected void getDataOltCapitalFlow(List<OltsCapitalFlowBean> capitalFlowData, final int nextPage) {
        super.getDataOltCapitalFlow(capitalFlowData, nextPage);
        if(nextPage == 1){
            mDatas.clear();
        }else if (nextPage == -1){
            // mDatas.clear();
            if(adapter != null) {
                //  mListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            refreshClose = false;
        }
        mPullRefreshListView.onRefreshComplete();
        if (capitalFlowData != null) {
            //	mDatas.clear();
            //	mDatas.addAll(capitalFlowData);
			/*mListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();*/
            int dataSize = mDatas.size();
            mDatas.addAll(capitalFlowData);
            if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                    || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
                if (adapter == null) {
                    adapter = new CommonAdapter<OltsCapitalFlowBean>(this, mDatas, R.layout.outlets_capitalflow_item) {

                        @Override
                        public void convert(ViewHolder holder, OltsCapitalFlowBean cptFlowBean) {

                            if (!"".equals(cptFlowBean) || cptFlowBean != null) {
                                holder.setText(R.id.tv_date, cptFlowBean.getTradeDate())
                                        .setText(R.id.tv_tradeName, cptFlowBean.getTradeName());
                                final String channelType = cptFlowBean.getChannelType(); //交易渠道
                                String expireTime = cptFlowBean.getExpireTime();  //超时时间

                                tv_refund = holder.getView(R.id.tv_refund);
                                tv_refund.setTag(cptFlowBean.getFlowId());
                                iv_imgRefund = holder.getView(R.id.iv_img_refund); //本地图片
                                final String flowId = cptFlowBean.getFlowId();
                                tv_refund.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        try {
                                            final JSONObject jsonRequest = new JSONObject();
                                            jsonRequest.put("flowId", flowId);

                                            myPopWindow = new MyPopAbstract() {
                                                @Override
                                                protected void setMapSettingViewWidget(View view) {
                                                    ImageView iv_deletepwd = (ImageView) view.findViewById(R.id.iv_deletepwd);
                                                    pwEditText = (PayEditText) view.findViewById(R.id.PayEditText_pay);
//										tv_promptContent = (TextView) view.findViewById(R.id.tv_promptContent);
                                                    tv_refundTitle = (TextView) view.findViewById(tv_title);
                                                    TextView tv_paypwd = (TextView) view.findViewById(R.id.tv_paypwd);  //设置支付密码
                                                    if (MyApplication.payPasswordIsEmpty) { //如果等于true 表示没有设置过密码这个时候就要显示出来
                                                        tv_paypwd.setVisibility(View.VISIBLE);
                                                        tv_paypwd.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                ThreadCommonUtils.runonuiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        MyApplication.mContext.startActivity(new Intent(MyApplication.mContext, SettingPayPwd.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //跳转设置支付密码
                                                                        myPopWindow.dissmiss();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
//										layout_pwArea = (LinearLayout) view.findViewById(R.id.layout_pwArea);
//										layout_cofirmArea = (LinearLayout) view.findViewById(R.id.layout_cofirmArea);
//										tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                                                    Keyboard keyboardView_pay = (Keyboard) view.findViewById(R.id.KeyboardView_pay);
                                                    iv_deletepwd.setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            myPopWindow.dissmiss();
                                                        }
                                                    });
                                                    //设置键盘
                                                    keyboardView_pay.setKeyboardKeys(KEY);

                                                    keyboardView_pay.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
                                                        @Override
                                                        public void onKeyClick(int position, String value) {
                                                            if (position < 11 && position != 9) {
                                                                pwEditText.add(value);
                                                            } else if (position == 9) {
                                                                pwEditText.remove();
                                                            } else if (position == 11) {
                                                                //当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
                                                                // Toast.makeText(MyApplication.mContext, "您的密码是：" + payEditText.getText(), Toast.LENGTH_SHORT).show();
                                                                if (pwEditText.getText().toString().trim().length() < 6) {
                                                                    Toast.makeText(MyApplication.mContext, "密码长度不够，请继续输入。", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }

                                                                String password = pwEditText.getText().toString();
                                                                try {
                                                                    jsonRequest.put("payPassword", password);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                if (Define.ACCT_TRADE_CHAN_TYPE_ALI_QR.equals(channelType) || Define.ACCT_TRADE_CHAN_TYPE_ALI_XFT.equals(channelType)) {
                                                                    refundMethod(jsonRequest, Define.ALIREFUND, 1);  //支付宝扫码退款
                                                                } else if (Define.ACCT_TRADE_CHAN_TYPE_WE_QR.equals(channelType) || Define.ACCT_TRADE_CHAN_TYPE_WE_XFT.equals(channelType)) {
                                                                    refundMethod(jsonRequest, Define.WECHATREFUND, 2); //微信扫码退款
                                                                }

                                                            }
                                                        }
                                                    });


                                                }

                                                @Override
                                                protected int layout() {
                                                    return R.layout.refund_textpwdpop;
                                                }
                                            }.poPwindow(CapitalFlowA.this,true)
                                                    .showAtLocation(iv_back, Gravity.CENTER, 0, 0);
//									}
//								});


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                tv_tradeAmount = holder.getView(R.id.tv_tradeAmount);
                                tv_tradeStatus = holder.getView(R.id.tv_tradeStatus);
                                String tradeType = cptFlowBean.getType();
                                String tradeAmountStr = cptFlowBean.getTradeFee();
                                if (!tradeAmountStr.isEmpty() && tradeAmountStr != null) {
                                    Double tradeAmount = Double.valueOf(tradeAmountStr);

                                    if (Define.TRADE_TYPE1.equals(tradeType)) {

                                        tv_tradeAmount.setText("+" + String.format("%.2f", tradeAmount));
                                    } else {
//					tv_tradeAmount.setText(cptFlowBean.getTradeFee());
                                        tv_tradeAmount.setText(String.format("%.2f", tradeAmount));
                                    }
                                }


                                //判断设置 图片
                                if (Define.INCOME.equals(cptFlowBean.getType())) {
                                    try {
                                        Glide.with(CapitalFlowA.this).load(R.drawable.shou).into(iv_imgRefund);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Glide.with(CapitalFlowA.this).load(R.drawable.kou).into(iv_imgRefund);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (Define.ACCT_TRADE_STATUS_WAIT.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("等待付款");

                                    long etimes = 0; //表示只有在规定时间内才可以 完成显示退款功能
                                    try {
                                        String formentHaoMiao = cptFlowBean.getExpireTime();
                                        if (formentHaoMiao != null) {
                                            etimes = DateUtils.dataFormentHaoMiao(cptFlowBean.getExpireTime());
                                            if (DateUtils.getSystemDataHaoMiao() > etimes) {  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
                                                tv_tradeStatus.setText("交易关闭");
                                                setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                            }
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (Define.ACCT_TRADE_STATUS_SUSPENSE.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("等待处理");
                                } else if (Define.ACCT_TRADE_STATUS_FAIL.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("交易失败");
                                } else if (Define.ACCT_TRADE_STATUS_CLOSED.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("交易关闭");
                                } else if (Define.ACCT_TRADE_STATUS_PENDING.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("等待收款");

                                } else if (Define.ACCT_TRADE_STATUS_SUCCESS.equals(cptFlowBean.getTradeStatus())) { //表示支付成功的
                                    //如果超时未处理 不显示 退款按钮
                                    //如果是支付宝扫码支付 或者 微信扫码支付  就显示退款按钮   否则不显示退款
                                    if (Define.ACCT_TRADE_CHAN_TYPE_ALI_QR.equals(cptFlowBean.getChannelType())  //表示 支付成功 按照  扫码的才可以退款的
                                            || Define.ACCT_TRADE_CHAN_TYPE_WE_QR.equals(cptFlowBean.getChannelType())
                                                || Define.ACCT_TRADE_CHAN_TYPE_ALI_XFT.equals(cptFlowBean.getChannelType())
                                                    ||Define.ACCT_TRADE_CHAN_TYPE_WE_XFT.equals(cptFlowBean.getChannelType())) {
                                        try {
                                            long etimes = DateUtils.dataFormentHaoMiao(cptFlowBean.getExpireTime()); //表示只有在规定时间内才可以 完成显示退款功能
                                            if (DateUtils.getSystemDataHaoMiao() < etimes) {  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
                                                if (tv_refund.getTag().equals(cptFlowBean.getFlowId())) {
                                                    tv_refund.setVisibility(View.VISIBLE); //屏蔽退款按钮
                                                    tv_refund.setText("退款");
                                                    tv_refund.setTextColor(getResources().getColor(R.color.blueCash));
                                                    tv_refund.setEnabled(true);
                                                    tv_refund.setBackgroundResource(R.drawable.text_round_ring);
                                                }else {
                                                    tv_refund.setVisibility(View.GONE);
                                                }
                                            }else {
                                                setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    }
                                    tv_tradeStatus.setText("交易成功");
                                } else if (Define.ACCT_TRADE_STATUS_FINISHED.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("交易结束");
                                } else if (Define.ACCT_TRADE_STATUS_REFUNDING.equals(cptFlowBean.getTradeStatus())) {

                                    tv_tradeStatus.setText("退款中"); //退款中
                                    tv_tradeStatus.setTextColor(Color.rgb(244,176,69));
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
//                                    setRefundState(View.VISIBLE, false, R.color.yellow, "退款中", R.color.white);

                                } else if (Define.ACCT_TRADE_STATUS_REFUND.equals(cptFlowBean.getTradeStatus())) {
                                    tv_tradeStatus.setText("已退款");
                                    tv_tradeStatus.setTextColor(Color.rgb(63,184,48));
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());// 本来状态改为  已退款  现在把已退款放到按钮上
//                                    setRefundState(View.VISIBLE, false, R.color.grayss, "已退款", R.color.white);
                                } else if (Define.ACC_TRADE_STATUS_REFUND_FAIL.equals(cptFlowBean.getTradeStatus())) {
                                    tv_tradeStatus.setText("退款失败");
                                    tv_tradeStatus.setTextColor(Color.rgb(255,64,129));
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
//                                    setRefundState(View.VISIBLE, false, R.color.colorAccent, "退款失败", R.color.white);
                                }
                            }
                        }

                    };
                    mListView.setAdapter(adapter);
                }
            }else if (roleName.contains("系统管理员")) {

                if (adapter == null) {
                    adapter = new CommonAdapter<OltsCapitalFlowBean>(this, mDatas, R.layout.outlets_capitalflow_item) {

                        @Override
                        public void convert(ViewHolder holder, OltsCapitalFlowBean cptFlowBean) {

                            if (!"".equals(cptFlowBean) || cptFlowBean != null) {
                                holder.setText(R.id.tv_date, cptFlowBean.getTradeDate())
                                        .setText(R.id.tv_tradeName, cptFlowBean.getTradeName());
                                final String channelType = cptFlowBean.getChannelType(); //交易渠道
                                String expireTime = cptFlowBean.getExpireTime();  //超时时间

                                tv_refund = holder.getView(R.id.tv_refund);
                                tv_refund.setTag(cptFlowBean.getFlowId());
                                iv_imgRefund = holder.getView(R.id.iv_img_refund); //本地图片
                                final String flowId = cptFlowBean.getFlowId();
                                tv_refund.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        try {
                                            final JSONObject jsonRequest = new JSONObject();
                                            jsonRequest.put("flowId", flowId);

                                            myPopWindow = new MyPopAbstract() {
                                                @Override
                                                protected void setMapSettingViewWidget(View view) {
                                                    ImageView iv_deletepwd = (ImageView) view.findViewById(R.id.iv_deletepwd);
                                                    pwEditText = (PayEditText) view.findViewById(R.id.PayEditText_pay);
//										tv_promptContent = (TextView) view.findViewById(R.id.tv_promptContent);
                                                    tv_refundTitle = (TextView) view.findViewById(tv_title);
                                                    TextView tv_paypwd = (TextView) findViewById(R.id.tv_paypwd);  //设置支付密码
                                                    if (MyApplication.payPasswordIsEmpty) { //如果等于true 表示没有设置过密码这个时候就要显示出来
                                                        tv_paypwd.setVisibility(View.VISIBLE);
                                                        tv_paypwd.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                ThreadCommonUtils.runonuiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        MyApplication.mContext.startActivity(new Intent(MyApplication.mContext, SettingPayPwd.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //跳转设置支付密码
                                                                        myPopWindow.dissmiss();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
//										layout_pwArea = (LinearLayout) view.findViewById(R.id.layout_pwArea);
//										layout_cofirmArea = (LinearLayout) view.findViewById(R.id.layout_cofirmArea);
//										tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
                                                    Keyboard keyboardView_pay = (Keyboard) view.findViewById(R.id.KeyboardView_pay);
                                                    iv_deletepwd.setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            myPopWindow.dissmiss();
                                                        }
                                                    });
                                                    //设置键盘
                                                    keyboardView_pay.setKeyboardKeys(KEY);

                                                    keyboardView_pay.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
                                                        @Override
                                                        public void onKeyClick(int position, String value) {
                                                            if (position < 11 && position != 9) {
                                                                pwEditText.add(value);
                                                            } else if (position == 9) {
                                                                pwEditText.remove();
                                                            } else if (position == 11) {
                                                                //当点击完成的时候，也可以通过payEditText.getText()获取密码，此时不应该注册OnInputFinishedListener接口
                                                                // Toast.makeText(MyApplication.mContext, "您的密码是：" + payEditText.getText(), Toast.LENGTH_SHORT).show();
                                                                if (pwEditText.getText().toString().trim().length() < 6) {
                                                                    Toast.makeText(MyApplication.mContext, "密码长度不够，请继续输入。", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }

                                                                String password = pwEditText.getText().toString();
                                                                try {
                                                                    jsonRequest.put("payPassword", password);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                if (Define.ACCT_TRADE_CHAN_TYPE_ALI_QR.equals(channelType) || Define.ACCT_TRADE_CHAN_TYPE_ALI_XFT.equals(channelType)) {
                                                                    refundMethod(jsonRequest, Define.ALIREFUND, 1);  //支付宝扫码退款
                                                                } else if (Define.ACCT_TRADE_CHAN_TYPE_WE_QR.equals(channelType) || Define.ACCT_TRADE_CHAN_TYPE_WE_XFT.equals(channelType)) {
                                                                    refundMethod(jsonRequest, Define.WECHATREFUND, 2); //微信扫码退款
                                                                }

                                                            }
                                                        }
                                                    });


                                                }

                                                @Override
                                                protected int layout() {
                                                    return R.layout.refund_textpwdpop;
                                                }
                                            }.poPwindow(CapitalFlowA.this,true)
                                                    .showAtLocation(iv_back, Gravity.CENTER, 0, 0);
//									}
//								});


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                tv_tradeAmount = holder.getView(R.id.tv_tradeAmount);
                                tv_tradeStatus = holder.getView(R.id.tv_tradeStatus);
                                String tradeType = cptFlowBean.getType();
                                String tradeAmountStr = cptFlowBean.getTradeFee();
                                if (!tradeAmountStr.isEmpty() && tradeAmountStr != null) {
                                    Double tradeAmount = Double.valueOf(tradeAmountStr);

                                    if (Define.TRADE_TYPE1.equals(tradeType)) {

                                        tv_tradeAmount.setText("+" + String.format("%.2f", tradeAmount));
                                    } else {
//					tv_tradeAmount.setText(cptFlowBean.getTradeFee());
                                        tv_tradeAmount.setText(String.format("%.2f", tradeAmount));
                                    }
                                }


                                //判断设置 图片
                                if (Define.INCOME.equals(cptFlowBean.getType())) {
                                    try {
                                        Glide.with(CapitalFlowA.this).load(R.drawable.shou).into(iv_imgRefund);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Glide.with(CapitalFlowA.this).load(R.drawable.kou).into(iv_imgRefund);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (Define.ACCT_TRADE_STATUS_WAIT.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("等待付款");

                                    long etimes = 0; //表示只有在规定时间内才可以 完成显示退款功能
                                    try {
                                        String formentHaoMiao = cptFlowBean.getExpireTime();
                                        if (formentHaoMiao != null) {
                                            etimes = DateUtils.dataFormentHaoMiao(cptFlowBean.getExpireTime());
                                            if (DateUtils.getSystemDataHaoMiao() > etimes) {  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
                                                tv_tradeStatus.setText("交易关闭");
                                                setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                            }
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else if (Define.ACCT_TRADE_STATUS_SUSPENSE.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("等待处理");
                                } else if (Define.ACCT_TRADE_STATUS_FAIL.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("交易失败");
                                } else if (Define.ACCT_TRADE_STATUS_CLOSED.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("交易关闭");
                                } else if (Define.ACCT_TRADE_STATUS_PENDING.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("等待收款");

                                } else if (Define.ACCT_TRADE_STATUS_SUCCESS.equals(cptFlowBean.getTradeStatus())) { //表示支付成功的
                                    //如果超时未处理 不显示 退款按钮
                                    //如果是支付宝扫码支付 或者 微信扫码支付  就显示退款按钮   否则不显示退款
                                    if (Define.ACCT_TRADE_CHAN_TYPE_ALI_QR.equals(cptFlowBean.getChannelType())  //表示 支付成功 按照  扫码的才可以退款的
                                            || Define.ACCT_TRADE_CHAN_TYPE_WE_QR.equals(cptFlowBean.getChannelType())
                                                || Define.ACCT_TRADE_CHAN_TYPE_ALI_XFT.equals(cptFlowBean.getChannelType())
                                                    || Define.ACCT_TRADE_CHAN_TYPE_WE_XFT.equals(cptFlowBean.getChannelType())) {
                                        try {
                                            long etimes = DateUtils.dataFormentHaoMiao(cptFlowBean.getExpireTime()); //表示只有在规定时间内才可以 完成显示退款功能
                                            if (DateUtils.getSystemDataHaoMiao() < etimes) {  //如果当前系统时间小于 传过来的超时时间 表示 按照预约时间内可以操作
                                                if (tv_refund.getTag().equals(cptFlowBean.getFlowId())) {
                                                    tv_refund.setVisibility(View.GONE); //屏蔽退款按钮
                                                    tv_refund.setText("退款");
                                                    tv_refund.setTextColor(getResources().getColor(R.color.blueCash));
                                                    tv_refund.setEnabled(true);
                                                    tv_refund.setBackgroundResource(R.drawable.text_round_ring);
                                                }else {
                                                    tv_refund.setVisibility(View.GONE);
                                                }
                                            }else{
                                                setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    }
                                    tv_tradeStatus.setText("交易成功");
                                } else if (Define.ACCT_TRADE_STATUS_FINISHED.equals(cptFlowBean.getTradeStatus())) {
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
                                    tv_tradeStatus.setText("交易结束");
                                } else if (Define.ACCT_TRADE_STATUS_REFUNDING.equals(cptFlowBean.getTradeStatus())) {

                                    tv_tradeStatus.setText("退款中"); //退款中
                                    tv_tradeStatus.setTextColor(Color.rgb(244,176,69));
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
//                                    setRefundState(View.VISIBLE, false, R.color.yellow, "退款中", R.color.white);

                                } else if (Define.ACCT_TRADE_STATUS_REFUND.equals(cptFlowBean.getTradeStatus())) {
                                    tv_tradeStatus.setText("已退款");
                                    tv_tradeStatus.setTextColor(Color.rgb(63,184,48));
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());// 本来状态改为  已退款  现在把已退款放到按钮上
//                                    setRefundState(View.VISIBLE, false, R.color.grayss, "已退款", R.color.white);
                                } else if (Define.ACC_TRADE_STATUS_REFUND_FAIL.equals(cptFlowBean.getTradeStatus())) {
                                    tv_tradeStatus.setText("退款失败");
                                    tv_tradeStatus.setTextColor(Color.rgb(255,64,129));
//                                    tv_refund.setVisibility(View.GONE);
                                    setTextVisible(tv_refund, cptFlowBean.getFlowId());
//                                    setRefundState(View.VISIBLE, false, R.color.colorAccent, "退款失败", R.color.white);
                                }
                            }
                        }

                    };
                    mListView.setAdapter(adapter);
                }

            }
//            mListView.setAdapter(adapter);

//            int i = mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition() + 1;
//            if (dataSize > i){
//                mListView.setSelection(dataSize - i);
//            }
            if(nextPage == 2){
                if(mDatas.size()>0){
                    mListView.setAdapter(adapter);
                    mListView.setSelection(1);
                }
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
//            if (capitalFlowData.size() < 50){
//                refreshClose = false;
//
//            }
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OltsCapitalFlowBean cptFlowBean = mDatas.get(position-1);

                    if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
                            || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
                        intent = new Intent(CapitalFlowA.this, CapitalFlowDetailsA.class);
                        intent.putExtra("tradeDate", cptFlowBean.getTradeDate());
                        intent.putExtra("flowId", cptFlowBean.getFlowId());
                        intent.putExtra("tradeName", cptFlowBean.getTradeName());
                        intent.putExtra("tradeAcct", cptFlowBean.getTradeAcct());
                        intent.putExtra("tradeFee", cptFlowBean.getTradeFee());
                        intent.putExtra("type", cptFlowBean.getType());
                        intent.putExtra("tradeStatus", cptFlowBean.getTradeStatus());
                        intent.putExtra("expireTime",cptFlowBean.getExpireTime()); //超时时间
                        intent.putExtra("curBalanceString",cptFlowBean.getCurBalance()); //账户余额
                        intent.putExtra("updateDate",cptFlowBean.getUpdateDate()); //更新时间
                        intent.putExtra("sourceFlowId",cptFlowBean.getSourceFlowId()); //关联流水号
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }else if (roleName.contains("系统管理员")) {
                        intent = new Intent(CapitalFlowA.this, AdminCapitalFlowDetailsA.class);
                        intent.putExtra("loginName", cptFlowBean.getLoginName());
                        intent.putExtra("name", cptFlowBean.getName());
                        intent.putExtra("balance", cptFlowBean.getCurBalance());
                        intent.putExtra("tradeDate", cptFlowBean.getTradeDate());
                        intent.putExtra("flowId", cptFlowBean.getFlowId());
                        intent.putExtra("tradeName", cptFlowBean.getTradeName());
                        intent.putExtra("tradeAcct", cptFlowBean.getTradeAcct());
                        intent.putExtra("tradeFee", cptFlowBean.getTradeFee());
                        intent.putExtra("type", cptFlowBean.getType());
                        intent.putExtra("tradeStatus", cptFlowBean.getTradeStatus());
                        intent.putExtra("expireTime",cptFlowBean.getExpireTime()); //超时时间
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }
                }
            });
        }
//        adapter.notifyDataSetChanged();

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshClose) {  //如果有数据 继续刷新
                    try {

                        getOltCapitalFlowDataE(startMonth, endMonth, nextPage, queryType);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(CapitalFlowA.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    ThreadCommonUtils.runonuiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshListView.onRefreshComplete();
                        }
                    });

                }
            }
        });


    }

    //获取最新支出 收入的数据
    @Override
    protected void getIncomeCountAndPayout(double incomeCount, double payoutCount) {
        super.getIncomeCountAndPayout(incomeCount, payoutCount);
        String incomeCounts = formatDigits(incomeCount);
        String payoutCounts = NumberFormatUtils.formatDigits(payoutCount);

        tv_incomeCount.setText(incomeCounts + "");
        if (payoutCounts.contains("-")){
            String newPayoutCounts = payoutCounts.replace("-", "");
            tv_outpayCount.setText(newPayoutCounts + "");
        }else {
            tv_outpayCount.setText(payoutCounts + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_startDataChoice:
                datePickerDialogFlag = true;
                queryType = et_queryType.getText().toString().trim();
                Calendar cal = Calendar.getInstance();

                DatePickerDialog mDialog = new DatePickerDialog(CapitalFlowA.this, new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //
                        startMonth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                       /* if (Date.valueOf(startMonth).after(java.sql.Date.valueOf(endMonth))) {
                            Toast.makeText(CapitalFlowA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                            return;
                        }*/
                        tv_startDate.setText("(已选择" + year + "." + (monthOfYear + 1) + "." + dayOfMonth + "-");
//					tv_paysysOnMonth.setText((monthOfYear + 1) + "月");
                        try {

                            if (Date.valueOf(startMonth).after(java.sql.Date.valueOf(endMonth))) {
                                Toast.makeText(CapitalFlowA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //getOltFlowForMonth();
                            if(mDatas != null){
                                mDatas.clear();
                            }
                            //加这个判断是为了兼容4.1版本以下DatePickerDialog默认点击两次确定的问题
                            if(datePickerDialogFlag) {
                                getOltCapitalFlowDataE(startMonth, endMonth,1, queryType);
                                datePickerDialogFlag= false;
                            }
//                            pageNumber = 1;
                            refreshClose = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                mDialog.show();
                //隐藏不想显示的子控件，这里隐藏日控件
//				DatePicker dp = findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());  
//				if (dp != null) {  
//				    ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);  
//				}   
                /**
                 * 设置可选日期范围
                 *
                 */
//				dp = mDialog.getDatePicker();
//				//限制显示6个月的
//				dp.setMinDate(new Date().getTime() - (long)150 * 24 * 60 * 60 * 1000);
//
//				dp.setMaxDate(new Date().getTime());

                break;

            case R.id.layout_endDataChoice:
                datePickerDialogFlag = true;
                queryType = et_queryType.getText().toString().trim();
                Calendar cal2 = Calendar.getInstance();

                DatePickerDialog mDialog2 = new DatePickerDialog(CapitalFlowA.this, new OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endMonth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        tv_endDate.setText(year + "." + (monthOfYear + 1) + "." + dayOfMonth + ")");
//					tv_paysysOnMonth.setText((monthOfYear + 1) + "月");
                        try {
                            if (Date.valueOf(startMonth).after(java.sql.Date.valueOf(endMonth))) {
                                Toast.makeText(CapitalFlowA.this, "开始日期不能大于结束日期！", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //getOltFlowForMonth();
                            if(mDatas != null){
                                mDatas.clear();
                            }
                            //加这个判断是为了兼容4.1版本以下DatePickerDialog默认点击两次确定的问题
                            if(datePickerDialogFlag) {
                                getOltCapitalFlowDataE(startMonth, endMonth,1, queryType);
                                datePickerDialogFlag= false;
                            }
//                            pageNumber = 1;
                            refreshClose = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));

                mDialog2.show();
                //隐藏不想显示的子控件，这里隐藏日控件
//				DatePicker dp = findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());  
//				if (dp != null) {  
//				    ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);  
//				}   
                /**
                 * 设置可选日期范围
                 *
                 */
//				dp = mDialog.getDatePicker();
//				//限制显示6个月的
//				dp.setMinDate(new Date().getTime() - (long)150 * 24 * 60 * 60 * 1000);
//
//				dp.setMaxDate(new Date().getTime());

                break;

            case R.id.tv_query:
                queryType = et_queryType.getText().toString().trim();
                try {
                    if(mDatas != null){
                        mDatas.clear();
                    }
                    getOltCapitalFlowDataE(startMonth, endMonth,1, queryType);
                    refreshClose = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 通过遍历方法查找DatePicker里的子控件：年、月、日
     *
     * @param group
     * @return
     */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }

    public void setTextVisible(TextView tv_refund,String flowId) {
        if (tv_refund.getTag().equals(flowId))
            tv_refund.setVisibility(View.GONE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("flowForMoth");
        if (volleyNetCommon != null) {
            volleyNetCommon.getRequestQueue().cancelAll(REFUNDMETHOD);
        }
    }


}

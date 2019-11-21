package com.huoniao.oc.outlets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.BankCardBean;
import com.huoniao.oc.common.BankIcon;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.custom.MyListView;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BankCardListA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private MyListView mListView;
//	private ProgressDialog pd;
	private TextView tv_cardCode, tv_bankName, tv_cardType;
	private ImageView cardIcon;
	private Intent intent;
	private TextView tv_title; //标题
	private LinearLayout ll_add_card;
	private ScrollView scroll_view;
	private MyPopWindow myPopWindow;
	private String cardId, bankCode, cardType, cardNumber;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankcard);
		initView();
		initData();
		initWidget();
	}

	private void initWidget() {
		//setPremissionShowHideView(Premission.ACCT_USERCARD_EDIT,iv_addCard);  //是否有添加,修改,删除银卡权限
		ll_add_card.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}

	private void initData() {
		tv_title.setText("银行卡");
		try {
			getBankCardList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		mListView = (MyListView) findViewById(R.id.bankCardList);
		ll_add_card = (LinearLayout) findViewById(R.id.ll_add_card);  //添加银行卡
		scroll_view = (ScrollView) findViewById(R.id.scroll_view);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.ll_add_card: //添加加银行卡
			intent = new Intent(BankCardListA.this, TemporaryAddCardA.class);
//			intent.putExtra("operationTag", "addCard");
			startActivityForResult(intent,2);
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			break;
		default:
			break;
		}
		
	}
	
	
	/**
	 * 获取银行卡列表
	 * 
	 * @throws Exception
	 */
	private void getBankCardList() throws Exception {
//		pd = new CustomProgressDialog(BankCardListA.this, "正在加载中...", R.anim.frame_anim);
		cpd.show();
	
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("operateType", Define.BANKCARD_SELECT);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		String url = Define.URL + "acct/manageBankCard";
		
		
		SessionJsonObjectRequest refreshBankCardListRequest = new SessionJsonObjectRequest(Method.POST,
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
									String cardName = cardObj.optString("cardName");//银行名称
									String openBankName = cardObj.optString("openBankName");// 开户行名称
									String openBankAreaName = cardObj.optString("openBankAreaName");// 开户行所在地区名称
                                    String isSigned = cardObj.optString("isSigned")==null ?"0":cardObj.optString("isSigned"); //是否签约         是否签约 0未签约，1 已签约     若未传或传空值这默认为0
									String isPublic = cardObj.optString("isPublic")==null ?"0":cardObj.optString("isPublic"); //是否为对公账户    是否为对公账户  0不是， 1是   若未传或传空值这默认为0
									bankCard.setId(id);
									bankCard.setCardnumber(cardno);
									bankCard.setCardType(cardType);
									bankCard.setCustname(custname);
									bankCard.setBankCode(bankCode);
									bankCard.setOpenBankName(openBankName);
									bankCard.setOpenBankAreaName(openBankAreaName);
									bankCard.setIsSigned(isSigned);
									bankCard.setIsPublic(isPublic);
									bankCard.setCardName(cardName);
		         					String bankInfoJson = cardObj.optJSONObject("bankInfo")==null ?"":cardObj.optJSONObject("bankInfo").toString();
									Gson gson = new Gson();
									BankCardBean.BankInfoBean bankInfoBean = gson.fromJson(bankInfoJson, BankCardBean.BankInfoBean.class);
									bankCard.setBankInfo(bankInfoBean==null? new BankCardBean.BankInfoBean() :bankInfoBean); //防止设置报空
								    bankCardData.add(bankCard);
								}
								CommonAdapter adapter = new CommonAdapter<BankCardBean>(BankCardListA.this, bankCardData, R.layout.bankcard_item_layout) {

									@Override
									public void convert(ViewHolder holder, final BankCardBean bankCardBean) {
										cardNumber = bankCardBean.getCardnumber();
										if (cardNumber != null && !cardNumber.isEmpty() && cardNumber.length() > 4) {
											String newCardNumber = cardNumber.substring(cardNumber.length()-4, cardNumber.length());
											tv_cardCode = holder.getView(R.id.tv_cardCode);
											tv_cardCode.setText(newCardNumber);
										}

										bankCode = bankCardBean.getBankCode();
										cardIcon = holder.getView(R.id.iv_bankLog);
										tv_bankName = holder.getView(R.id.tv_bankName);
										TextView tv_custname =	holder.getView(R.id.tv_custname);
										tv_custname.setText(bankCardBean.getCustname()==null ?"":bankCardBean.getCustname());
										tv_bankName.setText(bankCardBean.getCardName()); //银行名称
										cardIcon.setTag(bankCardBean.getId());

										if(cardIcon.getTag().equals(bankCardBean.getId())){
											cardIcon.setImageResource(BankIcon.getBankIcon(bankCode));


										}
										tv_cardType = holder.getView(R.id.tv_cardType);
										final String isPublic = bankCardBean.getIsPublic();
										if (Define.SAVINGS_CARD.equals(isPublic)) {
											tv_cardType.setText("储蓄卡");
										}else if (Define.PUBLIC_ACCOUNT.equals(isPublic)) {
											tv_cardType.setText("对公账户");
										}
										TextView tv_unbundling = holder.getView(R.id.tv_unbundling);
										setPremissionShowHideView(Premission.ACCT_USERCARD_EDIT,tv_unbundling); //是否有 添加,修改,删除银卡权限
										final String isSigned = bankCardBean.getIsSigned();



										tv_unbundling.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View view) {

												if(Define.PUBLIC_ACCOUNT.equals(isPublic) && Define.SIGNED.equals(isSigned)){

														ToastUtils.showToast(BankCardListA.this,"对公账户解绑请联系客服处理！");
														return;

												}


												if(Define.SIGNED.equals(isSigned)){
													ToastUtils.showToast(BankCardListA.this,"银行卡解绑失败，该银行卡已签约代扣！");
													return;
												}

												if(Define.PUBLIC_ACCOUNT.equals(isPublic)){

													ToastUtils.showToast(BankCardListA.this,"对公账户解绑请联系客服处理！");
													return;
												}

												cardId = bankCardBean.getId();

												if(myPopWindow !=null && myPopWindow.isShow()){
													myPopWindow.dissmiss();
												}
												myPopWindow = new MyPopAbstract(){
													@Override
													protected void setMapSettingViewWidget(View view) {
														TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
														TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
														tv_confirm.setOnClickListener(new OnClickListener() {
															@Override
															public void onClick(View view) {
																deleteCard(); //解除绑定
																myPopWindow.dissmiss();
															}
														});
														tv_cancle.setOnClickListener(new OnClickListener() {
															@Override
															public void onClick(View view) {
																myPopWindow.dissmiss();
															}
														});
													}

													@Override
													protected int layout() {
														return R.layout.unbound_pop;
													}
												}.popWindowTouch(BankCardListA.this).showAtLocation(tv_title, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

											}
										});
//										if (Define.OUTLETS_NORMAL.equals(user.getAuditState())) {
//											tv_auditingStatus.setText("审核通过");
//										}else if (Define.OUTLETS_PENDIG_AUDIT.equals(user.getAuditState())) {
//											tv_auditingStatus.setText("待审核");
//										}else {
//											tv_auditingStatus.setText("审核不通过");
//										}

									}
								};
								mListView.setAdapter(adapter);
                                  adapter.notifyDataSetChanged();

								cpd.dismiss();
								mListView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										/*BankCardBean cardBean = bankCardData.get(position);
										intent = new Intent(BankCardListA.this, BankCardDetailsA.class);
										
										intent.putExtra("cardId", cardBean.getId());
										intent.putExtra("bankCode", cardBean.getBankCode());
										intent.putExtra("cardType", cardBean.getCardType());
										intent.putExtra("cardNumber", cardBean.getCardnumber());
										intent.putExtra("openBankName", cardBean.getOpenBankName());
										intent.putExtra("openBankAreaName", cardBean.getOpenBankAreaName());
										intent.putExtra("custname",cardBean.getCustname()); //开户人姓名
										intent.putExtra("isSigned",cardBean.getIsSigned());
										intent.putExtra("isPublic",cardBean.getIsPublic());
                                        intent.putExtra("cardName",cardBean.getCardName());
										intent.putExtra("bankInfo",cardBean.getBankInfo());
										startActivityForResult(intent,2);
										overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);*/
									}
									
								});
								
								
							} else if("46000".equals(responseCode)){
								cpd.dismiss();
								Toast.makeText(BankCardListA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(BankCardListA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(BankCardListA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							cpd.dismiss();
							e.printStackTrace();
							Log.d("exciption", "exciption =" + e.toString());
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						cpd.dismiss();
						Toast.makeText(BankCardListA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("debug", "error = " + error.toString());

					}
				});
		// 解决重复请求后台的问题
		refreshBankCardListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		refreshBankCardListRequest.setTag("refreshBankCardListRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(refreshBankCardListRequest);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==2){
			scroll_view.smoothScrollTo(0,0);
			try {
				getBankCardList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	//删除银行卡
	private void deleteCard() {
		cpd.show();
		VolleyNetCommon volleyNetCommon = new VolleyNetCommon();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("operateType", Define.BANKCARD_DELETE);
			jsonObject.put("id", cardId);
			jsonObject.put("bankCode", bankCode);
			//	jsonObject.put("cardType", cardType);
			jsonObject.put("cardNo", cardNumber);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = Define.URL + "acct/manageBankCard";
		JsonObjectRequest manageBankCard = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(BankCardListA.this) {
			@Override
			public void volleyResponse(Object o) {

			}

			@Override
			public void volleyError(VolleyError volleyError) {
				Toast.makeText(BankCardListA.this, R.string.netError, Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void netVolleyResponese(JSONObject json) {
				Toast.makeText(BankCardListA.this, "删除银行卡成功！", Toast.LENGTH_SHORT).show();
//				setResult(2);          //关闭当前页去更新银行卡列表
//				finish();
				try {
					getBankCardList();
				} catch (Exception e) {
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
		}, "manageBankCard", true);
		volleyNetCommon.addQueue(manageBankCard);

	}

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("refreshBankCardListRequest");
		MyApplication.getHttpQueues().cancelAll("manageBankCard");
	}
}

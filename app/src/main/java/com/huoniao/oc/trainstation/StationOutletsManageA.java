package com.huoniao.oc.trainstation;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.StationOltsManage;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ThreadCommonUtils;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StationOutletsManageA extends BaseActivity implements OnClickListener {

	private ImageView iv_back;
	private Intent intent;
	private PullToRefreshListView mPullRefreshListView;
	private List<StationOltsManage> mDatas = new ArrayList<>();
	private List<StationOltsManage> selectList;
	private CommonAdapter<StationOltsManage> adapter;
	private LinearLayout layout_allOption;// 全选
	private LinearLayout layout_delete;// 删除
	private LinearLayout layout_operationButton;//操作按钮区域(全选、删除)
	private TextView tv_allOption;
	boolean visflag = true; //记录当前是全选还是反选

	private ListView mListView;
	private boolean refreshClose = true;//标记是否还有数据可加载
//	private ProgressDialog cpd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_station_outletsmanage);
		initView();
		initData();
//		ExitApplication.getInstance().addActivity(this);
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		intent = getIntent();
//		mDatas = (List<StationOltsManage>) intent.getSerializableExtra("oltsManageList");
		try {
			getOltsManageList(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initView() {
//		cpd = new CustomProgressDialog(StationOutletsManageA.this, "正在加载中...", R.anim.frame_anim);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.oltManageList);
		mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
		mListView = mPullRefreshListView.getRefreshableView();

		layout_allOption = (LinearLayout) findViewById(R.id.layout_allOption);
		layout_delete = (LinearLayout) findViewById(R.id.layout_delete);
		tv_allOption = (TextView) findViewById(R.id.tv_allOption);
		layout_operationButton = (LinearLayout) findViewById(R.id.layout_operationButton);
		setPremissionShowHideView(Premission.FB_AGENCY_EDIT, layout_operationButton);  //是否有操作代售点的权限

		iv_back.setOnClickListener(this);
		layout_allOption.setOnClickListener(this);
		layout_delete.setOnClickListener(this);

		/*layout_operationButton = (LinearLayout) findViewById(R.id.layout_operationButton);
		setPremissionShowHideView(Premission.FB_AGENCY_EDIT, layout_operationButton);  //是否有操作代售点的权限*/

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		// 删除代售点
		case R.id.layout_delete:
			//找出所有勾选的代售点，再通知服务端删除
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < mDatas.size(); ++i) {
				StationOltsManage ol = mDatas.get(i);
				if (ol.isCheckFlag())
					list.add(ol.getId());
			}
			
			if (list.isEmpty()){
	
				Toast.makeText(StationOutletsManageA.this, "请选择您要删除的代售点!", Toast.LENGTH_SHORT).show();
				
				return;
			}
			try {
				deleteLinkOlts(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
			
		// 全选/反选
		case R.id.layout_allOption:
			for (int i = 0; i < mDatas.size(); ++i) {
				StationOltsManage ol = mDatas.get(i);
				if (visflag || !ol.isCheckFlag())
					ol.setCheckFlag(true);
				else
					ol.setCheckFlag(false);
			}
			visflag = !visflag;
			if(visflag) tv_allOption.setText("全选");
			else tv_allOption.setText("反选");
			adapter.notifyDataSetChanged();
			break;

		default:
			break;
		}

	}
	
	/**
	 * 删除关联代售点
	 * 
	 * @throws Exception
	 */
	private void deleteLinkOlts(ArrayList<String> ids) throws Exception {
		cpd.show();
	
		JSONObject jsonObject = new JSONObject();
		String sId = "";
		for (String id : ids) {
			if (!sId.isEmpty()) sId += ",";
			sId += id;
		}
		
		
//		String[] array = (String[])ids.toArray(new String[ids.size()]); 
		try {
			jsonObject.put("ids", sId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		
		SessionJsonObjectRequest deleteLinkOltsRequest = new SessionJsonObjectRequest(Method.POST,
				Define.URL+"fb/deleteLinkAgency", jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
//								cpd.dismiss();
								if (mDatas != null) {
									mDatas.clear();
								}
								getOltsManageList(1);
								refreshClose = true;
								Toast.makeText(StationOutletsManageA.this, "删除成功!", Toast.LENGTH_SHORT).show();

							} else if("46000".equals(responseCode)){
								cpd.dismiss();
								Toast.makeText(StationOutletsManageA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(StationOutletsManageA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							}else {
								cpd.dismiss();
								Toast.makeText(StationOutletsManageA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							cpd.dismiss();
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						cpd.dismiss();
						Toast.makeText(StationOutletsManageA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);


					}
				});
		// 解决重复请求后台的问题
		deleteLinkOltsRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		deleteLinkOltsRequest.setTag("deleteLinkOlts");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(deleteLinkOltsRequest);

	}
	
	/**
	 * 更新代售点管理列表数据
	 * 
	 * @throws Exception
	 */
	private void getOltsManageList(final int pageNo) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pageNo", pageNo);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		final List<StationOltsManage> oltsManageList = new ArrayList<StationOltsManage>();
		SessionJsonObjectRequest OltsManageListRequest = new SessionJsonObjectRequest(Method.POST,
				Define.URL+"fb/agencyList", jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							int nextPage = response.getInt("next");
							setRefreshPager(nextPage);
							if ("0".equals(responseCode)) {
								JSONArray jsonArray = response.getJSONArray("data");

								for (int i = 0; i < jsonArray.length(); i++) {
									StationOltsManage oltsManage = new StationOltsManage();

									JSONObject oltsManageObj = (JSONObject) jsonArray.get(i);
									String id = oltsManageObj.optString("id");
									String code = oltsManageObj.optString("code");
									JSONObject area = oltsManageObj.optJSONObject("area");
									String name = oltsManageObj.optString("name");
									String address = oltsManageObj.optString("address");
									String master = oltsManageObj.optString("master");
									String phone = oltsManageObj.optString("phone");
									String winNumber = oltsManageObj.optString("winNumber");
									String corpName = oltsManageObj.optString("corpName");
									String corpMobile = oltsManageObj.optString("corpMobile");
									String corpIdNum = oltsManageObj.optString("corpIdNum");
									String state = oltsManageObj.optString("state");
									String city = area.optString("name");
									oltsManage.setId(id);
									oltsManage.setCode(code);
									// oltsRelation.setArea(area);
									oltsManage.setName(name);
									oltsManage.setAddress(address);
									oltsManage.setMaster(master);
									oltsManage.setPhone(phone);
									oltsManage.setWinNumber(winNumber);
									oltsManage.setCorpName(corpName);
									oltsManage.setCorpMobile(corpMobile);
									oltsManage.setCorpIdNum(corpIdNum);
									oltsManage.setState(state);
									oltsManage.setCity(city);

									Log.d("debug", oltsManage.getName());
									oltsManageList.add(oltsManage);
								}

								if (nextPage == 1) {
									mDatas.clear();
								} else if (nextPage == -1) {
									refreshClose = false;
								}
								mPullRefreshListView.onRefreshComplete();
								mDatas.addAll(oltsManageList);
								if (adapter == null) {
									adapter = new CommonAdapter<StationOltsManage>(StationOutletsManageA.this, mDatas,
											R.layout.station_oltmanagelist_item) {

										@Override
										public void convert(ViewHolder holder, final StationOltsManage outlets) {
											holder.setText(R.id.tv_name, outlets.getName())
//					  .setText(R.id.tv_outletNumber, outlets.getCode())
													.setText(R.id.tv_windowNumber, outlets.getWinNumber())
													.setText(R.id.tv_city, outlets.getCity());

											final TextView tv_outletNumber = holder.getView(R.id.tv_outletNumber);
											tv_outletNumber.setText(outlets.getCode());

											CheckBox checkBox = holder.getView(R.id.checkBox);

											checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
												@Override
												public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
													if (isChecked) {
														outlets.setCheckFlag(true);
													} else {
														outlets.setCheckFlag(false);
													}
												}
											});
											checkBox.setChecked(outlets.isCheckFlag());

											tv_outletNumber.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View view) {
													String outletNumber = tv_outletNumber.getText().toString();
													intent = new Intent(StationOutletsManageA.this, WindowsAnchoredListA.class);
													intent.putExtra("outletNumber", outletNumber);
													startActivity(intent);
													overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

												}
											});

										}
									};
									mListView.setAdapter(adapter);
								}

								adapter.refreshData(mDatas);
								cpd.dismiss();
								//
								mListView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										StationOltsManage oltsItem = mDatas.get(position - 1);

										intent = new Intent(StationOutletsManageA.this, StationOutletsDetailsA.class);
										intent.putExtra("id", oltsItem.getId());
										intent.putExtra("name", oltsItem.getName());
										intent.putExtra("code", oltsItem.getCode());
										intent.putExtra("city", oltsItem.getCity());
										intent.putExtra("winNumber", oltsItem.getWinNumber());
										intent.putExtra("corpName", oltsItem.getCorpName());
										intent.putExtra("corpMobile", oltsItem.getCorpMobile());
										intent.putExtra("corpIdNum", oltsItem.getCorpIdNum());
										intent.putExtra("master", oltsItem.getMaster());
										intent.putExtra("operatorName", oltsItem.getOperatorName());
										intent.putExtra("operatorMobile", oltsItem.getOperatorMobile());
										intent.putExtra("operatorIdNum", oltsItem.getOperatorIdNum());
										intent.putExtra("phone", oltsItem.getPhone());

										intent.putExtra("state", oltsItem.getState());

										startActivity(intent);
										overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
									}
								});


								
							} else {
								cpd.dismiss();
								Toast.makeText(StationOutletsManageA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							cpd.dismiss();
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						cpd.dismiss();
						Toast.makeText(StationOutletsManageA.this,R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

					}
				});
		// 解决重复请求后台的问题
		OltsManageListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		OltsManageListRequest.setTag("refreshOutletsManage");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(OltsManageListRequest);

	}


	/**
	 * 上拉加载下一页
	 *
	 * @param nextPage 下一页
	 */
	private void setRefreshPager(final int nextPage) {
		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshClose) {  //如果有数据 继续刷新
					try {
//						pageNumber++;
						getOltsManageList(nextPage);

					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
//                    Toast.makeText(FinancialListA.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
					ToastUtils.showToast(StationOutletsManageA.this, "没有更多数据了");
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
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("deleteLinkOlts");
		MyApplication.getHttpQueues().cancelAll("refreshOutletsManage");
	}
}

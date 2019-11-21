package com.huoniao.oc.trainstation;


import android.app.ProgressDialog;
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
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.StationOltsManage;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StationOutletsRelationA extends BaseActivity implements OnClickListener{
	
	private ImageView iv_back;
	private Intent intent;
	private ListView oltRelationList;
	private CommonAdapter<StationOltsManage> adapter;
	private List<StationOltsManage> mDatas;
	private ProgressDialog pd;
	private LinearLayout layout_oltsRelation;
	private LinearLayout layout_allOption;
	private TextView tv_allOption;
	boolean visflag = false;
	private LinearLayout ll_relation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_station_outletsrelation);
		initView();
		initData();
//		ExitApplication.getInstance().addActivity(this);
	}
	
	@SuppressWarnings("unchecked")
	private void initData() {
		intent = getIntent();
		mDatas = (List<StationOltsManage>) intent.getSerializableExtra("outletsRelationList");
		adapter = new CommonAdapter<StationOltsManage>(this, mDatas, R.layout.station_oltmanagelist_item) {

			@Override
			public void convert(ViewHolder holder, final StationOltsManage oltsRelation) {
				holder.setText(R.id.tv_name, oltsRelation.getName())
				  .setText(R.id.tv_outletNumber,oltsRelation.getCode())
				  .setText(R.id.tv_windowNumber, oltsRelation.getWinNumber())
				  .setText(R.id.tv_city, oltsRelation.getCity());
            ImageView iv_state = holder.getView(R.id.iv_state);
				CheckBox checkBox = holder.getView(R.id.checkBox);
//				String states = oltsRelation.getState();
//				if (Define.OUTLETS_PENDIG_AUDIT.equals(states) || Define.OUTLETS_NOTPASS.equals(states)) {
//					checkBox.setEnabled(false);
//				}

				if (!"0".equals(oltsRelation.getState())){
					checkBox.setEnabled(false);

				}else{
					checkBox.setEnabled(true);

				}

				switch (oltsRelation.getState()){
					case "0":  //正常
				       iv_state.setBackgroundResource(R.drawable.iv_audited);
						break;
					case "1": //待审核
						iv_state.setBackgroundResource(R.drawable.iv_pending_audit);
						break;
					case "2":  //审核不通过
						iv_state.setBackgroundResource(R.drawable.iv_not_pass);
						break;
				}


					checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			            @Override
			            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			            	if (isChecked) {
			            		oltsRelation.setCheckFlag(true);
			                } else {
			                	oltsRelation.setCheckFlag(false);
			                }
			            }
			        });
				checkBox.setChecked(oltsRelation.isCheckFlag());
			}
		};
		oltRelationList.setAdapter(adapter);
		oltRelationList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				StationOltsManage oltsRelationItem = mDatas.get(position);
//				if (visflag) {
//					checkBox.toggle();
//					if (checkBox.isChecked()) {
//						boolList.set(position, true);
//					} else {
//						boolList.set(position, visflag);
//					}
//				}
				intent = new Intent(StationOutletsRelationA.this, StationOutletsDetailsA.class);
//				Bundle bundle = new Bundle();
//
//				bundle.putSerializable("oltDetails", (Serializable) mDatas);
//				intent.putExtras(bundle);

				intent.putExtra("relationTag", "relationDetails");
				intent.putExtra("id", oltsRelationItem.getId());
				intent.putExtra("name", oltsRelationItem.getName());
				intent.putExtra("code", oltsRelationItem.getCode());
				intent.putExtra("city", oltsRelationItem.getCity());
				intent.putExtra("winNumber", oltsRelationItem.getWinNumber());
				intent.putExtra("corpName", oltsRelationItem.getCorpName());
				intent.putExtra("corpMobile", oltsRelationItem.getCorpMobile());
				intent.putExtra("corpIdNum", oltsRelationItem.getCorpIdNum());
				intent.putExtra("master", oltsRelationItem.getMaster());
				intent.putExtra("phone", oltsRelationItem.getPhone());
				intent.putExtra("state", oltsRelationItem.getState());
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			}
		});	
	}
	private void initView() {
		pd = new CustomProgressDialog(StationOutletsRelationA.this, "正在加载中...", R.drawable.frame_anim);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		oltRelationList = (ListView) findViewById(R.id.oltRelationList);
		layout_oltsRelation = (LinearLayout) findViewById(R.id.layout_oltsRelation);
		layout_allOption = (LinearLayout) findViewById(R.id.layout_allOption);
		tv_allOption = (TextView) findViewById(R.id.tv_allOption);
		ll_relation = (LinearLayout) findViewById(R.id.ll_relation); //代售点关联容器

		iv_back.setOnClickListener(this);
		layout_oltsRelation.setOnClickListener(this);
		layout_allOption.setOnClickListener(this);
		setPremissionShowHideView(Premission.FB_AGENCY_EDIT,ll_relation);  //代售点关联权限
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		//全选/反选	
		case R.id.layout_allOption:
			for (int i = 0; i < adapter.getCount(); ++i) {
				StationOltsManage ol = (StationOltsManage)oltRelationList.getItemAtPosition(i);
				if ("0".equals(ol.getState())) {   //审核通过的才可以选中
				if (visflag || !ol.isCheckFlag())
						ol.setCheckFlag(true);
				else
					ol.setCheckFlag(false);
				}
			}
			visflag = !visflag;
			if(visflag) tv_allOption.setText("全选");
			else tv_allOption.setText("反选");
			adapter.notifyDataSetChanged();
			break;
			
		case R.id.layout_oltsRelation:
			//找出所有勾选的代售点，再通知服务端进行关联
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < adapter.getCount(); ++i) {
				StationOltsManage ol = (StationOltsManage)oltRelationList.getItemAtPosition(i);
				if (ol.isCheckFlag())
					list.add(ol.getId());
			}
			
			if (list.isEmpty()){
				
			Toast.makeText(StationOutletsRelationA.this, "请选择您要审核的代售点!", Toast.LENGTH_SHORT).show();
				
				return;
			}
			
			try {
				relationOutlets(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		
	}
	
	/**
	 * 更新关联代售点列表
	 * 
	 * @throws Exception
	 */
	private void getRelationOltsList() throws Exception {
//		pd.show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("", "");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		final List<StationOltsManage> RelationOltsList = new ArrayList<StationOltsManage>();
		SessionJsonObjectRequest refreshRelationOltsListRequest = new SessionJsonObjectRequest(Method.POST,
				Define.URL+"fb/canLinkList", jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								JSONArray jsonArray = response.getJSONArray("data");

								for (int i = 0; i < jsonArray.length(); i++) {
									StationOltsManage oltsRelation = new StationOltsManage();

									JSONObject oltsRelationObj = (JSONObject) jsonArray.get(i);
									String state = oltsRelationObj.optString("state");
									//if (!"0".equals(state)) continue;  //不管哪种状态的数据都要显示
									String id = oltsRelationObj.optString("id");
									String code = oltsRelationObj.optString("code");
									JSONObject area = oltsRelationObj.optJSONObject("area");
									String name = oltsRelationObj.optString("name");
									String address = oltsRelationObj.optString("address");
									String master = oltsRelationObj.optString("master");
									String phone = oltsRelationObj.optString("phone");
									String winNumber = oltsRelationObj.optString("winNumber");
									String corpName = oltsRelationObj.optString("corpName");
									String corpMobile = oltsRelationObj.optString("corpMobile");
									String corpIdNum = oltsRelationObj.optString("corpIdNum");
									String city = area.optString("name");
									oltsRelation.setId(id);
									oltsRelation.setCode(code);
									// oltsRelation.setArea(area);
									oltsRelation.setName(name);
									oltsRelation.setAddress(address);
									oltsRelation.setMaster(master);
									oltsRelation.setPhone(phone);
									oltsRelation.setWinNumber(winNumber);
									oltsRelation.setCorpName(corpName);
									oltsRelation.setCorpMobile(corpMobile);
									oltsRelation.setCorpIdNum(corpIdNum);
									oltsRelation.setState(state);
									oltsRelation.setCity(city);

									Log.d("debug", oltsRelation.getName());
									RelationOltsList.add(oltsRelation);
								}
								adapter.refreshData(RelationOltsList);
								pd.dismiss();
								Toast.makeText(StationOutletsRelationA.this, "审核成功！", Toast.LENGTH_SHORT).show();
							} else if("46000".equals(responseCode)){
								pd.dismiss();
								Toast.makeText(StationOutletsRelationA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(StationOutletsRelationA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							}else {
								pd.dismiss();
								Toast.makeText(StationOutletsRelationA.this, message, Toast.LENGTH_SHORT).show();
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						pd.dismiss();
						Toast.makeText(StationOutletsRelationA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

					}
				});
		// 解决重复请求后台的问题
		refreshRelationOltsListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		refreshRelationOltsListRequest.setTag("outletsRelation");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(refreshRelationOltsListRequest);

	}
	
	
	/**
	 * 关联代售点
	 * 
	 * @throws Exception
	 */
	private void relationOutlets(ArrayList<String> ids) throws Exception {
		pd.show();
		
		JSONObject jsonObject = new JSONObject();
		String sId = "";
		for (String id : ids) {
			if (!sId.isEmpty()) sId += ",";
			sId += id;
		}
		try {
			jsonObject.put("ids", sId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//		final List<StationOltsManage> RelationOltsList = new ArrayList<StationOltsManage>();
		SessionJsonObjectRequest relationOutletsRequest = new SessionJsonObjectRequest(Method.POST,
				Define.URL+"fb/linkAgency", jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								
								getRelationOltsList();												
								
							} else if("46000".equals(responseCode)){
								pd.dismiss();
								Toast.makeText(StationOutletsRelationA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(StationOutletsRelationA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							}else {
								pd.dismiss();
								Toast.makeText(StationOutletsRelationA.this, message, Toast.LENGTH_SHORT).show();
							}
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						pd.dismiss();
						Toast.makeText(StationOutletsRelationA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

					}
				});
		// 解决重复请求后台的问题
		relationOutletsRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		relationOutletsRequest.setTag("refreshRelationOutlets");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(relationOutletsRequest);

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("outletsRelation");
		MyApplication.getHttpQueues().cancelAll("refreshRelationOutlets");
	}

}

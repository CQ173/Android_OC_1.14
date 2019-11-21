package com.huoniao.oc.admin;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.huoniao.oc.bean.PaySystemBean;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.DateUtils;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminHisPaySystA extends BaseActivity implements OnClickListener{
	private ImageView iv_back, iv_dataChoice;
	private Intent intent;
	private ListView mListView;
	private List<PaySystemBean> mDatas;
	private TextView tv_data, tv_paysysOnMonth;
	private TextView tv_shouldSum, tv_alreadySum;
	private String date;
	private CommonAdapter<PaySystemBean> adapter;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_station_historpaysystem);
		initView();
		initData();

	}
	@SuppressWarnings("unchecked")
	private void initData() {
		intent = getIntent();
		mDatas = (List<PaySystemBean>) intent.getSerializableExtra("paySysData");
		adapter = new CommonAdapter<PaySystemBean>(this, mDatas, R.layout.admin_historpaysystem_item) {

			@Override
			public void convert(ViewHolder holder, PaySystemBean paySysBean) {
				if (!"".equals(paySysBean) || paySysBean != null) {
					String payDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(paySysBean.getDate()));
					holder.setText(R.id.tv_date, payDate)
//					  .setText(R.id.tv_oltsAccount, paySysBean.getOfficeCode())
					  .setText(R.id.tv_agentName, paySysBean.getAgentName())
					  .setText(R.id.tv_shouldAmount, paySysBean.getShouldAmount())
					  .setText(R.id.tv_alreadyAmount, paySysBean.getAlreadyAmount())
					  .setText(R.id.tv_trainstationName, paySysBean.getRailwayStationName());
					
				TextView tv_withholdStatus = holder.getView(R.id.tv_withholdStatus);
				
				if (Define.CHARG_SUCCESS.equals(paySysBean.getWithholdStatus())) {
					tv_withholdStatus.setText("成功");
				}else if (Define.CHARG_WAIT.equals(paySysBean.getWithholdStatus())) {
					tv_withholdStatus.setText("待扣款");
				}else {
					tv_withholdStatus.setText("失败");		
					
				}
				
				
				}
				
			}
		};
		mListView.setAdapter(adapter);
	mListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			PaySystemBean pBean = mDatas.get(position);
			intent = new Intent(AdminHisPaySystA.this, AdminHisPstDetailsA.class);
			String payDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(pBean.getDate()));
			intent.putExtra("date", payDate);
			intent.putExtra("stationAccount", pBean.getRailwayStationId());
			intent.putExtra("stationName", pBean.getRailwayStationName());
			intent.putExtra("outletsAccount", pBean.getOfficeCode());
			intent.putExtra("outletsName", pBean.getAgentName());
			intent.putExtra("shouldAmount", pBean.getShouldAmount());
			intent.putExtra("alreadyAmount", pBean.getAlreadyAmount());
			intent.putExtra("withholdStatus", pBean.getWithholdStatus());
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
		}
	});
		tv_shouldSum.setText(intent.getStringExtra("shouldSum"));	
		tv_alreadySum.setText(intent.getStringExtra("alreadySum"));
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_dataChoice = (ImageView) findViewById(R.id.iv_dataChoice);
		mListView = (ListView) findViewById(R.id.listView);
		tv_shouldSum = (TextView) findViewById(R.id.tv_shouldSum);
		tv_alreadySum = (TextView) findViewById(R.id.tv_alreadySum);
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_paysysOnMonth = (TextView) findViewById(R.id.tv_paysysOnMonth);
		pd = new CustomProgressDialog(this, "正在加载中...", R.drawable.frame_anim);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		tv_data.setText(sdf.format(new java.util.Date()));
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM月");
		tv_paysysOnMonth.setText(sdf2.format(new java.util.Date()));
		iv_back.setOnClickListener(this);
		iv_dataChoice.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_dataChoice:
			Calendar cal = Calendar.getInstance();
			
			DatePickerDialog mDialog = new DatePickerDialog(AdminHisPaySystA.this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					tv_data.setText(year + "-" + (monthOfYear + 1));
					tv_paysysOnMonth.setText((monthOfYear + 1) + "月");
					try {
						getOltPaySysForMonth();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

				mDialog.show();  
				//隐藏不想显示的子控件，这里隐藏日控件
			try {
				DatePicker dp = findDatePicker((ViewGroup) mDialog.getWindow().getDecorView());
				if (dp != null) {
				    ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/**
				 * 设置可选日期范围
				 * 
				 */
//				dp = mDialog.getDatePicker();
//				//限制显示6个月的
//				dp.setMinDate(new Date().getTime() - (long)150 * 24 * 60 * 60 * 1000);
//
//				dp.setMaxDate(new Date().getTime());
				
			// 直接创建一个DatePickerDialog对话框实例，并将它显示出来
//			new DatePickerDialog(PaySystemA.this,
//					// 绑定监听器
//					new DatePickerDialog.OnDateSetListener() {
//
//						@Override
//						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//							tv_data.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//						}
//					}
//					// 设置初始日期
//					, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
			break;
		default:
			break;
		}
		
	}
	
	
	/**
	 * 通过遍历方法查找DatePicker里的子控件：年、月、日
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
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("hisSysForMoth");
	}
	
	/**
	 * 按月查询历史汇缴数据
	 * 
	 * @throws Exception
	 */
	private void getOltPaySysForMonth() throws Exception {
		pd.show();
		date = tv_data.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("time", date);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = Define.URL + "fb/paymentList";
		
		
//		String url = "http://192.168.0.117:8181/OC/app/fb/paymentList";
		final List<PaySystemBean> paySysData = new ArrayList<PaySystemBean>();
		SessionJsonObjectRequest hisSysForMonthRequest = new SessionJsonObjectRequest(Method.POST,
				url, jsonObject, new Listener<JSONObject>() {
				 	String alreadySum;
				 	String shouldSum;
					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								int num = response.getInt("size");
								JSONArray jsonArray = response.getJSONArray("data");
								num = Math.min(num, jsonArray.length());

//								for (int i = 0; i < jsonArray.length(); i++) {
								for (int i = 0; i < num; i++) {
									PaySystemBean paySysBean = new PaySystemBean();

									JSONObject paySysObj = (JSONObject) jsonArray.get(i);
									String date = paySysObj.optString("date");// 汇缴时间
									String officeCode = paySysObj.optString("officeCode");// 机构编码
									String agentName = paySysObj.optString("agentName");// 代售点名称
									String shouldAmount = paySysObj.optString("shouldAmountString");// 应扣金额
									String alreadyAmount = paySysObj.optString("alreadyAmountString");// 已扣金额
									String withholdStatus = paySysObj.optString("withholdStatus");// 扣款状态
									String railwayStationName = paySysObj.optString("railwayStationName");//火车站名称
									String railwayStationId = paySysObj.optString("railwayStationId");//火车站id
									

									paySysBean.setDate(date);
									paySysBean.setOfficeCode(officeCode);
									
									paySysBean.setAgentName(agentName);
									paySysBean.setShouldAmount(shouldAmount);
									paySysBean.setAlreadyAmount(alreadyAmount);
									paySysBean.setWithholdStatus(withholdStatus);
									paySysBean.setRailwayStationName(railwayStationName);
									paySysBean.setRailwayStationId(railwayStationId);

									paySysData.add(paySysBean);
								}
								adapter.refreshData(paySysData);
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject paySysObj = (JSONObject) jsonArray.get(i);
									alreadySum = paySysObj.optString("alreadySumString");// 已扣总金额
									shouldSum = paySysObj.optString("shouldSumString");// 应扣总金额
									}
								tv_shouldSum.setText(shouldSum);	
								tv_alreadySum.setText(alreadySum);
								pd.dismiss();
								
								mListView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										PaySystemBean pstBean = paySysData.get(position);
										intent = new Intent(AdminHisPaySystA.this, AdminHisPstDetailsA.class);
										String payDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(pstBean.getDate()));
										intent.putExtra("date", payDate);
										intent.putExtra("stationAccount", pstBean.getRailwayStationId());
										intent.putExtra("stationName", pstBean.getRailwayStationName());
										intent.putExtra("outletsAccount", pstBean.getOfficeCode());
										intent.putExtra("outletsName", pstBean.getAgentName());
										intent.putExtra("shouldAmount", pstBean.getShouldAmount());
										intent.putExtra("alreadyAmount", pstBean.getAlreadyAmount());
										intent.putExtra("withholdStatus", pstBean.getWithholdStatus());
										startActivity(intent);
										overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
									}
								});
								
							} else if("46000".equals(responseCode)){
								pd.dismiss();
								Toast.makeText(AdminHisPaySystA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(AdminHisPaySystA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(AdminHisPaySystA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.d("exciption", "exciption =" + e.toString());
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
//						pd.dismiss();
						Toast.makeText(AdminHisPaySystA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("debug", "error = " + error.toString());

					}
				});
		// 解决重复请求后台的问题
		hisSysForMonthRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		hisSysForMonthRequest.setTag("hisSysForMoth");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(hisSysForMonthRequest);

	}
}

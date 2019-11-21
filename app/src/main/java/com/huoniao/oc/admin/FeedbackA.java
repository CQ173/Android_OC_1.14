package com.huoniao.oc.admin;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import com.huoniao.oc.bean.FeedbackBean;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
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
import java.util.Date;
import java.util.List;

public class FeedbackA extends BaseActivity implements OnClickListener{
	private ImageView iv_back, iv_dataChoice;
	private Intent intent;
	private ListView mListView;
	private List<FeedbackBean> mDatas;
	private TextView tv_tradeAmount, tv_tradeStatus, tv_startDate, tv_endDate, tv_paysysOnMonth;
	private CommonAdapter<FeedbackBean> adapter;
	private String startDate, endDate;
	private LinearLayout layout_startDataChoice;
	private LinearLayout layout_endDataChoice;
//	private ProgressDialog cpd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_feedback);
		initView();
		initData();

	}
	
	@SuppressWarnings("unchecked")
	private void initData() {
		intent = getIntent();
		try {
			getFeedbackForMonth();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*mDatas = (List<FeedbackBean>) intent.getSerializableExtra("feedbackData");
		adapter = new CommonAdapter<FeedbackBean>(this, mDatas, R.layout.admin_feedback_item) {

			@Override
			public void convert(ViewHolder holder, FeedbackBean feedbackBean) {
				String feedbackTime = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(feedbackBean.getFeedbackTime()));
				if (!"".equals(feedbackBean) || feedbackBean != null) {
				holder.setText(R.id.tv_userName, feedbackBean.getUserName())
//					  .setText(R.id.tv_feedbackType, feedbackBean.getFeedbackType())
					  .setText(R.id.tv_feedbackContent, feedbackBean.getFeedbackContent())
//					  .setText(R.id.tv_status, feedbackBean.getStatus())
					  .setText(R.id.tv_feedbackTime, feedbackTime);
				TextView tv_feedbackType = holder.getView(R.id.tv_feedbackType);
				if (Define.CONSULTATION.equals(feedbackBean.getFeedbackType())) {
					tv_feedbackType.setText("咨询");
				}else if (Define.PROPOSAL.equals(feedbackBean.getFeedbackType())) {
					tv_feedbackType.setText("建议");
				}else if (Define.COMPLAINT.equals(feedbackBean.getFeedbackType())) {
					tv_feedbackType.setText("投诉");
				}else {
					tv_feedbackType.setText("其它");
				}

				TextView tv_status = holder.getView(R.id.tv_status);
				if (Define.UNTREATED.equals(feedbackBean.getStatus())) {
					tv_status.setText("待处理");
				}else {
					tv_status.setText("已处理");
				}
			}	
		}
		
	};
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FeedbackBean feedbackBean = mDatas.get(position);
				
				intent = new Intent(FeedbackA.this, FeedbackDetailsA.class);
				intent.putExtra("id", feedbackBean.getId());
				intent.putExtra("opinion", feedbackBean.getOpinion());
				intent.putExtra("status", feedbackBean.getStatus());
				intent.putExtra("type", feedbackBean.getFeedbackType());
				intent.putExtra("content", feedbackBean.getFeedbackContent());
				
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
				
			}
		});*/
		
	}
	private void initView() {
		MyApplication.getInstence().addActivity(this);
		iv_back = (ImageView) findViewById(R.id.iv_back);
//		iv_dataChoice = (ImageView) findViewById(R.id.iv_dataChoice);
		tv_startDate = (TextView) findViewById(R.id.tv_startDate);
		tv_endDate = (TextView) findViewById(R.id.tv_endDate);
		layout_startDataChoice = (LinearLayout) findViewById(R.id.layout_startDataChoice);
		layout_endDataChoice = (LinearLayout) findViewById(R.id.layout_endDataChoice);
//		tv_paysysOnMonth = (TextView) findViewById(R.id.tv_paysysOnMonth);
//		cpd = new CustomProgressDialog(this, "正在加载中...", R.anim.frame_anim);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();  
		//获取当月第一天
        cal.set(Calendar.DAY_OF_MONTH, 1);  
        cal.getTime();  
        tv_startDate.setText(sdf.format(cal.getTime()));  
      //获取当月最后一天
//        cal.set(Calendar.DAY_OF_MONTH,  
//                 cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
//        tv_endDate.setText(sdf.format(cal.getTime()));  
		//获取当前年月日
        tv_endDate.setText(sdf.format(new Date()));  
		mListView = (ListView) findViewById(R.id.lv_feedback);
		iv_back.setOnClickListener(this);
//		iv_dataChoice.setOnClickListener(this);
		layout_startDataChoice.setOnClickListener(this);
		layout_endDataChoice.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.layout_startDataChoice:
			Calendar cal = Calendar.getInstance();
			
			DatePickerDialog mDialog = new DatePickerDialog(FeedbackA.this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					tv_startDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//					tv_paysysOnMonth.setText((monthOfYear + 1) + "月");
					try {
						getFeedbackForMonth();
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
			Calendar cal2 = Calendar.getInstance();
			
			DatePickerDialog mDialog2 = new DatePickerDialog(FeedbackA.this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					tv_endDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//					tv_paysysOnMonth.setText((monthOfYear + 1) + "月");
					try {
						getFeedbackForMonth();
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
	
	
	/**
	 * 按月查询反馈信息
	 * 
	 * @throws Exception
	 */
	private void getFeedbackForMonth() throws Exception {
		cpd.show();
		startDate = tv_startDate.getText().toString();
		endDate = tv_endDate.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("beginDate", startDate);
			jsonObject.put("endDate", endDate);
		} catch (JSONException e1) {

			e1.printStackTrace();
		}
		String url = Define.URL + "fb/feedbackList";
		
		
		SessionJsonObjectRequest feedbackForMonthRequest = new SessionJsonObjectRequest(Method.POST,
				url, jsonObject, new Listener<JSONObject>() {
			
					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
//								int num = response.getInt("size");
								JSONArray jsonArray = response.getJSONArray("data");

								final List<FeedbackBean> feedbackData = new ArrayList<FeedbackBean>();

//								num = Math.min(num, jsonArray.length());


								for (int i = 0; i < jsonArray.length(); i++) {
									FeedbackBean feedbackBean = new FeedbackBean();

									JSONObject feedbackObj = (JSONObject) jsonArray.get(i);
									String id = feedbackObj.optString("id");// 反馈信息id
									String opinion = feedbackObj.optString("opinion");// 处理意见
									String content = feedbackObj.optString("content");// 反馈内容
//									String handleDate = feedbackObj.optString("handleDate");// 处理时间
									String createDate = feedbackObj.optString("createDate");//反馈时间
									String status = feedbackObj.optString("status");//处理状态,0未处理,1已处理
									String type = feedbackObj.optString("type");//反馈类型:1咨询,2建议,3投诉,4其它
									JSONObject feedbackUser = feedbackObj.optJSONObject("user");// 反馈用户信息
									String userName = feedbackUser.optString("name");// 用户名
			
									feedbackBean.setId(id);
									feedbackBean.setOpinion(opinion);
									feedbackBean.setFeedbackContent(content);
									feedbackBean.setFeedbackTime(createDate);
									feedbackBean.setUserName(userName);
									feedbackBean.setFeedbackType(type);
									feedbackBean.setStatus(status);
									feedbackData.add(feedbackBean);
								}

								adapter = new CommonAdapter<FeedbackBean>(FeedbackA.this, feedbackData, R.layout.admin_feedback_item) {

									@Override
									public void convert(ViewHolder holder, FeedbackBean feedbackBean) {
										String feedbackTime = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(feedbackBean.getFeedbackTime()));
										if (!"".equals(feedbackBean) || feedbackBean != null) {
											holder.setText(R.id.tv_userName, feedbackBean.getUserName())
//					  .setText(R.id.tv_feedbackType, feedbackBean.getFeedbackType())
													.setText(R.id.tv_feedbackContent, feedbackBean.getFeedbackContent())
//					  .setText(R.id.tv_status, feedbackBean.getStatus())
													.setText(R.id.tv_feedbackTime, feedbackTime);
											TextView tv_feedbackType = holder.getView(R.id.tv_feedbackType);
											if (Define.CONSULTATION.equals(feedbackBean.getFeedbackType())) {
												tv_feedbackType.setText("咨询");
											}else if (Define.PROPOSAL.equals(feedbackBean.getFeedbackType())) {
												tv_feedbackType.setText("建议");
											}else if (Define.COMPLAINT.equals(feedbackBean.getFeedbackType())) {
												tv_feedbackType.setText("投诉");
											}else {
												tv_feedbackType.setText("其它");
											}

											TextView tv_status = holder.getView(R.id.tv_status);
											if (Define.UNTREATED.equals(feedbackBean.getStatus())) {
												tv_status.setText("待处理");
											}else {
												tv_status.setText("已处理");
											}
										}
									}

								};
								mListView.setAdapter(adapter);
								adapter.refreshData(feedbackData);
								cpd.dismiss();
								mListView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										FeedbackBean fBean = feedbackData.get(position);
										
										intent = new Intent(FeedbackA.this, FeedbackDetailsA.class);
										intent.putExtra("id", fBean.getId());
										intent.putExtra("opinion", fBean.getOpinion());
										intent.putExtra("status", fBean.getStatus());
										intent.putExtra("type", fBean.getFeedbackType());
										intent.putExtra("content", fBean.getFeedbackContent());
										startActivity(intent);
										overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
										
									}
								});
								
							} else if("46000".equals(responseCode)){
								cpd.dismiss();
								Toast.makeText(FeedbackA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(FeedbackA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(FeedbackA.this, message, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(FeedbackA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("debug", "error = " + error.toString());

					}
				});
		// 解决重复请求后台的问题
		feedbackForMonthRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		feedbackForMonthRequest.setTag("feedbackForMonth");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(feedbackForMonthRequest);

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("feedbackForMonth");
	}
}

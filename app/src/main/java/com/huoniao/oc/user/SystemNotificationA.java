package com.huoniao.oc.user;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.huoniao.oc.bean.NotificationBean;
import com.huoniao.oc.bean.User;
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

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

public class SystemNotificationA extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private Intent intent;
	private ListView mListView;
	private List<NotificationBean> mDatas;
	private TextView tv_startDate, tv_endDate;
	private CommonAdapter<NotificationBean> adapter;
	private String startDate, endDate;
	private LinearLayout layout_startDataChoice;
	private LinearLayout layout_endDataChoice;
	private LinearLayout layout_allOption;// 全选
	private LinearLayout layout_delete;// 删除
	private TextView tv_allOption;
	boolean visflag = true; // 记录当前是全选还是反选
	private LinearLayout layout_hengXian;// 横线
	private LinearLayout layout_allOptAndDelet;
	private ProgressDialog pd;
	private User user;
	private String roleName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_sysnotification);
		initView();
		initData();

	}

	@SuppressWarnings("unchecked")
	private void initData() {

		intent = getIntent();
		mDatas = (List<NotificationBean>) intent.getSerializableExtra("sysNotificationData");
		if (roleName != null && roleName.contains("系统管理员")) {
			adapter = new CommonAdapter<NotificationBean>(this, mDatas, R.layout.admin_sysnotification_item) {

				@Override
				public void convert(ViewHolder holder, final NotificationBean notificationBean) {
					// String feedbackTime =
					// DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(feedbackBean.getFeedbackTime()));
					String startDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(notificationBean.getStartTime()));
					String endDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(notificationBean.getEndTime()));
					if (!"".equals(notificationBean) || notificationBean != null) {
						holder.setText(R.id.tv_title, notificationBean.getNotificationTitle())
								// .setText(R.id.tv_type,
								// notificationBean.getNotificationType())
								.setText(R.id.tv_startTime, startDate)
								.setText(R.id.tv_endTime, endDate);
						TextView tv_type = holder.getView(R.id.tv_type);
						if (Define.COMMONLY_NOTIFICATION.equals(notificationBean.getNotificationType())) {
							tv_type.setText("一般通知");
						} else {
							tv_type.setText("重要通知");
						}

						CheckBox checkBox = holder.getView(R.id.checkBox);

						checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									notificationBean.setCheckFlag(true);
								} else {
									notificationBean.setCheckFlag(false);
								}
							}
						});
						checkBox.setChecked(notificationBean.isCheckFlag());
					}
				}

			};

		} else {
			adapter = new CommonAdapter<NotificationBean>(this, mDatas, R.layout.user_sysnotification_item) {

				@Override
				public void convert(ViewHolder holder, NotificationBean notificationBean) {
					// String feedbackTime =
					// DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(feedbackBean.getFeedbackTime()));
					String startDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(notificationBean.getStartTime()));
					String endDate = DateUtils.getInstance().formatString(DateUtils.getInstance().formatDate(notificationBean.getEndTime()));
					if (!"".equals(notificationBean) || notificationBean != null) {
						holder.setText(R.id.tv_title, notificationBean.getNotificationTitle())
								// .setText(R.id.tv_type,
								// notificationBean.getNotificationType())
								.setText(R.id.tv_startTime, startDate)
								.setText(R.id.tv_endTime, endDate);
						TextView tv_type = holder.getView(R.id.tv_type);
						if (Define.COMMONLY_NOTIFICATION.equals(notificationBean.getNotificationType())) {
							tv_type.setText("一般通知");
						} else {
							tv_type.setText("重要通知");
						}

					}
				}

			};
		}

		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				NotificationBean notificationBean = mDatas.get(position);

				intent = new Intent(SystemNotificationA.this, SystemNotificationDetailsA.class);
				intent.putExtra("id", notificationBean.getNotificationId());
				intent.putExtra("title", notificationBean.getNotificationTitle());
				intent.putExtra("type", notificationBean.getNotificationType());
				intent.putExtra("startTime", notificationBean.getStartTime());
				intent.putExtra("endTime", notificationBean.getEndTime());
				intent.putExtra("content", notificationBean.getNotificationContent());

				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

			}
		});

	}

	private void initView() {
		user = (User) readObject(SystemNotificationA.this, "loginResult");
		roleName = user.getRoleName();
		iv_back = (ImageView) findViewById(R.id.iv_back);
		// iv_dataChoice = (ImageView) findViewById(R.id.iv_dataChoice);
		tv_startDate = (TextView) findViewById(R.id.tv_startDate);
		tv_endDate = (TextView) findViewById(R.id.tv_endDate);
		layout_startDataChoice = (LinearLayout) findViewById(R.id.layout_startDataChoice);
		layout_endDataChoice = (LinearLayout) findViewById(R.id.layout_endDataChoice);
		layout_allOption = (LinearLayout) findViewById(R.id.layout_allOption);
		layout_delete = (LinearLayout) findViewById(R.id.layout_delete);
		tv_allOption = (TextView) findViewById(R.id.tv_allOption);
		layout_hengXian = (LinearLayout) findViewById(R.id.layout_hengXian);
		layout_allOptAndDelet = (LinearLayout) findViewById(R.id.layout_allOptAndDelet);
		if (roleName != null && roleName.contains("系统管理员")) {
			layout_hengXian.setVisibility(View.VISIBLE);
			layout_allOptAndDelet.setVisibility(View.VISIBLE);
		}else {
			layout_hengXian.setVisibility(View.GONE);
			layout_allOptAndDelet.setVisibility(View.GONE);
		}
		// tv_paysysOnMonth = (TextView) findViewById(R.id.tv_paysysOnMonth);
		pd = new CustomProgressDialog(this, "正在加载中...", R.drawable.frame_anim);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		// 获取当月第一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.getTime();
		tv_startDate.setText(sdf.format(cal.getTime()));
		// 获取当月最后一天
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		tv_endDate.setText(sdf.format(cal.getTime()));
		// 获取当前年月日
		// tv_endDate.setText(sdf.format(new Date()));
		mListView = (ListView) findViewById(R.id.lv_sysNotification);
		iv_back.setOnClickListener(this);
		// iv_dataChoice.setOnClickListener(this);
		layout_startDataChoice.setOnClickListener(this);
		layout_endDataChoice.setOnClickListener(this);
		layout_allOption.setOnClickListener(this);
		layout_delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.layout_startDataChoice:
			Calendar cal = Calendar.getInstance();

			DatePickerDialog mDialog = new DatePickerDialog(SystemNotificationA.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					tv_startDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
					// tv_paysysOnMonth.setText((monthOfYear + 1) + "月");
					try {
						getNotifacationForDate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

			mDialog.show();
			// 隐藏不想显示的子控件，这里隐藏日控件
			// DatePicker dp = findDatePicker((ViewGroup)
			// mDialog.getWindow().getDecorView());
			// if (dp != null) {
			// ((ViewGroup)((ViewGroup)
			// dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
			// }
			/**
			 * 设置可选日期范围
			 * 
			 */
			// dp = mDialog.getDatePicker();
			// //限制显示6个月的
			// dp.setMinDate(new Date().getTime() - (long)150 * 24 * 60 * 60 *
			// 1000);
			//
			// dp.setMaxDate(new Date().getTime());

			break;

		case R.id.layout_endDataChoice:
			Calendar cal2 = Calendar.getInstance();

			DatePickerDialog mDialog2 = new DatePickerDialog(SystemNotificationA.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					tv_endDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
					// tv_paysysOnMonth.setText((monthOfYear + 1) + "月");
					try {
						getNotifacationForDate();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));

			mDialog2.show();
			// 隐藏不想显示的子控件，这里隐藏日控件
			// DatePicker dp = findDatePicker((ViewGroup)
			// mDialog.getWindow().getDecorView());
			// if (dp != null) {
			// ((ViewGroup)((ViewGroup)
			// dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
			// }
			/**
			 * 设置可选日期范围
			 * 
			 */
			// dp = mDialog.getDatePicker();
			// //限制显示6个月的
			// dp.setMinDate(new Date().getTime() - (long)150 * 24 * 60 * 60 *
			// 1000);
			//
			// dp.setMaxDate(new Date().getTime());

			break;

		// 删除通知
		case R.id.layout_delete:
			// 找出所有勾选的通知，再通知服务端删除
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < adapter.getCount(); ++i) {
				NotificationBean nBean = (NotificationBean) mListView.getItemAtPosition(i);
				if (nBean.isCheckFlag())
					list.add(nBean.getNotificationId());
			}

			if (list.isEmpty()) {

				Toast.makeText(SystemNotificationA.this, "请选择您要删除的通知!", Toast.LENGTH_SHORT).show();

				return;
			}
			try {
				deleteNotification(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		// 全选/反选
		case R.id.layout_allOption:
			for (int i = 0; i < adapter.getCount(); ++i) {
				NotificationBean nBean = (NotificationBean) mListView.getItemAtPosition(i);
				if (visflag || !nBean.isCheckFlag())
					nBean.setCheckFlag(true);
				else
					nBean.setCheckFlag(false);
			}
			visflag = !visflag;
			if (visflag)
				tv_allOption.setText("全选");
			else
				tv_allOption.setText("反选");
			adapter.notifyDataSetChanged();
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
	
	/**
	 * 删除通知
	 * 
	 * @throws Exception
	 */
	private void deleteNotification(ArrayList<String> ids) throws Exception {
		pd.show();
	
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

		
		SessionJsonObjectRequest deleteNotificationRequest = new SessionJsonObjectRequest(Method.POST,
				Define.URL+"fb/noticeDelete", jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
//								pd.dismiss();													
								getNotifacationForDate();			

							} else if("46000".equals(responseCode)){
								pd.dismiss();
								Toast.makeText(SystemNotificationA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(SystemNotificationA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							}else {
								pd.dismiss();
								Toast.makeText(SystemNotificationA.this, message, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(SystemNotificationA.this,R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);


					}
				});
		// 解决重复请求后台的问题
		deleteNotificationRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		deleteNotificationRequest.setTag("deleteNotificationRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(deleteNotificationRequest);

	}
	

	/**
	 * 按时间段查询通知列表
	 * 
	 * @throws Exception
	 */
	private void getNotifacationForDate() throws Exception {
		pd.show();
		startDate = tv_startDate.getText().toString();
		endDate = tv_endDate.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("validDateStart", startDate);
			jsonObject.put("validDateEnd", endDate);
			jsonObject.put("type", "");
			jsonObject.put("title", "");

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = Define.URL + "fb/noticeList";

		SessionJsonObjectRequest notifacationForDateRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								// int num = response.getInt("size");
								JSONArray jsonArray = response.getJSONArray("data");

								final List<NotificationBean> notificationData = new ArrayList<NotificationBean>();

								// num = Math.min(num, jsonArray.length());

								for (int i = 0; i < jsonArray.length(); i++) {
									NotificationBean notificationBean = new NotificationBean();

									JSONObject notifiObj = (JSONObject) jsonArray.get(i);
									String content = notifiObj.optString("content");// 通知内容
									String id = notifiObj.optString("id");// 通知id
									String title = notifiObj.optString("title");// 通知标题
									String validDateStart = notifiObj.optString("validDateStart");// 有效期开始时间
									String validDateEnd = notifiObj.optString("validDateEnd");// 有效期结束时间
									String type = notifiObj.optString("type");// 通知类型

									notificationBean.setNotificationId(id);
									notificationBean.setNotificationTitle(title);
									notificationBean.setNotificationContent(content);
									notificationBean.setNotificationType(type);
									notificationBean.setStartTime(validDateStart);
									notificationBean.setEndTime(validDateEnd);
									notificationData.add(notificationBean);
								}
								adapter.refreshData(notificationData);
								pd.dismiss();
								mListView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										NotificationBean ntfBean = notificationData.get(position);

										intent = new Intent(SystemNotificationA.this, SystemNotificationDetailsA.class);
										intent.putExtra("id", ntfBean.getNotificationId());
										intent.putExtra("title", ntfBean.getNotificationTitle());
										intent.putExtra("type", ntfBean.getNotificationType());
										intent.putExtra("startTime", ntfBean.getStartTime());
										intent.putExtra("endTime", ntfBean.getEndTime());
										intent.putExtra("content", ntfBean.getNotificationContent());

										startActivity(intent);
										overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

									}
								});

							} else if ("46000".equals(responseCode)) {
								pd.dismiss();
								Toast.makeText(SystemNotificationA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(SystemNotificationA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(SystemNotificationA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Log.d("exciption", "exciption =" + e.toString());
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// pd.dismiss();
						Toast.makeText(SystemNotificationA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("debug", "error = " + error.toString());

					}
				});
		// 解决重复请求后台的问题
		notifacationForDateRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		notifacationForDateRequest.setTag("notifacationForDateRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(notifacationForDateRequest);

	}

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("notifacationForDateRequest");
		MyApplication.getHttpQueues().cancelAll("deleteNotificationRequest");
	}

}

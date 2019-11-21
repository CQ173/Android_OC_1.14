package com.huoniao.oc.admin;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddNotificationA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private EditText et_notificationTitle, et_notificationContent;
	private LinearLayout layout_startDateChoice, layout_endDateChoice;
	private TextView tv_startDate, tv_endDate;
	private Button bt_submit;
	private String notificationTite;
	private String notificationType;
	private String notificationContent;
	private String startDate;
	private String endDate;
	private Spinner mySpinner;
	private List<String> list;
	private ArrayAdapter<String> adapter;
	private Intent intent;
//	private ProgressDialog cpd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_addnotification);
		initView();
		initData();
	}
	
	private void initData() {
		list = new ArrayList<String>();
		list.add("一般通知");
		list.add("重要通知");
	
		// 将数据加载到适配器
				adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
				//为适配器设置下拉列表下拉时的菜单样式。
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// 将适配器添加到下拉列表上
				mySpinner.setAdapter(adapter);
				// 为下拉列表设置各种事件的响应，这个事响应菜单被选中
				mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						/* 将所选mySpinner 的值赋值给type */
						notificationType = adapter.getItem(arg2);
						if ("一般通知".equals(notificationType)) {
							notificationType = Define.COMMONLY_NOTIFICATION;	
						}else {
							notificationType = Define.IMPORTANT_NOTIFICATION;
						}
						/* 将mySpinner 显示 */
						arg0.setVisibility(View.VISIBLE);
					}

					public void onNothingSelected(AdapterView<?> arg0) {
						notificationType = Define.COMMONLY_NOTIFICATION;
						arg0.setVisibility(View.VISIBLE);
					}
				});
				/* 下拉菜单弹出的内容选项触屏事件处理 */
				mySpinner.setOnTouchListener(new Spinner.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						/** 
						 *  
						 */
						return false;
					}
				});
				/* 下拉菜单弹出的内容选项焦点改变事件处理 */
				mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_notificationTitle = (EditText) findViewById(R.id.et_notificationTitle);
		et_notificationContent = (EditText) findViewById(R.id.et_notificationContent);
		layout_startDateChoice = (LinearLayout) findViewById(R.id.layout_startDateChoice);
		layout_endDateChoice = (LinearLayout) findViewById(R.id.layout_endDateChoice);
		tv_startDate = (TextView) findViewById(R.id.tv_startDate);
		tv_endDate = (TextView) findViewById(R.id.tv_endDate);
		bt_submit = (Button) findViewById(R.id.bt_submit);
		mySpinner = (Spinner) findViewById(R.id.sp_notifactionType);
//		pd = new CustomProgressDialog(this, "正在发布...", R.anim.frame_anim);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		// 获取当月第一天
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		cal.getTime();
//		tv_startDate.setText(sdf.format(cal.getTime()));
		tv_startDate.setText(sdf.format(new Date()));
		// 获取当月最后一天
//		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//		tv_endDate.setText(sdf.format(cal.getTime()));
		// 获取当前年月日
		tv_endDate.setText(sdf.format(new Date()));
		iv_back.setOnClickListener(this);
		layout_startDateChoice.setOnClickListener(this);
		layout_endDateChoice.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.layout_startDateChoice:
			Calendar cal = Calendar.getInstance();

			DatePickerDialog mDialog = new DatePickerDialog(AddNotificationA.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					tv_startDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
				
				}
			}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

			mDialog.show();
			break;
			
		case R.id.layout_endDateChoice:
			Calendar cal2 = Calendar.getInstance();

			DatePickerDialog mDialog2 = new DatePickerDialog(AddNotificationA.this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					tv_endDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
					
				}
			}, cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));

			mDialog2.show();
			break;
		case R.id.bt_submit:
			try {
				releaseNotifaction();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 删除字符串空格
	 * @param str
	 * @return
	 */
	public String removeAllSpace(String str)  
	{  
	    String tmpstr=str.replace(" ","");  
	    return tmpstr;  
	}   
	
	
	/**
	 * 发布通知
	 * 
	 * @throws Exception
	 */
	private void releaseNotifaction() throws Exception {
		cpd.show();
		cpd.setCustomPd("正在发布...");
		notificationTite = et_notificationTitle.getText().toString();
		notificationTite = removeAllSpace(notificationTite);
		notificationContent = et_notificationContent.getText().toString();
		notificationContent = removeAllSpace(notificationContent);
		startDate = tv_startDate.getText().toString();
		endDate = tv_endDate.getText().toString();
		if (notificationContent.isEmpty()) {
			cpd.dismiss();
			Toast.makeText(AddNotificationA.this, "通知内容不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
			
		if (notificationTite.isEmpty()) {
			cpd.dismiss();
			Toast.makeText(AddNotificationA.this, "通知标题不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("validDateStart", startDate);
			jsonObject.put("validDateEnd", endDate);
			jsonObject.put("type", notificationType);
			jsonObject.put("title", notificationTite);
			jsonObject.put("content", notificationContent);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = Define.URL + "fb/noticeAdd";
		
		
		SessionJsonObjectRequest releaseNotifactionRequest = new SessionJsonObjectRequest(Method.POST,
				url, jsonObject, new Listener<JSONObject>() {
			
					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								cpd.dismiss();
								Toast.makeText(AddNotificationA.this, "通知发布成功!", Toast.LENGTH_SHORT).show();
								finish();
											
								
							} else if("46000".equals(responseCode)){
								cpd.dismiss();
								Toast.makeText(AddNotificationA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(AddNotificationA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(AddNotificationA.this, message, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(AddNotificationA.this, "请求失败!", Toast.LENGTH_SHORT).show();
						Log.d("debug", "error = " + error.toString());

					}
				});
		// 解决重复请求后台的问题
		releaseNotifactionRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		releaseNotifactionRequest.setTag("releaseNotifactionRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(releaseNotifactionRequest);
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("releaseNotifactionRequest");
	}
}

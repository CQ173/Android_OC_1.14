package com.huoniao.oc.admin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FeedbackDetailsA extends BaseActivity implements OnClickListener{
	private ImageView iv_back;
	private Intent intent;
	private TextView tv_feedbackType, tv_dealtStatus, tv_feedbackContent;
	private EditText et_dealtOpinion;
	private Button bt_submit;
	private String id;//反馈信息id
	private String opinion = "";//处理意见
	private String feedbackType;//反馈类型
	private String status;//处理状态
	private String feedbackContent;//反馈内容
	private LinearLayout layout_handleContent;//处理意见区域
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_feedbackdetails);
		initView();
		initData();
	}
	
	private void initData() {
		intent = getIntent();
		id = intent.getStringExtra("id");
		opinion = intent.getStringExtra("opinion");
		et_dealtOpinion.setText(opinion);
		status = intent.getStringExtra("status");
		if (Define.UNTREATED.equals(status)) {
			tv_dealtStatus.setText("待处理");
		}else {
			tv_dealtStatus.setText("已处理");
			layout_handleContent.setVisibility(View.GONE);
			et_dealtOpinion.setKeyListener(null);//如何已处理就让它不再能输入处理意见
		}
		
		feedbackType = intent.getStringExtra("type");
		if (Define.CONSULTATION.equals(feedbackType)) {
			tv_feedbackType.setText("咨询");
		}else if (Define.PROPOSAL.equals(feedbackType)) {
			tv_feedbackType.setText("建议");
		}else if (Define.COMPLAINT.equals(feedbackType)) {
			tv_feedbackType.setText("投诉");
		}else {
			tv_feedbackType.setText("其它");
		}
		feedbackContent = intent.getStringExtra("content");
		
		tv_feedbackContent.setText(feedbackContent);
	}

	private void initView() {
		MyApplication.getInstence().addActivity(this);
		pd = new CustomProgressDialog(FeedbackDetailsA.this, "正在加载中...", R.drawable.frame_anim);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_feedbackType = (TextView) findViewById(R.id.tv_feedbackType);
		tv_dealtStatus = (TextView) findViewById(R.id.tv_dealtStatus);
		tv_feedbackContent = (TextView) findViewById(R.id.tv_feedbackContent);
		et_dealtOpinion = (EditText) findViewById(R.id.et_dealtOpinion);
		layout_handleContent = (LinearLayout) findViewById(R.id.layout_handleContent);
		bt_submit = (Button) findViewById(R.id.bt_submit);
		iv_back.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		//提交处理反馈	
		case R.id.bt_submit:
			try {
				submitOpinion();
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
	 * 提交处理反馈信息
	 * 
	 * @throws Exception
	 */
	private void submitOpinion() throws Exception {
		pd.show();
		opinion = et_dealtOpinion.getText().toString();
		opinion = removeAllSpace(opinion);
		if (opinion.isEmpty()) {
			pd.dismiss();
			Toast.makeText(FeedbackDetailsA.this, "处理意见不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", id);
			jsonObject.put("opinion", opinion);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = Define.URL + "fb/handle";
		
		
		SessionJsonObjectRequest submitOpinionRequest = new SessionJsonObjectRequest(Method.POST,
				url, jsonObject, new Listener<JSONObject>() {
			
					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
//								Toast.makeText(FeedbackDetailsA.this, "处理成功!", Toast.LENGTH_SHORT).show();
								refreshFeedbackList();
								
							} else if("46000".equals(responseCode)){
								pd.dismiss();
								Toast.makeText(FeedbackDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(FeedbackDetailsA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(FeedbackDetailsA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Log.d("exciption", "exciption =" + e.toString());
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
//						pd.dismiss();
						Toast.makeText(FeedbackDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("debug", "error = " + error.toString());

					}
				});
		// 解决重复请求后台的问题
		submitOpinionRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		submitOpinionRequest.setTag("submitOpinion");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(submitOpinionRequest);

	}
	
	/**
	 * 更新反馈信息列表
	 * 
	 * @throws Exception
	 */
	private void refreshFeedbackList() throws Exception {
//		pd.show();
	
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("beginDate", "");
			jsonObject.put("endDate", "");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = Define.URL + "fb/feedbackList";
		
		
		SessionJsonObjectRequest refreshFeedbackDataRequest = new SessionJsonObjectRequest(Method.POST,
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
									String content = feedbackObj.optString("content");// 反馈内容
									String id = feedbackObj.optString("id");// 反馈信息id
									String opinion = feedbackObj.optString("opinion");// 处理意见
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

								MyApplication.getInstence().activityFinish();
								intent = new Intent(FeedbackDetailsA.this, FeedbackA.class);
								Bundle bundle = new Bundle();

								bundle.putSerializable("feedbackData", (Serializable) feedbackData);
								
								intent.putExtras(bundle);
								pd.dismiss();
								Toast.makeText(FeedbackDetailsA.this, "处理成功!", Toast.LENGTH_SHORT).show();
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
								
								
								
							} else if("46000".equals(responseCode)){
								pd.dismiss();
								Toast.makeText(FeedbackDetailsA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(FeedbackDetailsA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(FeedbackDetailsA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Log.d("exciption", "exciption =" + e.toString());
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
//						pd.dismiss();
						Toast.makeText(FeedbackDetailsA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("debug", "error = " + error.toString());

					}
				});
		// 解决重复请求后台的问题
		refreshFeedbackDataRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		refreshFeedbackDataRequest.setTag("refereshFeedbackData");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(refreshFeedbackDataRequest);

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("submitOpinion");
		MyApplication.getHttpQueues().cancelAll("refereshFeedbackData");
	}
}

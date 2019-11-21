package com.huoniao.oc.umenpush;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class PushMsgHandleA extends BaseActivity implements OnClickListener{
	private Intent intent;
	private ImageView iv_back;
	private TextView tv_messageDetails, tv_messageTitle;
	private Button bt_confirm;
	private String message;
	private String title;
	private String type;
	private String paymentId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pushmsghandle);
		initView();
		initData();
	}
	
	
	private void initData() {
		intent = getIntent();
		title = intent.getStringExtra("title");	
		message = intent.getStringExtra("message");	
		type = intent.getStringExtra("type");
		paymentId = intent.getStringExtra("paymentId");
		tv_messageDetails.setText(message);
		tv_messageTitle.setText(title);
	}


	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_messageDetails = (TextView) findViewById(R.id.tv_messageDetails);
		tv_messageTitle = (TextView) findViewById(R.id.tv_messageTitle);
		bt_confirm = (Button) findViewById(R.id.bt_confirm);
		iv_back.setOnClickListener(this);
		bt_confirm.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.bt_confirm:
			bt_confirm.setEnabled(false);
			if (type != null && !type.isEmpty()) {
				if ("1".equals(type)) {
					bt_confirm.setEnabled(true);
//					intent = new Intent(PushMsgHandleA.this, LoginA.class);
//			        startActivity(intent);
//			        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					finish();
				}else if ("2".equals(type)){
					try {
						confirmPush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			break;
		default:
			break;
		}
		
	}
	/**
	 * 确定推送消息类型为2
	 *
	 * @throws Exception
	 */
	private void confirmPush() throws Exception {
		cpd.show();
//		pd.show();
		JSONObject jsonObject = new JSONObject();
		try {
			if (paymentId != null && !paymentId.isEmpty()){
			jsonObject.put("paymentId", paymentId);
			}else {
				jsonObject.put("", "");
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		String url = Define.URL + "fb/paymentConfirm";


		SessionJsonObjectRequest confirmPushRequest = new SessionJsonObjectRequest(Request.Method.POST,
				url, jsonObject, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d("debug", "response =" + response.toString());
				try {
					String responseCode = response.getString("code");
					String message = response.getString("msg");
					if ("0".equals(responseCode)) {
						bt_confirm.setEnabled(true);
						cpd.dismiss();
//						pd.dismiss();
						Toast.makeText(PushMsgHandleA.this, "您的汇缴已扣款成功!", Toast.LENGTH_SHORT).show();
						finish();


					} else if("46000".equals(responseCode)){
						bt_confirm.setEnabled(true);
						cpd.dismiss();
//						pd.dismiss();
						Toast.makeText(PushMsgHandleA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
						intent = new Intent(PushMsgHandleA.this, LoginA.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					} else {
						bt_confirm.setEnabled(true);
						cpd.dismiss();
//						pd.dismiss();
						Toast.makeText(PushMsgHandleA.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.d("exciption", "exciption =" + e.toString());
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				bt_confirm.setEnabled(true);
				cpd.dismiss();
//				pd.dismiss();
				Toast.makeText(PushMsgHandleA.this, R.string.netError, Toast.LENGTH_SHORT).show();
				Log.d("debug", "error = " + error.toString());

			}
		});
		// 解决重复请求后台的问题
		confirmPushRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		confirmPushRequest.setTag("confirmPushRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(confirmPushRequest);

	}

}

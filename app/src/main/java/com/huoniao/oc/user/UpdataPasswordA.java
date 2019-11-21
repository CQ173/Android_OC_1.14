package com.huoniao.oc.user;


import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MainActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdataPasswordA extends BaseActivity implements OnClickListener{
	private String oldPassword, newPassword, confirmPassword;
	private EditText et_oldPassword, et_newPassword, et_confirmPassword;
	private Button bt_confirmUpdata;
	private ImageView iv_back;
	
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_updata_password);
		initView();
//		ExitApplication.getInstance().addActivity(this);
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_oldPassword = (EditText) findViewById(R.id.et_oldPassword);
		et_newPassword = (EditText) findViewById(R.id.et_newPassword);
		et_confirmPassword = (EditText) findViewById(R.id.et_confirmPassword);
		bt_confirmUpdata = (Button) findViewById(R.id.bt_confirmUpdata);
		iv_back.setOnClickListener(this);
		bt_confirmUpdata.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
		case R.id.bt_confirmUpdata:
			try {
				updataPassword();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
			
		default:
			break;
		}
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("updataPassword");
	}
	
	private void updataPassword() throws Exception {
//		pd.show();
		oldPassword = et_oldPassword.getText().toString();
		newPassword = et_newPassword.getText().toString();
		confirmPassword = et_confirmPassword.getText().toString();
		if (oldPassword.isEmpty()) {
			Toast.makeText(UpdataPasswordA.this, "旧密码不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (newPassword.isEmpty()) {
			Toast.makeText(UpdataPasswordA.this, "新密码不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (confirmPassword.isEmpty()) {
			Toast.makeText(UpdataPasswordA.this, "确认密码不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!confirmPassword.equals(newPassword)) {
			Toast.makeText(UpdataPasswordA.this, "两次密码输入不一致!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (newPassword.length() < 6) {
			Toast.makeText(UpdataPasswordA.this, "新密码不能少于6位!", Toast.LENGTH_SHORT).show();
			return;
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("oldPassword", oldPassword);
		jsonObject.put("newPassword", confirmPassword);
		String url = Define.URL+"user/loginModifyPassword";
//		String url = "http://192.168.0.117:8181/OC/app/user/loginModifyPassword";
		SessionJsonObjectRequest updatePasswordRequest = new SessionJsonObjectRequest(Method.POST,
				url, jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String code = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(code)) {
								Toast.makeText(UpdataPasswordA.this, "密码修改成功，下次登录记得用新密码哦!", Toast.LENGTH_SHORT).show();
								finish();

							} else if("46000".equals(code)){
//								pd.dismiss();
								Toast.makeText(UpdataPasswordA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UpdataPasswordA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
//								pd.dismiss();
								Toast.makeText(UpdataPasswordA.this, "旧密码输入错误！", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(UpdataPasswordA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);
					}
				});
		// 解决重复请求后台的问题
		updatePasswordRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		updatePasswordRequest.setTag("updataPassword");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(updatePasswordRequest);

	}

}

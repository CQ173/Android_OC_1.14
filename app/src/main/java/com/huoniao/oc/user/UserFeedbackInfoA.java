package com.huoniao.oc.user;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.huoniao.oc.adapter.MyTextWatcher;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.EditFilterUtils;
import com.huoniao.oc.util.RepeatClickUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserFeedbackInfoA extends BaseActivity implements OnClickListener {
	private ImageView iv_back;
	private Intent intent;
	//private List<String> list;
	//private Spinner mySpinner;
	private ArrayAdapter<String> adapter;
	private String type;
	private String content;
	private EditText et_feedbackContent;
	private TextView bt_submit;
	private ProgressDialog pd;
	private TextView tv_lenth;
	private TextView tv_type;
	private MyPopWindow myPopWindowType;
	private ArrayList list;
	private float xs;
	private float ys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_feedbackinfo);
		initView();
		initWidget();
		initData();
	}

	private void initData() {
		list = new ArrayList<String>();
		list.add("请选择");
		list.add("咨询");
		list.add("建议");
		list.add("投诉");
		list.add("其他");
	}

	private void initWidget() {
		et_feedbackContent.addTextChangedListener(new MyTextWatcher(){
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				super.onTextChanged(s, start, before, count);
				tv_lenth.setText(et_feedbackContent.getText().toString().length()+"/240");
			}
		});
	}

/*	private void initData() {
		list = new ArrayList<String>();
		list.add("咨询");
		list.add("建议");
		list.add("投诉");
		list.add("其它");
		
		// 将数据加载到适配器
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		//为适配器设置下拉列表下拉时的菜单样式。
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将适配器添加到下拉列表上
		mySpinner.setAdapter(adapter);
		// 为下拉列表设置各种事件的响应，这个事响应菜单被选中
		mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				*//* 将所选mySpinner 的值赋值给type *//*
				type = adapter.getItem(arg2);
				if ("咨询".equals(type)) {
					type = Define.CONSULTATION;
				}else if ("建议".equals(type)) {
					type = Define.PROPOSAL;
				}else if ("投诉".equals(type)) {
					type = Define.COMPLAINT;
				}else {
					type = Define.OTHER;
				}
				*//* 将mySpinner 显示 *//*
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				type = Define.CONSULTATION;
				arg0.setVisibility(View.VISIBLE);
			}
		});
		*//* 下拉菜单弹出的内容选项触屏事件处理 *//*
		mySpinner.setOnTouchListener(new Spinner.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				*//**
				 *  
				 *//*
				return false;
			}
		});
		*//* 下拉菜单弹出的内容选项焦点改变事件处理 *//*
		mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

			}
		});
	}*/

	private void initView() {
		pd = new CustomProgressDialog(UserFeedbackInfoA.this, "正在加载中...", R.drawable.frame_anim);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_type = (TextView) findViewById(R.id.tv_type);
		//mySpinner = (Spinner) findViewById(R.id.sp_feedbackType);
		et_feedbackContent = (EditText) findViewById(R.id.et_feedbackContent);
		bt_submit = (TextView) findViewById(R.id.bt_submit);
		//文字内容实时长度
		tv_lenth = (TextView) findViewById(R.id.tv_lenth);
		iv_back.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		tv_type.setOnClickListener(this);
		et_feedbackContent.setFilters(new InputFilter[]{EditFilterUtils.inputFilter,new InputFilter.LengthFilter(240)});  //过滤表情

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		//提交反馈信息	
		case R.id.bt_submit:
			try {
				if(RepeatClickUtils.repeatClick()) {
					submitFeedbackInfo();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			case R.id.tv_type://反馈类型
				if(RepeatClickUtils.repeatClick()){
					showType();
				}
				break;
		default:
			break;
		}

	}

	/**
	 * 类型弹出窗
	 */
	/*private void showType() {
		myPopWindowType = new MyPopAbstract() {
                @Override
                protected void setMapSettingViewWidget(View view) {
                    ListView lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
					int[] arr = new int[2];
					tv_type.getLocationOnScreen(arr);
					view.measure(0, 0);
					Rect frame = new Rect();
					getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
					xs = arr[0] + tv_type.getWidth() - view.getMeasuredWidth();
					ys = arr[1] + tv_type.getHeight();
					CommonAdapter commonAdapter = new CommonAdapter(UserFeedbackInfoA.this,list,R.layout.admin_item_audit_status_pop) {
						@Override
						public void convert(ViewHolder holder, Object o) {
							holder.setText(R.id.tv_text,(String)o);

						}
					};

					lv_audit_status.setAdapter(commonAdapter);

                }

                @Override
                protected int layout() {
                    return R.layout.admin_audit_status_pop;
                }
            }.poPwindow(this,false).showAtLocation(tv_type, Gravity.NO_GRAVITY,(int)xs,(int)ys);
		;
	}*/

   public void	 showType(){
	   if (myPopWindowType != null) {
		   myPopWindowType.dissmiss();
	   }

	   myPopWindowType = new MyPopAbstract() {
		   @Override
		   protected void setMapSettingViewWidget(View view) {
			   ListView lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
             lv_audit_status.setBackgroundDrawable(getResources().getDrawable(R.drawable.ownership_pop));
			   //这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
			   int[] arr = new int[2];
			   tv_type.getLocationOnScreen(arr);
			   view.measure(0, 0);
			   Rect frame = new Rect();
			   getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
			   xs = arr[0] + tv_type.getWidth() - view.getMeasuredWidth();
			   ys = arr[1] + tv_type.getHeight();

			   CommonAdapter commonAdapter = new CommonAdapter(UserFeedbackInfoA.this,list,R.layout.admin_item_audit_status_pop) {
				   @Override
				   public void convert(ViewHolder holder, Object o) {
					   holder.setText(R.id.tv_text,(String)o);

				   }
			   };
			   lv_audit_status.setAdapter(commonAdapter);
			   lv_audit_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				   @Override
				   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					   String  audit = (String) list.get(i); //获取点击的文字
					   tv_type.setText(audit);
					   if ("咨询".equals(audit)) {
						   type = Define.CONSULTATION;
					   }else if ("建议".equals(audit)) {
						   type = Define.PROPOSAL;
					   }else if ("投诉".equals(audit)) {
						   type = Define.COMPLAINT;
					   }else if("其他".equals(audit)){
						   type = Define.OTHER;
					   }else if("请选择".equals(audit)){
						   type = Define.SELECT;
					   }
					   myPopWindowType.dissmiss();
				   }
			   });
		   }

		   @Override
		   protected int layout() {
			   return R.layout.admin_audit_status_pop;
		   }
	   }.popupWindowBuilder(this).create();
	   myPopWindowType.keyCodeDismiss(false);
	   myPopWindowType.showAsDropDown(tv_type);
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
	 * 提交反馈信息
	 * 
	 * @throws Exception
	 */
	private void submitFeedbackInfo() throws Exception {

		content = et_feedbackContent.getText().toString();
		//content = removeAllSpace(content);
		if(type==null || type.equals(Define.SELECT)){
			ToastUtils.showToast(UserFeedbackInfoA.this,"请选择，类型！");
			return;
		}
		if (content.isEmpty()) {
			pd.dismiss();
			Toast.makeText(UserFeedbackInfoA.this, "反馈内容不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		 if(content.length()<10){
			 ToastUtils.showToast(UserFeedbackInfoA.this,"不能少于10个字符！");
			 return;
		 }



		pd.show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("type",  type);
			jsonObject.put("content", content);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = Define.URL + "fb/feedbackAdd";
		
		
		SessionJsonObjectRequest submitfeedbackRequest = new SessionJsonObjectRequest(Method.POST,
				url, jsonObject, new Listener<JSONObject>() {
			
					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
								pd.dismiss();
							/*	Toast.makeText(UserFeedbackInfoA.this, "提交反馈信息成功!", Toast.LENGTH_SHORT).show();
								finish();*/
								commitShows();
								
							} else if("46000".equals(responseCode)){
								pd.dismiss();
								Toast.makeText(UserFeedbackInfoA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(UserFeedbackInfoA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								pd.dismiss();
								Toast.makeText(UserFeedbackInfoA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Log.d("exciption", "exciption =" + e.toString());
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						pd.dismiss();
						Toast.makeText(UserFeedbackInfoA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("debug", "error = " + error.toString());

					}
				});
		// 解决重复请求后台的问题
		submitfeedbackRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		submitfeedbackRequest.setTag("submitfeedbackRequest");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(submitfeedbackRequest);
		
	}

	private MyPopWindow  myPopWindow ;
	public void commitShows(){
		if(myPopWindow !=null){
			myPopWindow.dissmiss();
		}

		myPopWindow = new MyPopAbstract(){
			@Override
			protected void setMapSettingViewWidget(View view) {
			 	TextView title = (TextView) view.findViewById(R.id.title);	 //标题
		        TextView tv_content =	(TextView) view.findViewById(R.id.tv_pop_phone);  //内容
				LinearLayout ll_cancle = (LinearLayout) view.findViewById(R.id.ll_cancel);//取消容器
				title.setText("提交成功");
				tv_content.setText("谢谢您的建议，我们将持续为您改进");
				ll_cancle.setVisibility(View.GONE);
				TextView confirm = (TextView) view.findViewById(R.id.tv_call_cancle);//确定
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						myPopWindow.dissmiss();
						finish();
					}
				});

			}

			@Override
			protected int layout() {
				return R.layout.pop_message_all_delete;
			}
		}.poPwindow(UserFeedbackInfoA.this,false).showAtLocation(iv_back, Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("submitfeedbackRequest");
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if(myPopWindow !=null && myPopWindow.isShow()){
					//如果popwindow是显示状态就 不要做任何处理
					return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}

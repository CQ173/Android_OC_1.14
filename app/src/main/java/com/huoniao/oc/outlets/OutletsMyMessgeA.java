package com.huoniao.oc.outlets;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
import com.huoniao.oc.bean.OutletsMyMessageBean;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.MainMessageDetailsA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.SPUtils;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OutletsMyMessgeA extends BaseActivity implements OnClickListener{


	private ImageView iv_back;
	private ListView mListView;
	private List<OutletsMyMessageBean> mDatas;
	
	private Intent intent;
	private String id;
	private String infoStatus;
	private String content;
	private String infoDate;
	private TextView tv_allOption;
	private TextView tv_messageCount;
	CommonAdapter<OutletsMyMessageBean> adapter;
	private OutletsMyMessageBean msgBean;
//	private ProgressDialog pd;
	boolean visflag = true; //记录当前是全选还是反选
//	private LinearLayout layout_allOption, layout_delete;
	private TextView tv_messageState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_outlets_mymessage);
			initView();
			initData();
			
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		intent = getIntent();

		mDatas = (List<OutletsMyMessageBean>) intent.getSerializableExtra("oltMsgData");

//		mListView.setAdapter(new CommonAdapter<OutletsMyMessageBean>(this, mDatas, R.activity_windowanchored.outlets_mymessage_item) {
//
//			@Override
//			public void convert(ViewHolder holder, final OutletsMyMessageBean msgBean) {
//				holder.setText(R.id.tv_content, msgBean.getContent())
//					  .setText(R.id.tv_data, msgBean.getInfoDate());
//				CheckBox checkBox = holder.getView(R.id.checkBox);
//
//				checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			            @Override
//			            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			            	if (isChecked) {
//			            		msgBean.setCheckFlag(true);
//			                } else {
//			                	msgBean.setCheckFlag(false);
//			                }
//			            }
//			        });
//				checkBox.setChecked(msgBean.isCheckFlag());
//
//			}
//		});
		int msgCount = mDatas.size();
//		String msgCountStr = String.valueOf(msgCount);
		String msgCountStr = "（" + msgCount + "）";
		tv_messageCount.setText(msgCountStr);

		if(mDatas==null || mDatas.size()<=0){
			Toast.makeText(this, "暂无消息！", Toast.LENGTH_SHORT).show();
			tv_messageState.setVisibility(View.VISIBLE);
		}else{
			tv_messageState.setVisibility(View.GONE);
		}

		adapter = new CommonAdapter<OutletsMyMessageBean>(this, mDatas, R.layout.outlets_mymessage_item) {

			@Override
			public void convert(ViewHolder holder, final OutletsMyMessageBean msg) {
				holder.setText(R.id.tv_content, msg.getContent())
						.setText(R.id.tv_data, msg.getInfoDate());
	          /*
	           TODO 后面消息红点消息需要做处理  重要！！！！！不要删除
	           TextView tvMessageReader = holder.getView(R.id.tv_message_reader);
				tvMessageReader.setTag(msg.getId());   //设置tag防止条目错乱
			    String messageId= (String) SPUtils.get(OutletsMyMessgeA.this,msg.getId(),"");
				if(messageId.isEmpty()){  //如果不存在就保存
					SPUtils.put(OutletsMyMessgeA.this,msg.getId(),"");
				}

				if(tvMessageReader.getTag().equals(msg.getId())){
					if(SPUtils.get(OutletsMyMessgeA.this,msg.getId(),"").equals(msg.getId())){
						tvMessageReader.setVisibility(View.GONE);
					}else{
						tvMessageReader.setVisibility(View.VISIBLE);
					}
				}*/




//			CheckBox checkBox = holder.getView(R.id.checkBox);
//
//			checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//		            @Override
//		            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		            	if (isChecked) {
//		            		msg.setCheckFlag(true);
//		                } else {
//		                	msg.setCheckFlag(false);
//		                }
//		            }
//		        });
//			checkBox.setChecked(msg.isCheckFlag());

			}
		};
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				OutletsMyMessageBean myMessage = mDatas.get(i);
				SPUtils.put(OutletsMyMessgeA.this,myMessage.getId(), myMessage.getId());
				intent = new Intent(OutletsMyMessgeA.this, MainMessageDetailsA.class);
				intent.putExtra("content", myMessage.getContent());
				intent.putExtra("date", myMessage.getInfoDate());

				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
			}
		});

		Log.d("mydata", "content =" + content);
	}
	
	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		mListView = (ListView) findViewById(R.id.myMessageListView);
//		layout_allOption = (LinearLayout) findViewById(layout_allOption);
//		layout_delete = (LinearLayout) findViewById(layout_delete);
//		tv_allOption = (TextView) findViewById(R.id.tv_allOption);
		tv_messageState = (TextView) findViewById(R.id.tv_message_state);  //消息状态 显示状态
		tv_messageCount = (TextView) findViewById(R.id.tv_messageCount);  //消息数目
		iv_back.setOnClickListener(this);
//		layout_allOption.setOnClickListener(this);
//		layout_delete.setOnClickListener(this);
//		tv_allOption.setOnClickListener(this);
//		pd = new CustomProgressDialog(OutletsMyMessgeA.this, "正在加载中...", R.anim.frame_anim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
			
//		case R.id.layout_allOption:
//			for (int i = 0; i < adapter.getCount(); ++i) {
//				OutletsMyMessageBean msg = (OutletsMyMessageBean)mListView.getItemAtPosition(i);
//				if (visflag || !msg.isCheckFlag())
//					msg.setCheckFlag(true);
//				else
//					msg.setCheckFlag(false);
//			}
//			visflag = !visflag;
//			if(visflag) tv_allOption.setText("全选");
//			else tv_allOption.setText("反选");
//			adapter.notifyDataSetChanged();
//			break;
			
//		case R.id.layout_delete:
//			//找出所有勾选的消息，再通知服务端删除
//			ArrayList<String> list = new ArrayList<String>();
//			for (int i = 0; i < adapter.getCount(); ++i) {
//				OutletsMyMessageBean oltMsg = (OutletsMyMessageBean)mListView.getItemAtPosition(i);
//				if (oltMsg.isCheckFlag())
//					list.add(oltMsg.getId());
//			}
//
//			if (list.isEmpty()){
//
//				Toast.makeText(OutletsMyMessgeA.this, "请选择您要删除的消息!", Toast.LENGTH_SHORT).show();
//
//				return;
//			}
//			try {
//				deleteMessage(list);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 删除消息
	 * 
	 * @throws Exception
	 */
	private void deleteMessage(ArrayList<String> ids) throws Exception {
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

		
		SessionJsonObjectRequest deleteMsgRequest = new SessionJsonObjectRequest(Method.POST,
				Define.URL+"fb/infoDelete", jsonObject, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(responseCode)) {
//								pd.dismiss();													
								getOutletsMessageData();			

							} else if("46000".equals(responseCode)){
								cpd.dismiss();
								Toast.makeText(OutletsMyMessgeA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(OutletsMyMessgeA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							}else {
								cpd.dismiss();
								Toast.makeText(OutletsMyMessgeA.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						cpd.dismiss();
						Toast.makeText(OutletsMyMessgeA.this, R.string.netError, Toast.LENGTH_SHORT).show();
						Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);


					}
				});
		// 解决重复请求后台的问题
		deleteMsgRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		deleteMsgRequest.setTag("deleteMessage");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(deleteMsgRequest);

	}
	
	/**
	 * 刷新'我的消息'
	 * 
	 * @throws Exception
	 */
	private void getOutletsMessageData() throws Exception {
		cpd.show();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("limit", -1);
		// String url = "http://ecy.120368.com/app/fb/infoList";
		String url = Define.URL + "fb/infoList";
		final List<OutletsMyMessageBean> oltMessageList = new ArrayList<OutletsMyMessageBean>();
		SessionJsonObjectRequest myMessageRequest = new SessionJsonObjectRequest(Method.POST, url, jsonObject,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d("debug", "response =" + response.toString());
						try {
							String code = response.getString("code");
							String message = response.getString("msg");
							if ("0".equals(code)) {
								JSONArray jsonArray = response.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									OutletsMyMessageBean oltMsgBean = new OutletsMyMessageBean();

									JSONObject myMessageData = (JSONObject) jsonArray.get(i);
									id = myMessageData.getString("id");
									infoStatus = myMessageData.getString("infoStatus");
									content = myMessageData.getString("content");
									infoDate = myMessageData.getString("infoDate");
									oltMsgBean.setContent(content);
									oltMsgBean.setInfoDate(infoDate);
									oltMsgBean.setInfoStatus(infoStatus);
									oltMsgBean.setId(id);

									Log.d("debug", oltMsgBean.getInfoDate());
									oltMessageList.add(oltMsgBean);
								}

								cpd.dismiss();
								adapter.refreshData(oltMessageList);
								Toast.makeText(OutletsMyMessgeA.this, "删除成功!", Toast.LENGTH_SHORT).show();
							} else if ("46000".equals(code)) {
								cpd.dismiss();
								Toast.makeText(OutletsMyMessgeA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								intent = new Intent(OutletsMyMessgeA.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(OutletsMyMessgeA.this, message, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(OutletsMyMessgeA.this, R.string.netError, Toast.LENGTH_SHORT).show();

					}
				});
		// 解决重复请求后台的问题
		myMessageRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		myMessageRequest.setTag("refreshMyMessage");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(myMessageRequest);

	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueues().cancelAll("deleteMessage");
		MyApplication.getHttpQueues().cancelAll("refreshMyMessage");
	}

	@Override
	protected void onStart() {
		super.onStart();
		adapter.notifyDataSetChanged();
	}
}

package com.huoniao.oc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.bean.OltsCapitalFlowBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.common.MyPopAbstract;
import com.huoniao.oc.common.MyPopWindow;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.util.ViewHolder;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

//import com.umeng.message.PushAgent;

public class BaseActivity extends FragmentActivity{
	protected CustomProgressDialog cpd;
	protected  List<String> premissionsList = new ArrayList<>();
	protected  VolleyNetCommon volleyNetCommon;
	private File licenceFile;

	MyPopWindow myPopWindowPublic;  //公共弹出框
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppBaseTheme);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题
		setRequestedOrientation(ActivityInfo
				.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		ExitApplication.getInstance().addActivity(this);
		//PushAgent.getInstance(this).onAppStart();

		if(cpd == null) {
			cpd = new CustomProgressDialog(this, "正在加载中...", R.drawable.frame_anim);
			cpd.setCancelable(false);//设置进度条不可以按退回键取消
			cpd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
		}


		User loginResult = (User) ObjectSaveUtil.readObject(this, "loginResult");//获取登录时存储的相关 权限信息
		if(loginResult != null) {
			premissionsList = loginResult.getPremissions();
		}

		if(volleyNetCommon==null){
			volleyNetCommon = new VolleyNetCommon();
		}
	}

	protected  VolleyNetCommon getVolleyNetCommon(){
		return volleyNetCommon;
	}


	//抽取权限
	public   void setPremissionShowHideView(String premissionFlag, View view){
		for (String premissions: premissionsList ) {
			if(premissionFlag.equals(premissions)){
				view.setVisibility(View.VISIBLE); //如果有这个权限就显示  默认是隐藏的
				return;
			}else{
				view.setVisibility(View.GONE);
			}
		}
	}



	//跳转界面封装
	public void startActivityMethod(Class<?> cla){
		startActivity(new Intent(this,cla));
		overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	}

	//跳转界面
	public void startActivityIntent(Intent intent){
		startActivity(intent);
		overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
	}
	/**
	 * 获取代售点资金流水
	 *
	 * @throws Exception
	 */
	protected void getOltCapitalFlowDataE(String startMonth, String endMonth, final int pageNo, String str) throws Exception {
		cpd.show();

		JSONObject jsonObject = new JSONObject();
		try {
			if(startMonth==null || endMonth == null) {
				jsonObject.put("", "");
				jsonObject.put("pageNo", pageNo);
				jsonObject.put("str", str);
			}else {       // 按月查询资金流水   startMonth 开始日期   endMonth 结束日期
				jsonObject.put("beginDate", startMonth);
				jsonObject.put("endDate", endMonth);
				jsonObject.put("pageNo", pageNo);
				jsonObject.put("str", str);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		String url = Define.URL + "acct/tradeFlowList";
		final List<OltsCapitalFlowBean> capitalFlowData = new ArrayList<OltsCapitalFlowBean>();
		SessionJsonObjectRequest capitalFlowRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
				new Response.Listener<JSONObject>() {
					String incomeSum, payoutSum;
					double newIncomeSum;
					double newPayoutSum;
					@Override
					public void onResponse(JSONObject response) {
						if (response == null) {
							Toast.makeText(BaseActivity.this, "服务器数据异常", Toast.LENGTH_SHORT).show();
							cpd.dismiss();
							return;
						}
						Log.d("debug", "response =" + response.toString());
						try {
							String responseCode = response.getString("code");
							String message = response.optString("msg");
							int nextPage = response.optInt("next");
							if ("0".equals(responseCode)) {
								int num = response.getInt("size");
								JSONArray jsonArray = response.getJSONArray("data");
								num = Math.min(num, jsonArray.length());
								// for (int i = 0; i < jsonArray.length(); i++)
								// {
								/*double incomeCount = 0 ;// 总收入
								double payoutCount = 0 ; //总支出*/
								for (int i = 0; i < num; i++) {
									OltsCapitalFlowBean cptlFlowBean = new OltsCapitalFlowBean();

									JSONObject cptlFlowObj = (JSONObject) jsonArray.get(i);
									String flowId = cptlFlowObj.optString("flowId");// 交易流水号
									String tradeName = cptlFlowObj.optString("tradeName");// 名称
									JSONObject tradeUser = cptlFlowObj.optJSONObject("tradeUser");// 交易的用户信息
									String curBalance = cptlFlowObj.optString("curBalanceString");// 用户当前余额
									String updateDate = cptlFlowObj.optString("updateDate");// 更新时间
									String loginName = tradeUser.optString("loginName");//交易账户-用户名
									String name = tradeUser.optString("name");//用户姓名

									// 用optString()解析字段可以防止字段没有值时抛异常
									String tradeAcct = cptlFlowObj.optString("tradeAcct");// 交易账号
									if ("".equals(tradeAcct) || tradeAcct == null) {
										tradeAcct = "无";
									}
									String tradeFee = cptlFlowObj.optString("tradeFeeString");// 交易金额

									String type = cptlFlowObj.optString("type");// 交易类型
									// income:收入
									// payout:支出
									String tradeStatus = cptlFlowObj.optString("tradeStatus");// 交易状态
									String tradeDate = cptlFlowObj.optString("tradeDate");// 交易日期
									String channelType =cptlFlowObj.optString("channelType"); //交易渠道
									String sourceFlowId = cptlFlowObj.optString("sourceFlowId"); //关联流水号
									String expireTime = cptlFlowObj.optString("expireTime"); // 超时时间
									cptlFlowBean.setFlowId(flowId);
									cptlFlowBean.setTradeName(tradeName);
									cptlFlowBean.setLoginName(loginName);
									cptlFlowBean.setSourceFlowId(sourceFlowId);
									cptlFlowBean.setName(name);
									// oltsRelation.setArea(area);
									cptlFlowBean.setTradeAcct(tradeAcct);
									cptlFlowBean.setTradeFee(tradeFee);
									cptlFlowBean.setCurBalance(curBalance);
									cptlFlowBean.setType(type);
									cptlFlowBean.setTradeStatus(tradeStatus);
									cptlFlowBean.setTradeDate(tradeDate);
									cptlFlowBean.setChannelType(channelType);
									cptlFlowBean.setExpireTime(expireTime);
									cptlFlowBean.setUpdateDate(updateDate);
									Log.d("debug", cptlFlowBean.getFlowId());
									capitalFlowData.add(cptlFlowBean);


									// 收入总金额  交易失败不加 退款中不加，等待付款不加，已退款不加
									//支出总金额  一样
									/*if(Define.INCOME.equals(type)){
										if(!(Define.ACCT_TRADE_STATUS_FAIL.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUNDING.equals(tradeStatus)
												|| Define.ACCT_TRADE_STATUS_WAIT.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUND.equals(tradeStatus))) {
											incomeCount += Double.parseDouble(tradeFee);
										}
									}else if(Define.PAYOUT.equals(type)){
										if(!(Define.ACCT_TRADE_STATUS_FAIL.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUNDING.equals(tradeStatus)
												|| Define.ACCT_TRADE_STATUS_WAIT.equals(tradeStatus) || Define.ACCT_TRADE_STATUS_REFUND.equals(tradeStatus))) {
											payoutCount+= Double.parseDouble(tradeFee);
										}

									}*/

								}
								if (pageNo == 1){
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject cptfObj = (JSONObject) jsonArray.get(i);
										incomeSum = cptfObj.optString("incomeSum");// 收入总金额
										payoutSum = cptfObj.optString("payoutSum");// 支出总金额
									}
									newIncomeSum = Double.parseDouble(incomeSum);
									newPayoutSum = Double.parseDouble(payoutSum);
									getIncomeCountAndPayout(newIncomeSum,newPayoutSum);
								}

								if(pageNo==1 && nextPage==2) {
									getDataOltCapitalFlow(capitalFlowData, 2);
								}else{
									getDataOltCapitalFlow(capitalFlowData, nextPage);
								}
								cpd.dismiss();

							}  else if ("40035".equals(responseCode)) {
								cpd.dismiss();
								Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
								getDataOltCapitalFlow(capitalFlowData, nextPage);
							} else if ("46000".equals(responseCode)) {
								cpd.dismiss();
								Toast.makeText(BaseActivity.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(BaseActivity.this, LoginA.class);
								startActivity(intent);
								overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
							} else {
								cpd.dismiss();
								Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							cpd.dismiss();
							e.printStackTrace();

						}

					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(BaseActivity.this, R.string.netError, Toast.LENGTH_SHORT).show();
				Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

			}
		});
		// 解决重复请求后台的问题
		capitalFlowRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		capitalFlowRequest.setTag("capitalFlow");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(capitalFlowRequest);

	}


	//获取代售点资金流水集合数据
	protected  void getDataOltCapitalFlow(List<OltsCapitalFlowBean> capitalFlowData, int nextPage){

	}

	//获取支出和收入  总额
	protected  void getIncomeCountAndPayout(double incomeCount, double payoutCount){

	}

	/**
	 * 查看图片(可放大)
	 * @param context 上下文
	 * @param imgUrl 图片相对路径
	 * @param view 控件
	 */
	protected void enlargeImage(final Context context, final String imgUrl, View view){
//		final Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() * SCALE, bitmap.getHeight() * SCALE);
		MyPopWindow myPopWindow = new MyPopAbstract() {
			@Override
			protected void setMapSettingViewWidget(View view) {
				PhotoView documentImage = (PhotoView) view.findViewById(R.id.documentImage);
				if (imgUrl != null && !imgUrl.isEmpty()){

					/*Glide.with(context).load(Define.IMG_URL + imgUrl).listener(new RequestListener<String, GlideDrawable>() {
						@Override
						public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
							Toast.makeText(MyApplication.mContext, "图片链接错误！", Toast.LENGTH_SHORT).show();
							return false;
						}

						@Override
						public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
							return false;
						}
					}).into(documentImage);*/
					try {
						getDocumentImage(imgUrl, documentImage);
					} catch (Exception e) {
						e.printStackTrace();
					}


				}else {
					Toast.makeText(BaseActivity.this, "无图片信息！", Toast.LENGTH_SHORT).show();
				}

			}
			@Override
			protected int layout() {
				return R.layout.documentimage_dialog;
			}
		}.popWindowTouch(BaseActivity.this).showAtLocation(view, Gravity.CENTER,0,0);
	}

	/**
	 * 获取服务器图片接口
	 * @param imgUrl
	 * @param imageView
	 * @throws Exception
	 */
	protected void getDocumentImage(String imgUrl, final PhotoView imageView) throws Exception {
		cpd.show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("imageSrc", imgUrl);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}


		SessionJsonObjectRequest documentImageRequest = new SessionJsonObjectRequest(Request.Method.POST,
				Define.URL+"fb/getImgBase64BySrc", jsonObject, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d("userInfo", "response =" + response.toString());
				try {
					String responseCode = response.getString("code");
					String message = response.getString("msg");
					if ("0".equals(responseCode)) {
						String imgString = 	response.optString("data");
						Bitmap licenceBitmap = Base64ConvertBitmap.base64ToBitmap(imgString);
						imageView.setImageBitmap(licenceBitmap);
						cpd.dismiss();

					} else if("46000".equals(responseCode)){
						cpd.dismiss();
						Toast.makeText(BaseActivity.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(BaseActivity.this, LoginA.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					} else {
						cpd.dismiss();
						Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
						Log.d("debug", message);
					}
				} catch (JSONException e) {
					cpd.dismiss();
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(BaseActivity.this, R.string.netError, Toast.LENGTH_SHORT).show();
				Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

			}
		});
		// 解决重复请求后台的问题
		documentImageRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		documentImageRequest.setTag("documentImage");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(documentImageRequest);

	}

	Bitmap licenceBitmap;
	/**
	 * 获取服务器图片,并将图片显示在控件上
	 * @param imgUrl
	 *
	 * @param i
	 * @throws Exception
	 */
	protected void getDocumentImage2(String imgUrl, final String tag, final int i) throws Exception {
		cpd.show();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("imageSrc", imgUrl);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (volleyNetCommon == null) {
			volleyNetCommon = new VolleyNetCommon();
		}
		JsonObjectRequest abb = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "fb/getImgBase64BySrc", jsonObject, new VolleyAbstract(this) {
			@Override
			public void volleyResponse(Object o) {

			}

			@Override
			public void volleyError(VolleyError volleyError) {
				Toast.makeText(BaseActivity.this, R.string.netError, Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void netVolleyResponese(JSONObject json) {
				Log.e("abb", json.toString());
				String imgString = 	json.optString("data");
				licenceBitmap = Base64ConvertBitmap.base64ToBitmap(imgString);
//			;	imageView.setImageBitmap(licenceBitmap)
				getImageBitmap(licenceBitmap,tag,i);

			}

			@Override
			protected void PdDismiss() {
				cpd.dismiss();
			}
		},"documentImage", true);
		volleyNetCommon.addQueue(abb);


	/*	SessionJsonObjectRequest documentImageRequest = new SessionJsonObjectRequest(Request.Method.POST,
				Define.URL+"fb/getImgBase64BySrc", jsonObject, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d("userInfo", "response =" + response.toString());
				try {
					String responseCode = response.getString("code");
					String message = response.getString("msg");
					if ("0".equals(responseCode)) {
						String imgString = 	response.optString("data");
						licenceBitmap = Base64ConvertBitmap.base64ToBitmap(imgString);
//			;			imageView.setImageBitmap(licenceBitmap)
						getImageBitmap(licenceBitmap,tag);
						cpd.dismiss();


					} else if("46000".equals(responseCode)){
						cpd.dismiss();
						Toast.makeText(BaseActivity.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(BaseActivity.this, LoginA.class);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
					} else {
						cpd.dismiss();
						Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
						Log.d("debug", message);
					}
				} catch (JSONException e) {
					cpd.dismiss();
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				cpd.dismiss();
				Toast.makeText(BaseActivity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
				Log.d("REQUEST-ERROR", error.getMessage(), error);
//                   	  	byte[] htmlBodyBytes = error.networkResponse.data;
//                   	  	Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

			}
		});
		// 解决重复请求后台的问题
		documentImageRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
		documentImageRequest.setTag("documentImage2");
		// 将请求加入全局队列中
		MyApplication.getHttpQueues().add(documentImageRequest);*/

	}

	/**
	 * 请求网络
	 * @param url  网络链接
	 * @param jsonObject  携带参数
	 * @param tag  //标识哪个请求
	 * @param controlOff 开始这个网络请求请求成功后 是不是需要关闭
	 * @param repeatRequest 请求超时后是否需要重复请求（true为需要，false为不需要）
	 */
	public void requestNet(String url, JSONObject jsonObject, final String tag, final String pageNumber, final boolean controlOff, boolean repeatRequest){
		cpd.show();
		if(volleyNetCommon==null){
			volleyNetCommon = new VolleyNetCommon();
		}
		JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(this) {
			@Override
			public void volleyResponse(Object o) {

			}

			@Override
			public void volleyError(VolleyError volleyError) {
				Toast.makeText(BaseActivity.this, R.string.netError, Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void netVolleyResponese(JSONObject json) {
				dataSuccess(json, tag,pageNumber);
			}

			@Override
			protected void PdDismiss() {
				if(controlOff) { //true 表示可以关闭
					cpd.dismiss();
				}
				closeDismiss();
			}

			@Override
			protected void errorMessages(String message) {
				super.errorMessages(message);
				ToastUtils.showToast(MyApplication.mContext,message);
			}
		}, tag, repeatRequest);
		volleyNetCommon.addQueue(jsonObjectRequest);
	}

	//子类重写 获取数据
	protected void dataSuccess(JSONObject json, String tag, String pageNumber) {	}
	//关闭
	protected void  closeDismiss(){

	}


	private float xs;
	private float ys;

	//这个方法只有使用一个布局里面只有listview 可以使用
	public void showPop(final View llview, final List<? extends  Object> objectsList, final String tag, final int switchLayoutState){
		if(myPopWindowPublic != null){
			myPopWindowPublic.dissmiss();
		}
		myPopWindowPublic = new MyPopAbstract() {
			@Override
			protected void setMapSettingViewWidget(View view) {
				ListView lv_audit_status = (ListView) view.findViewById(R.id.lv_audit_status);
				//这个步骤是得到该view相对于屏幕的坐标, 注意不是相对于父布局哦!
				int[] arr = new int[2];
				llview.getLocationOnScreen(arr);
				view.measure(0, 0);
				Rect frame = new Rect();
				getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);//得到状态栏高度
				xs = arr[0] + llview.getWidth() - view.getMeasuredWidth();
				ys = arr[1] + llview.getHeight();
				CommonAdapter<? extends  Object> commonAdapter = new CommonAdapter(BaseActivity.this,objectsList,R.layout.admin_item_audit_status_pop) {
					@Override
					public void convert(ViewHolder holder, Object o) {
						setDataGetView(holder,o,tag);
					}
				};

				lv_audit_status.setAdapter(commonAdapter);
				lv_audit_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
						itemPopClick(adapterView,view,i,l,tag);
						myPopWindowPublic.dissmiss();
					}
				});
			}

			@Override
			protected int layout() {
				if(switchLayoutState==0) {  //等于0高度固定
					return R.layout.admin_audit_status_pop;
				}else{  //高度不固定
					return R.layout.admin_audit_status_pop4;
				}
			}
		}.popupWindowBuilder(this).create();

		myPopWindowPublic.keyCodeDismiss(true); //返回键关闭
		myPopWindowPublic.showAtLocation(llview,Gravity.NO_GRAVITY,(int)xs,(int)ys);
	}

	/**
	 * popwindow点击获取条目的数据
	 * @param adapterView
	 * @param view
	 * @param i
	 * @param l
	 */
	protected void itemPopClick(AdapterView<?> adapterView, View view, int i, long l,String tag) {

	}

	/**
	 * pop弹出框数据设置
	 * @param holder
	 * @param o
	 */
	protected void setDataGetView(ViewHolder holder, Object o,String tag) {

	}


	/**
	 * 获取服务器图片,并将图片显示在控件上
	 * @param imgUrl
	 *
	 * @param i
	 * @param linkUrl 轮播图链接，其它地方传空就好
	 * @throws Exception
	 */
	protected void getDocumentImage3(final String imgUrl, final String tag, final int i, boolean thumbnail, final String linkUrl) throws Exception {


		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("imageSrc", imgUrl);
			if(thumbnail) {
				jsonObject.put("abbreviation", "1");
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (volleyNetCommon == null) {
			volleyNetCommon = new VolleyNetCommon();
		}
		JsonObjectRequest abb = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "fb/getImgBase64BySrc", jsonObject, new VolleyAbstract(this) {
			@Override
			public void volleyResponse(Object o) {

			}

			@Override
			public void volleyError(VolleyError volleyError) {
				//	Toast.makeText(BaseActivity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
			}

			@Override
			protected void netVolleyResponese(JSONObject json) {
				Log.e("abb", json.toString());
				String imgString = json.optString("data");
				licenceFile = Base64ConvertBitmap.base64ToFile(imgString);
//			;	imageView.setImageBitmap(licenceBitmap)
				if(imgResult != null){
					imgResult.getImageFile(licenceFile,imgUrl,tag,i,linkUrl);
				}

			}

			@Override
			protected void PdDismiss() {

			}
		}, "documentImage", true);
		volleyNetCommon.addQueue(abb);

	}

	protected void getImageBitmap(Bitmap licenceBitmap, String tag,int i) {
	}

	protected void getImageFile(File licenceFile, String tag,int i) {

	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return  onkeyDown(keyCode,event);
	}

	protected boolean  onkeyDown(int keyCode,KeyEvent event){

		return super.onKeyDown(keyCode,event);
	}

	@Override
	protected void onStop() {
		super.onStop();
		cpd.dismiss();
		/*if (volleyNetCommon != null){
			volleyNetCommon.getRequestQueue().cancelAll("documentImage");
		}

		MyApplication.getHttpQueues().cancelAll("documentImage2");*/
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getSimpleName());
		MobclickAgent.onPause(this);
	}

	private ImgResult imgResult;
	public  void setImgResultLinstener(ImgResult imgResultLinstener){
		this.imgResult = imgResultLinstener;
	}
	public  	interface  ImgResult{
		void  getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr);
	}

	@Override
	public Resources getResources() {//设置app字体大小不受手机设置字体影响
		Resources res = super.getResources();
		Configuration config=new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config,res.getDisplayMetrics() );
		return res;
	}
}

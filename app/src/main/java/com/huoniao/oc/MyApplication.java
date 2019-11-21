package com.huoniao.oc;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.request.target.ViewTarget;
import com.chinaums.commondhjt.model.LoggingTimeout;
import com.chinaums.commondhjt.service.DHJTManager;
import com.huoniao.oc.common.SimpleActivityLifecycleCallbacks;
import com.huoniao.oc.common.XunFeiUtils;
import com.huoniao.oc.umenpush.PushMsgHandleA;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.volleynet.SsX509TrustManager;
import com.huoniao.oc.xunfeivoice.KqwSpeechCompound;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;*/

public class MyApplication extends Application {

	private static final String SET_COOKIE_KEY = "Set-Cookie";
	private static final String COOKIE_KEY = "Cookie";
	private static final String SESSION_COOKIE = "JSESSIONID";
	public static IWXAPI api;
	public static PushAgent mPushAgent;
	private static SharedPreferences preferences;
	private static MyApplication instance;
	private static MyApplication appInstance;
	private static RequestQueue queues;
	private Intent intent;
	public static Handler mainHandler;
	public static Context mContext;
    public static List<String> treeIdList = new ArrayList<>(); //全局保存火车站层级树点击动态增加节点搜索  记录添加过得节点 后面点击就不用访问网络了
	public static List<String> treeIdList2 = new ArrayList<>(); // 不做为地图
    public static String type ="0" ;//层级树点击弹气泡类行
    public static boolean updatePersonMessagePhone = false; //表示在修改个人信息中手机号进行修改记录 状态值
	public static boolean payPasswordIsEmpty = true;  //默认支付密码没有设置
    private List<Activity> activityList = new ArrayList<>();  //记录打开了多少个activity
	public static String serviceCode;
	public static String serviceVersionName;
	public static MyApplication newInstance() {
		return instance;
	}
	public static int infoNum=0;  //消息数量
	public  static String waitAgency; //待审核窗口数量
	public  static String waitUser; //待审核用户数量

	private static List<Activity> activitylist = Collections.synchronizedList(new ArrayList<Activity>());

	public static KqwSpeechCompound kqwSpeechCompound;

	public static boolean voiceRemindSwitch = true;//充值推送播报开关状态

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		instance = this;


		registerActivity();


		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		queues = Volley.newRequestQueue(getApplicationContext());
		SsX509TrustManager.allowAllSSL();   //允许https 访问
		mainHandler = new Handler();

		initPush();
		initPos();
       //百度地图初始化
		SDKInitializer.initialize(this);
		ViewTarget.setTagId(R.id.glide_tag);

		//二维码库初始化
		ZXingLibrary.initDisplayOpinion(this);

		String processName = getProcessName(this, android.os.Process.myPid());
		if(processName != null){
			boolean defaultProcess = processName.equals("com.huoniao.oc");
			if(defaultProcess) {
				//当前应用的初始化
				XunFeiUtils.getInstance();
			}
		}


	}

     public static  MyApplication getInstence(){

		 return instance;
	 }
     //添加activity
	 public void addActivity(Activity activity){
		 if(!activityList.contains(activity)) {
			 activityList.add(activity);
		 }
	 }

	 //销毁全部activity
	public void activityFinish(){
		for (Activity activity: activityList
			 ) {
			activity.finish();
		}
	}

	/**
	 * 结束指定的Activity
	 *
	 * @param activity
	 */

	public void finishActivity(Activity activity) {

		if (activity != null) {
			this.activityList.remove(activity);
			activity.finish();
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivityclass(Class<?> cls) {
		if (activityList != null) {
			for (Activity activity : activityList) {
				if (activity.getClass().equals(cls)) {
					this.activityList.remove(activity);
					finishActivity(activity);
					break;
				}
			}
		}

	}


	public  void initPos() {
		final String shopname="";
		final String username="";
		final ContentValues cv=new ContentValues();
		DHJTManager.getInstance().init(this, new LoggingTimeout() {
			public void outTime() {

			}
			public void inTime() {
			}
		});
	}


	/**
	 * 初始化友盟推送
	*/
	public void initPush(){
		mPushAgent = PushAgent.getInstance(this);
		//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {

		    @Override
		    public void onSuccess(String deviceToken) {
		        //注册成功会返回device token
		    	Log.d("myToken", "deviceToken = " + deviceToken);
		    }

		    @Override
		    public void onFailure(String s, String s1) {
				Log.d("error1", "error1 = " + s);
				Log.d("error2", "error2 = " + s1);
		    }
		});
		
		mPushAgent.setDisplayNotificationNumber(0);
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
		    @Override
		    public void dealWithCustomAction(Context context, UMessage msg) {
//		        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//		        String message = msg.toString();
		     	String key = "";
		        String msgType = "";
				String paymentId = "";
		        for (Entry<String, String> entry : msg.extra.entrySet()) {
					Log.d("debug", "set" + msg.extra.entrySet());
					key = entry.getKey();
					if (!key.isEmpty() && key.equals("msgType")){
						msgType = entry.getValue();
					}

					if (!key.isEmpty() && key.equals("paymentId")){
						paymentId = entry.getValue();
					}
				}


//				kqwSpeechCompound.speaking("Hello word！");
		        intent = new Intent(MyApplication.this, PushMsgHandleA.class);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        intent.putExtra("title", msg.title);
		        intent.putExtra("message", msg.text);
		        intent.putExtra("type", msgType);
				intent.putExtra("paymentId", paymentId);
		        startActivity(intent);
		        
//		        if (value != null && !value.isEmpty()) {
//					if ("1".equals(value)) {
//							intent = new Intent(MyApplication.this, MainActivity.class);
//					        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					        intent.putExtra("title", msg.title);
//					        intent.putExtra("message", msg.text);
//
//					        
//					        startActivity(intent);		   
//					}else if ("2".equals(value)) {
//						intent = new Intent(MyApplication.this, PushMsgHandleA.class);
//				        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				        intent.putExtra("title", msg.title);
//				        intent.putExtra("message", msg.text);
//					}
//				}
		           

		    }
//		    @Override
//		    public void openActivity(Context arg0, UMessage arg1) {
//		    	super.openActivity(arg0, arg1);
//		    	String message = arg1.toString();
//		        intent = new Intent(MyApplication.this, PushMsgHandleA.class);
//		        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		        intent.putExtra("message", message);
//		        startActivity(intent);
//
//		    }
		};

		UmengMessageHandler messageHandler = new UmengMessageHandler() {
			@Override
			public Notification getNotification(Context context, UMessage msg) {
				String key = "";
				String voiceMsg = "";
				for (Entry<String, String> entry : msg.extra.entrySet()) {
					Log.d("debug", "set" + msg.extra.entrySet());
					key = entry.getKey();
					if (!key.isEmpty() && key.equals("voiceMsg")){
						voiceMsg = entry.getValue();
						break;
					}


				}

				Log.d("voiceMsg",voiceMsg);
				if (voiceMsg != null && !voiceMsg.isEmpty()){
					if (voiceRemindSwitch == false){
						XunFeiUtils.getInstance().stop();
					}else {
						XunFeiUtils.getInstance().speak(voiceMsg);
					}

				}


				//默认为0，若填写的builder_id并不存在，也使用默认。
				return super.getNotification(context, msg);

			}

			/*@Override
			public void dealWithCustomMessage(Context context, UMessage uMessage) {
				super.dealWithCustomMessage(context, uMessage);
				XunFeiUtils.getInstance().speak(uMessage.title + uMessage.text);

			}*/
		};
		mPushAgent.setMessageHandler(messageHandler);
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}



	public static RequestQueue getHttpQueues() {
		return queues;

	}

	/**
	 * 检查返回的Response header中有没有session
	 * 
	 * @param responseHeaders
	 *            Response Headers.
	 */
	public final void checkSessionCookie(Map<String, String> responseHeaders) {
		if (responseHeaders.containsKey(SET_COOKIE_KEY)
				&& responseHeaders.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
			String cookie = responseHeaders.get(SET_COOKIE_KEY);
			if (cookie.length() > 0) {
				String[] splitCookie = cookie.split(";");
				String[] splitSessionId = splitCookie[0].split("=");
				cookie = splitSessionId[1];
				SharedPreferences.Editor prefEditor = preferences.edit();
				prefEditor.putString(SESSION_COOKIE, cookie);
				prefEditor.commit();
			}
		}
	}


  private static 	String sesstionString ;
	/**
	 * 添加session到Request header中
	 */
	public final void addSessionCookie(Map<String, String> requestHeaders) {
		String sessionId = preferences.getString(SESSION_COOKIE, "");
		Log.d("sessionId", "sessionId=" + sessionId);
		if (sessionId.length() > 0) {
			StringBuilder builder = new StringBuilder();
			builder.append(SESSION_COOKIE);
			builder.append("=");
			builder.append(sessionId);
			if (requestHeaders.containsKey(COOKIE_KEY)) {
				builder.append("; ");
				builder.append(requestHeaders.get(COOKIE_KEY));
			}
			sesstionString = builder.toString();
			requestHeaders.put(COOKIE_KEY, builder.toString());
		}
	}


	public static String getSessionid(){
		return  sesstionString;
	}


	/**
	 * 监听activity并加到集合中
	 */
	private void registerActivity() {
//		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler() );  //全局异常捕获处理   测试打包必须需要开启

		registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle bundle) {
				super.onActivityCreated(activity, bundle);
				if(activity!=null) {
					activitylist.add(activity);
					Log.e("我进来了", "aa");
				}
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				super.onActivityDestroyed(activity);
				if(activity!=null) {
					activitylist.remove(activity);
				}
			}
		});
	}


	public static void exit(int code) {
		Iterator<Activity> iterator = activitylist.iterator();
		while (iterator.hasNext()) {
			Activity next = iterator.next();
			next.finish();
			iterator.remove();
		}
		activitylist.clear();
		Intent intent =new Intent();
		switch (code){
			case 0:
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				break;
			case 1:
//				intent = new Intent(MyApplication.mContext, LoginA.class);
				break;
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		System.exit(0);
	}




   //分包需要重写这个方法  继承 MultiDexApplication
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}


	public static String getProcessName(Context cxt, int pid) {
		ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
			if (procInfo.pid == pid) {
				return procInfo.processName;
			}
		}
		return null;
	}


}

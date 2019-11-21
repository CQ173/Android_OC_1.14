package com.huoniao.oc.versionupdate;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huoniao.oc.R;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ExitApplication;
import com.huoniao.oc.util.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 *@author lpj
 *@date 2017-1
 *
 */

public class UpdateManager
{
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 更新状态检查结果 */
	private static final int CHECK_FINISH = 3;
	
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	private String apkPath = "";//apk路径
	private String versionInfo;//版本信息
	private String serviceCode;//网络上的版本号
	private String descContent;//网络上的版本更新内容
	/* 是否可更新 */
	private boolean canUpdate = false;
	private boolean status = false; //是否强制更新
	private Intent intent;
	private String updateMode = "";
	private String serviceVersionName;

	/* 版本更新回调接口 */
	public interface versionUpdateNotifyListenner{
		void UpdateCheckResult(boolean result, boolean bNeedUpdate);
		void version(String versionCode,String versonName);
	} 
	
	private versionUpdateNotifyListenner mListenner;

//	private void setCanUpdateStatus(versionUpdateNotifyListenner listenner){
//		this.mListenner = listenner;
//	}
	
	private void NotifyUpdateCheckResult(boolean result, boolean bNeedUpdate) {
		if (null == mListenner){ 	
			return;
		}
		mListenner.UpdateCheckResult(result, bNeedUpdate);
	}
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case CHECK_FINISH:	
				versionInfo = msg.obj.toString();
				if (versionInfo == null || versionInfo.isEmpty()) {
					NotifyUpdateCheckResult(false, false);
					return;
				}
				
				int versionCode = getVersionCode(mContext);
				JSONObject object = null;  
		        try {  
		            object = new JSONObject(versionInfo);  
		        } catch (JSONException e) {  
		            e.printStackTrace(); 
		            NotifyUpdateCheckResult(false, false);
		            return;
		        } 
		       
		        serviceCode = object.optString("versionId");
				serviceVersionName = object.optString("version");
				if(mListenner!=null &&serviceCode!=null && serviceVersionName!=null){
					mListenner.version(serviceCode,serviceVersionName);
				}
				status = object.optBoolean("force");
				apkPath = object.optString("vesionPath");
				descContent = object.optString("desc");
				if (serviceCode != null && !serviceCode.isEmpty()){
					int serverCode = Integer.parseInt(serviceCode);
//				int serviceCode = getServiceVersion();

					// 根据网络上的version.xml，获取网络上的版本号
					// 版本判断
					if (serverCode > versionCode)
					{
						canUpdate = true;
						NotifyUpdateCheckResult(true, true);

					}else {
						canUpdate = false;
						NotifyUpdateCheckResult(true, false);
					}
				}

				
				break;
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate()
	{
//		if (isUpdate())
		if (canUpdate == true && status == true)
		{
			updateMode = "force";
			versionUpdateDialog(updateMode);
//			showDownloadDialog();
		} else if (canUpdate == true && status == false)
		{
			updateMode = "Optional";
			versionUpdateDialog(updateMode);
			// 显示提示对话框
//			showNoticeDialog();
			
		}else {
			Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.huoniao.oc", 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}

	
	/**
	 * 获取网络上版本信息
	 * 
	 *
	 * @return
	 */
	public void getServiceVersionInfo(versionUpdateNotifyListenner listenner){
		this.mListenner = listenner;
//		setCanUpdateStatus(listenner);
		new Thread(new Runnable() {
		
		@Override
		public void run() {
			
			String jsonResult = HttpUtils.getJsonContent(Define.VERSION_URL);
			Message msg = new Message();  
            msg.obj = jsonResult;  
            msg.what = CHECK_FINISH;  
            mHandler.sendMessage(msg);  
			}
		}).start();
		
		
	}
	
	 /** 
     * JSON解析方法 ,解析网络上的version信息,顺便返回版本号
     */  
//	protected int JSONAnalysis(String string) {
//		 JSONObject object = null;  
//	        try {  
//	            object = new JSONObject(string);  
//	        } catch (JSONException e) {  
//	            e.printStackTrace();  
//	        } 
//	        String versionCode = "";
//	        versionCode = object.optString("versionId");
//			status = object.optBoolean("force");
//			apkPath = object.optString("vesionPath");
//			int serverCode = Integer.parseInt(versionCode);
//			return serverCode; 
//	}

	/**
	 * 版本更新dialog
	 * @param mode
     */
	 private void versionUpdateDialog(final String mode){

		 LayoutInflater inflater = LayoutInflater.from(mContext);
		 View view = inflater.inflate(R.layout.versionupdata_dialog, null);
		 TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		 if (descContent != null && !descContent.isEmpty()){
			 tv_content.setText(descContent);
		 }
		 TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		 if ("force".equals(mode)){//强制更新
			 tv_cancel.setText("退出");
		 }else if ("Optional".equals(mode)){//选择更新
			 tv_cancel.setText("下次再说");
		 }
		 TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		 AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		 builder.setCancelable(false);
		 builder.setView(view);



		 final AlertDialog dialog = builder.create();//获取dialog

		 dialog.show();
		 tv_cancel.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View view) {
				 if ("force".equals(mode)){//强制更新
					 ExitApplication.getInstance().exit();
				 }else if ("Optional".equals(mode)){//选择更新
					 dialog.dismiss();
				 }
			 }
		 });

		 tv_confirm.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View view) {

					 intent = new Intent();
					 intent.setAction("android.intent.action.VIEW");
					 Uri content_url = Uri.parse("https://a.app.qq.com/o/simple.jsp?pkgname=com.huoniao.oc");
					 intent.setData(content_url);
//				 	 dialog.dismiss();
					 mContext.startActivity(intent);


			 }
		 });





	 }
	
	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (status == true) {
					Field field;
					try {
						field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						// 将mShowing变量设为false，表示对话框已关闭
						field.set(dialog, true);
						dialog.dismiss();
						cancelUpdate = true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else {
					try {
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						// 将mShowing变量设为false，表示对话框已关闭
						field.set(dialog, false);
						dialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//				dialog.dismiss();
//				// 设置取消状态
//				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 下载文件
		downloadApk();
		
		
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author lpj
	 * @date 2017-1
	 *
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					String apkUrl = Define.VERSION_URL + apkPath;
					URL url = new URL(apkUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(3000);
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
//					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, "oc.apk" + serviceCode);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
//				}else {
//					
//					Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
//				}
			}		
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
				
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, "oc.apk" + serviceCode);
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}

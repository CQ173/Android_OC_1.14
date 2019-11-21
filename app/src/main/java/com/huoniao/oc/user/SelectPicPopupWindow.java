package com.huoniao.oc.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.util.TransformationImageUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SelectPicPopupWindow extends Activity implements OnClickListener {
	private int permissionsFlag = 0;   //成功  用户是否拒绝了权限    1拒绝  2接受 权限
	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private LinearLayout layout;
	private Intent intent;
	private static final int SCALE = 5;//照片缩小比例
	//下面这句指定调用相机拍照后的照片存储的路径
//	File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
	File tempFile = new File(Environment.getExternalStorageDirectory() + "/image.jpg");
	String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/image/certificates.jpg";
	File dirFile = new File(sdCardDir);  //目录转化成文件夹
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		intent = getIntent();
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);//照相
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);//相册
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

		layout = (LinearLayout) findViewById(R.id.pop_layout);

		// 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
		layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
						Toast.LENGTH_SHORT).show();
			}
		});
		// 添加按钮监听
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
		checkPermission(this);
	}

	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_photo:
/*//			if(permissionsFlag==2) {
				try {
					//拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
					//有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
					startActivityForResult(intent, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}*/
			cameraClick();
			break;
		case R.id.btn_pick_photo:
			/*try {
				//选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
				//有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
				Intent intent = new Intent();
				intent.setType("image*//*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE); 
				startActivityForResult(intent, 2);
			} catch (ActivityNotFoundException e) {

			}*/
			photoClick();
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 拍照
	 */
	public void cameraClick() {

		try {
			String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/register";
			File dirFile = new File(sdCardDir);  //目录转化成文件夹
			if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
				dirFile.mkdirs();
			}                          //文件夹有啦，就可以保存图片啦
			// 在SDcard的目录下创建图片文,以当前时间为其命名
		File	file = new File(sdCardDir, "image.jpg");

			//拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
			//有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	/**
	 * 从相册中选择
	 */
	public void photoClick() {
		//选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
		//有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, 2);
	}



	private void saveCodeImage(Bitmap bitmap) {


		FileOutputStream out = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
		{    // 获取SDCard指定目录下

			if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
				dirFile.mkdirs();
			}                          //文件夹有啦，就可以保存图片啦
//			File file = new File(sdCardDir);// 在SDcard的目录下创建图片文,以当前时间为其命名

			try {
				out = new FileOutputStream(dirFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(dirFile);
			intent.setData(uri);
			MyApplication.mContext.sendBroadcast(intent);  //通知相册 有图片更新
//			Toast.makeText(MyApplication.mContext, "保存已经至" + Environment.getExternalStorageDirectory() + "/OC/" + "目录文件夹下", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(MyApplication.mContext, "未检测到sd卡", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		/*Compres compres = new Compres();*/
		switch (requestCode) {
		// 如果是调用相机拍照时
		case 1:
//			startPhotoZoom(Uri.fromFile(tempFile), 150);
//			startPhotoZoom(Uri.fromFile(tempFile));
			/*Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpg");
			Bitmap photoBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
			saveBitmapFile(tempFile, photoBitmap);
			intent = new Intent();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFile);
			setResult(1,intent);*/
			if (resultCode == RESULT_OK) {
				String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/register/image.jpg";
				intent = new Intent();
				intent.putExtra("absolutePath", sdCardDir);
				setResult(1, intent);
				finish();
			}
			break;
		// 如果是直接从相册获取
		case 2:

			if (data != null && resultCode == RESULT_OK) {

				final Uri mImageCaptureUri = data.getData();
				//返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				if (mImageCaptureUri != null) {

		    	String	absolutePath = TransformationImageUtils.getRealFilePath(this, mImageCaptureUri);
					 intent = new Intent();
					intent.putExtra("absolutePath",absolutePath);
					setResult(1,intent);
					finish();

				}
			}

			/*if (data != null && resultCode == RESULT_OK) {

				final Uri mImageCaptureUri = data.getData();
				//返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
				if (mImageCaptureUri != null) {

				final String 	absolutePath = TransformationImageUtils.getRealFilePath(this, mImageCaptureUri);
					final String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/register";
					File dirFile = new File(sdCardDir);  //目录转化成文件夹
					if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
						dirFile.mkdirs();
					}
					ThreadCommonUtils.runOnBackgroundThread(new Runnable() {
						@Override
						public void run() {
							copyFile(absolutePath,sdCardDir+"/image.jpg");
							setResult(1);
							finish();
						}
					});


			   	}
			}
			break;*/


		case 3:
			 Log.e("zoom", "begin2");
			if (data != null){ 
				setPicToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}


	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}


	//Bitmap对象保存为图片文件
	public void saveBitmapFile(File file, Bitmap bitmap){
//	            File file=new File("/mnt/sdcard/pic/01.jpg");//将要保存图片的路径
	            try {
	                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
	                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
	                    bos.flush();
	                    bos.close();
	            } catch (IOException e) {
	                    e.printStackTrace();
	            }
	}
	/** 
	 * 图片质量压缩方法 
	 * 
	 * @param image 
	 * @return 
	 */  
	private Bitmap compressImage(Bitmap image) {  
	  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
	    int options = 90;  
	  
	    while (baos.toByteArray().length / 1024 > 1024) { // 循环判断如果压缩后图片是否大于1024kb,大于继续压缩  
	        baos.reset(); // 重置baos即清空baos  
	        image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中  
	        options -= 10;// 每次都减少10  
	    }  
	    Log.d("debug", "baos=" + baos.toByteArray().length);
	    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片  
//	    bitmap.recycle();
	    return bitmap;  
	}
	
//	private void startPhotoZoom(Uri uri, int size) {
	private void startPhotoZoom(Uri uri) {
		 Log.e("zoom", "begin");
		Intent intent = new Intent("com.android.camera.action.CROP");
//		Intent intent = new Intent();
		 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {  
             String url=getPath(this,uri);  
             intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");  
         }else{  
             intent.setDataAndType(uri, "image/*");  
         }  
//		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "false");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1.7);

		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 425);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
//		intent.putExtra("return-data", false);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFile);
		Log.e("zoom", "begin1");
		startActivityForResult(intent, 3);
	}

	//保存裁剪之后的图片数据	
	private void setPicToView(Intent data) {
		
		//选择完或者拍完照后会在这里处理，然后我们继续使用setResult返回Intent以便可以传递数据和调用
				if (data.getExtras() != null)
					intent.putExtras(data.getExtras());
				if (data.getData()!= null)
					intent.setData(data.getData());
				setResult(1, intent);
				finish();
				
	}
	//保存裁剪之后的图片数据	
//		private void setPicToView2(Uri uri) {
//			
//			//选择完或者拍完照后会在这里处理，然后我们继续使用setResult返回Intent以便可以传递数据和调用
////			Log.d(TAG, "CHOOSE_BIG_PICTURE: data = " + data);
//			//it seems to be null 
//			if(uri != null){   
//				Bitmap bitmap = decodeUriAsBitmap(uri);
//				//decode bitmap   
//				imageView.setImageBitmap(bitmap); 
//				} 
//			
//					
//		}
		
//		private Bitmap decodeUriAsBitmap(Uri uri){ 
//			Bitmap bitmap = null; 
//			try {   
//				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)); 
//				} catch (FileNotFoundException e) {   
//					e.printStackTrace();   
//					return null; } 
//			return bitmap; 
//		}
		
	// 获得照片的文件名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	 @SuppressLint("NewApi")  
     public static String getPath(final Context context, final Uri uri) {  

         final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  

         // DocumentProvider  
         if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
             // ExternalStorageProvider  
             if (isExternalStorageDocument(uri)) {  
                 final String docId = DocumentsContract.getDocumentId(uri);  
                 final String[] split = docId.split(":");  
                 final String type = split[0];  

                 if ("primary".equalsIgnoreCase(type)) {  
                     return Environment.getExternalStorageDirectory() + "/" + split[1];  
                 }  

             }  
             // DownloadsProvider  
             else if (isDownloadsDocument(uri)) {  
                 final String id = DocumentsContract.getDocumentId(uri);  
                 final Uri contentUri = ContentUris.withAppendedId(  
                         Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  

                 return getDataColumn(context, contentUri, null, null);  
             }  
             // MediaProvider  
             else if (isMediaDocument(uri)) {  
                 final String docId = DocumentsContract.getDocumentId(uri);  
                 final String[] split = docId.split(":");  
                 final String type = split[0];  

                 Uri contentUri = null;  
                 if ("image".equals(type)) {  
                     contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
                 } else if ("video".equals(type)) {  
                     contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
                 } else if ("audio".equals(type)) {  
                     contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
                 }  

                 final String selection = "_id=?";  
                 final String[] selectionArgs = new String[] {  
                         split[1]  
                 };  

                 return getDataColumn(context, contentUri, selection, selectionArgs);  
             }  
         }  
         // MediaStore (and general)  
         else if ("content".equalsIgnoreCase(uri.getScheme())) {  
             // Return the remote address  
             if (isGooglePhotosUri(uri))  
                 return uri.getLastPathSegment();  

             return getDataColumn(context, uri, null, null);  
         }  
         // File  
         else if ("file".equalsIgnoreCase(uri.getScheme())) {  
             return uri.getPath();  
         }  

         return null;  
     }  

     /** 
      * Get the value of the data column for this Uri. This is useful for 
      * MediaStore Uris, and other file-based ContentProviders. 
      * 
      * @param context The context. 
      * @param uri The Uri to query. 
      * @param selection (Optional) Filter used in the query. 
      * @param selectionArgs (Optional) Selection arguments used in the query. 
      * @return The value of the _data column, which is typically a file path. 
      */  
     public static String getDataColumn(Context context, Uri uri, String selection,  
             String[] selectionArgs) {  

         Cursor cursor = null;  
         final String column = "_data";  
         final String[] projection = {  
                 column  
         };  

         try {  
             cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
                     null);  
             if (cursor != null && cursor.moveToFirst()) {  
                 final int index = cursor.getColumnIndexOrThrow(column);  
                 return cursor.getString(index);  
             }  
         } finally {  
             if (cursor != null)  
                 cursor.close();  
         }  
         return null;  
     }  


     /** 
      * @param uri The Uri to check. 
      * @return Whether the Uri authority is ExternalStorageProvider. 
      */  
     public static boolean isExternalStorageDocument(Uri uri) {  
         return "com.android.externalstorage.documents".equals(uri.getAuthority());  
     }  

     /** 
      * @param uri The Uri to check. 
      * @return Whether the Uri authority is DownloadsProvider. 
      */  
     public static boolean isDownloadsDocument(Uri uri) {  
         return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
     }  

     /** 
      * @param uri The Uri to check. 
      * @return Whether the Uri authority is MediaProvider. 
      */  
     public static boolean isMediaDocument(Uri uri) {  
         return "com.android.providers.media.documents".equals(uri.getAuthority());  
     }  

     /** 
      * @param uri The Uri to check. 
      * @return Whether the Uri authority is Google Photos. 
      */  
     public static boolean isGooglePhotosUri(Uri uri) {  
         return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
     }

	/*
权限校验
 */
	public void checkPermission(Activity activity) {

		if (Build.VERSION.SDK_INT >= 23) {

			int readSdcard = activity.checkSelfPermission(Manifest.permission.CAMERA);

			int requestCode = 0;
			ArrayList<String> permissions = new ArrayList<>();

			if (readSdcard != PackageManager.PERMISSION_GRANTED) {
				requestCode = 1;
				permissions.add(Manifest.permission.CAMERA);
			}

			if (requestCode > 0) {
				String[] permission = new String[permissions.size()];
				activity.requestPermissions(permissions.toArray(permission), requestCode);
				return;
			}

		}


	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 1:
				if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//					permissionsFlag = 1;
					Toast.makeText(this, "拍照权限未授权！", Toast.LENGTH_SHORT).show();
				} else {
//					permissionsFlag = 2;
					Toast.makeText(this, "恭喜！拍照权限授权成功！", Toast.LENGTH_SHORT).show();
				}
				break;

		}
	}
	
}

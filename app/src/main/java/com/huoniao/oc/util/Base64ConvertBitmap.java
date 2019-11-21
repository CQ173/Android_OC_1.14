package com.huoniao.oc.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64ConvertBitmap {
	/** 
	 * bitmap转为base64 
	 * @param bitmap 
	 * @return 
	 */  
	public static String bitmapToBase64(Bitmap bitmap) {  
	  
	    String result = null;  
	    ByteArrayOutputStream baos = null;  
	    try {  
	        if (bitmap != null) {  
	            baos = new ByteArrayOutputStream();
		   	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

				baos.flush();
	            baos.close();  
	  
	            byte[] bitmapBytes = baos.toByteArray();
	            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);  
	        }  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (baos != null) {  
	                baos.flush();  
	                baos.close();  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return result;  
	}  
	  
	/** 
	 * base64转为bitmap 
	 * @param base64Data 
	 * @return 
	 */  
	public static Bitmap base64ToBitmap(String base64Data) {  
	    byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
	}




	/**
	 * 文件转base64字符串
	 * @param file
	 * @return
	 */
	public static String fileToBase64(File file) {
		String base64 = null;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] bytes = new byte[in.available()];
			int length = in.read(bytes);
			base64 = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return base64;
	}


	/**
	 * base64字符串转文件
	 * @param base64
	 * @return
	 */
	public static File base64ToFile(String base64) {
		File file = null;
		String fileName = "/OC/img/i.jpg";
		FileOutputStream out = null;
		try {
			// 解码，然后将字节转换为文件
			// 获取SDCard指定目录下
			String sdCardDir = Environment.getExternalStorageDirectory() + "/OC/.cache";
			File dirFile = new File(sdCardDir);  //目录转化成文件夹
			if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
				dirFile.mkdirs();
			}                          //文件夹有啦，就可以保存图片啦
			file = new File(sdCardDir,  System.currentTimeMillis() +"");// 在SDcard的目录下创建图片文,以当前时间为其命名

			byte[] bytes = Base64.decode(base64, Base64.DEFAULT);// 将字符串转换为byte数组
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			byte[] buffer = new byte[1024];
			out = new FileOutputStream(file);
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				bytesum += byteread;
				out.write(buffer, 0, byteread); // 文件写操作
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (out!= null) {
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}
}

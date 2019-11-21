package com.huoniao.oc.util;

import android.os.Environment;

public class SDCardUtil {

	public static boolean hasSDCard() {
		return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED;
	}
	
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}
	
}

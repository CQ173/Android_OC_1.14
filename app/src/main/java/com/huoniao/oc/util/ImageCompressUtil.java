package com.huoniao.oc.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageCompressUtil {
	public static final String CONTENT = "content";
	public static final String FILE = "file";

	/**
	 * 图片压缩参数
	 * 
	 * @author Administrator
	 * 
	 */
	public static class CompressOptions {
		public static final int DEFAULT_WIDTH = 500;
		public static final int DEFAULT_HEIGHT = 800;

		public int maxWidth = DEFAULT_WIDTH;
		public int maxHeight = DEFAULT_HEIGHT;
		/**
		 * 压缩后图片保存的文件
		 */
		public File destFile;
		/**
		 * 图片压缩格式,默认为jpg格式
		 */
		public CompressFormat imgFormat = CompressFormat.JPEG;

		/**
		 * 图片压缩比例
		 */
		public int quality = 100;

		public Uri uri;
	}

	Bitmap bitmap = null;
	Bitmap destBitmap = null;
	public   Bitmap compressFromFilePath(Context context,
										CompressOptions compressOptions, String absolutePath) {

		// uri指向的文件路径
	//	String filePath = getFilePath(context, compressOptions.uri);
		String filePath = absolutePath;

		if (null == filePath) {
			return null;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// Bitmap temp = BitmapFactory.decodeFile(filePath, options);

		int actualWidth = options.outWidth;
		int actualHeight = options.outHeight;

			Bitmap bitmap2 = BitmapFactory.decodeFile(filePath,options);//此时返回bm为空
		   int w=	options.outWidth;
		   int h = options.outHeight;
		int desiredWidth =0;
		int desiredHeight =0;
		if(w<h){
			 desiredWidth = getResizedDimension(compressOptions.maxWidth,
					compressOptions.maxHeight, actualWidth, actualHeight);
			  desiredHeight = getResizedDimension(compressOptions.maxHeight,
					compressOptions.maxWidth, actualHeight, actualWidth);
		}else{
			desiredWidth = getResizedDimension(compressOptions.maxHeight,
					compressOptions.maxWidth, actualWidth, actualHeight);
			desiredHeight = getResizedDimension(compressOptions.maxWidth,
					compressOptions.maxHeight, actualHeight, actualWidth);
		}


		options.inSampleSize = computeSampleSize(options, -1, 500*800);

		options.inJustDecodeBounds = false;




		  destBitmap = BitmapFactory.decodeFile(filePath, options);

		bitmap = Bitmap.createScaledBitmap(destBitmap, desiredWidth,
				desiredHeight, true);
		destBitmap.recycle();
destBitmap.getByteCount();

		/*if (destBitmap.getWidth() > desiredWidth
				|| destBitmap.getHeight() > desiredHeight) {
			bitmap = Bitmap.createScaledBitmap(destBitmap, desiredWidth,
					desiredHeight, true);
			destBitmap.recycle();
		} else {
			bitmap = destBitmap;
		}


		if (null != compressOptions.destFile) {
			compressFile(compressOptions, bitmap);
		}*/

		return bitmap;
	}


	private void compressFile(CompressOptions compressOptions, Bitmap bitmap) {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(compressOptions.destFile);
		} catch (FileNotFoundException e) {
			Log.e("ImageCompress", e.getMessage());
		}

		bitmap.compress(compressOptions.imgFormat, compressOptions.quality,
				stream);
	}

	private static int findBestSampleSize(int actualWidth, int actualHeight,
			int desiredWidth, int desiredHeight) {
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}

		return (int) n;
	}

	private static int getResizedDimension(int maxPrimary, int maxSecondary,
			int actualPrimary, int actualSecondary) {

		if (maxPrimary == 0 && maxSecondary == 0) {
			return actualPrimary;
		}


		if (maxPrimary == 0) {
			double ratio = (double) maxSecondary / (double) actualSecondary;
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0) {
			return maxPrimary;
		}

		double ratio = (double) actualSecondary / (double) actualPrimary;
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary) {
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}

	/**
	 * 获取文件的路径
	 * 
	 * @param
	 * * @return
	 */
	private String getFilePath(Context context, Uri uri) {

		String filePath = null;

		if (CONTENT.equalsIgnoreCase(uri.getScheme())) {

			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { Images.Media.DATA }, null, null, null);

			if (null == cursor) {
				return null;
			}

			try {
				if (cursor.moveToNext()) {
					filePath = cursor.getString(cursor
							.getColumnIndex(Images.Media.DATA));
				}
			} finally {
				cursor.close();
			}
		}

		// 从文件中选择
		if (FILE.equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}

		return filePath;
	}


	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {

		int initialSize = computeInitialSampleSize(options, minSideLength,

				maxNumOfPixels);



		int roundedSize;

		if (initialSize <= 8) {

			roundedSize = 1;

			while (roundedSize < initialSize) {

				roundedSize <<= 1;

			}

		} else {

			roundedSize = (initialSize + 7) / 8 * 8;

		}


		return roundedSize;


	}


	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {

		double w = options.outWidth;

		double h = options.outHeight;


		int lowerBound = (maxNumOfPixels == -1) ? 1 :

				(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

		int upperBound = (minSideLength == -1) ? 128 :

				(int) Math.min(Math.floor(w / minSideLength),

						Math.floor(h / minSideLength));



		if (upperBound < lowerBound) {

			return lowerBound;

		}



		if ((maxNumOfPixels == -1) &&	(minSideLength == -1)) {

			return 1;

		} else if (minSideLength == -1) {

			return lowerBound;

		} else {

			return upperBound;

		}

	}

}
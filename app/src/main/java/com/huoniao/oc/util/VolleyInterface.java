package com.huoniao.oc.util;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import android.content.Context;

public abstract class VolleyInterface {
	public Context mContext;
	public static Listener<String> mListener;
	public static ErrorListener mErrorListener;
	
	public VolleyInterface(Context context, Listener<String> listener, ErrorListener errorListener){
		this.mContext = context;
		this.mListener = listener;
		this.mErrorListener = errorListener;
	}
	
	public abstract void onMySuccess(String result);
	public abstract void onMyError(VolleyError error);
	
	public Listener<String> loadingListener(){
		mListener = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				onMySuccess(response);
				
			}
		};
		
		return mListener;
		
	}
	
	public ErrorListener errorListener(){
		mErrorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				onMyError(error);
				
			}
		};
		return mErrorListener;
		
	}
}

package com.huoniao.oc.util;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import com.android.volley.toolbox.ImageRequest;
import com.huoniao.oc.MyApplication;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class SessionImageRequest extends ImageRequest{

	public SessionImageRequest(String url, Listener<Bitmap> listener, int maxWidth, int maxHeight, Config decodeConfig,
			ErrorListener errorListener) {
		super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
		
	}
	
	@Override
	protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
		 Response<Bitmap> superResponse = super  
                 .parseNetworkResponse(response);
		MyApplication.newInstance().checkSessionCookie(response.headers);
		
//		Bitmap jb =null;
//        try {
//            String parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return superResponse;
        
	}
	
	@Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        MyApplication.newInstance().addSessionCookie(headers);

        return headers;
    }
}

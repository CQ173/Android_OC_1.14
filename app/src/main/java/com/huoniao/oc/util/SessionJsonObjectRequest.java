package com.huoniao.oc.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionJsonObjectRequest extends JsonObjectRequest {



    public SessionJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        super(method, url, jsonRequest, listener, errorListener);

    }

    public SessionJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }




    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        // 检查本地是否有session.如果没有，就将header中的session数据保存到本地
        MyApplication.newInstance().checkSessionCookie(response.headers);

        JSONObject jb =null;
        try {
            String parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            jb = new JSONObject(parsed);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Response.success(jb, HttpHeaderParser.parseCacheHeaders(response));
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

//    @Override
//    protected VolleyError parseNetworkError(VolleyError volleyError) {
//    	return super.parseNetworkError(volleyError);
//    }

}
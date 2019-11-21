package com.huoniao.oc.util;

import java.util.HashMap;
import java.util.Map;


import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.huoniao.oc.MyApplication;

import android.content.Context;

public class VolleyRequest {
	public static StringRequest stringRequest;
	public static Context context;
	/**
	 *  使用Post方式返回JsonObject类型的请求结果数据
	 *
	 *  new JsonObjectRequest(int method,String url,JsonObject jsonObject,Listener listener,ErrorListener errorListener)
	 *  method：请求方式，Get请求为Method.GET，Post请求为Method.POST
	 *  url：请求地址
	 *  JsonObject：Json格式的请求参数。如果使用的是Get请求方式，请求参数已经包含在url中，所以可以将此参数置为null
	 *  listener：请求成功后的回调
	 *  errorListener：请求失败的回调
	 */
	private static void volleyJSONObjectPost(Context context, String url, JSONObject jsonObject) {
//	    String url = "http://www.kuaidi100.com/query";
//	    Map<String,String> map = new HashMap<>();
//	    map.put("type","yuantong");
//	    map.put("postid","229728279823");
//	    //将map转化为JSONObject对象
//	    JSONObject jsonObject = new JSONObject(map);
		
		String mString = null;
	   JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jsonObject, new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject response) {//jsonObject为请求成功返回的Json格式数据
			Gson gson = new Gson();
			
		}
	}, new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {//jsonObject为请求失败返回的Json格式数据
			// TODO Auto-generated method stub
			
		}
	});
	
	            
	    //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
	    request.setTag("testPost");
	    //将请求加入全局队列中
	    MyApplication.getHttpQueues().add(request);
	}
	
	/**
     * 使用Post方式返回String类型的请求结果数据
     * 
     *  new StringRequest(int method,String url,Listener listener,ErrorListener errorListener)
     *  method：请求方式，Get请求为Method.GET，Post请求为Method.POST
     *  url：请求地址
     *  listener：请求成功后的回调
     *  errorListener：请求失败的回调
     */
    private static void volleyStringRequestPost() {
        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
        StringRequest request = new StringRequest(Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {//response为请求返回的字符串数据
				// TODO Auto-generated method stub
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		}){
        	@Override
        	protected Map<String, String> getParams() throws AuthFailureError {
        		
        		
        		 Map<String,String> map = new HashMap<String,String>();
                 //将请求参数名与参数值放入map中
                 map.put("tel","15850781443");
                 return map;
        	}
        };
                
                  
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }
    
    /**
     * 二次封装
     */
    
    public static void RequestGet(Context mContext, String url, String tag, VolleyInterface vif){
    	MyApplication.getHttpQueues().cancelAll(tag);
    	stringRequest = new StringRequest(Method.GET, url, vif.loadingListener(), vif.errorListener());
    	stringRequest.setTag(tag);
    	MyApplication.getHttpQueues().add(stringRequest);
    	MyApplication.getHttpQueues().start();
    }
    
    public static void RequestPost(Context mContext, String url, String tag, final Map<String, String> params, 
    		VolleyInterface vif){
    	MyApplication.getHttpQueues().cancelAll(tag);
    	stringRequest = new StringRequest(url, vif.loadingListener(), vif.errorListener()){
    		@Override
    		protected Map<String, String> getParams() throws AuthFailureError {
    			// TODO Auto-generated method stub
    			return params;
    		}
    	};
    	
    	stringRequest.setTag(tag);
    	MyApplication.getHttpQueues().add(stringRequest);
    	MyApplication.getHttpQueues().start();
    }
}

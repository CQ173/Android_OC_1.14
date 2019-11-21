package com.huoniao.oc.volleynet;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.util.SessionJsonObjectRequest;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/5/5.
 */

public class VolleyNetCommon {

   protected RequestQueue requestQueue = null;


    public RequestQueue getRequestQueue(){
        if(requestQueue != null) {
            return requestQueue;
        }
        return  null;
    }

    public VolleyNetCommon() {
        if(requestQueue==null){
           requestQueue = Volley.newRequestQueue(MyApplication.mContext);
               SsX509TrustManager.allowAllSSL();   //允许https 访问
           }
    }



    /**
     *  图片请求封装
     * @param url    网络链接
     * @param volleyAbstract  封装了 请求成功失败
     * @param maxWidth
     * @param maxHeight
     * @return
     */

    public ImageRequest imageRequest(String url, VolleyAbstract volleyAbstract, int maxWidth, int maxHeight, String tag){

        ImageRequest imageRequest = new ImageRequest(url,volleyAbstract.responseListener(),
                maxWidth, maxHeight, Bitmap.Config.RGB_565, volleyAbstract.errorListener()){
            //重写getHeaders 默认的key为cookie，value则为localCookie
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


        };

        //解决重复请求后台的问题
        imageRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        10*1000,//默认超时时间，应设置一个稍微大点儿的
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        imageRequest.setTag(tag); //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        return imageRequest;
    }

    /**
     *  图片请求封装重载
     *  @param method  请求方式
     * @param url    网络链接
     * @param volleyAbstract  封装了 请求成功失败
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public ImageRequest imageRequest(int method, String url, VolleyAbstract volleyAbstract, int maxWidth, int maxHeight, String tag, final Map<String, String> map){

        ImageRequest imageRequest = new ImageRequest(method, url, volleyAbstract.responseListener(),
                maxWidth, maxHeight, Bitmap.Config.RGB_565, volleyAbstract.errorListener()){
            //重写getHeaders 默认的key为cookie，value则为localCookie
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        //解决重复请求后台的问题
        imageRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        10*1000,//默认超时时间，应设置一个稍微大点儿的
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        imageRequest.setTag(tag); //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        return imageRequest;
    }

    /**
     * 把请求添加到队列
     * @param request
     */
    public void addQueue(Request request){
          requestQueue.add(request);

    }




    //字符串请求
    public  StringRequest stringRequest(int method,String url,VolleyAbstract volleyAbstract){
        StringRequest stringRequest= new StringRequest(method,url,volleyAbstract.responseListener(),volleyAbstract.errorListener());

        return  stringRequest;
    }

    //json,  repeatRequest表示超时后是否需要重复请求（true为需要，false为不需要）
    public JsonObjectRequest jsonObjectRequest(int method, String url, JSONObject jsonRequest,VolleyAbstract volleyAbstract,String tag, boolean repeatRequest) {

        SessionJsonObjectRequest jsonObjectRequest = new SessionJsonObjectRequest(method, url, jsonRequest, volleyAbstract.responseListener(), volleyAbstract.errorListener());

        // 解决重复请求后台的问题
        if (repeatRequest) {
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }else {
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                    0, // 默认最大尝试次数
                    0f));
        }

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        jsonObjectRequest.setTag(tag); //"outletsMyMessage"

        return jsonObjectRequest;
    }


    //  程序奔溃调用的          日志错误搜集方法
    public JsonObjectRequest jsonObjectRequest2(int method, String url, JSONObject jsonRequest,String tag) {

        SessionJsonObjectRequest jsonObjectRequest = new SessionJsonObjectRequest(method, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if(jsonObject ==null){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        // 解决重复请求后台的问题
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        jsonObjectRequest.setTag(tag); //"outletsMyMessage"

        return jsonObjectRequest;
    }

    /**
     *  文件下载请求封装
     * @param url    网络链接
     * @param volleyAbstract  封装了 请求成功失败
     * @return
     */

    public FileRequest fileRequest(String url, VolleyAbstract volleyAbstract, String tag){

        FileRequest fileRequest = new FileRequest(url,volleyAbstract.responseListener(),
                 volleyAbstract.errorListener()){
            //重写getHeaders 默认的key为cookie，value则为localCookie
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


        };

        //解决重复请求后台的问题
        fileRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        10*1000,//默认超时时间，应设置一个稍微大点儿的
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        fileRequest.setTag(tag); //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        return fileRequest;
    }

    /**
     *  文件下载请求封装重载
     *  @param method  请求方式
     * @param url    网络链接
     * @param volleyAbstract  封装了 请求成功失败
     * @return
     */
    public FileRequest fileRequest(int method , String url, VolleyAbstract volleyAbstract, String tag, final Map<String, String> map){

        FileRequest fileRequest = new FileRequest( method , url ,volleyAbstract.responseListener(), volleyAbstract.errorListener()){
            //重写getHeaders 默认的key为cookie，value则为localCookie
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        //解决重复请求后台的问题
        fileRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        10*1000,//默认超时时间，应设置一个稍微大点儿的
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        fileRequest.setTag(tag); //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        return fileRequest;
    }

}

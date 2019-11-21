package com.huoniao.oc.volleynet;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;



/**
 * Created by CHENQING on 2019/5/23.
 * 自定义的Volley请求文件网络请求
 */
public class FileRequest extends Request<byte[]> {

    private final Response.Listener<byte[]> mListener;
    private static String filename;
    /**
     * Decoding lock so that we don't decode more than one image at a time (to avoid OOM's)
     */
    private static final Object sDecodeLock = new Object();

    /**
     * Creates a new image request, decoding to a maximum specified width and
     * height. If both width and height are zero, the image will be decoded to
     * its natural size. If one of the two is nonzero, that dimension will be
     * clamped and the other one will be set to preserve the image's aspect
     * ratio. If both width and height are nonzero, the image will be decoded to
     * be fit in the rectangle of dimensions width x height while keeping its
     * aspect ratio.
     *
     * @param url           URL of the image
     * @param listener      Listener to receive the decoded bitmap
     * @param errorListener Error listener, or null to ignore errors
     */
    public FileRequest(String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);

        mListener = listener;
    }

    public FileRequest( int method ,String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        mListener = listener;
    }


    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        // Serialize all decode on a global lock to reduce concurrent heap usage.
        synchronized (sDecodeLock) {
            try {
                if (response.data == null) {
                    return Response.error(new ParseError(response));
                } else {
                    Log.i("getdownloadresponse" , response.headers.get("Content-Disposition"));
                    filename = response.headers.get("Content-Disposition");
                    return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
                }
            } catch (OutOfMemoryError e) {
                VolleyLog.e("Caught OOM for %d byte image, url=%s", response.data.length, getUrl());
                return Response.error(new ParseError(e));
            }
        }
    }
    //获取文件名称并传给页面
    public static String getfilename(){
        return filename;
    }


    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

}
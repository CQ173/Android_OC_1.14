package com.huoniao.oc.volleynet;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.user.LoginA;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/5.
 */

public abstract class VolleyAbstract {

    private Response.ErrorListener mErrorListener;
    private Response.Listener mResponseLinListener;
    private BaseActivity activity;

    public VolleyAbstract(BaseActivity activity){
      this.activity = activity;
    }

    // 请求 响应成功
    public  Response.Listener responseListener(){

         mResponseLinListener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
             if(o==null){
                 Toast.makeText(MyApplication.mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                 PdDismiss();
                 pdExceptionDismiss();
                 return;
             }
                volleyResponse(o);  //这个因为写了好多了就不干掉了    这个可以去转换成图片
                Log.e("当前线程是：", Thread.currentThread().getName() + "ss");
                if (o instanceof JSONObject) {
                    JSONObject json = (JSONObject) o;
                    if (json == null) {
                        Toast.makeText(MyApplication.mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        PdDismiss();
                        pdExceptionDismiss();
                        return;
                    }
                    try {
                        String code = json.getString("code");
                        String message = json.getString("msg");
                        if ("0".equals(code)) {
                      /*  Gson gson = new Gson();
                          CashBean cashBean = gson.fromJson(json.toString(), CashBean.class);*/
                            // dataEntityList = cashBean.getData();

                            netVolleyResponese(json);
                            PdDismiss();   //这个必须放到下面  不然会影响其他

                        } else if ("46000".equals(code)) {
                            PdDismiss();
                            pdExceptionDismiss();
                            Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MyApplication.mContext, LoginA.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApplication.mContext.startActivity(intent);
                            activity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        } else {
                            PdDismiss();
                            pdExceptionDismiss();
                          //  Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                            errorMessages(message);
                        }

                    } catch (JSONException e) {
                        PdDismiss();
                        pdExceptionDismiss();
                        e.printStackTrace();
                    }
                }
            }
        };
        return  mResponseLinListener;
    }

    //额外消息错误提示
    protected void errorMessages(String message) {

    }

    //请求响应失败
    public Response.ErrorListener  errorListener(){
            mErrorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    pdExceptionDismiss();
                    PdDismiss();
                     volleyError(volleyError);

                }
            };
        return mErrorListener;
    }





    /**
     * 请求后获取结果进行处理
     * @param o
     */
    public abstract void volleyResponse(Object o);


    /**
     * 请求出错后的处理
     * @param volleyError
     */
    public abstract void volleyError(VolleyError volleyError);

    /**
     * 直接获取最后响应的结果
     * @param json
     */
    protected abstract void netVolleyResponese(JSONObject json);

    /**
     * 关闭加载弹框
     */
    protected abstract void PdDismiss();

    /**
     * 网络异常关闭加载框
     */
    protected void pdExceptionDismiss(){

    };

}

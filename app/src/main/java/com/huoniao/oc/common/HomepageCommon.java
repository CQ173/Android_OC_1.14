package com.huoniao.oc.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.adapter.Gallery_adapter;
import com.huoniao.oc.bean.CarouseImgBean;
import com.huoniao.oc.bean.NotificationBean;
import com.huoniao.oc.bean.OutletsMyLogBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.outlets.OutletsMyLogA;
import com.huoniao.oc.sort.Order;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.user.Message2A;
import com.huoniao.oc.useragreement.RegisterAgreeA;
import com.huoniao.oc.util.Base64ConvertBitmap;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.SmartImageView;
import com.huoniao.oc.util.TextSwitcherView;
import com.huoniao.oc.util.ToastUtils;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;

/**
 * Created by Administrator on 2017/10/30.
 * 使用方法   调用轮播图    homepageCommon.transferControl(main_gallery,main_lin);
                             homepageCommon.carouselFigure("1");
                             homepageCommon.create();
              调用头像    homepageCommon.transferControlHeaderImage(iv_logo);  //参数 图片控件
                      homepageCommon.setImageHeader(photoimgFlag);   //相对图片路径
                      homepageCommon.create();   //完成回调

 */

public class HomepageCommon {
    private List<Object> li;  //轮播图的集合
    List<String> tagLsit = new ArrayList<String>();
    private File licenceFile;
    protected  List<String> premissionsList = new ArrayList<>(); //权限集合
    private final String  lunBoImagTag  ="lunBoImag";//轮播图标识
    private final String  headerImgTag  ="headerImg";//头像图片标识
    private List<CarouseImgBean> carouseImgList2 = new ArrayList<>();
    private  Gallery_adapter gallery_adapter;
    private int current_circle = 0;
    public Runnable timeadv;
    private int count;
    public static Handler handler = new Handler();   //防止内存泄漏
    protected  VolleyNetCommon volleyNetCommon;
    protected CustomProgressDialog cpd;
    private SmartImageView main_gallery;
    private LinearLayout main_lin;
    private ImageView iv_logo;
    private BaseActivity baseActivity;
    private ArrayList<String> notifiContentList;
    private Intent intent;
    private List<CarouseImgBean> carouseImgList;
    private String linkUrl;
    private User user;

    public HomepageCommon(BaseActivity baseActivity){

         this.baseActivity = baseActivity;
        volleyNetCommon = new VolleyNetCommon();
        if(cpd == null) {
            cpd = new CustomProgressDialog(baseActivity, "正在加载中...", R.drawable.frame_anim);
            cpd.setCancelable(false);//设置进度条不可以按退回键取消
            cpd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
        }
        gallery_adapter = new Gallery_adapter(MyApplication.mContext);


    }


    /**
     * 传递控件过来 如果你要获取轮播图  创建这个对象必须先要调用这个方法
     * @param main_gallery 轮播图控件
     * @param main_lin 轮播图小圆点
     */
      public  void transferControl(SmartImageView main_gallery, LinearLayout main_lin){
          this.main_gallery = main_gallery;
          this.main_lin = main_lin;
      }

    /**
     * 传递头像控件  如果你要获取头像  创建这个对象必须要先调用这个方法
     */
    public void transferControlHeaderImage( ImageView iv_logo){
        this.iv_logo = iv_logo;
    }

    /**
     *  图片轮播图 头像 处理回调
     */
   public  void  create(){
       //获取图片回调
       setImgResultLinstener(new BaseActivity.ImgResult() {
           @Override
           public void getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr) {
               switch (tag){
                   case lunBoImagTag:
                       glideSetImg(file, null, lunBoImagTag, i, linkUrlStr);
                       break;
                   case headerImgTag:
                       glideSetImg(file, iv_logo, headerImgTag, 0, "");
                       break;
               }
               /* if(i!=-1){
                    documentInformationBeenList.get(i).imageSrc =imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }*/
           }
       });
   }

    /**
     * 从服务器获取轮播图路径
     */
    public void carouselFigure(String type) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pType",type);  //轮播图类型
            String URL = Define.URL + "sys/getPictureSrcByType";
            JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, URL, jsonObject, new VolleyAbstract(baseActivity) {
                @Override
                protected void netVolleyResponese(JSONObject json) {
                    if (li != null){
                        li.clear();
                    }
                    try {
                        JSONArray jsonArray = json.getJSONArray("data");
                        li = new ArrayList<Object>();
                        carouseImgList = new ArrayList<CarouseImgBean>();
                        final List<Integer> luoBoNumber = new ArrayList<Integer>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CarouseImgBean carouseImgBean = new CarouseImgBean();
                            JSONObject luoBoObj = (JSONObject) jsonArray.get(i);
                            String pimgpath = luoBoObj.optString("pimgpath");// 图片路径
                            int sort = luoBoObj.optInt("sort");// 图片排序
                            String remarks = luoBoObj.optString("remarks");// beizhu
                            // lunbio
                            linkUrl = luoBoObj.optString("url");

                            carouseImgBean.setImgUrl(pimgpath);
                            carouseImgBean.setLevel(sort);
                            carouseImgBean.setRemarks(remarks);
                            carouseImgBean.setLinkUrl(linkUrl);
//							li.add(Define.IMG_URL+pimgpath);
                            carouseImgList.add(carouseImgBean);
                        }

                        Collections.sort(carouseImgList, new Order());
                        for (int i = 0; i < carouseImgList.size(); i++) {
                            int level = carouseImgList.get(i).getLevel();
                            Log.d("debug", "排序号：" + level);
                        }
                        for (int i = 0; i < carouseImgList.size(); i++) {
//							li.add(Define.IMG_URL + carouseImgList.get(i).getImgUrl());
                            tagLsit.add("luoBo"+i);
//							li.add(carouseImgList.get(i).getImgUrl());
                            try {
                                linkUrl = carouseImgList.get(i).getLinkUrl();
//								getDocumentImage2(carouseImgList.get(i).getImgUrl(), "luoBo"+i, carouseImgList.get(i).getLevel());
                                getDocumentImage3(carouseImgList.get(i).getImgUrl(), lunBoImagTag, carouseImgList.get(i).getLevel(), false, linkUrl);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//							setCarouselFigure(li, main_gallery, main_lin);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void PdDismiss() {

                }



                @Override
                public void volleyResponse(Object o) {

                }

                @Override
                protected void errorMessages(String message) {
                    super.errorMessages(message);

                }

                @Override
                public void volleyError(VolleyError volleyError) {

                    ToastUtils.showToast(MyApplication.mContext, MyApplication.mContext.getResources().getString(R.string.netError));

                }
            },"carouselFigure", true);
            volleyNetCommon.addQueue(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setImageHeader(String photoimgFlag) {
        if(photoimgFlag == null || photoimgFlag.isEmpty()){
            try {
                Glide.with(MyApplication.mContext).load(R.drawable.applogo).asBitmap().centerCrop()
                        .into(new BitmapImageViewTarget(iv_logo) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(MyApplication.mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                iv_logo.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

            try {
//				getDocumentImage2(photoimgFlag, "headerImg", 0);
                getDocumentImage3(photoimgFlag, headerImgTag, 0, false, "");
            } catch (Exception e) {
                e.printStackTrace();
            }


            //获取图片回调
            setImgResultLinstener(new BaseActivity.ImgResult() {
                @Override
                public void getImageFile(File file, String imgUrl, String tag, int i, String linkUrlStr) {
                    switch (tag){
                        case headerImgTag:

                            glideSetImg(file, iv_logo, headerImgTag, 0, "");

                            break;

                    }
               /* if(i!=-1){
                    documentInformationBeenList.get(i).imageSrc =imgUrl; //目前没有什么作用
                    documentInformationBeenList.get(i).imageFile = file;
                    commonAdapter.notifyDataSetChanged();
                }*/


                }
            });

        }

    }




    /**
     * 获取服务器图片,并将图片显示在控件上
     * @param imgUrl
     *
     * @param i
     * @param linkUrlStr 轮播图链接
     * @throws Exception
     */
    protected void getDocumentImage3(final String imgUrl, final String tag, final int i, boolean thumbnail, final String linkUrlStr) throws Exception {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imageSrc", imgUrl);
            if(thumbnail) {
                jsonObject.put("abbreviation", "1");
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JsonObjectRequest abb = volleyNetCommon.jsonObjectRequest(Request.Method.POST, Define.URL + "fb/getImgBase64BySrc", jsonObject, new VolleyAbstract(baseActivity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                //	Toast.makeText(BaseActivity.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                Log.e("abb", json.toString());
                String imgString = json.optString("data");
                licenceFile = Base64ConvertBitmap.base64ToFile(imgString);
//			;	imageView.setImageBitmap(licenceBitmap)
                if(imgResult != null){
                    imgResult.getImageFile(licenceFile,imgUrl,tag,i, linkUrlStr);
                }

            }

            @Override
            protected void PdDismiss() {

            }


        }, "documentImage", true);
        volleyNetCommon.addQueue(abb);

    }


    private void glideSetImg(File file, final ImageView imageView, String tag, int position, String linkUrlStr){
        if ("headerImg".equals(tag)){

            //设置圆角图片
            try {
                Glide.with(MyApplication.mContext).load(file).asBitmap().centerCrop()
                        .into(new BitmapImageViewTarget(imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(MyApplication.mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageView.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //设置普通图片
            if ("lunBoImag".equals(tag)){
                CarouseImgBean carouseImgBean = new CarouseImgBean();
                carouseImgBean.setLevel(position);
//						carouseImgBean.setBitmap(licenceBitmap);
                carouseImgBean.setLinkUrl(linkUrlStr);
                carouseImgBean.setFile(file);
                carouseImgList2.add(carouseImgBean);

//				}

                if(tagLsit.size()==carouseImgList2.size()){
                    Collections.sort(carouseImgList2, new Order());
                    for (int i = 0; i < carouseImgList2.size(); i++) {
//						li.add(carouseImgList2.get(i).getBitmap());
                        li.add(carouseImgList2.get(i).getFile());
                    }
                    setCarouselFigure(main_gallery, main_lin,li);
                }


            }
        }


    }


    // 设置轮播图
    private void setCarouselFigure(SmartImageView gallery, final LinearLayout dianLayout, final List<Object> li){
        if(li ==null || li.size()<0){
            return;
        }
        gallery.setAdapter(gallery_adapter);
        gallery_adapter.setList(li);
        setCircle();

        // 设置滚动图片的时候，对应小圆点的图片切换
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                View v = dianLayout.getChildAt(position);
                View cuview = dianLayout.getChildAt(current_circle);

                if (v != null && cuview != null) {
                    ImageView pointView = (ImageView) v;
                    ImageView curpointView = (ImageView) cuview;
                    curpointView.setBackgroundResource(R.drawable.circle_transparent);
                    pointView.setBackgroundResource(R.drawable.circle_white);
                    current_circle = position;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CarouseImgBean CarouseImgBean = carouseImgList2.get(i);
                String linkUrl = CarouseImgBean.getLinkUrl();
                user = (User) readObject(MyApplication.mContext, "loginResult");
                String loginName = user.getLoginName();
                if (linkUrl != null && !linkUrl.isEmpty()){
                    Intent intent = new Intent(MyApplication.mContext, RegisterAgreeA.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", linkUrl + "?loginName=" + loginName);
                    MyApplication.mContext.startActivity(intent);
                }
            }
        });
        //
        timeadv = new Runnable() {

            @Override
            public void run() {
                // 获取当前的图片是哪一张图片，图片的序号，
                count = main_gallery.getSelectedItemPosition();
                // 当前滚动的图片序号大于多有的图片的数量，就跳转到第一张图片，否则就跳转到下一张图片
                if (count + 1 >= li.size()) {
                    count = 0;
                } else {
                    count = count + 1;
                }
                main_gallery.setSelection(count);
                handler.postDelayed(this, 4000);

            }
        };

        // 开始定时器，4000毫秒切换一次图片
        handler.postDelayed(timeadv, 4000);
    }



    // 设置滚动图片的小圆点
    private void setCircle() {
        for (int i = 0; i < li.size(); i++) {
            ImageView iv = new ImageView(MyApplication.mContext);
            // 循环创建小圆点，判断第一个小圆点为白色的，其他的都是透明的
            if (i == 0) {
                iv.setBackgroundResource(R.drawable.circle_white);
            } else {
                iv.setBackgroundResource(R.drawable.circle_transparent);
            }
            main_lin.addView(iv);

            // 设置小圆点的margin值
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1); // , 1�ǿ�ѡд��
            lp.setMargins(5, 10, 5, 10);
            iv.setLayoutParams(lp);
        }
    }





    /**
     * 获取首页通知列表
     *
     * @throws Exception
     */
    public void getMainNotificationList(final LinearLayout ll_noticeBar, final TextSwitcherView tv_notifications , final CustomProgressDialog cpd ) throws Exception {
        		cpd.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("showList", false);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = Define.URL + "fb/getShowNoticeList";


        SessionJsonObjectRequest mainNotificationRequest = new SessionJsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response == null) {
                    mainNotificationCpdClose(cpd,"1");
                    return;
                }
//						String data = response.toString();
//						if (data.isEmpty() || data == null) {
//							return;
//						}
//						Log.d("debug", "response =" + data);
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
//								int num = response.getInt("size");
                        mainNotificationCpdClose(cpd,"0");
                        JSONArray jsonArray = response.getJSONArray("data");

                        final List<NotificationBean> notificationData = new ArrayList<NotificationBean>();
                        notifiContentList = new ArrayList<String>();
//								num = Math.min(num, jsonArray.length());

                        if (notifiContentList != null){
                            notifiContentList.clear();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            NotificationBean notificationBean = new NotificationBean();

                            JSONObject notifiObj = (JSONObject) jsonArray.get(i);
                            String id = notifiObj.optString("id");// 通知id
                            String content = notifiObj.optString("content");// 通知内容
                            String title = notifiObj.optString("title");// 通知标题
                            String type = notifiObj.optString("type");//通知类型
//									String validDateStart = notifiObj.optString("validDateStart");//有效期开始时间
//									String validDateEnd = notifiObj.optString("validDateEnd");//有效期结束时间

                            notifiContentList.add(title);
                            notificationBean.setNotificationId(id);
                            notificationBean.setNotificationTitle(title);
                            notificationBean.setNotificationContent(content);
                            notificationBean.setNotificationType(type);
//									notificationBean.setStartTime(validDateStart);
//									notificationBean.setEndTime(validDateEnd);
                            notificationData.add(notificationBean);
                        }
                        if (notifiContentList.size() < 1){
                            ll_noticeBar.setVisibility(View.GONE);
                        }else {
                         //   ll_noticeBar.setVisibility(View.VISIBLE);
                            baseActivity.setPremissionShowHideView(Premission.FB_NOTICE_VIEW,ll_noticeBar);
                        }

                        tv_notifications.getResource(notifiContentList);
                        tv_notifications.setOnItemClickListener(new TextSwitcherView.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
//							            Toast.makeText(Main.this, "点击了 : " + notifiContentList.get(position-1), Toast.LENGTH_SHORT).show();
//							            NotificationBean nBean = notificationData.get(position);
//							            scrollNotificationDialog(nBean);
                                intent = new Intent(MyApplication.mContext, Message2A.class);
                                intent.putExtra("paidUpState", "message");
                                baseActivity.startActivityForResult(intent,101);
                                baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            }
                        });



                    } else if("46000".equals(responseCode)){
                        mainNotificationCpdClose(cpd,"1");
                        Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(MyApplication.mContext, LoginA.class);
                        baseActivity.startActivityIntent(intent);
                    } else {
                        mainNotificationCpdClose(cpd,"1");
                        Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exciption", "exciption =" + e.toString());
                    mainNotificationCpdClose(cpd,"1");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mainNotificationCpdClose(cpd,"1");
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("debug", "error = " + error.toString());

            }
        });
        // 解决重复请求后台的问题
        mainNotificationRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        mainNotificationRequest.setTag("mainNotificationRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(mainNotificationRequest);

    }




    /**
     * // 首页通知成功加载框关闭
     * @param cpd
     * @param state   0 为加载成功 自己根据业务是否需要关闭加载框  1加载失败
     *                 state等于0 需要 重写这个方法去关闭加载框
     */
    protected void mainNotificationCpdClose(CustomProgressDialog cpd, String state){
            if(state.equals("1")){
                cpd.dismiss();
            }
    }


    /**
     * 获取代售点'我的日志'
     *
     * @throws Exception
     */
    public void getOutletsLogData() throws Exception {
        cpd.show();
        cpd.setCustomPd("正在加载中...");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("limit", 20);
        String url = Define.URL + "user/userLogList";
        final List<OutletsMyLogBean> oltLogList = new ArrayList<OutletsMyLogBean>();
        SessionJsonObjectRequest myLogRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            cpd.dismiss();
                            Toast.makeText(MyApplication.mContext, "服务器数据异常！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("debug", "response =" + response.toString());
                        try {
                            String code = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(code)) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                OutletsMyLogBean oltLogBean = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    oltLogBean = new OutletsMyLogBean();

                                    JSONObject myLogObject = (JSONObject) jsonArray.get(i);
                                    String id = myLogObject.optString("id");
                                    String remoteAddr = myLogObject.optString("remoteAddr");
                                    String content = myLogObject.optString("content");
                                    String createDate = myLogObject.optString("createDate");
                                    oltLogBean.setContent(content);
                                    oltLogBean.setRemoteAddr(remoteAddr);
                                    oltLogBean.setCreateDate(createDate);
                                    oltLogBean.setId(id);

                                    Log.d("debug", oltLogBean.getCreateDate());
                                    oltLogList.add(oltLogBean);
                                }

//								Toast.makeText(Main.this, "解析数据成功!", Toast.LENGTH_SHORT).show();
//								ObjectSaveUtil.saveObject(Main.this, "logInfo", oltLogBean);
                                intent = new Intent(MyApplication.mContext, OutletsMyLogA.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("oltLogData", (Serializable) oltLogList);
                                intent.putExtras(bundle);
                                cpd.dismiss();
                                baseActivity.startActivityIntent(intent);

                            } else if ("46000".equals(code)) {
                                cpd.dismiss();
                                Toast.makeText(MyApplication.mContext, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                // ExitApplication.getInstance().exit();
                                intent = new Intent(MyApplication.mContext, LoginA.class);
                                baseActivity.startActivityIntent(intent);
                            } else {
                                cpd.dismiss();
                                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            cpd.dismiss();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cpd.dismiss();
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();

            }
        });
        // 解决重复请求后台的问题
        myLogRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        myLogRequest.setTag("outletsMyLog");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(myLogRequest);

    }





    private BaseActivity.ImgResult imgResult;
    public  void setImgResultLinstener(BaseActivity.ImgResult imgResultLinstener){
        this.imgResult = imgResultLinstener;
    }
    public  interface  ImgResult{
        void  getImageFile(File file, String imgUrl, String tag, int i);
    }

}

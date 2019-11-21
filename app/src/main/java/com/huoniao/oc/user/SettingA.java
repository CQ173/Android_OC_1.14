package com.huoniao.oc.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.admin.FeedbackA;
import com.huoniao.oc.bean.NotificationBean;
import com.huoniao.oc.bean.OutletsMyLogBean;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.outlets.NewMessageNoticeA;
import com.huoniao.oc.outlets.OutletsMyLogA;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.Premission;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.VersonCodeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.huoniao.oc.util.ObjectSaveUtil.readObject;


public class SettingA extends BaseActivity {

    @InjectView(R.id.qrcode_iv_back)
    ImageView qrcodeIvBack;              //返回键
    @InjectView(R.id.ll_log)
    LinearLayout llLog;            //日志
    // @InjectView(R.id.ll_system_notify)
    //LinearLayout llSystemNotify;    //系统通知
    @InjectView(R.id.ll_feedback_message)
    LinearLayout llFeedbackMessage;   //反馈消息
    @InjectView(R.id.ll_about)
    LinearLayout llAbout;    //关于

    @InjectView(R.id.v_log)
    View vLog;
    // @InjectView(R.id.v_system_notify)
    // View vSystemNotify;
    @InjectView(R.id.v_feedback_message)
    View vFeedbackMessage;
    @InjectView(R.id.tv_red_circle)
    TextView tvRedCircle;  //小红点
    @InjectView(R.id.tv_verson_newest)
    TextView tvVersonNewest; //消息通知
    @InjectView(R.id.ll_newMessage)
    LinearLayout llNewMessage;
    @InjectView(R.id.v_lineNewMessage)
    View vLineNewMessage;

    private ProgressDialog pd;
    private Intent intent;

    private User user;
    private String roleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        initWidget(); //操作控件
//        Log.i("ad",1/0 + "");
    }

    private void initWidget() {
        user = (User) readObject(SettingA.this, "loginResult");
        roleName = user.getRoleName();
        pd = new CustomProgressDialog(this, "正在加载中...", R.drawable.frame_anim);


        setPremissionShowHideView(Premission.FB_USERLOG_VIEW, llLog);    //是否有显示 我的日志 权限
        setPremissionShowHideView(Premission.FB_USERLOG_VIEW, vLog);    //是否有显示 我的日志 权限 线条


        // setPremissionShowHideView(Premission.FB_NOTICE_VIEW, llSystemNotify);  //  显示系统通知"通知列表"菜单权限
        //setPremissionShowHideView(Premission.FB_NOTICE_VIEW, vSystemNotify);  //  显示系统通知"通知列表"菜单权限线条
//        if (Define.OUTLETS.equals(LoginA.IDENTITY_TAG) || Define.OUTLETS.equals(PerfectInformationA.IDENTITY_TAG)
//                || Define.OUTLETS.equals(WXEntryActivity.IDENTITY_TAG) || Define.OUTLETS.equals(RegisterSuccessA.IDENTITY_TAG)) {
            setPremissionShowHideView(Premission.FB_WECHATPUBLICNOTICE_INDEX, llNewMessage);    //是否有显示 新消息通知 权限
            setPremissionShowHideView(Premission.FB_WECHATPUBLICNOTICE_INDEX, vLineNewMessage);    //是否有显示 新消息通知 权限 线条
//        }
        if (roleName != null && roleName.contains("系统管理员")) {  //只有管理员才有反馈信息   因为里面有处理我要反馈的消息
            setPremissionShowHideView(Premission.FB_FEEDBACK_VIEW, llFeedbackMessage);  //是否有反馈消息这个权限
            setPremissionShowHideView(Premission.FB_FEEDBACK_VIEW, vFeedbackMessage);  //是否有反馈消息这个权限 线条
        } else {
            llFeedbackMessage.setVisibility(View.GONE);
        }

        if (MyApplication.serviceCode != null && MyApplication.serviceVersionName != null) {
            if (Integer.parseInt(MyApplication.serviceCode) > VersonCodeUtils.getVersionCode(SettingA.this)) {
                tvRedCircle.setVisibility(View.VISIBLE);
                tvVersonNewest.setText("可更新至" + MyApplication.serviceVersionName);
            } else {
                tvRedCircle.setVisibility(View.GONE);
                tvVersonNewest.setText("已是最新版本");
            }
        } else {
            tvRedCircle.setVisibility(View.GONE);
            tvVersonNewest.setText("已是最新版本");
        }
    }

    @OnClick({R.id.qrcode_iv_back, R.id.ll_log, R.id.ll_feedback_message, R.id.ll_about, R.id.ll_newMessage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qrcode_iv_back:
                finish();
                break;
            case R.id.ll_log:
                try {
                    getOutletsLogData(); //获取日志
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
          /*  case R.id.ll_system_notify:  //系统通知
                try {
                    getSysNotificationList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;*/
            case R.id.ll_feedback_message:   //反馈
                if (roleName != null && roleName.contains("系统管理员")) {
                    intent = new Intent(SettingA.this, FeedbackA.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                } else {
                    intent = new Intent(SettingA.this, UserFeedbackInfoA.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }

                break;

          /*  case R.id.ll_exit_login:
                 treeIdList.clear();  //清除火车站层级树节点
                //清除用户保存的用户名密码
                SPUtils.put(SettingA.this, "password", "");
                startActivity(new Intent(this, LoginA.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                 onProfileSignOff();  //友盟账号退出登录
                finish();
                break;*/
            case R.id.ll_about:   //  关于
                startActivity(new Intent(SettingA.this, AboutA.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;

            case R.id.ll_newMessage:   //  新消息通知
                intent = new Intent(SettingA.this, NewMessageNoticeA.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }


    /**
     * 获取代售点'我的日志'
     *
     * @throws Exception
     */
    private void getOutletsLogData() throws Exception {
        pd.show();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("limit", 20);
        String url = Define.URL + "user/userLogList";
        final List<OutletsMyLogBean> oltLogList = new ArrayList<OutletsMyLogBean>();
        SessionJsonObjectRequest myLogRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            Toast.makeText(SettingA.this, "服务器数据异常！", Toast.LENGTH_SHORT).show();
                            cpd.dismiss();
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

//								Toast.makeText(MainActivity.this, "解析数据成功!", Toast.LENGTH_SHORT).show();
//								ObjectSaveUtil.saveObject(MainActivity.this, "logInfo", oltLogBean);
                                intent = new Intent(SettingA.this, OutletsMyLogA.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("oltLogData", (Serializable) oltLogList);
                                intent.putExtras(bundle);
                                pd.dismiss();
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                            } else if ("46000".equals(code)) {
                                pd.dismiss();
                                Toast.makeText(SettingA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                // ExitApplication.getInstance().exit();
                                intent = new Intent(SettingA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                pd.dismiss();
                                Toast.makeText(SettingA.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(SettingA.this, R.string.netError, Toast.LENGTH_SHORT).show();

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


    /**
     * 获取系统通知列表
     *
     * @throws Exception
     */
    private void getSysNotificationList() throws Exception {
        pd.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("validDateStart", "");
            jsonObject.put("validDateEnd", "");
            jsonObject.put("type", "");
            jsonObject.put("title", "");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String url = Define.URL + "fb/noticeList";


        SessionJsonObjectRequest SysNotificationRequest = new SessionJsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("debug", "response =" + response.toString());
                try {
                    String responseCode = response.getString("code");
                    String message = response.getString("msg");
                    if ("0".equals(responseCode)) {
//								int num = response.getInt("size");
                        JSONArray jsonArray = response.getJSONArray("data");

                        final List<NotificationBean> notificationData = new ArrayList<NotificationBean>();

//								num = Math.min(num, jsonArray.length());


                        for (int i = 0; i < jsonArray.length(); i++) {
                            NotificationBean notificationBean = new NotificationBean();

                            JSONObject notifiObj = (JSONObject) jsonArray.get(i);
                            String content = notifiObj.optString("content");// 通知内容
                            String id = notifiObj.optString("id");// 通知id
                            String title = notifiObj.optString("title");// 通知标题
                            String validDateStart = notifiObj.optString("validDateStart");//有效期开始时间
                            String validDateEnd = notifiObj.optString("validDateEnd");//有效期结束时间
                            String type = notifiObj.optString("type");//通知类型


                            notificationBean.setNotificationId(id);
                            notificationBean.setNotificationTitle(title);
                            notificationBean.setNotificationContent(content);
                            notificationBean.setNotificationType(type);
                            notificationBean.setStartTime(validDateStart);
                            notificationBean.setEndTime(validDateEnd);
                            notificationData.add(notificationBean);
                        }

                        intent = new Intent(SettingA.this, SystemNotificationA.class);
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("sysNotificationData", (Serializable) notificationData);

                        intent.putExtras(bundle);
                        pd.dismiss();
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);


                    } else if ("46000".equals(responseCode)) {
                        pd.dismiss();
                        Toast.makeText(SettingA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        intent = new Intent(SettingA.this, LoginA.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    } else {
                        pd.dismiss();
                        Toast.makeText(SettingA.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exciption", "exciption =" + e.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(SettingA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("debug", "error = " + error.toString());

            }
        });
        // 解决重复请求后台的问题
        SysNotificationRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        SysNotificationRequest.setTag("SysNotificationRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(SysNotificationRequest);

    }

}

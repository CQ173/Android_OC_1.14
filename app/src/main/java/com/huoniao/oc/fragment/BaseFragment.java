package com.huoniao.oc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.User;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.volleynet.VolleyAbstract;
import com.huoniao.oc.volleynet.VolleyNetCommon;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BaseFragment extends Fragment {
    public final static String TrainStationHomepageF = "TrainStationHomepageF";//火车站首页
    public final static String TrainStationOjiF = "TrainStationOjiF"; //火车站o计界面
    public final static String MineF = "MineF";//公用“我的”模块界面
    public final static String OutletsHomepageF = "OutletsHomepageF";//代售点首页
    public final static String OutletsOjiF = "OutletsOjiF";//代售点o计
    public final static String OutletsShoppingF = "OutletsShoppingF"; //代售点商城
    public final static String AdminHomepageF = "AdminHomepageF";  //管理员首页
    public final static String AdminOjiF = "AdminOjiF"; // 管理员O计界面
    public final static String AdminSubscriptionManagementF = "AdminSubscriptionManagementF"; //管理员汇缴界面
    public final static String FinancialHomepageF = "FinancialHomepageF";//会计和出纳 首页
    public final static String FinancialOjiF = "FinancialOjiF";//会计和出纳o计
    public final static String FinancialUserF = "FinancialUserF";  //会计和出纳 用户
    public final static String AdministrationHomepageF = "AdministrationHomepageF";  //铁总，铁分，车务段首页
    public final static String AdministrationPaymentF = "AdministrationPaymentF";  //铁总，铁分，车务段首页

    protected CustomProgressDialog cpd;
    protected VolleyNetCommon volleyNetCommon;
    protected List<String> premissionsList = new ArrayList<>(); //权限集合
    protected int[] colors = {R.color.colorPrimary, R.color.colorAccent, R.color.gray};
    protected BaseActivity baseActivity;


     @Override
     public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          if(cpd == null) {
               cpd = new CustomProgressDialog(getActivity(), "正在加载中...", R.drawable.frame_anim);
               cpd.setCancelable(false);//设置进度条不可以按退回键取消
               cpd.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
          }


        volleyNetCommon = new VolleyNetCommon();
        User loginResult = (User) ObjectSaveUtil.readObject(MyApplication.mContext, "loginResult");//获取登录时存储的相关 权限信息
        if (loginResult != null) {
            premissionsList = loginResult.getPremissions();
        }
        baseActivity = (BaseActivity) getActivity();

    }


    //抽取权限
    protected void setPremissionShowHideView(String premissionFlag, View view) {
        for (String premissions : premissionsList) {
            if (premissionFlag.equals(premissions)) {
                view.setVisibility(View.VISIBLE); //如果有这个权限就显示
                return;
            } else {
                view.setVisibility(View.GONE);//没有就隐藏
            }
        }
    }

    //跳转界面封装
    public void startActivityMethod(Class<?> cla) {
        baseActivity.startActivity(new Intent(MyApplication.mContext, cla));
        baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    //跳转界面
    public void startActivityIntent(Intent intent) {
        startActivity(intent);
        baseActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }


    /**
     * 请求网络
     *
     * @param url        网络链接
     * @param jsonObject 携带参数
     * @param tag        //标识哪个请求
     * @param controlOff 开始这个网络请求请求成功后 是不是需要关闭
     * @param repeatRequest 请求超时后是否需要重复请求（true为需要，false为不需要）
     */
    protected void requestNet(String url, JSONObject jsonObject, final String tag, final String pageNumber, final boolean controlOff, boolean repeatRequest) {
        cpd.show();
        if (volleyNetCommon == null) {
            volleyNetCommon = new VolleyNetCommon();
        }
        JsonObjectRequest jsonObjectRequest = volleyNetCommon.jsonObjectRequest(Request.Method.POST, url, jsonObject, new VolleyAbstract(baseActivity) {
            @Override
            public void volleyResponse(Object o) {

            }

            @Override
            public void volleyError(VolleyError volleyError) {
                Toast.makeText(MyApplication.mContext, R.string.netError, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void netVolleyResponese(JSONObject json) {
                dataSuccess(json, tag, pageNumber, controlOff);
            }

            @Override
            protected void PdDismiss() {
                if (controlOff) { //true 表示可以关闭
                    cpd.dismiss();
                }
                closeDismiss(cpd, tag, controlOff);
            }

            @Override
            protected void errorMessages(String message) {
                super.errorMessages(message);
                Toast.makeText(MyApplication.mContext, message, Toast.LENGTH_SHORT).show();
            }

            /**
             * 网络异常直接关闭加载框
             */
            @Override
            protected void pdExceptionDismiss() {
                super.pdExceptionDismiss();
                cpd.dismiss();
                loadControlExceptionDismiss(cpd, tag, controlOff);
            }
        }, tag, repeatRequest);
        volleyNetCommon.addQueue(jsonObjectRequest);
    }

    //子类重写 获取数据
    protected void dataSuccess(JSONObject json, String tag, String pageNumber, boolean controlOff) {

    }

    //关闭
    protected void closeDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {

    }

    //对有上拉加载框 在网络异常的时候关闭
    protected void loadControlExceptionDismiss(CustomProgressDialog cpd, String tag, boolean controlOff) {

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


}

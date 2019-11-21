package com.huoniao.oc.trainstation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.MyApplication;
import com.huoniao.oc.R;
import com.huoniao.oc.bean.StationWindowManageBean;
import com.huoniao.oc.user.LoginA;
import com.huoniao.oc.util.CommonAdapter;
import com.huoniao.oc.util.Define;
import com.huoniao.oc.util.ObjectSaveUtil;
import com.huoniao.oc.util.SessionJsonObjectRequest;
import com.huoniao.oc.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/11.
 */

public class WindowsAnchoredListA extends BaseActivity {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.et_searchContent)
    EditText etSearchContent;
    @InjectView(R.id.tv_select)
    TextView tvSelect;
    @InjectView(R.id.mListView)
    ListView mListView;
    private String clickTag = "";
    private String searchContent;
//    private ProgressDialog cpd;
    private Intent intent;
    private CommonAdapter<StationWindowManageBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windowrelationlist);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        clickTag = "firstList";
        intent = getIntent();
        searchContent = intent.getStringExtra("outletNumber");
//        if (searchContent != null && !searchContent.isEmpty()) {
//            etSearchContent.setText(searchContent);
//        }
        try {
            winAnchoredList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_select:
                searchContent = etSearchContent.getText().toString();
                if (searchContent.isEmpty()){
                    Toast.makeText(WindowsAnchoredListA.this, "请输入查询内容！", Toast.LENGTH_SHORT).show();
                    return;
                }
                clickTag = "selectList";
                try {
                    winAnchoredList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 窗口号挂靠列表
     * @throws Exception
     */
    private void winAnchoredList() throws Exception {
//        cpd = new CustomProgressDialog(WindowsAnchoredListA.this, "正在处理中...", R.anim.frame_anim);
        cpd.show();
        cpd.setCustomPd("正在处理中...");
        JSONObject jsonObject = new JSONObject();
        try {
            // operateType: 1 查看  2 添加  3 修改  4 取消申请  5 解除绑定
//            if ("firstList".equals(tag)) {
//                jsonObject.put("operateType", "1");
//            }else if ("selectList".equals(tag)){
                jsonObject.put("conditions", searchContent);
                jsonObject.put("operateType", "1");
//            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        String url = Define.URL + "fb/manageAgencyConnect";
        final List<StationWindowManageBean> winManageList = new ArrayList<StationWindowManageBean>();
        SessionJsonObjectRequest winAnchoredListRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            cpd.dismiss();
                            Toast.makeText(WindowsAnchoredListA.this, "服务器异常！", Toast.LENGTH_SHORT);
                            return;
                        }

                        Log.d("debug", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                cpd.dismiss();
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    StationWindowManageBean winManage = new StationWindowManageBean();

                                    JSONObject winManageObj = (JSONObject) jsonArray.get(i);
                                    String id = winManageObj.optString("id");//子代售点id
                                    String winNumber = winManageObj.optString("winNumber");//子代售点窗口号
                                    String agencyName = winManageObj.optString("agencyName");//子代售点名称
                                    String operatorName = winManageObj.optString("operatorName");//子代售点负责人姓名
                                    String operatorMobile = winManageObj.optString("operatorMobile");//子代售点负责人手机
                                    String operatorIdNum = winManageObj.optString("operatorIdNum");//子代售点负责人身份证号
                                    String operatorCardforntSrc = winManageObj.optString("operatorCardforntSrc");//子代售点身份证正面
                                    String operatorCardrearSrc = winManageObj.optString("operatorCardrearSrc");//子代售点身份证反面
                                    String staContIndexSrc = winManageObj.optString("staContIndexSrc");//子代售点合同首页
                                    String staContLastSrc = winManageObj.optString("staContLastSrc");//子代售点合同盖章尾页
                                    String fareAuthorizationSrc = winManageObj.optString("fareAuthorizationSrc");//子代售点授权书
                                    String auditState = winManageObj.optString("auditState");//子代售点审核状态
                                    String auditReason = winManageObj.optString("auditReason");//子代售点审核理由
                                    String officeAreaName = winManageObj.optString("officeAreaName");//主代售点区域名称
                                    String officeCorpName = winManageObj.optString("officeCorpName");//主代售点法人姓名
                                    String officeCorpMobile = winManageObj.optString("officeCorpMobile");//主代售点法人手机号
                                    String officeCorpIdNum = winManageObj.optString("officeCorpIdNum");//主代售点法人身份证号
                                    String officeOperatorName = winManageObj.optString("officeOperatorName");//主代售点负责人姓名
                                    String officeOperatorMoblie = winManageObj.optString("officeOperatorMoblie");//主代售点负责人手机
                                    String officeOperatorIdNum = winManageObj.optString("officeOperatorIdNum");//主代售点负责人身份证号
                                    String officeCode = winManageObj.optString("officeCode");//主代售点编号
                                    String officeName = winManageObj.optString("officeName");//主代售点名称
                                    String officeWinNumber = winManageObj.optString("officeWinNumber");//主代售点窗口号
                                    String updateDate = winManageObj.optString("updateDate");//操作时间


                                    winManage.setId(id);
                                    winManage.setWinNumber(winNumber);
                                    winManage.setAgencyName(agencyName);
                                    winManage.setOperatorName(operatorName);
                                    winManage.setOperatorMobile(operatorMobile);
                                    winManage.setOperatorIdNum(operatorIdNum);
                                    winManage.setOperatorCardforntSrc(operatorCardforntSrc);
                                    winManage.setOperatorCardrearSrc(operatorCardrearSrc);
                                    winManage.setStaContIndexSrc(staContIndexSrc);
                                    winManage.setStaContLastSrc(staContLastSrc);
                                    winManage.setFareAuthorizationSrc(fareAuthorizationSrc);
                                    winManage.setOfficeAreaName(officeAreaName);
                                    winManage.setOfficeCorpName(officeCorpName);
                                    winManage.setOfficeCorpMobile(officeCorpMobile);
                                    winManage.setOfficeCorpIdNum(officeCorpIdNum);
                                    winManage.setOfficeOperatorName(officeOperatorName);
                                    winManage.setOfficeOperatorMoblie(officeOperatorMoblie);
                                    winManage.setOfficeOperatorIdNum(officeOperatorIdNum);
                                    winManage.setOfficeCode(officeCode);
                                    winManage.setOfficeName(officeName);
                                    winManage.setOfficeWinNumber(officeWinNumber);
                                    winManage.setAuditState(auditState);
                                    winManage.setAuditReason(auditReason);
                                    winManage.setUpdateDate(updateDate);
                                    winManageList.add(winManage);
                                }
                                Log.d("debug","winManageList =" + winManageList.size());
                                adapter = new CommonAdapter<StationWindowManageBean>(WindowsAnchoredListA.this, winManageList, R.layout.windowrelation_item_layout) {
                                    @Override
                                    public void convert(ViewHolder holder, StationWindowManageBean stationWindowManageBean) {
                                        if (stationWindowManageBean != null){
                                            holder.setText(R.id.tv_outletName, stationWindowManageBean.getOfficeName())
                                                    .setText(R.id.tv_outletCode, stationWindowManageBean.getOfficeCode())
                                                    .setText(R.id.tv_windowNumber, stationWindowManageBean.getWinNumber())
                                                    .setText(R.id.tv_city, stationWindowManageBean.getOfficeAreaName());
                                        }

                                    }
                                };
                                mListView.setAdapter(adapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        StationWindowManageBean winManage = winManageList.get(i);
                                        ObjectSaveUtil.saveObject(WindowsAnchoredListA.this, "windowAnchoredInfo", winManage);
                                        intent = new Intent(WindowsAnchoredListA.this, WindowsAnchoredDetailsA.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                                    }
                                });



                            } else if ("46000".equals(responseCode)) {
                                cpd.dismiss();
                                Toast.makeText(WindowsAnchoredListA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(WindowsAnchoredListA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                cpd.dismiss();
                                Toast.makeText(WindowsAnchoredListA.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(WindowsAnchoredListA.this, R.string.netError, Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);


            }
        });
        // 解决重复请求后台的问题
        winAnchoredListRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        winAnchoredListRequest.setTag("winAnchoredListRequest");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(winAnchoredListRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueues().cancelAll("winAnchoredListRequest");
    }
}

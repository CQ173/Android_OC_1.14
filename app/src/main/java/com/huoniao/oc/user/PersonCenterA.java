package com.huoniao.oc.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huoniao.oc.BaseActivity;
import com.huoniao.oc.R;
import com.huoniao.oc.common.UserInfo;
import com.huoniao.oc.util.CustomProgressDialog;
import com.huoniao.oc.util.Premission;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 个人中心
 */
public class PersonCenterA extends BaseActivity {

    @InjectView(R.id.ll_person_message)
    LinearLayout llPersonMessage;   //个人信息
    @InjectView(R.id.ll_update_pwd)
    LinearLayout llUpdatePwd;     //修改密码

    @InjectView(R.id.qrcode_iv_back)
    ImageView qrcodeIvBack;





    private CustomProgressDialog pd;

    private String isBindQQ = "";//个人信息-用户是否绑定QQ
    private String isBindWx = "";//个人信息-用户是否绑定微信
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        ButterKnife.inject(this);
        pd = new CustomProgressDialog(this, "正在加载中", R.drawable.frame_anim);
        initData();
        initWeigt();
    }

    //对控件进行操作
    private void initWeigt() {
        setPremissionShowHideView(Premission.SYS_USER_INFO,llPersonMessage);  //判断是否有个人信息这个权限 ，有显示没有就隐藏
        setPremissionShowHideView(Premission.SYS_USER_MODIFYPWD,llUpdatePwd); //是否有修改密码这个权限
    }

    //加载数据
    private void initData() {

    }


    @OnClick({R.id.ll_person_message, R.id.ll_update_pwd,R.id.qrcode_iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_person_message:

                try {
                    UserInfo userInfo = new UserInfo();
                    userInfo.getUserInfo(PersonCenterA.this, pd, PersonalInformationA.class);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.ll_update_pwd:
                updatePwd();
                break;
            case R.id.qrcode_iv_back:
                finish();
                break;
        }
    }


    //修改密码
    public void updatePwd() {

        intent = new Intent(PersonCenterA.this, UpdataPasswordA.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }


   /* *//**
     * 获取代售点用户信息
     *
     * @throws Exception
     *//*
    private void getUserInfo() throws Exception {
        pd.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String url = Define.URL + "user/queryUserInfo";
        SessionJsonObjectRequest userInfoRequest = new SessionJsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            Toast.makeText(PersonCenterA.this, "服务器数据异常！", Toast.LENGTH_SHORT);
                            return;
                        }
                        Log.d("userInfo", "response =" + response.toString());
                        try {
                            String responseCode = response.getString("code");
                            String message = response.getString("msg");
                            if ("0".equals(responseCode)) {
                                Gson gson = new Gson();
                                User allUser = gson.fromJson( response.toString(), User.class);  //解析所有获取的数据

                                int num = response.getInt("size");
                                JSONArray jsonArray = response.getJSONArray("data");

                                num = Math.min(num, jsonArray.length());
                                User user = new User();

                                for (int i = 0; i < num; i++) {

                                    JSONObject userObj = (JSONObject) jsonArray.get(i);
                                    String id = userObj.optString("id");// 用户ID
                                    String loginName = userObj.optString("loginName");// 用户名
                                    String name = userObj.optString("name");// 用户姓名
                                    String mobile = userObj.optString("mobile");// 手机号码
                                    String balance = userObj.optString("balanceString");// 余额
                                    String minimum = userObj.optString("minimum");// 最低限额
                                    String userType = userObj.optString("userType");// 用户类型
                                    String auditState = userObj.optString("auditState");// 审核状态
                                    String auditReason = userObj.optString("auditReason");// 审核理由
                                    String roleNames = userObj.optString("roleNames");// 用户角色名
                                    String email = userObj.optString("email");// 邮箱
//									String isBindQQ = userObj.optString("isBindQQ");// 是否绑定QQ
//									String isBindWx = userObj.optString("isBindWx");// 是否绑定微信
                                    String remarks = userObj.optString("remarks");// 备注

                                    JSONObject office = userObj.optJSONObject("office");
                                    String officeId = office.optString("id");// 机构ID
                                    String userCode = office.optString("code");// 用户编号
                                    String area_name = "";
                                     if(office.optJSONObject("area")!=null){
                                           area_name = office.optJSONObject("area").getString("name")==null ? "": office.optJSONObject("area").getString("name");// 归属区域名称

                                     }
                                    String jurisArea = "";
                                    if(office.optJSONObject("jurisArea") != null) {
                                       jurisArea = office.optJSONObject("jurisArea").getString("name") == null ? "" : office.optJSONObject("jurisArea").getString("name");// 管辖区域名称
                                    }
                                    String orgName = office.optString("name");// 机构名称
                                    String corpName = office.optString("corpName");// 法人姓名
                                    String corpMobile = office.optString("corpMobile");// 法人手机
                                    String corpIdNum = office.optString("corpIdNum");// 法人身份证号
                                    String address = office.optString("address");// 联系地址
                                    String master = office.optString("master");// 联系人
                                    String contactPhone = office.optString("phone");// 联系人电话
                                    String winNumber = office.optString("winNumber");// 窗口号
                                    String agent = office.optString("agent");//第一代理人
                                    String agentType = office.optString("agentType");//代理人类型
                                    String operatorName = office.optString("operatorName");//负责人姓名
                                    String operatorMobile = office.optString("operatorMobile");//负责人手机
                                    String operatorIdNum = office.optString("operatorIdNum");//负责人身份证号
                                    String agentCompanyName = office.optString("agentCompanyName");//公司名称
                                    String corp_licence = office.optString("corpLicenceSrc");// 营业执照图片
                                    String corp_card_fornt = office.optString("corpCardforntSrc");// 法人身份证正面图片
                                    String corp_card_rear = office.optString("corpCardrearSrc");// 法人身份证反面图片
                                    String staContIndexSrc = office.optString("staContIndexSrc");// 车站合同首页
                                    String staContLastSrc = office.optString("staContLastSrc");// 车站合同尾页
                                    String staDepositSrc = office.optString("staDepositSrc");// 车站押金条
                                    String staDepInspSrc = office.optString("staDepInspSrc");// 车站押金年检证书
//									String bankFlowSrc = office.optString("bankFlowSrc");// 银行流水
                                    String operatorCardforntSrc = office.optString("operatorCardforntSrc");// 负责人身份证正面
                                    String operatorCardrearSrc = office.optString("operatorCardrearSrc");// 负责人身份证反面
                                    String fareAuthorizationSrc = office.optString("fareAuthorizationSrc");// 票款汇缴授权书

                                    user.setId(id);
                                    user.setLoginName(loginName);
                                    user.setName(name);
                                    user.setMobile(mobile);
                                    user.setBalance(balance);
                                    user.setMinimum(minimum);
                                    user.setUserType(userType);
                                    user.setAuditState(auditState);
                                    user.setAuditReason(auditReason);
                                    user.setRoleNames(roleNames);
                                    user.setEmail(email);
                                    user.setRemarks(remarks);
                                    user.setUserCode(userCode);
                                    user.setArea_name(area_name);
                                    user.setJurisArea(jurisArea);
                                    user.setOrgName(orgName);
                                    user.setCorpName(corpName);
                                    user.setCorpMobile(corpMobile);
                                    user.setCorpIdNum(corpIdNum);
                                    user.setAddress(address);
                                    user.setMaster(master);
                                    user.setContactPhone(contactPhone);
                                    user.setWinNumber(winNumber);
                                    user.setOfficeId(officeId);
                                    user.setCorp_licence(corp_licence);
                                    user.setCorp_card_fornt(corp_card_fornt);
                                    user.setCorp_card_rear(corp_card_rear);
                                    user.setAgent(agent);
                                    user.setAgentType(agentType);
                                    user.setAgentCompanyName(agentCompanyName);
                                    user.setStaContIndexSrc(staContIndexSrc);
                                    user.setStaContLastSrc(staContLastSrc);
                                    user.setStaDepositSrc(staDepositSrc);
                                    user.setStaDepInspSrc(staDepInspSrc);
                                    user.setOperatorCardforntSrc(operatorCardforntSrc);
                                    user.setOperatorCardrearSrc(operatorCardrearSrc);
                                    user.setFareAuthorizationSrc(fareAuthorizationSrc);
                                    user.setOperatorName(operatorName);
                                    user.setOperatorMobile(operatorMobile);
                                    user.setOperatorIdNum(operatorIdNum);

                                }

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject bindObj = (JSONObject) jsonArray.get(i);
                                    isBindQQ = bindObj.optString("isBindQQ");// 是否绑定QQ
                                    isBindWx = bindObj.optString("isBindWx");// 应扣总金额
                                }

                                ObjectSaveUtil.saveObject(PersonCenterA.this, "usetInfo", user);  //保存部分用户信息
                                ObjectSaveUtil.saveObject(PersonCenterA.this,"allUser",allUser);  //保存所有用户信息

                                intent = new Intent(PersonCenterA.this, PersonalInformationA.class);
                                intent.putExtra("isBindQQ", isBindQQ);
                                intent.putExtra("isBindWx", isBindWx);
                                pd.dismiss();
                                startActivity(intent);

                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else if ("46000".equals(responseCode)) {
                                pd.dismiss();
                                Toast.makeText(PersonCenterA.this, "登录过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(PersonCenterA.this, LoginA.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                            } else {
                                pd.dismiss();
                                Toast.makeText(PersonCenterA.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(PersonCenterA.this, "网络错误，请检查网络连接！", Toast.LENGTH_SHORT).show();
                Log.d("REQUEST-ERROR", error.getMessage(), error);
//						byte[] htmlBodyBytes = error.networkResponse.data;
//						Log.d("REQUEST-ERROR", new String(htmlBodyBytes), error);

            }
        });
        // 解决重复请求后台的问题
        userInfoRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, // 默认超时时间，应设置一个稍微大点儿的
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // 默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        userInfoRequest.setTag("userInfoTag");
        // 将请求加入全局队列中
        MyApplication.getHttpQueues().add(userInfoRequest);

    }*/
}
